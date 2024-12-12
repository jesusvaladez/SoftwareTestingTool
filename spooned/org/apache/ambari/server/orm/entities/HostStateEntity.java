package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import org.apache.commons.lang.StringUtils;
@javax.persistence.Table(name = "hoststate")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "hostStateByHostId", query = "SELECT hostState FROM HostStateEntity hostState " + "WHERE hostState.hostId=:hostId") })
public class HostStateEntity {
    @javax.persistence.Column(name = "host_id", nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private java.lang.Long hostId;

    @javax.persistence.Column(name = "available_mem", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.Long availableMem = 0L;

    @javax.persistence.Column(name = "time_in_state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.Long timeInState = 0L;

    @javax.persistence.Column(name = "health_status", insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String healthStatus;

    @javax.persistence.Column(name = "agent_version", insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String agentVersion = "";

    @javax.persistence.Column(name = "current_state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.HostState currentState = org.apache.ambari.server.state.HostState.INIT;

    @javax.persistence.Column(name = "maintenance_state", nullable = true, insertable = true, updatable = true)
    private java.lang.String maintenanceState = null;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id", nullable = false)
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    public java.lang.Long getAvailableMem() {
        return availableMem;
    }

    public void setAvailableMem(java.lang.Long availableMem) {
        this.availableMem = availableMem;
    }

    public java.lang.Long getTimeInState() {
        return timeInState;
    }

    public void setTimeInState(java.lang.Long timeInState) {
        this.timeInState = timeInState;
    }

    public java.lang.String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(java.lang.String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public java.lang.String getAgentVersion() {
        return org.apache.commons.lang.StringUtils.defaultString(agentVersion);
    }

    public void setAgentVersion(java.lang.String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public org.apache.ambari.server.state.HostState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(org.apache.ambari.server.state.HostState currentState) {
        this.currentState = currentState;
    }

    public java.lang.String getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(java.lang.String state) {
        maintenanceState = state;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.HostStateEntity that = ((org.apache.ambari.server.orm.entities.HostStateEntity) (o));
        return (((java.util.Objects.equals(hostId, that.hostId) && java.util.Objects.equals(availableMem, that.availableMem)) && java.util.Objects.equals(timeInState, that.timeInState)) && java.util.Objects.equals(agentVersion, that.agentVersion)) && (currentState == that.currentState);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(hostId, availableMem, timeInState, agentVersion, currentState);
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
    }
}