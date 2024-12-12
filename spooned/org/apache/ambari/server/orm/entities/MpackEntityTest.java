package org.apache.ambari.server.orm.entities;
public class MpackEntityTest {
    @org.junit.Test
    public void testHashCodeAndEquals() {
        org.apache.ambari.server.orm.entities.MpackEntity entity1 = new org.apache.ambari.server.orm.entities.MpackEntity();
        org.apache.ambari.server.orm.entities.MpackEntity entity2 = new org.apache.ambari.server.orm.entities.MpackEntity();
        org.junit.Assert.assertEquals(entity1.hashCode(), entity2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(entity1, entity2));
        entity1.setId(new java.lang.Long(1));
        entity2.setId(new java.lang.Long(2));
        org.junit.Assert.assertNotSame(entity1.hashCode(), entity2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(entity1, entity2));
        entity2.setId(new java.lang.Long(1));
        org.junit.Assert.assertEquals(entity1.hashCode(), entity2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(entity1, entity2));
        entity1.setMpackName("testMpack1");
        entity2.setMpackName("testMpack2");
        org.junit.Assert.assertNotSame(entity1.hashCode(), entity2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(entity1, entity2));
        entity2.setMpackName("testMpack1");
        org.junit.Assert.assertEquals(entity1.hashCode(), entity2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(entity1, entity2));
        entity1.setMpackVersion("3.0");
        entity2.setMpackVersion("3.1");
        org.junit.Assert.assertNotSame(entity1.hashCode(), entity2.hashCode());
        org.junit.Assert.assertFalse(java.util.Objects.equals(entity1, entity2));
        entity2.setMpackVersion("3.0");
        org.junit.Assert.assertEquals(entity1.hashCode(), entity2.hashCode());
        org.junit.Assert.assertTrue(java.util.Objects.equals(entity1, entity2));
    }
}