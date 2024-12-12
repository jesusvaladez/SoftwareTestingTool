package org.apache.ambari.server.state.fsm;
public interface MultipleArcTransition<OPERAND, EVENT, STATE extends java.lang.Enum<STATE>> {
    STATE transition(OPERAND operand, EVENT event);
}