package org.apache.ambari.server.agent.stomp.dto;
public class ComponentVersionReport {
    private java.lang.String componentName;

    private java.lang.String serviceName;

    private java.lang.String version;

    private java.lang.Long clusterId;

    public ComponentVersionReport() {
    }

    public ComponentVersionReport(java.lang.String componentName, java.lang.String serviceName, java.lang.String version, java.lang.Long clusterId) {
        this.componentName = componentName;
        this.serviceName = serviceName;
        this.version = version;
        this.clusterId = clusterId;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }
}