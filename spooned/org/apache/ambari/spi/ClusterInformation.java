package org.apache.ambari.spi;
public class ClusterInformation {
    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> m_configurations;

    private final java.lang.String m_clusterName;

    private final boolean m_isKerberosEnabled;

    private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> m_topology;

    private final java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> m_serviceVersions;

    public ClusterInformation(java.lang.String clusterName, boolean isKerberosEnabled, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> topology, java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> serviceVersions) {
        m_configurations = configurations;
        m_clusterName = clusterName;
        m_isKerberosEnabled = isKerberosEnabled;
        m_topology = topology;
        m_serviceVersions = serviceVersions;
    }

    public java.lang.String getClusterName() {
        return m_clusterName;
    }

    public boolean isKerberosEnabled() {
        return m_isKerberosEnabled;
    }

    public java.util.Set<java.lang.String> getHosts(java.lang.String serviceName, java.lang.String componentName) {
        java.util.Set<java.lang.String> hosts = m_topology.get((serviceName + "/") + componentName);
        if (null == hosts) {
            hosts = new java.util.HashSet<>();
        }
        return hosts;
    }

    public java.util.Map<java.lang.String, java.lang.String> getConfigurationProperties(java.lang.String configurationType) {
        return m_configurations.get(configurationType);
    }

    public java.lang.String getConfigurationProperty(java.lang.String configurationType, java.lang.String propertyName) {
        java.util.Map<java.lang.String, java.lang.String> configType = getConfigurationProperties(configurationType);
        if (null == configType) {
            return null;
        }
        return configType.get(propertyName);
    }

    public org.apache.ambari.spi.RepositoryVersion getServiceRepositoryVersion(java.lang.String serviceName) {
        return m_serviceVersions.get(serviceName);
    }

    public java.util.Set<java.lang.String> getServices() {
        return m_serviceVersions.keySet();
    }
}