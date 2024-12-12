package org.apache.ambari.server.events.listeners.alerts;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertHashInvalidationListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertHashInvalidationListener.class);

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.alert.AlertDefinitionHash> m_alertDefinitionHash;

    @com.google.inject.Inject
    public AlertHashInvalidationListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.AlertHashInvalidationEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertHashInvalidationListener.LOG.debug("Received event {}", event);
        java.util.Collection<java.lang.String> hosts = event.getHosts();
        long clusterId = event.getClusterId();
        if ((null == hosts) || hosts.isEmpty()) {
            return;
        }
        org.apache.ambari.server.state.alert.AlertDefinitionHash hash = m_alertDefinitionHash.get();
        hash.enqueueAgentCommands(clusterId, hosts);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onEvent(org.apache.ambari.server.events.ServiceComponentUninstalledEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertHashInvalidationListener.LOG.debug("Received event {}", event);
        long clusterId = event.getClusterId();
        java.lang.String hostName = event.getHostName();
        if (null == hostName) {
            return;
        }
        m_alertDefinitionHash.get().invalidate(hostName);
        m_alertDefinitionHash.get().enqueueAgentCommands(clusterId, java.util.Collections.singletonList(hostName));
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.ClusterEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertHashInvalidationListener.LOG.debug("Received event {}", event);
        if (event.getType() != org.apache.ambari.server.events.AmbariEvent.AmbariEventType.CLUSTER_RENAME) {
            return;
        }
        org.apache.ambari.server.state.alert.AlertDefinitionHash hash = m_alertDefinitionHash.get();
        hash.invalidateAll();
        long clusterId = event.getClusterId();
        hash.enqueueAgentCommands(clusterId);
    }
}