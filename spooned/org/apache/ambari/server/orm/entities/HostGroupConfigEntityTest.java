package org.apache.ambari.server.orm.entities;
public class HostGroupConfigEntityTest {
    @org.junit.Test
    public void testSetGetHostGroupName() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntity entity = new org.apache.ambari.server.orm.entities.HostGroupConfigEntity();
        entity.setHostGroupName("foo");
        org.junit.Assert.assertEquals("foo", entity.getHostGroupName());
    }

    @org.junit.Test
    public void testSetGetType() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntity entity = new org.apache.ambari.server.orm.entities.HostGroupConfigEntity();
        entity.setType("testType");
        org.junit.Assert.assertEquals("testType", entity.getType());
    }

    @org.junit.Test
    public void testSetGetHostGroupEntity() {
        org.apache.ambari.server.orm.entities.HostGroupEntity group = new org.apache.ambari.server.orm.entities.HostGroupEntity();
        org.apache.ambari.server.orm.entities.HostGroupConfigEntity entity = new org.apache.ambari.server.orm.entities.HostGroupConfigEntity();
        entity.setHostGroupEntity(group);
        org.junit.Assert.assertSame(group, entity.getHostGroupEntity());
    }

    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntity entity = new org.apache.ambari.server.orm.entities.HostGroupConfigEntity();
        entity.setBlueprintName("foo");
        org.junit.Assert.assertEquals("foo", entity.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetConfigData() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntity entity = new org.apache.ambari.server.orm.entities.HostGroupConfigEntity();
        java.lang.String configData = "{ \"prop_name\" : \"value\" }";
        entity.setConfigData(configData);
        org.junit.Assert.assertEquals(configData, entity.getConfigData());
    }
}