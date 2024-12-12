package org.apache.ambari.server.events.listeners.alerts;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertHostListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertHostListener.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_alertDefinitionDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher m_eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions m_ambariServiceAlertDefinitions;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_alertDefinitionFactory;

    private java.util.concurrent.locks.Lock m_hostAlertLock = new java.util.concurrent.locks.ReentrantLock();

    @com.google.inject.Inject
    public AlertHostListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.HostsAddedEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertHostListener.LOG.debug("Received event {}", event);
        long clusterId = event.getClusterId();
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> agentDefinitions = m_ambariServiceAlertDefinitions.getAgentDefinitions();
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> serverDefinitions = m_ambariServiceAlertDefinitions.getServerDefinitions();
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> ambariServiceDefinitions = new java.util.ArrayList<>();
        ambariServiceDefinitions.addAll(agentDefinitions);
        ambariServiceDefinitions.addAll(serverDefinitions);
        m_hostAlertLock.lock();
        try {
            for (org.apache.ambari.server.state.alert.AlertDefinition agentDefinition : ambariServiceDefinitions) {
                org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = m_alertDefinitionDao.findByName(clusterId, agentDefinition.getName());
                if (null == definition) {
                    definition = m_alertDefinitionFactory.coerce(clusterId, agentDefinition);
                    try {
                        m_alertDefinitionDao.create(definition);
                    } catch (java.lang.Exception e) {
                        org.apache.ambari.server.events.listeners.alerts.AlertHostListener.LOG.error("Unable to create an alert definition named {} in cluster {}", definition.getDefinitionName(), definition.getClusterId(), e);
                    }
                }
            }
        } finally {
            m_hostAlertLock.unlock();
        }
        for (java.lang.String hostName : event.getHostNames()) {
            org.apache.ambari.server.events.AlertHashInvalidationEvent invalidationEvent = new org.apache.ambari.server.events.AlertHashInvalidationEvent(event.getClusterId(), java.util.Collections.singletonList(hostName));
            m_eventPublisher.publish(invalidationEvent);
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.HostsRemovedEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertHostListener.LOG.debug("Received event {}", event);
        for (java.lang.String hostName : event.getHostNames()) {
            m_alertsDao.removeCurrentByHost(hostName);
        }
    }
}