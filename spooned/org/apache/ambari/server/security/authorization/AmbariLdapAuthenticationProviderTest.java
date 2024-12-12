package org.apache.ambari.server.security.authorization;
import org.easymock.EasyMockSupport;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class AmbariLdapAuthenticationProviderTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String ALLOWED_USER_NAME = "allowedUser";

    private static final java.lang.String ALLOWED_USER_DN = "uid=alloweduser,ou=people,dc=ambari,dc=apache,dc=org";

    @org.junit.Test(expected = org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException.class)
    public void testBadCredential() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_NAME, "password");
        org.apache.ambari.server.configuration.Configuration configuration = createMockConfiguration(org.apache.ambari.server.security.ClientSecurityType.LDAP);
        org.apache.ambari.server.security.authorization.Users users = createMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider ldapConfigurationProvider = createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class);
        org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator authoritiesPopulator = createMock(org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.class);
        org.springframework.security.ldap.authentication.LdapAuthenticationProvider ldapAuthenticationProvider = createMock(org.springframework.security.ldap.authentication.LdapAuthenticationProvider.class);
        EasyMock.expect(ldapAuthenticationProvider.authenticate(authentication)).andThrow(new org.springframework.security.authentication.BadCredentialsException("")).once();
        org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider authenticationProvider = createMockBuilder(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.class).withConstructor(users, configuration, ldapConfigurationProvider, authoritiesPopulator).addMockedMethod("loadLdapAuthenticationProvider").createMock();
        EasyMock.expect(authenticationProvider.loadLdapAuthenticationProvider(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_NAME)).andReturn(ldapAuthenticationProvider).once();
        replayAll();
        authenticationProvider.authenticate(authentication);
    }

    @org.junit.Test
    public void testAuthenticate() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_NAME, "password");
        org.springframework.security.ldap.userdetails.LdapUserDetails ldapUserDetails = createMock(org.springframework.security.ldap.userdetails.LdapUserDetails.class);
        EasyMock.expect(ldapUserDetails.getDn()).andReturn(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_DN).atLeastOnce();
        org.springframework.security.core.Authentication authenticatedAuthentication = createMock(org.springframework.security.core.Authentication.class);
        EasyMock.expect(authenticatedAuthentication.getPrincipal()).andReturn(ldapUserDetails).atLeastOnce();
        org.apache.ambari.server.configuration.Configuration configuration = createMockConfiguration(org.apache.ambari.server.security.ClientSecurityType.LDAP);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = createMock(org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
        EasyMock.expect(userAuthenticationEntity.getUser()).andReturn(userEntity).atLeastOnce();
        org.apache.ambari.server.security.authorization.User user = createMock(org.apache.ambari.server.security.authorization.User.class);
        org.apache.ambari.server.security.authorization.Users users = createMock(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUserAuthenticationEntities(org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP, org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_DN)).andReturn(java.util.Collections.singleton(userAuthenticationEntity)).atLeastOnce();
        users.validateLogin(userEntity, org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_NAME);
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.expect(users.getUser(userEntity)).andReturn(user).atLeastOnce();
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(java.util.Collections.emptyList()).atLeastOnce();
        org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider ldapConfigurationProvider = createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class);
        org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator authoritiesPopulator = createMock(org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.class);
        org.springframework.security.ldap.authentication.LdapAuthenticationProvider ldapAuthenticationProvider = createMock(org.springframework.security.ldap.authentication.LdapAuthenticationProvider.class);
        EasyMock.expect(ldapAuthenticationProvider.authenticate(authentication)).andReturn(authenticatedAuthentication).once();
        org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider authenticationProvider = createMockBuilder(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.class).withConstructor(users, configuration, ldapConfigurationProvider, authoritiesPopulator).addMockedMethod("loadLdapAuthenticationProvider").createMock();
        EasyMock.expect(authenticationProvider.loadLdapAuthenticationProvider(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_NAME)).andReturn(ldapAuthenticationProvider).once();
        replayAll();
        org.springframework.security.core.Authentication result = authenticationProvider.authenticate(authentication);
        org.junit.Assert.assertTrue(result instanceof org.apache.ambari.server.security.authentication.AmbariUserAuthentication);
        org.junit.Assert.assertTrue(result.isAuthenticated());
        verifyAll();
    }

    @org.junit.Test
    public void testDisabled() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProviderTest.ALLOWED_USER_NAME, "password");
        org.apache.ambari.server.configuration.Configuration configuration = createMockConfiguration(org.apache.ambari.server.security.ClientSecurityType.LOCAL);
        org.apache.ambari.server.security.authorization.Users users = createMock(org.apache.ambari.server.security.authorization.Users.class);
        org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider ldapConfigurationProvider = createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class);
        org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator authoritiesPopulator = createMock(org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator.class);
        org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider authenticationProvider = createMockBuilder(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.class).withConstructor(users, configuration, ldapConfigurationProvider, authoritiesPopulator).addMockedMethod("loadLdapAuthenticationProvider").createMock();
        replayAll();
        org.springframework.security.core.Authentication result = authenticationProvider.authenticate(authentication);
        org.junit.Assert.assertNull(result);
        verifyAll();
    }

    private org.apache.ambari.server.configuration.Configuration createMockConfiguration(org.apache.ambari.server.security.ClientSecurityType clientSecurityType) {
        org.apache.ambari.server.configuration.Configuration configuration = createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getClientSecurityType()).andReturn(clientSecurityType).atLeastOnce();
        return configuration;
    }
}