package org.apache.ambari.server.serveraction.kerberos.stageutils;
public class ResolvedKerberosKeytab {
    private java.lang.String ownerName = null;

    private java.lang.String ownerAccess = null;

    private java.lang.String groupName = null;

    private java.lang.String groupAccess = null;

    private java.lang.String file = null;

    private java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> principals = new java.util.HashSet<>();

    private boolean isAmbariServerKeytab = false;

    private boolean mustWriteAmbariJaasFile = false;

    public ResolvedKerberosKeytab(java.lang.String file, java.lang.String ownerName, java.lang.String ownerAccess, java.lang.String groupName, java.lang.String groupAccess, java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> principals, boolean isAmbariServerKeytab, boolean writeAmbariJaasFile) {
        this.ownerName = ownerName;
        this.ownerAccess = ownerAccess;
        this.groupName = groupName;
        this.groupAccess = groupAccess;
        this.file = file;
        setPrincipals(principals);
        this.isAmbariServerKeytab = isAmbariServerKeytab;
        this.mustWriteAmbariJaasFile = writeAmbariJaasFile;
    }

    public java.lang.String getFile() {
        return file;
    }

    public void setFile(java.lang.String file) {
        this.file = file;
    }

    public java.lang.String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(java.lang.String name) {
        this.ownerName = name;
    }

    public java.lang.String getOwnerAccess() {
        return ownerAccess;
    }

    public void setOwnerAccess(java.lang.String access) {
        this.ownerAccess = access;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String name) {
        this.groupName = name;
    }

    public java.lang.String getGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(java.lang.String access) {
        this.groupAccess = access;
    }

    public java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> getPrincipals() {
        return principals;
    }

    public void setPrincipals(java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> principals) {
        this.principals = principals;
        if (principals != null) {
            for (org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal principal : this.principals) {
                principal.setResolvedKerberosKeytab(this);
            }
        }
    }

    public void addPrincipal(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal principal) {
        if (!principals.contains(principal)) {
            principal.setResolvedKerberosKeytab(this);
            principals.add(principal);
        }
    }

    public boolean isAmbariServerKeytab() {
        return isAmbariServerKeytab;
    }

    public void setAmbariServerKeytab(boolean isAmbariServerKeytab) {
        this.isAmbariServerKeytab = isAmbariServerKeytab;
    }

    public boolean isMustWriteAmbariJaasFile() {
        return mustWriteAmbariJaasFile;
    }

    public void setMustWriteAmbariJaasFile(boolean mustWriteAmbariJaasFile) {
        this.mustWriteAmbariJaasFile = mustWriteAmbariJaasFile;
    }

    public void mergePrincipals(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab otherKeytab) {
        for (org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal rkp : otherKeytab.getPrincipals()) {
            org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal existent = findPrincipal(rkp.getHostId(), rkp.getPrincipal(), rkp.getKeytabPath());
            if (existent != null) {
                existent.mergeComponentMapping(rkp);
            } else {
                principals.add(rkp);
            }
        }
    }

    private org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal findPrincipal(java.lang.Long hostId, java.lang.String principal, java.lang.String keytabPath) {
        for (org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal rkp : principals) {
            boolean hostIdIsSame;
            if ((hostId != null) && (rkp.getHostId() != null)) {
                hostIdIsSame = hostId.equals(rkp.getHostId());
            } else if ((hostId == null) && (rkp.getHostId() == null)) {
                hostIdIsSame = true;
            } else {
                hostIdIsSame = false;
            }
            if ((hostIdIsSame && principal.equals(rkp.getPrincipal())) && keytabPath.equals(rkp.getKeytabPath())) {
                return rkp;
            }
        }
        return null;
    }
}