package org.apache.ambari.server.checks;
import org.mockito.Mockito;
public class HostsHeartbeatCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    final org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion = org.mockito.Mockito.mock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.mockito.Mockito.when(m_repositoryVersion.getType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn("2.2.0.0-1234");
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2"));
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        final org.apache.ambari.server.checks.HostsHeartbeatCheck hostHeartbeatCheck = new org.apache.ambari.server.checks.HostsHeartbeatCheck();
        hostHeartbeatCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2"));
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        final java.util.List<org.apache.ambari.server.state.Host> hosts = new java.util.ArrayList<>();
        final org.apache.ambari.server.state.Host host1 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host2 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host3 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.HostHealthStatus status1 = org.mockito.Mockito.mock(org.apache.ambari.server.state.HostHealthStatus.class);
        final org.apache.ambari.server.state.HostHealthStatus status2 = org.mockito.Mockito.mock(org.apache.ambari.server.state.HostHealthStatus.class);
        final org.apache.ambari.server.state.HostHealthStatus status3 = org.mockito.Mockito.mock(org.apache.ambari.server.state.HostHealthStatus.class);
        org.mockito.Mockito.when(host1.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host2.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host1.getHealthStatus()).thenReturn(status1);
        org.mockito.Mockito.when(host2.getHealthStatus()).thenReturn(status2);
        org.mockito.Mockito.when(host3.getHealthStatus()).thenReturn(status3);
        org.mockito.Mockito.when(status1.getHealthStatus()).thenReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY);
        org.mockito.Mockito.when(status2.getHealthStatus()).thenReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY);
        org.mockito.Mockito.when(status3.getHealthStatus()).thenReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN);
        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
        org.mockito.Mockito.when(cluster.getHosts()).thenReturn(hosts);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = hostHeartbeatCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.ON);
        check = hostHeartbeatCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(status3.getHealthStatus()).thenReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        check = hostHeartbeatCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
    }
}