package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class AlertNoticeResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider implements org.apache.ambari.server.controller.spi.ExtendedResourceProvider {
    public static final java.lang.String ALERT_NOTICE_ID = "AlertNotice/id";

    public static final java.lang.String ALERT_NOTICE_STATE = "AlertNotice/notification_state";

    public static final java.lang.String ALERT_NOTICE_UUID = "AlertNotice/uuid";

    public static final java.lang.String ALERT_NOTICE_SERVICE_NAME = "AlertNotice/service_name";

    public static final java.lang.String ALERT_NOTICE_TARGET_ID = "AlertNotice/target_id";

    public static final java.lang.String ALERT_NOTICE_TARGET_NAME = "AlertNotice/target_name";

    public static final java.lang.String ALERT_NOTICE_HISTORY_ID = "AlertNotice/history_id";

    public static final java.lang.String ALERT_NOTICE_CLUSTER_NAME = "AlertNotice/cluster_name";

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID));

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertDispatchDAO s_dao = null;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(ALERT_NOTICE_ID);
        PROPERTY_IDS.add(ALERT_NOTICE_STATE);
        PROPERTY_IDS.add(ALERT_NOTICE_UUID);
        PROPERTY_IDS.add(ALERT_NOTICE_SERVICE_NAME);
        PROPERTY_IDS.add(ALERT_NOTICE_TARGET_ID);
        PROPERTY_IDS.add(ALERT_NOTICE_TARGET_NAME);
        PROPERTY_IDS.add(ALERT_NOTICE_HISTORY_ID);
        PROPERTY_IDS.add(ALERT_NOTICE_CLUSTER_NAME);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertNotice, ALERT_NOTICE_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, ALERT_NOTICE_CLUSTER_NAME);
    }

    AlertNoticeResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertNotice, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.KEY_PROPERTY_IDS, managementController);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.PK_PROPERTY_IDS;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            try {
                java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME)));
                java.lang.Long clusterResourceId = (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) ? null : getClusterResourceId(clusterName);
                java.lang.String serviceName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_SERVICE_NAME)));
                if (clusterResourceId == null) {
                    clusterResourceId = -1L;
                }
                org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(serviceName, clusterResourceId);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
        }
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        org.apache.ambari.server.controller.AlertNoticeRequest noticeRequest = new org.apache.ambari.server.controller.AlertNoticeRequest();
        noticeRequest.Predicate = predicate;
        noticeRequest.Pagination = request.getPageRequest();
        noticeRequest.Sort = request.getSortRequest();
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> entities = org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.s_dao.findAllNotices(noticeRequest);
        for (org.apache.ambari.server.orm.entities.AlertNoticeEntity entity : entities) {
            results.add(toResource(entity, requestPropertyIds));
        }
        return results;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.QueryResponse queryForResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return new org.apache.ambari.server.controller.internal.QueryResponseImpl(getResources(request, predicate), request.getSortRequest() != null, request.getPageRequest() != null, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.s_dao.getNoticesCount(predicate));
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.AlertNoticeEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = entity.getAlertHistory();
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = entity.getAlertTarget();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = history.getAlertDefinition();
        org.apache.ambari.server.orm.entities.ClusterEntity cluster = definition.getCluster();
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.AlertNotice);
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID, entity.getNotificationId());
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_STATE, entity.getNotifyState(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_UUID, entity.getUuid(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_SERVICE_NAME, definition.getServiceName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_ID, target.getTargetId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME, target.getTargetName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_HISTORY_ID, history.getAlertId(), requestedIds);
        if (null != cluster) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME, cluster.getClusterName(), requestedIds);
        }
        return resource;
    }
}