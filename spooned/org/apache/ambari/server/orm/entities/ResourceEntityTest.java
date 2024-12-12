package org.apache.ambari.server.orm.entities;
public class ResourceEntityTest {
    @org.junit.Test
    public void testSetGetId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceEntity entity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        entity.setId(1L);
        org.junit.Assert.assertEquals(1L, ((long) (entity.getId())));
        entity.setId(99L);
        org.junit.Assert.assertEquals(99L, ((long) (entity.getId())));
    }

    @org.junit.Test
    public void testSetGetResourceType() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceEntity entity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity typeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        entity.setResourceType(typeEntity);
        org.junit.Assert.assertEquals(typeEntity, entity.getResourceType());
    }

    @org.junit.Test
    public void testSetGetPrivileges() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceEntity entity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = java.util.Collections.singleton(privilegeEntity);
        org.junit.Assert.assertNull(entity.getResourceType());
        entity.setPrivileges(privileges);
        org.junit.Assert.assertEquals(privileges, entity.getPrivileges());
    }
}