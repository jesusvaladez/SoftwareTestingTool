package org.apache.ambari.server.api.services.persistence;
public class PersistenceManagerImpl implements org.apache.ambari.server.api.services.persistence.PersistenceManager {
    private org.apache.ambari.server.controller.spi.ClusterController m_controller;

    public PersistenceManagerImpl(org.apache.ambari.server.controller.spi.ClusterController controller) {
        m_controller = controller;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus create(org.apache.ambari.server.api.resources.ResourceInstance resource, org.apache.ambari.server.api.services.RequestBody requestBody) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, null, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA))) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization " + "to create/store user persisted data.");
        }
        if (resource != null) {
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapResourceIds = resource.getKeyValueMap();
            org.apache.ambari.server.controller.spi.Resource.Type type = resource.getResourceDefinition().getType();
            org.apache.ambari.server.controller.spi.Schema schema = m_controller.getSchema(type);
            java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProperties = requestBody.getNamedPropertySets();
            if (setProperties.isEmpty()) {
                requestBody.addPropertySet(new org.apache.ambari.server.api.services.NamedPropertySet("", new java.util.HashMap<>()));
            }
            for (org.apache.ambari.server.api.services.NamedPropertySet propertySet : setProperties) {
                for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> entry : mapResourceIds.entrySet()) {
                    java.util.Map<java.lang.String, java.lang.Object> mapProperties = propertySet.getProperties();
                    java.lang.String property = schema.getKeyPropertyId(entry.getKey());
                    if (!mapProperties.containsKey(property)) {
                        mapProperties.put(property, entry.getValue());
                    }
                }
            }
            return m_controller.createResources(type, createControllerRequest(requestBody));
        } else {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException("Resource is null");
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus update(org.apache.ambari.server.api.resources.ResourceInstance resource, org.apache.ambari.server.api.services.RequestBody requestBody) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, null, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA))) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization " + "to update/store user persisted data.");
        }
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapResourceIds = resource.getKeyValueMap();
        org.apache.ambari.server.controller.spi.Resource.Type type = resource.getResourceDefinition().getType();
        org.apache.ambari.server.controller.spi.Schema schema = m_controller.getSchema(type);
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setProperties = requestBody.getNamedPropertySets();
        for (org.apache.ambari.server.api.services.NamedPropertySet propertySet : setProperties) {
            for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> entry : mapResourceIds.entrySet()) {
                if (entry.getValue() != null) {
                    java.util.Map<java.lang.String, java.lang.Object> mapProperties = propertySet.getProperties();
                    java.lang.String property = schema.getKeyPropertyId(entry.getKey());
                    if (!mapProperties.containsKey(property)) {
                        mapProperties.put(property, entry.getValue());
                    }
                }
            }
        }
        return m_controller.updateResources(type, createControllerRequest(requestBody), resource.getQuery().getPredicate());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus delete(org.apache.ambari.server.api.resources.ResourceInstance resource, org.apache.ambari.server.api.services.RequestBody requestBody) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, null, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_USER_PERSISTED_DATA))) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization " + "to delete/store user persisted data.");
        }
        return m_controller.deleteResources(resource.getResourceDefinition().getType(), createControllerRequest(requestBody), resource.getQuery().getPredicate());
    }

    protected org.apache.ambari.server.controller.spi.Request createControllerRequest(org.apache.ambari.server.api.services.RequestBody body) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(body.getPropertySets(), body.getRequestInfoProperties());
    }
}