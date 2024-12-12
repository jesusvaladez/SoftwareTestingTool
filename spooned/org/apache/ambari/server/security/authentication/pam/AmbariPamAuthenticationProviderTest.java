package org.apache.ambari.server.security.authentication.pam;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import org.jvnet.libpam.PAM;
import org.jvnet.libpam.PAMException;
import org.jvnet.libpam.UnixUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class AmbariPamAuthenticationProviderTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String TEST_USER_NAME = "userName";

    private static final java.lang.String TEST_USER_PASS = "userPass";

    private static final java.lang.String TEST_USER_INCORRECT_PASS = "userIncorrectPass";

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() {
        final org.apache.ambari.server.security.authorization.Users users = createMockBuilder(org.apache.ambari.server.security.authorization.Users.class).addMockedMethod("getUserEntity", java.lang.String.class).addMockedMethod("getUserAuthorities", org.apache.ambari.server.orm.entities.UserEntity.class).addMockedMethod("createUser", java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class).addMockedMethod("addPamAuthentication", org.apache.ambari.server.orm.entities.UserEntity.class, java.lang.String.class).addMockedMethod("getUser", org.apache.ambari.server.orm.entities.UserEntity.class).createMock();
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_SECURITY.getKey(), org.apache.ambari.server.security.ClientSecurityType.PAM.name());
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.PAM_CONFIGURATION_FILE.getKey(), "ambari-pam");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHOW_LOCKED_OUT_USER_MESSAGE.getKey(), "true");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.MAX_LOCAL_AUTHENTICATION_FAILURES.getKey(), "10");
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createNiceMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createNiceMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class).toInstance(createMock(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                bind(org.apache.ambari.server.security.authorization.Users.class).toInstance(users);
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
            }
        });
    }

    @org.junit.Test(expected = org.springframework.security.core.AuthenticationException.class)
    public void testBadCredential() throws java.lang.Exception {
        org.jvnet.libpam.PAM pam = createMock(org.jvnet.libpam.PAM.class);
        EasyMock.expect(pam.authenticate(EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME), EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_INCORRECT_PASS))).andThrow(new org.jvnet.libpam.PAMException()).once();
        pam.dispose();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory pamAuthenticationFactory = injector.getInstance(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class);
        EasyMock.expect(pamAuthenticationFactory.createInstance(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class))).andReturn(pam).once();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME)).andReturn(null).once();
        replayAll();
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_INCORRECT_PASS);
        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider authenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);
        authenticationProvider.authenticate(authentication);
        verifyAll();
    }

    @org.junit.Test
    public void testAuthenticateExistingUser() throws java.lang.Exception {
        org.jvnet.libpam.UnixUser unixUser = createNiceMock(org.jvnet.libpam.UnixUser.class);
        org.jvnet.libpam.PAM pam = createMock(org.jvnet.libpam.PAM.class);
        EasyMock.expect(pam.authenticate(EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME), EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS))).andReturn(unixUser).once();
        pam.dispose();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory pamAuthenticationFactory = injector.getInstance(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class);
        EasyMock.expect(pamAuthenticationFactory.createInstance(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class))).andReturn(pam).once();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = combineUserEntity(true, true, 0);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).once();
        EasyMock.expect(users.getUser(userEntity)).andReturn(new org.apache.ambari.server.security.authorization.User(userEntity)).once();
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(null).once();
        replayAll();
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS);
        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider authenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);
        org.springframework.security.core.Authentication result = authenticationProvider.authenticate(authentication);
        junit.framework.Assert.assertNotNull(result);
        junit.framework.Assert.assertEquals(true, result.isAuthenticated());
        junit.framework.Assert.assertTrue(result instanceof org.apache.ambari.server.security.authentication.AmbariUserAuthentication);
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.AccountDisabledException.class)
    public void testAuthenticateDisabledUser() throws java.lang.Exception {
        org.jvnet.libpam.UnixUser unixUser = createNiceMock(org.jvnet.libpam.UnixUser.class);
        org.jvnet.libpam.PAM pam = createMock(org.jvnet.libpam.PAM.class);
        EasyMock.expect(pam.authenticate(EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME), EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS))).andReturn(unixUser).once();
        pam.dispose();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory pamAuthenticationFactory = injector.getInstance(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class);
        EasyMock.expect(pamAuthenticationFactory.createInstance(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class))).andReturn(pam).once();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = combineUserEntity(true, false, 0);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).once();
        replayAll();
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS);
        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider authenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);
        authenticationProvider.authenticate(authentication);
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.TooManyLoginFailuresException.class)
    public void testAuthenticateLockedUser() throws java.lang.Exception {
        org.jvnet.libpam.UnixUser unixUser = createNiceMock(org.jvnet.libpam.UnixUser.class);
        org.jvnet.libpam.PAM pam = createMock(org.jvnet.libpam.PAM.class);
        EasyMock.expect(pam.authenticate(EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME), EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS))).andReturn(unixUser).once();
        pam.dispose();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory pamAuthenticationFactory = injector.getInstance(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class);
        EasyMock.expect(pamAuthenticationFactory.createInstance(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class))).andReturn(pam).once();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = combineUserEntity(true, true, 11);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME)).andReturn(userEntity).once();
        replayAll();
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS);
        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider authenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);
        authenticationProvider.authenticate(authentication);
        verifyAll();
    }

    @org.junit.Test
    public void testAuthenticateNewUser() throws java.lang.Exception {
        org.jvnet.libpam.UnixUser unixUser = createNiceMock(org.jvnet.libpam.UnixUser.class);
        EasyMock.expect(unixUser.getUserName()).andReturn(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME.toLowerCase()).atLeastOnce();
        org.jvnet.libpam.PAM pam = createMock(org.jvnet.libpam.PAM.class);
        EasyMock.expect(pam.authenticate(EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME), EasyMock.eq(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS))).andReturn(unixUser).once();
        pam.dispose();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory pamAuthenticationFactory = injector.getInstance(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class);
        EasyMock.expect(pamAuthenticationFactory.createInstance(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class))).andReturn(pam).once();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = combineUserEntity(false, true, 0);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME)).andReturn(null).once();
        EasyMock.expect(users.createUser(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME.toLowerCase(), org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, true)).andReturn(userEntity).once();
        users.addPamAuthentication(userEntity, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME.toLowerCase());
        EasyMock.expectLastCall().once();
        EasyMock.expect(users.getUser(userEntity)).andReturn(new org.apache.ambari.server.security.authorization.User(userEntity)).once();
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(null).once();
        replayAll();
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS);
        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider authenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);
        org.springframework.security.core.Authentication result = authenticationProvider.authenticate(authentication);
        junit.framework.Assert.assertNotNull(result);
        junit.framework.Assert.assertEquals(true, result.isAuthenticated());
        junit.framework.Assert.assertTrue(result instanceof org.apache.ambari.server.security.authentication.AmbariUserAuthentication);
        verifyAll();
    }

    @org.junit.Test
    public void testDisabled() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        configuration.setClientSecurityType(org.apache.ambari.server.security.ClientSecurityType.LOCAL);
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME, org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_PASS);
        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider authenticationProvider = injector.getInstance(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);
        org.springframework.security.core.Authentication auth = authenticationProvider.authenticate(authentication);
        junit.framework.Assert.assertTrue(auth == null);
    }

    private org.apache.ambari.server.orm.entities.UserEntity combineUserEntity(boolean addAuthentication, java.lang.Boolean active, java.lang.Integer consecutiveFailures) {
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserId(1);
        userEntity.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME).toString());
        userEntity.setLocalUsername(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME);
        userEntity.setPrincipal(principalEntity);
        userEntity.setActive(active);
        userEntity.setConsecutiveFailures(consecutiveFailures);
        if (addAuthentication) {
            org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = new org.apache.ambari.server.orm.entities.UserAuthenticationEntity();
            userAuthenticationEntity.setAuthenticationType(org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM);
            userAuthenticationEntity.setAuthenticationKey(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProviderTest.TEST_USER_NAME);
            userEntity.setAuthenticationEntities(java.util.Collections.singletonList(userAuthenticationEntity));
        }
        return userEntity;
    }
}