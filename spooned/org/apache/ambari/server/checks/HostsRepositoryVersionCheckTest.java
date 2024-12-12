package org.apache.ambari.server.checks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class HostsRepositoryVersionCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO = org.mockito.Mockito.mock(org.apache.ambari.server.orm.dao.HostVersionDAO.class);

    private final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = org.mockito.Mockito.mock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);

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
        org.mockito.Mockito.when(m_repositoryVersionEntity.getVersion()).thenReturn(version);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getStackId()).thenReturn(stackId);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getRepositoryXml()).thenReturn(m_vdfXml);
        org.mockito.Mockito.when(m_vdfXml.getClusterSummary(org.mockito.Mockito.any(org.apache.ambari.server.state.Cluster.class), org.mockito.Mockito.any(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).thenReturn(m_clusterVersionSummary);
        org.mockito.Mockito.when(m_clusterVersionSummary.getAvailableServiceNames()).thenReturn(m_services.keySet());
        m_checkHelper.m_clusters = clusters;
        org.mockito.Mockito.when(m_checkHelper.m_repositoryVersionDAO.findByPK(org.mockito.Mockito.anyLong())).thenReturn(m_repositoryVersionEntity);
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        final org.apache.ambari.server.checks.HostsRepositoryVersionCheck hostsRepositoryVersionCheck = new org.apache.ambari.server.checks.HostsRepositoryVersionCheck();
        hostsRepositoryVersionCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        hostsRepositoryVersionCheck.repositoryVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.RepositoryVersionDAO get() {
                return repositoryVersionDAO;
            }
        };
        hostsRepositoryVersionCheck.hostVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.HostVersionDAO get() {
                return hostVersionDAO;
            }
        };
        hostsRepositoryVersionCheck.checkHelperProvider = new com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.state.CheckHelper get() {
                return m_checkHelper;
            }
        };
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getDesiredStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId());
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        final org.apache.ambari.server.state.Host host1 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host2 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host3 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.mockito.Mockito.when(host1.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host2.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        hosts.put("host1", host1);
        hosts.put("host2", host2);
        hosts.put("host3", host3);
        org.mockito.Mockito.when(clusters.getHostsForCluster("cluster")).thenReturn(hosts);
        org.mockito.Mockito.when(repositoryVersionDAO.findByStackAndVersion(org.mockito.Mockito.any(org.apache.ambari.server.state.StackId.class), org.mockito.Mockito.anyString())).thenReturn(null);
        org.mockito.Mockito.when(repositoryVersionDAO.findByStackAndVersion(org.mockito.Mockito.any(org.apache.ambari.server.orm.entities.StackEntity.class), org.mockito.Mockito.anyString())).thenReturn(null);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = hostsRepositoryVersionCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("HDP");
        stackEntity.setStackVersion("2.0.6");
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repositoryVersion.setStack(stackEntity);
        org.mockito.Mockito.when(repositoryVersionDAO.findByStackAndVersion(org.mockito.Mockito.any(org.apache.ambari.server.state.StackId.class), org.mockito.Mockito.anyString())).thenReturn(repositoryVersion);
        org.mockito.Mockito.when(repositoryVersionDAO.findByStackAndVersion(org.mockito.Mockito.any(org.apache.ambari.server.orm.entities.StackEntity.class), org.mockito.Mockito.anyString())).thenReturn(repositoryVersion);
        final org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion = new org.apache.ambari.server.orm.entities.HostVersionEntity();
        hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        org.mockito.Mockito.when(hostVersionDAO.findByClusterStackVersionAndHost(org.mockito.Mockito.anyString(), org.mockito.Mockito.any(org.apache.ambari.server.state.StackId.class), org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenReturn(hostVersion);
        check = hostsRepositoryVersionCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
    }

    @org.junit.Test
    public void testPerformWithVersion() throws java.lang.Exception {
        final org.apache.ambari.server.checks.HostsRepositoryVersionCheck hostsRepositoryVersionCheck = new org.apache.ambari.server.checks.HostsRepositoryVersionCheck();
        hostsRepositoryVersionCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        hostsRepositoryVersionCheck.repositoryVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.RepositoryVersionDAO get() {
                return repositoryVersionDAO;
            }
        };
        hostsRepositoryVersionCheck.hostVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.HostVersionDAO get() {
                return hostVersionDAO;
            }
        };
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getDesiredStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId());
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        final org.apache.ambari.server.state.Host host1 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host2 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host3 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.mockito.Mockito.when(host1.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host2.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host1.getHostName()).thenReturn("host1");
        org.mockito.Mockito.when(host2.getHostName()).thenReturn("host2");
        org.mockito.Mockito.when(host3.getHostName()).thenReturn("host3");
        hosts.put("host1", host1);
        hosts.put("host2", host2);
        hosts.put("host3", host3);
        org.mockito.Mockito.when(clusters.getHostsForCluster("cluster")).thenReturn(hosts);
        org.apache.ambari.server.orm.entities.HostVersionEntity hve = new org.apache.ambari.server.orm.entities.HostVersionEntity();
        hve.setRepositoryVersion(m_repositoryVersionEntity);
        hve.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        for (java.lang.String hostName : hosts.keySet()) {
            org.mockito.Mockito.when(hostVersionDAO.findByClusterStackVersionAndHost("cluster", m_repositoryVersionEntity.getStackId(), m_repositoryVersion.getVersion(), hostName)).thenReturn(hve);
        }
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = hostsRepositoryVersionCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
    }

    @org.junit.Test
    public void testPerformWithVersionNotRequired() throws java.lang.Exception {
        final org.apache.ambari.server.checks.HostsRepositoryVersionCheck hostsRepositoryVersionCheck = new org.apache.ambari.server.checks.HostsRepositoryVersionCheck();
        hostsRepositoryVersionCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        hostsRepositoryVersionCheck.repositoryVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.RepositoryVersionDAO get() {
                return repositoryVersionDAO;
            }
        };
        hostsRepositoryVersionCheck.hostVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.HostVersionDAO get() {
                return hostVersionDAO;
            }
        };
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getDesiredStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId());
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        final org.apache.ambari.server.state.Host host1 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host2 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        final org.apache.ambari.server.state.Host host3 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.mockito.Mockito.when(host1.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host2.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(1L)).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host1.getHostName()).thenReturn("host1");
        org.mockito.Mockito.when(host2.getHostName()).thenReturn("host2");
        org.mockito.Mockito.when(host3.getHostName()).thenReturn("host3");
        hosts.put("host1", host1);
        hosts.put("host2", host2);
        hosts.put("host3", host3);
        org.mockito.Mockito.when(clusters.getHostsForCluster("cluster")).thenReturn(hosts);
        org.apache.ambari.server.orm.entities.HostVersionEntity hve = new org.apache.ambari.server.orm.entities.HostVersionEntity();
        hve.setRepositoryVersion(m_repositoryVersionEntity);
        hve.setState(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED);
        for (java.lang.String hostName : hosts.keySet()) {
            org.mockito.Mockito.when(hostVersionDAO.findByClusterStackVersionAndHost("cluster", m_repositoryVersionEntity.getStackId(), m_repositoryVersion.getVersion(), hostName)).thenReturn(hve);
        }
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = hostsRepositoryVersionCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
    }
}