package org.apache.ambari.server.controller.internal;
public class QuickLinkArtifactResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.class);

    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("QuickLinkInfo", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("QuickLinkInfo", "stack_version");

    public static final java.lang.String STACK_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("QuickLinkInfo", "service_name");

    public static final java.lang.String QUICKLINK_DEFAULT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("QuickLinkInfo", "default");

    public static final java.lang.String QUICKLINK_FILE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("QuickLinkInfo", "file_name");

    public static final java.lang.String QUICKLINK_DATA_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("QuickLinkInfo", "quicklink_data");

    public static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_FILE_NAME_PROPERTY_ID).build();

    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.QuickLink, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_FILE_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).build();

    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_FILE_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_DATA_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_DEFAULT_PROPERTY_ID).build();

    protected QuickLinkArtifactResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.QuickLink, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Creating of quick links is not supported");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.LinkedHashSet<>();
        resources.addAll(getQuickLinks(request, predicate));
        if (resources.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: QuickLink not found, " + predicate);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Updating of quick links is not supported");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Deleting of quick links is not supported");
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getQuickLinks(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.LinkedHashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : getPropertyMaps(predicate)) {
            java.lang.String quickLinksFileName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_FILE_NAME_PROPERTY_ID)));
            java.lang.String stackName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID)));
            java.lang.String stackVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID)));
            java.lang.String stackService = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID)));
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
                java.util.List<org.apache.ambari.server.state.QuickLinksConfigurationInfo> serviceQuickLinks = new java.util.ArrayList<>();
                if (quickLinksFileName != null) {
                    org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.LOG.debug("Getting quick links from service {}, quick links = {}", serviceInfo.getName(), serviceInfo.getQuickLinksConfigurationsMap());
                    serviceQuickLinks.add(serviceInfo.getQuickLinksConfigurationsMap().get(quickLinksFileName));
                } else {
                    for (org.apache.ambari.server.state.QuickLinksConfigurationInfo quickLinksConfigurationInfo : serviceInfo.getQuickLinksConfigurationsMap().values()) {
                        if (quickLinksConfigurationInfo.getIsDefault()) {
                            serviceQuickLinks.add(0, quickLinksConfigurationInfo);
                        } else {
                            serviceQuickLinks.add(quickLinksConfigurationInfo);
                        }
                    }
                }
                setVisibilityAndOverrides(serviceInfo.getName(), serviceQuickLinks);
                java.util.List<org.apache.ambari.server.controller.spi.Resource> serviceResources = new java.util.ArrayList<>();
                for (org.apache.ambari.server.state.QuickLinksConfigurationInfo quickLinksConfigurationInfo : serviceQuickLinks) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.QuickLink);
                    java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_FILE_NAME_PROPERTY_ID, quickLinksConfigurationInfo.getFileName(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_DATA_PROPERTY_ID, quickLinksConfigurationInfo.getQuickLinksConfigurationMap(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.QUICKLINK_DEFAULT_PROPERTY_ID, quickLinksConfigurationInfo.getIsDefault(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_NAME_PROPERTY_ID, stackName, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_VERSION_PROPERTY_ID, stackVersion, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID, serviceInfo.getName(), requestedIds);
                    serviceResources.add(resource);
                }
                resources.addAll(serviceResources);
            }
        }
        return resources;
    }

    private void setVisibilityAndOverrides(java.lang.String serviceName, java.util.List<org.apache.ambari.server.state.QuickLinksConfigurationInfo> serviceQuickLinks) {
        org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityController visibilityController = getManagementController().getQuicklinkVisibilityController();
        for (org.apache.ambari.server.state.QuickLinksConfigurationInfo configurationInfo : serviceQuickLinks) {
            for (org.apache.ambari.server.state.quicklinks.QuickLinks links : configurationInfo.getQuickLinksConfigurationMap().values()) {
                for (org.apache.ambari.server.state.quicklinks.Link link : links.getQuickLinksConfiguration().getLinks()) {
                    link.setVisible(visibilityController.isVisible(serviceName, link));
                    visibilityController.getUrlOverride(serviceName, link).ifPresent(link::setUrl);
                }
            }
        }
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return null;
    }
}