package org.apache.ambari.server.controller.internal;
public class ViewVersionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    public static final java.lang.String VIEW_VERSION_INFO = "ViewVersionInfo";

    public static final java.lang.String VIEW_NAME_PROPERTY_ID = "view_name";

    public static final java.lang.String VERSION_PROPERTY_ID = "version";

    public static final java.lang.String BUILD_NUMBER_PROPERTY_ID = "build_number";

    public static final java.lang.String LABEL_PROPERTY_ID = "label";

    public static final java.lang.String DESCRIPTION_PROPERTY_ID = "description";

    public static final java.lang.String MIN_AMBARI_VERSION_PROPERTY_ID = "min_ambari_version";

    public static final java.lang.String MAX_AMBARI_VERSION_PROPERTY_ID = "max_ambari_version";

    public static final java.lang.String PARAMETERS_PROPERTY_ID = "parameters";

    public static final java.lang.String ARCHIVE_PROPERTY_ID = "archive";

    public static final java.lang.String MASKER_CLASS_PROPERTY_ID = "masker_class";

    public static final java.lang.String STATUS_PROPERTY_ID = "status";

    public static final java.lang.String STATUS_DETAIL_PROPERTY_ID = "status_detail";

    public static final java.lang.String CLUSTER_CONFIGURABLE_PROPERTY_ID = "cluster_configurable";

    public static final java.lang.String SYSTEM_PROPERTY_ID = "system";

    public static final java.lang.String VIEW_NAME = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_NAME_PROPERTY_ID;

    public static final java.lang.String VERSION = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VERSION_PROPERTY_ID;

    public static final java.lang.String BUILD_NUMBER = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.BUILD_NUMBER_PROPERTY_ID;

    public static final java.lang.String LABEL = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.LABEL_PROPERTY_ID;

    public static final java.lang.String DESCRIPTION = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.DESCRIPTION_PROPERTY_ID;

    public static final java.lang.String MIN_AMBARI_VERSION = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MIN_AMBARI_VERSION_PROPERTY_ID;

    public static final java.lang.String MAX_AMBARI_VERSION = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MAX_AMBARI_VERSION_PROPERTY_ID;

    public static final java.lang.String PARAMETERS = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.PARAMETERS_PROPERTY_ID;

    public static final java.lang.String ARCHIVE = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.ARCHIVE_PROPERTY_ID;

    public static final java.lang.String MASKER_CLASS = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MASKER_CLASS_PROPERTY_ID;

    public static final java.lang.String STATUS = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS_PROPERTY_ID;

    public static final java.lang.String STATUS_DETAIL = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS_DETAIL_PROPERTY_ID;

    public static final java.lang.String CLUSTER_CONFIGURABLE = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.CLUSTER_CONFIGURABLE_PROPERTY_ID;

    public static final java.lang.String SYSTEM = (org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.SYSTEM_PROPERTY_ID;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VERSION).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_NAME, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VERSION, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.BUILD_NUMBER, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.LABEL, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.DESCRIPTION, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MIN_AMBARI_VERSION, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MAX_AMBARI_VERSION, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.PARAMETERS, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.ARCHIVE, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MASKER_CLASS, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS_DETAIL, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.CLUSTER_CONFIGURABLE, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.SYSTEM);

    public ViewVersionResourceProvider() {
        super(org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.keyPropertyIds);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported.");
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
            java.lang.String viewName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_NAME)));
            java.lang.String viewVersion = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VERSION)));
            for (org.apache.ambari.server.orm.entities.ViewEntity viewDefinition : viewRegistry.getDefinitions()) {
                if ((viewName == null) || viewName.equals(viewDefinition.getCommonName())) {
                    if ((viewVersion == null) || viewVersion.equals(viewDefinition.getVersion())) {
                        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion);
                        org.apache.ambari.server.controller.ViewVersionResponse viewVersionResponse = getResponse(viewDefinition);
                        org.apache.ambari.server.controller.ViewVersionResponse.ViewVersionInfo viewVersionInfo = viewVersionResponse.getViewVersionInfo();
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_NAME, viewVersionInfo.getViewName(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VERSION, viewVersionInfo.getVersion(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.BUILD_NUMBER, viewVersionInfo.getBuildNumber(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.LABEL, viewVersionInfo.getLabel(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.DESCRIPTION, viewVersionInfo.getDescription(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MIN_AMBARI_VERSION, viewVersionInfo.getMinAmbariVersion(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MAX_AMBARI_VERSION, viewVersionInfo.getMaxAmbariVersion(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.PARAMETERS, viewVersionInfo.getParameters(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.ARCHIVE, viewVersionInfo.getArchive(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MASKER_CLASS, viewVersionInfo.getMaskerClass(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS, viewVersionInfo.getStatus().toString(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS_DETAIL, viewVersionInfo.getStatusDetail(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.CLUSTER_CONFIGURABLE, viewVersionInfo.isClusterConfigurable(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.SYSTEM, viewVersionInfo.isSystem(), requestedIds);
                        resources.add(resource);
                    }
                }
            }
        }
        return resources;
    }

    public org.apache.ambari.server.controller.ViewVersionResponse getResponse(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition) {
        java.lang.String archive = viewDefinition.getArchive();
        java.lang.String buildNumber = viewDefinition.getBuild();
        boolean clusterConfigurable = viewDefinition.isClusterConfigurable();
        java.lang.String description = viewDefinition.getDescription();
        java.lang.String label = viewDefinition.getLabel();
        java.lang.String maskerClass = viewDefinition.getMask();
        java.lang.String maxAmbariVersion = viewDefinition.getConfiguration().getMaxAmbariVersion();
        java.lang.String minAmbariVersion = viewDefinition.getConfiguration().getMinAmbariVersion();
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters = viewDefinition.getConfiguration().getParameters();
        org.apache.ambari.view.ViewDefinition.ViewStatus status = viewDefinition.getStatus();
        java.lang.String statusDetail = viewDefinition.getStatusDetail();
        boolean system = viewDefinition.isSystem();
        java.lang.String version = viewDefinition.getVersion();
        java.lang.String viewName = viewDefinition.getCommonName();
        org.apache.ambari.server.controller.ViewVersionResponse.ViewVersionInfo viewVersionInfo = new org.apache.ambari.server.controller.ViewVersionResponse.ViewVersionInfo(archive, buildNumber, clusterConfigurable, description, label, maskerClass, maxAmbariVersion, minAmbariVersion, parameters, status, statusDetail, system, version, viewName);
        return new org.apache.ambari.server.controller.ViewVersionResponse(viewVersionInfo);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.keyPropertyIds.values());
    }
}