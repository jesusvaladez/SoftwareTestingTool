package org.apache.ambari.server.orm.entities;
public class BlueprintConfigEntityPKTest {
    @org.junit.Test
    public void testSetGetBlueprintName() {
        org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK pk = new org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK();
        pk.setBlueprintName("foo");
        org.junit.Assert.assertEquals("foo", pk.getBlueprintName());
    }

    @org.junit.Test
    public void testSetGetType() {
        org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK pk = new org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK();
        pk.setType("foo");
        org.junit.Assert.assertEquals("foo", pk.getType());
    }

    @org.junit.Test
    public void testEquals() {
        org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK pk = new org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK();
        org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK pk2 = new org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK();
        pk.setBlueprintName("foo");
        pk.setType("core-site");
        pk2.setBlueprintName("foo");
        pk2.setType("core-site");
        org.junit.Assert.assertEquals(pk, pk2);
        org.junit.Assert.assertEquals(pk2, pk);
        pk.setBlueprintName("foo2");
        org.junit.Assert.assertFalse(pk.equals(pk2));
        org.junit.Assert.assertFalse(pk2.equals(pk));
        pk2.setBlueprintName("foo2");
        org.junit.Assert.assertEquals(pk, pk2);
        org.junit.Assert.assertEquals(pk2, pk);
        pk.setType("other-type");
        org.junit.Assert.assertFalse(pk.equals(pk2));
        org.junit.Assert.assertFalse(pk2.equals(pk));
    }
}