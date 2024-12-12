package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class HostUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    @com.fasterxml.jackson.annotation.JsonProperty("cluster_name")
    private java.lang.String clusterName;

    @com.fasterxml.jackson.annotation.JsonProperty("host_name")
    private java.lang.String hostName;

    @com.fasterxml.jackson.annotation.JsonProperty("host_status")
    private java.lang.String hostStatus;

    @com.fasterxml.jackson.annotation.JsonProperty("host_state")
    private org.apache.ambari.server.state.HostState hostState;

    @com.fasterxml.jackson.annotation.JsonProperty("last_heartbeat_time")
    private java.lang.Long lastHeartbeatTime;

    @com.fasterxml.jackson.annotation.JsonProperty("maintenance_state")
    private org.apache.ambari.server.state.MaintenanceState maintenanceState;

    @com.fasterxml.jackson.annotation.JsonProperty("alerts_summary")
    private org.apache.ambari.server.orm.dao.AlertSummaryDTO alertsSummary;

    public HostUpdateEvent(java.lang.String clusterName, java.lang.String hostName, java.lang.String hostStatus, org.apache.ambari.server.state.HostState hostState, java.lang.Long lastHeartbeatTime, org.apache.ambari.server.state.MaintenanceState maintenanceState, org.apache.ambari.server.orm.dao.AlertSummaryDTO alertsSummary) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.HOST);
        this.clusterName = clusterName;
        this.hostName = hostName;
        this.hostStatus = hostStatus;
        this.hostState = hostState;
        this.lastHeartbeatTime = lastHeartbeatTime;
        this.maintenanceState = maintenanceState;
        this.alertsSummary = alertsSummary;
    }

    public static org.apache.ambari.server.events.HostUpdateEvent createHostStatusUpdate(java.lang.String clusterName, java.lang.String hostName, java.lang.String hostStatus, java.lang.Long lastHeartbeatTime) {
        return new org.apache.ambari.server.events.HostUpdateEvent(clusterName, hostName, hostStatus, null, lastHeartbeatTime, null, null);
    }

    public static org.apache.ambari.server.events.HostUpdateEvent createHostStateUpdate(java.lang.String clusterName, java.lang.String hostName, org.apache.ambari.server.state.HostState hostState, java.lang.Long lastHeartbeatTime) {
        return new org.apache.ambari.server.events.HostUpdateEvent(clusterName, hostName, null, hostState, lastHeartbeatTime, null, null);
    }

    public static org.apache.ambari.server.events.HostUpdateEvent createHostMaintenanceStatusUpdate(java.lang.String clusterName, java.lang.String hostName, org.apache.ambari.server.state.MaintenanceState maintenanceState, org.apache.ambari.server.orm.dao.AlertSummaryDTO alertsSummary) {
        return new org.apache.ambari.server.events.HostUpdateEvent(clusterName, hostName, null, null, null, maintenanceState, alertsSummary);
    }

    public static org.apache.ambari.server.events.HostUpdateEvent createHostAlertsUpdate(java.lang.String clusterName, java.lang.String hostName, org.apache.ambari.server.orm.dao.AlertSummaryDTO alertsSummary) {
        return new org.apache.ambari.server.events.HostUpdateEvent(clusterName, hostName, null, null, null, null, alertsSummary);
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public java.lang.String getHostStatus() {
        return hostStatus;
    }

    public void setHostStatus(java.lang.String hostStatus) {
        this.hostStatus = hostStatus;
    }

    public java.lang.Long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(java.lang.Long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public org.apache.ambari.server.orm.dao.AlertSummaryDTO getAlertsSummary() {
        return alertsSummary;
    }

    public void setAlertsSummary(org.apache.ambari.server.orm.dao.AlertSummaryDTO alertsSummary) {
        this.alertsSummary = alertsSummary;
    }

    public org.apache.ambari.server.state.HostState getHostState() {
        return hostState;
    }

    public void setHostState(org.apache.ambari.server.state.HostState hostState) {
        this.hostState = hostState;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.HostUpdateEvent that = ((org.apache.ambari.server.events.HostUpdateEvent) (o));
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null)
            return false;

        if (hostName != null ? !hostName.equals(that.hostName) : that.hostName != null)
            return false;

        if (hostStatus != null ? !hostStatus.equals(that.hostStatus) : that.hostStatus != null)
            return false;

        if (hostState != that.hostState)
            return false;

        if (lastHeartbeatTime != null ? !lastHeartbeatTime.equals(that.lastHeartbeatTime) : that.lastHeartbeatTime != null)
            return false;

        if (maintenanceState != that.maintenanceState)
            return false;

        return alertsSummary != null ? alertsSummary.equals(that.alertsSummary) : that.alertsSummary == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterName != null) ? clusterName.hashCode() : 0;
        result = (31 * result) + (hostName != null ? hostName.hashCode() : 0);
        result = (31 * result) + (hostStatus != null ? hostStatus.hashCode() : 0);
        result = (31 * result) + (hostState != null ? hostState.hashCode() : 0);
        result = (31 * result) + (lastHeartbeatTime != null ? lastHeartbeatTime.hashCode() : 0);
        result = (31 * result) + (maintenanceState != null ? maintenanceState.hashCode() : 0);
        result = (31 * result) + (alertsSummary != null ? alertsSummary.hashCode() : 0);
        return result;
    }
}