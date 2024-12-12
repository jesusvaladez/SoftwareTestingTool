package org.apache.ambari.server.state.stack;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
public class RepoVdfCallable implements java.util.concurrent.Callable<java.lang.Void> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.stack.RepoVdfCallable.class);

    private final org.apache.ambari.server.state.StackInfo m_stack;

    private final org.apache.ambari.server.state.stack.OsFamily m_family;

    private final java.util.Map<java.lang.String, java.net.URI> m_vdfMap;

    private java.lang.String m_version;

    public RepoVdfCallable(org.apache.ambari.server.stack.StackModule stackModule, java.lang.String version, java.util.Map<java.lang.String, java.net.URI> vdfOsMap, org.apache.ambari.server.state.stack.OsFamily os_family) {
        m_stack = stackModule.getModuleInfo();
        m_family = os_family;
        m_version = version;
        m_vdfMap = vdfOsMap;
    }

    public RepoVdfCallable(org.apache.ambari.server.stack.StackModule stackModule, java.util.Map<java.lang.String, java.net.URI> vdfOsMap, org.apache.ambari.server.state.stack.OsFamily os_family) {
        m_stack = stackModule.getModuleInfo();
        m_family = os_family;
        m_version = null;
        m_vdfMap = vdfOsMap;
    }

    @java.lang.Override
    public java.lang.Void call() throws java.lang.Exception {
        if (org.apache.commons.collections.MapUtils.isEmpty(m_vdfMap)) {
            return null;
        }
        boolean forLatest = null == m_version;
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(m_stack);
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = mergeDefinitions(stackId, m_version, m_vdfMap);
        if (null == xml) {
            return null;
        }
        if (forLatest) {
            xml.setStackDefault(true);
            m_stack.setLatestVersionDefinition(xml);
        } else {
            m_stack.addVersionDefinition(m_version, xml);
        }
        return null;
    }

    private org.apache.ambari.server.state.repository.VersionDefinitionXml mergeDefinitions(org.apache.ambari.server.state.StackId stackId, java.lang.String version, java.util.Map<java.lang.String, java.net.URI> osMap) throws java.lang.Exception {
        java.util.Set<java.lang.String> oses = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.RepositoryInfo ri : m_stack.getRepositories()) {
            if (null != m_family.find(ri.getOsType())) {
                oses.add(m_family.find(ri.getOsType()));
            }
        }
        org.apache.ambari.server.state.repository.VersionDefinitionXml.Merger merger = new org.apache.ambari.server.state.repository.VersionDefinitionXml.Merger();
        for (java.util.Map.Entry<java.lang.String, java.net.URI> versionEntry : osMap.entrySet()) {
            java.lang.String osFamily = m_family.find(versionEntry.getKey());
            java.net.URI uri = versionEntry.getValue();
            if (null == osFamily) {
                java.lang.String alias = m_family.getAliases().get(versionEntry.getKey());
                if (null != alias) {
                    osFamily = m_family.find(alias);
                }
            }
            if ((null == osFamily) || (!oses.contains(osFamily))) {
                org.apache.ambari.server.state.stack.RepoVdfCallable.LOG.info("Stack {} cannot resolve OS {} to the supported ones: {}. Family: {}", stackId, versionEntry.getKey(), org.apache.commons.lang.StringUtils.join(oses, ','), osFamily);
                continue;
            }
            try {
                org.apache.ambari.server.state.repository.VersionDefinitionXml xml = timedVDFLoad(uri);
                version = (null == version) ? xml.release.version : version;
                merger.add(version, xml);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.stack.RepoVdfCallable.LOG.warn("Could not load version definition for {} identified by {}. {}", stackId, uri.toString(), e.getMessage(), e);
            }
        }
        return merger.merge();
    }

    private org.apache.ambari.server.state.repository.VersionDefinitionXml timedVDFLoad(java.net.URI uri) throws java.lang.Exception {
        long time = java.lang.System.currentTimeMillis();
        try {
            return org.apache.ambari.server.state.repository.VersionDefinitionXml.load(uri.toURL());
        } finally {
            org.apache.ambari.server.state.stack.RepoVdfCallable.LOG.debug("Loaded VDF {} in {}ms", uri, java.lang.System.currentTimeMillis() - time);
        }
    }
}