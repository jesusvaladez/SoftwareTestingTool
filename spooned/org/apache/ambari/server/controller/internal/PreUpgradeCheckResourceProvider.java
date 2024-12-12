package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class PreUpgradeCheckResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.class);

    public static final java.lang.String UPGRADE_CHECK_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "id");

    public static final java.lang.String UPGRADE_CHECK_CHECK_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "check");

    public static final java.lang.String UPGRADE_CHECK_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "status");

    public static final java.lang.String UPGRADE_CHECK_REASON_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "reason");

    public static final java.lang.String UPGRADE_CHECK_FAILED_ON_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "failed_on");

    public static final java.lang.String UPGRADE_CHECK_FAILED_DETAIL_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "failed_detail");

    public static final java.lang.String UPGRADE_CHECK_CHECK_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "check_type");

    public static final java.lang.String UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "cluster_name");

    public static final java.lang.String UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "upgrade_type");

    public static final java.lang.String UPGRADE_CHECK_TARGET_REPOSITORY_VERSION_ID_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "repository_version_id");

    public static final java.lang.String UPGRADE_CHECK_TARGET_REPOSITORY_VERSION = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "repository_version");

    public static final java.lang.String UPGRADE_CHECK_UPGRADE_PACK_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "upgrade_pack");

    public static final java.lang.String UPGRADE_CHECK_REPOSITORY_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "repository_version");

    public static final java.lang.String UPGRADE_CHECK_FOR_REVERT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeChecks", "for_revert");

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clustersProvider;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.checks.UpgradeCheckRegistry> upgradeCheckRegistryProvider;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper> upgradeHelper;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.configuration.Configuration> config;

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.CheckHelper checkHelper;

    private static final java.util.Set<java.lang.String> pkPropertyIds = java.util.Collections.singleton(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_ID_PROPERTY_ID);

    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CHECK_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_REASON_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FAILED_ON_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FAILED_DETAIL_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CHECK_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FOR_REVERT_PROPERTY_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_TARGET_REPOSITORY_VERSION_ID_ID, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_PACK_PROPERTY_ID);

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.PreUpgradeCheck, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID).build();

    public PreUpgradeCheckResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.PreUpgradeCheck, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            final java.lang.String clusterName = propertyMap.get(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID).toString();
            org.apache.ambari.spi.upgrade.UpgradeType upgradeType = org.apache.ambari.spi.upgrade.UpgradeType.ROLLING;
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID)) {
                try {
                    upgradeType = org.apache.ambari.spi.upgrade.UpgradeType.valueOf(propertyMap.get(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID).toString());
                } catch (java.lang.Exception e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Property %s has an incorrect value of %s.", org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID, propertyMap.get(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID)));
                }
            }
            final org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.clustersProvider.get().getCluster(clusterName);
            } catch (org.apache.ambari.server.AmbariException ambariException) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(ambariException.getMessage());
            }
            org.apache.ambari.server.state.StackId sourceStackId = cluster.getCurrentStackVersion();
            java.lang.String repositoryVersionId = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_TARGET_REPOSITORY_VERSION_ID_ID)));
            if (org.apache.commons.lang.StringUtils.isBlank(repositoryVersionId)) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("%s is a required property when executing upgrade checks", org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_TARGET_REPOSITORY_VERSION_ID_ID));
            }
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.repositoryVersionDAO.findByPK(java.lang.Long.valueOf(repositoryVersionId));
            org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = null;
            java.lang.String preferredUpgradePackName = (propertyMap.containsKey(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_PACK_PROPERTY_ID)) ? ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_PACK_PROPERTY_ID))) : null;
            try {
                upgradePack = org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.upgradeHelper.get().suggestUpgradePack(clusterName, sourceStackId, repositoryVersion.getStackId(), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, upgradeType, preferredUpgradePackName);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
            if (upgradePack == null) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Upgrade pack not found for the target repository version %s", repositoryVersion));
            }
            org.apache.ambari.spi.ClusterInformation clusterInformation = cluster.buildClusterInformation();
            org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
            org.apache.ambari.spi.RepositoryVersion targetRepositoryVersion = new org.apache.ambari.spi.RepositoryVersion(repositoryVersion.getId(), stackId.getStackName(), stackId.getStackVersion(), stackId.getStackId(), repositoryVersion.getVersion(), repositoryVersion.getType());
            final org.apache.ambari.spi.upgrade.UpgradeCheckRequest upgradeCheckRequest = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, upgradeType, targetRepositoryVersion, upgradePack.getPrerequisiteCheckConfig().getAllProperties(), new org.apache.ambari.server.controller.internal.URLStreamProvider.AmbariHttpUrlConnectionProvider());
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FOR_REVERT_PROPERTY_ID)) {
                java.lang.Boolean forRevert = org.apache.commons.lang.BooleanUtils.toBooleanObject(propertyMap.get(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FOR_REVERT_PROPERTY_ID).toString());
                upgradeCheckRequest.setRevert(forRevert);
            }
            org.apache.ambari.server.checks.UpgradeCheckRegistry upgradeCheckRegistry = org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.upgradeCheckRegistryProvider.get();
            final java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> upgradeChecksToRun;
            try {
                upgradeChecksToRun = upgradeCheckRegistry.getFilteredUpgradeChecks(upgradePack);
            } catch (org.apache.ambari.server.AmbariException ambariException) {
                throw new org.apache.ambari.server.controller.spi.SystemException("Unable to load upgrade checks", ambariException);
            }
            java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheckResult> results = org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.checkHelper.performChecks(upgradeCheckRequest, upgradeChecksToRun, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.config.get());
            for (org.apache.ambari.spi.upgrade.UpgradeCheckResult prerequisiteCheck : results) {
                final org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.PreUpgradeCheck);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_ID_PROPERTY_ID, prerequisiteCheck.getId(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CHECK_PROPERTY_ID, prerequisiteCheck.getDescription(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_STATUS_PROPERTY_ID, prerequisiteCheck.getStatus(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_REASON_PROPERTY_ID, prerequisiteCheck.getFailReason(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FAILED_ON_PROPERTY_ID, prerequisiteCheck.getFailedOn(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FAILED_DETAIL_PROPERTY_ID, prerequisiteCheck.getFailedDetail(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CHECK_TYPE_PROPERTY_ID, prerequisiteCheck.getType(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID, cluster.getClusterName(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID, upgradeType, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_TARGET_REPOSITORY_VERSION_ID_ID, repositoryVersion.getId(), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_TARGET_REPOSITORY_VERSION, repositoryVersion.getVersion(), requestedIds);
                resources.add(resource);
            }
        }
        return resources;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.pkPropertyIds;
    }
}