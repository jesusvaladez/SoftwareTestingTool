package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostRestoreEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostRestoreEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_RESTORE, serviceComponentName, hostName, opTimestamp);
    }
}