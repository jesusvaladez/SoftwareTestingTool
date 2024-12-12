package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class ServiceComponentNotFoundException extends org.apache.ambari.server.ObjectNotFoundException {
    public ServiceComponentNotFoundException(java.lang.String clusterName, java.lang.String serviceName, java.lang.String serviceComponentName) {
        super(((((("ServiceComponent not found" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName) + ", serviceComponentName=") + serviceComponentName);
    }
}