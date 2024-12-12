package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import static org.apache.commons.lang.StringUtils.defaultString;
@javax.persistence.Entity
@javax.persistence.Table(name = "hostcomponentdesiredstate", uniqueConstraints = @javax.persistence.UniqueConstraint(name = "UQ_hcdesiredstate_name", columnNames = { "component_name", "service_name", "host_id", "cluster_id" }))
@javax.persistence.TableGenerator(name = "hostcomponentdesiredstate_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "hostcomponentdesiredstate_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "HostComponentDesiredStateEntity.findAll", query = "SELECT hcds from HostComponentDesiredStateEntity hcds"), @javax.persistence.NamedQuery(name = "HostComponentDesiredStateEntity.findByServiceAndComponent", query = "SELECT hcds from HostComponentDesiredStateEntity hcds WHERE hcds.serviceName=:serviceName AND hcds.componentName=:componentName"), @javax.persistence.NamedQuery(name = "HostComponentDesiredStateEntity.findByServiceComponentAndHost", query = "SELECT hcds from HostComponentDesiredStateEntity hcds WHERE hcds.serviceName=:serviceName AND hcds.componentName=:componentName AND hcds.hostEntity.hostName=:hostName"), @javax.persistence.NamedQuery(name = "HostComponentDesiredStateEntity.findByIndexAndHost", query = "SELECT hcds from HostComponentDesiredStateEntity hcds WHERE hcds.clusterId=:clusterId AND hcds.serviceName=:serviceName AND hcds.componentName=:componentName AND hcds.hostId=:hostId"), @javax.persistence.NamedQuery(name = "HostComponentDesiredStateEntity.findByIndex", query = "SELECT hcds from HostComponentDesiredStateEntity hcds WHERE hcds.clusterId=:clusterId AND hcds.serviceName=:serviceName AND hcds.componentName=:componentName"), @javax.persistence.NamedQuery(name = "HostComponentDesiredStateEntity.findByHostsAndCluster", query = "SELECT hcds from HostComponentDesiredStateEntity hcds WHERE hcds.hostId IN :hostIds AND hcds.clusterId=:clusterId") })
public class HostComponentDesiredStateEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "hostcomponentdesiredstate_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "service_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "host_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long hostId;

    @javax.persistence.Column(name = "component_name", insertable = false, updatable = false)
    private java.lang.String componentName = "";

    @javax.persistence.Basic
    @javax.persistence.Column(name = "desired_state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.State desiredState = org.apache.ambari.server.state.State.INIT;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "admin_state", nullable = true, insertable = true, updatable = true)
    private org.apache.ambari.server.state.HostComponentAdminState adminState;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false), @javax.persistence.JoinColumn(name = "service_name", referencedColumnName = "service_name", nullable = false), @javax.persistence.JoinColumn(name = "component_name", referencedColumnName = "component_name", nullable = false) })
    private org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id", nullable = false)
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "maintenance_state", nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.state.MaintenanceState maintenanceState = org.apache.ambari.server.state.MaintenanceState.OFF;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "restart_required", insertable = true, updatable = true, nullable = false)
    private java.lang.Integer restartRequired = 0;

    @javax.persistence.Basic
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "blueprint_provisioning_state", insertable = true, updatable = true)
    private org.apache.ambari.server.state.BlueprintProvisioningState blueprintProvisioningState = org.apache.ambari.server.state.BlueprintProvisioningState.NONE;

    public java.lang.Long getId() {
        return id;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.Long getHostId() {
        return hostEntity != null ? hostEntity.getHostId() : null;
    }

    public java.lang.String getComponentName() {
        return StringUtils.defaultString(componentName);
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.state.State getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(org.apache.ambari.server.state.State desiredState) {
        this.desiredState = desiredState;
    }

    public org.apache.ambari.server.state.HostComponentAdminState getAdminState() {
        return adminState;
    }

    public void setAdminState(org.apache.ambari.server.state.HostComponentAdminState attribute) {
        adminState = attribute;
    }

    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState state) {
        maintenanceState = state;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    public org.apache.ambari.server.state.BlueprintProvisioningState getBlueprintProvisioningState() {
        return blueprintProvisioningState;
    }

    public void setBlueprintProvisioningState(org.apache.ambari.server.state.BlueprintProvisioningState blueprintProvisioningState) {
        this.blueprintProvisioningState = blueprintProvisioningState;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity that = ((org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity) (o));
        if (!com.google.common.base.Objects.equal(id, that.id)) {
            return false;
        }
        if (!com.google.common.base.Objects.equal(clusterId, that.clusterId)) {
            return false;
        }
        if (!com.google.common.base.Objects.equal(componentName, that.componentName)) {
            return false;
        }
        if (!com.google.common.base.Objects.equal(desiredState, that.desiredState)) {
            return false;
        }
        if (!com.google.common.base.Objects.equal(hostEntity, that.hostEntity)) {
            return false;
        }
        if (!com.google.common.base.Objects.equal(serviceName, that.serviceName)) {
            return false;
        }
        if (!com.google.common.base.Objects.equal(blueprintProvisioningState, that.blueprintProvisioningState)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.hashCode() : 0;
        result = (31 * result) + (clusterId != null ? clusterId.hashCode() : 0);
        result = (31 * result) + (hostEntity != null ? hostEntity.hashCode() : 0);
        result = (31 * result) + (componentName != null ? componentName.hashCode() : 0);
        result = (31 * result) + (desiredState != null ? desiredState.hashCode() : 0);
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (31 * result) + (blueprintProvisioningState != null ? blueprintProvisioningState.hashCode() : 0);
        return result;
    }

    public org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity getServiceComponentDesiredStateEntity() {
        return serviceComponentDesiredStateEntity;
    }

    public void setServiceComponentDesiredStateEntity(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        this.serviceComponentDesiredStateEntity = serviceComponentDesiredStateEntity;
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
    }

    public boolean isRestartRequired() {
        return restartRequired == 0 ? false : true;
    }

    public void setRestartRequired(boolean restartRequired) {
        this.restartRequired = (restartRequired == false) ? 0 : 1;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("serviceName", serviceName).add("componentName", componentName).add("hostId", hostId).add("desiredState", desiredState).toString();
    }
}