package org.apache.ambari.server.state.stack;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
@org.junit.Ignore
@org.junit.experimental.categories.Category({ category.StackUpgradeTest.class })
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.MULTI_SERVICE, comment = "This was a very useful test that no longer works since all of the XML files for " + ("upgrades are delivered in mpacks. This should be converted into a some realtime check " + "when an mpack is registered."))
public class ConfigUpgradeValidityTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.stack.ConfigUpgradeValidityTest.class);

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory upgradeContextFactory;

    private int validatedConfigCount = 0;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        validatedConfigCount = 0;
        org.apache.ambari.server.orm.InMemoryDefaultTestModule testModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        testModule.getProperties().put(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), "src/main/resources/stacks");
        injector = com.google.inject.Guice.createInjector(testModule);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        upgradeContextFactory = injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    @org.junit.Ignore("Ignoring until active HDP stacks are available")
    public void testConfigurationDefinitionsExist() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = ambariMetaInfo.getStacks();
        junit.framework.Assert.assertFalse(stacks.isEmpty());
        org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        for (org.apache.ambari.server.state.StackInfo stack : stacks) {
            if (!stack.isActive()) {
                org.apache.ambari.server.state.stack.ConfigUpgradeValidityTest.LOG.info("Skipping configuration validity test for {}", new org.apache.ambari.server.state.StackId(stack));
                continue;
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks = ambariMetaInfo.getUpgradePacks(stack.getName(), stack.getVersion());
            for (java.lang.String key : upgradePacks.keySet()) {
                org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgradePacks.get(key);
                final org.apache.ambari.server.state.StackId sourceStack = new org.apache.ambari.server.state.StackId(stack);
                final org.apache.ambari.server.orm.entities.RepositoryVersionEntity rve = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity() {
                    {
                        setStack(new org.apache.ambari.server.orm.entities.StackEntity() {
                            {
                                setStackName(sourceStack.getStackName());
                                setStackVersion(sourceStack.getStackVersion());
                            }
                        });
                    }
                };
                final org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = new org.apache.ambari.server.orm.entities.UpgradeEntity();
                org.apache.ambari.server.orm.entities.UpgradeHistoryEntity upgradeHistoryEntity = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity() {
                    {
                        setServiceName("TEST");
                        setComponentName("TEST");
                        setFromRepositoryVersion(rve);
                        setUpgrade(upgradeEntity);
                    }
                };
                upgradeEntity.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
                upgradeEntity.addHistory(upgradeHistoryEntity);
                upgradeEntity.setRepositoryVersion(rve);
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext cx = upgradeContextFactory.create(cluster, upgradeEntity);
                org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.ConfigurationPackBuilder.build(cx);
                java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups = upgradePack.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
                for (org.apache.ambari.server.stack.upgrade.Grouping group : groups) {
                    if (group instanceof org.apache.ambari.server.stack.upgrade.ClusterGrouping) {
                        org.apache.ambari.server.stack.upgrade.ClusterGrouping clusterGrouping = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group));
                        if (null != clusterGrouping.executionStages) {
                            for (org.apache.ambari.server.stack.upgrade.ExecuteStage executionStage : clusterGrouping.executionStages) {
                                if (executionStage.task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE) {
                                    org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (executionStage.task));
                                    assertIdDefinitionExists(configureTask.id, configUpgradePack, upgradePack, sourceStack);
                                    if (org.apache.commons.lang.StringUtils.isNotBlank(executionStage.service)) {
                                        junit.framework.Assert.assertEquals(executionStage.service, configureTask.associatedService);
                                    } else {
                                        junit.framework.Assert.assertTrue(null == configureTask.associatedService);
                                    }
                                }
                            }
                        }
                    }
                }
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent>> tasks = upgradePack.getTasks();
                for (java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent> value : tasks.values()) {
                    for (org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent processingComponent : value.values()) {
                        if (null != processingComponent.preTasks) {
                            for (org.apache.ambari.server.stack.upgrade.Task preTask : processingComponent.preTasks) {
                                if (preTask.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE) {
                                    org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (preTask));
                                    assertIdDefinitionExists(configureTask.id, configUpgradePack, upgradePack, sourceStack);
                                    junit.framework.Assert.assertTrue(org.apache.commons.lang.StringUtils.isNotBlank(configureTask.associatedService));
                                }
                            }
                            if (null != processingComponent.tasks) {
                                for (org.apache.ambari.server.stack.upgrade.Task task : processingComponent.tasks) {
                                    if (task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE) {
                                        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (task));
                                        assertIdDefinitionExists(configureTask.id, configUpgradePack, upgradePack, sourceStack);
                                        junit.framework.Assert.assertTrue(org.apache.commons.lang.StringUtils.isNotBlank(configureTask.associatedService));
                                    }
                                }
                            }
                            if (null != processingComponent.postTasks) {
                                for (org.apache.ambari.server.stack.upgrade.Task postTask : processingComponent.postTasks) {
                                    if (postTask.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE) {
                                        org.apache.ambari.server.stack.upgrade.ConfigureTask configureTask = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (postTask));
                                        assertIdDefinitionExists(configureTask.id, configUpgradePack, upgradePack, sourceStack);
                                        junit.framework.Assert.assertTrue(org.apache.commons.lang.StringUtils.isNotBlank(configureTask.associatedService));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        junit.framework.Assert.assertTrue(validatedConfigCount > 100);
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testValidateConfigUpgradePacks() throws java.lang.Exception {
        org.apache.commons.io.filefilter.IOFileFilter filter = new org.apache.commons.io.filefilter.IOFileFilter() {
            @java.lang.Override
            public boolean accept(java.io.File dir, java.lang.String name) {
                return false;
            }

            @java.lang.Override
            public boolean accept(java.io.File file) {
                if (file.getAbsolutePath().contains("upgrades") && file.getAbsolutePath().endsWith("config-upgrade.xml")) {
                    return true;
                }
                return false;
            }
        };
        java.util.List<java.io.File> files = new java.util.ArrayList<>();
        files.addAll(org.apache.commons.io.FileUtils.listFiles(new java.io.File("src/main/resources/stacks"), filter, org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter()));
        files.addAll(org.apache.commons.io.FileUtils.listFiles(new java.io.File("src/test/resources/stacks"), filter, org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter()));
        files.addAll(org.apache.commons.io.FileUtils.listFiles(new java.io.File("src/test/resources/stacks_with_upgrade_cycle"), filter, org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter()));
        org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller = new org.apache.ambari.server.stack.ModuleFileUnmarshaller();
        int filesTestedCount = 0;
        for (java.io.File file : files) {
            java.lang.String fileContent = org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
            if (fileContent.contains("<upgrade-config-changes") && fileContent.contains("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"")) {
                if (!fileContent.contains("xsi:noNamespaceSchemaLocation=\"upgrade-config.xsd\"")) {
                    java.lang.String msg = java.lang.String.format("File %s appears to be a config upgrade pack, but does not define 'upgrade-config.xsd' as its schema", file.getAbsolutePath());
                    junit.framework.Assert.fail(msg);
                } else {
                    filesTestedCount++;
                    unmarshaller.unmarshal(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.class, file, true);
                }
            }
        }
        junit.framework.Assert.assertTrue("This test didn't appear to do any work which could indicate that it failed to find files to validate", filesTestedCount > 5);
    }

    private void assertIdDefinitionExists(java.lang.String id, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack, org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, org.apache.ambari.server.state.StackId sourceStackId) {
        junit.framework.Assert.assertNotNull(id);
        if (configUpgradePack.enumerateConfigChangesByID().containsKey(id)) {
            validatedConfigCount++;
            org.apache.ambari.server.state.stack.ConfigUpgradeValidityTest.LOG.info("Validated {} from upgrade pack {} for {}", id, upgradePack.getTargetStack(), sourceStackId);
            return;
        }
        junit.framework.Assert.fail(java.lang.String.format("Missing %s in upgrade from %s to %s (%s)", id, sourceStackId, upgradePack.getTargetStack(), upgradePack.getType()));
    }
}