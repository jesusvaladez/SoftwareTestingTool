package org.apache.ambari.server.controller;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.persist.PersistModule;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.eclipse.persistence.config.PersistenceUnitProperties.CREATE_JDBC_DDL_FILE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.CREATE_ONLY;
import static org.eclipse.persistence.config.PersistenceUnitProperties.CREATE_OR_EXTEND;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_BOTH_GENERATION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_GENERATION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_GENERATION_MODE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DROP_AND_CREATE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DROP_JDBC_DDL_FILE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_DRIVER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_URL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.NON_JTA_DATASOURCE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.THROW_EXCEPTIONS;
public class ControllerModule extends com.google.inject.AbstractModule {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.ControllerModule.class);

    private static final java.lang.String AMBARI_PACKAGE = "org.apache.ambari.server";

    private final org.apache.ambari.server.configuration.Configuration configuration;

    private final org.apache.ambari.server.state.stack.OsFamily os_family;

    private final org.apache.ambari.server.controller.HostsMap hostsMap;

    private final org.apache.ambari.server.utils.ThreadPools threadPools;

    private boolean dbInitNeeded;

    private final com.google.gson.Gson prettyGson = new com.google.gson.GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public ControllerModule() throws java.lang.Exception {
        configuration = new org.apache.ambari.server.configuration.Configuration();
        hostsMap = new org.apache.ambari.server.controller.HostsMap(configuration);
        os_family = new org.apache.ambari.server.state.stack.OsFamily(configuration);
        threadPools = new org.apache.ambari.server.utils.ThreadPools(configuration);
    }

    public ControllerModule(java.util.Properties properties) throws java.lang.Exception {
        configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        hostsMap = new org.apache.ambari.server.controller.HostsMap(configuration);
        os_family = new org.apache.ambari.server.state.stack.OsFamily(configuration);
        threadPools = new org.apache.ambari.server.utils.ThreadPools(configuration);
    }

    public static java.util.Properties getPersistenceProperties(org.apache.ambari.server.configuration.Configuration configuration) {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        org.apache.ambari.server.controller.ControllerModule.LOG.info("Detected {} as the database type from the JDBC URL", databaseType);
        switch (configuration.getPersistenceType()) {
            case IN_MEMORY :
                properties.setProperty(org.apache.ambari.server.controller.JDBC_URL, org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL);
                properties.setProperty(org.apache.ambari.server.controller.JDBC_DRIVER, org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER);
                properties.setProperty(org.apache.ambari.server.controller.JDBC_USER, org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_USER);
                properties.setProperty(org.apache.ambari.server.controller.JDBC_PASSWORD, org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_PASSWORD);
                properties.setProperty(org.apache.ambari.server.controller.DDL_GENERATION, org.apache.ambari.server.controller.CREATE_ONLY);
                properties.setProperty(org.apache.ambari.server.controller.THROW_EXCEPTIONS, "true");
                break;
            case REMOTE :
                properties.setProperty(org.apache.ambari.server.controller.JDBC_URL, configuration.getDatabaseUrl());
                properties.setProperty(org.apache.ambari.server.controller.JDBC_DRIVER, configuration.getDatabaseDriver());
                break;
            case LOCAL :
                properties.setProperty(org.apache.ambari.server.controller.JDBC_URL, configuration.getLocalDatabaseUrl());
                properties.setProperty(org.apache.ambari.server.controller.JDBC_DRIVER, org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_DRIVER.getDefaultValue());
                break;
        }
        java.util.Properties customDatabaseDriverProperties = configuration.getDatabaseCustomProperties();
        properties.putAll(customDatabaseDriverProperties);
        java.util.Properties customPersistenceProperties = configuration.getPersistenceCustomProperties();
        properties.putAll(customPersistenceProperties);
        boolean isConnectionPoolingExternal = false;
        org.apache.ambari.server.configuration.Configuration.ConnectionPoolType connectionPoolType = configuration.getConnectionPoolType();
        if (connectionPoolType == org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.C3P0) {
            isConnectionPoolingExternal = true;
        }
        if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL) {
            isConnectionPoolingExternal = true;
        }
        if (isConnectionPoolingExternal) {
            org.apache.ambari.server.controller.ControllerModule.LOG.info("Using c3p0 {} as the EclipsLink DataSource", com.mchange.v2.c3p0.ComboPooledDataSource.class.getSimpleName());
            java.lang.String testQuery = "SELECT 1";
            if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE) {
                testQuery = "SELECT 1 FROM DUAL";
            }
            com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
            try {
                dataSource.setDriverClass(configuration.getDatabaseDriver());
            } catch (java.beans.PropertyVetoException pve) {
                org.apache.ambari.server.controller.ControllerModule.LOG.warn("Unable to initialize c3p0", pve);
                return properties;
            }
            dataSource.setJdbcUrl(configuration.getDatabaseUrl());
            dataSource.setUser(configuration.getDatabaseUser());
            dataSource.setPassword(configuration.getDatabasePassword());
            dataSource.setMinPoolSize(configuration.getConnectionPoolMinimumSize());
            dataSource.setInitialPoolSize(configuration.getConnectionPoolMinimumSize());
            dataSource.setMaxPoolSize(configuration.getConnectionPoolMaximumSize());
            dataSource.setAcquireIncrement(configuration.getConnectionPoolAcquisitionSize());
            dataSource.setAcquireRetryAttempts(configuration.getConnectionPoolAcquisitionRetryAttempts());
            dataSource.setAcquireRetryDelay(configuration.getConnectionPoolAcquisitionRetryDelay());
            dataSource.setMaxConnectionAge(configuration.getConnectionPoolMaximumAge());
            dataSource.setMaxIdleTime(configuration.getConnectionPoolMaximumIdle());
            dataSource.setMaxIdleTimeExcessConnections(configuration.getConnectionPoolMaximumExcessIdle());
            dataSource.setPreferredTestQuery(testQuery);
            dataSource.setIdleConnectionTestPeriod(configuration.getConnectionPoolIdleTestInternval());
            properties.put(org.apache.ambari.server.controller.NON_JTA_DATASOURCE, dataSource);
        }
        return properties;
    }

    @java.lang.Override
    protected void configure() {
        installFactories();
        final org.eclipse.jetty.server.session.SessionHandler sessionHandler = new org.eclipse.jetty.server.session.SessionHandler();
        bind(org.eclipse.jetty.server.session.SessionHandler.class).toInstance(sessionHandler);
        bind(org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory.class);
        bind(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class);
        bind(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory.class);
        bind(org.apache.ambari.server.controller.KerberosHelper.class).to(org.apache.ambari.server.controller.KerberosHelperImpl.class);
        bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
        bind(org.apache.ambari.server.security.encryption.EncryptionService.class).to(org.apache.ambari.server.security.encryption.AESEncryptionService.class);
        if (configuration.shouldEncryptSensitiveData()) {
            bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config>>() {}).annotatedWith(com.google.inject.name.Names.named("ConfigPropertiesEncryptor")).to(org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor.class);
            bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).to(org.apache.ambari.server.security.encryption.AgentConfigUpdateEncryptor.class);
            bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).to(org.apache.ambari.server.security.encryption.AmbariServerConfigurationEncryptor.class);
        } else {
            bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config>>() {}).annotatedWith(com.google.inject.name.Names.named("ConfigPropertiesEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
            bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
            bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
        }
        bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
        bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(os_family);
        bind(org.apache.ambari.server.utils.ThreadPools.class).toInstance(threadPools);
        bind(org.apache.ambari.server.controller.HostsMap.class).toInstance(hostsMap);
        bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
        bind(org.springframework.web.filter.DelegatingFilterProxy.class).toInstance(new org.springframework.web.filter.DelegatingFilterProxy() {
            {
                setTargetBeanName("springSecurityFilterChain");
            }
        });
        bind(com.google.gson.Gson.class).annotatedWith(com.google.inject.name.Names.named("prettyGson")).toInstance(prettyGson);
        install(buildJpaPersistModule());
        bind(com.google.gson.Gson.class).in(com.google.inject.Scopes.SINGLETON);
        bind(java.security.SecureRandom.class).in(com.google.inject.Scopes.SINGLETON);
        bind(org.apache.ambari.server.state.Clusters.class).to(org.apache.ambari.server.state.cluster.ClustersImpl.class);
        bind(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);
        bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        bindConstant().annotatedWith(com.google.inject.name.Names.named("schedulerSleeptime")).to(configuration.getExecutionSchedulerWait());
        bindConstant().annotatedWith(com.google.inject.name.Names.named("actionTimeout")).to(600000L);
        bindConstant().annotatedWith(com.google.inject.name.Names.named("alertServiceCorePoolSize")).to(configuration.getAlertServiceCorePoolSize());
        bindConstant().annotatedWith(com.google.inject.name.Names.named("dbInitNeeded")).to(dbInitNeeded);
        bindConstant().annotatedWith(com.google.inject.name.Names.named("statusCheckInterval")).to(5000L);
        bindConstant().annotatedWith(com.google.inject.name.Names.named("executionCommandCacheSize")).to(configuration.getExecutionCommandsCacheSize());
        bindConstant().annotatedWith(com.google.inject.name.Names.named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_ENABLED)).to(configuration.getHostRoleCommandStatusSummaryCacheEnabled());
        bindConstant().annotatedWith(com.google.inject.name.Names.named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_SIZE)).to(configuration.getHostRoleCommandStatusSummaryCacheSize());
        bindConstant().annotatedWith(com.google.inject.name.Names.named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION_MINUTES)).to(configuration.getHostRoleCommandStatusSummaryCacheExpiryDuration());
        bind(org.apache.ambari.server.controller.AmbariManagementController.class).to(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class);
        bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
        bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).to(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class);
        bind(org.apache.ambari.server.orm.DBAccessor.class).to(org.apache.ambari.server.orm.DBAccessorImpl.class);
        bind(org.apache.ambari.server.view.ViewInstanceHandlerList.class).to(org.apache.ambari.server.controller.AmbariHandlerList.class);
        bind(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        bind(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class);
        bind(org.apache.ambari.server.topology.SecurityConfigurationFactory.class).in(com.google.inject.Scopes.SINGLETON);
        bind(org.apache.ambari.server.topology.PersistedState.class).to(org.apache.ambari.server.topology.PersistedStateImpl.class);
        bind(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class).to(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.class);
        bind(org.apache.ambari.server.metrics.system.MetricsService.class).to(org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.class).in(com.google.inject.Scopes.SINGLETON);
        requestStaticInjection(org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.class);
        requestStaticInjection(org.apache.ambari.server.controller.utilities.KerberosChecker.class);
        requestStaticInjection(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);
        requestStaticInjection(org.apache.ambari.server.utils.PasswordUtils.class);
        bindByAnnotation(null);
        bindNotificationDispatchers(null);
        bind(org.apache.ambari.server.checks.UpgradeCheckRegistry.class).toProvider(org.apache.ambari.server.checks.UpgradeCheckRegistryProvider.class).in(com.google.inject.Singleton.class);
        bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
        org.apache.ambari.server.security.authorization.internal.InternalAuthenticationInterceptor ambariAuthenticationInterceptor = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationInterceptor();
        requestInjection(ambariAuthenticationInterceptor);
        bindInterceptor(com.google.inject.matcher.Matchers.any(), com.google.inject.matcher.Matchers.annotatedWith(org.apache.ambari.server.security.authorization.internal.RunWithInternalSecurityContext.class), ambariAuthenticationInterceptor);
    }

    private com.google.inject.persist.PersistModule buildJpaPersistModule() {
        org.apache.ambari.server.orm.PersistenceType persistenceType = configuration.getPersistenceType();
        com.google.inject.persist.jpa.AmbariJpaPersistModule jpaPersistModule = new com.google.inject.persist.jpa.AmbariJpaPersistModule(org.apache.ambari.server.configuration.Configuration.JDBC_UNIT_NAME);
        java.util.Properties persistenceProperties = org.apache.ambari.server.controller.ControllerModule.getPersistenceProperties(configuration);
        if (!persistenceType.equals(org.apache.ambari.server.orm.PersistenceType.IN_MEMORY)) {
            persistenceProperties.setProperty(org.apache.ambari.server.controller.JDBC_USER, configuration.getDatabaseUser());
            persistenceProperties.setProperty(org.apache.ambari.server.controller.JDBC_PASSWORD, configuration.getDatabasePassword());
            switch (configuration.getJPATableGenerationStrategy()) {
                case CREATE :
                    persistenceProperties.setProperty(org.apache.ambari.server.controller.DDL_GENERATION, org.apache.ambari.server.controller.CREATE_ONLY);
                    dbInitNeeded = true;
                    break;
                case DROP_AND_CREATE :
                    persistenceProperties.setProperty(org.apache.ambari.server.controller.DDL_GENERATION, org.apache.ambari.server.controller.DROP_AND_CREATE);
                    dbInitNeeded = true;
                    break;
                case CREATE_OR_EXTEND :
                    persistenceProperties.setProperty(org.apache.ambari.server.controller.DDL_GENERATION, org.apache.ambari.server.controller.CREATE_OR_EXTEND);
                    break;
                default :
                    break;
            }
            persistenceProperties.setProperty(org.apache.ambari.server.controller.DDL_GENERATION_MODE, org.apache.ambari.server.controller.DDL_BOTH_GENERATION);
            persistenceProperties.setProperty(org.apache.ambari.server.controller.CREATE_JDBC_DDL_FILE, "DDL-create.jdbc");
            persistenceProperties.setProperty(org.apache.ambari.server.controller.DROP_JDBC_DDL_FILE, "DDL-drop.jdbc");
        }
        jpaPersistModule.properties(persistenceProperties);
        return jpaPersistModule;
    }

    private void installFactories() {
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.cluster.ClusterImpl.class).build(org.apache.ambari.server.state.cluster.ClusterFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Host.class, org.apache.ambari.server.state.host.HostImpl.class).build(org.apache.ambari.server.state.host.HostFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Service.class, org.apache.ambari.server.state.ServiceImpl.class).build(org.apache.ambari.server.state.ServiceFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("host"), org.apache.ambari.server.controller.internal.HostResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("hostComponent"), org.apache.ambari.server.controller.internal.HostComponentResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("service"), org.apache.ambari.server.controller.internal.ServiceResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("component"), org.apache.ambari.server.controller.internal.ComponentResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("member"), org.apache.ambari.server.controller.internal.MemberResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("repositoryVersion"), org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("hostKerberosIdentity"), org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("user"), org.apache.ambari.server.controller.internal.UserResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("userAuthenticationSource"), org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("credential"), org.apache.ambari.server.controller.internal.CredentialResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("kerberosDescriptor"), org.apache.ambari.server.controller.internal.KerberosDescriptorResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("upgrade"), org.apache.ambari.server.controller.internal.UpgradeResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("clusterStackVersion"), org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("alertTarget"), org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("viewInstance"), org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("rootServiceHostComponentConfiguration"), org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.class).implement(org.apache.ambari.server.controller.spi.ResourceProvider.class, com.google.inject.name.Names.named("auth"), org.apache.ambari.server.controller.internal.AuthResourceProvider.class).build(org.apache.ambari.server.controller.ResourceProviderFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.ServiceComponent.class, org.apache.ambari.server.state.ServiceComponentImpl.class).build(org.apache.ambari.server.state.ServiceComponentFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.ServiceComponentHost.class, org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class).build(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Config.class, org.apache.ambari.server.state.ConfigImpl.class).build(org.apache.ambari.server.state.ConfigFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.configgroup.ConfigGroup.class, org.apache.ambari.server.state.configgroup.ConfigGroupImpl.class).build(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.scheduler.RequestExecution.class, org.apache.ambari.server.state.scheduler.RequestExecutionImpl.class).build(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
        bind(org.apache.ambari.server.actionmanager.StageFactory.class).to(org.apache.ambari.server.actionmanager.StageFactoryImpl.class);
        bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.actionmanager.RequestFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.StackManagerFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.mpack.MpackManagerFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.topology.addservice.RequestValidatorFactory.class));
        bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
        bind(org.apache.ambari.server.security.SecurityHelper.class).toInstance(org.apache.ambari.server.security.SecurityHelperImpl.getInstance());
        bind(org.apache.ambari.server.topology.StackFactory.class).to(org.apache.ambari.server.topology.DefaultStackFactory.class);
        bind(org.apache.ambari.server.topology.BlueprintFactory.class);
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.events.AmbariEvent.class, com.google.inject.name.Names.named("userCreated"), org.apache.ambari.server.hooks.users.UserCreatedEvent.class).build(org.apache.ambari.server.hooks.AmbariEventFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.hooks.HookContext.class, org.apache.ambari.server.hooks.users.PostUserCreationHookContext.class).build(org.apache.ambari.server.hooks.HookContextFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.serveraction.users.CollectionPersisterService.class, org.apache.ambari.server.serveraction.users.CsvFilePersisterService.class).build(org.apache.ambari.server.serveraction.users.CollectionPersisterServiceFactory.class));
        install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
    }

    @java.lang.SuppressWarnings("unchecked")
    protected java.util.Set<java.lang.Class<?>> bindByAnnotation(java.util.Set<java.lang.Class<?>> matchedClasses) {
        if (null == matchedClasses) {
            java.util.List<java.lang.Class<?>> classes = new java.util.ArrayList<>();
            classes.add(org.apache.ambari.server.EagerSingleton.class);
            classes.add(org.apache.ambari.server.StaticallyInject.class);
            classes.add(org.apache.ambari.server.AmbariService.class);
            org.apache.ambari.server.controller.ControllerModule.LOG.info("Searching package {} for annotations matching {}", org.apache.ambari.server.controller.ControllerModule.AMBARI_PACKAGE, classes);
            matchedClasses = org.apache.ambari.server.cleanup.ClasspathScannerUtils.findOnClassPath(org.apache.ambari.server.controller.ControllerModule.AMBARI_PACKAGE, new java.util.ArrayList<>(), classes);
            if ((null == matchedClasses) || (matchedClasses.size() == 0)) {
                org.apache.ambari.server.controller.ControllerModule.LOG.warn("No instances of {} found to register", classes);
                return matchedClasses;
            }
        }
        java.util.Set<com.google.common.util.concurrent.Service> services = new java.util.HashSet<>();
        for (java.lang.Class<?> clazz : matchedClasses) {
            if (null != clazz.getAnnotation(org.apache.ambari.server.EagerSingleton.class)) {
                bind(clazz).asEagerSingleton();
                org.apache.ambari.server.controller.ControllerModule.LOG.debug("Eagerly binding singleton {}", clazz);
            }
            if (null != clazz.getAnnotation(org.apache.ambari.server.StaticallyInject.class)) {
                requestStaticInjection(clazz);
                org.apache.ambari.server.controller.ControllerModule.LOG.debug("Statically injecting {} ", clazz);
            }
            if (null != clazz.getAnnotation(org.apache.ambari.server.AmbariService.class)) {
                if (!com.google.common.util.concurrent.Service.class.isAssignableFrom(clazz)) {
                    java.lang.String message = java.text.MessageFormat.format("Unable to register service {0} because it is not a Service which can be scheduled", clazz);
                    org.apache.ambari.server.controller.ControllerModule.LOG.error(message);
                    throw new java.lang.RuntimeException(message);
                }
                com.google.common.util.concurrent.Service service = null;
                try {
                    service = ((com.google.common.util.concurrent.Service) (clazz.newInstance()));
                    bind(((java.lang.Class<com.google.common.util.concurrent.Service>) (clazz))).toInstance(service);
                    services.add(service);
                    org.apache.ambari.server.controller.ControllerModule.LOG.info("Registering service {} ", clazz);
                } catch (java.lang.Exception exception) {
                    org.apache.ambari.server.controller.ControllerModule.LOG.error("Unable to register {} as a service", clazz, exception);
                    throw new java.lang.RuntimeException(exception);
                }
            }
        }
        com.google.common.util.concurrent.ServiceManager manager = new com.google.common.util.concurrent.ServiceManager(services);
        bind(com.google.common.util.concurrent.ServiceManager.class).toInstance(manager);
        return matchedClasses;
    }

    @java.lang.SuppressWarnings("unchecked")
    protected java.util.Set<org.springframework.beans.factory.config.BeanDefinition> bindNotificationDispatchers(java.util.Set<org.springframework.beans.factory.config.BeanDefinition> beanDefinitions) {
        org.apache.ambari.server.notifications.DispatchFactory dispatchFactory = org.apache.ambari.server.notifications.DispatchFactory.getInstance();
        bind(org.apache.ambari.server.notifications.DispatchFactory.class).toInstance(dispatchFactory);
        if ((null == beanDefinitions) || beanDefinitions.isEmpty()) {
            java.lang.String packageName = org.apache.ambari.server.notifications.dispatchers.AlertScriptDispatcher.class.getPackage().getName();
            org.apache.ambari.server.controller.ControllerModule.LOG.info("Searching package {} for dispatchers matching {}", packageName, org.apache.ambari.server.notifications.NotificationDispatcher.class);
            org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider scanner = new org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider(false);
            org.springframework.core.type.filter.AssignableTypeFilter filter = new org.springframework.core.type.filter.AssignableTypeFilter(org.apache.ambari.server.notifications.NotificationDispatcher.class);
            scanner.addIncludeFilter(filter);
            beanDefinitions = scanner.findCandidateComponents(packageName);
        }
        if ((null == beanDefinitions) || (beanDefinitions.size() == 0)) {
            org.apache.ambari.server.controller.ControllerModule.LOG.error("No instances of {} found to register", org.apache.ambari.server.notifications.NotificationDispatcher.class);
            return null;
        }
        for (org.springframework.beans.factory.config.BeanDefinition beanDefinition : beanDefinitions) {
            java.lang.String className = beanDefinition.getBeanClassName();
            if (className != null) {
                java.lang.Class<?> clazz = org.springframework.util.ClassUtils.resolveClassName(className, org.springframework.util.ClassUtils.getDefaultClassLoader());
                try {
                    org.apache.ambari.server.notifications.NotificationDispatcher dispatcher;
                    if (clazz.equals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.class)) {
                        dispatcher = ((org.apache.ambari.server.notifications.NotificationDispatcher) (clazz.getConstructor(java.lang.Integer.class).newInstance(configuration.getAmbariSNMPUdpBindPort())));
                    } else if (clazz.equals(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.class)) {
                        dispatcher = ((org.apache.ambari.server.notifications.NotificationDispatcher) (clazz.getConstructor(java.lang.Integer.class).newInstance(configuration.getSNMPUdpBindPort())));
                    } else {
                        dispatcher = ((org.apache.ambari.server.notifications.NotificationDispatcher) (clazz.newInstance()));
                    }
                    dispatchFactory.register(dispatcher.getType(), dispatcher);
                    bind(((java.lang.Class<org.apache.ambari.server.notifications.NotificationDispatcher>) (clazz))).toInstance(dispatcher);
                    org.apache.ambari.server.controller.ControllerModule.LOG.info("Binding and registering notification dispatcher {}", clazz);
                } catch (java.lang.Exception exception) {
                    org.apache.ambari.server.controller.ControllerModule.LOG.error("Unable to bind and register notification dispatcher {}", clazz, exception);
                }
            } else {
                org.apache.ambari.server.controller.ControllerModule.LOG.error("Binding and registering notification dispatcher is not possible for" + " beanDefinition: {} in the absence of className", beanDefinition);
            }
        }
        return beanDefinitions;
    }
}