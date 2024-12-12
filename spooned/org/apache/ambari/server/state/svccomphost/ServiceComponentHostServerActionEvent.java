package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostServerActionEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    protected ServiceComponentHostServerActionEvent(org.apache.ambari.server.state.ServiceComponentHostEventType type, java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp, java.lang.String stackId) {
        super(type, serviceComponentName, hostName, opTimestamp, stackId);
    }

    public ServiceComponentHostServerActionEvent(java.lang.String hostName, long opTimestamp) {
        this("AMBARI_SERVER", hostName, opTimestamp);
    }

    public ServiceComponentHostServerActionEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        this(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_SERVER_ACTION, serviceComponentName, hostName, opTimestamp, "");
    }
}