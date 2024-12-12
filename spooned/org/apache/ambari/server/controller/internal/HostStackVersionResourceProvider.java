package org.apache.ambari.server.controller.internal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_STACK_NAME;
@org.apache.ambari.server.StaticallyInject
public class HostStackVersionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.class);

    protected static final java.lang.String HOST_STACK_VERSION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "id");

    protected static final java.lang.String HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "cluster_name");

    protected static final java.lang.String HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "host_name");

    protected static final java.lang.String HOST_STACK_VERSION_STACK_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "stack");

    protected static final java.lang.String HOST_STACK_VERSION_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "version");

    protected static final java.lang.String HOST_STACK_VERSION_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "state");

    protected static final java.lang.String HOST_STACK_VERSION_REPOSITORIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "repositories");

    protected static final java.lang.String HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "repository_version");

    protected static final java.lang.String HOST_STACK_VERSION_FORCE_INSTALL_ON_NON_MEMBER_HOST_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "force_non_member_install");

    protected static final java.lang.String HOST_STACK_VERSION_COMPONENT_NAMES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostStackVersions", "components");

    protected static final java.lang.String COMPONENT_NAME_PROPERTY_ID = "name";

    protected static final java.lang.String INSTALL_PACKAGES_ACTION = "install_packages";

    protected static final java.lang.String STACK_SELECT_ACTION = "stack_select_set_all";

    protected static final java.lang.String INSTALL_PACKAGES_FULL_NAME = "Install Version";

    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID);

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPOSITORIES_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_FORCE_INSTALL_ON_NON_MEMBER_HOST_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_COMPONENT_NAMES_PROPERTY_ID);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>() {
        {
            put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID);
            put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID);
            put(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_ID_PROPERTY_ID);
            put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID);
            put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID);
            put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID);
        }
    };

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private static org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.controller.AmbariActionExecutionHelper> actionExecutionHelper;

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private static org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repoVersionHelper;

    public HostStackVersionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            final java.lang.String hostName = propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID).toString();
            java.lang.String clusterName = null;
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID)) {
                clusterName = propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID).toString();
            }
            final java.lang.Long id;
            java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> requestedEntities;
            if (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_ID_PROPERTY_ID) == null) {
                if (clusterName == null) {
                    requestedEntities = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.hostVersionDAO.findByHost(hostName);
                } else {
                    requestedEntities = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.hostVersionDAO.findByClusterAndHost(clusterName, hostName);
                }
            } else {
                try {
                    id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_ID_PROPERTY_ID).toString());
                } catch (java.lang.Exception ex) {
                    throw new org.apache.ambari.server.controller.spi.SystemException("Stack version should have numerical id");
                }
                final org.apache.ambari.server.orm.entities.HostVersionEntity entity = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.hostVersionDAO.findByPK(id);
                if (entity == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("There is no stack version with id " + id);
                } else {
                    requestedEntities = java.util.Collections.singletonList(entity);
                }
            }
            if (requestedEntities != null) {
                addRequestedEntities(resources, requestedEntities, requestedIds, clusterName);
            }
        }
        return resources;
    }

    public void addRequestedEntities(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> requestedEntities, java.util.Set<java.lang.String> requestedIds, java.lang.String clusterName) {
        for (org.apache.ambari.server.orm.entities.HostVersionEntity entity : requestedEntities) {
            org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(entity.getRepositoryVersion().getStack());
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVerEntity = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.repositoryVersionDAO.findByStackAndVersion(stackId, entity.getRepositoryVersion().getVersion());
            final org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID, entity.getHostName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_ID_PROPERTY_ID, entity.getId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID, stackId.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID, stackId.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STATE_PROPERTY_ID, entity.getState().name(), requestedIds);
            if (clusterName != null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
            }
            if (repoVerEntity != null) {
                java.lang.Long repoVersionId = repoVerEntity.getId();
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID, repoVersionId, requestedIds);
            }
            resources.add(resource);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        java.lang.String hostName;
        final java.lang.String desiredRepoVersion;
        java.lang.String stackName;
        java.lang.String stackVersion;
        if (request.getProperties().size() > 1) {
            throw new java.lang.UnsupportedOperationException("Multiple requests cannot be executed at the same time.");
        }
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = iterator.next();
        java.util.Set<java.lang.String> requiredProperties = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID);
        for (java.lang.String requiredProperty : requiredProperties) {
            org.apache.commons.lang.Validate.isTrue(propertyMap.containsKey(requiredProperty), java.lang.String.format("The required property %s is not defined", requiredProperty));
        }
        java.lang.String clName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID)));
        hostName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_HOST_NAME_PROPERTY_ID)));
        desiredRepoVersion = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_REPO_VERSION_PROPERTY_ID)));
        stackName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_STACK_PROPERTY_ID)));
        stackVersion = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_VERSION_PROPERTY_ID)));
        boolean forceInstallOnNonMemberHost = false;
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> componentNames = null;
        java.lang.String forceInstallOnNonMemberHostString = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_FORCE_INSTALL_ON_NON_MEMBER_HOST_PROPERTY_ID)));
        if (org.apache.commons.lang.BooleanUtils.toBoolean(forceInstallOnNonMemberHostString)) {
            forceInstallOnNonMemberHost = true;
            componentNames = ((java.util.Set<java.util.Map<java.lang.String, java.lang.String>>) (propertyMap.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_COMPONENT_NAMES_PROPERTY_ID)));
            if (componentNames == null) {
                throw new java.lang.IllegalArgumentException((("In case " + org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.HOST_STACK_VERSION_FORCE_INSTALL_ON_NON_MEMBER_HOST_PROPERTY_ID) + " is set to true, the list of ") + "components should be specified in request.");
            }
        }
        org.apache.ambari.server.controller.internal.RequestStageContainer req = createInstallPackagesRequest(hostName, desiredRepoVersion, stackName, stackVersion, clName, forceInstallOnNonMemberHost, componentNames);
        return getRequestStatus(req.getRequestStatusResponse());
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer createInstallPackagesRequest(java.lang.String hostName, final java.lang.String desiredRepoVersion, java.lang.String stackName, java.lang.String stackVersion, java.lang.String clName, final boolean forceInstallOnNonMemberHost, java.util.Set<java.util.Map<java.lang.String, java.lang.String>> componentNames) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.state.Host host;
        try {
            host = getManagementController().getClusters().getHost(hostName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Can not find host %s", hostName), e);
        }
        org.apache.ambari.server.controller.AmbariManagementController managementController = getManagementController();
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = managementController.getAmbariMetaInfo();
        final org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackName, stackVersion);
        if (!ami.isSupportedStack(stackName, stackVersion)) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Stack %s is not supported", stackId));
        }
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet;
        if (clName == null) {
            try {
                clusterSet = getManagementController().getClusters().getClustersForHost(hostName);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Host %s does not belong to any cluster", hostName), e);
            }
        } else {
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = getManagementController().getClusters().getCluster(clName);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Cluster %s does not exist", clName), e);
            }
            clusterSet = java.util.Collections.singleton(cluster);
        }
        org.apache.ambari.server.state.Cluster cluster;
        if (clusterSet.isEmpty()) {
            throw new java.lang.UnsupportedOperationException(java.lang.String.format("Host %s belongs " + "to 0 clusters with stack id %s. Performing %s action failed.", hostName, stackId, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.INSTALL_PACKAGES_FULL_NAME));
        } else if (clusterSet.size() > 1) {
            throw new java.lang.UnsupportedOperationException(java.lang.String.format("Host %s belongs " + ("to %d clusters with stack id %s. Performing %s action on multiple " + "clusters is not supported"), hostName, clusterSet.size(), stackId, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.INSTALL_PACKAGES_FULL_NAME));
        } else {
            cluster = clusterSet.iterator().next();
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEnt = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.repositoryVersionDAO.findByStackAndVersion(stackId, desiredRepoVersion);
        if (repoVersionEnt == null) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Repo version %s is not available for stack %s", desiredRepoVersion, stackId));
        }
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersEntity = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.hostVersionDAO.findByClusterStackVersionAndHost(clName, stackId, desiredRepoVersion, hostName);
        if (!forceInstallOnNonMemberHost) {
            if (hostVersEntity == null) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Repo version %s for stack %s is not available for host %s", desiredRepoVersion, stackId, hostName));
            }
            if (((hostVersEntity.getState() != org.apache.ambari.server.state.RepositoryVersionState.INSTALLED) && (hostVersEntity.getState() != org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED)) && (hostVersEntity.getState() != org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC)) {
                throw new java.lang.UnsupportedOperationException(java.lang.String.format("Repo version %s for stack %s " + "for host %s is in %s state. Can not transition to INSTALLING state", desiredRepoVersion, stackId, hostName, hostVersEntity.getState().toString()));
            }
        }
        java.lang.String osFamily = host.getOsFamily();
        org.apache.ambari.server.orm.entities.RepoOsEntity osEntity = null;
        for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystem : repoVersionEnt.getRepoOsEntities()) {
            if (osFamily.equals(operatingSystem.getFamily())) {
                osEntity = operatingSystem;
                break;
            }
        }
        if (null == osEntity) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Operating System matching %s could not be found", osFamily));
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(osEntity.getRepoDefinitionEntities())) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Repositories for os type %s are " + "not defined. Repo version=%s, stackId=%s", osFamily, desiredRepoVersion, stackId));
        }
        java.util.Set<java.lang.String> servicesOnHost = new java.util.HashSet<>();
        if (forceInstallOnNonMemberHost) {
            for (java.util.Map<java.lang.String, java.lang.String> componentProperties : componentNames) {
                java.lang.String componentName = componentProperties.get(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.COMPONENT_NAME_PROPERTY_ID);
                if (org.apache.commons.lang.StringUtils.isEmpty(componentName)) {
                    throw new java.lang.IllegalArgumentException("Components list contains a component with no 'name' property");
                }
                java.lang.String serviceName = null;
                try {
                    serviceName = ami.getComponentToService(stackName, stackVersion, componentName.trim().toUpperCase());
                    if (serviceName == null) {
                        throw new java.lang.IllegalArgumentException("Service not found for component : " + componentName);
                    }
                    servicesOnHost.add(serviceName);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.LOG.error("Service not found for component {}!", componentName, e);
                    throw new java.lang.IllegalArgumentException("Service not found for component : " + componentName);
                }
            }
        } else {
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> components = cluster.getServiceComponentHosts(host.getHostName());
            for (org.apache.ambari.server.state.ServiceComponentHost component : components) {
                servicesOnHost.add(component.getServiceName());
            }
        }
        java.util.Map<java.lang.String, java.lang.String> roleParams = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.repoVersionHelper.buildRoleParams(managementController, repoVersionEnt, osFamily, servicesOnHost);
        org.apache.ambari.server.controller.internal.RequestResourceFilter filter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(null, null, java.util.Collections.singletonList(hostName));
        roleParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS, null);
        roleParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_STACK_NAME, null);
        org.apache.ambari.server.controller.ActionExecutionContext actionContext = new org.apache.ambari.server.controller.ActionExecutionContext(null, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.INSTALL_PACKAGES_ACTION, java.util.Collections.singletonList(filter), roleParams);
        actionContext.setTimeout(java.lang.Integer.valueOf(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.configuration.getDefaultAgentTaskTimeout(true)));
        actionContext.setStackId(repoVersionEnt.getStackId());
        org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.repoVersionHelper.addCommandRepositoryToContext(actionContext, repoVersionEnt, osEntity);
        java.lang.String caption = java.lang.String.format(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.INSTALL_PACKAGES_FULL_NAME + " on host %s", hostName);
        org.apache.ambari.server.controller.internal.RequestStageContainer req = createRequest(caption);
        java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.HashMap<>();
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION, getManagementController().getJdkResourceUrl());
        java.lang.String clusterHostInfoJson;
        try {
            clusterHostInfoJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster));
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Could not build cluster topology", e);
        }
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.stageFactory.createNew(req.getId(), "/tmp/ambari", cluster.getClusterName(), cluster.getClusterId(), caption, "{}", org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostLevelParams));
        long stageId = req.getLastStageId() + 1;
        if (0L == stageId) {
            stageId = 1L;
        }
        stage.setStageId(stageId);
        req.addStages(java.util.Collections.singletonList(stage));
        try {
            org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.actionExecutionHelper.get().addExecutionCommandsToStage(actionContext, stage, null, !forceInstallOnNonMemberHost);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Can not modify stage", e);
        }
        if (forceInstallOnNonMemberHost) {
            addSelectStackStage(desiredRepoVersion, forceInstallOnNonMemberHost, cluster, filter, caption, req, hostLevelParams, clusterHostInfoJson);
        }
        try {
            if (!forceInstallOnNonMemberHost) {
                hostVersEntity.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
                org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.hostVersionDAO.merge(hostVersEntity);
            }
            req.persist();
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Can not persist request", e);
        }
        return req;
    }

    private void addSelectStackStage(java.lang.String desiredRepoVersion, boolean forceInstallOnNonMemberHost, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.internal.RequestResourceFilter filter, java.lang.String caption, org.apache.ambari.server.controller.internal.RequestStageContainer req, java.util.Map<java.lang.String, java.lang.String> hostLevelParams, java.lang.String clusterHostInfoJson) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.actionmanager.Stage stage;
        long stageId;
        org.apache.ambari.server.controller.ActionExecutionContext actionContext;
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("version", desiredRepoVersion);
        stage = org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.stageFactory.createNew(req.getId(), "/tmp/ambari", cluster.getClusterName(), cluster.getClusterId(), caption, org.apache.ambari.server.utils.StageUtils.getGson().toJson(commandParams), org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostLevelParams));
        stageId = req.getLastStageId() + 1;
        if (0L == stageId) {
            stageId = 1L;
        }
        stage.setStageId(stageId);
        req.addStages(java.util.Collections.singletonList(stage));
        actionContext = new org.apache.ambari.server.controller.ActionExecutionContext(null, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.STACK_SELECT_ACTION, java.util.Collections.singletonList(filter), java.util.Collections.emptyMap());
        actionContext.setTimeout(java.lang.Integer.valueOf(org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.configuration.getDefaultAgentTaskTimeout(true)));
        try {
            org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.actionExecutionHelper.get().addExecutionCommandsToStage(actionContext, stage, null, !forceInstallOnNonMemberHost);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Can not modify stage", e);
        }
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer createRequest(java.lang.String caption) {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = getManagementController().getActionManager();
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.requestFactory, actionManager);
        requestStages.setRequestContext(caption);
        return requestStages;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Method not supported");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Method not supported");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider.pkPropertyIds;
    }
}