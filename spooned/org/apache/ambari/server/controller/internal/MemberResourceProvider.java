package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
public class MemberResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.MemberResourceProvider.class);

    public static final java.lang.String MEMBER_GROUP_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("MemberInfo", "group_name");

    public static final java.lang.String MEMBER_USER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("MemberInfo", "user_name");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Group, org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Member, org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_USER_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_USER_NAME_PROPERTY_ID);

    @com.google.inject.assistedinject.AssistedInject
    public MemberResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Member, org.apache.ambari.server.controller.internal.MemberResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.MemberResourceProvider.keyPropertyIds, managementController);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> manageUserAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        setRequiredCreateAuthorizations(manageUserAuthorizations);
        setRequiredGetAuthorizations(manageUserAuthorizations);
        setRequiredUpdateAuthorizations(manageUserAuthorizations);
        setRequiredDeleteAuthorizations(manageUserAuthorizations);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                getManagementController().createMembers(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.MemberResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.MemberResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.MemberResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getMembers(requests);
            }
        });
        org.apache.ambari.server.controller.internal.MemberResourceProvider.LOG.debug("Found member responses matching get members request, membersRequestSize={}, membersResponseSize={}", requests.size(), responses.size());
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.MemberResponse memberResponse : responses) {
            org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Member);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID, memberResponse.getGroupName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_USER_NAME_PROPERTY_ID, memberResponse.getUserName(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        if (requests.isEmpty()) {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>();
            propertyMap.put(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID, predicate));
            requests.add(getRequest(propertyMap));
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                getManagementController().updateMembers(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            final org.apache.ambari.server.controller.MemberRequest req = getRequest(propertyMap);
            requests.add(req);
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                getManagementController().deleteMembers(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.MemberResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.controller.MemberRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        if (properties == null) {
            return new org.apache.ambari.server.controller.MemberRequest(null, null);
        }
        final org.apache.ambari.server.controller.MemberRequest request = new org.apache.ambari.server.controller.MemberRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_USER_NAME_PROPERTY_ID))));
        return request;
    }
}