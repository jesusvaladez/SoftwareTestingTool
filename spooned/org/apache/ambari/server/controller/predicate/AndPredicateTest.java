package org.apache.ambari.server.controller.predicate;
public class AndPredicateTest {
    @org.junit.Test
    public void testApply() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "property1");
        java.lang.String propertyId2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "property2");
        java.lang.String propertyId3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "property3");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate1 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId1, "v1");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate2 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId2, "v2");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate3 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId3, "v3");
        org.apache.ambari.server.controller.predicate.AndPredicate andPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2, predicate3);
        resource.setProperty(propertyId1, "v1");
        resource.setProperty(propertyId2, "monkey");
        resource.setProperty(propertyId3, "v3");
        junit.framework.Assert.assertFalse(andPredicate.evaluate(resource));
        resource.setProperty(propertyId2, "v2");
        junit.framework.Assert.assertTrue(andPredicate.evaluate(resource));
    }

    @org.junit.Test
    public void testGetProperties() {
        java.lang.String propertyId1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "property1");
        java.lang.String propertyId2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "property2");
        java.lang.String propertyId3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "property3");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate1 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId1, "v1");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate2 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId2, "v2");
        org.apache.ambari.server.controller.predicate.EqualsPredicate predicate3 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId3, "v3");
        org.apache.ambari.server.controller.predicate.AndPredicate andPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2, predicate3);
        java.util.Set<java.lang.String> ids = andPredicate.getPropertyIds();
        junit.framework.Assert.assertEquals(3, ids.size());
        junit.framework.Assert.assertTrue(ids.contains(propertyId1));
        junit.framework.Assert.assertTrue(ids.contains(propertyId2));
        junit.framework.Assert.assertTrue(ids.contains(propertyId3));
    }
}