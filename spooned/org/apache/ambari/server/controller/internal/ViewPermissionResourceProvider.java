package org.apache.ambari.server.controller.internal;
public class ViewPermissionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private static org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    public static final java.lang.String PERMISSION_INFO = "PermissionInfo";

    public static final java.lang.String VIEW_NAME_PROPERTY_ID = "view_name";

    public static final java.lang.String VERSION_PROPERTY_ID = "version";

    public static final java.lang.String PERMISSION_ID_PROPERTY_ID = "permission_id";

    public static final java.lang.String PERMISSION_NAME_PROPERTY_ID = "permission_name";

    public static final java.lang.String RESOURCE_NAME_PROPERTY_ID = "resource_name";

    public static final java.lang.String VIEW_NAME = (org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VIEW_NAME_PROPERTY_ID;

    public static final java.lang.String VERSION = (org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VERSION_PROPERTY_ID;

    public static final java.lang.String PERMISSION_ID = (org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_ID_PROPERTY_ID;

    public static final java.lang.String PERMISSION_NAME = (org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_NAME_PROPERTY_ID;

    public static final java.lang.String RESOURCE_NAME = (org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.RESOURCE_NAME_PROPERTY_ID;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VIEW_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VERSION).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewPermission, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VIEW_NAME, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VERSION, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_ID, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_NAME, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.RESOURCE_NAME);

    public ViewPermissionResourceProvider() {
        super(org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.keyPropertyIds);
    }

    public static void init(org.apache.ambari.server.orm.dao.PermissionDAO dao) {
        org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.permissionDAO = dao;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        org.apache.ambari.server.orm.entities.PermissionEntity viewUsePermission = org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.permissionDAO.findViewUsePermission();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.Object viewName = propertyMap.get(org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VIEW_NAME);
            java.lang.Object viewVersion = propertyMap.get(org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VERSION);
            if ((viewName != null) && (viewVersion != null)) {
                org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewRegistry.getDefinition(viewName.toString(), viewVersion.toString());
                if (viewEntity.isDeployed()) {
                    org.apache.ambari.server.controller.ViewPermissionResponse viewPermissionResponse = getResponse(viewUsePermission, viewEntity.getResourceType(), viewEntity);
                    resources.add(toResource(viewPermissionResponse, requestedIds));
                }
            }
        }
        for (org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity : org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.permissionDAO.findAll()) {
            org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType = permissionEntity.getResourceType();
            org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewRegistry.getDefinition(resourceType);
            if ((viewEntity != null) && viewEntity.isDeployed()) {
                org.apache.ambari.server.controller.ViewPermissionResponse viewPermissionResponse = getResponse(permissionEntity, resourceType, viewEntity);
                resources.add(toResource(viewPermissionResponse, requestedIds));
            }
        }
        return resources;
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
        return org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.controller.ViewPermissionResponse getResponse(org.apache.ambari.server.orm.entities.PermissionEntity entity, org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType, org.apache.ambari.server.orm.entities.ViewEntity viewEntity) {
        java.lang.String viewName = viewEntity.getCommonName();
        java.lang.String version = viewEntity.getVersion();
        java.lang.Integer permissionId = entity.getId();
        java.lang.String permissionName = entity.getPermissionName();
        java.lang.String resourceName = resourceType.getName();
        org.apache.ambari.server.controller.ViewPermissionResponse.ViewPermissionInfo viewPermissionInfo = new org.apache.ambari.server.controller.ViewPermissionResponse.ViewPermissionInfo(viewName, version, permissionId, permissionName, resourceName);
        return new org.apache.ambari.server.controller.ViewPermissionResponse(viewPermissionInfo);
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.controller.ViewPermissionResponse viewPermissionResponse, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ViewPermission);
        org.apache.ambari.server.controller.ViewPermissionResponse.ViewPermissionInfo viewPermissionInfo = viewPermissionResponse.getViewPermissionInfo();
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VIEW_NAME, viewPermissionInfo.getViewName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VERSION, viewPermissionInfo.getVersion(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_ID, viewPermissionInfo.getPermissionId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_NAME, viewPermissionInfo.getPermissionName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.RESOURCE_NAME, viewPermissionInfo.getResourceName(), requestedIds);
        return resource;
    }
}