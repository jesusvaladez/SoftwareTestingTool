package org.apache.ambari.server.orm.entities;
public class ResourceTypeEntityTest {
    @org.junit.Test
    public void testSetGetId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(1);
        org.junit.Assert.assertEquals(1L, ((long) (resourceTypeEntity.getId())));
        resourceTypeEntity.setId(99);
        org.junit.Assert.assertEquals(99L, ((long) (resourceTypeEntity.getId())));
    }

    @org.junit.Test
    public void testSetGetName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setName("foo");
        org.junit.Assert.assertEquals("foo", resourceTypeEntity.getName());
        resourceTypeEntity.setName("bar");
        org.junit.Assert.assertEquals("bar", resourceTypeEntity.getName());
    }
}