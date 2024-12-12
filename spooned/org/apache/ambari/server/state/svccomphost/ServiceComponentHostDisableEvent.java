package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostDisableEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostDisableEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_DISABLE, serviceComponentName, hostName, opTimestamp);
    }
}