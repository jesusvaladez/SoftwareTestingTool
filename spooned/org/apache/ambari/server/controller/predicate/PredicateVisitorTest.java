package org.apache.ambari.server.controller.predicate;
public class PredicateVisitorTest {
    @org.junit.Test
    public void testVisitor() {
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.predicate.EqualsPredicate equalsPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, "bar");
        org.apache.ambari.server.controller.predicate.PredicateVisitorTest.TestPredicateVisitor visitor = new org.apache.ambari.server.controller.predicate.PredicateVisitorTest.TestPredicateVisitor();
        equalsPredicate.accept(visitor);
        junit.framework.Assert.assertSame(equalsPredicate, visitor.visitedComparisonPredicate);
        junit.framework.Assert.assertNull(visitor.visitedArrayPredicate);
        junit.framework.Assert.assertNull(visitor.visitedUnaryPredicate);
        org.apache.ambari.server.controller.predicate.AndPredicate andPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(equalsPredicate);
        visitor = new org.apache.ambari.server.controller.predicate.PredicateVisitorTest.TestPredicateVisitor();
        andPredicate.accept(visitor);
        junit.framework.Assert.assertNull(visitor.visitedComparisonPredicate);
        junit.framework.Assert.assertSame(andPredicate, visitor.visitedArrayPredicate);
        junit.framework.Assert.assertNull(visitor.visitedUnaryPredicate);
        org.apache.ambari.server.controller.predicate.NotPredicate notPredicate = new org.apache.ambari.server.controller.predicate.NotPredicate(andPredicate);
        visitor = new org.apache.ambari.server.controller.predicate.PredicateVisitorTest.TestPredicateVisitor();
        notPredicate.accept(visitor);
        junit.framework.Assert.assertNull(visitor.visitedComparisonPredicate);
        junit.framework.Assert.assertNull(visitor.visitedArrayPredicate);
        junit.framework.Assert.assertSame(notPredicate, visitor.visitedUnaryPredicate);
        org.apache.ambari.server.controller.predicate.CategoryPredicate categoryPredicate = new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate("cat1");
        visitor = new org.apache.ambari.server.controller.predicate.PredicateVisitorTest.TestPredicateVisitor();
        categoryPredicate.accept(visitor);
        junit.framework.Assert.assertNull(visitor.visitedComparisonPredicate);
        junit.framework.Assert.assertNull(visitor.visitedArrayPredicate);
        junit.framework.Assert.assertNull(visitor.visitedUnaryPredicate);
        junit.framework.Assert.assertSame(categoryPredicate, visitor.visitedCategoryPredicate);
    }

    public static class TestPredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
        org.apache.ambari.server.controller.predicate.ComparisonPredicate visitedComparisonPredicate = null;

        org.apache.ambari.server.controller.predicate.ArrayPredicate visitedArrayPredicate = null;

        org.apache.ambari.server.controller.predicate.UnaryPredicate visitedUnaryPredicate = null;

        org.apache.ambari.server.controller.predicate.AlwaysPredicate visitedAlwaysPredicate = null;

        org.apache.ambari.server.controller.predicate.CategoryPredicate visitedCategoryPredicate = null;

        @java.lang.Override
        public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
            visitedComparisonPredicate = predicate;
        }

        @java.lang.Override
        public void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate predicate) {
            visitedArrayPredicate = predicate;
        }

        @java.lang.Override
        public void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate) {
            visitedUnaryPredicate = predicate;
        }

        @java.lang.Override
        public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
            visitedAlwaysPredicate = predicate;
        }

        @java.lang.Override
        public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
            visitedCategoryPredicate = predicate;
        }
    }
}