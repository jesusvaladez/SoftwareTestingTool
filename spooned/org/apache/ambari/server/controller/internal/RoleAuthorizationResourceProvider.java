package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class RoleAuthorizationResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.class);

    public static final java.lang.String AUTHORIZATION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "authorization_id");

    public static final java.lang.String PERMISSION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "permission_id");

    public static final java.lang.String AUTHORIZATION_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "authorization_name");

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS;

    static {
        java.util.Set<java.lang.String> set;
        set = new java.util.HashSet<>();
        set.add(AUTHORIZATION_ID_PROPERTY_ID);
        set.add(PERMISSION_ID_PROPERTY_ID);
        PK_PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        set = new java.util.HashSet<>();
        set.add(AUTHORIZATION_ID_PROPERTY_ID);
        set.add(PERMISSION_ID_PROPERTY_ID);
        set.add(AUTHORIZATION_NAME_PROPERTY_ID);
        PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.Permission, PERMISSION_ID_PROPERTY_ID);
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, AUTHORIZATION_ID_PROPERTY_ID);
        KEY_PROPERTY_IDS = java.util.Collections.unmodifiableMap(map);
    }

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RoleAuthorizationDAO roleAuthorizationDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    public RoleAuthorizationResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.KEY_PROPERTY_IDS, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps;
        if (predicate == null) {
            propertyMaps = java.util.Collections.singleton(java.util.Collections.<java.lang.String, java.lang.Object>emptyMap());
        } else {
            propertyMaps = getPropertyMaps(predicate);
        }
        if (propertyMaps != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                java.lang.Object object = propertyMap.get(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID);
                java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizationEntities;
                java.lang.Integer permissionId;
                if (object instanceof java.lang.String) {
                    try {
                        permissionId = java.lang.Integer.valueOf(((java.lang.String) (object)));
                    } catch (java.lang.NumberFormatException e) {
                        org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.LOG.warn(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID + " is not a valid integer value", e);
                        throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Authorization not found, " + predicate, e);
                    }
                } else if (object instanceof java.lang.Number) {
                    permissionId = ((java.lang.Number) (object)).intValue();
                } else {
                    permissionId = null;
                }
                if (permissionId == null) {
                    authorizationEntities = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.roleAuthorizationDAO.findAll();
                } else {
                    org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.permissionDAO.findById(permissionId);
                    if (permissionEntity == null) {
                        authorizationEntities = null;
                    } else {
                        authorizationEntities = permissionEntity.getAuthorizations();
                    }
                }
                if (authorizationEntities != null) {
                    java.lang.String authorizationId = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID)));
                    if (!org.apache.commons.lang.StringUtils.isEmpty(authorizationId)) {
                        java.util.Iterator<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> iterator = authorizationEntities.iterator();
                        while (iterator.hasNext()) {
                            if (!authorizationId.equals(iterator.next().getAuthorizationId())) {
                                iterator.remove();
                            }
                        } 
                    }
                    for (org.apache.ambari.server.orm.entities.RoleAuthorizationEntity entity : authorizationEntities) {
                        resources.add(toResource(permissionId, entity, requestedIds));
                    }
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PK_PROPERTY_IDS;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(java.lang.Integer permissionId, org.apache.ambari.server.orm.entities.RoleAuthorizationEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID, entity.getAuthorizationId(), requestedIds);
        if (permissionId != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.PERMISSION_ID_PROPERTY_ID, permissionId, requestedIds);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID, entity.getAuthorizationName(), requestedIds);
        return resource;
    }
}