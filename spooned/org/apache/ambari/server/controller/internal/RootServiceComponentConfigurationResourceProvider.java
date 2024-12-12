package org.apache.ambari.server.controller.internal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
public class RootServiceComponentConfigurationResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    static final java.lang.String RESOURCE_KEY = "Configuration";

    public static final java.lang.String CONFIGURATION_CATEGORY_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RESOURCE_KEY, "category");

    public static final java.lang.String CONFIGURATION_PROPERTIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RESOURCE_KEY, "properties");

    public static final java.lang.String CONFIGURATION_PROPERTY_TYPES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RESOURCE_KEY, "property_types");

    public static final java.lang.String CONFIGURATION_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RESOURCE_KEY, "component_name");

    public static final java.lang.String CONFIGURATION_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RESOURCE_KEY, "service_name");

    private static final java.util.Set<java.lang.String> PROPERTIES;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> PK_PROPERTY_MAP;

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS;

    static {
        java.util.Set<java.lang.String> set = new java.util.HashSet<>();
        set.add(CONFIGURATION_SERVICE_NAME_PROPERTY_ID);
        set.add(CONFIGURATION_COMPONENT_NAME_PROPERTY_ID);
        set.add(CONFIGURATION_CATEGORY_PROPERTY_ID);
        set.add(CONFIGURATION_PROPERTIES_PROPERTY_ID);
        set.add(CONFIGURATION_PROPERTY_TYPES_PROPERTY_ID);
        PROPERTIES = java.util.Collections.unmodifiableSet(set);
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.RootService, CONFIGURATION_SERVICE_NAME_PROPERTY_ID);
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, CONFIGURATION_COMPONENT_NAME_PROPERTY_ID);
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponentConfiguration, CONFIGURATION_CATEGORY_PROPERTY_ID);
        PK_PROPERTY_MAP = java.util.Collections.unmodifiableMap(map);
        PK_PROPERTY_IDS = java.util.Collections.unmodifiableSet(new java.util.HashSet<>(PK_PROPERTY_MAP.values()));
    }

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandlerFactory rootServiceComponentConfigurationHandlerFactory;

    public RootServiceComponentConfigurationResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponentConfiguration, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.PROPERTIES, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.PK_PROPERTY_MAP);
        java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> authorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_CONFIGURATION);
        setRequiredCreateAuthorizations(authorizations);
        setRequiredDeleteAuthorizations(authorizations);
        setRequiredUpdateAuthorizations(authorizations);
        setRequiredGetAuthorizations(authorizations);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.PK_PROPERTY_IDS;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.internal.OperationStatusMetaData operationStatusMetadata = null;
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = request.getRequestInfoProperties();
        if (requestInfoProperties.containsKey(org.apache.ambari.server.api.services.RootServiceComponentConfigurationService.DIRECTIVE_OPERATION)) {
            java.lang.String operationType = requestInfoProperties.get(org.apache.ambari.server.api.services.RootServiceComponentConfigurationService.DIRECTIVE_OPERATION);
            java.util.Map<java.lang.String, java.lang.Object> operationParameters = getOperationParameters(requestInfoProperties);
            operationStatusMetadata = performOperation(null, null, null, request.getProperties(), false, operationType, operationParameters);
        } else {
            createOrAddProperties(null, null, null, request.getProperties(), true);
        }
        return getRequestStatus(null, null, operationStatusMetadata);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.spi.Resource>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.spi.Resource> invoke() throws org.apache.ambari.server.AmbariException {
                java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
                java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
                if (org.apache.commons.collections.CollectionUtils.isEmpty(requestedIds)) {
                    requestedIds = org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.PROPERTIES;
                }
                if (predicate == null) {
                    java.util.Set<org.apache.ambari.server.controller.spi.Resource> _resources;
                    try {
                        _resources = getConfigurationResources(requestedIds, null);
                    } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
                        throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                    }
                    if (!org.apache.commons.collections.CollectionUtils.isEmpty(_resources)) {
                        resources.addAll(_resources);
                    }
                } else {
                    for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                        java.util.Set<org.apache.ambari.server.controller.spi.Resource> _resources;
                        try {
                            _resources = getConfigurationResources(requestedIds, propertyMap);
                        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
                            throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                        }
                        if (!org.apache.commons.collections.CollectionUtils.isEmpty(_resources)) {
                            resources.addAll(_resources);
                        }
                    }
                }
                return resources;
            }
        });
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.lang.String serviceName = ((java.lang.String) (org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate).get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID)));
        java.lang.String componentName = ((java.lang.String) (org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate).get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID)));
        java.lang.String categoryName = ((java.lang.String) (org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate).get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID)));
        org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler handler = rootServiceComponentConfigurationHandlerFactory.getInstance(serviceName, componentName, categoryName);
        if (handler != null) {
            handler.removeComponentConfiguration(categoryName);
        } else {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Configurations may not be updated for the %s component of the root service %s", componentName, serviceName));
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.lang.String serviceName = ((java.lang.String) (org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate).get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID)));
        java.lang.String componentName = ((java.lang.String) (org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate).get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID)));
        java.lang.String categoryName = ((java.lang.String) (org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate).get(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID)));
        org.apache.ambari.server.controller.internal.OperationStatusMetaData operationStatusMetadata = null;
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = request.getRequestInfoProperties();
        if (requestInfoProperties.containsKey(org.apache.ambari.server.api.services.RootServiceComponentConfigurationService.DIRECTIVE_OPERATION)) {
            java.lang.String operationType = requestInfoProperties.get(org.apache.ambari.server.api.services.RootServiceComponentConfigurationService.DIRECTIVE_OPERATION);
            java.util.Map<java.lang.String, java.lang.Object> operationParameters = getOperationParameters(requestInfoProperties);
            operationStatusMetadata = performOperation(serviceName, componentName, categoryName, request.getProperties(), true, operationType, operationParameters);
        } else {
            createOrAddProperties(serviceName, componentName, categoryName, request.getProperties(), false);
        }
        return getRequestStatus(null, null, operationStatusMetadata);
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(java.lang.String serviceName, java.lang.String componentName, java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.lang.String> propertyTypes, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponentConfiguration);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID, serviceName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID, componentName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID, categoryName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_PROPERTIES_PROPERTY_ID, org.apache.ambari.server.utils.SecretReference.maskPasswordInPropertyMap(properties), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_PROPERTY_TYPES_PROPERTY_ID, propertyTypes, requestedIds);
        return resource;
    }

    private void createOrAddProperties(java.lang.String defaultServiceName, java.lang.String defaultComponentName, java.lang.String defaultCategoryName, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties, boolean removePropertiesIfNotSpecified) throws org.apache.ambari.server.controller.spi.SystemException {
        if (requestProperties != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> resourceProperties : requestProperties) {
                org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RequestDetails requestDetails = parseProperties(defaultServiceName, defaultComponentName, defaultCategoryName, resourceProperties);
                org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler handler = rootServiceComponentConfigurationHandlerFactory.getInstance(requestDetails.serviceName, requestDetails.componentName, requestDetails.categoryName);
                if (handler != null) {
                    try {
                        handler.updateComponentCategory(requestDetails.categoryName, requestDetails.properties, removePropertiesIfNotSpecified);
                    } catch (org.apache.ambari.server.AmbariException | java.lang.IllegalArgumentException e) {
                        throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e.getCause());
                    }
                } else {
                    throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Configurations may not be updated for the %s component of the root service, %s", requestDetails.serviceName, requestDetails.componentName));
                }
            }
        }
    }

    private org.apache.ambari.server.controller.internal.OperationStatusMetaData performOperation(java.lang.String defaultServiceName, java.lang.String defaultComponentName, java.lang.String defaultCategoryName, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties, boolean mergeExistingProperties, java.lang.String operationType, java.util.Map<java.lang.String, java.lang.Object> operationParameters) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.controller.internal.OperationStatusMetaData metaData = new org.apache.ambari.server.controller.internal.OperationStatusMetaData();
        if (requestProperties != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> resourceProperties : requestProperties) {
                org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RequestDetails requestDetails = parseProperties(defaultServiceName, defaultComponentName, defaultCategoryName, resourceProperties);
                org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler handler = rootServiceComponentConfigurationHandlerFactory.getInstance(requestDetails.serviceName, requestDetails.componentName, requestDetails.categoryName);
                if (handler != null) {
                    org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler.OperationResult operationResult = handler.performOperation(requestDetails.categoryName, requestDetails.properties, mergeExistingProperties, operationType, operationParameters);
                    if (operationResult == null) {
                        throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("An unexpected error has occurred while handling an operation for the %s component of the root service, %s", requestDetails.serviceName, requestDetails.componentName));
                    }
                    metaData.addResult(operationResult.getId(), operationResult.isSuccess(), operationResult.getMessage(), operationResult.getResponse());
                } else {
                    throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Operations may not be performed on configurations for the %s component of the root service, %s", requestDetails.serviceName, requestDetails.componentName));
                }
            }
        }
        return metaData;
    }

    private org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RequestDetails parseProperties(java.lang.String defaultServiceName, java.lang.String defaultComponentName, java.lang.String defaultCategoryName, java.util.Map<java.lang.String, java.lang.Object> resourceProperties) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String serviceName = defaultServiceName;
        java.lang.String componentName = defaultComponentName;
        java.lang.String categoryName = defaultCategoryName;
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : resourceProperties.entrySet()) {
            java.lang.String propertyName = entry.getKey();
            if (org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID.equals(propertyName)) {
                if (entry.getValue() instanceof java.lang.String) {
                    categoryName = ((java.lang.String) (entry.getValue()));
                }
            } else if (org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID.equals(propertyName)) {
                if (entry.getValue() instanceof java.lang.String) {
                    componentName = ((java.lang.String) (entry.getValue()));
                }
            } else if (org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID.equals(propertyName)) {
                if (entry.getValue() instanceof java.lang.String) {
                    serviceName = ((java.lang.String) (entry.getValue()));
                }
            } else {
                java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey());
                if ((propertyCategory != null) && propertyCategory.equals(org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_PROPERTIES_PROPERTY_ID)) {
                    java.lang.String name = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey());
                    java.lang.Object value = entry.getValue();
                    properties.put(name, value == null ? null : value.toString());
                }
            }
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
            throw new org.apache.ambari.server.controller.spi.SystemException("The service name must be set");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(componentName)) {
            throw new org.apache.ambari.server.controller.spi.SystemException("The component name must be set");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(categoryName)) {
            throw new org.apache.ambari.server.controller.spi.SystemException("The configuration category must be set");
        }
        if (properties.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.SystemException("The configuration properties must be set");
        }
        return new org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.RequestDetails(serviceName, componentName, categoryName, properties);
    }

    private java.util.Map<java.lang.String, java.lang.Object> getOperationParameters(java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) {
        java.util.Map<java.lang.String, java.lang.Object> operationParameters = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : requestInfoProperties.entrySet()) {
            java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey());
            if ((propertyCategory != null) && propertyCategory.equals("parameters")) {
                java.lang.String name = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey());
                java.lang.Object value = entry.getValue();
                operationParameters.put(name, value == null ? null : value.toString());
            }
        }
        return operationParameters;
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getConfigurationResources(java.util.Set<java.lang.String> requestedIds, java.util.Map<java.lang.String, java.lang.Object> propertyMap) throws org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.lang.String serviceName = getStringProperty(propertyMap, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_SERVICE_NAME_PROPERTY_ID);
        java.lang.String componentName = getStringProperty(propertyMap, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_COMPONENT_NAME_PROPERTY_ID);
        java.lang.String categoryName = getStringProperty(propertyMap, org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationResourceProvider.CONFIGURATION_CATEGORY_PROPERTY_ID);
        org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler handler = rootServiceComponentConfigurationHandlerFactory.getInstance(serviceName, componentName, categoryName);
        if (handler != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> configurations = handler.getComponentConfigurations(categoryName);
            if (configurations == null) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(categoryName);
            } else {
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> entry : configurations.entrySet()) {
                    resources.add(toResource(serviceName, componentName, entry.getKey(), entry.getValue().getProperties(), entry.getValue().getPropertyTypes(), requestedIds));
                }
            }
        }
        return resources;
    }

    private java.lang.String getStringProperty(java.util.Map<java.lang.String, java.lang.Object> propertyMap, java.lang.String propertyId) {
        java.lang.String value = null;
        if (propertyMap != null) {
            java.lang.Object o = propertyMap.get(propertyId);
            if (o instanceof java.lang.String) {
                value = ((java.lang.String) (o));
            }
        }
        return value;
    }

    private class RequestDetails {
        final java.lang.String serviceName;

        final java.lang.String componentName;

        final java.lang.String categoryName;

        final java.util.Map<java.lang.String, java.lang.String> properties;

        private RequestDetails(java.lang.String serviceName, java.lang.String componentName, java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties) {
            this.serviceName = serviceName;
            this.componentName = componentName;
            this.categoryName = categoryName;
            this.properties = properties;
        }
    }
}