package org.apache.ambari.server.controller.predicate;
public class NotPredicateTest {
    @org.junit.Test
    public void testApply() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, "bar");
        org.apache.ambari.server.controller.predicate.NotPredicate notPredicate = new org.apache.ambari.server.controller.predicate.NotPredicate(predicate);
        resource.setProperty(propertyId, "monkey");
        junit.framework.Assert.assertTrue(notPredicate.evaluate(resource));
        resource.setProperty(propertyId, "bar");
        junit.framework.Assert.assertFalse(notPredicate.evaluate(resource));
    }

    @org.junit.Test
    public void testGetProperties() {
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, "bar");
        java.util.Set<java.lang.String> ids = predicate.getPropertyIds();
        junit.framework.Assert.assertEquals(1, ids.size());
        junit.framework.Assert.assertTrue(ids.contains(propertyId));
    }
}