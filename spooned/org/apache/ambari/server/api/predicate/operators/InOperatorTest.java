package org.apache.ambari.server.api.predicate.operators;
public class InOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("InOperator", new org.apache.ambari.server.api.predicate.operators.InOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() throws java.lang.Exception {
        java.lang.String prop = "prop";
        java.lang.String val = "one,2,three";
        org.apache.ambari.server.controller.predicate.EqualsPredicate p1 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(prop, "one");
        org.apache.ambari.server.controller.predicate.EqualsPredicate p2 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(prop, "2");
        org.apache.ambari.server.controller.predicate.EqualsPredicate p3 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(prop, "three");
        org.apache.ambari.server.controller.predicate.OrPredicate orPredicate = new org.apache.ambari.server.controller.predicate.OrPredicate(p1, p2, p3);
        org.junit.Assert.assertEquals(orPredicate, new org.apache.ambari.server.api.predicate.operators.InOperator().toPredicate(prop, val));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.IN, new org.apache.ambari.server.api.predicate.operators.InOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.InOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.InOperator().getPrecedence());
    }
}