package org.apache.ambari.server.state.cluster;
import com.google.inject.persist.Transactional;
import javax.persistence.RollbackException;
import org.springframework.security.core.GrantedAuthority;
@com.google.inject.Singleton
public class ClustersImpl implements org.apache.ambari.server.state.Clusters {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.cluster.ClustersImpl.class);

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, org.apache.ambari.server.state.Cluster> clustersByName = null;

    private java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.state.Cluster> clustersById = null;

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, org.apache.ambari.server.state.Host> hostsByName = null;

    private java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.state.Host> hostsById = null;

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Cluster>> hostClustersMap = null;

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Host>> clusterHostsMap1 = null;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostStateDAO hostStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestOperationLevelDAO requestOperationLevelDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostConfigMappingDAO hostConfigMappingDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.cluster.ClusterFactory clusterFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.host.HostFactory hostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.SecurityHelper securityHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyLogicalTaskDAO topologyLogicalTaskDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyHostInfoDAO topologyHostInfoDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.TopologyManager topologyManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.TopologyHolder> m_topologyHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.AgentConfigsHolder> m_agentConfigsHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.MetadataHolder> m_metadataHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.controller.AmbariManagementControllerImpl> m_ambariManagementController;

    @com.google.inject.Inject
    public ClustersImpl(org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.state.cluster.ClusterFactory clusterFactory, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.state.host.HostFactory hostFactory) {
        this.clusterDAO = clusterDAO;
        this.clusterFactory = clusterFactory;
        this.hostDAO = hostDAO;
        this.hostFactory = hostFactory;
    }

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, org.apache.ambari.server.state.Cluster> getClustersByName() {
        if (clustersByName == null) {
            safelyLoadClustersAndHosts();
        }
        return clustersByName;
    }

    private java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.state.Cluster> getClustersById() {
        if (clustersById == null) {
            safelyLoadClustersAndHosts();
        }
        return clustersById;
    }

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, org.apache.ambari.server.state.Host> getHostsByName() {
        if (hostsByName == null) {
            safelyLoadClustersAndHosts();
        }
        return hostsByName;
    }

    private java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.state.Host> getHostsById() {
        if (hostsById == null) {
            safelyLoadClustersAndHosts();
        }
        return hostsById;
    }

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Cluster>> getHostClustersMap() {
        if (hostClustersMap == null) {
            safelyLoadClustersAndHosts();
        }
        return hostClustersMap;
    }

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Host>> getClusterHostsMap() {
        if (clusterHostsMap1 == null) {
            safelyLoadClustersAndHosts();
        }
        return clusterHostsMap1;
    }

    private synchronized void safelyLoadClustersAndHosts() {
        if ((((((clustersByName == null) || (clustersById == null)) || (hostsByName == null)) || (hostsById == null)) || (hostClustersMap == null)) || (clusterHostsMap1 == null)) {
            loadClustersAndHosts();
        }
    }

    private void loadClustersAndHosts() {
        org.apache.ambari.server.state.cluster.ClustersImpl.LOG.info("Initializing cluster and host data.");
        java.util.concurrent.ConcurrentHashMap<java.lang.String, org.apache.ambari.server.state.Cluster> clustersByNameTemp = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.state.Cluster> clustersByIdTemp = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.concurrent.ConcurrentHashMap<java.lang.String, org.apache.ambari.server.state.Host> hostsByNameTemp = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.state.Host> hostsByIdTemp = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Cluster>> hostClustersMapTemp = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Host>> clusterHostsMap1Temp = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = hostDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostEntity hostEntity : hostEntities) {
            org.apache.ambari.server.state.Host host = hostFactory.create(hostEntity);
            hostsByNameTemp.put(hostEntity.getHostName(), host);
            hostsByIdTemp.put(hostEntity.getHostId(), host);
        }
        hostsByName = hostsByNameTemp;
        hostsById = hostsByIdTemp;
        for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : clusterDAO.findAll()) {
            org.apache.ambari.server.state.Cluster currentCluster = clusterFactory.create(clusterEntity);
            clustersByNameTemp.put(clusterEntity.getClusterName(), currentCluster);
            clustersByIdTemp.put(currentCluster.getClusterId(), currentCluster);
            clusterHostsMap1Temp.put(currentCluster.getClusterName(), java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>()));
        }
        clustersByName = clustersByNameTemp;
        clustersById = clustersByIdTemp;
        for (org.apache.ambari.server.orm.entities.HostEntity hostEntity : hostEntities) {
            java.util.Set<org.apache.ambari.server.state.Cluster> cSet = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<org.apache.ambari.server.state.Cluster, java.lang.Boolean>());
            hostClustersMapTemp.put(hostEntity.getHostName(), cSet);
            org.apache.ambari.server.state.Host host = getHostsByName().get(hostEntity.getHostName());
            for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : hostEntity.getClusterEntities()) {
                clusterHostsMap1Temp.get(clusterEntity.getClusterName()).add(host);
                cSet.add(clustersByName.get(clusterEntity.getClusterName()));
            }
        }
        hostClustersMap = hostClustersMapTemp;
        clusterHostsMap1 = clusterHostsMap1Temp;
        for (java.lang.Long hostId : hostsById.keySet()) {
            try {
                m_agentConfigsHolder.get().initializeDataIfNeeded(hostId, true);
            } catch (org.apache.ambari.server.AmbariRuntimeException e) {
                org.apache.ambari.server.state.cluster.ClustersImpl.LOG.error("Agent configs initialization was failed", e);
            }
        }
    }

    @java.lang.Override
    public void addCluster(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        addCluster(clusterName, stackId, null);
    }

    @java.lang.Override
    public void addCluster(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.SecurityType securityType) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = null;
        if (getClustersByName().containsKey(clusterName)) {
            throw new org.apache.ambari.server.DuplicateResourceException(("Attempted to create a Cluster which already exists" + ", clusterName=") + clusterName);
        }
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = resourceTypeDAO.findById(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        if (resourceTypeEntity == null) {
            resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
            resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
            resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
            resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        }
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName(clusterName);
        clusterEntity.setDesiredStack(stackEntity);
        clusterEntity.setResource(resourceEntity);
        if (securityType != null) {
            clusterEntity.setSecurityType(securityType);
        }
        try {
            clusterDAO.create(clusterEntity);
        } catch (javax.persistence.RollbackException e) {
            org.apache.ambari.server.state.cluster.ClustersImpl.LOG.warn("Unable to create cluster " + clusterName, e);
            throw new org.apache.ambari.server.AmbariException("Unable to create cluster " + clusterName, e);
        }
        cluster = clusterFactory.create(clusterEntity);
        getClustersByName().put(clusterName, cluster);
        getClustersById().put(cluster.getClusterId(), cluster);
        getClusterHostsMap().put(clusterName, java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>()));
        cluster.setCurrentStackVersion(stackId);
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> addedClusters = new java.util.TreeMap<>();
        org.apache.ambari.server.agent.stomp.dto.TopologyCluster addedCluster = new org.apache.ambari.server.agent.stomp.dto.TopologyCluster();
        addedClusters.put(java.lang.Long.toString(cluster.getClusterId()), addedCluster);
        org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(addedClusters, org.apache.ambari.server.events.UpdateEventType.UPDATE);
        m_topologyHolder.get().updateData(topologyUpdateEvent);
        m_metadataHolder.get().updateData(m_ambariManagementController.get().getClusterMetadata(cluster));
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Cluster getCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = null;
        if (clusterName != null) {
            cluster = getClustersByName().get(clusterName);
        }
        if (null == cluster) {
            throw new org.apache.ambari.server.ClusterNotFoundException(clusterName);
        }
        org.apache.ambari.server.utils.RetryHelper.addAffectedCluster(cluster);
        return cluster;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Cluster getCluster(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = null;
        if (clusterId != null) {
            cluster = getClustersById().get(clusterId);
        }
        if (null == cluster) {
            throw new org.apache.ambari.server.ClusterNotFoundException(clusterId);
        }
        org.apache.ambari.server.utils.RetryHelper.addAffectedCluster(cluster);
        return cluster;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Cluster getClusterById(long id) throws org.apache.ambari.server.AmbariException {
        java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.state.Cluster> clustersById = getClustersById();
        org.apache.ambari.server.state.Cluster cluster = clustersById.get(id);
        if (null == cluster) {
            throw new org.apache.ambari.server.ClusterNotFoundException("clusterID=" + id);
        }
        return clustersById.get(id);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.Host> getHosts() {
        return new java.util.ArrayList<>(getHostsByName().values());
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.state.Cluster> getClustersForHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.Cluster> clusters = getHostClustersMap().get(hostname);
        if (clusters == null) {
            throw new org.apache.ambari.server.HostNotFoundException(hostname);
        }
        if (org.apache.ambari.server.state.cluster.ClustersImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClustersImpl.LOG.debug("Looking up clusters for hostname, hostname={}, mappedClusters={}", hostname, clusters.size());
        }
        return java.util.Collections.unmodifiableSet(clusters);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Host getHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host host = getHostsByName().get(hostname);
        if (null == host) {
            throw new org.apache.ambari.server.HostNotFoundException(hostname);
        }
        return host;
    }

    @java.lang.Override
    public boolean hostExists(java.lang.String hostname) {
        return getHostsByName().containsKey(hostname);
    }

    @java.lang.Override
    public boolean isHostMappedToCluster(long clusterId, java.lang.String hostName) {
        java.util.Set<org.apache.ambari.server.state.Cluster> clusters = getHostClustersMap().get(hostName);
        for (org.apache.ambari.server.state.Cluster cluster : clusters) {
            if (clusterId == cluster.getClusterId()) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Host getHostById(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        if (!getHostsById().containsKey(hostId)) {
            throw new org.apache.ambari.server.HostNotFoundException("Host Id = " + hostId);
        }
        return getHostsById().get(hostId);
    }

    @java.lang.Override
    public void updateHostMappings(org.apache.ambari.server.state.Host host) {
        java.lang.Long hostId = host.getHostId();
        if (null != hostId) {
            getHostsById().put(hostId, host);
            try {
                m_agentConfigsHolder.get().initializeDataIfNeeded(hostId, true);
            } catch (org.apache.ambari.server.AmbariRuntimeException e) {
                org.apache.ambari.server.state.cluster.ClustersImpl.LOG.error("Agent configs initialization was failed for host with id %s", hostId, e);
            }
        }
    }

    @java.lang.Override
    public void addHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        if (getHostsByName().containsKey(hostname)) {
            throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("Duplicate entry for Host {0}", hostname));
        }
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostname);
        hostEntity.setClusterEntities(new java.util.ArrayList<>());
        org.apache.ambari.server.state.Host host = hostFactory.create(hostEntity);
        host.setAgentVersion(new org.apache.ambari.server.state.AgentVersion(""));
        java.util.List<org.apache.ambari.server.agent.DiskInfo> emptyDiskList = new java.util.concurrent.CopyOnWriteArrayList<>();
        host.setDisksInfo(emptyDiskList);
        host.setHealthStatus(new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN, ""));
        host.setHostAttributes(new java.util.concurrent.ConcurrentHashMap<>());
        host.setState(org.apache.ambari.server.state.HostState.INIT);
        getHostsByName().put(hostname, host);
        getHostsById().put(host.getHostId(), host);
        getHostClustersMap().put(hostname, java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>()));
        if (org.apache.ambari.server.state.cluster.ClustersImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClustersImpl.LOG.debug("Adding a host to Clusters, hostname={}", hostname);
        }
    }

    @java.lang.Override
    public void updateHostWithClusterAndAttributes(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostClusters, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostAttributes) throws org.apache.ambari.server.AmbariException {
        if ((null == hostClusters) || hostClusters.isEmpty()) {
            return;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostMap = getHostsMap(hostClusters.keySet());
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHosts = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> hostClustersEntry : hostClusters.entrySet()) {
            java.util.Set<java.lang.String> hostClusterNames = hostClustersEntry.getValue();
            java.lang.String hostname = hostClustersEntry.getKey();
            org.apache.ambari.server.state.Host host = hostMap.get(hostname);
            java.util.Map<java.lang.String, java.lang.String> attributes = hostAttributes.get(hostname);
            if ((attributes != null) && (!attributes.isEmpty())) {
                host.setHostAttributes(attributes);
            }
            for (java.lang.String clusterName : hostClusterNames) {
                if ((clusterName != null) && (!clusterName.isEmpty())) {
                    if (!clusterHosts.containsKey(clusterName)) {
                        clusterHosts.put(clusterName, new java.util.HashSet<>());
                    }
                    clusterHosts.get(clusterName).add(hostname);
                }
            }
        }
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> clusterHostsEntry : clusterHosts.entrySet()) {
            java.util.Set<java.lang.String> clusterHostsNames = clusterHostsEntry.getValue();
            java.lang.String clusterName = clusterHostsEntry.getKey();
            mapAndPublishHostsToCluster(clusterHostsNames, clusterName);
        }
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> getHostsMap(java.util.Collection<java.lang.String> hostSet) throws org.apache.ambari.server.HostNotFoundException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.Host host = null;
        for (java.lang.String hostName : hostSet) {
            if (null != hostName) {
                host = getHostsByName().get(hostName);
                if (host == null) {
                    throw new org.apache.ambari.server.HostNotFoundException(hostName);
                }
            } else {
                throw new org.apache.ambari.server.HostNotFoundException(hostName);
            }
            hostMap.put(hostName, host);
        }
        return hostMap;
    }

    @java.lang.Override
    public void mapAndPublishHostsToCluster(java.util.Set<java.lang.String> hostnames, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        for (java.lang.String hostname : hostnames) {
            mapHostToCluster(hostname, clusterName);
        }
        publishAddingHostsToCluster(hostnames, clusterName);
        getCluster(clusterName).refresh();
    }

    private void publishAddingHostsToCluster(java.util.Set<java.lang.String> hostnames, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.HostsAddedEvent event = new org.apache.ambari.server.events.HostsAddedEvent(getCluster(clusterName).getClusterId(), hostnames);
        eventPublisher.publish(event);
    }

    @java.lang.Override
    public void mapHostToCluster(java.lang.String hostname, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host host = getHost(hostname);
        org.apache.ambari.server.state.Cluster cluster = getCluster(clusterName);
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Cluster>> hostClustersMap = getHostClustersMap();
        for (org.apache.ambari.server.state.Cluster c : hostClustersMap.get(hostname)) {
            if (c.getClusterName().equals(clusterName)) {
                throw new org.apache.ambari.server.DuplicateResourceException((("Attempted to create a host which already exists: clusterName=" + clusterName) + ", hostName=") + hostname);
            }
        }
        long clusterId = cluster.getClusterId();
        if (org.apache.ambari.server.state.cluster.ClustersImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClustersImpl.LOG.debug("Mapping host {} to cluster {} (id={})", hostname, clusterName, clusterId);
        }
        mapHostClusterEntities(hostname, clusterId);
        hostClustersMap.get(hostname).add(cluster);
        getClusterHostsMap().get(clusterName).add(host);
    }

    @com.google.inject.persist.Transactional
    void mapHostClusterEntities(java.lang.String hostName, java.lang.Long clusterId) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(clusterId);
        hostEntity.getClusterEntities().add(clusterEntity);
        clusterEntity.getHostEntities().add(hostEntity);
        clusterDAO.merge(clusterEntity);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> getClusters() {
        return java.util.Collections.unmodifiableMap(getClustersByName());
    }

    @java.lang.Override
    public void updateClusterName(java.lang.String oldName, java.lang.String newName) {
        java.util.concurrent.ConcurrentHashMap<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = getClustersByName();
        clusters.put(newName, clusters.remove(oldName));
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Host>> clusterHostsMap = getClusterHostsMap();
        clusterHostsMap.put(newName, clusterHostsMap.remove(oldName));
    }

    @java.lang.Override
    public void debugDump(java.lang.StringBuilder sb) {
        sb.append("Clusters=[ ");
        boolean first = true;
        for (org.apache.ambari.server.state.Cluster c : getClustersByName().values()) {
            if (!first) {
                sb.append(" , ");
            }
            first = false;
            sb.append("\n  ");
            c.debugDump(sb);
            sb.append(" ");
        }
        sb.append(" ]");
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> getHostsForCluster(java.lang.String clusterName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Host h : getClusterHostsMap().get(clusterName)) {
            hosts.put(h.getHostName(), h);
        }
        return hosts;
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> getHostIdsForCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Host h : getClusterHostsMap().get(clusterName)) {
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(h.getHostName());
            hosts.put(hostEntity.getHostId(), h);
        }
        return hosts;
    }

    @java.lang.Override
    public void deleteCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = getCluster(clusterName);
        if (!cluster.canBeRemoved()) {
            throw new org.apache.ambari.server.AmbariException(("Could not delete cluster" + ", clusterName=") + clusterName);
        }
        org.apache.ambari.server.state.cluster.ClustersImpl.LOG.info("Deleting cluster " + cluster.getClusterName());
        cluster.delete();
        for (java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet : getHostClustersMap().values()) {
            clusterSet.remove(cluster);
        }
        getClusterHostsMap().remove(cluster.getClusterName());
        getClustersByName().remove(clusterName);
    }

    @java.lang.Override
    public void unmapHostFromCluster(java.lang.String hostname, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.state.Cluster cluster = getCluster(clusterName);
        org.apache.ambari.server.state.Host host = getHost(hostname);
        unmapHostFromClusters(host, com.google.common.collect.Sets.newHashSet(cluster));
        cluster.refresh();
    }

    @com.google.inject.persist.Transactional
    void unmapHostFromClusters(org.apache.ambari.server.state.Host host, java.util.Set<org.apache.ambari.server.state.Cluster> clusters) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = null;
        if (clusters.isEmpty()) {
            return;
        }
        java.lang.String hostname = host.getHostName();
        hostEntity = hostDAO.findByName(hostname);
        for (org.apache.ambari.server.state.Cluster cluster : clusters) {
            long clusterId = cluster.getClusterId();
            if (org.apache.ambari.server.state.cluster.ClustersImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.cluster.ClustersImpl.LOG.debug("Unmapping host {} from cluster {} (id={})", hostname, cluster.getClusterName(), clusterId);
            }
            unmapHostClusterEntities(hostname, cluster.getClusterId());
            getHostClustersMap().get(hostname).remove(cluster);
            getClusterHostsMap().get(cluster.getClusterName()).remove(host);
        }
        deleteConfigGroupHostMapping(hostEntity.getHostId());
        kerberosKeytabPrincipalDAO.removeByHost(hostEntity.getHostId());
    }

    @com.google.inject.persist.Transactional
    void unmapHostClusterEntities(java.lang.String hostName, long clusterId) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(clusterId);
        hostEntity.getClusterEntities().remove(clusterEntity);
        clusterEntity.getHostEntities().remove(hostEntity);
        hostDAO.merge(hostEntity);
        clusterDAO.merge(clusterEntity, true);
    }

    @com.google.inject.persist.Transactional
    void deleteConfigGroupHostMapping(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.Cluster cluster : getClustersByName().values()) {
            for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : cluster.getConfigGroups().values()) {
                configGroup.removeHost(hostId);
            }
        }
    }

    @java.lang.Override
    public void deleteHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.Cluster> clusters = getHostClustersMap().get(hostname);
        if (clusters == null) {
            throw new org.apache.ambari.server.HostNotFoundException(hostname);
        }
        deleteHostEntityRelationships(hostname);
    }

    @java.lang.Override
    public void publishHostsDeletion(java.util.Set<java.lang.Long> hostIds, java.util.Set<java.lang.String> hostNames) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.HostsRemovedEvent event = new org.apache.ambari.server.events.HostsRemovedEvent(hostNames, hostIds);
        eventPublisher.publish(event);
    }

    @com.google.inject.persist.Transactional
    void deleteHostEntityRelationships(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        if (!getHostsByName().containsKey(hostname)) {
            throw new org.apache.ambari.server.HostNotFoundException("Could not find host " + hostname);
        }
        org.apache.ambari.server.orm.entities.HostEntity entity = hostDAO.findByName(hostname);
        if (entity == null) {
            return;
        }
        java.util.Set<org.apache.ambari.server.state.Cluster> clusters = getHostClustersMap().get(hostname);
        java.util.Set<java.lang.Long> clusterIds = com.google.common.collect.Sets.newHashSet();
        for (org.apache.ambari.server.state.Cluster cluster : clusters) {
            clusterIds.add(cluster.getClusterId());
        }
        org.apache.ambari.server.state.Host host = getHostsByName().get(hostname);
        unmapHostFromClusters(host, clusters);
        hostDAO.refresh(entity);
        hostVersionDAO.removeByHostName(hostname);
        if (entity.getHostRoleCommandEntities() != null) {
            for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrcEntity : entity.getHostRoleCommandEntities()) {
                org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity topologyLogicalTaskEnity = hrcEntity.getTopologyLogicalTaskEntity();
                if (topologyLogicalTaskEnity != null) {
                    topologyLogicalTaskDAO.remove(topologyLogicalTaskEnity);
                    hrcEntity.setTopologyLogicalTaskEntity(null);
                }
            }
        }
        topologyManager.removeHostRequests(hostname);
        entity.setHostStateEntity(null);
        hostStateDAO.removeByHostId(entity.getHostId());
        hostConfigMappingDAO.removeByHostId(entity.getHostId());
        serviceConfigDAO.removeHostFromServiceConfigs(entity.getHostId());
        requestOperationLevelDAO.removeByHostId(entity.getHostId());
        topologyHostInfoDAO.removeByHost(entity);
        getHostsByName().remove(hostname);
        getHostsById().remove(entity.getHostId());
        hostDAO.remove(entity);
    }

    @java.lang.Override
    public boolean checkPermission(java.lang.String clusterName, boolean readOnly) {
        org.apache.ambari.server.state.Cluster cluster = findCluster(clusterName);
        return ((cluster == null) && readOnly) || checkPermission(cluster, readOnly);
    }

    @java.lang.Override
    public void addSessionAttributes(java.lang.String name, java.util.Map<java.lang.String, java.lang.Object> attributes) {
        org.apache.ambari.server.state.Cluster cluster = findCluster(name);
        if (cluster != null) {
            cluster.addSessionAttributes(attributes);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> getSessionAttributes(java.lang.String name) {
        org.apache.ambari.server.state.Cluster cluster = findCluster(name);
        return cluster == null ? java.util.Collections.emptyMap() : cluster.getSessionAttributes();
    }

    @java.lang.Override
    public int getClusterSize(java.lang.String clusterName) {
        int hostCount = 0;
        java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.Set<org.apache.ambari.server.state.Host>> clusterHostsMap = getClusterHostsMap();
        java.util.Set<org.apache.ambari.server.state.Host> hosts = clusterHostsMap.get(clusterName);
        if (null != hosts) {
            hostCount = clusterHostsMap.get(clusterName).size();
        }
        return hostCount;
    }

    protected org.apache.ambari.server.state.Cluster findCluster(java.lang.String name) {
        org.apache.ambari.server.state.Cluster cluster = null;
        try {
            cluster = (name == null) ? null : getCluster(name);
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        return cluster;
    }

    private boolean checkPermission(org.apache.ambari.server.state.Cluster cluster, boolean readOnly) {
        for (org.springframework.security.core.GrantedAuthority grantedAuthority : securityHelper.getCurrentAuthorities()) {
            if (grantedAuthority instanceof org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) {
                org.apache.ambari.server.security.authorization.AmbariGrantedAuthority authority = ((org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) (grantedAuthority));
                org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = authority.getPrivilegeEntity();
                java.lang.Integer permissionId = privilegeEntity.getPermission().getId();
                if (permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION)) {
                    return true;
                }
                if (cluster != null) {
                    if (cluster.checkPermission(privilegeEntity, readOnly)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public void invalidate(org.apache.ambari.server.state.Cluster cluster) {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(cluster.getClusterId());
        org.apache.ambari.server.state.Cluster currentCluster = clusterFactory.create(clusterEntity);
        getClustersByName().put(clusterEntity.getClusterName(), currentCluster);
        getClustersById().put(currentCluster.getClusterId(), currentCluster);
    }

    @java.lang.Override
    public void invalidateAllClusters() {
        if (clustersByName != null) {
            java.util.Collection<org.apache.ambari.server.state.Cluster> clusters = clustersByName.values();
            for (org.apache.ambari.server.state.Cluster cluster : clusters) {
                invalidate(cluster);
            }
        }
    }
}