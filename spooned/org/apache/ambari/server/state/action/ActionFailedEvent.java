package org.apache.ambari.server.state.action;
public class ActionFailedEvent extends org.apache.ambari.server.state.action.ActionEvent {
    private final long completionTime;

    public ActionFailedEvent(org.apache.ambari.server.state.action.ActionId actionId, long completionTime) {
        super(org.apache.ambari.server.state.action.ActionEventType.ACTION_FAILED, actionId);
        this.completionTime = completionTime;
    }

    public long getCompletionTime() {
        return completionTime;
    }
}