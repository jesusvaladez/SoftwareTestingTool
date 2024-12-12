package org.apache.ambari.server.state.action;
public abstract class ActionEvent extends org.apache.ambari.server.state.fsm.event.AbstractEvent<org.apache.ambari.server.state.action.ActionEventType> {
    private final org.apache.ambari.server.state.action.ActionId actionId;

    public ActionEvent(org.apache.ambari.server.state.action.ActionEventType type, org.apache.ambari.server.state.action.ActionId actionId) {
        super(type);
        this.actionId = actionId;
    }

    public org.apache.ambari.server.state.action.ActionId getActionId() {
        return actionId;
    }
}