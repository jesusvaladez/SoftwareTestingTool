package org.apache.ambari.server.controller.internal;
import com.google.inject.persist.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION;
@org.apache.ambari.server.StaticallyInject
public class ClusterStackVersionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.class);

    protected static final java.lang.String CLUSTER_STACK_VERSION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "id");

    protected static final java.lang.String CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "cluster_name");

    protected static final java.lang.String CLUSTER_STACK_VERSION_STACK_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "stack");

    protected static final java.lang.String CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "version");

    protected static final java.lang.String CLUSTER_STACK_VERSION_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "state");

    protected static final java.lang.String CLUSTER_STACK_VERSION_HOST_STATES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "host_states");

    protected static final java.lang.String CLUSTER_STACK_VERSION_REPO_SUMMARY_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "repository_summary");

    protected static final java.lang.String CLUSTER_STACK_VERSION_REPO_SUPPORTS_REVERT = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "supports_revert");

    protected static final java.lang.String CLUSTER_STACK_VERSION_REPO_REVERT_UPGRADE_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "revert_upgrade_id");

    protected static final java.lang.String CLUSTER_STACK_VERSION_REPOSITORY_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "repository_version");

    protected static final java.lang.String CLUSTER_STACK_VERSION_STAGE_SUCCESS_FACTOR = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ClusterStackVersions", "success_factor");

    protected static final java.lang.String CLUSTER_STACK_VERSION_FORCE = "ClusterStackVersions/force";

    protected static final java.lang.String INSTALL_PACKAGES_ACTION = "install_packages";

    protected static final java.lang.String INSTALL_PACKAGES_FULL_NAME = "Install Version";

    private static final float INSTALL_PACKAGES_SUCCESS_FACTOR = 0.85F;

    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STACK_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPOSITORY_VERSION_PROPERTY_ID);

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STACK_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_HOST_STATES_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPOSITORY_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STAGE_SUCCESS_FACTOR, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_FORCE, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPO_SUMMARY_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPO_SUPPORTS_REVERT, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPO_REVERT_UPGRADE_ID);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STACK_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPOSITORY_VERSION_PROPERTY_ID).build();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.controller.AmbariActionExecutionHelper> actionExecutionHelper;

    @com.google.inject.Inject
    private static org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private static org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private static org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repoVersionHelper;

    @com.google.inject.Inject
    private static com.google.gson.Gson gson;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> metaInfo;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> configHelperProvider;

    @com.google.inject.Inject
    public ClusterStackVersionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.keyPropertyIds, managementController);
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK));
        setRequiredDeleteAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK));
        setRequiredUpdateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK));
    }

    @java.lang.Override
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "this is a fake response until the UI no longer uses the endpoint")
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.LinkedHashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        if (1 != propertyMaps.size()) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Cannot request more than one resource");
        }
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = propertyMaps.iterator().next();
        java.lang.String clusterName = propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID).toString();
        final org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.clusters.get().getCluster(clusterName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
        }
        java.util.Set<java.lang.Long> requestedEntities = new java.util.LinkedHashSet<>();
        if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_ID_PROPERTY_ID)) {
            java.lang.Long id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_ID_PROPERTY_ID).toString());
            requestedEntities.add(id);
        } else {
            java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> entities = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.repositoryVersionDAO.findAll();
            java.util.Collections.sort(entities, new java.util.Comparator<org.apache.ambari.server.orm.entities.RepositoryVersionEntity>() {
                @java.lang.Override
                public int compare(org.apache.ambari.server.orm.entities.RepositoryVersionEntity o1, org.apache.ambari.server.orm.entities.RepositoryVersionEntity o2) {
                    return org.apache.ambari.server.utils.VersionUtils.compareVersionsWithBuild(o1.getVersion(), o2.getVersion(), 4);
                }
            });
            for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity : entities) {
                requestedEntities.add(entity.getId());
            }
        }
        if (requestedEntities.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Could not find any repositories to show");
        }
        org.apache.ambari.server.orm.entities.UpgradeEntity revertableUpgrade = null;
        if (null == cluster.getUpgradeInProgress()) {
            revertableUpgrade = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.upgradeDAO.findRevertable(cluster.getClusterId());
        }
        for (java.lang.Long repositoryVersionId : requestedEntities) {
            final org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion);
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.repositoryVersionDAO.findByPK(repositoryVersionId);
            final java.util.List<org.apache.ambari.server.state.RepositoryVersionState> allStates = new java.util.ArrayList<>();
            final java.util.Map<org.apache.ambari.server.state.RepositoryVersionState, java.util.List<java.lang.String>> hostStates = new java.util.HashMap<>();
            for (org.apache.ambari.server.state.RepositoryVersionState state : org.apache.ambari.server.state.RepositoryVersionState.values()) {
                hostStates.put(state, new java.util.ArrayList<>());
            }
            org.apache.ambari.server.orm.entities.StackEntity repoVersionStackEntity = repositoryVersion.getStack();
            org.apache.ambari.server.state.StackId repoVersionStackId = new org.apache.ambari.server.state.StackId(repoVersionStackEntity);
            java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionsForRepository = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.hostVersionDAO.findHostVersionByClusterAndRepository(cluster.getClusterId(), repositoryVersion);
            for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersionsForRepository) {
                hostStates.get(hostVersionEntity.getState()).add(hostVersionEntity.getHostName());
                allStates.add(hostVersionEntity.getState());
            }
            org.apache.ambari.server.state.repository.ClusterVersionSummary versionSummary = null;
            try {
                org.apache.ambari.server.state.repository.VersionDefinitionXml vdf = repositoryVersion.getRepositoryXml();
                if (null != vdf) {
                    versionSummary = vdf.getClusterSummary(cluster, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.metaInfo.get());
                }
            } catch (java.lang.Exception e) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Version %s is backed by a version definition, but it could not be parsed", repositoryVersion.getVersion()), e);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_HOST_STATES_PROPERTY_ID, hostStates, requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPO_SUMMARY_PROPERTY_ID, versionSummary, requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_ID_PROPERTY_ID, repositoryVersion.getId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STACK_PROPERTY_ID, repoVersionStackId.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID, repoVersionStackId.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPOSITORY_VERSION_PROPERTY_ID, repositoryVersion.getId(), requestedIds);
            @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "this is a fake status until the UI can handle services that are on their own")
            org.apache.ambari.server.state.RepositoryVersionState aggregateState = org.apache.ambari.server.state.RepositoryVersionState.getAggregateState(allStates);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STATE_PROPERTY_ID, aggregateState, requestedIds);
            boolean revertable = false;
            if (null != revertableUpgrade) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity revertableRepositoryVersion = revertableUpgrade.getRepositoryVersion();
                revertable = java.util.Objects.equals(revertableRepositoryVersion.getId(), repositoryVersionId);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPO_SUPPORTS_REVERT, revertable, requestedIds);
            if (revertable) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPO_REVERT_UPGRADE_ID, revertableUpgrade.getId(), requestedIds);
            }
            if ((predicate == null) || predicate.evaluate(resource)) {
                resources.add(resource);
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        if (request.getProperties().size() > 1) {
            throw new java.lang.UnsupportedOperationException("Multiple requests cannot be executed at the same time.");
        }
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        java.lang.String clName;
        final java.lang.String desiredRepoVersion;
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = iterator.next();
        java.util.Set<java.lang.String> requiredProperties = new java.util.HashSet<>();
        requiredProperties.add(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID);
        requiredProperties.add(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPOSITORY_VERSION_PROPERTY_ID);
        requiredProperties.add(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STACK_PROPERTY_ID);
        requiredProperties.add(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID);
        for (java.lang.String requiredProperty : requiredProperties) {
            if (!propertyMap.containsKey(requiredProperty)) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("The required property %s is not defined", requiredProperty));
            }
        }
        clName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID)));
        desiredRepoVersion = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_REPOSITORY_VERSION_PROPERTY_ID)));
        org.apache.ambari.server.state.Cluster cluster;
        org.apache.ambari.server.controller.AmbariManagementController managementController = getManagementController();
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = managementController.getAmbariMetaInfo();
        try {
            org.apache.ambari.server.state.Clusters clusters = managementController.getClusters();
            cluster = clusters.getCluster(clName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
        }
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = cluster.getUpgradeInProgress();
        if (null != entity) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Cluster %s %s is in progress.  Cannot install packages.", cluster.getClusterName(), entity.getDirection().getText(false)));
        }
        java.lang.String stackName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STACK_PROPERTY_ID)));
        java.lang.String stackVersion = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID)));
        if (org.apache.commons.lang.StringUtils.isBlank(stackName) || org.apache.commons.lang.StringUtils.isBlank(stackVersion)) {
            java.lang.String message = java.lang.String.format("Both the %s and %s properties are required when distributing a new stack", org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STACK_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID);
            throw new org.apache.ambari.server.controller.spi.SystemException(message);
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackName, stackVersion);
        if (!ami.isSupportedStack(stackName, stackVersion)) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Stack %s is not supported", stackId));
        }
        try {
            bootstrapStackTools(stackId, cluster);
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Unable to modify stack tools for new stack being distributed", ambariException);
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.repositoryVersionDAO.findByStackAndVersion(stackId, desiredRepoVersion);
        if (repoVersionEntity == null) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Repo version %s is not available for stack %s", desiredRepoVersion, stackId));
        }
        org.apache.ambari.server.state.repository.VersionDefinitionXml desiredVersionDefinition = null;
        try {
            desiredVersionDefinition = repoVersionEntity.getRepositoryXml();
        } catch (java.lang.Exception e) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Version %s is backed by a version definition, but it could not be parsed", desiredRepoVersion), e);
        }
        try {
            if (repoVersionEntity.getType().isPartial()) {
                java.util.Set<java.lang.String> missingDependencies = desiredVersionDefinition.getMissingDependencies(cluster, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.metaInfo.get());
                if (!missingDependencies.isEmpty()) {
                    java.lang.String message = java.lang.String.format("The following services are also required to be included in this upgrade: %s", org.apache.commons.lang.StringUtils.join(missingDependencies, ", "));
                    throw new org.apache.ambari.server.controller.spi.SystemException(message.toString());
                }
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Unable to determine if this repository contains the necessary service dependencies", ambariException);
        }
        boolean forceInstalled = java.lang.Boolean.parseBoolean(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_FORCE))));
        try {
            return createOrUpdateHostVersions(cluster, repoVersionEntity, desiredVersionDefinition, stackId, forceInstalled, propertyMap);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Can not persist request", e);
        }
    }

    @com.google.inject.persist.Transactional(rollbackOn = { java.lang.RuntimeException.class, org.apache.ambari.server.controller.spi.SystemException.class, org.apache.ambari.server.AmbariException.class })
    org.apache.ambari.server.controller.spi.RequestStatus createOrUpdateHostVersions(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity, org.apache.ambari.server.state.repository.VersionDefinitionXml versionDefinitionXml, org.apache.ambari.server.state.StackId stackId, boolean forceInstalled, java.util.Map<java.lang.String, java.lang.Object> propertyMap) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.controller.spi.SystemException {
        final java.lang.String desiredRepoVersion = repoVersionEntity.getVersion();
        java.util.List<org.apache.ambari.server.state.Host> hosts = com.google.common.collect.Lists.newArrayList(cluster.getHosts());
        for (org.apache.ambari.server.state.Host host : hosts) {
            for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : host.getAllHostVersions()) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity hostRepoVersion = hostVersion.getRepositoryVersion();
                if (!hostRepoVersion.getStackName().equals(repoVersionEntity.getStackName())) {
                    continue;
                }
                int compare = org.apache.ambari.server.utils.VersionUtils.compareVersionsWithBuild(hostRepoVersion.getVersion(), desiredRepoVersion, 4);
                if (compare <= 0) {
                    continue;
                }
                if (null == versionDefinitionXml) {
                    continue;
                }
                if (org.apache.commons.lang.StringUtils.isBlank(versionDefinitionXml.getPackageVersion(host.getOsFamily()))) {
                    java.lang.String msg = java.lang.String.format("Ambari cannot install version %s.  Version %s is already installed.", desiredRepoVersion, hostRepoVersion.getVersion());
                    throw new java.lang.IllegalArgumentException(msg);
                }
            }
        }
        java.util.List<org.apache.ambari.server.state.Host> hostsNeedingInstallCommands = cluster.transitionHostsToInstalling(repoVersionEntity, versionDefinitionXml, forceInstalled);
        org.apache.ambari.server.controller.RequestStatusResponse response = null;
        if (!forceInstalled) {
            org.apache.ambari.server.controller.internal.RequestStageContainer installRequest = createOrchestration(cluster, stackId, hostsNeedingInstallCommands, repoVersionEntity, versionDefinitionXml, propertyMap);
            response = installRequest.getRequestStatusResponse();
        }
        return getRequestStatus(response);
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.controller.internal.RequestStageContainer createOrchestration(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, java.util.List<org.apache.ambari.server.state.Host> hosts, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEnt, org.apache.ambari.server.state.repository.VersionDefinitionXml desiredVersionDefinition, java.util.Map<java.lang.String, java.lang.Object> propertyMap) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.controller.spi.SystemException {
        final org.apache.ambari.server.controller.AmbariManagementController managementController = getManagementController();
        final org.apache.ambari.server.api.services.AmbariMetaInfo ami = managementController.getAmbariMetaInfo();
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = repoVersionEnt.getRepoOsEntities();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.orm.entities.RepoDefinitionEntity>> perOsRepos = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystem : operatingSystems) {
            if (operatingSystem.isAmbariManaged()) {
                perOsRepos.put(operatingSystem.getFamily(), operatingSystem.getRepoDefinitionEntities());
            } else {
                perOsRepos.put(operatingSystem.getFamily(), java.util.Collections.<org.apache.ambari.server.orm.entities.RepoDefinitionEntity>emptyList());
            }
        }
        org.apache.ambari.server.controller.internal.RequestStageContainer req = createRequest();
        java.util.Iterator<org.apache.ambari.server.state.Host> hostIterator = hosts.iterator();
        java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.HashMap<>();
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION, getManagementController().getJdkResourceUrl());
        java.lang.String hostParamsJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostLevelParams);
        int maxTasks = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.configuration.getAgentPackageParallelCommandsLimit();
        int hostCount = hosts.size();
        int batchCount = ((int) (java.lang.Math.ceil(((double) (hostCount)) / maxTasks)));
        long stageId = req.getLastStageId() + 1;
        if (0L == stageId) {
            stageId = 1L;
        }
        java.lang.Float successFactor = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.INSTALL_PACKAGES_SUCCESS_FACTOR;
        java.lang.String successFactorProperty = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.CLUSTER_STACK_VERSION_STAGE_SUCCESS_FACTOR)));
        if (org.apache.commons.lang.StringUtils.isNotBlank(successFactorProperty)) {
            successFactor = java.lang.Float.valueOf(successFactorProperty);
        }
        boolean hasStage = false;
        java.util.ArrayList<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>(batchCount);
        for (int batchId = 1; batchId <= batchCount; batchId++) {
            java.lang.String stageName;
            if (batchCount > 1) {
                stageName = java.lang.String.format(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.INSTALL_PACKAGES_FULL_NAME + ". Batch %d of %d", batchId, batchCount);
            } else {
                stageName = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.INSTALL_PACKAGES_FULL_NAME;
            }
            org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.stageFactory.createNew(req.getId(), "/tmp/ambari", cluster.getClusterName(), cluster.getClusterId(), stageName, "{}", hostParamsJson);
            stage.getSuccessFactors().put(org.apache.ambari.server.Role.INSTALL_PACKAGES, successFactor);
            stage.setStageId(stageId);
            stageId++;
            stages.add(stage);
            java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
            if (org.apache.ambari.spi.RepositoryType.STANDARD != repoVersionEnt.getType()) {
                org.apache.ambari.server.state.repository.ClusterVersionSummary clusterSummary = desiredVersionDefinition.getClusterSummary(cluster, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.metaInfo.get());
                serviceNames.addAll(clusterSummary.getAvailableServiceNames());
            } else {
                serviceNames.addAll(ami.getStack(stackId).getServiceNames());
            }
            for (int i = 0; (i < maxTasks) && hostIterator.hasNext(); i++) {
                org.apache.ambari.server.state.Host host = hostIterator.next();
                if (hostHasVersionableComponents(cluster, serviceNames, ami, stackId, host)) {
                    org.apache.ambari.server.controller.ActionExecutionContext actionContext = getHostVersionInstallCommand(repoVersionEnt, cluster, managementController, ami, stackId, serviceNames, stage, host);
                    if (null != actionContext) {
                        try {
                            org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.actionExecutionHelper.get().addExecutionCommandsToStage(actionContext, stage, null);
                            hasStage = true;
                        } catch (org.apache.ambari.server.AmbariException e) {
                            throw new org.apache.ambari.server.controller.spi.SystemException("Cannot modify stage", e);
                        }
                    }
                }
            }
        }
        if (!hasStage) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("There are no hosts that have components to install for repository %s", repoVersionEnt.getDisplayName()));
        }
        req.addStages(stages);
        req.persist();
        return req;
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.controller.ActionExecutionContext getHostVersionInstallCommand(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.AmbariManagementController managementController, org.apache.ambari.server.api.services.AmbariMetaInfo ami, final org.apache.ambari.server.state.StackId stackId, java.util.Set<java.lang.String> repoServices, org.apache.ambari.server.actionmanager.Stage stage1, org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String osFamily = host.getOsFamily();
        org.apache.ambari.server.orm.entities.RepoOsEntity osEntity = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.repoVersionHelper.getOSEntityForHost(host, repoVersion);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(osEntity.getRepoDefinitionEntities())) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Repositories for os type %s are not defined for version %s of Stack %s.", osFamily, repoVersion.getVersion(), stackId));
        }
        java.util.Set<java.lang.String> servicesOnHost = new java.util.HashSet<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> components = cluster.getServiceComponentHosts(host.getHostName());
        for (org.apache.ambari.server.state.ServiceComponentHost component : components) {
            if (repoServices.isEmpty() || repoServices.contains(component.getServiceName())) {
                servicesOnHost.add(component.getServiceName());
            }
        }
        if (servicesOnHost.isEmpty()) {
            return null;
        }
        java.util.Map<java.lang.String, java.lang.String> roleParams = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.repoVersionHelper.buildRoleParams(managementController, repoVersion, osFamily, servicesOnHost);
        org.apache.ambari.server.controller.internal.RequestResourceFilter filter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(null, null, java.util.Collections.singletonList(host.getHostName()));
        org.apache.ambari.server.controller.ActionExecutionContext actionContext = new org.apache.ambari.server.controller.ActionExecutionContext(cluster.getClusterName(), org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.INSTALL_PACKAGES_ACTION, java.util.Collections.singletonList(filter), roleParams);
        actionContext.setStackId(repoVersion.getStackId());
        actionContext.setTimeout(java.lang.Integer.valueOf(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.configuration.getDefaultAgentTaskTimeout(true)));
        org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.repoVersionHelper.addCommandRepositoryToContext(actionContext, repoVersion, osEntity);
        return actionContext;
    }

    private boolean hostHasVersionableComponents(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> serviceNames, org.apache.ambari.server.api.services.AmbariMetaInfo ami, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> components = cluster.getServiceComponentHosts(host.getHostName());
        for (org.apache.ambari.server.state.ServiceComponentHost component : components) {
            if ((!serviceNames.isEmpty()) && (!serviceNames.contains(component.getServiceName()))) {
                continue;
            }
            org.apache.ambari.server.state.ComponentInfo componentInfo;
            try {
                componentInfo = ami.getComponent(stackId.getStackName(), stackId.getStackVersion(), component.getServiceName(), component.getServiceComponentName());
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.LOG.warn(java.lang.String.format("Exception while accessing component %s of service %s for stack %s", component.getServiceComponentName(), component.getServiceName(), stackId));
                continue;
            }
            if (componentInfo.isVersionAdvertised()) {
                return true;
            }
        }
        return false;
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer createRequest() {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = getManagementController().getActionManager();
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.requestFactory, actionManager);
        requestStages.setRequestContext(org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.INSTALL_PACKAGES_FULL_NAME);
        return requestStages;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Method not supported");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.pkPropertyIds;
    }

    private void bootstrapStackTools(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        if (org.apache.commons.lang.StringUtils.equals(stackId.getStackName(), cluster.getCurrentStackVersion().getStackName())) {
            return;
        }
        org.apache.ambari.server.state.ConfigHelper configHelper = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.configHelperProvider.get();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> defaultStackConfigurationsByType = configHelper.getDefaultStackProperties(stackId);
        java.util.Map<java.lang.String, java.lang.String> clusterEnvDefaults = defaultStackConfigurationsByType.get(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV);
        org.apache.ambari.server.state.Config clusterEnv = cluster.getDesiredConfigByType(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV);
        java.util.Map<java.lang.String, java.lang.String> clusterEnvProperties = clusterEnv.getProperties();
        java.util.Set<java.lang.String> properties = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_TOOLS_PROPERTY, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_FEATURES_PROPERTY, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_PACKAGES_PROPERTY);
        java.util.Map<java.lang.String, java.lang.String> updatedProperties = new java.util.HashMap<>();
        for (java.lang.String property : properties) {
            java.lang.String newStackDefaultJson = clusterEnvDefaults.get(property);
            if (org.apache.commons.lang.StringUtils.isBlank(newStackDefaultJson)) {
                continue;
            }
            java.lang.String existingPropertyJson = clusterEnvProperties.get(property);
            if (org.apache.commons.lang.StringUtils.isBlank(existingPropertyJson)) {
                updatedProperties.put(property, newStackDefaultJson);
                continue;
            }
            final java.util.Map<java.lang.String, java.lang.Object> existingJson;
            final java.util.Map<java.lang.String, ?> newStackJsonAsObject;
            if (org.apache.commons.lang.StringUtils.equals(property, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY)) {
                existingJson = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.gson.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(existingPropertyJson, java.util.Map.class);
                newStackJsonAsObject = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(newStackDefaultJson, java.util.Map.class);
            } else {
                existingJson = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.gson.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(existingPropertyJson, java.util.Map.class);
                newStackJsonAsObject = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.gson.<java.util.Map<java.lang.String, java.util.Map<java.lang.Object, java.lang.Object>>>fromJson(newStackDefaultJson, java.util.Map.class);
            }
            if (existingJson.keySet().contains(stackId.getStackName())) {
                continue;
            }
            existingJson.put(stackId.getStackName(), newStackJsonAsObject.get(stackId.getStackName()));
            java.lang.String newJson = org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider.gson.toJson(existingJson);
            updatedProperties.put(property, newJson);
        }
        if (!updatedProperties.isEmpty()) {
            org.apache.ambari.server.controller.AmbariManagementController amc = getManagementController();
            java.lang.String serviceNote = java.lang.String.format("Adding stack tools for %s while distributing a new repository", stackId.toString());
            configHelper.updateConfigType(cluster, stackId, amc, clusterEnv.getType(), updatedProperties, null, amc.getAuthName(), serviceNote);
        }
    }
}