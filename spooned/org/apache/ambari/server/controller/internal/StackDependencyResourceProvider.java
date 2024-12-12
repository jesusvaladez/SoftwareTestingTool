package org.apache.ambari.server.controller.internal;
public class StackDependencyResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    protected static final java.lang.String STACK_NAME_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "stack_name");

    protected static final java.lang.String STACK_VERSION_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "stack_version");

    protected static final java.lang.String DEPENDENT_SERVICE_NAME_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "dependent_service_name");

    protected static final java.lang.String DEPENDENT_COMPONENT_NAME_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "dependent_component_name");

    protected static final java.lang.String SERVICE_NAME_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "service_name");

    protected static final java.lang.String COMPONENT_NAME_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "component_name");

    protected static final java.lang.String SCOPE_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "scope");

    protected static final java.lang.String TYPE_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "type");

    protected static final java.lang.String CONDITIONS_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Dependencies", "conditions");

    protected static final java.lang.String AUTO_DEPLOY_ENABLED_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("auto_deploy", "enabled");

    protected static final java.lang.String AUTO_DEPLOY_LOCATION_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("auto_deploy", "location");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponentDependency, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SERVICE_NAME_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SCOPE_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.TYPE_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.CONDITIONS_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.AUTO_DEPLOY_ENABLED_ID, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.AUTO_DEPLOY_LOCATION_ID);

    private static org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    protected StackDependencyResourceProvider() {
        super(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.keyPropertyIds);
    }

    public static void init(org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo) {
        org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.ambariMetaInfo = metaInfo;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProps = getPropertyMaps(predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> properties : requestProps) {
            try {
                resources.addAll(getDependencyResources(properties, getRequestPropertyIds(request, predicate)));
            } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
                if (requestProps.size() == 1) {
                    throw e;
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Stack resources are read only");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Stack resources are read only");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Stack resources are read only");
    }

    private java.util.Collection<org.apache.ambari.server.controller.spi.Resource> getDependencyResources(java.util.Map<java.lang.String, java.lang.Object> properties, java.util.Set<java.lang.String> requestedIds) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        final java.lang.String stackName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID)));
        final java.lang.String version = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID)));
        final java.lang.String dependentService = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID)));
        final java.lang.String dependentComponent = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID)));
        final java.lang.String dependencyName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID)));
        java.util.List<org.apache.ambari.server.state.DependencyInfo> dependencies = new java.util.ArrayList<>();
        if (dependencyName != null) {
            dependencies.add(getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.state.DependencyInfo>() {
                @java.lang.Override
                public org.apache.ambari.server.state.DependencyInfo invoke() throws org.apache.ambari.server.AmbariException {
                    return org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.ambariMetaInfo.getComponentDependency(stackName, version, dependentService, dependentComponent, dependencyName);
                }
            }));
        } else {
            dependencies.addAll(getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.List<org.apache.ambari.server.state.DependencyInfo>>() {
                @java.lang.Override
                public java.util.List<org.apache.ambari.server.state.DependencyInfo> invoke() throws org.apache.ambari.server.AmbariException {
                    return org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.ambariMetaInfo.getComponentDependencies(stackName, version, dependentService, dependentComponent);
                }
            }));
        }
        java.util.Collection<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.DependencyInfo dependency : dependencies) {
            if (dependency != null) {
                resources.add(toResource(dependency, stackName, version, dependentService, dependentComponent, requestedIds));
            }
        }
        return resources;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.state.DependencyInfo dependency, java.lang.String stackName, java.lang.String version, java.lang.String dependentService, java.lang.String dependentComponent, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponentDependency);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SERVICE_NAME_ID, dependency.getServiceName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID, dependency.getComponentName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID, stackName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID, version, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID, dependentService, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID, dependentComponent, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SCOPE_ID, dependency.getScope(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.TYPE_ID, dependency.getType(), requestedIds);
        org.apache.ambari.server.state.AutoDeployInfo autoDeployInfo = dependency.getAutoDeploy();
        if (autoDeployInfo != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.AUTO_DEPLOY_ENABLED_ID, autoDeployInfo.isEnabled(), requestedIds);
            if (autoDeployInfo.getCoLocate() != null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.AUTO_DEPLOY_LOCATION_ID, autoDeployInfo.getCoLocate(), requestedIds);
            }
        }
        java.util.List<org.apache.ambari.server.state.DependencyConditionInfo> dependencyConditionsInfo = dependency.getDependencyConditions();
        if (dependencyConditionsInfo != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.CONDITIONS_ID, dependencyConditionsInfo, requestedIds);
        }
        return resource;
    }
}