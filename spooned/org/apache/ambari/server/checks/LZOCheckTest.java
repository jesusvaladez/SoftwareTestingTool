package org.apache.ambari.server.checks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class LZOCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.checks.LZOCheck lZOCheck = new org.apache.ambari.server.checks.LZOCheck();

    @org.mockito.Mock
    private org.apache.ambari.spi.RepositoryVersion m_repositoryVersion;

    @org.mockito.Mock
    private org.apache.ambari.server.configuration.Configuration configuration;

    final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> m_services = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        lZOCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        lZOCheck.config = configuration;
        m_services.clear();
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(services);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        final org.apache.ambari.server.state.DesiredConfig desiredConfig = org.mockito.Mockito.mock(org.apache.ambari.server.state.DesiredConfig.class);
        org.mockito.Mockito.when(desiredConfig.getTag()).thenReturn("tag");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> configMap = new java.util.HashMap<>();
        configMap.put("core-site", desiredConfig);
        org.mockito.Mockito.when(cluster.getDesiredConfigs()).thenReturn(configMap);
        final org.apache.ambari.server.state.Config config = org.mockito.Mockito.mock(org.apache.ambari.server.state.Config.class);
        org.mockito.Mockito.when(cluster.getConfig(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenReturn(config);
        final java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        org.mockito.Mockito.when(config.getProperties()).thenReturn(properties);
        org.mockito.Mockito.when(configuration.getGplLicenseAccepted()).thenReturn(false);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = lZOCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
        properties.put(org.apache.ambari.server.checks.LZOCheck.IO_COMPRESSION_CODECS, "test," + org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_VALUE);
        result = lZOCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING, result.getStatus());
        properties.put(org.apache.ambari.server.checks.LZOCheck.IO_COMPRESSION_CODECS, "test");
        result = lZOCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
        properties.put(org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_KEY, org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_VALUE);
        result = lZOCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING, result.getStatus());
        properties.put(org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_KEY, org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_VALUE);
        properties.put(org.apache.ambari.server.checks.LZOCheck.IO_COMPRESSION_CODECS, "test," + org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_VALUE);
        result = lZOCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING, result.getStatus());
        org.mockito.Mockito.when(configuration.getGplLicenseAccepted()).thenReturn(true);
        result = lZOCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }
}