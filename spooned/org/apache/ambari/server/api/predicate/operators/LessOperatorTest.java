package org.apache.ambari.server.api.predicate.operators;
public class LessOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("LessOperator", new org.apache.ambari.server.api.predicate.operators.LessOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.LessPredicate<>("1", "2"), new org.apache.ambari.server.api.predicate.operators.LessOperator().toPredicate("1", "2"));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.LESS, new org.apache.ambari.server.api.predicate.operators.LessOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.LessOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.LessOperator().getPrecedence());
    }
}