package org.apache.ambari.server.controller.internal;
public class ResourceImplTest {
    @org.junit.Test
    public void testGetType() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, resource.getType());
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Service, resource.getType());
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, resource.getType());
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Component, resource.getType());
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, resource.getType());
    }

    @org.junit.Test
    public void testSetGetProperty() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p1");
        resource.setProperty(propertyId, "foo");
        junit.framework.Assert.assertEquals("foo", resource.getPropertyValue(propertyId));
        resource.setProperty(propertyId, 1);
        junit.framework.Assert.assertEquals(1, resource.getPropertyValue(propertyId));
        resource.setProperty(propertyId, ((float) (1.99)));
        junit.framework.Assert.assertEquals(((float) (1.99)), resource.getPropertyValue(propertyId));
        resource.setProperty(propertyId, 1.99);
        junit.framework.Assert.assertEquals(1.99, resource.getPropertyValue(propertyId));
        resource.setProperty(propertyId, 65L);
        junit.framework.Assert.assertEquals(65L, resource.getPropertyValue(propertyId));
    }

    @org.junit.Test
    public void testAddCategory() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.addCategory("c1");
        resource.addCategory("c2/sub2");
        resource.addCategory("c3/sub3/sub3a");
        junit.framework.Assert.assertTrue(resource.getPropertiesMap().containsKey("c1"));
        junit.framework.Assert.assertTrue(resource.getPropertiesMap().containsKey("c2/sub2"));
        junit.framework.Assert.assertTrue(resource.getPropertiesMap().containsKey("c3/sub3/sub3a"));
    }

    @org.junit.Test
    public void testCopyConstructor() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "p1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1/c2", "p3");
        java.lang.String p4 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1/c2/c3", "p4");
        java.lang.String p5 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p5");
        resource.setProperty(p1, "foo");
        junit.framework.Assert.assertEquals("foo", resource.getPropertyValue(p1));
        resource.setProperty(p2, 1);
        junit.framework.Assert.assertEquals(1, resource.getPropertyValue(p2));
        resource.setProperty(p3, ((float) (1.99)));
        junit.framework.Assert.assertEquals(((float) (1.99)), resource.getPropertyValue(p3));
        resource.setProperty(p4, 1.99);
        junit.framework.Assert.assertEquals(1.99, resource.getPropertyValue(p4));
        resource.setProperty(p5, 65L);
        junit.framework.Assert.assertEquals(65L, resource.getPropertyValue(p5));
        org.apache.ambari.server.controller.spi.Resource copy = new org.apache.ambari.server.controller.internal.ResourceImpl(resource);
        junit.framework.Assert.assertEquals("foo", copy.getPropertyValue(p1));
        junit.framework.Assert.assertEquals(1, copy.getPropertyValue(p2));
        junit.framework.Assert.assertEquals(((float) (1.99)), copy.getPropertyValue(p3));
        junit.framework.Assert.assertEquals(1.99, copy.getPropertyValue(p4));
        junit.framework.Assert.assertEquals(65L, copy.getPropertyValue(p5));
    }

    @org.junit.Test
    public void testGetPropertiesMap() {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        java.lang.String p1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "p1");
        java.lang.String p2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p2");
        java.lang.String p3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1/c2", "p3");
        java.lang.String p4 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1/c2/c3", "p4");
        java.lang.String p5 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("c1", "p5");
        resource.setProperty(p1, "foo");
        resource.setProperty(p2, 1);
        resource.setProperty(p3, ((float) (1.99)));
        resource.setProperty(p4, 1.99);
        resource.setProperty(p5, 65L);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> map = resource.getPropertiesMap();
        junit.framework.Assert.assertEquals(4, map.keySet().size());
        junit.framework.Assert.assertTrue(map.containsKey(""));
        junit.framework.Assert.assertTrue(map.containsKey("c1"));
        junit.framework.Assert.assertTrue(map.containsKey("c1/c2"));
        junit.framework.Assert.assertTrue(map.containsKey("c1/c2/c3"));
        java.lang.String lastCategory = null;
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> entry : map.entrySet()) {
            java.lang.String category = entry.getKey();
            if (lastCategory != null) {
                junit.framework.Assert.assertTrue(category.compareTo(lastCategory) > 0);
            }
            lastCategory = category;
            java.lang.String lastProperty = null;
            for (java.lang.String property : entry.getValue().keySet()) {
                if (lastProperty != null) {
                    junit.framework.Assert.assertTrue(property.compareTo(lastProperty) > 0);
                }
                lastProperty = property;
            }
        }
    }

    @org.junit.Test
    public void testEquals() {
        org.apache.ambari.server.controller.spi.Resource resource1 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.Resource resource2 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.spi.Resource resource3 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        junit.framework.Assert.assertTrue(resource1.equals(resource2));
        junit.framework.Assert.assertTrue(resource2.equals(resource1));
        junit.framework.Assert.assertFalse(resource1.equals(resource3));
        junit.framework.Assert.assertFalse(resource3.equals(resource1));
        junit.framework.Assert.assertFalse(resource2.equals(resource3));
        junit.framework.Assert.assertFalse(resource3.equals(resource2));
        resource1.setProperty("p1", "foo");
        resource2.setProperty("p1", "bar");
        junit.framework.Assert.assertFalse(resource1.equals(resource2));
        junit.framework.Assert.assertFalse(resource2.equals(resource1));
        resource2.setProperty("p1", "foo");
        junit.framework.Assert.assertTrue(resource1.equals(resource2));
        junit.framework.Assert.assertTrue(resource2.equals(resource1));
    }
}