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
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntityPK.class)
@javax.persistence.Entity
@javax.persistence.Table(name = "configgrouphostmapping")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "groupsByHost", query = "SELECT confighosts FROM ConfigGroupHostMappingEntity confighosts " + "WHERE confighosts.hostEntity.hostName=:hostname"), @javax.persistence.NamedQuery(name = "hostsByGroup", query = "SELECT confighosts FROM ConfigGroupHostMappingEntity confighosts " + "WHERE confighosts.configGroupId=:groupId") })
public class ConfigGroupHostMappingEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "config_group_id", nullable = false, insertable = true, updatable = true)
    private java.lang.Long configGroupId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "host_id", nullable = false, insertable = true, updatable = true)
    private java.lang.Long hostId;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id", nullable = false, insertable = false, updatable = false) })
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "config_group_id", referencedColumnName = "group_id", nullable = false, insertable = false, updatable = false) })
    private org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity;

    public java.lang.Long getConfigGroupId() {
        return configGroupId;
    }

    public void setConfigGroupId(java.lang.Long configGroupId) {
        this.configGroupId = configGroupId;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    public java.lang.String getHostname() {
        return hostEntity != null ? hostEntity.getHostName() : null;
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
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

        org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity that = ((org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity) (o));
        if (!configGroupId.equals(that.configGroupId))
            return false;

        if (!hostEntity.equals(that.hostEntity))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = configGroupId.hashCode();
        result = (31 * result) + hostEntity.hashCode();
        return result;
    }
}