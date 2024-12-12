package org.apache.ambari.server.controller.predicate;
public class GreaterPredicateTest {
    @org.junit.Test
    public void testApply() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.GreaterPredicate<>(propertyId, 10);
        resource.setProperty(propertyId, 1);
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        resource.setProperty(propertyId, 100);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        resource.setProperty(propertyId, 10);
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
    }

    @org.junit.Test
    public void testNullValue() {
        try {
            new org.apache.ambari.server.controller.predicate.GreaterPredicate<java.lang.Integer>("category/foo", null);
            junit.framework.Assert.fail("Expected IllegalArgumentException for null value.");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    @org.junit.Test
    public void testGetProperties() {
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.predicate.GreaterPredicate predicate = new org.apache.ambari.server.controller.predicate.GreaterPredicate<>(propertyId, 10);
        java.util.Set<java.lang.String> ids = predicate.getPropertyIds();
        junit.framework.Assert.assertEquals(1, ids.size());
        junit.framework.Assert.assertTrue(ids.contains(propertyId));
    }

    @org.junit.Test
    public void testApplyWithSmallFloats() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.GreaterPredicate<>(propertyId, 0.1);
        resource.setProperty(propertyId, 1.3);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        resource.setProperty(propertyId, 0.06);
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        resource.setProperty(propertyId, 100.3);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
    }
}