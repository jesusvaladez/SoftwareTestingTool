package org.apache.ambari.server.state.action;
public class ActionCompletedEvent extends org.apache.ambari.server.state.action.ActionEvent {
    private final long completionTime;

    public ActionCompletedEvent(org.apache.ambari.server.state.action.ActionId actionId, long completionTime) {
        super(org.apache.ambari.server.state.action.ActionEventType.ACTION_COMPLETED, actionId);
        this.completionTime = completionTime;
    }

    public long getCompletionTime() {
        return completionTime;
    }
}