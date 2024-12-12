package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class SettingResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    public static final java.lang.String RESPONSE_KEY = "Settings";

    protected static final java.lang.String ID = "id";

    protected static final java.lang.String SETTING = "Setting";

    public static final java.lang.String NAME = "name";

    public static final java.lang.String SETTING_TYPE = "setting_type";

    public static final java.lang.String CONTENT = "content";

    public static final java.lang.String UPDATED_BY = "updated_by";

    public static final java.lang.String UPDATE_TIMESTAMP = "update_timestamp";

    public static final java.lang.String SETTING_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.SettingResourceProvider.NAME;

    public static final java.lang.String SETTING_SETTING_TYPE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_TYPE;

    public static final java.lang.String SETTING_CONTENT_PROPERTY_ID = (org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.SettingResourceProvider.CONTENT;

    public static final java.lang.String SETTING_UPDATED_BY_PROPERTY_ID = (org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.SettingResourceProvider.UPDATED_BY;

    public static final java.lang.String SETTING_UPDATE_TIMESTAMP_PROPERTY_ID = (org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.SettingResourceProvider.UPDATE_TIMESTAMP;

    public static final java.lang.String ALL_PROPERTIES = (org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "*";

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING).build();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Setting, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> REQUIRED_PROPERTIES = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID);

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.SettingDAO dao;

    protected SettingResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Setting, org.apache.ambari.server.controller.internal.SettingResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.SettingResourceProvider.keyPropertyIds);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_SETTINGS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.SettingResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            org.apache.ambari.server.controller.SettingResponse setting = createResources(newCreateCommand(request, properties));
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Setting);
            resource.setProperty(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, setting.getName());
            associatedResources.add(resource);
        }
        return getRequestStatus(null, associatedResources);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.List<org.apache.ambari.server.orm.entities.SettingEntity> entities = new java.util.LinkedList<>();
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        if (propertyMaps.isEmpty()) {
            entities = org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.findAll();
        }
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID)) {
                java.lang.String name = propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).toString();
                org.apache.ambari.server.orm.entities.SettingEntity entity = org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.findByName(name);
                if (entity == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(java.lang.String.format("Setting with name %s does not exists", name));
                }
                entities.add(entity);
            } else {
                entities = org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.findAll();
                break;
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.SettingEntity entity : entities) {
            resources.add(toResource(org.apache.ambari.server.controller.internal.SettingResourceProvider.toResponse(entity), requestedIds));
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.SystemException {
        modifyResources(newUpdateCommand(request));
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID)) {
                org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.removeByName(propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).toString());
            }
        }
        return getRequestStatus(null);
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.SettingResponse> newCreateCommand(final org.apache.ambari.server.controller.spi.Request request, final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.SettingResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.SettingResponse invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                org.apache.ambari.server.orm.entities.SettingEntity entity = toEntity(properties);
                if (org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.findByName(entity.getName()) != null) {
                    throw new org.apache.ambari.server.DuplicateResourceException(java.lang.String.format("Setting already exists. setting name :%s ", entity.getName()));
                }
                org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.create(entity);
                notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Setting, request);
                return org.apache.ambari.server.controller.internal.SettingResourceProvider.toResponse(entity);
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> newUpdateCommand(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.SystemException {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = request.getProperties();
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                    if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID)) {
                        java.lang.String name = propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).toString();
                        org.apache.ambari.server.orm.entities.SettingEntity entity = org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.findByName(name);
                        if (entity == null) {
                            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("There is no setting with name: %s ", name));
                        }
                        updateEntity(entity, propertyMap);
                        org.apache.ambari.server.controller.internal.SettingResourceProvider.dao.merge(entity);
                    }
                }
                return null;
            }
        };
    }

    private void updateEntity(org.apache.ambari.server.orm.entities.SettingEntity entity, java.util.Map<java.lang.String, java.lang.Object> propertyMap) throws org.apache.ambari.server.AmbariException {
        java.lang.String name = propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).toString();
        if (!java.util.Objects.equals(name, entity.getName())) {
            throw new org.apache.ambari.server.AmbariException("Name for Setting is immutable, cannot change name.");
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID)))) {
            entity.setContent(propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID).toString());
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID)))) {
            entity.setSettingType(propertyMap.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID).toString());
        }
        entity.setUpdatedBy(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
        entity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(final org.apache.ambari.server.controller.SettingResponse setting, final java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Setting);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID, setting.getName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID, setting.getSettingType(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID, setting.getContent(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATED_BY_PROPERTY_ID, setting.getUpdatedBy(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_UPDATE_TIMESTAMP_PROPERTY_ID, setting.getUpdateTimestamp(), requestedIds);
        return resource;
    }

    private org.apache.ambari.server.orm.entities.SettingEntity toEntity(final java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        for (java.lang.String propertyName : org.apache.ambari.server.controller.internal.SettingResourceProvider.REQUIRED_PROPERTIES) {
            if (properties.get(propertyName) == null) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Property %s should be provided", propertyName));
            }
        }
        org.apache.ambari.server.orm.entities.SettingEntity entity = new org.apache.ambari.server.orm.entities.SettingEntity();
        entity.setName(properties.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_NAME_PROPERTY_ID).toString());
        entity.setSettingType(properties.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_SETTING_TYPE_PROPERTY_ID).toString());
        entity.setContent(properties.get(org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_CONTENT_PROPERTY_ID).toString());
        entity.setUpdatedBy(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
        entity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
        return entity;
    }

    private static org.apache.ambari.server.controller.SettingResponse toResponse(org.apache.ambari.server.orm.entities.SettingEntity entity) {
        return new org.apache.ambari.server.controller.SettingResponse(entity.getName(), entity.getSettingType(), entity.getContent(), entity.getUpdatedBy(), entity.getUpdateTimestamp());
    }
}