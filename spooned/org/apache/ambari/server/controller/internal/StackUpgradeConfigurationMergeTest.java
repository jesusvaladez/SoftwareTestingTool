package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class StackUpgradeConfigurationMergeTest extends org.easymock.EasyMockSupport {
    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.api.services.AmbariMetaInfo m_metainfo;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_metainfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.internal.StackUpgradeConfigurationMergeTest.MockModule mockModule = new org.apache.ambari.server.controller.internal.StackUpgradeConfigurationMergeTest.MockModule();
        m_injector = com.google.inject.Guice.createInjector(mockModule);
        EasyMock.reset(m_injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class));
    }

    @org.junit.After
    public void teardown() {
    }

    @org.junit.Test
    public void testMergedConfigurationsDoNotAddExplicitelyRemovedProperties() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion211 = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.apache.ambari.server.state.StackId stack211 = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        org.apache.ambari.server.state.StackId stack220 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        java.lang.String version211 = "2.1.1.0-1234";
        java.lang.String version220 = "2.2.0.0-1234";
        EasyMock.expect(repoVersion211.getStackId()).andReturn(stack211).atLeastOnce();
        EasyMock.expect(repoVersion211.getVersion()).andReturn(version211).atLeastOnce();
        EasyMock.expect(repoVersion220.getStackId()).andReturn(stack220).atLeastOnce();
        EasyMock.expect(repoVersion220.getVersion()).andReturn(version220).atLeastOnce();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stack211Configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack211FooType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack211BarType = new java.util.HashMap<>();
        stack211Configs.put("foo-site", stack211FooType);
        stack211Configs.put("bar-site", stack211BarType);
        stack211FooType.put("foo-property-1", "stack-211-original");
        stack211FooType.put("foo-property-2", "stack-211-original");
        stack211BarType.put("bar-property-1", "stack-211-original");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stack220Configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack220FooType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack220BarType = new java.util.HashMap<>();
        stack220Configs.put("foo-site", stack220FooType);
        stack220Configs.put("bar-site", stack220BarType);
        stack220FooType.put("foo-property-1", "stack-220-original");
        stack220FooType.put("foo-property-2", "stack-220-original");
        stack220BarType.put("bar-property-1", "stack-220-original");
        stack220BarType.put("bar-property-2", "stack-220-original");
        java.util.Map<java.lang.String, java.lang.String> existingFooType = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> existingBarType = new java.util.HashMap<>();
        org.apache.ambari.server.orm.entities.ClusterConfigEntity fooConfigEntity = createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity barConfigEntity = createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        EasyMock.expect(fooConfigEntity.getType()).andReturn("foo-site");
        EasyMock.expect(barConfigEntity.getType()).andReturn("bar-site");
        org.apache.ambari.server.state.Config fooConfig = createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.Config barConfig = createNiceMock(org.apache.ambari.server.state.Config.class);
        existingFooType.put("foo-property-1", "my-foo-property-1");
        existingBarType.put("bar-property-1", "stack-211-original");
        EasyMock.expect(fooConfig.getType()).andReturn("foo-site").atLeastOnce();
        EasyMock.expect(barConfig.getType()).andReturn("bar-site").atLeastOnce();
        EasyMock.expect(fooConfig.getProperties()).andReturn(existingFooType);
        EasyMock.expect(barConfig.getProperties()).andReturn(existingBarType);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigurations = new java.util.HashMap<>();
        desiredConfigurations.put("foo-site", null);
        desiredConfigurations.put("bar-site", null);
        org.apache.ambari.server.state.Service zookeeper = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(zookeeper.getName()).andReturn("ZOOKEEPER").atLeastOnce();
        EasyMock.expect(zookeeper.getServiceComponents()).andReturn(new java.util.HashMap<>()).once();
        zookeeper.setDesiredRepositoryVersion(repoVersion220);
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stack211).atLeastOnce();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stack220);
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(desiredConfigurations);
        EasyMock.expect(cluster.getDesiredConfigByType("foo-site")).andReturn(fooConfig);
        EasyMock.expect(cluster.getDesiredConfigByType("bar-site")).andReturn(barConfig);
        EasyMock.expect(cluster.getService("ZOOKEEPER")).andReturn(zookeeper);
        EasyMock.expect(cluster.getDesiredConfigByType("foo-type")).andReturn(fooConfig);
        EasyMock.expect(cluster.getDesiredConfigByType("bar-type")).andReturn(barConfig);
        org.apache.ambari.server.state.ConfigHelper configHelper = m_injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelper.getDefaultProperties(stack211, "ZOOKEEPER")).andReturn(stack211Configs).anyTimes();
        EasyMock.expect(configHelper.getDefaultProperties(stack220, "ZOOKEEPER")).andReturn(stack220Configs).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> expectedConfigurationsCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(configHelper.createConfigTypes(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class), org.easymock.EasyMock.capture(expectedConfigurationsCapture), org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(true);
        org.apache.ambari.server.orm.entities.ServiceConfigEntity zookeeperServiceConfig = createNiceMock(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        EasyMock.expect(zookeeperServiceConfig.getClusterConfigEntities()).andReturn(com.google.common.collect.Lists.newArrayList(fooConfigEntity, barConfigEntity));
        org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAOMock = m_injector.getInstance(org.apache.ambari.server.orm.dao.ServiceConfigDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> latestServiceConfigs = com.google.common.collect.Lists.newArrayList(zookeeperServiceConfig);
        EasyMock.expect(serviceConfigDAOMock.getLastServiceConfigsForService(org.easymock.EasyMock.anyLong(), EasyMock.eq("ZOOKEEPER"))).andReturn(latestServiceConfigs).once();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        EasyMock.expect(context.getCluster()).andReturn(cluster).atLeastOnce();
        EasyMock.expect(context.getType()).andReturn(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING).atLeastOnce();
        EasyMock.expect(context.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).atLeastOnce();
        EasyMock.expect(context.getRepositoryVersion()).andReturn(repoVersion220).anyTimes();
        EasyMock.expect(context.getSupportedServices()).andReturn(com.google.common.collect.Sets.newHashSet("ZOOKEEPER")).atLeastOnce();
        EasyMock.expect(context.getSourceRepositoryVersion(org.easymock.EasyMock.anyString())).andReturn(repoVersion211).atLeastOnce();
        EasyMock.expect(context.getTargetRepositoryVersion(org.easymock.EasyMock.anyString())).andReturn(repoVersion220).atLeastOnce();
        EasyMock.expect(context.getOrchestrationType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        EasyMock.expect(context.getHostRoleCommandFactory()).andStubReturn(m_injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class));
        EasyMock.expect(context.getRoleGraphFactory()).andStubReturn(m_injector.getInstance(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = m_injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        upgradeHelper.updateDesiredRepositoriesAndConfigs(context);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedConfigurations = expectedConfigurationsCapture.getValue();
        java.util.Map<java.lang.String, java.lang.String> expectedFooType = expectedConfigurations.get("foo-site");
        java.util.Map<java.lang.String, java.lang.String> expectedBarType = expectedConfigurations.get("bar-site");
        org.junit.Assert.assertEquals(2, expectedConfigurations.size());
        org.junit.Assert.assertEquals("my-foo-property-1", expectedFooType.get("foo-property-1"));
        org.junit.Assert.assertEquals(null, expectedFooType.get("foo-property-2"));
        org.junit.Assert.assertEquals("stack-220-original", expectedBarType.get("bar-property-1"));
        org.junit.Assert.assertEquals("stack-220-original", expectedBarType.get("bar-property-2"));
    }

    @org.junit.Test
    public void testReadOnlyPropertyIsTakenFromTargetStack() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion211 = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.apache.ambari.server.state.StackId stack211 = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        org.apache.ambari.server.state.StackId stack220 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        java.lang.String version211 = "2.1.1.0-1234";
        java.lang.String version220 = "2.2.0.0-1234";
        EasyMock.expect(repoVersion211.getStackId()).andReturn(stack211).atLeastOnce();
        EasyMock.expect(repoVersion211.getVersion()).andReturn(version211).atLeastOnce();
        EasyMock.expect(repoVersion220.getStackId()).andReturn(stack220).atLeastOnce();
        EasyMock.expect(repoVersion220.getVersion()).andReturn(version220).atLeastOnce();
        java.lang.String fooSite = "foo-site";
        java.lang.String fooPropertyName = "foo-property-1";
        java.lang.String serviceName = "ZOOKEEPER";
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stack211Configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack211FooType = new java.util.HashMap<>();
        stack211Configs.put(fooSite, stack211FooType);
        stack211FooType.put(fooPropertyName, "stack-211-original");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stack220Configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> stack220FooType = new java.util.HashMap<>();
        stack220Configs.put(fooSite, stack220FooType);
        stack220FooType.put(fooPropertyName, "stack-220-original");
        org.apache.ambari.server.state.PropertyInfo readOnlyProperty = new org.apache.ambari.server.state.PropertyInfo();
        org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfo = new org.apache.ambari.server.state.ValueAttributesInfo();
        valueAttributesInfo.setReadOnly(true);
        readOnlyProperty.setName(fooPropertyName);
        readOnlyProperty.setFilename(fooSite + ".xml");
        readOnlyProperty.setPropertyValueAttributes(null);
        readOnlyProperty.setPropertyValueAttributes(valueAttributesInfo);
        EasyMock.expect(m_metainfo.getServiceProperties(stack211.getStackName(), stack211.getStackVersion(), serviceName)).andReturn(com.google.common.collect.Sets.newHashSet(readOnlyProperty)).atLeastOnce();
        java.util.Map<java.lang.String, java.lang.String> existingFooType = new java.util.HashMap<>();
        org.apache.ambari.server.orm.entities.ClusterConfigEntity fooConfigEntity = createNiceMock(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        EasyMock.expect(fooConfigEntity.getType()).andReturn(fooSite);
        org.apache.ambari.server.state.Config fooConfig = createNiceMock(org.apache.ambari.server.state.Config.class);
        existingFooType.put(fooPropertyName, "my-foo-property-1");
        EasyMock.expect(fooConfig.getType()).andReturn(fooSite).atLeastOnce();
        EasyMock.expect(fooConfig.getProperties()).andReturn(existingFooType);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigurations = new java.util.HashMap<>();
        desiredConfigurations.put(fooSite, null);
        org.apache.ambari.server.state.Service zookeeper = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(zookeeper.getName()).andReturn(serviceName).atLeastOnce();
        EasyMock.expect(zookeeper.getServiceComponents()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>()).once();
        zookeeper.setDesiredRepositoryVersion(repoVersion220);
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stack211).atLeastOnce();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stack220);
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(desiredConfigurations);
        EasyMock.expect(cluster.getDesiredConfigByType(fooSite)).andReturn(fooConfig);
        EasyMock.expect(cluster.getService(serviceName)).andReturn(zookeeper);
        org.apache.ambari.server.state.ConfigHelper configHelper = m_injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelper.getDefaultProperties(stack211, serviceName)).andReturn(stack211Configs).anyTimes();
        EasyMock.expect(configHelper.getDefaultProperties(stack220, serviceName)).andReturn(stack220Configs).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> expectedConfigurationsCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(configHelper.createConfigTypes(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class), org.easymock.EasyMock.capture(expectedConfigurationsCapture), org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(true);
        org.apache.ambari.server.orm.entities.ServiceConfigEntity zookeeperServiceConfig = createNiceMock(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        EasyMock.expect(zookeeperServiceConfig.getClusterConfigEntities()).andReturn(com.google.common.collect.Lists.newArrayList(fooConfigEntity));
        org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAOMock = m_injector.getInstance(org.apache.ambari.server.orm.dao.ServiceConfigDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> latestServiceConfigs = com.google.common.collect.Lists.newArrayList(zookeeperServiceConfig);
        EasyMock.expect(serviceConfigDAOMock.getLastServiceConfigsForService(org.easymock.EasyMock.anyLong(), EasyMock.eq(serviceName))).andReturn(latestServiceConfigs).once();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        EasyMock.expect(context.getCluster()).andReturn(cluster).atLeastOnce();
        EasyMock.expect(context.getType()).andReturn(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING).atLeastOnce();
        EasyMock.expect(context.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).atLeastOnce();
        EasyMock.expect(context.getRepositoryVersion()).andReturn(repoVersion220).anyTimes();
        EasyMock.expect(context.getSupportedServices()).andReturn(com.google.common.collect.Sets.newHashSet(serviceName)).atLeastOnce();
        EasyMock.expect(context.getSourceRepositoryVersion(org.easymock.EasyMock.anyString())).andReturn(repoVersion211).atLeastOnce();
        EasyMock.expect(context.getTargetRepositoryVersion(org.easymock.EasyMock.anyString())).andReturn(repoVersion220).atLeastOnce();
        EasyMock.expect(context.getOrchestrationType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        EasyMock.expect(context.getHostRoleCommandFactory()).andStubReturn(m_injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class));
        EasyMock.expect(context.getRoleGraphFactory()).andStubReturn(m_injector.getInstance(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
        replayAll();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper = m_injector.getInstance(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        upgradeHelper.updateDesiredRepositoriesAndConfigs(context);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> expectedConfigurations = expectedConfigurationsCapture.getValue();
        java.util.Map<java.lang.String, java.lang.String> expectedFooType = expectedConfigurations.get(fooSite);
        org.junit.Assert.assertEquals(1, expectedConfigurations.size());
        org.junit.Assert.assertEquals(1, expectedFooType.size());
        org.junit.Assert.assertEquals("stack-220-original", expectedFooType.get(fooPropertyName));
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.orm.dao.StageDAO stageDAO = createNiceMock(org.apache.ambari.server.orm.dao.StageDAO.class);
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder(StackUpgradeConfigurationMergeTest.this).addActionDBAccessorConfigsBindings().addPasswordEncryptorBindings().build().configure(binder);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
            binder.bind(org.apache.ambari.server.orm.dao.StageDAO.class).toInstance(stageDAO);
            binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
            binder.bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
            binder.bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class));
            binder.bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
            binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class));
            binder.bind(org.apache.ambari.server.controller.spi.ClusterController.class).toInstance(createNiceMock(org.apache.ambari.server.controller.spi.ClusterController.class));
            binder.bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
            binder.bind(org.eclipse.jetty.server.session.SessionHandler.class).toInstance(createNiceMock(org.eclipse.jetty.server.session.SessionHandler.class));
            binder.bind(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
            binder.bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
            binder.bind(org.apache.ambari.server.actionmanager.RequestFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.RequestFactory.class));
            binder.bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class));
            binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
            binder.bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class));
            binder.bind(org.apache.ambari.server.state.ConfigFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigFactory.class));
            binder.bind(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
            binder.bind(org.apache.ambari.server.state.ServiceFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceFactory.class));
            binder.bind(org.apache.ambari.server.state.ServiceComponentFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentFactory.class));
            binder.bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
            binder.bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createNiceMock(org.springframework.security.crypto.password.PasswordEncoder.class));
            binder.bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class));
            binder.bind(org.apache.ambari.server.security.authorization.Users.class).toInstance(createNiceMock(org.apache.ambari.server.security.authorization.Users.class));
            binder.bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
            binder.bind(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class));
            binder.bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
            binder.bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
            binder.bind(org.apache.ambari.server.orm.dao.ServiceConfigDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.ServiceConfigDAO.class));
            binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
            binder.bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
            binder.bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(m_metainfo);
            binder.bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(createNiceMock(org.apache.ambari.server.topology.PersistedState.class));
            binder.bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
            binder.bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
            binder.bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
            binder.bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createNiceMock(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class));
            binder.bind(org.apache.ambari.server.agent.stomp.MetadataHolder.class).toInstance(createNiceMock(org.apache.ambari.server.agent.stomp.MetadataHolder.class));
            binder.bind(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class).toInstance(createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class));
            binder.bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
            binder.bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
            binder.requestStaticInjection(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.class);
            binder.install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
            EasyMock.expect(stageDAO.getLastRequestId()).andReturn(1L).anyTimes();
            EasyMock.replay(stageDAO);
        }
    }
}