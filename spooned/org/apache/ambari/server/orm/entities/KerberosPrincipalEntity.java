package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@javax.persistence.Entity
@javax.persistence.Table(name = "kerberos_principal")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "KerberosPrincipalEntityFindAll", query = "SELECT kp FROM KerberosPrincipalEntity kp") })
public class KerberosPrincipalEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "principal_name", insertable = true, updatable = false, nullable = false)
    private java.lang.String principalName = null;

    @javax.persistence.Column(name = "is_service", insertable = true, updatable = false, nullable = false)
    private java.lang.Integer service = 1;

    @javax.persistence.Column(name = "cached_keytab_path", insertable = true, updatable = true, nullable = true)
    private java.lang.String cachedKeytabPath = null;

    @javax.persistence.OneToMany(mappedBy = "kerberosPrincipalEntity", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.EAGER)
    private java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> kerberosKeytabPrincipalEntities = new java.util.ArrayList<>();

    public KerberosPrincipalEntity() {
    }

    public KerberosPrincipalEntity(java.lang.String principalName, boolean service, java.lang.String cachedKeytabPath) {
        setPrincipalName(principalName);
        setService(service);
        setCachedKeytabPath(cachedKeytabPath);
    }

    public java.lang.String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(java.lang.String principalName) {
        this.principalName = principalName;
    }

    public boolean isService() {
        return service == 1;
    }

    public void setService(boolean service) {
        this.service = (service) ? 1 : 0;
    }

    public java.lang.String getCachedKeytabPath() {
        return cachedKeytabPath;
    }

    public void setCachedKeytabPath(java.lang.String cachedKeytabPath) {
        this.cachedKeytabPath = cachedKeytabPath;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> getKerberosKeytabPrincipalEntities() {
        return kerberosKeytabPrincipalEntities;
    }

    public void setKerberosKeytabPrincipalEntities(java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> kerberosKeytabPrincipalEntities) {
        this.kerberosKeytabPrincipalEntities = kerberosKeytabPrincipalEntities;
    }

    public void addKerberosKeytabPrincipal(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity) {
        if (!kerberosKeytabPrincipalEntities.contains(kerberosKeytabPrincipalEntity)) {
            kerberosKeytabPrincipalEntities.add(kerberosKeytabPrincipalEntity);
        }
    }
}