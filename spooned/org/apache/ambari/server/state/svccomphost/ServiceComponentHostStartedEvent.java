package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostStartedEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostStartedEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STARTED, serviceComponentName, hostName, opTimestamp);
    }
}