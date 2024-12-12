package org.apache.ambari.server.checks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ServicesMaintenanceModeCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.ClusterVersionSummary m_clusterVersionSummary;

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    @org.mockito.Mock
    private org.apache.ambari.spi.RepositoryVersion m_repositoryVersion;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity;

    private org.apache.ambari.server.checks.MockCheckHelper m_checkHelper = new org.apache.ambari.server.checks.MockCheckHelper();

    final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> m_services = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_services.clear();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "1.0");
        java.lang.String version = "1.0.0.0-1234";
        org.mockito.Mockito.when(m_repositoryVersion.getId()).thenReturn(1L);
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(stackId.toString());
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn(version);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getVersion()).thenReturn("2.2.0.0-1234");
        org.mockito.Mockito.when(m_repositoryVersionEntity.getStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2"));
        org.mockito.Mockito.when(m_repositoryVersionEntity.getRepositoryXml()).thenReturn(m_vdfXml);
        org.mockito.Mockito.when(m_vdfXml.getClusterSummary(org.mockito.Mockito.any(org.apache.ambari.server.state.Cluster.class), org.mockito.Mockito.any(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).thenReturn(m_clusterVersionSummary);
        org.mockito.Mockito.when(m_clusterVersionSummary.getAvailableServiceNames()).thenReturn(m_services.keySet());
        m_checkHelper.m_clusters = clusters;
        org.mockito.Mockito.when(m_checkHelper.m_repositoryVersionDAO.findByPK(org.mockito.Mockito.anyLong())).thenReturn(m_repositoryVersionEntity);
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        final org.apache.ambari.server.checks.ServicesMaintenanceModeCheck servicesMaintenanceModeCheck = new org.apache.ambari.server.checks.ServicesMaintenanceModeCheck();
        servicesMaintenanceModeCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        servicesMaintenanceModeCheck.ambariMetaInfo = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return org.mockito.Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            }
        };
        m_checkHelper.setMetaInfoProvider(servicesMaintenanceModeCheck.ambariMetaInfo);
        servicesMaintenanceModeCheck.checkHelperProvider = new com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.state.CheckHelper get() {
                return m_checkHelper;
            }
        };
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2"));
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        final org.apache.ambari.server.state.Service service = org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(java.util.Collections.singletonMap("service", service));
        org.mockito.Mockito.when(service.isClientOnlyService()).thenReturn(false);
        org.mockito.Mockito.when(service.getDesiredState()).thenReturn(org.apache.ambari.server.state.State.UNKNOWN);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = servicesMaintenanceModeCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.mockito.Mockito.when(service.getDesiredState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        check = servicesMaintenanceModeCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
    }
}