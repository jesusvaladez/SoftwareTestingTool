package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "topology_logical_task")
@javax.persistence.TableGenerator(name = "topology_logical_task_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "topology_logical_task_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "TopologyLogicalTaskEntity.findHostTaskIdsByPhysicalTaskIds", query = "SELECT DISTINCT logicaltask.hostTaskId from TopologyLogicalTaskEntity logicaltask WHERE logicaltask.physicalTaskId IN :physicalTaskIds"), @javax.persistence.NamedQuery(name = "TopologyLogicalTaskEntity.removeByPhysicalTaskIds", query = "DELETE FROM TopologyLogicalTaskEntity logicaltask WHERE logicaltask.physicalTaskId IN :taskIds") })
public class TopologyLogicalTaskEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "topology_logical_task_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "component", length = 255)
    private java.lang.String componentName;

    @javax.persistence.Column(name = "host_task_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long hostTaskId;

    @javax.persistence.Column(name = "physical_task_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long physicalTaskId;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "host_task_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.TopologyHostTaskEntity topologyHostTaskEntity;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "physical_task_id", referencedColumnName = "task_id", nullable = false)
    private org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getPhysicalTaskId() {
        return hostRoleCommandEntity != null ? hostRoleCommandEntity.getTaskId() : null;
    }

    public void setPhysicalTaskId(java.lang.Long physicalTaskId) {
        this.physicalTaskId = physicalTaskId;
    }

    public void setHostTaskId(java.lang.Long hostTaskId) {
        this.hostTaskId = hostTaskId;
    }

    public java.lang.Long getHostTaskId() {
        return hostTaskId;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.orm.entities.TopologyHostTaskEntity getTopologyHostTaskEntity() {
        return topologyHostTaskEntity;
    }

    public void setTopologyHostTaskEntity(org.apache.ambari.server.orm.entities.TopologyHostTaskEntity topologyHostTaskEntity) {
        this.topologyHostTaskEntity = topologyHostTaskEntity;
    }

    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity getHostRoleCommandEntity() {
        return hostRoleCommandEntity;
    }

    public void setHostRoleCommandEntity(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity) {
        this.hostRoleCommandEntity = hostRoleCommandEntity;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity that = ((org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity) (o));
        if (!id.equals(that.id))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }
}