package org.apache.ambari.server.agent.stomp;
import org.apache.commons.collections.CollectionUtils;
import static org.apache.ambari.server.events.AlertDefinitionEventType.CREATE;
import static org.apache.ambari.server.events.AlertDefinitionEventType.DELETE;
@javax.inject.Singleton
public class AlertDefinitionsHolder extends org.apache.ambari.server.agent.stomp.AgentHostDataHolder<org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.class);

    @javax.inject.Inject
    private javax.inject.Provider<org.apache.ambari.server.state.alert.AlertDefinitionHash> helper;

    @javax.inject.Inject
    private javax.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    @javax.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO;

    @javax.inject.Inject
    private org.apache.ambari.server.state.alert.AlertHelper alertHelper;

    @javax.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory alertDefinitionFactory;

    @javax.inject.Inject
    public AlertDefinitionsHolder(org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) {
        eventPublisher.register(this);
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent getCurrentData(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> result = new java.util.TreeMap<>();
        java.util.Map<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition>> alertDefinitions = helper.get().getAlertDefinitions(hostId);
        java.lang.String hostName = clusters.get().getHostById(hostId).getHostName();
        long count = 0;
        for (java.util.Map.Entry<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition>> e : alertDefinitions.entrySet()) {
            java.lang.Long clusterId = e.getKey();
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> definitionMap = e.getValue();
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity ambariStaleAlert = alertDefinitionDAO.findByName(clusterId, org.apache.ambari.server.events.listeners.alerts.AlertDefinitionsUIUpdateListener.AMBARI_STALE_ALERT_NAME);
            java.lang.Integer staleIntervalMultiplier = alertHelper.getWaitFactorMultiplier(alertDefinitionFactory.coerce(ambariStaleAlert));
            result.put(clusterId, new org.apache.ambari.server.agent.stomp.dto.AlertCluster(definitionMap, hostName, staleIntervalMultiplier));
            count += definitionMap.size();
        }
        org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.LOG.info("Loaded {} alert definitions for {} clusters for host {}", count, result.size(), hostName);
        return new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, result, hostName, hostId);
    }

    public org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent getDeleteCluster(java.lang.Long clusterId, java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> result = new java.util.TreeMap<>();
        result.put(clusterId, org.apache.ambari.server.agent.stomp.dto.AlertCluster.emptyAlertCluster());
        return new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.DELETE, result, null, hostId);
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent getEmptyData() {
        return org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent.emptyEvent();
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent handleUpdate(org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent current, org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent update) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> updateClusters = update.getClusters();
        if (updateClusters.isEmpty()) {
            return null;
        }
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent result = null;
        java.lang.Long hostId = update.getHostId();
        boolean changed = false;
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> existingClusters = current.getClusters();
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> mergedClusters = new java.util.HashMap<>();
        switch (update.getEventType()) {
            case UPDATE :
            case DELETE :
                if (!existingClusters.keySet().containsAll(updateClusters.keySet())) {
                    org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.LOG.info("Unknown clusters in update, perhaps cluster was removed previously");
                }
                for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> e : existingClusters.entrySet()) {
                    java.lang.Long clusterId = e.getKey();
                    if (!updateClusters.containsKey(clusterId)) {
                        mergedClusters.put(clusterId, e.getValue());
                    }
                }
                for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> e : updateClusters.entrySet()) {
                    java.lang.Long clusterId = e.getKey();
                    if (existingClusters.containsKey(clusterId)) {
                        if (update.getEventType().equals(org.apache.ambari.server.events.AlertDefinitionEventType.DELETE) && org.apache.commons.collections.CollectionUtils.isEmpty(e.getValue().getAlertDefinitions())) {
                            changed = true;
                        } else {
                            org.apache.ambari.server.agent.stomp.dto.AlertCluster mergedCluster = existingClusters.get(e.getKey()).handleUpdate(update.getEventType(), e.getValue());
                            if (mergedCluster != null) {
                                mergedClusters.put(clusterId, mergedCluster);
                                changed = true;
                            }
                        }
                    } else {
                        mergedClusters.put(clusterId, e.getValue());
                        changed = true;
                    }
                }
                org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.LOG.debug("Handled {} of alerts for {} cluster(s) on host with id {}, changed = {}", update.getEventType(), updateClusters.size(), hostId, changed);
                break;
            case CREATE :
                if (!com.google.common.collect.Sets.intersection(existingClusters.keySet(), updateClusters.keySet()).isEmpty()) {
                    throw new org.apache.ambari.server.AmbariException("Existing clusters in create");
                }
                mergedClusters.putAll(existingClusters);
                mergedClusters.putAll(updateClusters);
                org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.LOG.debug("Handled {} of alerts for {} cluster(s)", update.getEventType(), updateClusters.size());
                changed = true;
                break;
            default :
                org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.LOG.warn("Unhandled event type {}", update.getEventType());
                break;
        }
        if (changed) {
            result = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, mergedClusters, current.getHostName(), hostId);
        }
        return result;
    }

    @com.google.common.eventbus.Subscribe
    public void onHostToClusterAssign(org.apache.ambari.server.events.HostsAddedEvent hostsAddedEvent) throws org.apache.ambari.server.AmbariException {
        java.lang.Long clusterId = hostsAddedEvent.getClusterId();
        for (java.lang.String hostName : hostsAddedEvent.getHostNames()) {
            java.lang.Long hostId = clusters.get().getHost(hostName).getHostId();
            java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> existingClusters = getData(hostId).getClusters();
            if (!existingClusters.containsKey(clusterId)) {
                existingClusters.put(clusterId, new org.apache.ambari.server.agent.stomp.dto.AlertCluster(new java.util.HashMap<>(), hostName));
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onHostsRemoved(org.apache.ambari.server.events.HostsRemovedEvent event) {
        for (java.lang.Long hostId : event.getHostIds()) {
            onHostRemoved(hostId);
        }
    }

    private void safelyUpdateData(org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent event) throws org.apache.ambari.server.AmbariException {
        try {
            updateData(event);
        } catch (org.apache.ambari.server.AmbariRuntimeException e) {
            org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.LOG.warn(java.lang.String.format("Failed to %s alert definitions for host %s", event.getEventType(), event.getHostName()), e);
        }
    }

    private void safelyResetData(java.lang.Long hostId) {
        try {
            resetData(hostId);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.LOG.warn(java.lang.String.format("Failed to reset alert definitions for host with id %s", hostId), e);
        }
    }

    public void provideAlertDefinitionAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType eventType, java.lang.Long clusterId, java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitions, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        java.lang.Long hostId = clusters.get().getHost(hostName).getHostId();
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> update = java.util.Collections.singletonMap(clusterId, new org.apache.ambari.server.agent.stomp.dto.AlertCluster(alertDefinitions, hostName));
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent event = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(eventType, update, hostName, hostId);
        safelyUpdateData(event);
    }

    public void provideStaleAlertDefinitionUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType eventType, java.lang.Long clusterId, java.lang.Integer staleIntervalMultiplier, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        java.lang.Long hostId = clusters.get().getHost(hostName).getHostId();
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> update = java.util.Collections.singletonMap(clusterId, new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.emptyMap(), hostName, staleIntervalMultiplier));
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent event = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(eventType, update, hostName, hostId);
        safelyUpdateData(event);
    }
}