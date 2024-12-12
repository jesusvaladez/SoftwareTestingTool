package org.apache.ambari.server.state;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
public class ServiceComponentImpl implements org.apache.ambari.server.state.ServiceComponent {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.ServiceComponentImpl.class);

    private final org.apache.ambari.server.state.Service service;

    private final java.util.concurrent.locks.ReadWriteLock readWriteLock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    private final java.lang.String componentName;

    private java.lang.String displayName;

    private boolean isClientComponent;

    private boolean isMasterComponent;

    private boolean isVersionAdvertised;

    private final org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO;

    private final org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO;

    private final org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private final java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = new java.util.concurrent.ConcurrentHashMap<>();

    private final long desiredStateEntityId;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.assistedinject.AssistedInject
    public ServiceComponentImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Service service, @com.google.inject.assistedinject.Assisted
    java.lang.String componentName, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo, org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO, org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO, org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) throws org.apache.ambari.server.AmbariException {
        this.ambariMetaInfo = ambariMetaInfo;
        this.service = service;
        this.componentName = componentName;
        this.serviceComponentDesiredStateDAO = serviceComponentDesiredStateDAO;
        this.clusterServiceDAO = clusterServiceDAO;
        this.serviceComponentHostFactory = serviceComponentHostFactory;
        this.eventPublisher = eventPublisher;
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = new org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity();
        desiredStateEntity.setComponentName(componentName);
        desiredStateEntity.setDesiredState(org.apache.ambari.server.state.State.INIT);
        desiredStateEntity.setServiceName(service.getName());
        desiredStateEntity.setClusterId(service.getClusterId());
        desiredStateEntity.setRecoveryEnabled(false);
        desiredStateEntity.setDesiredRepositoryVersion(service.getDesiredRepositoryVersion());
        updateComponentInfo();
        persistEntities(desiredStateEntity);
        desiredStateEntityId = desiredStateEntity.getId();
    }

    @java.lang.Override
    public void updateComponentInfo() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
        try {
            org.apache.ambari.server.state.ComponentInfo compInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), service.getName(), componentName);
            isClientComponent = compInfo.isClient();
            isMasterComponent = compInfo.isMaster();
            isVersionAdvertised = compInfo.isVersionAdvertised();
            displayName = compInfo.getDisplayName();
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            throw new java.lang.RuntimeException(((((((("Trying to create a ServiceComponent" + (" not recognized in stack info" + ", clusterName=")) + service.getCluster().getClusterName()) + ", serviceName=") + service.getName()) + ", componentName=") + componentName) + ", stackInfo=") + stackId.getStackId());
        }
    }

    @com.google.inject.assistedinject.AssistedInject
    public ServiceComponentImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Service service, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo, org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO, org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO, org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO, org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) throws org.apache.ambari.server.AmbariException {
        this.service = service;
        this.serviceComponentDesiredStateDAO = serviceComponentDesiredStateDAO;
        this.clusterServiceDAO = clusterServiceDAO;
        this.serviceComponentHostFactory = serviceComponentHostFactory;
        this.eventPublisher = eventPublisher;
        this.ambariMetaInfo = ambariMetaInfo;
        desiredStateEntityId = serviceComponentDesiredStateEntity.getId();
        componentName = serviceComponentDesiredStateEntity.getComponentName();
        updateComponentInfo();
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities = hostComponentDesiredStateDAO.findByIndex(service.getClusterId(), service.getName(), serviceComponentDesiredStateEntity.getComponentName());
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> mappedHostComponentDesiredStateEntitites = hostComponentDesiredStateEntities.stream().collect(java.util.stream.Collectors.toMap(h -> h.getHostEntity().getHostName(), java.util.function.Function.identity()));
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity : serviceComponentDesiredStateEntity.getHostComponentStateEntities()) {
            try {
                hostComponents.put(hostComponentStateEntity.getHostName(), serviceComponentHostFactory.createExisting(this, hostComponentStateEntity, mappedHostComponentDesiredStateEntitites.get(hostComponentStateEntity.getHostName())));
            } catch (com.google.inject.ProvisionException ex) {
                org.apache.ambari.server.state.StackId currentStackId = getDesiredStackId();
                org.apache.ambari.server.state.ServiceComponentImpl.LOG.error(java.lang.String.format("Can not get host component info: stackName=%s, stackVersion=%s, serviceName=%s, componentName=%s, hostname=%s", currentStackId.getStackName(), currentStackId.getStackVersion(), service.getName(), serviceComponentDesiredStateEntity.getComponentName(), hostComponentStateEntity.getHostName()));
                ex.printStackTrace();
            }
        }
    }

    @java.lang.Override
    public java.lang.String getName() {
        return componentName;
    }

    @java.lang.Override
    public boolean isRecoveryEnabled() {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        if (desiredStateEntity != null) {
            return desiredStateEntity.isRecoveryEnabled();
        } else {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn((((("Trying to fetch a member from an entity object that may " + "have been previously deleted, serviceName = ") + service.getName()) + ", ") + "componentName = ") + componentName);
        }
        return false;
    }

    @java.lang.Override
    public void setRecoveryEnabled(boolean recoveryEnabled) {
        if (org.apache.ambari.server.state.ServiceComponentImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.debug("Setting RecoveryEnabled of Component, clusterName={}, clusterId={}, serviceName={}, componentName={}, oldRecoveryEnabled={}, newRecoveryEnabled={}", service.getCluster().getClusterName(), service.getCluster().getClusterId(), service.getName(), getName(), isRecoveryEnabled(), recoveryEnabled);
        }
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        if (desiredStateEntity != null) {
            desiredStateEntity.setRecoveryEnabled(recoveryEnabled);
            desiredStateEntity = serviceComponentDesiredStateDAO.merge(desiredStateEntity);
            org.apache.ambari.server.events.ServiceComponentRecoveryChangedEvent event = new org.apache.ambari.server.events.ServiceComponentRecoveryChangedEvent(getClusterId(), getClusterName(), getServiceName(), getName(), isRecoveryEnabled());
            eventPublisher.publish(event);
        } else {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn(("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + service.getName());
        }
    }

    @java.lang.Override
    public java.lang.String getServiceName() {
        return service.getName();
    }

    @java.lang.Override
    public java.lang.String getDisplayName() {
        return displayName;
    }

    @java.lang.Override
    public long getClusterId() {
        return service.getClusterId();
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts() {
        return new java.util.HashMap<>(hostComponents);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getServiceComponentsHosts() {
        java.util.Set<java.lang.String> serviceComponentsHosts = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : getServiceComponentHosts().values()) {
            serviceComponentsHosts.add(serviceComponentHost.getHostName());
        }
        return serviceComponentsHosts;
    }

    @java.lang.Override
    public void addServiceComponentHosts(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents) throws org.apache.ambari.server.AmbariException {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            if (!entry.getKey().equals(entry.getValue().getHostName())) {
                throw new org.apache.ambari.server.AmbariException("Invalid arguments in map" + ", hostname does not match the key in map");
            }
        }
        for (org.apache.ambari.server.state.ServiceComponentHost sch : hostComponents.values()) {
            addServiceComponentHost(sch);
        }
    }

    @java.lang.Override
    public void addServiceComponentHost(org.apache.ambari.server.state.ServiceComponentHost hostComponent) throws org.apache.ambari.server.AmbariException {
        readWriteLock.writeLock().lock();
        try {
            if (org.apache.ambari.server.state.ServiceComponentImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.ServiceComponentImpl.LOG.debug("Adding a ServiceComponentHost to ServiceComponent, clusterName={}, clusterId={}, serviceName={}, serviceComponentName={}, hostname={}, recoveryEnabled={}", service.getCluster().getClusterName(), service.getCluster().getClusterId(), service.getName(), getName(), hostComponent.getHostName(), isRecoveryEnabled());
            }
            if (hostComponents.containsKey(hostComponent.getHostName())) {
                throw new org.apache.ambari.server.AmbariException(((((((((((("Cannot add duplicate ServiceComponentHost" + ", clusterName=") + service.getCluster().getClusterName()) + ", clusterId=") + service.getCluster().getClusterId()) + ", serviceName=") + service.getName()) + ", serviceComponentName=") + getName()) + ", hostname=") + hostComponent.getHostName()) + ", recoveryEnabled=") + isRecoveryEnabled());
            }
            org.apache.ambari.server.state.cluster.ClusterImpl clusterImpl = ((org.apache.ambari.server.state.cluster.ClusterImpl) (service.getCluster()));
            clusterImpl.addServiceComponentHost(hostComponent);
            hostComponents.put(hostComponent.getHostName(), hostComponent);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ServiceComponentHost addServiceComponentHost(java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponentHost hostComponent = serviceComponentHostFactory.createNew(this, hostName);
        addServiceComponentHost(hostComponent);
        return hostComponent;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ServiceComponentHost getServiceComponentHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        if (!hostComponents.containsKey(hostname)) {
            throw new org.apache.ambari.server.ServiceComponentHostNotFoundException(getClusterName(), getServiceName(), getName(), hostname);
        }
        return hostComponents.get(hostname);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.State getDesiredState() {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        if (desiredStateEntity != null) {
            return desiredStateEntity.getDesiredState();
        } else {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn((((("Trying to fetch a member from an entity object that may " + "have been previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + componentName);
        }
        return null;
    }

    @java.lang.Override
    public void setDesiredState(org.apache.ambari.server.state.State state) {
        if (org.apache.ambari.server.state.ServiceComponentImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.debug("Setting DesiredState of Service, clusterName={}, clusterId={}, serviceName={}, serviceComponentName={}, oldDesiredState={}, newDesiredState={}", service.getCluster().getClusterName(), service.getCluster().getClusterId(), service.getName(), getName(), getDesiredState(), state);
        }
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        if (desiredStateEntity != null) {
            desiredStateEntity.setDesiredState(state);
            desiredStateEntity = serviceComponentDesiredStateDAO.merge(desiredStateEntity);
        } else {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn(("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + (service != null ? service.getName() : ""));
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.StackId getDesiredStackId() {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = desiredStateEntity.getDesiredStack();
        if (null != stackEntity) {
            return new org.apache.ambari.server.state.StackId(stackEntity.getStackName(), stackEntity.getStackVersion());
        } else {
            return null;
        }
    }

    @java.lang.Override
    public void setDesiredRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        if (desiredStateEntity != null) {
            desiredStateEntity.setDesiredRepositoryVersion(repositoryVersionEntity);
            desiredStateEntity = serviceComponentDesiredStateDAO.merge(desiredStateEntity);
        } else {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn(("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + (service != null ? service.getName() : ""));
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getDesiredRepositoryVersion() {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        return desiredStateEntity.getDesiredRepositoryVersion();
    }

    @java.lang.Override
    public java.lang.String getDesiredVersion() {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        return desiredStateEntity.getDesiredVersion();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceComponentResponse convertToResponse() {
        org.apache.ambari.server.state.Cluster cluster = service.getCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = getDesiredRepositoryVersion();
        org.apache.ambari.server.state.StackId desiredStackId = repositoryVersionEntity.getStackId();
        org.apache.ambari.server.controller.ServiceComponentResponse r = new org.apache.ambari.server.controller.ServiceComponentResponse(getClusterId(), cluster.getClusterName(), service.getName(), getName(), desiredStackId, getDesiredState().toString(), getServiceComponentStateCount(), isRecoveryEnabled(), displayName, repositoryVersionEntity.getVersion(), getRepositoryState());
        return r;
    }

    @java.lang.Override
    public java.lang.String getClusterName() {
        return service.getCluster().getClusterName();
    }

    @java.lang.Override
    public void debugDump(java.lang.StringBuilder sb) {
        sb.append("ServiceComponent={ serviceComponentName=").append(getName()).append(", recoveryEnabled=").append(isRecoveryEnabled()).append(", clusterName=").append(service.getCluster().getClusterName()).append(", clusterId=").append(service.getCluster().getClusterId()).append(", serviceName=").append(service.getName()).append(", desiredStackVersion=").append(getDesiredStackId()).append(", desiredState=").append(getDesiredState()).append(", hostcomponents=[ ");
        boolean first = true;
        for (org.apache.ambari.server.state.ServiceComponentHost sch : hostComponents.values()) {
            if (!first) {
                sb.append(" , ");
            }
            first = false;
            sb.append("\n        ");
            sch.debugDump(sb);
            sb.append(" ");
        }
        sb.append(" ] }");
    }

    @com.google.inject.persist.Transactional
    protected void persistEntities(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity) {
        org.apache.ambari.server.orm.entities.ClusterServiceEntityPK pk = new org.apache.ambari.server.orm.entities.ClusterServiceEntityPK();
        pk.setClusterId(service.getClusterId());
        pk.setServiceName(service.getName());
        org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity = clusterServiceDAO.findByPK(pk);
        desiredStateEntity.setClusterServiceEntity(serviceEntity);
        serviceComponentDesiredStateDAO.create(desiredStateEntity);
        serviceEntity.getServiceComponentDesiredStateEntities().add(desiredStateEntity);
        serviceEntity = clusterServiceDAO.merge(serviceEntity);
    }

    @java.lang.Override
    public boolean isClientComponent() {
        return isClientComponent;
    }

    @java.lang.Override
    public boolean isMasterComponent() {
        return isMasterComponent;
    }

    @java.lang.Override
    public boolean isVersionAdvertised() {
        return isVersionAdvertised;
    }

    @java.lang.Override
    public boolean canBeRemoved() {
        for (org.apache.ambari.server.state.ServiceComponentHost sch : hostComponents.values()) {
            if (!sch.canBeRemoved()) {
                org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn(((((((((("Found non removable hostcomponent when trying to" + (" delete service component" + ", clusterName=")) + getClusterName()) + ", serviceName=") + getServiceName()) + ", componentName=") + getName()) + ", state=") + sch.getState()) + ", hostname=") + sch.getHostName());
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void deleteAllServiceComponentHosts(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) {
        readWriteLock.writeLock().lock();
        try {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.info(((((((("Deleting all servicecomponenthosts for component" + ", clusterName=") + getClusterName()) + ", serviceName=") + getServiceName()) + ", componentName=") + getName()) + ", recoveryEnabled=") + isRecoveryEnabled());
            for (org.apache.ambari.server.state.ServiceComponentHost sch : hostComponents.values()) {
                if (!sch.canBeRemoved()) {
                    deleteMetaData.setAmbariException(new org.apache.ambari.server.AmbariException(((((((((("Found non removable hostcomponent " + ((" when trying to delete" + " all hostcomponents from servicecomponent") + ", clusterName=")) + getClusterName()) + ", serviceName=") + getServiceName()) + ", componentName=") + getName()) + ", recoveryEnabled=") + isRecoveryEnabled()) + ", hostname=") + sch.getHostName()));
                    return;
                }
            }
            for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : hostComponents.values()) {
                serviceComponentHost.delete(deleteMetaData);
            }
            hostComponents.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public void deleteServiceComponentHosts(java.lang.String hostname, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException {
        readWriteLock.writeLock().lock();
        try {
            org.apache.ambari.server.state.ServiceComponentHost sch = getServiceComponentHost(hostname);
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.info(((((((((((("Deleting servicecomponenthost for cluster" + ", clusterName=") + getClusterName()) + ", serviceName=") + getServiceName()) + ", componentName=") + getName()) + ", recoveryEnabled=") + isRecoveryEnabled()) + ", hostname=") + sch.getHostName()) + ", state=") + sch.getState());
            if (!sch.canBeRemoved()) {
                throw new org.apache.ambari.server.AmbariException(((((((((((("Current host component state prohibiting component removal." + ", clusterName=") + getClusterName()) + ", serviceName=") + getServiceName()) + ", componentName=") + getName()) + ", recoveryEnabled=") + isRecoveryEnabled()) + ", hostname=") + sch.getHostName()) + ", state=") + sch.getState());
            }
            sch.delete(deleteMetaData);
            hostComponents.remove(hostname);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void delete(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) {
        readWriteLock.writeLock().lock();
        try {
            deleteAllServiceComponentHosts(deleteMetaData);
            if (deleteMetaData.getAmbariException() != null) {
                return;
            }
            org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
            serviceComponentDesiredStateDAO.remove(desiredStateEntity);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void updateRepositoryState(java.lang.String reportedVersion) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> componentVersions = serviceComponentDesiredStateDAO.findVersions(getClusterId(), getServiceName(), getName());
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> map = new java.util.HashMap<>(com.google.common.collect.Maps.uniqueIndex(componentVersions, new com.google.common.base.Function<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity, java.lang.String>() {
            @java.lang.Override
            public java.lang.String apply(org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity input) {
                return input.getRepositoryVersion().getVersion();
            }
        }));
        if (org.apache.ambari.server.state.ServiceComponentImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.debug("Existing versions for {}/{}/{}: {}", getClusterName(), getServiceName(), getName(), map.keySet());
        }
        org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity componentVersion = map.get(reportedVersion);
        if (null == componentVersion) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = repoVersionDAO.findByStackAndVersion(getDesiredStackId(), reportedVersion);
            if (null != repoVersion) {
                componentVersion = new org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity();
                componentVersion.setRepositoryVersion(repoVersion);
                componentVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
                componentVersion.setUserName("auto-reported");
                serviceComponentDesiredStateEntity.setRepositoryState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
                serviceComponentDesiredStateEntity.addVersion(componentVersion);
                serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.merge(serviceComponentDesiredStateEntity);
                map.put(reportedVersion, componentVersion);
            } else {
                org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn("There is no repository available for stack {}, version {}", getDesiredStackId(), reportedVersion);
            }
        }
        if (org.apache.commons.collections.MapUtils.isNotEmpty(map)) {
            java.lang.String desiredVersion = serviceComponentDesiredStateEntity.getDesiredVersion();
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion = service.getDesiredRepositoryVersion();
            java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponents = hostComponentDAO.findByServiceAndComponentAndNotVersion(serviceComponentDesiredStateEntity.getServiceName(), serviceComponentDesiredStateEntity.getComponentName(), reportedVersion);
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.debug("{}/{} reportedVersion={}, desiredVersion={}, non-matching desired count={}, repo_state={}", serviceComponentDesiredStateEntity.getServiceName(), serviceComponentDesiredStateEntity.getComponentName(), reportedVersion, desiredVersion, hostComponents.size(), serviceComponentDesiredStateEntity.getRepositoryState());
            if (org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION.equals(desiredVersion)) {
                if (org.apache.commons.collections.CollectionUtils.isEmpty(hostComponents)) {
                    serviceComponentDesiredStateEntity.setDesiredRepositoryVersion(desiredRepositoryVersion);
                    serviceComponentDesiredStateEntity.setRepositoryState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
                } else {
                    serviceComponentDesiredStateEntity.setRepositoryState(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
                }
            } else if (!reportedVersion.equals(desiredVersion)) {
                serviceComponentDesiredStateEntity.setRepositoryState(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
            } else if (org.apache.commons.collections.CollectionUtils.isEmpty(hostComponents)) {
                serviceComponentDesiredStateEntity.setRepositoryState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
            }
            serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.merge(serviceComponentDesiredStateEntity);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.RepositoryVersionState getRepositoryState() {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity component = serviceComponentDesiredStateDAO.findById(desiredStateEntityId);
        if (null != component) {
            return component.getRepositoryState();
        } else {
            org.apache.ambari.server.state.ServiceComponentImpl.LOG.warn("Cannot retrieve repository state on component that may have been deleted: service {}, component {}", service != null ? service.getName() : null, componentName);
            return null;
        }
    }

    private int getSCHCountByState(org.apache.ambari.server.state.State state) {
        int count = 0;
        for (org.apache.ambari.server.state.ServiceComponentHost sch : hostComponents.values()) {
            if (sch.getState() == state) {
                count++;
            }
        }
        return count;
    }

    private int getMaintenanceOffSCHCountByState(org.apache.ambari.server.state.State state) {
        int count = 0;
        for (org.apache.ambari.server.state.ServiceComponentHost sch : hostComponents.values()) {
            try {
                org.apache.ambari.server.state.MaintenanceState effectiveMaintenanceState = maintenanceStateHelper.getEffectiveState(sch, sch.getHost());
                if ((sch.getState() == state) && (effectiveMaintenanceState == org.apache.ambari.server.state.MaintenanceState.OFF)) {
                    count++;
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    private java.util.Map<java.lang.String, java.lang.Integer> getServiceComponentStateCount() {
        java.util.Map<java.lang.String, java.lang.Integer> serviceComponentStateCountMap = new java.util.HashMap<>();
        serviceComponentStateCountMap.put("startedCount", getSCHCountByState(org.apache.ambari.server.state.State.STARTED));
        serviceComponentStateCountMap.put("installedCount", getSCHCountByState(org.apache.ambari.server.state.State.INSTALLED));
        serviceComponentStateCountMap.put("installedAndMaintenanceOffCount", getMaintenanceOffSCHCountByState(org.apache.ambari.server.state.State.INSTALLED));
        serviceComponentStateCountMap.put("installFailedCount", getSCHCountByState(org.apache.ambari.server.state.State.INSTALL_FAILED));
        serviceComponentStateCountMap.put("initCount", getSCHCountByState(org.apache.ambari.server.state.State.INIT));
        serviceComponentStateCountMap.put("unknownCount", getSCHCountByState(org.apache.ambari.server.state.State.UNKNOWN));
        serviceComponentStateCountMap.put("totalCount", hostComponents.size());
        return serviceComponentStateCountMap;
    }
}