package org.apache.ambari.server.controller;
public class RootServiceComponentRequest extends org.apache.ambari.server.controller.RootServiceRequest {
    private java.lang.String componentName;

    public RootServiceComponentRequest(java.lang.String serviceName, java.lang.String componentName) {
        super(serviceName);
        this.componentName = componentName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }
}