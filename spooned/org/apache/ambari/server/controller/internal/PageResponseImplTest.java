package org.apache.ambari.server.controller.internal;
public class PageResponseImplTest {
    @org.junit.Test
    public void testGetIterable() throws java.lang.Exception {
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Resource prev = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.Resource next = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.PageResponse response = new org.apache.ambari.server.controller.internal.PageResponseImpl(iterable, 99, prev, next, 0);
        junit.framework.Assert.assertEquals(iterable, response.getIterable());
    }

    @org.junit.Test
    public void testGetOffset() throws java.lang.Exception {
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Resource prev = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.Resource next = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.PageResponse response = new org.apache.ambari.server.controller.internal.PageResponseImpl(iterable, 99, prev, next, 0);
        junit.framework.Assert.assertEquals(99, response.getOffset());
    }

    @org.junit.Test
    public void testGetPreviousResource() throws java.lang.Exception {
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Resource prev = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.Resource next = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.PageResponse response = new org.apache.ambari.server.controller.internal.PageResponseImpl(iterable, 99, prev, next, 0);
        junit.framework.Assert.assertEquals(prev, response.getPreviousResource());
    }

    @org.junit.Test
    public void testGetNextResource() throws java.lang.Exception {
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> iterable = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Resource prev = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.Resource next = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.PageResponse response = new org.apache.ambari.server.controller.internal.PageResponseImpl(iterable, 99, prev, next, 0);
        junit.framework.Assert.assertEquals(next, response.getNextResource());
    }
}