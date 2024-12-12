package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostStartEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostStartEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_START, serviceComponentName, hostName, opTimestamp);
    }
}