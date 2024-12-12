package org.apache.ambari.server.orm.entities;
public class PrincipalTypeEntityTest {
    @org.junit.Test
    public void testSetGetId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
        principalTypeEntity.setId(1);
        org.junit.Assert.assertEquals(1L, ((long) (principalTypeEntity.getId())));
        principalTypeEntity.setId(99);
        org.junit.Assert.assertEquals(99L, ((long) (principalTypeEntity.getId())));
    }

    @org.junit.Test
    public void testSetGetName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
        principalTypeEntity.setName("foo");
        org.junit.Assert.assertEquals("foo", principalTypeEntity.getName());
        principalTypeEntity.setName("bar");
        org.junit.Assert.assertEquals("bar", principalTypeEntity.getName());
    }
}