package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@javax.persistence.Entity
@javax.persistence.Table(name = "kkp_mapping_service")
public class KerberosKeytabServiceMappingEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "kkp_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long kerberosKeytabPrincipalId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "service_name", nullable = false)
    private java.lang.String serviceName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "component_name", nullable = false)
    private java.lang.String componentName;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "kkp_id")
    private org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity;

    public KerberosKeytabServiceMappingEntity() {
    }

    public KerberosKeytabServiceMappingEntity(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity, java.lang.String serviceName, java.lang.String componentName) {
        this.kerberosKeytabPrincipalId = kerberosKeytabPrincipalEntity.getKkpId();
        this.kerberosKeytabPrincipalEntity = kerberosKeytabPrincipalEntity;
        this.serviceName = serviceName;
        this.componentName = componentName;
    }

    public java.lang.Long getKerberosKeytabPrincipalId() {
        return kerberosKeytabPrincipalId;
    }

    public void setKerberosKeytabPrincipalId(java.lang.Long kerberosKeytabPrincipalId) {
        this.kerberosKeytabPrincipalId = kerberosKeytabPrincipalId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity getKerberosKeytabPrincipalEntity() {
        return kerberosKeytabPrincipalEntity;
    }

    public void setKerberosKeytabPrincipalEntity(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity) {
        this.kerberosKeytabPrincipalEntity = kerberosKeytabPrincipalEntity;
    }
}