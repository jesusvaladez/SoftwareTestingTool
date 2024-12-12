package org.apache.ambari.server.orm.entities;
public class LdapSyncEventEntityTest {
    @org.junit.Test
    public void testGetId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        org.junit.Assert.assertEquals(1L, event.getId());
    }

    @org.junit.Test
    public void testSetGetStatus() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.PENDING, event.getStatus());
        event.setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.RUNNING);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.RUNNING, event.getStatus());
        event.setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.COMPLETE);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.COMPLETE, event.getStatus());
        event.setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.ERROR);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.ERROR, event.getStatus());
        event.setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.PENDING);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.PENDING, event.getStatus());
    }

    @org.junit.Test
    public void testSetGetStatusDetail() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setStatusDetail("some detail");
        org.junit.Assert.assertEquals("some detail", event.getStatusDetail());
    }

    @org.junit.Test
    public void testSetGetSpecs() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        org.apache.ambari.server.orm.entities.LdapSyncSpecEntity spec = new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.GROUPS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, java.util.Collections.emptyList(), false);
        event.setSpecs(java.util.Collections.singletonList(spec));
        java.util.List<org.apache.ambari.server.orm.entities.LdapSyncSpecEntity> specs = event.getSpecs();
        org.junit.Assert.assertEquals(1, specs.size());
        org.junit.Assert.assertEquals(spec, specs.get(0));
    }

    @org.junit.Test
    public void testSetGetStartTime() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setStartTime(10001000L);
        org.junit.Assert.assertEquals(10001000L, event.getStartTime());
    }

    @org.junit.Test
    public void testSetGetEndTime() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setEndTime(90009000L);
        org.junit.Assert.assertEquals(90009000L, event.getEndTime());
    }

    @org.junit.Test
    public void testSetGetUsersCreated() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setUsersCreated(98);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(98), event.getUsersCreated());
    }

    @org.junit.Test
    public void testSetGetUsersUpdated() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setUsersUpdated(97);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(97), event.getUsersUpdated());
    }

    @org.junit.Test
    public void testSetGetUsersRemoved() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setUsersRemoved(96);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(96), event.getUsersRemoved());
    }

    @org.junit.Test
    public void testSetGetGroupsCreated() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setGroupsCreated(94);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(94), event.getGroupsCreated());
    }

    @org.junit.Test
    public void testSetGetGroupsUpdated() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setGroupsUpdated(93);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(93), event.getGroupsUpdated());
    }

    @org.junit.Test
    public void testSetGetGroupsRemoved() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setGroupsRemoved(92);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(92), event.getGroupsRemoved());
    }

    @org.junit.Test
    public void testSetGetMembershipsCreated() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setMembershipsCreated(90);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(90), event.getMembershipsCreated());
    }

    @org.junit.Test
    public void testSetGetMembershipsUpdated() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncEventEntity event = new org.apache.ambari.server.orm.entities.LdapSyncEventEntity(1L);
        event.setMembershipsRemoved(99);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(99), event.getMembershipsRemoved());
    }
}