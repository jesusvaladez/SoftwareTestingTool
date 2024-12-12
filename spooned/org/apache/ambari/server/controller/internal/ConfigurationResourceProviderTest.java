package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ConfigurationResourceProviderTest {
    @org.junit.BeforeClass
    public static void setupAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        managementController.createConfiguration(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getConfigurationRequest("Cluster100", "type", "tag", new java.util.HashMap<>(), null));
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(managementController, response);
        org.apache.ambari.server.controller.internal.ConfigurationResourceProvider provider = new org.apache.ambari.server.controller.internal.ConfigurationResourceProvider(managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG, "tag");
        properties.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE, "type");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testCreateAttributesResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        managementController.createConfiguration(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getConfigurationRequest("Cluster100", "type", "tag", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("final", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("a", "true");
                    }
                });
            }
        }));
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(managementController, response);
        org.apache.ambari.server.controller.internal.ConfigurationResourceProvider provider = new org.apache.ambari.server.controller.internal.ConfigurationResourceProvider(managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG, "tag");
        properties.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE, "type");
        properties.put("properties/a", "b");
        properties.put("properties_attributes/final/a", "true");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Configuration;
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "0.1");
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.ConfigurationResponse("Cluster100", stackId, "type", "tag1", 1L, null, null));
        allResponse.add(new org.apache.ambari.server.controller.ConfigurationResponse("Cluster100", stackId, "type", "tag2", 2L, null, null));
        allResponse.add(new org.apache.ambari.server.controller.ConfigurationResponse("Cluster100", stackId, "type", "tag3", 3L, null, null));
        java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> orResponse = new java.util.HashSet<>();
        orResponse.add(new org.apache.ambari.server.controller.ConfigurationResponse("Cluster100", stackId, "type", "tag1", 1L, null, null));
        orResponse.add(new org.apache.ambari.server.controller.ConfigurationResponse("Cluster100", stackId, "type", "tag2", 2L, null, null));
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ConfigurationRequest>> configRequestCapture1 = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ConfigurationRequest>> configRequestCapture2 = org.easymock.EasyMock.newCapture();
        EasyMock.expect(managementController.getConfigurations(EasyMock.capture(configRequestCapture1))).andReturn(allResponse).once();
        EasyMock.expect(managementController.getConfigurations(EasyMock.capture(configRequestCapture2))).andReturn(orResponse).once();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME).equals("Cluster100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.ConfigurationRequest> setRequest = configRequestCapture1.getValue();
        junit.framework.Assert.assertEquals(1, setRequest.size());
        org.apache.ambari.server.controller.ConfigurationRequest configRequest = setRequest.iterator().next();
        junit.framework.Assert.assertEquals("Cluster100", configRequest.getClusterName());
        junit.framework.Assert.assertNull(configRequest.getType());
        junit.framework.Assert.assertNull(configRequest.getVersionTag());
        org.junit.Assert.assertEquals(3, resources.size());
        boolean containsResource1 = false;
        boolean containsResource2 = false;
        boolean containsResource3 = false;
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME)));
            java.lang.String stackIdProperty = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.STACK_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            org.junit.Assert.assertEquals(stackId.getStackId(), stackIdProperty);
            java.lang.String tag = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG)));
            if (tag.equals("tag1")) {
                containsResource1 = true;
            } else if (tag.equals("tag2")) {
                containsResource2 = true;
            } else if (tag.equals("tag3")) {
                containsResource3 = true;
            }
        }
        junit.framework.Assert.assertTrue(containsResource1);
        junit.framework.Assert.assertTrue(containsResource2);
        junit.framework.Assert.assertTrue(containsResource3);
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG).equals("tag1").or().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG).equals("tag2").toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        resources = provider.getResources(request, predicate);
        setRequest = configRequestCapture2.getValue();
        junit.framework.Assert.assertEquals(2, setRequest.size());
        boolean containsTag1 = false;
        boolean containsTag2 = false;
        for (org.apache.ambari.server.controller.ConfigurationRequest cr : setRequest) {
            junit.framework.Assert.assertNull(cr.getClusterName());
            if (cr.getVersionTag().equals("tag1")) {
                containsTag1 = true;
            } else if (cr.getVersionTag().equals("tag2")) {
                containsTag2 = true;
            }
        }
        junit.framework.Assert.assertTrue(containsTag1);
        junit.framework.Assert.assertTrue(containsTag2);
        org.junit.Assert.assertEquals(2, resources.size());
        containsResource1 = false;
        containsResource2 = false;
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            java.lang.String tag = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG)));
            if (tag.equals("tag1")) {
                containsResource1 = true;
            } else if (tag.equals("tag2")) {
                containsResource2 = true;
            }
        }
        junit.framework.Assert.assertTrue(containsResource1);
        junit.framework.Assert.assertTrue(containsResource2);
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Configuration;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.replay(managementController, response);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG).equals("Configuration100").toPredicate();
        try {
            provider.updateResources(request, predicate);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Configuration;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG).equals("Configuration100").toPredicate();
        try {
            provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        EasyMock.verify(managementController);
    }
}