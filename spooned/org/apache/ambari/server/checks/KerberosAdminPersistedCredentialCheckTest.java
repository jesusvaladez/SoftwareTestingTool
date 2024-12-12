package org.apache.ambari.server.checks;
import javax.persistence.EntityManager;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class KerberosAdminPersistedCredentialCheckTest extends org.easymock.EasyMockSupport {
    @org.easymock.Mock
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper;

    @org.junit.Test
    public void testMissingCredentialStoreKerberosEnabledManagingIdentities() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(true, true, false, false);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
        org.junit.Assert.assertTrue(result.getFailReason().startsWith("Ambari's credential store has not been configured."));
    }

    @org.junit.Test
    public void testMissingCredentialStoreKerberosEnabledNotManagingIdentities() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(true, false, false, false);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testMissingCredentialStoreKerberosNotEnabled() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(false, false, false, false);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testMissingCredentialKerberosEnabledManagingIdentities() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(true, true, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
        org.junit.Assert.assertTrue(result.getFailReason().startsWith("The KDC administrator credential has not been stored in the persisted credential store."));
    }

    @org.junit.Test
    public void testMissingCredentialKerberosEnabledNotManagingIdentities() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(true, false, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testMissingCredentialKerberosNotEnabled() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(false, true, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testCredentialsSetKerberosNotEnabled() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(false, false, true, true);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testCredentialsSetKerberosEnabledNotManagingIdentities() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(true, false, true, true);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testCredentialsSetKerberosEnabledManagingIdentities() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = executeCheck(true, true, true, true);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    private org.apache.ambari.spi.upgrade.UpgradeCheckResult executeCheck(boolean kerberosEnabled, boolean manageIdentities, boolean credentialStoreInitialized, boolean credentialSet) throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = createNiceMock(org.apache.ambari.spi.RepositoryVersion.class);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(clusterName, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, checkProperties, null);
        EasyMock.expect(upgradeHelper.suggestUpgradePack(EasyMock.eq(clusterName), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.eq(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE), EasyMock.eq(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING), EasyMock.anyObject())).andReturn(upgradePackWithRegenKeytab()).anyTimes();
        org.apache.ambari.server.state.DesiredConfig desiredKerberosEnv = createMock(org.apache.ambari.server.state.DesiredConfig.class);
        EasyMock.expect(desiredKerberosEnv.getTag()).andReturn("tag").anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = java.util.Collections.singletonMap("kerberos-env", desiredKerberosEnv);
        org.apache.ambari.server.state.Config kerberosEnv = createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kerberosEnv.getProperties()).andReturn(java.util.Collections.singletonMap("manage_identities", manageIdentities ? "true" : "false")).anyTimes();
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getSecurityType()).andReturn(kerberosEnabled ? org.apache.ambari.server.state.SecurityType.KERBEROS : org.apache.ambari.server.state.SecurityType.NONE).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(desiredConfigs).anyTimes();
        EasyMock.expect(cluster.getConfig("kerberos-env", "tag")).andReturn(kerberosEnv).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(createNiceMock(org.apache.ambari.server.state.StackId.class)).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster(clusterName)).andReturn(cluster).anyTimes();
        org.apache.ambari.server.security.credential.Credential credential = createMock(org.apache.ambari.server.security.credential.Credential.class);
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreProvider = injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        EasyMock.expect(credentialStoreProvider.isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).andReturn(credentialStoreInitialized).anyTimes();
        EasyMock.expect(credentialStoreProvider.getCredential(clusterName, org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)).andReturn(credentialSet ? credential : null).anyTimes();
        com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clustersProvider = () -> clusters;
        replayAll();
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class).init();
        org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck check = new org.apache.ambari.server.checks.KerberosAdminPersistedCredentialCheck();
        injector.injectMembers(check);
        check.clustersProvider = clustersProvider;
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = check.perform(request);
        verifyAll();
        return result;
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack upgradePackWithRegenKeytab() {
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        EasyMock.expect(upgradePack.anyGroupTaskMatch(EasyMock.anyObject())).andReturn(true).anyTimes();
        return upgradePack;
    }

    com.google.inject.Injector getInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addActionDBAccessorConfigsBindings().addFactoriesInstallBinding().addPasswordEncryptorBindings().addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(createNiceMock(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class));
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class));
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createNiceMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                bind(org.apache.ambari.server.topology.PersistedState.class).to(org.apache.ambari.server.topology.PersistedStateImpl.class);
                bind(org.apache.ambari.server.security.SecurityHelper.class).toInstance(createNiceMock(org.apache.ambari.server.security.SecurityHelper.class));
                bind(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class));
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.orm.dao.ArtifactDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.ArtifactDAO.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class).toInstance(upgradeHelper);
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
            }
        });
    }
}