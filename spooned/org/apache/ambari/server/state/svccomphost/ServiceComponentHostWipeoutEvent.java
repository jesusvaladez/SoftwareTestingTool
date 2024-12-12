package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostWipeoutEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostWipeoutEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_WIPEOUT, serviceComponentName, hostName, opTimestamp);
    }
}