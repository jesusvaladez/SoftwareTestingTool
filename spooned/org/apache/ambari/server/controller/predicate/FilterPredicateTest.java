package org.apache.ambari.server.controller.predicate;
public class FilterPredicateTest {
    private static final java.lang.String IP_ADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + (("([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.") + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    @org.junit.Test
    public void testApply() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "ip");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.FilterPredicate(propertyId, org.apache.ambari.server.controller.predicate.FilterPredicateTest.IP_ADDRESS_PATTERN);
        resource.setProperty(propertyId, "monkey");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        resource.setProperty(propertyId, "10.0.0.1");
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        resource.setProperty(propertyId, "127.0.0.1");
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        resource.setProperty(propertyId, "0.0.0.0");
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
        propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "fun");
        predicate = new org.apache.ambari.server.controller.predicate.FilterPredicate(propertyId, org.apache.ambari.server.controller.predicate.FilterPredicateTest.IP_ADDRESS_PATTERN);
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
    }

    @org.junit.Test
    public void testApplyNullValue() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.FilterPredicate(propertyId, null);
        resource.setProperty(propertyId, "monkey");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        resource.setProperty(propertyId, null);
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
    }

    @org.junit.Test
    public void testApplyEmptyValue() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.FilterPredicate(propertyId, "");
        resource.setProperty(propertyId, "monkey");
        junit.framework.Assert.assertFalse(predicate.evaluate(resource));
        predicate = new org.apache.ambari.server.controller.predicate.FilterPredicate(propertyId, "monkey");
        junit.framework.Assert.assertTrue(predicate.evaluate(resource));
    }

    @org.junit.Test
    public void testGetProperties() {
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category1", "foo");
        org.apache.ambari.server.controller.predicate.FilterPredicate predicate = new org.apache.ambari.server.controller.predicate.FilterPredicate(propertyId, "bar");
        java.util.Set<java.lang.String> ids = predicate.getPropertyIds();
        junit.framework.Assert.assertEquals(1, ids.size());
        junit.framework.Assert.assertTrue(ids.contains(propertyId));
    }
}