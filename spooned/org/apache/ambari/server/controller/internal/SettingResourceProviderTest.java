package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.RandomStringUtils;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;
public class SettingResourceProviderTest {
    org.easymock.IMocksControl mockControl;

    org.apache.ambari.server.orm.dao.SettingDAO dao;

    org.apache.ambari.server.controller.internal.SettingResourceProvider resourceProvider;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        mockControl = EasyMock.createControl();
        dao = mockControl.createMock(org.apache.ambari.server.orm.dao.SettingDAO.class);
        resourceProvider = new org.apache.ambari.server.controller.internal.SettingResourceProvider();
        setPrivateField(resourceProvider, "dao", dao);
    }

    @org.junit.After
    public void tearDown() {
        mockControl.verify();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        mockControl.reset();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_instance_noAuth() throws java.lang.Exception {
        getResources_instance(newEntity("motd"), readRequest());
    }

    @org.junit.Test
    public void testGetResources_instance_clusterUser() throws java.lang.Exception {
        setupAuthenticationForClusterUser();
        java.lang.String name = "motd";
        org.apache.ambari.server.orm.entities.SettingEntity entity = newEntity(name);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = getResources_instance(entity, readRequest());
        org.junit.Assert.assertEquals(1, response.size());
        org.apache.ambari.server.controller.spi.Resource resource = response.iterator().next();
        assertEqualsEntityAndResource(entity, resource);
    }

    @org.junit.Test
    public void testGetResources_instance_admin() throws java.lang.Exception {
        setupAuthenticationForAdmin();
        org.apache.ambari.server.orm.entities.SettingEntity entity = newEntity("motd");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = getResources_instance(entity, readRequest());
        org.junit.Assert.assertEquals(1, response.size());
        org.apache.ambari.server.controller.spi.Resource resource = response.iterator().next();
        assertEqualsEntityAndResource(entity, resource);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_collection_noAuth() throws java.lang.Exception {
        mockControl.replay();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID);
        resourceProvider.getResources(request, null);
    }

    @org.junit.Test
    public void testGetResources_collection_clusterUser() throws java.lang.Exception {
        setupAuthenticationForClusterUser();
        org.apache.ambari.server.orm.entities.SettingEntity entity1 = newEntity("motd");
        org.apache.ambari.server.orm.entities.SettingEntity entity2 = newEntity("ldap");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID);
        EasyMock.expect(dao.findAll()).andReturn(com.google.common.collect.Lists.newArrayList(entity1, entity2));
        mockControl.replay();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = resourceProvider.getResources(request, null);
        org.junit.Assert.assertEquals(2, response.size());
        java.util.Map<java.lang.Object, org.apache.ambari.server.controller.spi.Resource> resourceMap = new java.util.HashMap<>();
        java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> resourceIterator = response.iterator();
        org.apache.ambari.server.controller.spi.Resource nextResource = resourceIterator.next();
        resourceMap.put(nextResource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID), nextResource);
        nextResource = resourceIterator.next();
        resourceMap.put(nextResource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID), nextResource);
        assertEqualsEntityAndResource(entity1, resourceMap.get(entity1.getName()));
        assertEqualsEntityAndResource(entity2, resourceMap.get(entity2.getName()));
    }

    @org.junit.Test
    public void testGetResources_collection_admin() throws java.lang.Exception {
        setupAuthenticationForAdmin();
        org.apache.ambari.server.orm.entities.SettingEntity entity1 = newEntity("motd");
        org.apache.ambari.server.orm.entities.SettingEntity entity2 = newEntity("ldap");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID);
        EasyMock.expect(dao.findAll()).andReturn(com.google.common.collect.Lists.newArrayList(entity1, entity2));
        mockControl.replay();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = resourceProvider.getResources(request, null);
        org.junit.Assert.assertEquals(2, response.size());
        java.util.Map<java.lang.Object, org.apache.ambari.server.controller.spi.Resource> resourceMap = new java.util.HashMap<>();
        java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> resourceIterator = response.iterator();
        org.apache.ambari.server.controller.spi.Resource nextResource = resourceIterator.next();
        resourceMap.put(nextResource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID), nextResource);
        nextResource = resourceIterator.next();
        resourceMap.put(nextResource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID), nextResource);
        assertEqualsEntityAndResource(entity1, resourceMap.get(entity1.getName()));
        assertEqualsEntityAndResource(entity2, resourceMap.get(entity2.getName()));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResource_noAuth() throws java.lang.Exception {
        mockControl.replay();
        resourceProvider.createResources(createRequest(newEntity("moted")));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResource_clusterUser() throws java.lang.Exception {
        setupAuthenticationForClusterUser();
        mockControl.replay();
        resourceProvider.createResources(createRequest(newEntity("motd")));
    }

    @org.junit.Test
    public void testCreateResource_admin() throws java.lang.Exception {
        setupAuthenticationForAdmin();
        org.apache.ambari.server.orm.entities.SettingEntity entity = newEntity("motd");
        org.easymock.Capture<org.apache.ambari.server.orm.entities.SettingEntity> entityCapture = org.easymock.Capture.newInstance();
        org.apache.ambari.server.controller.spi.Request request = createRequest(entity);
        EasyMock.expect(dao.findByName(entity.getName())).andReturn(null);
        dao.create(EasyMock.capture(entityCapture));
        mockControl.replay();
        org.apache.ambari.server.controller.spi.RequestStatus response = resourceProvider.createResources(request);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete, response.getStatus());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = response.getAssociatedResources();
        org.junit.Assert.assertEquals(1, associatedResources.size());
        org.apache.ambari.server.orm.entities.SettingEntity capturedEntity = entityCapture.getValue();
        org.junit.Assert.assertEquals(entity.getName(), capturedEntity.getName());
        org.junit.Assert.assertEquals(entity.getContent(), capturedEntity.getContent());
        org.junit.Assert.assertEquals(entity.getSettingType(), capturedEntity.getSettingType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName(), capturedEntity.getUpdatedBy());
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException.class)
    public void testCreateDuplicateResource() throws java.lang.Exception {
        setupAuthenticationForAdmin();
        org.apache.ambari.server.orm.entities.SettingEntity entity = newEntity("motd");
        org.apache.ambari.server.controller.spi.Request request = createRequest(entity);
        EasyMock.expect(dao.findByName(entity.getName())).andReturn(entity);
        mockControl.replay();
        resourceProvider.createResources(request);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_noAuth() throws java.lang.Exception {
        mockControl.replay();
        resourceProvider.updateResources(updateRequest(newEntity("motd")), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_clusterUser() throws java.lang.Exception {
        setupAuthenticationForClusterUser();
        mockControl.replay();
        resourceProvider.updateResources(updateRequest(newEntity("motd")), null);
    }

    @org.junit.Test
    public void testUpdateResources_admin() throws java.lang.Exception {
        setupAuthenticationForAdmin();
        java.lang.String name = "motd";
        org.apache.ambari.server.orm.entities.SettingEntity oldEntity = newEntity(name);
        org.apache.ambari.server.orm.entities.SettingEntity updatedEntity = oldEntity.clone();
        updatedEntity.setContent("{text}");
        updatedEntity.setSettingType("new-type");
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).equals(name).end().toPredicate();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.SettingEntity> capture = org.easymock.Capture.newInstance();
        EasyMock.expect(dao.findByName(name)).andReturn(oldEntity);
        EasyMock.expect(dao.merge(EasyMock.capture(capture))).andReturn(updatedEntity);
        mockControl.replay();
        org.apache.ambari.server.controller.spi.RequestStatus response = resourceProvider.updateResources(updateRequest(updatedEntity), predicate);
        org.apache.ambari.server.orm.entities.SettingEntity capturedEntity = capture.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete, response.getStatus());
        org.junit.Assert.assertEquals(updatedEntity.getId(), capturedEntity.getId());
        org.junit.Assert.assertEquals(updatedEntity.getName(), capturedEntity.getName());
        org.junit.Assert.assertEquals(updatedEntity.getSettingType(), capturedEntity.getSettingType());
        org.junit.Assert.assertEquals(updatedEntity.getContent(), capturedEntity.getContent());
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName(), capturedEntity.getUpdatedBy());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_noAuth() throws java.lang.Exception {
        mockControl.replay();
        resourceProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_clusterUser() throws java.lang.Exception {
        setupAuthenticationForClusterUser();
        mockControl.replay();
        resourceProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), null);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        setupAuthenticationForAdmin();
        java.lang.String name = "motd";
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).equals(name).end().toPredicate();
        dao.removeByName(name);
        mockControl.replay();
        resourceProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources_instance(org.apache.ambari.server.orm.entities.SettingEntity entity, org.apache.ambari.server.controller.spi.Request request) throws java.lang.Exception {
        java.lang.String name = entity.getName();
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.begin().property(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).equals(name).end().toPredicate();
        EasyMock.expect(dao.findByName(name)).andReturn(entity).anyTimes();
        mockControl.replay();
        return resourceProvider.getResources(request, predicate);
    }

    private org.apache.ambari.server.controller.spi.Request readRequest() {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID);
    }

    private org.apache.ambari.server.controller.spi.Request createRequest(org.apache.ambari.server.orm.entities.SettingEntity entity) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, entity.getName());
        properties.put(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID, entity.getContent());
        properties.put(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID, entity.getUpdatedBy());
        properties.put(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, entity.getSettingType());
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(properties), null);
    }

    private org.apache.ambari.server.controller.spi.Request updateRequest(org.apache.ambari.server.orm.entities.SettingEntity entity) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, entity.getName());
        properties.put(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID, entity.getContent());
        properties.put(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, entity.getSettingType());
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
    }

    private void setupAuthenticationForClusterUser() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void setupAuthenticationForAdmin() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    private org.apache.ambari.server.orm.entities.SettingEntity newEntity(java.lang.String name) {
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setName(name);
        entity.setContent(org.apache.commons.lang.RandomStringUtils.randomAlphabetic(10));
        entity.setSettingType(org.apache.commons.lang.RandomStringUtils.randomAlphabetic(5));
        entity.setUpdatedBy("ambari");
        entity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
        return entity;
    }

    private void assertEqualsEntityAndResource(org.apache.ambari.server.orm.entities.SettingEntity entity, org.apache.ambari.server.controller.spi.Resource resource) {
        org.junit.Assert.assertEquals(entity.getName(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals(entity.getSettingType(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID));
        org.junit.Assert.assertEquals(entity.getContent(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID));
        org.junit.Assert.assertEquals(entity.getUpdatedBy(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID));
        org.junit.Assert.assertEquals(entity.getUpdateTimestamp(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID));
    }

    private void setPrivateField(java.lang.Object o, java.lang.String field, java.lang.Object value) throws java.lang.Exception {
        java.lang.Class<?> c = o.getClass();
        java.lang.reflect.Field f = c.getDeclaredField(field);
        f.setAccessible(true);
        f.set(o, value);
    }
}