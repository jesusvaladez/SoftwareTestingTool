package org.apache.ambari.server.orm.entities;
public class LdapSyncSpecEntity {
    private org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType principalType;

    private org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType syncType;

    private java.util.List<java.lang.String> principalNames;

    private boolean postProcessExistingUsers;

    public LdapSyncSpecEntity(org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType principalType, org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType syncType, java.util.List<java.lang.String> principalNames, boolean postProcessExistingUsers) {
        this.principalType = principalType;
        this.syncType = syncType;
        this.principalNames = principalNames;
        this.postProcessExistingUsers = postProcessExistingUsers;
        assert principalNames != null;
        if (syncType == org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.SPECIFIC) {
            if (principalNames.isEmpty()) {
                throw new java.lang.IllegalArgumentException(("Missing principal names for " + syncType) + " sync-type.");
            }
        } else if (!principalNames.isEmpty()) {
            throw new java.lang.IllegalArgumentException(("Principal names should not be specified for " + syncType) + " sync-type.");
        }
    }

    public org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType getPrincipalType() {
        return principalType;
    }

    public org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType getSyncType() {
        return syncType;
    }

    public java.util.List<java.lang.String> getPrincipalNames() {
        return principalNames;
    }

    public boolean getPostProcessExistingUsers() {
        return postProcessExistingUsers;
    }

    public enum PrincipalType {

        USERS,
        GROUPS;
        public static org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType valueOfIgnoreCase(java.lang.String type) {
            return org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.PrincipalType.valueOf(type.toUpperCase());
        }
    }

    public enum SyncType {

        ALL,
        EXISTING,
        SPECIFIC;
        public static org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType valueOfIgnoreCase(java.lang.String type) {
            return org.apache.ambari.server.orm.entities.LdapSyncSpecEntity.SyncType.valueOf(type.toUpperCase());
        }
    }
}