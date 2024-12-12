package org.apache.ambari.server.state.fsm;
@java.lang.SuppressWarnings("serial")
public class InvalidStateTransitionException extends java.lang.Exception {
    private java.lang.Enum<?> currentState;

    private java.lang.Enum<?> event;

    public InvalidStateTransitionException(java.lang.Enum<?> currentState, java.lang.Enum<?> event) {
        super((("Invalid event: " + event) + " at ") + currentState);
        this.currentState = currentState;
        this.event = event;
    }

    public java.lang.Enum<?> getCurrentState() {
        return currentState;
    }

    public java.lang.Enum<?> getEvent() {
        return event;
    }
}