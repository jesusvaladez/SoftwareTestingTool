package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.persistence.Entity
@javax.persistence.Table(name = "servicecomponentdesiredstate", uniqueConstraints = @javax.persistence.UniqueConstraint(name = "unq_scdesiredstate_name", columnNames = { "component_name", "service_name", "cluster_id" }))
@javax.persistence.TableGenerator(name = "servicecomponentdesiredstate_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "servicecomponentdesiredstate_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "ServiceComponentDesiredStateEntity.findByName", query = "SELECT scds FROM ServiceComponentDesiredStateEntity scds WHERE scds.clusterId = :clusterId AND scds.serviceName = :serviceName AND scds.componentName = :componentName") })
public class ServiceComponentDesiredStateEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "servicecomponentdesiredstate_id_generator")
    private java.lang.Long id;

    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "service_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "component_name", nullable = false, insertable = true, updatable = true)
    private java.lang.String componentName;

    @javax.persistence.Column(name = "desired_state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.State desiredState = org.apache.ambari.server.state.State.INIT;

    @javax.persistence.Column(name = "recovery_enabled", nullable = false, insertable = true, updatable = true)
    private java.lang.Integer recoveryEnabled = 0;

    @javax.persistence.Column(name = "repo_state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.RepositoryVersionState repoState = org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "desired_repo_version_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false), @javax.persistence.JoinColumn(name = "service_name", referencedColumnName = "service_name", nullable = false) })
    private org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity;

    @javax.persistence.OneToMany(mappedBy = "serviceComponentDesiredStateEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStateEntities;

    @javax.persistence.OneToMany(mappedBy = "serviceComponentDesiredStateEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities;

    @javax.persistence.OneToMany(mappedBy = "m_serviceComponentDesiredStateEntity", cascade = { javax.persistence.CascadeType.ALL })
    private java.util.Collection<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> serviceComponentVersions;

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

    public java.lang.String getComponentName() {
        return componentName;
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

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getDesiredRepositoryVersion() {
        return desiredRepositoryVersion;
    }

    public void setDesiredRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion) {
        this.desiredRepositoryVersion = desiredRepositoryVersion;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getDesiredStack() {
        return desiredRepositoryVersion.getStack();
    }

    public java.lang.String getDesiredVersion() {
        return desiredRepositoryVersion.getVersion();
    }

    public void addVersion(org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity versionEntity) {
        if (null == serviceComponentVersions) {
            serviceComponentVersions = new java.util.ArrayList<>();
        }
        serviceComponentVersions.add(versionEntity);
        versionEntity.setServiceComponentDesiredState(this);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> getVersions() {
        return serviceComponentVersions;
    }

    public boolean isRecoveryEnabled() {
        return recoveryEnabled != 0;
    }

    public void setRecoveryEnabled(boolean recoveryEnabled) {
        this.recoveryEnabled = (recoveryEnabled == false) ? 0 : 1;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity that = ((org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity) (o));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(id, that.id);
        equalsBuilder.append(clusterId, that.clusterId);
        equalsBuilder.append(componentName, that.componentName);
        equalsBuilder.append(desiredState, that.desiredState);
        equalsBuilder.append(serviceName, that.serviceName);
        equalsBuilder.append(desiredRepositoryVersion, that.desiredRepositoryVersion);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(id, clusterId, serviceName, componentName, desiredState, desiredRepositoryVersion);
    }

    public org.apache.ambari.server.orm.entities.ClusterServiceEntity getClusterServiceEntity() {
        return clusterServiceEntity;
    }

    public void setClusterServiceEntity(org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity) {
        this.clusterServiceEntity = clusterServiceEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> getHostComponentStateEntities() {
        return hostComponentStateEntities;
    }

    public void setHostComponentStateEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStateEntities) {
        this.hostComponentStateEntities = hostComponentStateEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> getHostComponentDesiredStateEntities() {
        return hostComponentDesiredStateEntities;
    }

    public void setHostComponentDesiredStateEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities) {
        this.hostComponentDesiredStateEntities = hostComponentDesiredStateEntities;
    }

    public void setRepositoryState(org.apache.ambari.server.state.RepositoryVersionState state) {
        repoState = state;
    }

    public org.apache.ambari.server.state.RepositoryVersionState getRepositoryState() {
        return repoState;
    }
}