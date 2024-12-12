package org.apache.ambari.server.events.listeners.alerts;
@org.apache.ambari.server.EagerSingleton
public class AlertDefinitionDisabledListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertDefinitionDisabledListener.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao = null;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    public AlertDefinitionDisabledListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onEvent(org.apache.ambari.server.events.AlertDefinitionDisabledEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertDefinitionDisabledListener.LOG.debug("Received event {}", event);
        m_alertsDao.removeCurrentDisabledAlerts();
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> alertUpdates = new java.util.HashMap<>();
        alertUpdates.put(event.getClusterId(), org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.generateEmptySummary(event.getDefinitionId(), event.getDefinitionName()));
        STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AlertUpdateEvent(alertUpdates));
    }
}