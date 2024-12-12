package org.apache.ambari.server.serveraction.kerberos;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class UpdateKerberosConfigsServerActionTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder testFolder = new org.junit.rules.TemporaryFolder();

    java.lang.String dataDir;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction action;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        final org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster).once();
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder(UpdateKerberosConfigsServerActionTest.this).addClustersBinding().addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.state.Clusters.class).to(org.apache.ambari.server.state.cluster.ClustersImpl.class);
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.security.SecurityHelper.class).toInstance(org.apache.ambari.server.security.SecurityHelperImpl.getInstance());
            }
        });
        final org.apache.ambari.server.controller.AmbariManagementController controller = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(controller.getClusters()).andReturn(clusters).once();
        dataDir = testFolder.getRoot().getAbsolutePath();
        setupConfigDat();
        action = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction.class);
    }

    private void setupConfigDat() throws java.lang.Exception {
        java.io.File configFile = new java.io.File(dataDir, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.DATA_FILE_NAME);
        org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory factory = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class);
        org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter writer = factory.createKerberosConfigDataFileWriter(configFile);
        writer.addRecord("hdfs-site", "hadoop.security.authentication", "kerberos", org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.OPERATION_TYPE_SET);
        writer.addRecord("hdfs-site", "remove.me", null, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.OPERATION_TYPE_REMOVE);
        writer.close();
    }

    @org.junit.Test
    public void testUpdateConfig() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDir);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        configHelper.updateBulkConfigType(EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject());
        EasyMock.expectLastCall().atLeastOnce();
        replayAll();
        action.setExecutionCommand(executionCommand);
        action.execute(null);
        verifyAll();
    }

    @org.junit.Test
    public void testUpdateConfigMissingDataDirectory() throws java.lang.Exception {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        executionCommand.setCommandParams(commandParams);
        replayAll();
        action.setExecutionCommand(executionCommand);
        action.execute(null);
        verifyAll();
    }

    @org.junit.Test
    public void testUpdateConfigEmptyDataDirectory() throws java.lang.Exception {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, testFolder.newFolder().getAbsolutePath());
        executionCommand.setCommandParams(commandParams);
        replayAll();
        action.setExecutionCommand(executionCommand);
        action.execute(null);
        verifyAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testUpdateConfigForceSecurityEnabled() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDir);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        org.easymock.Capture<java.lang.Iterable<java.lang.String>> configTypes = org.easymock.Capture.newInstance(CaptureType.ALL);
        org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configUpdates = org.easymock.Capture.newInstance(CaptureType.ALL);
        configHelper.updateBulkConfigType(EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.capture(configTypes), EasyMock.capture(configUpdates), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject());
        EasyMock.expectLastCall().atLeastOnce();
        replayAll();
        action.setExecutionCommand(executionCommand);
        action.execute(null);
        org.junit.Assert.assertTrue(java.util.stream.StreamSupport.stream(configTypes.getValues().get(0).spliterator(), false).anyMatch(config -> config.equals("cluster-env")));
        org.junit.Assert.assertTrue(configUpdates.getValues().stream().flatMap(x -> x.values().stream()).flatMap(x -> x.entrySet().stream()).anyMatch(property -> property.getKey().equals("security_enabled")));
        verifyAll();
    }
}