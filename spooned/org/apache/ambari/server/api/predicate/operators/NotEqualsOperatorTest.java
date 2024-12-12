package org.apache.ambari.server.api.predicate.operators;
public class NotEqualsOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("NotEqualsOperator", new org.apache.ambari.server.api.predicate.operators.NotEqualsOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.NotPredicate(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("prop", "val")), new org.apache.ambari.server.api.predicate.operators.NotEqualsOperator().toPredicate("prop", "val"));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.NOT_EQUAL, new org.apache.ambari.server.api.predicate.operators.NotEqualsOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.NotEqualsOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.NotEqualsOperator().getPrecedence());
    }
}