package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostOpRestartedEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostOpRestartedEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, serviceComponentName, hostName, opTimestamp);
    }
}