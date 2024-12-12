package org.apache.ambari.server.controller.internal;
public class PageRequestImplTest {
    @org.junit.Test
    public void testGetStartingPoint() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 20, 99, null, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, pageRequest.getStartingPoint());
    }

    @org.junit.Test
    public void testGetPageSize() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 20, 99, null, null);
        org.junit.Assert.assertEquals(20, pageRequest.getPageSize());
    }

    @org.junit.Test
    public void testGetOffset() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 20, 99, null, null);
        org.junit.Assert.assertEquals(99, pageRequest.getOffset());
    }

    @org.junit.Test
    public void testGetPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.utilities.PredicateBuilder pb = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        org.apache.ambari.server.controller.spi.Predicate predicate = pb.property("id").equals(20).toPredicate();
        org.apache.ambari.server.controller.spi.PageRequest pageRequest = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 20, 99, predicate, null);
        org.junit.Assert.assertEquals(predicate, pageRequest.getPredicate());
    }
}