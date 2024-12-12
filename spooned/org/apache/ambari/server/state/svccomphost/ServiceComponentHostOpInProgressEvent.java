package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostOpInProgressEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostOpInProgressEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, serviceComponentName, hostName, opTimestamp);
    }
}