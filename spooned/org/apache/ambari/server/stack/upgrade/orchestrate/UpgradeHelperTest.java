package org.apache.ambari.server.stack.upgrade.orchestrate;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
public class UpgradeHelperTest extends org.easymock.EasyMockSupport {
    private static final org.apache.ambari.server.state.StackId STACK_ID_HDP_211 = new org.apache.ambari.server.state.StackId("HDP-2.1.1");

    private static final org.apache.ambari.server.state.StackId STACK_ID_HDP_220 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");

    private static final java.lang.String UPGRADE_VERSION = "2.2.1.0-1234";

    private static final java.lang.String DOWNGRADE_VERSION = "2.2.0.0-1234";

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private org.apache.ambari.server.stack.StackManagerMock stackManagerMock;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private org.apache.ambari.server.stack.MasterHostResolver m_masterHostResolver;

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper m_upgradeHelper;

    private org.apache.ambari.server.state.ConfigHelper m_configHelper;

    private org.apache.ambari.server.controller.AmbariManagementController m_managementController;

    private com.google.gson.Gson m_gson = new com.google.gson.Gson();

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2110;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2200;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2210;

    private org.apache.ambari.server.stack.HostsType namenodeHosts = org.apache.ambari.server.stack.HostsType.highAvailability("h1", "h2", com.google.common.collect.Sets.newLinkedHashSet(java.util.Arrays.asList("h1", "h2")));

    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    private void setConfigMocks() throws java.lang.Exception {
        m_configHelper = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(m_configHelper.getPlaceholderValueFromDesiredConfigurations(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.eq("{{foo/bar}}"))).andReturn("placeholder-rendered-properly").anyTimes();
        EasyMock.expect(m_configHelper.getEffectiveDesiredTags(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(m_configHelper.getHostActualConfigs(org.easymock.EasyMock.anyLong())).andReturn(new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, java.util.Collections.emptySortedMap())).anyTimes();
        EasyMock.expect(m_configHelper.getChangedConfigTypes(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class), EasyMock.anyLong(), EasyMock.anyLong(), EasyMock.anyString())).andReturn(java.util.Collections.emptyMap()).anyTimes();
    }

