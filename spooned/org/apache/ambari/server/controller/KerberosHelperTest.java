package org.apache.ambari.server.controller;
import javax.persistence.EntityManager;
import org.apache.directory.server.kerberos.shared.keytab.Keytab;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_COMPONENTS;
import static org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_HOSTS;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@java.lang.SuppressWarnings("unchecked")
public class KerberosHelperTest extends org.easymock.EasyMockSupport {
    private static com.google.inject.Injector injector;

    private final org.apache.ambari.server.controller.spi.ClusterController clusterController = createStrictMock(org.apache.ambari.server.controller.spi.ClusterController.class);

    private final org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class);

    private final org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory kerberosConfigDataFileWriterFactory = createStrictMock(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class);

    private final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    private final org.apache.ambari.server.topology.TopologyManager topologyManager = createMock(org.apache.ambari.server.topology.TopologyManager.class);

    private final org.apache.ambari.server.configuration.Configuration configuration = createMock(org.apache.ambari.server.configuration.Configuration.class);

    private final org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper customCommandExecutionHelperMock = createNiceMock(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);

    @org.junit.Rule
    public org.junit.rules.TemporaryFolder temporaryFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        EasyMock.reset(clusterController);
        EasyMock.reset(metaInfo);
        final org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory kerberosOperationHandlerFactory = createMock(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class);
        EasyMock.expect(kerberosOperationHandlerFactory.getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType.NONE)).andReturn(null).anyTimes();
        EasyMock.expect(kerberosOperationHandlerFactory.getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC)).andReturn(new org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler() {
            @java.lang.Override
            public void open(org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredentials, java.lang.String defaultRealm, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                setAdministratorCredential(administratorCredentials);
                setDefaultRealm(defaultRealm);
                setOpen(true);
            }

            @java.lang.Override
            public void close() throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
            }

            @java.lang.Override
            public boolean principalExists(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return "principal".equals(principal);
            }

            @java.lang.Override
            public java.lang.Integer createPrincipal(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return null;
            }

            @java.lang.Override
            public java.lang.Integer setPrincipalPassword(java.lang.String principal, java.lang.String password, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return null;
            }

            @java.lang.Override
            public boolean removePrincipal(java.lang.String principal, boolean service) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return false;
            }

            @java.lang.Override
            public boolean createKeytabFile(org.apache.directory.server.kerberos.shared.keytab.Keytab keytab, java.io.File destinationKeytabFile) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
                return true;
            }
        }).anyTimes();
        java.lang.reflect.Method methodGetConfiguredTemporaryDirectory = org.apache.ambari.server.controller.KerberosHelperImpl.class.getDeclaredMethod("getConfiguredTemporaryDirectory");
        final org.apache.ambari.server.controller.KerberosHelperImpl kerberosHelper = createMockBuilder(org.apache.ambari.server.controller.KerberosHelperImpl.class).addMockedMethod(methodGetConfiguredTemporaryDirectory).createMock();
        EasyMock.expect(kerberosHelper.getConfiguredTemporaryDirectory()).andReturn(temporaryFolder.getRoot()).anyTimes();
        org.apache.ambari.server.controller.KerberosHelperTest.injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addActionDBAccessorConfigsBindings().addFactoriesInstallBinding().addPasswordEncryptorBindings().build().configure(binder());
                bind(com.google.inject.persist.jpa.AmbariJpaPersistService.class).toInstance(createNiceMock(com.google.inject.persist.jpa.AmbariJpaPersistService.class));
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).to(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class);
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                bind(org.apache.ambari.server.topology.PersistedState.class).to(org.apache.ambari.server.topology.PersistedStateImpl.class);
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.security.SecurityHelper.class).toInstance(createNiceMock(org.apache.ambari.server.security.SecurityHelper.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class).toInstance(customCommandExecutionHelperMock);
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(metaInfo);
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class));
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class).toInstance(kerberosOperationHandlerFactory);
                bind(org.apache.ambari.server.controller.spi.ClusterController.class).toInstance(clusterController);
                bind(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class).toInstance(kerberosDescriptorFactory);
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class).toInstance(kerberosConfigDataFileWriterFactory);
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(kerberosHelper);
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
                bind(org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.class).toInstance(createMock(org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.class));
                bind(org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.class).toInstance(createMock(org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.class));
                bind(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class).toInstance(createMock(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class));
                bind(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class).toInstance(createMock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class));
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.orm.dao.ArtifactDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.ArtifactDAO.class));
                bind(org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.class));
                bind(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.ldap.service.LdapFacade.class).toInstance(createNiceMock(org.apache.ambari.server.ldap.service.LdapFacade.class));
                requestStaticInjection(org.apache.ambari.server.controller.utilities.KerberosChecker.class);
            }
        });
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(topologyManager);
        EasyMock.expect(topologyManager.getPendingHostComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        org.apache.ambari.server.utils.StageUtils.setConfiguration(configuration);
        EasyMock.expect(configuration.getApiSSLAuthentication()).andReturn(false).anyTimes();
        EasyMock.expect(configuration.getClientApiPort()).andReturn(8080).anyTimes();
        EasyMock.expect(configuration.getServerTempDir()).andReturn(temporaryFolder.getRoot().getAbsolutePath()).anyTimes();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        if (!credentialStoreService.isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)) {
            ((org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl) (credentialStoreService)).initializeTemporaryCredentialStore(10, java.util.concurrent.TimeUnit.MINUTES, false);
        }
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testMissingClusterEnv() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createNiceMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        replayAll();
        kerberosHelper.toggleKerberos(cluster, org.apache.ambari.server.state.SecurityType.KERBEROS, requestStageContainer, true);
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testMissingKrb5Conf() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get("ldap_url")).andReturn("").once();
        EasyMock.expect(kerberosEnvProperties.get("container_dn")).andReturn("").once();
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).once();
        final org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getDesiredConfigByType("kerberos-env")).andReturn(kerberosEnvConfig).once();
        replayAll();
        kerberosHelper.toggleKerberos(cluster, org.apache.ambari.server.state.SecurityType.KERBEROS, null, true);
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testMissingKerberosEnvConf() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM)).andReturn("EXAMPLE.COM").once();
        EasyMock.expect(kerberosEnvProperties.get("kdc_hosts")).andReturn("10.0.100.1").once();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        EasyMock.expect(krb5ConfProperties.get("kadmin_host")).andReturn("10.0.100.1").once();
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).once();
        final org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getDesiredConfigByType("krb5-conf")).andReturn(krb5ConfConfig).once();
        replayAll();
        kerberosHelper.toggleKerberos(cluster, org.apache.ambari.server.state.SecurityType.KERBEROS, null, true);
        verifyAll();
    }

    @org.junit.Test
    public void testEnableKerberos() throws java.lang.Exception {
        testEnableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), "mit-kdc", "true");
    }

    @org.junit.Test
    public void testEnableKerberos_ManageIdentitiesFalseKdcNone() throws java.lang.Exception {
        testEnableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), "none", "false");
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testEnableKerberos_ManageIdentitiesTrueKdcNone() throws java.lang.Exception {
        testEnableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), "none", "true");
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException.class)
    public void testEnableKerberos_ManageIdentitiesTrueKdcNull() throws java.lang.Exception {
        testEnableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), null, "true");
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testEnableKerberosMissingCredentials() throws java.lang.Exception {
        try {
            testEnableKerberos(null, "mit-kdc", "true");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertTrue(e.getMessage().startsWith("Missing KDC administrator credentials"));
            throw e;
        }
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testEnableKerberosInvalidCredentials() throws java.lang.Exception {
        try {
            testEnableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("invalid_principal", "password"), "mit-kdc", "true");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertTrue(e.getMessage().startsWith("Invalid KDC administrator credentials"));
            throw e;
        }
    }

    @org.junit.Test
    public void testEnableKerberos_GetKerberosDescriptorFromCluster() throws java.lang.Exception {
        testEnableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), "mit-kdc", "true");
    }

    @org.junit.Test
    public void testEnableKerberos_GetKerberosDescriptorFromStack() throws java.lang.Exception {
        testEnableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), "mit-kdc", "true");
    }

    @org.junit.Test
    public void testEnsureIdentities() throws java.lang.Exception {
        testEnsureIdentities(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testEnsureIdentitiesMissingCredentials() throws java.lang.Exception {
        try {
            testEnsureIdentities(null, null);
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertTrue(e.getMessage().startsWith("Missing KDC administrator credentials"));
            throw e;
        }
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testEnsureIdentitiesInvalidCredentials() throws java.lang.Exception {
        try {
            testEnsureIdentities(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("invalid_principal", "password"), null);
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertTrue(e.getMessage().startsWith("Invalid KDC administrator credentials"));
            throw e;
        }
    }

    @org.junit.Test
    public void testEnsureIdentities_FilteredHosts() throws java.lang.Exception {
        testEnsureIdentities(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), java.util.Collections.singleton("hostA"));
    }

    @org.junit.Test
    public void testDeleteIdentities() throws java.lang.Exception {
        testDeleteIdentities(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testDeleteIdentitiesMissingCredentials() throws java.lang.Exception {
        try {
            testDeleteIdentities(null);
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertTrue(e.getMessage().startsWith("Missing KDC administrator credentials"));
            throw e;
        }
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testDeleteIdentitiesInvalidCredentials() throws java.lang.Exception {
        try {
            testDeleteIdentities(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("invalid_principal", "password"));
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertTrue(e.getMessage().startsWith("Invalid KDC administrator credentials"));
            throw e;
        }
    }

    @org.junit.Test
    public void testExecuteCustomOperationsInvalidOperation() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        try {
            kerberosHelper.executeCustomOperations(cluster, java.util.Collections.singletonMap("invalid_operation", "false"), null, true);
        } catch (java.lang.Throwable t) {
            org.junit.Assert.fail("Exception should not have been thrown");
        }
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testRegenerateKeytabsInvalidValue() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        kerberosHelper.executeCustomOperations(cluster, java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_REGENERATE_KEYTABS, "false"), null, true);
        org.junit.Assert.fail("AmbariException should have failed");
    }

    @org.junit.Test
    public void testRegenerateKeytabsValidateRequestStageContainer() throws java.lang.Exception {
        testRegenerateKeytabs(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), true, false);
    }

    @org.junit.Test
    public void testRegenerateKeytabsValidateSkipInvalidHost() throws java.lang.Exception {
        testRegenerateKeytabs(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), true, true);
    }

    @org.junit.Test
    public void testRegenerateKeytabs() throws java.lang.Exception {
        testRegenerateKeytabs(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), false, false);
    }

    @org.junit.Test
    public void testRegenerateKeytabsWithRetryAllowed() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.controller.ActionExecutionContext> captureContext = org.easymock.Capture.newInstance();
        customCommandExecutionHelperMock.addExecutionCommandsToStage(EasyMock.capture(captureContext), EasyMock.anyObject(org.apache.ambari.server.actionmanager.Stage.class), EasyMock.anyObject(), EasyMock.eq(null));
        EasyMock.expectLastCall().atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> requestMap = new java.util.HashMap<>();
        requestMap.put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_REGENERATE_KEYTABS, "true");
        requestMap.put(org.apache.ambari.server.controller.KerberosHelper.ALLOW_RETRY, "true");
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = testRegenerateKeytabs(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), requestMap, false, false);
        org.junit.Assert.assertNotNull(requestStageContainer);
        org.apache.ambari.server.controller.ActionExecutionContext capturedContext = captureContext.getValue();
        org.junit.Assert.assertTrue(capturedContext.isRetryAllowed());
    }

    @org.junit.Test
    public void testDisableKerberos() throws java.lang.Exception {
        testDisableKerberos(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"));
    }

    @org.junit.Test
    public void testCreateTestIdentity_ManageIdentitiesDefault() throws java.lang.Exception {
        testCreateTestIdentity(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), null);
    }

    @org.junit.Test
    public void testCreateTestIdentity_ManageIdentitiesTrue() throws java.lang.Exception {
        testCreateTestIdentity(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), java.lang.Boolean.TRUE);
    }

    @org.junit.Test
    public void testCreateTestIdentity_ManageIdentitiesFalse() throws java.lang.Exception {
        testCreateTestIdentity(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), java.lang.Boolean.FALSE);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testCreateTestIdentityNoCredentials_ManageIdentitiesDefault() throws java.lang.Exception {
        testCreateTestIdentity(null, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.class)
    public void testCreateTestIdentityNoCredentials_ManageIdentitiesTrue() throws java.lang.Exception {
        testCreateTestIdentity(null, java.lang.Boolean.TRUE);
    }

    @org.junit.Test
    public void testCreateTestIdentityNoCredentials_ManageIdentitiesFalse() throws java.lang.Exception {
        testCreateTestIdentity(null, java.lang.Boolean.FALSE);
    }

    @org.junit.Test
    public void testDeleteTestIdentity() throws java.lang.Exception {
        testDeleteTestIdentity(new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testGetActiveIdentities_MissingCluster() throws java.lang.Exception {
        testGetActiveIdentities(null, null, null, null, true, org.apache.ambari.server.state.SecurityType.KERBEROS);
    }

    @org.junit.Test
    public void testGetActiveIdentities_SecurityTypeKerberos_All() throws java.lang.Exception {
        testGetActiveIdentities_All(org.apache.ambari.server.state.SecurityType.KERBEROS);
    }

    @org.junit.Test
    public void testGetActiveIdentities_SecurityTypeNone_All() throws java.lang.Exception {
        testGetActiveIdentities_All(org.apache.ambari.server.state.SecurityType.NONE);
    }

    @org.junit.Test
    public void testGetActiveIdentities_SingleHost() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> identities = testGetActiveIdentities("c1", "host1", null, null, true, org.apache.ambari.server.state.SecurityType.KERBEROS);
        org.junit.Assert.assertNotNull(identities);
        org.junit.Assert.assertEquals(1, identities.size());
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> hostIdentities;
        hostIdentities = identities.get("host1");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(3, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity1", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/component1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/component1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity2", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "component2/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service2-site/component2.kerberos.principal");
                        put("principal_local_username", "service2");
                        put("keytab_file", "${keytab_dir}/service2.keytab");
                        put("keytab_owner_name", "service2");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service2-site/component2.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity3", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/service1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.service.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/service1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
    }

    @org.junit.Test
    public void addAmbariServerIdentity_CreateAmbariPrincipal() throws java.lang.Exception {
        addAmbariServerIdentity(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL, "true"));
    }

    @org.junit.Test
    public void addAmbariServerIdentity_DoNotCreateAmbariPrincipal() throws java.lang.Exception {
        addAmbariServerIdentity(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL, "false"));
    }

    @org.junit.Test
    public void addAmbariServerIdentity_MissingProperty() throws java.lang.Exception {
        addAmbariServerIdentity(java.util.Collections.singletonMap("not_create_ambari_principal", "value"));
    }

    @org.junit.Test
    public void addAmbariServerIdentity_MissingKerberosEnv() throws java.lang.Exception {
        addAmbariServerIdentity(null);
    }

    @org.junit.Test
    public void testGetActiveIdentities_SingleService() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> identities = testGetActiveIdentities("c1", null, "SERVICE1", null, true, org.apache.ambari.server.state.SecurityType.KERBEROS);
        org.junit.Assert.assertNotNull(identities);
        org.junit.Assert.assertEquals(3, identities.size());
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> hostIdentities;
        hostIdentities = identities.get("host1");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(2, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity1", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/component1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/component1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity3", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/service1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.service.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/service1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
        hostIdentities = identities.get("host2");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(2, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity1", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/component1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/component1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity3", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/service1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.service.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/service1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
    }

    @org.junit.Test
    public void testGetActiveIdentities_SingleServiceSingleHost() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> identities = testGetActiveIdentities("c1", "host2", "SERVICE1", null, true, org.apache.ambari.server.state.SecurityType.KERBEROS);
        org.junit.Assert.assertNotNull(identities);
        org.junit.Assert.assertEquals(1, identities.size());
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> hostIdentities;
        hostIdentities = identities.get("host2");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(2, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity1", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/component1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/component1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity3", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/service1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.service.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/service1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
    }

    @org.junit.Test
    public void testGetActiveIdentities_SingleComponent() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> identities = testGetActiveIdentities("c1", null, null, "COMPONENT2", true, org.apache.ambari.server.state.SecurityType.KERBEROS);
        org.junit.Assert.assertNotNull(identities);
        org.junit.Assert.assertEquals(3, identities.size());
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> hostIdentities;
        hostIdentities = identities.get("host1");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(1, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity2", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "component2/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service2-site/component2.kerberos.principal");
                        put("principal_local_username", "service2");
                        put("keytab_file", "${keytab_dir}/service2.keytab");
                        put("keytab_owner_name", "service2");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service2-site/component2.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
        hostIdentities = identities.get("host2");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(1, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity2", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "component2/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service2-site/component2.kerberos.principal");
                        put("principal_local_username", "service2");
                        put("keytab_file", "${keytab_dir}/service2.keytab");
                        put("keytab_owner_name", "service2");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service2-site/component2.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
    }

    private void testGetActiveIdentities_All(org.apache.ambari.server.state.SecurityType clusterSecurityType) throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> identities = testGetActiveIdentities("c1", null, null, null, true, clusterSecurityType);
        org.junit.Assert.assertNotNull(identities);
        org.junit.Assert.assertEquals(3, identities.size());
        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> hostIdentities;
        hostIdentities = identities.get("host1");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(3, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity1", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/component1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/component1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity2", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "component2/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service2-site/component2.kerberos.principal");
                        put("principal_local_username", "service2");
                        put("keytab_file", "${keytab_dir}/service2.keytab");
                        put("keytab_owner_name", "service2");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service2-site/component2.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity3", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host1@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/service1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.service.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/service1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
        hostIdentities = identities.get("host2");
        org.junit.Assert.assertNotNull(hostIdentities);
        org.junit.Assert.assertEquals(3, hostIdentities.size());
        validateIdentities(hostIdentities, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>() {
            {
                put("identity1", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/component1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/component1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity2", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "component2/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service2-site/component2.kerberos.principal");
                        put("principal_local_username", "service2");
                        put("keytab_file", "${keytab_dir}/service2.keytab");
                        put("keytab_owner_name", "service2");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service2-site/component2.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
                put("identity3", new java.util.HashMap<java.lang.String, java.lang.Object>() {
                    {
                        put("principal_name", "service1/host2@EXAMPLE.COM");
                        put("principal_type", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE);
                        put("principal_configuration", "service1-site/service1.kerberos.principal");
                        put("principal_local_username", "service1");
                        put("keytab_file", "${keytab_dir}/service1.service.keytab");
                        put("keytab_owner_name", "service1");
                        put("keytab_owner_access", "rw");
                        put("keytab_group_name", "hadoop");
                        put("keytab_group_access", "");
                        put("keytab_configuration", "service1-site/service1.keytab.file");
                        put("keytab_cachable", false);
                    }
                });
            }
        });
    }

    private void validateIdentities(java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> expectedDataMap) {
        org.junit.Assert.assertEquals(expectedDataMap.size(), identities.size());
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
            java.util.Map<java.lang.String, java.lang.Object> expectedData = expectedDataMap.get(identity.getName());
            org.junit.Assert.assertNotNull(expectedData);
            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principal = identity.getPrincipalDescriptor();
            org.junit.Assert.assertNotNull(principal);
            org.junit.Assert.assertEquals(expectedData.get("principal_name"), principal.getName());
            org.junit.Assert.assertEquals(expectedData.get("principal_type"), principal.getType());
            org.junit.Assert.assertEquals(expectedData.get("principal_configuration"), principal.getConfiguration());
            org.junit.Assert.assertEquals(expectedData.get("principal_local_username"), principal.getLocalUsername());
            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytab = identity.getKeytabDescriptor();
            org.junit.Assert.assertNotNull(keytab);
            org.junit.Assert.assertEquals(expectedData.get("keytab_file"), keytab.getFile());
            org.junit.Assert.assertEquals(expectedData.get("keytab_owner_name"), keytab.getOwnerName());
            org.junit.Assert.assertEquals(expectedData.get("keytab_owner_access"), keytab.getOwnerAccess());
            org.junit.Assert.assertEquals(expectedData.get("keytab_group_name"), keytab.getGroupName());
            org.junit.Assert.assertEquals(expectedData.get("keytab_group_access"), keytab.getGroupAccess());
            org.junit.Assert.assertEquals(expectedData.get("keytab_configuration"), keytab.getConfiguration());
            org.junit.Assert.assertEquals(java.lang.Boolean.TRUE.equals(expectedData.get("keytab_cachable")), keytab.isCachable());
        }
    }

    private void testEnableKerberos(final org.apache.ambari.server.security.credential.PrincipalKeyCredential PrincipalKeyCredential, java.lang.String kdcType, java.lang.String manageIdentities) throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        boolean identitiesManaged = (manageIdentities == null) || (!"false".equalsIgnoreCase(manageIdentities));
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClient.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(schKerberosClient.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        EasyMock.expect(sch1.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(sch1.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2.getServiceName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(sch2.getServiceComponentName()).andReturn("COMPONENT2").anyTimes();
        EasyMock.expect(sch2.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(sch2.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.Host host = createMockHost("host1");
        final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(java.util.Collections.singletonMap("host1", schKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service serviceKerberos = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(stackId).anyTimes();
        EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service service1 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service1.getDesiredStackId()).andReturn(stackId).anyTimes();
        EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.Service service2 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(service2.getDesiredStackId()).andReturn(stackId).anyTimes();
        EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE)).andReturn(kdcType).anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.MANAGE_IDENTITIES)).andReturn(manageIdentities).anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM)).andReturn("FOOBAR.COM").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL)).andReturn("false").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.PRECONFIGURE_SERVICES)).andReturn(org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.DEFAULT.name()).anyTimes();
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.singleton(host), org.apache.ambari.server.state.SecurityType.KERBEROS, krb5ConfConfig, kerberosEnvConfig);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                put("SERVICE1", service1);
                put("SERVICE2", service2);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("host1")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(schKerberosClient);
                add(sch1);
                add(sch2);
            }
        }).once();
        EasyMock.expect(cluster.getServiceComponentHosts("KERBEROS", "KERBEROS_CLIENT")).andReturn(java.util.Collections.singletonList(schKerberosClient)).once();
        if (identitiesManaged) {
            final org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
            EasyMock.expect(clusters.getHost("host1")).andReturn(host).once();
        }
        final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).once();
        EasyMock.expect(ambariManagementController.getRoleCommandOrder(cluster)).andReturn(createMock(org.apache.ambari.server.metadata.RoleCommandOrder.class)).once();
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getService("KERBEROS")).andReturn(null).once();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).once();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE2")).andReturn(serviceDescriptor2).once();
        setupKerberosDescriptor(kerberosDescriptor);
        setupStageFactory();
        final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createStrictMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
        EasyMock.expectLastCall().once();
        if (identitiesManaged) {
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(0L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
            EasyMock.expectLastCall().once();
        }
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(3L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, PrincipalKeyCredential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        kerberosHelper.toggleKerberos(cluster, org.apache.ambari.server.state.SecurityType.KERBEROS, requestStageContainer, null);
        verifyAll();
    }

    private void testDisableKerberos(final org.apache.ambari.server.security.credential.PrincipalKeyCredential PrincipalKeyCredential) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClient.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(schKerberosClient.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1.getServiceName()).andReturn("SERVICE1").times(1);
        EasyMock.expect(sch1.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(sch1.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2.getServiceName()).andReturn("SERVICE2").times(1);
        EasyMock.expect(sch2.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(sch2.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.Host host = createMockHost("host1");
        final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(java.util.Collections.singletonMap("host1", schKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service serviceKerberos = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service service1 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service1.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.Service service2 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service2.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE)).andReturn("mit-kdc").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM)).andReturn("FOOBAR.COM").anyTimes();
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.singleton(host), org.apache.ambari.server.state.SecurityType.NONE, krb5ConfConfig, kerberosEnvConfig);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                put("SERVICE1", service1);
                put("SERVICE2", service2);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("host1")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(schKerberosClient);
                add(sch1);
                add(sch2);
            }
        }).once();
        EasyMock.expect(cluster.getServiceComponentHosts("KERBEROS", "KERBEROS_CLIENT")).andReturn(java.util.Collections.singletonList(schKerberosClient)).once();
        final org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getHost("host1")).andReturn(host).once();
        final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).once();
        EasyMock.expect(ambariManagementController.getRoleCommandOrder(cluster)).andReturn(createMock(org.apache.ambari.server.metadata.RoleCommandOrder.class)).once();
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getService("KERBEROS")).andReturn(null).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE2")).andReturn(serviceDescriptor2).atLeastOnce();
        setupKerberosDescriptor(kerberosDescriptor);
        setupStageFactory();
        final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createStrictMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        EasyMock.expect(cluster.getService("ZOOKEEPER")).andReturn(service1).anyTimes();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(EasyMock.anyObject(java.util.List.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(3L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(3L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, PrincipalKeyCredential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        kerberosHelper.toggleKerberos(cluster, org.apache.ambari.server.state.SecurityType.NONE, requestStageContainer, true);
        verifyAll();
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer testRegenerateKeytabs(final org.apache.ambari.server.security.credential.PrincipalKeyCredential principalKeyCredential, boolean mockRequestStageContainer, final boolean testInvalidHost) throws java.lang.Exception {
        return testRegenerateKeytabs(principalKeyCredential, java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_REGENERATE_KEYTABS, "true"), mockRequestStageContainer, testInvalidHost);
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer testRegenerateKeytabs(final org.apache.ambari.server.security.credential.PrincipalKeyCredential PrincipalKeyCredential, java.util.Map<java.lang.String, java.lang.String> requestMap, boolean mockRequestStageContainer, final boolean testInvalidHost) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClient.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(schKerberosClient.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        EasyMock.expect(sch1.getHostName()).andReturn("host1").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2.getServiceName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(sch2.getServiceComponentName()).andReturn("COMPONENT2").anyTimes();
        EasyMock.expect(sch2.getHostName()).andReturn("host1").anyTimes();
        final org.apache.ambari.server.state.Host host = createMockHost("host1");
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClientInvalid;
        final org.apache.ambari.server.state.ServiceComponentHost sch1a;
        final org.apache.ambari.server.state.Host hostInvalid;
        if (testInvalidHost) {
            schKerberosClientInvalid = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
            EasyMock.expect(schKerberosClientInvalid.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
            EasyMock.expect(schKerberosClientInvalid.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
            EasyMock.expect(schKerberosClientInvalid.getHostName()).andReturn("host2").anyTimes();
            EasyMock.expect(schKerberosClientInvalid.getState()).andReturn(org.apache.ambari.server.state.State.INIT).anyTimes();
            sch1a = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
            EasyMock.expect(sch1a.getServiceName()).andReturn("SERVICE1").anyTimes();
            EasyMock.expect(sch1a.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
            EasyMock.expect(sch1a.getHostName()).andReturn("host2").anyTimes();
            hostInvalid = createMockHost("host1");
        } else {
            schKerberosClientInvalid = null;
            hostInvalid = null;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> map = new java.util.HashMap<>();
        final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        map.put("host1", schKerberosClient);
        EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        if (testInvalidHost) {
            map.put("host2", schKerberosClientInvalid);
        }
        EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(map).anyTimes();
        final org.apache.ambari.server.state.Service serviceKerberos = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service service1 = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service1.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.Service service2 = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service2.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE)).andReturn("mit-kdc").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM)).andReturn("FOOBAR.COM").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL)).andReturn("false").anyTimes();
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        final java.util.Collection<org.apache.ambari.server.state.Host> hosts = (testInvalidHost) ? java.util.Arrays.asList(host, hostInvalid) : java.util.Collections.singleton(host);
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", hosts, org.apache.ambari.server.state.SecurityType.KERBEROS, krb5ConfConfig, kerberosEnvConfig);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                put("SERVICE1", service1);
                put("SERVICE2", service2);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("host1")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(schKerberosClient);
                add(sch1);
                add(sch2);
            }
        }).atLeastOnce();
        EasyMock.expect(cluster.getServiceComponentHosts("KERBEROS", "KERBEROS_CLIENT")).andReturn(java.util.Collections.singletonList(schKerberosClient)).once();
        final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).once();
        EasyMock.expect(ambariManagementController.getRoleCommandOrder(cluster)).andReturn(createMock(org.apache.ambari.server.metadata.RoleCommandOrder.class)).once();
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getService("KERBEROS")).andReturn(null).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE2")).andReturn(serviceDescriptor2).atLeastOnce();
        setupKerberosDescriptor(kerberosDescriptor);
        setupStageFactory();
        final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer;
        if (mockRequestStageContainer) {
            requestStageContainer = createStrictMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(0L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(3L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
        } else {
            requestStageContainer = null;
        }
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, PrincipalKeyCredential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        org.apache.ambari.server.controller.internal.RequestStageContainer returnValue = kerberosHelper.executeCustomOperations(cluster, requestMap, requestStageContainer, true);
        org.junit.Assert.assertNotNull(returnValue);
        verifyAll();
        return returnValue;
    }

    @org.junit.Test
    public void testIsClusterKerberosEnabled_false() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.apache.ambari.server.state.Cluster cluster = createStrictMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.NONE);
        EasyMock.replay(cluster);
        org.junit.Assert.assertFalse(kerberosHelper.isClusterKerberosEnabled(cluster));
        EasyMock.verify(cluster);
    }

    @org.junit.Test
    public void testIsClusterKerberosEnabled_true() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.apache.ambari.server.state.Cluster cluster = createStrictMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS);
        EasyMock.replay(cluster);
        org.junit.Assert.assertTrue(kerberosHelper.isClusterKerberosEnabled(cluster));
        EasyMock.verify(cluster);
    }

    @org.junit.Test
    public void testGetManageIdentitiesDirective_NotSet() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.junit.Assert.assertEquals(null, kerberosHelper.getManageIdentitiesDirective(null));
        org.junit.Assert.assertEquals(null, kerberosHelper.getManageIdentitiesDirective(java.util.Collections.emptyMap()));
        org.junit.Assert.assertEquals(null, kerberosHelper.getManageIdentitiesDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES, null);
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
        org.junit.Assert.assertEquals(null, kerberosHelper.getManageIdentitiesDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
    }

    @org.junit.Test
    public void testGetManageIdentitiesDirective_True() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, kerberosHelper.getManageIdentitiesDirective(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES, "true")));
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, kerberosHelper.getManageIdentitiesDirective(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES, "not_false")));
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, kerberosHelper.getManageIdentitiesDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES, "true");
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
    }

    @org.junit.Test
    public void testGetManageIdentitiesDirective_False() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, kerberosHelper.getManageIdentitiesDirective(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES, "false")));
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, kerberosHelper.getManageIdentitiesDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES, "false");
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
    }

    @org.junit.Test
    public void testGetForceToggleKerberosDirective_NotSet() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.junit.Assert.assertEquals(false, kerberosHelper.getForceToggleKerberosDirective(null));
        org.junit.Assert.assertEquals(false, kerberosHelper.getForceToggleKerberosDirective(java.util.Collections.emptyMap()));
        org.junit.Assert.assertEquals(false, kerberosHelper.getForceToggleKerberosDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS, null);
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
        org.junit.Assert.assertEquals(false, kerberosHelper.getForceToggleKerberosDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
    }

    @org.junit.Test
    public void testGetForceToggleKerberosDirective_True() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.junit.Assert.assertEquals(true, kerberosHelper.getForceToggleKerberosDirective(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS, "true")));
        org.junit.Assert.assertEquals(false, kerberosHelper.getForceToggleKerberosDirective(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS, "not_true")));
        org.junit.Assert.assertEquals(true, kerberosHelper.getForceToggleKerberosDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS, "true");
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
    }

    @org.junit.Test
    public void testGetForceToggleKerberosDirective_False() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.junit.Assert.assertEquals(false, kerberosHelper.getForceToggleKerberosDirective(java.util.Collections.singletonMap(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS, "false")));
        org.junit.Assert.assertEquals(false, kerberosHelper.getForceToggleKerberosDirective(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS, "false");
                put("some_directive_0", "false");
                put("some_directive_1", null);
            }
        }));
    }

    @org.junit.Test
    public void testSetAuthToLocalRules() throws java.lang.Exception {
        testSetAuthToLocalRules(false);
    }

    @org.junit.Test
    public void testSetAuthToLocalRulesWithPreconfiguredServices() throws java.lang.Exception {
        testSetAuthToLocalRules(true);
    }

    private void testSetAuthToLocalRules(boolean includePreconfiguredServices) throws java.lang.Exception {
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1.getValue()).andReturn("principal1/host1@EXAMPLE.COM").times(1);
        EasyMock.expect(principalDescriptor1.getLocalUsername()).andReturn("principal1_user").times(1);
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor2.getValue()).andReturn("principal2/host2@EXAMPLE.COM").times(1);
        EasyMock.expect(principalDescriptor2.getLocalUsername()).andReturn("principal2_user").times(1);
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor3.getValue()).andReturn("principal3/host3@EXAMPLE.COM").times(1);
        EasyMock.expect(principalDescriptor3.getLocalUsername()).andReturn("principal3_user").times(1);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor1.getPrincipalDescriptor()).andReturn(principalDescriptor1).times(1);
        EasyMock.expect(identityDescriptor1.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor2.getPrincipalDescriptor()).andReturn(principalDescriptor2).times(1);
        EasyMock.expect(identityDescriptor2.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor3.getPrincipalDescriptor()).andReturn(principalDescriptor3).times(1);
        EasyMock.expect(identityDescriptor3.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor1 = createMockComponentDescriptor("COMPONENT1", java.util.Collections.singletonList(identityDescriptor3), null);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        EasyMock.expect(serviceDescriptor1.getName()).andReturn("SERVICE1").times(includePreconfiguredServices ? 2 : 1);
        if (includePreconfiguredServices) {
            EasyMock.expect(serviceDescriptor1.shouldPreconfigure()).andReturn(false).times(2);
        }
        EasyMock.expect(serviceDescriptor1.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(java.util.Arrays.asList(identityDescriptor1, identityDescriptor2)).times(1);
        EasyMock.expect(serviceDescriptor1.getComponents()).andReturn(java.util.Collections.singletonMap("COMPONENT1", componentDescriptor1)).times(1);
        EasyMock.expect(serviceDescriptor1.getAuthToLocalProperties()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("default", "explicit_multiple_lines|new_lines", "explicit_multiple_lines_escaped|new_lines_escaped", "explicit_single_line|spaces", "service-site/default", "service-site/explicit_multiple_lines|new_lines", "service-site/explicit_multiple_lines_escaped|new_lines_escaped", "service-site/explicit_single_line|spaces"))).times(1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptorMap = new java.util.HashMap<>();
        serviceDescriptorMap.put("SERVICE1", serviceDescriptor1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> component1Map = new java.util.HashMap<>();
        org.apache.ambari.server.state.Service service1 = createMockService("SERVICE1", component1Map);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = new java.util.HashMap<>();
        serviceMap.put("SERVICE1", service1);
        java.util.Map<java.lang.String, java.lang.String> serviceSiteProperties = new java.util.HashMap<>();
        serviceSiteProperties.put("default", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\nDEFAULT");
        serviceSiteProperties.put("explicit_multiple_lines", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\nDEFAULT");
        serviceSiteProperties.put("explicit_multiple_lines_escaped", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\\\nDEFAULT");
        serviceSiteProperties.put("explicit_single_line", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/ DEFAULT");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigs = new java.util.HashMap<>();
        existingConfigs.put("kerberos-env", new java.util.HashMap<java.lang.String, java.lang.String>());
        existingConfigs.put("service-site", serviceSiteProperties);
        if (includePreconfiguredServices) {
            final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor4 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
            EasyMock.expect(principalDescriptor4.getValue()).andReturn("${preconfig-site/service_user}/_HOST@EXAMPLE.COM").times(1);
            EasyMock.expect(principalDescriptor4.getLocalUsername()).andReturn("principal4_user").times(1);
            final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor5 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
            EasyMock.expect(principalDescriptor5.getValue()).andReturn("${preconfig-site/component_property1}/_HOST@EXAMPLE.COM").times(1);
            EasyMock.expect(principalDescriptor5.getLocalUsername()).andReturn("principal5_user").times(1);
            final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor4 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
            EasyMock.expect(identityDescriptor4.getPrincipalDescriptor()).andReturn(principalDescriptor4).times(1);
            EasyMock.expect(identityDescriptor4.shouldInclude(EasyMock.anyObject(java.util.Map.class))).andReturn(true).anyTimes();
            final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor5 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
            EasyMock.expect(identityDescriptor5.getPrincipalDescriptor()).andReturn(principalDescriptor5).times(1);
            EasyMock.expect(identityDescriptor5.shouldInclude(EasyMock.anyObject(java.util.Map.class))).andReturn(true).anyTimes();
            final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor2 = createMockComponentDescriptor("PRECONFIGURE_SERVICE_MASTER", java.util.Collections.singletonList(identityDescriptor5), null);
            final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
            EasyMock.expect(serviceDescriptor2.getName()).andReturn("PRECONFIGURE_SERVICE").times(2);
            EasyMock.expect(serviceDescriptor2.shouldPreconfigure()).andReturn(true).times(2);
            EasyMock.expect(serviceDescriptor2.getIdentities(EasyMock.eq(true), EasyMock.anyObject(java.util.Map.class))).andReturn(java.util.Collections.singletonList(identityDescriptor4)).times(1);
            EasyMock.expect(serviceDescriptor2.getComponents()).andReturn(java.util.Collections.singletonMap("PRECONFIGURE_SERVICE_MASTER", componentDescriptor2)).times(1);
            EasyMock.expect(serviceDescriptor2.getAuthToLocalProperties()).andReturn(java.util.Collections.<java.lang.String>emptySet()).times(1);
            serviceDescriptorMap.put("PRECONFIGURE_SERVICE", serviceDescriptor2);
            org.apache.ambari.server.state.ComponentInfo preconfigureComponentInfo = createMock(org.apache.ambari.server.state.ComponentInfo.class);
            org.apache.ambari.server.state.PropertyInfo preconfigureServiceUser = createMockPropertyInfo("preconfig-site.xml", "service_user", "principal4");
            org.apache.ambari.server.state.PropertyInfo preconfigureComponentProperty1 = createMockPropertyInfo("preconfig-site.xml", "component_property1", "principal5");
            java.util.List<org.apache.ambari.server.state.PropertyInfo> preconfigureServiceProperties = java.util.Arrays.asList(preconfigureComponentProperty1, preconfigureServiceUser);
            org.apache.ambari.server.state.ServiceInfo preconfigureServiceInfo = createMock(org.apache.ambari.server.state.ServiceInfo.class);
            EasyMock.expect(preconfigureServiceInfo.getProperties()).andReturn(preconfigureServiceProperties).anyTimes();
            EasyMock.expect(preconfigureServiceInfo.getComponents()).andReturn(java.util.Collections.singletonList(preconfigureComponentInfo)).anyTimes();
            org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            EasyMock.expect(ambariMetaInfo.isValidService("HDP", "2.2", "PRECONFIGURE_SERVICE")).andReturn(true).anyTimes();
            EasyMock.expect(ambariMetaInfo.getService("HDP", "2.2", "PRECONFIGURE_SERVICE")).andReturn(preconfigureServiceInfo).anyTimes();
        }
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getProperty("additional_realms")).andReturn(null).times(1);
        EasyMock.expect(kerberosDescriptor.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(null).times(1);
        EasyMock.expect(kerberosDescriptor.getAuthToLocalProperties()).andReturn(null).times(1);
        EasyMock.expect(kerberosDescriptor.getServices()).andReturn(serviceDescriptorMap).times(includePreconfiguredServices ? 2 : 1);
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.<org.apache.ambari.server.state.Host>emptyList(), org.apache.ambari.server.state.SecurityType.KERBEROS, null, null);
        if (includePreconfiguredServices) {
            EasyMock.expect(cluster.getServices()).andReturn(serviceMap).once();
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices = java.util.Collections.singletonMap("SERVICE1", java.util.Collections.singleton("COMPONENT1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        kerberosHelper.setAuthToLocalRules(cluster, kerberosDescriptor, "EXAMPLE.COM", installedServices, existingConfigs, kerberosConfigurations, includePreconfiguredServices);
        verifyAll();
        java.util.Map<java.lang.String, java.lang.String> configs;
        configs = kerberosConfigurations.get("");
        org.junit.Assert.assertNotNull(configs);
        if (includePreconfiguredServices) {
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/\n") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/\n") + "DEFAULT"), configs.get("default"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/\n") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/\n") + "DEFAULT"), configs.get("explicit_multiple_lines"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\\\n" + ((((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\\\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\\\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\\\n") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/\\\n") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/\\\n") + "DEFAULT"), configs.get("explicit_multiple_lines_escaped"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*// " + ((((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/ " + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/ ") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/ ") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/ ") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/ ") + "DEFAULT"), configs.get("explicit_single_line"));
        } else {
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "DEFAULT"), configs.get("default"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + ((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "DEFAULT"), configs.get("explicit_multiple_lines"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\\\n" + ((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\\\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\\\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\\\n") + "DEFAULT"), configs.get("explicit_multiple_lines_escaped"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*// " + ((("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/ " + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/ ") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/ ") + "DEFAULT"), configs.get("explicit_single_line"));
        }
        configs = kerberosConfigurations.get("service-site");
        org.junit.Assert.assertNotNull(configs);
        if (includePreconfiguredServices) {
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\n" + (((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/\n") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/\n") + "DEFAULT"), configs.get("default"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\n" + (((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/\n") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/\n") + "DEFAULT"), configs.get("explicit_multiple_lines"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\\\n" + (((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\\\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\\\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\\\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\\\n") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/\\\n") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/\\\n") + "DEFAULT"), configs.get("explicit_multiple_lines_escaped"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/ " + (((((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*// " + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/ ") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/ ") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/ ") + "RULE:[2:$1@$0](principal4@EXAMPLE.COM)s/.*/principal4_user/ ") + "RULE:[2:$1@$0](principal5@EXAMPLE.COM)s/.*/principal5_user/ ") + "DEFAULT"), configs.get("explicit_single_line"));
        } else {
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\n" + (((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "DEFAULT"), configs.get("default"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\n" + (((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\n") + "DEFAULT"), configs.get("explicit_multiple_lines"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\\\n" + (((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\\\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\\\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\\\n") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/\\\n") + "DEFAULT"), configs.get("explicit_multiple_lines_escaped"));
            org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/ " + (((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*// " + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/ ") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/ ") + "RULE:[2:$1@$0](principal3@EXAMPLE.COM)s/.*/principal3_user/ ") + "DEFAULT"), configs.get("explicit_single_line"));
        }
    }

    private org.apache.ambari.server.state.PropertyInfo createMockPropertyInfo(java.lang.String filename, java.lang.String propertyName, java.lang.String value) {
        org.apache.ambari.server.state.PropertyInfo propertyInfo = createMock(org.apache.ambari.server.state.PropertyInfo.class);
        EasyMock.expect(propertyInfo.getFilename()).andReturn(filename).anyTimes();
        EasyMock.expect(propertyInfo.getName()).andReturn(propertyName).anyTimes();
        EasyMock.expect(propertyInfo.getValue()).andReturn(value).anyTimes();
        return propertyInfo;
    }

    @org.junit.Test
    public void testSettingAuthToLocalRulesForUninstalledServiceComponents() throws java.lang.Exception {
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1.getValue()).andReturn("principal1/host1@EXAMPLE.COM").times(2);
        EasyMock.expect(principalDescriptor1.getLocalUsername()).andReturn("principal1_user").times(2);
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor2.getValue()).andReturn("principal2/host2@EXAMPLE.COM").times(1);
        EasyMock.expect(principalDescriptor2.getLocalUsername()).andReturn("principal2_user").times(1);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor1.getPrincipalDescriptor()).andReturn(principalDescriptor1).times(2);
        EasyMock.expect(identityDescriptor1.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor2.getPrincipalDescriptor()).andReturn(principalDescriptor2).times(1);
        EasyMock.expect(identityDescriptor2.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor1 = createMockComponentDescriptor("COMPONENT1", java.util.Collections.singletonList(identityDescriptor1), null);
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor2 = createMockComponentDescriptor("COMPONENT2", java.util.Collections.singletonList(identityDescriptor2), null);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        EasyMock.expect(serviceDescriptor1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(serviceDescriptor1.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(java.util.Arrays.asList(identityDescriptor1)).times(1);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> kerberosComponents = new java.util.HashMap<>();
        kerberosComponents.put("COMPONENT1", componentDescriptor1);
        kerberosComponents.put("COMPONENT2", componentDescriptor2);
        EasyMock.expect(serviceDescriptor1.getComponents()).andReturn(kerberosComponents).times(1);
        EasyMock.expect(serviceDescriptor1.getAuthToLocalProperties()).andReturn(new java.util.HashSet<>(java.util.Arrays.asList("default", "explicit_multiple_lines|new_lines", "explicit_multiple_lines_escaped|new_lines_escaped", "explicit_single_line|spaces", "service-site/default", "service-site/explicit_multiple_lines|new_lines", "service-site/explicit_multiple_lines_escaped|new_lines_escaped", "service-site/explicit_single_line|spaces"))).times(1);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptorMap = new java.util.HashMap<>();
        serviceDescriptorMap.put("SERVICE1", serviceDescriptor1);
        final org.apache.ambari.server.state.Service service1 = createMockService("SERVICE1", new java.util.HashMap<>());
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = new java.util.HashMap<>();
        serviceMap.put("SERVICE1", service1);
        final java.util.Map<java.lang.String, java.lang.String> serviceSiteProperties = new java.util.HashMap<>();
        serviceSiteProperties.put("default", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\nDEFAULT");
        serviceSiteProperties.put("explicit_multiple_lines", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\nDEFAULT");
        serviceSiteProperties.put("explicit_multiple_lines_escaped", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\\\nDEFAULT");
        serviceSiteProperties.put("explicit_single_line", "RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/ DEFAULT");
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigs = new java.util.HashMap<>();
        existingConfigs.put("kerberos-env", new java.util.HashMap<java.lang.String, java.lang.String>());
        existingConfigs.get("kerberos-env").put(org.apache.ambari.server.controller.KerberosHelper.INCLUDE_ALL_COMPONENTS_IN_AUTH_TO_LOCAL_RULES, "true");
        existingConfigs.put("service-site", serviceSiteProperties);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getProperty("additional_realms")).andReturn(null).times(1);
        EasyMock.expect(kerberosDescriptor.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(null).times(1);
        EasyMock.expect(kerberosDescriptor.getAuthToLocalProperties()).andReturn(null).times(1);
        EasyMock.expect(kerberosDescriptor.getServices()).andReturn(serviceDescriptorMap).times(1);
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.<org.apache.ambari.server.state.Host>emptyList(), org.apache.ambari.server.state.SecurityType.KERBEROS, null, null);
        final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices = java.util.Collections.singletonMap("SERVICE1", java.util.Collections.singleton("COMPONENT1"));
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class).setAuthToLocalRules(cluster, kerberosDescriptor, "EXAMPLE.COM", installedServices, existingConfigs, kerberosConfigurations, false);
        verifyAll();
        java.util.Map<java.lang.String, java.lang.String> configs = kerberosConfigurations.get("");
        org.junit.Assert.assertNotNull(configs);
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + (("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "DEFAULT"), configs.get("default"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + (("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "DEFAULT"), configs.get("explicit_multiple_lines"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\\\n" + (("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\\\n" + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\\\n") + "DEFAULT"), configs.get("explicit_multiple_lines_escaped"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*// " + (("RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/ " + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/ ") + "DEFAULT"), configs.get("explicit_single_line"));
        configs = kerberosConfigurations.get("service-site");
        org.junit.Assert.assertNotNull(configs);
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\n" + ((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "DEFAULT"), configs.get("default"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\n" + ((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\n") + "DEFAULT"), configs.get("explicit_multiple_lines"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/\\\n" + ((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*//\\\n" + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/\\\n") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/\\\n") + "DEFAULT"), configs.get("explicit_multiple_lines_escaped"));
        org.junit.Assert.assertEquals("RULE:[1:$1@$0](service_site@EXAMPLE.COM)s/.*/service_user/ " + ((("RULE:[1:$1@$0](.*@EXAMPLE.COM)s/@.*// " + "RULE:[2:$1@$0](principal1@EXAMPLE.COM)s/.*/principal1_user/ ") + "RULE:[2:$1@$0](principal2@EXAMPLE.COM)s/.*/principal2_user/ ") + "DEFAULT"), configs.get("explicit_single_line"));
    }

    @org.junit.Test
    public void testMergeConfigurationsForPreconfiguring() throws java.lang.Exception {
        org.apache.ambari.server.state.Service existingService = createMockService("EXISTING_SERVICE", null);
        java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
        serviceNames.add("EXISTING_SERVICE");
        serviceNames.add("PRECONFIGURE_SERVICE");
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.<org.apache.ambari.server.state.Host>emptyList(), org.apache.ambari.server.state.SecurityType.KERBEROS, null, null);
        EasyMock.expect(cluster.getServices()).andReturn(services).times(2);
        EasyMock.expect(cluster.getServiceComponentHostMap(null, serviceNames)).andReturn(hostMap).once();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createKerberosDescriptor();
        org.apache.ambari.server.state.ComponentInfo preconfigureComponentInfo = createMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(preconfigureComponentInfo.getName()).andReturn("PRECONFIGURE_SERVICE_MASTER").once();
        java.util.List<org.apache.ambari.server.state.PropertyInfo> preconfigureServiceProperties = java.util.Collections.singletonList(createMockPropertyInfo("preconfigure-service-env.xml", "service_user", "preconfigure_user"));
        org.apache.ambari.server.state.ServiceInfo preconfigureServiceInfo = createMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(preconfigureServiceInfo.getProperties()).andReturn(preconfigureServiceProperties).anyTimes();
        EasyMock.expect(preconfigureServiceInfo.getComponents()).andReturn(java.util.Collections.singletonList(preconfigureComponentInfo)).anyTimes();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(ambariMetaInfo.isValidService("HDP", "2.2", "PRECONFIGURE_SERVICE")).andReturn(true).anyTimes();
        EasyMock.expect(ambariMetaInfo.getService("HDP", "2.2", "PRECONFIGURE_SERVICE")).andReturn(preconfigureServiceInfo).anyTimes();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> host1Components = new java.util.HashSet<>();
        host1Components.add(java.util.Collections.singletonMap("name", "EXISTING_SERVICE_MASTER"));
        host1Components.add(java.util.Collections.singletonMap("name", "PRECONFIGURE_SERVICE_MASTER"));
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> host2Components = new java.util.HashSet<>();
        host2Components.add(java.util.Collections.singletonMap("name", "EXISTING_SERVICE_MASTER"));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup1 = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup.class);
        EasyMock.expect(hostGroup1.getName()).andReturn("host1").once();
        EasyMock.expect(hostGroup1.getComponents()).andReturn(host1Components).once();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup2 = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup.class);
        EasyMock.expect(hostGroup2.getName()).andReturn("host2").once();
        EasyMock.expect(hostGroup2.getComponents()).andReturn(host2Components).once();
        java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroups = new java.util.HashSet<>();
        hostGroups.add(hostGroup1);
        hostGroups.add(hostGroup2);
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint.class);
        EasyMock.expect(blueprint.getHostGroups()).andReturn(hostGroups).once();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup bindHostGroup1 = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup.class);
        EasyMock.expect(bindHostGroup1.getName()).andReturn("host1").once();
        EasyMock.expect(bindHostGroup1.getHosts()).andReturn(java.util.Collections.singleton(java.util.Collections.singletonMap("fqdn", "host1"))).once();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup bindHostGroup2 = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup.class);
        EasyMock.expect(bindHostGroup2.getName()).andReturn("host2").once();
        EasyMock.expect(bindHostGroup2.getHosts()).andReturn(java.util.Collections.singleton(java.util.Collections.singletonMap("fqdn", "host2"))).once();
        java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> bindingHostGroups = new java.util.HashSet<>();
        bindingHostGroups.add(bindHostGroup1);
        bindingHostGroups.add(bindHostGroup2);
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding binding = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding.class);
        EasyMock.expect(binding.getHostGroups()).andReturn(bindingHostGroups).once();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendation = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation.class);
        EasyMock.expect(recommendation.getBlueprint()).andReturn(blueprint).once();
        EasyMock.expect(recommendation.getBlueprintClusterBinding()).andReturn(binding).once();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.class);
        EasyMock.expect(response.getRecommendations()).andReturn(recommendation).once();
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class);
        EasyMock.expect(stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(response).once();
        replayAll();
        services.put(existingService.getName(), existingService);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = new java.util.HashMap<>();
        existingConfigurations.put("core-site", new java.util.HashMap<>(java.util.Collections.singletonMap("core-property1", "original_value")));
        existingConfigurations.put("hadoop-env", new java.util.HashMap<>(java.util.Collections.singletonMap("proxyuser_group", "hadoop")));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements = new java.util.HashMap<>(existingConfigurations);
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = kerberosHelper.processPreconfiguredServiceConfigurations(existingConfigurations, replacements, cluster, kerberosDescriptor);
        verifyAll();
        org.junit.Assert.assertNotNull(configurations);
        org.junit.Assert.assertEquals(2, configurations.size());
        org.junit.Assert.assertNotNull(configurations.get("core-site"));
        org.junit.Assert.assertNotNull(configurations.get("hadoop-env"));
        org.junit.Assert.assertEquals("hadoop", configurations.get("core-site").get("hadoop.proxyuser.preconfigure_user.groups"));
        org.junit.Assert.assertEquals("host1", configurations.get("core-site").get("hadoop.proxyuser.preconfigure_user.hosts"));
    }

    @org.junit.Test
    public void testGetServiceConfigurationUpdates() throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.Host hostA = createMockHost("hostA");
        final org.apache.ambari.server.state.Host hostB = createMockHost("hostB");
        final org.apache.ambari.server.state.Host hostC = createMockHost("hostC");
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = java.util.Arrays.asList(hostA, hostB, hostC);
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE, "mit-kdc");
                put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "FOOBAR.COM");
                put("case_insensitive_username_rules", "false");
                put(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL, "false");
            }
        };
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).atLeastOnce();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).atLeastOnce();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1 = createMockPrincipalDescriptor("service1/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "service1user", "service1-site/service.kerberos.principal");
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1a = createMockPrincipalDescriptor("component1a/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "service1user", "service1-site/component1a.kerberos.principal");
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1b = createMockPrincipalDescriptor("component1b/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "service1user", "service1-site/component1b.kerberos.principal");
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor2a = createMockPrincipalDescriptor("component2a/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "service2user", "service2-site/component2a.kerberos.principal");
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor2b = createMockPrincipalDescriptor("component2b/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "service2user", "service2-site/component2b.kerberos.principal");
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor3a = createMockPrincipalDescriptor("component3a/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "service3user", "service3-site/component3a.kerberos.principal");
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor1 = createMockKeytabDescriptor("keytab1", "service1-site/service.kerberos.keytab");
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor1a = createMockKeytabDescriptor("keytab1a", "service1-site/component1a.kerberos.keytab");
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor1b = createMockKeytabDescriptor("keytab1b", "service1-site/component1b.kerberos.keytab");
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor2a = createMockKeytabDescriptor("keytab2a", "service2-site/component2a.kerberos.keytab");
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor2b = createMockKeytabDescriptor("keytab2b", "service2-site/component2b.kerberos.keytab");
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor3a = createMockKeytabDescriptor("keytab3a", "service3-site/component3a.kerberos.keytab");
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1 = createMockIdentityDescriptor("identity1", principalDescriptor1, keytabDescriptor1);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1a = createMockIdentityDescriptor("identity1a", principalDescriptor1a, keytabDescriptor1a);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1b = createMockIdentityDescriptor("identity1b", principalDescriptor1b, keytabDescriptor1b);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor2a = createMockIdentityDescriptor("identity2a", principalDescriptor2a, keytabDescriptor2a);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor2b = createMockIdentityDescriptor("identity2b", principalDescriptor2b, keytabDescriptor2b);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor3a = createMockIdentityDescriptor("identity3a", principalDescriptor3a, keytabDescriptor3a);
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor1a = createMockComponentDescriptor("COMPONENT1A", new java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>() {
            {
                add(identityDescriptor1a);
            }
        }, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor>() {
            {
                put("service1-site", createMockConfigurationDescriptor(java.util.Collections.singletonMap("component1a.property", "${replacement1}")));
            }
        });
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor1b = createMockComponentDescriptor("COMPONENT1B", new java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>() {
            {
                add(identityDescriptor1b);
            }
        }, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor>() {
            {
                put("service1-site", createMockConfigurationDescriptor(java.util.Collections.singletonMap("component1b.property", "${type1/replacement1}")));
            }
        });
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor2a = createMockComponentDescriptor("COMPONENT2A", new java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>() {
            {
                add(identityDescriptor2a);
            }
        }, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor>() {
            {
                put("service2-site", createMockConfigurationDescriptor(java.util.Collections.singletonMap("component2a.property", "${type1/replacement2}")));
            }
        });
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor2b = createMockComponentDescriptor("COMPONENT2B", new java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>() {
            {
                add(identityDescriptor2b);
            }
        }, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor>() {
            {
                put("service2-site", createMockConfigurationDescriptor(java.util.Collections.singletonMap("component2b.property", "${type2/replacement1}")));
            }
        });
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor3a = createMockComponentDescriptor("COMPONENT3A", new java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>() {
            {
                add(identityDescriptor3a);
            }
        }, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor>() {
            {
                put("service3-site", createMockConfigurationDescriptor(java.util.Collections.singletonMap("component3a.property", "${type3/replacement1}")));
                put("core-site", createMockConfigurationDescriptor(java.util.Collections.singletonMap("component3b.property", "${type3/replacement2}")));
            }
        });
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMockServiceDescriptor("SERVICE1", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor>() {
            {
                put("COMPONENT1A", componentDescriptor1a);
                put("COMPONENT1B", componentDescriptor1b);
            }
        }, new java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>() {
            {
                add(identityDescriptor1);
            }
        }, false);
        EasyMock.expect(serviceDescriptor1.getComponent("COMPONENT1A")).andReturn(componentDescriptor1a).times(2);
        EasyMock.expect(serviceDescriptor1.getComponent("COMPONENT1B")).andReturn(componentDescriptor1b).times(2);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor2 = createMockServiceDescriptor("SERVICE2", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor>() {
            {
                put("COMPONENT2A", componentDescriptor2a);
                put("COMPONENT2B", componentDescriptor2b);
            }
        }, java.util.Collections.<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>emptyList(), false);
        EasyMock.expect(serviceDescriptor2.getComponent("COMPONENT2A")).andReturn(componentDescriptor2a).times(1);
        EasyMock.expect(serviceDescriptor2.getComponent("COMPONENT2B")).andReturn(componentDescriptor2b).times(1);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor3 = createMockServiceDescriptor("SERVICE3", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor>() {
            {
                put("COMPONENT3A", componentDescriptor3a);
            }
        }, java.util.Collections.<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>emptyList(), false);
        EasyMock.expect(serviceDescriptor3.getComponent("COMPONENT3A")).andReturn(componentDescriptor3a).times(2);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptorMap = new java.util.HashMap<>();
        serviceDescriptorMap.put("SERVICE1", serviceDescriptor1);
        serviceDescriptorMap.put("SERVICE2", serviceDescriptor2);
        serviceDescriptorMap.put("SERVICE3", serviceDescriptor3);
        final java.util.Map<java.lang.String, java.lang.String> kerberosDescriptorProperties = new java.util.HashMap<>();
        kerberosDescriptorProperties.put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "${kerberos-env/realm}");
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getProperties()).andReturn(kerberosDescriptorProperties).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE2")).andReturn(serviceDescriptor2).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE3")).andReturn(serviceDescriptor3).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getServices()).andReturn(serviceDescriptorMap).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getProperty("additional_realms")).andReturn(null).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(null).atLeastOnce();
        EasyMock.expect(kerberosDescriptor.getAuthToLocalProperties()).andReturn(java.util.Collections.singleton("core-site/auth.to.local")).atLeastOnce();
        setupKerberosDescriptor(kerberosDescriptor);
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations coreSiteRecommendation = createNiceMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.class);
        EasyMock.expect(coreSiteRecommendation.getProperties()).andReturn(java.util.Collections.singletonMap("newPropertyRecommendation", "newPropertyRecommendation"));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations newTypeRecommendation = createNiceMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.class);
        EasyMock.expect(newTypeRecommendation.getProperties()).andReturn(java.util.Collections.singletonMap("newTypeRecommendation", "newTypeRecommendation"));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations type1Recommendation = createNiceMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.class);
        EasyMock.expect(type1Recommendation.getProperties()).andReturn(java.util.Collections.singletonMap("replacement1", "not replaced"));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations service1SiteRecommendation = createNiceMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.class);
        EasyMock.expect(service1SiteRecommendation.getProperties()).andReturn(java.util.Collections.singletonMap("component1b.property", "replaced value"));
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configurations = new java.util.HashMap<>();
        configurations.put("core-site", coreSiteRecommendation);
        configurations.put("new-type", newTypeRecommendation);
        configurations.put("type1", type1Recommendation);
        configurations.put("service1-site", service1SiteRecommendation);
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint.class);
        EasyMock.expect(blueprint.getConfigurations()).andReturn(configurations).once();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendations = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation.class);
        EasyMock.expect(recommendations.getBlueprint()).andReturn(blueprint).once();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse recommendationResponse = createMock(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.class);
        EasyMock.expect(recommendationResponse.getRecommendations()).andReturn(recommendations).once();
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class);
        EasyMock.expect(stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(null).once();
        EasyMock.expect(stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(recommendationResponse).once();
        final org.apache.ambari.server.state.Service service1 = createMockService("SERVICE1", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put("COMPONENT1A", createMockComponent("COMPONENT1A", true, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
                    {
                        put("hostA", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
                    }
                }));
                put("COMPONENT1B", createMockComponent("COMPONENT1B", false, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
                    {
                        put("hostB", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
                        put("hostC", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
                    }
                }));
            }
        });
        final org.apache.ambari.server.state.Service service2 = createMockService("SERVICE2", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put("COMPONENT2A", createMockComponent("COMPONENT2A", true, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
                    {
                        put("hostA", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
                    }
                }));
                put("COMPONENT2B", createMockComponent("COMPONENT2B", false, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
                    {
                        put("hostB", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
                        put("hostC", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
                    }
                }));
            }
        });
        final org.apache.ambari.server.state.Service service3 = createMockService("SERVICE3", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put("COMPONENT3A", createMockComponent("COMPONENT3A", true, new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
                    {
                        put("hostA", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
                    }
                }));
            }
        });
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        services.put("SERVICE1", service1);
        services.put("SERVICE2", service2);
        services.put("SERVICE3", service3);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceComponentHostMap = new java.util.HashMap<>();
        serviceComponentHostMap.put("COMPONENT1A", new java.util.TreeSet<>(java.util.Arrays.asList("hostA")));
        serviceComponentHostMap.put("COMPONENT1B", new java.util.TreeSet<>(java.util.Arrays.asList("hostB", "hostC")));
        serviceComponentHostMap.put("COMPONENT2A", new java.util.TreeSet<>(java.util.Arrays.asList("hostA")));
        serviceComponentHostMap.put("COMPONENT2B", new java.util.TreeSet<>(java.util.Arrays.asList("hostB", "hostC")));
        serviceComponentHostMap.put("COMPONEN3A", new java.util.TreeSet<>(java.util.Arrays.asList("hostA")));
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", hosts, org.apache.ambari.server.state.SecurityType.KERBEROS, krb5ConfConfig, kerberosEnvConfig);
        EasyMock.expect(cluster.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHostMap(org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject())).andReturn(serviceComponentHostMap).anyTimes();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("kerberos-env", kerberosEnvProperties);
                put("", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value1");
                    }
                });
                put("type1", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value2");
                        put("replacement2", "value3");
                    }
                });
                put("type2", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value4");
                        put("replacement2", "value5");
                    }
                });
                put("type3", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value6");
                        put("replacement2", "value7");
                    }
                });
            }
        };
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>> installedServices1 = new java.util.HashMap<>();
        installedServices1.put("SERVICE1", new java.util.HashSet<>(java.util.Arrays.asList("COMPONENT1A", "COMPONENT1B")));
        installedServices1.put("SERVICE2", new java.util.HashSet<>(java.util.Arrays.asList("COMPONENT2A", "COMPONENT2B")));
        installedServices1.put("SERVICE3", java.util.Collections.singleton("COMPONENT3A"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> updates1 = kerberosHelper.getServiceConfigurationUpdates(cluster, existingConfigurations, installedServices1, null, null, true, true);
        java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>> installedServices2 = new java.util.HashMap<>();
        installedServices2.put("SERVICE1", new java.util.HashSet<>(java.util.Arrays.asList("COMPONENT1A", "COMPONENT1B")));
        installedServices2.put("SERVICE3", java.util.Collections.singleton("COMPONENT3A"));
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceFilter2 = new java.util.HashMap<>();
        serviceFilter2.put("SERVICE1", new java.util.HashSet<>(java.util.Arrays.asList("COMPONENT1A", "COMPONENT1B")));
        serviceFilter2.put("SERVICE3", java.util.Collections.singleton("COMPONENT3A"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> updates2 = kerberosHelper.getServiceConfigurationUpdates(cluster, existingConfigurations, installedServices2, serviceFilter2, null, true, true);
        verifyAll();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedUpdates = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("service1-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("service.kerberos.principal", "service1/_HOST@FOOBAR.COM");
                        put("service.kerberos.keytab", "keytab1");
                        put("component1a.kerberos.principal", "component1a/_HOST@FOOBAR.COM");
                        put("component1a.kerberos.keytab", "keytab1a");
                        put("component1a.property", "value1");
                        put("component1b.kerberos.principal", "component1b/_HOST@FOOBAR.COM");
                        put("component1b.kerberos.keytab", "keytab1b");
                        put("component1b.property", "value2");
                    }
                });
                put("service2-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("component2a.kerberos.principal", "component2a/_HOST@FOOBAR.COM");
                        put("component2a.kerberos.keytab", "keytab2a");
                        put("component2a.property", "value3");
                        put("component2b.kerberos.principal", "component2b/_HOST@FOOBAR.COM");
                        put("component2b.kerberos.keytab", "keytab2b");
                        put("component2b.property", "value4");
                    }
                });
                put("service3-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("component3a.kerberos.principal", "component3a/_HOST@FOOBAR.COM");
                        put("component3a.kerberos.keytab", "keytab3a");
                        put("component3a.property", "value6");
                    }
                });
                put("core-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("auth.to.local", "RULE:[1:$1@$0](.*@FOOBAR.COM)s/@.*//\n" + (((((("RULE:[2:$1@$0](component1a@FOOBAR.COM)s/.*/service1user/\n" + "RULE:[2:$1@$0](component1b@FOOBAR.COM)s/.*/service1user/\n") + "RULE:[2:$1@$0](component2a@FOOBAR.COM)s/.*/service2user/\n") + "RULE:[2:$1@$0](component2b@FOOBAR.COM)s/.*/service2user/\n") + "RULE:[2:$1@$0](component3a@FOOBAR.COM)s/.*/service3user/\n") + "RULE:[2:$1@$0](service1@FOOBAR.COM)s/.*/service1user/\n") + "DEFAULT"));
                        put("component3b.property", "value7");
                    }
                });
            }
        };
        org.junit.Assert.assertEquals(expectedUpdates, updates1);
        expectedUpdates.remove("service2-site");
        expectedUpdates.get("core-site").put("newPropertyRecommendation", "newPropertyRecommendation");
        expectedUpdates.get("core-site").put("auth.to.local", "RULE:[1:$1@$0](.*@FOOBAR.COM)s/@.*//\n" + (((("RULE:[2:$1@$0](component1a@FOOBAR.COM)s/.*/service1user/\n" + "RULE:[2:$1@$0](component1b@FOOBAR.COM)s/.*/service1user/\n") + "RULE:[2:$1@$0](component3a@FOOBAR.COM)s/.*/service3user/\n") + "RULE:[2:$1@$0](service1@FOOBAR.COM)s/.*/service1user/\n") + "DEFAULT"));
        expectedUpdates.get("service1-site").put("component1b.property", "replaced value");
        expectedUpdates.put("new-type", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("newTypeRecommendation", "newTypeRecommendation");
            }
        });
        org.junit.Assert.assertEquals(expectedUpdates, updates2);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedExistingConfigurations = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("kerberos-env", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE, "mit-kdc");
                        put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "FOOBAR.COM");
                        put("case_insensitive_username_rules", "false");
                        put(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL, "false");
                    }
                });
                put("", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value1");
                    }
                });
                put("type1", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value2");
                        put("replacement2", "value3");
                    }
                });
                put("type2", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value4");
                        put("replacement2", "value5");
                    }
                });
                put("type3", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("replacement1", "value6");
                        put("replacement2", "value7");
                    }
                });
            }
        };
        org.junit.Assert.assertEquals(expectedExistingConfigurations, existingConfigurations);
    }

    @org.junit.Test
    public void testEnsureHeadlessIdentities() throws java.lang.Exception {
        testEnsureHeadlessIdentities(false, false);
    }

    @org.junit.Test
    public void testEnsureHeadlessAndAmbariIdentitiesAsUser() throws java.lang.Exception {
        testEnsureHeadlessIdentities(true, false);
    }

    @org.junit.Test
    public void testEnsureHeadlessAndAmbariIdentitiesAsService() throws java.lang.Exception {
        testEnsureHeadlessIdentities(true, true);
    }

    private void testEnsureHeadlessIdentities(boolean createAmbariIdentities, boolean ambariServerPrincipalAsService) throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String realm = "EXAMPLE.COM";
        java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
        java.lang.String ambariServerPrincipalName;
        java.lang.String ambariServerKeytabFilePath = new java.io.File("ambari.server.keytab").getAbsolutePath();
        org.apache.ambari.server.state.kerberos.KerberosPrincipalType ambariServerPrincipalType;
        java.lang.String ambariServerPrincipalNameExpected;
        if (ambariServerPrincipalAsService) {
            ambariServerPrincipalName = "ambari-server${principal_suffix}/_HOST@${realm}";
            ambariServerPrincipalType = org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE;
            ambariServerPrincipalNameExpected = java.lang.String.format("ambari-server-%s/%s@%s", clusterName, ambariServerHostname, realm);
        } else {
            ambariServerPrincipalName = "ambari-server${principal_suffix}@${realm}";
            ambariServerPrincipalType = org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER;
            ambariServerPrincipalNameExpected = java.lang.String.format("ambari-server-%s@%s", clusterName, realm);
        }
        java.util.Map<java.lang.String, java.lang.String> propertiesKrb5Conf = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> propertiesKerberosEnv = new java.util.HashMap<>();
        propertiesKerberosEnv.put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, realm);
        propertiesKerberosEnv.put(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE, "mit-kdc");
        propertiesKerberosEnv.put("password_length", "20");
        propertiesKerberosEnv.put("password_min_lowercase_letters", "1");
        propertiesKerberosEnv.put("password_min_uppercase_letters", "1");
        propertiesKerberosEnv.put("password_min_digits", "1");
        propertiesKerberosEnv.put("password_min_punctuation", "0");
        propertiesKerberosEnv.put("password_min_whitespace", "0");
        propertiesKerberosEnv.put(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL, createAmbariIdentities ? "true" : "false");
        org.apache.ambari.server.state.Config configKrb5Conf = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(configKrb5Conf.getProperties()).andReturn(propertiesKrb5Conf).times(1);
        org.apache.ambari.server.state.Config configKerberosEnv = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(configKerberosEnv.getProperties()).andReturn(propertiesKerberosEnv).times(1);
        org.apache.ambari.server.state.Host host1 = createMockHost("host1");
        org.apache.ambari.server.state.Host host2 = createMockHost("host3");
        org.apache.ambari.server.state.Host host3 = createMockHost("host2");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> service1Component1HostMap = new java.util.HashMap<>();
        service1Component1HostMap.put("host1", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> service2Component1HostMap = new java.util.HashMap<>();
        service2Component1HostMap.put("host2", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> service1ComponentMap = new java.util.HashMap<>();
        service1ComponentMap.put("COMPONENT11", createMockComponent("COMPONENT11", true, service1Component1HostMap));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> service2ComponentMap = new java.util.HashMap<>();
        service2ComponentMap.put("COMPONENT21", createMockComponent("COMPONENT21", true, service2Component1HostMap));
        org.apache.ambari.server.state.Service service1 = createMockService("SERVICE1", service1ComponentMap);
        org.apache.ambari.server.state.Service service2 = createMockService("SERVICE2", service2ComponentMap);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> servicesMap = new java.util.HashMap<>();
        servicesMap.put("SERVICE1", service1);
        servicesMap.put("SERVICE2", service2);
        org.apache.ambari.server.state.Cluster cluster = createMockCluster(clusterName, java.util.Arrays.asList(host1, host2, host3), org.apache.ambari.server.state.SecurityType.KERBEROS, configKrb5Conf, configKerberosEnv);
        EasyMock.expect(cluster.getServices()).andReturn(servicesMap).anyTimes();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> kerberosDescriptorProperties = new java.util.HashMap<>();
        kerberosDescriptorProperties.put("additional_realms", "");
        kerberosDescriptorProperties.put("keytab_dir", "/etc/security/keytabs");
        kerberosDescriptorProperties.put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "${kerberos-env/realm}");
        kerberosDescriptorProperties.put("principal_suffix", "-${cluster_name|toLower()}");
        java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> service1Component1Identities = new java.util.ArrayList<>();
        service1Component1Identities.add(createMockIdentityDescriptor("s1c1_1.user", createMockPrincipalDescriptor("s1c1_1@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, "s1c1", null), createMockKeytabDescriptor("s1c1_1.user.keytab", null)));
        service1Component1Identities.add(createMockIdentityDescriptor("s1c1_1.service", createMockPrincipalDescriptor("s1c1_1/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "s1c1", null), createMockKeytabDescriptor("s1c1_1.service.keytab", null)));
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> service1ComponentDescriptorMap = new java.util.HashMap<>();
        service1ComponentDescriptorMap.put("COMPONENT11", createMockComponentDescriptor("COMPONENT11", service1Component1Identities, null));
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> service1Identities = new java.util.ArrayList<>();
        service1Identities.add(createMockIdentityDescriptor("s1_1.user", createMockPrincipalDescriptor("s1_1@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, "s1", null), createMockKeytabDescriptor("s1_1.user.keytab", null)));
        service1Identities.add(createMockIdentityDescriptor("s1_1.service", createMockPrincipalDescriptor("s1/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "s1", null), createMockKeytabDescriptor("s1.service.keytab", null)));
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor service1KerberosDescriptor = createMockServiceDescriptor("SERVICE1", service1ComponentDescriptorMap, service1Identities, false);
        java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> service2Component1Identities = new java.util.ArrayList<>();
        service2Component1Identities.add(createMockIdentityDescriptor("s2_1.user", createMockPrincipalDescriptor("s2_1@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, "s2", null), createMockKeytabDescriptor("s2_1.user.keytab", null)));
        service2Component1Identities.add(createMockIdentityDescriptor("s2c1_1.service", createMockPrincipalDescriptor("s2c1_1/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, "s2c1", null), createMockKeytabDescriptor("s2c1_1.service.keytab", null)));
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> service2ComponentDescriptorMap = new java.util.HashMap<>();
        service2ComponentDescriptorMap.put("COMPONENT21", createMockComponentDescriptor("COMPONENT21", service2Component1Identities, null));
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor service2KerberosDescriptor = createMockServiceDescriptor("SERVICE2", service2ComponentDescriptorMap, null, false);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getProperties()).andReturn(kerberosDescriptorProperties);
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(service1KerberosDescriptor).times(1);
        EasyMock.expect(kerberosDescriptor.getService("SERVICE2")).andReturn(service2KerberosDescriptor).times(1);
        org.easymock.Capture<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> spnegoPrincipalCapture = EasyMock.newCapture(CaptureType.LAST);
        org.easymock.Capture<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> ambariPrincipalCapture = EasyMock.newCapture(CaptureType.LAST);
        java.lang.String spnegoPrincipalNameExpected = java.lang.String.format("HTTP/%s@%s", ambariServerHostname, realm);
        if (createAmbariIdentities) {
            java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> ambarServerComponent1Identities = new java.util.ArrayList<>();
            ambarServerComponent1Identities.add(createMockIdentityDescriptor(org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_KERBEROS_IDENTITY_NAME, createMockPrincipalDescriptor(ambariServerPrincipalName, ambariServerPrincipalType, "ambari", null), createMockKeytabDescriptor(ambariServerKeytabFilePath, null)));
            ambarServerComponent1Identities.add(createMockIdentityDescriptor("spnego", createMockPrincipalDescriptor("HTTP/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, null, null), createMockKeytabDescriptor("spnego.service.keytab", null)));
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor ambariServerComponentKerberosDescriptor = createMockComponentDescriptor(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), ambarServerComponent1Identities, null);
            java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> ambariServerComponentDescriptorMap = new java.util.HashMap<>();
            ambariServerComponentDescriptorMap.put(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), ambariServerComponentKerberosDescriptor);
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor ambariServiceKerberosDescriptor = createMockServiceDescriptor(org.apache.ambari.server.controller.RootService.AMBARI.name(), ambariServerComponentDescriptorMap, null, false);
            EasyMock.expect(ambariServiceKerberosDescriptor.getComponent(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())).andReturn(ambariServerComponentKerberosDescriptor).once();
            EasyMock.expect(kerberosDescriptor.getService(org.apache.ambari.server.controller.RootService.AMBARI.name())).andReturn(ambariServiceKerberosDescriptor).once();
            org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction configureAmbariIdentitiesServerAction = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class);
            EasyMock.expect(configureAmbariIdentitiesServerAction.installAmbariServerIdentity(EasyMock.capture(ambariPrincipalCapture), EasyMock.anyString(), EasyMock.eq(ambariServerKeytabFilePath), EasyMock.eq("user1"), EasyMock.eq("rw"), EasyMock.eq("groupA"), EasyMock.eq("r"), ((org.apache.ambari.server.serveraction.ActionLog) (EasyMock.eq(null))))).andReturn(true).once();
            EasyMock.expect(configureAmbariIdentitiesServerAction.installAmbariServerIdentity(EasyMock.capture(spnegoPrincipalCapture), EasyMock.anyString(), EasyMock.eq("spnego.service.keytab"), EasyMock.eq("user1"), EasyMock.eq("rw"), EasyMock.eq("groupA"), EasyMock.eq("r"), ((org.apache.ambari.server.serveraction.ActionLog) (EasyMock.eq(null))))).andReturn(true).once();
            configureAmbariIdentitiesServerAction.configureJAAS(ambariServerPrincipalNameExpected, ambariServerKeytabFilePath, null);
            EasyMock.expectLastCall().once();
        }
        setupKerberosDescriptor(kerberosDescriptor);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = new java.util.HashMap<>();
        existingConfigurations.put("kerberos-env", propertiesKerberosEnv);
        java.util.Set<java.lang.String> services = new java.util.HashSet<java.lang.String>() {
            {
                add("SERVICE1");
                add("SERVICE2");
            }
        };
        org.easymock.Capture<? extends java.lang.String> capturePrincipal = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<? extends java.lang.String> capturePrincipalForKeytab = EasyMock.newCapture(CaptureType.ALL);
        org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction createPrincipalsServerAction = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.class);
        EasyMock.expect(createPrincipalsServerAction.createPrincipal(EasyMock.capture(capturePrincipal), EasyMock.eq(false), org.easymock.EasyMock.anyObject(), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class), EasyMock.eq(false), EasyMock.isNull(org.apache.ambari.server.serveraction.ActionLog.class))).andReturn(new org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult("anything", "password", 1)).times(3);
        if (createAmbariIdentities) {
            if (ambariServerPrincipalAsService) {
                EasyMock.expect(createPrincipalsServerAction.createPrincipal(EasyMock.capture(capturePrincipal), EasyMock.eq(true), org.easymock.EasyMock.anyObject(), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class), EasyMock.eq(false), EasyMock.isNull(org.apache.ambari.server.serveraction.ActionLog.class))).andReturn(new org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult("anything", "password", 1)).times(2);
            } else {
                EasyMock.expect(createPrincipalsServerAction.createPrincipal(EasyMock.capture(capturePrincipal), EasyMock.eq(true), org.easymock.EasyMock.anyObject(), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class), EasyMock.eq(false), EasyMock.isNull(org.apache.ambari.server.serveraction.ActionLog.class))).andReturn(new org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult("anything", "password", 1)).times(1);
                EasyMock.expect(createPrincipalsServerAction.createPrincipal(EasyMock.capture(capturePrincipal), EasyMock.eq(false), org.easymock.EasyMock.anyObject(), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class), EasyMock.eq(false), EasyMock.isNull(org.apache.ambari.server.serveraction.ActionLog.class))).andReturn(new org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult("anything", "password", 1)).times(1);
            }
        }
        org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction createKeytabFilesServerAction = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.class);
        EasyMock.expect(createKeytabFilesServerAction.createKeytab(EasyMock.capture(capturePrincipalForKeytab), EasyMock.anyObject(), EasyMock.eq("password"), EasyMock.eq(1), EasyMock.anyObject(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler.class), EasyMock.eq(true), EasyMock.eq(true), EasyMock.isNull(org.apache.ambari.server.serveraction.ActionLog.class))).andReturn(new org.apache.directory.server.kerberos.shared.keytab.Keytab()).times(createAmbariIdentities ? 5 : 3);
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        kerberosHelper.ensureHeadlessIdentities(cluster, existingConfigurations, services);
        verifyAll();
        if (createAmbariIdentities) {
            org.junit.Assert.assertEquals(ambariPrincipalCapture.getValue().getPrincipal(), ambariServerPrincipalNameExpected);
            org.junit.Assert.assertEquals(spnegoPrincipalCapture.getValue().getPrincipal(), spnegoPrincipalNameExpected);
        }
        java.util.List<? extends java.lang.String> capturedPrincipals = capturePrincipal.getValues();
        org.junit.Assert.assertEquals(createAmbariIdentities ? 5 : 3, capturedPrincipals.size());
        org.junit.Assert.assertTrue(capturedPrincipals.contains("s1_1@EXAMPLE.COM"));
        org.junit.Assert.assertTrue(capturedPrincipals.contains("s1c1_1@EXAMPLE.COM"));
        org.junit.Assert.assertTrue(capturedPrincipals.contains("s2_1@EXAMPLE.COM"));
        java.util.List<? extends java.lang.String> capturedPrincipalsForKeytab = capturePrincipalForKeytab.getValues();
        org.junit.Assert.assertEquals(createAmbariIdentities ? 5 : 3, capturedPrincipalsForKeytab.size());
        org.junit.Assert.assertTrue(capturedPrincipalsForKeytab.contains("s1_1@EXAMPLE.COM"));
        org.junit.Assert.assertTrue(capturedPrincipalsForKeytab.contains("s1c1_1@EXAMPLE.COM"));
        org.junit.Assert.assertTrue(capturedPrincipalsForKeytab.contains("s2_1@EXAMPLE.COM"));
        if (createAmbariIdentities) {
            java.lang.String spnegoPrincipalName = java.lang.String.format("HTTP/%s@EXAMPLE.COM", ambariServerHostname);
            org.junit.Assert.assertTrue(capturedPrincipals.contains(ambariServerPrincipalNameExpected));
            org.junit.Assert.assertTrue(capturedPrincipals.contains(spnegoPrincipalName));
            org.junit.Assert.assertTrue(capturedPrincipalsForKeytab.contains(ambariServerPrincipalNameExpected));
            org.junit.Assert.assertTrue(capturedPrincipalsForKeytab.contains(spnegoPrincipalName));
        }
    }

    @org.junit.Test
    public void testServiceWithoutComponents() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> propertiesKrb5Conf = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> propertiesKerberosEnv = new java.util.HashMap<>();
        propertiesKerberosEnv.put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "EXAMPLE.COM");
        propertiesKerberosEnv.put(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE, "mit-kdc");
        propertiesKerberosEnv.put(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL, "false");
        org.apache.ambari.server.state.Config configKrb5Conf = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(configKrb5Conf.getProperties()).andReturn(propertiesKrb5Conf).times(1);
        org.apache.ambari.server.state.Config configKerberosEnv = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(configKerberosEnv.getProperties()).andReturn(propertiesKerberosEnv).times(1);
        org.apache.ambari.server.state.Host host1 = createMockHost("host1");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> service1Component1HostMap = new java.util.HashMap<>();
        service1Component1HostMap.put("host1", createMockServiceComponentHost(org.apache.ambari.server.state.State.INSTALLED));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> service1ComponentMap = new java.util.HashMap<>();
        service1ComponentMap.put("COMPONENT11", createMockComponent("COMPONENT11", true, service1Component1HostMap));
        org.apache.ambari.server.state.Service service1 = createMockService("SERVICE1", service1ComponentMap);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> servicesMap = new java.util.HashMap<>();
        servicesMap.put("SERVICE1", service1);
        org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.singletonList(host1), org.apache.ambari.server.state.SecurityType.KERBEROS, configKrb5Conf, configKerberosEnv);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(servicesMap).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> kerberosDescriptorProperties = new java.util.HashMap<>();
        kerberosDescriptorProperties.put("additional_realms", "");
        kerberosDescriptorProperties.put("keytab_dir", "/etc/security/keytabs");
        kerberosDescriptorProperties.put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "${kerberos-env/realm}");
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> service1ComponentDescriptorMap = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> service1Identities = new java.util.ArrayList<>();
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor service1KerberosDescriptor = createMockServiceDescriptor("SERVICE1", service1ComponentDescriptorMap, service1Identities, false);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getProperties()).andReturn(kerberosDescriptorProperties);
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(service1KerberosDescriptor).anyTimes();
        setupKerberosDescriptor(kerberosDescriptor);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = new java.util.HashMap<>();
        existingConfigurations.put("kerberos-env", propertiesKerberosEnv);
        java.util.Set<java.lang.String> services = new java.util.HashSet<java.lang.String>() {
            {
                add("SERVICE1");
            }
        };
        org.easymock.Capture<? extends java.lang.String> capturePrincipal = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<? extends java.lang.String> capturePrincipalForKeytab = EasyMock.newCapture(CaptureType.ALL);
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, new org.apache.ambari.server.security.credential.PrincipalKeyCredential("principal", "password"), org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        kerberosHelper.ensureHeadlessIdentities(cluster, existingConfigurations, services);
        verifyAll();
        java.util.List<? extends java.lang.String> capturedPrincipals = capturePrincipal.getValues();
        org.junit.Assert.assertEquals(0, capturedPrincipals.size());
        java.util.List<? extends java.lang.String> capturedPrincipalsForKeytab = capturePrincipalForKeytab.getValues();
        org.junit.Assert.assertEquals(0, capturedPrincipalsForKeytab.size());
    }

    @org.junit.Test
    public void testFiltersParsing() {
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_HOSTS, "host1,host2,host3");
                put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_COMPONENTS, "SERVICE1:COMPONENT1;COMPONENT2,SERVICE2:COMPONENT1;COMPONENT2;COMPONENT3");
            }
        };
        java.util.Set<java.lang.String> expectedHosts = new java.util.HashSet<>(java.util.Arrays.asList("host1", "host2", "host3"));
        java.util.Set<java.lang.String> hosts = org.apache.ambari.server.controller.KerberosHelperImpl.parseHostFilter(requestProperties);
        org.junit.Assert.assertEquals(expectedHosts, hosts);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> expectedComponents = new java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>>() {
            {
                put("SERVICE1", new java.util.HashSet<java.lang.String>() {
                    {
                        add("COMPONENT1");
                        add("COMPONENT2");
                    }
                });
                put("SERVICE2", new java.util.HashSet<java.lang.String>() {
                    {
                        add("COMPONENT1");
                        add("COMPONENT2");
                        add("COMPONENT3");
                    }
                });
            }
        };
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> components = org.apache.ambari.server.controller.KerberosHelperImpl.parseComponentFilter(requestProperties);
        org.junit.Assert.assertEquals(expectedComponents, components);
    }

    private void setupKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(metaInfo.getKerberosDescriptor("HDP", "2.2", false)).andReturn(kerberosDescriptor).anyTimes();
        EasyMock.expect(kerberosDescriptor.principals()).andReturn(java.util.Collections.<java.lang.String, java.lang.String>emptyMap()).anyTimes();
    }

    private void setupStageFactory() {
        final org.apache.ambari.server.actionmanager.StageFactory stageFactory = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class);
        EasyMock.expect(stageFactory.createNew(EasyMock.anyLong(), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyLong(), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.actionmanager.Stage>() {
            @java.lang.Override
            public org.apache.ambari.server.actionmanager.Stage answer() throws java.lang.Throwable {
                org.apache.ambari.server.actionmanager.Stage stage = createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
                EasyMock.expect(stage.getHostRoleCommands()).andReturn(java.util.Collections.emptyMap()).anyTimes();
                EasyMock.replay(stage);
                return stage;
            }
        }).anyTimes();
    }

    private void testEnsureIdentities(final org.apache.ambari.server.security.credential.PrincipalKeyCredential PrincipalKeyCredential, java.util.Set<java.lang.String> filteredHosts) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClientA = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClientA.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClientA.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(schKerberosClientA.getHostName()).andReturn("hostA").anyTimes();
        EasyMock.expect(schKerberosClientA.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClientB = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClientB.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClientB.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(schKerberosClientB.getHostName()).andReturn("hostB").anyTimes();
        EasyMock.expect(schKerberosClientB.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClientC = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClientC.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClientC.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(schKerberosClientC.getHostName()).andReturn("hostC").anyTimes();
        EasyMock.expect(schKerberosClientC.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1A = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1A.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1A.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        EasyMock.expect(sch1A.getHostName()).andReturn("hostA").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1B = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1B.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1B.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        EasyMock.expect(sch1B.getHostName()).andReturn("hostB").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1C = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1C.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1C.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        EasyMock.expect(sch1C.getHostName()).andReturn("hostC").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2.getServiceName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(sch2.getServiceComponentName()).andReturn("COMPONENT3").anyTimes();
        EasyMock.expect(sch2.getHostName()).andReturn("hostA").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch3 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch3.getServiceName()).andReturn("SERVICE3").anyTimes();
        EasyMock.expect(sch3.getServiceComponentName()).andReturn("COMPONENT3").anyTimes();
        EasyMock.expect(sch3.getHostName()).andReturn("hostA").anyTimes();
        final org.apache.ambari.server.state.Host hostA = createMockHost("hostA");
        final org.apache.ambari.server.state.Host hostB = createMockHost("hostB");
        final org.apache.ambari.server.state.Host hostC = createMockHost("hostC");
        final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                put("hostA", schKerberosClientA);
                put("hostB", schKerberosClientB);
                put("hostC", schKerberosClientC);
            }
        }).anyTimes();
        final org.apache.ambari.server.state.Service serviceKerberos = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service service1 = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service1.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.Service service2 = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service2.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE)).andReturn("mit-kdc").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM)).andReturn("FOOBAR.COM").anyTimes();
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Arrays.asList(hostA, hostB, hostC), org.apache.ambari.server.state.SecurityType.KERBEROS, krb5ConfConfig, kerberosEnvConfig);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                put("SERVICE1", service1);
                put("SERVICE2", service2);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("hostA")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(sch1A);
                add(sch2);
                add(sch3);
                add(schKerberosClientA);
            }
        }).once();
        EasyMock.expect(cluster.getServiceComponentHosts("hostB")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(sch1B);
                add(schKerberosClientB);
            }
        }).once();
        EasyMock.expect(cluster.getServiceComponentHosts("hostC")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(sch1C);
                add(schKerberosClientC);
            }
        }).once();
        EasyMock.expect(cluster.getServiceComponentHosts("KERBEROS", "KERBEROS_CLIENT")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(schKerberosClientA);
                add(schKerberosClientB);
                add(schKerberosClientC);
            }
        }).anyTimes();
        final org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        if ((filteredHosts == null) || filteredHosts.contains("hostA")) {
            EasyMock.expect(clusters.getHost("hostA")).andReturn(hostA).anyTimes();
        }
        if ((filteredHosts == null) || filteredHosts.contains("hostB")) {
            EasyMock.expect(clusters.getHost("hostB")).andReturn(hostB).anyTimes();
        }
        if ((filteredHosts == null) || filteredHosts.contains("hostC")) {
            EasyMock.expect(clusters.getHost("hostC")).andReturn(hostC).anyTimes();
        }
        final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).times(3);
        EasyMock.expect(ambariManagementController.getRoleCommandOrder(cluster)).andReturn(createMock(org.apache.ambari.server.metadata.RoleCommandOrder.class)).once();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1a = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1a.getValue()).andReturn("component1a/_HOST@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor1a.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor1a.getLocalUsername()).andReturn(null).anyTimes();
        EasyMock.expect(principalDescriptor1a.getConfiguration()).andReturn("service1b-site/component1.kerberos.principal").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1b = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1b.getValue()).andReturn("component1b/_HOST@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor1b.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor1b.getLocalUsername()).andReturn(null).anyTimes();
        EasyMock.expect(principalDescriptor1b.getConfiguration()).andReturn("service1b-site/component1.kerberos.principal").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor3.getValue()).andReturn("component3/${host}@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor3.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor3.getLocalUsername()).andReturn(null).anyTimes();
        EasyMock.expect(principalDescriptor3.getConfiguration()).andReturn("service3-site/component3.kerberos.principal").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1a = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor1a.getName()).andReturn("identity1a").anyTimes();
        EasyMock.expect(identityDescriptor1a.getPrincipalDescriptor()).andReturn(principalDescriptor1a).anyTimes();
        EasyMock.expect(identityDescriptor1a.getKeytabDescriptor()).andReturn(keytabDescriptor1).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1b = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor1b.getName()).andReturn("identity1b").anyTimes();
        EasyMock.expect(identityDescriptor1b.getPrincipalDescriptor()).andReturn(principalDescriptor1b).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor3.getName()).andReturn("identity3").anyTimes();
        EasyMock.expect(identityDescriptor3.getPrincipalDescriptor()).andReturn(principalDescriptor3).anyTimes();
        EasyMock.expect(identityDescriptor3.getKeytabDescriptor()).andReturn(keytabDescriptor3).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        if ((filteredHosts == null) || filteredHosts.contains("hostA")) {
            EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).times(1);
            EasyMock.expect(kerberosDescriptor.getService("SERVICE3")).andReturn(serviceDescriptor3).times(1);
        }
        if ((filteredHosts == null) || filteredHosts.contains("hostB")) {
            EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).times(1);
        }
        if ((filteredHosts == null) || filteredHosts.contains("hostC")) {
            EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).times(1);
        }
        setupKerberosDescriptor(kerberosDescriptor);
        setupStageFactory();
        final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createStrictMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponentFilter = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> identityFilter = java.util.Arrays.asList("identity1a", "identity3");
        serviceComponentFilter.put("SERVICE3", java.util.Collections.singleton("COMPONENT3"));
        serviceComponentFilter.put("SERVICE1", null);
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, PrincipalKeyCredential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        kerberosHelper.ensureIdentities(cluster, serviceComponentFilter, filteredHosts, identityFilter, null, requestStageContainer, true);
        verifyAll();
    }

    private void testDeleteIdentities(final org.apache.ambari.server.security.credential.PrincipalKeyCredential PrincipalKeyCredential) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClient.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(schKerberosClient.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        EasyMock.expect(sch1.getHostName()).andReturn("host1").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2.getServiceName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(sch2.getServiceComponentName()).andReturn("COMPONENT3").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch3 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch3.getServiceName()).andReturn("SERVICE3").anyTimes();
        EasyMock.expect(sch3.getServiceComponentName()).andReturn("COMPONENT3").anyTimes();
        EasyMock.expect(sch3.getHostName()).andReturn("host1").anyTimes();
        final org.apache.ambari.server.state.Host host = createMockHost("host1");
        final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(java.util.Collections.singletonMap("host1", schKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service serviceKerberos = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service service1 = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service1.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.Service service2 = createStrictMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service2.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE)).andReturn("mit-kdc").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM)).andReturn("FOOBAR.COM").anyTimes();
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.singleton(host), org.apache.ambari.server.state.SecurityType.KERBEROS, krb5ConfConfig, kerberosEnvConfig);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                put("SERVICE1", service1);
                put("SERVICE2", service2);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("host1")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(sch1);
                add(sch2);
                add(sch3);
                add(schKerberosClient);
            }
        }).once();
        EasyMock.expect(cluster.getServiceComponentHosts("KERBEROS", "KERBEROS_CLIENT")).andReturn(java.util.Collections.singletonList(schKerberosClient)).once();
        final org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getHost("host1")).andReturn(host).once();
        final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).once();
        EasyMock.expect(ambariManagementController.getRoleCommandOrder(cluster)).andReturn(createMock(org.apache.ambari.server.metadata.RoleCommandOrder.class)).once();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1a = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1a.getValue()).andReturn("component1a/_HOST@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor1a.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor1a.getLocalUsername()).andReturn(null).anyTimes();
        EasyMock.expect(principalDescriptor1a.getConfiguration()).andReturn("service1b-site/component1.kerberos.principal").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1b = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1b.getValue()).andReturn("component1b/_HOST@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor1b.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor1b.getLocalUsername()).andReturn(null).anyTimes();
        EasyMock.expect(principalDescriptor1b.getConfiguration()).andReturn("service1b-site/component1.kerberos.principal").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor3.getValue()).andReturn("component3/${host}@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor3.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor3.getLocalUsername()).andReturn(null).anyTimes();
        EasyMock.expect(principalDescriptor3.getConfiguration()).andReturn("service3-site/component3.kerberos.principal").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1a = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor1a.getName()).andReturn("identity1a").anyTimes();
        EasyMock.expect(identityDescriptor1a.getPrincipalDescriptor()).andReturn(principalDescriptor1a).anyTimes();
        EasyMock.expect(identityDescriptor1a.getKeytabDescriptor()).andReturn(keytabDescriptor1).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1b = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor1b.getName()).andReturn("identity1b").anyTimes();
        EasyMock.expect(identityDescriptor1b.getPrincipalDescriptor()).andReturn(principalDescriptor1b).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor3.getName()).andReturn("identity3").anyTimes();
        EasyMock.expect(identityDescriptor3.getPrincipalDescriptor()).andReturn(principalDescriptor3).anyTimes();
        EasyMock.expect(identityDescriptor3.getKeytabDescriptor()).andReturn(keytabDescriptor3).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor3 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).times(1);
        EasyMock.expect(kerberosDescriptor.getService("SERVICE3")).andReturn(serviceDescriptor3).times(1);
        setupKerberosDescriptor(kerberosDescriptor);
        setupStageFactory();
        final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createStrictMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponentFilter = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> identityFilter = java.util.Arrays.asList("identity1a", "identity3");
        serviceComponentFilter.put("SERVICE3", java.util.Collections.singleton("COMPONENT3"));
        serviceComponentFilter.put("SERVICE1", null);
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, PrincipalKeyCredential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        kerberosHelper.deleteIdentities(cluster, serviceComponentFilter, null, identityFilter, requestStageContainer, true);
        verifyAll();
    }

    private void testCreateTestIdentity(final org.apache.ambari.server.security.credential.PrincipalKeyCredential PrincipalKeyCredential, java.lang.Boolean manageIdentities) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class);
        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkp = createNiceMock(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult result = new org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult();
        result.created = true;
        result.kkp = kkp;
        EasyMock.expect(kerberosKeytabPrincipalDAO.findOrCreate(EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(result).anyTimes();
        boolean managingIdentities = !java.lang.Boolean.FALSE.equals(manageIdentities);
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = new java.util.HashMap<>();
        kerberosEnvProperties.put(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE, "mit-kdc");
        kerberosEnvProperties.put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "FOOBAR.COM");
        kerberosEnvProperties.put("manage_identities", manageIdentities == null ? null : manageIdentities ? "true" : "false");
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = new java.util.HashMap<>();
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.Object> attributeMap = new java.util.HashMap<>();
        final org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getDesiredConfigByType("krb5-conf")).andReturn(krb5ConfConfig).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("kerberos-env")).andReturn(kerberosEnvConfig).anyTimes();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createStrictMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        if (managingIdentities) {
            final org.apache.ambari.server.state.Host host = createMockHost("host1");
            EasyMock.expect(host.getHostId()).andReturn(1L).anyTimes();
            EasyMock.expect(cluster.getHosts()).andReturn(java.util.Collections.singleton(host)).anyTimes();
            final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
            EasyMock.expect(schKerberosClient.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
            EasyMock.expect(schKerberosClient.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
            EasyMock.expect(schKerberosClient.getHostName()).andReturn("host1").anyTimes();
            EasyMock.expect(schKerberosClient.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
            EasyMock.expect(schKerberosClient.getHost()).andReturn(host).anyTimes();
            final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
            EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
            EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(java.util.Collections.singletonMap("host1", schKerberosClient)).anyTimes();
            final org.apache.ambari.server.state.Service serviceKerberos = createNiceMock(org.apache.ambari.server.state.Service.class);
            EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
            EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
            EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
            final org.apache.ambari.server.state.Service service1 = createNiceMock(org.apache.ambari.server.state.Service.class);
            EasyMock.expect(service1.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
            EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
            EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
            final org.apache.ambari.server.state.Service service2 = createNiceMock(org.apache.ambari.server.state.Service.class);
            EasyMock.expect(service2.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
            EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
            EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
            EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
            EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
                {
                    put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                    put("SERVICE1", service1);
                    put("SERVICE2", service2);
                }
            }).anyTimes();
            EasyMock.expect(cluster.getServiceComponentHosts(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), org.apache.ambari.server.Role.KERBEROS_CLIENT.name())).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
                {
                    add(schKerberosClient);
                }
            }).anyTimes();
            EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2")).anyTimes();
            EasyMock.expect(cluster.getSessionAttributes()).andReturn(attributeMap).anyTimes();
            cluster.setSessionAttribute(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject());
            EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
                @java.lang.Override
                public java.lang.Object answer() throws java.lang.Throwable {
                    java.lang.Object[] args = EasyMock.getCurrentArguments();
                    attributeMap.put(((java.lang.String) (args[0])), args[1]);
                    return null;
                }
            }).anyTimes();
            final org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
            EasyMock.expect(clusters.getHost("host1")).andReturn(host).anyTimes();
            final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
            EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).once();
            EasyMock.expect(ambariManagementController.getRoleCommandOrder(cluster)).andReturn(createMock(org.apache.ambari.server.metadata.RoleCommandOrder.class)).once();
            final org.apache.ambari.server.state.ConfigHelper configHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
            EasyMock.expect(configHelper.calculateExistingConfigurations(EasyMock.eq(ambariManagementController), EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
                {
                    put("cluster-env", new java.util.HashMap<java.lang.String, java.lang.String>() {
                        {
                            put("kerberos_domain", "FOOBAR.COM");
                        }
                    });
                }
            }).times(1);
            final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
            EasyMock.expect(kerberosDescriptor.getProperties()).andReturn(null).once();
            setupKerberosDescriptor(kerberosDescriptor);
            setupStageFactory();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
            EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
            EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
            requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
            EasyMock.expectLastCall().once();
        }
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.Map<java.lang.String, java.lang.String> commandParamsStage = new java.util.HashMap<>();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, PrincipalKeyCredential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        kerberosHelper.createTestIdentity(cluster, commandParamsStage, requestStageContainer);
        verifyAll();
        if (managingIdentities) {
            org.junit.Assert.assertTrue(commandParamsStage.containsKey("principal_name"));
            org.junit.Assert.assertEquals("${kerberos-env/service_check_principal_name}@${realm}", commandParamsStage.get("principal_name"));
            org.junit.Assert.assertTrue(commandParamsStage.containsKey("keytab_file"));
            org.junit.Assert.assertEquals(("${keytab_dir}/kerberos.service_check." + new java.text.SimpleDateFormat("MMddyy").format(new java.util.Date())) + ".keytab", commandParamsStage.get("keytab_file"));
        }
    }

    private void testDeleteTestIdentity(final org.apache.ambari.server.security.credential.PrincipalKeyCredential PrincipalKeyCredential) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.class);
        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkp = createNiceMock(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult result = new org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult();
        result.created = true;
        result.kkp = kkp;
        EasyMock.expect(kerberosKeytabPrincipalDAO.findOrCreate(EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(result).anyTimes();
        org.apache.ambari.server.state.Host host1 = createMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host1.getHostId()).andReturn(1L).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClient.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(schKerberosClient.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(schKerberosClient.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        EasyMock.expect(schKerberosClient.getHost()).andReturn(host1).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        EasyMock.expect(sch1.getHostName()).andReturn("host1").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2 = createStrictMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2.getServiceName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(sch2.getServiceComponentName()).andReturn("COMPONENT3").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch3 = createStrictMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch3.getServiceName()).andReturn("SERVICE3").anyTimes();
        EasyMock.expect(sch3.getServiceComponentName()).andReturn("COMPONENT3").anyTimes();
        EasyMock.expect(sch3.getHostName()).andReturn("host1").anyTimes();
        final org.apache.ambari.server.state.Host host = createMockHost("host1");
        final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(java.util.Collections.singletonMap("host1", schKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service serviceKerberos = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service service1 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service1.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.Service service2 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service2.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = createMock(java.util.Map.class);
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE)).andReturn("mit-kdc").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM)).andReturn("FOOBAR.COM").anyTimes();
        EasyMock.expect(kerberosEnvProperties.get("manage_identities")).andReturn(null).anyTimes();
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        final org.apache.ambari.server.state.Cluster cluster = createMockCluster("c1", java.util.Collections.singleton(host), org.apache.ambari.server.state.SecurityType.KERBEROS, krb5ConfConfig, kerberosEnvConfig);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                put("SERVICE1", service1);
                put("SERVICE2", service2);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), org.apache.ambari.server.Role.KERBEROS_CLIENT.name())).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(schKerberosClient);
            }
        }).anyTimes();
        final org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getHost("host1")).andReturn(host).once();
        final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).once();
        EasyMock.expect(ambariManagementController.getRoleCommandOrder(cluster)).andReturn(createMock(org.apache.ambari.server.metadata.RoleCommandOrder.class)).once();
        final org.apache.ambari.server.state.ConfigHelper configHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelper.calculateExistingConfigurations(EasyMock.eq(ambariManagementController), EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("cluster-env", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("kerberos_domain", "FOOBAR.COM");
                    }
                });
            }
        }).times(1);
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createStrictMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getProperties()).andReturn(null).once();
        setupKerberosDescriptor(kerberosDescriptor);
        setupStageFactory();
        final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createStrictMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.expect(requestStageContainer.getLastStageId()).andReturn(-1L).anyTimes();
        EasyMock.expect(requestStageContainer.getId()).andReturn(1L).once();
        requestStageContainer.addStages(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.Map<java.lang.String, java.lang.String> commandParamsStage = new java.util.HashMap<>();
        commandParamsStage.put("principal_name", "${cluster-env/smokeuser}@${realm}");
        commandParamsStage.put("keytab_file", "${keytab_dir}/kerberos.service_check.keytab");
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        credentialStoreService.setCredential(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, PrincipalKeyCredential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        kerberosHelper.deleteTestIdentity(cluster, commandParamsStage, requestStageContainer);
        verifyAll();
    }

    private java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> testGetActiveIdentities(java.lang.String clusterName, java.lang.String hostName, java.lang.String serviceName, java.lang.String componentName, boolean replaceHostNames, org.apache.ambari.server.state.SecurityType clusterSecurityType) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient1 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClient1.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClient1.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost schKerberosClient2 = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(schKerberosClient2.getServiceName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(schKerberosClient2.getServiceComponentName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1a = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1a.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch1a.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch1b = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1b.getServiceName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(sch1b.getServiceComponentName()).andReturn("COMPONENT2").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2a = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2a.getServiceName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(sch2a.getServiceComponentName()).andReturn("COMPONENT1").anyTimes();
        final org.apache.ambari.server.state.ServiceComponentHost sch2b = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch2b.getServiceName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(sch2b.getServiceComponentName()).andReturn("COMPONENT2").anyTimes();
        final org.apache.ambari.server.state.Host host1 = createMockHost("host1");
        final org.apache.ambari.server.state.Host host2 = createMockHost("host2");
        final org.apache.ambari.server.state.ServiceComponent serviceComponentKerberosClient = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponentKerberosClient.getName()).andReturn(org.apache.ambari.server.Role.KERBEROS_CLIENT.name()).anyTimes();
        EasyMock.expect(serviceComponentKerberosClient.getServiceComponentHosts()).andReturn(java.util.Collections.singletonMap("host1", schKerberosClient1)).anyTimes();
        final org.apache.ambari.server.state.Service serviceKerberos = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceKerberos.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(serviceKerberos.getName()).andReturn(org.apache.ambari.server.state.Service.Type.KERBEROS.name()).anyTimes();
        EasyMock.expect(serviceKerberos.getServiceComponents()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), serviceComponentKerberosClient)).anyTimes();
        final org.apache.ambari.server.state.Service service1 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service1.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service1.getName()).andReturn("SERVICE1").anyTimes();
        EasyMock.expect(service1.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.Service service2 = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service2.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service2.getName()).andReturn("SERVICE2").anyTimes();
        EasyMock.expect(service2.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostMap = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Host>() {
            {
                put("host1", host1);
                put("host2", host2);
            }
        };
        final java.util.Collection<org.apache.ambari.server.state.Host> hosts = hostMap.values();
        final org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getSecurityType()).andReturn(clusterSecurityType).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn(clusterName).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType(EasyMock.anyString(), EasyMock.anyObject())).andReturn(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class)).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("host1")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(schKerberosClient1);
                add(sch1a);
                add(sch1b);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("host2")).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost>() {
            {
                add(schKerberosClient2);
                add(sch2a);
                add(sch2b);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts(java.net.InetAddress.getLocalHost().getCanonicalHostName().toLowerCase())).andReturn(new java.util.ArrayList<>()).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE, "mit-kdc");
                put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "FOOBAR.COM");
                put("case_insensitive_username_rules", "false");
                put(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL, "false");
            }
        };
        final org.apache.ambari.server.state.Config kerberosEnvConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnvConfig.getProperties()).andReturn(kerberosEnvProperties).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = createMock(java.util.Map.class);
        final org.apache.ambari.server.state.Config krb5ConfConfig = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(krb5ConfConfig.getProperties()).andReturn(krb5ConfProperties).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("krb5-conf")).andReturn(krb5ConfConfig).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("kerberos-env")).andReturn(kerberosEnvConfig).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), serviceKerberos);
                put("SERVICE1", service1);
                put("SERVICE2", service2);
            }
        }).anyTimes();
        EasyMock.expect(cluster.getHosts()).andReturn(hosts).anyTimes();
        final org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster(clusterName)).andReturn(cluster).times(1);
        if (hostName == null) {
            EasyMock.expect(clusters.getHostsForCluster(clusterName)).andReturn(hostMap).once();
        }
        final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, "host1", null)).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, "host2", null)).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null)).andReturn(java.util.Collections.emptyMap()).anyTimes();
        final org.apache.ambari.server.state.ConfigHelper configHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelper.calculateExistingConfigurations(EasyMock.eq(ambariManagementController), EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(), EasyMock.anyObject())).andReturn(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("cluster-env", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("kerberos_domain", "FOOBAR.COM");
                    }
                });
            }
        }).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor1.getValue()).andReturn("service1/_HOST@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor1.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor1.getConfiguration()).andReturn("service1-site/component1.kerberos.principal").anyTimes();
        EasyMock.expect(principalDescriptor1.getLocalUsername()).andReturn("service1").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptor2.getValue()).andReturn("component2/${host}@${realm}").anyTimes();
        EasyMock.expect(principalDescriptor2.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptor2.getConfiguration()).andReturn("service2-site/component2.kerberos.principal").anyTimes();
        EasyMock.expect(principalDescriptor2.getLocalUsername()).andReturn("service2").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptorService1 = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(principalDescriptorService1.getValue()).andReturn("service1/_HOST@${realm}").anyTimes();
        EasyMock.expect(principalDescriptorService1.getType()).andReturn(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE).anyTimes();
        EasyMock.expect(principalDescriptorService1.getConfiguration()).andReturn("service1-site/service1.kerberos.principal").anyTimes();
        EasyMock.expect(principalDescriptorService1.getLocalUsername()).andReturn("service1").anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        EasyMock.expect(keytabDescriptor1.getFile()).andReturn("${keytab_dir}/service1.keytab").anyTimes();
        EasyMock.expect(keytabDescriptor1.getOwnerName()).andReturn("service1").anyTimes();
        EasyMock.expect(keytabDescriptor1.getOwnerAccess()).andReturn("rw").anyTimes();
        EasyMock.expect(keytabDescriptor1.getGroupName()).andReturn("hadoop").anyTimes();
        EasyMock.expect(keytabDescriptor1.getGroupAccess()).andReturn("").anyTimes();
        EasyMock.expect(keytabDescriptor1.getConfiguration()).andReturn("service1-site/component1.keytab.file").anyTimes();
        EasyMock.expect(keytabDescriptor1.isCachable()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        EasyMock.expect(keytabDescriptor2.getFile()).andReturn("${keytab_dir}/service2.keytab").anyTimes();
        EasyMock.expect(keytabDescriptor2.getOwnerName()).andReturn("service2").anyTimes();
        EasyMock.expect(keytabDescriptor2.getOwnerAccess()).andReturn("rw").anyTimes();
        EasyMock.expect(keytabDescriptor2.getGroupName()).andReturn("hadoop").anyTimes();
        EasyMock.expect(keytabDescriptor2.getGroupAccess()).andReturn("").anyTimes();
        EasyMock.expect(keytabDescriptor2.getConfiguration()).andReturn("service2-site/component2.keytab.file").anyTimes();
        EasyMock.expect(keytabDescriptor2.isCachable()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptorService1 = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        EasyMock.expect(keytabDescriptorService1.getFile()).andReturn("${keytab_dir}/service1.service.keytab").anyTimes();
        EasyMock.expect(keytabDescriptorService1.getOwnerName()).andReturn("service1").anyTimes();
        EasyMock.expect(keytabDescriptorService1.getOwnerAccess()).andReturn("rw").anyTimes();
        EasyMock.expect(keytabDescriptorService1.getGroupName()).andReturn("hadoop").anyTimes();
        EasyMock.expect(keytabDescriptorService1.getGroupAccess()).andReturn("").anyTimes();
        EasyMock.expect(keytabDescriptorService1.getConfiguration()).andReturn("service1-site/service1.keytab.file").anyTimes();
        EasyMock.expect(keytabDescriptorService1.isCachable()).andReturn(false).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor1.getName()).andReturn("identity1").anyTimes();
        EasyMock.expect(identityDescriptor1.getReference()).andReturn(null).anyTimes();
        EasyMock.expect(identityDescriptor1.getPrincipalDescriptor()).andReturn(principalDescriptor1).anyTimes();
        EasyMock.expect(identityDescriptor1.getKeytabDescriptor()).andReturn(keytabDescriptor1).anyTimes();
        EasyMock.expect(identityDescriptor1.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        EasyMock.expect(identityDescriptor1.getWhen()).andReturn(null).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptor2.getName()).andReturn("identity2").anyTimes();
        EasyMock.expect(identityDescriptor2.getReference()).andReturn(null).anyTimes();
        EasyMock.expect(identityDescriptor2.getPrincipalDescriptor()).andReturn(principalDescriptor2).anyTimes();
        EasyMock.expect(identityDescriptor2.getKeytabDescriptor()).andReturn(keytabDescriptor2).anyTimes();
        EasyMock.expect(identityDescriptor2.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        EasyMock.expect(identityDescriptor2.getWhen()).andReturn(null).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptorService1 = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(identityDescriptorService1.getName()).andReturn("identity3").anyTimes();
        EasyMock.expect(identityDescriptorService1.getReference()).andReturn(null).anyTimes();
        EasyMock.expect(identityDescriptorService1.getPrincipalDescriptor()).andReturn(principalDescriptorService1).anyTimes();
        EasyMock.expect(identityDescriptorService1.getKeytabDescriptor()).andReturn(keytabDescriptorService1).anyTimes();
        EasyMock.expect(identityDescriptorService1.shouldInclude(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        EasyMock.expect(identityDescriptorService1.getWhen()).andReturn(null).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.class);
        EasyMock.expect(componentDescriptor1.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singletonList(identityDescriptor1)).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.class);
        EasyMock.expect(componentDescriptor2.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singletonList(identityDescriptor2)).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor1 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        EasyMock.expect(serviceDescriptor1.getComponent("COMPONENT1")).andReturn(componentDescriptor1).anyTimes();
        EasyMock.expect(serviceDescriptor1.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singletonList(identityDescriptorService1)).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor2 = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        EasyMock.expect(serviceDescriptor2.getComponent("COMPONENT2")).andReturn(componentDescriptor2).anyTimes();
        EasyMock.expect(serviceDescriptor2.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(null).anyTimes();
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptor.getProperties()).andReturn(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM, "EXAMPLE.COM");
            }
        }).anyTimes();
        EasyMock.expect(kerberosDescriptor.getService("KERBEROS")).andReturn(null).anyTimes();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE1")).andReturn(serviceDescriptor1).anyTimes();
        EasyMock.expect(kerberosDescriptor.getService("SERVICE2")).andReturn(serviceDescriptor2).anyTimes();
        EasyMock.expect(kerberosDescriptor.getService("AMBARI")).andReturn(null).anyTimes();
        setupKerberosDescriptor(kerberosDescriptor);
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> identities;
        identities = kerberosHelper.getActiveIdentities(clusterName, hostName, serviceName, componentName, replaceHostNames, null, null, null);
        verifyAll();
        return identities;
    }

    private void addAmbariServerIdentity(java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties) throws java.lang.Exception {
        org.apache.ambari.server.controller.KerberosHelperImpl kerberosHelper = org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.controller.KerberosHelperImpl.class);
        boolean createAmbariIdentities = kerberosHelper.createAmbariIdentities(kerberosEnvProperties);
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor ambariKerberosIdentity = null;
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        if (createAmbariIdentities) {
            java.lang.String ambariServerPrincipalName = "ambari-server${principal_suffix}@${realm}";
            org.apache.ambari.server.state.kerberos.KerberosPrincipalType ambariServerPrincipalType = org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER;
            java.lang.String ambariServerKeytabFilePath = new java.io.File("ambari.server.keytab").getAbsolutePath();
            ambariKerberosIdentity = createMockIdentityDescriptor(org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_KERBEROS_IDENTITY_NAME, createMockPrincipalDescriptor(ambariServerPrincipalName, ambariServerPrincipalType, "ambari", null), createMockKeytabDescriptor(ambariServerKeytabFilePath, null));
            java.util.ArrayList<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> ambarServerComponent1Identities = new java.util.ArrayList<>();
            ambarServerComponent1Identities.add(ambariKerberosIdentity);
            ambarServerComponent1Identities.add(createMockIdentityDescriptor("ambari-server_spnego", createMockPrincipalDescriptor("HTTP/_HOST@${realm}", org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, null, null), createMockKeytabDescriptor("spnego.service.keytab", null)));
            java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> ambariServerComponentDescriptorMap = new java.util.HashMap<>();
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescrptor = createMockComponentDescriptor(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), ambarServerComponent1Identities, null);
            ambariServerComponentDescriptorMap.put(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), componentDescrptor);
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor ambariServiceKerberosDescriptor = createMockServiceDescriptor(org.apache.ambari.server.controller.RootService.AMBARI.name(), ambariServerComponentDescriptorMap, null, false);
            EasyMock.expect(ambariServiceKerberosDescriptor.getComponent(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())).andReturn(componentDescrptor).once();
            EasyMock.expect(kerberosDescriptor.getService(org.apache.ambari.server.controller.RootService.AMBARI.name())).andReturn(ambariServiceKerberosDescriptor).once();
        }
        replayAll();
        org.apache.ambari.server.controller.KerberosHelperTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = (createAmbariIdentities) ? kerberosHelper.getAmbariServerIdentities(kerberosDescriptor) : new java.util.ArrayList<>();
        verifyAll();
        if (createAmbariIdentities) {
            org.junit.Assert.assertEquals(2, identities.size());
            org.junit.Assert.assertSame(ambariKerberosIdentity, identities.get(0));
        } else {
            org.junit.Assert.assertTrue(identities.isEmpty());
        }
    }

    private org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor createMockConfigurationDescriptor(java.util.Map<java.lang.String, java.lang.String> properties) {
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor descriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor.class);
        EasyMock.expect(descriptor.getProperties()).andReturn(properties).anyTimes();
        return descriptor;
    }

    private org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor createMockKeytabDescriptor(java.lang.String file, java.lang.String configuration) {
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor descriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.class);
        EasyMock.expect(descriptor.getFile()).andReturn(file).anyTimes();
        EasyMock.expect(descriptor.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(descriptor.getOwnerName()).andReturn("user1").anyTimes();
        EasyMock.expect(descriptor.getOwnerAccess()).andReturn("rw").anyTimes();
        EasyMock.expect(descriptor.getGroupName()).andReturn("groupA").anyTimes();
        EasyMock.expect(descriptor.getGroupAccess()).andReturn("r").anyTimes();
        return descriptor;
    }

    private org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor createMockPrincipalDescriptor(java.lang.String value, org.apache.ambari.server.state.kerberos.KerberosPrincipalType type, java.lang.String localUsername, java.lang.String configuration) {
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor descriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.class);
        EasyMock.expect(descriptor.getValue()).andReturn(value).anyTimes();
        EasyMock.expect(descriptor.getType()).andReturn(type).anyTimes();
        EasyMock.expect(descriptor.getLocalUsername()).andReturn(localUsername).anyTimes();
        EasyMock.expect(descriptor.getConfiguration()).andReturn(configuration).anyTimes();
        return descriptor;
    }

    private org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor createMockServiceDescriptor(java.lang.String serviceName, java.util.HashMap<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentMap, java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, boolean shouldPreconfigure) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor descriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.class);
        EasyMock.expect(descriptor.getName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(descriptor.getComponents()).andReturn(componentMap).anyTimes();
        EasyMock.expect(descriptor.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(identities).anyTimes();
        EasyMock.expect(descriptor.getAuthToLocalProperties()).andReturn(null).anyTimes();
        EasyMock.expect(descriptor.shouldPreconfigure()).andReturn(shouldPreconfigure).anyTimes();
        return descriptor;
    }

    private org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor createMockIdentityDescriptor(java.lang.String name, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor) {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor descriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.class);
        EasyMock.expect(descriptor.getName()).andReturn(name).anyTimes();
        EasyMock.expect(descriptor.getPrincipalDescriptor()).andReturn(principalDescriptor).anyTimes();
        EasyMock.expect(descriptor.getKeytabDescriptor()).andReturn(keytabDescriptor).anyTimes();
        return descriptor;
    }

    private org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor createMockComponentDescriptor(java.lang.String componentName, java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor descriptor = createMock(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.class);
        EasyMock.expect(descriptor.getName()).andReturn(componentName).anyTimes();
        EasyMock.expect(descriptor.getIdentities(EasyMock.eq(true), org.easymock.EasyMock.anyObject())).andReturn(identities).anyTimes();
        EasyMock.expect(descriptor.getConfigurations(true)).andReturn(configurations).anyTimes();
        EasyMock.expect(descriptor.getAuthToLocalProperties()).andReturn(null).anyTimes();
        return descriptor;
    }

    private org.apache.ambari.server.state.ServiceComponentHost createMockServiceComponentHost(org.apache.ambari.server.state.State state) {
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(serviceComponentHost.getDesiredState()).andReturn(state).anyTimes();
        return serviceComponentHost;
    }

    private org.apache.ambari.server.state.ServiceComponent createMockComponent(java.lang.String componentName, boolean isMasterComponent, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts) {
        org.apache.ambari.server.state.ServiceComponent component = createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(component.getName()).andReturn(componentName).anyTimes();
        EasyMock.expect(component.isMasterComponent()).andReturn(isMasterComponent).anyTimes();
        EasyMock.expect(component.isClientComponent()).andReturn(!isMasterComponent).anyTimes();
        EasyMock.expect(component.getServiceComponentHosts()).andReturn(hosts).anyTimes();
        return component;
    }

    private org.apache.ambari.server.state.Service createMockService(java.lang.String serviceName, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> componentMap) {
        org.apache.ambari.server.state.Service service = createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
        EasyMock.expect(service.getName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(service.getServiceComponents()).andReturn(componentMap).anyTimes();
        return service;
    }

    private org.apache.ambari.server.state.Host createMockHost(java.lang.String hostname) {
        org.apache.ambari.server.state.Host host = createMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host.getHostName()).andReturn(hostname).anyTimes();
        EasyMock.expect(host.getState()).andReturn(org.apache.ambari.server.state.HostState.HEALTHY).anyTimes();
        EasyMock.expect(host.getCurrentPingPort()).andReturn(1).anyTimes();
        EasyMock.expect(host.getRackInfo()).andReturn("rack1").anyTimes();
        EasyMock.expect(host.getIPv4()).andReturn("1.2.3.4").anyTimes();
        return host;
    }

    private org.apache.ambari.server.state.Cluster createMockCluster(java.lang.String clusterName, java.util.Collection<org.apache.ambari.server.state.Host> hosts, org.apache.ambari.server.state.SecurityType securityType, org.apache.ambari.server.state.Config krb5ConfConfig, org.apache.ambari.server.state.Config kerberosEnvConfig) {
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getHosts()).andReturn(hosts).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(cluster.getSecurityType()).andReturn(securityType).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("krb5-conf")).andReturn(krb5ConfConfig).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("kerberos-env")).andReturn(kerberosEnvConfig).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn(clusterName).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2")).anyTimes();
        return cluster;
    }

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor createKerberosDescriptor() throws org.apache.ambari.server.AmbariException {
        java.lang.String json = "{" + ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("  \"services\": [" + "    {") + "      \"name\": \"EXISTING_SERVICE\",") + "      \"components\": [") + "        {") + "          \"name\": \"EXISTING_SERVICE_MASTER\",") + "          \"identities\": [") + "            {") + "              \"name\": \"existing_service_principal\",") + "              \"principal\": {") + "                \"value\": \"${existing-service-env/service_user}/_HOST@${realm}\",") + "                \"type\": \"service\",") + "                \"configuration\": \"existing-service-env/service_principal_name\",") + "                \"local_username\": \"${existing-service-env/service_user}\"") + "              },") + "              \"keytab\": {") + "                \"file\": \"${keytab_dir}/existing_service.service.keytab\",") + "                \"owner\": {") + "                  \"name\": \"${existing-service-env/service_user}\",") + "                  \"access\": \"r\"") + "                },") + "                \"group\": {") + "                  \"name\": \"${cluster-env/user_group}\",") + "                  \"access\": \"\"") + "                },") + "                \"configuration\": \"existing-service-env/service_keytab_path\"") + "              }") + "            }") + "          ],") + "          \"configurations\": [") + "            {") + "              \"existing-service-site\": {") + "                \"kerberos.secured\": \"true\"") + "              }") + "            },") + "            {") + "              \"core-site\": {") + "                \"hadoop.proxyuser.${existing-service-env/service_user}.groups\": \"${hadoop-env/proxyuser_group}\",") + "                \"hadoop.proxyuser.${existing-service-env/service_user}.hosts\": \"${clusterHostInfo/existing_service_master_hosts}\"") + "              }") + "            }") + "          ]") + "        }") + "      ]") + "    },") + "    {") + "      \"name\": \"PRECONFIGURE_SERVICE\",") + "      \"preconfigure\": true,") + "      \"components\": [") + "        {") + "          \"name\": \"PRECONFIGURE_SERVICE_MASTER\",") + "          \"identities\": [") + "            {") + "              \"name\": \"preconfigure_service_principal\",") + "              \"principal\": {") + "                \"value\": \"${preconfigure-service-env/service_user}/_HOST@${realm}\",") + "                \"type\": \"service\",") + "                \"configuration\": \"preconfigure-service-env/service_principal_name\",") + "                \"local_username\": \"${preconfigure-service-env/service_user}\"") + "              },") + "              \"keytab\": {") + "                \"file\": \"${keytab_dir}/preconfigure_service.service.keytab\",") + "                \"owner\": {") + "                  \"name\": \"${preconfigure-service-env/service_user}\",") + "                  \"access\": \"r\"") + "                },") + "                \"group\": {") + "                  \"name\": \"${cluster-env/user_group}\",") + "                  \"access\": \"\"") + "                },") + "                \"configuration\": \"preconfigure-service-env/service_keytab_path\"") + "              }") + "            }") + "          ],") + "          \"configurations\": [") + "            {") + "              \"preconfigure-service-site\": {") + "                \"kerberos.secured\": \"true\"") + "              }") + "            },") + "            {") + "              \"core-site\": {") + "                \"hadoop.proxyuser.${preconfigure-service-env/service_user}.groups\": \"${hadoop-env/proxyuser_group}\",") + "                \"hadoop.proxyuser.${preconfigure-service-env/service_user}.hosts\": \"${clusterHostInfo/preconfigure_service_master_hosts}\"") + "              }") + "            }") + "          ]") + "        }") + "      ]") + "    }") + "  ]") + "}");
        return new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(json);
    }
}