package org.apache.ambari.server.controller.internal;
public class RequestOperationLevelTest {
    private final java.lang.String host_component = "HOST_COMPONENT";

    private final java.lang.String hostComponent = "HostComponent";

    @org.junit.Test
    public void test_ConstructionFromRequestProperties() throws java.lang.Exception {
        java.lang.String c1 = "c1";
        java.lang.String host_component = "HOST_COMPONENT";
        java.lang.String service_id = "HDFS";
        java.lang.String hostcomponent_id = "Namenode";
        java.lang.String host_id = "host1";
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, "RESTART");
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, host_component);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, c1);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_SERVICE_ID, service_id);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOSTCOMPONENT_ID, hostcomponent_id);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOST_NAME, host_id);
        org.apache.ambari.server.controller.internal.RequestOperationLevel opLevel = new org.apache.ambari.server.controller.internal.RequestOperationLevel(requestInfoProperties);
        junit.framework.Assert.assertEquals(opLevel.getLevel().toString(), "HostComponent");
        junit.framework.Assert.assertEquals(opLevel.getClusterName(), c1);
        junit.framework.Assert.assertEquals(opLevel.getServiceName(), service_id);
        junit.framework.Assert.assertEquals(opLevel.getHostComponentName(), hostcomponent_id);
        junit.framework.Assert.assertEquals(opLevel.getHostName(), host_id);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, "wrong_value");
        try {
            new org.apache.ambari.server.controller.internal.RequestOperationLevel(requestInfoProperties);
            org.junit.Assert.fail("Should throw an exception");
        } catch (java.lang.IllegalArgumentException e) {
        }
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, host_component);
        requestInfoProperties.remove(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID);
        try {
            new org.apache.ambari.server.controller.internal.RequestOperationLevel(requestInfoProperties);
            org.junit.Assert.fail("Should throw an exception");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    @org.junit.Test
    public void testGetInternalLevelName() throws java.lang.Exception {
        java.lang.String internal = org.apache.ambari.server.controller.internal.RequestOperationLevel.getInternalLevelName(host_component);
        junit.framework.Assert.assertEquals(internal, hostComponent);
        internal = org.apache.ambari.server.controller.internal.RequestOperationLevel.getInternalLevelName(host_component.toLowerCase());
        junit.framework.Assert.assertEquals(internal, hostComponent);
        try {
            org.apache.ambari.server.controller.internal.RequestOperationLevel.getInternalLevelName("Wrong_param");
            junit.framework.Assert.fail("Should throw exception");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    @org.junit.Test
    public void testGetExternalLevelName() throws java.lang.Exception {
        java.lang.String external = org.apache.ambari.server.controller.internal.RequestOperationLevel.getExternalLevelName(hostComponent);
        junit.framework.Assert.assertEquals(external, host_component);
        try {
            org.apache.ambari.server.controller.internal.RequestOperationLevel.getExternalLevelName("Wrong_param");
            junit.framework.Assert.fail("Should throw exception");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }
}