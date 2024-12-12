package org.apache.ambari.server.state.stack;
@org.junit.experimental.categories.Category({ category.StackUpgradeTest.class })
public class UpgradePackTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.stack.UpgradePackTest.class);

    @org.junit.Before
    public void before() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testIsDowngradeAllowed() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.junit.Assert.assertTrue(upgrades.size() > 0);
        java.lang.String upgradePackWithoutDowngrade = "upgrade_test_no_downgrade";
        boolean foundAtLeastOnePackWithoutDowngrade = false;
        for (java.lang.String key : upgrades.keySet()) {
            org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgrades.get(key);
            if (upgradePack.getName().equals(upgradePackWithoutDowngrade)) {
                foundAtLeastOnePackWithoutDowngrade = true;
                org.junit.Assert.assertFalse(upgradePack.isDowngradeAllowed());
                continue;
            }
            org.junit.Assert.assertTrue(upgradePack.isDowngradeAllowed());
        }
        org.junit.Assert.assertTrue(foundAtLeastOnePackWithoutDowngrade);
    }

    @org.junit.Test
    public void testExistence() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("foo", "bar");
        org.junit.Assert.assertTrue(upgrades.isEmpty());
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.size() > 0);
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
    }

    @org.junit.Test
    public void testUpgradeParsing() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.size() > 0);
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test");
        org.junit.Assert.assertEquals("2.2.*.*", upgrade.getTarget());
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> expectedStages = new java.util.LinkedHashMap<java.lang.String, java.util.List<java.lang.String>>() {
            {
                put("ZOOKEEPER", java.util.Arrays.asList("ZOOKEEPER_SERVER"));
                put("HDFS", java.util.Arrays.asList("NAMENODE", "DATANODE"));
            }
        };
        int i = 0;
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : expectedStages.entrySet()) {
            org.junit.Assert.assertTrue(upgrade.getTasks().containsKey(entry.getKey()));
            org.junit.Assert.assertEquals(i++, indexOf(upgrade.getTasks(), entry.getKey()));
            org.junit.Assert.assertEquals(entry.getValue().size(), upgrade.getTasks().get(entry.getKey()).size());
            int j = 0;
            for (java.lang.String comp : entry.getValue()) {
                org.junit.Assert.assertEquals(j++, indexOf(upgrade.getTasks().get(entry.getKey()), comp));
            }
        }
        org.junit.Assert.assertTrue(upgrade.getTasks().containsKey("HDFS"));
        org.junit.Assert.assertTrue(upgrade.getTasks().get("HDFS").containsKey("NAMENODE"));
        org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc = upgrade.getTasks().get("HDFS").get("NAMENODE");
        org.junit.Assert.assertNotNull(pc.preTasks);
        org.junit.Assert.assertNotNull(pc.postTasks);
        org.junit.Assert.assertNotNull(pc.tasks);
        org.junit.Assert.assertNotNull(pc.preDowngradeTasks);
        org.junit.Assert.assertNotNull(pc.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, pc.tasks.size());
        org.junit.Assert.assertEquals(3, pc.preDowngradeTasks.size());
        org.junit.Assert.assertEquals(1, pc.postDowngradeTasks.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Task.Type.RESTART, pc.tasks.get(0).getType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.RestartTask.class, pc.tasks.get(0).getClass());
        org.junit.Assert.assertTrue(upgrade.getTasks().containsKey("ZOOKEEPER"));
        org.junit.Assert.assertTrue(upgrade.getTasks().get("ZOOKEEPER").containsKey("ZOOKEEPER_SERVER"));
        pc = upgrade.getTasks().get("HDFS").get("DATANODE");
        org.junit.Assert.assertNotNull(pc.preDowngradeTasks);
        org.junit.Assert.assertEquals(0, pc.preDowngradeTasks.size());
        org.junit.Assert.assertNotNull(pc.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, pc.postDowngradeTasks.size());
        pc = upgrade.getTasks().get("ZOOKEEPER").get("ZOOKEEPER_SERVER");
        org.junit.Assert.assertNotNull(pc.preTasks);
        org.junit.Assert.assertEquals(1, pc.preTasks.size());
        org.junit.Assert.assertNotNull(pc.postTasks);
        org.junit.Assert.assertEquals(1, pc.postTasks.size());
        org.junit.Assert.assertNotNull(pc.tasks);
        org.junit.Assert.assertEquals(1, pc.tasks.size());
        pc = upgrade.getTasks().get("YARN").get("NODEMANAGER");
        org.junit.Assert.assertNotNull(pc.preTasks);
        org.junit.Assert.assertEquals(2, pc.preTasks.size());
        org.apache.ambari.server.stack.upgrade.Task t = pc.preTasks.get(1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ConfigureTask.class, t.getClass());
        org.apache.ambari.server.stack.upgrade.ConfigureTask ct = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (t));
        org.junit.Assert.assertEquals("hdp_2_1_1_nm_pre_upgrade", ct.getId());
        org.junit.Assert.assertFalse(ct.supportsPatch);
    }

    @org.junit.Test
    public void testGroupOrdersForRolling() {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.size() > 0);
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_checks"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_checks");
        org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckConfig prerequisiteCheckConfig = upgrade.getPrerequisiteCheckConfig();
        org.junit.Assert.assertNotNull(prerequisiteCheckConfig);
        org.junit.Assert.assertNotNull(prerequisiteCheckConfig.globalProperties);
        org.junit.Assert.assertTrue(prerequisiteCheckConfig.getGlobalProperties().containsKey("global-property-1"));
        org.junit.Assert.assertEquals("global-value-1", prerequisiteCheckConfig.getGlobalProperties().get("global-property-1"));
        org.junit.Assert.assertNotNull(prerequisiteCheckConfig.prerequisiteCheckProperties);
        org.junit.Assert.assertEquals(2, prerequisiteCheckConfig.prerequisiteCheckProperties.size());
        org.junit.Assert.assertNotNull(prerequisiteCheckConfig.getCheckProperties("org.apache.ambari.server.checks.ServicesMapReduceDistributedCacheCheck"));
        org.junit.Assert.assertTrue(prerequisiteCheckConfig.getCheckProperties("org.apache.ambari.server.checks.ServicesMapReduceDistributedCacheCheck").containsKey("dfs-protocols-regex"));
        org.junit.Assert.assertEquals("^([^:]*dfs|wasb|ecs):.*", prerequisiteCheckConfig.getCheckProperties("org.apache.ambari.server.checks.ServicesMapReduceDistributedCacheCheck").get("dfs-protocols-regex"));
        org.junit.Assert.assertNotNull(prerequisiteCheckConfig.getCheckProperties("org.apache.ambari.server.checks.ServicesTezDistributedCacheCheck"));
        org.junit.Assert.assertTrue(prerequisiteCheckConfig.getCheckProperties("org.apache.ambari.server.checks.ServicesTezDistributedCacheCheck").containsKey("dfs-protocols-regex"));
        org.junit.Assert.assertEquals("^([^:]*dfs|wasb|ecs):.*", prerequisiteCheckConfig.getCheckProperties("org.apache.ambari.server.checks.ServicesTezDistributedCacheCheck").get("dfs-protocols-regex"));
        java.util.List<java.lang.String> expected_up = java.util.Arrays.asList("PRE_CLUSTER", "ZOOKEEPER", "CORE_MASTER", "SERVICE_CHECK_1", "CORE_SLAVES", "SERVICE_CHECK_2", "OOZIE", "POST_CLUSTER");
        java.util.List<java.lang.String> expected_down = java.util.Arrays.asList("PRE_CLUSTER", "OOZIE", "CORE_SLAVES", "SERVICE_CHECK_2", "CORE_MASTER", "SERVICE_CHECK_1", "ZOOKEEPER", "POST_CLUSTER");
        org.apache.ambari.server.stack.upgrade.Grouping serviceCheckGroup = null;
        int i = 0;
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        for (org.apache.ambari.server.stack.upgrade.Grouping g : groups) {
            org.junit.Assert.assertEquals(expected_up.get(i), g.name);
            i++;
            if (g.name.equals("SERVICE_CHECK_1")) {
                serviceCheckGroup = g;
            }
        }
        java.util.List<java.lang.String> expected_priority = java.util.Arrays.asList("HDFS", "HBASE", "YARN");
        org.junit.Assert.assertNotNull(serviceCheckGroup);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class, serviceCheckGroup.getClass());
        org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping scg = ((org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping) (serviceCheckGroup));
        java.util.Set<java.lang.String> priorities = scg.getPriorities();
        org.junit.Assert.assertEquals(3, priorities.size());
        i = 0;
        for (java.lang.String s : priorities) {
            org.junit.Assert.assertEquals(expected_priority.get(i++), s);
        }
        i = 0;
        groups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        for (org.apache.ambari.server.stack.upgrade.Grouping g : groups) {
            org.junit.Assert.assertEquals(expected_down.get(i), g.name);
            i++;
        }
    }

    @org.junit.Test
    public void testGroupOrdersForNonRolling() {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.size() > 0);
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_nonrolling"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_nonrolling");
        java.util.List<java.lang.String> expected_up = java.util.Arrays.asList("PRE_CLUSTER", "Stop High-Level Daemons", "Backups", "Stop Low-Level Daemons", "UPDATE_DESIRED_REPOSITORY_ID", "ALL_HOST_OPS", "ZOOKEEPER", "HDFS", "MR and YARN", "POST_CLUSTER");
        java.util.List<java.lang.String> expected_down = java.util.Arrays.asList("Restore Backups", "UPDATE_DESIRED_REPOSITORY_ID", "ALL_HOST_OPS", "ZOOKEEPER", "HDFS", "MR and YARN", "POST_CLUSTER");
        java.util.Iterator<java.lang.String> itr_up = expected_up.iterator();
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> upgrade_groups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        for (org.apache.ambari.server.stack.upgrade.Grouping g : upgrade_groups) {
            org.junit.Assert.assertEquals(true, itr_up.hasNext());
            org.junit.Assert.assertEquals(itr_up.next(), g.name);
        }
        java.util.Iterator<java.lang.String> itr_down = expected_down.iterator();
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> downgrade_groups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        for (org.apache.ambari.server.stack.upgrade.Grouping g : downgrade_groups) {
            org.junit.Assert.assertEquals(true, itr_down.hasNext());
            org.junit.Assert.assertEquals(itr_down.next(), g.name);
        }
    }

    @org.junit.Test
    public void testDirectionForRolling() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.size() > 0);
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_direction"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_direction");
        org.junit.Assert.assertTrue(upgrade.getType() == org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertEquals(4, groups.size());
        org.apache.ambari.server.stack.upgrade.Grouping group = groups.get(2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        org.apache.ambari.server.stack.upgrade.ClusterGrouping cluster_group = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group));
        org.junit.Assert.assertEquals("Run on All", group.title);
        cluster_group = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (groups.get(3)));
        java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> stages = cluster_group.executionStages;
        org.junit.Assert.assertEquals(3, stages.size());
        org.junit.Assert.assertNotNull(stages.get(0).intendedDirection);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, stages.get(0).intendedDirection);
        groups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        org.junit.Assert.assertEquals(3, groups.size());
        group = groups.get(1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        org.junit.Assert.assertEquals("Run on All", group.title);
        group = groups.get(2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        org.junit.Assert.assertEquals("Finalize Upgrade", group.title);
    }

    @org.junit.Test
    public void testSkippableFailures() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        java.util.Set<java.lang.String> keys = upgrades.keySet();
        for (java.lang.String key : keys) {
            org.junit.Assert.assertFalse(upgrades.get(key).isComponentFailureAutoSkipped());
            org.junit.Assert.assertFalse(upgrades.get(key).isServiceCheckFailureAutoSkipped());
        }
        upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgrades.get("upgrade_test_skip_failures");
        org.junit.Assert.assertTrue(upgradePack.isComponentFailureAutoSkipped());
        org.junit.Assert.assertTrue(upgradePack.isServiceCheckFailureAutoSkipped());
    }

    @org.junit.Test
    public void testNoAutoSkipFailure() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgrades.get("upgrade_test_skip_failures");
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups = upgradePack.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        for (org.apache.ambari.server.stack.upgrade.Grouping grouping : groups) {
            if (grouping.name.equals("SKIPPABLE_BUT_NOT_AUTO_SKIPPABLE")) {
                org.junit.Assert.assertFalse(grouping.supportsAutoSkipOnFailure);
            } else {
                org.junit.Assert.assertTrue(grouping.supportsAutoSkipOnFailure);
            }
        }
    }

    @org.junit.Test
    public void testDirectionForNonRolling() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.junit.Assert.assertTrue(upgrades.size() > 0);
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_nonrolling"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgrade = upgrades.get("upgrade_test_nonrolling");
        org.junit.Assert.assertTrue(upgrade.getType() == org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING);
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups = upgrade.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertEquals(10, groups.size());
        org.apache.ambari.server.stack.upgrade.Grouping group = null;
        org.apache.ambari.server.stack.upgrade.ClusterGrouping clusterGroup = null;
        org.apache.ambari.server.stack.upgrade.UpdateStackGrouping updateStackGroup = null;
        org.apache.ambari.server.stack.upgrade.StopGrouping stopGroup = null;
        org.apache.ambari.server.stack.upgrade.RestartGrouping restartGroup = null;
        group = groups.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        clusterGroup = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group));
        org.junit.Assert.assertEquals("Prepare Upgrade", clusterGroup.title);
        org.junit.Assert.assertNull(clusterGroup.parallelScheduler);
        group = groups.get(1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.StopGrouping.class, group.getClass());
        stopGroup = ((org.apache.ambari.server.stack.upgrade.StopGrouping) (group));
        org.junit.Assert.assertEquals("Stop Daemons for High-Level Services", stopGroup.title);
        org.junit.Assert.assertNotNull(stopGroup.parallelScheduler);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ParallelScheduler.DEFAULT_MAX_DEGREE_OF_PARALLELISM, stopGroup.parallelScheduler.maxDegreeOfParallelism);
        group = groups.get(2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        clusterGroup = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group));
        org.junit.Assert.assertEquals("Take Backups", clusterGroup.title);
        org.junit.Assert.assertNull(clusterGroup.parallelScheduler);
        group = groups.get(3);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.StopGrouping.class, group.getClass());
        stopGroup = ((org.apache.ambari.server.stack.upgrade.StopGrouping) (group));
        org.junit.Assert.assertEquals("Stop Daemons for Low-Level Services", stopGroup.title);
        org.junit.Assert.assertNotNull(stopGroup.parallelScheduler);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ParallelScheduler.DEFAULT_MAX_DEGREE_OF_PARALLELISM, stopGroup.parallelScheduler.maxDegreeOfParallelism);
        group = groups.get(4);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.UpdateStackGrouping.class, group.getClass());
        updateStackGroup = ((org.apache.ambari.server.stack.upgrade.UpdateStackGrouping) (group));
        org.junit.Assert.assertEquals("Update Desired Stack Id", updateStackGroup.title);
        org.junit.Assert.assertNull(updateStackGroup.parallelScheduler);
        group = groups.get(5);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        clusterGroup = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group));
        org.junit.Assert.assertEquals("Set Version On All Hosts", clusterGroup.title);
        org.junit.Assert.assertNull(clusterGroup.parallelScheduler);
        group = groups.get(6);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.RestartGrouping.class, group.getClass());
        restartGroup = ((org.apache.ambari.server.stack.upgrade.RestartGrouping) (group));
        org.junit.Assert.assertEquals("Zookeeper", restartGroup.title);
        org.junit.Assert.assertNull(restartGroup.parallelScheduler);
        group = groups.get(7);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.RestartGrouping.class, group.getClass());
        restartGroup = ((org.apache.ambari.server.stack.upgrade.RestartGrouping) (group));
        org.junit.Assert.assertEquals("HDFS", restartGroup.title);
        org.junit.Assert.assertNotNull(restartGroup.parallelScheduler);
        org.junit.Assert.assertEquals(2, restartGroup.parallelScheduler.maxDegreeOfParallelism);
        group = groups.get(8);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.RestartGrouping.class, group.getClass());
        restartGroup = ((org.apache.ambari.server.stack.upgrade.RestartGrouping) (group));
        org.junit.Assert.assertEquals("MR and YARN", restartGroup.title);
        org.junit.Assert.assertNotNull(restartGroup.parallelScheduler);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ParallelScheduler.DEFAULT_MAX_DEGREE_OF_PARALLELISM, restartGroup.parallelScheduler.maxDegreeOfParallelism);
        group = groups.get(9);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        clusterGroup = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group));
        org.junit.Assert.assertEquals("Finalize {{direction.text.proper}}", clusterGroup.title);
        org.junit.Assert.assertNull(clusterGroup.parallelScheduler);
    }

    @org.junit.Test
    public void testServiceLevelUpgradePackMerge() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.junit.Assert.assertTrue(upgrades.containsKey("upgrade_test_15388"));
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgrades.get("upgrade_test_15388");
        java.util.List<java.lang.String> checks = upgradePack.getPrerequisiteChecks();
        org.junit.Assert.assertEquals(11, checks.size());
        org.junit.Assert.assertTrue(checks.contains("org.apache.ambari.server.checks.FooCheck"));
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups = upgradePack.getGroups(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertEquals(8, groups.size());
        org.apache.ambari.server.stack.upgrade.Grouping group = groups.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class, group.getClass());
        org.apache.ambari.server.stack.upgrade.ClusterGrouping cluster_group = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group));
        org.junit.Assert.assertEquals("Pre {{direction.text.proper}}", cluster_group.title);
        java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> stages = cluster_group.executionStages;
        org.junit.Assert.assertEquals(5, stages.size());
        org.apache.ambari.server.stack.upgrade.ExecuteStage stage = stages.get(3);
        org.junit.Assert.assertEquals("Backup FOO", stage.title);
        group = groups.get(2);
        org.junit.Assert.assertEquals("Core Masters", group.title);
        java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> services = group.services;
        org.junit.Assert.assertEquals(3, services.size());
        org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService service = services.get(2);
        org.junit.Assert.assertEquals("HBASE", service.serviceName);
        group = groups.get(3);
        org.junit.Assert.assertEquals("Core Slaves", group.title);
        services = group.services;
        org.junit.Assert.assertEquals(3, services.size());
        service = services.get(1);
        org.junit.Assert.assertEquals("HBASE", service.serviceName);
        group = groups.get(4);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class, group.getClass());
        org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping scGroup = ((org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping) (group));
        java.util.Set<java.lang.String> priorityServices = scGroup.getPriorities();
        org.junit.Assert.assertEquals(4, priorityServices.size());
        java.util.Iterator<java.lang.String> serviceIterator = priorityServices.iterator();
        org.junit.Assert.assertEquals("ZOOKEEPER", serviceIterator.next());
        org.junit.Assert.assertEquals("HBASE", serviceIterator.next());
        group = groups.get(5);
        org.junit.Assert.assertEquals("Hive", group.title);
        group = groups.get(6);
        org.junit.Assert.assertEquals("Foo", group.title);
        services = group.services;
        org.junit.Assert.assertEquals(2, services.size());
        service = services.get(1);
        org.junit.Assert.assertEquals("FOO2", service.serviceName);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent>> tasks = upgradePack.getTasks();
        org.junit.Assert.assertTrue(tasks.containsKey("HBASE"));
        boolean found = false;
        for (org.apache.ambari.server.stack.upgrade.Grouping grouping : upgradePack.getAllGroups()) {
            if (grouping.name.equals("GANGLIA_UPGRADE")) {
                found = true;
                break;
            }
        }
        org.junit.Assert.assertFalse(found);
        upgradePack = upgrades.get("upgrade_test_conditions");
        org.junit.Assert.assertNotNull(upgradePack);
        for (org.apache.ambari.server.stack.upgrade.Grouping grouping : upgradePack.getAllGroups()) {
            if (grouping.name.equals("GANGLIA_UPGRADE")) {
                found = true;
                break;
            }
        }
        org.junit.Assert.assertTrue(found);
    }

    @org.junit.Test
    public void testPackWithHostGroup() {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.2.0");
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgrades.get("upgrade_test_host_ordered");
        org.junit.Assert.assertNotNull(upgradePack);
        org.junit.Assert.assertEquals(upgradePack.getType(), org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED);
        org.junit.Assert.assertEquals(3, upgradePack.getAllGroups().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.HostOrderGrouping.class, upgradePack.getAllGroups().get(0).getClass());
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Grouping.class, upgradePack.getAllGroups().get(1).getClass());
    }

    @org.junit.Test
    public void testDowngradeComponentTasks() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgrades = ambariMetaInfo.getUpgradePacks("HDP", "2.1.1");
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgrades.get("upgrade_component_tasks_test");
        org.junit.Assert.assertNotNull(upgradePack);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent>> components = upgradePack.getTasks();
        org.junit.Assert.assertTrue(components.containsKey("ZOOKEEPER"));
        org.junit.Assert.assertTrue(components.containsKey("HDFS"));
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent> zkMap = components.get("ZOOKEEPER");
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent> hdfsMap = components.get("HDFS");
        org.junit.Assert.assertTrue(zkMap.containsKey("ZOOKEEPER_SERVER"));
        org.junit.Assert.assertTrue(zkMap.containsKey("ZOOKEEPER_CLIENT"));
        org.junit.Assert.assertTrue(hdfsMap.containsKey("NAMENODE"));
        org.junit.Assert.assertTrue(hdfsMap.containsKey("DATANODE"));
        org.junit.Assert.assertTrue(hdfsMap.containsKey("HDFS_CLIENT"));
        org.junit.Assert.assertTrue(hdfsMap.containsKey("JOURNALNODE"));
        org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent zkServer = zkMap.get("ZOOKEEPER_SERVER");
        org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent zkClient = zkMap.get("ZOOKEEPER_CLIENT");
        org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent hdfsNN = hdfsMap.get("NAMENODE");
        org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent hdfsDN = hdfsMap.get("DATANODE");
        org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent hdfsClient = hdfsMap.get("HDFS_CLIENT");
        org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent hdfsJN = hdfsMap.get("JOURNALNODE");
        org.junit.Assert.assertNotNull(zkServer.preTasks);
        org.junit.Assert.assertNotNull(zkServer.preDowngradeTasks);
        org.junit.Assert.assertNull(zkServer.postTasks);
        org.junit.Assert.assertNull(zkServer.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, zkServer.preTasks.size());
        org.junit.Assert.assertEquals(1, zkServer.preDowngradeTasks.size());
        org.junit.Assert.assertNull(zkClient.preTasks);
        org.junit.Assert.assertNull(zkClient.preDowngradeTasks);
        org.junit.Assert.assertNotNull(zkClient.postTasks);
        org.junit.Assert.assertNotNull(zkClient.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, zkClient.postTasks.size());
        org.junit.Assert.assertEquals(1, zkClient.postDowngradeTasks.size());
        org.junit.Assert.assertNotNull(hdfsNN.preTasks);
        org.junit.Assert.assertNotNull(hdfsNN.preDowngradeTasks);
        org.junit.Assert.assertNull(hdfsNN.postTasks);
        org.junit.Assert.assertNull(hdfsNN.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, hdfsNN.preTasks.size());
        org.junit.Assert.assertEquals(0, hdfsNN.preDowngradeTasks.size());
        org.junit.Assert.assertNull(hdfsDN.preTasks);
        org.junit.Assert.assertNull(hdfsDN.preDowngradeTasks);
        org.junit.Assert.assertNotNull(hdfsDN.postTasks);
        org.junit.Assert.assertNotNull(hdfsDN.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, hdfsDN.postTasks.size());
        org.junit.Assert.assertEquals(0, hdfsDN.postDowngradeTasks.size());
        org.junit.Assert.assertNull(hdfsClient.preTasks);
        org.junit.Assert.assertNotNull(hdfsClient.preDowngradeTasks);
        org.junit.Assert.assertNull(hdfsClient.postTasks);
        org.junit.Assert.assertNotNull(hdfsClient.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, hdfsClient.preDowngradeTasks.size());
        org.junit.Assert.assertEquals(1, hdfsClient.postDowngradeTasks.size());
        org.junit.Assert.assertNotNull(hdfsJN.preTasks);
        org.junit.Assert.assertNotNull(hdfsJN.preDowngradeTasks);
        org.junit.Assert.assertNotNull(hdfsJN.postTasks);
        org.junit.Assert.assertNotNull(hdfsJN.postDowngradeTasks);
        org.junit.Assert.assertEquals(1, hdfsJN.preTasks.size());
        org.junit.Assert.assertEquals(2, hdfsJN.preDowngradeTasks.size());
        org.junit.Assert.assertEquals(1, hdfsJN.postTasks.size());
        org.junit.Assert.assertEquals(2, hdfsJN.postDowngradeTasks.size());
        java.util.Set<java.lang.String> allIds = com.google.common.collect.Sets.newHashSet("some_id", "some_id1", "some_id2", "some_id3", "some_id4", "some_id5");
        @java.lang.SuppressWarnings("unchecked")
        java.util.Set<java.util.List<org.apache.ambari.server.stack.upgrade.Task>> allTasks = com.google.common.collect.Sets.newHashSet(hdfsJN.preTasks, hdfsJN.preDowngradeTasks, hdfsJN.postTasks, hdfsJN.postDowngradeTasks);
        for (java.util.List<org.apache.ambari.server.stack.upgrade.Task> list : allTasks) {
            for (org.apache.ambari.server.stack.upgrade.Task t : list) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.ConfigureTask.class, t.getClass());
                org.apache.ambari.server.stack.upgrade.ConfigureTask ct = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (t));
                org.junit.Assert.assertTrue(allIds.contains(ct.id));
                allIds.remove(ct.id);
            }
        }
        org.junit.Assert.assertTrue(allIds.isEmpty());
    }

    private int indexOf(java.util.Map<java.lang.String, ?> map, java.lang.String keyToFind) {
        int result = -1;
        int i = 0;
        for (java.util.Map.Entry<java.lang.String, ?> entry : map.entrySet()) {
            if (entry.getKey().equals(keyToFind)) {
                return i;
            }
            i++;
        }
        return result;
    }
}