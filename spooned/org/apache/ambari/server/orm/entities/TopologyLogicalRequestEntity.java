package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@javax.persistence.Entity
@javax.persistence.Table(name = "topology_logical_request")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "TopologyLogicalRequestEntity.findRequestIds", query = "SELECT DISTINCT t.topologyLogicalRequestEntity.topologyRequestId from TopologyHostRequestEntity t WHERE t.id IN :ids") })
public class TopologyLogicalRequestEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "request_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long topologyRequestId;

    @javax.persistence.Column(name = "description", length = 1024, nullable = false)
    private java.lang.String description;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "request_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity;

    @javax.persistence.OneToMany(mappedBy = "topologyLogicalRequestEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> topologyHostRequestEntities;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getTopologyRequestId() {
        return topologyRequestId;
    }

    public void setTopologyRequestId(java.lang.Long topologyRequestId) {
        this.topologyRequestId = topologyRequestId;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public org.apache.ambari.server.orm.entities.TopologyRequestEntity getTopologyRequestEntity() {
        return topologyRequestEntity;
    }

    public void setTopologyRequestEntity(org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity) {
        this.topologyRequestEntity = topologyRequestEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> getTopologyHostRequestEntities() {
        return topologyHostRequestEntities;
    }

    public void setTopologyHostRequestEntities(java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> topologyHostRequestEntities) {
        this.topologyHostRequestEntities = topologyHostRequestEntities;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity that = ((org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity) (o));
        if (!id.equals(that.id))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }
}