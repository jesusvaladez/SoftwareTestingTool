package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
@javax.persistence.Entity
@javax.persistence.Table(name = "kerberos_keytab")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "KerberosKeytabEntity.findAll", query = "SELECT kk FROM KerberosKeytabEntity kk"), @javax.persistence.NamedQuery(name = "KerberosKeytabEntity.findByPrincipalAndHost", query = "SELECT kk FROM KerberosKeytabEntity kk, KerberosKeytabPrincipalEntity kkp WHERE kkp.hostId=:hostId AND kkp.principalName=:principalName AND kkp.keytabPath = kk.keytabPath", hints = { @javax.persistence.QueryHint(name = "eclipselink.query-results-cache", value = "true"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.size", value = "500") }), @javax.persistence.NamedQuery(name = "KerberosKeytabEntity.findByPrincipalAndNullHost", query = "SELECT kk FROM KerberosKeytabEntity kk JOIN kk.kerberosKeytabPrincipalEntities kkp WHERE kkp.hostId IS NULL AND kkp.principalName=:principalName") })
public class KerberosKeytabEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "keytab_path", updatable = false, nullable = false)
    private java.lang.String keytabPath = null;

    @javax.persistence.Column(name = "owner_name")
    private java.lang.String ownerName;

    @javax.persistence.Column(name = "owner_access")
    private java.lang.String ownerAccess;

    @javax.persistence.Column(name = "group_name")
    private java.lang.String groupName;

    @javax.persistence.Column(name = "group_access")
    private java.lang.String groupAccess;

    @javax.persistence.Column(name = "is_ambari_keytab")
    private java.lang.Integer isAmbariServerKeytab = 0;

    @javax.persistence.Column(name = "write_ambari_jaas")
    private java.lang.Integer writeAmbariJaasFile = 0;

    @javax.persistence.OneToMany(mappedBy = "kerberosKeytabEntity", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.EAGER)
    private java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> kerberosKeytabPrincipalEntities = new java.util.ArrayList<>();

    public KerberosKeytabEntity() {
    }

    public KerberosKeytabEntity(java.lang.String keytabPath) {
        setKeytabPath(keytabPath);
    }

    public KerberosKeytabEntity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab) {
        setKeytabPath(resolvedKerberosKeytab.getFile());
        setAmbariServerKeytab(resolvedKerberosKeytab.isAmbariServerKeytab());
        setWriteAmbariJaasFile(resolvedKerberosKeytab.isMustWriteAmbariJaasFile());
        setOwnerName(resolvedKerberosKeytab.getOwnerName());
        setOwnerAccess(resolvedKerberosKeytab.getOwnerAccess());
        setGroupName(resolvedKerberosKeytab.getGroupName());
        setGroupAccess(resolvedKerberosKeytab.getGroupAccess());
    }

    public java.lang.String getKeytabPath() {
        return keytabPath;
    }

    public void setKeytabPath(java.lang.String keytabPath) {
        this.keytabPath = keytabPath;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> getKerberosKeytabPrincipalEntities() {
        return kerberosKeytabPrincipalEntities;
    }

    public void setKerberosKeytabPrincipalEntities(java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> kerberosKeytabPrincipalEntities) {
        this.kerberosKeytabPrincipalEntities = kerberosKeytabPrincipalEntities;
    }

    public java.lang.String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(java.lang.String ownerName) {
        this.ownerName = ownerName;
    }

    public java.lang.String getOwnerAccess() {
        return ownerAccess;
    }

    public void setOwnerAccess(java.lang.String ownerAccess) {
        this.ownerAccess = ownerAccess;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public java.lang.String getGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(java.lang.String groupAccess) {
        this.groupAccess = groupAccess;
    }

    public boolean isAmbariServerKeytab() {
        return isAmbariServerKeytab == 1;
    }

    public void setAmbariServerKeytab(boolean ambariServerKeytab) {
        this.isAmbariServerKeytab = (ambariServerKeytab) ? 1 : 0;
    }

    public boolean isWriteAmbariJaasFile() {
        return writeAmbariJaasFile == 1;
    }

    public void setWriteAmbariJaasFile(boolean writeAmbariJaasFile) {
        this.writeAmbariJaasFile = (writeAmbariJaasFile) ? 1 : 0;
    }

    public void addKerberosKeytabPrincipal(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity) {
        if (!kerberosKeytabPrincipalEntities.contains(kerberosKeytabPrincipalEntity)) {
            kerberosKeytabPrincipalEntities.add(kerberosKeytabPrincipalEntity);
        }
    }
}