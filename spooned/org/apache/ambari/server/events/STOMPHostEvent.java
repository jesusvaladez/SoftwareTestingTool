package org.apache.ambari.server.events;
public abstract class STOMPHostEvent extends org.apache.ambari.server.events.STOMPEvent {
    @java.beans.Transient
    public abstract java.lang.Long getHostId();

    public STOMPHostEvent(org.apache.ambari.server.events.STOMPEvent.Type type) {
        super(type);
    }
}