package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class RootServiceComponentResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> allResponse = new java.util.HashSet<>();
        java.lang.String serviceName = org.apache.ambari.server.controller.RootService.AMBARI.name();
        java.util.Map<java.lang.String, java.lang.String> emptyMap = java.util.Collections.emptyMap();
        allResponse.add(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, "component1", "1.1.1", emptyMap));
        allResponse.add(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, "component2", "1.1.1", emptyMap));
        allResponse.add(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, "component3", "1.1.1", emptyMap));
        allResponse.add(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), "1.1.1", emptyMap));
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, "component4", "1.1.1", emptyMap));
        EasyMock.expect(managementController.getRootServiceComponents(org.easymock.EasyMock.anyObject())).andReturn(allResponse).once();
        EasyMock.expect(managementController.getRootServiceComponents(org.easymock.EasyMock.anyObject())).andReturn(nameResponse).once();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.PROPERTIES_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVER_CLOCK_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID)));
            java.lang.String componentVersion = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_VERSION_PROPERTY_ID)));
            java.lang.Long server_clock = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVER_CLOCK_PROPERTY_ID)));
            if (componentName.equals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())) {
                org.junit.Assert.assertNotNull(server_clock);
            } else {
                org.junit.Assert.assertNull(server_clock);
            }
            org.junit.Assert.assertTrue(allResponse.contains(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, componentName, componentVersion, emptyMap)));
        }
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID).equals("component4").toPredicate();
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertEquals("component4", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID));
        EasyMock.verify(managementController);
    }
}