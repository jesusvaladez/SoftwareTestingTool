package org.apache.ambari.server.orm.entities;
public class HostRoleCommandEntityTest {
    @org.junit.Test
    public void testSetCustomCommandName() {
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        entity.setCustomCommandName("foo");
        org.junit.Assert.assertEquals("foo", entity.getCustomCommandName());
    }

    @org.junit.Test
    public void testSetCommandDetail() {
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        entity.setCommandDetail("foo");
        org.junit.Assert.assertEquals("foo", entity.getCommandDetail());
    }

    @org.junit.Test
    public void testSetOpsDisplayName() {
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        entity.setOpsDisplayName("foo");
        org.junit.Assert.assertEquals("foo", entity.getOpsDisplayName());
    }
}