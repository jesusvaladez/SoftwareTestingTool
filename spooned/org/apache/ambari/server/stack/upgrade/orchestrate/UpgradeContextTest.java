package org.apache.ambari.server.stack.upgrade.orchestrate;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
public class UpgradeContextTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String HDFS_SERVICE_NAME = "HDFS";

    private static final java.lang.String ZOOKEEPER_SERVICE_NAME = "ZOOKEEPER";

    @org.easymock.Mock
    private org.apache.ambari.server.orm.entities.UpgradeEntity m_completedRevertableUpgrade;

    @org.easymock.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_targetRepositoryVersion;

    @org.easymock.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_sourceRepositoryVersion;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Cluster m_cluster;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Service m_hdfsService;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Service m_zookeeperService;

    @org.easymock.Mock
    private org.apache.ambari.server.orm.dao.UpgradeDAO m_upgradeDAO;

    @org.easymock.Mock
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO m_repositoryVersionDAO;

    @org.easymock.Mock
    private org.apache.ambari.server.state.repository.ClusterVersionSummary m_clusterVersionSummary;

    @org.easymock.Mock
    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    @org.easymock.Mock
    private org.apache.ambari.server.api.services.AmbariMetaInfo m_ambariMetaInfo;

    private java.util.List<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> m_upgradeHistory = new java.util.ArrayList<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> m_services = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injectMocks(this);
        EasyMock.expect(m_ambariMetaInfo.getUpgradePacks(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(m_sourceRepositoryVersion.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(m_sourceRepositoryVersion.getStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.6")).anyTimes();
        EasyMock.expect(m_sourceRepositoryVersion.getVersion()).andReturn("2.6.0.0").anyTimes();
        EasyMock.expect(m_targetRepositoryVersion.getId()).andReturn(99L).anyTimes();
        EasyMock.expect(m_targetRepositoryVersion.getStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.6")).anyTimes();
        EasyMock.expect(m_targetRepositoryVersion.getVersion()).andReturn("2.6.0.2").anyTimes();
        org.apache.ambari.server.orm.entities.UpgradeHistoryEntity upgradeHistoryEntity = createNiceMock(org.apache.ambari.server.orm.entities.UpgradeHistoryEntity.class);
        EasyMock.expect(upgradeHistoryEntity.getServiceName()).andReturn(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.HDFS_SERVICE_NAME).anyTimes();
        EasyMock.expect(upgradeHistoryEntity.getFromReposistoryVersion()).andReturn(m_sourceRepositoryVersion).anyTimes();
        EasyMock.expect(upgradeHistoryEntity.getTargetRepositoryVersion()).andReturn(m_targetRepositoryVersion).anyTimes();
        m_upgradeHistory = com.google.common.collect.Lists.newArrayList(upgradeHistoryEntity);
        EasyMock.expect(m_repositoryVersionDAO.findByPK(1L)).andReturn(m_sourceRepositoryVersion).anyTimes();
        EasyMock.expect(m_repositoryVersionDAO.findByPK(99L)).andReturn(m_targetRepositoryVersion).anyTimes();
        EasyMock.expect(m_upgradeDAO.findUpgrade(1L)).andReturn(m_completedRevertableUpgrade).anyTimes();
        EasyMock.expect(m_upgradeDAO.findLastUpgradeForCluster(org.easymock.EasyMock.anyLong(), EasyMock.eq(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE))).andReturn(m_completedRevertableUpgrade).anyTimes();
        EasyMock.expect(m_completedRevertableUpgrade.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(m_completedRevertableUpgrade.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).anyTimes();
        EasyMock.expect(m_completedRevertableUpgrade.getRepositoryVersion()).andReturn(m_targetRepositoryVersion).anyTimes();
        EasyMock.expect(m_completedRevertableUpgrade.getOrchestration()).andReturn(org.apache.ambari.spi.RepositoryType.PATCH).anyTimes();
        EasyMock.expect(m_completedRevertableUpgrade.getHistory()).andReturn(m_upgradeHistory).anyTimes();
        EasyMock.expect(m_completedRevertableUpgrade.getUpgradePackage()).andReturn("myUpgradePack").anyTimes();
        EasyMock.expect(m_completedRevertableUpgrade.getUpgradePackStackId()).andReturn(new org.apache.ambari.server.state.StackId(((java.lang.String) (null)))).anyTimes();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity hdfsRepositoryVersion = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(hdfsRepositoryVersion.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(hdfsRepositoryVersion.getStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.6")).anyTimes();
        EasyMock.expect(m_hdfsService.getDesiredRepositoryVersion()).andReturn(hdfsRepositoryVersion).anyTimes();
        EasyMock.expect(m_zookeeperService.getDesiredRepositoryVersion()).andReturn(hdfsRepositoryVersion).anyTimes();
        EasyMock.expect(m_cluster.getService(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.HDFS_SERVICE_NAME)).andReturn(m_hdfsService).anyTimes();
        m_services.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.HDFS_SERVICE_NAME, m_hdfsService);
        EasyMock.expect(m_cluster.getServices()).andReturn(m_services).anyTimes();
        EasyMock.expect(m_cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(m_cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.expect(m_cluster.getUpgradeInProgress()).andReturn(null).atLeastOnce();
        EasyMock.expect(m_vdfXml.getClusterSummary(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).andReturn(m_clusterVersionSummary).anyTimes();
    }

    @org.junit.Test
    public void testFullUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(m_targetRepositoryVersion.getType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).atLeastOnce();
        EasyMock.expect(upgradeHelper.suggestUpgradePack(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.stack.upgrade.Direction.class), org.easymock.EasyMock.anyObject(org.apache.ambari.spi.upgrade.UpgradeType.class), org.easymock.EasyMock.anyString())).andReturn(upgradePack).once();
        replayAll();
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, m_targetRepositoryVersion.getId().toString());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.STANDARD, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertFalse(context.isPatchRevert());
        junit.framework.Assert.assertFalse(context.getUpgradeSummary().isSwitchBits);
        verifyAll();
    }

    @org.junit.Test
    public void testPatchUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(m_clusterVersionSummary.getAvailableServiceNames()).andReturn(com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.HDFS_SERVICE_NAME)).once();
        EasyMock.expect(m_targetRepositoryVersion.getType()).andReturn(org.apache.ambari.spi.RepositoryType.PATCH).atLeastOnce();
        EasyMock.expect(m_targetRepositoryVersion.getRepositoryXml()).andReturn(m_vdfXml).once();
        EasyMock.expect(upgradeHelper.suggestUpgradePack(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.stack.upgrade.Direction.class), org.easymock.EasyMock.anyObject(org.apache.ambari.spi.upgrade.UpgradeType.class), org.easymock.EasyMock.anyString())).andReturn(upgradePack).once();
        EasyMock.expect(m_cluster.getService(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.ZOOKEEPER_SERVICE_NAME)).andReturn(m_zookeeperService).anyTimes();
        m_services.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.ZOOKEEPER_SERVICE_NAME, m_zookeeperService);
        junit.framework.Assert.assertEquals(2, m_services.size());
        replayAll();
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, m_targetRepositoryVersion.getId().toString());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertFalse(context.isPatchRevert());
        junit.framework.Assert.assertTrue(context.getUpgradeSummary().isSwitchBits);
        verifyAll();
    }

    @org.junit.Test
    public void testMaintUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(m_clusterVersionSummary.getAvailableServiceNames()).andReturn(com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.HDFS_SERVICE_NAME)).once();
        EasyMock.expect(m_targetRepositoryVersion.getType()).andReturn(org.apache.ambari.spi.RepositoryType.MAINT).atLeastOnce();
        EasyMock.expect(m_targetRepositoryVersion.getRepositoryXml()).andReturn(m_vdfXml).once();
        EasyMock.expect(upgradeHelper.suggestUpgradePack(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.stack.upgrade.Direction.class), org.easymock.EasyMock.anyObject(org.apache.ambari.spi.upgrade.UpgradeType.class), org.easymock.EasyMock.anyString())).andReturn(upgradePack).once();
        EasyMock.expect(m_cluster.getService(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.ZOOKEEPER_SERVICE_NAME)).andReturn(m_zookeeperService).anyTimes();
        m_services.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.ZOOKEEPER_SERVICE_NAME, m_zookeeperService);
        junit.framework.Assert.assertEquals(2, m_services.size());
        replayAll();
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, m_targetRepositoryVersion.getId().toString());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.MAINT, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertFalse(context.isPatchRevert());
        verifyAll();
    }

    @org.junit.Test
    public void testRevert() throws java.lang.Exception {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> map = com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack>builder().put("myUpgradePack", upgradePack).build();
        EasyMock.expect(ami.getUpgradePacks(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(map).anyTimes();
        EasyMock.expect(m_upgradeDAO.findRevertable(1L)).andReturn(m_completedRevertableUpgrade).once();
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID, "1");
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertTrue(context.isPatchRevert());
        junit.framework.Assert.assertTrue(context.getUpgradeSummary().isSwitchBits);
        verifyAll();
    }

    @org.junit.Test
    public void testRevertEU() throws java.lang.Exception {
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> map = com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack>builder().put("myUpgradePack", upgradePack).build();
        EasyMock.expect(ami.getUpgradePacks(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(map).anyTimes();
        EasyMock.expect(m_upgradeDAO.findRevertable(1L)).andReturn(m_completedRevertableUpgrade).once();
        EasyMock.expect(m_completedRevertableUpgrade.getUpgradeType()).andReturn(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING);
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID, "1");
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, context.getType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertTrue(context.isPatchRevert());
        verifyAll();
    }

    @org.junit.Test
    public void testRevertWithDeletedService() throws java.lang.Exception {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.orm.entities.UpgradeHistoryEntity upgradeHistoryEntity = createNiceMock(org.apache.ambari.server.orm.entities.UpgradeHistoryEntity.class);
        EasyMock.expect(upgradeHistoryEntity.getServiceName()).andReturn(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.ZOOKEEPER_SERVICE_NAME).anyTimes();
        EasyMock.expect(upgradeHistoryEntity.getFromReposistoryVersion()).andReturn(m_sourceRepositoryVersion).anyTimes();
        EasyMock.expect(upgradeHistoryEntity.getTargetRepositoryVersion()).andReturn(m_targetRepositoryVersion).anyTimes();
        m_upgradeHistory.add(upgradeHistoryEntity);
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> map = com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack>builder().put("myUpgradePack", upgradePack).build();
        EasyMock.expect(ami.getUpgradePacks(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(map).anyTimes();
        EasyMock.expect(m_upgradeDAO.findRevertable(1L)).andReturn(m_completedRevertableUpgrade).once();
        m_services.remove(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.HDFS_SERVICE_NAME);
        EasyMock.expect(m_cluster.getService(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.ZOOKEEPER_SERVICE_NAME)).andReturn(m_zookeeperService).anyTimes();
        m_services.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextTest.ZOOKEEPER_SERVICE_NAME, m_zookeeperService);
        junit.framework.Assert.assertEquals(1, m_services.size());
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID, "1");
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertTrue(context.isPatchRevert());
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testWrongUpgradeBeingReverted() throws java.lang.Exception {
        java.lang.Long upgradeIdBeingReverted = 1L;
        java.lang.Long upgradeIdWhichCanBeReverted = 99L;
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(upgradeHelper.suggestUpgradePack(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.stack.upgrade.Direction.class), org.easymock.EasyMock.anyObject(org.apache.ambari.spi.upgrade.UpgradeType.class), org.easymock.EasyMock.anyString())).andReturn(upgradePack).once();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersionEntity.getVersion()).andReturn("1.2.3.4").anyTimes();
        org.apache.ambari.server.orm.entities.UpgradeEntity wrongRevertableUpgrade = createNiceMock(org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        EasyMock.expect(wrongRevertableUpgrade.getId()).andReturn(upgradeIdWhichCanBeReverted).atLeastOnce();
        EasyMock.expect(wrongRevertableUpgrade.getRepositoryVersion()).andReturn(repositoryVersionEntity).atLeastOnce();
        EasyMock.expect(m_upgradeDAO.findRevertable(1L)).andReturn(wrongRevertableUpgrade).once();
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID, upgradeIdBeingReverted.toString());
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertTrue(context.isPatchRevert());
        verifyAll();
    }

    @org.junit.Test
    public void testDowngradeForPatch() throws java.lang.Exception {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> map = com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack>builder().put("myUpgradePack", upgradePack).build();
        EasyMock.expect(ami.getUpgradePacks(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(map).anyTimes();
        java.util.Map<java.lang.String, java.lang.Object> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING.name());
        requestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext(m_cluster, requestMap, null, upgradeHelper, m_upgradeDAO, m_repositoryVersionDAO, configHelper, ami);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, context.getDirection());
        junit.framework.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, context.getOrchestrationType());
        junit.framework.Assert.assertEquals(1, context.getSupportedServices().size());
        junit.framework.Assert.assertFalse(context.isPatchRevert());
        verifyAll();
    }
}