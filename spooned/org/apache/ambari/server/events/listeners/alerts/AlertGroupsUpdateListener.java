package org.apache.ambari.server.events.listeners.alerts;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertGroupsUpdateListener {
    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO alertDispatchDAO;

    @com.google.inject.Inject
    public AlertGroupsUpdateListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher) {
        ambariEventPublisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    public void onAlertDefinitionDeleted(org.apache.ambari.server.events.AlertDefinitionDeleteEvent event) {
        java.util.List<org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate> alertGroupUpdates = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroupEntity : alertDispatchDAO.findAllGroups(event.getClusterId())) {
            boolean eventAffectsGroup = alertGroupEntity.getAlertDefinitions().stream().map(org.apache.ambari.server.orm.entities.AlertDefinitionEntity::getDefinitionId).anyMatch(each -> java.util.Objects.equals(each, event.getDefinition().getDefinitionId()));
            if (eventAffectsGroup) {
                org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate alertGroupUpdate = new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(alertGroupEntity);
                alertGroupUpdate.getTargets().remove(event.getDefinition().getDefinitionId());
                alertGroupUpdates.add(alertGroupUpdate);
            }
        }
        STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AlertGroupsUpdateEvent(alertGroupUpdates, org.apache.ambari.server.events.UpdateEventType.UPDATE));
    }
}