package org.apache.ambari.server.controller.predicate;
public class CategoryPredicateTest {
    @org.junit.Test
    public void testAccept() {
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.predicate.CategoryPredicateTest.TestCategoryPredicate predicate = new org.apache.ambari.server.controller.predicate.CategoryPredicateTest.TestCategoryPredicate(propertyId);
        org.apache.ambari.server.controller.predicate.CategoryPredicateTest.TestPredicateVisitor visitor = new org.apache.ambari.server.controller.predicate.CategoryPredicateTest.TestPredicateVisitor();
        predicate.accept(visitor);
        junit.framework.Assert.assertSame(predicate, visitor.visitedCategoryPredicate);
    }

    public static class TestCategoryPredicate extends org.apache.ambari.server.controller.predicate.CategoryPredicate {
        public TestCategoryPredicate(java.lang.String propertyId) {
            super(propertyId);
        }

        @java.lang.Override
        public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
            return false;
        }
    }

    public static class TestPredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
        org.apache.ambari.server.controller.predicate.CategoryPredicate visitedCategoryPredicate = null;

        @java.lang.Override
        public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        }

        @java.lang.Override
        public void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate predicate) {
        }

        @java.lang.Override
        public void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate) {
        }

        @java.lang.Override
        public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
        }

        @java.lang.Override
        public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
            visitedCategoryPredicate = predicate;
        }
    }
}