package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class StackConfigurationDependencyResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.StackConfigurationDependencyResponse("depName", "depType"));
        EasyMock.expect(managementController.getStackConfigurationDependencies(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getStackConfigurationDependencyRequestSet(null, null, null, null, null))).andReturn(allResponse).times(1);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.SERVICE_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.PROPERTY_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_TYPE_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String dependencyName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_NAME_PROPERTY_ID)));
            java.lang.String dependencyType = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_TYPE_PROPERTY_ID)));
            org.junit.Assert.assertEquals("depName", dependencyName);
            org.junit.Assert.assertEquals("depType", dependencyType);
        }
        EasyMock.verify(managementController);
    }
}