    @org.junit.Before
    public void before() throws java.lang.Exception {
        setConfigMocks();
        EasyMock.replay(m_configHelper);
        final org.apache.ambari.server.orm.InMemoryDefaultTestModule injectorModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                super.configure();
            }
        };
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.MockModule mockModule = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.MockModule();
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(injectorModule).with(mockModule));
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(injector);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        stackManagerMock = ((org.apache.ambari.server.stack.StackManagerMock) (ambariMetaInfo.getStackManager()));
        m_upgradeHelper = injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        m_masterHostResolver = org.easymock.EasyMock.createMock(org.apache.ambari.server.stack.MasterHostResolver.class);
        m_managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        repositoryVersion2110 = helper.getOrCreateRepositoryVersion(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.STACK_ID_HDP_211, "2.1.1.0-1234");
        repositoryVersion2200 = helper.getOrCreateRepositoryVersion(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.STACK_ID_HDP_220, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.DOWNGRADE_VERSION);
        repositoryVersion2210 = helper.getOrCreateRepositoryVersion(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.STACK_ID_HDP_220, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.UPGRADE_VERSION);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testSuggestUpgradePack() throws java.lang.Exception {
        final java.lang.String clusterName = "c1";
        final org.apache.ambari.server.state.StackId sourceStackId = new org.apache.ambari.server.state.StackId("HDP", "2.1.1");
        final org.apache.ambari.server.state.StackId targetStackId = new org.apache.ambari.server.state.StackId("HDP", "2.2.0");
        final org.apache.ambari.server.stack.upgrade.Direction upgradeDirection = org.apache.ambari.server.stack.upgrade.Direction.UPGRADE;
        final org.apache.ambari.spi.upgrade.UpgradeType upgradeType = org.apache.ambari.spi.upgrade.UpgradeType.ROLLING;
        makeCluster();
        try {
            java.lang.String preferredUpgradePackName = "upgrade_test";
            org.apache.ambari.server.stack.upgrade.UpgradePack up = m_upgradeHelper.suggestUpgradePack(clusterName, sourceStackId, targetStackId, upgradeDirection, upgradeType, preferredUpgradePackName);
            org.junit.Assert.assertEquals(upgradeType, up.getType());
        } catch (org.apache.ambari.server.AmbariException e) {
            org.junit.Assert.assertTrue(false);
        }
    }

    @org.junit.Test
    public void testSuggestUpgradePackFromSourceStack() throws java.lang.Exception {
        final java.lang.String clusterName = "c1";
        final org.apache.ambari.server.state.StackId sourceStackId = new org.apache.ambari.server.state.StackId("HDP", "2.1.1");
        final org.apache.ambari.server.state.StackId targetStackId = new org.apache.ambari.server.state.StackId("HDP", "2.2.0");
        final org.apache.ambari.server.stack.upgrade.Direction upgradeDirection = org.apache.ambari.server.stack.upgrade.Direction.UPGRADE;
        final org.apache.ambari.spi.upgrade.UpgradeType upgradeType = org.apache.ambari.spi.upgrade.UpgradeType.ROLLING;
        makeCluster();
        try {
            org.apache.ambari.server.stack.upgrade.UpgradePack up = m_upgradeHelper.suggestUpgradePack(clusterName, sourceStackId, targetStackId, upgradeDirection, upgradeType, null);
            org.junit.Assert.assertEquals(upgradeType, up.getType());
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
            org.junit.Assert.fail("unexpected exception suggesting upgrade pack");
        }
    }

    @org.junit.Test
    public void testUpgradeOrchestration() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService("HDP", "2.2.0", "ZOOKEEPER");
        si.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo ci = si.getComponentByName("ZOOKEEPER_SERVER");
        ci.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.junit.Assert.assertEquals("PRE_CLUSTER", groups.get(0).name);
        org.junit.Assert.assertEquals("ZOOKEEPER", groups.get(1).name);
        org.junit.Assert.assertEquals("CORE_MASTER", groups.get(2).name);
        org.junit.Assert.assertEquals("CORE_SLAVES", groups.get(3).name);
        org.junit.Assert.assertEquals("HIVE", groups.get(4).name);
        org.junit.Assert.assertEquals("OOZIE", groups.get(5).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder = groups.get(2);
        boolean found = false;
        for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : holder.items) {
            if (sw.getTasksJson().contains("Upgrading your database")) {
                found = true;
            }
        }
        org.junit.Assert.assertTrue("Expected to find replaced text for Upgrading", found);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(1);
        org.junit.Assert.assertTrue(group.items.get(1).getText().contains("ZooKeeper1 Server2"));
        org.junit.Assert.assertEquals(group.items.get(5).getText(), "Service Check Zk");
        group = groups.get(3);
        org.junit.Assert.assertEquals(8, group.items.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw = group.items.get(3);
        org.junit.Assert.assertEquals("Validate Partial Upgrade", sw.getText());
        org.junit.Assert.assertEquals(1, sw.getTasks().size());
        org.junit.Assert.assertEquals(1, sw.getTasks().get(0).getTasks().size());
        org.apache.ambari.server.stack.upgrade.Task t = sw.getTasks().get(0).getTasks().get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ManualTask.class, t.getClass());
        org.apache.ambari.server.stack.upgrade.ManualTask mt = ((org.apache.ambari.server.stack.upgrade.ManualTask) (t));
        org.junit.Assert.assertTrue(mt.messages.get(0).contains("DataNode and NodeManager"));
        org.junit.Assert.assertNotNull(mt.structuredOut);
        org.junit.Assert.assertTrue(mt.structuredOut.contains("DATANODE"));
        org.junit.Assert.assertTrue(mt.structuredOut.contains("NODEMANAGER"));
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder postGroup = groups.get(6);
        org.junit.Assert.assertEquals("POST_CLUSTER", postGroup.name);
        org.junit.Assert.assertEquals("Finalize Upgrade", postGroup.title);
        org.junit.Assert.assertEquals(3, postGroup.items.size());
        org.junit.Assert.assertEquals("Confirm Finalize", postGroup.items.get(0).getText());
        org.junit.Assert.assertEquals("Execute HDFS Finalize", postGroup.items.get(1).getText());
        org.junit.Assert.assertEquals("Save Cluster State", postGroup.items.get(2).getText());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, postGroup.items.get(2).getType());
        org.junit.Assert.assertEquals(4, groups.get(0).items.size());
        org.junit.Assert.assertEquals(6, groups.get(1).items.size());
        org.junit.Assert.assertEquals(9, groups.get(2).items.size());
        org.junit.Assert.assertEquals(8, groups.get(3).items.size());
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    @org.junit.Test
    public void testPartialUpgradeOrchestration() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService("HDP", "2.2.0", "ZOOKEEPER");
        si.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo ci = si.getComponentByName("ZOOKEEPER_SERVER");
        ci.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_partial"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_partial");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        java.util.Set<java.lang.String> services = java.util.Collections.singleton("ZOOKEEPER");
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2210, org.apache.ambari.spi.RepositoryType.PATCH, services);
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groupings = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertEquals(8, groupings.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.UpgradeScope.COMPLETE, groupings.get(6).scope);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(3, groups.size());
        org.junit.Assert.assertEquals("PRE_CLUSTER", groups.get(0).name);
        org.junit.Assert.assertEquals("ZOOKEEPER", groups.get(1).name);
        org.junit.Assert.assertEquals("POST_CLUSTER", groups.get(2).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(1);
        org.junit.Assert.assertTrue(group.items.get(1).getText().contains("ZooKeeper1 Server2"));
        org.junit.Assert.assertEquals("Service Check Zk", group.items.get(6).getText());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder postGroup = groups.get(2);
        org.junit.Assert.assertEquals("POST_CLUSTER", postGroup.name);
        org.junit.Assert.assertEquals("Finalize Upgrade", postGroup.title);
        org.junit.Assert.assertEquals(2, postGroup.items.size());
        org.junit.Assert.assertEquals("Confirm Finalize", postGroup.items.get(0).getText());
        org.junit.Assert.assertEquals("Save Cluster State", postGroup.items.get(1).getText());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, postGroup.items.get(1).getType());
        org.junit.Assert.assertEquals(2, groups.get(0).items.size());
        org.junit.Assert.assertEquals(7, groups.get(1).items.size());
        org.junit.Assert.assertEquals(2, groups.get(2).items.size());
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testCompleteUpgradeOrchestration() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService("HDP", "2.2.0", "ZOOKEEPER");
        si.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo ci = si.getComponentByName("ZOOKEEPER_SERVER");
        ci.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_partial"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_partial");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2210, org.apache.ambari.spi.RepositoryType.STANDARD, java.util.Collections.singleton("ZOOKEEPER"));
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groupings = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertEquals(8, groupings.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.UpgradeScope.COMPLETE, groupings.get(6).scope);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(4, groups.size());
        org.junit.Assert.assertEquals("PRE_CLUSTER", groups.get(0).name);
        org.junit.Assert.assertEquals("ZOOKEEPER", groups.get(1).name);
        org.junit.Assert.assertEquals("ALL_HOSTS", groups.get(2).name);
        org.junit.Assert.assertEquals("POST_CLUSTER", groups.get(3).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(1);
        org.junit.Assert.assertTrue(group.items.get(1).getText().contains("ZooKeeper1 Server2"));
        org.junit.Assert.assertEquals("Service Check Zk", group.items.get(5).getText());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder postGroup = groups.get(3);
        org.junit.Assert.assertEquals("POST_CLUSTER", postGroup.name);
        org.junit.Assert.assertEquals("Finalize Upgrade", postGroup.title);
        org.junit.Assert.assertEquals(2, postGroup.items.size());
        org.junit.Assert.assertEquals("Confirm Finalize", postGroup.items.get(0).getText());
        org.junit.Assert.assertEquals("Save Cluster State", postGroup.items.get(1).getText());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, postGroup.items.get(1).getType());
        org.junit.Assert.assertEquals(2, groups.get(0).items.size());
        org.junit.Assert.assertEquals(6, groups.get(1).items.size());
        org.junit.Assert.assertEquals(1, groups.get(2).items.size());
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testUpgradeServerActionOrchestration() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService("HDP", "2.1.1", "ZOOKEEPER");
        si.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo ci = si.getComponentByName("ZOOKEEPER_SERVER");
        ci.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_server_action_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_server_action_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(1, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(0);
        org.junit.Assert.assertEquals("CLUSTER_SERVER_ACTIONS", group.name);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = group.items;
        org.junit.Assert.assertEquals(6, stageWrappers.size());
        org.junit.Assert.assertEquals("Pre Upgrade", stageWrappers.get(0).getText());
        org.junit.Assert.assertEquals("Pre Upgrade Zookeeper", stageWrappers.get(1).getText());
        org.junit.Assert.assertEquals("Configuring", stageWrappers.get(2).getText());
        org.junit.Assert.assertEquals("Configuring HDFS", stageWrappers.get(3).getText());
        org.junit.Assert.assertEquals("Calculating Properties", stageWrappers.get(4).getText());
        org.junit.Assert.assertEquals("Calculating HDFS Properties", stageWrappers.get(5).getText());
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testUpgradeOrchestrationWithHostsInMM() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService("HDP", "2.1.1", "ZOOKEEPER");
        si.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo ci = si.getComponentByName("ZOOKEEPER_SERVER");
        ci.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.state.Host hostInMaintenanceMode = cluster.getHosts().iterator().next();
        hostInMaintenanceMode.setMaintenanceState(cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContextNoReplay(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2210);
        org.apache.ambari.server.stack.MasterHostResolver masterHostResolver = new org.apache.ambari.server.stack.MasterHostResolver(cluster, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(masterHostResolver).anyTimes();
        EasyMock.replay(context);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        for (org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group : groups) {
            for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stageWrapper : group.items) {
                java.util.Set<java.lang.String> hosts = stageWrapper.getHosts();
                org.junit.Assert.assertFalse(hosts.contains(hostInMaintenanceMode.getHostName()));
            }
        }
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testNamenodeOrder() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder mastersGroup = groups.get(2);
        org.junit.Assert.assertEquals("CORE_MASTER", mastersGroup.name);
        java.util.List<java.lang.String> orderedNameNodes = new java.util.LinkedList<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : mastersGroup.items) {
            if (sw.getType().equals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART) && sw.getText().toLowerCase().contains("NameNode".toLowerCase())) {
                for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : sw.getTasks()) {
                    for (java.lang.String hostName : tw.getHosts()) {
                        orderedNameNodes.add(hostName);
                    }
                }
            }
        }
        org.junit.Assert.assertEquals(2, orderedNameNodes.size());
        org.junit.Assert.assertEquals("h2", orderedNameNodes.get(0));
        org.junit.Assert.assertEquals("h1", orderedNameNodes.get(1));
    }

    @org.junit.Test
    public void testNamenodeFederationOrder() throws java.lang.Exception {
        namenodeHosts = org.apache.ambari.server.stack.HostsType.federated(java.util.Arrays.asList(new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts("h1", java.util.Arrays.asList("h2", "h3")), new org.apache.ambari.server.stack.HostsType.HighAvailabilityHosts("h4", java.util.Collections.singletonList("h5"))), com.google.common.collect.Sets.newLinkedHashSet(java.util.Arrays.asList("h1", "h2", "h3", "h4", "h5")));
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder mastersGroup = groups.get(2);
        org.junit.Assert.assertEquals("CORE_MASTER", mastersGroup.name);
        java.util.List<java.lang.String> orderedNameNodes = new java.util.LinkedList<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : mastersGroup.items) {
            if (sw.getType().equals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART) && sw.getText().toLowerCase().contains("NameNode".toLowerCase())) {
                for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : sw.getTasks()) {
                    for (java.lang.String hostName : tw.getHosts()) {
                        orderedNameNodes.add(hostName);
                    }
                }
            }
        }
        org.junit.Assert.assertEquals(java.util.Arrays.asList("h2", "h3", "h1", "h5", "h4"), orderedNameNodes);
    }

    @org.junit.Test
    public void testUpgradeOrchestrationWithNoHeartbeat() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster(false);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Host h4 = clusters.getHost("h4");
        h4.setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = cluster.getServiceComponentHosts("h4");
        org.junit.Assert.assertEquals(1, schs.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, schs.get(0).getHostState());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.junit.Assert.assertEquals("PRE_CLUSTER", groups.get(0).name);
        org.junit.Assert.assertEquals("ZOOKEEPER", groups.get(1).name);
        org.junit.Assert.assertEquals("CORE_MASTER", groups.get(2).name);
        org.junit.Assert.assertEquals("CORE_SLAVES", groups.get(3).name);
        org.junit.Assert.assertEquals("HIVE", groups.get(4).name);
        org.junit.Assert.assertEquals("OOZIE", groups.get(5).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder postGroup = groups.get(6);
        org.junit.Assert.assertEquals("POST_CLUSTER", postGroup.name);
        org.junit.Assert.assertEquals("Finalize Upgrade", postGroup.title);
        org.junit.Assert.assertEquals(3, postGroup.items.size());
        org.junit.Assert.assertEquals("Confirm Finalize", postGroup.items.get(0).getText());
        org.junit.Assert.assertEquals("Execute HDFS Finalize", postGroup.items.get(1).getText());
        org.junit.Assert.assertEquals("Save Cluster State", postGroup.items.get(2).getText());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, postGroup.items.get(2).getType());
        org.junit.Assert.assertEquals(6, groups.get(1).items.size());
        org.junit.Assert.assertEquals(9, groups.get(2).items.size());
        org.junit.Assert.assertEquals(7, groups.get(3).items.size());
    }

    @org.junit.Test
    public void testDowngradeOrchestration() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2200);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.junit.Assert.assertEquals("PRE_CLUSTER", groups.get(0).name);
        org.junit.Assert.assertEquals("OOZIE", groups.get(1).name);
        org.junit.Assert.assertEquals("HIVE", groups.get(2).name);
        org.junit.Assert.assertEquals("CORE_SLAVES", groups.get(3).name);
        org.junit.Assert.assertEquals("CORE_MASTER", groups.get(4).name);
        org.junit.Assert.assertEquals("ZOOKEEPER", groups.get(5).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder postGroup = groups.get(6);
        org.junit.Assert.assertEquals("POST_CLUSTER", postGroup.name);
        org.junit.Assert.assertEquals("Finalize Downgrade", postGroup.title);
        org.junit.Assert.assertEquals(3, postGroup.items.size());
        org.junit.Assert.assertEquals("Confirm Finalize", postGroup.items.get(0).getText());
        org.junit.Assert.assertEquals("Execute HDFS Finalize", postGroup.items.get(1).getText());
        org.junit.Assert.assertEquals("Save Cluster State", postGroup.items.get(2).getText());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, postGroup.items.get(2).getType());
        org.junit.Assert.assertEquals(4, groups.get(0).items.size());
        org.junit.Assert.assertEquals(8, groups.get(1).items.size());
        org.junit.Assert.assertEquals(6, groups.get(2).items.size());
        org.junit.Assert.assertEquals(6, groups.get(3).items.size());
        org.junit.Assert.assertEquals(8, groups.get(4).items.size());
    }

    @org.junit.Test
    public void testBuckets() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_bucket_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_bucket_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(1, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.iterator().next();
        org.junit.Assert.assertEquals(22, group.items.size());
    }

    @org.junit.Test
    public void testManualTaskPostProcessing() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder zookeeperGroup = groups.get(1);
        org.junit.Assert.assertEquals("ZOOKEEPER", zookeeperGroup.name);
        org.apache.ambari.server.stack.upgrade.ManualTask manualTask = ((org.apache.ambari.server.stack.upgrade.ManualTask) (zookeeperGroup.items.get(0).getTasks().get(0).getTasks().get(0)));
        org.junit.Assert.assertEquals(1, manualTask.messages.size());
        org.junit.Assert.assertEquals("This is a manual task with a placeholder of placeholder-rendered-properly", manualTask.messages.get(0));
    }

    @org.junit.Test
    public void testConditionalDeleteTask() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cup = ambariMetaInfo.getConfigUpgradePack("HDP", "2.1.1");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder hiveGroup = groups.get(4);
        org.junit.Assert.assertEquals("HIVE", hiveGroup.name);
        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (hiveGroup.items.get(1).getTasks().get(0).getTasks().get(0)));
        java.util.Map<java.lang.String, java.lang.String> hiveConfigs = new java.util.HashMap<>();
        hiveConfigs.put("hive.server2.transport.mode", "http");
        hiveConfigs.put("hive.server2.thrift.port", "10001");
        hiveConfigs.put("condition", "1");
        org.apache.ambari.server.controller.ConfigurationRequest configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest();
        configurationRequest.setClusterName(cluster.getClusterName());
        configurationRequest.setType("hive-site");
        configurationRequest.setVersionTag("version2");
        configurationRequest.setProperties(hiveConfigs);
        final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster.getClusterName(), cluster.getDesiredStackVersion().getStackVersion(), null);
        clusterRequest.setDesiredConfig(java.util.Collections.singletonList(configurationRequest));
        m_managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
            {
                add(clusterRequest);
            }
        }, null);
        java.util.Map<java.lang.String, java.lang.String> configProperties = configureTask.getConfigurationChanges(cluster, cup);
        org.junit.Assert.assertFalse(configProperties.isEmpty());
        org.junit.Assert.assertEquals(configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE), "hive-site");
        java.lang.String configurationJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS);
        org.junit.Assert.assertNotNull(configurationJson);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = m_gson.fromJson(configurationJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer>>() {}.getType());
        org.junit.Assert.assertEquals(6, transfers.size());
        org.junit.Assert.assertEquals("copy-key", transfers.get(0).fromKey);
        org.junit.Assert.assertEquals("copy-key-to", transfers.get(0).toKey);
        org.junit.Assert.assertEquals("move-key", transfers.get(1).fromKey);
        org.junit.Assert.assertEquals("move-key-to", transfers.get(1).toKey);
        org.junit.Assert.assertEquals("delete-key", transfers.get(2).deleteKey);
        org.junit.Assert.assertEquals("delete-http-1", transfers.get(3).deleteKey);
        org.junit.Assert.assertEquals("delete-http-2", transfers.get(4).deleteKey);
        org.junit.Assert.assertEquals("delete-http-3", transfers.get(5).deleteKey);
    }

    @org.junit.Test
    public void testConfigTaskConditionMet() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cup = ambariMetaInfo.getConfigUpgradePack("HDP", "2.1.1");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder hiveGroup = groups.get(4);
        org.junit.Assert.assertEquals("HIVE", hiveGroup.name);
        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (hiveGroup.items.get(2).getTasks().get(0).getTasks().get(0)));
        java.util.Map<java.lang.String, java.lang.String> configProperties = configureTask.getConfigurationChanges(cluster, cup);
        org.junit.Assert.assertFalse(configProperties.isEmpty());
        org.junit.Assert.assertEquals(configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE), "hive-site");
        org.junit.Assert.assertTrue(configProperties.containsKey(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS));
        org.junit.Assert.assertTrue(configProperties.containsKey(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS));
        org.junit.Assert.assertTrue(configProperties.containsKey(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS));
        java.lang.String configurationJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS);
        java.lang.String transferJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS);
        java.lang.String replacementJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS);
        org.junit.Assert.assertNotNull(configurationJson);
        org.junit.Assert.assertNotNull(transferJson);
        org.junit.Assert.assertNotNull(replacementJson);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> keyValuePairs = m_gson.fromJson(configurationJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue>>() {}.getType());
        org.junit.Assert.assertEquals("setKeyOne", keyValuePairs.get(0).key);
        org.junit.Assert.assertEquals("1", keyValuePairs.get(0).value);
        org.junit.Assert.assertEquals("setKeyTwo", keyValuePairs.get(1).key);
        org.junit.Assert.assertEquals("2", keyValuePairs.get(1).value);
        org.junit.Assert.assertEquals("setKeyThree", keyValuePairs.get(2).key);
        org.junit.Assert.assertEquals("3", keyValuePairs.get(2).value);
        org.junit.Assert.assertEquals("setKeyFour", keyValuePairs.get(3).key);
        org.junit.Assert.assertEquals("4", keyValuePairs.get(3).value);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = m_gson.fromJson(transferJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer>>() {}.getType());
        org.junit.Assert.assertEquals("copy-key-one", transfers.get(0).fromKey);
        org.junit.Assert.assertEquals("copy-to-key-one", transfers.get(0).toKey);
        org.junit.Assert.assertEquals("copy-key-two", transfers.get(1).fromKey);
        org.junit.Assert.assertEquals("copy-to-key-two", transfers.get(1).toKey);
        org.junit.Assert.assertEquals("copy-key-three", transfers.get(2).fromKey);
        org.junit.Assert.assertEquals("copy-to-key-three", transfers.get(2).toKey);
        org.junit.Assert.assertEquals("copy-key-four", transfers.get(3).fromKey);
        org.junit.Assert.assertEquals("copy-to-key-four", transfers.get(3).toKey);
        org.junit.Assert.assertEquals("move-key-one", transfers.get(4).fromKey);
        org.junit.Assert.assertEquals("move-to-key-one", transfers.get(4).toKey);
        org.junit.Assert.assertEquals("move-key-two", transfers.get(5).fromKey);
        org.junit.Assert.assertEquals("move-to-key-two", transfers.get(5).toKey);
        org.junit.Assert.assertEquals("move-key-three", transfers.get(6).fromKey);
        org.junit.Assert.assertEquals("move-to-key-three", transfers.get(6).toKey);
        org.junit.Assert.assertEquals("move-key-four", transfers.get(7).fromKey);
        org.junit.Assert.assertEquals("move-to-key-four", transfers.get(7).toKey);
        org.junit.Assert.assertEquals("delete-key-one", transfers.get(8).deleteKey);
        org.junit.Assert.assertEquals("delete-key-two", transfers.get(9).deleteKey);
        org.junit.Assert.assertEquals("delete-key-three", transfers.get(10).deleteKey);
        org.junit.Assert.assertEquals("delete-key-four", transfers.get(11).deleteKey);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = m_gson.fromJson(replacementJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace>>() {}.getType());
        org.junit.Assert.assertEquals("replace-key-one", replacements.get(0).key);
        org.junit.Assert.assertEquals("abc", replacements.get(0).find);
        org.junit.Assert.assertEquals("abc-replaced", replacements.get(0).replaceWith);
        org.junit.Assert.assertEquals("replace-key-two", replacements.get(1).key);
        org.junit.Assert.assertEquals("efg", replacements.get(1).find);
        org.junit.Assert.assertEquals("efg-replaced", replacements.get(1).replaceWith);
        org.junit.Assert.assertEquals("replace-key-three", replacements.get(2).key);
        org.junit.Assert.assertEquals("ijk", replacements.get(2).find);
        org.junit.Assert.assertEquals("ijk-replaced", replacements.get(2).replaceWith);
        org.junit.Assert.assertEquals("replace-key-four", replacements.get(3).key);
        org.junit.Assert.assertEquals("lmn", replacements.get(3).find);
        org.junit.Assert.assertEquals("lmn-replaced", replacements.get(3).replaceWith);
    }

    @org.junit.Test
    public void testConfigTaskConditionSkipped() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cup = ambariMetaInfo.getConfigUpgradePack("HDP", "2.1.1");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder hiveGroup = groups.get(4);
        org.junit.Assert.assertEquals("HIVE", hiveGroup.name);
        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (hiveGroup.items.get(3).getTasks().get(0).getTasks().get(0)));
        java.util.Map<java.lang.String, java.lang.String> configProperties = configureTask.getConfigurationChanges(cluster, cup);
        org.junit.Assert.assertFalse(configProperties.isEmpty());
        org.junit.Assert.assertEquals(configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE), "hive-site");
        org.junit.Assert.assertTrue(configProperties.containsKey(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS));
        org.junit.Assert.assertTrue(configProperties.containsKey(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS));
        org.junit.Assert.assertTrue(configProperties.containsKey(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS));
        java.lang.String configurationJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS);
        java.lang.String transferJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS);
        java.lang.String replacementJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS);
        org.junit.Assert.assertNotNull(configurationJson);
        org.junit.Assert.assertNotNull(transferJson);
        org.junit.Assert.assertNotNull(replacementJson);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> keyValuePairs = m_gson.fromJson(configurationJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue>>() {}.getType());
        org.junit.Assert.assertTrue(keyValuePairs.isEmpty());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = m_gson.fromJson(replacementJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace>>() {}.getType());
        org.junit.Assert.assertTrue(replacements.isEmpty());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = m_gson.fromJson(transferJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer>>() {}.getType());
        org.junit.Assert.assertTrue(transfers.isEmpty());
    }

    @org.junit.Test
    public void testConfigureTask() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cup = ambariMetaInfo.getConfigUpgradePack("HDP", "2.1.1");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder hiveGroup = groups.get(4);
        org.junit.Assert.assertEquals("HIVE", hiveGroup.name);
        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (hiveGroup.items.get(1).getTasks().get(0).getTasks().get(0)));
        java.util.Map<java.lang.String, java.lang.String> configProperties = configureTask.getConfigurationChanges(cluster, cup);
        org.junit.Assert.assertFalse(configProperties.isEmpty());
        org.junit.Assert.assertEquals(configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE), "hive-site");
        java.util.Map<java.lang.String, java.lang.String> hiveConfigs = new java.util.HashMap<>();
        hiveConfigs.put("fooKey", "THIS-BETTER-CHANGE");
        hiveConfigs.put("ifFooKey", "ifFooValue");
        org.apache.ambari.server.controller.ConfigurationRequest configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest();
        configurationRequest.setClusterName(cluster.getClusterName());
        configurationRequest.setType("hive-site");
        configurationRequest.setVersionTag("version2");
        configurationRequest.setProperties(hiveConfigs);
        final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster.getClusterName(), cluster.getDesiredStackVersion().getStackVersion(), null);
        clusterRequest.setDesiredConfig(java.util.Collections.singletonList(configurationRequest));
        m_managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
            {
                add(clusterRequest);
            }
        }, null);
        configProperties = configureTask.getConfigurationChanges(cluster, cup);
        org.junit.Assert.assertFalse(configProperties.isEmpty());
        org.junit.Assert.assertEquals(configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE), "hive-site");
        java.lang.String configurationJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS);
        org.junit.Assert.assertNotNull(configurationJson);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> keyValuePairs = m_gson.fromJson(configurationJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue>>() {}.getType());
        org.junit.Assert.assertEquals("fooKey", keyValuePairs.get(0).key);
        org.junit.Assert.assertEquals("fooValue", keyValuePairs.get(0).value);
    }

    @org.junit.Test
    public void testConfigureRegexTask() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cup = ambariMetaInfo.getConfigUpgradePack("HDP", "2.1.1");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder hiveGroup = groups.get(4);
        org.junit.Assert.assertEquals("HIVE", hiveGroup.name);
        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (hiveGroup.items.get(5).getTasks().get(0).getTasks().get(0)));
        org.junit.Assert.assertEquals("hdp_2_1_1_regex_replace", configureTask.getId());
        java.util.Map<java.lang.String, java.lang.String> hiveConfigs = new java.util.HashMap<>();
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("1-foo-2");
        builder.append(java.lang.System.lineSeparator());
        builder.append("1-bar-2");
        builder.append(java.lang.System.lineSeparator());
        builder.append("3-foo-4");
        builder.append(java.lang.System.lineSeparator());
        builder.append("1-foobar-2");
        builder.append(java.lang.System.lineSeparator());
        hiveConfigs.put("regex-replace-key-one", builder.toString());
        org.apache.ambari.server.controller.ConfigurationRequest configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest();
        configurationRequest.setClusterName(cluster.getClusterName());
        configurationRequest.setType("hive-site");
        configurationRequest.setVersionTag("version2");
        configurationRequest.setProperties(hiveConfigs);
        final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster.getClusterName(), cluster.getDesiredStackVersion().getStackVersion(), null);
        clusterRequest.setDesiredConfig(java.util.Collections.singletonList(configurationRequest));
        m_managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
            {
                add(clusterRequest);
            }
        }, null);
        java.util.Map<java.lang.String, java.lang.String> configProperties = configureTask.getConfigurationChanges(cluster, cup);
        org.junit.Assert.assertFalse(configProperties.isEmpty());
        org.junit.Assert.assertEquals(configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE), "hive-site");
        java.lang.String configurationJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS);
        org.junit.Assert.assertNotNull(configurationJson);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = m_gson.fromJson(configurationJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace>>() {}.getType());
        org.junit.Assert.assertEquals("1-foo-2" + java.lang.System.lineSeparator(), replacements.get(0).find);
        org.junit.Assert.assertEquals("REPLACED", replacements.get(0).replaceWith);
        org.junit.Assert.assertEquals("3-foo-4" + java.lang.System.lineSeparator(), replacements.get(1).find);
        org.junit.Assert.assertEquals("REPLACED", replacements.get(1).replaceWith);
        org.junit.Assert.assertEquals(2, replacements.size());
    }

    @org.junit.Test
    public void testConfigureTaskWithMultipleConfigurations() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack cup = ambariMetaInfo.getConfigUpgradePack("HDP", "2.1.1");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder hiveGroup = groups.get(4);
        org.junit.Assert.assertEquals("HIVE", hiveGroup.name);
        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (hiveGroup.items.get(1).getTasks().get(0).getTasks().get(0)));
        java.util.Map<java.lang.String, java.lang.String> configProperties = configureTask.getConfigurationChanges(cluster, cup);
        org.junit.Assert.assertFalse(configProperties.isEmpty());
        org.junit.Assert.assertEquals(configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE), "hive-site");
        java.lang.String configurationJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS);
        java.lang.String transferJson = configProperties.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS);
        org.junit.Assert.assertNotNull(configurationJson);
        org.junit.Assert.assertNotNull(transferJson);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> keyValuePairs = m_gson.fromJson(configurationJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue>>() {}.getType());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = m_gson.fromJson(transferJson, new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer>>() {}.getType());
        org.junit.Assert.assertEquals("fooKey", keyValuePairs.get(0).key);
        org.junit.Assert.assertEquals("fooValue", keyValuePairs.get(0).value);
        org.junit.Assert.assertEquals("fooKey2", keyValuePairs.get(1).key);
        org.junit.Assert.assertEquals("fooValue2", keyValuePairs.get(1).value);
        org.junit.Assert.assertEquals("fooKey3", keyValuePairs.get(2).key);
        org.junit.Assert.assertEquals("fooValue3", keyValuePairs.get(2).value);
        org.junit.Assert.assertEquals("copy-key", transfers.get(0).fromKey);
        org.junit.Assert.assertEquals("copy-key-to", transfers.get(0).toKey);
        org.junit.Assert.assertEquals("move-key", transfers.get(1).fromKey);
        org.junit.Assert.assertEquals("move-key-to", transfers.get(1).toKey);
    }

    @org.junit.Test
    public void testServiceCheckUpgradeStages() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_checks"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_checks");
        org.junit.Assert.assertNotNull(upgrade);
        java.util.Set<java.lang.String> additionalServices = new java.util.HashSet<java.lang.String>() {
            {
                add("HBASE");
                add("PIG");
                add("TEZ");
                add("AMBARI_METRICS");
            }
        };
        org.apache.ambari.server.state.Cluster c = makeCluster(true, additionalServices, "");
        int numServiceChecksExpected = 0;
        java.util.Collection<org.apache.ambari.server.state.Service> services = c.getServices().values();
        for (org.apache.ambari.server.state.Service service : services) {
            org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService(c.getCurrentStackVersion().getStackName(), c.getCurrentStackVersion().getStackVersion(), service.getName());
            if (null == si.getCommandScript()) {
                continue;
            }
            if (service.getName().equalsIgnoreCase("TEZ")) {
                org.junit.Assert.assertTrue("Expect Tez to not have any service checks", false);
            }
            if (service.getName().equalsIgnoreCase("AMBARI_METRICS")) {
                continue;
            }
            numServiceChecksExpected++;
        }
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(c, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(8, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder = groups.get(4);
        org.junit.Assert.assertEquals(holder.name, "SERVICE_CHECK_1");
        org.junit.Assert.assertEquals(7, holder.items.size());
        int numServiceChecksActual = 0;
        for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : holder.items) {
            for (org.apache.ambari.server.state.Service service : services) {
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(".*" + service.getName(), java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Matcher matcher = p.matcher(sw.getText());
                if (matcher.matches()) {
                    numServiceChecksActual++;
                    continue;
                }
            }
        }
        org.junit.Assert.assertEquals(numServiceChecksActual, numServiceChecksExpected);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder zookeeperGroup = groups.get(1);
        org.junit.Assert.assertEquals("ZOOKEEPER", zookeeperGroup.name);
        org.apache.ambari.server.stack.upgrade.ManualTask manualTask = ((org.apache.ambari.server.stack.upgrade.ManualTask) (zookeeperGroup.items.get(0).getTasks().get(0).getTasks().get(0)));
        org.junit.Assert.assertEquals(1, manualTask.messages.size());
        org.junit.Assert.assertEquals("This is a manual task with a placeholder of placeholder-rendered-properly", manualTask.messages.get(0));
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder clusterGroup = groups.get(3);
        org.junit.Assert.assertEquals(clusterGroup.name, "HBASE");
        org.junit.Assert.assertEquals(clusterGroup.title, "Update HBase Configuration");
        org.junit.Assert.assertEquals(1, clusterGroup.items.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stage = clusterGroup.items.get(0);
        org.junit.Assert.assertEquals(stage.getText(), "Update HBase Configuration");
    }

    @org.junit.Test
    public void testServiceCheckDowngradeStages() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_checks"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_checks");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2200);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(6, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder zookeeperGroup = groups.get(4);
        org.junit.Assert.assertEquals("ZOOKEEPER", zookeeperGroup.name);
        org.apache.ambari.server.stack.upgrade.ManualTask manualTask = ((org.apache.ambari.server.stack.upgrade.ManualTask) (zookeeperGroup.items.get(0).getTasks().get(0).getTasks().get(0)));
        org.junit.Assert.assertEquals(1, manualTask.messages.size());
        org.junit.Assert.assertEquals("This is a manual task with a placeholder of placeholder-rendered-properly", manualTask.messages.get(0));
    }

    @org.junit.Test
    public void testUpgradeOrchestrationFullTask() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService("HDP", "2.2.0", "ZOOKEEPER");
        si.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo ci = si.getComponentByName("ZOOKEEPER_SERVER");
        ci.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_to_new_stack"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_to_new_stack");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(6, groups.size());
        org.junit.Assert.assertEquals("PRE_CLUSTER", groups.get(0).name);
        org.junit.Assert.assertEquals("ZOOKEEPER", groups.get(1).name);
        org.junit.Assert.assertEquals("CORE_MASTER", groups.get(2).name);
        org.junit.Assert.assertEquals("CORE_SLAVES", groups.get(3).name);
        org.junit.Assert.assertEquals("HIVE", groups.get(4).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder = groups.get(2);
        boolean found = false;
        for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : holder.items) {
            if (sw.getTasksJson().contains("Upgrading your database")) {
                found = true;
            }
        }
        org.junit.Assert.assertTrue("Expected to find replaced text for Upgrading", found);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(1);
        org.junit.Assert.assertTrue(group.items.get(1).getText().contains("ZooKeeper1 Server2"));
        org.junit.Assert.assertEquals(group.items.get(4).getText(), "Service Check Zk");
        group = groups.get(3);
        org.junit.Assert.assertEquals(8, group.items.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw = group.items.get(3);
        org.junit.Assert.assertEquals("Validate Partial Upgrade", sw.getText());
        org.junit.Assert.assertEquals(1, sw.getTasks().size());
        org.junit.Assert.assertEquals(1, sw.getTasks().get(0).getTasks().size());
        org.apache.ambari.server.stack.upgrade.Task t = sw.getTasks().get(0).getTasks().get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ManualTask.class, t.getClass());
        org.apache.ambari.server.stack.upgrade.ManualTask mt = ((org.apache.ambari.server.stack.upgrade.ManualTask) (t));
        org.junit.Assert.assertTrue(mt.messages.get(0).contains("DataNode and NodeManager"));
        org.junit.Assert.assertNotNull(mt.structuredOut);
        org.junit.Assert.assertTrue(mt.structuredOut.contains("DATANODE"));
        org.junit.Assert.assertTrue(mt.structuredOut.contains("NODEMANAGER"));
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder postGroup = groups.get(5);
        org.junit.Assert.assertEquals(postGroup.name, "POST_CLUSTER");
        org.junit.Assert.assertEquals(postGroup.title, "Finalize Upgrade");
        org.junit.Assert.assertEquals(4, postGroup.items.size());
        org.junit.Assert.assertEquals("Confirm Finalize", postGroup.items.get(0).getText());
        org.junit.Assert.assertEquals("Execute HDFS Finalize", postGroup.items.get(1).getText());
        org.junit.Assert.assertEquals("Save Cluster State", postGroup.items.get(2).getText());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, postGroup.items.get(2).getType());
        org.junit.Assert.assertEquals("Run On All 2.2.1.0-1234", postGroup.items.get(3).getText());
        org.junit.Assert.assertEquals(1, postGroup.items.get(3).getTasks().size());
        java.util.Set<java.lang.String> hosts = postGroup.items.get(3).getTasks().get(0).getHosts();
        org.junit.Assert.assertNotNull(hosts);
        org.junit.Assert.assertEquals(4, hosts.size());
        org.junit.Assert.assertEquals(4, groups.get(0).items.size());
        org.junit.Assert.assertEquals(5, groups.get(1).items.size());
        org.junit.Assert.assertEquals(9, groups.get(2).items.size());
        org.junit.Assert.assertEquals(8, groups.get(3).items.size());
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    private org.apache.ambari.server.state.Cluster makeCluster() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        return makeCluster(true);
    }

    private org.apache.ambari.server.state.Cluster makeCluster(boolean clean) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        return makeCluster(clean, new java.util.HashSet<>(), "");
    }

    private org.apache.ambari.server.state.Cluster makeCluster(boolean clean, java.util.Set<java.lang.String> additionalServices, java.lang.String yamlFileName) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        java.lang.String repositoryVersionString = "2.1.1-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, repositoryVersionString);
        helper.getOrCreateRepositoryVersion(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.STACK_ID_HDP_220, "2.2.0");
        helper.getOrCreateRepositoryVersion(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.STACK_ID_HDP_220, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.UPGRADE_VERSION);
        for (int i = 0; i < 4; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "HDFS", repositoryVersion));
        c.addService(serviceFactory.createNew(c, "YARN", repositoryVersion));
        c.addService(serviceFactory.createNew(c, "ZOOKEEPER", repositoryVersion));
        c.addService(serviceFactory.createNew(c, "HIVE", repositoryVersion));
        c.addService(serviceFactory.createNew(c, "OOZIE", repositoryVersion));
        org.apache.ambari.server.state.Service s = c.getService("HDFS");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("NAMENODE");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        sc = s.addServiceComponent("DATANODE");
        sc.addServiceComponentHost("h2");
        sc.addServiceComponentHost("h3");
        org.apache.ambari.server.state.ServiceComponentHost sch = sc.addServiceComponentHost("h4");
        s = c.getService("ZOOKEEPER");
        sc = s.addServiceComponent("ZOOKEEPER_SERVER");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        sc.addServiceComponentHost("h3");
        s = c.getService("YARN");
        sc = s.addServiceComponent("RESOURCEMANAGER");
        sc.addServiceComponentHost("h2");
        sc = s.addServiceComponent("NODEMANAGER");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h3");
        s = c.getService("HIVE");
        sc = s.addServiceComponent("HIVE_SERVER");
        sc.addServiceComponentHost("h2");
        s = c.getService("OOZIE");
        sc = s.addServiceComponent("OOZIE_SERVER");
        sc.addServiceComponentHost("h2");
        sc.addServiceComponentHost("h3");
        sc = s.addServiceComponent("OOZIE_CLIENT");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        sc.addServiceComponentHost("h3");
        java.util.Map<java.lang.String, java.lang.String> hiveConfigs = new java.util.HashMap<>();
        hiveConfigs.put("hive.server2.transport.mode", "binary");
        hiveConfigs.put("hive.server2.thrift.port", "10001");
        org.apache.ambari.server.controller.ConfigurationRequest configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest();
        configurationRequest.setClusterName(clusterName);
        configurationRequest.setType("hive-site");
        configurationRequest.setVersionTag("version1");
        configurationRequest.setProperties(hiveConfigs);
        final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(c.getClusterId(), clusterName, c.getDesiredStackVersion().getStackVersion(), null);
        clusterRequest.setDesiredConfig(java.util.Collections.singletonList(configurationRequest));
        m_managementController.updateClusters(new java.util.HashSet<org.apache.ambari.server.controller.ClusterRequest>() {
            {
                add(clusterRequest);
            }
        }, null);
        org.apache.ambari.server.stack.HostsType type = org.apache.ambari.server.stack.HostsType.normal("h1", "h2", "h3");
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("ZOOKEEPER", "ZOOKEEPER_SERVER")).andReturn(type).anyTimes();
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("ZOOKEEPER", "ZOOKEEPER_CLIENT")).andReturn(type).anyTimes();
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("HDFS", "NAMENODE")).andReturn(namenodeHosts).anyTimes();
        if (clean) {
            type = org.apache.ambari.server.stack.HostsType.normal("h2", "h3", "h4");
        } else {
            type = org.apache.ambari.server.stack.HostsType.normal("h2", "h3");
            type.unhealthy = java.util.Collections.singletonList(sch);
        }
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("HDFS", "DATANODE")).andReturn(type).anyTimes();
        type = org.apache.ambari.server.stack.HostsType.normal("h2");
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("YARN", "RESOURCEMANAGER")).andReturn(type).anyTimes();
        type = org.apache.ambari.server.stack.HostsType.normal(com.google.common.collect.Sets.newLinkedHashSet());
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("YARN", "APP_TIMELINE_SERVER")).andReturn(type).anyTimes();
        type = org.apache.ambari.server.stack.HostsType.normal("h1", "h3");
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("YARN", "NODEMANAGER")).andReturn(type).anyTimes();
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("HIVE", "HIVE_SERVER")).andReturn(type).anyTimes();
        type = org.apache.ambari.server.stack.HostsType.normal("h2", "h3");
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("OOZIE", "OOZIE_SERVER")).andReturn(type).anyTimes();
        type = org.apache.ambari.server.stack.HostsType.normal("h1", "h2", "h3");
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("OOZIE", "OOZIE_CLIENT")).andReturn(type).anyTimes();
        EasyMock.expect(m_masterHostResolver.getCluster()).andReturn(c).anyTimes();
        EasyMock.expect(m_masterHostResolver.getValueFromDesiredConfigurations("cluster-env", "rack_yaml_file_path")).andReturn(yamlFileName).anyTimes();
        for (java.lang.String service : additionalServices) {
            c.addService(service, repositoryVersion);
            if (service.equals("HBASE")) {
                type = org.apache.ambari.server.stack.HostsType.normal("h1", "h2");
                EasyMock.expect(m_masterHostResolver.getMasterAndHosts("HBASE", "HBASE_MASTER")).andReturn(type).anyTimes();
            }
        }
        EasyMock.replay(m_masterHostResolver);
        return c;
    }

    @org.junit.Test
    public void testUpgradeWithMultipleTasksInOwnStage() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        org.junit.Assert.assertTrue(upgrade.getType() == org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> upgradePackGroups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        boolean foundService = false;
        for (org.apache.ambari.server.stack.upgrade.Grouping group : upgradePackGroups) {
            if (group.title.equals("Oozie")) {
                foundService = true;
            }
        }
        org.junit.Assert.assertTrue(foundService);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        int numPrepareStages = 0;
        for (org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group : groups) {
            if (group.name.equals("OOZIE")) {
                org.junit.Assert.assertTrue(group.items.size() > 0);
                for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : group.items) {
                    if (sw.getText().equalsIgnoreCase("Preparing Oozie Server on h2 (Batch 1 of 2)") || sw.getText().equalsIgnoreCase("Preparing Oozie Server on h3 (Batch 2 of 2)")) {
                        numPrepareStages++;
                        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> taskWrappers = sw.getTasks();
                        org.junit.Assert.assertEquals(1, taskWrappers.size());
                        java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks = taskWrappers.get(0).getTasks();
                        org.junit.Assert.assertEquals(1, taskWrappers.get(0).getHosts().size());
                        org.junit.Assert.assertEquals(1, tasks.size());
                        org.apache.ambari.server.stack.upgrade.ExecuteTask task = ((org.apache.ambari.server.stack.upgrade.ExecuteTask) (tasks.get(0)));
                        org.junit.Assert.assertTrue("scripts/oozie_server.py".equalsIgnoreCase(task.script));
                        org.junit.Assert.assertTrue("stop".equalsIgnoreCase(task.function));
                    }
                    if (sw.getText().equalsIgnoreCase("Preparing Oozie Server on h2")) {
                        numPrepareStages++;
                        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper> taskWrappers = sw.getTasks();
                        org.junit.Assert.assertEquals(1, taskWrappers.size());
                        java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks = taskWrappers.get(0).getTasks();
                        org.junit.Assert.assertEquals(1, taskWrappers.get(0).getHosts().size());
                        org.junit.Assert.assertEquals(1, tasks.size());
                        org.apache.ambari.server.stack.upgrade.ExecuteTask task = ((org.apache.ambari.server.stack.upgrade.ExecuteTask) (tasks.get(0)));
                        org.junit.Assert.assertTrue("scripts/oozie_server_upgrade.py".equalsIgnoreCase(task.script));
                        org.junit.Assert.assertTrue("upgrade_oozie_database_and_sharelib".equalsIgnoreCase(task.function));
                    }
                }
            }
        }
        org.junit.Assert.assertEquals(3, numPrepareStages);
    }

    @org.junit.Test
    public void testDowngradeAfterPartialUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        java.lang.String version = "2.1.1.0-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, version);
        for (int i = 0; i < 2; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "HDFS", repositoryVersion));
        org.apache.ambari.server.state.Service s = c.getService("HDFS");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("NAMENODE");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = c.getServiceComponentHosts("HDFS", "NAMENODE");
        org.junit.Assert.assertEquals(2, schs.size());
        org.apache.ambari.server.stack.HostsType type = org.apache.ambari.server.stack.HostsType.highAvailability("h1", "h2", new java.util.LinkedHashSet<>(java.util.Collections.emptySet()));
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("ZOOKEEPER", "ZOOKEEPER_SERVER")).andReturn(null).anyTimes();
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("HDFS", "NAMENODE")).andReturn(type).anyTimes();
        EasyMock.expect(m_masterHostResolver.getCluster()).andReturn(c).anyTimes();
        EasyMock.replay(m_masterHostResolver);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(c, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2200);
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_direction"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_direction");
        org.junit.Assert.assertNotNull(upgrade);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(2, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(0);
        org.junit.Assert.assertEquals(1, group.items.size());
        org.junit.Assert.assertEquals("PRE_POST_CLUSTER", group.name);
        group = groups.get(1);
        org.junit.Assert.assertEquals("POST_CLUSTER", group.name);
        org.junit.Assert.assertEquals(3, group.items.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stage = group.items.get(1);
        org.junit.Assert.assertEquals("NameNode Finalize", stage.getText());
        org.junit.Assert.assertEquals(1, stage.getTasks().size());
        org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper task = stage.getTasks().get(0);
        org.junit.Assert.assertEquals(1, task.getHosts().size());
    }

    @org.junit.Test
    public void testResolverWithFailedUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        for (int i = 0; i < 2; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "ZOOKEEPER", repositoryVersion2110));
        org.apache.ambari.server.state.Service s = c.getService("ZOOKEEPER");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc.addServiceComponentHost("h1");
        sch1.setVersion(repositoryVersion2110.getVersion());
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc.addServiceComponentHost("h2");
        sch2.setVersion(repositoryVersion2110.getVersion());
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = c.getServiceComponentHosts("ZOOKEEPER", "ZOOKEEPER_SERVER");
        org.junit.Assert.assertEquals(2, schs.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED, repositoryVersion2110);
        org.apache.ambari.server.stack.MasterHostResolver resolver = new org.apache.ambari.server.stack.MasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(resolver).anyTimes();
        EasyMock.replay(context);
        org.apache.ambari.server.stack.HostsType ht = resolver.getMasterAndHosts("ZOOKEEPER", "ZOOKEEPER_SERVER");
        org.junit.Assert.assertEquals(0, ht.getHosts().size());
        sch2.setUpgradeState(org.apache.ambari.server.state.UpgradeState.FAILED);
        ht = resolver.getMasterAndHosts("ZOOKEEPER", "ZOOKEEPER_SERVER");
        org.junit.Assert.assertEquals(1, ht.getHosts().size());
        org.junit.Assert.assertEquals("h2", ht.getHosts().iterator().next());
    }

    @org.junit.Test
    public void testResolverCaseInsensitive() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        java.lang.String version = "2.1.1.0-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion211 = helper.getOrCreateRepositoryVersion(stackId, version);
        for (int i = 0; i < 2; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "HDFS", repositoryVersion211));
        org.apache.ambari.server.state.Service s = c.getService("HDFS");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("NAMENODE");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = c.getServiceComponentHosts("HDFS", "NAMENODE");
        org.junit.Assert.assertEquals(2, schs.size());
        setConfigMocks();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.internal.nameservices")).andReturn("ha").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.ha.namenodes.ha")).andReturn("nn1,nn2").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.http.policy")).andReturn("HTTP_ONLY").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.namenode.http-address.ha.nn1")).andReturn("H1:50070").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.namenode.http-address.ha.nn2")).andReturn("H2:50070").anyTimes();
        EasyMock.replay(m_configHelper);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, repositoryVersion211);
        org.apache.ambari.server.stack.MasterHostResolver mhr = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.MockMasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(mhr).anyTimes();
        EasyMock.replay(context);
        org.apache.ambari.server.stack.HostsType ht = mhr.getMasterAndHosts("HDFS", "NAMENODE");
        org.junit.Assert.assertNotNull(ht.getMasters());
        org.junit.Assert.assertNotNull(ht.getSecondaries());
        org.junit.Assert.assertEquals(2, ht.getHosts().size());
        org.junit.Assert.assertTrue(ht.getHosts().contains("h1"));
        org.junit.Assert.assertTrue(ht.getHosts().contains("h1"));
    }

    @org.junit.Test
    public void testResolverBadJmx() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        java.lang.String version = "2.1.1.0-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion211 = helper.getOrCreateRepositoryVersion(stackId, version);
        for (int i = 0; i < 2; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "HDFS", repositoryVersion211));
        org.apache.ambari.server.state.Service s = c.getService("HDFS");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("NAMENODE");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = c.getServiceComponentHosts("HDFS", "NAMENODE");
        org.junit.Assert.assertEquals(2, schs.size());
        setConfigMocks();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.internal.nameservices")).andReturn("ha").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.ha.namenodes.ha")).andReturn("nn1,nn2").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.http.policy")).andReturn("HTTP_ONLY").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.namenode.http-address.ha.nn1")).andReturn("H1:50070").anyTimes();
        EasyMock.expect(m_configHelper.getValueFromDesiredConfigurations(c, "hdfs-site", "dfs.namenode.http-address.ha.nn2")).andReturn("H2:50070").anyTimes();
        EasyMock.replay(m_configHelper);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, repositoryVersion211);
        org.apache.ambari.server.stack.MasterHostResolver mhr = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.BadMasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(mhr).anyTimes();
        EasyMock.replay(context);
        org.apache.ambari.server.stack.HostsType ht = mhr.getMasterAndHosts("HDFS", "NAMENODE");
        org.junit.Assert.assertNotNull(ht.getMasters());
        org.junit.Assert.assertNotNull(ht.getSecondaries());
        org.junit.Assert.assertEquals(2, ht.getHosts().size());
        org.junit.Assert.assertTrue(ht.getHosts().contains("h1"));
        org.junit.Assert.assertTrue(ht.getHosts().contains("h2"));
    }

    @org.junit.Test
    public void testRollingUpgradesCanUseAdvancedGroupings() throws java.lang.Exception {
        final java.lang.String clusterName = "c1";
        final org.apache.ambari.server.state.StackId sourceStackId = new org.apache.ambari.server.state.StackId("HDP", "2.1.1");
        final org.apache.ambari.server.state.StackId targetStackId = new org.apache.ambari.server.state.StackId("HDP", "2.2.0");
        final org.apache.ambari.server.stack.upgrade.Direction upgradeDirection = org.apache.ambari.server.stack.upgrade.Direction.UPGRADE;
        final org.apache.ambari.spi.upgrade.UpgradeType upgradeType = org.apache.ambari.spi.upgrade.UpgradeType.ROLLING;
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        java.lang.String preferredUpgradePackName = "upgrade_grouping_rolling";
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = m_upgradeHelper.suggestUpgradePack(clusterName, sourceStackId, targetStackId, upgradeDirection, upgradeType, preferredUpgradePackName);
        org.junit.Assert.assertEquals(upgradeType, upgradePack.getType());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2210, org.apache.ambari.spi.RepositoryType.STANDARD, java.util.Collections.singleton("ZOOKEEPER"));
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groupings = upgradePack.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertEquals(2, groupings.size());
        org.junit.Assert.assertEquals("STOP_ZOOKEEPER", groupings.get(0).name);
        org.junit.Assert.assertEquals("RESTART_ZOOKEEPER", groupings.get(1).name);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgradePack, context);
        org.junit.Assert.assertEquals(2, groups.size());
        org.junit.Assert.assertEquals("STOP_ZOOKEEPER", groups.get(0).name);
        org.junit.Assert.assertEquals("RESTART_ZOOKEEPER", groups.get(1).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(0);
        org.junit.Assert.assertEquals("Stopping ZooKeeper Server on h1 (Batch 1 of 3)", group.items.get(0).getText());
    }

    @org.junit.Test
    public void testOrchestrationNoServerSideOnDowngrade() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        java.lang.String version = "2.1.1.0-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        org.apache.ambari.server.state.StackId stackId2 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion211 = helper.getOrCreateRepositoryVersion(stackId, version);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = helper.getOrCreateRepositoryVersion(stackId2, "2.2.0");
        for (int i = 0; i < 2; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "STORM", repoVersion211));
        org.apache.ambari.server.state.Service s = c.getService("STORM");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("NIMBUS");
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc.addServiceComponentHost("h1");
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc.addServiceComponentHost("h2");
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = new org.apache.ambari.server.stack.upgrade.UpgradePack() {
            @java.lang.Override
            public java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> getGroups(org.apache.ambari.server.stack.upgrade.Direction direction) {
                org.apache.ambari.server.stack.upgrade.Grouping g = new org.apache.ambari.server.stack.upgrade.Grouping();
                org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService orderService = new org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService();
                orderService.serviceName = "STORM";
                orderService.components = java.util.Collections.singletonList("NIMBUS");
                g.name = "GROUP1";
                g.title = "Nimbus Group";
                g.services.add(orderService);
                return com.google.common.collect.Lists.newArrayList(g);
            }

            @java.lang.Override
            public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent>> getTasks() {
                org.apache.ambari.server.stack.upgrade.ManualTask mt = new org.apache.ambari.server.stack.upgrade.ManualTask();
                mt.messages = com.google.common.collect.Lists.newArrayList("My New Message");
                org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc = new org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent();
                pc.name = "NIMBUS_MESSAGE";
                pc.preTasks = com.google.common.collect.Lists.newArrayList(mt);
                return java.util.Collections.singletonMap("STORM", java.util.Collections.singletonMap("NIMBUS", pc));
            }
        };
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, repoVersion220);
        org.apache.ambari.server.stack.MasterHostResolver masterHostResolver = new org.apache.ambari.server.stack.MasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(masterHostResolver).anyTimes();
        EasyMock.replay(context);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgradePack, context);
        org.junit.Assert.assertEquals(1, groups.size());
        sch1.setVersion(repoVersion211.getVersion());
        sch2.setVersion(repoVersion211.getVersion());
        context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, repoVersion211);
        masterHostResolver = new org.apache.ambari.server.stack.MasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(masterHostResolver).anyTimes();
        EasyMock.replay(context);
        groups = m_upgradeHelper.createSequence(upgradePack, context);
        org.junit.Assert.assertTrue(groups.isEmpty());
    }

    @org.junit.Test
    public void testMultipleServerTasks() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        java.lang.String version = "2.1.1.0-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        org.apache.ambari.server.state.StackId stackId2 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, version);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = helper.getOrCreateRepositoryVersion(stackId2, "2.2.0");
        helper.getOrCreateRepositoryVersion(stackId2, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.UPGRADE_VERSION);
        for (int i = 0; i < 2; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "ZOOKEEPER", repositoryVersion));
        org.apache.ambari.server.state.Service s = c.getService("ZOOKEEPER");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("ZOOKEEPER_SERVER");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        sc = s.addServiceComponent("ZOOKEEPER_CLIENT");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        org.easymock.EasyMock.reset(m_masterHostResolver);
        EasyMock.expect(m_masterHostResolver.getCluster()).andReturn(c).anyTimes();
        org.apache.ambari.server.stack.HostsType type = org.apache.ambari.server.stack.HostsType.normal("h1", "h2");
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("ZOOKEEPER", "ZOOKEEPER_SERVER")).andReturn(type).anyTimes();
        type = org.apache.ambari.server.stack.HostsType.normal("h1", "h2");
        EasyMock.expect(m_masterHostResolver.getMasterAndHosts("ZOOKEEPER", "ZOOKEEPER_CLIENT")).andReturn(type).anyTimes();
        EasyMock.expect(m_masterHostResolver.getValueFromDesiredConfigurations("cluster-env", "rack_yaml_file_path")).andReturn("").anyTimes();
        EasyMock.replay(m_masterHostResolver);
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo si = ambariMetaInfo.getService("HDP", "2.1.1", "ZOOKEEPER");
        si.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo ci = si.getComponentByName("ZOOKEEPER_SERVER");
        ci.setDisplayName("ZooKeeper1 Server2");
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_multi_server_tasks");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(c, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, repoVersion220);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(2, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group1 = groups.get(0);
        org.junit.Assert.assertEquals(7, group1.items.size());
        org.junit.Assert.assertEquals(4, group1.items.get(0).getTasks().size());
        org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper taskWrapper = group1.items.get(0).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL, taskWrapper.getTasks().get(0).getType());
        taskWrapper = group1.items.get(0).getTasks().get(1);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE, taskWrapper.getTasks().get(0).getType());
        taskWrapper = group1.items.get(0).getTasks().get(2);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE, taskWrapper.getTasks().get(0).getType());
        taskWrapper = group1.items.get(0).getTasks().get(3);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.EXECUTE, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group1.items.get(1).getTasks().size());
        taskWrapper = group1.items.get(1).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.RESTART, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertTrue(taskWrapper.getHosts().contains("h1"));
        org.junit.Assert.assertEquals(1, group1.items.get(2).getTasks().size());
        taskWrapper = group1.items.get(2).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.SERVICE_CHECK, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group1.items.get(3).getTasks().size());
        taskWrapper = group1.items.get(3).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group1.items.get(4).getTasks().size());
        taskWrapper = group1.items.get(4).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.EXECUTE, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group1.items.get(5).getTasks().size());
        taskWrapper = group1.items.get(5).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.RESTART, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertTrue(taskWrapper.getHosts().contains("h2"));
        org.junit.Assert.assertEquals(1, group1.items.get(6).getTasks().size());
        taskWrapper = group1.items.get(6).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.SERVICE_CHECK, taskWrapper.getTasks().get(0).getType());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group2 = groups.get(1);
        org.junit.Assert.assertEquals(5, group2.items.size());
        org.junit.Assert.assertEquals(1, group2.items.get(0).getTasks().size());
        taskWrapper = group2.items.get(0).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group2.items.get(1).getTasks().size());
        taskWrapper = group2.items.get(1).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.SERVER_ACTION, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group2.items.get(2).getTasks().size());
        taskWrapper = group2.items.get(2).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.RESTART, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group2.items.get(3).getTasks().size());
        taskWrapper = group2.items.get(3).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.RESTART, taskWrapper.getTasks().get(0).getType());
        org.junit.Assert.assertEquals(1, group2.items.get(4).getTasks().size());
        taskWrapper = group2.items.get(4).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getTasks().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.SERVICE_CHECK, taskWrapper.getTasks().get(0).getType());
    }

    @org.junit.Test
    public void testHostGroupingOrchestration() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        java.lang.String clusterName = "c1";
        java.lang.String version = "2.1.1.0-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        org.apache.ambari.server.state.StackId stackId2 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion211 = helper.getOrCreateRepositoryVersion(stackId, version);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = helper.getOrCreateRepositoryVersion(stackId2, "2.2.0");
        for (int i = 0; i < 2; i++) {
            java.lang.String hostName = "h" + (i + 1);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6");
            host.setHostAttributes(hostAttributes);
            clusters.mapHostToCluster(hostName, clusterName);
        }
        c.addService(serviceFactory.createNew(c, "ZOOKEEPER", repoVersion211));
        c.addService(serviceFactory.createNew(c, "HBASE", repoVersion211));
        org.apache.ambari.server.state.Service zookeeper = c.getService("ZOOKEEPER");
        org.apache.ambari.server.state.Service hbase = c.getService("HBASE");
        org.apache.ambari.server.state.ServiceComponent zookeeperServer = zookeeper.addServiceComponent("ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponentHost zookeeperServer1 = zookeeperServer.addServiceComponentHost("h1");
        org.apache.ambari.server.state.ServiceComponentHost zookeeperServer2 = zookeeperServer.addServiceComponentHost("h2");
        org.apache.ambari.server.state.ServiceComponent hbaseMaster = hbase.addServiceComponent("HBASE_MASTER");
        org.apache.ambari.server.state.ServiceComponentHost hbaseMaster1 = hbaseMaster.addServiceComponentHost("h1");
        org.apache.ambari.server.stack.upgrade.HostOrderItem hostItem = new org.apache.ambari.server.stack.upgrade.HostOrderItem(org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType.HOST_UPGRADE, com.google.common.collect.Lists.newArrayList("h1", "h2"));
        org.apache.ambari.server.stack.upgrade.HostOrderItem checkItem = new org.apache.ambari.server.stack.upgrade.HostOrderItem(org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType.SERVICE_CHECK, com.google.common.collect.Lists.newArrayList("ZOOKEEPER", "HBASE"));
        org.apache.ambari.server.stack.upgrade.Grouping g = new org.apache.ambari.server.stack.upgrade.HostOrderGrouping();
        ((org.apache.ambari.server.stack.upgrade.HostOrderGrouping) (g)).setHostOrderItems(com.google.common.collect.Lists.newArrayList(hostItem, checkItem));
        g.title = "Some Title";
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = new org.apache.ambari.server.stack.upgrade.UpgradePack();
        java.lang.reflect.Field field = org.apache.ambari.server.stack.upgrade.UpgradePack.class.getDeclaredField("groups");
        field.setAccessible(true);
        field.set(upgradePack, com.google.common.collect.Lists.newArrayList(g));
        field = org.apache.ambari.server.stack.upgrade.UpgradePack.class.getDeclaredField("type");
        field.setAccessible(true);
        field.set(upgradePack, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED, repoVersion220);
        org.apache.ambari.server.stack.MasterHostResolver resolver = new org.apache.ambari.server.stack.MasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(resolver).anyTimes();
        EasyMock.replay(context);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgradePack, context);
        org.junit.Assert.assertEquals(1, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder = groups.get(0);
        org.junit.Assert.assertEquals(9, holder.items.size());
        for (int i = 0; i < 7; i++) {
            org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper w = holder.items.get(i);
            if ((i == 0) || (i == 4)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.STOP, w.getType());
            } else if ((i == 1) || (i == 5)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, w.getType());
                org.junit.Assert.assertEquals(1, w.getTasks().size());
                org.junit.Assert.assertEquals(1, w.getTasks().get(0).getTasks().size());
                org.apache.ambari.server.stack.upgrade.Task t = w.getTasks().get(0).getTasks().get(0);
                org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ManualTask.class, t.getClass());
                org.apache.ambari.server.stack.upgrade.ManualTask mt = ((org.apache.ambari.server.stack.upgrade.ManualTask) (t));
                org.junit.Assert.assertNotNull(mt.structuredOut);
                org.junit.Assert.assertTrue(mt.structuredOut.contains("type"));
                org.junit.Assert.assertTrue(mt.structuredOut.contains(org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType.HOST_UPGRADE.toString()));
                org.junit.Assert.assertTrue(mt.structuredOut.contains("host"));
                org.junit.Assert.assertTrue(mt.structuredOut.contains(i == 1 ? "h1" : "h2"));
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART, w.getType());
            }
        }
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK, holder.items.get(7).getType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK, holder.items.get(8).getType());
        zookeeperServer1.setVersion(repoVersion211.getVersion());
        zookeeperServer2.setVersion(repoVersion211.getVersion());
        hbaseMaster1.setVersion(repoVersion211.getVersion());
        context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED, repoVersion211);
        resolver = new org.apache.ambari.server.stack.MasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(resolver).anyTimes();
        EasyMock.replay(context);
        groups = m_upgradeHelper.createSequence(upgradePack, context);
        org.junit.Assert.assertEquals(1, groups.size());
        org.junit.Assert.assertEquals(2, groups.get(0).items.size());
        zookeeperServer1.setVersion(repoVersion211.getVersion());
        zookeeperServer2.setVersion(repoVersion220.getVersion());
        hbaseMaster1.setVersion(repoVersion211.getVersion());
        context = getMockUpgradeContextNoReplay(c, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED, repoVersion211);
        resolver = new org.apache.ambari.server.stack.MasterHostResolver(c, m_configHelper, context);
        EasyMock.expect(context.getResolver()).andReturn(resolver).anyTimes();
        EasyMock.replay(context);
        groups = m_upgradeHelper.createSequence(upgradePack, context);
        org.junit.Assert.assertEquals(1, groups.size());
        org.junit.Assert.assertEquals(5, groups.get(0).items.size());
    }

    @org.junit.Test
    public void testUpgradeConditions() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_conditions"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_conditions");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.controller.KerberosDetails kerberosDetails = createNiceMock(org.apache.ambari.server.controller.KerberosDetails.class);
        EasyMock.expect(kerberosDetails.getKdcType()).andReturn(org.apache.ambari.server.serveraction.kerberos.KDCType.NONE).atLeastOnce();
        EasyMock.replay(kerberosDetails);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, false);
        EasyMock.expect(context.getKerberosDetails()).andReturn(kerberosDetails).atLeastOnce();
        EasyMock.replay(context);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(1, groups.size());
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = groups.get(0).items;
        org.junit.Assert.assertEquals(1, stageWrappers.size());
        org.junit.Assert.assertEquals(1, stageWrappers.get(0).getTasks().size());
        java.util.Map<java.lang.String, java.lang.String> fooConfigs = new java.util.HashMap<>();
        fooConfigs.put("foo-property", "foo-value");
        org.apache.ambari.server.controller.ConfigurationRequest configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest();
        configurationRequest.setClusterName(cluster.getClusterName());
        configurationRequest.setType("foo-site");
        configurationRequest.setVersionTag("version1");
        configurationRequest.setProperties(fooConfigs);
        final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster.getClusterName(), cluster.getDesiredStackVersion().getStackVersion(), null);
        clusterRequest.setDesiredConfig(java.util.Collections.singletonList(configurationRequest));
        m_managementController.updateClusters(com.google.common.collect.Sets.newHashSet(clusterRequest), null);
        groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(2, groups.size());
        org.junit.Assert.assertEquals("ZOOKEEPER_CONFIG_CONDITION_TEST", groups.get(0).name);
        cluster.setSecurityType(org.apache.ambari.server.state.SecurityType.KERBEROS);
        groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(5, groups.size());
        org.easymock.EasyMock.verify(kerberosDetails);
    }

    @org.junit.Test
    public void testUpgradeOrchestrationWithRack() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService("HDP", "2.2.0", "ZOOKEEPER");
        serviceInfo.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo componentInfo = serviceInfo.getComponentByName("ZOOKEEPER_SERVER");
        componentInfo.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        java.io.File file = tmpFolder.newFile("rack_config.yaml");
        org.apache.commons.io.FileUtils.writeStringToFile(file, (("racks:\n" + ((((((((((("  racka-1:\n" + "    hostGroups:\n") + "    - hosts:\n") + "      - h2\n") + "      - h4\n") + "    - hosts:\n") + "      - h3\n") + "      - h5\n") + "  rackb-22:\n") + "    hosts:\n") + "    - h1\n") + "    - ")) + org.apache.ambari.server.utils.StageUtils.getHostName()) + "\n", java.nio.charset.Charset.defaultCharset(), false);
        org.apache.ambari.server.state.Cluster cluster = makeCluster(true, new java.util.HashSet<>(), file.getAbsolutePath());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(7, groups.size());
        org.junit.Assert.assertEquals("PRE_CLUSTER", groups.get(0).name);
        org.junit.Assert.assertEquals("ZOOKEEPER", groups.get(1).name);
        org.junit.Assert.assertEquals("CORE_MASTER", groups.get(2).name);
        org.junit.Assert.assertEquals("CORE_SLAVES", groups.get(3).name);
        org.junit.Assert.assertEquals("HIVE", groups.get(4).name);
        org.junit.Assert.assertEquals("OOZIE", groups.get(5).name);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder = groups.get(2);
        boolean found = false;
        for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw : holder.items) {
            if (sw.getTasksJson().contains("Upgrading your database")) {
                found = true;
            }
        }
        org.junit.Assert.assertTrue("Expected to find replaced text for Upgrading", found);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(1);
        org.junit.Assert.assertTrue(group.items.get(1).getText().contains("ZooKeeper1 Server2"));
        org.junit.Assert.assertEquals(group.items.get(5).getText(), "Service Check Zk");
        group = groups.get(3);
        org.junit.Assert.assertEquals(8, group.items.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper sw = group.items.get(3);
        org.junit.Assert.assertEquals("Validate Partial Upgrade", sw.getText());
        org.junit.Assert.assertEquals(1, sw.getTasks().size());
        org.junit.Assert.assertEquals(1, sw.getTasks().get(0).getTasks().size());
        org.apache.ambari.server.stack.upgrade.Task task = sw.getTasks().get(0).getTasks().get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ManualTask.class, task.getClass());
        org.apache.ambari.server.stack.upgrade.ManualTask mt = ((org.apache.ambari.server.stack.upgrade.ManualTask) (task));
        org.junit.Assert.assertTrue(mt.messages.get(0).contains("DataNode and NodeManager"));
        org.junit.Assert.assertNotNull(mt.structuredOut);
        org.junit.Assert.assertTrue(mt.structuredOut.contains("DATANODE"));
        org.junit.Assert.assertTrue(mt.structuredOut.contains("NODEMANAGER"));
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder postGroup = groups.get(6);
        org.junit.Assert.assertEquals("POST_CLUSTER", postGroup.name);
        org.junit.Assert.assertEquals("Finalize Upgrade", postGroup.title);
        org.junit.Assert.assertEquals(3, postGroup.items.size());
        org.junit.Assert.assertEquals("Confirm Finalize", postGroup.items.get(0).getText());
        org.junit.Assert.assertEquals("Execute HDFS Finalize", postGroup.items.get(1).getText());
        org.junit.Assert.assertEquals("Save Cluster State", postGroup.items.get(2).getText());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, postGroup.items.get(2).getType());
        org.junit.Assert.assertEquals(4, groups.get(0).items.size());
        org.junit.Assert.assertEquals(6, groups.get(1).items.size());
        org.junit.Assert.assertEquals(9, groups.get(2).items.size());
        org.junit.Assert.assertEquals(8, groups.get(3).items.size());
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testUpgradeOrchestrationWithRackError() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService("HDP", "2.2.0", "ZOOKEEPER");
        serviceInfo.setDisplayName("Zk");
        org.apache.ambari.server.state.ComponentInfo componentInfo = serviceInfo.getComponentByName("ZOOKEEPER_SERVER");
        componentInfo.setDisplayName("ZooKeeper1 Server2");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertNotNull(upgrade);
        java.io.File file = tmpFolder.newFile("rack_config.yaml");
        org.apache.commons.io.FileUtils.writeStringToFile(file, "racks:\n" + ((((("  racka-1:\n" + "    hostGroups:\n") + "    - hosts:\n") + "      - h4\n") + "    - hosts:\n") + "      - h5\n"), java.nio.charset.Charset.defaultCharset(), false);
        org.apache.ambari.server.state.Cluster cluster = makeCluster(true, new java.util.HashSet<>(), file.getAbsolutePath());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        try {
            m_upgradeHelper.createSequence(upgrade, context);
        } catch (java.lang.RuntimeException e) {
            org.junit.Assert.assertTrue(e.getCause().getMessage().contains("Rack mapping is not present for host name"));
        }
    }

    @org.junit.Test
    public void testMergeConfigurations() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion211 = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.apache.ambari.server.state.StackId stack211 = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        org.apache.ambari.server.state.StackId stack220 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        java.lang.String version211 = "2.1.1.0-1234";
        java.lang.String version220 = "2.2.0.0-1234";
        EasyMock.expect(repoVersion211.getStackId()).andReturn(stack211).atLeastOnce();
        EasyMock.expect(repoVersion211.getVersion()).andReturn(version211).atLeastOnce();
        EasyMock.expect(repoVersion220.getStackId()).andReturn(stack220).atLeastOnce();
        EasyMock.expect(repoVersion220.getVersion()).andReturn(version220).atLeastOnce();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stack211Configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack211FooType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack211BarType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack211BazType = new java.util.HashMap<>();
        stack211Configs.put("foo-site", stack211FooType);
        stack211Configs.put("bar-site", stack211BarType);
        stack211Configs.put("baz-site", stack211BazType);
        stack211FooType.put("1", "one");
        stack211FooType.put("1A", "one-A");
        stack211BarType.put("2", "two");
        stack211BazType.put("3", "three");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stack220Configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack220FooType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack220BazType = new java.util.HashMap<>();
        stack220Configs.put("foo-site", stack220FooType);
        stack220Configs.put("baz-site", stack220BazType);
        stack220FooType.put("1", "one-new");
        stack220FooType.put("1A1", "one-A-one");
        stack220BazType.put("3", "three-new");
        java.util.Map<java.lang.String, java.lang.String> existingFooType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> existingBarType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> existingBazType = new java.util.HashMap<>();
        org.apache.ambari.server.orm.entities.ClusterConfigEntity fooConfigEntity = createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity barConfigEntity = createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity bazConfigEntity = createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        EasyMock.expect(fooConfigEntity.getType()).andReturn("foo-site");
        EasyMock.expect(barConfigEntity.getType()).andReturn("bar-site");
        EasyMock.expect(bazConfigEntity.getType()).andReturn("baz-site");
        org.apache.ambari.server.state.Config fooConfig = createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.Config barConfig = createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.Config bazConfig = createNiceMock(org.apache.ambari.server.state.Config.class);
        existingFooType.put("1", "one");
        existingFooType.put("1A", "one-A");
        existingBarType.put("2", "two");
        existingBazType.put("3", "three-changed");
        EasyMock.expect(fooConfig.getType()).andReturn("foo-site").atLeastOnce();
        EasyMock.expect(barConfig.getType()).andReturn("bar-site").atLeastOnce();
        EasyMock.expect(bazConfig.getType()).andReturn("baz-site").atLeastOnce();
        EasyMock.expect(fooConfig.getProperties()).andReturn(existingFooType);
        EasyMock.expect(barConfig.getProperties()).andReturn(existingBarType);
        EasyMock.expect(bazConfig.getProperties()).andReturn(existingBazType);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigurations = new java.util.HashMap<>();
        desiredConfigurations.put("foo-site", null);
        desiredConfigurations.put("bar-site", null);
        desiredConfigurations.put("baz-site", null);
        org.apache.ambari.server.state.Service zookeeper = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(zookeeper.getName()).andReturn("ZOOKEEPER").atLeastOnce();
        EasyMock.expect(zookeeper.getServiceComponents()).andReturn(new java.util.HashMap<>()).once();
        zookeeper.setDesiredRepositoryVersion(repoVersion220);
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stack211).atLeastOnce();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stack220);
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(desiredConfigurations);
        EasyMock.expect(cluster.getDesiredConfigByType("foo-site")).andReturn(fooConfig);
        EasyMock.expect(cluster.getDesiredConfigByType("bar-site")).andReturn(barConfig);
        EasyMock.expect(cluster.getDesiredConfigByType("baz-site")).andReturn(bazConfig);
        EasyMock.expect(cluster.getService("ZOOKEEPER")).andReturn(zookeeper);
        EasyMock.expect(cluster.getDesiredConfigByType("foo-type")).andReturn(fooConfig);
        EasyMock.expect(cluster.getDesiredConfigByType("bar-type")).andReturn(barConfig);
        EasyMock.expect(cluster.getDesiredConfigByType("baz-type")).andReturn(bazConfig);
        @java.lang.SuppressWarnings("unchecked")
        com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> configHelperProvider = org.easymock.EasyMock.createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelperProvider.get()).andStubReturn(configHelper);
        EasyMock.expect(configHelper.getDefaultProperties(stack211, "ZOOKEEPER")).andReturn(stack211Configs).anyTimes();
        EasyMock.expect(configHelper.getDefaultProperties(stack220, "ZOOKEEPER")).andReturn(stack220Configs).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> expectedConfigurationsCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(configHelper.createConfigTypes(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class), org.easymock.EasyMock.capture(expectedConfigurationsCapture), org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(true);
        org.easymock.EasyMock.replay(configHelperProvider, configHelper);
        org.apache.ambari.server.orm.entities.ServiceConfigEntity zookeeperServiceConfig = createNiceMock(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        EasyMock.expect(zookeeperServiceConfig.getClusterConfigEntities()).andReturn(com.google.common.collect.Lists.newArrayList(fooConfigEntity, barConfigEntity, bazConfigEntity));
        org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAOMock;
        serviceConfigDAOMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ServiceConfigDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> latestServiceConfigs = com.google.common.collect.Lists.newArrayList(zookeeperServiceConfig);
        EasyMock.expect(serviceConfigDAOMock.getLastServiceConfigsForService(org.easymock.EasyMock.anyLong(), EasyMock.eq("ZOOKEEPER"))).andReturn(latestServiceConfigs).once();
        EasyMock.replay(serviceConfigDAOMock);
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgradePacks.get("upgrade_to_new_stack");
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        EasyMock.expect(context.getCluster()).andReturn(cluster).atLeastOnce();
        EasyMock.expect(context.getType()).andReturn(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING).atLeastOnce();
        EasyMock.expect(context.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).atLeastOnce();
        EasyMock.expect(context.getRepositoryVersion()).andReturn(repoVersion220).anyTimes();
        EasyMock.expect(context.getSupportedServices()).andReturn(com.google.common.collect.Sets.newHashSet("ZOOKEEPER")).atLeastOnce();
        EasyMock.expect(context.getSourceRepositoryVersion(org.easymock.EasyMock.anyString())).andReturn(repoVersion211).atLeastOnce();
        EasyMock.expect(context.getTargetRepositoryVersion(org.easymock.EasyMock.anyString())).andReturn(repoVersion220).atLeastOnce();
        EasyMock.expect(context.getOrchestrationType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        EasyMock.expect(context.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(context.getHostRoleCommandFactory()).andStubReturn(injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class));
        EasyMock.expect(context.getRoleGraphFactory()).andStubReturn(injector.getInstance(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
        EasyMock.expect(context.getUpgradePack()).andReturn(upgradePack).atLeastOnce();
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        upgradeHelper.m_serviceConfigDAO = serviceConfigDAOMock;
        upgradeHelper.m_configHelperProvider = configHelperProvider;
        upgradeHelper.updateDesiredRepositoriesAndConfigs(context);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedConfigurations = expectedConfigurationsCapture.getValue();
        java.util.Map<java.lang.String, java.lang.String> expectedFooType = expectedConfigurations.get("foo-site");
        java.util.Map<java.lang.String, java.lang.String> expectedBarType = expectedConfigurations.get("bar-site");
        java.util.Map<java.lang.String, java.lang.String> expectedBazType = expectedConfigurations.get("baz-site");
        org.junit.Assert.assertEquals(3, expectedConfigurations.size());
        org.junit.Assert.assertEquals("one-new", expectedFooType.get("1"));
        org.junit.Assert.assertEquals("one-A", expectedFooType.get("1A"));
        org.junit.Assert.assertEquals("two", expectedBarType.get("2"));
        org.junit.Assert.assertEquals("three-changed", expectedBazType.get("3"));
    }

    @org.junit.Test
    public void testMergeConfigurationsWithClusterEnv() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = makeCluster(true);
        org.apache.ambari.server.state.StackId oldStack = cluster.getDesiredStackVersion();
        org.apache.ambari.server.state.StackId newStack = new org.apache.ambari.server.state.StackId("HDP-2.5.0");
        org.apache.ambari.server.state.ConfigFactory cf = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config clusterEnv = cf.createNew(cluster, "cluster-env", "version1", com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("a", "b").build(), java.util.Collections.emptyMap());
        org.apache.ambari.server.state.Config zooCfg = cf.createNew(cluster, "zoo.cfg", "version1", com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("c", "d").build(), java.util.Collections.emptyMap());
        cluster.addDesiredConfig("admin", com.google.common.collect.Sets.newHashSet(clusterEnv, zooCfg));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackMap = new java.util.HashMap<>();
        stackMap.put("cluster-env", new java.util.HashMap<>());
        stackMap.put("hive-site", new java.util.HashMap<>());
        final java.util.Map<java.lang.String, java.lang.String> clusterEnvMap = new java.util.HashMap<>();
        org.easymock.Capture<org.apache.ambari.server.state.Cluster> captureCluster = org.easymock.Capture.newInstance();
        org.easymock.Capture<org.apache.ambari.server.state.StackId> captureStackId = org.easymock.Capture.newInstance();
        org.easymock.Capture<org.apache.ambari.server.controller.AmbariManagementController> captureAmc = org.easymock.Capture.newInstance();
        org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> cap = new org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>() {
            @java.lang.Override
            public void setValue(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> value) {
                if (value.containsKey("cluster-env")) {
                    clusterEnvMap.putAll(value.get("cluster-env"));
                }
            }
        };
        org.easymock.Capture<java.lang.String> captureUsername = org.easymock.Capture.newInstance();
        org.easymock.Capture<java.lang.String> captureNote = org.easymock.Capture.newInstance();
        org.easymock.EasyMock.reset(m_configHelper);
        EasyMock.expect(m_configHelper.getDefaultProperties(oldStack, "HIVE")).andReturn(stackMap).atLeastOnce();
        EasyMock.expect(m_configHelper.getDefaultProperties(newStack, "HIVE")).andReturn(stackMap).atLeastOnce();
        EasyMock.expect(m_configHelper.getDefaultProperties(oldStack, "ZOOKEEPER")).andReturn(stackMap).atLeastOnce();
        EasyMock.expect(m_configHelper.getDefaultProperties(newStack, "ZOOKEEPER")).andReturn(stackMap).atLeastOnce();
        EasyMock.expect(m_configHelper.createConfigTypes(org.easymock.EasyMock.capture(captureCluster), org.easymock.EasyMock.capture(captureStackId), org.easymock.EasyMock.capture(captureAmc), org.easymock.EasyMock.capture(cap), org.easymock.EasyMock.capture(captureUsername), org.easymock.EasyMock.capture(captureNote))).andReturn(true);
        EasyMock.replay(m_configHelper);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.5.0"), "2.5.0-1234");
        java.util.Map<java.lang.String, java.lang.Object> upgradeRequestMap = new java.util.HashMap<>();
        upgradeRequestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        upgradeRequestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, repoVersionEntity.getId().toString());
        upgradeRequestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test_HDP-250");
        upgradeRequestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory contextFactory = injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = contextFactory.create(cluster, upgradeRequestMap);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        upgradeHelper.updateDesiredRepositoriesAndConfigs(context);
        org.junit.Assert.assertNotNull(clusterEnvMap);
        org.junit.Assert.assertTrue(clusterEnvMap.containsKey("a"));
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testSequentialServiceChecks() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_checks"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_checks");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        cluster.deleteService("HDFS", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        cluster.deleteService("YARN", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2110);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(5, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder serviceCheckGroup = groups.get(2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class, serviceCheckGroup.groupClass);
        org.junit.Assert.assertEquals(3, serviceCheckGroup.items.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = serviceCheckGroup.items.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper.class, wrapper.getClass());
        org.junit.Assert.assertTrue(wrapper.getText().contains("ZooKeeper"));
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testSequentialServiceChecksWithServiceCheckFailure() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_checks"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_checks");
        org.junit.Assert.assertNotNull(upgrade);
        for (org.apache.ambari.server.stack.upgrade.Grouping g : upgrade.getAllGroups()) {
            if (g.name.equals("SERVICE_CHECK_1") || g.name.equals("SERVICE_CHECK_2")) {
                g.skippable = true;
            }
        }
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        cluster.deleteService("HDFS", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        cluster.deleteService("YARN", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2110, org.apache.ambari.spi.RepositoryType.STANDARD, cluster.getServices().keySet(), m_masterHostResolver, false);
        EasyMock.expect(context.isServiceCheckFailureAutoSkipped()).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        EasyMock.replay(context);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(5, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder serviceCheckGroup = groups.get(2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class, serviceCheckGroup.groupClass);
        org.junit.Assert.assertEquals(4, serviceCheckGroup.items.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = serviceCheckGroup.items.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper.class, wrapper.getClass());
        org.junit.Assert.assertTrue(wrapper.getText().contains("ZooKeeper"));
        wrapper = serviceCheckGroup.items.get(serviceCheckGroup.items.size() - 1);
        org.junit.Assert.assertTrue(wrapper.getText().equals("Verifying Skipped Failures"));
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testPrematureServiceChecks() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_checks"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_checks");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        cluster.deleteService("HDFS", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        cluster.deleteService("YARN", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        cluster.deleteService("ZOOKEEPER", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion2110);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(3, groups.size());
        for (org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder : groups) {
            org.junit.Assert.assertFalse(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class.equals(holder.groupClass));
        }
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testAddComponentsDuringUpgrade() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_add_component"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_add_component");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        org.junit.Assert.assertEquals(3, groups.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group = groups.get(0);
        org.junit.Assert.assertEquals("STOP_HIVE", group.name);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = group.items;
        org.junit.Assert.assertEquals(2, stageWrappers.size());
        group = groups.get(2);
        org.junit.Assert.assertEquals("RESTART_HIVE", group.name);
        stageWrappers = group.items;
        org.junit.Assert.assertEquals(4, stageWrappers.size());
        stackManagerMock.invalidateCurrentPaths();
        ambariMetaInfo.init();
    }

    @org.junit.Test
    public void testParallelClients() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_parallel_client"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_parallel_client");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.state.Service s = cluster.getService("ZOOKEEPER");
        org.apache.ambari.server.state.ServiceComponent sc = s.addServiceComponent("ZOOKEEPER_CLIENT");
        sc.addServiceComponentHost("h1");
        sc.addServiceComponentHost("h2");
        sc.addServiceComponentHost("h3");
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        java.util.Optional<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> optional = groups.stream().filter(g -> g.name.equals("ZK_CLIENTS")).findAny();
        org.junit.Assert.assertTrue(optional.isPresent());
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder = optional.get();
        org.junit.Assert.assertEquals(3, holder.items.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART, holder.items.get(0).getType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK, holder.items.get(1).getType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART, holder.items.get(2).getType());
        org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper taskWrapper = holder.items.get(0).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getHosts().size());
        java.lang.String host1 = taskWrapper.getHosts().iterator().next();
        taskWrapper = holder.items.get(1).getTasks().get(0);
        org.junit.Assert.assertEquals(1, taskWrapper.getHosts().size());
        java.lang.String host2 = taskWrapper.getHosts().iterator().next();
        org.junit.Assert.assertEquals(host1, host2);
        taskWrapper = holder.items.get(2).getTasks().get(0);
        org.junit.Assert.assertEquals(2, taskWrapper.getHosts().size());
    }

    @org.junit.Test
    public void testOrchestrationOptions() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_from_211"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_from_211");
        org.junit.Assert.assertNotNull(upgrade);
        org.apache.ambari.server.state.Cluster cluster = makeCluster();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = getMockUpgradeContext(cluster, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, false);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.SimpleOrchestrationOptions options = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelperTest.SimpleOrchestrationOptions(1);
        EasyMock.expect(context.getOrchestrationOptions()).andReturn(options).anyTimes();
        EasyMock.replay(context);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = m_upgradeHelper.createSequence(upgrade, context);
        groups = groups.stream().filter(g -> g.name.equals("CORE_SLAVES")).collect(java.util.stream.Collectors.toList());
        org.junit.Assert.assertEquals(1, groups.size());
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> restarts = groups.get(0).items.stream().filter(sw -> (sw.getType() == org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART) && sw.getText().contains("DataNode")).collect(java.util.stream.Collectors.toList());
        org.junit.Assert.assertEquals("Expecting wrappers for each of 3 hosts", 3, restarts.size());
        options.m_count = 2;
        groups = m_upgradeHelper.createSequence(upgrade, context);
        groups = groups.stream().filter(g -> g.name.equals("CORE_SLAVES")).collect(java.util.stream.Collectors.toList());
        org.junit.Assert.assertEquals(1, groups.size());
        restarts = groups.get(0).items.stream().filter(sw -> (sw.getType() == org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.RESTART) && sw.getText().contains("DataNode")).collect(java.util.stream.Collectors.toList());
        org.junit.Assert.assertEquals("Expecting wrappers for each", 2, restarts.size());
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext getMockUpgradeContext(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type) {
        return getMockUpgradeContext(cluster, direction, type, repositoryVersion2210);
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext getMockUpgradeContext(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, boolean replay) {
        return getMockUpgradeContext(cluster, direction, type, repositoryVersion2210, org.apache.ambari.spi.RepositoryType.STANDARD, cluster.getServices().keySet(), m_masterHostResolver, replay);
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext getMockUpgradeContext(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        java.util.Set<java.lang.String> allServices = cluster.getServices().keySet();
        return getMockUpgradeContext(cluster, direction, type, repositoryVersion, org.apache.ambari.spi.RepositoryType.STANDARD, allServices);
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext getMockUpgradeContext(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, org.apache.ambari.spi.RepositoryType repositoryType, java.util.Set<java.lang.String> services) {
        return getMockUpgradeContext(cluster, direction, type, repositoryVersion, repositoryType, services, m_masterHostResolver, true);
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext getMockUpgradeContextNoReplay(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        java.util.Set<java.lang.String> allServices = cluster.getServices().keySet();
        return getMockUpgradeContext(cluster, direction, type, repositoryVersion, org.apache.ambari.spi.RepositoryType.STANDARD, allServices, null, false);
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext getMockUpgradeContext(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, final org.apache.ambari.spi.RepositoryType repositoryType, java.util.Set<java.lang.String> services, org.apache.ambari.server.stack.MasterHostResolver resolver, boolean replay) {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        EasyMock.expect(context.getCluster()).andReturn(cluster).anyTimes();
        EasyMock.expect(context.getType()).andReturn(type).anyTimes();
        EasyMock.expect(context.getDirection()).andReturn(direction).anyTimes();
        EasyMock.expect(context.getRepositoryVersion()).andReturn(repositoryVersion).anyTimes();
        EasyMock.expect(context.getSupportedServices()).andReturn(services).anyTimes();
        EasyMock.expect(context.getOrchestrationType()).andReturn(repositoryType).anyTimes();
        EasyMock.expect(context.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(context.getHostRoleCommandFactory()).andStubReturn(injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class));
        EasyMock.expect(context.getRoleGraphFactory()).andStubReturn(injector.getInstance(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
        if (null != resolver) {
            EasyMock.expect(context.getResolver()).andReturn(resolver).anyTimes();
        }
        final java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> targetRepositoryVersions = new java.util.HashMap<>();
        for (java.lang.String serviceName : services) {
            targetRepositoryVersions.put(serviceName, repositoryVersion);
        }
        final org.easymock.Capture<java.lang.String> repoVersionServiceName = org.easymock.EasyMock.newCapture();
        EasyMock.expect(context.getTargetRepositoryVersion(org.easymock.EasyMock.capture(repoVersionServiceName))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.orm.entities.RepositoryVersionEntity>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.entities.RepositoryVersionEntity answer() {
                return targetRepositoryVersions.get(repoVersionServiceName.getValue());
            }
        }).anyTimes();
        final org.easymock.Capture<java.lang.String> serviceNameSupported = org.easymock.EasyMock.newCapture();
        EasyMock.expect(context.isServiceSupported(org.easymock.EasyMock.capture(serviceNameSupported))).andAnswer(new org.easymock.IAnswer<java.lang.Boolean>() {
            @java.lang.Override
            public java.lang.Boolean answer() {
                return targetRepositoryVersions.containsKey(serviceNameSupported.getValue());
            }
        }).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> serviceNames = new java.util.HashMap<>();
        final org.easymock.Capture<java.lang.String> serviceDisplayNameArg1 = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<java.lang.String> serviceDisplayNameArg2 = org.easymock.EasyMock.newCapture();
        context.setServiceDisplay(org.easymock.EasyMock.capture(serviceDisplayNameArg1), org.easymock.EasyMock.capture(serviceDisplayNameArg2));
        EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer() {
                serviceNames.put(serviceDisplayNameArg1.getValue(), serviceDisplayNameArg2.getValue());
                return null;
            }
        }).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> componentNames = new java.util.HashMap<>();
        final org.easymock.Capture<java.lang.String> componentDisplayNameArg1 = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<java.lang.String> componentDisplayNameArg2 = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<java.lang.String> componentDisplayNameArg3 = org.easymock.EasyMock.newCapture();
        context.setComponentDisplay(org.easymock.EasyMock.capture(componentDisplayNameArg1), org.easymock.EasyMock.capture(componentDisplayNameArg2), org.easymock.EasyMock.capture(componentDisplayNameArg3));
        EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer() {
                componentNames.put((componentDisplayNameArg1.getValue() + ":") + componentDisplayNameArg2.getValue(), componentDisplayNameArg3.getValue());
                return null;
            }
        }).anyTimes();
        final org.easymock.Capture<java.lang.String> getServiceDisplayArgument1 = org.easymock.EasyMock.newCapture();
        EasyMock.expect(context.getServiceDisplay(org.easymock.EasyMock.capture(getServiceDisplayArgument1))).andAnswer(new org.easymock.IAnswer<java.lang.String>() {
            @java.lang.Override
            public java.lang.String answer() {
                return serviceNames.get(getServiceDisplayArgument1.getValue());
            }
        }).anyTimes();
        final org.easymock.Capture<java.lang.String> getComponentDisplayArgument1 = org.easymock.EasyMock.newCapture();
        final org.easymock.Capture<java.lang.String> getComponentDisplayArgument2 = org.easymock.EasyMock.newCapture();
        EasyMock.expect(context.getComponentDisplay(org.easymock.EasyMock.capture(getComponentDisplayArgument1), org.easymock.EasyMock.capture(getComponentDisplayArgument2))).andAnswer(new org.easymock.IAnswer<java.lang.String>() {
            @java.lang.Override
            public java.lang.String answer() {
                return componentNames.get((getComponentDisplayArgument1.getValue() + ":") + getComponentDisplayArgument2.getValue());
            }
        }).anyTimes();
        final org.easymock.Capture<org.apache.ambari.server.stack.upgrade.UpgradeScope> isScopedCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(context.isScoped(org.easymock.EasyMock.capture(isScopedCapture))).andStubAnswer(new org.easymock.IAnswer<java.lang.Boolean>() {
            @java.lang.Override
            public java.lang.Boolean answer() throws java.lang.Throwable {
                org.apache.ambari.server.stack.upgrade.UpgradeScope scope = isScopedCapture.getValue();
                if (scope == org.apache.ambari.server.stack.upgrade.UpgradeScope.ANY) {
                    return true;
                }
                if (scope == org.apache.ambari.server.stack.upgrade.UpgradeScope.PARTIAL) {
                    return repositoryType != org.apache.ambari.spi.RepositoryType.STANDARD;
                }
                return repositoryType == org.apache.ambari.spi.RepositoryType.STANDARD;
            }
        });
        if (replay) {
            EasyMock.replay(context);
        }
        return context;
    }

    private class MockMasterHostResolver extends org.apache.ambari.server.stack.MasterHostResolver {
        public MockMasterHostResolver(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context) {
            super(cluster, configHelper, context);
        }

        @java.lang.Override
        public java.lang.String queryJmxBeanValue(java.lang.String hostname, int port, java.lang.String beanName, java.lang.String attributeName, boolean asQuery, boolean encrypted) {
            if ((beanName.equalsIgnoreCase("Hadoop:service=NameNode,name=NameNodeStatus") && attributeName.equalsIgnoreCase("State")) && asQuery) {
                switch (hostname) {
                    case "H1" :
                        return org.apache.ambari.server.stack.MasterHostResolver.Status.ACTIVE.toString();
                    case "H2" :
                        return org.apache.ambari.server.stack.MasterHostResolver.Status.STANDBY.toString();
                    case "H3" :
                        return org.apache.ambari.server.stack.MasterHostResolver.Status.ACTIVE.toString();
                    case "H4" :
                        return org.apache.ambari.server.stack.MasterHostResolver.Status.STANDBY.toString();
                    default :
                        return "UNKNOWN_NAMENODE_STATUS_FOR_THIS_HOST";
                }
            }
            return "NOT_MOCKED";
        }
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
            binder.bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(m_configHelper);
            binder.bind(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class).toInstance(createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class));
        }
    }

    private static class BadMasterHostResolver extends org.apache.ambari.server.stack.MasterHostResolver {
        public BadMasterHostResolver(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context) {
            super(cluster, configHelper, context);
        }

        @java.lang.Override
        protected java.lang.String queryJmxBeanValue(java.lang.String hostname, int port, java.lang.String beanName, java.lang.String attributeName, boolean asQuery, boolean encrypted) {
            return null;
        }
    }

    private static class SimpleOrchestrationOptions implements org.apache.ambari.spi.upgrade.OrchestrationOptions {
        private int m_count;

        private SimpleOrchestrationOptions(int count) {
            m_count = count;
        }

        @java.lang.Override
        public int getConcurrencyCount(org.apache.ambari.spi.ClusterInformation cluster, java.lang.String service, java.lang.String component) {
            return m_count;
        }
    }
}