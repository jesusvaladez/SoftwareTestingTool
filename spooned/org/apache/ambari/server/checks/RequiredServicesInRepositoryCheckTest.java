package org.apache.ambari.server.checks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RequiredServicesInRepositoryCheckTest {
    private static final java.lang.String CLUSTER_NAME = "c1";

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    @org.mockito.Mock
    private org.apache.ambari.spi.RepositoryVersion m_repositoryVersion;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity;

    private org.apache.ambari.server.checks.MockCheckHelper m_checkHelper = new org.apache.ambari.server.checks.MockCheckHelper();

    private org.apache.ambari.server.checks.RequiredServicesInRepositoryCheck m_requiredServicesCheck;

    private java.util.Set<java.lang.String> m_missingDependencies = com.google.common.collect.Sets.newTreeSet();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        final org.apache.ambari.server.state.Clusters clusters = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        m_requiredServicesCheck = new org.apache.ambari.server.checks.RequiredServicesInRepositoryCheck();
        m_requiredServicesCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(clusters.getCluster(org.apache.ambari.server.checks.RequiredServicesInRepositoryCheckTest.CLUSTER_NAME)).thenReturn(cluster);
        org.mockito.Mockito.when(m_repositoryVersion.getId()).thenReturn(1L);
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getRepositoryXml()).thenReturn(m_vdfXml);
        org.mockito.Mockito.when(m_vdfXml.getMissingDependencies(org.mockito.Mockito.eq(cluster), org.mockito.Mockito.any(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).thenReturn(m_missingDependencies);
        m_checkHelper.m_clusters = clusters;
        org.mockito.Mockito.when(m_checkHelper.m_repositoryVersionDAO.findByPK(org.mockito.Mockito.anyLong())).thenReturn(m_repositoryVersionEntity);
        final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = org.mockito.Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        m_requiredServicesCheck.ambariMetaInfo = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return metaInfo;
            }
        };
        m_requiredServicesCheck.checkHelperProvider = new com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.state.CheckHelper get() {
                return m_checkHelper;
            }
        };
    }

    @org.junit.Test
    public void testNoMissingServices() throws java.lang.Exception {
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.RequiredServicesInRepositoryCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_requiredServicesCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
    }

    @org.junit.Test
    public void testMissingRequiredService() throws java.lang.Exception {
        m_missingDependencies.add("BAR");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.RequiredServicesInRepositoryCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_requiredServicesCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
    }
}