package org.apache.ambari.server.controller.internal;
public class ThemeArtifactResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.class);

    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ThemeInfo", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ThemeInfo", "stack_version");

    public static final java.lang.String STACK_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ThemeInfo", "service_name");

    public static final java.lang.String THEME_FILE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ThemeInfo", "file_name");

    public static final java.lang.String THEME_DEFAULT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ThemeInfo", "default");

    public static final java.lang.String THEME_DATA_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ThemeInfo", "theme_data");

    public static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_FILE_NAME_PROPERTY_ID).build();

    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Theme, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_FILE_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).build();

    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_FILE_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_DEFAULT_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_DATA_PROPERTY_ID).build();

    protected ThemeArtifactResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Theme, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Creating of themes is not supported");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.LinkedHashSet<>();
        resources.addAll(getThemes(request, predicate));
        if (resources.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Themes not found, " + predicate);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Updating of themes is not supported");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Deleting of themes is not supported");
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getThemes(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.LinkedHashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : getPropertyMaps(predicate)) {
            java.lang.String themeFileName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_FILE_NAME_PROPERTY_ID)));
            java.lang.String stackName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_NAME_PROPERTY_ID)));
            java.lang.String stackVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_VERSION_PROPERTY_ID)));
            java.lang.String stackService = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID)));
            org.apache.ambari.server.state.StackInfo stackInfo;
            try {
                stackInfo = getManagementController().getAmbariMetaInfo().getStack(stackName, stackVersion);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Parent stack resource doesn't exist: stackName='%s', stackVersion='%s'", stackName, stackVersion));
            }
            java.util.List<org.apache.ambari.server.state.ServiceInfo> serviceInfoList = new java.util.ArrayList<>();
            if (stackService == null) {
                serviceInfoList.addAll(stackInfo.getServices());
            } else {
                org.apache.ambari.server.state.ServiceInfo service = stackInfo.getService(stackService);
                if (service == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Parent stack/service resource doesn't exist: stackName='%s', stackVersion='%s', serviceName='%s'", stackName, stackVersion, stackService));
                }
                serviceInfoList.add(service);
            }
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : serviceInfoList) {
                java.util.List<org.apache.ambari.server.state.ThemeInfo> serviceThemes = new java.util.ArrayList<>();
                if (themeFileName != null) {
                    org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.LOG.debug("Getting themes from service {}, themes = {}", serviceInfo.getName(), serviceInfo.getThemesMap());
                    serviceThemes.add(serviceInfo.getThemesMap().get(themeFileName));
                } else {
                    for (org.apache.ambari.server.state.ThemeInfo themeInfo : serviceInfo.getThemesMap().values()) {
                        if (themeInfo.getIsDefault()) {
                            serviceThemes.add(0, themeInfo);
                        } else {
                            serviceThemes.add(themeInfo);
                        }
                    }
                }
                java.util.List<org.apache.ambari.server.controller.spi.Resource> serviceResources = new java.util.ArrayList<>();
                for (org.apache.ambari.server.state.ThemeInfo themeInfo : serviceThemes) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Theme);
                    java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_FILE_NAME_PROPERTY_ID, themeInfo.getFileName(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_DEFAULT_PROPERTY_ID, themeInfo.getIsDefault(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.THEME_DATA_PROPERTY_ID, themeInfo.getThemeMap(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_NAME_PROPERTY_ID, stackName, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_VERSION_PROPERTY_ID, stackVersion, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID, serviceInfo.getName(), requestedIds);
                    serviceResources.add(resource);
                }
                resources.addAll(serviceResources);
            }
        }
        return resources;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return null;
    }
}