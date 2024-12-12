package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "configgroup")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "configGroupByName", query = "SELECT configgroup " + ("FROM ConfigGroupEntity configgroup " + "WHERE configgroup.groupName=:groupName")), @javax.persistence.NamedQuery(name = "allConfigGroups", query = "SELECT configgroup " + "FROM ConfigGroupEntity configgroup"), @javax.persistence.NamedQuery(name = "configGroupsByTag", query = "SELECT configgroup FROM ConfigGroupEntity configgroup " + "WHERE configgroup.tag=:tagName") })
@javax.persistence.TableGenerator(name = "configgroup_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "configgroup_id_seq", initialValue = 1)
public class ConfigGroupEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "group_id", nullable = false, insertable = true, updatable = true)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "configgroup_id_generator")
    private java.lang.Long groupId;

    @javax.persistence.Column(name = "cluster_id", insertable = false, updatable = false, nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "group_name", nullable = false, unique = true, updatable = true)
    private java.lang.String groupName;

    @javax.persistence.Column(name = "tag", nullable = false)
    private java.lang.String tag;

    @javax.persistence.Column(name = "description")
    private java.lang.String description;

    @javax.persistence.Column(name = "create_timestamp", nullable = false, insertable = true, updatable = false)
    private long timestamp;

    @javax.persistence.Column(name = "service_name")
    private java.lang.String serviceName;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToMany(mappedBy = "configGroupEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> configGroupHostMappingEntities;

    @javax.persistence.OneToMany(mappedBy = "configGroupEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configGroupConfigMappingEntities;

    public java.lang.Long getGroupId() {
        return groupId;
    }

    public void setGroupId(java.lang.Long groupId) {
        this.groupId = groupId;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public java.lang.String getTag() {
        return tag;
    }

    public void setTag(java.lang.String tag) {
        this.tag = tag;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> getConfigGroupHostMappingEntities() {
        return configGroupHostMappingEntities;
    }

    public void setConfigGroupHostMappingEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> configGroupHostMappingEntities) {
        this.configGroupHostMappingEntities = configGroupHostMappingEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> getConfigGroupConfigMappingEntities() {
        return configGroupConfigMappingEntities;
    }

    public void setConfigGroupConfigMappingEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configGroupConfigMappingEntities) {
        this.configGroupConfigMappingEntities = configGroupConfigMappingEntities;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ConfigGroupEntity that = ((org.apache.ambari.server.orm.entities.ConfigGroupEntity) (o));
        if (!clusterId.equals(that.clusterId))
            return false;

        if (!groupId.equals(that.groupId))
            return false;

        if (!groupName.equals(that.groupName))
            return false;

        if (!tag.equals(that.tag))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = (31 * result) + clusterId.hashCode();
        result = (31 * result) + groupName.hashCode();
        result = (31 * result) + tag.hashCode();
        return result;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }
}