package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class StackServiceResourceProviderTest {
    private final java.lang.String SERVICE_PROPERTIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "properties");

    private final org.apache.ambari.server.state.ServicePropertyInfo P1 = new org.apache.ambari.server.state.ServicePropertyInfo();

    private final org.apache.ambari.server.state.ServicePropertyInfo P2 = new org.apache.ambari.server.state.ServicePropertyInfo();

    private java.util.Map<java.lang.String, java.lang.String> TEST_SERVICE_PROPERTIES = null;

    private java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> TEST_SERVICE_PROPERTY_LIST = null;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        P1.setName("P1");
        P1.setValue("V1");
        P2.setName("P2");
        P2.setValue("V2");
        TEST_SERVICE_PROPERTY_LIST = com.google.common.collect.ImmutableList.of(P1, P2);
        TEST_SERVICE_PROPERTIES = com.google.common.collect.ImmutableMap.of(P1.getName(), P1.getValue(), P2.getName(), P2.getValue());
    }

    @org.junit.Test
    public void testGetServiceProperties() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.StackService;
        org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse = EasyMock.createNiceMock(org.apache.ambari.server.controller.StackServiceResponse.class);
        EasyMock.expect(stackServiceResponse.getServiceProperties()).andReturn(TEST_SERVICE_PROPERTIES);
        EasyMock.expect(managementController.getStackServices(org.easymock.EasyMock.anyObject())).andReturn(com.google.common.collect.ImmutableSet.of(stackServiceResponse));
        EasyMock.replay(managementController, stackServiceResponse);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(SERVICE_PROPERTIES_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.ResourceProvider stackServiceResourceProvider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = stackServiceResourceProvider.getResources(request, null);
        org.apache.ambari.server.controller.spi.Resource expected = new org.apache.ambari.server.controller.internal.ResourceImpl(type);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(expected, SERVICE_PROPERTIES_PROPERTY_ID, TEST_SERVICE_PROPERTIES, com.google.common.collect.ImmutableSet.of(SERVICE_PROPERTIES_PROPERTY_ID));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(expected), resources);
        EasyMock.verify(managementController, stackServiceResponse);
    }

    @org.junit.Test
    public void testGetVisibilityServiceProperties() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.StackService;
        org.apache.ambari.server.state.ServiceInfo serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
        serviceInfo.setServicePropertyList(TEST_SERVICE_PROPERTY_LIST);
        org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse = new org.apache.ambari.server.controller.StackServiceResponse(serviceInfo);
        EasyMock.expect(managementController.getStackServices(org.easymock.EasyMock.anyObject())).andReturn(com.google.common.collect.ImmutableSet.of(stackServiceResponse));
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(SERVICE_PROPERTIES_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.ResourceProvider stackServiceResourceProvider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = stackServiceResourceProvider.getResources(request, null);
        java.util.Map<java.lang.String, java.lang.String> expectedServiceProperties = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().putAll(TEST_SERVICE_PROPERTIES).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY).put(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY).build();
        org.apache.ambari.server.controller.spi.Resource expected = new org.apache.ambari.server.controller.internal.ResourceImpl(type);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(expected, SERVICE_PROPERTIES_PROPERTY_ID, expectedServiceProperties, com.google.common.collect.ImmutableSet.of(SERVICE_PROPERTIES_PROPERTY_ID));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of(expected), resources);
        EasyMock.verify(managementController);
    }
}