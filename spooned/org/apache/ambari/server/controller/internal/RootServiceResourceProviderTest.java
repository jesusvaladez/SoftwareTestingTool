package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class RootServiceResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.RootService;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.RootServiceResponse("service1"));
        allResponse.add(new org.apache.ambari.server.controller.RootServiceResponse("service2"));
        allResponse.add(new org.apache.ambari.server.controller.RootServiceResponse("service3"));
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(new org.apache.ambari.server.controller.RootServiceResponse("service4"));
        EasyMock.expect(managementController.getRootServices(org.easymock.EasyMock.anyObject())).andReturn(allResponse).once();
        EasyMock.expect(managementController.getRootServices(org.easymock.EasyMock.anyObject())).andReturn(nameResponse).once();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String serviceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID)));
            org.junit.Assert.assertTrue(allResponse.contains(new org.apache.ambari.server.controller.RootServiceResponse(serviceName)));
        }
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID).equals("service4").toPredicate();
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertEquals("service4", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID));
        EasyMock.verify(managementController);
    }
}