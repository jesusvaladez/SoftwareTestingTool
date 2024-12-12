package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class UserAuthenticationSourceResourceProviderTest extends org.easymock.EasyMockSupport {
    private static final long CREATE_TIME = java.util.Calendar.getInstance().getTime().getTime();

    private static final long UPDATE_TIME = java.util.Calendar.getInstance().getTime().getTime();

    @org.junit.Before
    public void resetMocks() {
        resetAll();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResources_Administrator() throws java.lang.Exception {
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_NonAdministrator() throws java.lang.Exception {
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test
    public void testGetResources_NonAdministrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    @org.junit.Test
    public void testGetResource_Administrator_Self() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testGetResource_Administrator_Other() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test
    public void testGetResource_NonAdministrator_Self() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResource_NonAdministrator_Other() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    @org.junit.Test
    public void testUpdateResources_SetPassword_Administrator_Self() throws java.lang.Exception {
        updateResources_SetAuthenticationKey(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100", null);
    }

    @org.junit.Test
    public void testUpdateResources_SetPassword_Administrator_Other() throws java.lang.Exception {
        updateResources_SetAuthenticationKey(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100", null);
    }

    @org.junit.Test
    public void testUpdateResources_SetPassword_NonAdministrator_Self() throws java.lang.Exception {
        updateResources_SetAuthenticationKey(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1", null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_SetPassword_NonAdministrator_Other() throws java.lang.Exception {
        updateResources_SetAuthenticationKey(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100", null);
    }

    @org.junit.Test
    public void testUpdateResources_SetPassword_VerifyLocal_Success() throws java.lang.Exception {
        updateResources_SetAuthenticationKey(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), "User100", "local");
    }

    @org.junit.Test(expected = org.apache.velocity.exception.ResourceNotFoundException.class)
    public void testUpdateResources_SetPassword_VerifyLocal_Fail() throws java.lang.Exception {
        updateResources_SetAuthenticationKey(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), "User100", "KERBEROS");
    }

    @org.junit.Test
    public void testDeleteResource_Administrator_Self() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testDeleteResource_Administrator_Other() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User100");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResource_NonAdministrator_Self() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResource_NonAdministrator_Other() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User100");
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder(UserAuthenticationSourceResourceProviderTest.this).addAmbariMetaInfoBinding().addLdapBindings().build().configure(binder());
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.security.authorization.Users.class).toInstance(createMock(org.apache.ambari.server.security.authorization.Users.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
            }
        });
    }

    private void createResourcesTest(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity100 = createNiceMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        org.apache.ambari.server.orm.entities.UserEntity userEntity200 = createNiceMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity("User100")).andReturn(userEntity100).once();
        EasyMock.expect(users.getUserEntity("User200")).andReturn(userEntity200).once();
        users.addAuthentication(userEntity100, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL, "my_password_100_1234");
        EasyMock.expectLastCall().once();
        users.addAuthentication(userEntity200, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL, "my_password_200_1234");
        EasyMock.expectLastCall().once();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties;
        properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID, "User100");
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID, "local");
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID, "my_password_100_1234");
        propertySet.add(properties);
        properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID, "User200");
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID, "local");
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID, "my_password_200_1234");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        verifyAll();
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.UserAuthenticationEntity> entities = new java.util.HashMap<>();
        entities.put("User1", createMockUserAuthenticationEntity("User1"));
        if ("admin".equals(authentication.getName())) {
            entities.put("User10", createMockUserAuthenticationEntity("User10"));
            entities.put("User100", createMockUserAuthenticationEntity("User100"));
            entities.put("admin", createMockUserAuthenticationEntity("admin"));
            EasyMock.expect(users.getUserAuthenticationEntities(((java.lang.String) (null)), null)).andReturn(entities.values()).once();
        } else {
            EasyMock.expect(users.getUserAuthenticationEntities("user1", null)).andReturn(entities.values()).once();
        }
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_CREATED_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_UPDATED_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(entities.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String userName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertTrue(entities.containsKey(userName));
            org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID));
        }
        verifyAll();
    }

    private void getResourceTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> entities = new java.util.ArrayList<>();
        entities.add(createMockUserAuthenticationEntity(requestedUsername));
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserAuthenticationEntities(requestedUsername, null)).andReturn(entities).once();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, createPredicate(requestedUsername, null));
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String userName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals(requestedUsername, userName);
            org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID));
        }
        verifyAll();
    }

    private void updateResources_SetAuthenticationKey(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername, java.lang.String authenticationType) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = createMockUserAuthenticationEntity(requestedUsername);
        boolean isSelf = authentication.getName().equalsIgnoreCase(requestedUsername);
        java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> userAuthenticationEntities = new java.util.ArrayList<>();
        userAuthenticationEntities.add(userAuthenticationEntity);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getAuthenticationEntities()).andReturn(userAuthenticationEntities).once();
        if (isSelf) {
            EasyMock.expect(userEntity.getUserId()).andReturn(((org.apache.ambari.server.security.authentication.AmbariUserDetails) (authentication.getPrincipal())).getUserId()).once();
        } else {
            EasyMock.expect(userEntity.getUserId()).andReturn(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedId() + 100).once();
        }
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(requestedUsername)).andReturn(userEntity).once();
        users.modifyAuthentication(userAuthenticationEntity, "old_password", "new_password", isSelf);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_OLD_KEY_PROPERTY_ID, "old_password");
        properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID, "new_password");
        if (authenticationType != null) {
            properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID, authenticationType);
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        provider.updateResources(request, createPredicate(requestedUsername, userAuthenticationEntity.getUserAuthenticationId()));
        verifyAll();
    }

    private void deleteResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.removeAuthentication(requestedUsername, 1L);
        EasyMock.expectLastCall().atLeastOnce();
        replayAll();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariMetaInfo.init();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), createPredicate(requestedUsername, 1L));
        verifyAll();
    }

    private org.apache.ambari.server.controller.spi.Predicate createPredicate(java.lang.String requestedUsername, java.lang.Long sourceId) {
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID).equals(requestedUsername).toPredicate();
        if (sourceId == null) {
            return predicate1;
        } else {
            org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID).equals(sourceId.toString()).toPredicate();
            return new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2);
        }
    }

    private org.apache.ambari.server.orm.entities.UserAuthenticationEntity createMockUserAuthenticationEntity(java.lang.String username) {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = createMock(org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(entity.getAuthenticationType()).andReturn(org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL).anyTimes();
        EasyMock.expect(entity.getAuthenticationKey()).andReturn("this is a secret").anyTimes();
        EasyMock.expect(entity.getCreateTime()).andReturn(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProviderTest.CREATE_TIME).anyTimes();
        EasyMock.expect(entity.getUpdateTime()).andReturn(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProviderTest.UPDATE_TIME).anyTimes();
        EasyMock.expect(entity.getUserAuthenticationId()).andReturn(100L).anyTimes();
        EasyMock.expect(entity.getUser()).andReturn(userEntity).anyTimes();
        EasyMock.expect(userEntity.getUserName()).andReturn(username).anyTimes();
        return entity;
    }

    private org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(com.google.inject.Injector injector) {
        org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider();
        injector.injectMembers(resourceProvider);
        return resourceProvider;
    }
}