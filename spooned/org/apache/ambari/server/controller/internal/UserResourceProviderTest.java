package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
public class UserResourceProviderTest extends org.easymock.EasyMockSupport {
    private static final long CREATE_TIME = java.util.Calendar.getInstance().getTime().getTime();

    @org.junit.Before
    public void resetMocks() {
        resetAll();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResources_Administrator() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "User100");
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID, "user100");
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID, "User 100");
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), java.util.Collections.singleton(resource));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_NonAdministrator() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "User100");
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID, "user100");
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID, "User 100");
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), java.util.Collections.singleton(resource));
    }

    @org.junit.Test
    public void testCreateResources_Multiple() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> resource1 = new java.util.HashMap<>();
        resource1.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "User100");
        java.util.Map<java.lang.String, java.lang.Object> resource2 = new java.util.HashMap<>();
        resource2.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "User200");
        java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> resourceProperties = new java.util.HashSet<>();
        resourceProperties.add(resource1);
        resourceProperties.add(resource2);
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), resourceProperties);
    }

    @org.junit.Test
    public void testCreateResources_SetPassword() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "User100");
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, "password100");
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), java.util.Collections.singleton(resource));
    }

    @org.junit.Test
    public void testCreateResources_SetAdmin() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "User100");
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID, true);
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), java.util.Collections.singleton(resource));
    }

    @org.junit.Test
    public void testCreateResources_SetInactive() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "User100");
        resource.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, false);
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), java.util.Collections.singleton(resource));
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), null);
    }

    @org.junit.Test
    public void testGetResources_NonAdministrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), null);
    }

    @org.junit.Test
    public void testGetResource_Administrator_Self() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testGetResource_Administrator_Other() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test
    public void testGetResource_NonAdministrator_Self() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResource_NonAdministrator_Other() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateAdmin_Administrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateAdmin(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateAdmin_Administrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateAdmin(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdateAdmin_NonAdministrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateAdmin(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdateAdmin_NonAdministrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateAdmin(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateActive_Administrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateActive(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateActive_Administrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateActive(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdateActive_NonAdministrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateActive(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdateActive_NonAdministrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateActive(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateDisplayName_Administrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateDisplayName(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateDisplayName_Administrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateDisplayName(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateDisplayName_NonAdministrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateDisplayName(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdateDisplayName_NonAdministrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateDisplayName(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateLocalUserName_Administrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateLocalUserName(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testUpdateResources_UpdateLocalUserName_Administrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateLocalUserName(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdateLocalUserName_NonAdministrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdateLocalUserName(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdateLocalUserName_NonAdministrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdateLocalUserName(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_UpdatePassword_Administrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testUpdateResources_UpdatePassword_Administrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_UpdatePassword_NonAdministrator_Self() throws java.lang.Exception {
        testUpdateResources_UpdatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_UpdatePassword_NonAdministrator_Other() throws java.lang.Exception {
        testUpdateResources_UpdatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_CreatePassword_Administrator_Self() throws java.lang.Exception {
        testUpdateResources_CreatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testUpdateResources_CreatePassword_Administrator_Other() throws java.lang.Exception {
        testUpdateResources_CreatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_CreatePassword_NonAdministrator_Self() throws java.lang.Exception {
        testUpdateResources_CreatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_CreatePassword_NonAdministrator_Other() throws java.lang.Exception {
        testUpdateResources_CreatePassword(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testDeleteResource_Administrator_Self() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testDeleteResource_Administrator_Other() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResource_NonAdministrator_Self() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResource_NonAdministrator_Other() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected <T> com.google.inject.Provider<T> getProvider(java.lang.Class<T> type) {
                return super.getProvider(type);
            }

            @java.lang.Override
            protected void configure() {
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
                bind(javax.persistence.EntityManager.class).toInstance(createMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).toInstance(createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class));
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(createMock(org.apache.ambari.server.actionmanager.ActionManager.class));
                bind(org.apache.ambari.server.actionmanager.RequestFactory.class).toInstance(createMock(org.apache.ambari.server.actionmanager.RequestFactory.class));
                bind(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class).toInstance(createMock(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class));
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).toInstance(createMock(org.apache.ambari.server.actionmanager.StageFactory.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).toInstance(createMock(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.state.ConfigFactory.class).toInstance(createMock(org.apache.ambari.server.state.ConfigFactory.class));
                bind(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class).toInstance(createMock(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class));
                bind(org.apache.ambari.server.state.ServiceFactory.class).toInstance(createMock(org.apache.ambari.server.state.ServiceFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentFactory.class).toInstance(createMock(org.apache.ambari.server.state.ServiceComponentFactory.class));
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(createMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createMock(org.apache.ambari.server.controller.KerberosHelper.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createMock(org.apache.ambari.server.controller.AmbariManagementController.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.topology.PersistedState.class).to(org.apache.ambari.server.topology.PersistedStateImpl.class);
                bind(org.apache.ambari.server.orm.dao.UserDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.UserDAO.class));
                bind(org.apache.ambari.server.orm.dao.UserAuthenticationDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.UserAuthenticationDAO.class));
                bind(org.apache.ambari.server.orm.dao.GroupDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.GroupDAO.class));
                bind(org.apache.ambari.server.orm.dao.MemberDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.MemberDAO.class));
                bind(org.apache.ambari.server.orm.dao.PrincipalDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrincipalDAO.class));
                bind(org.apache.ambari.server.orm.dao.PermissionDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PermissionDAO.class));
                bind(org.apache.ambari.server.orm.dao.PrivilegeDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class));
                bind(org.apache.ambari.server.orm.dao.ResourceDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.ResourceDAO.class));
                bind(org.apache.ambari.server.orm.dao.PrincipalTypeDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrincipalTypeDAO.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
            }
        });
    }

    private void createResourcesTest(org.springframework.security.core.Authentication authentication, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> resourceProperties) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        org.easymock.Capture<? extends org.apache.ambari.server.orm.entities.UserEntity> userEntityCapture = EasyMock.newCapture(CaptureType.ALL);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> expectedUsers = new java.util.HashMap<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : resourceProperties) {
            java.lang.String username = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID)));
            if (!org.apache.commons.lang.StringUtils.isEmpty(username)) {
                org.junit.Assert.assertFalse("User names must be unique for this test case", expectedUsers.containsKey(username.toLowerCase()));
                EasyMock.expect(userDAO.findUserByName(username)).andReturn(null).times(2);
                userDAO.create(EasyMock.capture(userEntityCapture));
                EasyMock.expectLastCall().once();
                org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
                org.apache.ambari.server.orm.dao.PrincipalTypeDAO principalTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalTypeDAO.class);
                EasyMock.expect(principalTypeDAO.findById(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE)).andReturn(principalTypeEntity).once();
                org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class);
                principalDAO.create(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class));
                EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
                    @java.lang.Override
                    public java.lang.Object answer() throws java.lang.Throwable {
                        java.lang.Object[] args = EasyMock.getCurrentArguments();
                        ((org.apache.ambari.server.orm.entities.PrincipalEntity) (args[0])).setId(1L);
                        return null;
                    }
                }).once();
                org.apache.ambari.server.hooks.HookContextFactory hookContextFactory = injector.getInstance(org.apache.ambari.server.hooks.HookContextFactory.class);
                EasyMock.expect(hookContextFactory.createUserHookContext(username)).andReturn(null).once();
                org.apache.ambari.server.hooks.HookService hookService = injector.getInstance(org.apache.ambari.server.hooks.HookService.class);
                EasyMock.expect(hookService.execute(EasyMock.anyObject(org.apache.ambari.server.hooks.HookContext.class))).andReturn(true).once();
                if (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID) != null) {
                    org.apache.ambari.server.controller.ResourceProviderFactory factory = createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
                    org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = createMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
                    org.apache.ambari.server.controller.spi.RequestStatus status = createMock(org.apache.ambari.server.controller.spi.RequestStatus.class);
                    EasyMock.expect(resourceProvider.createResources(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class))).andReturn(status).once();
                    EasyMock.expect(factory.getUserAuthenticationSourceResourceProvider()).andReturn(resourceProvider).once();
                    org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
                }
                if (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID) != null) {
                    java.lang.Boolean isAdmin = java.lang.Boolean.TRUE.equals(properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID));
                    if (isAdmin) {
                        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
                        org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
                        EasyMock.expect(permissionDAO.findAmbariAdminPermission()).andReturn(permissionEntity).once();
                        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
                        org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class);
                        EasyMock.expect(resourceDAO.findAmbariResource()).andReturn(resourceEntity).once();
                        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
                        privilegeDAO.create(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrivilegeEntity.class));
                        EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
                            @java.lang.Override
                            public java.lang.Object answer() throws java.lang.Throwable {
                                java.lang.Object[] args = EasyMock.getCurrentArguments();
                                ((org.apache.ambari.server.orm.entities.PrivilegeEntity) (args[0])).setId(1);
                                return null;
                            }
                        }).once();
                        EasyMock.expect(principalDAO.merge(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(null).once();
                        EasyMock.expect(userDAO.merge(EasyMock.anyObject(org.apache.ambari.server.orm.entities.UserEntity.class))).andReturn(null).once();
                    }
                }
                expectedUsers.put(username.toLowerCase(), properties);
            }
        }
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(resourceProperties, null);
        provider.createResources(request);
        verifyAll();
        java.util.List<? extends org.apache.ambari.server.orm.entities.UserEntity> capturedUserEntities = userEntityCapture.getValues();
        org.junit.Assert.assertEquals(expectedUsers.size(), capturedUserEntities.size());
        for (org.apache.ambari.server.orm.entities.UserEntity userEntity : capturedUserEntities) {
            java.lang.String userName = userEntity.getUserName();
            java.util.Map<java.lang.String, java.lang.Object> userProperties = expectedUsers.get(userName);
            org.junit.Assert.assertNotNull(userProperties);
            java.lang.String username = ((java.lang.String) (userProperties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID)));
            java.lang.String displayName = ((java.lang.String) (userProperties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID)));
            java.lang.String localUsername = ((java.lang.String) (userProperties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID)));
            java.lang.Boolean isActive = (userProperties.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID)) ? !java.lang.Boolean.FALSE.equals(userProperties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID)) : java.lang.Boolean.TRUE;
            org.junit.Assert.assertEquals(username.toLowerCase(), userEntity.getUserName());
            org.junit.Assert.assertEquals(org.apache.commons.lang.StringUtils.defaultIfEmpty(localUsername, username), userEntity.getLocalUsername());
            org.junit.Assert.assertEquals(org.apache.commons.lang.StringUtils.defaultIfEmpty(displayName, username), userEntity.getDisplayName());
            org.junit.Assert.assertEquals(isActive, userEntity.getActive());
        }
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        java.lang.String username = requestedUsername;
        if (username == null) {
            if (!"admin".equals(authentication.getName())) {
                username = authentication.getName();
            }
        }
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        org.apache.ambari.server.orm.entities.PrincipalEntity userPrincipalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(userPrincipalEntity.getPrivileges()).andReturn(null).anyTimes();
        if (username == null) {
            org.apache.ambari.server.orm.entities.UserEntity userEntity1 = createMockUserEntity("User1");
            EasyMock.expect(userEntity1.getPrincipal()).andReturn(userPrincipalEntity).once();
            org.apache.ambari.server.orm.entities.UserEntity userEntity10 = createMockUserEntity("User10");
            EasyMock.expect(userEntity10.getPrincipal()).andReturn(userPrincipalEntity).once();
            org.apache.ambari.server.orm.entities.UserEntity userEntity100 = createMockUserEntity("User100");
            EasyMock.expect(userEntity100.getPrincipal()).andReturn(userPrincipalEntity).once();
            org.apache.ambari.server.orm.entities.UserEntity userEntityAdmin = createMockUserEntity("admin");
            EasyMock.expect(userEntityAdmin.getPrincipal()).andReturn(userPrincipalEntity).once();
            java.util.List<org.apache.ambari.server.orm.entities.UserEntity> allUsers = java.util.Arrays.asList(userEntity1, userEntity10, userEntity100, userEntityAdmin);
            EasyMock.expect(userDAO.findAll()).andReturn(allUsers).once();
        } else {
            org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(username);
            EasyMock.expect(userEntity.getPrincipal()).andReturn(userPrincipalEntity).once();
            EasyMock.expect(userDAO.findUserByName(username)).andReturn(userEntity).once();
        }
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, requestedUsername == null ? null : createPredicate(requestedUsername));
        if (username == null) {
            java.util.List<java.lang.String> expectedList = java.util.Arrays.asList("User1", "User10", "User100", "admin");
            org.junit.Assert.assertEquals(4, resources.size());
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                java.lang.String userName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID)));
                org.junit.Assert.assertTrue(expectedList.contains(userName));
            }
        } else {
            org.junit.Assert.assertEquals(1, resources.size());
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                org.junit.Assert.assertEquals(username, resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID));
            }
        }
        verifyAll();
    }

    private void testUpdateResources_UpdateAdmin(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        updateResourcesTest(authentication, requestedUsername, java.util.Collections.<java.lang.String, java.lang.Object>singletonMap(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID, true), false);
    }

    private void testUpdateResources_UpdateActive(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        updateResourcesTest(authentication, requestedUsername, java.util.Collections.<java.lang.String, java.lang.Object>singletonMap(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, false), false);
    }

    private void testUpdateResources_UpdateDisplayName(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        updateResourcesTest(authentication, requestedUsername, java.util.Collections.<java.lang.String, java.lang.Object>singletonMap(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID, "Updated Display Name"), false);
    }

    private void testUpdateResources_UpdateLocalUserName(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        updateResourcesTest(authentication, requestedUsername, java.util.Collections.<java.lang.String, java.lang.Object>singletonMap(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID, "updated_username"), false);
    }

    private void testUpdateResources_UpdatePassword(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_OLD_PASSWORD_PROPERTY_ID, "old_password");
        properties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, "new_password");
        updateResourcesTest(authentication, requestedUsername, properties, true);
    }

    private void testUpdateResources_CreatePassword(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_OLD_PASSWORD_PROPERTY_ID, "old_password");
        properties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, "new_password");
        updateResourcesTest(authentication, requestedUsername, properties, false);
    }

    private void updateResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername, java.util.Map<java.lang.String, java.lang.Object> updates, boolean passwordAlreadyExists) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Request> requestCapture = EasyMock.newCapture(CaptureType.FIRST);
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Predicate> predicateCapture = EasyMock.newCapture(CaptureType.FIRST);
        boolean hasUpdates = false;
        org.apache.ambari.server.controller.ResourceProviderFactory factory = createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserName()).andReturn(requestedUsername).anyTimes();
        EasyMock.expect(userEntity.getActive()).andReturn(true).anyTimes();
        EasyMock.expect(userEntity.getDisplayName()).andReturn(requestedUsername).anyTimes();
        EasyMock.expect(userEntity.getLocalUsername()).andReturn(requestedUsername).anyTimes();
        if (updates.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID)) {
            userEntity.setDisplayName(((java.lang.String) (updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID))));
            EasyMock.expectLastCall().once();
            hasUpdates = true;
        }
        if (updates.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID)) {
            userEntity.setLocalUsername(((java.lang.String) (updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID))));
            EasyMock.expectLastCall().once();
            hasUpdates = true;
        }
        if (updates.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID)) {
            userEntity.setActive(((java.lang.Boolean) (updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID))));
            EasyMock.expectLastCall().once();
            hasUpdates = true;
        }
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).once();
        if (hasUpdates) {
            EasyMock.expect(userDAO.merge(userEntity)).andReturn(userEntity).once();
        }
        if (updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID) != null) {
            java.lang.Boolean isAdmin = java.lang.Boolean.TRUE.equals(updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID));
            if (isAdmin) {
                org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
                org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
                EasyMock.expect(permissionDAO.findAmbariAdminPermission()).andReturn(permissionEntity).once();
                org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
                org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class);
                EasyMock.expect(resourceDAO.findAmbariResource()).andReturn(resourceEntity).once();
                org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
                privilegeDAO.create(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrivilegeEntity.class));
                EasyMock.expectLastCall().andAnswer(new org.easymock.IAnswer<java.lang.Object>() {
                    @java.lang.Override
                    public java.lang.Object answer() throws java.lang.Throwable {
                        java.lang.Object[] args = EasyMock.getCurrentArguments();
                        ((org.apache.ambari.server.orm.entities.PrivilegeEntity) (args[0])).setId(1);
                        return null;
                    }
                }).once();
                org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class);
                EasyMock.expect(principalDAO.merge(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(null).once();
                org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
                EasyMock.expect(principalEntity.getPrivileges()).andReturn(new java.util.HashSet<org.apache.ambari.server.orm.entities.PrivilegeEntity>()).anyTimes();
                EasyMock.expect(userEntity.getPrincipal()).andReturn(principalEntity).anyTimes();
                EasyMock.expect(userDAO.merge(EasyMock.anyObject(org.apache.ambari.server.orm.entities.UserEntity.class))).andReturn(null).once();
            }
        }
        if (updates.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID)) {
            if (passwordAlreadyExists) {
                org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = createMock(org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
                EasyMock.expect(authenticationEntity.getUserAuthenticationId()).andReturn(100L).anyTimes();
                EasyMock.expect(authenticationEntity.getAuthenticationType()).andReturn(org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL).anyTimes();
                EasyMock.expect(userEntity.getAuthenticationEntities()).andReturn(java.util.Collections.singletonList(authenticationEntity)).once();
            } else {
                EasyMock.expect(userEntity.getAuthenticationEntities()).andReturn(java.util.Collections.<org.apache.ambari.server.orm.entities.UserAuthenticationEntity>emptyList()).once();
            }
            org.apache.ambari.server.controller.spi.RequestStatus status = createMock(org.apache.ambari.server.controller.spi.RequestStatus.class);
            org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = createMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
            if (passwordAlreadyExists) {
                EasyMock.expect(resourceProvider.updateResources(EasyMock.capture(requestCapture), EasyMock.capture(predicateCapture))).andReturn(status).once();
            } else {
                EasyMock.expect(resourceProvider.createResources(EasyMock.capture(requestCapture))).andReturn(status).once();
            }
            EasyMock.expect(factory.getUserAuthenticationSourceResourceProvider()).andReturn(resourceProvider).once();
        }
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.apache.ambari.server.view.ViewRegistry.initInstance(new org.apache.ambari.server.view.ViewRegistry(publisher));
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(updates, null);
        provider.updateResources(request, createPredicate(requestedUsername));
        verifyAll();
        if (updates.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID)) {
            org.apache.ambari.server.controller.spi.Request capturedRequest = requestCapture.getValue();
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> capturedProperties = capturedRequest.getProperties();
            java.util.Map<java.lang.String, java.lang.Object> properties = capturedProperties.iterator().next();
            org.junit.Assert.assertNotNull(capturedProperties);
            if (passwordAlreadyExists) {
                org.junit.Assert.assertEquals(updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID), properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID));
                org.junit.Assert.assertEquals(updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_OLD_PASSWORD_PROPERTY_ID), properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_OLD_KEY_PROPERTY_ID));
            } else {
                org.junit.Assert.assertEquals(updates.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID), properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID));
                org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL.name(), properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID));
                org.junit.Assert.assertEquals(requestedUsername, properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID));
            }
            if (passwordAlreadyExists) {
                org.apache.ambari.server.controller.spi.Predicate capturedPredicate = predicateCapture.getValue();
                org.junit.Assert.assertEquals(org.apache.ambari.server.controller.predicate.AndPredicate.class, capturedPredicate.getClass());
                org.apache.ambari.server.controller.predicate.AndPredicate andPredicate = ((org.apache.ambari.server.controller.predicate.AndPredicate) (capturedPredicate));
                org.apache.ambari.server.controller.spi.Predicate[] predicates = andPredicate.getPredicates();
                org.junit.Assert.assertEquals(2, predicates.length);
                for (org.apache.ambari.server.controller.spi.Predicate p : predicates) {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.controller.predicate.EqualsPredicate.class, p.getClass());
                    org.apache.ambari.server.controller.predicate.EqualsPredicate equalsPredicate = ((org.apache.ambari.server.controller.predicate.EqualsPredicate) (p));
                    if (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID.equals(equalsPredicate.getPropertyId())) {
                        org.junit.Assert.assertEquals(requestedUsername, equalsPredicate.getValue());
                    } else if (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID.equals(equalsPredicate.getPropertyId())) {
                        org.junit.Assert.assertEquals("100", equalsPredicate.getValue());
                    }
                }
            } else {
                org.junit.Assert.assertFalse(predicateCapture.hasCaptured());
            }
        }
    }

    private void deleteResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(requestedUsername);
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> adminPrincipals = java.util.Collections.singletonList(createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class));
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> adminUserEntities = new java.util.ArrayList<>();
        adminUserEntities.add(createMockUserEntity("some admin"));
        if ("admin".equals(requestedUsername)) {
            adminUserEntities.add(userEntity);
        }
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).once();
        EasyMock.expect(userDAO.findUsersByPrincipal(adminPrincipals)).andReturn(adminUserEntities).once();
        userDAO.remove(userEntity);
        EasyMock.expectLastCall().atLeastOnce();
        org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class);
        EasyMock.expect(principalDAO.findByPermissionId(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION)).andReturn(adminPrincipals).once();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), createPredicate(requestedUsername));
        verifyAll();
    }

    private org.apache.ambari.server.controller.spi.Predicate createPredicate(java.lang.String requestedUsername) {
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).equals(requestedUsername).toPredicate();
    }

    private org.apache.ambari.server.orm.entities.UserEntity createMockUserEntity(java.lang.String username) {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserId()).andReturn(username.hashCode()).anyTimes();
        EasyMock.expect(userEntity.getUserName()).andReturn(username).anyTimes();
        EasyMock.expect(userEntity.getLocalUsername()).andReturn(username).anyTimes();
        EasyMock.expect(userEntity.getDisplayName()).andReturn(username).anyTimes();
        EasyMock.expect(userEntity.getActive()).andReturn(true).anyTimes();
        EasyMock.expect(userEntity.getCreateTime()).andReturn(org.apache.ambari.server.controller.internal.UserResourceProviderTest.CREATE_TIME).anyTimes();
        EasyMock.expect(userEntity.getConsecutiveFailures()).andReturn(0).anyTimes();
        EasyMock.expect(userEntity.getAuthenticationEntities()).andReturn(java.util.Collections.<org.apache.ambari.server.orm.entities.UserAuthenticationEntity>emptyList()).anyTimes();
        EasyMock.expect(userEntity.getMemberEntities()).andReturn(java.util.Collections.<org.apache.ambari.server.orm.entities.MemberEntity>emptySet()).anyTimes();
        return userEntity;
    }

    private org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(com.google.inject.Injector injector) {
        org.apache.ambari.server.controller.internal.UserResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.UserResourceProvider(injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class));
        injector.injectMembers(resourceProvider);
        return resourceProvider;
    }
}