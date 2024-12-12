package org.apache.ambari.server.checks;
import org.mockito.Mockito;
public class ServicePresenceCheckTest {
    private final org.apache.ambari.server.state.Clusters m_clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.checks.ServicePresenceCheck m_check = new org.apache.ambari.server.checks.ServicePresenceCheck();

    final org.apache.ambari.spi.RepositoryVersion m_repositoryVersion = org.mockito.Mockito.mock(org.apache.ambari.spi.RepositoryVersion.class);

    @org.junit.Before
    public void setup() {
        m_check.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return m_clusters;
            }
        };
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn("2.5.0.0-1234");
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.5").toString());
    }

    @org.junit.Test
    public void testPerformPass() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(m_clusters.getCluster("cluster")).thenReturn(cluster);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NO_UPGRADE_SUPPORT_SERVICES_PROPERTY_NAME, "MyServiceOne, MyServiceTwo");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REMOVED_SERVICES_PROPERTY_NAME, "RemovedServiceOne, RemovedServiceTwo");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME, "OldServiceOne, OldServiceTwo");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME, "NewServiceOne, NewServiceTwo");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testPerformHasNoUpgradeSupportServices() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(m_clusters.getCluster("cluster")).thenReturn(cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("ATLAS", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(services);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NO_UPGRADE_SUPPORT_SERVICES_PROPERTY_NAME, "Atlas, MyService");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testPerformHasReplacedServices() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(m_clusters.getCluster("cluster")).thenReturn(cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("ATLAS", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        services.put("OLDSERVICE", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(services);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME, "Atlas, OldService");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME, "Atlas2, NewService");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testPerformHasRemovedServices() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(m_clusters.getCluster("cluster")).thenReturn(cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("ATLAS", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        services.put("OLDSERVICE", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(services);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REMOVED_SERVICES_PROPERTY_NAME, "OldService");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testPerformMixOne() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(m_clusters.getCluster("cluster")).thenReturn(cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("ATLAS", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        services.put("REMOVEDSERVICE", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(services);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NO_UPGRADE_SUPPORT_SERVICES_PROPERTY_NAME, "MyServiceOne, MyServiceTwo");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME, "Atlas, OldService");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME, "Atlas2, NewService");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REMOVED_SERVICES_PROPERTY_NAME, "RemovedService");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testPerformMixTwo() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(m_clusters.getCluster("cluster")).thenReturn(cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("OLDSERVICE", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(services);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NO_UPGRADE_SUPPORT_SERVICES_PROPERTY_NAME, "Atlas, MyService");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME, "OldService");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME, "NewService");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testPerformMixThree() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(m_clusters.getCluster("cluster")).thenReturn(cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("ATLAS", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        services.put("HDFS", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        services.put("STORM", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        services.put("RANGER", org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class));
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(services);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NO_UPGRADE_SUPPORT_SERVICES_PROPERTY_NAME, "Atlas, HDFS");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME, "Storm, Ranger");
        checkProperties.put(org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME, "Storm2, Ranger2");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }
}