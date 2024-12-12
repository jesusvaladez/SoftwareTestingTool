package org.apache.ambari.server.state.fsm;
public interface SingleArcTransition<OPERAND, EVENT> {
    void transition(OPERAND operand, EVENT event);
}