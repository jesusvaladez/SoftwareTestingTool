package org.apache.ambari.server.topology;
import com.google.inject.persist.Transactional;
@javax.inject.Singleton
public class PersistedStateImpl implements org.apache.ambari.server.topology.PersistedState {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.PersistedState.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyRequestDAO topologyRequestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyHostInfoDAO topologyHostInfoDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyHostGroupDAO hostGroupDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyHostRequestDAO hostRequestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyLogicalRequestDAO topologyLogicalRequestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyLogicalTaskDAO topologyLogicalTaskDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO physicalTaskDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.BlueprintFactory blueprintFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.LogicalRequestFactory logicalRequestFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    private static com.google.gson.Gson jsonSerializer = new com.google.gson.Gson();

    @java.lang.Override
    public org.apache.ambari.server.topology.PersistedTopologyRequest persistTopologyRequest(org.apache.ambari.server.controller.internal.BaseClusterRequest request) {
        org.apache.ambari.server.orm.entities.TopologyRequestEntity requestEntity = toEntity(request);
        topologyRequestDAO.create(requestEntity);
        return new org.apache.ambari.server.topology.PersistedTopologyRequest(requestEntity.getId(), request);
    }

    @java.lang.Override
    public void persistLogicalRequest(org.apache.ambari.server.topology.LogicalRequest logicalRequest, long topologyRequestId) {
        org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity = topologyRequestDAO.findById(topologyRequestId);
        org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity entity = toEntity(logicalRequest, topologyRequestEntity);
        topologyRequestEntity.setTopologyLogicalRequestEntity(entity);
        topologyRequestDAO.merge(topologyRequestEntity);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void removeHostRequests(long logicalRequestId, java.util.Collection<org.apache.ambari.server.topology.HostRequest> hostRequests) {
        org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity logicalRequest = topologyLogicalRequestDAO.findById(logicalRequestId);
        for (org.apache.ambari.server.topology.HostRequest hostRequest : hostRequests) {
            org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntity = hostRequestDAO.findById(hostRequest.getId());
            if (logicalRequest != null) {
                logicalRequest.getTopologyHostRequestEntities().remove(hostRequestEntity);
            }
            hostRequestDAO.remove(hostRequestEntity);
        }
        if ((logicalRequest != null) && logicalRequest.getTopologyHostRequestEntities().isEmpty()) {
            java.lang.Long topologyRequestId = logicalRequest.getTopologyRequestId();
            topologyLogicalRequestDAO.remove(logicalRequest);
            topologyRequestDAO.removeByPK(topologyRequestId);
        }
    }

    @java.lang.Override
    public void setHostRequestStatus(long hostRequestId, org.apache.ambari.server.actionmanager.HostRoleStatus status, java.lang.String message) {
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntity = hostRequestDAO.findById(hostRequestId);
        if (hostRequestEntity != null) {
            hostRequestEntity.setStatus(status);
            hostRequestEntity.setStatusMessage(message);
            hostRequestDAO.merge(hostRequestEntity);
        }
    }

    @java.lang.Override
    public void registerPhysicalTask(long logicalTaskId, long physicalTaskId) {
        org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity entity = topologyLogicalTaskDAO.findById(logicalTaskId);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity physicalEntity = hostRoleCommandDAO.findByPK(physicalTaskId);
        entity.setHostRoleCommandEntity(physicalEntity);
        topologyLogicalTaskDAO.merge(entity);
    }

    @java.lang.Override
    public void registerHostName(long hostRequestId, java.lang.String hostName) {
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity entity = hostRequestDAO.findById(hostRequestId);
        if (entity.getHostName() == null) {
            entity.setHostName(hostName);
            hostRequestDAO.merge(entity);
        }
    }

    @java.lang.Override
    public void registerInTopologyHostInfo(org.apache.ambari.server.state.Host host) {
        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity entity = topologyHostInfoDAO.findByHostname(host.getHostName());
        if ((entity != null) && (entity.getHostEntity() == null)) {
            entity.setHostEntity(hostDAO.findById(host.getHostId()));
            topologyHostInfoDAO.merge(entity);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.LogicalRequest getProvisionRequest(long clusterId) {
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyRequestEntity> entities = topologyRequestDAO.findByClusterId(clusterId);
        for (org.apache.ambari.server.orm.entities.TopologyRequestEntity entity : entities) {
            if (org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION == org.apache.ambari.server.topology.TopologyRequest.Type.valueOf(entity.getAction())) {
                org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity logicalRequestEntity = entity.getTopologyLogicalRequestEntity();
                org.apache.ambari.server.topology.TopologyRequest replayedRequest = new org.apache.ambari.server.topology.PersistedStateImpl.ReplayedTopologyRequest(entity, blueprintFactory);
                try {
                    org.apache.ambari.server.topology.ClusterTopology clusterTopology = new org.apache.ambari.server.topology.ClusterTopologyImpl(ambariContext, replayedRequest);
                    java.lang.Long logicalId = logicalRequestEntity.getId();
                    return logicalRequestFactory.createRequest(logicalId, replayedRequest, clusterTopology, logicalRequestEntity);
                } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
                    throw new java.lang.RuntimeException("Failed to construct cluster topology while replaying request: " + e, e);
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new java.lang.RuntimeException("Failed to construct logical request during replay: " + e, e);
                }
            }
        }
        return null;
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> getAllRequests() {
        java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> allRequests = new java.util.HashMap<>();
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyRequestEntity> entities = topologyRequestDAO.findAll();
        java.util.Map<java.lang.Long, org.apache.ambari.server.topology.ClusterTopology> topologyRequests = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.TopologyRequestEntity entity : entities) {
            org.apache.ambari.server.topology.TopologyRequest replayedRequest = new org.apache.ambari.server.topology.PersistedStateImpl.ReplayedTopologyRequest(entity, blueprintFactory);
            org.apache.ambari.server.topology.ClusterTopology clusterTopology = topologyRequests.get(replayedRequest.getClusterId());
            if (clusterTopology == null) {
                try {
                    clusterTopology = new org.apache.ambari.server.topology.ClusterTopologyImpl(ambariContext, replayedRequest);
                    if (entity.getProvisionAction() != null) {
                        clusterTopology.setProvisionAction(entity.getProvisionAction());
                    }
                    topologyRequests.put(replayedRequest.getClusterId(), clusterTopology);
                    allRequests.put(clusterTopology, new java.util.ArrayList<>());
                } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
                    throw new java.lang.RuntimeException("Failed to construct cluster topology while replaying request: " + e, e);
                }
            } else {
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> groupInfoEntry : replayedRequest.getHostGroupInfo().entrySet()) {
                    java.lang.String name = groupInfoEntry.getKey();
                    if (!clusterTopology.getHostGroupInfo().containsKey(name)) {
                        clusterTopology.getHostGroupInfo().put(name, groupInfoEntry.getValue());
                    }
                }
            }
            org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity logicalRequestEntity = entity.getTopologyLogicalRequestEntity();
            if (logicalRequestEntity != null) {
                try {
                    java.lang.Long logicalId = logicalRequestEntity.getId();
                    ambariContext.getNextRequestId();
                    allRequests.get(clusterTopology).add(logicalRequestFactory.createRequest(logicalId, replayedRequest, clusterTopology, logicalRequestEntity));
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new java.lang.RuntimeException("Failed to construct logical request during replay: " + e, e);
                }
            }
        }
        return allRequests;
    }

