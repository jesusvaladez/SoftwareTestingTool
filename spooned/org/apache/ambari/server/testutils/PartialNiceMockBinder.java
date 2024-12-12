package org.apache.ambari.server.testutils;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.easymock.EasyMock.createNiceMock;
public class PartialNiceMockBinder implements com.google.inject.Module {
    private final java.util.List<org.apache.ambari.server.testutils.PartialNiceMockBinder.Configurer> configurers;

    private final org.easymock.EasyMockSupport easyMockSupport;

    private PartialNiceMockBinder(org.easymock.EasyMockSupport easyMockSupport) {
        configurers = new java.util.ArrayList<>();
        this.easyMockSupport = easyMockSupport;
    }

    public static org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder newBuilder() {
        return new org.apache.ambari.server.testutils.PartialNiceMockBinder(new org.easymock.EasyMockSupport()).new Builder();
    }

    public static org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder newBuilder(org.easymock.EasyMockSupport easyMockSupport) {
        return new org.apache.ambari.server.testutils.PartialNiceMockBinder(easyMockSupport).new Builder();
    }

    @java.lang.Override
    public void configure(com.google.inject.Binder binder) {
        configurers.forEach(configurer -> configurer.configure(binder));
    }

    public class Builder {
        public Builder() {
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addAlertDefinitionBinding() {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class));
                binder.bind(org.apache.ambari.server.orm.dao.DaoUtils.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.DaoUtils.class));
                binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
            });
            addDBAccessorBinding();
            addAmbariMetaInfoBinding();
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addAmbariMetaInfoBinding(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.topology.PersistedState.class));
                binder.bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                binder.bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
                binder.bind(com.google.inject.persist.UnitOfWork.class).toInstance(easyMockSupport.createNiceMock(com.google.inject.persist.UnitOfWork.class));
                binder.bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                binder.bind(org.apache.ambari.server.actionmanager.StageFactory.class).to(org.apache.ambari.server.actionmanager.StageFactoryImpl.class);
                binder.bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class));
                binder.bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                binder.bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                binder.bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                binder.bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                binder.bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(ambariManagementController);
                binder.bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).to(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class);
                binder.bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class));
            });
            addConfigsBindings();
            addFactoriesInstallBinding();
            addPasswordEncryptorBindings();
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addAmbariMetaInfoBinding() {
            return addAmbariMetaInfoBinding(easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class));
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addAlertDefinitionDAOBinding() {
            addAmbariMetaInfoBinding();
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addClustersBinding(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) {
            addAmbariMetaInfoBinding(ambariManagementController);
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addClustersBinding() {
            addAmbariMetaInfoBinding();
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addDBAccessorBinding(org.apache.ambari.server.orm.DBAccessor dbAccessor) {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                binder.bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                binder.bind(javax.persistence.EntityManager.class).toInstance(easyMockSupport.createNiceMock(javax.persistence.EntityManager.class));
                binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class));
                binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            });
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addDBAccessorBinding() {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                binder.bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                binder.bind(javax.persistence.EntityManager.class).toInstance(easyMockSupport.createNiceMock(javax.persistence.EntityManager.class));
                binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class));
                binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            });
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addConfigsBindings() {
            addHostRoleCommandsConfigsBindings();
            addActionSchedulerConfigsBindings();
            addActionDBAccessorConfigsBindings();
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addPasswordEncryptorBindings() {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bind(org.apache.ambari.server.security.encryption.EncryptionService.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.security.encryption.EncryptionService.class));
                binder.bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config>>() {}).annotatedWith(com.google.inject.name.Names.named("ConfigPropertiesEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                binder.bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                binder.bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
            });
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addHostRoleCommandsConfigsBindings() {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bindConstant().annotatedWith(com.google.inject.name.Names.named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_ENABLED)).to(true);
                binder.bindConstant().annotatedWith(com.google.inject.name.Names.named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_SIZE)).to(10000L);
                binder.bindConstant().annotatedWith(com.google.inject.name.Names.named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION_MINUTES)).to(30L);
            });
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addActionSchedulerConfigsBindings() {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bindConstant().annotatedWith(com.google.inject.name.Names.named("actionTimeout")).to(600000L);
                binder.bindConstant().annotatedWith(com.google.inject.name.Names.named("schedulerSleeptime")).to(1L);
            });
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addActionDBAccessorConfigsBindings() {
            configurers.add((com.google.inject.Binder binder) -> binder.bindConstant().annotatedWith(com.google.inject.name.Names.named("executionCommandCacheSize")).to(10000L));
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addLdapBindings() {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.bind(org.apache.ambari.server.ldap.service.LdapFacade.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.ldap.service.LdapFacade.class));
                binder.bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(easyMockSupport.createNiceMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
            });
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder.Builder addFactoriesInstallBinding() {
            configurers.add((com.google.inject.Binder binder) -> {
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Config.class, org.apache.ambari.server.state.ConfigImpl.class).build(org.apache.ambari.server.state.ConfigFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.actionmanager.RequestFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.hooks.HookContext.class, org.apache.ambari.server.hooks.users.PostUserCreationHookContext.class).build(org.apache.ambari.server.hooks.HookContextFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.ServiceComponent.class, org.apache.ambari.server.state.ServiceComponentImpl.class).build(org.apache.ambari.server.state.ServiceComponentFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.scheduler.RequestExecution.class, org.apache.ambari.server.state.scheduler.RequestExecutionImpl.class).build(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.configgroup.ConfigGroup.class, org.apache.ambari.server.state.configgroup.ConfigGroupImpl.class).build(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.events.AmbariEvent.class, com.google.inject.name.Names.named("userCreated"), org.apache.ambari.server.hooks.users.UserCreatedEvent.class).build(org.apache.ambari.server.hooks.AmbariEventFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Host.class, org.apache.ambari.server.state.host.HostImpl.class).build(org.apache.ambari.server.state.host.HostFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.cluster.ClusterImpl.class).build(org.apache.ambari.server.state.cluster.ClusterFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
                binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Service.class, org.apache.ambari.server.state.ServiceImpl.class).build(org.apache.ambari.server.state.ServiceFactory.class));
            });
            return this;
        }

        public org.apache.ambari.server.testutils.PartialNiceMockBinder build() {
            return PartialNiceMockBinder.this;
        }
    }

    private interface Configurer {
        void configure(com.google.inject.Binder binder);
    }
}