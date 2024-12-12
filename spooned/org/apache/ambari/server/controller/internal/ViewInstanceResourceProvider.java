package org.apache.ambari.server.controller.internal;
import com.google.inject.persist.Transactional;
public class ViewInstanceResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    public static final java.lang.String VIEW_INSTANCE_INFO = "ViewInstanceInfo";

    public static final java.lang.String VIEW_NAME_PROPERTY_ID = "view_name";

    public static final java.lang.String VERSION_PROPERTY_ID = "version";

    public static final java.lang.String INSTANCE_NAME_PROPERTY_ID = "instance_name";

    public static final java.lang.String LABEL_PROPERTY_ID = "label";

    public static final java.lang.String DESCRIPTION_PROPERTY_ID = "description";

    public static final java.lang.String VISIBLE_PROPERTY_ID = "visible";

    public static final java.lang.String ICON_PATH_PROPERTY_ID = "icon_path";

    public static final java.lang.String ICON64_PATH_PROPERTY_ID = "icon64_path";

    public static final java.lang.String PROPERTIES_PROPERTY_ID = "properties";

    public static final java.lang.String INSTANCE_DATA_PROPERTY_ID = "instance_data";

    public static final java.lang.String CONTEXT_PATH_PROPERTY_ID = "context_path";

    public static final java.lang.String STATIC_PROPERTY_ID = "static";

    public static final java.lang.String CLUSTER_HANDLE_PROPERTY_ID = "cluster_handle";

    public static final java.lang.String CLUSTER_TYPE_PROPERTY_ID = "cluster_type";

    public static final java.lang.String SHORT_URL_PROPERTY_ID = "short_url";

    public static final java.lang.String SHORT_URL_NAME_PROPERTY_ID = "short_url_name";

    public static final java.lang.String VALIDATION_RESULT_PROPERTY_ID = "validation_result";

    public static final java.lang.String PROPERTY_VALIDATION_RESULTS_PROPERTY_ID = "property_validation_results";

    public static final java.lang.String VIEW_NAME = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME_PROPERTY_ID;

    public static final java.lang.String VERSION = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION_PROPERTY_ID;

    public static final java.lang.String INSTANCE_NAME = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME_PROPERTY_ID;

    public static final java.lang.String LABEL = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL_PROPERTY_ID;

    public static final java.lang.String DESCRIPTION = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION_PROPERTY_ID;

    public static final java.lang.String VISIBLE = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VISIBLE_PROPERTY_ID;

    public static final java.lang.String ICON_PATH = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON_PATH_PROPERTY_ID;

    public static final java.lang.String ICON64_PATH = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON64_PATH_PROPERTY_ID;

    public static final java.lang.String PROPERTIES = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES_PROPERTY_ID;

    public static final java.lang.String INSTANCE_DATA = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_DATA_PROPERTY_ID;

    public static final java.lang.String CONTEXT_PATH = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CONTEXT_PATH_PROPERTY_ID;

    public static final java.lang.String STATIC = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.STATIC_PROPERTY_ID;

    public static final java.lang.String CLUSTER_HANDLE = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_HANDLE_PROPERTY_ID;

    public static final java.lang.String CLUSTER_TYPE = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_TYPE_PROPERTY_ID;

    public static final java.lang.String SHORT_URL = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL_PROPERTY_ID;

    public static final java.lang.String SHORT_URL_NAME = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL_NAME_PROPERTY_ID;

    public static final java.lang.String VALIDATION_RESULT = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VALIDATION_RESULT_PROPERTY_ID;

    public static final java.lang.String PROPERTY_VALIDATION_RESULTS = (org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTY_VALIDATION_RESULTS_PROPERTY_ID;

    private static final java.lang.String PROPERTIES_PREFIX = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES + "/";

    private static final java.lang.String DATA_PREFIX = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_DATA + "/";

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VISIBLE, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON_PATH, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON64_PATH, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_DATA, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CONTEXT_PATH, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.STATIC, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_HANDLE, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_TYPE, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL_NAME, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VALIDATION_RESULT, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTY_VALIDATION_RESULTS);

    @com.google.inject.Inject
    public ViewInstanceResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.keyPropertyIds);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_VIEWS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            createResources(getCreateCommand(properties));
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        if (propertyMaps.isEmpty()) {
            propertyMaps.add(java.util.Collections.emptyMap());
        }
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            java.lang.String viewName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME)));
            java.lang.String viewVersion = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION)));
            java.lang.String instanceName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME)));
            for (org.apache.ambari.server.orm.entities.ViewEntity viewDefinition : viewRegistry.getDefinitions()) {
                if (viewDefinition.isDeployed()) {
                    if ((viewName == null) || viewName.equals(viewDefinition.getCommonName())) {
                        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition : viewRegistry.getInstanceDefinitions(viewDefinition)) {
                            if ((instanceName == null) || instanceName.equals(viewInstanceDefinition.getName())) {
                                if ((viewVersion == null) || viewVersion.equals(viewDefinition.getVersion())) {
                                    if (includeInstance(viewInstanceDefinition, true)) {
                                        org.apache.ambari.server.controller.spi.Resource resource = toResource(viewInstanceDefinition, requestedIds);
                                        resources.add(resource);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                modifyResources(getUpdateCommand(propertyMap));
            }
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(getDeleteCommand(predicate));
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.keyPropertyIds.values());
    }

    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceEntity.getViewEntity();
        java.lang.String viewName = viewEntity.getCommonName();
        java.lang.String version = viewEntity.getVersion();
        java.lang.String name = viewInstanceEntity.getName();
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME, viewName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION, version, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME, name, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL, viewInstanceEntity.getLabel(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION, viewInstanceEntity.getDescription(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VISIBLE, viewInstanceEntity.isVisible(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.STATIC, viewInstanceEntity.isXmlDriven(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_HANDLE, viewInstanceEntity.getClusterHandle(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_TYPE, viewInstanceEntity.getClusterType(), requestedIds);
        org.apache.ambari.server.orm.entities.ViewURLEntity viewUrl = viewInstanceEntity.getViewUrl();
        if (viewUrl != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL, viewUrl.getUrlSuffix(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL_NAME, viewUrl.getUrlName(), requestedIds);
        }
        if (org.apache.ambari.server.view.ViewRegistry.getInstance().checkAdmin()) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES, viewInstanceEntity.getPropertyMap(), requestedIds);
        }
        java.util.Map<java.lang.String, java.lang.String> applicationData = new java.util.HashMap<>();
        java.lang.String currentUserName = viewInstanceEntity.getCurrentUserName();
        for (org.apache.ambari.server.orm.entities.ViewInstanceDataEntity viewInstanceDataEntity : viewInstanceEntity.getData()) {
            if (currentUserName.equals(viewInstanceDataEntity.getUser())) {
                applicationData.put(viewInstanceDataEntity.getName(), viewInstanceDataEntity.getValue());
            }
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_DATA, applicationData, requestedIds);
        java.lang.String contextPath = org.apache.ambari.server.orm.entities.ViewInstanceEntity.getContextPath(viewName, version, name);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CONTEXT_PATH, contextPath, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON_PATH, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.getIconPath(contextPath, viewInstanceEntity.getIcon()), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON64_PATH, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.getIconPath(contextPath, viewInstanceEntity.getIcon64()), requestedIds);
        if (viewEntity.hasValidator()) {
            if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VALIDATION_RESULT, requestedIds) || org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTY_VALIDATION_RESULTS, requestedIds)) {
                org.apache.ambari.server.view.validation.InstanceValidationResultImpl result = viewInstanceEntity.getValidationResult(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.EXISTING);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VALIDATION_RESULT, org.apache.ambari.server.view.validation.ValidationResultImpl.create(result), requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTY_VALIDATION_RESULTS, result.getPropertyResults(), requestedIds);
            }
        }
        return resource;
    }

    private org.apache.ambari.server.orm.entities.ViewInstanceEntity toEntity(java.util.Map<java.lang.String, java.lang.Object> properties, boolean update) throws org.apache.ambari.server.AmbariException {
        java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME)));
        if ((name == null) || name.isEmpty()) {
            throw new java.lang.IllegalArgumentException("View instance name must be provided");
        }
        java.lang.String version = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION)));
        if ((version == null) || version.isEmpty()) {
            throw new java.lang.IllegalArgumentException("View version must be provided");
        }
        java.lang.String commonViewName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME)));
        if ((commonViewName == null) || commonViewName.isEmpty()) {
            throw new java.lang.IllegalArgumentException("View name must be provided");
        }
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewRegistry.getDefinition(commonViewName, version);
        java.lang.String viewName = org.apache.ambari.server.orm.entities.ViewEntity.getViewName(commonViewName, version);
        if (viewEntity == null) {
            throw new java.lang.IllegalArgumentException(("View name " + viewName) + " does not exist.");
        }
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = null;
        if (update) {
            viewInstanceEntity = viewRegistry.getViewInstanceEntity(viewName, name);
        }
        if (viewInstanceEntity == null) {
            viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity();
            viewInstanceEntity.setName(name);
            viewInstanceEntity.setViewName(viewName);
            viewInstanceEntity.setViewEntity(viewEntity);
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL)) {
            viewInstanceEntity.setLabel(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL))));
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION)) {
            viewInstanceEntity.setDescription(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION))));
        }
        java.lang.String visible = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VISIBLE)));
        viewInstanceEntity.setVisible(visible == null ? true : java.lang.Boolean.valueOf(visible));
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON_PATH)) {
            viewInstanceEntity.setIcon(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON_PATH))));
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON64_PATH)) {
            viewInstanceEntity.setIcon64(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON64_PATH))));
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_HANDLE)) {
            java.lang.String handle = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_HANDLE)));
            if (handle != null) {
                viewInstanceEntity.setClusterHandle(java.lang.Long.valueOf(handle));
            } else {
                viewInstanceEntity.setClusterHandle(null);
            }
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_TYPE)) {
            java.lang.String clusterType = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_TYPE)));
            viewInstanceEntity.setClusterType(org.apache.ambari.view.ClusterType.valueOf(clusterType));
        }
        java.util.Map<java.lang.String, java.lang.String> instanceProperties = new java.util.HashMap<>();
        boolean isUserAdmin = viewRegistry.checkAdmin();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String propertyName = entry.getKey();
            if (propertyName.startsWith(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES_PREFIX)) {
                if (isUserAdmin) {
                    instanceProperties.put(entry.getKey().substring(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES_PREFIX.length()), ((java.lang.String) (entry.getValue())));
                }
            } else if (propertyName.startsWith(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DATA_PREFIX)) {
                viewInstanceEntity.putInstanceData(entry.getKey().substring(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DATA_PREFIX.length()), ((java.lang.String) (entry.getValue())));
            }
        }
        if (!instanceProperties.isEmpty()) {
            try {
                viewRegistry.setViewInstanceProperties(viewInstanceEntity, instanceProperties, viewEntity.getConfiguration(), viewEntity.getClassLoader());
            } catch (org.apache.ambari.view.SystemException e) {
                throw new org.apache.ambari.server.AmbariException("Caught exception trying to set view properties.", e);
            }
        }
        return viewInstanceEntity;
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getCreateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                create(properties);
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getUpdateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                update(properties);
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getDeleteCommand(final org.apache.ambari.server.controller.spi.Predicate predicate) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                delete(predicate);
                return null;
            }
        };
    }

    @com.google.inject.persist.Transactional
    void create(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        try {
            org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
            org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = toEntity(properties, false);
            org.apache.ambari.server.orm.entities.ViewEntity viewEntity = instanceEntity.getViewEntity();
            java.lang.String viewName = viewEntity.getCommonName();
            java.lang.String version = viewEntity.getVersion();
            org.apache.ambari.server.orm.entities.ViewEntity view = viewRegistry.getDefinition(viewName, version);
            if (view == null) {
                throw new java.lang.IllegalStateException(("The view " + viewName) + " is not registered.");
            }
            if (!view.isDeployed()) {
                throw new java.lang.IllegalStateException(("The view " + viewName) + " is not loaded.");
            }
            if (viewRegistry.instanceExists(instanceEntity)) {
                throw new org.apache.ambari.server.DuplicateResourceException(("The instance " + instanceEntity.getName()) + " already exists.");
            }
            viewRegistry.installViewInstance(instanceEntity);
        } catch (org.apache.ambari.view.SystemException e) {
            throw new org.apache.ambari.server.AmbariException("Caught exception trying to create view instance.", e);
        } catch (org.apache.ambari.server.view.validation.ValidationException e) {
            throw new java.lang.IllegalArgumentException(e.getMessage(), e);
        }
    }

    @com.google.inject.persist.Transactional
    void update(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instance = toEntity(properties, true);
        org.apache.ambari.server.orm.entities.ViewEntity view = instance.getViewEntity();
        if (includeInstance(view.getCommonName(), view.getVersion(), instance.getInstanceName(), false)) {
            try {
                org.apache.ambari.server.view.ViewRegistry.getInstance().updateViewInstance(instance);
                org.apache.ambari.server.view.ViewRegistry.getInstance().updateView(instance);
            } catch (org.apache.ambari.view.SystemException e) {
                throw new org.apache.ambari.server.AmbariException("Caught exception trying to update view instance.", e);
            } catch (org.apache.ambari.server.view.validation.ValidationException e) {
                throw new java.lang.IllegalArgumentException(e.getMessage(), e);
            }
        }
    }

    @com.google.inject.persist.Transactional
    void delete(org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceEntities = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : viewRegistry.getDefinitions()) {
            if (viewEntity.isDeployed()) {
                for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewRegistry.getInstanceDefinitions(viewEntity)) {
                    org.apache.ambari.server.controller.spi.Resource resource = toResource(viewInstanceEntity, requestedIds);
                    if ((predicate == null) || predicate.evaluate(resource)) {
                        if (includeInstance(viewInstanceEntity, false)) {
                            viewInstanceEntities.add(viewInstanceEntity);
                        }
                    }
                }
            }
        }
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewInstanceEntities) {
            viewRegistry.uninstallViewInstance(viewInstanceEntity);
        }
    }

    private static java.lang.String getIconPath(java.lang.String contextPath, java.lang.String iconPath) {
        return (iconPath == null) || (iconPath.length() == 0) ? null : (contextPath + (iconPath.startsWith("/") ? "" : "/")) + iconPath;
    }

    private boolean includeInstance(java.lang.String viewName, java.lang.String version, java.lang.String instanceName, boolean readOnly) {
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        return viewRegistry.checkPermission(viewName, version, instanceName, readOnly);
    }

    private boolean includeInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity, boolean readOnly) {
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        return viewRegistry.checkPermission(instanceEntity, readOnly);
    }
}