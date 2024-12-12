package org.apache.ambari.server.controller.internal;
public class PermissionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private static org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    public static final java.lang.String PERMISSION_ID_PROPERTY_ID = "PermissionInfo/permission_id";

    public static final java.lang.String PERMISSION_NAME_PROPERTY_ID = "PermissionInfo/permission_name";

    public static final java.lang.String PERMISSION_LABEL_PROPERTY_ID = "PermissionInfo/permission_label";

    public static final java.lang.String RESOURCE_NAME_PROPERTY_ID = "PermissionInfo/resource_name";

    public static final java.lang.String SORT_ORDER_PROPERTY_ID = "PermissionInfo/sort_order";

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Permission, org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_ID_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_LABEL_PROPERTY_ID, org.apache.ambari.server.controller.internal.PermissionResourceProvider.RESOURCE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.PermissionResourceProvider.SORT_ORDER_PROPERTY_ID);

    public PermissionResourceProvider() {
        super(org.apache.ambari.server.controller.internal.PermissionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.PermissionResourceProvider.keyPropertyIds);
    }

    public static void init(org.apache.ambari.server.orm.dao.PermissionDAO dao) {
        org.apache.ambari.server.controller.internal.PermissionResourceProvider.permissionDAO = dao;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        for (org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity : org.apache.ambari.server.controller.internal.PermissionResourceProvider.permissionDAO.findAll()) {
            resources.add(toResource(permissionEntity, requestedIds));
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
        return org.apache.ambari.server.controller.internal.PermissionResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.PermissionResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.PermissionEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Permission);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_ID_PROPERTY_ID, entity.getId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_NAME_PROPERTY_ID, entity.getPermissionName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PermissionResourceProvider.PERMISSION_LABEL_PROPERTY_ID, entity.getPermissionLabel(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PermissionResourceProvider.RESOURCE_NAME_PROPERTY_ID, entity.getResourceType().getName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PermissionResourceProvider.SORT_ORDER_PROPERTY_ID, entity.getSortOrder(), requestedIds);
        return resource;
    }
}