package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class AlertGroupsUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    @com.fasterxml.jackson.annotation.JsonProperty("groups")
    private java.util.List<org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate> groups;

    @com.fasterxml.jackson.annotation.JsonProperty("updateType")
    private org.apache.ambari.server.events.UpdateEventType type;

    public AlertGroupsUpdateEvent(java.util.List<org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate> groups, org.apache.ambari.server.events.UpdateEventType type) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.ALERT_GROUP);
        this.groups = groups;
        this.type = type;
    }

    public static org.apache.ambari.server.events.AlertGroupsUpdateEvent deleteAlertGroupsUpdateEvent(java.util.List<java.lang.Long> alertGroupIdsToDelete) {
        java.util.List<org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate> alertGroupUpdates = new java.util.ArrayList<>(alertGroupIdsToDelete.size());
        for (java.lang.Long alertGroupIdToDelete : alertGroupIdsToDelete) {
            alertGroupUpdates.add(new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(alertGroupIdToDelete));
        }
        return new org.apache.ambari.server.events.AlertGroupsUpdateEvent(alertGroupUpdates, org.apache.ambari.server.events.UpdateEventType.DELETE);
    }

    public java.util.List<org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate> getGroups() {
        return groups;
    }
}