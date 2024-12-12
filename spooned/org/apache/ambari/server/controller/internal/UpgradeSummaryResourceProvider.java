package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class UpgradeSummaryResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    protected static final java.lang.String UPGRADE_SUMMARY_CLUSTER_NAME = "UpgradeSummary/cluster_name";

    protected static final java.lang.String UPGRADE_SUMMARY_REQUEST_ID = "UpgradeSummary/request_id";

    protected static final java.lang.String UPGRADE_SUMMARY_FAIL_REASON = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeSummary", "fail_reason");

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_REQUEST_ID, org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_CLUSTER_NAME));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static java.util.Map<java.lang.String, java.lang.String> TASK_MAPPED_IDS = new java.util.HashMap<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.UpgradeDAO s_upgradeDAO = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostRoleCommandDAO s_hostRoleCommandDAO = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper s_upgradeHelper;

    static {
        PROPERTY_IDS.add(UPGRADE_SUMMARY_CLUSTER_NAME);
        PROPERTY_IDS.add(UPGRADE_SUMMARY_REQUEST_ID);
        PROPERTY_IDS.add(UPGRADE_SUMMARY_FAIL_REASON);
        for (java.lang.String p : org.apache.ambari.server.controller.internal.TaskResourceProvider.PROPERTY_IDS) {
            TASK_MAPPED_IDS.put(p, p.replace("Tasks/", "UpgradeSummary/failed_task/"));
        }
        PROPERTY_IDS.addAll(TASK_MAPPED_IDS.values());
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeSummary, UPGRADE_SUMMARY_REQUEST_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, UPGRADE_SUMMARY_CLUSTER_NAME);
    }

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.class);

    public UpgradeSummaryResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeSummary, org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Resource only supports GET operation.");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_CLUSTER_NAME)));
            if ((null == clusterName) || clusterName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("The cluster name is required when querying for upgrades");
            }
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = getManagementController().getClusters().getCluster(clusterName);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(java.lang.String.format("Cluster %s could not be loaded", clusterName));
            }
            java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = new java.util.ArrayList<>();
            java.lang.String upgradeRequestIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_REQUEST_ID)));
            if (null != upgradeRequestIdStr) {
                org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.s_upgradeDAO.findUpgradeByRequestId(java.lang.Long.valueOf(upgradeRequestIdStr));
                if (null != upgrade) {
                    upgrades.add(upgrade);
                }
            } else {
                upgrades = org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.s_upgradeDAO.findUpgrades(cluster.getClusterId());
            }
            for (org.apache.ambari.server.orm.entities.UpgradeEntity entity : upgrades) {
                org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeSummary);
                java.lang.Long upgradeRequestId = entity.getRequestId();
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_CLUSTER_NAME, clusterName, requestPropertyIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_REQUEST_ID, entity.getRequestId(), requestPropertyIds);
                org.apache.ambari.server.orm.entities.HostRoleCommandEntity mostRecentFailure = org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.s_hostRoleCommandDAO.findMostRecentFailure(upgradeRequestId);
                java.lang.String displayText = null;
                org.apache.ambari.server.orm.entities.HostRoleCommandEntity failedTask = null;
                if (mostRecentFailure != null) {
                    org.apache.ambari.server.controller.internal.UpgradeSummary summary = new org.apache.ambari.server.controller.internal.UpgradeSummary(mostRecentFailure);
                    displayText = summary.getDisplayText();
                    failedTask = summary.getFailedTask();
                    org.apache.ambari.server.controller.spi.Resource taskResource = org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.s_upgradeHelper.getTaskResource(clusterName, failedTask.getRequestId(), failedTask.getStageId(), failedTask.getTaskId());
                    if (taskResource != null) {
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.TASK_MAPPED_IDS.entrySet()) {
                            java.lang.String taskPropertyId = property.getKey();
                            java.lang.String upgradeSummaryPropertyId = property.getValue();
                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, upgradeSummaryPropertyId, taskResource.getPropertyValue(taskPropertyId), requestPropertyIds);
                        }
                    }
                }
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_FAIL_REASON, displayText, requestPropertyIds);
                resources.add(resource);
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Resource only supports GET operation.");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Resource only supports GET operation.");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.PK_PROPERTY_IDS;
    }
}