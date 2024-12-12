package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class UpgradeGroupResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    protected static final java.lang.String UPGRADE_REQUEST_ID = "UpgradeGroup/request_id";

    protected static final java.lang.String UPGRADE_GROUP_ID = "UpgradeGroup/group_id";

    protected static final java.lang.String UPGRADE_CLUSTER_NAME = "UpgradeGroup/cluster_name";

    protected static final java.lang.String UPGRADE_GROUP_NAME = "UpgradeGroup/name";

    protected static final java.lang.String UPGRADE_GROUP_TITLE = "UpgradeGroup/title";

    protected static final java.lang.String UPGRADE_GROUP_PROGRESS_PERCENT = "UpgradeGroup/progress_percent";

    protected static final java.lang.String UPGRADE_GROUP_STATUS = "UpgradeGroup/status";

    protected static final java.lang.String UPGRADE_GROUP_DISPLAY_STATUS = "UpgradeGroup/display_status";

    protected static final java.lang.String UPGRADE_GROUP_TOTAL_TASKS = "UpgradeGroup/total_task_count";

    protected static final java.lang.String UPGRADE_GROUP_IN_PROGRESS_TASKS = "UpgradeGroup/in_progress_task_count";

    protected static final java.lang.String UPGRADE_GROUP_COMPLETED_TASKS = "UpgradeGroup/completed_task_count";

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_REQUEST_ID, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_ID));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.UpgradeDAO m_dao = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostRoleCommandDAO s_hostRoleCommandDao;

    static {
        PROPERTY_IDS.add(UPGRADE_REQUEST_ID);
        PROPERTY_IDS.add(UPGRADE_GROUP_ID);
        PROPERTY_IDS.add(UPGRADE_GROUP_NAME);
        PROPERTY_IDS.add(UPGRADE_GROUP_TITLE);
        PROPERTY_IDS.add(UPGRADE_GROUP_PROGRESS_PERCENT);
        PROPERTY_IDS.add(UPGRADE_GROUP_STATUS);
        PROPERTY_IDS.add(UPGRADE_GROUP_DISPLAY_STATUS);
        PROPERTY_IDS.add(UPGRADE_GROUP_TOTAL_TASKS);
        PROPERTY_IDS.add(UPGRADE_GROUP_IN_PROGRESS_TASKS);
        PROPERTY_IDS.add(UPGRADE_GROUP_COMPLETED_TASKS);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup, UPGRADE_GROUP_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, UPGRADE_REQUEST_ID);
    }

    UpgradeGroupResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Upgrade Groups can only be created with an upgrade");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String upgradeIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_REQUEST_ID)));
            if ((null == upgradeIdStr) || upgradeIdStr.isEmpty()) {
                throw new java.lang.IllegalArgumentException("The upgrade id is required when querying for upgrades");
            }
            java.lang.Long upgradeId = java.lang.Long.valueOf(upgradeIdStr);
            org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.m_dao.findUpgradeByRequestId(upgradeId);
            java.lang.Long requestId = upgrade.getRequestId();
            java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> groups = upgrade.getUpgradeGroups();
            if (null != groups) {
                java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> map = org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.s_hostRoleCommandDao.findAggregateCounts(requestId);
                for (org.apache.ambari.server.orm.entities.UpgradeGroupEntity group : groups) {
                    org.apache.ambari.server.controller.spi.Resource r = toResource(upgrade, group, requestPropertyIds);
                    java.util.Set<java.lang.Long> stageIds = new java.util.HashSet<>();
                    for (org.apache.ambari.server.orm.entities.UpgradeItemEntity itemEntity : group.getItems()) {
                        stageIds.add(itemEntity.getStageId());
                    }
                    aggregate(map, r, requestId, stageIds, requestPropertyIds);
                    results.add(r);
                }
            }
        }
        return results;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Upgrade groups cannot be modified");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Upgrade groups cannot be removed");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.PK_PROPERTY_IDS;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.UpgradeEntity upgrade, org.apache.ambari.server.orm.entities.UpgradeGroupEntity group, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_REQUEST_ID, upgrade.getRequestId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_ID, group.getId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_NAME, group.getName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_TITLE, group.getTitle(), requestedIds);
        return resource;
    }

    private void aggregate(java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> smap, org.apache.ambari.server.controller.spi.Resource upgradeGroup, java.lang.Long requestId, java.util.Set<java.lang.Long> stageIds, java.util.Set<java.lang.String> requestedIds) {
        int size = 0;
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateTaskStatusCounts(smap, stageIds);
        org.apache.ambari.server.controller.internal.CalculatedStatus stageStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(smap, stageIds);
        for (java.lang.Integer i : counts.values()) {
            size += i.intValue();
        }
        java.lang.Integer inProgress = 0;
        java.lang.Integer completed = 0;
        for (java.util.Map.Entry<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> entry : counts.entrySet()) {
            if (entry.getKey().isCompletedState()) {
                completed += entry.getValue();
            } else if (entry.getKey().isInProgress()) {
                inProgress += entry.getValue();
            }
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(upgradeGroup, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_TOTAL_TASKS, size, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(upgradeGroup, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_IN_PROGRESS_TASKS, inProgress, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(upgradeGroup, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_COMPLETED_TASKS, completed, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(upgradeGroup, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_STATUS, stageStatus.getStatus(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(upgradeGroup, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_DISPLAY_STATUS, stageStatus.getDisplayStatus(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(upgradeGroup, org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_GROUP_PROGRESS_PERCENT, stageStatus.getPercent(), requestedIds);
    }
}