package org.apache.ambari.server.controller.internal;
public class PropertyPredicateVisitorTest {
    private static final java.lang.String PROPERTY_A = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category", "A");

    private static final java.lang.String PROPERTY_B = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category", "B");

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PROPERTY_A).equals("Monkey").toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PROPERTY_B).equals("Runner").toPredicate();

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_3 = new org.apache.ambari.server.controller.predicate.AndPredicate(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_1, org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_2);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_4 = new org.apache.ambari.server.controller.predicate.OrPredicate(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_1, org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_2);

    private static final org.apache.ambari.server.controller.spi.Predicate PREDICATE_5 = new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate("cat1");

    @org.junit.Test
    public void testVisit() {
        org.apache.ambari.server.controller.internal.PropertyPredicateVisitor visitor = new org.apache.ambari.server.controller.internal.PropertyPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_1, visitor);
        java.util.Map<java.lang.String, java.lang.Object> properties = visitor.getProperties();
        junit.framework.Assert.assertEquals(1, properties.size());
        junit.framework.Assert.assertEquals("Monkey", properties.get(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PROPERTY_A));
        visitor = new org.apache.ambari.server.controller.internal.PropertyPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_3, visitor);
        properties = visitor.getProperties();
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("Monkey", properties.get(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PROPERTY_A));
        junit.framework.Assert.assertEquals("Runner", properties.get(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PROPERTY_B));
        visitor = new org.apache.ambari.server.controller.internal.PropertyPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_4, visitor);
        properties = visitor.getProperties();
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("Monkey", properties.get(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PROPERTY_A));
        junit.framework.Assert.assertEquals("Runner", properties.get(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PROPERTY_B));
        visitor = new org.apache.ambari.server.controller.internal.PropertyPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(org.apache.ambari.server.controller.internal.PropertyPredicateVisitorTest.PREDICATE_5, visitor);
        properties = visitor.getProperties();
        junit.framework.Assert.assertTrue(properties.isEmpty());
    }
}