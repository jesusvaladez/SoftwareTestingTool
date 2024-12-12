package org.apache.ambari.server.api.predicate.operators;
public class GreaterOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("GreaterOperator", new org.apache.ambari.server.api.predicate.operators.GreaterOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.GreaterPredicate<>("1", "2"), new org.apache.ambari.server.api.predicate.operators.GreaterOperator().toPredicate("1", "2"));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.GREATER, new org.apache.ambari.server.api.predicate.operators.GreaterOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.GreaterOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.GreaterOperator().getPrecedence());
    }
}