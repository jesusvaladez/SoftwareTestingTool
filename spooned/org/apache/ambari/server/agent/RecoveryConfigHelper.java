package org.apache.ambari.server.agent;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class RecoveryConfigHelper {
    public static final java.lang.String RECOVERY_ENABLED_KEY = "recovery_enabled";

    public static final java.lang.String RECOVERY_TYPE_KEY = "recovery_type";

    public static final java.lang.String RECOVERY_TYPE_DEFAULT = "AUTO_START";

    public static final java.lang.String RECOVERY_LIFETIME_MAX_COUNT_KEY = "recovery_lifetime_max_count";

    public static final java.lang.String RECOVERY_LIFETIME_MAX_COUNT_DEFAULT = "12";

    public static final java.lang.String RECOVERY_MAX_COUNT_KEY = "recovery_max_count";

    public static final java.lang.String RECOVERY_MAX_COUNT_DEFAULT = "6";

    public static final java.lang.String RECOVERY_WINDOW_IN_MIN_KEY = "recovery_window_in_minutes";

    public static final java.lang.String RECOVERY_WINDOW_IN_MIN_DEFAULT = "60";

    public static final java.lang.String RECOVERY_RETRY_GAP_KEY = "recovery_retry_interval";

    public static final java.lang.String RECOVERY_RETRY_GAP_DEFAULT = "5";

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Long>> timestampMap;

    @com.google.inject.Inject
    public RecoveryConfigHelper(org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) {
        eventPublisher.register(this);
        timestampMap = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public org.apache.ambari.server.agent.RecoveryConfig getDefaultRecoveryConfig() throws org.apache.ambari.server.AmbariException {
        return getRecoveryConfig(null, null);
    }

    public org.apache.ambari.server.agent.RecoveryConfig getRecoveryConfig(java.lang.String clusterName, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        long now = java.lang.System.currentTimeMillis();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(clusterName)) {
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Long> hostTimestamp = timestampMap.get(clusterName);
            if (hostTimestamp == null) {
                hostTimestamp = new java.util.concurrent.ConcurrentHashMap<>();
                timestampMap.put(clusterName, hostTimestamp);
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(hostname)) {
                hostTimestamp.put(hostname, now);
            }
        }
        org.apache.ambari.server.agent.RecoveryConfigHelper.AutoStartConfig autoStartConfig = new org.apache.ambari.server.agent.RecoveryConfigHelper.AutoStartConfig(clusterName);
        org.apache.ambari.server.agent.RecoveryConfig recoveryConfig = new org.apache.ambari.server.agent.RecoveryConfig(autoStartConfig.getEnabledComponents(hostname));
        return recoveryConfig;
    }

    public boolean isConfigStale(java.lang.String clusterName, java.lang.String hostname, long recoveryTimestamp) {
        if (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
            throw new java.lang.IllegalArgumentException("clusterName cannot be empty or null.");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(hostname)) {
            throw new java.lang.IllegalArgumentException("hostname cannot be empty or null.");
        }
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Long> hostTimestamp = timestampMap.get(clusterName);
        if (hostTimestamp == null) {
            return true;
        }
        java.lang.Long timestamp = hostTimestamp.get(hostname);
        if (timestamp == null) {
            return true;
        }
        if (timestamp.longValue() != recoveryTimestamp) {
            return true;
        }
        return false;
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void handleMaintenanceModeEvent(org.apache.ambari.server.events.MaintenanceModeEvent event) throws org.apache.ambari.server.AmbariException {
        if (event.getHost() != null) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(event.getClusterId());
            if (cluster == null) {
                return;
            }
            org.apache.ambari.server.state.Host host = event.getHost();
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts = cluster.getServiceComponentHosts(host.getHostName());
            for (org.apache.ambari.server.state.ServiceComponentHost sch : scHosts) {
                if (sch.isRecoveryEnabled()) {
                    invalidateRecoveryTimestamp(sch.getClusterName(), sch.getHostName());
                    break;
                }
            }
        } else if (event.getService() != null) {
            org.apache.ambari.server.state.Service service = event.getService();
            invalidateRecoveryTimestamp(service.getCluster().getClusterName(), null);
        } else if (event.getServiceComponentHost() != null) {
            org.apache.ambari.server.state.ServiceComponentHost sch = event.getServiceComponentHost();
            if (sch.isRecoveryEnabled()) {
                invalidateRecoveryTimestamp(sch.getClusterName(), sch.getHostName());
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void handleServiceComponentInstalledEvent(org.apache.ambari.server.events.ServiceComponentInstalledEvent event) throws org.apache.ambari.server.AmbariException {
        if (event.isRecoveryEnabled()) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(event.getClusterId());
            if (cluster != null) {
                invalidateRecoveryTimestamp(cluster.getClusterName(), event.getHostName());
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void handleServiceComponentUninstalledEvent(org.apache.ambari.server.events.ServiceComponentUninstalledEvent event) throws org.apache.ambari.server.AmbariException {
        if (event.isRecoveryEnabled()) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(event.getClusterId());
            if (cluster != null) {
                invalidateRecoveryTimestamp(cluster.getClusterName(), event.getHostName());
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void handleServiceComponentRecoveryChangedEvent(org.apache.ambari.server.events.ServiceComponentRecoveryChangedEvent event) {
        invalidateRecoveryTimestamp(event.getClusterName(), null);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void handleClusterEnvConfigChangedEvent(org.apache.ambari.server.events.ClusterConfigChangedEvent event) {
        if (org.apache.commons.lang.StringUtils.equals(event.getConfigType(), org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV)) {
            invalidateRecoveryTimestamp(event.getClusterName(), null);
        }
    }

    private void invalidateRecoveryTimestamp(java.lang.String clusterName, java.lang.String hostname) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(clusterName)) {
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Long> hostTimestamp = timestampMap.get(clusterName);
            if (hostTimestamp != null) {
                if (org.apache.commons.lang.StringUtils.isNotEmpty(hostname)) {
                    hostTimestamp.put(hostname, 0L);
                } else {
                    for (java.util.Map.Entry<java.lang.String, java.lang.Long> hostEntry : hostTimestamp.entrySet()) {
                        hostEntry.setValue(0L);
                    }
                }
            }
        }
    }

    class AutoStartConfig {
        private org.apache.ambari.server.state.Cluster cluster;

        private java.util.Map<java.lang.String, java.lang.String> configProperties;

        public AutoStartConfig(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
            if (org.apache.commons.lang.StringUtils.isNotEmpty(clusterName)) {
                cluster = clusters.getCluster(clusterName);
            }
            if (cluster != null) {
                org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(getConfigType());
                if (config != null) {
                    configProperties = config.getProperties();
                }
            }
            if (configProperties == null) {
                configProperties = new java.util.HashMap<>();
            }
        }

        private java.util.List<org.apache.ambari.server.agent.RecoveryConfigComponent> getEnabledComponents(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
            java.util.List<org.apache.ambari.server.agent.RecoveryConfigComponent> enabledComponents = new java.util.ArrayList<>();
            if (cluster == null) {
                return enabledComponents;
            }
            org.apache.ambari.server.state.Host host = clusters.getHost(hostname);
            if (host == null) {
                return enabledComponents;
            }
            if (host.getMaintenanceState(cluster.getClusterId()) == org.apache.ambari.server.state.MaintenanceState.ON) {
                return enabledComponents;
            }
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts = cluster.getServiceComponentHosts(hostname);
            for (org.apache.ambari.server.state.ServiceComponentHost sch : scHosts) {
                if (sch.isRecoveryEnabled()) {
                    org.apache.ambari.server.state.Service service = cluster.getService(sch.getServiceName());
                    if (service.getMaintenanceState() == org.apache.ambari.server.state.MaintenanceState.OFF) {
                        if (sch.getMaintenanceState() == org.apache.ambari.server.state.MaintenanceState.OFF) {
                            enabledComponents.add(new org.apache.ambari.server.agent.RecoveryConfigComponent(sch));
                        }
                    }
                }
            }
            return enabledComponents;
        }

        private java.lang.String getConfigType() {
            return "cluster-env";
        }

        private boolean isRecoveryEnabled() {
            return java.lang.Boolean.parseBoolean(getProperty(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_ENABLED_KEY, "false"));
        }

        private java.lang.String getProperty(java.lang.String key, java.lang.String defaultValue) {
            if (configProperties.containsKey(key)) {
                return configProperties.get(key);
            }
            return defaultValue;
        }
    }
}