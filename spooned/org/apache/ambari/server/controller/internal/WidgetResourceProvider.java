package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import static org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_WIDGETS;
@org.apache.ambari.server.StaticallyInject
public class WidgetResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String WIDGET_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "id");

    public static final java.lang.String WIDGET_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "cluster_name");

    public static final java.lang.String WIDGET_WIDGET_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "widget_name");

    public static final java.lang.String WIDGET_WIDGET_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "widget_type");

    public static final java.lang.String WIDGET_TIME_CREATED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "time_created");

    public static final java.lang.String WIDGET_AUTHOR_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "author");

    public static final java.lang.String WIDGET_DESCRIPTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "description");

    public static final java.lang.String WIDGET_SCOPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "scope");

    public static final java.lang.String WIDGET_METRICS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "metrics");

    public static final java.lang.String WIDGET_VALUES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "values");

    public static final java.lang.String WIDGET_PROPERTIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "properties");

    public static final java.lang.String WIDGET_TAG_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("WidgetInfo", "tag");

    public enum SCOPE {

        CLUSTER,
        USER;}

    @java.lang.SuppressWarnings("serial")
    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).build();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TIME_CREATED_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TAG_PROPERTY_ID).build();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Widget, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID).build();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO;

    private static com.google.gson.Gson gson = new com.google.gson.GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().serializeNulls().setPrettyPrinting().registerTypeAdapter(java.lang.String.class, new com.google.gson.JsonSerializer<java.lang.String>() {
        @java.lang.Override
        public com.google.gson.JsonElement serialize(java.lang.String src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(org.apache.commons.text.StringEscapeUtils.escapeHtml4(src));
        }
    }).create();

    public WidgetResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Widget, org.apache.ambari.server.controller.internal.WidgetResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.WidgetResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        for (final java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.orm.entities.WidgetEntity>() {
                @java.lang.Override
                public org.apache.ambari.server.orm.entities.WidgetEntity invoke() throws org.apache.ambari.server.AmbariException {
                    final java.lang.String[] requiredProperties = new java.lang.String[]{ org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID };
                    for (java.lang.String propertyName : requiredProperties) {
                        if (properties.get(propertyName) == null) {
                            throw new org.apache.ambari.server.AmbariException(("Property " + propertyName) + " should be provided");
                        }
                    }
                    final org.apache.ambari.server.orm.entities.WidgetEntity entity = new org.apache.ambari.server.orm.entities.WidgetEntity();
                    java.lang.String clusterName = properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).toString();
                    java.lang.String scope = properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID).toString();
                    if (!isScopeAllowedForUser(scope, clusterName)) {
                        throw new org.springframework.security.access.AccessDeniedException("Only cluster operator can create widgets with cluster scope");
                    }
                    entity.setWidgetName(org.apache.commons.text.StringEscapeUtils.escapeHtml4(properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID).toString()));
                    entity.setWidgetType(properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID).toString());
                    entity.setClusterId(getManagementController().getClusters().getCluster(clusterName).getClusterId());
                    entity.setScope(scope);
                    java.lang.String metrics = (properties.containsKey(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID)) ? org.apache.ambari.server.controller.internal.WidgetResourceProvider.gson.toJson(properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID)) : null;
                    entity.setMetrics(metrics);
                    entity.setAuthor(getAuthorName(properties));
                    java.lang.String description = (properties.containsKey(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID)) ? org.apache.commons.text.StringEscapeUtils.escapeHtml4(properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID).toString()) : null;
                    entity.setDescription(description);
                    java.lang.String values = (properties.containsKey(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID)) ? org.apache.ambari.server.controller.internal.WidgetResourceProvider.gson.toJson(properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID)) : null;
                    entity.setWidgetValues(values);
                    java.util.Map<java.lang.String, java.lang.Object> widgetPropertiesMap = new java.util.HashMap<>();
                    for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
                        if (org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey()).equals(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID)) {
                            widgetPropertiesMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()), entry.getValue());
                        }
                    }
                    java.lang.String widgetProperties = (widgetPropertiesMap.isEmpty()) ? null : org.apache.ambari.server.controller.internal.WidgetResourceProvider.gson.toJson(widgetPropertiesMap);
                    entity.setProperties(widgetProperties);
                    if (properties.containsKey(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TAG_PROPERTY_ID)) {
                        entity.setTag(properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TAG_PROPERTY_ID).toString());
                    }
                    org.apache.ambari.server.controller.internal.WidgetResourceProvider.widgetDAO.create(entity);
                    notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Widget, request);
                    return entity;
                }
            });
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Widget);
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID, widgetEntity.getId());
            associatedResources.add(resource);
        }
        return getRequestStatus(null, associatedResources);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        java.lang.String author = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();
        java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> requestedEntities = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            if (propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID) != null) {
                final java.lang.Long id;
                try {
                    id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).toString());
                } catch (java.lang.Exception ex) {
                    throw new org.apache.ambari.server.controller.spi.SystemException("WidgetLayout should have numerical id");
                }
                final org.apache.ambari.server.orm.entities.WidgetEntity entity = org.apache.ambari.server.controller.internal.WidgetResourceProvider.widgetDAO.findById(id);
                if (entity == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(("WidgetLayout with id " + id) + " does not exists");
                }
                if (!(entity.getAuthor().equals(author) || entity.getScope().equals(org.apache.ambari.server.controller.internal.WidgetResourceProvider.SCOPE.CLUSTER.name()))) {
                    throw new org.springframework.security.access.AccessDeniedException("User must be author of the widget or widget must have cluster scope");
                }
                requestedEntities.add(entity);
            } else {
                requestedEntities.addAll(org.apache.ambari.server.controller.internal.WidgetResourceProvider.widgetDAO.findByScopeOrAuthor(author, org.apache.ambari.server.controller.internal.WidgetResourceProvider.SCOPE.CLUSTER.name()));
            }
        }
        for (org.apache.ambari.server.orm.entities.WidgetEntity entity : requestedEntities) {
            final org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Widget);
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID, entity.getId());
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID, entity.getWidgetName());
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID, entity.getWidgetType());
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID, entity.getMetrics(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TIME_CREATED_PROPERTY_ID, entity.getTimeCreated(), requestedIds);
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID, entity.getAuthor());
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID, entity.getDescription(), requestedIds);
            resource.setProperty(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID, entity.getScope());
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID, entity.getWidgetValues(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID, entity.getProperties(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TAG_PROPERTY_ID, entity.getTag(), requestedIds);
            java.lang.String clusterName = null;
            try {
                clusterName = getManagementController().getClusters().getClusterById(entity.getClusterId()).getClusterName();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage());
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = request.getProperties();
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                    final java.lang.Long id;
                    try {
                        id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).toString());
                    } catch (java.lang.Exception ex) {
                        throw new org.apache.ambari.server.AmbariException("Widget should have numerical id");
                    }
                    final org.apache.ambari.server.orm.entities.WidgetEntity entity = org.apache.ambari.server.controller.internal.WidgetResourceProvider.widgetDAO.findById(id);
                    if (entity == null) {
                        throw new org.apache.ambari.server.ObjectNotFoundException("There is no widget with id " + id);
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID)))) {
                        entity.setWidgetName(org.apache.commons.text.StringEscapeUtils.escapeHtml4(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_NAME_PROPERTY_ID).toString()));
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID)))) {
                        entity.setWidgetType(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_WIDGET_TYPE_PROPERTY_ID).toString());
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID)))) {
                        entity.setMetrics(org.apache.ambari.server.controller.internal.WidgetResourceProvider.gson.toJson(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_METRICS_PROPERTY_ID)));
                    }
                    entity.setAuthor(getAuthorName(propertyMap));
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID)))) {
                        entity.setDescription(org.apache.commons.text.StringEscapeUtils.escapeHtml4(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_DESCRIPTION_PROPERTY_ID).toString()));
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID)))) {
                        java.lang.String scope = propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_SCOPE_PROPERTY_ID).toString();
                        java.lang.String clusterName = propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_CLUSTER_NAME_PROPERTY_ID).toString();
                        if (!isScopeAllowedForUser(scope, clusterName)) {
                            throw new org.apache.ambari.server.AmbariException("Only cluster operator can create widgets with cluster scope");
                        }
                        entity.setScope(scope);
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID)))) {
                        entity.setWidgetValues(org.apache.ambari.server.controller.internal.WidgetResourceProvider.gson.toJson(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_VALUES_PROPERTY_ID)));
                    }
                    java.util.Map<java.lang.String, java.lang.Object> widgetPropertiesMap = new java.util.HashMap<>();
                    for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : propertyMap.entrySet()) {
                        if (org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey()).equals(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_PROPERTIES_PROPERTY_ID)) {
                            widgetPropertiesMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()), entry.getValue());
                        }
                    }
                    if (!widgetPropertiesMap.isEmpty()) {
                        entity.setProperties(org.apache.ambari.server.controller.internal.WidgetResourceProvider.gson.toJson(widgetPropertiesMap));
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TAG_PROPERTY_ID)))) {
                        entity.setTag(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_TAG_PROPERTY_ID).toString());
                    }
                    org.apache.ambari.server.controller.internal.WidgetResourceProvider.widgetDAO.merge(entity);
                }
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        final java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> entitiesToBeRemoved = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            final java.lang.Long id;
            try {
                id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_ID_PROPERTY_ID).toString());
            } catch (java.lang.Exception ex) {
                throw new org.apache.ambari.server.controller.spi.SystemException("Widget should have numerical id");
            }
            final org.apache.ambari.server.orm.entities.WidgetEntity entity = org.apache.ambari.server.controller.internal.WidgetResourceProvider.widgetDAO.findById(id);
            if (entity == null) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("There is no widget with id " + id);
            }
            entitiesToBeRemoved.add(entity);
        }
        for (org.apache.ambari.server.orm.entities.WidgetEntity entity : entitiesToBeRemoved) {
            if (entity.getListWidgetLayoutUserWidgetEntity() != null) {
                for (org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity layoutUserWidgetEntity : entity.getListWidgetLayoutUserWidgetEntity()) {
                    if (layoutUserWidgetEntity.getWidgetLayout().getListWidgetLayoutUserWidgetEntity() != null) {
                        layoutUserWidgetEntity.getWidgetLayout().getListWidgetLayoutUserWidgetEntity().remove(layoutUserWidgetEntity);
                    }
                }
            }
            org.apache.ambari.server.controller.internal.WidgetResourceProvider.widgetDAO.remove(entity);
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.WidgetResourceProvider.pkPropertyIds;
    }

    private boolean isScopeAllowedForUser(java.lang.String scope, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        if (org.apache.ambari.server.orm.entities.WidgetEntity.USER_SCOPE.equals(scope)) {
            return true;
        }
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, getClusterResourceId(clusterName), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_WIDGETS));
    }

    private java.lang.String getAuthorName(java.util.Map<java.lang.String, java.lang.Object> properties) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID)))) {
            return properties.get(org.apache.ambari.server.controller.internal.WidgetResourceProvider.WIDGET_AUTHOR_PROPERTY_ID).toString();
        }
        return getManagementController().getAuthName();
    }
}