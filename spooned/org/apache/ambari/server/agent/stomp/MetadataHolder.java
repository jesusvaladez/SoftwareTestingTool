package org.apache.ambari.server.agent.stomp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
@com.google.inject.Singleton
public class MetadataHolder extends org.apache.ambari.server.agent.stomp.AgentClusterDataHolder<org.apache.ambari.server.events.MetadataUpdateEvent> {
    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementControllerImpl ambariManagementController;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    public MetadataHolder(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher) {
        ambariEventPublisher.register(this);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.MetadataUpdateEvent getCurrentData() throws org.apache.ambari.server.AmbariException {
        return ambariManagementController.getClustersMetadata();
    }

    public org.apache.ambari.server.events.MetadataUpdateEvent getDeleteMetadata(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> clusterToRemove = new java.util.TreeMap<>();
        if (clusterId != null) {
            clusterToRemove.put(java.lang.Long.toString(clusterId), org.apache.ambari.server.agent.stomp.dto.MetadataCluster.emptyMetadataCluster());
        }
        org.apache.ambari.server.events.MetadataUpdateEvent deleteEvent = new org.apache.ambari.server.events.MetadataUpdateEvent(clusterToRemove, null, null, org.apache.ambari.server.events.UpdateEventType.DELETE);
        return deleteEvent;
    }

    @java.lang.Override
    protected boolean handleUpdate(org.apache.ambari.server.events.MetadataUpdateEvent update) throws org.apache.ambari.server.AmbariException {
        boolean changed = false;
        org.apache.ambari.server.events.UpdateEventType eventType = update.getEventType();
        if (org.apache.commons.collections.MapUtils.isNotEmpty(update.getMetadataClusters())) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusterEntry : update.getMetadataClusters().entrySet()) {
                org.apache.ambari.server.agent.stomp.dto.MetadataCluster updatedCluster = metadataClusterEntry.getValue();
                java.lang.String clusterId = metadataClusterEntry.getKey();
                java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> clusters = getData().getMetadataClusters();
                if (clusters.containsKey(clusterId)) {
                    if (eventType.equals(org.apache.ambari.server.events.UpdateEventType.DELETE)) {
                        getData().getMetadataClusters().remove(clusterId);
                        changed = true;
                    } else {
                        org.apache.ambari.server.agent.stomp.dto.MetadataCluster cluster = clusters.get(clusterId);
                        if (cluster.updateClusterLevelParams(updatedCluster.getClusterLevelParams())) {
                            changed = true;
                        }
                        if (cluster.updateServiceLevelParams(updatedCluster.getServiceLevelParams(), updatedCluster.isFullServiceLevelMetadata())) {
                            changed = true;
                        }
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(updatedCluster.getStatusCommandsToRun()) && (!cluster.getStatusCommandsToRun().containsAll(updatedCluster.getStatusCommandsToRun()))) {
                            cluster.getStatusCommandsToRun().addAll(updatedCluster.getStatusCommandsToRun());
                            changed = true;
                        }
                    }
                } else if (eventType.equals(org.apache.ambari.server.events.UpdateEventType.UPDATE)) {
                    clusters.put(clusterId, updatedCluster);
                    changed = true;
                } else {
                    throw new org.apache.ambari.server.ClusterNotFoundException(java.lang.Long.parseLong(clusterId));
                }
            }
        }
        return changed;
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.MetadataUpdateEvent getEmptyData() {
        return org.apache.ambari.server.events.MetadataUpdateEvent.emptyUpdate();
    }

    @com.google.common.eventbus.Subscribe
    public void onServiceCreate(org.apache.ambari.server.events.ServiceInstalledEvent serviceInstalledEvent) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getCluster(serviceInstalledEvent.getClusterId());
        updateData(ambariManagementController.getClusterMetadataOnServiceInstall(cluster, serviceInstalledEvent.getServiceName()));
    }

    @com.google.common.eventbus.Subscribe
    public void onClusterComponentsRepoUpdate(org.apache.ambari.server.events.ClusterComponentsRepoChangedEvent clusterComponentsRepoChangedEvent) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getCluster(clusterComponentsRepoChangedEvent.getClusterId());
        updateData(ambariManagementController.getClusterMetadataOnRepoUpdate(cluster));
    }

    @com.google.common.eventbus.Subscribe
    public void onServiceCredentialStoreUpdate(org.apache.ambari.server.events.ServiceCredentialStoreUpdateEvent serviceCredentialStoreUpdateEvent) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getCluster(serviceCredentialStoreUpdateEvent.getClusterId());
        updateData(ambariManagementController.getClusterMetadataOnServiceCredentialStoreUpdate(cluster, serviceCredentialStoreUpdateEvent.getServiceName()));
    }

    @com.google.common.eventbus.Subscribe
    public void onAmbariPropertiesChange(org.apache.ambari.server.events.AmbariPropertiesChangedEvent event) throws org.apache.ambari.server.AmbariException {
        updateData(ambariManagementController.getClustersMetadata());
    }
}