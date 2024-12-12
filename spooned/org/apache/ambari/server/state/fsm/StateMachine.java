package org.apache.ambari.server.state.fsm;
public interface StateMachine<STATE extends java.lang.Enum<STATE>, EVENTTYPE extends java.lang.Enum<EVENTTYPE>, EVENT> {
    STATE getCurrentState();

    void setCurrentState(STATE state);

    STATE doTransition(EVENTTYPE eventType, EVENT event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException;
}