    private org.apache.ambari.server.orm.entities.TopologyRequestEntity toEntity(org.apache.ambari.server.controller.internal.BaseClusterRequest request) {
        org.apache.ambari.server.orm.entities.TopologyRequestEntity entity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        entity.setAction(request.getType().name());
        if (request.getBlueprint() != null) {
            entity.setBlueprintName(request.getBlueprint().getName());
        }
        entity.setClusterAttributes(org.apache.ambari.server.topology.PersistedStateImpl.attributesAsString(request.getConfiguration().getAttributes()));
        entity.setClusterId(request.getClusterId());
        entity.setClusterProperties(org.apache.ambari.server.topology.PersistedStateImpl.propertiesAsString(request.getConfiguration().getProperties()));
        entity.setDescription(request.getDescription());
        if (request.getProvisionAction() != null) {
            entity.setProvisionAction(request.getProvisionAction());
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> hostGroupEntities = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : request.getHostGroupInfo().values()) {
            hostGroupEntities.add(toEntity(groupInfo, entity));
        }
        entity.setTopologyHostGroupEntities(hostGroupEntities);
        return entity;
    }

    private org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity toEntity(org.apache.ambari.server.topology.LogicalRequest request, org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity) {
        org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity entity = new org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity();
        entity.setDescription(request.getRequestContext());
        entity.setId(request.getRequestId());
        entity.setTopologyRequestEntity(topologyRequestEntity);
        entity.setTopologyRequestId(topologyRequestEntity.getId());
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> hostRequests = new java.util.ArrayList<>();
        entity.setTopologyHostRequestEntities(hostRequests);
        for (org.apache.ambari.server.topology.HostRequest hostRequest : request.getHostRequests()) {
            hostRequests.add(toEntity(hostRequest, entity));
        }
        return entity;
    }

