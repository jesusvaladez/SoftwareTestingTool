package org.apache.ambari.server.api.predicate.operators;
public class EqualsOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("EqualsOperator", new org.apache.ambari.server.api.predicate.operators.EqualsOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("prop", "val"), new org.apache.ambari.server.api.predicate.operators.EqualsOperator().toPredicate("prop", "val"));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.EQUAL, new org.apache.ambari.server.api.predicate.operators.EqualsOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.EqualsOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.EqualsOperator().getPrecedence());
    }
}