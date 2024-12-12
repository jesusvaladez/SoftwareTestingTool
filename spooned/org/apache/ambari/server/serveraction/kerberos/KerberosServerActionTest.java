package org.apache.ambari.server.serveraction.kerberos;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class KerberosServerActionTest extends org.easymock.EasyMockSupport {
    private static final java.util.Map<java.lang.String, java.lang.String> KERBEROS_ENV_PROPERTIES = java.util.Collections.singletonMap("admin_server_host", "kdc.example.com");

    java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();

    java.io.File temporaryDirectory;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.serveraction.kerberos.KerberosServerAction action;

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(org.apache.ambari.server.serveraction.kerberos.KerberosServerActionTest.KERBEROS_ENV_PROPERTIES).anyTimes();
        cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getDesiredConfigByType("kerberos-env")).andReturn(kerberosEnvConfig).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster(EasyMock.anyString())).andReturn(cluster).anyTimes();
        org.apache.ambari.server.agent.ExecutionCommand mockExecutionCommand = createMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand mockHostRoleCommand = createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        kerberosKeytabController = createMock(org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.class);
        EasyMock.expect(kerberosKeytabController.adjustServiceComponentFilter(EasyMock.anyObject(), EasyMock.eq(true), EasyMock.anyObject())).andReturn(null).anyTimes();
        EasyMock.expect(kerberosKeytabController.getFilteredKeytabs(((java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>) (null)), null, null)).andReturn(com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab(null, null, null, null, null, com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal(1L, "host", "principal", true, "/tmp", "SERVICE", "COMPONENT", "/tmp")), true, true))).anyTimes();
        action = new org.apache.ambari.server.serveraction.kerberos.KerberosServerAction() {
            @java.lang.Override
            protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
                junit.framework.Assert.assertNotNull(requestSharedDataContext);
                if (requestSharedDataContext.get("FAIL") != null) {
                    return createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "ERROR", "ERROR");
                } else {
                    requestSharedDataContext.put(resolvedPrincipal.getPrincipal(), resolvedPrincipal.getPrincipal());
                    return null;
                }
            }

            @java.lang.Override
            public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
                return processIdentities(requestSharedDataContext);
            }
        };
        action.setExecutionCommand(mockExecutionCommand);
        action.setHostRoleCommand(mockHostRoleCommand);
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createMock(org.apache.ambari.server.controller.KerberosHelper.class));
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.class).toInstance(action);
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class).toInstance(createMock(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class));
                bind(org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.class).toInstance(kerberosKeytabController);
            }
        });
        temporaryDirectory = java.io.File.createTempFile("ambari_ut_", ".d");
        junit.framework.Assert.assertTrue(temporaryDirectory.delete());
        junit.framework.Assert.assertTrue(temporaryDirectory.mkdirs());
        org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter writer = new org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter(new java.io.File(temporaryDirectory, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.DATA_FILE_NAME));
        for (int i = 0; i < 10; i++) {
            writer.writeRecord("hostName", "serviceName" + i, "serviceComponentName" + i, "principal|_HOST|_REALM" + i, "principal_type", "keytabFilePath" + i, "keytabFileOwnerName" + i, "keytabFileOwnerAccess" + i, "keytabFileGroupName" + i, "keytabFileGroupAccess" + i, "false");
        }
        writer.close();
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, temporaryDirectory.getAbsolutePath());
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, "REALM.COM");
        commandParams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC.toString());
        EasyMock.expect(mockExecutionCommand.getCommandParams()).andReturn(commandParams).anyTimes();
        EasyMock.expect(mockExecutionCommand.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.expect(mockExecutionCommand.getClusterId()).andReturn("1").anyTimes();
        EasyMock.expect(mockExecutionCommand.getConfigurations()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(mockExecutionCommand.getRoleCommand()).andReturn(null).anyTimes();
        EasyMock.expect(mockExecutionCommand.getRole()).andReturn(null).anyTimes();
        EasyMock.expect(mockExecutionCommand.getServiceName()).andReturn(null).anyTimes();
        EasyMock.expect(mockExecutionCommand.getTaskId()).andReturn(1L).anyTimes();
        EasyMock.expect(mockHostRoleCommand.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(mockHostRoleCommand.getStageId()).andReturn(1L).anyTimes();
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        if (temporaryDirectory != null) {
            new java.io.File(temporaryDirectory, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.DATA_FILE_NAME).delete();
            temporaryDirectory.delete();
        }
    }

    @org.junit.Test
    public void testGetCommandParameterValueStatic() throws java.lang.Exception {
        junit.framework.Assert.assertNull(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParams, "nonexistingvalue"));
        junit.framework.Assert.assertEquals("REALM.COM", org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParams, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM));
    }

    @org.junit.Test
    public void testGetDefaultRealmStatic() throws java.lang.Exception {
        junit.framework.Assert.assertEquals("REALM.COM", org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDefaultRealm(commandParams));
    }

    @org.junit.Test
    public void testGetKDCTypeStatic() throws java.lang.Exception {
        junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getKDCType(commandParams));
    }

    @org.junit.Test
    public void testGetDataDirectoryPathStatic() throws java.lang.Exception {
        junit.framework.Assert.assertEquals(temporaryDirectory.getAbsolutePath(), org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDataDirectoryPath(commandParams));
    }

    @org.junit.Test
    public void testSetPrincipalPasswordMapStatic() throws java.lang.Exception {
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> sharedMap = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.Map<java.lang.String, java.lang.String> dataMap = new java.util.HashMap<>();
        org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.setPrincipalPasswordMap(sharedMap, dataMap);
        junit.framework.Assert.assertSame(dataMap, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getPrincipalPasswordMap(sharedMap));
    }

    @org.junit.Test
    public void testGetPrincipalPasswordMapStatic() throws java.lang.Exception {
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> sharedMap = new java.util.concurrent.ConcurrentHashMap<>();
        junit.framework.Assert.assertNotNull(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getPrincipalPasswordMap(sharedMap));
    }

    @org.junit.Test
    public void testGetDataDirectoryPath() throws java.lang.Exception {
        replayAll();
        junit.framework.Assert.assertEquals(temporaryDirectory.getAbsolutePath(), action.getDataDirectoryPath());
        verifyAll();
    }

    @org.junit.Test
    public void testProcessIdentitiesSuccess() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        EasyMock.expect(kerberosHelper.getKDCAdministratorCredentials(EasyMock.anyObject(java.lang.String.class))).andReturn(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password")).anyTimes();
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler kerberosOperationHandler = createMock(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class);
        kerberosOperationHandler.open(EasyMock.anyObject(org.apache.ambari.server.security.credential.PrincipalKeyCredential.class), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class));
        EasyMock.expectLastCall().atLeastOnce();
        kerberosOperationHandler.close();
        EasyMock.expectLastCall().atLeastOnce();
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory factory = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class);
        EasyMock.expect(factory.getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC)).andReturn(kerberosOperationHandler).once();
        replayAll();
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> sharedMap = new java.util.concurrent.ConcurrentHashMap<>();
        org.apache.ambari.server.agent.CommandReport report = action.processIdentities(sharedMap);
        junit.framework.Assert.assertNotNull(report);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString(), report.getStatus());
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : sharedMap.entrySet()) {
            junit.framework.Assert.assertEquals(entry.getValue(), entry.getKey());
        }
        verifyAll();
    }

    @org.junit.Test
    public void testProcessIdentitiesFail() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        EasyMock.expect(kerberosHelper.getKDCAdministratorCredentials(EasyMock.anyObject(java.lang.String.class))).andReturn(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password")).anyTimes();
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler kerberosOperationHandler = createMock(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class);
        kerberosOperationHandler.open(EasyMock.anyObject(org.apache.ambari.server.security.credential.PrincipalKeyCredential.class), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class));
        EasyMock.expectLastCall().atLeastOnce();
        kerberosOperationHandler.close();
        EasyMock.expectLastCall().atLeastOnce();
        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory factory = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class);
        EasyMock.expect(factory.getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC)).andReturn(kerberosOperationHandler).anyTimes();
        replayAll();
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> sharedMap = new java.util.concurrent.ConcurrentHashMap<>();
        sharedMap.put("FAIL", "true");
        org.apache.ambari.server.agent.CommandReport report = action.processIdentities(sharedMap);
        junit.framework.Assert.assertNotNull(report);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString(), report.getStatus());
        verifyAll();
    }

    @org.junit.Test
    public void testGetConfigurationProperties() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config emptyConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(emptyConfig.getProperties()).andReturn(java.util.Collections.emptyMap()).once();
        org.apache.ambari.server.state.Config missingPropertiesConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(missingPropertiesConfig.getProperties()).andReturn(null).once();
        EasyMock.expect(cluster.getDesiredConfigByType("invalid-type")).andReturn(null).once();
        EasyMock.expect(cluster.getDesiredConfigByType("missing-properties-type")).andReturn(missingPropertiesConfig).once();
        EasyMock.expect(cluster.getDesiredConfigByType("empty-type")).andReturn(emptyConfig).once();
        replayAll();
        junit.framework.Assert.assertNull(action.getConfigurationProperties(null));
        junit.framework.Assert.assertNull(action.getConfigurationProperties("invalid-type"));
        junit.framework.Assert.assertNull(action.getConfigurationProperties("missing-properties-type"));
        junit.framework.Assert.assertEquals(java.util.Collections.emptyMap(), action.getConfigurationProperties("empty-type"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.serveraction.kerberos.KerberosServerActionTest.KERBEROS_ENV_PROPERTIES, action.getConfigurationProperties("kerberos-env"));
        verifyAll();
    }
}