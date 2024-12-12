package org.apache.ambari.server.topology;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
public class AmbariContext {
    public enum TaskType {

        INSTALL,
        START;}

    @javax.inject.Inject
    private org.apache.ambari.server.topology.PersistedState persistedState;

    @javax.inject.Inject
    org.apache.ambari.server.state.ConfigFactory configFactory;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @javax.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> configHelper;

    @javax.inject.Inject
    org.apache.ambari.server.agent.stomp.HostLevelParamsHolder hostLevelParamsHolder;

    private static org.apache.ambari.server.controller.AmbariManagementController controller;

    private static org.apache.ambari.server.controller.spi.ClusterController clusterController;

    private static final java.util.concurrent.atomic.AtomicLong nextTaskId = new java.util.concurrent.atomic.AtomicLong(10000);

    private static org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    private static org.apache.ambari.server.controller.internal.HostResourceProvider hostResourceProvider;

    private static org.apache.ambari.server.controller.internal.ServiceResourceProvider serviceResourceProvider;

    private static org.apache.ambari.server.controller.internal.ComponentResourceProvider componentResourceProvider;

    private static org.apache.ambari.server.controller.internal.HostComponentResourceProvider hostComponentResourceProvider;

    private static org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider versionDefinitionResourceProvider;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.AmbariContext.class);

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> configGroupCreateLock = com.google.common.util.concurrent.Striped.lazyWeakLock(1);

    public boolean isClusterKerberosEnabled(long clusterId) {
        org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getClusterById(clusterId);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException("Parent Cluster resource doesn't exist.  clusterId= " + clusterId);
        }
        return cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS;
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommand createAmbariTask(long requestId, long stageId, java.lang.String component, java.lang.String host, org.apache.ambari.server.topology.AmbariContext.TaskType type, boolean skipFailure) {
        org.apache.ambari.server.actionmanager.HostRoleCommand task = org.apache.ambari.server.topology.AmbariContext.hostRoleCommandFactory.create(host, org.apache.ambari.server.Role.valueOf(component), null, org.apache.ambari.server.RoleCommand.valueOf(type.name()), false, skipFailure);
        task.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        task.setCommandDetail(java.lang.String.format("Logical Task: %s component %s on host %s", type.name(), component, host));
        task.setTaskId(org.apache.ambari.server.topology.AmbariContext.nextTaskId.getAndIncrement());
        task.setRequestId(requestId);
        task.setStageId(stageId);
        return task;
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommand createAmbariTask(long taskId, long requestId, long stageId, java.lang.String component, java.lang.String host, org.apache.ambari.server.topology.AmbariContext.TaskType type, boolean skipFailure) {
        synchronized(org.apache.ambari.server.topology.AmbariContext.nextTaskId) {
            if (org.apache.ambari.server.topology.AmbariContext.nextTaskId.get() <= taskId) {
                org.apache.ambari.server.topology.AmbariContext.nextTaskId.set(taskId + 1);
            }
        }
        org.apache.ambari.server.actionmanager.HostRoleCommand task = org.apache.ambari.server.topology.AmbariContext.hostRoleCommandFactory.create(host, org.apache.ambari.server.Role.valueOf(component), null, org.apache.ambari.server.RoleCommand.valueOf(type.name()), false, skipFailure);
        task.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        task.setCommandDetail(java.lang.String.format("Logical Task: %s component %s on host %s", type.name(), component, host));
        task.setTaskId(taskId);
        task.setRequestId(requestId);
        task.setStageId(stageId);
        return task;
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommand getPhysicalTask(long id) {
        return org.apache.ambari.server.topology.AmbariContext.getController().getActionManager().getTaskById(id);
    }

    public java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> getPhysicalTasks(java.util.Collection<java.lang.Long> ids) {
        return org.apache.ambari.server.topology.AmbariContext.getController().getActionManager().getTasks(ids);
    }

    public void createAmbariResources(org.apache.ambari.server.topology.ClusterTopology topology, java.lang.String clusterName, org.apache.ambari.server.state.SecurityType securityType, java.lang.String repoVersionString, java.lang.Long repoVersionId) {
        org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stack.getName(), stack.getVersion());
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = null;
        if (org.apache.commons.lang.StringUtils.isEmpty(repoVersionString) && (null == repoVersionId)) {
            java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> stackRepoVersions = repositoryVersionDAO.findByStack(stackId);
            if (stackRepoVersions.isEmpty()) {
                org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider vdfProvider = getVersionDefinitionResourceProvider();
                java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
                properties.put(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_DEFINITION, stackId.toString());
                org.apache.ambari.server.controller.spi.Request request = new org.apache.ambari.server.controller.internal.RequestImpl(java.util.Collections.<java.lang.String>emptySet(), java.util.Collections.singleton(properties), java.util.Collections.<java.lang.String, java.lang.String>emptyMap(), null);
                java.lang.Long defaultRepoVersionId = null;
                try {
                    org.apache.ambari.server.controller.spi.RequestStatus requestStatus = vdfProvider.createResources(request);
                    if (!requestStatus.getAssociatedResources().isEmpty()) {
                        org.apache.ambari.server.controller.spi.Resource resource = requestStatus.getAssociatedResources().iterator().next();
                        defaultRepoVersionId = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID)));
                    }
                } catch (java.lang.Exception e) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Failed to create a default repository version definition for stack %s. " + (("This typically is a result of not loading the stack correctly or being able " + "to load information about released versions.  Create a repository version ") + " and try again."), stackId), e);
                }
                repoVersion = repositoryVersionDAO.findByPK(defaultRepoVersionId);
                if (null == repoVersion) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Failed to load the default repository version definition for stack %s. " + "Check for a valid repository version and try again.", stackId));
                }
            } else if (stackRepoVersions.size() > 1) {
                com.google.common.base.Function<org.apache.ambari.server.orm.entities.RepositoryVersionEntity, java.lang.String> function = new com.google.common.base.Function<org.apache.ambari.server.orm.entities.RepositoryVersionEntity, java.lang.String>() {
                    @java.lang.Override
                    public java.lang.String apply(org.apache.ambari.server.orm.entities.RepositoryVersionEntity input) {
                        return input.getVersion();
                    }
                };
                java.util.Collection<java.lang.String> versions = com.google.common.collect.Collections2.transform(stackRepoVersions, function);
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Several repositories were found for %s:  %s.  Specify the version" + " with '%s'", stackId, org.apache.commons.lang.StringUtils.join(versions, ", "), org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_PROPERTY));
            } else {
                repoVersion = stackRepoVersions.get(0);
                org.apache.ambari.server.topology.AmbariContext.LOG.warn("Cluster is being provisioned using the single matching repository version {}", repoVersion.getVersion());
            }
        } else {
            if (null != repoVersionId) {
                repoVersion = repositoryVersionDAO.findByPK(repoVersionId);
                if (null == repoVersion) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Could not identify repository version with repository version id %s for installing services. " + "Specify a valid repository version id with '%s'", repoVersionId, org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_ID_PROPERTY));
                }
            } else {
                repoVersion = repositoryVersionDAO.findByStackAndVersion(stackId, repoVersionString);
                if (null == repoVersion) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Could not identify repository version with stack %s and version %s for installing services. " + "Specify a valid version with '%s'", stackId, repoVersionString, org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_PROPERTY));
                }
            }
            if (!java.util.Objects.equals(repoVersion.getStackId(), stackId)) {
                java.lang.String repoVersionPair = (repoVersionId != null) ? java.lang.String.format("'%s' = %d", org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_ID_PROPERTY, repoVersionId) : java.lang.String.format("'%s' = '%s'", org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_PROPERTY, repoVersionString);
                java.lang.String msg = java.lang.String.format("The stack specified in the blueprint (%s) and the repository version (%s for %s) should match", stackId, repoVersion.getStackId(), repoVersionPair);
                throw new java.lang.IllegalArgumentException(msg);
            }
        }
        if (repoVersion.getType() != org.apache.ambari.spi.RepositoryType.STANDARD) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Unable to create a cluster using the following repository since it is not a STANDARD type: %s", repoVersion));
        }
        createAmbariClusterResource(clusterName, stack.getName(), stack.getVersion(), securityType);
        createAmbariServiceAndComponentResources(topology, clusterName, stackId, repoVersion.getId());
    }

    public void createAmbariClusterResource(java.lang.String clusterName, java.lang.String stackName, java.lang.String stackVersion, org.apache.ambari.server.state.SecurityType securityType) {
        java.lang.String stackInfo = java.lang.String.format("%s-%s", stackName, stackVersion);
        final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, null, securityType, stackInfo, null);
        try {
            org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<java.lang.Object>() {
                @java.lang.Override
                public java.lang.Object call() throws java.lang.Exception {
                    org.apache.ambari.server.topology.AmbariContext.getController().createCluster(clusterRequest);
                    return null;
                }
            });
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Failed to create Cluster resource: ", e);
            if (e.getCause() instanceof org.apache.ambari.server.DuplicateResourceException) {
                throw new java.lang.IllegalArgumentException(e);
            } else {
                throw new java.lang.RuntimeException("Failed to create Cluster resource: " + e, e);
            }
        }
    }

    public void createAmbariServiceAndComponentResources(org.apache.ambari.server.topology.ClusterTopology topology, java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, java.lang.Long repositoryVersionId) {
        java.util.Collection<java.lang.String> services = topology.getBlueprint().getServices();
        try {
            org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getCluster(clusterName);
            services.removeAll(cluster.getServices().keySet());
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException("Failed to persist service and component resources: " + e, e);
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> serviceRequests = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> componentRequests = new java.util.HashSet<>();
        for (java.lang.String service : services) {
            java.lang.String credentialStoreEnabled = topology.getBlueprint().getCredentialStoreEnabled(service);
            serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(clusterName, service, repositoryVersionId, null, credentialStoreEnabled));
            for (java.lang.String component : topology.getBlueprint().getComponents(service)) {
                java.lang.String recoveryEnabled = topology.getBlueprint().getRecoveryEnabled(service, component);
                componentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(clusterName, service, component, null, recoveryEnabled));
            }
        }
        try {
            getServiceResourceProvider().createServices(serviceRequests);
            getComponentResourceProvider().createComponents(componentRequests);
        } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.security.authorization.AuthorizationException e) {
            throw new java.lang.RuntimeException("Failed to persist service and component resources: " + e, e);
        }
        java.util.Map<java.lang.String, java.lang.Object> installProps = new java.util.HashMap<>();
        installProps.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "INSTALLED");
        installProps.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, clusterName);
        java.util.Map<java.lang.String, java.lang.Object> startProps = new java.util.HashMap<>();
        startProps.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "STARTED");
        startProps.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, clusterName);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, clusterName);
        try {
            getServiceResourceProvider().updateResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, java.util.Collections.singleton(installProps), null, null), predicate);
            getServiceResourceProvider().updateResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, java.util.Collections.singleton(startProps), null, null), predicate);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Unable to update state of services during cluster provision: " + e, e);
        }
    }

    public void createAmbariHostResources(long clusterId, java.lang.String hostName, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> components) {
        org.apache.ambari.server.state.Host host;
        try {
            host = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getHost(hostName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(java.lang.String.format("Unable to obtain host instance '%s' when persisting host resources", hostName));
        }
        org.apache.ambari.server.state.Cluster cluster = null;
        try {
            cluster = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getClusterById(clusterId);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Cannot get cluster for clusterId = " + clusterId, e);
            throw new java.lang.RuntimeException(e);
        }
        java.lang.String clusterName = cluster.getClusterName();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, hostName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID, host.getRackInfo());
        try {
            getHostResourceProvider().createHosts(new org.apache.ambari.server.controller.internal.RequestImpl(null, java.util.Collections.singleton(properties), null, null));
        } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.security.authorization.AuthorizationException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Unable to create host component resource for host {}", hostName, e);
            throw new java.lang.RuntimeException(java.lang.String.format("Unable to create host resource for host '%s': %s", hostName, e.toString()), e);
        }
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> entry : components.entrySet()) {
            java.lang.String service = entry.getKey();
            for (java.lang.String component : entry.getValue()) {
                try {
                    if ((cluster.getService(service) != null) && (!component.equals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name()))) {
                        requests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, service, component, hostName, null));
                    }
                } catch (org.apache.ambari.server.AmbariException se) {
                    org.apache.ambari.server.topology.AmbariContext.LOG.warn("Service already deleted from cluster: {}", service);
                }
            }
        }
        try {
            org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<java.lang.Object>() {
                @java.lang.Override
                public java.lang.Object call() throws java.lang.Exception {
                    org.apache.ambari.server.topology.AmbariContext.getController().createHostComponents(requests, true);
                    return null;
                }
            });
            hostLevelParamsHolder.updateData(hostLevelParamsHolder.getCurrentData(host.getHostId()));
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Unable to create host component resource for host {}", hostName, e);
            throw new java.lang.RuntimeException(java.lang.String.format("Unable to create host component resource for host '%s': %s", hostName, e.toString()), e);
        }
    }

    public java.lang.Long getNextRequestId() {
        return org.apache.ambari.server.topology.AmbariContext.getController().getActionManager().getNextRequestId();
    }

    public static synchronized org.apache.ambari.server.controller.AmbariManagementController getController() {
        if (org.apache.ambari.server.topology.AmbariContext.controller == null) {
            org.apache.ambari.server.topology.AmbariContext.controller = org.apache.ambari.server.controller.AmbariServer.getController();
        }
        return org.apache.ambari.server.topology.AmbariContext.controller;
    }

    public static synchronized org.apache.ambari.server.controller.spi.ClusterController getClusterController() {
        if (org.apache.ambari.server.topology.AmbariContext.clusterController == null) {
            org.apache.ambari.server.topology.AmbariContext.clusterController = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
        }
        return org.apache.ambari.server.topology.AmbariContext.clusterController;
    }

    public static void init(org.apache.ambari.server.actionmanager.HostRoleCommandFactory factory) {
        org.apache.ambari.server.topology.AmbariContext.hostRoleCommandFactory = factory;
    }

    public void registerHostWithConfigGroup(final java.lang.String hostName, final org.apache.ambari.server.topology.ClusterTopology topology, final java.lang.String groupName) {
        java.lang.String qualifiedGroupName = getConfigurationGroupName(topology.getBlueprint().getName(), groupName);
        java.util.concurrent.locks.Lock configGroupLock = configGroupCreateLock.get(qualifiedGroupName);
        try {
            configGroupLock.lock();
            boolean hostAdded = org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<java.lang.Boolean>() {
                @java.lang.Override
                public java.lang.Boolean call() throws java.lang.Exception {
                    return addHostToExistingConfigGroups(hostName, topology, qualifiedGroupName);
                }
            });
            if (!hostAdded) {
                createConfigGroupsAndRegisterHost(topology, groupName);
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Unable to register config group for host: ", e);
            throw new java.lang.RuntimeException("Unable to register config group for host: " + hostName);
        } finally {
            configGroupLock.unlock();
        }
    }

    public org.apache.ambari.server.controller.RequestStatusResponse installHost(java.lang.String hostName, java.lang.String clusterName, java.util.Collection<java.lang.String> skipInstallForComponents, java.util.Collection<java.lang.String> dontSkipInstallForComponents, boolean skipFailure) {
        try {
            return getHostResourceProvider().install(clusterName, hostName, skipInstallForComponents, dontSkipInstallForComponents, skipFailure, true);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("INSTALL Host request submission failed:", e);
            throw new java.lang.RuntimeException("INSTALL Host request submission failed: " + e, e);
        }
    }

    public org.apache.ambari.server.controller.RequestStatusResponse startHost(java.lang.String hostName, java.lang.String clusterName, java.util.Collection<java.lang.String> installOnlyComponents, boolean skipFailure) {
        try {
            return getHostComponentResourceProvider().start(clusterName, hostName, installOnlyComponents, skipFailure, true);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("START Host request submission failed:", e);
            throw new java.lang.RuntimeException("START Host request submission failed: " + e, e);
        }
    }

    public void persistInstallStateForUI(java.lang.String clusterName, java.lang.String stackName, java.lang.String stackVersion) {
        java.lang.String stackInfo = java.lang.String.format("%s-%s", stackName, stackVersion);
        final org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, "INSTALLED", null, stackInfo, null);
        try {
            org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<java.lang.Object>() {
                @java.lang.Override
                public java.lang.Object call() throws java.lang.Exception {
                    org.apache.ambari.server.topology.AmbariContext.getController().updateClusters(java.util.Collections.singleton(clusterRequest), null);
                    return null;
                }
            });
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Unable to set install state for UI", e);
        }
    }

    public java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> createConfigurationRequests(java.util.Map<java.lang.String, java.lang.Object> clusterProperties) {
        return org.apache.ambari.server.controller.internal.AbstractResourceProvider.getConfigurationRequests("Clusters", clusterProperties);
    }

    public void setConfigurationOnCluster(final org.apache.ambari.server.controller.ClusterRequest clusterRequest) {
        try {
            org.apache.ambari.server.utils.RetryHelper.executeWithRetry(new java.util.concurrent.Callable<java.lang.Object>() {
                @java.lang.Override
                public java.lang.Object call() throws java.lang.Exception {
                    org.apache.ambari.server.topology.AmbariContext.getController().updateClusters(java.util.Collections.singleton(clusterRequest), null, false, false);
                    return null;
                }
            });
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Failed to set configurations on cluster: ", e);
            throw new java.lang.RuntimeException("Failed to set configurations on cluster: " + e, e);
        }
    }

    public void notifyAgentsAboutConfigsChanges(java.lang.String clusterName) {
        try {
            configHelper.get().updateAgentConfigs(java.util.Collections.singleton(clusterName));
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.error("Failed to set send agent updates: ", e);
            throw new java.lang.RuntimeException("Failed to set send agent updates: " + e, e);
        }
    }

    public void waitForConfigurationResolution(java.lang.String clusterName, java.util.Set<java.lang.String> updatedConfigTypes) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getCluster(clusterName);
        boolean shouldWaitForResolution = true;
        while (shouldWaitForResolution) {
            int numOfRequestsStillRequiringResolution = 0;
            for (java.lang.String actualConfigType : updatedConfigTypes) {
                org.apache.ambari.server.state.DesiredConfig actualConfig = cluster.getDesiredConfigs().get(actualConfigType);
                if ((actualConfig == null) || actualConfigType.equals("core-site")) {
                    continue;
                }
                if (!actualConfig.getTag().equals(org.apache.ambari.server.topology.TopologyManager.TOPOLOGY_RESOLVED_TAG)) {
                    org.apache.ambari.server.topology.AmbariContext.LOG.info(("Config type " + actualConfigType) + " not resolved yet, Blueprint deployment will wait until configuration update is completed");
                    numOfRequestsStillRequiringResolution++;
                } else {
                    org.apache.ambari.server.topology.AmbariContext.LOG.info(("Config type " + actualConfigType) + " is resolved in the cluster config.");
                }
            }
            if (numOfRequestsStillRequiringResolution == 0) {
                org.apache.ambari.server.topology.AmbariContext.LOG.info(("All required configuration types are in the " + org.apache.ambari.server.topology.TopologyManager.TOPOLOGY_RESOLVED_TAG) + " state.  Blueprint deployment can now continue.");
                shouldWaitForResolution = false;
            } else {
                org.apache.ambari.server.topology.AmbariContext.LOG.info(("Waiting for " + numOfRequestsStillRequiringResolution) + " configuration types to be resolved before Blueprint deployment can continue");
                try {
                    java.lang.Thread.sleep(100);
                } catch (java.lang.InterruptedException e) {
                    org.apache.ambari.server.topology.AmbariContext.LOG.warn("sleep interrupted");
                }
            }
        } 
    }

    public boolean isTopologyResolved(long clusterId) {
        boolean isTopologyResolved = false;
        try {
            org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getClusterById(clusterId);
            java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> allDesiredConfigsByType = cluster.getAllDesiredConfigVersions();
            for (java.lang.String configType : allDesiredConfigsByType.keySet()) {
                java.util.Set<org.apache.ambari.server.state.DesiredConfig> desiredConfigVersions = allDesiredConfigsByType.get(configType);
                java.util.SortedSet<org.apache.ambari.server.state.DesiredConfig> desiredConfigsOrderedByVersion = new java.util.TreeSet<>(new java.util.Comparator<org.apache.ambari.server.state.DesiredConfig>() {
                    @java.lang.Override
                    public int compare(org.apache.ambari.server.state.DesiredConfig o1, org.apache.ambari.server.state.DesiredConfig o2) {
                        if (o1.getVersion() < o2.getVersion()) {
                            return -1;
                        }
                        if (o1.getVersion() > o2.getVersion()) {
                            return 1;
                        }
                        return 0;
                    }
                });
                desiredConfigsOrderedByVersion.addAll(desiredConfigVersions);
                int tagMatchState = 0;
                for (org.apache.ambari.server.state.DesiredConfig config : desiredConfigsOrderedByVersion) {
                    if (config.getTag().equals(org.apache.ambari.server.topology.TopologyManager.INITIAL_CONFIG_TAG) && (tagMatchState == 0)) {
                        tagMatchState = 1;
                    } else if (config.getTag().equals(org.apache.ambari.server.topology.TopologyManager.TOPOLOGY_RESOLVED_TAG) && (tagMatchState == 1)) {
                        tagMatchState = 2;
                        break;
                    }
                }
                if (tagMatchState == 2) {
                    isTopologyResolved = true;
                    break;
                }
            }
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
            org.apache.ambari.server.topology.AmbariContext.LOG.info("Attempted to determine if configuration is topology resolved for a non-existent cluster: {}", clusterId);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException("Unable to determine if cluster config is topology resolved due to unknown error: " + e, e);
        }
        return isTopologyResolved;
    }

    public org.apache.ambari.server.topology.PersistedState getPersistedTopologyState() {
        return persistedState;
    }

    public boolean isHostRegisteredWithCluster(long clusterId, java.lang.String host) {
        boolean found = false;
        try {
            java.util.Collection<org.apache.ambari.server.state.Host> hosts = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getClusterById(clusterId).getHosts();
            for (org.apache.ambari.server.state.Host h : hosts) {
                if (h.getHostName().equals(host)) {
                    found = true;
                    break;
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(java.lang.String.format("Unable to get hosts for cluster ID = %s: %s", clusterId, e), e);
        }
        return found;
    }

    public long getClusterId(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        return org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getCluster(clusterName).getClusterId();
    }

    public java.lang.String getClusterName(long clusterId) throws org.apache.ambari.server.AmbariException {
        return org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getClusterById(clusterId).getClusterName();
    }

    private boolean addHostToExistingConfigGroups(java.lang.String hostName, org.apache.ambari.server.topology.ClusterTopology topology, java.lang.String configGroupName) {
        boolean addedHost = false;
        org.apache.ambari.server.state.Clusters clusters;
        org.apache.ambari.server.state.Cluster cluster;
        try {
            clusters = org.apache.ambari.server.topology.AmbariContext.getController().getClusters();
            cluster = clusters.getClusterById(topology.getClusterId());
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(java.lang.String.format("Attempt to add hosts to a non-existent cluster: '%s'", topology.getClusterId()));
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = cluster.getConfigGroups();
        for (org.apache.ambari.server.state.configgroup.ConfigGroup group : configGroups.values()) {
            if (group.getName().equals(configGroupName)) {
                try {
                    org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
                    addedHost = true;
                    if (!group.getHosts().containsKey(host.getHostId())) {
                        group.addHost(host);
                    }
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new java.lang.RuntimeException(java.lang.String.format("An error occurred while registering host '%s' with config group '%s' ", hostName, group.getName()), e);
                }
            }
        }
        return addedHost;
    }

    private void createConfigGroupsAndRegisterHost(org.apache.ambari.server.topology.ClusterTopology topology, java.lang.String groupName) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.Config>> groupConfigs = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
        org.apache.ambari.server.topology.Configuration topologyHostGroupConfig = topology.getHostGroupInfo().get(groupName).getConfiguration();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userProvidedGroupProperties = topologyHostGroupConfig.getFullProperties(1);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : userProvidedGroupProperties.entrySet()) {
            java.lang.String type = entry.getKey();
            java.util.List<java.lang.String> services = stack.getServicesForConfigType(type);
            java.lang.String service = services.stream().filter(each -> topology.getBlueprint().getServices().contains(each)).findFirst().orElseThrow(() -> new java.lang.IllegalArgumentException("Specified configuration type is not associated with any service: " + type));
            org.apache.ambari.server.state.Config config = configFactory.createReadOnly(type, groupName, entry.getValue(), null);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> serviceConfigs = groupConfigs.get(service);
            if (serviceConfigs == null) {
                serviceConfigs = new java.util.HashMap<>();
                groupConfigs.put(service, serviceConfigs);
            }
            serviceConfigs.put(type, config);
        }
        java.lang.String bpName = topology.getBlueprint().getName();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.Config>> entry : groupConfigs.entrySet()) {
            java.lang.String service = entry.getKey();
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> serviceConfigs = entry.getValue();
            java.lang.String absoluteGroupName = getConfigurationGroupName(bpName, groupName);
            java.util.Collection<java.lang.String> groupHosts;
            groupHosts = topology.getHostGroupInfo().get(groupName).getHostNames();
            java.lang.String clusterName = null;
            try {
                clusterName = getClusterName(topology.getClusterId());
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.topology.AmbariContext.LOG.error("Cannot get cluster name for clusterId = " + topology.getClusterId(), e);
                throw new java.lang.RuntimeException(e);
            }
            final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHosts = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getHostsForCluster(clusterName);
            java.lang.Iterable<java.lang.String> filteredGroupHosts = com.google.common.collect.Iterables.filter(groupHosts, new com.google.common.base.Predicate<java.lang.String>() {
                @java.lang.Override
                public boolean apply(@javax.annotation.Nullable
                java.lang.String groupHost) {
                    return clusterHosts.containsKey(groupHost);
                }
            });
            org.apache.ambari.server.controller.ConfigGroupRequest request = new org.apache.ambari.server.controller.ConfigGroupRequest(null, clusterName, absoluteGroupName, service, service, "Host Group Configuration", com.google.common.collect.Sets.newHashSet(filteredGroupHosts), serviceConfigs);
            org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider configGroupProvider = ((org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider) (org.apache.ambari.server.topology.AmbariContext.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup)));
            try {
                configGroupProvider.createResources(java.util.Collections.singleton(request));
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.topology.AmbariContext.LOG.error("Failed to create new configuration group: " + e);
                throw new java.lang.RuntimeException("Failed to create new configuration group: " + e, e);
            }
        }
    }

    private java.lang.String getConfigurationGroupName(java.lang.String bpName, java.lang.String hostGroupName) {
        return java.lang.String.format("%s:%s", bpName, hostGroupName);
    }

    public org.apache.ambari.server.state.ConfigHelper getConfigHelper() {
        return configHelper.get();
    }

    private synchronized org.apache.ambari.server.controller.internal.HostResourceProvider getHostResourceProvider() {
        if (org.apache.ambari.server.topology.AmbariContext.hostResourceProvider == null) {
            org.apache.ambari.server.topology.AmbariContext.hostResourceProvider = ((org.apache.ambari.server.controller.internal.HostResourceProvider) (org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host)));
        }
        return org.apache.ambari.server.topology.AmbariContext.hostResourceProvider;
    }

    private synchronized org.apache.ambari.server.controller.internal.HostComponentResourceProvider getHostComponentResourceProvider() {
        if (org.apache.ambari.server.topology.AmbariContext.hostComponentResourceProvider == null) {
            org.apache.ambari.server.topology.AmbariContext.hostComponentResourceProvider = ((org.apache.ambari.server.controller.internal.HostComponentResourceProvider) (org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)));
        }
        return org.apache.ambari.server.topology.AmbariContext.hostComponentResourceProvider;
    }

    private synchronized org.apache.ambari.server.controller.internal.ServiceResourceProvider getServiceResourceProvider() {
        if (org.apache.ambari.server.topology.AmbariContext.serviceResourceProvider == null) {
            org.apache.ambari.server.topology.AmbariContext.serviceResourceProvider = ((org.apache.ambari.server.controller.internal.ServiceResourceProvider) (org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Service)));
        }
        return org.apache.ambari.server.topology.AmbariContext.serviceResourceProvider;
    }

    private synchronized org.apache.ambari.server.controller.internal.ComponentResourceProvider getComponentResourceProvider() {
        if (org.apache.ambari.server.topology.AmbariContext.componentResourceProvider == null) {
            org.apache.ambari.server.topology.AmbariContext.componentResourceProvider = ((org.apache.ambari.server.controller.internal.ComponentResourceProvider) (org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Component)));
        }
        return org.apache.ambari.server.topology.AmbariContext.componentResourceProvider;
    }

    private synchronized org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider getVersionDefinitionResourceProvider() {
        if (org.apache.ambari.server.topology.AmbariContext.versionDefinitionResourceProvider == null) {
            org.apache.ambari.server.topology.AmbariContext.versionDefinitionResourceProvider = ((org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider) (org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition)));
        }
        return org.apache.ambari.server.topology.AmbariContext.versionDefinitionResourceProvider;
    }
}