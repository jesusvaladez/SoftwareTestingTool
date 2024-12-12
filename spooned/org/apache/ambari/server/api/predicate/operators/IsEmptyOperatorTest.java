package org.apache.ambari.server.api.predicate.operators;
public class IsEmptyOperatorTest {
    @org.junit.Test
    public void testGetName() {
        org.junit.Assert.assertEquals("IsEmptyOperator", new org.apache.ambari.server.api.predicate.operators.IsEmptyOperator().getName());
    }

    @org.junit.Test
    public void testToPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.lang.String prop = "prop";
        org.apache.ambari.server.controller.spi.Predicate p = new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate(prop);
        org.junit.Assert.assertEquals(p, new org.apache.ambari.server.api.predicate.operators.IsEmptyOperator().toPredicate(prop, null));
    }

    @org.junit.Test
    public void testGetType() {
        org.junit.Assert.assertSame(org.apache.ambari.server.api.predicate.operators.Operator.TYPE.IS_EMPTY, new org.apache.ambari.server.api.predicate.operators.IsEmptyOperator().getType());
    }

    @org.junit.Test
    public void testGetBasePrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.IsEmptyOperator().getBasePrecedence());
    }

    @org.junit.Test
    public void testGetPrecedence() {
        org.junit.Assert.assertEquals(-1, new org.apache.ambari.server.api.predicate.operators.IsEmptyOperator().getPrecedence());
    }
}