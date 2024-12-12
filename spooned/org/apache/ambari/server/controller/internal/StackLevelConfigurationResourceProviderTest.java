package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class StackLevelConfigurationResourceProviderTest {
    private static final java.lang.String PROPERTY_NAME = "name";

    private static final java.lang.String PROPERTY_VALUE = "value";

    private static final java.lang.String PROPERTY_DESC = "Desc";

    private static final java.lang.String TYPE = "type.xml";

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
        attributes.put("final", "true");
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.StackConfigurationResponse(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_NAME, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_VALUE, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_DESC, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.TYPE, attributes));
        EasyMock.expect(managementController.getStackLevelConfigurations(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getStackLevelConfigurationRequestSet(null, null, null))).andReturn(allResponse).times(1);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DESCRIPTION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_TYPE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_FINAL_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String propertyName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID)));
            java.lang.String propertyValue = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_PROPERTY_ID)));
            java.lang.String propertyDesc = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DESCRIPTION_PROPERTY_ID)));
            java.lang.String propertyType = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_TYPE_PROPERTY_ID)));
            java.lang.String propertyIsFinal = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_FINAL_PROPERTY_ID)));
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_NAME, propertyName);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_VALUE, propertyValue);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_DESC, propertyDesc);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.TYPE, propertyType);
            org.junit.Assert.assertEquals("true", propertyIsFinal);
        }
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    public void testGetResources_noFinal() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.StackConfigurationResponse(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_NAME, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_VALUE, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_DESC, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.TYPE, attributes));
        EasyMock.expect(managementController.getStackLevelConfigurations(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getStackLevelConfigurationRequestSet(null, null, null))).andReturn(allResponse).times(1);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DESCRIPTION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_TYPE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_FINAL_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String propertyName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID)));
            java.lang.String propertyValue = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_PROPERTY_ID)));
            java.lang.String propertyDesc = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DESCRIPTION_PROPERTY_ID)));
            java.lang.String propertyType = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_TYPE_PROPERTY_ID)));
            java.lang.String propertyIsFinal = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_FINAL_PROPERTY_ID)));
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_NAME, propertyName);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_VALUE, propertyValue);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.PROPERTY_DESC, propertyDesc);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProviderTest.TYPE, propertyType);
            org.junit.Assert.assertEquals("false", propertyIsFinal);
        }
        EasyMock.verify(managementController);
    }
}