package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ViewURLResourceProviderTest {
    private static final org.apache.ambari.server.view.ViewRegistry viewregistry = EasyMock.createMock(org.apache.ambari.server.view.ViewRegistry.class);

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry.initInstance(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry);
        EasyMock.reset(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry);
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testToResource() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ViewURLResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewURLResourceProvider();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX);
        org.apache.ambari.server.orm.entities.ViewURLEntity viewURLEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewURLEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        EasyMock.expect(viewURLEntity.getUrlName()).andReturn("test").once();
        EasyMock.expect(viewURLEntity.getUrlSuffix()).andReturn("url").once();
        EasyMock.expect(viewURLEntity.getViewInstanceEntity()).andReturn(viewInstanceEntity).once();
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).once();
        EasyMock.expect(viewEntity.getCommonName()).andReturn("FILES").once();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.0.0").once();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("test").once();
        EasyMock.replay(viewURLEntity, viewEntity, viewInstanceEntity);
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(viewURLEntity);
        org.junit.Assert.assertEquals(resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME), "test");
        org.junit.Assert.assertEquals(resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX), "url");
        org.junit.Assert.assertEquals(resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME), "test");
        org.junit.Assert.assertEquals(resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION), "1.0.0");
        org.junit.Assert.assertEquals(resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME), "FILES");
        EasyMock.verify(viewURLEntity, viewInstanceEntity, viewEntity);
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    static void setDao(java.lang.reflect.Field field, java.lang.Object newValue) throws java.lang.Exception {
        field.setAccessible(true);
        java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & (~java.lang.reflect.Modifier.FINAL));
        field.set(null, newValue);
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewURLResourceProvider();
        org.apache.ambari.server.orm.dao.ViewURLDAO viewURLDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewURLDAO.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.setDao(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.class.getDeclaredField("viewURLDAO"), viewURLDAO);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX, "suffix");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME, "FILES");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION, "1.0.0");
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.getInstanceDefinition("FILES", "1.0.0", "test")).andReturn(viewInstanceEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.getDefinition("FILES", "1.0.0")).andReturn(viewEntity);
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).once();
        EasyMock.expect(viewEntity.getCommonName()).andReturn("FILES").once();
        EasyMock.expect(viewEntity.isDeployed()).andReturn(true).once();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.0.0").once();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("test").once();
        EasyMock.expect(viewInstanceEntity.getViewUrl()).andReturn(null).once();
        EasyMock.expect(viewURLDAO.findByName("test")).andReturn(com.google.common.base.Optional.absent());
        EasyMock.expect(viewURLDAO.findBySuffix("suffix")).andReturn(com.google.common.base.Optional.absent());
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ViewURLEntity> urlEntityCapture = EasyMock.newCapture();
        viewURLDAO.save(EasyMock.capture(urlEntityCapture));
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.updateViewInstance(viewInstanceEntity);
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.updateView(viewInstanceEntity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry, viewEntity, viewInstanceEntity, viewURLDAO);
        properties.add(propertyMap);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
        org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity = urlEntityCapture.getValue();
        org.junit.Assert.assertEquals(urlEntity.getUrlName(), "test");
        org.junit.Assert.assertEquals(urlEntity.getUrlSuffix(), "suffix");
        org.junit.Assert.assertEquals(urlEntity.getViewInstanceEntity(), viewInstanceEntity);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources_existingUrlName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewURLResourceProvider();
        org.apache.ambari.server.orm.dao.ViewURLDAO viewURLDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewURLDAO.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.setDao(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.class.getDeclaredField("viewURLDAO"), viewURLDAO);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX, "suffix");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME, "FILES");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION, "1.0.0");
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.getInstanceDefinition("FILES", "1.0.0", "test")).andReturn(viewInstanceEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.getDefinition("FILES", "1.0.0")).andReturn(viewEntity);
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).once();
        EasyMock.expect(viewEntity.getCommonName()).andReturn("FILES").once();
        EasyMock.expect(viewEntity.isDeployed()).andReturn(true).once();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.0.0").once();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("test").once();
        EasyMock.expect(viewInstanceEntity.getViewUrl()).andReturn(null).once();
        EasyMock.expect(viewURLDAO.findByName("test")).andReturn(com.google.common.base.Optional.of(new org.apache.ambari.server.orm.entities.ViewURLEntity()));
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry, viewEntity, viewInstanceEntity, viewURLDAO);
        properties.add(propertyMap);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources_existingUrlSuffix() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewURLResourceProvider();
        org.apache.ambari.server.orm.dao.ViewURLDAO viewURLDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewURLDAO.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.setDao(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.class.getDeclaredField("viewURLDAO"), viewURLDAO);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX, "suffix");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME, "FILES");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION, "1.0.0");
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.getInstanceDefinition("FILES", "1.0.0", "test")).andReturn(viewInstanceEntity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.getDefinition("FILES", "1.0.0")).andReturn(viewEntity);
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).once();
        EasyMock.expect(viewEntity.getCommonName()).andReturn("FILES").once();
        EasyMock.expect(viewEntity.isDeployed()).andReturn(true).once();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.0.0").once();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("test").once();
        EasyMock.expect(viewInstanceEntity.getViewUrl()).andReturn(null).once();
        EasyMock.expect(viewURLDAO.findByName("test")).andReturn(com.google.common.base.Optional.absent());
        EasyMock.expect(viewURLDAO.findBySuffix("suffix")).andReturn(com.google.common.base.Optional.of(new org.apache.ambari.server.orm.entities.ViewURLEntity()));
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry, viewEntity, viewInstanceEntity, viewURLDAO);
        properties.add(propertyMap);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null));
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewURLResourceProvider();
        org.apache.ambari.server.orm.entities.ViewURLEntity viewURLEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewURLEntity.class);
        org.apache.ambari.server.orm.dao.ViewURLDAO viewURLDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewURLDAO.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.setDao(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.class.getDeclaredField("viewURLDAO"), viewURLDAO);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX, "suffix2");
        EasyMock.expect(viewURLDAO.findByName("test")).andReturn(com.google.common.base.Optional.of(viewURLEntity));
        EasyMock.expect(viewURLEntity.getViewInstanceEntity()).andReturn(viewInstanceEntity).once();
        EasyMock.expect(viewURLEntity.getUrlName()).andReturn("test").once();
        EasyMock.expect(viewURLEntity.getUrlSuffix()).andReturn("suffix2").once();
        EasyMock.expect(viewURLEntity.getViewInstanceEntity()).andReturn(viewInstanceEntity).once();
        viewURLEntity.setUrlSuffix("suffix2");
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ViewURLEntity> urlEntityCapture = EasyMock.newCapture();
        viewURLDAO.update(EasyMock.capture(urlEntityCapture));
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.updateViewInstance(viewInstanceEntity);
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.updateView(viewInstanceEntity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry, viewEntity, viewInstanceEntity, viewURLDAO, viewURLEntity);
        properties.add(propertyMap);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.utilities.PredicateBuilder predicateBuilder = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = predicateBuilder.property(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME).equals("test").toPredicate();
        provider.updateResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null), predicate);
        org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity = urlEntityCapture.getValue();
        org.junit.Assert.assertEquals(urlEntity.getUrlName(), "test");
        org.junit.Assert.assertEquals(urlEntity.getUrlSuffix(), "suffix2");
        org.junit.Assert.assertEquals(urlEntity.getViewInstanceEntity(), viewInstanceEntity);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.controller.internal.ViewURLResourceProvider provider = new org.apache.ambari.server.controller.internal.ViewURLResourceProvider();
        org.apache.ambari.server.orm.entities.ViewURLEntity viewURLEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewURLEntity.class);
        org.apache.ambari.server.orm.dao.ViewURLDAO viewURLDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewURLDAO.class);
        org.apache.ambari.server.controller.predicate.EqualsPredicate<java.lang.String> equalsPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, "test");
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.setDao(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.class.getDeclaredField("viewURLDAO"), viewURLDAO);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, "test");
        propertyMap.put(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX, "suffix");
        EasyMock.expect(viewURLDAO.findByName("test")).andReturn(com.google.common.base.Optional.of(viewURLEntity));
        EasyMock.expect(viewURLEntity.getViewInstanceEntity()).andReturn(viewInstanceEntity).once();
        EasyMock.expect(viewURLEntity.getUrlName()).andReturn("test").once();
        EasyMock.expect(viewURLEntity.getUrlSuffix()).andReturn("suffix").once();
        EasyMock.expect(viewURLEntity.getViewInstanceEntity()).andReturn(viewInstanceEntity).once();
        viewURLEntity.setUrlSuffix("suffix");
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ViewURLEntity> urlEntityCapture = EasyMock.newCapture();
        viewInstanceEntity.clearUrl();
        viewURLEntity.clearEntity();
        viewURLDAO.delete(EasyMock.capture(urlEntityCapture));
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.updateViewInstance(viewInstanceEntity);
        org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry.updateView(viewInstanceEntity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.ViewURLResourceProviderTest.viewregistry, viewEntity, viewInstanceEntity, viewURLDAO, viewURLEntity);
        properties.add(propertyMap);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        provider.deleteResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, null), equalsPredicate);
        org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity = urlEntityCapture.getValue();
        org.junit.Assert.assertEquals(urlEntity.getUrlName(), "test");
        org.junit.Assert.assertEquals(urlEntity.getUrlSuffix(), "suffix");
        org.junit.Assert.assertEquals(urlEntity.getViewInstanceEntity(), viewInstanceEntity);
    }
}