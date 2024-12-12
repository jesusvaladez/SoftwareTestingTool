package org.apache.ambari.server.agent.stomp;
import org.apache.commons.collections.MapUtils;
@com.google.inject.Singleton
public class HostLevelParamsHolder extends org.apache.ambari.server.agent.stomp.AgentHostDataHolder<org.apache.ambari.server.events.HostLevelParamsUpdateEvent> {
    @com.google.inject.Inject
    private org.apache.ambari.server.agent.RecoveryConfigHelper recoveryConfigHelper;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.controller.AmbariManagementController> m_ambariManagementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    public HostLevelParamsHolder(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher) {
        ambariEventPublisher.register(this);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.HostLevelParamsUpdateEvent getCurrentData(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        return getCurrentDataExcludeCluster(hostId, null);
    }

    public org.apache.ambari.server.events.HostLevelParamsUpdateEvent getCurrentDataExcludeCluster(java.lang.Long hostId, java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> hostLevelParamsClusters = new java.util.TreeMap<>();
        org.apache.ambari.server.state.Host host = clusters.getHostById(hostId);
        for (org.apache.ambari.server.state.Cluster cl : clusters.getClustersForHost(host.getHostName())) {
            if ((clusterId != null) && (cl.getClusterId() == clusterId)) {
                continue;
            }
            org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster hostLevelParamsCluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(recoveryConfigHelper.getRecoveryConfig(cl.getClusterName(), host.getHostName()), m_ambariManagementController.get().getBlueprintProvisioningStates(cl.getClusterId(), host.getHostId()));
            hostLevelParamsClusters.put(java.lang.Long.toString(cl.getClusterId()), hostLevelParamsCluster);
        }
        return new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(hostId, hostLevelParamsClusters);
    }

    public void updateAllHosts() throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.Host host : clusters.getHosts()) {
            updateData(getCurrentData(host.getHostId()));
        }
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.HostLevelParamsUpdateEvent handleUpdate(org.apache.ambari.server.events.HostLevelParamsUpdateEvent current, org.apache.ambari.server.events.HostLevelParamsUpdateEvent update) {
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent result = null;
        boolean changed = false;
        java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> mergedClusters = new java.util.HashMap<>();
        if (org.apache.commons.collections.MapUtils.isNotEmpty(update.getHostLevelParamsClusters())) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> hostLevelParamsClusterEntry : current.getHostLevelParamsClusters().entrySet()) {
                java.lang.String clusterId = hostLevelParamsClusterEntry.getKey();
                if (!update.getHostLevelParamsClusters().containsKey(clusterId)) {
                    mergedClusters.put(clusterId, hostLevelParamsClusterEntry.getValue());
                }
            }
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> hostLevelParamsClusterEntry : update.getHostLevelParamsClusters().entrySet()) {
                java.lang.String clusterId = hostLevelParamsClusterEntry.getKey();
                if (current.getHostLevelParamsClusters().containsKey(clusterId)) {
                    boolean clusterChanged = false;
                    org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster updatedCluster = hostLevelParamsClusterEntry.getValue();
                    org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster currentCluster = current.getHostLevelParamsClusters().get(clusterId);
                    org.apache.ambari.server.agent.RecoveryConfig mergedRecoveryConfig;
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.BlueprintProvisioningState> mergedBlueprintProvisioningStates;
                    if (!currentCluster.getRecoveryConfig().equals(updatedCluster.getRecoveryConfig())) {
                        mergedRecoveryConfig = updatedCluster.getRecoveryConfig();
                        clusterChanged = true;
                    } else {
                        mergedRecoveryConfig = currentCluster.getRecoveryConfig();
                    }
                    if (!currentCluster.getBlueprintProvisioningState().equals(updatedCluster.getBlueprintProvisioningState())) {
                        mergedBlueprintProvisioningStates = updatedCluster.getBlueprintProvisioningState();
                        clusterChanged = true;
                    } else {
                        mergedBlueprintProvisioningStates = currentCluster.getBlueprintProvisioningState();
                    }
                    if (clusterChanged) {
                        org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster mergedCluster = new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(mergedRecoveryConfig, mergedBlueprintProvisioningStates);
                        mergedClusters.put(clusterId, mergedCluster);
                        changed = true;
                    } else {
                        mergedClusters.put(clusterId, hostLevelParamsClusterEntry.getValue());
                    }
                } else {
                    mergedClusters.put(clusterId, hostLevelParamsClusterEntry.getValue());
                    changed = true;
                }
            }
        }
        if (changed) {
            result = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(current.getHostId(), mergedClusters);
        }
        return result;
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.HostLevelParamsUpdateEvent getEmptyData() {
        return org.apache.ambari.server.events.HostLevelParamsUpdateEvent.emptyUpdate();
    }

    @com.google.common.eventbus.Subscribe
    public void onClusterComponentsRepoUpdate(org.apache.ambari.server.events.ClusterComponentsRepoChangedEvent clusterComponentsRepoChangedEvent) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterComponentsRepoChangedEvent.getClusterId());
        for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
            updateDataOfHost(clusterComponentsRepoChangedEvent.getClusterId(), cluster, host);
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onServiceComponentRecoveryChanged(org.apache.ambari.server.events.ServiceComponentRecoveryChangedEvent event) throws org.apache.ambari.server.AmbariException {
        long clusterId = event.getClusterId();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterId);
        for (org.apache.ambari.server.state.ServiceComponentHost host : cluster.getServiceComponentHosts(event.getServiceName(), event.getComponentName())) {
            updateDataOfHost(clusterId, cluster, host.getHost());
        }
    }

    private void updateDataOfHost(long clusterId, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.HostLevelParamsUpdateEvent hostLevelParamsUpdateEvent = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(host.getHostId(), java.lang.Long.toString(clusterId), new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), host.getHostName()), m_ambariManagementController.get().getBlueprintProvisioningStates(clusterId, host.getHostId())));
        updateData(hostLevelParamsUpdateEvent);
    }

    @com.google.common.eventbus.Subscribe
    public void onMaintenanceModeChanged(org.apache.ambari.server.events.MaintenanceModeEvent event) throws org.apache.ambari.server.AmbariException {
        long clusterId = event.getClusterId();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterId);
        if ((event.getHost() != null) || (event.getServiceComponentHost() != null)) {
            org.apache.ambari.server.state.Host host = (event.getHost() != null) ? event.getHost() : event.getServiceComponentHost().getHost();
            updateDataOfHost(clusterId, cluster, host);
        } else if (event.getService() != null) {
            for (java.lang.String hostName : event.getService().getServiceHosts()) {
                updateDataOfHost(clusterId, cluster, cluster.getHost(hostName));
            }
        }
    }
}