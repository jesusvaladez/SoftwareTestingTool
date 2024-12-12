package org.apache.ambari.server.serveraction.kerberos;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
import static org.mockito.Matchers.anyBoolean;
public class FinalizeKerberosServerActionTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder folder = new org.junit.rules.TemporaryFolder();

    private final org.apache.ambari.server.agent.stomp.TopologyHolder topologyHolder = createNiceMock(org.apache.ambari.server.agent.stomp.TopologyHolder.class);

    @org.junit.Test
    @org.junit.Ignore("Update accordingly to changes")
    public void executeMITKDCOption() throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String clusterId = "1";
        com.google.inject.Injector injector = setup(clusterName);
        java.io.File dataDirectory = createDataDirectory();
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC.name());
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createMockExecutionCommand(clusterId, clusterName, commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = createMockHostRoleCommand();
        org.apache.ambari.server.security.credential.PrincipalKeyCredential principleKeyCredential = createMock(org.apache.ambari.server.security.credential.PrincipalKeyCredential.class);
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        EasyMock.expect(kerberosHelper.getKDCAdministratorCredentials(clusterName)).andReturn(principleKeyCredential).anyTimes();
        replayAll();
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext = new java.util.concurrent.ConcurrentHashMap<>();
        org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction action = new org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction(topologyHolder);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        junit.framework.Assert.assertTrue(dataDirectory.exists());
        org.apache.ambari.server.agent.CommandReport commandReport = action.execute(requestSharedDataContext);
        assertSuccess(commandReport);
        junit.framework.Assert.assertTrue(!dataDirectory.exists());
        verifyAll();
    }

    @org.junit.Test
    public void executeManualOption() throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String clusterId = "1";
        com.google.inject.Injector injector = setup(clusterName);
        java.io.File dataDirectory = createDataDirectory();
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createMockExecutionCommand(clusterId, clusterName, commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = createMockHostRoleCommand();
        replayAll();
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext = new java.util.concurrent.ConcurrentHashMap<>();
        org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction action = new org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction(topologyHolder);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        junit.framework.Assert.assertTrue(dataDirectory.exists());
        org.apache.ambari.server.agent.CommandReport commandReport = action.execute(requestSharedDataContext);
        assertSuccess(commandReport);
        junit.framework.Assert.assertTrue(!dataDirectory.exists());
        verifyAll();
    }

    private java.io.File createDataDirectory() throws java.io.IOException {
        java.io.File directory = folder.newFolder();
        java.io.File dataDirectory = new java.io.File(directory, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY_PREFIX + "_test");
        junit.framework.Assert.assertTrue(dataDirectory.mkdir());
        return dataDirectory;
    }

    private void assertSuccess(org.apache.ambari.server.agent.CommandReport commandReport) {
        junit.framework.Assert.assertEquals(0, commandReport.getExitCode());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), commandReport.getStatus());
        junit.framework.Assert.assertEquals("{}", commandReport.getStructuredOut());
    }

    private org.apache.ambari.server.agent.ExecutionCommand createMockExecutionCommand(java.lang.String clusterId, java.lang.String clusterName, java.util.Map<java.lang.String, java.lang.String> commandParams) {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        EasyMock.expect(executionCommand.getClusterId()).andReturn(clusterId).anyTimes();
        EasyMock.expect(executionCommand.getClusterName()).andReturn(clusterName).anyTimes();
        EasyMock.expect(executionCommand.getCommandParams()).andReturn(commandParams).anyTimes();
        EasyMock.expect(executionCommand.getRoleCommand()).andReturn(org.apache.ambari.server.RoleCommand.EXECUTE).anyTimes();
        EasyMock.expect(executionCommand.getRole()).andReturn(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.name()).anyTimes();
        EasyMock.expect(executionCommand.getServiceName()).andReturn(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name()).anyTimes();
        EasyMock.expect(executionCommand.getTaskId()).andReturn(3L).anyTimes();
        return executionCommand;
    }

    private org.apache.ambari.server.actionmanager.HostRoleCommand createMockHostRoleCommand() {
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        EasyMock.expect(hostRoleCommand.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(hostRoleCommand.getStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(hostRoleCommand.getTaskId()).andReturn(3L).anyTimes();
        return hostRoleCommand;
    }

    private com.google.inject.Injector setup(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHostMap = new java.util.HashMap<>();
        clusterHostMap.put("host1", createMock(org.apache.ambari.server.state.Host.class));
        final org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(serviceComponentHost.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(serviceComponentHost.getServiceComponentName()).andReturn("COMPONENT1A").anyTimes();
        EasyMock.expect(serviceComponentHost.getHostName()).andReturn("host1").anyTimes();
        final java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = new java.util.ArrayList<>();
        serviceComponentHosts.add(serviceComponentHost);
        final org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterName()).andReturn(clusterName).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("host1")).andReturn(serviceComponentHosts).anyTimes();
        final org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getHostsForCluster(clusterName)).andReturn(clusterHostMap).anyTimes();
        EasyMock.expect(clusters.getCluster(clusterName)).andReturn(cluster).anyTimes();
        final org.apache.ambari.server.events.TopologyUpdateEvent event = createNiceMock(org.apache.ambari.server.events.TopologyUpdateEvent.class);
        EasyMock.expect(topologyHolder.getCurrentData()).andReturn(event).once();
        EasyMock.expect(topologyHolder.updateData(event)).andReturn(Matchers.anyBoolean()).once();
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createMock(org.apache.ambari.server.controller.KerberosHelper.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(javax.persistence.EntityManager.class).toProvider(org.easymock.EasyMock.createNiceMock(com.google.inject.Provider.class));
            }
        });
    }
}