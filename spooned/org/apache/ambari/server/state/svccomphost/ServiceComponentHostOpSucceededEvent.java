package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostOpSucceededEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostOpSucceededEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, serviceComponentName, hostName, opTimestamp);
    }
}