package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.mockito.Matchers;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.checks.UpgradeCheckRegistry.class })
public class PreUpgradeCheckResourceProviderTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String TEST_SERVICE_CHECK_CLASS_NAME = "org.apache.ambari.server.sample.checks.SampleServiceCheck";

    private static final java.lang.String CLUSTER_NAME = "Cluster100";

    @org.junit.Test
    @java.lang.SuppressWarnings({ "rawtypes", "unchecked" })
    public void testGetResources() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoDao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repo = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);
        org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckConfig config = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckConfig.class);
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        org.apache.ambari.spi.ClusterInformation clusterInformation = createNiceMock(org.apache.ambari.spi.ClusterInformation.class);
        EasyMock.expect(service.getDesiredRepositoryVersion()).andReturn(repo).atLeastOnce();
        org.apache.ambari.server.state.StackId currentStackId = createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.StackId targetStackId = createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        EasyMock.expectLastCall().anyTimes();
        org.apache.ambari.server.state.Config actualConfig = createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.DesiredConfig desiredConfig = createNiceMock(org.apache.ambari.server.state.DesiredConfig.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> configMap = com.google.common.collect.Maps.newHashMap();
        configMap.put("config-type", desiredConfig);
        EasyMock.expect(desiredConfig.getTag()).andReturn("config-tag-1").atLeastOnce();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(configMap).atLeastOnce();
        EasyMock.expect(cluster.getConfig("config-type", "config-tag-1")).andReturn(actualConfig).atLeastOnce();
        EasyMock.expect(cluster.buildClusterInformation()).andReturn(clusterInformation).anyTimes();
        EasyMock.expect(clusterInformation.getClusterName()).andReturn(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.CLUSTER_NAME).anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> allServiceMap = new java.util.HashMap<>();
        allServiceMap.put("Service100", service);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> allServiceInfoMap = new java.util.HashMap<>();
        allServiceInfoMap.put("Service100", serviceInfo);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(serviceComponentHost.getServiceName()).andReturn("Service100").atLeastOnce();
        EasyMock.expect(serviceComponentHost.getServiceComponentName()).andReturn("Component100").atLeastOnce();
        EasyMock.expect(serviceComponentHost.getHostName()).andReturn("c6401.ambari.apache.org").atLeastOnce();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = com.google.common.collect.Lists.newArrayList();
        serviceComponentHosts.add(serviceComponentHost);
        EasyMock.expect(cluster.getServiceComponentHosts()).andReturn(serviceComponentHosts).atLeastOnce();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.CLUSTER_NAME)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.CLUSTER_NAME).atLeastOnce();
        EasyMock.expect(cluster.getServices()).andReturn(allServiceMap).anyTimes();
        EasyMock.expect(cluster.getService("Service100")).andReturn(service).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(currentStackId).anyTimes();
        EasyMock.expect(currentStackId.getStackName()).andReturn("Stack100").anyTimes();
        EasyMock.expect(currentStackId.getStackVersion()).andReturn("1.0").anyTimes();
        EasyMock.expect(targetStackId.getStackId()).andReturn("Stack100-1.1").anyTimes();
        EasyMock.expect(targetStackId.getStackName()).andReturn("Stack100").anyTimes();
        EasyMock.expect(targetStackId.getStackVersion()).andReturn("1.1").anyTimes();
        EasyMock.expect(repoDao.findByPK(1L)).andReturn(repo).anyTimes();
        EasyMock.expect(repo.getStackId()).andReturn(targetStackId).atLeastOnce();
        EasyMock.expect(repo.getId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(repo.getType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).atLeastOnce();
        EasyMock.expect(repo.getVersion()).andReturn("1.1.0.0").atLeastOnce();
        EasyMock.expect(upgradeHelper.suggestUpgradePack(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.CLUSTER_NAME, currentStackId, targetStackId, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, "upgrade_pack11")).andReturn(upgradePack);
        java.util.List<java.lang.String> prerequisiteChecks = new java.util.LinkedList<>();
        prerequisiteChecks.add(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.TEST_SERVICE_CHECK_CLASS_NAME);
        EasyMock.expect(upgradePack.getPrerequisiteCheckConfig()).andReturn(config);
        EasyMock.expect(upgradePack.getPrerequisiteChecks()).andReturn(prerequisiteChecks).anyTimes();
        EasyMock.expect(upgradePack.getTarget()).andReturn("1.1.*.*").anyTimes();
        EasyMock.expect(upgradePack.getOwnerStackId()).andReturn(targetStackId).atLeastOnce();
        EasyMock.expect(upgradePack.getType()).andReturn(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING).atLeastOnce();
        EasyMock.expect(ambariMetaInfo.getServices("Stack100", "1.0")).andReturn(allServiceInfoMap).anyTimes();
        java.lang.String checks = java.lang.ClassLoader.getSystemClassLoader().getResource("checks").getPath();
        EasyMock.expect(serviceInfo.getChecksFolder()).andReturn(new java.io.File(checks));
        java.net.URL url = new java.net.URL("file://foo");
        java.net.URLClassLoader classLoader = createNiceMock(java.net.URLClassLoader.class);
        EasyMock.expect(classLoader.getURLs()).andReturn(new java.net.URL[]{ url }).once();
        org.apache.ambari.server.state.StackInfo stackInfo = createNiceMock(org.apache.ambari.server.state.StackInfo.class);
        EasyMock.expect(ambariMetaInfo.getStack(targetStackId)).andReturn(stackInfo).atLeastOnce();
        EasyMock.expect(stackInfo.getLibraryClassLoader()).andReturn(classLoader).atLeastOnce();
        EasyMock.expect(stackInfo.getLibraryInstance(org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.eq(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.TEST_SERVICE_CHECK_CLASS_NAME))).andReturn(new org.apache.ambari.server.sample.checks.SampleServiceCheck()).atLeastOnce();
        org.reflections.Reflections reflectionsMock = createNiceMock(org.reflections.Reflections.class);
        org.powermock.api.mockito.PowerMockito.whenNew(org.reflections.Reflections.class).withParameterTypes(org.reflections.Configuration.class).withArguments(org.mockito.Matchers.any(org.reflections.util.ConfigurationBuilder.class)).thenReturn(reflectionsMock);
        org.powermock.api.easymock.PowerMock.replay(org.reflections.Reflections.class);
        replayAll();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getPreUpgradeCheckResourceProvider(managementController, injector);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>());
        org.apache.ambari.server.controller.utilities.PredicateBuilder builder = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = builder.property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID).equals(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.CLUSTER_NAME).and().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_PACK_PROPERTY_ID).equals("upgrade_pack11").and().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID).equals(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING).and().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_TARGET_REPOSITORY_VERSION_ID_ID).equals("1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = java.util.Collections.emptySet();
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(20, resources.size());
        org.apache.ambari.server.controller.spi.Resource customUpgradeCheck = null;
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String id = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_ID_PROPERTY_ID)));
            if (org.apache.commons.lang3.StringUtils.equals(id, "SAMPLE_SERVICE_CHECK")) {
                customUpgradeCheck = resource;
                break;
            }
        }
        org.junit.Assert.assertNotNull(customUpgradeCheck);
        java.lang.String description = ((java.lang.String) (customUpgradeCheck.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CHECK_PROPERTY_ID)));
        org.junit.Assert.assertEquals("Sample service check description.", description);
        org.apache.ambari.spi.upgrade.UpgradeCheckStatus status = ((org.apache.ambari.spi.upgrade.UpgradeCheckStatus) (customUpgradeCheck.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_STATUS_PROPERTY_ID)));
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, status);
        java.lang.String reason = ((java.lang.String) (customUpgradeCheck.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_REASON_PROPERTY_ID)));
        org.junit.Assert.assertEquals("Sample service check always fails.", reason);
        org.apache.ambari.spi.upgrade.UpgradeCheckType checkType = ((org.apache.ambari.spi.upgrade.UpgradeCheckType) (customUpgradeCheck.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CHECK_TYPE_PROPERTY_ID)));
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, checkType);
        java.lang.String clusterName = ((java.lang.String) (customUpgradeCheck.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID)));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProviderTest.CLUSTER_NAME, clusterName);
        org.apache.ambari.spi.upgrade.UpgradeType upgradeType = ((org.apache.ambari.spi.upgrade.UpgradeType) (customUpgradeCheck.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID)));
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, upgradeType);
        org.powermock.api.easymock.PowerMock.verifyAll();
    }

    public org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider getPreUpgradeCheckResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController, com.google.inject.Injector injector) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider provider = new org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider(managementController);
        return provider;
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            protected void configure() {
                org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
                com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusterProvider = () -> clusters;
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
                com.google.inject.Provider<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper> upgradeHelperProvider = () -> upgradeHelper;
                org.apache.ambari.server.state.CheckHelper checkHelper = new org.apache.ambari.server.state.CheckHelper();
                bind(org.apache.ambari.server.state.CheckHelper.class).toInstance(checkHelper);
                bind(org.apache.ambari.server.state.Clusters.class).toProvider(clusterProvider);
                bind(org.apache.ambari.server.checks.UpgradeCheckRegistry.class).toProvider(org.apache.ambari.server.checks.UpgradeCheckRegistryProvider.class);
                bind(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class).toProvider(upgradeHelperProvider);
                bind(org.apache.ambari.server.controller.KerberosHelper.class).to(org.apache.ambari.server.controller.KerberosHelperImpl.class);
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory.class).toInstance(createNiceMock(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory.class));
                bind(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class).toInstance(createNiceMock(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class));
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.state.ConfigFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigFactory.class));
                bind(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                bind(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
                bind(org.apache.ambari.server.actionmanager.RequestFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.RequestFactory.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).toInstance(createNiceMock(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class));
                bind(org.apache.ambari.server.stageplanner.RoleGraphFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createNiceMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createNiceMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createNiceMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(createNiceMock(org.apache.ambari.server.topology.PersistedState.class));
                bind(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class).toInstance(createNiceMock(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
                bind(org.apache.ambari.server.topology.TopologyManager.class).toInstance(createNiceMock(org.apache.ambari.server.topology.TopologyManager.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class));
                bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
                bind(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = createNiceMock(com.google.inject.Provider.class);
                bind(javax.persistence.EntityManager.class).toProvider(entityManagerProvider);
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
                requestStaticInjection(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.class);
            }
        });
    }
}