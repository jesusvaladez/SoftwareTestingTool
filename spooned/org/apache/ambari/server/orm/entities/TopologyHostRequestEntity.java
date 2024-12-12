package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@javax.persistence.Entity
@javax.persistence.Table(name = "topology_host_request")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "TopologyHostRequestEntity.removeByIds", query = "DELETE FROM TopologyHostRequestEntity topologyHostRequest WHERE topologyHostRequest.id IN :hostRequestIds") })
public class TopologyHostRequestEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "stage_id", length = 10, nullable = false)
    private java.lang.Long stageId;

    @javax.persistence.Column(name = "host_name", length = 255)
    private java.lang.String hostName;

    @javax.persistence.Column(name = "status")
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.actionmanager.HostRoleStatus status;

    @javax.persistence.Column(name = "status_message")
    private java.lang.String statusMessage;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "logical_request_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity topologyLogicalRequestEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.TopologyHostGroupEntity topologyHostGroupEntity;

    @javax.persistence.OneToMany(mappedBy = "topologyHostRequestEntity", cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
    private java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> topologyHostTaskEntities;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getLogicalRequestId() {
        return topologyLogicalRequestEntity != null ? topologyLogicalRequestEntity.getTopologyRequestId() : null;
    }

    public java.lang.Long getHostGroupId() {
        return topologyHostGroupEntity.getId();
    }

    public java.lang.Long getStageId() {
        return stageId;
    }

    public void setStageId(java.lang.Long stageId) {
        this.stageId = stageId;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    public java.lang.String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(java.lang.String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity getTopologyLogicalRequestEntity() {
        return topologyLogicalRequestEntity;
    }

    public void setTopologyLogicalRequestEntity(org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity topologyLogicalRequestEntity) {
        this.topologyLogicalRequestEntity = topologyLogicalRequestEntity;
    }

    public org.apache.ambari.server.orm.entities.TopologyHostGroupEntity getTopologyHostGroupEntity() {
        return topologyHostGroupEntity;
    }

    public void setTopologyHostGroupEntity(org.apache.ambari.server.orm.entities.TopologyHostGroupEntity topologyHostGroupEntity) {
        this.topologyHostGroupEntity = topologyHostGroupEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> getTopologyHostTaskEntities() {
        return topologyHostTaskEntities;
    }

    public void setTopologyHostTaskEntities(java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> topologyHostTaskEntities) {
        this.topologyHostTaskEntities = topologyHostTaskEntities;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity that = ((org.apache.ambari.server.orm.entities.TopologyHostRequestEntity) (o));
        if (!id.equals(that.id))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }
}