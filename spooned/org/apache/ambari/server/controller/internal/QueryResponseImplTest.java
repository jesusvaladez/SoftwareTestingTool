package org.apache.ambari.server.controller.internal;
public class QueryResponseImplTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        resources.add(new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Stage));
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources);
        junit.framework.Assert.assertEquals(resources, queryResponse.getResources());
    }

    @org.junit.Test
    public void testIsSortedResponse() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        resources.add(new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Stage));
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources);
        junit.framework.Assert.assertFalse(queryResponse.isSortedResponse());
        queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources, false, true, 0);
        junit.framework.Assert.assertFalse(queryResponse.isSortedResponse());
        queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources, true, true, 0);
        junit.framework.Assert.assertTrue(queryResponse.isSortedResponse());
    }

    @org.junit.Test
    public void testIsPagedResponse() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        resources.add(new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Stage));
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources);
        junit.framework.Assert.assertFalse(queryResponse.isPagedResponse());
        queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources, true, false, 0);
        junit.framework.Assert.assertFalse(queryResponse.isPagedResponse());
        queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources, true, true, 0);
        junit.framework.Assert.assertTrue(queryResponse.isPagedResponse());
    }

    @org.junit.Test
    public void testGetTotalResourceCount() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        resources.add(new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Stage));
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources);
        junit.framework.Assert.assertEquals(0, queryResponse.getTotalResourceCount());
        queryResponse = new org.apache.ambari.server.controller.internal.QueryResponseImpl(resources, true, false, 99);
        junit.framework.Assert.assertEquals(99, queryResponse.getTotalResourceCount());
    }
}