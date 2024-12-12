package org.apache.ambari.server.controller.predicate;
public class EqualsPredicateTest {
    @org.junit.Test
    public void testApply() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, "bar");
        resource.setProperty(propertyId, "monkey");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        resource.setProperty(propertyId, "bar");
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "fun");
        predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, "bar");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
    }

    @org.junit.Test
    public void testApplyNullValue() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<java.lang.String>(propertyId, null);
        resource.setProperty(propertyId, "monkey");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        resource.setProperty(propertyId, null);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
    }

    @org.junit.Test
    public void testGetProperties() {
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, "bar");
        java.util.Set<java.lang.String> ids = predicate.getPropertyIds();
        junit.framework.Assert.assertEquals(1, ids.size());
        junit.framework.Assert.assertTrue(ids.contains(propertyId));
    }

    @org.junit.Test
    public void testApplyNumberValues() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("propertyId1", "1");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, "1");
        resource.setProperty(propertyId, "1");
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        resource.setProperty(propertyId, "2");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        resource.setProperty(propertyId, "5");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
    }
}