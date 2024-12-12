package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostOpFailedEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostOpFailedEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, serviceComponentName, hostName, opTimestamp);
    }
}