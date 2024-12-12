package org.apache.ambari.server.checks;
import org.mockito.Mockito;
public class HostMaintenanceModeCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        final org.apache.ambari.server.checks.HostMaintenanceModeCheck hostMaintenanceModeCheck = new org.apache.ambari.server.checks.HostMaintenanceModeCheck();
        hostMaintenanceModeCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
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
        org.mockito.Mockito.when(host1.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host2.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
        org.mockito.Mockito.when(cluster.getHosts()).thenReturn(hosts);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = hostMaintenanceModeCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.ON);
        check = hostMaintenanceModeCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING, check.getStatus());
    }

    @org.junit.Test
    public void testPerformHostOrdered() throws java.lang.Exception {
        final org.apache.ambari.server.checks.HostMaintenanceModeCheck hostMaintenanceModeCheck = new org.apache.ambari.server.checks.HostMaintenanceModeCheck();
        hostMaintenanceModeCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
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
        org.mockito.Mockito.when(host1.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host2.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host1.getHostName()).thenReturn("h1");
        org.mockito.Mockito.when(host2.getHostName()).thenReturn("h2");
        org.mockito.Mockito.when(host3.getHostName()).thenReturn("h3");
        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
        org.mockito.Mockito.when(cluster.getHosts()).thenReturn(hosts);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = hostMaintenanceModeCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.ON);
        request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED, null, null, null);
        check = hostMaintenanceModeCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
        org.junit.Assert.assertEquals("The following hosts cannot be in Maintenance Mode: h3.", check.getFailReason());
    }
}