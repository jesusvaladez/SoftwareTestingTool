package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostInstallEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostInstallEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp, java.lang.String stackId) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, serviceComponentName, hostName, opTimestamp, stackId);
    }
}