package org.apache.ambari.server.actionmanager;
public class TestStage {
    private static final java.lang.String CLUSTER_HOST_INFO = "cluster_host_info";

    com.google.inject.Injector injector;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.utils.StageUtils stageUtils;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
    }

    @org.junit.Test
    public void testTaskTimeout() {
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, "h1", "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        s.addHostRoleExecutionCommand("h1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.INSTALL, null, "c1", "HDFS", false, false);
        s.addHostRoleExecutionCommand("h1", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.INSTALL, null, "c1", "HBASE", false, false);
        for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper : s.getExecutionCommands("h1")) {
            java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, "600");
            wrapper.getExecutionCommand().setCommandParams(commandParams);
        }
        org.junit.Assert.assertEquals(3 * 600000, s.getStageTimeout());
    }

    @org.junit.Test
    public void testGetRequestContext() {
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(1, "/logDir", "c1", 1L, "My Context", "", "");
        org.junit.Assert.assertEquals("My Context", stage.getRequestContext());
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }
}