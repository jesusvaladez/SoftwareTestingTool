package org.apache.ambari.server.state.action;
public class ActionProgressUpdateEvent extends org.apache.ambari.server.state.action.ActionEvent {
    private final long progressUpdateTime;

    public ActionProgressUpdateEvent(org.apache.ambari.server.state.action.ActionId actionId, long progressUpdateTime) {
        super(org.apache.ambari.server.state.action.ActionEventType.ACTION_IN_PROGRESS, actionId);
        this.progressUpdateTime = progressUpdateTime;
    }

    public long getProgressUpdateTime() {
        return progressUpdateTime;
    }
}