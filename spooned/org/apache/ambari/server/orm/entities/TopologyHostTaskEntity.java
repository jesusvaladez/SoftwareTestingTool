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
@javax.persistence.Entity
@javax.persistence.Table(name = "topology_host_task")
@javax.persistence.TableGenerator(name = "topology_host_task_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "topology_host_task_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "TopologyHostTaskEntity.findByHostRequest", query = "SELECT req FROM TopologyHostTaskEntity req WHERE req.topologyHostRequestEntity.id = :hostRequestId"), @javax.persistence.NamedQuery(name = "TopologyLogicalTaskEntity.findHostRequestIdsByHostTaskIds", query = "SELECT DISTINCT tht.hostRequestId from TopologyHostTaskEntity tht WHERE tht.id IN :hostTaskIds"), @javax.persistence.NamedQuery(name = "TopologyHostTaskEntity.removeByTaskIds", query = "DELETE FROM TopologyHostTaskEntity tht WHERE tht.id IN :hostTaskIds") })
public class TopologyHostTaskEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "topology_host_task_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "type", length = 255, nullable = false)
    private java.lang.String type;

    @javax.persistence.Column(name = "host_request_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long hostRequestId;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "host_request_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.TopologyHostRequestEntity topologyHostRequestEntity;

    @javax.persistence.OneToMany(mappedBy = "topologyHostTaskEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity> topologyLogicalTaskEntities;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getHostRequestId() {
        return hostRequestId;
    }

    public void setHostRequestId(java.lang.Long hostRequestId) {
        this.hostRequestId = hostRequestId;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public org.apache.ambari.server.orm.entities.TopologyHostRequestEntity getTopologyHostRequestEntity() {
        return topologyHostRequestEntity;
    }

    public void setTopologyHostRequestEntity(org.apache.ambari.server.orm.entities.TopologyHostRequestEntity topologyHostRequestEntity) {
        this.topologyHostRequestEntity = topologyHostRequestEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity> getTopologyLogicalTaskEntities() {
        return topologyLogicalTaskEntities;
    }

    public void setTopologyLogicalTaskEntities(java.util.Collection<org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity> topologyLogicalTaskEntities) {
        this.topologyLogicalTaskEntities = topologyLogicalTaskEntities;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.TopologyHostTaskEntity that = ((org.apache.ambari.server.orm.entities.TopologyHostTaskEntity) (o));
        if (!id.equals(that.id))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }
}