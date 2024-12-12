package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class ConfigGroupHostMappingEntityPK implements java.io.Serializable {
    private java.lang.Long configGroupId;

    private java.lang.Long hostId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "config_group_id", nullable = false, insertable = true, updatable = true)
    public java.lang.Long getConfigGroupId() {
        return configGroupId;
    }

    public void setConfigGroupId(java.lang.Long configGroupId) {
        this.configGroupId = configGroupId;
    }

    @javax.persistence.Id
    @javax.persistence.Column(name = "host_id", nullable = false, insertable = true, updatable = true)
    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntityPK that = ((org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntityPK) (o));
        if (!configGroupId.equals(that.configGroupId))
            return false;

        if (!hostId.equals(that.hostId))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = configGroupId.hashCode();
        result = (31 * result) + hostId.hashCode();
        return result;
    }
}