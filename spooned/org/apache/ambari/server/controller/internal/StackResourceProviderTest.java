package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class StackResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Stack;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.StackResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.StackResponse("Stack1"));
        allResponse.add(new org.apache.ambari.server.controller.StackResponse("Stack2"));
        EasyMock.expect(managementController.getStacks(org.easymock.EasyMock.anyObject())).andReturn(allResponse).once();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.StackResourceProvider.STACK_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(2, resources.size());
        java.util.Set<java.lang.String> stackNames = new java.util.HashSet<>();
        stackNames.add("Stack1");
        stackNames.add("Stack2");
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String stackName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackResourceProvider.STACK_NAME_PROPERTY_ID)));
            org.junit.Assert.assertTrue(stackNames.contains(stackName));
        }
        EasyMock.verify(managementController);
    }
}