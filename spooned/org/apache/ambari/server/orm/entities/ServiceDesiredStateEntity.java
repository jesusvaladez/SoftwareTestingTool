package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK.class)
@javax.persistence.Table(name = "servicedesiredstate")
@javax.persistence.Entity
public class ServiceDesiredStateEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long clusterId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "service_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "desired_state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.State desiredState = org.apache.ambari.server.state.State.INIT;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "desired_host_role_mapping", nullable = false, insertable = true, updatable = true, length = 10)
    private int desiredHostRoleMapping = 0;

    @javax.persistence.Column(name = "maintenance_state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.MaintenanceState maintenanceState = org.apache.ambari.server.state.MaintenanceState.OFF;

    @javax.persistence.Column(name = "credential_store_enabled", nullable = false, insertable = true, updatable = true)
    private short credentialStoreEnabled = 0;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false), @javax.persistence.JoinColumn(name = "service_name", referencedColumnName = "service_name", nullable = false) })
    private org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "desired_repo_version_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion;

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

    public org.apache.ambari.server.state.State getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(org.apache.ambari.server.state.State desiredState) {
        this.desiredState = desiredState;
    }

    public int getDesiredHostRoleMapping() {
        return desiredHostRoleMapping;
    }

    public void setDesiredHostRoleMapping(int desiredHostRoleMapping) {
        this.desiredHostRoleMapping = desiredHostRoleMapping;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getDesiredStack() {
        return desiredRepositoryVersion.getStack();
    }

    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState state) {
        maintenanceState = state;
    }

    public boolean isCredentialStoreEnabled() {
        return credentialStoreEnabled != 0;
    }

    public void setCredentialStoreEnabled(boolean credentialStoreEnabled) {
        this.credentialStoreEnabled = ((short) ((credentialStoreEnabled == false) ? 0 : 1));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity that = ((org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity) (o));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(clusterId, that.clusterId);
        equalsBuilder.append(desiredState, that.desiredState);
        equalsBuilder.append(desiredHostRoleMapping, that.desiredHostRoleMapping);
        equalsBuilder.append(serviceName, that.serviceName);
        equalsBuilder.append(desiredRepositoryVersion, that.desiredRepositoryVersion);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(clusterId, serviceName, desiredState, desiredHostRoleMapping, desiredRepositoryVersion);
    }

    public org.apache.ambari.server.orm.entities.ClusterServiceEntity getClusterServiceEntity() {
        return clusterServiceEntity;
    }

    public void setClusterServiceEntity(org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity) {
        this.clusterServiceEntity = clusterServiceEntity;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getDesiredRepositoryVersion() {
        return desiredRepositoryVersion;
    }

    public void setDesiredRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion) {
        this.desiredRepositoryVersion = desiredRepositoryVersion;
    }
}