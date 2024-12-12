package org.apache.ambari.server.state.cluster;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
public class ClusterImpl implements org.apache.ambari.server.state.Cluster {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.cluster.ClusterImpl.class);

    private static final org.slf4j.Logger configChangeLog = org.slf4j.LoggerFactory.getLogger("configchange");

    private static final java.lang.String CLUSTER_SESSION_ATTRIBUTES_PREFIX = "cluster_session_attributes:";

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.StackId desiredStackVersion;

    private final java.util.concurrent.ConcurrentSkipListMap<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.concurrent.ConcurrentSkipListMap<>();

    private final java.util.concurrent.ConcurrentMap<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.Config>> allConfigs = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.concurrent.ConcurrentMap<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>>> serviceComponentHosts = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.concurrent.ConcurrentMap<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> serviceComponentHostsByHost = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> clusterConfigGroups = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> requestExecutions = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.concurrent.locks.ReadWriteLock clusterGlobalLock;

    private final long clusterId;

    private java.lang.String clusterName;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterStateDAO clusterStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.logging.LockFactory lockFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostConfigMappingDAO hostConfigMappingDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.scheduler.RequestExecutionFactory requestExecutionFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController controller;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO alertDispatchDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariSessionManager sessionManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyRequestDAO topologyRequestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.STOMPComponentsDeleteHandler STOMPComponentsDeleteHandler;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.stomp.HostLevelParamsHolder hostLevelParamsHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private volatile com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceConfigTypes;

    private org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.JPAEventPublisher jpaEventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.RoleCommandOrderProvider roleCommandOrderProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory upgradeContextFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO;

    private java.util.Map<java.lang.String, java.lang.String> m_clusterPropertyCache = new java.util.concurrent.ConcurrentHashMap<>();

    @com.google.inject.Inject
    public ClusterImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, com.google.inject.Injector injector, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) throws org.apache.ambari.server.AmbariException {
        clusterId = clusterEntity.getClusterId();
        clusterName = clusterEntity.getClusterName();
        injector.injectMembers(this);
        clusterGlobalLock = lockFactory.newReadWriteLock("clusterGlobalLock");
        loadStackVersion();
        loadServices();
        loadServiceHostComponents();
        cacheConfigurations();
        loadConfigGroups();
        loadRequestExecutions();
        if (((desiredStackVersion != null) && (!org.apache.commons.lang.StringUtils.isEmpty(desiredStackVersion.getStackName()))) && (!org.apache.commons.lang.StringUtils.isEmpty(desiredStackVersion.getStackVersion()))) {
            loadServiceConfigTypes();
        }
        eventPublisher.register(this);
        this.eventPublisher = eventPublisher;
    }

    private void loadServiceConfigTypes() throws org.apache.ambari.server.AmbariException {
        try {
            serviceConfigTypes = collectServiceConfigTypesMapping();
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error("Cannot load stack info:", e);
            throw e;
        }
        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info("Service config types loaded: {}", serviceConfigTypes);
    }

    private com.google.common.collect.Multimap<java.lang.String, java.lang.String> collectServiceConfigTypesMapping() throws org.apache.ambari.server.AmbariException {
        com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceConfigTypes = com.google.common.collect.HashMultimap.create();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = null;
        try {
            serviceInfoMap = ambariMetaInfo.getServices(desiredStackVersion.getStackName(), desiredStackVersion.getStackVersion());
        } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error("Service config versioning disabled due to exception: ", e);
            return serviceConfigTypes;
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceInfo> entry : serviceInfoMap.entrySet()) {
            java.lang.String serviceName = entry.getKey();
            org.apache.ambari.server.state.ServiceInfo serviceInfo = entry.getValue();
            java.util.Set<java.lang.String> configTypes = serviceInfo.getConfigTypeAttributes().keySet();
            for (java.lang.String configType : configTypes) {
                serviceConfigTypes.put(serviceName, configType);
            }
        }
        return serviceConfigTypes;
    }

    private void loadServiceHostComponents() {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Service> serviceKV : services.entrySet()) {
            org.apache.ambari.server.state.Service service = serviceKV.getValue();
            if (!serviceComponentHosts.containsKey(service.getName())) {
                serviceComponentHosts.put(service.getName(), new java.util.concurrent.ConcurrentHashMap<>());
            }
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponent> svcComponent : service.getServiceComponents().entrySet()) {
                org.apache.ambari.server.state.ServiceComponent comp = svcComponent.getValue();
                java.lang.String componentName = svcComponent.getKey();
                if (!serviceComponentHosts.get(service.getName()).containsKey(componentName)) {
                    serviceComponentHosts.get(service.getName()).put(componentName, new java.util.concurrent.ConcurrentHashMap<>());
                }
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> svchost : comp.getServiceComponentHosts().entrySet()) {
                    java.lang.String hostname = svchost.getKey();
                    org.apache.ambari.server.state.ServiceComponentHost svcHostComponent = svchost.getValue();
                    if (!serviceComponentHostsByHost.containsKey(hostname)) {
                        serviceComponentHostsByHost.put(hostname, new java.util.concurrent.CopyOnWriteArrayList<>());
                    }
                    java.util.List<org.apache.ambari.server.state.ServiceComponentHost> compList = serviceComponentHostsByHost.get(hostname);
                    compList.add(svcHostComponent);
                    if (!serviceComponentHosts.get(service.getName()).get(componentName).containsKey(hostname)) {
                        serviceComponentHosts.get(service.getName()).get(componentName).put(hostname, svcHostComponent);
                    }
                }
            }
        }
    }

    private void loadServices() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        if (org.apache.commons.collections.CollectionUtils.isEmpty(clusterEntity.getClusterServiceEntities())) {
            return;
        }
        for (org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity : clusterEntity.getClusterServiceEntities()) {
            org.apache.ambari.server.state.StackId stackId = getCurrentStackVersion();
            try {
                if (ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceEntity.getServiceName()) != null) {
                    services.put(serviceEntity.getServiceName(), serviceFactory.createExisting(this, serviceEntity));
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error(java.lang.String.format("Can not get service info: stackName=%s, stackVersion=%s, serviceName=%s", stackId.getStackName(), stackId.getStackVersion(), serviceEntity.getServiceName()));
            }
        }
    }

    private void loadConfigGroups() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        if (!clusterEntity.getConfigGroupEntities().isEmpty()) {
            for (org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity : clusterEntity.getConfigGroupEntities()) {
                clusterConfigGroups.put(configGroupEntity.getGroupId(), configGroupFactory.createExisting(this, configGroupEntity));
            }
        }
    }

    private void loadRequestExecutions() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        if (!clusterEntity.getRequestScheduleEntities().isEmpty()) {
            for (org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity : clusterEntity.getRequestScheduleEntities()) {
                requestExecutions.put(scheduleEntity.getScheduleId(), requestExecutionFactory.createExisting(this, scheduleEntity));
            }
        }
    }

    @java.lang.Override
    public void addConfigGroup(org.apache.ambari.server.state.configgroup.ConfigGroup configGroup) throws org.apache.ambari.server.AmbariException {
        java.lang.String hostList = "";
        if (org.apache.ambari.server.state.cluster.ClusterImpl.LOG.isDebugEnabled()) {
            if (configGroup.getHosts() != null) {
                for (org.apache.ambari.server.state.Host host : configGroup.getHosts().values()) {
                    hostList += host.getHostName() + ", ";
                }
            }
        }
        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Adding a new Config group, clusterName = {}, groupName = {}, tag = {} with hosts {}", getClusterName(), configGroup.getName(), configGroup.getTag(), hostList);
        if (clusterConfigGroups.containsKey(configGroup.getId())) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Config group already exists, clusterName = {}, groupName = {}, groupId = {}, tag = {}", getClusterName(), configGroup.getName(), configGroup.getId(), configGroup.getTag());
        } else {
            clusterConfigGroups.put(configGroup.getId(), configGroup);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> getConfigGroups() {
        return java.util.Collections.unmodifiableMap(clusterConfigGroups);
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> getConfigGroupsByHostname(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> groupEntry : clusterConfigGroups.entrySet()) {
            java.lang.Long id = groupEntry.getKey();
            org.apache.ambari.server.state.configgroup.ConfigGroup group = groupEntry.getValue();
            for (org.apache.ambari.server.state.Host host : group.getHosts().values()) {
                if (org.apache.commons.lang.StringUtils.equals(hostname, host.getHostName())) {
                    configGroups.put(id, group);
                    break;
                }
            }
        }
        return configGroups;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.configgroup.ConfigGroup getConfigGroupsById(java.lang.Long configId) {
        return clusterConfigGroups.get(configId);
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> getConfigGroupsByServiceName(java.lang.String serviceName) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> groupEntry : clusterConfigGroups.entrySet()) {
            java.lang.Long id = groupEntry.getKey();
            org.apache.ambari.server.state.configgroup.ConfigGroup group = groupEntry.getValue();
            if (org.apache.commons.lang.StringUtils.equals(serviceName, group.getServiceName())) {
                configGroups.put(id, group);
            }
        }
        return configGroups;
    }

    @java.lang.Override
    public void addRequestExecution(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info(((((("Adding a new request schedule" + ", clusterName = ") + getClusterName()) + ", id = ") + requestExecution.getId()) + ", description = ") + requestExecution.getDescription());
        if (requestExecutions.containsKey(requestExecution.getId())) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Request schedule already exists, clusterName = {}, id = {}, description = {}", getClusterName(), requestExecution.getId(), requestExecution.getDescription());
        } else {
            requestExecutions.put(requestExecution.getId(), requestExecution);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> getAllRequestExecutions() {
        return java.util.Collections.unmodifiableMap(requestExecutions);
    }

    @java.lang.Override
    public void deleteRequestExecution(java.lang.Long id) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = requestExecutions.get(id);
        if (requestExecution == null) {
            throw new org.apache.ambari.server.AmbariException(("Request schedule does not exists, " + "id = ") + id);
        }
        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info(((((("Deleting request schedule" + ", clusterName = ") + getClusterName()) + ", id = ") + requestExecution.getId()) + ", description = ") + requestExecution.getDescription());
        requestExecution.delete();
        requestExecutions.remove(id);
    }

    @java.lang.Override
    public void deleteConfigGroup(java.lang.Long id) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = clusterConfigGroups.get(id);
        if (configGroup == null) {
            throw new org.apache.ambari.server.ConfigGroupNotFoundException(getClusterName(), id.toString());
        }
        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Deleting Config group, clusterName = {}, groupName = {}, groupId = {}, tag = {}", getClusterName(), configGroup.getName(), configGroup.getId(), configGroup.getTag());
        configGroup.delete();
        clusterConfigGroups.remove(id);
        configHelper.updateAgentConfigs(java.util.Collections.singleton(configGroup.getClusterName()));
    }

    public org.apache.ambari.server.state.ServiceComponentHost getServiceComponentHost(java.lang.String serviceName, java.lang.String serviceComponentName, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        if (((!serviceComponentHosts.containsKey(serviceName)) || (!serviceComponentHosts.get(serviceName).containsKey(serviceComponentName))) || (!serviceComponentHosts.get(serviceName).get(serviceComponentName).containsKey(hostname))) {
            throw new org.apache.ambari.server.ServiceComponentHostNotFoundException(getClusterName(), serviceName, serviceComponentName, hostname);
        }
        return serviceComponentHosts.get(serviceName).get(serviceComponentName).get(hostname);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts() {
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = new java.util.ArrayList<>();
        if (!serviceComponentHostsByHost.isEmpty()) {
            for (java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schList : serviceComponentHostsByHost.values()) {
                serviceComponentHosts.addAll(schList);
            }
        }
        return java.util.Collections.unmodifiableList(serviceComponentHosts);
    }

    @java.lang.Override
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @java.lang.Override
    public void setClusterName(java.lang.String clusterName) {
        java.lang.String oldName = null;
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        oldName = clusterEntity.getClusterName();
        clusterEntity.setClusterName(clusterName);
        clusterEntity = clusterDAO.merge(clusterEntity);
        clusters.updateClusterName(oldName, clusterName);
        this.clusterName = clusterName;
        if (!org.apache.commons.lang.StringUtils.equals(oldName, clusterName)) {
            org.apache.ambari.server.events.ClusterEvent clusterNameChangedEvent = new org.apache.ambari.server.events.ClusterEvent(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.CLUSTER_RENAME, clusterId);
            eventPublisher.publish(clusterNameChangedEvent);
        }
    }

    @java.lang.Override
    public java.lang.Long getResourceId() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = clusterEntity.getResource();
        if (resourceEntity == null) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn("There is no resource associated with this cluster:\n\tCluster Name: {}\n\tCluster ID: {}", getClusterName(), getClusterId());
            return null;
        } else {
            return resourceEntity.getId();
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void addServiceComponentHosts(java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHosts) {
            org.apache.ambari.server.state.Service service = getService(serviceComponentHost.getServiceName());
            org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(serviceComponentHost.getServiceComponentName());
            serviceComponent.addServiceComponentHost(serviceComponentHost);
        }
    }

    public void addServiceComponentHost(org.apache.ambari.server.state.ServiceComponentHost svcCompHost) throws org.apache.ambari.server.AmbariException {
        if (org.apache.ambari.server.state.cluster.ClusterImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Trying to add component {} of service {} on {} to the cache", svcCompHost.getServiceComponentName(), svcCompHost.getServiceName(), svcCompHost.getHostName());
        }
        final java.lang.String hostname = svcCompHost.getHostName();
        final java.lang.String serviceName = svcCompHost.getServiceName();
        final java.lang.String componentName = svcCompHost.getServiceComponentName();
        java.util.Set<org.apache.ambari.server.state.Cluster> cs = clusters.getClustersForHost(hostname);
        boolean clusterFound = false;
        java.util.Iterator<org.apache.ambari.server.state.Cluster> iter = cs.iterator();
        while (iter.hasNext()) {
            org.apache.ambari.server.state.Cluster c = iter.next();
            if (c.getClusterId() == getClusterId()) {
                clusterFound = true;
                break;
            }
        } 
        if (!clusterFound) {
            throw new org.apache.ambari.server.AmbariException(((((("Host does not belong this cluster" + ", hostname=") + hostname) + ", clusterName=") + getClusterName()) + ", clusterId=") + getClusterId());
        }
        if (!serviceComponentHosts.containsKey(serviceName)) {
            serviceComponentHosts.put(serviceName, new java.util.concurrent.ConcurrentHashMap<>());
        }
        if (!serviceComponentHosts.get(serviceName).containsKey(componentName)) {
            serviceComponentHosts.get(serviceName).put(componentName, new java.util.concurrent.ConcurrentHashMap<>());
        }
        if (serviceComponentHosts.get(serviceName).get(componentName).containsKey(hostname)) {
            throw new org.apache.ambari.server.AmbariException(((((("Duplicate entry for ServiceComponentHost" + ", serviceName=") + serviceName) + ", serviceComponentName") + componentName) + ", hostname= ") + hostname);
        }
        if (!serviceComponentHostsByHost.containsKey(hostname)) {
            serviceComponentHostsByHost.put(hostname, new java.util.concurrent.CopyOnWriteArrayList<>());
        }
        if (org.apache.ambari.server.state.cluster.ClusterImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Adding a new ServiceComponentHost, clusterName={}, clusterId={}, serviceName={}, serviceComponentName{}, hostname= {}", getClusterName(), getClusterId(), serviceName, componentName, hostname);
        }
        serviceComponentHosts.get(serviceName).get(componentName).put(hostname, svcCompHost);
        serviceComponentHostsByHost.get(hostname).add(svcCompHost);
    }

    @java.lang.Override
    public void removeServiceComponentHost(org.apache.ambari.server.state.ServiceComponentHost svcCompHost) throws org.apache.ambari.server.AmbariException {
        if (org.apache.ambari.server.state.cluster.ClusterImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Trying to remove component {} of service {} on {} from the cache", svcCompHost.getServiceComponentName(), svcCompHost.getServiceName(), svcCompHost.getHostName());
        }
        final java.lang.String hostname = svcCompHost.getHostName();
        final java.lang.String serviceName = svcCompHost.getServiceName();
        final java.lang.String componentName = svcCompHost.getServiceComponentName();
        java.util.Set<org.apache.ambari.server.state.Cluster> cs = clusters.getClustersForHost(hostname);
        boolean clusterFound = false;
        java.util.Iterator<org.apache.ambari.server.state.Cluster> iter = cs.iterator();
        while (iter.hasNext()) {
            org.apache.ambari.server.state.Cluster c = iter.next();
            if (c.getClusterId() == getClusterId()) {
                clusterFound = true;
                break;
            }
        } 
        if (!clusterFound) {
            throw new org.apache.ambari.server.AmbariException(((((("Host does not belong this cluster" + ", hostname=") + hostname) + ", clusterName=") + getClusterName()) + ", clusterId=") + getClusterId());
        }
        if (((!serviceComponentHosts.containsKey(serviceName)) || (!serviceComponentHosts.get(serviceName).containsKey(componentName))) || (!serviceComponentHosts.get(serviceName).get(componentName).containsKey(hostname))) {
            throw new org.apache.ambari.server.AmbariException(((((("Invalid entry for ServiceComponentHost" + ", serviceName=") + serviceName) + ", serviceComponentName") + componentName) + ", hostname= ") + hostname);
        }
        if (!serviceComponentHostsByHost.containsKey(hostname)) {
            throw new org.apache.ambari.server.AmbariException(((((("Invalid host entry for ServiceComponentHost" + ", serviceName=") + serviceName) + ", serviceComponentName") + componentName) + ", hostname= ") + hostname);
        }
        org.apache.ambari.server.state.ServiceComponentHost schToRemove = null;
        for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHostsByHost.get(hostname)) {
            if ((sch.getServiceName().equals(serviceName) && sch.getServiceComponentName().equals(componentName)) && sch.getHostName().equals(hostname)) {
                schToRemove = sch;
                break;
            }
        }
        if (schToRemove == null) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn(((((("Unavailable in per host cache. ServiceComponentHost" + ", serviceName=") + serviceName) + ", serviceComponentName") + componentName) + ", hostname= ") + hostname);
        }
        if (org.apache.ambari.server.state.cluster.ClusterImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Removing a ServiceComponentHost, clusterName={}, clusterId={}, serviceName={}, serviceComponentName{}, hostname= {}", getClusterName(), getClusterId(), serviceName, componentName, hostname);
        }
        serviceComponentHosts.get(serviceName).get(componentName).remove(hostname);
        if (schToRemove != null) {
            serviceComponentHostsByHost.get(hostname).remove(schToRemove);
        }
    }

    @java.lang.Override
    public long getClusterId() {
        return clusterId;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts(java.lang.String hostname) {
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = serviceComponentHostsByHost.get(hostname);
        if (null != serviceComponentHosts) {
            return new java.util.concurrent.CopyOnWriteArrayList<>(serviceComponentHosts);
        }
        return new java.util.ArrayList<>();
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getServiceComponentHostMap(java.util.Set<java.lang.String> hostNames, java.util.Set<java.lang.String> serviceNames) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap = new java.util.TreeMap<>();
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = getHosts();
        if (hosts != null) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                java.lang.String hostname = host.getHostName();
                if ((hostNames == null) || hostNames.contains(hostname)) {
                    java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = getServiceComponentHosts(hostname);
                    if (serviceComponentHosts != null) {
                        for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHosts) {
                            if ((serviceNames == null) || serviceNames.contains(sch.getServiceName())) {
                                java.lang.String component = sch.getServiceComponentName();
                                java.util.Set<java.lang.String> componentHosts = componentHostMap.get(component);
                                if (componentHosts == null) {
                                    componentHosts = new java.util.TreeSet<>();
                                    componentHostMap.put(component, componentHosts);
                                }
                                componentHosts.add(hostname);
                            }
                        }
                    }
                }
            }
        }
        return componentHostMap;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts(java.lang.String serviceName, java.lang.String componentName) {
        java.util.ArrayList<org.apache.ambari.server.state.ServiceComponentHost> foundItems = new java.util.ArrayList<>();
        java.util.concurrent.ConcurrentMap<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost>> foundByService = serviceComponentHosts.get(serviceName);
        if (foundByService != null) {
            if (componentName == null) {
                for (java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> foundByComponent : foundByService.values()) {
                    foundItems.addAll(foundByComponent.values());
                }
            } else if (foundByService.containsKey(componentName)) {
                foundItems.addAll(foundByService.get(componentName).values());
            }
        }
        return foundItems;
    }

    @java.lang.Override
    public void addService(org.apache.ambari.server.state.Service service) {
        if (org.apache.ambari.server.state.cluster.ClusterImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Adding a new Service, clusterName={}, clusterId={}, serviceName={}", getClusterName(), getClusterId(), service.getName());
        }
        services.put(service.getName(), service);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Service addService(java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException {
        if (services.containsKey(serviceName)) {
            java.lang.String message = java.text.MessageFormat.format("The {0} service already exists in {1}", serviceName, getClusterName());
            throw new org.apache.ambari.server.AmbariException(message);
        }
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
        org.apache.ambari.server.state.Service service = serviceFactory.createNew(this, serviceName, repositoryVersion);
        addService(service);
        return service;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Service getService(java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = services.get(serviceName);
        if (null == service) {
            throw new org.apache.ambari.server.ServiceNotFoundException(getClusterName(), serviceName);
        }
        return service;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> getServices() {
        return new java.util.HashMap<>(services);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Service getServiceByComponentName(java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.Service service : services.values()) {
            for (org.apache.ambari.server.state.ServiceComponent component : service.getServiceComponents().values()) {
                if (component.getName().equals(componentName)) {
                    return service;
                }
            }
        }
        throw new org.apache.ambari.server.ServiceNotFoundException(getClusterName(), "component: " + componentName);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.StackId getDesiredStackVersion() {
        return desiredStackVersion;
    }

    @java.lang.Override
    public void setDesiredStackVersion(org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        clusterGlobalLock.writeLock().lock();
        try {
            if (org.apache.ambari.server.state.cluster.ClusterImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Changing DesiredStackVersion of Cluster, clusterName={}, clusterId={}, currentDesiredStackVersion={}, newDesiredStackVersion={}", getClusterName(), getClusterId(), desiredStackVersion, stackId);
            }
            desiredStackVersion = stackId;
            org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
            clusterEntity.setDesiredStack(stackEntity);
            clusterDAO.merge(clusterEntity);
            loadServiceConfigTypes();
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.StackId getCurrentStackVersion() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity = clusterEntity.getClusterStateEntity();
        if (clusterStateEntity != null) {
            org.apache.ambari.server.orm.entities.StackEntity currentStackEntity = clusterStateEntity.getCurrentStack();
            return new org.apache.ambari.server.state.StackId(currentStackEntity);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.State getProvisioningState() {
        org.apache.ambari.server.state.State provisioningState;
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        provisioningState = clusterEntity.getProvisioningState();
        if (null == provisioningState) {
            provisioningState = org.apache.ambari.server.state.State.INIT;
        }
        return provisioningState;
    }

    @java.lang.Override
    public void setProvisioningState(org.apache.ambari.server.state.State provisioningState) {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        clusterEntity.setProvisioningState(provisioningState);
        clusterDAO.merge(clusterEntity);
    }

    private boolean setBlueprintProvisioningState(org.apache.ambari.server.state.BlueprintProvisioningState blueprintProvisioningState) {
        boolean updated = false;
        for (org.apache.ambari.server.state.Service s : getServices().values()) {
            for (org.apache.ambari.server.state.ServiceComponent sc : s.getServiceComponents().values()) {
                if (!sc.isClientComponent()) {
                    for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = sch.getDesiredStateEntity();
                        if (desiredStateEntity.getBlueprintProvisioningState() != blueprintProvisioningState) {
                            desiredStateEntity.setBlueprintProvisioningState(blueprintProvisioningState);
                            hostComponentDesiredStateDAO.merge(desiredStateEntity);
                            updated = true;
                        }
                    }
                }
            }
        }
        return updated;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.SecurityType getSecurityType() {
        org.apache.ambari.server.state.SecurityType securityType = null;
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        securityType = clusterEntity.getSecurityType();
        if (null == securityType) {
            securityType = org.apache.ambari.server.state.SecurityType.NONE;
        }
        return securityType;
    }

    @java.lang.Override
    public void setSecurityType(org.apache.ambari.server.state.SecurityType securityType) {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        clusterEntity.setSecurityType(securityType);
        clusterDAO.merge(clusterEntity);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public java.util.List<org.apache.ambari.server.state.Host> transitionHostsToInstalling(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity, org.apache.ambari.server.state.repository.VersionDefinitionXml versionDefinitionXml, boolean forceInstalled) throws org.apache.ambari.server.AmbariException {
        final java.util.List<org.apache.ambari.server.state.Host> hostsRequiringInstallation;
        clusterGlobalLock.writeLock().lock();
        try {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = clusters.getHostsForCluster(getClusterName());
            hostsRequiringInstallation = new java.util.ArrayList<>(hosts.size());
            java.util.Collection<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = getClusterEntity().getHostEntities();
            for (org.apache.ambari.server.orm.entities.HostEntity hostEntity : hostEntities) {
                org.apache.ambari.server.state.RepositoryVersionState state = org.apache.ambari.server.state.RepositoryVersionState.INSTALLING;
                if (forceInstalled) {
                    state = org.apache.ambari.server.state.RepositoryVersionState.INSTALLED;
                }
                org.apache.ambari.server.state.Host host = hosts.get(hostEntity.getHostName());
                if (!host.hasComponentsAdvertisingVersions(desiredStackVersion)) {
                    state = org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED;
                }
                if (state != org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED) {
                    if (repoVersionEntity.getType() != org.apache.ambari.spi.RepositoryType.STANDARD) {
                        boolean hostRequiresRepository = false;
                        org.apache.ambari.server.state.repository.ClusterVersionSummary clusterSummary = versionDefinitionXml.getClusterSummary(this, ambariMetaInfo);
                        java.util.Set<java.lang.String> servicesInUpgrade = clusterSummary.getAvailableServiceNames();
                        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = getServiceComponentHosts(hostEntity.getHostName());
                        for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : schs) {
                            java.lang.String serviceName = serviceComponentHost.getServiceName();
                            if (servicesInUpgrade.contains(serviceName)) {
                                hostRequiresRepository = true;
                                break;
                            }
                        }
                        if (!hostRequiresRepository) {
                            state = org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED;
                        }
                    }
                }
                if (state != org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED) {
                    if (host.getMaintenanceState(clusterId) != org.apache.ambari.server.state.MaintenanceState.OFF) {
                        state = org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC;
                    }
                }
                org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity = null;
                java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostEntity.getHostVersionEntities();
                for (org.apache.ambari.server.orm.entities.HostVersionEntity existingHostVersion : hostVersions) {
                    if (java.util.Objects.equals(existingHostVersion.getRepositoryVersion().getId(), repoVersionEntity.getId())) {
                        hostVersionEntity = existingHostVersion;
                        break;
                    }
                }
                if (null == hostVersionEntity) {
                    hostVersionEntity = new org.apache.ambari.server.orm.entities.HostVersionEntity(hostEntity, repoVersionEntity, state);
                    hostVersionDAO.create(hostVersionEntity);
                    hostVersions.add(hostVersionEntity);
                    hostDAO.merge(hostEntity);
                } else {
                    hostVersionEntity.setState(state);
                    hostVersionEntity = hostVersionDAO.merge(hostVersionEntity);
                }
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info("Created host version for {}, state={}, repository version={} (repo_id={})", hostVersionEntity.getHostName(), hostVersionEntity.getState(), repoVersionEntity.getVersion(), repoVersionEntity.getId());
                if (state == org.apache.ambari.server.state.RepositoryVersionState.INSTALLING) {
                    hostsRequiringInstallation.add(host);
                }
            }
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
        return hostsRequiringInstallation;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setCurrentStackVersion(org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
            org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity = clusterStateDAO.findByPK(clusterEntity.getClusterId());
            if (clusterStateEntity == null) {
                clusterStateEntity = new org.apache.ambari.server.orm.entities.ClusterStateEntity();
                clusterStateEntity.setClusterId(clusterEntity.getClusterId());
                clusterStateEntity.setCurrentStack(stackEntity);
                clusterStateEntity.setClusterEntity(clusterEntity);
                clusterStateDAO.create(clusterStateEntity);
                clusterStateEntity = clusterStateDAO.merge(clusterStateEntity);
                clusterEntity.setClusterStateEntity(clusterStateEntity);
                clusterDAO.merge(clusterEntity);
            } else {
                clusterStateEntity.setCurrentStack(stackEntity);
                clusterStateDAO.merge(clusterStateEntity);
                clusterDAO.merge(clusterEntity);
            }
        } catch (javax.persistence.RollbackException e) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn((("Unable to set version " + stackId) + " for cluster ") + getClusterName());
            throw new org.apache.ambari.server.AmbariException(((("Unable to set" + " version=") + stackId) + " for cluster ") + getClusterName(), e);
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> getConfigsByType(java.lang.String configType) {
        clusterGlobalLock.readLock().lock();
        try {
            if (!allConfigs.containsKey(configType)) {
                return null;
            }
            return java.util.Collections.unmodifiableMap(allConfigs.get(configType));
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Config getConfig(java.lang.String configType, java.lang.String versionTag) {
        clusterGlobalLock.readLock().lock();
        try {
            if ((!allConfigs.containsKey(configType)) || (!allConfigs.get(configType).containsKey(versionTag))) {
                return null;
            }
            return allConfigs.get(configType).get(versionTag);
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Config getDesiredConfigByType(java.lang.String configType, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) {
        org.apache.ambari.server.state.DesiredConfig desiredConfig = (desiredConfigs == null) ? null : desiredConfigs.get(configType);
        return desiredConfig == null ? getDesiredConfigByType(configType) : getConfig(configType, desiredConfig.getTag());
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.Config> getLatestConfigsWithTypes(java.util.Collection<java.lang.String> types) {
        return clusterDAO.getLatestConfigurationsWithTypes(clusterId, getDesiredStackVersion(), types).stream().map(clusterConfigEntity -> configFactory.createExisting(this, clusterConfigEntity)).collect(java.util.stream.Collectors.toList());
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Config getConfigByVersion(java.lang.String configType, java.lang.Long configVersion) {
        clusterGlobalLock.readLock().lock();
        try {
            if (!allConfigs.containsKey(configType)) {
                return null;
            }
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Config> entry : allConfigs.get(configType).entrySet()) {
                if (entry.getValue().getVersion().equals(configVersion)) {
                    return entry.getValue();
                }
            }
            return null;
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public void addConfig(org.apache.ambari.server.state.Config config) {
        if ((config.getType() == null) || config.getType().isEmpty()) {
            throw new java.lang.IllegalArgumentException("Config type cannot be empty");
        }
        clusterGlobalLock.writeLock().lock();
        try {
            if (!allConfigs.containsKey(config.getType())) {
                allConfigs.put(config.getType(), new java.util.concurrent.ConcurrentHashMap<>());
            }
            allConfigs.get(config.getType()).put(config.getTag(), config);
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.state.Config> getAllConfigs() {
        clusterGlobalLock.readLock().lock();
        try {
            java.util.List<org.apache.ambari.server.state.Config> list = new java.util.ArrayList<>();
            for (java.util.Map.Entry<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.Config>> entry : allConfigs.entrySet()) {
                list.addAll(entry.getValue().values());
            }
            return java.util.Collections.unmodifiableList(list);
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ClusterResponse convertToResponse() throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = getClusterName();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = clusters.getHostsForCluster(clusterName);
        return new org.apache.ambari.server.controller.ClusterResponse(getClusterId(), clusterName, getProvisioningState(), getSecurityType(), hosts.keySet(), hosts.size(), getDesiredStackVersion().getStackId(), getClusterHealthReport(hosts));
    }

    @java.lang.Override
    public void debugDump(java.lang.StringBuilder sb) {
        sb.append("Cluster={ clusterName=").append(getClusterName()).append(", clusterId=").append(getClusterId()).append(", desiredStackVersion=").append(desiredStackVersion.getStackId()).append(", services=[ ");
        boolean first = true;
        for (org.apache.ambari.server.state.Service s : services.values()) {
            if (!first) {
                sb.append(" , ");
            }
            first = false;
            sb.append("\n    ");
            s.debugDump(sb);
            sb.append(' ');
        }
        sb.append(" ] }");
        lockFactory.debugDump(sb);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void refresh() {
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
            clusterDAO.refresh(clusterEntity);
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void deleteAllServices() throws org.apache.ambari.server.AmbariException {
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info(("Deleting all services for cluster" + ", clusterName=") + getClusterName());
            for (org.apache.ambari.server.state.Service service : services.values()) {
                if (!service.canBeRemoved()) {
                    throw new org.apache.ambari.server.AmbariException(((("Found non removable service when trying to" + (" all services from cluster" + ", clusterName=")) + getClusterName()) + ", serviceName=") + service.getName());
                }
            }
            org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData = new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData();
            for (org.apache.ambari.server.state.Service service : services.values()) {
                deleteService(service, deleteMetaData);
                STOMPComponentsDeleteHandler.processDeleteByMetaDataException(deleteMetaData);
            }
            STOMPComponentsDeleteHandler.processDeleteCluster(getClusterId());
            services.clear();
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void deleteAllClusterConfigs() {
        clusterGlobalLock.writeLock().lock();
        try {
            java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigs = getClusterEntity().getClusterConfigEntities();
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigs) {
                clusterDAO.removeConfig(clusterConfigEntity);
            }
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public void deleteService(java.lang.String serviceName, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException {
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.state.Service service = getService(serviceName);
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info(((("Deleting service for cluster" + ", clusterName=") + getClusterName()) + ", serviceName=") + service.getName());
            if (!service.canBeRemoved()) {
                deleteMetaData.setAmbariException(new org.apache.ambari.server.AmbariException(((("Could not delete service from cluster" + ", clusterName=") + getClusterName()) + ", serviceName=") + service.getName()));
                return;
            }
            deleteService(service, deleteMetaData);
            services.remove(serviceName);
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    private void deleteService(org.apache.ambari.server.state.Service service, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) {
        final java.lang.String serviceName = service.getName();
        service.delete(deleteMetaData);
        if (deleteMetaData.getAmbariException() != null) {
            return;
        }
        serviceComponentHosts.remove(serviceName);
        for (java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponents : serviceComponentHostsByHost.values()) {
            com.google.common.collect.Iterables.removeIf(serviceComponents, new com.google.common.base.Predicate<org.apache.ambari.server.state.ServiceComponentHost>() {
                @java.lang.Override
                public boolean apply(org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost) {
                    return serviceComponentHost.getServiceName().equals(serviceName);
                }
            });
        }
    }

    @java.lang.Override
    public boolean canBeRemoved() {
        clusterGlobalLock.readLock().lock();
        try {
            boolean safeToRemove = true;
            for (org.apache.ambari.server.state.Service service : services.values()) {
                if (!service.canBeRemoved()) {
                    safeToRemove = false;
                    org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn(((("Found non removable service" + ", clusterName=") + getClusterName()) + ", serviceName=") + service.getName());
                }
            }
            return safeToRemove;
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void delete() throws org.apache.ambari.server.AmbariException {
        clusterGlobalLock.writeLock().lock();
        try {
            refresh();
            deleteAllServices();
            deleteAllClusterConfigs();
            resetHostVersions();
            refresh();
            removeEntities();
            allConfigs.clear();
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @com.google.inject.persist.Transactional
    protected void removeEntities() throws org.apache.ambari.server.AmbariException {
        long clusterId = getClusterId();
        alertDefinitionDAO.removeAll(clusterId);
        alertDispatchDAO.removeAllGroups(clusterId);
        upgradeDAO.removeAll(clusterId);
        topologyRequestDAO.removeAll(clusterId);
        clusterDAO.removeByPK(clusterId);
    }

    private void resetHostVersions() {
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersionDAO.findByCluster(getClusterName())) {
            if (!hostVersionEntity.getState().equals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED)) {
                hostVersionEntity.setState(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED);
                hostVersionDAO.merge(hostVersionEntity);
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceConfigVersionResponse addDesiredConfig(java.lang.String user, java.util.Set<org.apache.ambari.server.state.Config> configs) throws org.apache.ambari.server.AmbariException {
        return addDesiredConfig(user, configs, null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceConfigVersionResponse addDesiredConfig(java.lang.String user, java.util.Set<org.apache.ambari.server.state.Config> configs, java.lang.String serviceConfigVersionNote) throws org.apache.ambari.server.AmbariException {
        if (null == user) {
            throw new java.lang.NullPointerException("User must be specified.");
        }
        clusterGlobalLock.writeLock().lock();
        try {
            if (configs == null) {
                return null;
            }
            java.util.Iterator<org.apache.ambari.server.state.Config> configIterator = configs.iterator();
            while (configIterator.hasNext()) {
                org.apache.ambari.server.state.Config config = configIterator.next();
                if (config == null) {
                    configIterator.remove();
                    continue;
                }
                org.apache.ambari.server.state.Config currentDesired = getDesiredConfigByType(config.getType());
                if ((null != currentDesired) && currentDesired.getTag().equals(config.getTag())) {
                    configIterator.remove();
                }
            } 
            org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse = applyConfigs(configs, user, serviceConfigVersionNote);
            return serviceConfigVersionResponse;
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> getAllDesiredConfigVersions() {
        return getDesiredConfigs(true, true);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> getDesiredConfigs() {
        return getDesiredConfigs(true);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> getDesiredConfigs(boolean cachedConfigEntities) {
        java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> activeConfigsByType = getDesiredConfigs(false, cachedConfigEntities);
        return com.google.common.collect.Maps.transformEntries(activeConfigsByType, (key, value) -> value.iterator().next());
    }

    private java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> getDesiredConfigs(boolean allVersions, boolean cachedConfigEntities) {
        clusterGlobalLock.readLock().lock();
        try {
            java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> map = new java.util.HashMap<>();
            java.util.Collection<java.lang.String> types = new java.util.HashSet<>();
            java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> entities;
            if (cachedConfigEntities) {
                entities = getClusterEntity().getClusterConfigEntities();
            } else {
                entities = clusterDAO.getEnabledConfigs(clusterId);
            }
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntity : entities) {
                if (allVersions || configEntity.isSelected()) {
                    org.apache.ambari.server.state.DesiredConfig desiredConfig = new org.apache.ambari.server.state.DesiredConfig();
                    desiredConfig.setServiceName(null);
                    desiredConfig.setTag(configEntity.getTag());
                    if (!allConfigs.containsKey(configEntity.getType())) {
                        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error("An inconsistency exists for configuration {}", configEntity.getType());
                        continue;
                    }
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configMap = allConfigs.get(configEntity.getType());
                    if (!configMap.containsKey(configEntity.getTag())) {
                        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error("An inconsistency exists for the configuration {} with tag {}", configEntity.getType(), configEntity.getTag());
                        continue;
                    }
                    org.apache.ambari.server.state.Config config = configMap.get(configEntity.getTag());
                    desiredConfig.setVersion(config.getVersion());
                    java.util.Set<org.apache.ambari.server.state.DesiredConfig> configs = map.get(configEntity.getType());
                    if (configs == null) {
                        configs = new java.util.HashSet<>();
                    }
                    configs.add(desiredConfig);
                    map.put(configEntity.getType(), configs);
                    types.add(configEntity.getType());
                }
            }
            java.util.Map<java.lang.Long, java.lang.String> hostIdToName = new java.util.HashMap<>();
            if (!map.isEmpty()) {
                java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.orm.cache.HostConfigMapping>> hostMappingsByType = hostConfigMappingDAO.findSelectedHostsByTypes(clusterId, types);
                for (java.util.Map.Entry<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> entry : map.entrySet()) {
                    java.util.List<org.apache.ambari.server.state.DesiredConfig.HostOverride> hostOverrides = new java.util.ArrayList<>();
                    for (org.apache.ambari.server.orm.cache.HostConfigMapping mappingEntity : hostMappingsByType.get(entry.getKey())) {
                        if (!hostIdToName.containsKey(mappingEntity.getHostId())) {
                            org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findById(mappingEntity.getHostId());
                            hostIdToName.put(mappingEntity.getHostId(), hostEntity.getHostName());
                        }
                        hostOverrides.add(new org.apache.ambari.server.state.DesiredConfig.HostOverride(hostIdToName.get(mappingEntity.getHostId()), mappingEntity.getVersion()));
                    }
                    for (org.apache.ambari.server.state.DesiredConfig c : entry.getValue()) {
                        c.setHostOverrides(hostOverrides);
                    }
                }
            }
            return map;
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceConfigVersionResponse createServiceConfigVersion(java.lang.String serviceName, java.lang.String user, java.lang.String note, org.apache.ambari.server.state.configgroup.ConfigGroup configGroup) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
            if (configGroup != null) {
                serviceConfigEntity.setGroupId(configGroup.getId());
                java.util.Collection<org.apache.ambari.server.state.Config> configs = configGroup.getConfigurations().values();
                java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configEntities = new java.util.ArrayList<>(configs.size());
                for (org.apache.ambari.server.state.Config config : configs) {
                    configEntities.add(clusterDAO.findConfig(getClusterId(), config.getType(), config.getTag()));
                }
                serviceConfigEntity.setClusterConfigEntities(configEntities);
            } else {
                java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configEntities = getClusterConfigEntitiesByService(serviceName);
                serviceConfigEntity.setClusterConfigEntities(configEntities);
            }
            java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> changedConfigs = configHelper.getChangedConfigTypes(this, serviceConfigEntity, configGroup == null ? null : configGroup.getId(), clusterId, serviceName);
            long nextServiceConfigVersion = serviceConfigDAO.findNextServiceConfigVersion(clusterId, serviceName);
            org.apache.ambari.server.orm.entities.StackEntity stackEntity = clusterEntity.getDesiredStack();
            org.apache.ambari.server.state.Service service = services.get(serviceName);
            if (null != service) {
                org.apache.ambari.server.state.StackId serviceStackId = service.getDesiredStackId();
                stackEntity = stackDAO.find(serviceStackId);
            }
            serviceConfigEntity.setServiceName(serviceName);
            serviceConfigEntity.setClusterEntity(clusterEntity);
            serviceConfigEntity.setVersion(nextServiceConfigVersion);
            serviceConfigEntity.setUser(user);
            serviceConfigEntity.setNote(note);
            serviceConfigEntity.setStack(stackEntity);
            serviceConfigDAO.create(serviceConfigEntity);
            java.util.List<java.lang.String> groupHostNames = null;
            if (configGroup != null) {
                if (org.apache.commons.collections.MapUtils.isNotEmpty(configGroup.getHosts())) {
                    groupHostNames = configGroup.getHosts().entrySet().stream().map(h -> h.getValue().getHostName()).collect(java.util.stream.Collectors.toList());
                }
                serviceConfigEntity.setHostIds(new java.util.ArrayList<>(configGroup.getHosts().keySet()));
                serviceConfigEntity = serviceConfigDAO.merge(serviceConfigEntity);
            }
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.ConfigsUpdateEvent(serviceConfigEntity, configGroup == null ? null : configGroup.getName(), groupHostNames, changedConfigs.keySet()));
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
        java.lang.String configGroupName = (configGroup == null) ? org.apache.ambari.server.controller.ServiceConfigVersionResponse.DEFAULT_CONFIG_GROUP_NAME : configGroup.getName();
        org.apache.ambari.server.state.cluster.ClusterImpl.configChangeLog.info("(configchange) Creating config version. cluster: '{}', changed by: '{}', " + "service_name: '{}', config_group: '{}', config_group_id: '{}', version: '{}', create_timestamp: '{}', note: '{}'", getClusterName(), user, serviceName, configGroupName, configGroup == null ? "null" : configGroup.getId(), serviceConfigEntity.getVersion(), serviceConfigEntity.getCreateTimestamp(), serviceConfigEntity.getNote());
        org.apache.ambari.server.controller.ServiceConfigVersionResponse response = new org.apache.ambari.server.controller.ServiceConfigVersionResponse(serviceConfigEntity, configGroupName);
        return response;
    }

    @java.lang.Override
    public java.lang.String getServiceForConfigTypes(java.util.Collection<java.lang.String> configTypes) {
        java.util.List<java.lang.String> serviceNames = configTypes.stream().map(this::getServiceByConfigType).filter(java.util.Objects::nonNull).collect(java.util.stream.Collectors.toList());
        boolean allTheSame = new java.util.HashSet<>(serviceNames).size() <= 1;
        if (!allTheSame) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Config types: %s should belong to a single installed service. But they belong to: %s", configTypes, serviceNames));
        }
        return serviceNames.isEmpty() ? null : serviceNames.get(0);
    }

    public java.util.List<java.lang.String> serviceNameByConfigType(java.lang.String configType) {
        return serviceConfigTypes.entries().stream().filter(entry -> org.apache.commons.lang.StringUtils.equals(entry.getValue(), configType)).map(entry -> entry.getKey()).collect(java.util.stream.Collectors.toList());
    }

    @java.lang.Override
    public java.lang.String getServiceByConfigType(java.lang.String configType) {
        return serviceNameByConfigType(configType).stream().filter(this::isServiceInstalled).findFirst().orElse(null);
    }

    private boolean isServiceInstalled(java.lang.String serviceName) {
        return services.get(serviceName) != null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceConfigVersionResponse setServiceConfigVersion(java.lang.String serviceName, java.lang.Long version, java.lang.String user, java.lang.String note) throws org.apache.ambari.server.AmbariException {
        if (null == user) {
            throw new java.lang.NullPointerException("User must be specified.");
        }
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse = applyServiceConfigVersion(serviceName, version, user, note);
            return serviceConfigVersionResponse;
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> getActiveServiceConfigVersions() {
        clusterGlobalLock.readLock().lock();
        try {
            java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> map = new java.util.HashMap<>();
            java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> responses = getActiveServiceConfigVersionSet();
            for (org.apache.ambari.server.controller.ServiceConfigVersionResponse response : responses) {
                if (map.get(response.getServiceName()) == null) {
                    map.put(response.getServiceName(), new java.util.ArrayList<>());
                }
                map.get(response.getServiceName()).add(response);
            }
            return map;
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getServiceConfigVersions() {
        clusterGlobalLock.readLock().lock();
        try {
            java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> serviceConfigVersionResponses = new java.util.ArrayList<>();
            java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigs = serviceConfigDAO.getServiceConfigs(getClusterId());
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.ServiceConfigVersionResponse>> activeServiceConfigResponses = new java.util.HashMap<>();
            for (org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity : serviceConfigs) {
                org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse = convertToServiceConfigVersionResponse(serviceConfigEntity);
                java.util.Map<java.lang.String, org.apache.ambari.server.controller.ServiceConfigVersionResponse> activeServiceConfigResponseGroups = activeServiceConfigResponses.get(serviceConfigVersionResponse.getServiceName());
                if (activeServiceConfigResponseGroups == null) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.controller.ServiceConfigVersionResponse> serviceConfigGroups = new java.util.HashMap<>();
                    activeServiceConfigResponses.put(serviceConfigVersionResponse.getServiceName(), serviceConfigGroups);
                    activeServiceConfigResponseGroups = serviceConfigGroups;
                }
                org.apache.ambari.server.controller.ServiceConfigVersionResponse activeServiceConfigResponse = activeServiceConfigResponseGroups.get(serviceConfigVersionResponse.getGroupName());
                if ((activeServiceConfigResponse == null) && (!org.apache.ambari.server.controller.ServiceConfigVersionResponse.DELETED_CONFIG_GROUP_NAME.equals(serviceConfigVersionResponse.getGroupName()))) {
                    activeServiceConfigResponseGroups.put(serviceConfigVersionResponse.getGroupName(), serviceConfigVersionResponse);
                    activeServiceConfigResponse = serviceConfigVersionResponse;
                }
                if (serviceConfigEntity.getGroupId() == null) {
                    if (serviceConfigVersionResponse.getCreateTime() > activeServiceConfigResponse.getCreateTime()) {
                        activeServiceConfigResponseGroups.put(serviceConfigVersionResponse.getGroupName(), serviceConfigVersionResponse);
                    }
                } else if ((clusterConfigGroups != null) && clusterConfigGroups.containsKey(serviceConfigEntity.getGroupId())) {
                    if (serviceConfigVersionResponse.getVersion() > activeServiceConfigResponse.getVersion()) {
                        activeServiceConfigResponseGroups.put(serviceConfigVersionResponse.getGroupName(), serviceConfigVersionResponse);
                    }
                }
                serviceConfigVersionResponse.setIsCurrent(false);
                serviceConfigVersionResponses.add(getServiceConfigVersionResponseWithConfig(serviceConfigVersionResponse, serviceConfigEntity));
            }
            for (java.util.Map<java.lang.String, org.apache.ambari.server.controller.ServiceConfigVersionResponse> serviceConfigVersionResponseGroup : activeServiceConfigResponses.values()) {
                for (org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse : serviceConfigVersionResponseGroup.values()) {
                    serviceConfigVersionResponse.setIsCurrent(true);
                }
            }
            return serviceConfigVersionResponses;
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    private java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getActiveServiceConfigVersionSet() {
        java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> responses = new java.util.HashSet<>();
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> activeServiceConfigVersions = getActiveServiceConfigVersionEntities();
        for (org.apache.ambari.server.orm.entities.ServiceConfigEntity lastServiceConfig : activeServiceConfigVersions) {
            org.apache.ambari.server.controller.ServiceConfigVersionResponse response = convertToServiceConfigVersionResponse(lastServiceConfig);
            response.setIsCurrent(true);
            responses.add(response);
        }
        return responses;
    }

    private java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getActiveServiceConfigVersionEntities() {
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> activeServiceConfigVersions = new java.util.ArrayList<>();
        activeServiceConfigVersions.addAll(serviceConfigDAO.getLastServiceConfigs(getClusterId()));
        if (clusterConfigGroups != null) {
            activeServiceConfigVersions.addAll(serviceConfigDAO.getLastServiceConfigVersionsForGroups(clusterConfigGroups.keySet()));
        }
        return activeServiceConfigVersions;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getActiveServiceConfigVersionResponse(java.lang.String serviceName) {
        clusterGlobalLock.readLock().lock();
        try {
            java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> activeServiceConfigVersionEntities = new java.util.ArrayList<>();
            java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> activeServiceConfigVersionResponses = new java.util.ArrayList<>();
            activeServiceConfigVersionEntities.addAll(serviceConfigDAO.getLastServiceConfigsForService(getClusterId(), serviceName));
            for (org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity : activeServiceConfigVersionEntities) {
                org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse = getServiceConfigVersionResponseWithConfig(convertToServiceConfigVersionResponse(serviceConfigEntity), serviceConfigEntity);
                serviceConfigVersionResponse.setIsCurrent(true);
                activeServiceConfigVersionResponses.add(serviceConfigVersionResponse);
            }
            return activeServiceConfigVersionResponses;
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    private org.apache.ambari.server.controller.ServiceConfigVersionResponse getServiceConfigVersionResponseWithConfig(org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse, org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity) {
        serviceConfigVersionResponse.setConfigurations(new java.util.ArrayList<>());
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities = serviceConfigEntity.getClusterConfigEntities();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigEntities) {
            org.apache.ambari.server.state.Config config = allConfigs.get(clusterConfigEntity.getType()).get(clusterConfigEntity.getTag());
            serviceConfigVersionResponse.getConfigurations().add(new org.apache.ambari.server.controller.ConfigurationResponse(getClusterName(), config));
        }
        return serviceConfigVersionResponse;
    }

    @org.apache.ambari.server.orm.RequiresSession
    org.apache.ambari.server.controller.ServiceConfigVersionResponse getActiveServiceConfigVersion(java.lang.String serviceName) {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity lastServiceConfig = serviceConfigDAO.getLastServiceConfig(getClusterId(), serviceName);
        if (lastServiceConfig == null) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("No service config version found for service {}", serviceName);
            return null;
        }
        return convertToServiceConfigVersionResponse(lastServiceConfig);
    }

    @org.apache.ambari.server.orm.RequiresSession
    org.apache.ambari.server.controller.ServiceConfigVersionResponse convertToServiceConfigVersionResponse(org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity) {
        java.lang.Long groupId = serviceConfigEntity.getGroupId();
        java.lang.String groupName;
        if (groupId != null) {
            org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = null;
            if (clusterConfigGroups != null) {
                configGroup = clusterConfigGroups.get(groupId);
            }
            if (configGroup != null) {
                groupName = configGroup.getName();
            } else {
                groupName = org.apache.ambari.server.controller.ServiceConfigVersionResponse.DELETED_CONFIG_GROUP_NAME;
            }
        } else {
            groupName = org.apache.ambari.server.controller.ServiceConfigVersionResponse.DEFAULT_CONFIG_GROUP_NAME;
        }
        org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse = new org.apache.ambari.server.controller.ServiceConfigVersionResponse(serviceConfigEntity, groupName);
        return serviceConfigVersionResponse;
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.controller.ServiceConfigVersionResponse applyServiceConfigVersion(java.lang.String serviceName, java.lang.Long serviceConfigVersion, java.lang.String user, java.lang.String serviceConfigVersionNote) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = serviceConfigDAO.findByServiceAndVersion(serviceName, serviceConfigVersion);
        if (serviceConfigEntity == null) {
            throw new org.apache.ambari.server.ObjectNotFoundException("Service config version with serviceName={} and version={} not found");
        }
        java.lang.String configGroupName = null;
        if (serviceConfigEntity.getGroupId() == null) {
            java.util.Collection<java.lang.String> configTypes = serviceConfigTypes.get(serviceName);
            java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> enabledConfigs = clusterDAO.getEnabledConfigsByTypes(clusterId, configTypes);
            java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> serviceConfigEntities = serviceConfigEntity.getClusterConfigEntities();
            java.util.ArrayList<org.apache.ambari.server.orm.entities.ClusterConfigEntity> duplicatevalues = new java.util.ArrayList<>(serviceConfigEntities);
            duplicatevalues.retainAll(enabledConfigs);
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity enabledConfig : enabledConfigs) {
                if (!duplicatevalues.contains(enabledConfig)) {
                    enabledConfig.setSelected(false);
                    clusterDAO.merge(enabledConfig);
                }
            }
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntity : serviceConfigEntities) {
                if (!duplicatevalues.contains(configEntity)) {
                    configEntity.setSelected(true);
                    clusterDAO.merge(configEntity);
                }
            }
        } else {
            java.lang.Long configGroupId = serviceConfigEntity.getGroupId();
            org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = clusterConfigGroups.get(configGroupId);
            if (configGroup != null) {
                configGroupName = configGroup.getName();
                java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> groupDesiredConfigs = new java.util.HashMap<>();
                for (org.apache.ambari.server.orm.entities.ClusterConfigEntity entity : serviceConfigEntity.getClusterConfigEntities()) {
                    org.apache.ambari.server.state.Config config = allConfigs.get(entity.getType()).get(entity.getTag());
                    groupDesiredConfigs.put(config.getType(), config);
                }
                configGroup.setConfigurations(groupDesiredConfigs);
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> groupDesiredHosts = new java.util.HashMap<>();
                if (serviceConfigEntity.getHostIds() != null) {
                    for (java.lang.Long hostId : serviceConfigEntity.getHostIds()) {
                        org.apache.ambari.server.state.Host host = clusters.getHostById(hostId);
                        if (host != null) {
                            groupDesiredHosts.put(hostId, host);
                        } else {
                            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn("Host with id {} doesn't exist anymore, skipping", hostId);
                        }
                    }
                }
                configGroup.setHosts(groupDesiredHosts);
            } else {
                throw new java.lang.IllegalArgumentException("Config group {} doesn't exist");
            }
        }
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> changedConfigs = configHelper.getChangedConfigTypes(this, serviceConfigEntity, serviceConfigEntity.getGroupId(), clusterId, serviceName);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        long nextServiceConfigVersion = serviceConfigDAO.findNextServiceConfigVersion(clusterEntity.getClusterId(), serviceName);
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntityClone = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        serviceConfigEntityClone.setCreateTimestamp(java.lang.System.currentTimeMillis());
        serviceConfigEntityClone.setUser(user);
        serviceConfigEntityClone.setServiceName(serviceName);
        serviceConfigEntityClone.setClusterEntity(clusterEntity);
        serviceConfigEntityClone.setStack(serviceConfigEntity.getStack());
        serviceConfigEntityClone.setClusterConfigEntities(serviceConfigEntity.getClusterConfigEntities());
        serviceConfigEntityClone.setClusterId(serviceConfigEntity.getClusterId());
        serviceConfigEntityClone.setHostIds(serviceConfigEntity.getHostIds());
        serviceConfigEntityClone.setGroupId(serviceConfigEntity.getGroupId());
        serviceConfigEntityClone.setNote(serviceConfigVersionNote);
        serviceConfigEntityClone.setVersion(nextServiceConfigVersion);
        java.util.List<java.lang.String> groupHostNames = null;
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(serviceConfigEntity.getHostIds())) {
            groupHostNames = getHosts().stream().filter(h -> serviceConfigEntity.getHostIds().contains(h.getHostId())).map(h -> h.getHostName()).collect(java.util.stream.Collectors.toList());
        }
        serviceConfigDAO.create(serviceConfigEntityClone);
        STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.ConfigsUpdateEvent(serviceConfigEntityClone, configGroupName, groupHostNames, changedConfigs.keySet()));
        return convertToServiceConfigVersionResponse(serviceConfigEntityClone);
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.controller.ServiceConfigVersionResponse applyConfigs(java.util.Set<org.apache.ambari.server.state.Config> configs, java.lang.String user, java.lang.String serviceConfigVersionNote) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> appliedConfigs = new java.util.ArrayList<>();
        java.lang.String serviceName = getServiceForConfigTypes(configs.stream().map(org.apache.ambari.server.state.Config::getType).collect(java.util.stream.Collectors.toList()));
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigs = clusterEntity.getClusterConfigEntities();
        for (org.apache.ambari.server.state.Config config : configs) {
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigs) {
                if (org.apache.commons.lang.StringUtils.equals(clusterConfigEntity.getType(), config.getType())) {
                    clusterConfigEntity.setSelected(false);
                    if (org.apache.commons.lang.StringUtils.equals(clusterConfigEntity.getTag(), config.getTag())) {
                        appliedConfigs.add(clusterConfigEntity);
                        clusterConfigEntity.setSelected(true);
                    }
                }
            }
        }
        clusterDAO.merge(clusterConfigs);
        if (serviceName == null) {
            java.util.ArrayList<java.lang.String> configTypes = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.Config config : configs) {
                configTypes.add(config.getType());
            }
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.ConfigsUpdateEvent(this, appliedConfigs));
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error("No service found for config types '{}', service config version not created", configTypes);
            return null;
        } else {
            return createServiceConfigVersion(serviceName, user, serviceConfigVersionNote);
        }
    }

    private org.apache.ambari.server.controller.ServiceConfigVersionResponse createServiceConfigVersion(java.lang.String serviceName, java.lang.String user, java.lang.String serviceConfigVersionNote) throws org.apache.ambari.server.AmbariException {
        return createServiceConfigVersion(serviceName, user, serviceConfigVersionNote, null);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getClusterConfigEntitiesByService(java.lang.String serviceName) {
        java.util.Collection<java.lang.String> configTypes = serviceConfigTypes.get(serviceName);
        return clusterDAO.getEnabledConfigsByTypes(getClusterId(), new java.util.ArrayList<>(configTypes));
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Config getDesiredConfigByType(java.lang.String configType) {
        org.apache.ambari.server.orm.entities.ClusterConfigEntity config = clusterDAO.findEnabledConfigByType(getClusterId(), configType);
        if (null == config) {
            return null;
        }
        return getConfig(configType, config.getTag());
    }

    @java.lang.Override
    public boolean isConfigTypeExists(java.lang.String configType) {
        org.apache.ambari.server.orm.entities.ClusterConfigEntity config = clusterDAO.findEnabledConfigByType(getClusterId(), configType);
        return null != config;
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig>> getHostsDesiredConfigs(java.util.Collection<java.lang.Long> hostIds) {
        if ((hostIds == null) || hostIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> mappingEntities = hostConfigMappingDAO.findSelectedByHosts(hostIds);
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig>> desiredConfigsByHost = new java.util.HashMap<>();
        for (java.lang.Long hostId : hostIds) {
            desiredConfigsByHost.put(hostId, new java.util.HashMap<>());
        }
        for (org.apache.ambari.server.orm.cache.HostConfigMapping mappingEntity : mappingEntities) {
            org.apache.ambari.server.state.DesiredConfig desiredConfig = new org.apache.ambari.server.state.DesiredConfig();
            desiredConfig.setTag(mappingEntity.getVersion());
            desiredConfig.setServiceName(mappingEntity.getServiceName());
            desiredConfigsByHost.get(mappingEntity.getHostId()).put(mappingEntity.getType(), desiredConfig);
        }
        return desiredConfigsByHost;
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig>> getAllHostsDesiredConfigs() {
        java.util.Collection<java.lang.Long> hostIds;
        try {
            hostIds = clusters.getHostIdsForCluster(clusterName).keySet();
        } catch (org.apache.ambari.server.AmbariException ignored) {
            return java.util.Collections.emptyMap();
        }
        return getHostsDesiredConfigs(hostIds);
    }

    @java.lang.Override
    public java.lang.Long getNextConfigVersion(java.lang.String type) {
        return clusterDAO.findNextConfigVersion(clusterId, type);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.state.ServiceComponentHostEvent, java.lang.String> processServiceComponentHostEvents(com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHostEvent> eventMap) {
        clusterGlobalLock.readLock().lock();
        try {
            return processServiceComponentHostEventsInSingleTransaction(eventMap);
        } finally {
            clusterGlobalLock.readLock().unlock();
        }
    }

    @com.google.inject.persist.Transactional
    protected java.util.Map<org.apache.ambari.server.state.ServiceComponentHostEvent, java.lang.String> processServiceComponentHostEventsInSingleTransaction(com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHostEvent> eventMap) {
        java.util.Map<org.apache.ambari.server.state.ServiceComponentHostEvent, java.lang.String> failedEvents = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHostEvent> entry : eventMap.entries()) {
            java.lang.String serviceName = entry.getKey();
            org.apache.ambari.server.state.ServiceComponentHostEvent event = entry.getValue();
            java.lang.String serviceComponentName = event.getServiceComponentName();
            if (org.apache.commons.lang.StringUtils.isBlank(serviceName) || org.apache.ambari.server.controller.RootService.AMBARI.name().equals(serviceName)) {
                continue;
            }
            if (org.apache.commons.lang.StringUtils.isBlank(serviceComponentName)) {
                continue;
            }
            try {
                org.apache.ambari.server.state.Service service = getService(serviceName);
                org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(serviceComponentName);
                org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = serviceComponent.getServiceComponentHost(event.getHostName());
                serviceComponentHost.handleEvent(event);
            } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                java.lang.String message = java.lang.String.format("ServiceComponentHost lookup exception. Service not found for Service: %s. Error: %s", serviceName, e.getMessage());
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error(message);
                failedEvents.put(event, message);
            } catch (org.apache.ambari.server.ServiceComponentNotFoundException e) {
                java.lang.String message = java.lang.String.format("ServiceComponentHost lookup exception. Service Component not found for Service: %s, Component: %s. Error: %s", serviceName, serviceComponentName, e.getMessage());
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error(message);
                failedEvents.put(event, message);
            } catch (org.apache.ambari.server.ServiceComponentHostNotFoundException e) {
                java.lang.String message = java.lang.String.format("ServiceComponentHost lookup exception. Service Component Host not found for Service: %s, Component: %s, Host: %s. Error: %s", serviceName, serviceComponentName, event.getHostName(), e.getMessage());
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error(message);
                failedEvents.put(event, message);
            } catch (org.apache.ambari.server.AmbariException e) {
                java.lang.String message = java.lang.String.format("ServiceComponentHost lookup exception %s", e.getMessage());
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error(message);
                failedEvents.put(event, message);
            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error("Invalid transition ", e);
                boolean isFailure = true;
                java.lang.Enum<?> currentState = e.getCurrentState();
                java.lang.Enum<?> failedEvent = e.getEvent();
                if ((currentState == org.apache.ambari.server.state.State.STARTED) && (failedEvent == org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_START)) {
                    isFailure = false;
                    org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn("The start request for {} is invalid since the component is already started. Ignoring the request.", serviceComponentName);
                }
                if ((currentState == org.apache.ambari.server.state.State.UNKNOWN) && (failedEvent == org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS)) {
                    isFailure = false;
                    org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn("The host {} is in an unknown state; attempting to put {} back in progress.", event.getHostName(), serviceComponentName);
                }
                if (isFailure) {
                    failedEvents.put(event, java.lang.String.format("Invalid transition. %s", e.getMessage()));
                }
            }
        }
        return failedEvents;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getHosts(java.lang.String serviceName, java.lang.String componentName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> clusterServices = getServices();
        if (!clusterServices.containsKey(serviceName)) {
            return java.util.Collections.emptySet();
        }
        org.apache.ambari.server.state.Service service = clusterServices.get(serviceName);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components = service.getServiceComponents();
        if ((!components.containsKey(componentName)) || (components.get(componentName).getServiceComponentHosts().size() == 0)) {
            return java.util.Collections.emptySet();
        }
        return components.get(componentName).getServiceComponentHosts().keySet();
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Host getHost(final java.lang.String hostName) {
        if (org.apache.commons.lang.StringUtils.isEmpty(hostName)) {
            return null;
        }
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = getHosts();
        if (hosts != null) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                java.lang.String hostString = host.getHostName();
                if (hostName.equalsIgnoreCase(hostString)) {
                    return host;
                }
            }
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Host getHost(final java.lang.Long hostId) {
        if (hostId == null) {
            return null;
        }
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = getHosts();
        if (hosts != null) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                if (hostId.equals(host.getHostId())) {
                    return host;
                }
            }
        }
        return null;
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.state.Host> getHosts() {
        return clusters.getHostsForCluster(clusterName).values();
    }

    private org.apache.ambari.server.state.ClusterHealthReport getClusterHealthReport(java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHosts) throws org.apache.ambari.server.AmbariException {
        int staleConfigsHosts = 0;
        int maintenanceStateHosts = 0;
        int healthyStateHosts = 0;
        int unhealthyStateHosts = 0;
        int initStateHosts = 0;
        int healthyStatusHosts = 0;
        int unhealthyStatusHosts = 0;
        int unknownStatusHosts = 0;
        int alertStatusHosts = 0;
        int heartbeatLostStateHosts = 0;
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = getDesiredConfigs();
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = clusterHosts.values();
        java.util.Iterator<org.apache.ambari.server.state.Host> iterator = hosts.iterator();
        java.util.List<java.lang.Long> hostIds = hosts.stream().map(org.apache.ambari.server.state.Host::getHostId).collect(java.util.stream.Collectors.toList());
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities = (hostIds.isEmpty()) ? java.util.Collections.EMPTY_LIST : hostComponentDesiredStateDAO.findByHostsAndCluster(hostIds, clusterId);
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity>> mappedHostIds = hostComponentDesiredStateEntities.stream().collect(java.util.stream.Collectors.groupingBy(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity::getHostId, java.util.stream.Collectors.toMap(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity::getComponentName, java.util.function.Function.identity())));
        while (iterator.hasNext()) {
            org.apache.ambari.server.state.Host host = iterator.next();
            java.lang.String hostName = host.getHostName();
            switch (host.getState()) {
                case HEALTHY :
                    healthyStateHosts++;
                    break;
                case UNHEALTHY :
                    unhealthyStateHosts++;
                    break;
                case INIT :
                    initStateHosts++;
                    break;
                case HEARTBEAT_LOST :
                    heartbeatLostStateHosts++;
                    break;
            }
            switch (org.apache.ambari.server.state.HostHealthStatus.HealthStatus.valueOf(host.getStatus())) {
                case HEALTHY :
                    healthyStatusHosts++;
                    break;
                case UNHEALTHY :
                    unhealthyStatusHosts++;
                    break;
                case UNKNOWN :
                    unknownStatusHosts++;
                    break;
                case ALERT :
                    alertStatusHosts++;
                    break;
            }
            boolean staleConfig = false;
            boolean maintenanceState = false;
            if (serviceComponentHostsByHost.containsKey(hostName)) {
                java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> componentsStates = mappedHostIds.get(host.getHostId());
                for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHostsByHost.get(hostName)) {
                    org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity componentState = (componentsStates == null) ? null : componentsStates.get(sch.getServiceComponentName());
                    if (componentState != null) {
                        staleConfig = staleConfig || configHelper.isStaleConfigs(sch, desiredConfigs, componentState);
                    } else {
                        staleConfig = staleConfig || configHelper.isStaleConfigs(sch, desiredConfigs);
                    }
                    maintenanceState = maintenanceState || (maintenanceStateHelper.getEffectiveState(sch) != org.apache.ambari.server.state.MaintenanceState.OFF);
                }
            }
            if (staleConfig) {
                staleConfigsHosts++;
            }
            if (maintenanceState) {
                maintenanceStateHosts++;
            }
        } 
        org.apache.ambari.server.state.ClusterHealthReport chr = new org.apache.ambari.server.state.ClusterHealthReport();
        chr.setAlertStatusHosts(alertStatusHosts);
        chr.setHealthyStateHosts(healthyStateHosts);
        chr.setUnknownStatusHosts(unknownStatusHosts);
        chr.setUnhealthyStatusHosts(unhealthyStatusHosts);
        chr.setUnhealthyStateHosts(unhealthyStateHosts);
        chr.setStaleConfigsHosts(staleConfigsHosts);
        chr.setMaintenanceStateHosts(maintenanceStateHosts);
        chr.setInitStateHosts(initStateHosts);
        chr.setHeartbeatLostStateHosts(heartbeatLostStateHosts);
        chr.setHealthyStatusHosts(healthyStatusHosts);
        return chr;
    }

    @java.lang.Override
    public boolean checkPermission(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, boolean readOnly) {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = clusterEntity.getResource();
        if (resourceEntity != null) {
            java.lang.Integer permissionId = privilegeEntity.getPermission().getId();
            if (privilegeEntity.getResource().equals(resourceEntity)) {
                if ((readOnly && permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_USER_PERMISSION)) || permissionId.equals(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_ADMINISTRATOR_PERMISSION)) {
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public void addSessionAttributes(java.util.Map<java.lang.String, java.lang.Object> attributes) {
        if ((attributes != null) && (!attributes.isEmpty())) {
            java.util.Map<java.lang.String, java.lang.Object> sessionAttributes = new java.util.HashMap<>(getSessionAttributes());
            sessionAttributes.putAll(attributes);
            setSessionAttributes(attributes);
        }
    }

    @java.lang.Override
    public void setSessionAttribute(java.lang.String key, java.lang.Object value) {
        if ((key != null) && (!key.isEmpty())) {
            java.util.Map<java.lang.String, java.lang.Object> sessionAttributes = new java.util.HashMap<>(getSessionAttributes());
            sessionAttributes.put(key, value);
            setSessionAttributes(sessionAttributes);
        }
    }

    @java.lang.Override
    public void removeSessionAttribute(java.lang.String key) {
        if ((key != null) && (!key.isEmpty())) {
            java.util.Map<java.lang.String, java.lang.Object> sessionAttributes = new java.util.HashMap<>(getSessionAttributes());
            sessionAttributes.remove(key);
            setSessionAttributes(sessionAttributes);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> getSessionAttributes() {
        java.util.Map<java.lang.String, java.lang.Object> attributes = ((java.util.Map<java.lang.String, java.lang.Object>) (getSessionManager().getAttribute(getClusterSessionAttributeName())));
        return attributes == null ? java.util.Collections.emptyMap() : attributes;
    }

    protected org.apache.ambari.server.controller.AmbariSessionManager getSessionManager() {
        return sessionManager;
    }

    private void setSessionAttributes(java.util.Map<java.lang.String, java.lang.Object> sessionAttributes) {
        getSessionManager().setAttribute(getClusterSessionAttributeName(), sessionAttributes);
    }

    private java.lang.String getClusterSessionAttributeName() {
        return org.apache.ambari.server.state.cluster.ClusterImpl.CLUSTER_SESSION_ATTRIBUTES_PREFIX + getClusterName();
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void applyLatestConfigurations(org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName) {
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
            java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configEntities = clusterEntity.getClusterConfigEntities();
            com.google.common.collect.ImmutableMap<java.lang.Object, org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntityMap = com.google.common.collect.Maps.uniqueIndex(configEntities, com.google.common.base.Functions.identity());
            java.util.Set<java.lang.String> configTypesForService = new java.util.HashSet<>();
            java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> latestServiceConfigs = serviceConfigDAO.getLastServiceConfigsForService(getClusterId(), serviceName);
            for (org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfig : latestServiceConfigs) {
                java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> latestConfigs = serviceConfig.getClusterConfigEntities();
                for (org.apache.ambari.server.orm.entities.ClusterConfigEntity latestConfig : latestConfigs) {
                    latestConfig = clusterConfigEntityMap.get(latestConfig);
                    configTypesForService.add(latestConfig.getType());
                    org.apache.ambari.server.state.cluster.ClusterImpl.LOG.debug("Disabling configuration {} with tag {}", latestConfig.getType(), latestConfig.getTag());
                    latestConfig.setSelected(false);
                }
            }
            java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> latestConfigsByStack = clusterDAO.getLatestConfigurations(clusterId, stackId);
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity latestConfigByStack : latestConfigsByStack) {
                if (!configTypesForService.contains(latestConfigByStack.getType())) {
                    continue;
                }
                org.apache.ambari.server.orm.entities.ClusterConfigEntity entity = clusterConfigEntityMap.get(latestConfigByStack);
                entity.setSelected(true);
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info("Setting {} with version tag {} created on {} to selected for stack {}", entity.getType(), entity.getTag(), new java.util.Date(entity.getTimestamp()), stackId);
            }
            clusterDAO.merge(configEntities, true);
            cacheConfigurations();
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info("Applied latest configurations for {} on stack {}. The the following types were modified: {}", serviceName, stackId, org.apache.commons.lang.StringUtils.join(configTypesForService, ','));
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
        org.apache.ambari.server.events.jpa.EntityManagerCacheInvalidationEvent event = new org.apache.ambari.server.events.jpa.EntityManagerCacheInvalidationEvent();
        jpaEventPublisher.publish(event);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getConfigPropertiesTypes(java.lang.String configType) {
        return getConfigPropertiesTypes(configType, getCurrentStackVersion());
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getConfigPropertiesTypes(java.lang.String configType, org.apache.ambari.server.state.StackId stackId) {
        try {
            org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
            return stackInfo.getConfigPropertiesTypes(configType);
        } catch (org.apache.ambari.server.AmbariException ignored) {
        }
        return new java.util.HashMap<>();
    }

    @com.google.inject.persist.Transactional
    void removeAllConfigsForStack(org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName) {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        clusterDAO.refresh(clusterEntity);
        long clusterId = clusterEntity.getClusterId();
        java.util.Set<java.lang.String> removedConfigurationTypes = new java.util.HashSet<>();
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> removedClusterConfigs = new java.util.ArrayList<>(50);
        java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> allClusterConfigEntities = clusterEntity.getClusterConfigEntities();
        java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> allServiceConfigEntities = clusterEntity.getServiceConfigEntities();
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigs = serviceConfigDAO.getServiceConfigsForServiceAndStack(clusterId, stackId, serviceName);
        for (org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfig : serviceConfigs) {
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntity : serviceConfig.getClusterConfigEntities()) {
                removedConfigurationTypes.add(configEntity.getType());
                allClusterConfigEntities.remove(configEntity);
                clusterDAO.removeConfig(configEntity);
                removedClusterConfigs.add(configEntity);
            }
            serviceConfig.getClusterConfigEntities().clear();
            serviceConfigDAO.remove(serviceConfig);
            allServiceConfigEntities.remove(serviceConfig);
        }
        clusterEntity.setClusterConfigEntities(allClusterConfigEntities);
        clusterEntity = clusterDAO.merge(clusterEntity);
        org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info("Removed the following configuration types for {} on stack {}: {}", serviceName, stackId, org.apache.commons.lang.StringUtils.join(removedConfigurationTypes, ','));
    }

    @java.lang.Override
    public void removeConfigurations(org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName) {
        clusterGlobalLock.writeLock().lock();
        try {
            removeAllConfigsForStack(stackId, serviceName);
            cacheConfigurations();
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    private void cacheConfigurations() {
        clusterGlobalLock.writeLock().lock();
        try {
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
            allConfigs.clear();
            if (!clusterEntity.getClusterConfigEntities().isEmpty()) {
                for (org.apache.ambari.server.orm.entities.ClusterConfigEntity entity : clusterEntity.getClusterConfigEntities()) {
                    if (!allConfigs.containsKey(entity.getType())) {
                        allConfigs.put(entity.getType(), new java.util.concurrent.ConcurrentHashMap<>());
                    }
                    org.apache.ambari.server.state.Config config = configFactory.createExisting(this, entity);
                    allConfigs.get(entity.getType()).put(entity.getTag(), config);
                }
            }
        } finally {
            clusterGlobalLock.writeLock().unlock();
        }
    }

    private void loadStackVersion() {
        desiredStackVersion = new org.apache.ambari.server.state.StackId(getClusterEntity().getDesiredStack());
        if ((!org.apache.commons.lang.StringUtils.isEmpty(desiredStackVersion.getStackName())) && (!org.apache.commons.lang.StringUtils.isEmpty(desiredStackVersion.getStackVersion()))) {
            try {
                loadServiceConfigTypes();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
    }

    @java.lang.Override
    public boolean isBluePrintDeployed() {
        java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> topologyRequests = topologyRequestDAO.findByClusterId(getClusterId());
        for (org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequest : topologyRequests) {
            org.apache.ambari.server.topology.TopologyRequest.Type requestAction = org.apache.ambari.server.topology.TopologyRequest.Type.valueOf(topologyRequest.getAction());
            if (requestAction == org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterDAO.findById(clusterId);
    }

    @java.lang.Override
    public int getClusterSize() {
        return clusters.getClusterSize(clusterName);
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.UpgradeEntity getUpgradeInProgress() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
        return clusterEntity.getUpgradeEntity();
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setUpgradeEntity(org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity) throws org.apache.ambari.server.AmbariException {
        try {
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = getClusterEntity();
            clusterEntity.setUpgradeEntity(upgradeEntity);
            clusterDAO.merge(clusterEntity);
        } catch (javax.persistence.RollbackException e) {
            throw new org.apache.ambari.server.AmbariException("Unable to update the associated upgrade with the cluster", e);
        }
    }

    @java.lang.Override
    public boolean isUpgradeSuspended() {
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = getUpgradeInProgress();
        if (null != upgrade) {
            return upgrade.isSuspended();
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String getClusterProperty(java.lang.String propertyName, java.lang.String defaultValue) {
        java.lang.String cachedValue = m_clusterPropertyCache.get(propertyName);
        if (null != cachedValue) {
            return cachedValue;
        }
        cachedValue = defaultValue;
        org.apache.ambari.server.state.Config clusterEnv = getDesiredConfigByType(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV);
        if (null != clusterEnv) {
            java.util.Map<java.lang.String, java.lang.String> clusterEnvProperties = clusterEnv.getProperties();
            if (clusterEnvProperties.containsKey(propertyName)) {
                java.lang.String value = clusterEnvProperties.get(propertyName);
                if (null != value) {
                    cachedValue = value;
                }
            }
        }
        m_clusterPropertyCache.put(propertyName, cachedValue);
        return cachedValue;
    }

    boolean isClusterPropertyCached(java.lang.String propertyName) {
        return m_clusterPropertyCache.containsKey(propertyName);
    }

    @com.google.common.eventbus.Subscribe
    public void handleClusterEnvConfigChangedEvent(org.apache.ambari.server.events.ClusterConfigChangedEvent event) {
        if (!org.apache.commons.lang.StringUtils.equals(event.getConfigType(), org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV)) {
            return;
        }
        m_clusterPropertyCache.clear();
    }

    @com.google.common.eventbus.Subscribe
    public void onClusterProvisioned(org.apache.ambari.server.events.ClusterProvisionedEvent event) {
        if (event.getClusterId() == getClusterId()) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info("Removing temporary configurations after successful deployment of cluster id={} name={}", getClusterId(), getClusterName());
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> e : org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.TEMPORARY_PROPERTIES_FOR_CLUSTER_DEPLOYMENT.entrySet()) {
                try {
                    configHelper.updateConfigType(this, getCurrentStackVersion(), controller, e.getKey(), java.util.Collections.emptyMap(), e.getValue(), "internal", "Removing temporary configurations after successful deployment");
                    org.apache.ambari.server.state.cluster.ClusterImpl.LOG.info("Removed temporary configurations: {} / {}", e.getKey(), e.getValue());
                } catch (org.apache.ambari.server.AmbariException ex) {
                    org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn("Failed to remove temporary configurations: {} / {}", e.getKey(), e.getValue(), ex);
                }
            }
            changeBlueprintProvisioningState(org.apache.ambari.server.state.BlueprintProvisioningState.FINISHED);
        }
    }

    private void changeBlueprintProvisioningState(org.apache.ambari.server.state.BlueprintProvisioningState newState) {
        boolean updated = setBlueprintProvisioningState(newState);
        if (updated) {
            try {
                hostLevelParamsHolder.updateAllHosts();
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.state.cluster.ClusterImpl.LOG.error("Topology update failed after setting blueprint provision state to {}", newState, e);
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder() {
        return roleCommandOrderProvider.getRoleCommandOrder(this);
    }

    @java.lang.Override
    public void addSuspendedUpgradeParameters(java.util.Map<java.lang.String, java.lang.String> commandParams, java.util.Map<java.lang.String, java.lang.String> roleParams) {
        org.apache.ambari.server.orm.entities.UpgradeEntity suspendedUpgrade = getUpgradeInProgress();
        if (null == suspendedUpgrade) {
            org.apache.ambari.server.state.cluster.ClusterImpl.LOG.warn("An upgrade is not currently suspended. The command and role parameters will not be modified.");
            return;
        }
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = upgradeContextFactory.create(this, suspendedUpgrade);
        commandParams.putAll(upgradeContext.getInitializedCommandParameters());
        roleParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.UPGRADE_SUSPENDED, java.lang.Boolean.TRUE.toString().toLowerCase());
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getComponentVersionMap() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> componentVersionMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Service service : getServices().values()) {
            java.util.Map<java.lang.String, java.lang.String> componentMap = new java.util.HashMap<>();
            for (org.apache.ambari.server.state.ServiceComponent component : service.getServiceComponents().values()) {
                if (!component.isVersionAdvertised()) {
                    continue;
                }
                if (!component.getDesiredRepositoryVersion().isResolved()) {
                    continue;
                }
                componentMap.put(component.getName(), component.getDesiredVersion());
            }
            if (!componentMap.isEmpty()) {
                componentVersionMap.put(service.getName(), componentMap);
            }
        }
        return componentVersionMap;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.cluster.ClusterImpl cluster = ((org.apache.ambari.server.state.cluster.ClusterImpl) (o));
        return clusterId == cluster.clusterId;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(clusterId);
    }

    @java.lang.Override
    public org.apache.ambari.spi.ClusterInformation buildClusterInformation() {
        org.apache.ambari.server.state.SecurityType securityType = getSecurityType();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> topology = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = getServiceComponentHosts();
        for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHosts) {
            java.lang.String hash = (serviceComponentHost.getServiceName() + "/") + serviceComponentHost.getServiceComponentName();
            java.util.Set<java.lang.String> hosts = topology.get(hash);
            if (null == hosts) {
                hosts = com.google.common.collect.Sets.newTreeSet();
                topology.put(hash, hosts);
            }
            hosts.add(serviceComponentHost.getHostName());
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = getDesiredConfigs();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigEntry : desiredConfigs.entrySet()) {
            java.lang.String configType = desiredConfigEntry.getKey();
            org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigEntry.getValue();
            org.apache.ambari.server.state.Config clusterConfig = getConfig(configType, desiredConfig.getTag());
            configurations.put(configType, clusterConfig.getProperties());
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> clusterServices = getServices();
        java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> clusterServiceVersions = new java.util.HashMap<>();
        if (null != clusterServices) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Service> serviceEntry : clusterServices.entrySet()) {
                org.apache.ambari.server.state.Service service = serviceEntry.getValue();
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryEntity = service.getDesiredRepositoryVersion();
                org.apache.ambari.spi.RepositoryVersion desiredRepositoryVersion = desiredRepositoryEntity.getRepositoryVersion();
                clusterServiceVersions.put(serviceEntry.getKey(), desiredRepositoryVersion);
            }
        }
        return new org.apache.ambari.spi.ClusterInformation(getClusterName(), securityType == org.apache.ambari.server.state.SecurityType.KERBEROS, configurations, topology, clusterServiceVersions);
    }
}