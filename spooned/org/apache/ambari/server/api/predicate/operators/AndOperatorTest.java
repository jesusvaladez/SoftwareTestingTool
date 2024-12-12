package org.apache.ambari.server.api.predicate.operators;
public class AndOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("AndOperator", new org.apache.ambari.server.api.predicate.operators.AndOperator(1).getName());
    }

    @org.junit.Test
    public void testToPredicate() {
        org.apache.ambari.server.controller.predicate.EqualsPredicate p1 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("p1", "one");
        org.apache.ambari.server.controller.predicate.EqualsPredicate p2 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("p2", "two");
        org.apache.ambari.server.controller.predicate.AndPredicate andPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(p1, p2);
        org.junit.Assert.assertEquals(andPredicate, new org.apache.ambari.server.api.predicate.operators.AndOperator(1).toPredicate(p1, p2));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.AND, new org.apache.ambari.server.api.predicate.operators.AndOperator(1).getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(2, new org.apache.ambari.server.api.predicate.operators.AndOperator(1).getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(4, new org.apache.ambari.server.api.predicate.operators.AndOperator(2).getPrecedence());
    }
}