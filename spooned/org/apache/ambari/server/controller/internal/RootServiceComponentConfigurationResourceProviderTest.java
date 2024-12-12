package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMockSupport;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_PROPERTIES_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.commons.io.FileUtils.class, org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler.class })
public class RootServiceComponentConfigurationResourceProviderTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String LDAP_CONFIG_CATEGORY = org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName();

    private static final java.lang.String SSO_CONFIG_CATEGORY = org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName();

    private org.apache.ambari.server.controller.spi.Predicate predicate;

    private org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider;

    private org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandlerFactory factory;

    private org.apache.ambari.server.controller.spi.Request request;

    private org.apache.ambari.server.orm.dao.AmbariConfigurationDAO dao;

    private org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher;

    private org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler ambariServerLDAPConfigurationHandler;

    private org.apache.ambari.server.controller.internal.AmbariServerSSOConfigurationHandler ambariServerSSOConfigurationHandler;

    @org.junit.Before
    public void init() {
        com.google.inject.Injector injector = createInjector();
        resourceProvider = injector.getInstance(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.class);
        predicate = createPredicate(org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY);
        request = createMock(org.apache.ambari.server.controller.spi.Request.class);
        dao = injector.getInstance(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
        factory = injector.getInstance(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandlerFactory.class);
        publisher = injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        ambariServerLDAPConfigurationHandler = injector.getInstance(org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.class);
        ambariServerSSOConfigurationHandler = injector.getInstance(org.apache.ambari.server.controller.internal.AmbariServerSSOConfigurationHandler.class);
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResources_Administrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_ClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_ClusterOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_ServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_ServiceOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), null);
    }

    @org.junit.Test
    public void testCreateResourcesWithDirective_Administrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithDirective_ClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithDirective_ClusterOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithDirective_ServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesWithDirective_ServiceOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), "test-directive");
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication, java.lang.String opDirective) throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySets = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.key(), "value1");
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_BASE.key(), "value2");
        propertySets.add(toRequestProperties(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY, properties));
        java.util.Map<java.lang.String, java.lang.String> properties2 = new java.util.HashMap<>();
        if (opDirective == null) {
            properties2.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES.key(), "true");
            propertySets.add(toRequestProperties(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.SSO_CONFIG_CATEGORY, properties2));
        }
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties;
        if (opDirective == null) {
            requestInfoProperties = java.util.Collections.emptyMap();
        } else {
            requestInfoProperties = java.util.Collections.singletonMap(org.apache.ambari.server.api.services.RootServiceComponentConfigurationService.DIRECTIVE_OPERATION, opDirective);
        }
        EasyMock.expect(request.getProperties()).andReturn(propertySets).once();
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties).once();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> capturedProperties1 = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> capturedProperties2 = EasyMock.newCapture();
        if (opDirective == null) {
            EasyMock.expect(dao.reconcileCategory(EasyMock.eq(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY), EasyMock.capture(capturedProperties1), EasyMock.eq(true))).andReturn(true).once();
            EasyMock.expect(dao.reconcileCategory(EasyMock.eq(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.SSO_CONFIG_CATEGORY), EasyMock.capture(capturedProperties2), EasyMock.eq(true))).andReturn(true).once();
            EasyMock.expect(dao.findByCategory(EasyMock.eq(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.SSO_CONFIG_CATEGORY))).andReturn(java.util.Collections.emptyList()).once();
            publisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.AmbariConfigurationChangedEvent.class));
            EasyMock.expectLastCall().times(2);
        }
        EasyMock.expect(factory.getInstance(org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY)).andReturn(ambariServerLDAPConfigurationHandler).once();
        if (opDirective == null) {
            EasyMock.expect(factory.getInstance(org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.SSO_CONFIG_CATEGORY)).andReturn(ambariServerSSOConfigurationHandler).once();
        }
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            resourceProvider.createResources(request);
            if (opDirective != null) {
                junit.framework.Assert.fail("Expected SystemException to be thrown");
            }
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
            throw e;
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            if (opDirective == null) {
                junit.framework.Assert.fail("Unexpected exception: " + e.getMessage());
            } else {
                junit.framework.Assert.assertEquals("The requested operation is not supported for this category: " + org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY, e.getMessage());
            }
        }
        verifyAll();
        if (opDirective == null) {
            validateCapturedProperties(properties, capturedProperties1);
            validateCapturedProperties(properties2, capturedProperties2);
        } else {
            junit.framework.Assert.assertFalse(capturedProperties1.hasCaptured());
            junit.framework.Assert.assertFalse(capturedProperties2.hasCaptured());
        }
    }

    @org.junit.Test
    public void testDeleteResources_Administrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_ClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_ClusterOperator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_ServiceAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_ServiceOperator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(dao.removeByCategory(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY)).andReturn(1).once();
        publisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.AmbariConfigurationChangedEvent.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(factory.getInstance(org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY)).andReturn(ambariServerLDAPConfigurationHandler).once();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        resourceProvider.deleteResources(request, predicate);
        verifyAll();
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_ClusterAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_ClusterOperator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_ServiceAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_ServiceOperator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    private void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        EasyMock.expect(request.getPropertyIds()).andReturn(null).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND.key(), "value1");
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE.key(), "value2");
        EasyMock.expect(dao.findByCategory(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY)).andReturn(createEntities(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY, properties)).once();
        EasyMock.expect(factory.getInstance(org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY)).andReturn(ambariServerLDAPConfigurationHandler).once();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = resourceProvider.getResources(request, predicate);
        verifyAll();
        junit.framework.Assert.assertNotNull(response);
        junit.framework.Assert.assertEquals(1, response.size());
        org.apache.ambari.server.controller.spi.Resource resource = response.iterator().next();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponentConfiguration, resource.getType());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertiesMap = resource.getPropertiesMap();
        junit.framework.Assert.assertEquals(3, propertiesMap.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY, propertiesMap.get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RESOURCE_KEY).get("category"));
        java.util.Map<java.lang.String, java.lang.Object> retrievedProperties = propertiesMap.get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_PROPERTIES_PROPERTY_ID);
        junit.framework.Assert.assertEquals(2, retrievedProperties.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : properties.entrySet()) {
            junit.framework.Assert.assertEquals(entry.getValue(), retrievedProperties.get(entry.getKey()));
        }
        java.util.Map<java.lang.String, java.lang.Object> retrievedPropertyTypes = propertiesMap.get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_PROPERTY_TYPES_PROPERTY_ID);
        junit.framework.Assert.assertEquals(2, retrievedPropertyTypes.size());
    }

    @org.junit.Test
    public void testUpdateResources_Administrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_ClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_ClusterOperator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_ServiceAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_ServiceOperator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), null);
    }

    @org.junit.Test
    public void testUpdateResourcesWithDirective_Administrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesWithDirective_ClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesWithDirective_ClusterOperator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesWithDirective_ServiceAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), "test-directive");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesWithDirective_ServiceOperator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), "test-directive");
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication, java.lang.String opDirective) throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySets = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_BASE.key(), "value1");
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE.key(), "value2");
        propertySets.add(toRequestProperties(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY, properties));
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties;
        if (opDirective == null) {
            requestInfoProperties = java.util.Collections.emptyMap();
        } else {
            requestInfoProperties = java.util.Collections.singletonMap(org.apache.ambari.server.api.services.RootServiceComponentConfigurationService.DIRECTIVE_OPERATION, opDirective);
        }
        EasyMock.expect(request.getProperties()).andReturn(propertySets).once();
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties).once();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> capturedProperties1 = EasyMock.newCapture();
        if (opDirective == null) {
            EasyMock.expect(dao.reconcileCategory(EasyMock.eq(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY), EasyMock.capture(capturedProperties1), EasyMock.eq(false))).andReturn(true).once();
            publisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.AmbariConfigurationChangedEvent.class));
            EasyMock.expectLastCall().times(1);
        }
        EasyMock.expect(factory.getInstance(org.apache.ambari.server.controller.RootService.AMBARI.name(), org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY)).andReturn(ambariServerLDAPConfigurationHandler).once();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            resourceProvider.updateResources(request, predicate);
            if (opDirective != null) {
                junit.framework.Assert.fail("Expected SystemException to be thrown");
            }
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
            throw e;
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            if (opDirective == null) {
                junit.framework.Assert.fail("Unexpected exception: " + e.getMessage());
            } else {
                junit.framework.Assert.assertEquals("The requested operation is not supported for this category: " + org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProviderTest.LDAP_CONFIG_CATEGORY, e.getMessage());
            }
        }
        verifyAll();
        if (opDirective == null) {
            validateCapturedProperties(properties, capturedProperties1);
        } else {
            junit.framework.Assert.assertFalse(capturedProperties1.hasCaptured());
        }
    }

    private org.apache.ambari.server.controller.spi.Predicate createPredicate(java.lang.String serviceName, java.lang.String componentName, java.lang.String categoryName) {
        org.apache.ambari.server.controller.spi.Predicate predicateService = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID).equals(serviceName).toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicateComponent = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID).equals(componentName).toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicateCategory = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID).equals(categoryName).toPredicate();
        return new org.apache.ambari.server.controller.predicate.AndPredicate(predicateService, predicateComponent, predicateCategory);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> createEntities(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties) {
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : properties.entrySet()) {
            org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity = new org.apache.ambari.server.orm.entities.AmbariConfigurationEntity();
            entity.setCategoryName(categoryName);
            entity.setPropertyName(property.getKey());
            entity.setPropertyValue(property.getValue());
            entities.add(entity);
        }
        return entities;
    }

    private java.util.Map<java.lang.String, java.lang.Object> toRequestProperties(java.lang.String categoryName1, java.util.Map<java.lang.String, java.lang.String> properties) {
        java.util.Map<java.lang.String, java.lang.Object> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID, "AMBARI");
        requestProperties.put(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID, "AMBARI_SERVER");
        requestProperties.put(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID, categoryName1);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : properties.entrySet()) {
            requestProperties.put((org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_PROPERTIES_PROPERTY_ID + "/") + entry.getKey(), entry.getValue());
        }
        return requestProperties;
    }

    private void validateCapturedProperties(java.util.Map<java.lang.String, java.lang.String> expectedProperties, org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> capturedProperties) {
        junit.framework.Assert.assertTrue(capturedProperties.hasCaptured());
        java.util.Map<java.lang.String, java.lang.String> properties = capturedProperties.getValue();
        junit.framework.Assert.assertNotNull(properties);
        expectedProperties = new java.util.TreeMap<>(expectedProperties);
        properties = new java.util.TreeMap<>(properties);
        junit.framework.Assert.assertEquals(expectedProperties, properties);
    }

    private com.google.inject.Injector createInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = createMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
                org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO = createMock(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
                org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
                org.apache.ambari.server.state.ConfigHelper configHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
                org.apache.ambari.server.controller.AmbariManagementController managementController = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
                org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper = createNiceMock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class);
                org.apache.ambari.server.ldap.service.LdapFacade ldapFacade = createNiceMock(org.apache.ambari.server.ldap.service.LdapFacade.class);
                org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration> encryptor = createNiceMock(org.apache.ambari.server.security.encryption.AmbariServerConfigurationEncryptor.class);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class).toInstance(ambariConfigurationDAO);
                bind(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class).toInstance(publisher);
                bind(org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler.class).toInstance(new org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler(ambariConfigurationDAO, publisher));
                bind(org.apache.ambari.server.controller.internal.AmbariServerSSOConfigurationHandler.class).toInstance(new org.apache.ambari.server.controller.internal.AmbariServerSSOConfigurationHandler(clusters, configHelper, managementController, stackAdvisorHelper, ambariConfigurationDAO, publisher));
                bind(org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler.class).toInstance(new org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler(clusters, configHelper, managementController, stackAdvisorHelper, ambariConfigurationDAO, publisher, ldapFacade, encryptor));
                bind(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandlerFactory.class).toInstance(createMock(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandlerFactory.class));
            }
        });
    }
}