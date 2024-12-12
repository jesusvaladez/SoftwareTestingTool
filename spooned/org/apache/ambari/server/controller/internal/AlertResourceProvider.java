package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
@org.apache.ambari.server.StaticallyInject
public class AlertResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider implements org.apache.ambari.server.controller.spi.ExtendedResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AlertResourceProvider.class);

    public static final java.lang.String ALERT_ID = "Alert/id";

    public static final java.lang.String ALERT_STATE = "Alert/state";

    public static final java.lang.String ALERT_ORIGINAL_TIMESTAMP = "Alert/original_timestamp";

    public static final java.lang.String ALERT_LATEST_TIMESTAMP = "Alert/latest_timestamp";

    public static final java.lang.String ALERT_MAINTENANCE_STATE = "Alert/maintenance_state";

    public static final java.lang.String ALERT_DEFINITION_ID = "Alert/definition_id";

    public static final java.lang.String ALERT_DEFINITION_NAME = "Alert/definition_name";

    public static final java.lang.String ALERT_TEXT = "Alert/text";

    public static final java.lang.String ALERT_CLUSTER_NAME = "Alert/cluster_name";

    public static final java.lang.String ALERT_COMPONENT = "Alert/component_name";

    public static final java.lang.String ALERT_HOST = "Alert/host_name";

    public static final java.lang.String ALERT_SERVICE = "Alert/service_name";

    protected static final java.lang.String ALERT_INSTANCE = "Alert/instance";

    protected static final java.lang.String ALERT_LABEL = "Alert/label";

    protected static final java.lang.String ALERT_SCOPE = "Alert/scope";

    protected static final java.lang.String ALERT_REPEAT_TOLERANCE = "Alert/repeat_tolerance";

    protected static final java.lang.String ALERT_OCCURRENCES = "Alert/occurrences";

    protected static final java.lang.String ALERT_REPEAT_TOLERANCE_REMAINING = "Alert/repeat_tolerance_remaining";

    protected static final java.lang.String ALERT_FIRMNESS = "Alert/firmness";

    private static final java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME));

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO = null;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(ALERT_ID);
        PROPERTY_IDS.add(ALERT_STATE);
        PROPERTY_IDS.add(ALERT_ORIGINAL_TIMESTAMP);
        PROPERTY_IDS.add(ALERT_DEFINITION_ID);
        PROPERTY_IDS.add(ALERT_DEFINITION_NAME);
        PROPERTY_IDS.add(ALERT_CLUSTER_NAME);
        PROPERTY_IDS.add(ALERT_LATEST_TIMESTAMP);
        PROPERTY_IDS.add(ALERT_MAINTENANCE_STATE);
        PROPERTY_IDS.add(ALERT_INSTANCE);
        PROPERTY_IDS.add(ALERT_LABEL);
        PROPERTY_IDS.add(ALERT_TEXT);
        PROPERTY_IDS.add(ALERT_COMPONENT);
        PROPERTY_IDS.add(ALERT_HOST);
        PROPERTY_IDS.add(ALERT_SERVICE);
        PROPERTY_IDS.add(ALERT_SCOPE);
        PROPERTY_IDS.add(ALERT_REPEAT_TOLERANCE);
        PROPERTY_IDS.add(ALERT_OCCURRENCES);
        PROPERTY_IDS.add(ALERT_REPEAT_TOLERANCE_REMAINING);
        PROPERTY_IDS.add(ALERT_FIRMNESS);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Alert, ALERT_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, ALERT_CLUSTER_NAME);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, ALERT_SERVICE);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, ALERT_HOST);
    }

    AlertResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Alert, org.apache.ambari.server.controller.internal.AlertResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.AlertResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.AlertResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.QueryResponse queryForResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return new org.apache.ambari.server.controller.internal.QueryResponseImpl(getResources(request, predicate), request.getSortRequest() != null, request.getPageRequest() != null, org.apache.ambari.server.controller.internal.AlertResourceProvider.alertsDAO.getCount(predicate));
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME)));
            if ((null == clusterName) || clusterName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Invalid argument, cluster name is required");
            }
            java.lang.String id = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID)));
            if (null != id) {
                org.apache.ambari.server.orm.entities.AlertCurrentEntity entity = org.apache.ambari.server.controller.internal.AlertResourceProvider.alertsDAO.findCurrentById(java.lang.Long.parseLong(id));
                if (null != entity) {
                    org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(entity);
                    results.add(toResource(false, clusterName, entity, requestPropertyIds));
                }
            } else {
                try {
                    java.lang.Long clusterId = getClusterId(clusterName);
                    java.lang.String definitionName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME)));
                    java.lang.String definitionId = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_ID)));
                    if (clusterId == null) {
                        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization("", -1L);
                    } else if (!org.apache.commons.lang.StringUtils.isEmpty(definitionName)) {
                        org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition = org.apache.ambari.server.controller.internal.AlertResourceProvider.alertDefinitionDAO.findByName(clusterId, definitionName);
                        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(alertDefinition);
                    } else if (org.apache.commons.lang.StringUtils.isNumeric(definitionId)) {
                        org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition = org.apache.ambari.server.controller.internal.AlertResourceProvider.alertDefinitionDAO.findById(java.lang.Long.parseLong(definitionId));
                        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(alertDefinition);
                    } else {
                        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization("", getClusterResourceId(clusterName));
                    }
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
                }
                java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> entities = null;
                org.apache.ambari.server.controller.AlertCurrentRequest alertCurrentRequest = new org.apache.ambari.server.controller.AlertCurrentRequest();
                alertCurrentRequest.Predicate = predicate;
                alertCurrentRequest.Pagination = request.getPageRequest();
                alertCurrentRequest.Sort = request.getSortRequest();
                entities = org.apache.ambari.server.controller.internal.AlertResourceProvider.alertsDAO.findAll(alertCurrentRequest);
                if (null == entities) {
                    entities = java.util.Collections.emptyList();
                }
                for (org.apache.ambari.server.orm.entities.AlertCurrentEntity entity : entities) {
                    results.add(toResource(true, clusterName, entity, requestPropertyIds));
                }
            }
        }
        return results;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(boolean isCollection, java.lang.String clusterName, org.apache.ambari.server.orm.entities.AlertCurrentEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = entity.getAlertHistory();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = history.getAlertDefinition();
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Alert);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME, clusterName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, entity.getAlertId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LATEST_TIMESTAMP, entity.getLatestTimestamp(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_MAINTENANCE_STATE, entity.getMaintenanceState(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP, entity.getOriginalTimestamp(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_TEXT, entity.getLatestText(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_INSTANCE, history.getAlertInstance(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_LABEL, definition.getLabel(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE, history.getAlertState(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_COMPONENT, history.getComponentName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_HOST, history.getHostName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_SERVICE, history.getServiceName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_ID, definition.getDefinitionId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, definition.getDefinitionName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_SCOPE, definition.getScope(), requestedIds);
        int repeatTolerance = getRepeatTolerance(definition, clusterName);
        long occurrences = entity.getOccurrences();
        long remaining = (occurrences > repeatTolerance) ? 0 : repeatTolerance - occurrences;
        if (history.getAlertState() == org.apache.ambari.server.state.AlertState.OK) {
            remaining = 0;
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_REPEAT_TOLERANCE, repeatTolerance, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_OCCURRENCES, occurrences, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_REPEAT_TOLERANCE_REMAINING, remaining, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_FIRMNESS, entity.getFirmness(), requestedIds);
        if (isCollection) {
            resource.setProperty(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_ID, definition.getDefinitionId());
            resource.setProperty(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME, definition.getDefinitionName());
        }
        return resource;
    }

    private int getRepeatTolerance(org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition, java.lang.String clusterName) {
        if (definition.isRepeatToleranceEnabled()) {
            return definition.getRepeatTolerance();
        }
        int repeatTolerance = 1;
        try {
            org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.internal.AlertResourceProvider.clusters.get().getCluster(clusterName);
            java.lang.String value = cluster.getClusterProperty(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_ALERT_REPEAT_TOLERANCE, "1");
            repeatTolerance = org.apache.commons.lang.math.NumberUtils.toInt(value, 1);
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.controller.internal.AlertResourceProvider.LOG.warn("Unable to read {}/{} from cluster {}, defaulting to 1", org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_ALERT_REPEAT_TOLERANCE, clusterName, ambariException);
        }
        return repeatTolerance;
    }
}