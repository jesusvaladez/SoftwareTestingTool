package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostStoppedEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostStoppedEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOPPED, serviceComponentName, hostName, opTimestamp);
    }
}