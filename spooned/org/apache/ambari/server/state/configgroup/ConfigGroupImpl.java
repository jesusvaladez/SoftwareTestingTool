package org.apache.ambari.server.state.configgroup;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
public class ConfigGroupImpl implements org.apache.ambari.server.state.configgroup.ConfigGroup {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.configgroup.ConfigGroupImpl.class);

    private org.apache.ambari.server.state.Cluster cluster;

    private java.util.concurrent.ConcurrentMap<java.lang.Long, org.apache.ambari.server.state.Host> m_hosts;

    private java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.Config> m_configurations;

    private java.lang.String configGroupName;

    private java.lang.String serviceName;

    private long configGroupId;

    private final java.util.concurrent.locks.ReadWriteLock hostLock;

    private static final java.lang.String hostLockLabel = "configurationGroupHostLock";

    private final org.apache.ambari.server.orm.dao.ConfigGroupDAO configGroupDAO;

    private final org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO configGroupConfigMappingDAO;

    private final org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO configGroupHostMappingDAO;

    private final org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private final org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private final org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.assistedinject.AssistedInject
    public ConfigGroupImpl(@com.google.inject.assistedinject.Assisted("cluster")
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("serviceName")
    @javax.annotation.Nullable
    java.lang.String serviceName, @com.google.inject.assistedinject.Assisted("name")
    java.lang.String name, @com.google.inject.assistedinject.Assisted("tag")
    java.lang.String tag, @com.google.inject.assistedinject.Assisted("description")
    java.lang.String description, @com.google.inject.assistedinject.Assisted("configs")
    java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configurations, @com.google.inject.assistedinject.Assisted("hosts")
    java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.ConfigFactory configFactory, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ConfigGroupDAO configGroupDAO, org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO configGroupConfigMappingDAO, org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO configGroupHostMappingDAO, org.apache.ambari.server.logging.LockFactory lockFactory) throws org.apache.ambari.server.AmbariException {
        this.configFactory = configFactory;
        this.clusterDAO = clusterDAO;
        this.hostDAO = hostDAO;
        this.configGroupDAO = configGroupDAO;
        this.configGroupConfigMappingDAO = configGroupConfigMappingDAO;
        this.configGroupHostMappingDAO = configGroupHostMappingDAO;
        hostLock = lockFactory.newReadWriteLock(org.apache.ambari.server.state.configgroup.ConfigGroupImpl.hostLockLabel);
        this.cluster = cluster;
        this.serviceName = serviceName;
        configGroupName = name;
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = new org.apache.ambari.server.orm.entities.ConfigGroupEntity();
        configGroupEntity.setClusterId(cluster.getClusterId());
        configGroupEntity.setGroupName(name);
        configGroupEntity.setTag(tag);
        configGroupEntity.setDescription(description);
        configGroupEntity.setServiceName(serviceName);
        m_hosts = (hosts == null) ? new java.util.concurrent.ConcurrentHashMap<>() : new java.util.concurrent.ConcurrentHashMap<>(hosts);
        m_configurations = (configurations == null) ? new java.util.concurrent.ConcurrentHashMap<>() : new java.util.concurrent.ConcurrentHashMap<>(configurations);
        persist(configGroupEntity);
        configGroupId = configGroupEntity.getGroupId();
    }

    @com.google.inject.assistedinject.AssistedInject
    public ConfigGroupImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.ConfigFactory configFactory, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ConfigGroupDAO configGroupDAO, org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO configGroupConfigMappingDAO, org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO configGroupHostMappingDAO, org.apache.ambari.server.logging.LockFactory lockFactory) {
        this.configFactory = configFactory;
        this.clusterDAO = clusterDAO;
        this.hostDAO = hostDAO;
        this.configGroupDAO = configGroupDAO;
        this.configGroupConfigMappingDAO = configGroupConfigMappingDAO;
        this.configGroupHostMappingDAO = configGroupHostMappingDAO;
        hostLock = lockFactory.newReadWriteLock(org.apache.ambari.server.state.configgroup.ConfigGroupImpl.hostLockLabel);
        this.cluster = cluster;
        configGroupId = configGroupEntity.getGroupId();
        configGroupName = configGroupEntity.getGroupName();
        serviceName = configGroupEntity.getServiceName();
        m_configurations = new java.util.concurrent.ConcurrentHashMap<>();
        m_hosts = new java.util.concurrent.ConcurrentHashMap<>();
        for (org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configMappingEntity : configGroupEntity.getConfigGroupConfigMappingEntities()) {
            org.apache.ambari.server.state.Config config = cluster.getConfig(configMappingEntity.getConfigType(), configMappingEntity.getVersionTag());
            if (config != null) {
                m_configurations.put(config.getType(), config);
            } else {
                org.apache.ambari.server.state.configgroup.ConfigGroupImpl.LOG.warn("Unable to find config mapping {}/{} for config group in cluster {}", configMappingEntity.getConfigType(), configMappingEntity.getVersionTag(), cluster.getClusterName());
            }
        }
        for (org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity hostMappingEntity : configGroupEntity.getConfigGroupHostMappingEntities()) {
            try {
                org.apache.ambari.server.state.Host host = clusters.getHost(hostMappingEntity.getHostname());
                org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostMappingEntity.getHostEntity();
                if ((host != null) && (hostEntity != null)) {
                    m_hosts.put(hostEntity.getHostId(), host);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.configgroup.ConfigGroupImpl.LOG.warn("Host {} seems to be deleted but Config group {} mapping " + "still exists !", hostMappingEntity.getHostname(), configGroupName);
                org.apache.ambari.server.state.configgroup.ConfigGroupImpl.LOG.debug("Host seems to be deleted but Config group mapping still exists !", e);
            }
        }
    }

    @java.lang.Override
    public java.lang.Long getId() {
        return configGroupId;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return configGroupName;
    }

    @java.lang.Override
    public void setName(java.lang.String name) {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        configGroupEntity.setGroupName(name);
        configGroupDAO.merge(configGroupEntity);
        configGroupName = name;
    }

    @java.lang.Override
    public java.lang.String getClusterName() {
        return cluster.getClusterName();
    }

    @java.lang.Override
    public java.lang.String getTag() {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        return configGroupEntity.getTag();
    }

    @java.lang.Override
    public void setTag(java.lang.String tag) {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        configGroupEntity.setTag(tag);
        configGroupDAO.merge(configGroupEntity);
    }

    @java.lang.Override
    public java.lang.String getDescription() {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        return configGroupEntity.getDescription();
    }

    @java.lang.Override
    public void setDescription(java.lang.String description) {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        configGroupEntity.setDescription(description);
        configGroupDAO.merge(configGroupEntity);
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> getHosts() {
        return java.util.Collections.unmodifiableMap(m_hosts);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> getConfigurations() {
        return java.util.Collections.unmodifiableMap(m_configurations);
    }

    @java.lang.Override
    public void setHosts(java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts) {
        hostLock.writeLock().lock();
        try {
            replaceHostMappings(hosts);
            m_hosts = new java.util.concurrent.ConcurrentHashMap<>(hosts);
        } finally {
            hostLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public void setConfigurations(java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configurations) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = configGroupEntity.getClusterEntity();
        persistConfigMapping(clusterEntity, configGroupEntity, configurations);
        m_configurations = new java.util.concurrent.ConcurrentHashMap<>(configurations);
    }

    @java.lang.Override
    public void removeHost(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        hostLock.writeLock().lock();
        try {
            org.apache.ambari.server.state.Host host = m_hosts.get(hostId);
            if (null == host) {
                return;
            }
            java.lang.String hostName = host.getHostName();
            org.apache.ambari.server.state.configgroup.ConfigGroupImpl.LOG.info("Removing host (id={}, name={}) from config group", host.getHostId(), hostName);
            try {
                removeConfigGroupHostEntity(host);
                m_hosts.remove(hostId);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.configgroup.ConfigGroupImpl.LOG.error("Failed to delete config group host mapping for cluster {} and host {}", cluster.getClusterName(), hostName, e);
                throw new org.apache.ambari.server.AmbariException(e.getMessage());
            }
        } finally {
            hostLock.writeLock().unlock();
        }
    }

    @com.google.inject.persist.Transactional
    void removeConfigGroupHostEntity(org.apache.ambari.server.state.Host host) {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntityPK hostMappingEntityPK = new org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntityPK();
        hostMappingEntityPK.setHostId(host.getHostId());
        hostMappingEntityPK.setConfigGroupId(configGroupId);
        org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity configGroupHostMapping = configGroupHostMappingDAO.findByPK(hostMappingEntityPK);
        configGroupHostMappingDAO.remove(configGroupHostMapping);
        configGroupEntity.getConfigGroupHostMappingEntities().remove(configGroupHostMapping);
        configGroupEntity = configGroupDAO.merge(getConfigGroupEntity());
    }

    private void persist(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) throws org.apache.ambari.server.AmbariException {
        persistEntities(configGroupEntity);
        cluster.refresh();
    }

    @com.google.inject.persist.Transactional
    void persistEntities(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(cluster.getClusterId());
        configGroupEntity.setClusterEntity(clusterEntity);
        configGroupEntity.setTimestamp(java.lang.System.currentTimeMillis());
        configGroupDAO.create(configGroupEntity);
        configGroupId = configGroupEntity.getGroupId();
        persistConfigMapping(clusterEntity, configGroupEntity, m_configurations);
        replaceHostMappings(m_hosts);
    }

    @com.google.inject.persist.Transactional
    void replaceHostMappings(java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts) {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        configGroupHostMappingDAO.removeAllByGroup(configGroupEntity.getGroupId());
        configGroupEntity.setConfigGroupHostMappingEntities(new java.util.HashSet<>());
        if ((hosts != null) && (!hosts.isEmpty())) {
            configGroupEntity = persistHostMapping(hosts.values(), configGroupEntity);
        }
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.orm.entities.ConfigGroupEntity persistHostMapping(java.util.Collection<org.apache.ambari.server.state.Host> hosts, org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) {
        for (org.apache.ambari.server.state.Host host : hosts) {
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findById(host.getHostId());
            if (hostEntity != null) {
                org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity hostMappingEntity = new org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity();
                hostMappingEntity.setHostId(hostEntity.getHostId());
                hostMappingEntity.setHostEntity(hostEntity);
                hostMappingEntity.setConfigGroupEntity(configGroupEntity);
                hostMappingEntity.setConfigGroupId(configGroupEntity.getGroupId());
                configGroupEntity.getConfigGroupHostMappingEntities().add(hostMappingEntity);
                configGroupHostMappingDAO.create(hostMappingEntity);
            } else {
                org.apache.ambari.server.state.configgroup.ConfigGroupImpl.LOG.warn("The host {} has been removed from the cluster and cannot be added to the configuration group {}", host.getHostName(), configGroupName);
            }
        }
        return configGroupDAO.merge(configGroupEntity);
    }

    @com.google.inject.persist.Transactional
    void persistConfigMapping(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity, java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configurations) throws org.apache.ambari.server.AmbariException {
        configGroupConfigMappingDAO.removeAllByGroup(configGroupEntity.getGroupId());
        configGroupEntity.setConfigGroupConfigMappingEntities(new java.util.HashSet<>());
        if ((configurations != null) && (!configurations.isEmpty())) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Config> entry : configurations.entrySet()) {
                org.apache.ambari.server.state.Config config = entry.getValue();
                org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity = clusterDAO.findConfig(cluster.getClusterId(), config.getType(), config.getTag());
                if (clusterConfigEntity == null) {
                    java.lang.String serviceName = getServiceName();
                    org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                    config = configFactory.createNew(service.getDesiredStackId(), config.getType(), cluster, config.getTag(), config.getProperties(), config.getPropertiesAttributes(), true);
                    entry.setValue(config);
                    clusterConfigEntity = clusterDAO.findConfig(cluster.getClusterId(), config.getType(), config.getTag());
                }
                org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configMappingEntity = new org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity();
                configMappingEntity.setTimestamp(java.lang.System.currentTimeMillis());
                configMappingEntity.setClusterId(clusterEntity.getClusterId());
                configMappingEntity.setClusterConfigEntity(clusterConfigEntity);
                configMappingEntity.setConfigGroupEntity(configGroupEntity);
                configMappingEntity.setConfigGroupId(configGroupEntity.getGroupId());
                configMappingEntity.setConfigType(clusterConfigEntity.getType());
                configMappingEntity.setVersionTag(clusterConfigEntity.getTag());
                configGroupConfigMappingDAO.create(configMappingEntity);
                configGroupEntity.getConfigGroupConfigMappingEntities().add(configMappingEntity);
                configGroupEntity = configGroupDAO.merge(configGroupEntity);
            }
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void delete() {
        configGroupConfigMappingDAO.removeAllByGroup(configGroupId);
        configGroupHostMappingDAO.removeAllByGroup(configGroupId);
        configGroupDAO.removeByPK(configGroupId);
        cluster.refresh();
    }

    @java.lang.Override
    public void addHost(org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.AmbariException {
        hostLock.writeLock().lock();
        try {
            if (m_hosts.containsKey(host.getHostId())) {
                java.lang.String message = java.lang.String.format("Host %s is already associated with the configuration group %s", host.getHostName(), configGroupName);
                throw new org.apache.ambari.server.DuplicateResourceException(message);
            }
            org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
            persistHostMapping(java.util.Collections.singletonList(host), configGroupEntity);
            m_hosts.putIfAbsent(host.getHostId(), host);
        } finally {
            hostLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ConfigGroupResponse convertToResponse() throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostnames = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Host host : m_hosts.values()) {
            java.util.Map<java.lang.String, java.lang.Object> hostMap = new java.util.HashMap<>();
            hostMap.put("host_name", host.getHostName());
            hostnames.add(hostMap);
        }
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configObjMap = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Config config : m_configurations.values()) {
            java.util.Map<java.lang.String, java.lang.Object> configMap = new java.util.HashMap<>();
            configMap.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE, config.getType());
            configMap.put(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG, config.getTag());
            configObjMap.add(configMap);
        }
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        org.apache.ambari.server.controller.ConfigGroupResponse configGroupResponse = new org.apache.ambari.server.controller.ConfigGroupResponse(configGroupEntity.getGroupId(), cluster.getClusterName(), configGroupEntity.getGroupName(), configGroupEntity.getTag(), configGroupEntity.getDescription(), hostnames, configObjMap);
        return configGroupResponse;
    }

    @java.lang.Override
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @java.lang.Override
    public void setServiceName(java.lang.String serviceName) {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = getConfigGroupEntity();
        configGroupEntity.setServiceName(serviceName);
        configGroupDAO.merge(configGroupEntity);
        this.serviceName = serviceName;
    }

    private org.apache.ambari.server.orm.entities.ConfigGroupEntity getConfigGroupEntity() {
        return configGroupDAO.findById(configGroupId);
    }
}