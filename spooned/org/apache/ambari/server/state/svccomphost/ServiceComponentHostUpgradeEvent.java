package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostUpgradeEvent extends org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent {
    public ServiceComponentHostUpgradeEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp, java.lang.String stackId) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UPGRADE, serviceComponentName, hostName, opTimestamp, stackId);
    }
}