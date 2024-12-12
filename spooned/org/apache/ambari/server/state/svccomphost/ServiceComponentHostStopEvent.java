package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostStopEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostStopEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOP, serviceComponentName, hostName, opTimestamp);
    }
}