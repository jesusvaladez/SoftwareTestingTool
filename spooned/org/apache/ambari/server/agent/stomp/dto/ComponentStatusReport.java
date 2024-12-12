package org.apache.ambari.server.agent.stomp.dto;
public class ComponentStatusReport {
    private java.lang.String componentName;

    private org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport.CommandStatusCommand command;

    private java.lang.String status;

    private java.lang.String serviceName;

    private java.lang.Long clusterId;

    public ComponentStatusReport() {
    }

    public ComponentStatusReport(java.lang.String componentName, org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport.CommandStatusCommand command, java.lang.String status, java.lang.String serviceName, java.lang.Long clusterId) {
        this.componentName = componentName;
        this.command = command;
        this.status = status;
        this.serviceName = serviceName;
        this.clusterId = clusterId;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport.CommandStatusCommand getCommand() {
        return command;
    }

    public void setCommand(org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport.CommandStatusCommand command) {
        this.command = command;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public enum CommandStatusCommand {

        STATUS;}
}