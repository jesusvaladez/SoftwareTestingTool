package org.apache.ambari.server.state.stack;
import org.apache.commons.lang.StringUtils;
public class LatestRepoCallable implements java.util.concurrent.Callable<java.lang.Void> {
    private static final int LOOKUP_CONNECTION_TIMEOUT = 2000;

    private static final int LOOKUP_READ_TIMEOUT = 3000;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.stack.LatestRepoCallable.class);

    private java.lang.String sourceUri = null;

    private java.io.File stackRepoFolder = null;

    private org.apache.ambari.server.state.StackInfo stack = null;

    private org.apache.ambari.server.state.stack.OsFamily os_family;

    public LatestRepoCallable(java.lang.String latestSourceUri, java.io.File stackRepoFolder, org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.stack.OsFamily os_family) {
        this.sourceUri = latestSourceUri;
        this.stackRepoFolder = stackRepoFolder;
        this.stack = stack;
        this.os_family = os_family;
    }

    @java.lang.Override
    public java.lang.Void call() throws java.lang.Exception {
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>>() {}.getType();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> latestUrlMap = null;
        java.lang.Long time = java.lang.System.currentTimeMillis();
        try {
            if (sourceUri.startsWith("http")) {
                org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(org.apache.ambari.server.state.stack.LatestRepoCallable.LOOKUP_CONNECTION_TIMEOUT, org.apache.ambari.server.state.stack.LatestRepoCallable.LOOKUP_READ_TIMEOUT, null, null, null);
                org.apache.ambari.server.state.stack.LatestRepoCallable.LOG.info("Loading latest URL info for stack {}-{} from {}", stack.getName(), stack.getVersion(), sourceUri);
                latestUrlMap = gson.fromJson(new java.io.InputStreamReader(streamProvider.readFrom(sourceUri)), type);
            } else {
                java.io.File jsonFile = null;
                if (sourceUri.charAt(0) == '.') {
                    jsonFile = new java.io.File(stackRepoFolder, sourceUri);
                } else {
                    jsonFile = new java.io.File(sourceUri);
                }
                if (jsonFile.exists()) {
                    org.apache.ambari.server.state.stack.LatestRepoCallable.LOG.info("Loading latest URL info for stack {}-{} from {}", stack.getName(), stack.getVersion(), jsonFile);
                    latestUrlMap = gson.fromJson(new java.io.FileReader(jsonFile), type);
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.stack.LatestRepoCallable.LOG.info("Could not load the URI for stack {}-{} from {}, ({}).  Using default repository values", stack.getName(), stack.getVersion(), sourceUri, e.getMessage());
            throw e;
        } finally {
            org.apache.ambari.server.state.stack.LatestRepoCallable.LOG.info("Loaded uri {} in {}ms", sourceUri, java.lang.System.currentTimeMillis() - time);
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stack);
        if ((latestUrlMap == null) || (!latestUrlMap.containsKey(stackId.toString()))) {
            return null;
        }
        java.util.Map<java.lang.String, java.lang.Object> map = latestUrlMap.get(stackId.toString());
        if (null == map) {
            return null;
        }
        java.util.Map<java.net.URI, org.apache.ambari.server.state.repository.VersionDefinitionXml> parsedMap = new java.util.HashMap<>();
        if (map.containsKey("manifests")) {
            @java.lang.SuppressWarnings("unchecked")
            java.util.Map<java.lang.String, java.lang.Object> versionMap = ((java.util.Map<java.lang.String, java.lang.Object>) (map.get("manifests")));
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : versionMap.entrySet()) {
                java.lang.String version = entry.getKey();
                @java.lang.SuppressWarnings("unchecked")
                java.util.Map<java.lang.String, java.lang.String> osMap = ((java.util.Map<java.lang.String, java.lang.String>) (entry.getValue()));
                org.apache.ambari.server.state.repository.VersionDefinitionXml xml = mergeDefinitions(stackId, version, osMap, parsedMap);
                if (null != xml) {
                    stack.addVersionDefinition(version, xml);
                }
            }
        }
        if (map.containsKey("latest-vdf")) {
            @java.lang.SuppressWarnings("unchecked")
            java.util.Map<java.lang.String, java.lang.String> osMap = ((java.util.Map<java.lang.String, java.lang.String>) (map.get("latest-vdf")));
            org.apache.ambari.server.state.repository.VersionDefinitionXml xml = mergeDefinitions(stackId, null, osMap, parsedMap);
            xml.setStackDefault(true);
            stack.setLatestVersionDefinition(xml);
        }
        return null;
    }

    private org.apache.ambari.server.state.repository.VersionDefinitionXml mergeDefinitions(org.apache.ambari.server.state.StackId stackId, java.lang.String version, java.util.Map<java.lang.String, java.lang.String> osMap, java.util.Map<java.net.URI, org.apache.ambari.server.state.repository.VersionDefinitionXml> parsedMap) throws java.lang.Exception {
        java.util.Set<java.lang.String> oses = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.RepositoryInfo ri : stack.getRepositories()) {
            if (null != os_family.find(ri.getOsType())) {
                oses.add(os_family.find(ri.getOsType()));
            }
        }
        org.apache.ambari.server.state.repository.VersionDefinitionXml.Merger merger = new org.apache.ambari.server.state.repository.VersionDefinitionXml.Merger();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> versionEntry : osMap.entrySet()) {
            java.lang.String osFamily = os_family.find(versionEntry.getKey());
            if (null == osFamily) {
                java.lang.String alias = os_family.getAliases().get(versionEntry.getKey());
                if (null != alias) {
                    osFamily = os_family.find(alias);
                }
            }
            if ((null == osFamily) || (!oses.contains(osFamily))) {
                org.apache.ambari.server.state.stack.LatestRepoCallable.LOG.info("Stack {} cannot resolve OS {} to the supported ones: {}. Family: {}", stackId, versionEntry.getKey(), org.apache.commons.lang.StringUtils.join(oses, ','), osFamily);
                continue;
            }
            java.lang.String uriString = versionEntry.getValue();
            if ('.' == uriString.charAt(0)) {
                uriString = new java.io.File(stackRepoFolder, uriString).toURI().toString();
            }
            try {
                java.net.URI uri = new java.net.URI(uriString);
                org.apache.ambari.server.state.repository.VersionDefinitionXml xml = (parsedMap.containsKey(uri)) ? parsedMap.get(uri) : timedVDFLoad(uri);
                version = (null == version) ? xml.release.version : version;
                merger.add(version, xml);
                if (!parsedMap.containsKey(uri)) {
                    parsedMap.put(uri, xml);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.stack.LatestRepoCallable.LOG.warn("Could not load version definition for {} identified by {}. {}", stackId, uriString, e.getMessage(), e);
            }
        }
        return merger.merge();
    }

    private java.lang.String resolveOsUrl(java.lang.String os, java.util.Map<java.lang.String, java.lang.String> osMap) {
        if (osMap.containsKey(os))
            return osMap.get(os);

        java.util.Set<java.lang.String> possibleTypes = os_family.findTypes(os);
        for (java.lang.String type : possibleTypes) {
            if (osMap.containsKey(type))
                return osMap.get(type);

        }
        return null;
    }

    private org.apache.ambari.server.state.repository.VersionDefinitionXml timedVDFLoad(java.net.URI uri) throws java.lang.Exception {
        long time = java.lang.System.currentTimeMillis();
        try {
            return org.apache.ambari.server.state.repository.VersionDefinitionXml.load(uri.toURL());
        } finally {
            org.apache.ambari.server.state.stack.LatestRepoCallable.LOG.info("Loaded VDF {} in {}ms", uri, java.lang.System.currentTimeMillis() - time);
        }
    }
}