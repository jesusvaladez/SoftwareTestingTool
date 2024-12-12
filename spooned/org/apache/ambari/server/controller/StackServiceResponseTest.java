package org.apache.ambari.server.controller;
public class StackServiceResponseTest {
    private org.apache.ambari.server.state.ServiceInfo serviceInfo;

    @org.junit.Before
    public void setUp() {
        serviceInfo = new org.apache.ambari.server.state.ServiceInfo();
    }

    @org.junit.Test
    public void testDefaultServiceVisibilityProperties() {
        org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse = new org.apache.ambari.server.controller.StackServiceResponse(serviceInfo);
        org.junit.Assert.assertTrue("true".equals(stackServiceResponse.getServiceProperties().get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_INSTALLABLE_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("true".equals(stackServiceResponse.getServiceProperties().get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MANAGED_PROPERTY.getKey())));
        org.junit.Assert.assertTrue("true".equals(stackServiceResponse.getServiceProperties().get(org.apache.ambari.server.state.ServiceInfo.DEFAULT_SERVICE_MONITORED_PROPERTY.getKey())));
    }
}