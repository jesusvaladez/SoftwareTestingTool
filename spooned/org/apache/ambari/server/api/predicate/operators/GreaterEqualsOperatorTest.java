package org.apache.ambari.server.api.predicate.operators;
public class GreaterEqualsOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("GreaterEqualsOperator", new org.apache.ambari.server.api.predicate.operators.GreaterEqualsOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.GreaterEqualsPredicate<>("1", "2"), new org.apache.ambari.server.api.predicate.operators.GreaterEqualsOperator().toPredicate("1", "2"));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.GREATER_EQUAL, new org.apache.ambari.server.api.predicate.operators.GreaterEqualsOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.GreaterEqualsOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.GreaterEqualsOperator().getPrecedence());
    }
}