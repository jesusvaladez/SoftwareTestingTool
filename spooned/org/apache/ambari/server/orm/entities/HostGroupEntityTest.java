package org.apache.ambari.server.orm.entities;
public class HostGroupEntityTest {
    @org.junit.Test
    public void testSetGetName() {
        org.apache.ambari.server.orm.entities.HostGroupEntity entity = new org.apache.ambari.server.orm.entities.HostGroupEntity();
        entity.setName("foo");
        org.junit.Assert.assertEquals("foo", entity.getName());
    }

    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.HostGroupEntity entity = new org.apache.ambari.server.orm.entities.HostGroupEntity();
        entity.setBlueprintName("foo");
        org.junit.Assert.assertEquals("foo", entity.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetCardinality() {
        org.apache.ambari.server.orm.entities.HostGroupEntity entity = new org.apache.ambari.server.orm.entities.HostGroupEntity();
        entity.setCardinality("foo");
        org.junit.Assert.assertEquals("foo", entity.getCardinality());
    }

    @org.junit.Test
    public void testSetGetBlueprintEntity() {
        org.apache.ambari.server.orm.entities.HostGroupEntity entity = new org.apache.ambari.server.orm.entities.HostGroupEntity();
        org.apache.ambari.server.orm.entities.BlueprintEntity bp = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        entity.setBlueprintEntity(bp);
        org.junit.Assert.assertSame(bp, entity.getBlueprintEntity());
    }

    @org.junit.Test
    public void testSetGetComponents() {
        org.apache.ambari.server.orm.entities.HostGroupEntity entity = new org.apache.ambari.server.orm.entities.HostGroupEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupComponentEntity> components = java.util.Collections.emptyList();
        entity.setComponents(components);
        org.junit.Assert.assertSame(components, entity.getComponents());
    }
}