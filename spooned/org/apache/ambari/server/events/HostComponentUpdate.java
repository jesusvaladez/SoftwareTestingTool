package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class HostComponentUpdate {
    private java.lang.Long clusterId;

    private java.lang.String serviceName;

    private java.lang.String hostName;

    private java.lang.String componentName;

    private org.apache.ambari.server.state.State currentState;

    private org.apache.ambari.server.state.State previousState;

    private org.apache.ambari.server.state.MaintenanceState maintenanceState;

    private java.lang.Boolean staleConfigs;

    private HostComponentUpdate(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String hostName, java.lang.String componentName, org.apache.ambari.server.state.State currentState, org.apache.ambari.server.state.State previousState, org.apache.ambari.server.state.MaintenanceState maintenanceState, java.lang.Boolean staleConfigs) {
        this.clusterId = clusterId;
        this.serviceName = serviceName;
        this.hostName = hostName;
        this.componentName = componentName;
        this.currentState = currentState;
        this.previousState = previousState;
        this.maintenanceState = maintenanceState;
        this.staleConfigs = staleConfigs;
    }

    public static org.apache.ambari.server.events.HostComponentUpdate createHostComponentStatusUpdate(org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity, org.apache.ambari.server.state.State previousState) {
        org.apache.ambari.server.events.HostComponentUpdate hostComponentUpdate = new org.apache.ambari.server.events.HostComponentUpdate(stateEntity.getClusterId(), stateEntity.getServiceName(), stateEntity.getHostEntity().getHostName(), stateEntity.getComponentName(), stateEntity.getCurrentState(), previousState, null, null);
        return hostComponentUpdate;
    }

    public static org.apache.ambari.server.events.HostComponentUpdate createHostComponentMaintenanceStatusUpdate(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String hostName, java.lang.String componentName, org.apache.ambari.server.state.MaintenanceState maintenanceState) {
        org.apache.ambari.server.events.HostComponentUpdate hostComponentUpdate = new org.apache.ambari.server.events.HostComponentUpdate(clusterId, serviceName, hostName, componentName, null, null, maintenanceState, null);
        return hostComponentUpdate;
    }

    public static org.apache.ambari.server.events.HostComponentUpdate createHostComponentStaleConfigsStatusUpdate(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String hostName, java.lang.String componentName, java.lang.Boolean isStaleConfig) {
        org.apache.ambari.server.events.HostComponentUpdate hostComponentUpdate = new org.apache.ambari.server.events.HostComponentUpdate(clusterId, serviceName, hostName, componentName, null, null, null, isStaleConfig);
        return hostComponentUpdate;
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
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
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

    public org.apache.ambari.server.state.State getPreviousState() {
        return previousState;
    }

    public void setPreviousState(org.apache.ambari.server.state.State previousState) {
        this.previousState = previousState;
    }

    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public java.lang.Boolean getStaleConfigs() {
        return staleConfigs;
    }

    public void setStaleConfigs(java.lang.Boolean staleConfigs) {
        this.staleConfigs = staleConfigs;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.HostComponentUpdate that = ((org.apache.ambari.server.events.HostComponentUpdate) (o));
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null)
            return false;

        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null)
            return false;

        if (hostName != null ? !hostName.equals(that.hostName) : that.hostName != null)
            return false;

        if (componentName != null ? !componentName.equals(that.componentName) : that.componentName != null)
            return false;

        if (currentState != that.currentState)
            return false;

        if (previousState != that.previousState)
            return false;

        if (maintenanceState != that.maintenanceState)
            return false;

        return staleConfigs != null ? staleConfigs.equals(that.staleConfigs) : that.staleConfigs == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.hashCode() : 0;
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (31 * result) + (hostName != null ? hostName.hashCode() : 0);
        result = (31 * result) + (componentName != null ? componentName.hashCode() : 0);
        result = (31 * result) + (currentState != null ? currentState.hashCode() : 0);
        result = (31 * result) + (previousState != null ? previousState.hashCode() : 0);
        result = (31 * result) + (maintenanceState != null ? maintenanceState.hashCode() : 0);
        result = (31 * result) + (staleConfigs != null ? staleConfigs.hashCode() : 0);
        return result;
    }
}