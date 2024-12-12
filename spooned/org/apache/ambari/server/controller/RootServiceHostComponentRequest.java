package org.apache.ambari.server.controller;
public class RootServiceHostComponentRequest extends org.apache.ambari.server.controller.RootServiceComponentRequest {
    private java.lang.String hostName;

    public RootServiceHostComponentRequest(java.lang.String serviceName, java.lang.String hostName, java.lang.String componentName) {
        super(serviceName, componentName);
        this.hostName = hostName;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }
}