package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ExtensionResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Extension;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.ExtensionResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.ExtensionResponse("Extension1"));
        allResponse.add(new org.apache.ambari.server.controller.ExtensionResponse("Extension2"));
        EasyMock.expect(managementController.getExtensions(org.easymock.EasyMock.anyObject())).andReturn(allResponse).once();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ExtensionResourceProvider.EXTENSION_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(2, resources.size());
        java.util.Set<java.lang.String> extensionNames = new java.util.HashSet<>();
        extensionNames.add("Extension1");
        extensionNames.add("Extension2");
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String extensionName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ExtensionResourceProvider.EXTENSION_NAME_PROPERTY_ID)));
            org.junit.Assert.assertTrue(extensionNames.contains(extensionName));
        }
        EasyMock.verify(managementController);
    }
}