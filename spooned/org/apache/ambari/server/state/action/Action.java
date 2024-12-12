package org.apache.ambari.server.state.action;
public interface Action {
    org.apache.ambari.server.state.action.ActionId getId();

    long getStartTime();

    long getLastUpdateTime();

    long getCompletionTime();

    org.apache.ambari.server.state.action.ActionState getState();

    void setState(org.apache.ambari.server.state.action.ActionState state);

    void handleEvent(org.apache.ambari.server.state.action.ActionEvent event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException;
}