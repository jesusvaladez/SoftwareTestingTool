package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@javax.persistence.Table(name = "hostconfigmapping")
@javax.persistence.Entity
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.HostConfigMappingEntityPK.class)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "HostConfigMappingEntity.findAll", query = "SELECT entity FROM HostConfigMappingEntity entity"), @javax.persistence.NamedQuery(name = "HostConfigMappingEntity.findByHostId", query = "SELECT entity FROM HostConfigMappingEntity entity WHERE entity.hostId = :hostId") })
public class HostConfigMappingEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", insertable = true, updatable = false, nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "host_id", insertable = true, updatable = false, nullable = false)
    private java.lang.Long hostId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "type_name", insertable = true, updatable = false, nullable = false)
    private java.lang.String type;

    @javax.persistence.Id
    @javax.persistence.Column(name = "create_timestamp", insertable = true, updatable = false, nullable = false)
    private java.lang.Long createTimestamp;

    @javax.persistence.Column(name = "version_tag", insertable = true, updatable = false, nullable = false)
    private java.lang.String versionTag;

    @javax.persistence.Column(name = "service_name", insertable = true, updatable = true)
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "selected", insertable = true, updatable = true, nullable = false)
    private int selected = 0;

    @javax.persistence.Column(name = "user_name", insertable = true, updatable = true, nullable = false)
    private java.lang.String user = null;

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long id) {
        clusterId = id;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(java.lang.Long timestamp) {
        createTimestamp = timestamp;
    }

    public java.lang.String getVersion() {
        return versionTag;
    }

    public void setVersion(java.lang.String version) {
        versionTag = version;
    }

    public int isSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String name) {
        serviceName = name;
    }

    public java.lang.String getUser() {
        return user;
    }

    public void setUser(java.lang.String userName) {
        user = userName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.HostConfigMappingEntity that = ((org.apache.ambari.server.orm.entities.HostConfigMappingEntity) (o));
        if (selected != that.selected)
            return false;

        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null)
            return false;

        if (createTimestamp != null ? !createTimestamp.equals(that.createTimestamp) : that.createTimestamp != null)
            return false;

        if (hostId != null ? !hostId.equals(that.hostId) : that.hostId != null)
            return false;

        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null)
            return false;

        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;

        if (user != null ? !user.equals(that.user) : that.user != null)
            return false;

        if (versionTag != null ? !versionTag.equals(that.versionTag) : that.versionTag != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.hashCode() : 0;
        result = (31 * result) + (hostId != null ? hostId.hashCode() : 0);
        result = (31 * result) + (type != null ? type.hashCode() : 0);
        result = (31 * result) + (createTimestamp != null ? createTimestamp.hashCode() : 0);
        result = (31 * result) + (versionTag != null ? versionTag.hashCode() : 0);
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (31 * result) + selected;
        result = (31 * result) + (user != null ? user.hashCode() : 0);
        return result;
    }
}