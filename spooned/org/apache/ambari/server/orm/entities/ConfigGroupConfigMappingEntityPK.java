package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class ConfigGroupConfigMappingEntityPK implements java.io.Serializable {
    private java.lang.Long configGroupId;

    private java.lang.Long clusterId;

    private java.lang.String configType;

    @javax.persistence.Id
    @javax.persistence.Column(name = "config_group_id", nullable = false, insertable = true, updatable = true)
    public java.lang.Long getConfigGroupId() {
        return configGroupId;
    }

    public void setConfigGroupId(java.lang.Long configGroupId) {
        this.configGroupId = configGroupId;
    }

    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = true, updatable = true, length = 10)
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    @javax.persistence.Id
    @javax.persistence.Column(name = "config_type", nullable = false, insertable = true, updatable = true)
    public java.lang.String getConfigType() {
        return configType;
    }

    public void setConfigType(java.lang.String configType) {
        this.configType = configType;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntityPK that = ((org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntityPK) (o));
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