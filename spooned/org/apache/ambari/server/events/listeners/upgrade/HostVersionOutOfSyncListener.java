package org.apache.ambari.server.events.listeners.upgrade;
import com.google.inject.persist.Transactional;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class HostVersionOutOfSyncListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class);

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostVersionDAO> hostVersionDAO;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostDAO> hostDAO;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ami;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO> repositoryVersionDAO;

    private final java.util.concurrent.locks.Lock m_lock;

    @com.google.inject.Inject
    public HostVersionOutOfSyncListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, org.apache.ambari.server.logging.LockFactory lockFactory) {
        ambariEventPublisher.register(this);
        m_lock = lockFactory.newLock("hostVersionOutOfSyncListenerLock");
    }

    @com.google.common.eventbus.Subscribe
    @com.google.inject.persist.Transactional
    public void onServiceComponentEvent(org.apache.ambari.server.events.ServiceComponentInstalledEvent event) {
        if (org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.isDebugEnabled()) {
            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.debug(event.toString());
        }
        m_lock.lock();
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.get().getClusterById(event.getClusterId());
            java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities = hostVersionDAO.get().findByClusterAndHost(cluster.getClusterName(), event.getHostName());
            org.apache.ambari.server.state.Service service = cluster.getService(event.getServiceName());
            org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(event.getComponentName());
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity componentRepo = serviceComponent.getDesiredRepositoryVersion();
            for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersionEntities) {
                org.apache.ambari.server.orm.entities.StackEntity hostStackEntity = hostVersionEntity.getRepositoryVersion().getStack();
                org.apache.ambari.server.state.StackId hostStackId = new org.apache.ambari.server.state.StackId(hostStackEntity);
                java.lang.String serviceName = event.getServiceName();
                java.lang.String componentName = event.getComponentName();
                if (!ami.get().isValidServiceComponent(hostStackId.getStackName(), hostStackId.getStackVersion(), serviceName, componentName)) {
                    org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.debug("Component not found is host stack, stack={}, version={}, service={}, component={}", hostStackId.getStackName(), hostStackId.getStackVersion(), serviceName, componentName);
                    continue;
                }
                org.apache.ambari.server.state.ComponentInfo component = ami.get().getComponent(hostStackId.getStackName(), hostStackId.getStackVersion(), serviceName, componentName);
                if (!component.isVersionAdvertised()) {
                    org.apache.ambari.server.state.RepositoryVersionState state = checkAllHostComponents(hostStackId, hostVersionEntity.getHostEntity());
                    if (null != state) {
                        hostVersionEntity.setState(state);
                        hostVersionDAO.get().merge(hostVersionEntity);
                    }
                    continue;
                }
                if (!hostVersionEntity.getRepositoryVersion().equals(componentRepo)) {
                    continue;
                }
                switch (hostVersionEntity.getState()) {
                    case INSTALLED :
                    case NOT_REQUIRED :
                        hostVersionEntity.setState(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
                        hostVersionDAO.get().merge(hostVersionEntity);
                        break;
                    default :
                        break;
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.error("Can not update hosts about out of sync", e);
        } finally {
            m_lock.unlock();
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.inject.persist.Transactional
    public void onServiceComponentHostEvent(org.apache.ambari.server.events.ServiceComponentUninstalledEvent event) {
        m_lock.lock();
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.get().getClusterById(event.getClusterId());
            java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities = hostVersionDAO.get().findByClusterAndHost(cluster.getClusterName(), event.getHostName());
            for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersionEntities) {
                org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostVersionEntity.getHostEntity();
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = hostVersionEntity.getRepositoryVersion();
                org.apache.ambari.server.state.StackId stackId = repoVersionEntity.getStackId();
                if (null == stackId) {
                    org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.info("Stack id could not be loaded for host version {}, repo {}", hostVersionEntity.getHostName(), repoVersionEntity.getVersion());
                    continue;
                }
                org.apache.ambari.server.state.RepositoryVersionState repoState = checkAllHostComponents(stackId, hostEntity);
                if (null != repoState) {
                    hostVersionEntity.setState(repoState);
                    hostVersionDAO.get().merge(hostVersionEntity);
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.error("Cannot update states after a component was uninstalled: {}", event, e);
        } finally {
            m_lock.unlock();
        }
    }

    private org.apache.ambari.server.state.RepositoryVersionState checkAllHostComponents(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.orm.entities.HostEntity host) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponents = host.getHostComponentDesiredStateEntities();
        for (org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponent : hostComponents) {
            if (!ami.get().isValidServiceComponent(stackId.getStackName(), stackId.getStackVersion(), hostComponent.getServiceName(), hostComponent.getComponentName())) {
                org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.debug("Component not found is host stack, stack={}, version={}, service={}, component={}", stackId.getStackName(), stackId.getStackVersion(), hostComponent.getServiceName(), hostComponent.getComponentName());
                continue;
            }
            org.apache.ambari.server.state.ComponentInfo ci = ami.get().getComponent(stackId.getStackName(), stackId.getStackVersion(), hostComponent.getServiceName(), hostComponent.getComponentName());
            if (ci.isVersionAdvertised()) {
                return null;
            }
        }
        return org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED;
    }

    @com.google.common.eventbus.Subscribe
    @com.google.inject.persist.Transactional
    public void onServiceEvent(org.apache.ambari.server.events.ServiceInstalledEvent event) {
        if (org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.isDebugEnabled()) {
            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.debug(event.toString());
        }
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.get().getClusterById(event.getClusterId());
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = cluster.getService(event.getServiceName()).getServiceComponents();
            java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponent>> affectedHosts = new java.util.HashMap<>();
            for (org.apache.ambari.server.state.ServiceComponent component : serviceComponents.values()) {
                for (java.lang.String hostname : component.getServiceComponentHosts().keySet()) {
                    if (!affectedHosts.containsKey(hostname)) {
                        affectedHosts.put(hostname, new java.util.ArrayList<>());
                    }
                    affectedHosts.get(hostname).add(component);
                }
            }
            for (java.lang.String hostName : affectedHosts.keySet()) {
                java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities = hostVersionDAO.get().findByClusterAndHost(cluster.getClusterName(), hostName);
                for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersionEntities) {
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = hostVersionEntity.getRepositoryVersion();
                    boolean hasChangedComponentsWithVersions = false;
                    java.lang.String serviceName = event.getServiceName();
                    for (org.apache.ambari.server.state.ServiceComponent comp : affectedHosts.get(hostName)) {
                        java.lang.String componentName = comp.getName();
                        if (!ami.get().isValidServiceComponent(repositoryVersion.getStackName(), repositoryVersion.getStackVersion(), serviceName, componentName)) {
                            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.debug("Component not found is host stack, stack={}, version={}, service={}, component={}", repositoryVersion.getStackName(), repositoryVersion.getStackVersion(), serviceName, componentName);
                            continue;
                        }
                        org.apache.ambari.server.state.ComponentInfo component = ami.get().getComponent(repositoryVersion.getStackName(), repositoryVersion.getStackVersion(), serviceName, componentName);
                        if (component.isVersionAdvertised()) {
                            hasChangedComponentsWithVersions = true;
                        }
                    }
                    if (!hasChangedComponentsWithVersions) {
                        continue;
                    }
                    if (hostVersionEntity.getState().equals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED)) {
                        hostVersionEntity.setState(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
                        hostVersionDAO.get().merge(hostVersionEntity);
                    }
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.error("Can not update hosts about out of sync", e);
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.inject.persist.Transactional
    public void onHostEvent(org.apache.ambari.server.events.HostsAddedEvent event) {
        if (org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.isDebugEnabled()) {
            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.debug(event.toString());
        }
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "Eventually take into account deleted repositories")
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> repos = repositoryVersionDAO.get().findAll();
        for (java.lang.String hostName : event.getHostNames()) {
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.get().findByName(hostName);
            for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion : repos) {
                org.apache.ambari.server.orm.entities.HostVersionEntity missingHostVersion = new org.apache.ambari.server.orm.entities.HostVersionEntity(hostEntity, repositoryVersion, org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED);
                org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.info("Creating host version for {}, state={}, repo={} (repo_id={})", missingHostVersion.getHostName(), missingHostVersion.getState(), missingHostVersion.getRepositoryVersion().getVersion(), missingHostVersion.getRepositoryVersion().getId());
                hostVersionDAO.get().create(missingHostVersion);
                hostDAO.get().merge(hostEntity);
                hostEntity.getHostVersionEntities().add(missingHostVersion);
                hostEntity = hostDAO.get().merge(hostEntity);
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    @com.google.inject.persist.Transactional
    public void onHostEvent(org.apache.ambari.server.events.HostsRemovedEvent event) {
        if (org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.isDebugEnabled()) {
            org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.LOG.debug(event.toString());
        }
    }
}