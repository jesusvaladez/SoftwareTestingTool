package org.apache.ambari.server.events;
public class HostComponentsUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    @com.fasterxml.jackson.annotation.JsonProperty("hostComponents")
    private java.util.List<org.apache.ambari.server.events.HostComponentUpdate> hostComponentUpdates = new java.util.ArrayList<>();

    public HostComponentsUpdateEvent(java.util.List<org.apache.ambari.server.events.HostComponentUpdate> hostComponentUpdates) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.HOSTCOMPONENT);
        this.hostComponentUpdates = hostComponentUpdates;
    }

    public java.util.List<org.apache.ambari.server.events.HostComponentUpdate> getHostComponentUpdates() {
        return hostComponentUpdates;
    }

    public void setHostComponentUpdates(java.util.List<org.apache.ambari.server.events.HostComponentUpdate> hostComponentUpdates) {
        this.hostComponentUpdates = hostComponentUpdates;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.HostComponentsUpdateEvent that = ((org.apache.ambari.server.events.HostComponentsUpdateEvent) (o));
        return hostComponentUpdates != null ? hostComponentUpdates.equals(that.hostComponentUpdates) : that.hostComponentUpdates == null;
    }

    @java.lang.Override
    public int hashCode() {
        return hostComponentUpdates != null ? hostComponentUpdates.hashCode() : 0;
    }
}