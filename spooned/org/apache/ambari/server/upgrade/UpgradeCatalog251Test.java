package org.apache.ambari.server.upgrade;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class UpgradeCatalog251Test {
    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.persistence.EntityManager entityManager;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.configuration.Configuration configuration;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private java.sql.Connection connection;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private java.sql.Statement statement;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private java.sql.ResultSet resultSet;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.stack.OsFamily osFamily;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Config config;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.state.Service service;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.agent.stomp.MetadataHolder metadataHolder;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private com.google.inject.Injector injector;

    @org.junit.Before
    public void init() {
        EasyMock.reset(entityManagerProvider, injector);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).anyTimes();
        EasyMock.expect(injector.getInstance(com.google.gson.Gson.class)).andReturn(null).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.MaintenanceStateHelper.class)).andReturn(null).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class)).andReturn(kerberosHelper).anyTimes();
        EasyMock.replay(entityManagerProvider, injector);
    }

    @org.junit.After
    public void tearDown() {
    }

    @org.junit.Test
    public void testExecuteDDLUpdates() throws java.lang.Exception {
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> hrcBackgroundColumnCapture = EasyMock.newCapture();
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog251.HOST_ROLE_COMMAND_TABLE), EasyMock.capture(hrcBackgroundColumnCapture));
        EasyMock.expect(dbAccessor.getConnection()).andReturn(connection).anyTimes();
        EasyMock.expect(connection.createStatement()).andReturn(statement).anyTimes();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).anyTimes();
        EasyMock.expect(configuration.getDatabaseType()).andReturn(org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES).anyTimes();
        EasyMock.replay(dbAccessor, configuration, connection, statement, resultSet);
        com.google.inject.Module module = new com.google.inject.Module() {
            @java.lang.Override
            public void configure(com.google.inject.Binder binder) {
                binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(osFamily);
                binder.bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                binder.bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            }
        };
        com.google.inject.Injector injector = getInjector(EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class), EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class));
        org.apache.ambari.server.upgrade.UpgradeCatalog251 upgradeCatalog251 = injector.getInstance(org.apache.ambari.server.upgrade.UpgradeCatalog251.class);
        upgradeCatalog251.executeDDLUpdates();
        EasyMock.verify(dbAccessor);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo captured = hrcBackgroundColumnCapture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.upgrade.UpgradeCatalog251.HRC_IS_BACKGROUND_COLUMN, captured.getName());
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(0), captured.getDefaultValue());
        org.junit.Assert.assertEquals(java.lang.Short.class, captured.getType());
    }

    @org.junit.Test
    public void testExecuteDMLUpdates() throws java.lang.Exception {
        java.lang.reflect.Method addNewConfigurationsFromXml = org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.class.getDeclaredMethod("addNewConfigurationsFromXml");
        java.lang.reflect.Method updateKAFKAConfigs = org.apache.ambari.server.upgrade.UpgradeCatalog251.class.getDeclaredMethod("updateKAFKAConfigs");
        java.lang.reflect.Method updateSTORMConfigs = org.apache.ambari.server.upgrade.UpgradeCatalog251.class.getDeclaredMethod("updateSTORMConfigs");
        org.apache.ambari.server.upgrade.UpgradeCatalog251 upgradeCatalog251 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog251.class).addMockedMethod(addNewConfigurationsFromXml).addMockedMethod(updateKAFKAConfigs).addMockedMethod(updateSTORMConfigs).createMock();
        upgradeCatalog251.addNewConfigurationsFromXml();
        EasyMock.expectLastCall().once();
        java.lang.reflect.Field field = org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.class.getDeclaredField("dbAccessor");
        field.set(upgradeCatalog251, dbAccessor);
        upgradeCatalog251.updateKAFKAConfigs();
        EasyMock.expectLastCall().once();
        upgradeCatalog251.updateSTORMConfigs();
        EasyMock.expectLastCall().once();
        EasyMock.replay(upgradeCatalog251, dbAccessor);
        upgradeCatalog251.executeDMLUpdates();
        EasyMock.verify(upgradeCatalog251, dbAccessor);
    }

    @org.junit.Test
    public void testUpdateKAFKAConfigs() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster mockClusterExpected = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        java.util.Map<java.lang.String, java.lang.String> initialProperties = java.util.Collections.singletonMap("listeners", "PLAINTEXT://localhost:6667,SSL://localhost:6666");
        java.util.Map<java.lang.String, java.lang.String> expectedUpdates = java.util.Collections.singletonMap("listeners", "PLAINTEXTSASL://localhost:6667,SSL://localhost:6666");
        final org.apache.ambari.server.state.Config kafkaBroker = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(kafkaBroker.getProperties()).andReturn(initialProperties).times(1);
        EasyMock.expect(kafkaBroker.getProperties()).andReturn(expectedUpdates).times(1);
        final com.google.inject.Injector mockInjector = getInjector(mockClusters, mockAmbariManagementController);
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).atLeastOnce();
        EasyMock.expect(mockClusters.getClusters()).andReturn(java.util.Collections.singletonMap("normal", mockClusterExpected)).atLeastOnce();
        EasyMock.expect(mockClusterExpected.getDesiredConfigByType("kafka-broker")).andReturn(kafkaBroker).atLeastOnce();
        EasyMock.expect(mockClusterExpected.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS).atLeastOnce();
        EasyMock.expect(mockClusterExpected.getServices()).andReturn(java.util.Collections.singletonMap("KAFKA", null)).atLeastOnce();
        org.apache.ambari.server.upgrade.UpgradeCatalog251 upgradeCatalog251 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog251.class).withConstructor(com.google.inject.Injector.class).withArgs(mockInjector).addMockedMethod("updateConfigurationProperties", java.lang.String.class, java.util.Map.class, boolean.class, boolean.class).createMock();
        upgradeCatalog251.updateConfigurationProperties("kafka-broker", expectedUpdates, true, false);
        EasyMock.expectLastCall().once();
        easyMockSupport.replayAll();
        EasyMock.replay(upgradeCatalog251);
        upgradeCatalog251.updateKAFKAConfigs();
        upgradeCatalog251.updateKAFKAConfigs();
        easyMockSupport.verifyAll();
    }

    private com.google.inject.Injector getInjector(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) {
        com.google.inject.Module module = new com.google.inject.Module() {
            @java.lang.Override
            public void configure(com.google.inject.Binder binder) {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addFactoriesInstallBinding().addPasswordEncryptorBindings().addLdapBindings().build().configure(binder);
                binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(osFamily);
                binder.bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
                binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(ambariManagementController);
                binder.bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class));
                binder.bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(EasyMock.createMock(org.apache.ambari.server.topology.PersistedStateImpl.class));
                binder.bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                binder.bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class));
                binder.bind(org.apache.ambari.server.actionmanager.StageFactory.class).to(org.apache.ambari.server.actionmanager.StageFactoryImpl.class);
                binder.bind(com.google.inject.persist.UnitOfWork.class).toInstance(EasyMock.createNiceMock(com.google.inject.persist.UnitOfWork.class));
                binder.bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                binder.bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                binder.bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                binder.bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                binder.bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                binder.bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                binder.bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                binder.bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                binder.bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.KerberosHelperImpl.class));
                binder.bind(org.apache.ambari.server.agent.stomp.MetadataHolder.class).toInstance(metadataHolder);
                binder.bind(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class));
                binder.bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                binder.bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
            }
        };
        return com.google.inject.Guice.createInjector(module);
    }
}