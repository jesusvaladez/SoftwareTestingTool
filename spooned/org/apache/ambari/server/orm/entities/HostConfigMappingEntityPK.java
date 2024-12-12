package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class HostConfigMappingEntityPK implements java.io.Serializable {
    private java.lang.Long clusterId;

    private java.lang.Long hostId;

    private java.lang.String type;

    private java.lang.Long createTimestamp;

    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = true, updatable = true, length = 10)
    @javax.persistence.Id
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long id) {
        clusterId = id;
    }

    @javax.persistence.Column(name = "host_id", insertable = true, updatable = true, nullable = false)
    @javax.persistence.Id
    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    @javax.persistence.Column(name = "type_name", insertable = true, updatable = true, nullable = false)
    @javax.persistence.Id
    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @javax.persistence.Column(name = "create_timestamp", insertable = true, updatable = true, nullable = false)
    @javax.persistence.Id
    public java.lang.Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(java.lang.Long timestamp) {
        createTimestamp = timestamp;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.HostConfigMappingEntityPK that = ((org.apache.ambari.server.orm.entities.HostConfigMappingEntityPK) (o));
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null)
            return false;

        if (hostId != null ? !hostId.equals(that.hostId) : that.hostId != null)
            return false;

        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;

        if (createTimestamp != null ? !createTimestamp.equals(that.createTimestamp) : that.createTimestamp != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.intValue() : 0;
        result = (31 * result) + (type != null ? type.hashCode() : 0);
        result = (31 * result) + (hostId != null ? hostId.hashCode() : 0);
        result = (31 * result) + createTimestamp.intValue();
        return result;
    }
}