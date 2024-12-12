package org.apache.ambari.server.events.listeners.alerts;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertLifecycleListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AggregateDefinitionMapping m_aggregateMapping;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.alert.AlertDefinitionHash> m_alertDefinitionHash;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher m_eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    @com.google.inject.Inject
    public AlertLifecycleListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.AlertDefinitionRegistrationEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.LOG.debug("Received event {}", event);
        org.apache.ambari.server.state.alert.AlertDefinition definition = event.getDefinition();
        org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.LOG.debug("Registering alert definition {}", definition);
        if (definition.getSource().getType() == org.apache.ambari.server.state.alert.SourceType.AGGREGATE) {
            m_aggregateMapping.registerAggregate(event.getClusterId(), definition);
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.AlertDefinitionChangedEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.LOG.debug("Received event {}", event);
        org.apache.ambari.server.state.alert.AlertDefinition definition = event.getDefinition();
        org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.LOG.debug("Updating alert definition {}", definition);
        if (definition.getSource().getType() == org.apache.ambari.server.state.alert.SourceType.AGGREGATE) {
            m_aggregateMapping.registerAggregate(event.getClusterId(), definition);
        }
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_alertsDao.findCurrentByDefinitionId(definition.getDefinitionId());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currentAlerts) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = current.getAlertHistory();
            if (!org.apache.commons.lang.StringUtils.equals(definition.getLabel(), history.getAlertLabel())) {
                history.setAlertLabel(definition.getLabel());
                m_alertsDao.merge(history);
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.AlertDefinitionDeleteEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.LOG.debug("Received event {}", event);
        org.apache.ambari.server.state.alert.AlertDefinition definition = event.getDefinition();
        org.apache.ambari.server.events.listeners.alerts.AlertLifecycleListener.LOG.debug("Removing alert definition {}", definition);
        if (null == definition) {
            return;
        }
        m_aggregateMapping.removeAssociatedAggregate(event.getClusterId(), definition.getName());
        org.apache.ambari.server.state.alert.AlertDefinitionHash hashHelper = m_alertDefinitionHash.get();
        java.util.Set<java.lang.String> invalidatedHosts = hashHelper.invalidateHosts(definition);
        org.apache.ambari.server.events.AlertHashInvalidationEvent hashInvalidationEvent = new org.apache.ambari.server.events.AlertHashInvalidationEvent(definition.getClusterId(), invalidatedHosts);
        m_eventPublisher.publish(hashInvalidationEvent);
    }
}