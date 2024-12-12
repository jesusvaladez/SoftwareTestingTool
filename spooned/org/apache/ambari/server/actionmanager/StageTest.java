package org.apache.ambari.server.actionmanager;
public class StageTest {
    private static final java.lang.String SERVER_HOST_NAME = org.apache.ambari.server.utils.StageUtils.getHostName();

    private static final java.lang.String CLUSTER_HOST_INFO = ((("{all_hosts=[" + org.apache.ambari.server.actionmanager.StageTest.SERVER_HOST_NAME) + "], slave_hosts=[") + org.apache.ambari.server.actionmanager.StageTest.SERVER_HOST_NAME) + "]}";

    com.google.inject.Injector injector;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
    }

    @org.junit.Test
    public void testAddServerActionCommand_userName() throws java.lang.Exception {
        final org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(1, "/tmp", "cluster1", 978, "context", "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        stage.addServerActionCommand(org.apache.ambari.server.serveraction.upgrades.ConfigureAction.class.getName(), "user1", org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, "cluster1", new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(org.apache.ambari.server.utils.StageUtils.getHostName(), java.lang.System.currentTimeMillis()), java.util.Collections.emptyMap(), null, null, 1200, false, false);
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> executionCommands = stage.getExecutionCommands(null);
        org.junit.Assert.assertEquals(1, executionCommands.size());
        java.lang.String actionUserName = executionCommands.get(0).getExecutionCommand().getRoleParams().get(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME);
        org.junit.Assert.assertEquals("user1", actionUserName);
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }
}