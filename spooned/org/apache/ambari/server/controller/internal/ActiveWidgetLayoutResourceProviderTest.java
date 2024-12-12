package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
@org.junit.Ignore
public class ActiveWidgetLayoutResourceProviderTest extends org.easymock.EasyMockSupport {
    @org.junit.Before
    public void before() {
        resetAll();
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test
    public void testGetResources_NonAdministrator_Self() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_NonAdministrator_Other() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User10");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources_Administrator() throws java.lang.Exception {
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources_NonAdministrator_Self() throws java.lang.Exception {
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources_NonAdministrator_Other() throws java.lang.Exception {
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User10");
    }

    @org.junit.Test
    public void testUpdateResources_Administrator() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test
    public void testUpdateResources_NonAdministrator_Self() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test
    public void testUpdateResources_NoUserName_Self() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1", false);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_NonAdministrator_Other() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User10");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources_Administrator() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources_NonAdministrator_Self() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources_NonAdministrator_Other() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User10");
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(requestedUsername);
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO = injector.getInstance(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class);
        EasyMock.expect(widgetLayoutDAO.findById(1L)).andReturn(createMockWidgetLayout(1L, requestedUsername)).atLeastOnce();
        EasyMock.expect(widgetLayoutDAO.findById(2L)).andReturn(createMockWidgetLayout(2L, requestedUsername)).atLeastOnce();
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").atLeastOnce();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getClusterById(2L)).andReturn(cluster).atLeastOnce();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector, managementController);
        java.util.Set<java.lang.String> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, createPredicate(requestedUsername));
        org.junit.Assert.assertEquals(2, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals("section" + id, resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_SECTION_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("CLUSTER", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_SCOPE_PROPERTY_ID));
            org.junit.Assert.assertEquals(requestedUsername, resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("display name" + id, resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_DISPLAY_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("layout name" + id, resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_LAYOUT_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("[]", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_WIDGETS_PROPERTY_ID).toString());
        }
        verifyAll();
    }

    private void createResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID, requestedUsername);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        verifyAll();
    }

    private void updateResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        updateResourcesTest(authentication, requestedUsername, true);
    }

    private void updateResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername, boolean setUserName) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.easymock.Capture<? extends java.lang.String> widgetLayoutJsonCapture = EasyMock.newCapture();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(requestedUsername);
        userEntity.setActiveWidgetLayouts(EasyMock.capture(widgetLayoutJsonCapture));
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).atLeastOnce();
        EasyMock.expect(userDAO.merge(userEntity)).andReturn(userEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO = injector.getInstance(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class);
        EasyMock.expect(widgetLayoutDAO.findById(1L)).andReturn(createMockWidgetLayout(1L, requestedUsername)).atLeastOnce();
        EasyMock.expect(widgetLayoutDAO.findById(2L)).andReturn(createMockWidgetLayout(2L, requestedUsername)).atLeastOnce();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> widgetLayouts = new java.util.HashSet<>();
        java.util.HashMap<java.lang.String, java.lang.String> layout;
        layout = new java.util.HashMap<>();
        layout.put("id", "1");
        widgetLayouts.add(layout);
        layout = new java.util.HashMap<>();
        layout.put("id", "2");
        widgetLayouts.add(layout);
        java.util.HashMap<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT, widgetLayouts);
        if (setUserName) {
            requestProps.put(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID, requestedUsername);
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector, managementController);
        provider.updateResources(request, createPredicate(requestedUsername));
        verifyAll();
        java.lang.String json = widgetLayoutJsonCapture.getValue();
        org.junit.Assert.assertNotNull(json);
        java.util.Set capturedWidgetLayouts = new com.google.gson.Gson().fromJson(json, widgetLayouts.getClass());
        org.junit.Assert.assertEquals(widgetLayouts, capturedWidgetLayouts);
    }

    private void deleteResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(requestedUsername);
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).atLeastOnce();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector, managementController);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), createPredicate(requestedUsername));
        verifyAll();
    }

    private org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(com.google.inject.Injector injector, org.apache.ambari.server.controller.AmbariManagementController managementController) throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.WidgetDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class), new com.google.gson.Gson());
        return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout, managementController);
    }

    private org.apache.ambari.server.controller.spi.Predicate createPredicate(java.lang.String requestedUsername) {
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider.WIDGETLAYOUT_USERNAME_PROPERTY_ID).equals(requestedUsername).toPredicate();
    }

    private org.apache.ambari.server.orm.entities.WidgetLayoutEntity createMockWidgetLayout(java.lang.Long id, java.lang.String username) {
        org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity = createMock(org.apache.ambari.server.orm.entities.WidgetLayoutEntity.class);
        EasyMock.expect(widgetLayoutEntity.getId()).andReturn(id).anyTimes();
        EasyMock.expect(widgetLayoutEntity.getUserName()).andReturn(username).anyTimes();
        EasyMock.expect(widgetLayoutEntity.getLayoutName()).andReturn("layout name" + id).anyTimes();
        EasyMock.expect(widgetLayoutEntity.getSectionName()).andReturn("section" + id).anyTimes();
        EasyMock.expect(widgetLayoutEntity.getScope()).andReturn("CLUSTER").anyTimes();
        EasyMock.expect(widgetLayoutEntity.getDisplayName()).andReturn("display name" + id).anyTimes();
        EasyMock.expect(widgetLayoutEntity.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(widgetLayoutEntity.getListWidgetLayoutUserWidgetEntity()).andReturn(java.util.Collections.emptyList()).anyTimes();
        return widgetLayoutEntity;
    }

    private org.apache.ambari.server.orm.entities.UserEntity createMockUserEntity(java.lang.String username) {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserId()).andReturn(username.hashCode()).anyTimes();
        EasyMock.expect(userEntity.getUserName()).andReturn(username).anyTimes();
        EasyMock.expect(userEntity.getActiveWidgetLayouts()).andReturn("[{\"id\":\"1\"},{\"id\":\"2\"}]").anyTimes();
        return userEntity;
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
                bind(org.apache.ambari.server.topology.PersistedState.class).to(org.apache.ambari.server.topology.PersistedStateImpl.class);
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class));
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class));
                bind(org.apache.ambari.server.actionmanager.RequestFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.RequestFactory.class));
                bind(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).toInstance(createNiceMock(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.state.ConfigFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ConfigFactory.class));
                bind(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
                bind(org.apache.ambari.server.state.ServiceFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createNiceMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class));
                bind(org.apache.ambari.server.security.authorization.Users.class).toInstance(createMock(org.apache.ambari.server.security.authorization.Users.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).to(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class);
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
                bind(org.apache.ambari.server.orm.dao.UserDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.UserDAO.class));
                bind(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class));
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
            }
        });
    }
}