package org.apache.ambari.server.stageplanner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class TestStagePlanner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stageplanner.TestStagePlanner.class);

    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.RoleCommandOrderProvider roleCommandOrderProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.utils.StageUtils stageUtils;

    @com.google.inject.Inject
    private org.apache.ambari.server.stageplanner.RoleGraphFactory roleGraphFactory;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testSingleStagePlan() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        java.lang.String hostname = "dummy";
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, hostname, "", "");
        rg.build(stage);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        for (org.apache.ambari.server.actionmanager.Stage s : outStages) {
            org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(s.toString());
        }
        org.junit.Assert.assertEquals(1, outStages.size());
        org.junit.Assert.assertEquals(stage.getExecutionCommands(hostname), outStages.get(0).getExecutionCommands(hostname));
    }

    @org.junit.Test
    public void testSCCInGraphDetectedShort() {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        org.apache.ambari.server.state.Service hbaseService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hbaseService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        org.apache.ambari.server.state.Service zkService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(zkService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HBASE", hbaseService).put("ZOOKEEPER", zkService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "host1", "", "");
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_MASTER", "host2", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("ZOOKEEPER_SERVER", "host3", now), "cluster1", "ZOOKEEPER", false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info("Build and ready to detect circular dependencies - short chain");
        rg.build(stage);
        boolean exceptionThrown = false;
        try {
            java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        } catch (org.apache.ambari.server.AmbariException e) {
            exceptionThrown = true;
        }
        junit.framework.Assert.assertTrue(exceptionThrown);
    }

    @org.junit.Test
    public void testSCCInGraphDetectedLong() {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        org.apache.ambari.server.state.Service hbaseService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hbaseService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        org.apache.ambari.server.state.Service zkService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(zkService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        org.apache.ambari.server.state.Service yarnService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(yarnService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HBASE", hbaseService).put("ZOOKEEPER", zkService).put("YARN", yarnService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "host1", "", "");
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_MASTER", "host2", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("ZOOKEEPER_SERVER", "host3", now), "cluster1", "ZOOKEEPER", false, false);
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.RESOURCEMANAGER, org.apache.ambari.server.RoleCommand.STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("RESOURCEMANAGER", "host4", now), "cluster1", "YARN", false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info("Build and ready to detect circular dependencies - long chain");
        rg.build(stage);
        boolean exceptionThrown = false;
        try {
            java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        } catch (org.apache.ambari.server.AmbariException e) {
            exceptionThrown = true;
        }
        junit.framework.Assert.assertTrue(exceptionThrown);
    }

    @org.junit.Test
    public void testSCCInGraphDetectedLongTwo() {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        org.apache.ambari.server.state.Service hbaseService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hbaseService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        org.apache.ambari.server.state.Service zkService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(zkService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6.1"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HBASE", hbaseService).put("ZOOKEEPER", zkService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "host1", "", "");
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_MASTER", "host2", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("ZOOKEEPER_SERVER", "host3", now), "cluster1", "ZOOKEEPER", false, false);
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_REGIONSERVER", "host4", now), "cluster1", "HBASE", false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info("Build and ready to detect circular dependencies - long chain");
        rg.build(stage);
        boolean exceptionThrown = false;
        try {
            java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        } catch (org.apache.ambari.server.AmbariException e) {
            exceptionThrown = true;
        }
        junit.framework.Assert.assertTrue(exceptionThrown);
    }

    @org.junit.Test
    public void testNoSCCInGraphDetected() {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service hbaseService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hbaseService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service zkService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(zkService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HBASE", hbaseService).put("ZOOKEEPER", zkService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "host1", "", "");
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_MASTER", "host2", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("ZOOKEEPER_SERVER", "host3", now), "cluster1", "ZOOKEEPER", false, false);
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_REGIONSERVER", "host4", now), "cluster1", "HBASE", false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info("Build and ready to detect circular dependencies");
        rg.build(stage);
        boolean exceptionThrown = false;
        try {
            java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        } catch (org.apache.ambari.server.AmbariException e) {
            exceptionThrown = true;
        }
        junit.framework.Assert.assertFalse(exceptionThrown);
    }

    @org.junit.Test
    public void testMultiStagePlan() throws java.lang.Throwable {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service hbaseService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hbaseService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service zkService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(zkService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HBASE", hbaseService).put("ZOOKEEPER", zkService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "host1", "", "");
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_MASTER", "host2", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("ZOOKEEPER_SERVER", "host3", now), "cluster1", "ZOOKEEPER", false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(stage.toString());
        rg.build(stage);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(rg.stringifyGraph());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        for (org.apache.ambari.server.actionmanager.Stage s : outStages) {
            org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(s.toString());
        }
        org.junit.Assert.assertEquals(3, outStages.size());
    }

    @org.junit.Test
    public void testRestartStagePlan() throws java.lang.Throwable {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service hiveService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hiveService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HIVE", hiveService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(1, "/tmp", "cluster1", 1L, "execution command wrapper test", "commandParamsStage", "hostParamsStage");
        stage.setStageId(1);
        stage.addServerActionCommand("RESTART", null, org.apache.ambari.server.Role.HIVE_METASTORE, org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "cluster1", new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent("host2", java.lang.System.currentTimeMillis()), null, "command detail", null, null, false, false);
        stage.addServerActionCommand("RESTART", null, org.apache.ambari.server.Role.MYSQL_SERVER, org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "cluster1", new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent("host2", java.lang.System.currentTimeMillis()), null, "command detail", null, null, false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(stage.toString());
        rg.build(stage);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(rg.stringifyGraph());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        for (org.apache.ambari.server.actionmanager.Stage s : outStages) {
            org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(s.toString());
        }
        org.junit.Assert.assertEquals(2, outStages.size());
    }

    @org.junit.Test
    public void testManyStages() throws java.lang.Throwable {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service hdfsService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hdfsService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service hbaseService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hbaseService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service zkService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(zkService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service mrService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(mrService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service oozieService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(oozieService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service webhcatService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(webhcatService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service gangliaService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(gangliaService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", hdfsService).put("HBASE", hbaseService).put("ZOOKEEPER", zkService).put("MAPREDUCE", mrService).put("OOZIE", oozieService).put("WEBHCAT", webhcatService).put("GANGLIA", gangliaService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "host1", "", "");
        stage.addHostRoleExecutionCommand("host11", org.apache.ambari.server.Role.SECONDARY_NAMENODE, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("SECONDARY_NAMENODE", "host11", now), "cluster1", "HDFS", false, false);
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_MASTER", "host2", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("ZOOKEEPER_SERVER", "host3", now), "cluster1", "ZOOKEEPER", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("DATANODE", "host4", now), "cluster1", "HDFS", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_REGIONSERVER", "host4", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.TASKTRACKER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("TASKTRACKER", "host4", now), "cluster1", "MAPREDUCE", false, false);
        stage.addHostRoleExecutionCommand("host5", org.apache.ambari.server.Role.JOBTRACKER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("JOBTRACKER", "host5", now), "cluster1", "MAPREDUCE", false, false);
        stage.addHostRoleExecutionCommand("host6", org.apache.ambari.server.Role.OOZIE_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("OOZIE_SERVER", "host6", now), "cluster1", "OOZIE", false, false);
        stage.addHostRoleExecutionCommand("host7", org.apache.ambari.server.Role.WEBHCAT_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("WEBHCAT_SERVER", "host7", now), "cluster1", "WEBHCAT", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("GANGLIA_MONITOR", "host4", now), "cluster1", "GANGLIA", false, false);
        stage.addHostRoleExecutionCommand("host9", org.apache.ambari.server.Role.GANGLIA_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("GANGLIA_SERVER", "host9", now), "cluster1", "GANGLIA", false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(stage.toString());
        rg.build(stage);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(rg.stringifyGraph());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        for (org.apache.ambari.server.actionmanager.Stage s : outStages) {
            org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(s.toString());
        }
        org.junit.Assert.assertEquals(4, outStages.size());
    }

    @org.junit.Test
    public void testDependencyOrderedStageCreate() throws java.lang.Throwable {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph rg = roleGraphFactory.createNew(rco);
        rg.setCommandExecutionType(org.apache.ambari.server.actionmanager.CommandExecutionType.DEPENDENCY_ORDERED);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "host1", "", "");
        stage.addHostRoleExecutionCommand("host11", org.apache.ambari.server.Role.SECONDARY_NAMENODE, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("SECONDARY_NAMENODE", "host11", now), "cluster1", "HDFS", false, false);
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_MASTER", "host2", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("ZOOKEEPER_SERVER", "host3", now), "cluster1", "ZOOKEEPER", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("DATANODE", "host4", now), "cluster1", "HDFS", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("HBASE_REGIONSERVER", "host4", now), "cluster1", "HBASE", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.TASKTRACKER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("TASKTRACKER", "host4", now), "cluster1", "MAPREDUCE", false, false);
        stage.addHostRoleExecutionCommand("host5", org.apache.ambari.server.Role.JOBTRACKER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("JOBTRACKER", "host5", now), "cluster1", "MAPREDUCE", false, false);
        stage.addHostRoleExecutionCommand("host6", org.apache.ambari.server.Role.OOZIE_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("OOZIE_SERVER", "host6", now), "cluster1", "OOZIE", false, false);
        stage.addHostRoleExecutionCommand("host7", org.apache.ambari.server.Role.WEBHCAT_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("WEBHCAT_SERVER", "host7", now), "cluster1", "WEBHCAT", false, false);
        stage.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("GANGLIA_MONITOR", "host4", now), "cluster1", "GANGLIA", false, false);
        stage.addHostRoleExecutionCommand("host9", org.apache.ambari.server.Role.GANGLIA_SERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent("GANGLIA_SERVER", "host9", now), "cluster1", "GANGLIA", false, false);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(stage.toString());
        rg.build(stage);
        org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(rg.stringifyGraph());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> outStages = rg.getStages();
        for (org.apache.ambari.server.actionmanager.Stage s : outStages) {
            org.apache.ambari.server.stageplanner.TestStagePlanner.log.info(s.toString());
        }
        org.junit.Assert.assertEquals(1, outStages.size());
    }
}