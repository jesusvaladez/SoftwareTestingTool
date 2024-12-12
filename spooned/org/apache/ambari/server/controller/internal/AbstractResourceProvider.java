package org.apache.ambari.server.controller.internal;
public abstract class AbstractResourceProvider extends org.apache.ambari.server.controller.internal.BaseProvider implements org.apache.ambari.server.controller.spi.ResourceProvider , org.apache.ambari.server.controller.internal.ObservableResourceProvider {
    protected final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds;

    private final java.util.Set<org.apache.ambari.server.controller.internal.ResourceProviderObserver> observers = new java.util.HashSet<>();

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AbstractResourceProvider.class);

    protected static final java.lang.String PROPERTIES_ATTRIBUTES_REGEX = "properties_attributes/[a-zA-Z][a-zA-Z._-]*$";

    private static final java.util.regex.Pattern propertiesAttributesPattern = java.util.regex.Pattern.compile(".*/" + org.apache.ambari.server.controller.internal.AbstractResourceProvider.PROPERTIES_ATTRIBUTES_REGEX);

    protected AbstractResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
        super(propertyIds);
        this.keyPropertyIds = keyPropertyIds;
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return keyPropertyIds;
    }

    @java.lang.Override
    public void updateObservers(org.apache.ambari.server.controller.internal.ResourceProviderEvent event) {
        for (org.apache.ambari.server.controller.internal.ResourceProviderObserver observer : observers) {
            observer.update(event);
        }
    }

    @java.lang.Override
    public void addObserver(org.apache.ambari.server.controller.internal.ResourceProviderObserver observer) {
        observers.add(observer);
    }

    protected abstract java.util.Set<java.lang.String> getPKPropertyIds();

    protected void notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request) {
        updateObservers(new org.apache.ambari.server.controller.internal.ResourceProviderEvent(type, org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, request, null));
    }

    protected void notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        updateObservers(new org.apache.ambari.server.controller.internal.ResourceProviderEvent(type, org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Update, request, predicate));
    }

    protected void notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.Predicate predicate) {
        updateObservers(new org.apache.ambari.server.controller.internal.ResourceProviderEvent(type, org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, null, predicate));
    }

    protected java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getPropertyMaps(org.apache.ambari.server.controller.spi.Predicate givenPredicate) {
        org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor visitor = new org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor(this);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(givenPredicate, visitor);
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> predicates = visitor.getSimplifiedPredicates();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            propertyMaps.add(org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate));
        }
        return propertyMaps;
    }

    protected java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getPropertyMaps(java.util.Map<java.lang.String, java.lang.Object> requestPropertyMap, org.apache.ambari.server.controller.spi.Predicate givenPredicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = new java.util.HashSet<>();
        if (specifiesUniqueResource(givenPredicate)) {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>(org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(givenPredicate));
            propertyMap.putAll(requestPropertyMap);
            propertyMaps.add(propertyMap);
        } else {
            for (org.apache.ambari.server.controller.spi.Resource resource : getResources(givenPredicate)) {
                java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>(org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource));
                propertyMap.putAll(requestPropertyMap);
                propertyMaps.add(propertyMap);
            }
        }
        return propertyMaps;
    }

    protected org.apache.ambari.server.controller.spi.RequestStatus getRequestStatus(org.apache.ambari.server.controller.RequestStatusResponse response, java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources) {
        return getRequestStatus(response, associatedResources, null);
    }

    protected org.apache.ambari.server.controller.spi.RequestStatus getRequestStatus(org.apache.ambari.server.controller.RequestStatusResponse response, java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources, org.apache.ambari.server.controller.spi.RequestStatusMetaData requestStatusMetaData) {
        if (response != null) {
            org.apache.ambari.server.controller.spi.Resource requestResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Request);
            if (response.getMessage() != null) {
                requestResource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "message"), response.getMessage());
            }
            requestResource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id"), response.getRequestId());
            requestResource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status"), "Accepted");
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(requestResource, associatedResources, requestStatusMetaData);
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null, associatedResources, requestStatusMetaData);
    }

    protected org.apache.ambari.server.controller.spi.RequestStatus getRequestStatus(org.apache.ambari.server.controller.RequestStatusResponse response) {
        return getRequestStatus(response, null);
    }

    protected static java.lang.Object getQueryParameterValue(java.lang.String queryParameterId, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.lang.Object result = null;
        if (predicate instanceof org.apache.ambari.server.controller.predicate.EqualsPredicate) {
            org.apache.ambari.server.controller.predicate.EqualsPredicate equalsPredicate = ((org.apache.ambari.server.controller.predicate.EqualsPredicate) (predicate));
            if (queryParameterId.equals(equalsPredicate.getPropertyId())) {
                return equalsPredicate.getValue();
            }
        } else if (predicate instanceof org.apache.ambari.server.controller.predicate.ArrayPredicate) {
            org.apache.ambari.server.controller.predicate.ArrayPredicate arrayPredicate = ((org.apache.ambari.server.controller.predicate.ArrayPredicate) (predicate));
            for (org.apache.ambari.server.controller.spi.Predicate predicateItem : arrayPredicate.getPredicates()) {
                result = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(queryParameterId, predicateItem);
                if (result != null) {
                    return result;
                }
            }
        }
        return result;
    }

    protected <T> T createResources(org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<T> command) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        try {
            return invokeWithRetry(command);
        } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
        } catch (org.apache.ambari.server.DuplicateResourceException e) {
            throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(e.getMessage());
        } catch (org.apache.ambari.server.AmbariException e) {
            if (org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.isErrorEnabled()) {
                org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.error("Caught AmbariException when creating a resource", e);
            }
            throw new org.apache.ambari.server.controller.spi.SystemException("An internal system exception occurred: " + e.getMessage(), e);
        }
    }

    protected <T> T getResources(org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<T> command) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        try {
            return command.invoke();
        } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: " + e.getMessage(), e);
        } catch (org.apache.ambari.server.AmbariException e) {
            if (org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.isErrorEnabled()) {
                org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.error("Caught AmbariException when getting a resource", e);
            }
            throw new org.apache.ambari.server.controller.spi.SystemException("An internal system exception occurred: " + e.getMessage(), e);
        }
    }

    protected <T> T modifyResources(org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<T> command) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        try {
            return invokeWithRetry(command);
        } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The specified resource doesn't exist: " + e.getMessage(), e);
        } catch (org.apache.ambari.server.AmbariException e) {
            if (org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.isErrorEnabled()) {
                org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.error("Caught AmbariException when modifying a resource", e);
            }
            throw new org.apache.ambari.server.controller.spi.SystemException("An internal system exception occurred: " + e.getMessage(), e);
        }
    }

    public static java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> getConfigurationRequests(java.lang.String parentCategory, java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> configs = new java.util.LinkedList<>();
        java.lang.String desiredConfigKey = parentCategory + "/desired_config";
        if (properties.containsKey(desiredConfigKey) && (properties.get(desiredConfigKey) instanceof java.util.Set)) {
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configProperties = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get(desiredConfigKey)));
            for (java.util.Map<java.lang.String, java.lang.Object> value : configProperties) {
                org.apache.ambari.server.controller.ConfigurationRequest newConfig = new org.apache.ambari.server.controller.ConfigurationRequest();
                for (java.util.Map.Entry<java.lang.String, java.lang.Object> e : value.entrySet()) {
                    java.lang.String propName = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName((desiredConfigKey + '/') + e.getKey());
                    java.lang.String absCatategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory((desiredConfigKey + '/') + e.getKey());
                    org.apache.ambari.server.controller.internal.AbstractResourceProvider.parseProperties(newConfig, absCatategory, propName, e.getValue() == null ? null : e.getValue().toString());
                }
                configs.add(newConfig);
            }
            return configs;
        }
        org.apache.ambari.server.controller.ConfigurationRequest config = null;
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String absCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey());
            java.lang.String propName = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey());
            if ((absCategory != null) && absCategory.startsWith(desiredConfigKey)) {
                config = (null == config) ? new org.apache.ambari.server.controller.ConfigurationRequest() : config;
                if (entry.getValue() != null) {
                    org.apache.ambari.server.controller.internal.AbstractResourceProvider.parseProperties(config, absCategory, propName, entry.getValue().toString());
                }
            }
        }
        if (config != null) {
            configs.add(config);
        }
        return configs;
    }

    public static void parseProperties(org.apache.ambari.server.controller.ConfigurationRequest config, java.lang.String absCategory, java.lang.String propName, java.lang.String propValue) {
        if (propName.equals("type"))
            config.setType(propValue);
        else if (propName.equals("tag"))
            config.setVersionTag(propValue);
        else if (propName.equals("selected")) {
            config.setSelected(java.lang.Boolean.parseBoolean(propValue));
        } else if (propName.equals("service_config_version_note")) {
            config.setServiceConfigVersionNote(propValue);
        } else if (absCategory.endsWith("/properties")) {
            config.getProperties().put(propName, propValue);
        } else if (org.apache.ambari.server.controller.internal.AbstractResourceProvider.propertiesAttributesPattern.matcher(absCategory).matches()) {
            java.lang.String attributeName = absCategory.substring(absCategory.lastIndexOf('/') + 1);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributesMap = config.getPropertiesAttributes();
            if (null == configAttributesMap) {
                configAttributesMap = new java.util.HashMap<>();
                config.setPropertiesAttributes(configAttributesMap);
            }
            java.util.Map<java.lang.String, java.lang.String> attributesMap = configAttributesMap.get(attributeName);
            if (null == attributesMap) {
                attributesMap = new java.util.HashMap<>();
                configAttributesMap.put(attributeName, attributesMap);
            }
            attributesMap.put(propName, propValue);
        }
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Predicate givenPredicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(getPKPropertyIds()), givenPredicate);
    }

    private boolean specifiesUniqueResource(org.apache.ambari.server.controller.spi.Predicate predicate) {
        org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor visitor = new org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor(this);
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> predicates = visitor.getSimplifiedPredicates();
        return (predicates.size() == 1) && org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate).containsAll(getPKPropertyIds());
    }

    private <T> T invokeWithRetry(org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<T> command) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
        int retryAttempts = org.apache.ambari.server.utils.RetryHelper.getOperationsRetryAttempts();
        do {
            try {
                return command.invoke();
            } catch (java.lang.Exception e) {
                if (org.apache.ambari.server.utils.RetryHelper.isDatabaseException(e)) {
                    org.apache.ambari.server.utils.RetryHelper.invalidateAffectedClusters();
                    if (retryAttempts > 0) {
                        org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.error("Ignoring database exception to perform operation retry, attempts remaining: " + retryAttempts, e);
                        retryAttempts--;
                    } else {
                        org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
                        throw e;
                    }
                } else {
                    org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
                    throw e;
                }
            }
        } while (true );
    }

    protected interface Command<T> {
        T invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;
    }
}