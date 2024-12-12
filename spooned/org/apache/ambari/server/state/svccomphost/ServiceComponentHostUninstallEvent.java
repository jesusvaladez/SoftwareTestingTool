package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostUninstallEvent extends org.apache.ambari.server.state.ServiceComponentHostEvent {
    public ServiceComponentHostUninstallEvent(java.lang.String serviceComponentName, java.lang.String hostName, long opTimestamp) {
        super(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UNINSTALL, serviceComponentName, hostName, opTimestamp);
    }
}