package org.apache.ambari.server.state.action;
public class ActionInitEvent extends org.apache.ambari.server.state.action.ActionEvent {
    private final long startTime;

    public ActionInitEvent(org.apache.ambari.server.state.action.ActionId actionId, long startTime) {
        super(org.apache.ambari.server.state.action.ActionEventType.ACTION_INIT, actionId);
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }
}