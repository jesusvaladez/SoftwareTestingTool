package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
public class UserAuthorizationResourceProviderTest extends org.easymock.EasyMockSupport {
    @org.junit.Before
    public void setup() throws java.lang.Exception {
        resetAll();
    }

    @org.junit.After
    public void cleanup() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
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
    public void testCreateResources() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider(managementController);
        provider.createResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class));
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResources() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider(managementController);
        provider.updateResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class), null);
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider(managementController);
        provider.deleteResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class), null);
        verifyAll();
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.spi.Resource clusterResource = createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(clusterResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME)).andReturn("CLUSTER.DO_SOMETHING").anyTimes();
        EasyMock.expect(clusterResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE)).andReturn("CLUSTER").anyTimes();
        EasyMock.expect(clusterResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME)).andReturn("Cluster Name").anyTimes();
        org.apache.ambari.server.controller.spi.Resource viewResource = createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(viewResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME)).andReturn("VIEW.DO_SOMETHING").anyTimes();
        EasyMock.expect(viewResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE)).andReturn("VIEW").anyTimes();
        EasyMock.expect(viewResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_NAME)).andReturn("View Name").anyTimes();
        EasyMock.expect(viewResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_VERSION)).andReturn("View Version").anyTimes();
        EasyMock.expect(viewResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.INSTANCE_NAME)).andReturn("View Instance Name").anyTimes();
        org.apache.ambari.server.controller.spi.Resource adminResource = createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(adminResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME)).andReturn("ADMIN.DO_SOMETHING").anyTimes();
        EasyMock.expect(adminResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE)).andReturn("ADMIN").anyTimes();
        EasyMock.expect(adminResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME)).andReturn(null).anyTimes();
        org.apache.ambari.server.controller.spi.Resource emptyResource = createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(emptyResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME)).andReturn("EMPTY.DO_SOMETHING").anyTimes();
        EasyMock.expect(emptyResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE)).andReturn("ADMIN").anyTimes();
        EasyMock.expect(emptyResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME)).andReturn(null).anyTimes();
        org.apache.ambari.server.controller.spi.Resource nullResource = createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(nullResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME)).andReturn("NULL.DO_SOMETHING").anyTimes();
        EasyMock.expect(nullResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE)).andReturn("ADMIN").anyTimes();
        EasyMock.expect(nullResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME)).andReturn(null).anyTimes();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> userPrivilegeResources = new java.util.HashSet<>();
        userPrivilegeResources.add(clusterResource);
        userPrivilegeResources.add(viewResource);
        userPrivilegeResources.add(adminResource);
        userPrivilegeResources.add(emptyResource);
        userPrivilegeResources.add(nullResource);
        org.apache.ambari.server.controller.spi.ResourceProvider userPrivilegeProvider = createMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        EasyMock.expect(userPrivilegeProvider.getResources(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class), EasyMock.anyObject(org.apache.ambari.server.controller.spi.Predicate.class))).andReturn(userPrivilegeResources);
        org.apache.ambari.server.controller.spi.ClusterController clusterController = createMock(org.apache.ambari.server.controller.spi.ClusterController.class);
        EasyMock.expect(clusterController.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege)).andReturn(userPrivilegeProvider).anyTimes();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(clusterResourceTypeEntity.getId()).andReturn(1).anyTimes();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity viewResourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(viewResourceTypeEntity.getId()).andReturn(2).anyTimes();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity adminResourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(adminResourceTypeEntity.getId()).andReturn(3).anyTimes();
        org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
        EasyMock.expect(resourceTypeDAO.findByName("CLUSTER")).andReturn(clusterResourceTypeEntity).anyTimes();
        EasyMock.expect(resourceTypeDAO.findByName("VIEW")).andReturn(viewResourceTypeEntity).anyTimes();
        EasyMock.expect(resourceTypeDAO.findByName("ADMIN")).andReturn(adminResourceTypeEntity).anyTimes();
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity clusterRoleAuthorizationEntity = createMock(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        EasyMock.expect(clusterRoleAuthorizationEntity.getAuthorizationId()).andReturn("CLUSTER.DO_SOMETHING").anyTimes();
        EasyMock.expect(clusterRoleAuthorizationEntity.getAuthorizationName()).andReturn("CLUSTER DO_SOMETHING").anyTimes();
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity viewRoleAuthorizationEntity = createMock(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        EasyMock.expect(viewRoleAuthorizationEntity.getAuthorizationId()).andReturn("VIEW.DO_SOMETHING").anyTimes();
        EasyMock.expect(viewRoleAuthorizationEntity.getAuthorizationName()).andReturn("VIEW DO_SOMETHING").anyTimes();
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity adminRoleAuthorizationEntity = createMock(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        EasyMock.expect(adminRoleAuthorizationEntity.getAuthorizationId()).andReturn("ADMIN.DO_SOMETHING").anyTimes();
        EasyMock.expect(adminRoleAuthorizationEntity.getAuthorizationName()).andReturn("ADMIN DO_SOMETHING").anyTimes();
        java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> clusterPermissionAuthorizations = java.util.Collections.singleton(clusterRoleAuthorizationEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> viewPermissionAuthorizations = java.util.Collections.singleton(viewRoleAuthorizationEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> adminPermissionAuthorizations = java.util.Collections.singleton(adminRoleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity clusterPermissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(clusterPermissionEntity.getAuthorizations()).andReturn(clusterPermissionAuthorizations).anyTimes();
        org.apache.ambari.server.orm.entities.PermissionEntity viewPermissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(viewPermissionEntity.getAuthorizations()).andReturn(viewPermissionAuthorizations).anyTimes();
        org.apache.ambari.server.orm.entities.PermissionEntity adminPermissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(adminPermissionEntity.getAuthorizations()).andReturn(adminPermissionAuthorizations).anyTimes();
        org.apache.ambari.server.orm.entities.PermissionEntity emptyPermissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(emptyPermissionEntity.getAuthorizations()).andReturn(java.util.Collections.emptyList()).anyTimes();
        org.apache.ambari.server.orm.entities.PermissionEntity nullPermissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(nullPermissionEntity.getAuthorizations()).andReturn(null).anyTimes();
        org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("CLUSTER.DO_SOMETHING", clusterResourceTypeEntity)).andReturn(clusterPermissionEntity).anyTimes();
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("VIEW.DO_SOMETHING", viewResourceTypeEntity)).andReturn(viewPermissionEntity).anyTimes();
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("ADMIN.DO_SOMETHING", adminResourceTypeEntity)).andReturn(adminPermissionEntity).anyTimes();
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("EMPTY.DO_SOMETHING", adminResourceTypeEntity)).andReturn(emptyPermissionEntity).anyTimes();
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("NULL.DO_SOMETHING", adminResourceTypeEntity)).andReturn(nullPermissionEntity).anyTimes();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.init(permissionDAO, resourceTypeDAO);
        org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider(managementController);
        setClusterController(provider, clusterController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.USERNAME_PROPERTY_ID).equals(requestedUsername).toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        java.util.LinkedList<java.lang.String> expectedIds = new java.util.LinkedList<>();
        expectedIds.add("CLUSTER.DO_SOMETHING");
        expectedIds.add("VIEW.DO_SOMETHING");
        expectedIds.add("ADMIN.DO_SOMETHING");
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String authorizationId = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID)));
            switch (authorizationId) {
                case "CLUSTER.DO_SOMETHING" :
                    org.junit.Assert.assertEquals("CLUSTER DO_SOMETHING", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID));
                    org.junit.Assert.assertEquals("CLUSTER", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID));
                    org.junit.Assert.assertEquals("Cluster Name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_CLUSTER_NAME_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_NAME_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_VERSION_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_INSTANCE_NAME_PROPERTY_ID));
                    break;
                case "VIEW.DO_SOMETHING" :
                    org.junit.Assert.assertEquals("VIEW DO_SOMETHING", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID));
                    org.junit.Assert.assertEquals("VIEW", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID));
                    org.junit.Assert.assertEquals("View Name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_NAME_PROPERTY_ID));
                    org.junit.Assert.assertEquals("View Version", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_VERSION_PROPERTY_ID));
                    org.junit.Assert.assertEquals("View Instance Name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_INSTANCE_NAME_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_CLUSTER_NAME_PROPERTY_ID));
                    break;
                case "ADMIN.DO_SOMETHING" :
                    org.junit.Assert.assertEquals("ADMIN DO_SOMETHING", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID));
                    org.junit.Assert.assertEquals("ADMIN", resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_CLUSTER_NAME_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_NAME_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_VERSION_PROPERTY_ID));
                    org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_INSTANCE_NAME_PROPERTY_ID));
                    break;
            }
            expectedIds.remove();
        }
        org.junit.Assert.assertEquals(0, expectedIds.size());
        verifyAll();
    }

    private void setClusterController(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider provider, org.apache.ambari.server.controller.spi.ClusterController clusterController) throws java.lang.Exception {
        java.lang.Class<?> c = provider.getClass();
        java.lang.reflect.Field f = c.getDeclaredField("clusterController");
        f.setAccessible(true);
        f.set(provider, clusterController);
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stageplanner.RoleGraphFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.topology.tasks.ConfigureClusterTaskFactory.class));
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
                bind(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class));
                bind(org.apache.ambari.server.orm.dao.PermissionDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PermissionDAO.class));
                bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.topology.PersistedState.class).to(org.apache.ambari.server.topology.PersistedStateImpl.class);
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent>>() {}).annotatedWith(com.google.inject.name.Names.named("AgentConfigEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
            }
        });
    }
}