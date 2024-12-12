package org.apache.ambari.server.checks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class HostsMasterMaintenanceCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = org.mockito.Mockito.mock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);

    private final org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper = org.mockito.Mockito.mock(org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.class);

    private final org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.mockito.Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.ClusterVersionSummary m_clusterVersionSummary;

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    @org.mockito.Mock
    private org.apache.ambari.spi.RepositoryVersion m_repositoryVersion;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity;

    final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> m_services = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_services.clear();
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getRepositoryXml()).thenReturn(m_vdfXml);
        org.mockito.Mockito.when(m_vdfXml.getClusterSummary(org.mockito.Mockito.any(org.apache.ambari.server.state.Cluster.class), org.mockito.Mockito.any(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).thenReturn(m_clusterVersionSummary);
        org.mockito.Mockito.when(m_clusterVersionSummary.getAvailableServiceNames()).thenReturn(m_services.keySet());
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn("1.0.0.0-1234");
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "1.0").getStackId());
        org.mockito.Mockito.when(m_repositoryVersionEntity.getVersion()).thenReturn("1.0.0.0-1234");
        org.mockito.Mockito.when(m_repositoryVersionEntity.getStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "1.0"));
        final java.lang.String upgradePackName = "upgrade_pack";
        final org.apache.ambari.server.checks.HostsMasterMaintenanceCheck hostsMasterMaintenanceCheck = new org.apache.ambari.server.checks.HostsMasterMaintenanceCheck();
        hostsMasterMaintenanceCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        hostsMasterMaintenanceCheck.repositoryVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.RepositoryVersionDAO get() {
                return repositoryVersionDAO;
            }
        };
        hostsMasterMaintenanceCheck.repositoryVersionHelper = new com.google.inject.Provider<org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper get() {
                return repositoryVersionHelper;
            }
        };
        hostsMasterMaintenanceCheck.ambariMetaInfo = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return ambariMetaInfo;
            }
        };
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        org.mockito.Mockito.when(cluster.getDesiredStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "1.0"));
        org.mockito.Mockito.when(repositoryVersionHelper.getUpgradePackageName(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString(), ((org.apache.ambari.spi.upgrade.UpgradeType) (org.mockito.Mockito.anyObject())))).thenReturn(null);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest checkRequest = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = hostsMasterMaintenanceCheck.perform(checkRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
        org.mockito.Mockito.when(repositoryVersionHelper.getUpgradePackageName(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString(), ((org.apache.ambari.spi.upgrade.UpgradeType) (org.mockito.Mockito.anyObject())))).thenReturn(upgradePackName);
        org.mockito.Mockito.when(ambariMetaInfo.getUpgradePacks(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenReturn(new java.util.HashMap<>());
        result = hostsMasterMaintenanceCheck.perform(checkRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
        final java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks = new java.util.HashMap<>();
        final org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = org.mockito.Mockito.mock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.mockito.Mockito.when(upgradePack.getName()).thenReturn(upgradePackName);
        upgradePacks.put(upgradePack.getName(), upgradePack);
        org.mockito.Mockito.when(ambariMetaInfo.getUpgradePacks(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenReturn(upgradePacks);
        org.mockito.Mockito.when(upgradePack.getTasks()).thenReturn(new java.util.HashMap<>());
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(new java.util.HashMap<>());
        org.mockito.Mockito.when(clusters.getHostsForCluster(org.mockito.Mockito.anyString())).thenReturn(new java.util.HashMap<>());
        result = hostsMasterMaintenanceCheck.perform(checkRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }
}