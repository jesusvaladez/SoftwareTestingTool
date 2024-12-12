package org.apache.ambari.server.controller;
public class LdapSyncRequestTest {
    @org.junit.Test
    public void testAddPrincipalNames() throws java.lang.Exception {
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        names.add("name1");
        org.apache.ambari.server.controller.LdapSyncRequest request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, names, false);
        names = new java.util.HashSet<>();
        names.add("name2");
        names.add("name3");
        request.addPrincipalNames(names);
        java.util.Set<java.lang.String> principalNames = request.getPrincipalNames();
        org.junit.Assert.assertEquals(3, principalNames.size());
        org.junit.Assert.assertTrue(principalNames.contains("name1"));
        org.junit.Assert.assertTrue(principalNames.contains("name2"));
        org.junit.Assert.assertTrue(principalNames.contains("name3"));
    }

    @org.junit.Test
    public void testGetPrincipalNames() throws java.lang.Exception {
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        names.add("name1");
        names.add("name2");
        names.add("name3");
        org.apache.ambari.server.controller.LdapSyncRequest request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, names, false);
        java.util.Set<java.lang.String> principalNames = request.getPrincipalNames();
        org.junit.Assert.assertEquals(3, principalNames.size());
        org.junit.Assert.assertTrue(principalNames.contains("name1"));
        org.junit.Assert.assertTrue(principalNames.contains("name2"));
        org.junit.Assert.assertTrue(principalNames.contains("name3"));
    }

    @org.junit.Test
    public void testGetType() throws java.lang.Exception {
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        org.apache.ambari.server.controller.LdapSyncRequest request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, names, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, request.getType());
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, request.getType());
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, request.getType());
    }

    @org.junit.Test
    public void testGetPostProcessExistingUsers() throws java.lang.Exception {
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        org.apache.ambari.server.controller.LdapSyncRequest request;
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, names, false);
        org.junit.Assert.assertFalse(request.getPostProcessExistingUsers());
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC, names, true);
        org.junit.Assert.assertTrue(request.getPostProcessExistingUsers());
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, false);
        org.junit.Assert.assertFalse(request.getPostProcessExistingUsers());
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.ALL, true);
        org.junit.Assert.assertTrue(request.getPostProcessExistingUsers());
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, false);
        org.junit.Assert.assertFalse(request.getPostProcessExistingUsers());
        request = new org.apache.ambari.server.controller.LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.EXISTING, true);
        org.junit.Assert.assertTrue(request.getPostProcessExistingUsers());
    }
}