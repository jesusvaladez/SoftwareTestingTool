package org.apache.ambari.server.controller.internal;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class AlertTargetResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.class);

    public static final java.lang.String ALERT_TARGET = "AlertTarget";

    public static final java.lang.String ID_PROPERTY_ID = "id";

    public static final java.lang.String NAME_PROPERTY_ID = "name";

    public static final java.lang.String DESCRIPTION_PROPERTY_ID = "description";

    public static final java.lang.String NOTIFICATION_TYPE_PROPERTY_ID = "notification_type";

    public static final java.lang.String PROPERTIES_PROPERTY_ID = "properties";

    public static final java.lang.String GROUPS_PROPERTY_ID = "groups";

    public static final java.lang.String STATES_PROPERTY_ID = "alert_states";

    public static final java.lang.String GLOBAL_PROPERTY_ID = "global";

    public static final java.lang.String ENABLED_PROPERTY_ID = "enabled";

    public static final java.lang.String ALERT_TARGET_ID = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ID_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_NAME = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.NAME_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_DESCRIPTION = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.DESCRIPTION_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_NOTIFICATION_TYPE = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.NOTIFICATION_TYPE_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_PROPERTIES = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.PROPERTIES_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_GROUPS = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.GROUPS_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_STATES = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.STATES_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_GLOBAL = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.GLOBAL_PROPERTY_ID;

    public static final java.lang.String ALERT_TARGET_ENABLED = (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ENABLED_PROPERTY_ID;

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(ALERT_TARGET_ID);
        PROPERTY_IDS.add(ALERT_TARGET_NAME);
        PROPERTY_IDS.add(ALERT_TARGET_DESCRIPTION);
        PROPERTY_IDS.add(ALERT_TARGET_NOTIFICATION_TYPE);
        PROPERTY_IDS.add(ALERT_TARGET_PROPERTIES);
        PROPERTY_IDS.add(ALERT_TARGET_GROUPS);
        PROPERTY_IDS.add(ALERT_TARGET_STATES);
        PROPERTY_IDS.add(ALERT_TARGET_GLOBAL);
        PROPERTY_IDS.add(ALERT_TARGET_ENABLED);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, ALERT_TARGET_ID);
    }

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertDispatchDAO s_dao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.notifications.DispatchFactory dispatchFactory;

    private static final com.google.gson.Gson s_gson = new com.google.gson.Gson();

    @com.google.inject.Inject
    AlertTargetResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.KEY_PROPERTY_IDS);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_ALERT_NOTIFICATIONS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                createAlertTargets(request.getProperties(), request.getRequestInfoProperties());
                return null;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        if (null == predicate) {
            java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> entities = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findAllTargets();
            for (org.apache.ambari.server.orm.entities.AlertTargetEntity entity : entities) {
                results.add(toResource(entity, requestPropertyIds));
            }
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                java.lang.String id = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID)));
                if (null == id) {
                    continue;
                }
                org.apache.ambari.server.orm.entities.AlertTargetEntity entity = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findTargetById(java.lang.Long.parseLong(id));
                if (null != entity) {
                    results.add(toResource(entity, requestPropertyIds));
                }
            }
        }
        return results;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps = request.getProperties();
                for (java.util.Map<java.lang.String, java.lang.Object> requestMap : requestMaps) {
                    java.lang.String stringId = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID)));
                    if (org.apache.commons.lang.StringUtils.isEmpty(stringId)) {
                        throw new java.lang.IllegalArgumentException("The ID of the alert target is required when updating an existing target");
                    }
                    long alertTargetId = java.lang.Long.parseLong(stringId);
                    updateAlertTargets(alertTargetId, requestMap);
                }
                return null;
            }
        });
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        java.util.Set<java.lang.Long> targetIds = new java.util.HashSet<>();
        for (final org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID)));
            targetIds.add(id);
        }
        for (java.lang.Long targetId : targetIds) {
            org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.LOG.info("Deleting alert target {}", targetId);
            final org.apache.ambari.server.orm.entities.AlertTargetEntity entity = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findTargetById(targetId.longValue());
            modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.remove(entity);
                    return null;
                }
            });
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.PK_PROPERTY_IDS;
    }

    @java.lang.SuppressWarnings("unchecked")
    private void createAlertTargets(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps, java.util.Map<java.lang.String, java.lang.String> requestInfoProps) throws org.apache.ambari.server.AmbariException {
        for (java.util.Map<java.lang.String, java.lang.Object> requestMap : requestMaps) {
            java.lang.String name = org.apache.commons.text.StringEscapeUtils.escapeHtml4(((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME))));
            java.lang.String description = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION)));
            java.lang.String notificationType = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE)));
            java.util.Collection<java.lang.String> alertStates = ((java.util.Collection<java.lang.String>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES)));
            java.lang.String globalProperty = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL)));
            java.lang.String enabledProperty = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ENABLED)));
            if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
                throw new java.lang.IllegalArgumentException("The name of the alert target is required.");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(notificationType)) {
                throw new java.lang.IllegalArgumentException("The type of the alert target is required.");
            }
            java.util.Map<java.lang.String, java.lang.Object> properties = extractProperties(requestMap);
            java.lang.String propertiesJson = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_gson.toJson(properties);
            if (org.apache.commons.lang.StringUtils.isEmpty(propertiesJson)) {
                throw new java.lang.IllegalArgumentException("Alert targets must be created with their connection properties");
            }
            java.lang.String validationDirective = requestInfoProps.get(org.apache.ambari.server.api.resources.AlertTargetResourceDefinition.VALIDATE_CONFIG_DIRECTIVE);
            if ((validationDirective != null) && validationDirective.equalsIgnoreCase("true")) {
                validateTargetConfig(notificationType, properties);
            }
            boolean overwriteExisting = false;
            if (requestInfoProps.containsKey(org.apache.ambari.server.api.resources.AlertTargetResourceDefinition.OVERWRITE_DIRECTIVE)) {
                overwriteExisting = true;
            }
            boolean isGlobal = false;
            if (null != globalProperty) {
                isGlobal = java.lang.Boolean.parseBoolean(globalProperty);
            }
            boolean isEnabled = true;
            if (null != enabledProperty) {
                isEnabled = java.lang.Boolean.parseBoolean(enabledProperty);
            }
            final java.util.Set<org.apache.ambari.server.state.AlertState> alertStateSet;
            if (null != alertStates) {
                alertStateSet = new java.util.HashSet<>(alertStates.size());
                for (java.lang.String state : alertStates) {
                    alertStateSet.add(org.apache.ambari.server.state.AlertState.valueOf(state));
                }
            } else {
                alertStateSet = java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class);
            }
            org.apache.ambari.server.orm.entities.AlertTargetEntity entity = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findTargetByName(name);
            if (null == entity) {
                entity = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
            } else if (!overwriteExisting) {
                throw new java.lang.IllegalArgumentException("Alert targets already exists and can't be created");
            }
            if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS)) {
                java.util.Collection<java.lang.Long> groupIds = ((java.util.Collection<java.lang.Long>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS)));
                if (!groupIds.isEmpty()) {
                    java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.HashSet<>();
                    java.util.List<java.lang.Long> ids = new java.util.ArrayList<>(groupIds);
                    groups.addAll(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findGroupsById(ids));
                    entity.setAlertGroups(groups);
                }
            }
            entity.setDescription(description);
            entity.setNotificationType(notificationType);
            entity.setProperties(propertiesJson);
            entity.setTargetName(name);
            entity.setAlertStates(alertStateSet);
            entity.setGlobal(isGlobal);
            entity.setEnabled(isEnabled);
            if ((null == entity.getTargetId()) || (0 == entity.getTargetId())) {
                org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.create(entity);
            } else {
                org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.merge(entity);
            }
        }
    }

    @com.google.inject.persist.Transactional
    @java.lang.SuppressWarnings("unchecked")
    void updateAlertTargets(long alertTargetId, java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.AlertTargetEntity entity = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findTargetById(alertTargetId);
        if (null == entity) {
            java.lang.String message = java.text.MessageFormat.format("The alert target with ID {0} could not be found", alertTargetId);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        java.lang.String name = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME)));
        java.lang.String description = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION)));
        java.lang.String notificationType = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE)));
        java.util.Collection<java.lang.String> alertStates = ((java.util.Collection<java.lang.String>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES)));
        java.util.Collection<java.lang.Long> groupIds = ((java.util.Collection<java.lang.Long>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS)));
        java.lang.String isGlobal = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL)));
        java.lang.String isEnabled = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ENABLED)));
        if (null != isGlobal) {
            entity.setGlobal(java.lang.Boolean.parseBoolean(isGlobal));
        }
        if (null != isEnabled) {
            entity.setEnabled(java.lang.Boolean.parseBoolean(isEnabled));
        }
        if (!org.apache.commons.lang.StringUtils.isBlank(name)) {
            entity.setTargetName(name);
        }
        if (null != description) {
            entity.setDescription(description);
        }
        if (!org.apache.commons.lang.StringUtils.isBlank(notificationType)) {
            entity.setNotificationType(notificationType);
        }
        java.util.Map<java.lang.String, java.lang.Object> propertiesMap = extractProperties(requestMap);
        if (propertiesMap != null) {
            java.lang.String properties = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_gson.toJson(propertiesMap);
            if (!org.apache.commons.lang.StringUtils.isEmpty(properties)) {
                org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.LOG.debug("Updating Alert Target properties map to: " + properties);
                entity.setProperties(properties);
            }
        }
        if (null != alertStates) {
            final java.util.Set<org.apache.ambari.server.state.AlertState> alertStateSet;
            if (alertStates.isEmpty()) {
                alertStateSet = java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class);
            } else {
                alertStateSet = new java.util.HashSet<>(alertStates.size());
                for (java.lang.String state : alertStates) {
                    alertStateSet.add(org.apache.ambari.server.state.AlertState.valueOf(state));
                }
            }
            entity.setAlertStates(alertStateSet);
        }
        if (null != groupIds) {
            java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.HashSet<>();
            java.util.List<java.lang.Long> ids = new java.util.ArrayList<>(groupIds);
            if (ids.size() > 0) {
                groups.addAll(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findGroupsById(ids));
            }
            entity.setAlertGroups(groups);
        } else if (entity.isGlobal()) {
            java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.HashSet<>(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.findAllGroups());
            entity.setAlertGroups(groups);
        }
        org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_dao.merge(entity);
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.AlertTargetEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget);
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ID, entity.getTargetId());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, entity.getTargetName());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION, entity.getDescription());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE, entity.getNotificationType());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_ENABLED, entity.isEnabled());
        if (requestedIds.contains(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES)) {
            java.lang.String properties = entity.getProperties();
            java.util.Map<java.lang.String, java.lang.String> map = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.s_gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(properties, java.util.Map.class);
            resource.setProperty(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES, map);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES, entity.getAlertStates(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GLOBAL, entity.isGlobal(), requestedIds);
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS, requestedIds)) {
            java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> groupEntities = entity.getAlertGroups();
            java.util.List<org.apache.ambari.server.state.alert.AlertGroup> groups = new java.util.ArrayList<>(groupEntities.size());
            for (org.apache.ambari.server.orm.entities.AlertGroupEntity groupEntity : groupEntities) {
                org.apache.ambari.server.state.alert.AlertGroup group = new org.apache.ambari.server.state.alert.AlertGroup();
                group.setId(groupEntity.getGroupId());
                group.setName(groupEntity.getGroupName());
                group.setClusterName(groupEntity.getClusterId());
                group.setDefault(groupEntity.isDefault());
                groups.add(group);
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS, groups);
        }
        return resource;
    }

    private java.util.Map<java.lang.String, java.lang.Object> extractProperties(java.util.Map<java.lang.String, java.lang.Object> requestMap) {
        java.util.Map<java.lang.String, java.lang.Object> normalizedMap = new java.util.HashMap<>(requestMap.size());
        boolean has_properties = false;
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : requestMap.entrySet()) {
            java.lang.String key = entry.getKey();
            java.lang.String propCat = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(key);
            if (propCat.equals(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES)) {
                has_properties = true;
                java.lang.String propKey = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(key);
                normalizedMap.put(propKey, entry.getValue());
            }
        }
        if (!has_properties) {
            normalizedMap = null;
        }
        return normalizedMap;
    }

    private void validateTargetConfig(java.lang.String notificationType, java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.dispatchFactory.getDispatcher(notificationType);
        if (dispatcher == null) {
            throw new java.lang.IllegalArgumentException("Dispatcher for given notification type doesn't exist");
        }
        org.apache.ambari.server.notifications.TargetConfigurationResult validationResult = dispatcher.validateTargetConfig(properties);
        if (validationResult.getStatus() == org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID) {
            throw new java.lang.IllegalArgumentException(validationResult.getMessage());
        }
    }
}