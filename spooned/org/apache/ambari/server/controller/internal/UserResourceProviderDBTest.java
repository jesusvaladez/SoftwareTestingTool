package org.apache.ambari.server.controller.internal;
import com.google.inject.persist.PersistService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replay;
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.security.authorization.AuthorizationHelper.class })
public class UserResourceProviderDBTest {
    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.controller.AmbariManagementController amc;

    private static org.apache.ambari.server.controller.spi.Resource.Type userType = org.apache.ambari.server.controller.spi.Resource.Type.User;

    private static org.apache.ambari.server.controller.internal.UserResourceProvider userResourceProvider;

    private static org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider userAuthenticationSourceResourceProvider;

    private static java.lang.String JDBC_IN_MEMORY_URL_CREATE = java.lang.String.format("jdbc:derby:memory:myDB/%s;create=true", org.apache.ambari.server.configuration.Configuration.DEFAULT_DERBY_SCHEMA);

    private static java.lang.String JDBC_IN_MEMORY_URL_DROP = java.lang.String.format("jdbc:derby:memory:myDB/%s;drop=true", org.apache.ambari.server.configuration.Configuration.DEFAULT_DERBY_SCHEMA);

    @org.junit.Before
    public void setupInMemoryDB() {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule testModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        java.util.Properties properties = testModule.getProperties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.JDBC_IN_MEMORY_URL_CREATE);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_DRIVER.getKey(), org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER);
        org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.injector = com.google.inject.Guice.createInjector(testModule);
        org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.injector.getInstance(com.google.inject.persist.PersistService.class).start();
        org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.amc = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider = new org.apache.ambari.server.controller.internal.UserResourceProvider(org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.amc);
        org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.injector.injectMembers(org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider);
        org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userAuthenticationSourceResourceProvider = new org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider();
        org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.injector.injectMembers(org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userAuthenticationSourceResourceProvider);
        org.apache.ambari.server.controller.ResourceProviderFactory factory = createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        EasyMock.expect(factory.getUserAuthenticationSourceResourceProvider()).andReturn(org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userAuthenticationSourceResourceProvider).anyTimes();
        replay(factory);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
    }

    @org.junit.After
    public void teardownInMemoryDB() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        if (org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.injector != null) {
            org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.injector);
        }
    }

    @org.junit.Test
    public void createUserTest() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "viewUser");
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, "password");
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID, false);
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, true);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProperties), null);
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.createResources(request);
        org.junit.Assert.assertNotNull(requestStatus);
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(java.util.Collections.singleton("Users")));
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).equals("viewUser").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.getResources(getRequest, predicate);
        org.junit.Assert.assertEquals(resources.size(), 1);
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        java.lang.String userName = resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).toString();
        org.junit.Assert.assertEquals("viewuser", userName);
        requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.deleteResources(request, predicate);
        org.junit.Assert.assertNotNull(requestStatus);
        resources = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(resources.size(), 0);
    }

    @org.junit.Test
    public void createExistingUserTest() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "abcd");
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, "password");
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID, false);
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, true);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProperties), null);
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.createResources(request);
        org.junit.Assert.assertNotNull(requestStatus);
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "ABCD");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProperties), null);
        try {
            requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.createResources(request);
            org.junit.Assert.assertTrue("Should fail with user exists", false);
        } catch (java.lang.Exception ex) {
            org.junit.Assert.assertTrue(ex.getMessage().contains("already exists"));
        }
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).equals("abcd").toPredicate();
        requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.deleteResources(request, predicate);
        org.junit.Assert.assertNotNull(requestStatus);
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(java.util.Arrays.asList("Users")));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(resources.size(), 0);
    }

    @org.junit.Test
    public void getExistingUser() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, "viewUser");
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, "password");
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID, false);
        requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, true);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProperties), null);
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.createResources(request);
        org.junit.Assert.assertNotNull(requestStatus);
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(java.util.Arrays.asList("Users")));
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).equals("viewuser").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.getResources(getRequest, predicate);
        org.junit.Assert.assertEquals(resources.size(), 1);
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        java.lang.String userName = resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).toString();
        org.junit.Assert.assertEquals("viewuser", userName);
        requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.deleteResources(request, predicate);
        org.junit.Assert.assertNotNull(requestStatus);
        resources = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(resources.size(), 0);
    }

    @org.junit.Test
    public void getAllUserTest() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.List<java.lang.String> userNames = java.util.Arrays.asList("user1", "uSer2", "User3", "useR4");
        java.util.List<java.lang.String> lowercaseUserNames = new java.util.ArrayList<>();
        for (java.lang.String username : userNames) {
            lowercaseUserNames.add(username.toLowerCase());
        }
        for (java.lang.String userName : userNames) {
            java.util.Map<java.lang.String, java.lang.Object> requestProperties = new java.util.HashMap<>();
            requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, userName);
            requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, "password");
            requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID, false);
            requestProperties.put(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, true);
            org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProperties), null);
            org.apache.ambari.server.controller.spi.RequestStatus requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.createResources(request);
            org.junit.Assert.assertNotNull(requestStatus);
        }
        org.apache.ambari.server.controller.spi.Request getRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("Users"));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.getResources(getRequest, null);
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.System.out.println("Resource: " + resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).toString());
        }
        for (java.lang.String s : lowercaseUserNames) {
            java.lang.System.out.println("LC UN: " + s);
        }
        org.junit.Assert.assertEquals(lowercaseUserNames.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String userName = resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).toString();
            org.junit.Assert.assertTrue(lowercaseUserNames.contains(userName));
        }
        for (java.lang.String userName : userNames) {
            org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).equals(userName).toPredicate();
            org.apache.ambari.server.controller.spi.RequestStatus requestStatus = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.deleteResources(null, predicate);
            org.junit.Assert.assertNotNull(requestStatus);
        }
        resources = org.apache.ambari.server.controller.internal.UserResourceProviderDBTest.userResourceProvider.getResources(getRequest, null);
        org.junit.Assert.assertEquals(resources.size(), 0);
    }
}