package org.apache.ambari.server.orm.entities;
public class HostGroupComponentEntityTest {
    @org.junit.Test
    public void testSetGetName() {
        org.apache.ambari.server.orm.entities.HostGroupComponentEntity entity = new org.apache.ambari.server.orm.entities.HostGroupComponentEntity();
        entity.setName("foo");
        org.junit.Assert.assertEquals("foo", entity.getName());
    }

    @org.junit.Test
    public void testSetGetHostGroupEntity() {
        org.apache.ambari.server.orm.entities.HostGroupComponentEntity entity = new org.apache.ambari.server.orm.entities.HostGroupComponentEntity();
        org.apache.ambari.server.orm.entities.HostGroupEntity hg = new org.apache.ambari.server.orm.entities.HostGroupEntity();
        entity.setHostGroupEntity(hg);
        org.junit.Assert.assertSame(hg, entity.getHostGroupEntity());
    }

    @org.junit.Test
    public void testSetGetHostGroupName() {
        org.apache.ambari.server.orm.entities.HostGroupComponentEntity entity = new org.apache.ambari.server.orm.entities.HostGroupComponentEntity();
        entity.setHostGroupName("foo");
        org.junit.Assert.assertEquals("foo", entity.getHostGroupName());
    }

    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.HostGroupComponentEntity entity = new org.apache.ambari.server.orm.entities.HostGroupComponentEntity();
        entity.setBlueprintName("foo");
        org.junit.Assert.assertEquals("foo", entity.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetProvisionAction() {
        org.apache.ambari.server.orm.entities.HostGroupComponentEntity entity = new org.apache.ambari.server.orm.entities.HostGroupComponentEntity();
        entity.setProvisionAction("INSTALL_ONLY");
        org.junit.Assert.assertEquals("INSTALL_ONLY", entity.getProvisionAction());
    }
}