package org.apache.ambari.server.orm.entities;
public class BlueprintConfigEntityTest {
    @org.junit.Test
    public void testSetGetType() {
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity entity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        entity.setType("foo");
        org.junit.Assert.assertEquals("foo", entity.getType());
    }

    @org.junit.Test
    public void testSetGetBlueprintEntity() {
        org.apache.ambari.server.orm.entities.BlueprintEntity bp = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity entity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        entity.setBlueprintEntity(bp);
        org.junit.Assert.assertSame(bp, entity.getBlueprintEntity());
    }

    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity entity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        entity.setBlueprintName("foo");
        org.junit.Assert.assertEquals("foo", entity.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetConfigData() {
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity entity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        entity.setConfigData("foo");
        org.junit.Assert.assertEquals("foo", entity.getConfigData());
    }
}