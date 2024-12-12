package org.apache.ambari.server.checks;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class HealthCheckTest {
    private static final java.lang.String CLUSTER_NAME = "cluster1";

    private static final long CLUSTER_ID = 1L;

    private static final java.lang.String ALERT_HOSTNAME = "some hostname 1";

    private static final java.lang.String ALERT_DEFINITION_LABEL = "label 1";

    private org.apache.ambari.server.checks.HealthCheck healthCheck;

    private org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO = Mockito.mock(org.apache.ambari.server.orm.dao.AlertsDAO.class);

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        final org.apache.ambari.server.state.Clusters clusters = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        healthCheck = new org.apache.ambari.server.checks.HealthCheck();
        healthCheck.alertsDAOProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.AlertsDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.AlertsDAO get() {
                return alertsDAO;
            }
        };
        healthCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        org.apache.ambari.server.state.Cluster cluster = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        Mockito.when(clusters.getCluster(org.apache.ambari.server.checks.HealthCheckTest.CLUSTER_NAME)).thenReturn(cluster);
        Mockito.when(cluster.getClusterId()).thenReturn(org.apache.ambari.server.checks.HealthCheckTest.CLUSTER_ID);
    }

    @org.junit.Test
    public void testWarningWhenNoAlertsExist() throws org.apache.ambari.server.AmbariException {
        Mockito.when(alertsDAO.findCurrentByCluster(Matchers.eq(org.apache.ambari.server.checks.HealthCheckTest.CLUSTER_ID))).thenReturn(java.util.Collections.emptyList());
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.HealthCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = healthCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
        org.junit.Assert.assertTrue(result.getFailedDetail().isEmpty());
    }

    @org.junit.Test
    public void testWarningWhenCriticalAlertExists() throws org.apache.ambari.server.AmbariException {
        expectWarning(org.apache.ambari.server.state.AlertState.CRITICAL);
    }

    @org.junit.Test
    public void testWarningWhenWarningAlertExists() throws org.apache.ambari.server.AmbariException {
        expectWarning(org.apache.ambari.server.state.AlertState.WARNING);
    }

    private void expectWarning(org.apache.ambari.server.state.AlertState alertState) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.AlertCurrentEntity alertCurrentEntity = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity criticalAlert = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        criticalAlert.setAlertDefinition(alertDefinition);
        criticalAlert.setHostName(org.apache.ambari.server.checks.HealthCheckTest.ALERT_HOSTNAME);
        criticalAlert.setAlertState(alertState);
        alertDefinition.setLabel(org.apache.ambari.server.checks.HealthCheckTest.ALERT_DEFINITION_LABEL);
        alertCurrentEntity.setAlertHistory(criticalAlert);
        Mockito.when(alertsDAO.findCurrentByCluster(Matchers.eq(org.apache.ambari.server.checks.HealthCheckTest.CLUSTER_ID))).thenReturn(java.util.Arrays.asList(alertCurrentEntity));
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.HealthCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = healthCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING, result.getStatus());
        org.junit.Assert.assertFalse(result.getFailedDetail().isEmpty());
    }
}