    private org.apache.ambari.server.orm.entities.TopologyHostRequestEntity toEntity(org.apache.ambari.server.topology.HostRequest request, org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity logicalRequestEntity) {
        org.apache.ambari.server.orm.entities.TopologyHostRequestEntity entity = new org.apache.ambari.server.orm.entities.TopologyHostRequestEntity();
        entity.setHostName(request.getHostName());
        entity.setId(request.getId());
        entity.setStageId(request.getStageId());
        entity.setTopologyLogicalRequestEntity(logicalRequestEntity);
        entity.setTopologyHostGroupEntity(hostGroupDAO.findByRequestIdAndName(logicalRequestEntity.getTopologyRequestId(), request.getHostgroupName()));
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> hostRequestTaskEntities = new java.util.ArrayList<>();
        entity.setTopologyHostTaskEntities(hostRequestTaskEntities);
        for (org.apache.ambari.server.topology.tasks.TopologyTask task : request.getTopologyTasks()) {
            if ((task.getType() == org.apache.ambari.server.topology.tasks.TopologyTask.Type.INSTALL) || (task.getType() == org.apache.ambari.server.topology.tasks.TopologyTask.Type.START)) {
                org.apache.ambari.server.orm.entities.TopologyHostTaskEntity topologyTaskEntity = new org.apache.ambari.server.orm.entities.TopologyHostTaskEntity();
                hostRequestTaskEntities.add(topologyTaskEntity);
                topologyTaskEntity.setType(task.getType().name());
                topologyTaskEntity.setTopologyHostRequestEntity(entity);
                java.util.Collection<org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity> logicalTaskEntities = new java.util.ArrayList<>();
                topologyTaskEntity.setTopologyLogicalTaskEntities(logicalTaskEntities);
                for (java.lang.Long logicalTaskId : request.getLogicalTasksForTopologyTask(task).values()) {
                    org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity logicalTaskEntity = new org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity();
                    logicalTaskEntities.add(logicalTaskEntity);
                    org.apache.ambari.server.actionmanager.HostRoleCommand logicalTask = request.getLogicalTask(logicalTaskId);
                    logicalTaskEntity.setId(logicalTaskId);
                    logicalTaskEntity.setComponentName(logicalTask.getRole().name());
                    logicalTaskEntity.setTopologyHostTaskEntity(topologyTaskEntity);
                    java.lang.Long physicalId = request.getPhysicalTaskId(logicalTaskId);
                    if (physicalId != null) {
                        logicalTaskEntity.setHostRoleCommandEntity(physicalTaskDAO.findByPK(physicalId));
                    }
                    logicalTaskEntity.setTopologyHostTaskEntity(topologyTaskEntity);
                }
            }
        }
        return entity;
    }

