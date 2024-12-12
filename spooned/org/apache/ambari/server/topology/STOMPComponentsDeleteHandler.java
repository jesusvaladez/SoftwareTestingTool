package org.apache.ambari.server.topology;
@com.google.inject.Singleton
public class STOMPComponentsDeleteHandler {
    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.TopologyHolder> m_topologyHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.AgentConfigsHolder> agentConfigsHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.MetadataHolder> metadataHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.HostLevelParamsHolder> hostLevelParamsHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder> alertDefinitionsHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    public void processDeleteByMetaDataException(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData metaData) throws org.apache.ambari.server.AmbariException {
        if (metaData.getAmbariException() != null) {
            processDeleteByMetaData(metaData);
            throw metaData.getAmbariException();
        }
    }

    public void processDeleteByMetaData(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData metaData) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(createUpdateFromDeleteByMetaData(metaData), org.apache.ambari.server.events.UpdateEventType.DELETE);
        m_topologyHolder.get().updateData(topologyUpdateEvent);
        updateNonTopologyAgentInfo(metaData.getRemovedHostComponents().stream().map(hc -> hc.getHostId()).collect(java.util.stream.Collectors.toSet()), null);
    }

    public void processDeleteCluster(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyUpdates = new java.util.TreeMap<>();
        topologyUpdates.put(java.lang.Long.toString(clusterId), new org.apache.ambari.server.agent.stomp.dto.TopologyCluster(null, null));
        org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(topologyUpdates, org.apache.ambari.server.events.UpdateEventType.DELETE);
        m_topologyHolder.get().updateData(topologyUpdateEvent);
        updateNonTopologyAgentInfo(clusters.getCluster(clusterId).getHosts().stream().map(h -> h.getHostId()).collect(java.util.stream.Collectors.toSet()), clusterId);
    }

    private void updateNonTopologyAgentInfo(java.util.Set<java.lang.Long> changedHosts, java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        for (java.lang.Long hostId : changedHosts) {
            if (clusterId != null) {
                alertDefinitionsHolder.get().updateData(alertDefinitionsHolder.get().getDeleteCluster(clusterId, hostId));
                agentConfigsHolder.get().updateData(agentConfigsHolder.get().getCurrentDataExcludeCluster(hostId, clusterId));
                hostLevelParamsHolder.get().updateData(hostLevelParamsHolder.get().getCurrentDataExcludeCluster(hostId, clusterId));
            } else {
                agentConfigsHolder.get().updateData(agentConfigsHolder.get().getCurrentData(hostId));
                hostLevelParamsHolder.get().updateData(hostLevelParamsHolder.get().getCurrentData(hostId));
            }
        }
        if (clusterId != null) {
            metadataHolder.get().updateData(metadataHolder.get().getDeleteMetadata(clusterId));
        } else {
            metadataHolder.get().updateData(metadataHolder.get().getCurrentData());
        }
    }

    public java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> createUpdateFromDeleteByMetaData(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData metaData) {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyUpdates = new java.util.TreeMap<>();
        for (org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData.HostComponent hostComponent : metaData.getRemovedHostComponents()) {
            org.apache.ambari.server.agent.stomp.dto.TopologyComponent deletedComponent = org.apache.ambari.server.agent.stomp.dto.TopologyComponent.newBuilder().setComponentName(hostComponent.getComponentName()).setServiceName(hostComponent.getServiceName()).setVersion(hostComponent.getVersion()).setHostIdentifiers(new java.util.HashSet<>(java.util.Arrays.asList(hostComponent.getHostId())), new java.util.HashSet<>(java.util.Arrays.asList(hostComponent.getHostName()))).setLastComponentState(hostComponent.getLastComponentState()).build();
            java.lang.String clusterId = hostComponent.getClusterId();
            if (!topologyUpdates.containsKey(clusterId)) {
                topologyUpdates.put(clusterId, new org.apache.ambari.server.agent.stomp.dto.TopologyCluster());
            }
            if (!topologyUpdates.get(clusterId).getTopologyComponents().contains(deletedComponent)) {
                topologyUpdates.get(clusterId).addTopologyComponent(deletedComponent);
            } else {
                topologyUpdates.get(clusterId).getTopologyComponents().stream().filter(t -> t.equals(deletedComponent)).forEach(t -> t.addHostName(hostComponent.getHostName()));
                topologyUpdates.get(clusterId).getTopologyComponents().stream().filter(t -> t.equals(deletedComponent)).forEach(t -> t.addHostId(hostComponent.getHostId()));
            }
        }
        return topologyUpdates;
    }
}