package org.apache.ambari.server.api.predicate.operators;
public class OrOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("OrOperator", new org.apache.ambari.server.api.predicate.operators.OrOperator(1).getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.apache.ambari.server.controller.predicate.EqualsPredicate p1 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("p1", "one");
        org.apache.ambari.server.controller.predicate.EqualsPredicate p2 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("p2", "two");
        org.apache.ambari.server.controller.predicate.OrPredicate orPRedicate = new org.apache.ambari.server.controller.predicate.OrPredicate(p1, p2);
        org.junit.Assert.assertEquals(orPRedicate, new org.apache.ambari.server.api.predicate.operators.OrOperator(1).toPredicate(p1, p2));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.OR, new org.apache.ambari.server.api.predicate.operators.OrOperator(1).getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(1, new org.apache.ambari.server.api.predicate.operators.OrOperator(1).getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(3, new org.apache.ambari.server.api.predicate.operators.OrOperator(2).getPrecedence());
    }
}