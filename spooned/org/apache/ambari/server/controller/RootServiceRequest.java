package org.apache.ambari.server.controller;
public class RootServiceRequest {
    private java.lang.String serviceName;

    public RootServiceRequest(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }
}