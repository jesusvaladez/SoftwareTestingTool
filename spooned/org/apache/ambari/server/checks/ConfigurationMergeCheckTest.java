package org.apache.ambari.server.checks;
import org.easymock.EasyMock;
import org.mockito.Mockito;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class ConfigurationMergeCheckTest {
    private static final java.lang.String CONFIG_FILE = "hdfs-site.xml";

    private static final java.lang.String CONFIG_TYPE = "hdfs-site";

    private static final java.lang.String CONFIG_PROPERTY = "hdfs.property";

    private org.apache.ambari.server.state.Clusters clusters = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);

    private java.util.Map<java.lang.String, java.lang.String> m_configMap = new java.util.HashMap<>();

    private static final org.apache.ambari.server.state.StackId stackId_1_0 = new org.apache.ambari.server.state.StackId("HDP-1.0");

    final org.apache.ambari.spi.RepositoryVersion m_repositoryVersion = org.mockito.Mockito.mock(org.apache.ambari.spi.RepositoryVersion.class);

    final org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity = org.mockito.Mockito.mock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.stackId_1_0).anyTimes();
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put("HDFS", org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Service.class));
            }
        }).anyTimes();
        m_configMap.put(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY, "1024m");
        org.apache.ambari.server.state.Config config = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(config.getProperties()).andReturn(m_configMap).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_TYPE)).andReturn(config).anyTimes();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "1.1");
        java.lang.String version = "1.1.0.0-1234";
        org.mockito.Mockito.when(m_repositoryVersion.getId()).thenReturn(1L);
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(stackId.toString());
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn(version);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getVersion()).thenReturn(version);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getStackId()).thenReturn(stackId);
        EasyMock.replay(clusters, cluster, config);
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        org.apache.ambari.server.checks.ConfigurationMergeCheck cmc = new org.apache.ambari.server.checks.ConfigurationMergeCheck();
        final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = org.easymock.EasyMock.createMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.expect(repositoryVersionDAO.findByStackNameAndVersion("HDP", "1.0")).andReturn(createFor("1.0")).anyTimes();
        EasyMock.expect(repositoryVersionDAO.findByStackNameAndVersion("HDP", "1.1.0.0-1234")).andReturn(createFor("1.1")).anyTimes();
        EasyMock.replay(repositoryVersionDAO);
        cmc.repositoryVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.RepositoryVersionDAO get() {
                return repositoryVersionDAO;
            }
        };
        cmc.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        cmc.m_mergeHelper = new org.apache.ambari.server.state.ConfigMergeHelper();
        java.lang.reflect.Field field = org.apache.ambari.server.state.ConfigMergeHelper.class.getDeclaredField("m_clusters");
        field.setAccessible(true);
        field.set(cmc.m_mergeHelper, cmc.clustersProvider);
        final org.apache.ambari.server.api.services.AmbariMetaInfo ami = org.easymock.EasyMock.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        field = org.apache.ambari.server.state.ConfigMergeHelper.class.getDeclaredField("m_ambariMetaInfo");
        field.setAccessible(true);
        field.set(cmc.m_mergeHelper, new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return ami;
            }
        });
        org.apache.ambari.server.state.PropertyInfo pi10 = new org.apache.ambari.server.state.PropertyInfo();
        pi10.setFilename(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_FILE);
        pi10.setName(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY);
        pi10.setValue("1024");
        org.apache.ambari.server.state.PropertyInfo pi11 = new org.apache.ambari.server.state.PropertyInfo();
        pi11.setFilename(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_FILE);
        pi11.setName(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY);
        pi11.setValue("1024");
        EasyMock.expect(ami.getServiceProperties("HDP", "1.0", "HDFS")).andReturn(java.util.Collections.singleton(pi10)).anyTimes();
        EasyMock.expect(ami.getServiceProperties("HDP", "1.1", "HDFS")).andReturn(java.util.Collections.singleton(pi11)).anyTimes();
        EasyMock.expect(ami.getStackProperties(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.replay(ami);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = cmc.perform(request);
        org.junit.Assert.assertEquals("Expect no warnings", 0, check.getFailedOn().size());
        m_configMap.put(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY, "1025m");
        pi11.setValue("1026");
        check = cmc.perform(request);
        org.junit.Assert.assertEquals("Expect warning when user-set has changed from new default", 1, check.getFailedOn().size());
        org.junit.Assert.assertEquals(1, check.getFailedDetail().size());
        org.apache.ambari.server.checks.ConfigurationMergeCheck.MergeDetail detail = ((org.apache.ambari.server.checks.ConfigurationMergeCheck.MergeDetail) (check.getFailedDetail().get(0)));
        org.junit.Assert.assertEquals("1025m", detail.current);
        org.junit.Assert.assertEquals("1026m", detail.new_stack_value);
        org.junit.Assert.assertEquals("1025m", detail.result_value);
        org.junit.Assert.assertEquals(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_TYPE, detail.type);
        org.junit.Assert.assertEquals(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY, detail.property);
        pi11.setName(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY + ".foo");
        check = cmc.perform(request);
        org.junit.Assert.assertEquals("Expect no warning when user new stack is empty", 0, check.getFailedOn().size());
        org.junit.Assert.assertEquals(0, check.getFailedDetail().size());
        pi11.setName(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY);
        pi10.setName(org.apache.ambari.server.checks.ConfigurationMergeCheckTest.CONFIG_PROPERTY + ".foo");
        check = cmc.perform(request);
        org.junit.Assert.assertEquals("Expect warning when user old stack is empty, and value changed", 1, check.getFailedOn().size());
        org.junit.Assert.assertEquals(1, check.getFailedDetail().size());
        detail = ((org.apache.ambari.server.checks.ConfigurationMergeCheck.MergeDetail) (check.getFailedDetail().get(0)));
        org.junit.Assert.assertEquals("1025m", detail.current);
        org.junit.Assert.assertEquals("1026m", detail.new_stack_value);
    }

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity createFor(final java.lang.String stackVersion) {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setStack(new org.apache.ambari.server.orm.entities.StackEntity() {
            @java.lang.Override
            public java.lang.String getStackVersion() {
                return stackVersion;
            }

            @java.lang.Override
            public java.lang.String getStackName() {
                return "HDP";
            }
        });
        return entity;
    }
}