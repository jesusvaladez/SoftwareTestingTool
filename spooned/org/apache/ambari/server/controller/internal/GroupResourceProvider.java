package org.apache.ambari.server.controller.internal;
public class GroupResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.GroupResourceProvider.class);

    public static final java.lang.String GROUP_GROUPNAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Groups", "group_name");

    public static final java.lang.String GROUP_LDAP_GROUP_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Groups", "ldap_group");

    public static final java.lang.String GROUP_GROUPTYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Groups", "group_type");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Group, org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_GROUPNAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_GROUPNAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_LDAP_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_GROUPTYPE_PROPERTY_ID);

    GroupResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Group, org.apache.ambari.server.controller.internal.GroupResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.GroupResourceProvider.keyPropertyIds, managementController);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> manageUserAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        setRequiredCreateAuthorizations(manageUserAuthorizations);
        setRequiredGetAuthorizations(manageUserAuthorizations);
        setRequiredUpdateAuthorizations(manageUserAuthorizations);
        setRequiredDeleteAuthorizations(manageUserAuthorizations);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                getManagementController().createGroups(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(null));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<org.apache.ambari.server.controller.GroupResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.GroupResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.GroupResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getGroups(requests);
            }
        });
        org.apache.ambari.server.controller.internal.GroupResourceProvider.LOG.debug("Found group responses matching get group request, groupRequestSize={}, groupResponseSize={}", requests.size(), responses.size());
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.GroupResponse groupResponse : responses) {
            org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Group);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_GROUPNAME_PROPERTY_ID, groupResponse.getGroupName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_LDAP_GROUP_PROPERTY_ID, groupResponse.isLdapGroup(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_GROUPTYPE_PROPERTY_ID, groupResponse.getGroupType(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(request.getProperties().iterator().next(), predicate)) {
            final org.apache.ambari.server.controller.GroupRequest req = getRequest(propertyMap);
            requests.add(req);
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                getManagementController().updateGroups(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            final org.apache.ambari.server.controller.GroupRequest req = getRequest(propertyMap);
            requests.add(req);
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                getManagementController().deleteGroups(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.GroupResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.controller.GroupRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        if (properties == null) {
            return new org.apache.ambari.server.controller.GroupRequest(null);
        }
        final org.apache.ambari.server.controller.GroupRequest request = new org.apache.ambari.server.controller.GroupRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_GROUPNAME_PROPERTY_ID))));
        return request;
    }
}