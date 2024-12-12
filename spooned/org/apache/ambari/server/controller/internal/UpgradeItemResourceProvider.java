package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class UpgradeItemResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.class);

    public static final java.lang.String UPGRADE_CLUSTER_NAME = "UpgradeItem/cluster_name";

    public static final java.lang.String UPGRADE_REQUEST_ID = "UpgradeItem/request_id";

    public static final java.lang.String UPGRADE_GROUP_ID = "UpgradeItem/group_id";

    public static final java.lang.String UPGRADE_ITEM_STAGE_ID = "UpgradeItem/stage_id";

    public static final java.lang.String UPGRADE_ITEM_TEXT = "UpgradeItem/text";

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_REQUEST_ID, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_ITEM_STAGE_ID));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    private static java.util.Map<java.lang.String, java.lang.String> STAGE_MAPPED_IDS = new java.util.HashMap<>();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.UpgradeDAO s_dao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.StageDAO s_stageDao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostRoleCommandDAO s_hostRoleCommandDAO;

    static {
        PROPERTY_IDS.add(UPGRADE_ITEM_STAGE_ID);
        PROPERTY_IDS.add(UPGRADE_GROUP_ID);
        PROPERTY_IDS.add(UPGRADE_REQUEST_ID);
        PROPERTY_IDS.add(UPGRADE_ITEM_TEXT);
        for (java.lang.String p : org.apache.ambari.server.controller.internal.StageResourceProvider.PROPERTY_IDS) {
            STAGE_MAPPED_IDS.put(p, p.replace("Stage/", "UpgradeItem/"));
        }
        PROPERTY_IDS.addAll(STAGE_MAPPED_IDS.values());
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, UPGRADE_ITEM_STAGE_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup, UPGRADE_GROUP_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, UPGRADE_REQUEST_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, UPGRADE_CLUSTER_NAME);
    }

    UpgradeItemResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.Object> updateProperties = iterator.next();
            java.lang.String statusPropertyId = org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.STAGE_MAPPED_IDS.get(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS);
            java.lang.String stageStatus = ((java.lang.String) (updateProperties.get(statusPropertyId)));
            if (null == stageStatus) {
                throw new java.lang.IllegalArgumentException("Upgrade items can only have their status changed.");
            }
            org.apache.ambari.server.actionmanager.HostRoleStatus desiredStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(stageStatus);
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                final java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_CLUSTER_NAME)));
                final org.apache.ambari.server.state.Cluster cluster;
                try {
                    cluster = getManagementController().getClusters().getCluster(clusterName);
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Cluster %s could not be loaded", clusterName));
                }
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK))) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to " + "manage upgrade and downgrade");
                }
                java.lang.Long requestId = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_REQUEST_ID)));
                java.lang.Long stageId = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_ITEM_STAGE_ID)));
                org.apache.ambari.server.orm.entities.StageEntityPK primaryKey = new org.apache.ambari.server.orm.entities.StageEntityPK();
                primaryKey.setRequestId(requestId);
                primaryKey.setStageId(stageId);
                org.apache.ambari.server.orm.entities.StageEntity stageEntity = org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.s_stageDao.findByPK(primaryKey);
                if (null == stageEntity) {
                    org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.LOG.warn("Unable to change the status of request {} and stage {} to {} because it does not exist", requestId, stageId, desiredStatus);
                    return getRequestStatus(null);
                }
                org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.s_stageDao.updateStageStatus(stageEntity, desiredStatus, getManagementController().getActionManager());
            }
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String requestIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_REQUEST_ID)));
            java.lang.String groupIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_GROUP_ID)));
            java.lang.String stageIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_ITEM_STAGE_ID)));
            if ((null == requestIdStr) || requestIdStr.isEmpty()) {
                throw new java.lang.IllegalArgumentException("The upgrade id is required when querying for upgrades");
            }
            if ((null == groupIdStr) || groupIdStr.isEmpty()) {
                throw new java.lang.IllegalArgumentException("The upgrade group id is required when querying for upgrades");
            }
            java.lang.Long requestId = java.lang.Long.valueOf(requestIdStr);
            java.lang.Long groupId = java.lang.Long.valueOf(groupIdStr);
            java.lang.Long stageId = null;
            if (null != stageIdStr) {
                stageId = java.lang.Long.valueOf(stageIdStr);
            }
            java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> entities = new java.util.ArrayList<>();
            if (null == stageId) {
                org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.s_dao.findUpgradeGroup(groupId);
                if ((null == group) || (null == group.getItems())) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(java.lang.String.format("Cannot load upgrade for %s", requestIdStr));
                }
                entities = group.getItems();
            } else {
                org.apache.ambari.server.orm.entities.UpgradeItemEntity entity = org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.s_dao.findUpgradeItemByRequestAndStage(requestId, stageId);
                if (null != entity) {
                    entities.add(entity);
                }
            }
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> requestAggregateCounts = org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.s_hostRoleCommandDAO.findAggregateCounts(requestId);
            java.util.Map<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>> cache = new java.util.HashMap<>();
            cache.put(requestId, requestAggregateCounts);
            for (org.apache.ambari.server.orm.entities.UpgradeItemEntity entity : entities) {
                org.apache.ambari.server.controller.spi.Resource upgradeItemResource = toResource(entity, requestPropertyIds);
                org.apache.ambari.server.orm.entities.StageEntityPK stagePrimaryKey = new org.apache.ambari.server.orm.entities.StageEntityPK();
                stagePrimaryKey.setRequestId(requestId);
                stagePrimaryKey.setStageId(entity.getStageId());
                org.apache.ambari.server.orm.entities.StageEntity stageEntity = org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.s_stageDao.findByPK(stagePrimaryKey);
                org.apache.ambari.server.controller.spi.Resource stageResource = org.apache.ambari.server.controller.internal.StageResourceProvider.toResource(cache, stageEntity, org.apache.ambari.server.controller.internal.StageResourceProvider.PROPERTY_IDS);
                for (java.lang.String propertyId : org.apache.ambari.server.controller.internal.StageResourceProvider.PROPERTY_IDS) {
                    java.lang.Object value = stageResource.getPropertyValue(propertyId);
                    if ((org.apache.ambari.server.controller.internal.StageResourceProvider.PROPERTIES_TO_MASK_PASSWORD_IN.contains(propertyId) && value.getClass().equals(java.lang.String.class)) && (!org.apache.commons.lang.StringUtils.isBlank(((java.lang.String) (value))))) {
                        value = org.apache.ambari.server.utils.SecretReference.maskPasswordInPropertyMap(((java.lang.String) (value)));
                    }
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(upgradeItemResource, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.STAGE_MAPPED_IDS.get(propertyId), value, requestPropertyIds);
                }
                results.add(upgradeItemResource);
            }
        }
        return results;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.PK_PROPERTY_IDS;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.UpgradeItemEntity item, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem);
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = item.getGroupEntity();
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = group.getUpgradeEntity();
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_REQUEST_ID, upgrade.getRequestId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_GROUP_ID, group.getId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_ITEM_STAGE_ID, item.getStageId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_ITEM_TEXT, item.getText(), requestedIds);
        return resource;
    }
}