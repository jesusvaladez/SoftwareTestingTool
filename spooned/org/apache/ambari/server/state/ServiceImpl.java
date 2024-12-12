package org.apache.ambari.server.state;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
public class ServiceImpl implements org.apache.ambari.server.state.Service {
    private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    private org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK serviceDesiredStateEntityPK;

    private org.apache.ambari.server.orm.entities.ClusterServiceEntityPK serviceEntityPK;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.ServiceImpl.class);

    private final org.apache.ambari.server.state.Cluster cluster;

    private final java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components = new java.util.concurrent.ConcurrentHashMap<>();

    private boolean isClientOnlyService;

    private boolean isCredentialStoreSupported;

    private boolean isCredentialStoreRequired;

    private final boolean ssoIntegrationSupported;

    private final org.apache.ambari.server.collections.Predicate ssoEnabledTest;

    private final boolean ldapIntegrationSupported;

    private final org.apache.ambari.server.collections.Predicate ldapEnabledTest;

    private final boolean ssoRequiresKerberos;

    private final org.apache.ambari.server.collections.Predicate kerberosEnabledTest;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private java.util.concurrent.atomic.AtomicReference<org.apache.ambari.server.state.MaintenanceState> maintenanceState = new java.util.concurrent.atomic.AtomicReference<>();

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.internal.AmbariServerSSOConfigurationHandler ambariServerSSOConfigurationHandler;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler ambariServerLDAPConfigurationHandler;

    private final org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO;

    private final org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO serviceDesiredStateDAO;

    private final org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private final org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    private final java.lang.String serviceName;

    private final java.lang.String displayName;

    @com.google.inject.assistedinject.AssistedInject
    ServiceImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted
    java.lang.String serviceName, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO, org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO serviceDesiredStateDAO, org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) throws org.apache.ambari.server.AmbariException {
        this.cluster = cluster;
        this.clusterDAO = clusterDAO;
        this.clusterServiceDAO = clusterServiceDAO;
        this.serviceDesiredStateDAO = serviceDesiredStateDAO;
        this.serviceComponentFactory = serviceComponentFactory;
        this.eventPublisher = eventPublisher;
        this.serviceName = serviceName;
        this.ambariMetaInfo = ambariMetaInfo;
        org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity = new org.apache.ambari.server.orm.entities.ClusterServiceEntity();
        serviceEntity.setClusterId(cluster.getClusterId());
        serviceEntity.setServiceName(serviceName);
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = new org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity();
        serviceDesiredStateEntity.setServiceName(serviceName);
        serviceDesiredStateEntity.setClusterId(cluster.getClusterId());
        serviceDesiredStateEntity.setDesiredRepositoryVersion(desiredRepositoryVersion);
        serviceDesiredStateEntityPK = getServiceDesiredStateEntityPK(serviceDesiredStateEntity);
        serviceEntityPK = getServiceEntityPK(serviceEntity);
        serviceDesiredStateEntity.setClusterServiceEntity(serviceEntity);
        serviceEntity.setServiceDesiredStateEntity(serviceDesiredStateEntity);
        org.apache.ambari.server.state.StackId stackId = desiredRepositoryVersion.getStackId();
        org.apache.ambari.server.state.ServiceInfo sInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
        displayName = sInfo.getDisplayName();
        isClientOnlyService = sInfo.isClientOnlyService();
        isCredentialStoreSupported = sInfo.isCredentialStoreSupported();
        isCredentialStoreRequired = sInfo.isCredentialStoreRequired();
        ssoIntegrationSupported = sInfo.isSingleSignOnSupported();
        ssoEnabledTest = compileSsoEnabledPredicate(sInfo);
        ssoRequiresKerberos = sInfo.isKerberosRequiredForSingleSignOnIntegration();
        kerberosEnabledTest = compileKerberosEnabledPredicate(sInfo);
        if ((ssoIntegrationSupported && ssoRequiresKerberos) && (kerberosEnabledTest == null)) {
            org.apache.ambari.server.state.ServiceImpl.LOG.warn("The service, {}, requires Kerberos to be enabled for SSO integration support; " + ("however, the kerberosEnabledTest specification has not been specified in the metainfo.xml file. " + "Automated SSO integration will not be allowed for this service."), serviceName);
        }
        ldapIntegrationSupported = sInfo.isLdapSupported();
        ldapEnabledTest = (org.apache.commons.lang.StringUtils.isNotBlank(sInfo.getLdapEnabledTest())) ? org.apache.ambari.server.collections.PredicateUtils.fromJSON(sInfo.getLdapEnabledTest()) : null;
        persist(serviceEntity);
    }

    @com.google.inject.assistedinject.AssistedInject
    ServiceImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO, org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO serviceDesiredStateDAO, org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) throws org.apache.ambari.server.AmbariException {
        this.cluster = cluster;
        this.clusterDAO = clusterDAO;
        this.clusterServiceDAO = clusterServiceDAO;
        this.serviceDesiredStateDAO = serviceDesiredStateDAO;
        this.serviceComponentFactory = serviceComponentFactory;
        this.eventPublisher = eventPublisher;
        serviceName = serviceEntity.getServiceName();
        this.ambariMetaInfo = ambariMetaInfo;
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = serviceEntity.getServiceDesiredStateEntity();
        serviceDesiredStateEntityPK = getServiceDesiredStateEntityPK(serviceDesiredStateEntity);
        serviceEntityPK = getServiceEntityPK(serviceEntity);
        if (!serviceEntity.getServiceComponentDesiredStateEntities().isEmpty()) {
            for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity : serviceEntity.getServiceComponentDesiredStateEntities()) {
                try {
                    components.put(serviceComponentDesiredStateEntity.getComponentName(), serviceComponentFactory.createExisting(this, serviceComponentDesiredStateEntity));
                } catch (com.google.inject.ProvisionException ex) {
                    org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(serviceComponentDesiredStateEntity.getDesiredStack());
                    org.apache.ambari.server.state.ServiceImpl.LOG.error(java.lang.String.format("Can not get component info: stackName=%s, stackVersion=%s, serviceName=%s, componentName=%s", stackId.getStackName(), stackId.getStackVersion(), serviceEntity.getServiceName(), serviceComponentDesiredStateEntity.getComponentName()));
                    ex.printStackTrace();
                }
            }
        }
        org.apache.ambari.server.state.StackId stackId = getDesiredStackId();
        org.apache.ambari.server.state.ServiceInfo sInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), getName());
        isClientOnlyService = sInfo.isClientOnlyService();
        isCredentialStoreSupported = sInfo.isCredentialStoreSupported();
        isCredentialStoreRequired = sInfo.isCredentialStoreRequired();
        displayName = sInfo.getDisplayName();
        ssoIntegrationSupported = sInfo.isSingleSignOnSupported();
        ssoEnabledTest = compileSsoEnabledPredicate(sInfo);
        ssoRequiresKerberos = sInfo.isKerberosRequiredForSingleSignOnIntegration();
        kerberosEnabledTest = compileKerberosEnabledPredicate(sInfo);
        if ((ssoIntegrationSupported && ssoRequiresKerberos) && (kerberosEnabledTest == null)) {
            org.apache.ambari.server.state.ServiceImpl.LOG.warn("The service, {}, requires Kerberos to be enabled for SSO integration support; " + ("however, the kerberosEnabledTest specification has not been specified in the metainfo.xml file. " + "Automated SSO integration will not be allowed for this service."), serviceName);
        }
        ldapIntegrationSupported = sInfo.isLdapSupported();
        ldapEnabledTest = (org.apache.commons.lang.StringUtils.isNotBlank(sInfo.getLdapEnabledTest())) ? org.apache.ambari.server.collections.PredicateUtils.fromJSON(sInfo.getLdapEnabledTest()) : null;
    }

    @java.lang.Override
    public void updateServiceInfo() throws org.apache.ambari.server.AmbariException {
        try {
            org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(this);
            isClientOnlyService = serviceInfo.isClientOnlyService();
            isCredentialStoreSupported = serviceInfo.isCredentialStoreSupported();
            isCredentialStoreRequired = serviceInfo.isCredentialStoreRequired();
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            throw new java.lang.RuntimeException(((((("Trying to create a ServiceInfo" + (" not recognized in stack info" + ", clusterName=")) + cluster.getClusterName()) + ", serviceName=") + getName()) + ", stackInfo=") + getDesiredStackId().getStackName());
        }
    }

    @java.lang.Override
    public java.lang.String getName() {
        return serviceName;
    }

    @java.lang.Override
    public java.lang.String getDisplayName() {
        return org.apache.commons.lang.StringUtils.isBlank(displayName) ? serviceName : displayName;
    }

    @java.lang.Override
    public long getClusterId() {
        return cluster.getClusterId();
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> getServiceComponents() {
        return new java.util.HashMap<>(components);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getServiceHosts() {
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.ServiceComponent serviceComponent : getServiceComponents().values()) {
            hostNames.addAll(serviceComponent.getServiceComponentsHosts());
        }
        return hostNames;
    }

    @java.lang.Override
    public void addServiceComponents(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.ServiceComponent sc : components.values()) {
            addServiceComponent(sc);
        }
    }

    @java.lang.Override
    public void addServiceComponent(org.apache.ambari.server.state.ServiceComponent component) throws org.apache.ambari.server.AmbariException {
        if (components.containsKey(component.getName())) {
            throw new org.apache.ambari.server.AmbariException(((((((("Cannot add duplicate ServiceComponent" + ", clusterName=") + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + getName()) + ", serviceComponentName=") + component.getName());
        }
        components.put(component.getName(), component);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ServiceComponent addServiceComponent(java.lang.String serviceComponentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(this, serviceComponentName);
        addServiceComponent(component);
        return component;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ServiceComponent getServiceComponent(java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = components.get(componentName);
        if (null == serviceComponent) {
            throw new org.apache.ambari.server.ServiceComponentNotFoundException(cluster.getClusterName(), getName(), componentName);
        }
        return serviceComponent;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.State getDesiredState() {
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = getServiceDesiredStateEntity();
        return serviceDesiredStateEntity.getDesiredState();
    }

    @java.lang.Override
    public void setDesiredState(org.apache.ambari.server.state.State state) {
        if (org.apache.ambari.server.state.ServiceImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.ServiceImpl.LOG.debug("Setting DesiredState of Service, clusterName={}, clusterId={}, serviceName={}, oldDesiredState={}, newDesiredState={}", cluster.getClusterName(), cluster.getClusterId(), getName(), getDesiredState(), state);
        }
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = getServiceDesiredStateEntity();
        serviceDesiredStateEntity.setDesiredState(state);
        serviceDesiredStateDAO.merge(serviceDesiredStateEntity);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.StackId getDesiredStackId() {
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = getServiceDesiredStateEntity();
        if (null == serviceDesiredStateEntity) {
            return null;
        } else {
            org.apache.ambari.server.orm.entities.StackEntity desiredStackEntity = serviceDesiredStateEntity.getDesiredStack();
            return new org.apache.ambari.server.state.StackId(desiredStackEntity);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getDesiredRepositoryVersion() {
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = getServiceDesiredStateEntity();
        return serviceDesiredStateEntity.getDesiredRepositoryVersion();
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setDesiredRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) {
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = getServiceDesiredStateEntity();
        serviceDesiredStateEntity.setDesiredRepositoryVersion(repositoryVersionEntity);
        serviceDesiredStateDAO.merge(serviceDesiredStateEntity);
        java.util.Collection<org.apache.ambari.server.state.ServiceComponent> components = getServiceComponents().values();
        for (org.apache.ambari.server.state.ServiceComponent component : components) {
            component.setDesiredRepositoryVersion(repositoryVersionEntity);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.RepositoryVersionState getRepositoryState() {
        if (components.isEmpty()) {
            return org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED;
        }
        java.util.List<org.apache.ambari.server.state.RepositoryVersionState> states = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceComponent component : components.values()) {
            states.add(component.getRepositoryState());
        }
        return org.apache.ambari.server.state.RepositoryVersionState.getAggregateState(states);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceResponse convertToResponse() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRespositoryVersion = getDesiredRepositoryVersion();
        org.apache.ambari.server.state.StackId desiredStackId = desiredRespositoryVersion.getStackId();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations;
        try {
            existingConfigurations = configHelper.calculateExistingConfigurations(ambariManagementController, cluster);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.state.ServiceImpl.LOG.warn("Failed to get the existing configurations for the cluster.  Predicate calculations may not be correct due to missing data.");
            existingConfigurations = java.util.Collections.emptyMap();
        }
        org.apache.ambari.server.controller.ServiceResponse r = new org.apache.ambari.server.controller.ServiceResponse(cluster.getClusterId(), cluster.getClusterName(), getName(), desiredStackId, desiredRespositoryVersion.getVersion(), getRepositoryState(), getDesiredState().toString(), isCredentialStoreSupported(), isCredentialStoreEnabled(), ssoIntegrationSupported, isSsoIntegrationDesired(), isSsoIntegrationEnabled(existingConfigurations), isKerberosRequiredForSsoIntegration(), isKerberosEnabled(existingConfigurations), ldapIntegrationSupported, isLdapIntegrationEnabeled(existingConfigurations), isLdapIntegrationDesired());
        r.setDesiredRepositoryVersionId(desiredRespositoryVersion.getId());
        r.setMaintenanceState(getMaintenanceState().name());
        return r;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Cluster getCluster() {
        return cluster;
    }

    @java.lang.Override
    public boolean isCredentialStoreSupported() {
        return isCredentialStoreSupported;
    }

    @java.lang.Override
    public boolean isCredentialStoreRequired() {
        return isCredentialStoreRequired;
    }

    @java.lang.Override
    public boolean isCredentialStoreEnabled() {
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity desiredStateEntity = getServiceDesiredStateEntity();
        if (desiredStateEntity != null) {
            return desiredStateEntity.isCredentialStoreEnabled();
        } else {
            org.apache.ambari.server.state.ServiceImpl.LOG.warn(("Trying to fetch a member from an entity object that may " + "have been previously deleted, serviceName = ") + getName());
        }
        return false;
    }

    @java.lang.Override
    public void setCredentialStoreEnabled(boolean credentialStoreEnabled) {
        if (org.apache.ambari.server.state.ServiceImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.ServiceImpl.LOG.debug("Setting CredentialStoreEnabled of Service, clusterName={}, clusterId={}, serviceName={}, oldCredentialStoreEnabled={}, newCredentialStoreEnabled={}", cluster.getClusterName(), cluster.getClusterId(), getName(), isCredentialStoreEnabled(), credentialStoreEnabled);
        }
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity desiredStateEntity = getServiceDesiredStateEntity();
        if (desiredStateEntity != null) {
            org.apache.ambari.server.events.ServiceCredentialStoreUpdateEvent serviceCredentialStoreUpdateEvent = null;
            if (desiredStateEntity.isCredentialStoreEnabled() != credentialStoreEnabled) {
                org.apache.ambari.server.state.StackId stackId = getDesiredStackId();
                serviceCredentialStoreUpdateEvent = new org.apache.ambari.server.events.ServiceCredentialStoreUpdateEvent(getClusterId(), stackId.getStackName(), stackId.getStackVersion(), getName());
            }
            desiredStateEntity.setCredentialStoreEnabled(credentialStoreEnabled);
            desiredStateEntity = serviceDesiredStateDAO.merge(desiredStateEntity);
            if (serviceCredentialStoreUpdateEvent != null) {
                eventPublisher.publish(serviceCredentialStoreUpdateEvent);
            }
        } else {
            org.apache.ambari.server.state.ServiceImpl.LOG.warn(("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getName());
        }
    }

    @java.lang.Override
    public void debugDump(java.lang.StringBuilder sb) {
        sb.append("Service={ serviceName=").append(getName()).append(", clusterName=").append(cluster.getClusterName()).append(", clusterId=").append(cluster.getClusterId()).append(", desiredStackVersion=").append(getDesiredStackId()).append(", desiredState=").append(getDesiredState()).append(", components=[ ");
        boolean first = true;
        for (org.apache.ambari.server.state.ServiceComponent sc : components.values()) {
            if (!first) {
                sb.append(" , ");
            }
            first = false;
            sb.append("\n      ");
            sc.debugDump(sb);
            sb.append(" ");
        }
        sb.append(" ] }");
    }

    private void persist(org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity) {
        persistEntities(serviceEntity);
        org.apache.ambari.server.state.StackId stackId = getDesiredStackId();
        cluster.addService(this);
        org.apache.ambari.server.events.ServiceInstalledEvent event = new org.apache.ambari.server.events.ServiceInstalledEvent(getClusterId(), stackId.getStackName(), stackId.getStackVersion(), getName());
        eventPublisher.publish(event);
    }

    @com.google.inject.persist.Transactional
    void persistEntities(org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity) {
        long clusterId = cluster.getClusterId();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(clusterId);
        serviceEntity.setClusterEntity(clusterEntity);
        clusterServiceDAO.create(serviceEntity);
        clusterEntity.getClusterServiceEntities().add(serviceEntity);
        clusterDAO.merge(clusterEntity);
        clusterServiceDAO.merge(serviceEntity);
    }

    @java.lang.Override
    public boolean canBeRemoved() {
        for (org.apache.ambari.server.state.ServiceComponent sc : components.values()) {
            if (!sc.canBeRemoved()) {
                org.apache.ambari.server.state.ServiceImpl.LOG.warn(((((("Found non removable component when trying to delete service" + ", clusterName=") + cluster.getClusterName()) + ", serviceName=") + getName()) + ", componentName=") + sc.getName());
                return false;
            }
        }
        return true;
    }

    @com.google.inject.persist.Transactional
    void deleteAllServiceConfigs() throws org.apache.ambari.server.AmbariException {
        long clusterId = getClusterId();
        org.apache.ambari.server.orm.entities.ServiceConfigEntity lastServiceConfigEntity = serviceConfigDAO.findMaxVersion(clusterId, getName());
        if (lastServiceConfigEntity != null) {
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity serviceConfigEntity : lastServiceConfigEntity.getClusterConfigEntities()) {
                org.apache.ambari.server.state.ServiceImpl.LOG.info("Disabling and unmapping configuration {}", serviceConfigEntity);
                serviceConfigEntity.setSelected(false);
                serviceConfigEntity.setUnmapped(true);
                clusterDAO.merge(serviceConfigEntity);
            }
        }
        org.apache.ambari.server.state.ServiceImpl.LOG.info("Deleting all configuration associations for {} on cluster {}", getName(), cluster.getClusterName());
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities = serviceConfigDAO.findByService(cluster.getClusterId(), getName());
        for (org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity : serviceConfigEntities) {
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : serviceConfigEntity.getClusterConfigEntities()) {
                if (!clusterConfigEntity.isUnmapped()) {
                    org.apache.ambari.server.state.ServiceImpl.LOG.info("Unmapping configuration {}", clusterConfigEntity);
                    clusterConfigEntity.setUnmapped(true);
                    clusterDAO.merge(clusterConfigEntity);
                }
            }
            serviceConfigDAO.remove(serviceConfigEntity);
        }
    }

    void deleteAllServiceConfigGroups() throws org.apache.ambari.server.AmbariException {
        for (java.lang.Long configGroupId : cluster.getConfigGroupsByServiceName(serviceName).keySet()) {
            cluster.deleteConfigGroup(configGroupId);
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void deleteAllComponents(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) {
        lock.lock();
        try {
            org.apache.ambari.server.state.ServiceImpl.LOG.info(((("Deleting all components for service" + ", clusterName=") + cluster.getClusterName()) + ", serviceName=") + getName());
            for (org.apache.ambari.server.state.ServiceComponent component : components.values()) {
                if (!component.canBeRemoved()) {
                    deleteMetaData.setAmbariException(new org.apache.ambari.server.AmbariException(((((("Found non removable component when trying to" + (" delete all components from service" + ", clusterName=")) + cluster.getClusterName()) + ", serviceName=") + getName()) + ", componentName=") + component.getName()));
                    return;
                }
            }
            for (org.apache.ambari.server.state.ServiceComponent serviceComponent : components.values()) {
                serviceComponent.delete(deleteMetaData);
                if (deleteMetaData.getAmbariException() != null) {
                    return;
                }
            }
            components.clear();
        } finally {
            lock.unlock();
        }
    }

    @java.lang.Override
    public void deleteServiceComponent(java.lang.String componentName, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException {
        lock.lock();
        try {
            org.apache.ambari.server.state.ServiceComponent component = getServiceComponent(componentName);
            org.apache.ambari.server.state.ServiceImpl.LOG.info(((((("Deleting servicecomponent for cluster" + ", clusterName=") + cluster.getClusterName()) + ", serviceName=") + getName()) + ", componentName=") + componentName);
            if (!component.canBeRemoved()) {
                throw new org.apache.ambari.server.AmbariException(((((("Could not delete component from cluster" + ", clusterName=") + cluster.getClusterName()) + ", serviceName=") + getName()) + ", componentName=") + componentName);
            }
            component.delete(deleteMetaData);
            components.remove(componentName);
        } finally {
            lock.unlock();
        }
    }

    @java.lang.Override
    public boolean isClientOnlyService() {
        return isClientOnlyService;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void delete(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) {
        java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components = getComponents();
        deleteAllComponents(deleteMetaData);
        if (deleteMetaData.getAmbariException() != null) {
            return;
        }
        org.apache.ambari.server.state.StackId stackId = getDesiredStackId();
        try {
            deleteAllServiceConfigs();
            deleteAllServiceConfigGroups();
            removeEntities();
        } catch (org.apache.ambari.server.AmbariException e) {
            deleteMetaData.setAmbariException(e);
            return;
        }
        if (null == stackId) {
            return;
        }
        org.apache.ambari.server.events.ServiceRemovedEvent event = new org.apache.ambari.server.events.ServiceRemovedEvent(getClusterId(), stackId.getStackName(), stackId.getStackVersion(), getName(), components);
        eventPublisher.publish(event);
    }

    private java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> getComponents() {
        java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> result = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceComponent component : getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost host : component.getServiceComponentHosts().values()) {
                result.add(new org.apache.ambari.server.serveraction.kerberos.Component(host.getHostName(), getName(), component.getName(), host.getHost().getHostId()));
            }
        }
        return result;
    }

    @com.google.inject.persist.Transactional
    protected void removeEntities() throws org.apache.ambari.server.AmbariException {
        serviceDesiredStateDAO.removeByPK(serviceDesiredStateEntityPK);
        clusterServiceDAO.removeByPK(serviceEntityPK);
    }

    @java.lang.Override
    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState state) {
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = getServiceDesiredStateEntity();
        serviceDesiredStateEntity.setMaintenanceState(state);
        maintenanceState.set(serviceDesiredStateDAO.merge(serviceDesiredStateEntity).getMaintenanceState());
        org.apache.ambari.server.events.MaintenanceModeEvent event = new org.apache.ambari.server.events.MaintenanceModeEvent(state, this);
        eventPublisher.publish(event);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        if (maintenanceState.get() == null) {
            maintenanceState.set(getServiceDesiredStateEntity().getMaintenanceState());
        }
        return maintenanceState.get();
    }

    @java.lang.Override
    public boolean isKerberosEnabled() {
        if (kerberosEnabledTest != null) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations;
            try {
                existingConfigurations = configHelper.calculateExistingConfigurations(ambariManagementController, cluster);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.state.ServiceImpl.LOG.warn("Failed to get the existing configurations for the cluster.  Predicate calculations may not be correct due to missing data.");
                existingConfigurations = java.util.Collections.emptyMap();
            }
            return isKerberosEnabled(existingConfigurations);
        }
        return false;
    }

    @java.lang.Override
    public boolean isKerberosEnabled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) {
        return (kerberosEnabledTest != null) && kerberosEnabledTest.evaluate(configurations);
    }

    private org.apache.ambari.server.orm.entities.ClusterServiceEntityPK getServiceEntityPK(org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity) {
        org.apache.ambari.server.orm.entities.ClusterServiceEntityPK pk = new org.apache.ambari.server.orm.entities.ClusterServiceEntityPK();
        pk.setClusterId(serviceEntity.getClusterId());
        pk.setServiceName(serviceEntity.getServiceName());
        return pk;
    }

    private org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK getServiceDesiredStateEntityPK(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity) {
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK pk = new org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK();
        pk.setClusterId(serviceDesiredStateEntity.getClusterId());
        pk.setServiceName(serviceDesiredStateEntity.getServiceName());
        return pk;
    }

    private org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity getServiceDesiredStateEntity() {
        return serviceDesiredStateDAO.findByPK(serviceDesiredStateEntityPK);
    }

    private org.apache.ambari.server.collections.Predicate compileSsoEnabledPredicate(org.apache.ambari.server.state.ServiceInfo sInfo) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(sInfo.getSingleSignOnEnabledTest())) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(sInfo.getSingleSignOnEnabledConfiguration())) {
                org.apache.ambari.server.state.ServiceImpl.LOG.warn("Both <ssoEnabledTest> and <enabledConfiguration> have been declared within <sso> for {}; using <ssoEnabledTest>", serviceName);
            }
            return org.apache.ambari.server.collections.PredicateUtils.fromJSON(sInfo.getSingleSignOnEnabledTest());
        } else if (org.apache.commons.lang.StringUtils.isNotBlank(sInfo.getSingleSignOnEnabledConfiguration())) {
            org.apache.ambari.server.state.ServiceImpl.LOG.warn("Only <enabledConfiguration> have been declared  within <sso> for {}; converting its value to an equals predicate", serviceName);
            final java.lang.String equalsPredicateJson = ("{\"equals\": [\"" + sInfo.getSingleSignOnEnabledConfiguration()) + "\", \"true\"]}";
            return org.apache.ambari.server.collections.PredicateUtils.fromJSON(equalsPredicateJson);
        }
        return null;
    }

    private org.apache.ambari.server.collections.Predicate compileKerberosEnabledPredicate(org.apache.ambari.server.state.ServiceInfo sInfo) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(sInfo.getKerberosEnabledTest())) {
            return org.apache.ambari.server.collections.PredicateUtils.fromJSON(sInfo.getKerberosEnabledTest());
        }
        return null;
    }

    private boolean isSsoIntegrationDesired() {
        return ambariServerSSOConfigurationHandler.getSSOEnabledServices().contains(serviceName);
    }

    private boolean isSsoIntegrationEnabled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations) {
        return (ssoIntegrationSupported && (ssoEnabledTest != null)) && ssoEnabledTest.evaluate(existingConfigurations);
    }

    private boolean isKerberosRequiredForSsoIntegration() {
        return ssoRequiresKerberos;
    }

    private boolean isLdapIntegrationEnabeled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations) {
        return (ldapIntegrationSupported && (ldapEnabledTest != null)) && ldapEnabledTest.evaluate(existingConfigurations);
    }

    private boolean isLdapIntegrationDesired() {
        return ambariServerLDAPConfigurationHandler.getLDAPEnabledServices().contains(serviceName);
    }
}