package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
@org.apache.ambari.server.StaticallyInject
public class AlertDefinitionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.class);

    protected static final java.lang.String ALERT_DEF = "AlertDefinition";

    protected static final java.lang.String ALERT_DEF_CLUSTER_NAME = "AlertDefinition/cluster_name";

    protected static final java.lang.String ALERT_DEF_ID = "AlertDefinition/id";

    protected static final java.lang.String ALERT_DEF_NAME = "AlertDefinition/name";

    protected static final java.lang.String ALERT_DEF_LABEL = "AlertDefinition/label";

    protected static final java.lang.String ALERT_DEF_HELP_URL = "AlertDefinition/help_url";

    protected static final java.lang.String ALERT_DEF_DESCRIPTION = "AlertDefinition/description";

    protected static final java.lang.String ALERT_DEF_INTERVAL = "AlertDefinition/interval";

    protected static final java.lang.String ALERT_DEF_SERVICE_NAME = "AlertDefinition/service_name";

    protected static final java.lang.String ALERT_DEF_COMPONENT_NAME = "AlertDefinition/component_name";

    protected static final java.lang.String ALERT_DEF_ENABLED = "AlertDefinition/enabled";

    protected static final java.lang.String ALERT_DEF_SCOPE = "AlertDefinition/scope";

    protected static final java.lang.String ALERT_DEF_IGNORE_HOST = "AlertDefinition/ignore_host";

    protected static final java.lang.String ALERT_DEF_REPEAT_TOLERANCE = "AlertDefinition/repeat_tolerance";

    protected static final java.lang.String ALERT_DEF_REPEAT_TOLERANCE_ENABLED = "AlertDefinition/repeat_tolerance_enabled";

    protected static final java.lang.String ALERT_DEF_SOURCE = "AlertDefinition/source";

    protected static final java.lang.String ALERT_DEF_SOURCE_TYPE = "AlertDefinition/source/type";

    private static final java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME));

    private static com.google.gson.Gson gson = new com.google.gson.Gson();

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.alert.AlertDefinitionHash alertDefinitionHash;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.alert.AlertDefinitionFactory definitionFactory;

    @com.google.inject.Inject
    private static org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(ALERT_DEF_CLUSTER_NAME);
        PROPERTY_IDS.add(ALERT_DEF_SERVICE_NAME);
        PROPERTY_IDS.add(ALERT_DEF_COMPONENT_NAME);
        PROPERTY_IDS.add(ALERT_DEF_ID);
        PROPERTY_IDS.add(ALERT_DEF_NAME);
        PROPERTY_IDS.add(ALERT_DEF_LABEL);
        PROPERTY_IDS.add(ALERT_DEF_DESCRIPTION);
        PROPERTY_IDS.add(ALERT_DEF_HELP_URL);
        PROPERTY_IDS.add(ALERT_DEF_INTERVAL);
        PROPERTY_IDS.add(ALERT_DEF_ENABLED);
        PROPERTY_IDS.add(ALERT_DEF_SCOPE);
        PROPERTY_IDS.add(ALERT_DEF_IGNORE_HOST);
        PROPERTY_IDS.add(ALERT_DEF_REPEAT_TOLERANCE);
        PROPERTY_IDS.add(ALERT_DEF_REPEAT_TOLERANCE_ENABLED);
        PROPERTY_IDS.add(ALERT_DEF_SOURCE);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition, ALERT_DEF_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, ALERT_DEF_CLUSTER_NAME);
    }

    AlertDefinitionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                createAlertDefinitions(request.getProperties());
                return null;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition, request);
        return getRequestStatus(null);
    }

    private void createAlertDefinitions(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entities = new java.util.ArrayList<>();
        java.lang.String clusterName = null;
        for (java.util.Map<java.lang.String, java.lang.Object> requestMap : requestMaps) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            populateEntity(entity, requestMap);
            entities.add(entity);
            if (null == clusterName) {
                clusterName = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME)));
            }
        }
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity : entities) {
            org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.create(entity);
            long clusterId = entity.getClusterId();
            final java.util.Set<java.lang.String> invalidatedHosts = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionHash.invalidateHosts(entity);
            org.apache.ambari.server.events.AlertHashInvalidationEvent event = new org.apache.ambari.server.events.AlertHashInvalidationEvent(clusterId, invalidatedHosts);
            org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.eventPublisher.publish(event);
        }
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME)));
            if ((null == clusterName) || clusterName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Invalid argument, cluster name is required");
            }
            java.lang.String id = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID)));
            if (null != id) {
                org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.findById(java.lang.Long.parseLong(id));
                if (null != entity) {
                    org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(entity);
                    results.add(toResource(clusterName, entity, requestPropertyIds));
                }
            } else {
                org.apache.ambari.server.state.Cluster cluster = null;
                try {
                    cluster = getManagementController().getClusters().getCluster(clusterName);
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("Parent Cluster resource doesn't exist", e);
                }
                java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entities = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.findAll(cluster.getClusterId());
                boolean serviceLevelAuthorization = org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.hasViewAuthorization("_SERVICE_NAME_", cluster.getResourceId());
                boolean clusterLevelAuthorization = org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.hasViewAuthorization("", cluster.getResourceId());
                for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity : entities) {
                    java.lang.String serviceName = entity.getServiceName();
                    if (org.apache.commons.lang.StringUtils.isEmpty(serviceName) || "AMBARI".equals(serviceName) ? clusterLevelAuthorization : serviceLevelAuthorization) {
                        results.add(toResource(clusterName, entity, requestPropertyIds));
                    }
                }
            }
        }
        return results;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Map<java.lang.String, java.lang.String> requestInfoProps = request.getRequestInfoProperties();
        if ((null != requestInfoProps) && requestInfoProps.containsKey(org.apache.ambari.server.api.resources.AlertDefResourceDefinition.EXECUTE_IMMEDIATE_DIRECTIVE)) {
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> predicateMaps = getPropertyMaps(predicate);
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : predicateMaps) {
                scheduleImmediateAlert(propertyMap);
            }
            return getRequestStatus(null);
        }
        for (java.util.Map<java.lang.String, java.lang.Object> requestPropMap : request.getProperties()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(requestPropMap, predicate)) {
                java.lang.String stringId = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID)));
                long id = java.lang.Long.parseLong(stringId);
                org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.findById(id);
                if (null == entity) {
                    continue;
                }
                boolean oldEnabled = entity.getEnabled();
                try {
                    populateEntity(entity, propertyMap);
                    org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.merge(entity);
                    java.util.Set<java.lang.String> invalidatedHosts = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionHash.invalidateHosts(entity);
                    org.apache.ambari.server.events.AlertHashInvalidationEvent event = new org.apache.ambari.server.events.AlertHashInvalidationEvent(entity.getClusterId(), invalidatedHosts);
                    org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.eventPublisher.publish(event);
                } catch (org.apache.ambari.server.AmbariException ae) {
                    org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.LOG.error("Unable to find cluster when updating alert definition", ae);
                }
                if (oldEnabled && (!entity.getEnabled())) {
                    org.apache.ambari.server.events.AlertDefinitionDisabledEvent event = new org.apache.ambari.server.events.AlertDefinitionDisabledEvent(entity.getClusterId(), entity.getDefinitionId(), entity.getDefinitionName());
                    org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.eventPublisher.publish(event);
                }
            }
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        java.util.Set<java.lang.Long> definitionIds = new java.util.HashSet<>();
        for (final org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID)));
            definitionIds.add(id);
        }
        for (java.lang.Long definitionId : definitionIds) {
            org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.LOG.info("Deleting alert definition {}", definitionId);
            final org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.findById(definitionId.longValue());
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyManageAuthorization(entity);
            modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    long clusterId = entity.getClusterId();
                    org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.remove(entity);
                    final java.util.Set<java.lang.String> invalidatedHosts = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionHash.invalidateHosts(entity);
                    org.apache.ambari.server.events.AlertHashInvalidationEvent event = new org.apache.ambari.server.events.AlertHashInvalidationEvent(clusterId, invalidatedHosts);
                    org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.eventPublisher.publish(event);
                    return null;
                }
            });
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition, predicate);
        return getRequestStatus(null);
    }

    private void populateEntity(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity, java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        boolean bCreate = true;
        if (null != entity.getDefinitionId()) {
            bCreate = false;
        }
        java.lang.String clusterName = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME)));
        java.lang.String definitionName = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME)));
        java.lang.String serviceName = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SERVICE_NAME)));
        java.lang.String componentName = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_COMPONENT_NAME)));
        java.lang.String type = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE)));
        java.lang.String label = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL)));
        java.lang.String helpURL = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_HELP_URL)));
        java.lang.String description = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION)));
        java.lang.String desiredScope = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SCOPE)));
        java.lang.Integer interval = null;
        if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL)) {
            interval = java.lang.Integer.valueOf(((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL))));
        }
        java.lang.Boolean enabled = null;
        if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ENABLED)) {
            enabled = java.lang.Boolean.parseBoolean(((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ENABLED))));
        } else if (bCreate) {
            enabled = java.lang.Boolean.TRUE;
        }
        java.lang.Boolean ignoreHost = null;
        if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_IGNORE_HOST)) {
            ignoreHost = java.lang.Boolean.parseBoolean(((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_IGNORE_HOST))));
        } else if (bCreate) {
            ignoreHost = java.lang.Boolean.FALSE;
        }
        org.apache.ambari.server.state.alert.Scope scope = null;
        if ((null != desiredScope) && (desiredScope.length() > 0)) {
            scope = org.apache.ambari.server.state.alert.Scope.valueOf(desiredScope);
        }
        org.apache.ambari.server.state.alert.SourceType sourceType = null;
        if ((null != type) && (type.length() > 0)) {
            sourceType = org.apache.ambari.server.state.alert.SourceType.valueOf(type);
        }
        if ((null == scope) && bCreate) {
            scope = org.apache.ambari.server.state.alert.Scope.ANY;
        }
        java.lang.Integer repeatTolerance = null;
        if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_REPEAT_TOLERANCE)) {
            repeatTolerance = java.lang.Integer.valueOf(((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_REPEAT_TOLERANCE))));
        }
        java.lang.Boolean repeatToleranceEnabled = null;
        if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_REPEAT_TOLERANCE_ENABLED)) {
            repeatToleranceEnabled = java.lang.Boolean.valueOf(requestMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_REPEAT_TOLERANCE_ENABLED).toString());
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
            throw new java.lang.IllegalArgumentException("Invalid argument, cluster name is required");
        }
        if (bCreate && (!requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL))) {
            throw new java.lang.IllegalArgumentException("Check interval must be specified");
        }
        if (bCreate && org.apache.commons.lang.StringUtils.isEmpty(definitionName)) {
            throw new java.lang.IllegalArgumentException("Definition name must be specified");
        }
        if (bCreate && org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
            throw new java.lang.IllegalArgumentException("Service name must be specified");
        }
        if (bCreate && (null == sourceType)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Source type must be specified and one of %s", java.util.EnumSet.allOf(org.apache.ambari.server.state.alert.SourceType.class)));
        }
        java.util.Map<java.lang.String, com.google.gson.JsonObject> jsonObjectMapping = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : requestMap.entrySet()) {
            java.lang.String propertyKey = entry.getKey();
            if (!propertyKey.startsWith(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE)) {
                continue;
            }
            com.google.gson.JsonObject jsonObject = getJsonObjectMapping(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE, jsonObjectMapping, propertyKey);
            java.lang.String propertyName = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(propertyKey);
            java.lang.Object entryValue = entry.getValue();
            if (entryValue instanceof java.util.Collection<?>) {
                com.google.gson.JsonElement jsonElement = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.gson.toJsonTree(entryValue);
                jsonObject.add(propertyName, jsonElement);
            } else if (entryValue instanceof java.lang.Number) {
                jsonObject.addProperty(propertyName, ((java.lang.Number) (entryValue)));
            } else {
                java.lang.String value = entryValue.toString();
                if (org.apache.commons.lang.StringUtils.isNotEmpty(value) && org.apache.commons.lang.math.NumberUtils.isNumber(value)) {
                    java.lang.Number number = org.apache.commons.lang.math.NumberUtils.createNumber(value);
                    jsonObject.addProperty(propertyName, number);
                } else {
                    jsonObject.addProperty(propertyName, value);
                }
            }
        }
        com.google.gson.JsonObject source = jsonObjectMapping.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE);
        if (bCreate && ((null == source) || (0 == source.entrySet().size()))) {
            throw new java.lang.IllegalArgumentException("Source must be specified");
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        java.lang.Long clusterId = cluster.getClusterId();
        boolean managed = false;
        boolean toggled = false;
        if (!clusterId.equals(entity.getClusterId())) {
            entity.setClusterId(clusterId);
            managed = true;
        }
        if ((null != componentName) && (!componentName.equals(entity.getComponentName()))) {
            entity.setComponentName(componentName);
            managed = true;
        }
        if ((null != definitionName) && (!definitionName.equals(entity.getDefinitionName()))) {
            entity.setDefinitionName(definitionName);
            managed = true;
        }
        if ((null != label) && (!label.equals(entity.getLabel()))) {
            entity.setLabel(label);
            managed = true;
        }
        if ((null != helpURL) && (!helpURL.equals(entity.getHelpURL()))) {
            entity.setHelpURL(helpURL);
            managed = true;
        }
        if ((null != description) && (!description.equals(entity.getDescription()))) {
            entity.setDescription(description);
            managed = true;
        }
        if ((null != enabled) && (!enabled.equals(entity.getEnabled()))) {
            entity.setEnabled(enabled);
            toggled = true;
        }
        if ((null != ignoreHost) && (!ignoreHost.equals(entity.isHostIgnored()))) {
            entity.setHostIgnored(ignoreHost);
            managed = true;
        }
        if ((null != interval) && (!interval.equals(entity.getScheduleInterval()))) {
            entity.setScheduleInterval(interval);
            managed = true;
        }
        if ((null != serviceName) && (!serviceName.equals(entity.getServiceName()))) {
            entity.setServiceName(serviceName);
            managed = true;
        }
        if ((null != sourceType) && (!sourceType.equals(entity.getSourceType()))) {
            entity.setSourceType(sourceType);
            managed = true;
        }
        if (null != source) {
            entity.setSource(source.toString());
            managed = true;
        }
        if ((null != scope) && (!scope.equals(entity.getScope()))) {
            entity.setScope(scope);
            managed = true;
        }
        if (entity.getSourceType() != org.apache.ambari.server.state.alert.SourceType.AGGREGATE) {
            if (null != repeatTolerance) {
                entity.setRepeatTolerance(repeatTolerance);
                managed = true;
            }
            if (null != repeatToleranceEnabled) {
                entity.setRepeatToleranceEnabled(repeatToleranceEnabled);
                managed = true;
            }
        }
        if (managed) {
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyManageAuthorization(entity);
        } else if (toggled) {
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyToggleAuthorization(entity);
        }
        entity.setHash(java.util.UUID.randomUUID().toString());
    }

    private com.google.gson.JsonObject getJsonObjectMapping(java.lang.String root, java.util.Map<java.lang.String, com.google.gson.JsonObject> jsonObjectMapping, java.lang.String propertyKey) {
        if (jsonObjectMapping.containsKey(propertyKey)) {
            return jsonObjectMapping.get(propertyKey);
        }
        if (root.equals(propertyKey)) {
            com.google.gson.JsonObject jsonRoot = jsonObjectMapping.get(root);
            if (null == jsonRoot) {
                jsonRoot = new com.google.gson.JsonObject();
                jsonObjectMapping.put(root, jsonRoot);
            }
            return jsonRoot;
        }
        java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyKey);
        com.google.gson.JsonObject categoryJson = jsonObjectMapping.get(propertyCategory);
        if (null == categoryJson) {
            com.google.gson.JsonObject parent = getJsonObjectMapping(root, jsonObjectMapping, propertyCategory);
            categoryJson = new com.google.gson.JsonObject();
            jsonObjectMapping.put(propertyCategory, categoryJson);
            java.lang.String categoryName = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(propertyCategory);
            parent.add(categoryName, categoryJson);
        }
        return categoryJson;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(java.lang.String clusterName, org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition);
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID, entity.getDefinitionId());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_CLUSTER_NAME, clusterName);
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_NAME, entity.getDefinitionName());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_LABEL, entity.getLabel());
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_DESCRIPTION, entity.getDescription(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_INTERVAL, entity.getScheduleInterval(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SERVICE_NAME, entity.getServiceName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_COMPONENT_NAME, entity.getComponentName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ENABLED, java.lang.Boolean.valueOf(entity.getEnabled()), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_IGNORE_HOST, java.lang.Boolean.valueOf(entity.isHostIgnored()), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SCOPE, entity.getScope(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_HELP_URL, entity.getHelpURL(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_REPEAT_TOLERANCE, entity.getRepeatTolerance(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_REPEAT_TOLERANCE_ENABLED, java.lang.Boolean.valueOf(entity.isRepeatToleranceEnabled()), requestedIds);
        boolean sourceTypeRequested = org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE, entity.getSourceType(), requestedIds);
        if (sourceTypeRequested && (null != resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE_TYPE))) {
            try {
                java.util.Map<java.lang.String, java.lang.String> map = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(entity.getSource(), java.util.Map.class);
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : map.entrySet()) {
                    java.lang.String subProp = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_SOURCE, entry.getKey());
                    resource.setProperty(subProp, entry.getValue());
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.LOG.error("Could not coerce alert JSON into a type");
            }
        }
        return resource;
    }

    private void scheduleImmediateAlert(java.util.Map<java.lang.String, java.lang.Object> propertyMap) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        java.lang.String stringId = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.ALERT_DEF_ID)));
        long id = java.lang.Long.parseLong(stringId);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionDAO.findById(id);
        if (null == entity) {
            org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.LOG.error("Unable to lookup alert definition with ID {}", id);
            return;
        }
        org.apache.ambari.server.state.Cluster cluster = null;
        try {
            cluster = clusters.getClusterById(entity.getClusterId());
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.LOG.error("Unable to lookup cluster with ID {}", entity.getClusterId(), ambariException);
            return;
        }
        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyExecuteAuthorization(entity);
        java.util.Set<java.lang.String> hostNames = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.alertDefinitionHash.getAssociatedHosts(cluster, entity.getSourceType(), entity.getDefinitionName(), entity.getServiceName(), entity.getComponentName());
        for (java.lang.String hostName : hostNames) {
            org.apache.ambari.server.state.alert.AlertDefinition definition = org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider.definitionFactory.coerce(entity);
            org.apache.ambari.server.agent.AlertExecutionCommand command = new org.apache.ambari.server.agent.AlertExecutionCommand(cluster.getClusterName(), hostName, definition);
        }
    }
}