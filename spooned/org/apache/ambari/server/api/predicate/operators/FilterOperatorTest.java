package org.apache.ambari.server.api.predicate.operators;
public class FilterOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("FilterOperator", new org.apache.ambari.server.api.predicate.operators.FilterOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() throws java.lang.Exception {
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.FilterPredicate("p1", ".*"), new org.apache.ambari.server.api.predicate.operators.FilterOperator().toPredicate("p1", ".*"));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.FILTER, new org.apache.ambari.server.api.predicate.operators.FilterOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.FilterOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.FilterOperator().getPrecedence());
    }
}