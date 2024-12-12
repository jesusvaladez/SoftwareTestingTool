package org.apache.ambari.server.state.fsm.event;
public abstract class AbstractEvent<TYPE extends java.lang.Enum<TYPE>> implements org.apache.ambari.server.state.fsm.event.Event<TYPE> {
    private final TYPE type;

    private final long timestamp;

    public AbstractEvent(TYPE type) {
        this.type = type;
        timestamp = -1L;
    }

    public AbstractEvent(TYPE type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    @java.lang.Override
    public long getTimestamp() {
        return timestamp;
    }

    @java.lang.Override
    public TYPE getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "EventType: " + getType();
    }
}