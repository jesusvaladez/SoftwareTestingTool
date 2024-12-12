package org.apache.ambari.server.view;
public class ViewSubResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewSubResourceProvider.class);

    private static final java.lang.String VIEW_NAME_PROPERTY_ID = "view_name";

    private static final java.lang.String VIEW_VERSION_PROPERTY_ID = "version";

    private static final java.lang.String INSTANCE_NAME_PROPERTY_ID = "instance_name";

    private final org.apache.ambari.server.orm.entities.ViewEntity viewDefinition;

    private final java.lang.String pkField;

    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final java.util.Map<java.lang.String, java.beans.PropertyDescriptor> descriptorMap;

    private final java.util.Set<java.lang.String> pkPropertyIds;

    public ViewSubResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, java.lang.Class<?> clazz, java.lang.String pkField, org.apache.ambari.server.orm.entities.ViewEntity viewDefinition) throws java.beans.IntrospectionException {
        super(org.apache.ambari.server.view.ViewSubResourceProvider.discoverPropertyIds(clazz), org.apache.ambari.server.view.ViewSubResourceProvider.getKeyPropertyIds(pkField, type));
        this.pkField = pkField;
        this.viewDefinition = viewDefinition;
        this.pkPropertyIds = new java.util.HashSet<>(getKeyPropertyIds().values());
        this.type = type;
        this.descriptorMap = org.apache.ambari.server.view.ViewSubResourceProvider.getDescriptorMap(clazz);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = request.getProperties();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : properties) {
            java.lang.String resourceId = ((java.lang.String) (propertyMap.get(pkField)));
            java.lang.String instanceName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.view.ViewSubResourceProvider.INSTANCE_NAME_PROPERTY_ID)));
            try {
                getResourceProvider(instanceName).createResource(resourceId, propertyMap);
            } catch (org.apache.ambari.view.NoSuchResourceException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
            } catch (org.apache.ambari.view.UnsupportedPropertyException e) {
                throw new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(getResourceType(e), e.getPropertyIds());
            } catch (org.apache.ambari.view.ResourceAlreadyExistsException e) {
                throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(e.getMessage());
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.view.ViewSubResourceProvider.LOG.error("Caught exception creating view sub resources.", e);
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions = new java.util.HashSet<>();
        try {
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
            int size = propertyMaps.size();
            java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = viewDefinition.getInstances();
            if (size == 0) {
                instanceDefinitions.addAll(viewInstanceDefinitions);
            } else {
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                    java.lang.String instanceName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.view.ViewSubResourceProvider.INSTANCE_NAME_PROPERTY_ID)));
                    if ((size == 1) && (instanceName != null)) {
                        java.lang.String resourceId = ((java.lang.String) (propertyMap.get(pkField)));
                        if (resourceId != null) {
                            java.lang.Object bean = getResourceProvider(instanceName).getResource(resourceId, requestedIds);
                            return java.util.Collections.singleton(getResource(bean, viewDefinition.getCommonName(), viewDefinition.getVersion(), instanceName, requestedIds));
                        }
                    }
                    if (instanceName == null) {
                        instanceDefinitions.addAll(viewInstanceDefinitions);
                        break;
                    } else {
                        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition = viewDefinition.getInstanceDefinition(instanceName);
                        if (instanceDefinition != null) {
                            instanceDefinitions.add(instanceDefinition);
                        }
                    }
                }
            }
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.HashSet<>();
            org.apache.ambari.view.ReadRequest readRequest = new org.apache.ambari.server.view.ViewSubResourceProvider.ViewReadRequest(request, requestedIds, predicate == null ? "" : predicate.toString());
            for (org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition : instanceDefinitions) {
                java.util.Set<?> beans = instanceDefinition.getResourceProvider(type).getResources(readRequest);
                for (java.lang.Object bean : beans) {
                    org.apache.ambari.server.controller.spi.Resource resource = getResource(bean, viewDefinition.getCommonName(), viewDefinition.getVersion(), instanceDefinition.getName(), requestedIds);
                    if (predicate.evaluate(resource)) {
                        results.add(resource);
                    }
                }
            }
            return results;
        } catch (org.apache.ambari.view.NoSuchResourceException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
        } catch (org.apache.ambari.view.UnsupportedPropertyException e) {
            throw new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(getResourceType(e), e.getPropertyIds());
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.ViewSubResourceProvider.LOG.error("Caught exception getting view sub resources.", e);
            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = iterator.next();
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(request, predicate);
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                java.lang.String resourceId = ((java.lang.String) (resource.getPropertyValue(pkField)));
                java.lang.String instanceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.view.ViewSubResourceProvider.INSTANCE_NAME_PROPERTY_ID)));
                try {
                    getResourceProvider(instanceName).updateResource(resourceId, propertyMap);
                } catch (org.apache.ambari.view.NoSuchResourceException e) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
                } catch (org.apache.ambari.view.UnsupportedPropertyException e) {
                    throw new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(getResourceType(e), e.getPropertyIds());
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.view.ViewSubResourceProvider.LOG.error("Caught exception updating view sub resources.", e);
                    throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
                }
            }
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String resourceId = ((java.lang.String) (resource.getPropertyValue(pkField)));
            java.lang.String instanceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.view.ViewSubResourceProvider.INSTANCE_NAME_PROPERTY_ID)));
            try {
                getResourceProvider(instanceName).deleteResource(resourceId);
            } catch (org.apache.ambari.view.NoSuchResourceException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
            } catch (org.apache.ambari.view.UnsupportedPropertyException e) {
                throw new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(getResourceType(e), e.getPropertyIds());
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.view.ViewSubResourceProvider.LOG.error("Caught exception deleting view sub resources.", e);
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return pkPropertyIds;
    }

    private org.apache.ambari.server.controller.spi.Resource getResource(java.lang.Object bean, java.lang.String viewName, java.lang.String viewVersion, java.lang.String instanceName, java.util.Set<java.lang.String> requestedIds) throws java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(type);
        resource.setProperty(org.apache.ambari.server.view.ViewSubResourceProvider.VIEW_NAME_PROPERTY_ID, viewName);
        resource.setProperty(org.apache.ambari.server.view.ViewSubResourceProvider.VIEW_VERSION_PROPERTY_ID, viewVersion);
        resource.setProperty(org.apache.ambari.server.view.ViewSubResourceProvider.INSTANCE_NAME_PROPERTY_ID, instanceName);
        for (java.util.Map.Entry<java.lang.String, java.beans.PropertyDescriptor> entry : descriptorMap.entrySet()) {
            java.lang.Object value = entry.getValue().getReadMethod().invoke(bean);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, entry.getKey(), value, requestedIds);
        }
        return resource;
    }

    private org.apache.ambari.view.ResourceProvider<?> getResourceProvider(java.lang.String instanceName) {
        return viewDefinition.getInstanceDefinition(instanceName).getResourceProvider(type);
    }

    private org.apache.ambari.server.controller.spi.Resource.Type getResourceType(org.apache.ambari.view.UnsupportedPropertyException e) {
        return org.apache.ambari.server.controller.spi.Resource.Type.valueOf(e.getType());
    }

    private static java.util.Set<java.lang.String> discoverPropertyIds(java.lang.Class<?> clazz) throws java.beans.IntrospectionException {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>(org.apache.ambari.server.view.ViewSubResourceProvider.getDescriptorMap(clazz).keySet());
        propertyIds.add(org.apache.ambari.server.view.ViewSubResourceProvider.INSTANCE_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.view.ViewSubResourceProvider.VIEW_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.view.ViewSubResourceProvider.VIEW_VERSION_PROPERTY_ID);
        return propertyIds;
    }

    private static java.util.Map<java.lang.String, java.beans.PropertyDescriptor> getDescriptorMap(java.lang.Class<?> clazz) throws java.beans.IntrospectionException {
        java.util.Map<java.lang.String, java.beans.PropertyDescriptor> descriptorMap = new java.util.HashMap<>();
        for (java.beans.PropertyDescriptor pd : java.beans.Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
            java.lang.String name = pd.getName();
            if ((pd.getReadMethod() != null) && (!name.equals("class"))) {
                descriptorMap.put(name, pd);
            }
        }
        return descriptorMap;
    }

    private static java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds(java.lang.String pkField, org.apache.ambari.server.controller.spi.Resource.Type type) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<>();
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.view.ViewSubResourceProvider.VIEW_NAME_PROPERTY_ID);
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, org.apache.ambari.server.view.ViewSubResourceProvider.VIEW_VERSION_PROPERTY_ID);
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, org.apache.ambari.server.view.ViewSubResourceProvider.INSTANCE_NAME_PROPERTY_ID);
        keyPropertyIds.put(type, pkField);
        return keyPropertyIds;
    }

    private static class ViewReadRequest implements org.apache.ambari.view.ReadRequest {
        private final org.apache.ambari.server.controller.spi.Request request;

        private final java.util.Set<java.lang.String> propertyIds;

        private final java.lang.String predicate;

        private ViewReadRequest(org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> propertyIds, java.lang.String predicate) {
            this.request = request;
            this.propertyIds = propertyIds;
            this.predicate = predicate;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> getPropertyIds() {
            return propertyIds;
        }

        @java.lang.Override
        public java.lang.String getPredicate() {
            return predicate;
        }

        @java.lang.Override
        public org.apache.ambari.view.ReadRequest.TemporalInfo getTemporalInfo(java.lang.String id) {
            org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = request.getTemporalInfo(id);
            return temporalInfo == null ? null : new org.apache.ambari.server.view.ViewSubResourceProvider.ViewReadRequestTemporalInfo(temporalInfo);
        }
    }

    private static class ViewReadRequestTemporalInfo implements org.apache.ambari.view.ReadRequest.TemporalInfo {
        private final org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo;

        private ViewReadRequestTemporalInfo(org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
            this.temporalInfo = temporalInfo;
        }

        @java.lang.Override
        public java.lang.Long getStartTime() {
            return temporalInfo.getStartTime();
        }

        @java.lang.Override
        public java.lang.Long getEndTime() {
            return temporalInfo.getEndTime();
        }

        @java.lang.Override
        public java.lang.Long getStep() {
            return temporalInfo.getStep();
        }
    }
}