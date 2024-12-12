package org.apache.ambari.server.api.predicate.operators;
public class LessEqualsOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("LessEqualsOperator", new org.apache.ambari.server.api.predicate.operators.LessEqualsOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.LessEqualsPredicate<>("1", "2"), new org.apache.ambari.server.api.predicate.operators.LessEqualsOperator().toPredicate("1", "2"));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.LESS_EQUAL, new org.apache.ambari.server.api.predicate.operators.LessEqualsOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.LessEqualsOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.LessEqualsOperator().getPrecedence());
    }
}