    private org.apache.ambari.server.orm.entities.TopologyHostGroupEntity toEntity(org.apache.ambari.server.topology.HostGroupInfo groupInfo, org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity) {
        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity entity = new org.apache.ambari.server.orm.entities.TopologyHostGroupEntity();
        entity.setGroupAttributes(org.apache.ambari.server.topology.PersistedStateImpl.attributesAsString(groupInfo.getConfiguration().getAttributes()));
        entity.setGroupProperties(org.apache.ambari.server.topology.PersistedStateImpl.propertiesAsString(groupInfo.getConfiguration().getProperties()));
        entity.setName(groupInfo.getHostGroupName());
        entity.setTopologyRequestEntity(topologyRequestEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> hostInfoEntities = new java.util.ArrayList<>();
        entity.setTopologyHostInfoEntities(hostInfoEntities);
        java.util.Collection<java.lang.String> hosts = groupInfo.getHostNames();
        if (hosts.isEmpty()) {
            org.apache.ambari.server.orm.entities.TopologyHostInfoEntity hostInfoEntity = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
            hostInfoEntity.setTopologyHostGroupEntity(entity);
            hostInfoEntity.setHostCount(groupInfo.getRequestedHostCount());
            if (groupInfo.getPredicate() != null) {
                hostInfoEntity.setPredicate(groupInfo.getPredicateString());
            }
            hostInfoEntities.add(hostInfoEntity);
        } else {
            for (java.lang.String hostName : hosts) {
                org.apache.ambari.server.orm.entities.TopologyHostInfoEntity hostInfoEntity = new org.apache.ambari.server.orm.entities.TopologyHostInfoEntity();
                hostInfoEntity.setTopologyHostGroupEntity(entity);
                if (groupInfo.getPredicate() != null) {
                    hostInfoEntity.setPredicate(groupInfo.getPredicateString());
                }
                hostInfoEntity.setFqdn(hostName);
                hostInfoEntity.setRackInfo(groupInfo.getHostRackInfo().get(hostName));
                hostInfoEntity.setHostCount(0);
                hostInfoEntities.add(hostInfoEntity);
            }
        }
        return entity;
    }

    private static java.lang.String propertiesAsString(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationProperties) {
        return org.apache.ambari.server.topology.PersistedStateImpl.jsonSerializer.toJson(configurationProperties);
    }

    private static java.lang.String attributesAsString(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes) {
        return org.apache.ambari.server.topology.PersistedStateImpl.jsonSerializer.toJson(configurationAttributes);
    }

    private static class ReplayedTopologyRequest implements org.apache.ambari.server.topology.TopologyRequest {
        private final java.lang.Long clusterId;

        private final org.apache.ambari.server.topology.TopologyRequest.Type type;

        private final java.lang.String description;

        private final org.apache.ambari.server.topology.Blueprint blueprint;

        private final org.apache.ambari.server.topology.Configuration configuration;

        private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = new java.util.HashMap<>();

        public ReplayedTopologyRequest(org.apache.ambari.server.orm.entities.TopologyRequestEntity entity, org.apache.ambari.server.topology.BlueprintFactory blueprintFactory) {
            clusterId = entity.getClusterId();
            type = org.apache.ambari.server.topology.TopologyRequest.Type.valueOf(entity.getAction());
            description = entity.getDescription();
            try {
                blueprint = blueprintFactory.getBlueprint(entity.getBlueprintName());
            } catch (org.apache.ambari.server.stack.NoSuchStackException e) {
                throw new java.lang.RuntimeException("Unable to load blueprint while replaying topology request: " + e, e);
            }
            configuration = createConfiguration(entity.getClusterProperties(), entity.getClusterAttributes());
            configuration.setParentConfiguration(blueprint.getConfiguration());
            parseHostGroupInfo(entity);
        }

        @java.lang.Override
        public java.lang.Long getClusterId() {
            return clusterId;
        }

        @java.lang.Override
        public org.apache.ambari.server.topology.TopologyRequest.Type getType() {
            return type;
        }

        @java.lang.Override
        public org.apache.ambari.server.topology.Blueprint getBlueprint() {
            return blueprint;
        }

        @java.lang.Override
        public org.apache.ambari.server.topology.Configuration getConfiguration() {
            return configuration;
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> getHostGroupInfo() {
            return hostGroupInfoMap;
        }

        @java.lang.Override
        public java.lang.String getDescription() {
            return description;
        }

        private org.apache.ambari.server.topology.Configuration createConfiguration(java.lang.String propString, java.lang.String attributeString) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = org.apache.ambari.server.topology.PersistedStateImpl.jsonSerializer.<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>fromJson(propString, java.util.Map.class);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = org.apache.ambari.server.topology.PersistedStateImpl.jsonSerializer.<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>>fromJson(attributeString, java.util.Map.class);
            return new org.apache.ambari.server.topology.Configuration(properties, attributes);
        }

        private void parseHostGroupInfo(org.apache.ambari.server.orm.entities.TopologyRequestEntity entity) {
            for (org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity : entity.getTopologyHostGroupEntities()) {
                for (org.apache.ambari.server.orm.entities.TopologyHostInfoEntity hostInfoEntity : hostGroupEntity.getTopologyHostInfoEntities()) {
                    java.lang.String groupName = hostGroupEntity.getName();
                    org.apache.ambari.server.topology.HostGroupInfo groupInfo = hostGroupInfoMap.get(groupName);
                    if (groupInfo == null) {
                        groupInfo = new org.apache.ambari.server.topology.HostGroupInfo(groupName);
                        hostGroupInfoMap.put(groupName, groupInfo);
                    }
                    java.lang.String hostname = hostInfoEntity.getFqdn();
                    if ((hostname != null) && (!hostname.isEmpty())) {
                        groupInfo.addHost(hostname);
                        groupInfo.addHostRackInfo(hostname, hostInfoEntity.getRackInfo());
                    } else {
                        groupInfo.setRequestedCount(hostInfoEntity.getHostCount());
                        java.lang.String hostPredicate = hostInfoEntity.getPredicate();
                        if (hostPredicate != null) {
                            try {
                                groupInfo.setPredicate(hostPredicate);
                            } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
                                org.apache.ambari.server.topology.PersistedStateImpl.LOG.error(java.lang.String.format("Failed to compile predicate '%s' during request replay: %s", hostPredicate, e), e);
                            }
                        }
                    }
                    java.lang.String groupConfigProperties = hostGroupEntity.getGroupProperties();
                    java.lang.String groupConfigAttributes = hostGroupEntity.getGroupAttributes();
                    groupInfo.setConfiguration(createConfiguration(groupConfigProperties, groupConfigAttributes));
                }
            }
        }
    }
}