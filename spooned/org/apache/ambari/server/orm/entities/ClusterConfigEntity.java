package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@javax.persistence.Table(name = "clusterconfig", uniqueConstraints = { @javax.persistence.UniqueConstraint(name = "UQ_config_type_tag", columnNames = { "cluster_id", "type_name", "version_tag" }), @javax.persistence.UniqueConstraint(name = "UQ_config_type_version", columnNames = { "cluster_id", "type_name", "version" }) })
@javax.persistence.TableGenerator(name = "config_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "config_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findNextConfigVersion", query = "SELECT COALESCE(MAX(clusterConfig.version),0) + 1 as nextVersion FROM ClusterConfigEntity clusterConfig WHERE clusterConfig.type=:configType AND clusterConfig.clusterId=:clusterId"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findAllConfigsByStack", query = "SELECT clusterConfig FROM ClusterConfigEntity clusterConfig WHERE clusterConfig.clusterId=:clusterId AND clusterConfig.stack=:stack"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findLatestConfigsByStack", query = "SELECT clusterConfig FROM ClusterConfigEntity clusterConfig WHERE clusterConfig.clusterId = :clusterId AND clusterConfig.stack = :stack AND clusterConfig.selectedTimestamp = (SELECT MAX(clusterConfig2.selectedTimestamp) FROM ClusterConfigEntity clusterConfig2 WHERE clusterConfig2.clusterId=:clusterId AND clusterConfig2.stack=:stack AND clusterConfig2.type = clusterConfig.type)"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findLatestConfigsByStackWithTypes", query = "SELECT clusterConfig FROM ClusterConfigEntity clusterConfig WHERE clusterConfig.type IN :types AND clusterConfig.clusterId = :clusterId AND clusterConfig.stack = :stack AND clusterConfig.selectedTimestamp = (SELECT MAX(clusterConfig2.selectedTimestamp) FROM ClusterConfigEntity clusterConfig2 WHERE clusterConfig2.clusterId=:clusterId AND clusterConfig2.stack=:stack AND clusterConfig2.type = clusterConfig.type)"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findNotMappedClusterConfigsToService", query = "SELECT clusterConfig FROM ClusterConfigEntity clusterConfig WHERE clusterConfig.serviceConfigEntities IS EMPTY AND clusterConfig.type != 'cluster-env'"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findEnabledConfigsByStack", query = "SELECT config FROM ClusterConfigEntity config WHERE config.clusterId = :clusterId AND config.selected = 1 AND config.stack = :stack"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findEnabledConfigByType", query = "SELECT config FROM ClusterConfigEntity config WHERE config.clusterId = :clusterId AND config.selected = 1 and config.type = :type"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findEnabledConfigsByTypes", query = "SELECT config FROM ClusterConfigEntity config WHERE config.clusterId = :clusterId AND config.selected = 1 and config.type in :types"), @javax.persistence.NamedQuery(name = "ClusterConfigEntity.findEnabledConfigs", query = "SELECT config FROM ClusterConfigEntity config WHERE config.clusterId = :clusterId AND config.selected = 1") })
public class ClusterConfigEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "config_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "config_id_generator")
    private java.lang.Long configId;

    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "type_name")
    private java.lang.String type;

    @javax.persistence.Column(name = "version")
    private java.lang.Long version;

    @javax.persistence.Column(name = "version_tag")
    private java.lang.String tag;

    @javax.persistence.Column(name = "selected", insertable = true, updatable = true, nullable = false)
    private int selected = 0;

    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Column(name = "config_data", nullable = false, insertable = true)
    @javax.persistence.Lob
    private java.lang.String configJson;

    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Column(name = "config_attributes", nullable = true, insertable = true)
    @javax.persistence.Lob
    private java.lang.String configAttributesJson;

    @javax.persistence.Column(name = "create_timestamp", nullable = false, insertable = true, updatable = false)
    private long timestamp;

    @javax.persistence.Column(name = "selected_timestamp", nullable = false, insertable = true, updatable = true)
    private long selectedTimestamp = 0;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToMany(mappedBy = "clusterConfigEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configGroupConfigMappingEntities;

    @javax.persistence.ManyToMany(mappedBy = "clusterConfigEntities")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities;

    @javax.persistence.Column(name = "unmapped", nullable = false, insertable = true, updatable = true)
    private short unmapped = 0;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "stack_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.StackEntity stack;

    public boolean isUnmapped() {
        return unmapped != 0;
    }

    public void setUnmapped(boolean unmapped) {
        this.unmapped = ((short) ((unmapped) ? 1 : 0));
    }

    public java.lang.Long getConfigId() {
        return configId;
    }

    public void setConfigId(java.lang.Long configId) {
        this.configId = configId;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String typeName) {
        type = typeName;
    }

    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    public java.lang.String getTag() {
        return tag;
    }

    public void setTag(java.lang.String versionTag) {
        tag = versionTag;
    }

    public java.lang.String getData() {
        return configJson;
    }

    public void setData(java.lang.String data) {
        configJson = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long stamp) {
        timestamp = stamp;
    }

    public long getSelectedTimestamp() {
        return selectedTimestamp;
    }

    public java.lang.String getAttributes() {
        return configAttributesJson;
    }

    public void setAttributes(java.lang.String attributes) {
        configAttributesJson = attributes;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getStack() {
        return stack;
    }

    public void setStack(org.apache.ambari.server.orm.entities.StackEntity stack) {
        this.stack = stack;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ClusterConfigEntity that = ((org.apache.ambari.server.orm.entities.ClusterConfigEntity) (object));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(configId, that.configId);
        equalsBuilder.append(clusterId, that.clusterId);
        equalsBuilder.append(type, that.type);
        equalsBuilder.append(tag, that.tag);
        equalsBuilder.append(stack, that.stack);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(configId, clusterId, type, tag, stack);
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> getConfigGroupConfigMappingEntities() {
        return configGroupConfigMappingEntities;
    }

    public void setConfigGroupConfigMappingEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configGroupConfigMappingEntities) {
        this.configGroupConfigMappingEntities = configGroupConfigMappingEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getServiceConfigEntities() {
        return serviceConfigEntities;
    }

    public void setServiceConfigEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities) {
        this.serviceConfigEntities = serviceConfigEntities;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("clusterId", clusterId).add("type", type).add("version", version).add("tag", tag).add("selected", selected == 1).add("selectedTimeStamp", selectedTimestamp).add("created", timestamp).toString();
    }

    public boolean isSelected() {
        return selected == 1;
    }

    public void setSelected(boolean selected) {
        this.selected = (selected) ? 1 : 0;
        if (selected) {
            selectedTimestamp = java.lang.System.currentTimeMillis();
        }
    }
}