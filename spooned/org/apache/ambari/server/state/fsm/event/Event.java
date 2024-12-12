package org.apache.ambari.server.state.fsm.event;
public interface Event<TYPE extends java.lang.Enum<TYPE>> {
    TYPE getType();

    long getTimestamp();

    java.lang.String toString();
}