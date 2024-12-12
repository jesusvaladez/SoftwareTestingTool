package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "kerberos_keytab_principal")
@javax.persistence.TableGenerator(name = "kkp_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "kkp_id_seq")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "KerberosKeytabPrincipalEntity.findAll", query = "SELECT kkpe FROM KerberosKeytabPrincipalEntity kkpe"), @javax.persistence.NamedQuery(name = "KerberosKeytabPrincipalEntity.findByHostAndKeytab", query = "SELECT kkpe FROM KerberosKeytabPrincipalEntity kkpe WHERE kkpe.hostId=:hostId AND kkpe.keytabPath=:keytabPath"), @javax.persistence.NamedQuery(name = "KerberosKeytabPrincipalEntity.findByPrincipal", query = "SELECT kkpe FROM KerberosKeytabPrincipalEntity kkpe WHERE kkpe.principalName=:principalName"), @javax.persistence.NamedQuery(name = "KerberosKeytabPrincipalEntity.findByHost", query = "SELECT kkpe FROM KerberosKeytabPrincipalEntity kkpe WHERE kkpe.hostId=:hostId"), @javax.persistence.NamedQuery(name = "KerberosKeytabPrincipalEntity.findByPrincipalAndHost", query = "SELECT kkpe FROM KerberosKeytabPrincipalEntity kkpe WHERE kkpe.hostId=:hostId AND kkpe.principalName=:principalName", hints = { @javax.persistence.QueryHint(name = "eclipselink.query-results-cache", value = "true"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.size", value = "500") }), @javax.persistence.NamedQuery(name = "KerberosKeytabPrincipalEntity.findByHostKeytabAndPrincipal", query = "SELECT kkpe FROM KerberosKeytabPrincipalEntity kkpe WHERE kkpe.hostId=:hostId AND kkpe.keytabPath=:keytabPath AND kkpe.principalName=:principalName"), @javax.persistence.NamedQuery(name = "KerberosKeytabPrincipalEntity.findByKeytabAndPrincipalNullHost", query = "SELECT kkpe FROM KerberosKeytabPrincipalEntity kkpe WHERE kkpe.principalName=:principalName AND kkpe.keytabPath=:keytabPath AND kkpe.hostId IS NULL") })
public class KerberosKeytabPrincipalEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "kkp_id_generator")
    @javax.persistence.Column(name = "kkp_id")
    private java.lang.Long kkpId;

    @javax.persistence.Column(name = "keytab_path", updatable = false, nullable = false)
    private java.lang.String keytabPath;

    @javax.persistence.Column(name = "principal_name", updatable = false, nullable = false)
    private java.lang.String principalName;

    @javax.persistence.Column(name = "host_id")
    private java.lang.Long hostId;

    @javax.persistence.Column(name = "is_distributed", nullable = false)
    private java.lang.Integer isDistributed = 0;

    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    @javax.persistence.JoinColumn(name = "keytab_path", referencedColumnName = "keytab_path", updatable = false, nullable = false, insertable = false)
    private org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id", updatable = false, insertable = false)
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "principal_name", referencedColumnName = "principal_name", updatable = false, nullable = false, insertable = false)
    private org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "kerberosKeytabPrincipalEntity", orphanRemoval = true, fetch = javax.persistence.FetchType.EAGER)
    private java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity> serviceMapping = new java.util.ArrayList<>();

    public KerberosKeytabPrincipalEntity() {
    }

    public KerberosKeytabPrincipalEntity(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity, org.apache.ambari.server.orm.entities.HostEntity hostEntity, org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        setKerberosKeytabEntity(kerberosKeytabEntity);
        setHostEntity(hostEntity);
        setKerberosPrincipalEntity(kerberosPrincipalEntity);
    }

    public java.lang.Long getKkpId() {
        return kkpId;
    }

    public void setKkpId(java.lang.Long kkpId) {
        this.kkpId = kkpId;
    }

    public java.lang.Boolean isDistributed() {
        return isDistributed == 1;
    }

    public void setDistributed(java.lang.Boolean isDistributed) {
        this.isDistributed = (isDistributed) ? 1 : 0;
    }

    public org.apache.ambari.server.orm.entities.KerberosKeytabEntity getKerberosKeytabEntity() {
        return kerberosKeytabEntity;
    }

    public void setKerberosKeytabEntity(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke) {
        kerberosKeytabEntity = kke;
        if (kke != null) {
            keytabPath = kke.getKeytabPath();
        }
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
        if (hostEntity != null) {
            hostId = hostEntity.getHostId();
        }
    }

    public org.apache.ambari.server.orm.entities.KerberosPrincipalEntity getKerberosPrincipalEntity() {
        return kerberosPrincipalEntity;
    }

    public void setKerberosPrincipalEntity(org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        this.kerberosPrincipalEntity = kerberosPrincipalEntity;
        if (kerberosPrincipalEntity != null) {
            principalName = kerberosPrincipalEntity.getPrincipalName();
        }
    }

    public java.lang.String getKeytabPath() {
        return kerberosKeytabEntity != null ? kerberosKeytabEntity.getKeytabPath() : null;
    }

    public java.lang.String getPrincipalName() {
        return kerberosPrincipalEntity != null ? kerberosPrincipalEntity.getPrincipalName() : null;
    }

    public java.lang.Long getHostId() {
        return hostEntity != null ? hostEntity.getHostId() : null;
    }

    public java.lang.String getHostName() {
        return hostEntity != null ? hostEntity.getHostName() : null;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity> getServiceMapping() {
        return serviceMapping;
    }

    public void setServiceMapping(java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity> serviceMapping) {
        this.serviceMapping = (serviceMapping == null) ? java.util.Collections.emptyList() : new java.util.ArrayList<>(serviceMapping);
    }

    public boolean putServiceMapping(java.lang.String service, java.lang.String component) {
        if (containsMapping(service, component)) {
            return false;
        } else {
            serviceMapping.add(new org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity(this, service, component));
            return true;
        }
    }

    public com.google.common.collect.Multimap<java.lang.String, java.lang.String> getServiceMappingAsMultimap() {
        com.google.common.collect.Multimap<java.lang.String, java.lang.String> result = com.google.common.collect.ArrayListMultimap.create();
        for (org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity mappingEntity : serviceMapping) {
            result.put(mappingEntity.getServiceName(), mappingEntity.getComponentName());
        }
        return result;
    }

    public boolean containsMapping(java.lang.String serviceName, java.lang.String componentName) {
        for (org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity mappingEntity : serviceMapping) {
            if (com.google.common.base.Objects.equal(mappingEntity.getComponentName(), componentName) && com.google.common.base.Objects.equal(mappingEntity.getServiceName(), serviceName)) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity that = ((org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity) (o));
        return (com.google.common.base.Objects.equal(keytabPath, that.keytabPath) && com.google.common.base.Objects.equal(principalName, that.principalName)) && com.google.common.base.Objects.equal(hostId, that.hostId);
    }

    @java.lang.Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(keytabPath, principalName, hostId);
    }
}