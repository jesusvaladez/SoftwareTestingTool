package org.apache.ambari.server.security.ldap;
public class LdapUserDto {
    private java.lang.String userName;

    private boolean synced;

    private java.lang.String uid;

    private java.lang.String dn;

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public java.lang.String getUid() {
        return uid;
    }

    public void setUid(java.lang.String uid) {
        this.uid = uid;
    }

    public java.lang.String getDn() {
        return dn;
    }

    public void setDn(java.lang.String dn) {
        this.dn = dn;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (userName != null) ? userName.hashCode() : 0;
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.security.ldap.LdapUserDto that = ((org.apache.ambari.server.security.ldap.LdapUserDto) (o));
        if (userName != null ? !userName.equals(that.getUserName()) : that.getUserName() != null)
            return false;

        return true;
    }
}