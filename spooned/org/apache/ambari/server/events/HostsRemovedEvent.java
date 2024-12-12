package org.apache.ambari.server.events;
public class HostsRemovedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final java.util.Set<java.lang.Long> hostIds;

    private final java.util.Set<java.lang.String> hosts;

    public HostsRemovedEvent(java.util.Set<java.lang.String> hosts, java.util.Set<java.lang.Long> hostIds) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.HOST_REMOVED);
        this.hostIds = (hostIds != null) ? hostIds : java.util.Collections.emptySet();
        this.hosts = (hosts != null) ? hosts : java.util.Collections.emptySet();
    }

    public java.util.Set<java.lang.String> getHostNames() {
        return hosts;
    }

    public java.util.Set<java.lang.Long> getHostIds() {
        return hostIds;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("HostsRemovedEvent{" + hosts) + "}";
    }
}