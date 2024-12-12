package org.apache.ambari.server.security.authentication;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
public abstract class AbstractAuthenticationProviderTest extends org.easymock.EasyMockSupport {
    static final java.lang.String TEST_USER_NAME = "userName";

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.After
    public void cleanUp() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testAuthenticationSuccess() {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = getUserEntity(injector, org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME, 9, true);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).atLeastOnce();
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(null).atLeastOnce();
        org.springframework.security.core.Authentication authentication = getAuthentication(true, true);
        replayAll();
        org.springframework.security.authentication.AuthenticationProvider provider = getAuthenticationProvider(injector);
        org.springframework.security.core.Authentication result = provider.authenticate(authentication);
        verifyAll();
        org.junit.Assert.assertNotNull(result);
        org.junit.Assert.assertEquals(true, result.isAuthenticated());
        org.junit.Assert.assertTrue(result instanceof org.apache.ambari.server.security.authentication.AmbariUserAuthentication);
        validateAuthenticationResult(((org.apache.ambari.server.security.authentication.AmbariUserAuthentication) (result)));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.AmbariAuthenticationException.class)
    public void testAuthenticationWithIncorrectUserName() {
        com.google.inject.Injector injector = getInjector();
        org.springframework.security.core.Authentication authentication = getAuthentication(false, true);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(EasyMock.anyString())).andReturn(null).atLeastOnce();
        replayAll();
        org.springframework.security.authentication.AuthenticationProvider provider = getAuthenticationProvider(injector);
        provider.authenticate(authentication);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.AmbariAuthenticationException.class)
    public void testAuthenticationWithoutCredentials() {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = getUserEntity(injector, org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME, 0, true);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).atLeastOnce();
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(null).atLeastOnce();
        org.springframework.security.core.Authentication authentication = createMock(org.springframework.security.core.Authentication.class);
        EasyMock.expect(authentication.getName()).andReturn(org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME).atLeastOnce();
        EasyMock.expect(authentication.getCredentials()).andReturn(null).atLeastOnce();
        replayAll();
        org.springframework.security.authentication.AuthenticationProvider provider = getAuthenticationProvider(injector);
        provider.authenticate(authentication);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.AmbariAuthenticationException.class)
    public void testAuthenticationWithIncorrectCredential() {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = getUserEntity(injector, org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME, 0, true);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).atLeastOnce();
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(null).atLeastOnce();
        org.springframework.security.core.Authentication authentication = getAuthentication(true, false);
        replayAll();
        org.springframework.security.authentication.AuthenticationProvider provider = getAuthenticationProvider(injector);
        provider.authenticate(authentication);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.TooManyLoginFailuresException.class)
    public void testUserIsLockedOutAfterConsecutiveFailures() {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = getUserEntity(injector, org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME, 11, true);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).atLeastOnce();
        org.springframework.security.core.Authentication authentication = getAuthentication(true, true);
        replayAll();
        org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider ambariLocalAuthenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.class);
        ambariLocalAuthenticationProvider.authenticate(authentication);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.AccountDisabledException.class)
    public void testUserIsInactive() {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = getUserEntity(injector, org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME, 10, false);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.AbstractAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).atLeastOnce();
        org.springframework.security.core.Authentication authentication = getAuthentication(true, true);
        replayAll();
        org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider ambariLocalAuthenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.class);
        ambariLocalAuthenticationProvider.authenticate(authentication);
    }

    protected com.google.inject.Injector getInjector() {
        final org.apache.ambari.server.security.authorization.Users users = createMockBuilder(org.apache.ambari.server.security.authorization.Users.class).addMockedMethod("getUserEntity", java.lang.String.class).addMockedMethod("getUserAuthorities", org.apache.ambari.server.orm.entities.UserEntity.class).createMock();
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.configuration.Configuration configuration = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
                EasyMock.expect(configuration.getMaxAuthenticationFailures()).andReturn(10).anyTimes();
                EasyMock.expect(configuration.showLockedOutUserMessage()).andReturn(true).anyTimes();
                bind(javax.persistence.EntityManager.class).toInstance(createMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
                bind(org.apache.ambari.server.security.authorization.Users.class).toInstance(users);
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            }
        }, getAdditionalModule());
    }

    protected abstract org.springframework.security.authentication.AuthenticationProvider getAuthenticationProvider(com.google.inject.Injector injector);

    protected abstract org.springframework.security.core.Authentication getAuthentication(boolean correctUsername, boolean correctCredential);

    protected abstract org.apache.ambari.server.orm.entities.UserEntity getUserEntity(com.google.inject.Injector injector, java.lang.String username, int consecutiveFailures, boolean active);

    protected abstract com.google.inject.Module getAdditionalModule();

    protected abstract void validateAuthenticationResult(org.apache.ambari.server.security.authentication.AmbariUserAuthentication result);
}