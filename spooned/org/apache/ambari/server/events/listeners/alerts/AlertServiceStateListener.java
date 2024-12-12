package org.apache.ambari.server.events.listeners.alerts;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertServiceStateListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.class);

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> m_metaInfoProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_alertDefinitionFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_alertDispatchDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> m_locksByService = com.google.common.util.concurrent.Striped.lazyWeakLock(20);

    @com.google.inject.Inject
    public AlertServiceStateListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.ServiceInstalledEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.LOG.debug("Received event {}", event);
        long clusterId = event.getClusterId();
        java.lang.String stackName = event.getStackName();
        java.lang.String stackVersion = event.getStackVersion();
        java.lang.String serviceName = event.getServiceName();
        java.util.concurrent.locks.Lock lock = m_locksByService.get(serviceName);
        lock.lock();
        try {
            if (null == m_alertDispatchDao.findDefaultServiceGroup(clusterId, serviceName)) {
                try {
                    m_alertDispatchDao.createDefaultGroup(clusterId, serviceName);
                } catch (org.apache.ambari.server.AmbariException ambariException) {
                    org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.LOG.error("Unable to create a default alert group for {}", event.getServiceName(), ambariException);
                }
            }
            try {
                java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitions = m_metaInfoProvider.get().getAlertDefinitions(stackName, stackVersion, serviceName);
                for (org.apache.ambari.server.state.alert.AlertDefinition definition : alertDefinitions) {
                    org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = m_alertDefinitionFactory.coerce(clusterId, definition);
                    m_definitionDao.create(entity);
                }
            } catch (org.apache.ambari.server.AmbariException ae) {
                java.lang.String message = java.text.MessageFormat.format("Unable to populate alert definitions from the database during installation of {0}", serviceName);
                org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.LOG.error(message, ae);
            }
        } finally {
            lock.unlock();
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.ServiceRemovedEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.LOG.debug("Received event {}", event);
        try {
            m_clusters.get().getClusterById(event.getClusterId());
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.LOG.warn("Unable to retrieve cluster with id {}", event.getClusterId());
            return;
        }
        java.lang.String serviceName = event.getServiceName();
        java.util.concurrent.locks.Lock lock = m_locksByService.get(serviceName);
        lock.lock();
        try {
            java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = m_definitionDao.findByService(event.getClusterId(), event.getServiceName());
            for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
                try {
                    m_definitionDao.remove(definition);
                } catch (java.lang.Exception exception) {
                    org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.LOG.error("Unable to remove alert definition {}", definition.getDefinitionName(), exception);
                }
            }
            org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_alertDispatchDao.findGroupByName(event.getClusterId(), event.getServiceName());
            if ((null != group) && group.isDefault()) {
                try {
                    m_alertDispatchDao.remove(group);
                } catch (java.lang.Exception exception) {
                    org.apache.ambari.server.events.listeners.alerts.AlertServiceStateListener.LOG.error("Unable to remove default alert group {}", group.getGroupName(), exception);
                }
            }
        } finally {
            lock.unlock();
        }
    }
}