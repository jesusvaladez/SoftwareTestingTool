package org.apache.ambari.server.security.authorization;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
public class LdapServerPropertiesTest {
    private final com.google.inject.Injector injector;

    private static final java.lang.String INCORRECT_URL_LIST = "Incorrect LDAP URL list created";

    private static final java.lang.String INCORRECT_USER_SEARCH_FILTER = "Incorrect search filter";

    protected org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties;

    public LdapServerPropertiesTest() {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.security.authorization.AuthorizationTestModule(), new org.apache.ambari.server.ldap.LdapModule());
        injector.injectMembers(this);
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        ldapServerProperties = new org.apache.ambari.server.security.authorization.LdapServerProperties();
        ldapServerProperties.setAnonymousBind(true);
        ldapServerProperties.setBaseDN("dc=ambari,dc=apache,dc=org");
        ldapServerProperties.setManagerDn("uid=manager," + ldapServerProperties.getBaseDN());
        ldapServerProperties.setManagerPassword("password");
        ldapServerProperties.setUseSsl(false);
        ldapServerProperties.setPrimaryUrl("1.2.3.4:389");
        ldapServerProperties.setUsernameAttribute("uid");
        ldapServerProperties.setUserObjectClass("dummyObjectClass");
    }

    @org.junit.Test
    public void testGetLdapUrls() throws java.lang.Exception {
        java.util.List<java.lang.String> urls = ldapServerProperties.getLdapUrls();
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_URL_LIST, 1, urls.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_URL_LIST, "ldap://1.2.3.4:389", urls.get(0));
        ldapServerProperties.setSecondaryUrl("4.3.2.1:1234");
        urls = ldapServerProperties.getLdapUrls();
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_URL_LIST, 2, urls.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_URL_LIST, "ldap://4.3.2.1:1234", urls.get(1));
        ldapServerProperties.setUseSsl(true);
        urls = ldapServerProperties.getLdapUrls();
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_URL_LIST, "ldaps://1.2.3.4:389", urls.get(0));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_URL_LIST, "ldaps://4.3.2.1:1234", urls.get(1));
    }

    @org.junit.Test
    public void testGetUserSearchFilter() throws java.lang.Exception {
        ldapServerProperties.setUserSearchFilter("(&({usernameAttribute}={0})(objectClass={userObjectClass}))");
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_USER_SEARCH_FILTER, "(&(uid={0})(objectClass=dummyObjectClass))", ldapServerProperties.getUserSearchFilter(false));
        ldapServerProperties.setUsernameAttribute("anotherName");
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_USER_SEARCH_FILTER, "(&(anotherName={0})(objectClass=dummyObjectClass))", ldapServerProperties.getUserSearchFilter(false));
    }

    @org.junit.Test
    public void testGetAlternatUserSearchFilterForUserPrincipalName() throws java.lang.Exception {
        ldapServerProperties.setAlternateUserSearchFilter("(&({usernameAttribute}={0})(objectClass={userObjectClass}))");
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_USER_SEARCH_FILTER, "(&(uid={0})(objectClass=dummyObjectClass))", ldapServerProperties.getUserSearchFilter(true));
        ldapServerProperties.setUsernameAttribute("anotherName");
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.LdapServerPropertiesTest.INCORRECT_USER_SEARCH_FILTER, "(&(anotherName={0})(objectClass=dummyObjectClass))", ldapServerProperties.getUserSearchFilter(true));
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier<org.apache.ambari.server.security.authorization.LdapServerProperties> verifier = nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.security.authorization.LdapServerProperties.class);
        verifier.suppress(Warning.NONFINAL_FIELDS);
        verifier.verify();
    }

    @org.junit.Test
    public void testResolveUserSearchFilterPlaceHolders() throws java.lang.Exception {
        java.lang.String ldapUserSearchFilter = "{usernameAttribute}={0}  {userObjectClass}={1}";
        java.lang.String filter = ldapServerProperties.resolveUserSearchFilterPlaceHolders(ldapUserSearchFilter);
        org.junit.Assert.assertEquals("uid={0}  dummyObjectClass={1}", filter);
    }
}