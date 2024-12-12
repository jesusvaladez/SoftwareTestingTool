package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ViewPermissionResourceProviderTest {
    private static final org.apache.ambari.server.orm.dao.PermissionDAO dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.PermissionDAO.class);

    private static final org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createMock(org.apache.ambari.server.view.ViewRegistry.class);

    @org.junit.BeforeClass
    public static void initClass() {
        org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.init(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao);
    }

    @org.junit.Before
    public void resetGlobalMocks() {
        org.apache.ambari.server.view.ViewRegistry.initInstance(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry);
        EasyMock.reset(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao, org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.PermissionEntity> permissionEntities = new java.util.LinkedList<>();
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        org.apache.ambari.server.orm.entities.PermissionEntity viewUsePermissionEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        permissionEntities.add(permissionEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao.findViewUsePermission()).andReturn(viewUsePermissionEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao.findAll()).andReturn(java.util.Collections.singletonList(permissionEntity));
        EasyMock.expect(permissionEntity.getId()).andReturn(99);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("P1");
        EasyMock.expect(permissionEntity.getResourceType()).andReturn(resourceTypeEntity);
        EasyMock.expect(resourceTypeEntity.getName()).andReturn("V1");
        EasyMock.expect(viewEntity.isDeployed()).andReturn(true).anyTimes();
        EasyMock.expect(viewEntity.getCommonName()).andReturn("V1").anyTimes();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.0.0").anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry.getDefinition(resourceTypeEntity)).andReturn(viewEntity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao, permissionEntity, viewUsePermissionEntity, resourceTypeEntity, viewEntity, org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry);
        org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals(99, resource.getPropertyValue(org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_ID_PROPERTY_ID));
        org.junit.Assert.assertEquals("P1", resource.getPropertyValue(org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals("V1", resource.getPropertyValue(org.apache.ambari.server.controller.internal.PermissionResourceProvider.RESOURCE_NAME_PROPERTY_ID));
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao, permissionEntity, viewUsePermissionEntity, resourceTypeEntity, viewEntity, org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry);
    }

    @org.junit.Test
    public void testGetResources_viewNotLoaded() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.PermissionEntity> permissionEntities = new java.util.LinkedList<>();
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        org.apache.ambari.server.orm.entities.PermissionEntity viewUsePermissionEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        permissionEntities.add(permissionEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao.findViewUsePermission()).andReturn(viewUsePermissionEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao.findAll()).andReturn(java.util.Collections.singletonList(permissionEntity));
        EasyMock.expect(permissionEntity.getResourceType()).andReturn(resourceTypeEntity);
        EasyMock.expect(viewEntity.isDeployed()).andReturn(false).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry.getDefinition(resourceTypeEntity)).andReturn(viewEntity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao, permissionEntity, viewUsePermissionEntity, resourceTypeEntity, viewEntity, org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry);
        org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(0, resources.size());
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.dao, permissionEntity, viewUsePermissionEntity, resourceTypeEntity, viewEntity, org.apache.ambari.server.controller.internal.ViewPermissionResourceProviderTest.viewRegistry);
    }
}