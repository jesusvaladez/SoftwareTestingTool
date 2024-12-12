package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class ServiceComponentHostNotFoundException extends org.apache.ambari.server.ObjectNotFoundException {
    public ServiceComponentHostNotFoundException(java.lang.String clusterName, java.lang.String serviceName, java.lang.String serviceComponentName, java.lang.String hostName) {
        super(((((((("ServiceComponentHost not found" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName) + ", serviceComponentName=") + serviceComponentName) + ", hostName=") + hostName);
    }
}