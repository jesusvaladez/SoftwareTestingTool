package org.apache.ambari.server.orm.entities;
public class PrincipalEntityTest {
    @org.junit.Test
    public void testSetGetId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.PrincipalEntity entity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        entity.setId(1L);
        org.junit.Assert.assertEquals(1L, ((long) (entity.getId())));
        entity.setId(99L);
        org.junit.Assert.assertEquals(99L, ((long) (entity.getId())));
    }

    @org.junit.Test
    public void testSetGetPrincipalType() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.PrincipalEntity entity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity typeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
        entity.setPrincipalType(typeEntity);
        org.junit.Assert.assertEquals(typeEntity, entity.getPrincipalType());
    }

    @org.junit.Test
    public void testSetGetPrivileges() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = new java.util.HashSet<>();
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegeEntity.setId(1);
        privileges.add(privilegeEntity);
        privilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegeEntity.setId(2);
        privileges.add(privilegeEntity);
        org.apache.ambari.server.orm.entities.PrincipalEntity entity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        entity.setPrivileges(privileges);
        org.junit.Assert.assertEquals(privileges, entity.getPrivileges());
    }

    @org.junit.Test
    public void testRemovePrivilege() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = new java.util.HashSet<>();
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity1 = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegeEntity1.setId(1);
        privileges.add(privilegeEntity1);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity2 = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegeEntity2.setId(2);
        privileges.add(privilegeEntity2);
        org.apache.ambari.server.orm.entities.PrincipalEntity entity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        entity.setPrivileges(privileges);
        entity.removePrivilege(privilegeEntity2);
        privileges = entity.getPrivileges();
        org.junit.Assert.assertEquals(1, privileges.size());
        privileges.contains(privilegeEntity1);
    }
}