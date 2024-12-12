package org.apache.ambari.server.state;
import org.easymock.EasyMock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class CheckHelperTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private org.apache.ambari.server.state.CheckHelperTest.MockCheck m_mockCheck;

    private org.apache.ambari.spi.upgrade.UpgradeCheckDescription m_mockUpgradeCheckDescription = org.mockito.Mockito.mock(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.class);

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.ClusterVersionSummary m_clusterVersionSummary;

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    @org.mockito.Mock
    private org.apache.ambari.spi.RepositoryVersion m_repositoryVersion;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity;

    @org.mockito.Mock
    private java.lang.Object m_mockPerform;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDao;

    final java.util.Set<java.lang.String> m_services = new java.util.HashSet<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_mockCheck = new org.apache.ambari.server.state.CheckHelperTest.MockCheck();
        org.mockito.Mockito.when(m_mockPerform.toString()).thenReturn("Perform!");
        m_services.clear();
        org.mockito.Mockito.when(m_repositoryVersion.getId()).thenReturn(1L);
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(repositoryVersionDao.findByPK(org.mockito.Mockito.anyLong())).thenReturn(m_repositoryVersionEntity);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getRepositoryXml()).thenReturn(m_vdfXml);
        org.mockito.Mockito.when(m_vdfXml.getClusterSummary(org.mockito.Mockito.any(org.apache.ambari.server.state.Cluster.class), org.mockito.Mockito.any(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).thenReturn(m_clusterVersionSummary);
        org.mockito.Mockito.when(m_clusterVersionSummary.getAvailableServiceNames()).thenReturn(m_services);
    }

    @org.junit.Test
    public void testPreUpgradeCheck() throws java.lang.Exception {
        final org.apache.ambari.server.state.CheckHelper helper = new org.apache.ambari.server.state.CheckHelper();
        helper.clustersProvider = () -> clusters;
        helper.repositoryVersionDaoProvider = () -> repositoryVersionDao;
        org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> updateChecksRegistry = new java.util.ArrayList<>();
        org.easymock.EasyMock.expect(configuration.isUpgradePrecheckBypass()).andReturn(false);
        org.easymock.EasyMock.replay(configuration);
        updateChecksRegistry.add(m_mockCheck);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        helper.performChecks(request, updateChecksRegistry, configuration);
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, request.getResult(m_mockUpgradeCheckDescription));
    }

    @org.junit.Test
    public void testPreUpgradeCheckNotApplicable() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        m_services.add("KAFKA");
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(new java.util.HashMap<>());
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        final org.apache.ambari.server.state.CheckHelper helper = new org.apache.ambari.server.state.CheckHelper();
        helper.clustersProvider = () -> clusters;
        helper.repositoryVersionDaoProvider = () -> repositoryVersionDao;
        org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> updateChecksRegistry = new java.util.ArrayList<>();
        org.easymock.EasyMock.expect(configuration.isUpgradePrecheckBypass()).andReturn(false);
        org.easymock.EasyMock.replay(configuration);
        updateChecksRegistry.add(m_mockCheck);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, m_repositoryVersion, null, null);
        helper.performChecks(request, updateChecksRegistry, configuration);
        junit.framework.Assert.assertEquals(null, request.getResult(m_mockUpgradeCheckDescription));
        request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
    }

    @org.junit.Test
    public void testPreUpgradeCheckThrowsException() throws java.lang.Exception {
        final org.apache.ambari.server.state.CheckHelper helper = new org.apache.ambari.server.state.CheckHelper();
        helper.clustersProvider = () -> clusters;
        helper.repositoryVersionDaoProvider = () -> repositoryVersionDao;
        org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> updateChecksRegistry = new java.util.ArrayList<>();
        org.easymock.EasyMock.expect(configuration.isUpgradePrecheckBypass()).andReturn(false);
        org.easymock.EasyMock.replay(configuration);
        updateChecksRegistry.add(m_mockCheck);
        org.mockito.Mockito.when(m_mockPerform.toString()).thenThrow(new java.lang.RuntimeException());
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        helper.performChecks(request, updateChecksRegistry, configuration);
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, request.getResult(m_mockUpgradeCheckDescription));
    }

    @org.junit.Test
    public void testPreUpgradeCheckBypassesFailure() throws java.lang.Exception {
        final org.apache.ambari.server.state.CheckHelper helper = new org.apache.ambari.server.state.CheckHelper();
        helper.clustersProvider = () -> clusters;
        helper.repositoryVersionDaoProvider = () -> repositoryVersionDao;
        org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> updateChecksRegistry = new java.util.ArrayList<>();
        org.easymock.EasyMock.expect(configuration.isUpgradePrecheckBypass()).andReturn(true);
        org.easymock.EasyMock.replay(configuration);
        updateChecksRegistry.add(m_mockCheck);
        org.mockito.Mockito.when(m_mockPerform.toString()).thenThrow(new java.lang.RuntimeException());
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        helper.performChecks(request, updateChecksRegistry, configuration);
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.BYPASS, request.getResult(m_mockUpgradeCheckDescription));
    }

    @org.junit.Test
    public void testPreUpgradeCheckClusterMissing() throws java.lang.Exception {
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        final org.apache.ambari.server.state.Service service = org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class);
        m_services.add("KAFKA");
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(new java.util.HashMap<>());
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(clusters.getCluster(org.mockito.Mockito.anyString())).thenReturn(cluster);
        final org.apache.ambari.server.checks.MockCheckHelper helper = new org.apache.ambari.server.checks.MockCheckHelper();
        helper.m_clusters = clusters;
        helper.m_repositoryVersionDAO = repositoryVersionDao;
        helper.clustersProvider = () -> clusters;
        final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = org.mockito.Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        helper.metaInfoProvider = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return metaInfo;
            }
        };
        org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> updateChecksRegistry = new java.util.ArrayList<>();
        org.easymock.EasyMock.expect(configuration.isUpgradePrecheckBypass()).andReturn(false);
        org.easymock.EasyMock.replay(configuration);
        updateChecksRegistry.add(m_mockCheck);
        org.mockito.Mockito.when(m_mockPerform.toString()).thenThrow(new java.lang.RuntimeException());
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        helper.performChecks(request, updateChecksRegistry, configuration);
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, request.getResult(m_mockUpgradeCheckDescription));
    }

    @org.apache.ambari.annotations.UpgradeCheckInfo(required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING })
    class MockCheck extends org.apache.ambari.server.checks.ClusterCheck {
        protected MockCheck() {
            super(m_mockUpgradeCheckDescription);
            clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
                @java.lang.Override
                public org.apache.ambari.server.state.Clusters get() {
                    return clusters;
                }
            };
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> getApplicableServices() {
            return m_services;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
            m_mockPerform.toString();
            return new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        }
    }
}