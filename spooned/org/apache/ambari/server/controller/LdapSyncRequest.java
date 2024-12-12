package org.apache.ambari.server.controller;
public class LdapSyncRequest {
    private final java.util.Set<java.lang.String> principalNames;

    private final org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType type;

    private final boolean postProcessExistingUsers;

    public LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType type, java.util.Set<java.lang.String> principalNames, boolean postProcessExistingUsers) {
        this.type = type;
        this.principalNames = (principalNames == null) ? new java.util.HashSet<>() : principalNames;
        this.postProcessExistingUsers = postProcessExistingUsers;
    }

    public LdapSyncRequest(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType type, boolean postProcessExistingUsers) {
        this(type, null, postProcessExistingUsers);
    }

    public void addPrincipalNames(java.util.Set<java.lang.String> principalNames) {
        this.principalNames.addAll(principalNames);
    }

    public java.util.Set<java.lang.String> getPrincipalNames() {
        return principalNames;
    }

    public org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType getType() {
        return type;
    }

    public boolean getPostProcessExistingUsers() {
        return postProcessExistingUsers;
    }
}