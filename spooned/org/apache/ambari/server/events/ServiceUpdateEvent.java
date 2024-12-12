package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class ServiceUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    @com.fasterxml.jackson.annotation.JsonProperty("cluster_name")
    private java.lang.String clusterName;

    @com.fasterxml.jackson.annotation.JsonProperty("maintenance_state")
    private org.apache.ambari.server.state.MaintenanceState maintenanceState;

    @com.fasterxml.jackson.annotation.JsonProperty("service_name")
    private java.lang.String serviceName;

    @com.fasterxml.jackson.annotation.JsonProperty("state")
    private org.apache.ambari.server.state.State state;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private boolean stateChanged = false;

    public ServiceUpdateEvent(java.lang.String clusterName, org.apache.ambari.server.state.MaintenanceState maintenanceState, java.lang.String serviceName, org.apache.ambari.server.state.State state, boolean stateChanged) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.SERVICE);
        this.clusterName = clusterName;
        this.maintenanceState = maintenanceState;
        this.serviceName = serviceName;
        this.state = state;
        this.stateChanged = stateChanged;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public org.apache.ambari.server.state.State getState() {
        return state;
    }

    public void setState(org.apache.ambari.server.state.State state) {
        this.state = state;
    }

    public boolean isStateChanged() {
        return stateChanged;
    }

    public void setStateChanged(boolean stateChanged) {
        this.stateChanged = stateChanged;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.ServiceUpdateEvent that = ((org.apache.ambari.server.events.ServiceUpdateEvent) (o));
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null)
            return false;

        return serviceName != null ? serviceName.equals(that.serviceName) : that.serviceName == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterName != null) ? clusterName.hashCode() : 0;
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        return result;
    }
}