package org.apache.ambari.server.state.action;
public class ActionId {
    final long actionId;

    final org.apache.ambari.server.state.action.ActionType actionType;

    public ActionId(long actionId, org.apache.ambari.server.state.action.ActionType actionType) {
        super();
        this.actionId = actionId;
        this.actionType = actionType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("[ actionId=" + actionId) + ", actionType=") + actionType) + "]";
    }
}