package org.apache.ambari.server.controller;
public class ServiceComponentHostRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String componentName;

    private java.lang.String hostname;

    private java.lang.String publicHostname;

    private java.lang.String state;

    private java.lang.String desiredState;

    private java.lang.String desiredStackId;

    private java.lang.String staleConfig;

    private java.lang.String adminState;

    private java.lang.String maintenanceState;

    public ServiceComponentHostRequest(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, java.lang.String desiredState) {
        super();
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.hostname = hostname;
        this.desiredState = desiredState;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getHostname() {
        return hostname;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    public java.lang.String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(java.lang.String desiredState) {
        this.desiredState = desiredState;
    }

    public java.lang.String getState() {
        return state;
    }

    public void setState(java.lang.String state) {
        this.state = state;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public void setStaleConfig(java.lang.String staleConfig) {
        this.staleConfig = staleConfig;
    }

    public java.lang.String getStaleConfig() {
        return staleConfig;
    }

    public void setAdminState(java.lang.String adminState) {
        this.adminState = adminState;
    }

    public java.lang.String getAdminState() {
        return adminState;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("{" + " clusterName=").append(clusterName).append(", serviceName=").append(serviceName).append(", componentName=").append(componentName).append(", hostname=").append(hostname).append(", publicHostname=").append(publicHostname).append(", desiredState=").append(desiredState).append(", state=").append(state).append(", desiredStackId=").append(desiredStackId).append(", staleConfig=").append(staleConfig).append(", adminState=").append(adminState).append(", maintenanceState=").append(maintenanceState).append("}");
        return sb.toString();
    }

    public void setMaintenanceState(java.lang.String state) {
        maintenanceState = state;
    }

    public java.lang.String getMaintenanceState() {
        return maintenanceState;
    }

    public java.lang.String getPublicHostname() {
        return publicHostname;
    }

    public void setPublicHostname(java.lang.String publicHostname) {
        this.publicHostname = publicHostname;
    }
}