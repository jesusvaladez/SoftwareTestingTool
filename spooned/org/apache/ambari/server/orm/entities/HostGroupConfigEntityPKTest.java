package org.apache.ambari.server.orm.entities;
public class HostGroupConfigEntityPKTest {
    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK pk = new org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK();
        pk.setBlueprintName("foo");
        org.junit.Assert.assertEquals("foo", pk.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetHostGroupName() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK pk = new org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK();
        pk.setHostGroupName("foo");
        org.junit.Assert.assertEquals("foo", pk.getHostGroupName());
    }

    @org.junit.Test
    public void testSetGetType() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK pk = new org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK();
        pk.setType("testType");
        org.junit.Assert.assertEquals("testType", pk.getType());
    }

    @org.junit.Test
    public void testHashcode() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK pk1 = new org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK();
        org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK pk2 = new org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK();
        pk1.setType("foo");
        pk2.setType("foo");
        pk1.setBlueprintName("bp");
        pk2.setBlueprintName("bp");
        pk1.setHostGroupName("hg");
        pk2.setHostGroupName("hg");
        org.junit.Assert.assertEquals(pk1.hashCode(), pk2.hashCode());
    }

    @org.junit.Test
    public void testEquals() {
        org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK pk1 = new org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK();
        org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK pk2 = new org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK();
        pk1.setType("foo");
        pk2.setType("foo");
        pk1.setBlueprintName("bp");
        pk2.setBlueprintName("bp");
        pk1.setHostGroupName("hg");
        pk2.setHostGroupName("hg");
        org.junit.Assert.assertEquals(pk1, pk2);
        pk1.setType("something_else");
        org.junit.Assert.assertFalse(pk1.equals(pk2));
        pk2.setType("something_else");
        org.junit.Assert.assertEquals(pk1, pk2);
        pk1.setType("other_type");
        org.junit.Assert.assertFalse(pk1.equals(pk2));
        pk2.setType("other_type");
        org.junit.Assert.assertEquals(pk1, pk2);
        pk1.setHostGroupName("hg2");
        org.junit.Assert.assertFalse(pk1.equals(pk2));
    }
}