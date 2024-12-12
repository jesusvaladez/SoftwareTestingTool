package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class ServiceNotFoundException extends org.apache.ambari.server.ObjectNotFoundException {
    public ServiceNotFoundException(java.lang.String clusterName, java.lang.String serviceName) {
        super(((("Service not found" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName);
    }
}