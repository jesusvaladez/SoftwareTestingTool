package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@javax.persistence.Entity
@javax.persistence.Table(name = "confgroupclusterconfigmapping")
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntityPK.class)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "configsByGroup", query = "SELECT configs FROM ConfigGroupConfigMappingEntity configs " + "WHERE configs.configGroupId=:groupId") })
public class ConfigGroupConfigMappingEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "config_group_id", nullable = false, insertable = true, updatable = true)
    private java.lang.Long configGroupId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = true, updatable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "config_type", nullable = false, insertable = true, updatable = false)
    private java.lang.String configType;

    @javax.persistence.Column(name = "version_tag", nullable = false, insertable = true, updatable = false)
    private java.lang.String versionTag;

    @javax.persistence.Column(name = "create_timestamp", nullable = false, insertable = true, updatable = true)
    private java.lang.Long timestamp;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false, insertable = false, updatable = false), @javax.persistence.JoinColumn(name = "config_type", referencedColumnName = "type_name", nullable = false, insertable = false, updatable = false), @javax.persistence.JoinColumn(name = "version_tag", referencedColumnName = "version_tag", nullable = false, insertable = false, updatable = false) })
    private org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "config_group_id", referencedColumnName = "group_id", nullable = false, insertable = false, updatable = false) })
    private org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity;

    public java.lang.Long getConfigGroupId() {
        return configGroupId;
    }

    public void setConfigGroupId(java.lang.Long configGroupId) {
        this.configGroupId = configGroupId;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getConfigType() {
        return configType;
    }

    public void setConfigType(java.lang.String configType) {
        this.configType = configType;
    }

    public java.lang.String getVersionTag() {
        return versionTag;
    }

    public void setVersionTag(java.lang.String versionTag) {
        this.versionTag = versionTag;
    }

    public java.lang.Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.lang.Long timestamp) {
        this.timestamp = timestamp;
    }

    public org.apache.ambari.server.orm.entities.ClusterConfigEntity getClusterConfigEntity() {
        return clusterConfigEntity;
    }

    public void setClusterConfigEntity(org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity) {
        this.clusterConfigEntity = clusterConfigEntity;
    }

    public org.apache.ambari.server.orm.entities.ConfigGroupEntity getConfigGroupEntity() {
        return configGroupEntity;
    }

    public void setConfigGroupEntity(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) {
        this.configGroupEntity = configGroupEntity;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity that = ((org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity) (o));
        if (!clusterId.equals(that.clusterId))
            return false;

        if (!configGroupId.equals(that.configGroupId))
            return false;

        if (!configType.equals(that.configType))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = configGroupId.hashCode();
        result = (31 * result) + clusterId.hashCode();
        result = (31 * result) + configType.hashCode();
        return result;
    }
}