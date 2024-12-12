package org.apache.ambari.server.agent.stomp;
import org.apache.commons.collections.CollectionUtils;
@com.google.inject.Singleton
public class TopologyHolder extends org.apache.ambari.server.agent.stomp.AgentClusterDataHolder<org.apache.ambari.server.events.TopologyUpdateEvent> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.TopologyHolder.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementControllerImpl ambariManagementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper;

    @com.google.inject.Inject
    public TopologyHolder(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher) {
        ambariEventPublisher.register(this);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.TopologyUpdateEvent getUpdateIfChanged(java.lang.String agentHash) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = super.getUpdateIfChanged(agentHash);
        prepareAgentTopology(topologyUpdateEvent);
        return topologyUpdateEvent;
    }

    @java.lang.Override
    public org.apache.ambari.server.events.TopologyUpdateEvent getCurrentData() throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyClusters = new java.util.TreeMap<>();
        for (org.apache.ambari.server.state.Cluster cl : clusters.getClusters().values()) {
            java.util.Collection<org.apache.ambari.server.state.Host> clusterHosts = cl.getHosts();
            java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> topologyComponents = new java.util.HashSet<>();
            java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyHost> topologyHosts = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.Host host : clusterHosts) {
                topologyHosts.add(new org.apache.ambari.server.agent.stomp.dto.TopologyHost(host.getHostId(), host.getHostName(), host.getRackInfo(), host.getIPv4()));
            }
            for (org.apache.ambari.server.state.Service service : cl.getServices().values()) {
                for (org.apache.ambari.server.state.ServiceComponent component : service.getServiceComponents().values()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> componentsMap = component.getServiceComponentHosts();
                    if (!componentsMap.isEmpty()) {
                        org.apache.ambari.server.state.ServiceComponentHost sch = componentsMap.entrySet().iterator().next().getValue();
                        java.util.Set<java.lang.String> hostNames = cl.getHosts(sch.getServiceName(), sch.getServiceComponentName());
                        java.util.Set<java.lang.Long> hostOrderIds = clusterHosts.stream().filter(h -> hostNames.contains(h.getHostName())).map(org.apache.ambari.server.state.Host::getHostId).collect(java.util.stream.Collectors.toSet());
                        java.util.Set<java.lang.String> hostOrderNames = clusterHosts.stream().filter(h -> hostNames.contains(h.getHostName())).map(org.apache.ambari.server.state.Host::getHostName).collect(java.util.stream.Collectors.toSet());
                        java.lang.String serviceName = sch.getServiceName();
                        java.lang.String componentName = sch.getServiceComponentName();
                        org.apache.ambari.server.agent.stomp.dto.TopologyComponent topologyComponent = org.apache.ambari.server.agent.stomp.dto.TopologyComponent.newBuilder().setComponentName(sch.getServiceComponentName()).setServiceName(sch.getServiceName()).setHostIdentifiers(hostOrderIds, hostOrderNames).setComponentLevelParams(ambariManagementController.getTopologyComponentLevelParams(cl.getClusterId(), serviceName, componentName, cl.getSecurityType())).setCommandParams(ambariManagementController.getTopologyCommandParams(cl.getClusterId(), serviceName, componentName, sch)).build();
                        topologyComponents.add(topologyComponent);
                    }
                }
            }
            topologyClusters.put(java.lang.Long.toString(cl.getClusterId()), new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(topologyComponents, topologyHosts));
        }
        return new org.apache.ambari.server.events.TopologyUpdateEvent(topologyClusters, org.apache.ambari.server.events.UpdateEventType.CREATE);
    }

    @java.lang.Override
    public boolean updateData(org.apache.ambari.server.events.TopologyUpdateEvent update) throws org.apache.ambari.server.AmbariException {
        boolean changed = super.updateData(update);
        if (changed) {
            org.apache.ambari.server.events.TopologyUpdateEvent copiedUpdate = update.deepCopy();
            org.apache.ambari.server.events.TopologyAgentUpdateEvent topologyAgentUpdateEvent = new org.apache.ambari.server.events.TopologyAgentUpdateEvent(copiedUpdate.getClusters(), copiedUpdate.getHash(), copiedUpdate.getEventType());
            prepareAgentTopology(topologyAgentUpdateEvent);
            org.apache.ambari.server.agent.stomp.TopologyHolder.LOG.debug("Publishing Topology Agent Update Event hash={}", topologyAgentUpdateEvent.getHash());
            STOMPUpdatePublisher.publish(topologyAgentUpdateEvent);
        }
        return changed;
    }

    @java.lang.Override
    protected boolean handleUpdate(org.apache.ambari.server.events.TopologyUpdateEvent update) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport report = new org.apache.ambari.server.agent.stomp.dto.TopologyUpdateHandlingReport();
        org.apache.ambari.server.events.UpdateEventType eventType = update.getEventType();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> updatedCluster : update.getClusters().entrySet()) {
            java.lang.String clusterId = updatedCluster.getKey();
            org.apache.ambari.server.agent.stomp.dto.TopologyCluster cluster = updatedCluster.getValue();
            if (getData().getClusters().containsKey(clusterId)) {
                if ((eventType.equals(org.apache.ambari.server.events.UpdateEventType.DELETE) && org.apache.commons.collections.CollectionUtils.isEmpty(cluster.getTopologyComponents())) && org.apache.commons.collections.CollectionUtils.isEmpty(cluster.getTopologyHosts())) {
                    getData().getClusters().remove(clusterId);
                    report.mappingWasChanged();
                } else {
                    getData().getClusters().get(clusterId).update(update.getClusters().get(clusterId).getTopologyComponents(), update.getClusters().get(clusterId).getTopologyHosts(), eventType, report);
                }
            } else if (eventType.equals(org.apache.ambari.server.events.UpdateEventType.UPDATE)) {
                getData().getClusters().put(clusterId, cluster);
                report.mappingWasChanged();
            } else {
                throw new org.apache.ambari.server.ClusterNotFoundException(java.lang.Long.parseLong(clusterId));
            }
        }
        stackAdvisorHelper.clearCaches(report.getUpdatedHostNames());
        return report.wasChanged();
    }

    private void prepareAgentTopology(org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent) {
        if (topologyUpdateEvent.getClusters() != null) {
            for (org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster : topologyUpdateEvent.getClusters().values()) {
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(topologyCluster.getTopologyComponents())) {
                    for (org.apache.ambari.server.agent.stomp.dto.TopologyComponent topologyComponent : topologyCluster.getTopologyComponents()) {
                        topologyComponent.setHostNames(new java.util.HashSet<>());
                        topologyComponent.setPublicHostNames(new java.util.HashSet<>());
                        topologyComponent.setLastComponentState(null);
                    }
                }
                if (topologyUpdateEvent.getEventType().equals(org.apache.ambari.server.events.UpdateEventType.DELETE) && org.apache.commons.collections.CollectionUtils.isNotEmpty(topologyCluster.getTopologyHosts())) {
                    for (org.apache.ambari.server.agent.stomp.dto.TopologyHost topologyHost : topologyCluster.getTopologyHosts()) {
                        topologyHost.setHostName(null);
                    }
                }
            }
        }
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.TopologyUpdateEvent getEmptyData() {
        return org.apache.ambari.server.events.TopologyUpdateEvent.emptyUpdate();
    }

    @com.google.common.eventbus.Subscribe
    public void onClusterComponentsRepoUpdate(org.apache.ambari.server.events.ClusterComponentsRepoChangedEvent clusterComponentsRepoChangedEvent) throws org.apache.ambari.server.AmbariException {
        java.lang.Long clusterId = clusterComponentsRepoChangedEvent.getClusterId();
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster topologyCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster();
        topologyCluster.setTopologyComponents(getTopologyComponentRepos(clusterId));
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyUpdates = new java.util.TreeMap<>();
        topologyUpdates.put(java.lang.Long.toString(clusterId), topologyCluster);
        org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(topologyUpdates, org.apache.ambari.server.events.UpdateEventType.UPDATE);
        updateData(topologyUpdateEvent);
    }

    private java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> getTopologyComponentRepos(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.agent.stomp.dto.TopologyComponent> topologyComponents = new java.util.HashSet<>();
        org.apache.ambari.server.state.Cluster cl = clusters.getCluster(clusterId);
        for (org.apache.ambari.server.state.Service service : cl.getServices().values()) {
            for (org.apache.ambari.server.state.ServiceComponent component : service.getServiceComponents().values()) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> componentsMap = component.getServiceComponentHosts();
                if (!componentsMap.isEmpty()) {
                    org.apache.ambari.server.state.ServiceComponentHost sch = componentsMap.entrySet().iterator().next().getValue();
                    java.lang.String serviceName = sch.getServiceName();
                    java.lang.String componentName = sch.getServiceComponentName();
                    org.apache.ambari.server.agent.stomp.dto.TopologyComponent topologyComponent = org.apache.ambari.server.agent.stomp.dto.TopologyComponent.newBuilder().setComponentName(sch.getServiceComponentName()).setServiceName(sch.getServiceName()).setCommandParams(ambariManagementController.getTopologyCommandParams(cl.getClusterId(), serviceName, componentName, sch)).setComponentLevelParams(ambariManagementController.getTopologyComponentLevelParams(clusterId, serviceName, componentName, cl.getSecurityType())).build();
                    topologyComponents.add(topologyComponent);
                }
            }
        }
        return topologyComponents;
    }
}