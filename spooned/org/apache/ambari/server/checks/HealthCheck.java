package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.apache.ambari.server.state.AlertState.CRITICAL;
import static org.apache.ambari.server.state.AlertState.WARNING;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class HealthCheck extends org.apache.ambari.server.checks.ClusterCheck {
    private static final java.util.List<org.apache.ambari.server.state.AlertState> ALERT_STATES = java.util.Arrays.asList(org.apache.ambari.server.state.AlertState.WARNING, org.apache.ambari.server.state.AlertState.CRITICAL);

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.orm.dao.AlertsDAO> alertsDAOProvider;

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription HEALTH = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("HEALTH", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Cluster Health", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following issues have been detected on this cluster and should be addressed before upgrading: %s").build());

    public HealthCheck() {
        super(org.apache.ambari.server.checks.HealthCheck.HEALTH);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO = alertsDAOProvider.get();
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = alertsDAO.findCurrentByCluster(cluster.getClusterId());
        java.util.List<java.lang.String> errorMessages = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity alert : alerts) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity alertHistory = alert.getAlertHistory();
            org.apache.ambari.server.state.AlertState alertState = alertHistory.getAlertState();
            if (org.apache.ambari.server.checks.HealthCheck.ALERT_STATES.contains(alertState) && (!alert.getMaintenanceState().equals(org.apache.ambari.server.state.MaintenanceState.ON))) {
                java.lang.String state = alertState.name();
                java.lang.String label = alertHistory.getAlertDefinition().getLabel();
                java.lang.String hostName = alertHistory.getHostName();
                if (hostName == null) {
                    errorMessages.add((state + ": ") + label);
                } else {
                    errorMessages.add((((state + ": ") + label) + ": ") + hostName);
                }
                result.getFailedDetail().add(new org.apache.ambari.server.checks.HealthCheck.AlertDetail(state, label, hostName));
            }
        }
        if (!errorMessages.isEmpty()) {
            result.getFailedOn().add(clusterName);
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING);
            java.lang.String failReason = getFailReason(result, request);
            result.setFailReason(java.lang.String.format(failReason, org.apache.commons.lang.StringUtils.join(errorMessages, java.lang.System.lineSeparator())));
        }
        return result;
    }

    private static class AlertDetail {
        @org.codehaus.jackson.annotate.JsonProperty("state")
        public java.lang.String state;

        @org.codehaus.jackson.annotate.JsonProperty("label")
        public java.lang.String label;

        @org.codehaus.jackson.annotate.JsonProperty("host_name")
        public java.lang.String hostName;

        AlertDetail(java.lang.String state, java.lang.String label, java.lang.String hostName) {
            this.state = state;
            this.label = label;
            this.hostName = hostName;
        }
    }
}