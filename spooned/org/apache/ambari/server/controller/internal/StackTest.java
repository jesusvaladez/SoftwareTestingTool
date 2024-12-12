package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verifyAll;
public class StackTest {
    @org.junit.Test
    public void testTestXmlExtensionStrippedOff() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController controller = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackServiceRequest>> stackServiceRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceResponse.class);
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackServiceComponentRequest>> stackComponentRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.StackServiceComponentResponse stackComponentResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceComponentResponse.class);
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackConfigurationRequest>> stackConfigurationRequestCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>> stackLevelConfigurationRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.controller.StackConfigurationResponse.class);
        EasyMock.expect(controller.getStackServices(EasyMock.capture(stackServiceRequestCapture))).andReturn(java.util.Collections.singleton(stackServiceResponse)).anyTimes();
        EasyMock.expect(controller.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.expect(stackServiceResponse.getServiceName()).andReturn("service1").anyTimes();
        EasyMock.expect(stackServiceResponse.getExcludedConfigTypes()).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(stackServiceResponse.getConfigTypes()).andReturn(java.util.Collections.emptyMap());
        EasyMock.expect(controller.getStackComponents(EasyMock.capture(stackComponentRequestCapture))).andReturn(java.util.Collections.singleton(stackComponentResponse)).anyTimes();
        EasyMock.expect(stackComponentResponse.getComponentName()).andReturn("component1").anyTimes();
        EasyMock.expect(stackComponentResponse.getComponentCategory()).andReturn("test-site.xml").anyTimes();
        EasyMock.expect(controller.getStackConfigurations(EasyMock.capture(stackConfigurationRequestCapture))).andReturn(java.util.Collections.singleton(stackConfigurationResponse)).anyTimes();
        EasyMock.expect(controller.getStackLevelConfigurations(EasyMock.capture(stackLevelConfigurationRequestCapture))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyName()).andReturn("prop1").anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyValue()).andReturn("prop1Val").anyTimes();
        EasyMock.expect(stackConfigurationResponse.getType()).andReturn("test-site.xml").anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyType()).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyAttributes()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stackConfigurationResponse.isRequired()).andReturn(true).anyTimes();
        EasyMock.expect(metaInfo.getComponentDependencies("test", "1.0", "service1", "component1")).andReturn(java.util.Collections.emptyList()).anyTimes();
        replay(controller, stackServiceResponse, stackComponentResponse, stackConfigurationResponse, metaInfo);
        org.apache.ambari.server.controller.internal.Stack stack = new org.apache.ambari.server.controller.internal.Stack("test", "1.0", controller);
        org.apache.ambari.server.topology.Configuration configuration = stack.getConfiguration(java.util.Collections.singleton("service1"));
        org.junit.Assert.assertEquals("prop1Val", configuration.getProperties().get("test-site").get("prop1"));
        org.junit.Assert.assertEquals("test-site", stack.getRequiredConfigurationProperties("service1").iterator().next().getType());
        org.apache.ambari.server.controller.StackServiceRequest stackServiceRequest = stackServiceRequestCapture.getValue().iterator().next();
        org.junit.Assert.assertEquals("test", stackServiceRequest.getStackName());
        org.junit.Assert.assertEquals("1.0", stackServiceRequest.getStackVersion());
        org.apache.ambari.server.controller.StackServiceComponentRequest stackComponentRequest = stackComponentRequestCapture.getValue().iterator().next();
        org.junit.Assert.assertEquals("service1", stackComponentRequest.getServiceName());
        org.junit.Assert.assertEquals("test", stackComponentRequest.getStackName());
        org.junit.Assert.assertEquals("1.0", stackComponentRequest.getStackVersion());
        org.junit.Assert.assertNull(stackComponentRequest.getComponentName());
    }

    @org.junit.Test
    public void testConfigPropertyReadsInDependencies() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> setOfDependencyInfo = new java.util.HashSet<>();
        org.apache.ambari.server.controller.StackConfigurationResponse mockResponse = mockSupport.createMock(org.apache.ambari.server.controller.StackConfigurationResponse.class);
        EasyMock.expect(mockResponse.getPropertyName()).andReturn("test-property-one");
        EasyMock.expect(mockResponse.getPropertyValue()).andReturn("test-value-one");
        EasyMock.expect(mockResponse.getPropertyAttributes()).andReturn(java.util.Collections.emptyMap());
        EasyMock.expect(mockResponse.getPropertyType()).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(mockResponse.getType()).andReturn("test-type-one");
        EasyMock.expect(mockResponse.getDependsOnProperties()).andReturn(setOfDependencyInfo);
        EasyMock.expect(mockResponse.getPropertyValueAttributes()).andReturn(new org.apache.ambari.server.state.ValueAttributesInfo());
        mockSupport.replayAll();
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(mockResponse);
        org.junit.Assert.assertSame("DependencyInfo was not properly parsed from the stack response object", setOfDependencyInfo, configProperty.getDependsOnProperties());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testGetRequiredProperties_serviceAndPropertyType() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController controller = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackServiceRequest>> stackServiceRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceResponse.class);
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackServiceComponentRequest>> stackComponentRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.StackServiceComponentResponse stackComponentResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceComponentResponse.class);
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackConfigurationRequest>> stackConfigurationRequestCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>> stackLevelConfigurationRequestCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.controller.StackConfigurationResponse.class);
        org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse2 = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.controller.StackConfigurationResponse.class);
        EasyMock.expect(controller.getStackServices(EasyMock.capture(stackServiceRequestCapture))).andReturn(java.util.Collections.singleton(stackServiceResponse)).anyTimes();
        EasyMock.expect(controller.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.expect(stackServiceResponse.getServiceName()).andReturn("service1").anyTimes();
        EasyMock.expect(stackServiceResponse.getExcludedConfigTypes()).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(stackServiceResponse.getConfigTypes()).andReturn(java.util.Collections.emptyMap());
        EasyMock.expect(controller.getStackComponents(EasyMock.capture(stackComponentRequestCapture))).andReturn(java.util.Collections.singleton(stackComponentResponse)).anyTimes();
        EasyMock.expect(stackComponentResponse.getComponentName()).andReturn("component1").anyTimes();
        EasyMock.expect(stackComponentResponse.getComponentCategory()).andReturn("test-site.xml").anyTimes();
        EasyMock.expect(controller.getStackConfigurations(EasyMock.capture(stackConfigurationRequestCapture))).andReturn(new java.util.HashSet<>(java.util.Arrays.asList(stackConfigurationResponse, stackConfigurationResponse2))).anyTimes();
        EasyMock.expect(controller.getStackLevelConfigurations(EasyMock.capture(stackLevelConfigurationRequestCapture))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyName()).andReturn("prop1").anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyValue()).andReturn(null).anyTimes();
        EasyMock.expect(stackConfigurationResponse.getType()).andReturn("test-site.xml").anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyType()).andReturn(java.util.Collections.singleton(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)).anyTimes();
        EasyMock.expect(stackConfigurationResponse.getPropertyAttributes()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stackConfigurationResponse.isRequired()).andReturn(true).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyName()).andReturn("prop2").anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyValue()).andReturn(null).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getType()).andReturn("test-site.xml").anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyType()).andReturn(java.util.Collections.singleton(org.apache.ambari.server.state.PropertyInfo.PropertyType.USER)).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyAttributes()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.isRequired()).andReturn(true).anyTimes();
        EasyMock.expect(metaInfo.getComponentDependencies("test", "1.0", "service1", "component1")).andReturn(java.util.Collections.emptyList()).anyTimes();
        replay(controller, stackServiceResponse, stackComponentResponse, stackConfigurationResponse, stackConfigurationResponse2, metaInfo);
        org.apache.ambari.server.controller.internal.Stack stack = new org.apache.ambari.server.controller.internal.Stack("test", "1.0", controller);
        java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> requiredPasswordProperties = stack.getRequiredConfigurationProperties("service1", org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD);
        org.junit.Assert.assertEquals(1, requiredPasswordProperties.size());
        org.apache.ambari.server.controller.internal.Stack.ConfigProperty requiredPasswordConfigProperty = requiredPasswordProperties.iterator().next();
        org.junit.Assert.assertEquals("test-site", requiredPasswordConfigProperty.getType());
        org.junit.Assert.assertEquals("prop1", requiredPasswordConfigProperty.getName());
        org.junit.Assert.assertTrue(requiredPasswordConfigProperty.getPropertyTypes().contains(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD));
        org.apache.ambari.server.controller.StackServiceRequest stackServiceRequest = stackServiceRequestCapture.getValue().iterator().next();
        org.junit.Assert.assertEquals("test", stackServiceRequest.getStackName());
        org.junit.Assert.assertEquals("1.0", stackServiceRequest.getStackVersion());
        org.apache.ambari.server.controller.StackServiceComponentRequest stackComponentRequest = stackComponentRequestCapture.getValue().iterator().next();
        org.junit.Assert.assertEquals("service1", stackComponentRequest.getServiceName());
        org.junit.Assert.assertEquals("test", stackComponentRequest.getStackName());
        org.junit.Assert.assertEquals("1.0", stackComponentRequest.getStackVersion());
        org.junit.Assert.assertNull(stackComponentRequest.getComponentName());
    }

    @org.junit.Test
    public void testGetAllConfigurationTypesWithEmptyStackServiceConfigType() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController controller = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceResponse.class);
        org.apache.ambari.server.controller.StackServiceComponentResponse stackComponentResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceComponentResponse.class);
        org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse1 = createNiceMock(org.apache.ambari.server.controller.StackConfigurationResponse.class);
        org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse2 = createNiceMock(org.apache.ambari.server.controller.StackConfigurationResponse.class);
        java.lang.String testServiceName = "service1";
        java.lang.String testEmptyConfigType = "test-empty-config-type";
        java.lang.String testSiteConfigFile = "test-site.xml";
        java.lang.String testSiteConfigType = "test-site";
        EasyMock.expect(controller.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.expect(controller.getStackServices(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singleton(stackServiceResponse)).anyTimes();
        EasyMock.expect(stackServiceResponse.getServiceName()).andReturn(testServiceName).anyTimes();
        EasyMock.expect(stackServiceResponse.getExcludedConfigTypes()).andReturn(java.util.Collections.emptySet());
        EasyMock.expect(stackComponentResponse.getComponentName()).andReturn("component1").anyTimes();
        EasyMock.expect(stackComponentResponse.getComponentCategory()).andReturn(testSiteConfigFile).anyTimes();
        EasyMock.expect(controller.getStackComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singleton(stackComponentResponse)).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyName()).andReturn("prop1").anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyValue()).andReturn(null).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getType()).andReturn(testSiteConfigFile).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyType()).andReturn(java.util.Collections.singleton(org.apache.ambari.server.state.PropertyInfo.PropertyType.TEXT)).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyAttributes()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.isRequired()).andReturn(true).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyName()).andReturn("prop2").anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyValue()).andReturn(null).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getType()).andReturn(testSiteConfigFile).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyType()).andReturn(java.util.Collections.singleton(org.apache.ambari.server.state.PropertyInfo.PropertyType.USER)).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.getPropertyAttributes()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stackConfigurationResponse2.isRequired()).andReturn(true).anyTimes();
        EasyMock.expect(controller.getStackConfigurations(org.easymock.EasyMock.anyObject())).andReturn(com.google.common.collect.Sets.newHashSet(stackConfigurationResponse1, stackConfigurationResponse2)).anyTimes();
        EasyMock.expect(stackServiceResponse.getConfigTypes()).andReturn(java.util.Collections.singletonMap(testEmptyConfigType, java.util.Collections.emptyMap()));
        EasyMock.expect(controller.getStackLevelConfigurations(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(metaInfo.getComponentDependencies("test", "1.0", "service1", "component1")).andReturn(java.util.Collections.emptyList()).anyTimes();
        replay(controller, stackServiceResponse, stackComponentResponse, stackConfigurationResponse1, stackConfigurationResponse2, metaInfo);
        org.apache.ambari.server.controller.internal.Stack stack = new org.apache.ambari.server.controller.internal.Stack("test", "1.0", controller);
        java.util.Collection<java.lang.String> allServiceConfigTypes = stack.getAllConfigurationTypes(testServiceName);
        org.junit.Assert.assertTrue(allServiceConfigTypes.containsAll(com.google.common.collect.ImmutableSet.of(testSiteConfigType, testEmptyConfigType)));
        org.junit.Assert.assertEquals(2, allServiceConfigTypes.size());
        verifyAll();
    }

    @org.junit.Test
    public void testGetServiceForConfigTypeWithExcludedConfigs() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController controller = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceResponse.class);
        org.apache.ambari.server.controller.StackServiceComponentResponse stackComponentResponse = createNiceMock(org.apache.ambari.server.controller.StackServiceComponentResponse.class);
        org.apache.ambari.server.controller.StackConfigurationResponse stackConfigurationResponse1 = createNiceMock(org.apache.ambari.server.controller.StackConfigurationResponse.class);
        java.lang.String testServiceName = "service1";
        java.lang.String testEmptyConfigType = "test-empty-config-type";
        java.lang.String testSiteConfigFile = "test-site.xml";
        java.lang.String testSiteConfigType = "test-site";
        EasyMock.expect(controller.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.expect(controller.getStackServices(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singleton(stackServiceResponse)).anyTimes();
        EasyMock.expect(stackServiceResponse.getServiceName()).andReturn(testServiceName).anyTimes();
        EasyMock.expect(stackServiceResponse.getExcludedConfigTypes()).andReturn(java.util.Collections.singleton(testSiteConfigType));
        EasyMock.expect(stackComponentResponse.getComponentName()).andReturn("component1").anyTimes();
        EasyMock.expect(stackComponentResponse.getComponentCategory()).andReturn(testSiteConfigFile).anyTimes();
        EasyMock.expect(controller.getStackComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singleton(stackComponentResponse)).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyName()).andReturn("prop1").anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyValue()).andReturn(null).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getType()).andReturn(testSiteConfigFile).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyType()).andReturn(java.util.Collections.singleton(org.apache.ambari.server.state.PropertyInfo.PropertyType.TEXT)).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.getPropertyAttributes()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stackConfigurationResponse1.isRequired()).andReturn(true).anyTimes();
        EasyMock.expect(controller.getStackConfigurations(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singleton(stackConfigurationResponse1)).anyTimes();
        EasyMock.expect(stackServiceResponse.getConfigTypes()).andReturn(java.util.Collections.singletonMap(testEmptyConfigType, java.util.Collections.emptyMap()));
        EasyMock.expect(controller.getStackLevelConfigurations(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(metaInfo.getComponentDependencies("test", "1.0", "service1", "component1")).andReturn(java.util.Collections.emptyList()).anyTimes();
        replay(controller, stackServiceResponse, stackComponentResponse, stackConfigurationResponse1, metaInfo);
        org.apache.ambari.server.controller.internal.Stack stack = new org.apache.ambari.server.controller.internal.Stack("test", "1.0", controller);
        try {
            stack.getServiceForConfigType(testSiteConfigType);
            org.junit.Assert.fail("Exception not thrown");
        } catch (java.lang.IllegalArgumentException e) {
        }
        org.junit.Assert.assertEquals(testServiceName, stack.getServiceForConfigType(testEmptyConfigType));
        verifyAll();
    }
}