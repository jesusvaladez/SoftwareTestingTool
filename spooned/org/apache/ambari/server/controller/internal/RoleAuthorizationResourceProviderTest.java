package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.reset;
public class RoleAuthorizationResourceProviderTest extends org.easymock.EasyMockSupport {
    private static com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() {
        EasyMock.reset();
        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.controller.AmbariManagementController managementController = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(managementController);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.dao.PermissionDAO.class).toInstance(createStrictMock(org.apache.ambari.server.orm.dao.PermissionDAO.class));
                bind(org.apache.ambari.server.orm.dao.RoleAuthorizationDAO.class).toInstance(createStrictMock(org.apache.ambari.server.orm.dao.RoleAuthorizationDAO.class));
            }
        }));
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorizationEntity = createNiceMock(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        EasyMock.expect(roleAuthorizationEntity.getAuthorizationId()).andReturn("TEST.DO_SOMETHING");
        EasyMock.expect(roleAuthorizationEntity.getAuthorizationName()).andReturn("Do Something");
        java.util.List<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizationEntities = new java.util.ArrayList<>();
        authorizationEntities.add(roleAuthorizationEntity);
        org.apache.ambari.server.orm.dao.RoleAuthorizationDAO roleAuthorizationDAO = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.RoleAuthorizationDAO.class);
        EasyMock.expect(roleAuthorizationDAO.findAll()).andReturn(authorizationEntities);
        replayAll();
        org.apache.ambari.server.controller.AmbariManagementController managementController = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider(managementController);
        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.injectMembers(provider);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("TEST.DO_SOMETHING", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID));
        org.junit.Assert.assertEquals("Do Something", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID));
        verifyAll();
    }

    @org.junit.Test
    public void testGetResourcesForPermission() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorizationEntity = createNiceMock(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        EasyMock.expect(roleAuthorizationEntity.getAuthorizationId()).andReturn("TEST.DO_SOMETHING").once();
        EasyMock.expect(roleAuthorizationEntity.getAuthorizationName()).andReturn("Do Something").once();
        java.util.List<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizationEntities = new java.util.ArrayList<>();
        authorizationEntities.add(roleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntry = createStrictMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntry.getAuthorizations()).andReturn(authorizationEntities).once();
        org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
        EasyMock.expect(permissionDAO.findById(1)).andReturn(permissionEntry).once();
        replayAll();
        org.apache.ambari.server.controller.AmbariManagementController managementController = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID).equals("1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("TEST.DO_SOMETHING", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID));
        org.junit.Assert.assertEquals("Do Something", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID));
        verifyAll();
    }

    @org.junit.Test
    public void testGetResource() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorizationEntity1 = createNiceMock(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        EasyMock.expect(roleAuthorizationEntity1.getAuthorizationId()).andReturn("TEST.DO_SOMETHING").anyTimes();
        EasyMock.expect(roleAuthorizationEntity1.getAuthorizationName()).andReturn("Do Something").anyTimes();
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorizationEntity2 = createNiceMock(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        EasyMock.expect(roleAuthorizationEntity2.getAuthorizationId()).andReturn("TEST.DO_SOMETHING_ELSE").anyTimes();
        EasyMock.expect(roleAuthorizationEntity2.getAuthorizationName()).andReturn("Do Something Else").anyTimes();
        java.util.List<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizationEntities = new java.util.ArrayList<>();
        authorizationEntities.add(roleAuthorizationEntity1);
        authorizationEntities.add(roleAuthorizationEntity2);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntry = createStrictMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntry.getAuthorizations()).andReturn(authorizationEntities).once();
        org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
        EasyMock.expect(permissionDAO.findById(1)).andReturn(permissionEntry).once();
        replayAll();
        org.apache.ambari.server.controller.AmbariManagementController managementController = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().begin().property(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID).equals("TEST.DO_SOMETHING").and().property(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID).equals("1").end().toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("TEST.DO_SOMETHING", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID));
        org.junit.Assert.assertEquals("Do Something", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID));
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResources() throws java.lang.Exception {
        replayAll();
        org.apache.ambari.server.controller.AmbariManagementController managementController = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider(managementController);
        provider.updateResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources() throws java.lang.Exception {
        replayAll();
        org.apache.ambari.server.controller.AmbariManagementController managementController = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProviderTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider provider = new org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider(managementController);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), null);
    }
}