package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "serviceconfig")
@javax.persistence.TableGenerator(name = "service_config_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "service_config_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "ServiceConfigEntity.findAll", query = "SELECT serviceConfig FROM ServiceConfigEntity serviceConfig WHERE serviceConfig.clusterId=:clusterId ORDER BY serviceConfig.version DESC"), @javax.persistence.NamedQuery(name = "ServiceConfigEntity.findNextServiceConfigVersion", query = "SELECT COALESCE(MAX(serviceConfig.version), 0) + 1 AS nextVersion FROM ServiceConfigEntity serviceConfig WHERE serviceConfig.serviceName=:serviceName AND serviceConfig.clusterId=:clusterId"), @javax.persistence.NamedQuery(name = "ServiceConfigEntity.findAllServiceConfigsByStack", query = "SELECT serviceConfig FROM ServiceConfigEntity serviceConfig WHERE serviceConfig.clusterId=:clusterId AND serviceConfig.stack=:stack AND serviceConfig.serviceName=:serviceName"), @javax.persistence.NamedQuery(name = "ServiceConfigEntity.findLatestServiceConfigsByStack", query = "SELECT serviceConfig FROM ServiceConfigEntity serviceConfig WHERE serviceConfig.clusterId = :clusterId AND (serviceConfig.groupId = null OR serviceConfig.groupId IN (SELECT cg.groupId from ConfigGroupEntity cg)) AND serviceConfig.version = (SELECT MAX(serviceConfig2.version) FROM ServiceConfigEntity serviceConfig2 WHERE serviceConfig2.clusterId= :clusterId AND serviceConfig2.stack = :stack AND serviceConfig2.serviceName = serviceConfig.serviceName)"), @javax.persistence.NamedQuery(name = "ServiceConfigEntity.findLatestServiceConfigsByService", query = "SELECT scv FROM ServiceConfigEntity scv WHERE scv.clusterId = :clusterId AND scv.serviceName = :serviceName AND (scv.groupId = null OR scv.groupId IN (SELECT cg.groupId from ConfigGroupEntity cg)) AND scv.version = (SELECT MAX(scv2.version) FROM ServiceConfigEntity scv2 WHERE (scv2.serviceName = :serviceName AND scv2.clusterId = :clusterId) AND (scv2.groupId = scv.groupId OR (scv2.groupId IS NULL AND scv.groupId IS NULL)))"), @javax.persistence.NamedQuery(name = "ServiceConfigEntity.findLatestServiceConfigsByServiceDefaultGroup", query = "SELECT scv FROM ServiceConfigEntity scv WHERE scv.clusterId = :clusterId AND scv.serviceName = :serviceName AND scv.groupId = null AND scv.version = (SELECT MAX(scv2.version) FROM ServiceConfigEntity scv2 WHERE (scv2.serviceName = :serviceName AND scv2.clusterId = :clusterId) AND scv2.groupId IS NULL)"), @javax.persistence.NamedQuery(name = "ServiceConfigEntity.findLatestServiceConfigsByCluster", query = "SELECT scv FROM ServiceConfigEntity scv WHERE scv.clusterId = :clusterId AND scv.serviceConfigId IN (SELECT MAX(scv1.serviceConfigId) FROM ServiceConfigEntity scv1 WHERE (scv1.clusterId = :clusterId) AND (scv1.groupId IS NULL) GROUP BY scv1.serviceName)") })
public class ServiceConfigEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "service_config_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "service_config_id_generator")
    private java.lang.Long serviceConfigId;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "cluster_id", insertable = false, updatable = false, nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "service_name", nullable = false)
    private java.lang.String serviceName;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "group_id", nullable = true)
    private java.lang.Long groupId;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "version", nullable = false)
    private java.lang.Long version;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "create_timestamp", nullable = false)
    private java.lang.Long createTimestamp = java.lang.System.currentTimeMillis();

    @javax.persistence.Basic
    @javax.persistence.Column(name = "user_name")
    private java.lang.String user = "_db";

    @javax.persistence.Basic
    @javax.persistence.Column(name = "note")
    private java.lang.String note;

    @javax.persistence.ElementCollection
    @javax.persistence.CollectionTable(name = "serviceconfighosts", joinColumns = { @javax.persistence.JoinColumn(name = "service_config_id") })
    @javax.persistence.Column(name = "host_id")
    private java.util.List<java.lang.Long> hostIds;

    @javax.persistence.ManyToMany
    @javax.persistence.JoinTable(name = "serviceconfigmapping", joinColumns = { @javax.persistence.JoinColumn(name = "service_config_id", referencedColumnName = "service_config_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "config_id", referencedColumnName = "config_id") })
    private java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "stack_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.StackEntity stack;

    public java.lang.Long getServiceConfigId() {
        return serviceConfigId;
    }

    public void setServiceConfigId(java.lang.Long serviceConfigId) {
        this.serviceConfigId = serviceConfigId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    public java.lang.Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(java.lang.Long create_timestamp) {
        createTimestamp = create_timestamp;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getClusterConfigEntities() {
        return clusterConfigEntities;
    }

    public void setClusterConfigEntities(java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities) {
        this.clusterConfigEntities = clusterConfigEntities;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public java.lang.String getUser() {
        return user;
    }

    public void setUser(java.lang.String user) {
        this.user = user;
    }

    public java.lang.String getNote() {
        return note;
    }

    public void setNote(java.lang.String note) {
        this.note = note;
    }

    public java.lang.Long getGroupId() {
        return groupId;
    }

    public void setGroupId(java.lang.Long groupId) {
        this.groupId = groupId;
    }

    public java.util.List<java.lang.Long> getHostIds() {
        return hostIds;
    }

    public void setHostIds(java.util.List<java.lang.Long> hostIds) {
        this.hostIds = hostIds;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getStack() {
        return stack;
    }

    public void setStack(org.apache.ambari.server.orm.entities.StackEntity stack) {
        this.stack = stack;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (serviceConfigId == null ? 0 : serviceConfigId.hashCode());
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ServiceConfigEntity other = ((org.apache.ambari.server.orm.entities.ServiceConfigEntity) (obj));
        if (serviceConfigId == null) {
            if (other.serviceConfigId != null) {
                return false;
            }
        } else if (!serviceConfigId.equals(other.serviceConfigId)) {
            return false;
        }
        return true;
    }
}