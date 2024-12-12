package org.apache.ambari.server.security.authorization;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMockSupport;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class AmbariLdapBindAuthenticatorTest extends org.easymock.EasyMockSupport {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration;

    @org.junit.Before
    public void init() throws java.lang.Exception {
        injector = createInjector();
        ldapConfiguration = injector.getInstance(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
    }

    @org.junit.Test
    public void testAuthenticateWithoutLogin() throws java.lang.Exception {
        testAuthenticate("username", "username", false);
    }

    @org.junit.Test
    public void testAuthenticateWithNullLDAPUsername() throws java.lang.Exception {
        testAuthenticate("username", null, false);
    }

    @org.junit.Test
    public void testAuthenticateWithLoginAliasDefault() throws java.lang.Exception {
        testAuthenticate("username", "ldapUsername", false);
    }

    @org.junit.Test
    public void testAuthenticateWithLoginAliasForceToLower() throws java.lang.Exception {
        testAuthenticate("username", "ldapUsername", true);
    }

    @org.junit.Test
    public void testAuthenticateBadPassword() throws java.lang.Exception {
        java.lang.String basePathString = "dc=apache,dc=org";
        java.lang.String ldapUserRelativeDNString = java.lang.String.format("uid=%s,ou=people,ou=dev", "ldapUsername");
        javax.naming.ldap.LdapName ldapUserRelativeDN = new javax.naming.ldap.LdapName(ldapUserRelativeDNString);
        java.lang.String ldapUserDNString = java.lang.String.format("%s,%s", ldapUserRelativeDNString, basePathString);
        javax.naming.ldap.LdapName basePath = org.springframework.ldap.support.LdapUtils.newLdapName(basePathString);
        org.springframework.ldap.core.support.LdapContextSource ldapCtxSource = createMock(org.springframework.ldap.core.support.LdapContextSource.class);
        EasyMock.expect(ldapCtxSource.getBaseLdapName()).andReturn(basePath).atLeastOnce();
        EasyMock.expect(ldapCtxSource.getContext(ldapUserDNString, "password")).andThrow(new org.springframework.ldap.AuthenticationException(null)).once();
        org.springframework.ldap.core.DirContextOperations searchedUserContext = createMock(org.springframework.ldap.core.DirContextOperations.class);
        EasyMock.expect(searchedUserContext.getDn()).andReturn(ldapUserRelativeDN).atLeastOnce();
        org.springframework.security.ldap.search.FilterBasedLdapUserSearch userSearch = createMock(org.springframework.security.ldap.search.FilterBasedLdapUserSearch.class);
        EasyMock.expect(userSearch.searchForUser(EasyMock.anyString())).andReturn(searchedUserContext).once();
        setupDatabaseConfigurationExpectations(false, false);
        replayAll();
        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator bindAuthenticator = new org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator(ldapCtxSource, ldapConfiguration);
        bindAuthenticator.setUserSearch(userSearch);
        try {
            bindAuthenticator.authenticate(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("username", "password"));
            org.junit.Assert.fail("Expected thrown exception: org.springframework.security.authentication.BadCredentialsException");
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
        } catch (java.lang.Throwable t) {
            org.junit.Assert.fail("Expected thrown exception: org.springframework.security.authentication.BadCredentialsException\nEncountered thrown exception " + t.getClass().getName());
        }
        verifyAll();
    }

    private void testAuthenticate(java.lang.String ambariUsername, java.lang.String ldapUsername, boolean forceUsernameToLower) throws java.lang.Exception {
        java.lang.String basePathString = "dc=apache,dc=org";
        java.lang.String ldapUserRelativeDNString = java.lang.String.format("uid=%s,ou=people,ou=dev", ldapUsername);
        javax.naming.ldap.LdapName ldapUserRelativeDN = new javax.naming.ldap.LdapName(ldapUserRelativeDNString);
        java.lang.String ldapUserDNString = java.lang.String.format("%s,%s", ldapUserRelativeDNString, basePathString);
        javax.naming.ldap.LdapName basePath = org.springframework.ldap.support.LdapUtils.newLdapName(basePathString);
        @java.lang.SuppressWarnings("unchecked")
        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> adminGroups = createMock(javax.naming.NamingEnumeration.class);
        EasyMock.expect(adminGroups.hasMore()).andReturn(false).atLeastOnce();
        adminGroups.close();
        EasyMock.expectLastCall().atLeastOnce();
        org.springframework.ldap.core.DirContextOperations boundUserContext = createMock(org.springframework.ldap.core.DirContextOperations.class);
        java.lang.System.out.println(ldapUserDNString);
        EasyMock.expect(boundUserContext.search(EasyMock.eq("ou=groups"), EasyMock.eq(("(&(member=" + ldapUserDNString) + ")(objectclass=group)(|(cn=Ambari Administrators)))"), EasyMock.anyObject(javax.naming.directory.SearchControls.class))).andReturn(adminGroups).atLeastOnce();
        boundUserContext.close();
        EasyMock.expectLastCall().atLeastOnce();
        org.springframework.ldap.core.support.LdapContextSource ldapCtxSource = createMock(org.springframework.ldap.core.support.LdapContextSource.class);
        EasyMock.expect(ldapCtxSource.getBaseLdapName()).andReturn(basePath).atLeastOnce();
        EasyMock.expect(ldapCtxSource.getContext(ldapUserDNString, "password")).andReturn(boundUserContext).once();
        EasyMock.expect(ldapCtxSource.getReadOnlyContext()).andReturn(boundUserContext).once();
        javax.naming.directory.Attributes searchedAttributes = new javax.naming.directory.BasicAttributes("uid", ldapUsername);
        org.springframework.ldap.core.DirContextOperations searchedUserContext = createMock(org.springframework.ldap.core.DirContextOperations.class);
        EasyMock.expect(searchedUserContext.getDn()).andReturn(ldapUserRelativeDN).atLeastOnce();
        EasyMock.expect(searchedUserContext.getAttributes()).andReturn(searchedAttributes).atLeastOnce();
        org.springframework.security.ldap.search.FilterBasedLdapUserSearch userSearch = createMock(org.springframework.security.ldap.search.FilterBasedLdapUserSearch.class);
        EasyMock.expect(userSearch.searchForUser(ambariUsername)).andReturn(searchedUserContext).once();
        org.springframework.web.context.request.ServletRequestAttributes servletRequestAttributes = createMock(org.springframework.web.context.request.ServletRequestAttributes.class);
        if ((!org.apache.commons.lang.StringUtils.isEmpty(ldapUsername)) && (!ambariUsername.equals(ldapUsername))) {
            servletRequestAttributes.setAttribute(EasyMock.eq(ambariUsername), EasyMock.eq(forceUsernameToLower ? ldapUsername.toLowerCase() : ldapUsername), EasyMock.eq(org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION));
            EasyMock.expectLastCall().once();
            servletRequestAttributes.setAttribute(EasyMock.eq(forceUsernameToLower ? ldapUsername.toLowerCase() : ldapUsername), EasyMock.eq(ambariUsername), EasyMock.eq(org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION));
            EasyMock.expectLastCall().once();
        }
        setupDatabaseConfigurationExpectations(true, forceUsernameToLower);
        replayAll();
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator bindAuthenticator = new org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator(ldapCtxSource, ldapConfiguration);
        bindAuthenticator.setUserSearch(userSearch);
        org.springframework.ldap.core.DirContextOperations user = bindAuthenticator.authenticate(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(ambariUsername, "password"));
        verifyAll();
        java.lang.String ldapUserNameAttribute = ldapConfiguration.getLdapServerProperties().getUsernameAttribute();
        org.junit.Assert.assertEquals(ldapUsername, user.getStringAttribute(ldapUserNameAttribute));
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class).toInstance(createNiceMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class));
            }
        });
    }

    private void setupDatabaseConfigurationExpectations(boolean expectedDatabaseConfigCall, boolean forceUsernameToLowerCase) {
        final org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticatorTest.getDefaultLdapServerProperties(forceUsernameToLowerCase);
        ldapServerProperties.setGroupObjectClass("group");
        if (expectedDatabaseConfigCall) {
            EasyMock.expect(ldapConfiguration.getLdapServerProperties()).andReturn(ldapServerProperties).anyTimes();
        }
    }

    private static org.apache.ambari.server.security.authorization.LdapServerProperties getDefaultLdapServerProperties(boolean forceUsernameToLowerCase) {
        final org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = new org.apache.ambari.server.security.authorization.LdapServerProperties();
        ldapServerProperties.setPrimaryUrl((org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST.getDefaultValue() + ":") + org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_PORT.getDefaultValue());
        ldapServerProperties.setSecondaryUrl((org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_HOST.getDefaultValue() + ":") + org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_PORT.getDefaultValue());
        ldapServerProperties.setUseSsl(java.lang.Boolean.parseBoolean(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USE_SSL.getDefaultValue()));
        ldapServerProperties.setAnonymousBind(java.lang.Boolean.parseBoolean(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND.getDefaultValue()));
        ldapServerProperties.setManagerDn(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_DN.getDefaultValue());
        ldapServerProperties.setManagerPassword(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD.getDefaultValue());
        ldapServerProperties.setBaseDN(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE.getDefaultValue());
        ldapServerProperties.setUsernameAttribute(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE.getDefaultValue());
        ldapServerProperties.setForceUsernameToLowercase(forceUsernameToLowerCase);
        ldapServerProperties.setUserBase(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_BASE.getDefaultValue());
        ldapServerProperties.setUserObjectClass(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS.getDefaultValue());
        ldapServerProperties.setDnAttribute(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.DN_ATTRIBUTE.getDefaultValue());
        ldapServerProperties.setGroupBase(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_BASE.getDefaultValue());
        ldapServerProperties.setGroupObjectClass(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS.getDefaultValue());
        ldapServerProperties.setGroupMembershipAttr(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE.getDefaultValue());
        ldapServerProperties.setGroupNamingAttr(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE.getDefaultValue());
        ldapServerProperties.setAdminGroupMappingRules(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MAPPING_RULES.getDefaultValue());
        ldapServerProperties.setAdminGroupMappingMemberAttr("");
        ldapServerProperties.setUserSearchFilter(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_FILTER.getDefaultValue());
        ldapServerProperties.setAlternateUserSearchFilter(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_FILTER.getDefaultValue());
        ldapServerProperties.setGroupSearchFilter(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_FILTER.getDefaultValue());
        ldapServerProperties.setReferralMethod(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.REFERRAL_HANDLING.getDefaultValue());
        ldapServerProperties.setSyncUserMemberReplacePattern(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_REPLACE_PATTERN.getDefaultValue());
        ldapServerProperties.setSyncGroupMemberReplacePattern(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_REPLACE_PATTERN.getDefaultValue());
        ldapServerProperties.setSyncUserMemberFilter(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_FILTER.getDefaultValue());
        ldapServerProperties.setSyncGroupMemberFilter(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_FILTER.getDefaultValue());
        ldapServerProperties.setPaginationEnabled(java.lang.Boolean.parseBoolean(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.PAGINATION_ENABLED.getDefaultValue()));
        return ldapServerProperties;
    }
}