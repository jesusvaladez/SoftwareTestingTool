package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class AlertHistoryResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider implements org.apache.ambari.server.controller.spi.ExtendedResourceProvider {
    public static final java.lang.String ALERT_HISTORY_DEFINITION_ID = "AlertHistory/definition_id";

    public static final java.lang.String ALERT_HISTORY_DEFINITION_NAME = "AlertHistory/definition_name";

    public static final java.lang.String ALERT_HISTORY_ID = "AlertHistory/id";

    public static final java.lang.String ALERT_HISTORY_CLUSTER_NAME = "AlertHistory/cluster_name";

    public static final java.lang.String ALERT_HISTORY_SERVICE_NAME = "AlertHistory/service_name";

    public static final java.lang.String ALERT_HISTORY_COMPONENT_NAME = "AlertHistory/component_name";

    public static final java.lang.String ALERT_HISTORY_HOSTNAME = "AlertHistory/host_name";

    public static final java.lang.String ALERT_HISTORY_LABEL = "AlertHistory/label";

    public static final java.lang.String ALERT_HISTORY_STATE = "AlertHistory/state";

    public static final java.lang.String ALERT_HISTORY_TEXT = "AlertHistory/text";

    public static final java.lang.String ALERT_HISTORY_TIMESTAMP = "AlertHistory/timestamp";

    public static final java.lang.String ALERT_HISTORY_INSTANCE = "AlertHistory/instance";

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_ID));

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertsDAO s_dao = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO = null;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(ALERT_HISTORY_DEFINITION_ID);
        PROPERTY_IDS.add(ALERT_HISTORY_DEFINITION_NAME);
        PROPERTY_IDS.add(ALERT_HISTORY_ID);
        PROPERTY_IDS.add(ALERT_HISTORY_CLUSTER_NAME);
        PROPERTY_IDS.add(ALERT_HISTORY_SERVICE_NAME);
        PROPERTY_IDS.add(ALERT_HISTORY_COMPONENT_NAME);
        PROPERTY_IDS.add(ALERT_HISTORY_HOSTNAME);
        PROPERTY_IDS.add(ALERT_HISTORY_LABEL);
        PROPERTY_IDS.add(ALERT_HISTORY_STATE);
        PROPERTY_IDS.add(ALERT_HISTORY_TEXT);
        PROPERTY_IDS.add(ALERT_HISTORY_TIMESTAMP);
        PROPERTY_IDS.add(ALERT_HISTORY_INSTANCE);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory, ALERT_HISTORY_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, ALERT_HISTORY_CLUSTER_NAME);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, ALERT_HISTORY_SERVICE_NAME);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, ALERT_HISTORY_HOSTNAME);
    }

    AlertHistoryResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.PK_PROPERTY_IDS;
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
                java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_CLUSTER_NAME)));
                java.lang.Long clusterId = (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) ? null : getClusterId(clusterName);
                java.lang.String definitionName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_DEFINITION_NAME)));
                java.lang.String definitionId = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_DEFINITION_ID)));
                if (clusterId == null) {
                    org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization("", -1L);
                } else if (!org.apache.commons.lang.StringUtils.isEmpty(definitionName)) {
                    org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition = org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.alertDefinitionDAO.findByName(clusterId, definitionName);
                    org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(alertDefinition);
                } else if (org.apache.commons.lang.StringUtils.isNumeric(definitionId)) {
                    org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition = org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.alertDefinitionDAO.findById(java.lang.Long.parseLong(definitionId));
                    org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(alertDefinition);
                } else {
                    org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization("", getClusterResourceId(clusterName));
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
        }
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        org.apache.ambari.server.controller.AlertHistoryRequest historyRequest = new org.apache.ambari.server.controller.AlertHistoryRequest();
        historyRequest.Predicate = predicate;
        historyRequest.Pagination = request.getPageRequest();
        historyRequest.Sort = request.getSortRequest();
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> entities = org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.s_dao.findAll(historyRequest);
        for (org.apache.ambari.server.orm.entities.AlertHistoryEntity entity : entities) {
            results.add(toResource(entity, requestPropertyIds));
        }
        return results;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.QueryResponse queryForResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return new org.apache.ambari.server.controller.internal.QueryResponseImpl(getResources(request, predicate), request.getSortRequest() != null, request.getPageRequest() != null, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.s_dao.getCount(predicate));
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.AlertHistoryEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = entity.getAlertDefinition();
        org.apache.ambari.server.orm.entities.ClusterEntity cluster = definition.getCluster();
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory);
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_ID, entity.getAlertId());
        if (null != cluster) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_CLUSTER_NAME, cluster.getClusterName(), requestedIds);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_DEFINITION_ID, definition.getDefinitionId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_DEFINITION_NAME, definition.getDefinitionName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME, entity.getServiceName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_COMPONENT_NAME, entity.getComponentName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_HOSTNAME, entity.getHostName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_LABEL, entity.getAlertLabel(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_STATE, entity.getAlertState(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_TEXT, entity.getAlertText(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_TIMESTAMP, entity.getAlertTimestamp(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_INSTANCE, entity.getAlertInstance(), requestedIds);
        return resource;
    }
}