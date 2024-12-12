package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import static org.apache.commons.lang.StringUtils.defaultString;
@javax.persistence.Table(name = "clusters")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "clusterByName", query = "SELECT cluster " + ("FROM ClusterEntity cluster " + "WHERE cluster.clusterName=:clusterName")), @javax.persistence.NamedQuery(name = "allClusters", query = "SELECT clusters " + "FROM ClusterEntity clusters"), @javax.persistence.NamedQuery(name = "clusterByResourceId", query = "SELECT cluster " + ("FROM ClusterEntity cluster " + "WHERE cluster.resource.id=:resourceId")) })
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "cluster_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "cluster_id_seq", initialValue = 1)
public class ClusterEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = true, updatable = true)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "cluster_id_generator")
    private java.lang.Long clusterId;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "cluster_name", nullable = false, insertable = true, updatable = true, unique = true, length = 100)
    private java.lang.String clusterName;

    @javax.persistence.Basic
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "provisioning_state", insertable = true, updatable = true)
    private org.apache.ambari.server.state.State provisioningState = org.apache.ambari.server.state.State.INIT;

    @javax.persistence.Basic
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "security_type", nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.state.SecurityType securityType = org.apache.ambari.server.state.SecurityType.NONE;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "desired_cluster_state", insertable = true, updatable = true)
    private java.lang.String desiredClusterState = "";

    @javax.persistence.Basic
    @javax.persistence.Column(name = "cluster_info", insertable = true, updatable = true)
    private java.lang.String clusterInfo = "";

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "desired_stack_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.StackEntity desiredStack;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ClusterServiceEntity> clusterServiceEntities;

    @javax.persistence.OneToOne(mappedBy = "clusterEntity", cascade = javax.persistence.CascadeType.REMOVE)
    private org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity;

    @javax.persistence.ManyToMany(mappedBy = "clusterEntities")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostEntity> hostEntities;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configEntities;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupEntity> configGroupEntities;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleEntity> requestScheduleEntities;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity", cascade = javax.persistence.CascadeType.REMOVE)
    private java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.LAZY)
    private java.util.Collection<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitionEntities;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.LAZY)
    private java.util.Collection<org.apache.ambari.server.orm.entities.WidgetEntity> widgetEntities;

    @javax.persistence.OneToMany(mappedBy = "clusterEntity", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.LAZY)
    private java.util.Collection<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> widgetLayoutEntities;

    @javax.persistence.OneToOne(cascade = javax.persistence.CascadeType.ALL)
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "resource_id", referencedColumnName = "resource_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.ResourceEntity resource;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "upgrade_id", nullable = true, insertable = false, updatable = false)
    private java.lang.Long upgradeId;

    @javax.persistence.OneToOne(cascade = javax.persistence.CascadeType.REMOVE)
    @javax.persistence.JoinColumn(name = "upgrade_id", referencedColumnName = "upgrade_id", nullable = true, insertable = false, updatable = true)
    private org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = null;

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getDesiredClusterState() {
        return StringUtils.defaultString(desiredClusterState);
    }

    public void setDesiredClusterState(java.lang.String desiredClusterState) {
        this.desiredClusterState = desiredClusterState;
    }

    public java.lang.String getClusterInfo() {
        return StringUtils.defaultString(clusterInfo);
    }

    public void setClusterInfo(java.lang.String clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getDesiredStack() {
        return desiredStack;
    }

    public void setDesiredStack(org.apache.ambari.server.orm.entities.StackEntity desiredStack) {
        this.desiredStack = desiredStack;
    }

    public org.apache.ambari.server.state.State getProvisioningState() {
        return provisioningState;
    }

    public void setProvisioningState(org.apache.ambari.server.state.State provisioningState) {
        this.provisioningState = provisioningState;
    }

    public org.apache.ambari.server.state.SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(org.apache.ambari.server.state.SecurityType securityType) {
        this.securityType = securityType;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ClusterEntity that = ((org.apache.ambari.server.orm.entities.ClusterEntity) (o));
        if (!clusterId.equals(that.clusterId)) {
            return false;
        }
        if (!clusterName.equals(that.clusterName)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (null == clusterId) ? 0 : clusterId.hashCode();
        result = (31 * result) + clusterName.hashCode();
        return result;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ClusterServiceEntity> getClusterServiceEntities() {
        return clusterServiceEntities;
    }

    public void setClusterServiceEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ClusterServiceEntity> clusterServiceEntities) {
        this.clusterServiceEntities = clusterServiceEntities;
    }

    public org.apache.ambari.server.orm.entities.ClusterStateEntity getClusterStateEntity() {
        return clusterStateEntity;
    }

    public void setClusterStateEntity(org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity) {
        this.clusterStateEntity = clusterStateEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostEntity> getHostEntities() {
        return hostEntities;
    }

    public void setHostEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostEntity> hostEntities) {
        this.hostEntities = hostEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getClusterConfigEntities() {
        return configEntities;
    }

    public void setClusterConfigEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> entities) {
        configEntities = entities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupEntity> getConfigGroupEntities() {
        return configGroupEntities;
    }

    public void setConfigGroupEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupEntity> configGroupEntities) {
        this.configGroupEntities = configGroupEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleEntity> getRequestScheduleEntities() {
        return requestScheduleEntities;
    }

    public void setRequestScheduleEntities(java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleEntity> requestScheduleEntities) {
        this.requestScheduleEntities = requestScheduleEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getServiceConfigEntities() {
        return serviceConfigEntities;
    }

    public void setServiceConfigEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities) {
        this.serviceConfigEntities = serviceConfigEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> getAlertDefinitionEntities() {
        return alertDefinitionEntities;
    }

    public org.apache.ambari.server.orm.entities.ResourceEntity getResource() {
        return resource;
    }

    public void setResource(org.apache.ambari.server.orm.entities.ResourceEntity resource) {
        this.resource = resource;
    }

    public org.apache.ambari.server.orm.entities.UpgradeEntity getUpgradeEntity() {
        return upgradeEntity;
    }

    public void setUpgradeEntity(org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity) {
        this.upgradeEntity = upgradeEntity;
    }
}