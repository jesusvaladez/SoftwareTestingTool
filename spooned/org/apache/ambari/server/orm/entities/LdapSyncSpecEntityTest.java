package org.apache.ambari.server.orm.entities;
public class LdapSyncSpecEntityTest {
    @org.junit.Test
    public void testGetPrincipalType() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncSpecEntity entity = new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, java.util.Collections.emptyList(), false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, entity.getPrincipalType());
        entity = new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.GROUPS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, java.util.Collections.emptyList(), false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.GROUPS, entity.getPrincipalType());
    }

    @org.junit.Test
    public void testGetSyncType() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.LdapSyncSpecEntity entity = new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, java.util.Collections.emptyList(), false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, entity.getSyncType());
        entity = new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, java.util.Collections.emptyList(), false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, entity.getSyncType());
    }

    @org.junit.Test
    public void testGetPrincipalNames() throws java.lang.Exception {
        java.util.List<java.lang.String> names = new java.util.LinkedList<>();
        names.add("joe");
        names.add("fred");
        org.apache.ambari.server.orm.entities.LdapSyncSpecEntity entity = new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, names, false);
        org.junit.Assert.assertEquals(names, entity.getPrincipalNames());
    }

    @org.junit.Test
    public void testIllegalConstruction() throws java.lang.Exception {
        try {
            new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, java.util.Collections.emptyList(), false);
            org.junit.Assert.fail("expected IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException e) {
        }
        java.util.List<java.lang.String> names = new java.util.LinkedList<>();
        names.add("joe");
        names.add("fred");
        try {
            new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, names, false);
            org.junit.Assert.fail("expected IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            new org.apache.ambari.server.orm.entities.LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.USERS, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, names, false);
            org.junit.Assert.fail("expected IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }
}