package org.apache.ambari.server.checks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.orm.models.HostComponentSummary.class)
public class InstallPackagesCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO = org.mockito.Mockito.mock(org.apache.ambari.server.orm.dao.HostVersionDAO.class);

    private final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = org.mockito.Mockito.mock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.mockito.Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    private org.apache.ambari.server.state.StackId targetStackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");

    private java.lang.String repositoryVersion = "2.2.6.0-1234";

    private java.lang.String clusterName = "cluster";

    final org.apache.ambari.spi.RepositoryVersion m_repositoryVersion = org.mockito.Mockito.mock(org.apache.ambari.spi.RepositoryVersion.class);

    final org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity = org.mockito.Mockito.mock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.mockito.Mockito.when(m_repositoryVersion.getId()).thenReturn(1L);
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(targetStackId.toString());
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn(repositoryVersion);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getVersion()).thenReturn(repositoryVersion);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getStackId()).thenReturn(targetStackId);
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.checks.InstallPackagesCheck installPackagesCheck = new org.apache.ambari.server.checks.InstallPackagesCheck();
        installPackagesCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        installPackagesCheck.ambariMetaInfo = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return ambariMetaInfo;
            }
        };
        installPackagesCheck.hostVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.HostVersionDAO get() {
                return hostVersionDAO;
            }
        };
        installPackagesCheck.repositoryVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.RepositoryVersionDAO get() {
                return repositoryVersionDAO;
            }
        };
        org.apache.ambari.server.orm.entities.StackEntity stack = new org.apache.ambari.server.orm.entities.StackEntity();
        stack.setStackName(stackId.getStackName());
        stack.setStackVersion(stackId.getStackVersion());
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> osEntities = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("rhel6");
        repoOsEntity.setAmbariManaged(true);
        osEntities.add(repoOsEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity rve = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity(stack, repositoryVersion, repositoryVersion, osEntities);
        org.mockito.Mockito.when(repositoryVersionDAO.findByStackNameAndVersion(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenReturn(rve);
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterName()).thenReturn(clusterName);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getCurrentStackVersion()).thenReturn(stackId);
        org.mockito.Mockito.when(clusters.getCluster(clusterName)).thenReturn(cluster);
        final java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
        hostNames.add("host1");
        hostNames.add("host2");
        hostNames.add("host3");
        final java.util.List<org.apache.ambari.server.state.Host> hosts = new java.util.ArrayList<>();
        final java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities = new java.util.ArrayList<>();
        for (java.lang.String hostName : hostNames) {
            org.apache.ambari.server.state.Host host = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
            org.mockito.Mockito.when(host.getHostName()).thenReturn(hostName);
            org.mockito.Mockito.when(host.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
            hosts.add(host);
            org.apache.ambari.server.orm.entities.HostVersionEntity hve = org.mockito.Mockito.mock(org.apache.ambari.server.orm.entities.HostVersionEntity.class);
            org.mockito.Mockito.when(hve.getRepositoryVersion()).thenReturn(rve);
            org.mockito.Mockito.when(hve.getState()).thenReturn(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
            hostVersionEntities.add(hve);
            org.mockito.Mockito.when(hostVersionDAO.findByHost(hostName)).thenReturn(java.util.Collections.singletonList(hve));
        }
        org.mockito.Mockito.when(cluster.getHosts()).thenReturn(hosts);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = installPackagesCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(hostVersionEntities.get(0).getState()).thenReturn(org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
        check = installPackagesCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertNotNull(check.getFailedOn());
        org.junit.Assert.assertTrue(check.getFailedOn().contains("host1"));
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
    }
}