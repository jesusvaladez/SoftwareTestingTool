package org.apache.ambari.server.api.predicate.operators;
public class NotOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("NotOperator", new org.apache.ambari.server.api.predicate.operators.NotOperator(1).getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.apache.ambari.server.controller.predicate.EqualsPredicate p = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("prop", "val");
        org.apache.ambari.server.controller.predicate.NotPredicate notPredicate = new org.apache.ambari.server.controller.predicate.NotPredicate(p);
        org.junit.Assert.assertEquals(notPredicate, new org.apache.ambari.server.api.predicate.operators.NotOperator(1).toPredicate(null, p));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.NOT, new org.apache.ambari.server.api.predicate.operators.NotOperator(1).getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(3, new org.apache.ambari.server.api.predicate.operators.NotOperator(1).getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(5, new org.apache.ambari.server.api.predicate.operators.NotOperator(2).getPrecedence());
    }
}