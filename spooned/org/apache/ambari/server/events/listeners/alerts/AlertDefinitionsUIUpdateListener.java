package org.apache.ambari.server.events.listeners.alerts;
import static org.apache.ambari.server.events.AlertDefinitionEventType.DELETE;
import static org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertDefinitionsUIUpdateListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertDefinitionsUIUpdateListener.class);

    @com.google.inject.Inject
    private javax.inject.Provider<org.apache.ambari.server.state.alert.AlertDefinitionHash> helper;

    @com.google.inject.Inject
    private javax.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionsHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertHelper alertHelper;

    @com.google.inject.Inject
    public AlertDefinitionsUIUpdateListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher) {
        ambariEventPublisher.register(this);
    }

    public static final java.lang.String AMBARI_STALE_ALERT_NAME = "ambari_server_stale_alerts";

    @com.google.common.eventbus.Subscribe
    public void onAlertDefinitionRegistered(org.apache.ambari.server.events.AlertDefinitionRegistrationEvent event) throws org.apache.ambari.server.AmbariException {
        handleSingleDefinitionChange(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, event.getDefinition());
    }

    @com.google.common.eventbus.Subscribe
    public void onAlertDefinitionChanged(org.apache.ambari.server.events.AlertDefinitionChangedEvent event) throws org.apache.ambari.server.AmbariException {
        handleSingleDefinitionChange(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, event.getDefinition());
    }

    @com.google.common.eventbus.Subscribe
    public void onAlertDefinitionDeleted(org.apache.ambari.server.events.AlertDefinitionDeleteEvent event) throws org.apache.ambari.server.AmbariException {
        handleSingleDefinitionChange(org.apache.ambari.server.events.AlertDefinitionEventType.DELETE, event.getDefinition());
    }

    @com.google.common.eventbus.Subscribe
    public void onServiceComponentInstalled(org.apache.ambari.server.events.ServiceComponentInstalledEvent event) throws org.apache.ambari.server.AmbariException {
        java.lang.String hostName = event.getHostName();
        java.lang.String serviceName = event.getServiceName();
        java.lang.String componentName = event.getComponentName();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> definitions = helper.get().findByServiceComponent(event.getClusterId(), serviceName, componentName);
        if (event.isMasterComponent()) {
            try {
                org.apache.ambari.server.state.Cluster cluster = clusters.get().getClusterById(event.getClusterId());
                if (cluster.getService(serviceName).getServiceComponents().get(componentName).getServiceComponentHosts().containsKey(hostName)) {
                    definitions.putAll(helper.get().findByServiceMaster(event.getClusterId(), serviceName));
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                java.lang.String msg = java.lang.String.format("Failed to get alert definitions for master component %s/%s", serviceName, componentName);
                org.apache.ambari.server.events.listeners.alerts.AlertDefinitionsUIUpdateListener.LOG.warn(msg, e);
            }
        }
        if (!definitions.isEmpty()) {
            alertDefinitionsHolder.provideAlertDefinitionAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, event.getClusterId(), definitions, hostName);
            java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> map = java.util.Collections.singletonMap(event.getClusterId(), new org.apache.ambari.server.agent.stomp.dto.AlertCluster(definitions, hostName));
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AlertDefinitionsUIUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.UPDATE, map));
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onServiceComponentUninstalled(org.apache.ambari.server.events.ServiceComponentUninstalledEvent event) throws org.apache.ambari.server.AmbariException {
        java.lang.String hostName = event.getHostName();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> definitions = helper.get().findByServiceComponent(event.getClusterId(), event.getServiceName(), event.getComponentName());
        if (event.isMasterComponent()) {
            definitions.putAll(helper.get().findByServiceMaster(event.getClusterId(), event.getServiceName()));
        }
        if (!definitions.isEmpty()) {
            alertDefinitionsHolder.provideAlertDefinitionAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.DELETE, event.getClusterId(), definitions, hostName);
            java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> map = java.util.Collections.singletonMap(event.getClusterId(), new org.apache.ambari.server.agent.stomp.dto.AlertCluster(definitions, hostName));
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AlertDefinitionsUIUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.DELETE, map));
        }
    }

    private void handleSingleDefinitionChange(org.apache.ambari.server.events.AlertDefinitionEventType eventType, org.apache.ambari.server.state.alert.AlertDefinition alertDefinition) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.listeners.alerts.AlertDefinitionsUIUpdateListener.LOG.info("{} alert definition '{}'", eventType, alertDefinition);
        org.apache.ambari.server.state.Cluster cluster = clusters.get().getCluster(alertDefinition.getClusterId());
        helper.get().invalidateHosts(alertDefinition);
        for (java.lang.String hostName : alertDefinition.matchingHosts(clusters.get())) {
            alertDefinitionsHolder.provideAlertDefinitionAgentUpdateEvent(eventType, alertDefinition.getClusterId(), java.util.Collections.singletonMap(alertDefinition.getDefinitionId(), alertDefinition), hostName);
        }
        if (alertDefinition.getName().equals(org.apache.ambari.server.events.listeners.alerts.AlertDefinitionsUIUpdateListener.AMBARI_STALE_ALERT_NAME)) {
            for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
                alertDefinitionsHolder.provideStaleAlertDefinitionUpdateEvent(eventType, alertDefinition.getClusterId(), alertHelper.getWaitFactorMultiplier(alertDefinition), host.getHostName());
            }
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> update = java.util.Collections.singletonMap(alertDefinition.getClusterId(), new org.apache.ambari.server.agent.stomp.dto.AlertCluster(alertDefinition, null));
        org.apache.ambari.server.events.AlertDefinitionsUIUpdateEvent event = new org.apache.ambari.server.events.AlertDefinitionsUIUpdateEvent(eventType, update);
        STOMPUpdatePublisher.publish(event);
    }
}