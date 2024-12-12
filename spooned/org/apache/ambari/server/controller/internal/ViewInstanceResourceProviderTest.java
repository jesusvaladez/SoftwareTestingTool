package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ViewInstanceResourceProviderTest {
    private static final org.apache.ambari.server.view.ViewRegistry viewregistry = EasyMock.createMock(org.apache.ambari.server.view.ViewRegistry.class);

    @org.junit.Before
    public void before() {
        org.apache.ambari.server.view.ViewRegistry.initInstance(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
        EasyMock.reset(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testToResource() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES);
        propertyIds.add(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_HANDLE);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> propertyMap = new java.util.HashMap<>();
        propertyMap.put("par1", "val1");
        propertyMap.put("par2", "val2");
        EasyMock.expect(viewInstanceEntity.getPropertyMap()).andReturn(propertyMap);
        EasyMock.expect(viewInstanceEntity.getData()).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.checkAdmin()).andReturn(true);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.checkAdmin()).andReturn(false);
        EasyMock.expect(viewInstanceEntity.getClusterHandle()).andReturn(1L);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry, viewEntity, viewInstanceEntity);
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(viewInstanceEntity, propertyIds);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> properties = resource.getPropertiesMap();
        org.junit.Assert.assertEquals(2, properties.size());
        java.util.Map<java.lang.String, java.lang.Object> props = properties.get("ViewInstanceInfo");
        org.junit.Assert.assertNotNull(props);
        org.junit.Assert.assertEquals(1, props.size());
        org.junit.Assert.assertEquals(1L, props.get("cluster_handle"));
        props = properties.get("ViewInstanceInfo/properties");
        org.junit.Assert.assertNotNull(props);
        org.junit.Assert.assertEquals(2, props.size());
        org.junit.Assert.assertEquals("val1", props.get("par1"));
        org.junit.Assert.assertEquals("val2", props.get("par2"));
        resource = provider.toResource(viewInstanceEntity, propertyIds);
        properties = resource.getPropertiesMap();
        props = properties.get("ViewInstanceInfo/properties");
        org.junit.Assert.assertNull(props);
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry, viewEntity, viewInstanceEntity);
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME, "V1");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION, "1.0.0");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME, "I1");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES + "/test_property", "test_value");
        properties.add(propertyMap);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
        viewInstanceEntity.setViewName("V1{1.0.0}");
        viewInstanceEntity.setName("I1");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity2 = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
        viewInstanceEntity2.setViewName("V1{1.0.0}");
        viewInstanceEntity2.setName("I1");
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = new org.apache.ambari.server.orm.entities.ViewEntity();
        viewEntity.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED);
        viewEntity.setName("V1{1.0.0}");
        viewInstanceEntity.setViewEntity(viewEntity);
        viewInstanceEntity2.setViewEntity(viewEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.instanceExists(viewInstanceEntity)).andReturn(false);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.instanceExists(viewInstanceEntity2)).andReturn(false);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinition("V1", "1.0.0")).andReturn(viewEntity).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinition("V1", null)).andReturn(viewEntity).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> captureProperties = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.setViewInstanceProperties(EasyMock.eq(viewInstanceEntity), EasyMock.capture(captureProperties), EasyMock.anyObject(org.apache.ambari.server.view.configuration.ViewConfig.class), EasyMock.anyObject(java.lang.ClassLoader.class));
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceEntityCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.installViewInstance(EasyMock.capture(instanceEntityCapture));
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.checkAdmin()).andReturn(true);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.checkAdmin()).andReturn(false);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
        org.junit.Assert.assertEquals(viewInstanceEntity, instanceEntityCapture.getValue());
        java.util.Map<java.lang.String, java.lang.String> props = captureProperties.getValue();
        org.junit.Assert.assertEquals(1, props.size());
        org.junit.Assert.assertEquals("test_value", props.get("test_property"));
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
        org.junit.Assert.assertEquals(viewInstanceEntity2, instanceEntityCapture.getValue());
        props = viewInstanceEntity2.getPropertyMap();
        org.junit.Assert.assertTrue(props.isEmpty());
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
    }

    @org.junit.Test
    public void testCreateResources_existingInstance() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME, "V1");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION, "1.0.0");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME, "I1");
        properties.add(propertyMap);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
        viewInstanceEntity.setViewName("V1{1.0.0}");
        viewInstanceEntity.setName("I1");
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = new org.apache.ambari.server.orm.entities.ViewEntity();
        viewEntity.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED);
        viewEntity.setName("V1{1.0.0}");
        viewInstanceEntity.setViewEntity(viewEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.instanceExists(viewInstanceEntity)).andReturn(true);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinition("V1", "1.0.0")).andReturn(viewEntity).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinition("V1", null)).andReturn(viewEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.checkAdmin()).andReturn(true);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        try {
            provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
            org.junit.Assert.fail("Expected ResourceAlreadyExistsException.");
        } catch (org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException e) {
        }
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
    }

    @org.junit.Test
    public void testCreateResources_viewNotLoaded() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME, "V1");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION, "1.0.0");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME, "I1");
        properties.add(propertyMap);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = new org.apache.ambari.server.orm.entities.ViewEntity();
        viewEntity.setName("V1{1.0.0}");
        viewEntity.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
        viewInstanceEntity.setViewName("V1{1.0.0}");
        viewInstanceEntity.setName("I1");
        viewInstanceEntity.setViewEntity(viewEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinition("V1", "1.0.0")).andReturn(viewEntity).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinition("V1", null)).andReturn(viewEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.checkAdmin()).andReturn(true);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        try {
            provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
            org.junit.Assert.fail("Expected IllegalStateException.");
        } catch (java.lang.IllegalStateException e) {
        }
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
    }

    @org.junit.Test
    public void testUpdateResources_viewNotLoaded() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON_PATH, "path");
        properties.add(propertyMap);
        org.apache.ambari.server.controller.utilities.PredicateBuilder predicateBuilder = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = predicateBuilder.property(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME).equals("V1").toPredicate();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = new org.apache.ambari.server.orm.entities.ViewEntity();
        viewEntity.setName("V1{1.0.0}");
        viewEntity.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
        viewInstanceEntity.setViewName("V1{1.0.0}");
        viewInstanceEntity.setName("I1");
        viewInstanceEntity.setViewEntity(viewEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinitions()).andReturn(java.util.Collections.singleton(viewEntity));
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        provider.updateResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null), predicate);
        org.junit.Assert.assertNull(viewInstanceEntity.getIcon());
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
    }

    @org.junit.Test
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider();
        org.apache.ambari.server.controller.utilities.PredicateBuilder predicateBuilder = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = predicateBuilder.property(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME).equals("V1").toPredicate();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = new org.apache.ambari.server.orm.entities.ViewEntity();
        viewEntity.setName("V1{1.0.0}");
        viewEntity.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
        viewInstanceEntity.setViewName("V1{1.0.0}");
        viewInstanceEntity.setName("I1");
        viewInstanceEntity.setViewEntity(viewEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry.getDefinitions()).andReturn(java.util.Collections.singleton(viewEntity));
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        EasyMock.verify(org.apache.ambari.server.controller.internal.ViewInstanceResourceProviderTest.viewregistry);
    }
}