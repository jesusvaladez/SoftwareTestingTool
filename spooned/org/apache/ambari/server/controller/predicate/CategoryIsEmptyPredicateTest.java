package org.apache.ambari.server.controller.predicate;
public class CategoryIsEmptyPredicateTest {
    @org.junit.Test
    public void testApply() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String categoryId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate(categoryId);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        resource.addCategory(categoryId);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "bar");
        resource.setProperty(propertyId, "value1");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
    }

    @org.junit.Test
    public void testApplyWithMap() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "mapProperty");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate(propertyId);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        java.util.Map<java.lang.String, java.lang.String> mapProperty = new java.util.HashMap<>();
        resource.setProperty(propertyId, mapProperty);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        mapProperty.put("foo", "bar");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
    }
}