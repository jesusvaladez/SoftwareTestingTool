package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class MemberResourceProviderTest {
    @org.junit.Before
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResources_Administrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_ClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Member;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        EasyMock.expect(resourceProviderFactory.getMemberResourceProvider(EasyMock.eq(managementController))).andReturn(new org.apache.ambari.server.controller.internal.MemberResourceProvider(managementController)).anyTimes();
        managementController.createMembers(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getMemberRequestSet("engineering", "joe"));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(managementController, response, resourceProviderFactory);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID, "engineering");
        properties.put(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_USER_NAME_PROPERTY_ID, "joe");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_ClusterAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    private void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Member;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        EasyMock.expect(resourceProviderFactory.getMemberResourceProvider(EasyMock.eq(managementController))).andReturn(new org.apache.ambari.server.controller.internal.MemberResourceProvider(managementController)).anyTimes();
        EasyMock.expect(managementController.getMembers(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getMemberRequestSet(null, null))).andReturn(java.util.Collections.emptySet()).atLeastOnce();
        EasyMock.replay(managementController, response, resourceProviderFactory);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(null, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.GroupResourceProvider.GROUP_GROUPNAME_PROPERTY_ID).equals("engineering").toPredicate();
        provider.getResources(request, predicate);
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testUpdateResources_Administrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_ClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Member;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        EasyMock.expect(resourceProviderFactory.getMemberResourceProvider(EasyMock.eq(managementController))).andReturn(new org.apache.ambari.server.controller.internal.MemberResourceProvider(managementController)).anyTimes();
        managementController.updateMembers(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getMemberRequestSet("engineering", "joe"));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(managementController, response, resourceProviderFactory);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID, "engineering");
        properties.put(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_USER_NAME_PROPERTY_ID, "joe");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.utilities.PredicateBuilder builder = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        builder.property(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID).equals("engineering");
        org.apache.ambari.server.controller.spi.Predicate predicate = builder.toPredicate();
        provider.updateResources(request, predicate);
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testDeleteResources_Administrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_ClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Member;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        EasyMock.expect(resourceProviderFactory.getMemberResourceProvider(EasyMock.eq(managementController))).andReturn(new org.apache.ambari.server.controller.internal.MemberResourceProvider(managementController)).anyTimes();
        managementController.deleteMembers(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getMemberRequestSet("engineering", null));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(managementController, response, resourceProviderFactory);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.utilities.PredicateBuilder builder = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        builder.property(org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID).equals("engineering");
        org.apache.ambari.server.controller.spi.Predicate predicate = builder.toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        EasyMock.verify(managementController, response);
    }
}