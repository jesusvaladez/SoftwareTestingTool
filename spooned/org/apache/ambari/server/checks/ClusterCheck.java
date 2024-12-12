package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
public abstract class ClusterCheck implements org.apache.ambari.spi.upgrade.UpgradeCheck {
    @com.google.inject.Inject
    protected com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clustersProvider;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostVersionDAO> hostVersionDaoProvider;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO> repositoryVersionDaoProvider;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.orm.dao.UpgradeDAO> upgradeDaoProvider;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper> repositoryVersionHelper;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ambariMetaInfo;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration config;

    @com.google.inject.Inject
    com.google.gson.Gson gson;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper> checkHelperProvider;

    private org.apache.ambari.spi.upgrade.UpgradeCheckDescription m_description;

    protected ClusterCheck(org.apache.ambari.spi.upgrade.UpgradeCheckDescription description) {
        m_description = description;
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckDescription getCheckDescription() {
        return m_description;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getApplicableServices() {
        return java.util.Collections.emptySet();
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.spi.upgrade.CheckQualification> getQualifications() {
        return java.util.Collections.emptyList();
    }

    public org.apache.ambari.spi.upgrade.UpgradeCheckDescription getDescription() {
        return m_description;
    }

    protected java.lang.String getFailReason(org.apache.ambari.spi.upgrade.UpgradeCheckResult upgradeCheckResult, org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        return getFailReason(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, upgradeCheckResult, request);
    }

    protected java.lang.String getProperty(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request, java.lang.String configType, java.lang.String propertyName) throws org.apache.ambari.server.AmbariException {
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        final org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        if (null == desiredConfig) {
            return null;
        }
        final org.apache.ambari.server.state.Config config = cluster.getConfig(configType, desiredConfig.getTag());
        java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
        return properties.get(propertyName);
    }

    protected java.lang.String getFailReason(java.lang.String key, org.apache.ambari.spi.upgrade.UpgradeCheckResult upgradeCheckResult, org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        java.lang.String fail = m_description.getFailureReason(key);
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        if (fail.contains("{{version}}") && (null != repositoryVersion)) {
            fail = fail.replace("{{version}}", repositoryVersion.getVersion());
        }
        if (fail.contains("{{fails}}")) {
            java.util.LinkedHashSet<java.lang.String> names = upgradeCheckResult.getFailedOn();
            if (getDescription().getType() == org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE) {
                org.apache.ambari.server.state.Clusters clusters = clustersProvider.get();
                org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = ambariMetaInfo.get();
                org.apache.ambari.server.state.Cluster c = clusters.getCluster(request.getClusterName());
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = metaInfo.getServices(c.getDesiredStackVersion().getStackName(), c.getDesiredStackVersion().getStackVersion());
                java.util.LinkedHashSet<java.lang.String> displays = new java.util.LinkedHashSet<>();
                for (java.lang.String name : names) {
                    if (services.containsKey(name)) {
                        displays.add(services.get(name).getDisplayName());
                    } else {
                        displays.add(name);
                    }
                }
                names = displays;
            }
            fail = fail.replace("{{fails}}", formatEntityList(names));
        }
        return fail;
    }

    protected java.lang.String formatEntityList(java.util.LinkedHashSet<java.lang.String> entities) {
        if ((entities == null) || entities.isEmpty()) {
            return "";
        }
        final java.lang.StringBuilder formatted = new java.lang.StringBuilder(org.apache.commons.lang.StringUtils.join(entities, ", "));
        if (entities.size() > 1) {
            formatted.replace(formatted.lastIndexOf(","), formatted.lastIndexOf(",") + 1, " and");
        }
        return formatted.toString();
    }

    static class ServiceDetail implements java.lang.Comparable<org.apache.ambari.server.checks.ClusterCheck.ServiceDetail> {
        @org.codehaus.jackson.annotate.JsonProperty("service_name")
        final java.lang.String serviceName;

        ServiceDetail(java.lang.String serviceName) {
            this.serviceName = serviceName;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(serviceName);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            org.apache.ambari.server.checks.ClusterCheck.ServiceDetail other = ((org.apache.ambari.server.checks.ClusterCheck.ServiceDetail) (obj));
            return java.util.Objects.equals(serviceName, other.serviceName);
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.checks.ClusterCheck.ServiceDetail other) {
            return serviceName.compareTo(other.serviceName);
        }
    }

    static class ServiceComponentDetail implements java.lang.Comparable<org.apache.ambari.server.checks.ClusterCheck.ServiceComponentDetail> {
        @org.codehaus.jackson.annotate.JsonProperty("service_name")
        final java.lang.String serviceName;

        @org.codehaus.jackson.annotate.JsonProperty("component_name")
        final java.lang.String componentName;

        ServiceComponentDetail(java.lang.String serviceName, java.lang.String componentName) {
            this.serviceName = serviceName;
            this.componentName = componentName;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(serviceName, componentName);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            org.apache.ambari.server.checks.ClusterCheck.ServiceComponentDetail other = ((org.apache.ambari.server.checks.ClusterCheck.ServiceComponentDetail) (obj));
            return java.util.Objects.equals(serviceName, other.serviceName) && java.util.Objects.equals(componentName, other.componentName);
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.checks.ClusterCheck.ServiceComponentDetail other) {
            return java.util.Comparator.comparing((org.apache.ambari.server.checks.ClusterCheck.ServiceComponentDetail detail) -> detail.serviceName).thenComparing(detail -> detail.componentName).compare(this, other);
        }
    }

    static class HostDetail implements java.lang.Comparable<org.apache.ambari.server.checks.ClusterCheck.HostDetail> {
        @org.codehaus.jackson.annotate.JsonProperty("host_id")
        final java.lang.Long hostId;

        @org.codehaus.jackson.annotate.JsonProperty("host_name")
        final java.lang.String hostName;

        HostDetail(java.lang.Long hostId, java.lang.String hostName) {
            this.hostId = hostId;
            this.hostName = hostName;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(hostId, hostName);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            org.apache.ambari.server.checks.ClusterCheck.HostDetail other = ((org.apache.ambari.server.checks.ClusterCheck.HostDetail) (obj));
            return java.util.Objects.equals(hostId, other.hostId) && java.util.Objects.equals(hostName, other.hostName);
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.checks.ClusterCheck.HostDetail other) {
            return hostName.compareTo(other.hostName);
        }
    }
}