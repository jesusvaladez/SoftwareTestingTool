package org.apache.ambari.server.state.stack;
import org.apache.commons.lang.StringUtils;
public class RepoUrlInfoCallable implements java.util.concurrent.Callable<java.util.Map<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult>> {
    private static final int LOOKUP_CONNECTION_TIMEOUT = 2000;

    private static final int LOOKUP_READ_TIMEOUT = 3000;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.stack.RepoUrlInfoCallable.class);

    private java.net.URI m_uri = null;

    private java.util.Set<org.apache.ambari.server.stack.StackModule> m_stacks = new java.util.HashSet<>();

    public RepoUrlInfoCallable(java.net.URI uri) {
        m_uri = uri;
    }

    public void addStack(org.apache.ambari.server.stack.StackModule stackModule) {
        m_stacks.add(stackModule);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult> call() throws java.lang.Exception {
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>>() {}.getType();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> latestUrlMap = null;
        java.util.Set<java.lang.String> ids = new java.util.HashSet<>();
        ids.addAll(com.google.common.collect.Collections2.transform(m_stacks, new com.google.common.base.Function<org.apache.ambari.server.stack.StackModule, java.lang.String>() {
            @java.lang.Override
            public java.lang.String apply(org.apache.ambari.server.stack.StackModule input) {
                return new org.apache.ambari.server.state.StackId(input.getModuleInfo()).toString();
            }
        }));
        java.lang.String stackIds = org.apache.commons.lang.StringUtils.join(ids, ',');
        java.lang.Long time = java.lang.System.nanoTime();
        try {
            if (m_uri.getScheme().startsWith("http")) {
                org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOOKUP_CONNECTION_TIMEOUT, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOOKUP_READ_TIMEOUT, null, null, null);
                org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOG.info("Loading latest URL info from {} for stacks {}", m_uri, stackIds);
                latestUrlMap = gson.fromJson(new java.io.InputStreamReader(streamProvider.readFrom(m_uri.toString())), type);
            } else {
                java.io.File jsonFile = new java.io.File(m_uri);
                if (jsonFile.exists()) {
                    org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOG.info("Loading latest URL info from file {} for stacks {}", m_uri, stackIds);
                    latestUrlMap = gson.fromJson(new java.io.FileReader(jsonFile), type);
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOG.info("Could not load the URI from {}, stack defaults will be used", m_uri);
            throw e;
        } finally {
            org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOG.info("Loaded URI {} for stacks {} in {}ms", m_uri, stackIds, java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(java.lang.System.nanoTime() - time));
        }
        java.util.Map<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult> result = new java.util.HashMap<>();
        if (null == latestUrlMap) {
            org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOG.error("Could not load latest data for URI {} and stacks {}", m_uri, stackIds);
            return result;
        }
        for (org.apache.ambari.server.stack.StackModule stackModule : m_stacks) {
            org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackModule.getModuleInfo());
            java.util.Map<java.lang.String, java.lang.Object> map = latestUrlMap.get(stackId.toString());
            if (null == map) {
                continue;
            }
            org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult res = new org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult();
            if (map.containsKey("manifests")) {
                @java.lang.SuppressWarnings("unchecked")
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> versionMap = ((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>) (map.get("manifests")));
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> versionEntry : versionMap.entrySet()) {
                    java.lang.String version = versionEntry.getKey();
                    java.util.Map<java.lang.String, java.net.URI> resolvedOsMap = resolveOsMap(stackModule, versionEntry.getValue());
                    res.addVersion(version, resolvedOsMap);
                }
            }
            if (map.containsKey("latest-vdf")) {
                @java.lang.SuppressWarnings("unchecked")
                java.util.Map<java.lang.String, java.lang.String> osMap = ((java.util.Map<java.lang.String, java.lang.String>) (map.get("latest-vdf")));
                java.util.Map<java.lang.String, java.net.URI> resolvedOsMap = resolveOsMap(stackModule, osMap);
                res.setLatest(resolvedOsMap);
            }
            result.put(stackModule, res);
        }
        return result;
    }

    private java.util.Map<java.lang.String, java.net.URI> resolveOsMap(org.apache.ambari.server.stack.StackModule stackModule, java.util.Map<java.lang.String, java.lang.String> osMap) {
        java.util.Map<java.lang.String, java.net.URI> resolved = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> osEntry : osMap.entrySet()) {
            java.lang.String uriString = osEntry.getValue();
            java.net.URI uri = org.apache.ambari.server.stack.StackModule.getURI(stackModule, uriString);
            if (null == uri) {
                org.apache.ambari.server.state.stack.RepoUrlInfoCallable.LOG.warn("Could not resolve URI {}", uriString);
            } else {
                resolved.put(osEntry.getKey(), uri);
            }
        }
        return resolved;
    }

    public static class RepoUrlInfoResult {
        private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.net.URI>> versions = new java.util.HashMap<>();

        private java.util.Map<java.lang.String, java.net.URI> latestVdf = new java.util.HashMap<>();

        private void addVersion(java.lang.String version, java.util.Map<java.lang.String, java.net.URI> vdfMap) {
            versions.put(version, vdfMap);
        }

        private void setLatest(java.util.Map<java.lang.String, java.net.URI> latestMap) {
            latestVdf = latestMap;
        }

        public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.net.URI>> getManifest() {
            return versions;
        }

        public java.util.Map<java.lang.String, java.net.URI> getLatestVdf() {
            return latestVdf;
        }
    }
}