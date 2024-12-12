package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "hostcomponentstate")
@javax.persistence.TableGenerator(name = "hostcomponentstate_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "hostcomponentstate_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "HostComponentStateEntity.findAll", query = "SELECT hcs from HostComponentStateEntity hcs"), @javax.persistence.NamedQuery(name = "HostComponentStateEntity.findByHost", query = "SELECT hcs from HostComponentStateEntity hcs WHERE hcs.hostEntity.hostName=:hostName"), @javax.persistence.NamedQuery(name = "HostComponentStateEntity.findByService", query = "SELECT hcs from HostComponentStateEntity hcs WHERE hcs.serviceName=:serviceName"), @javax.persistence.NamedQuery(name = "HostComponentStateEntity.findByServiceAndComponent", query = "SELECT hcs from HostComponentStateEntity hcs WHERE hcs.serviceName=:serviceName AND hcs.componentName=:componentName"), @javax.persistence.NamedQuery(name = "HostComponentStateEntity.findByServiceComponentAndHost", query = "SELECT hcs from HostComponentStateEntity hcs WHERE hcs.serviceName=:serviceName AND hcs.componentName=:componentName AND hcs.hostEntity.hostName=:hostName"), @javax.persistence.NamedQuery(name = "HostComponentStateEntity.findByIndex", query = "SELECT hcs from HostComponentStateEntity hcs WHERE hcs.clusterId=:clusterId AND hcs.serviceName=:serviceName AND hcs.componentName=:componentName AND hcs.hostId=:hostId"), @javax.persistence.NamedQuery(name = "HostComponentStateEntity.findByServiceAndComponentAndNotVersion", query = "SELECT hcs from HostComponentStateEntity hcs WHERE hcs.serviceName=:serviceName AND hcs.componentName=:componentName AND hcs.version != :version") })
public class HostComponentStateEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "hostcomponentstate_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "service_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "host_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long hostId;

    @javax.persistence.Column(name = "component_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String componentName;

    @javax.persistence.Column(name = "version", nullable = false, insertable = true, updatable = true)
    private java.lang.String version = org.apache.ambari.server.state.State.UNKNOWN.toString();

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "current_state", nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.state.State currentState = org.apache.ambari.server.state.State.INIT;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "upgrade_state", nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.state.UpgradeState upgradeState = org.apache.ambari.server.state.UpgradeState.NONE;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false), @javax.persistence.JoinColumn(name = "service_name", referencedColumnName = "service_name", nullable = false), @javax.persistence.JoinColumn(name = "component_name", referencedColumnName = "component_name", nullable = false) })
    private org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id", nullable = false)
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    public java.lang.Long getId() {
        return id;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getHostName() {
        return hostEntity.getHostName();
    }

    public java.lang.Long getHostId() {
        return hostEntity != null ? hostEntity.getHostId() : null;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.state.State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(org.apache.ambari.server.state.State currentState) {
        this.currentState = currentState;
    }

    public org.apache.ambari.server.state.UpgradeState getUpgradeState() {
        return upgradeState;
    }

    public void setUpgradeState(org.apache.ambari.server.state.UpgradeState upgradeState) {
        this.upgradeState = upgradeState;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.HostComponentStateEntity that = ((org.apache.ambari.server.orm.entities.HostComponentStateEntity) (o));
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null) {
            return false;
        }
        if (componentName != null ? !componentName.equals(that.componentName) : that.componentName != null) {
            return false;
        }
        if (currentState != null ? !currentState.equals(that.currentState) : that.currentState != null) {
            return false;
        }
        if (upgradeState != null ? !upgradeState.equals(that.upgradeState) : that.upgradeState != null) {
            return false;
        }
        if (hostEntity != null ? !hostEntity.equals(that.hostEntity) : that.hostEntity != null) {
            return false;
        }
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) {
            return false;
        }
        if (version != null ? !version.equals(that.version) : that.version != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.intValue() : 0;
        result = (31 * result) + (clusterId != null ? clusterId.intValue() : 0);
        result = (31 * result) + (hostEntity != null ? hostEntity.hashCode() : 0);
        result = (31 * result) + (componentName != null ? componentName.hashCode() : 0);
        result = (31 * result) + (currentState != null ? currentState.hashCode() : 0);
        result = (31 * result) + (upgradeState != null ? upgradeState.hashCode() : 0);
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (31 * result) + (version != null ? version.hashCode() : 0);
        return result;
    }

    public org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity getServiceComponentDesiredStateEntity() {
        return serviceComponentDesiredStateEntity;
    }

    public void setServiceComponentDesiredStateEntity(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        this.serviceComponentDesiredStateEntity = serviceComponentDesiredStateEntity;
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("serviceName", serviceName).add("componentName", componentName).add("hostId", hostId).add("state", currentState).toString();
    }
}