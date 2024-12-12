package org.apache.ambari.server.ldap;
public class AmbariLdapConfigurationTest {
    private org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration;

    @org.junit.Before
    public void setup() {
        configuration = new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration();
    }

    @org.junit.Test
    public void testLdapUserSearchFilterDefault() throws java.lang.Exception {
        org.junit.Assert.assertEquals("(&(uid={0})(objectClass=person))", configuration.getLdapServerProperties().getUserSearchFilter(false));
    }

    @org.junit.Test
    public void testLdapUserSearchFilter() throws java.lang.Exception {
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE, "test_uid");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_FILTER, "{usernameAttribute}={0}");
        org.junit.Assert.assertEquals("test_uid={0}", configuration.getLdapServerProperties().getUserSearchFilter(false));
    }

    @org.junit.Test
    public void testAlternateLdapUserSearchFilterDefault() throws java.lang.Exception {
        org.junit.Assert.assertEquals("(&(userPrincipalName={0})(objectClass=person))", configuration.getLdapServerProperties().getUserSearchFilter(true));
    }

    @org.junit.Test
    public void testAlternatLdapUserSearchFilter() throws java.lang.Exception {
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE, "test_uid");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_FILTER, "{usernameAttribute}={5}");
        org.junit.Assert.assertEquals("test_uid={5}", configuration.getLdapServerProperties().getUserSearchFilter(true));
    }

    @org.junit.Test
    public void testAlternateUserSearchEnabledIsSetToFalseByDefault() throws java.lang.Exception {
        org.junit.Assert.assertFalse(configuration.isLdapAlternateUserSearchEnabled());
    }

    @org.junit.Test
    public void testAlternateUserSearchEnabledTrue() throws java.lang.Exception {
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_ENABLED, "true");
        org.junit.Assert.assertTrue(configuration.isLdapAlternateUserSearchEnabled());
    }

    @org.junit.Test
    public void testAlternateUserSearchEnabledFalse() throws java.lang.Exception {
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_ENABLED, "false");
        org.junit.Assert.assertFalse(configuration.isLdapAlternateUserSearchEnabled());
    }

    @org.junit.Test
    public void testGetLdapServerProperties() throws java.lang.Exception {
        final java.lang.String managerPw = "ambariTest";
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST, "host");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_PORT, "1");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_HOST, "secHost");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_PORT, "2");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USE_SSL, "true");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND, "true");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_DN, "5");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD, managerPw);
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE, "7");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE, "8");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_BASE, "9");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS, "10");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_BASE, "11");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS, "12");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE, "13");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE, "14");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MAPPING_RULES, "15");
        configuration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_FILTER, "16");
        final org.apache.ambari.server.security.authorization.LdapServerProperties ldapProperties = configuration.getLdapServerProperties();
        org.junit.Assert.assertEquals("host:1", ldapProperties.getPrimaryUrl());
        org.junit.Assert.assertEquals("secHost:2", ldapProperties.getSecondaryUrl());
        org.junit.Assert.assertTrue(ldapProperties.isUseSsl());
        org.junit.Assert.assertTrue(ldapProperties.isAnonymousBind());
        org.junit.Assert.assertEquals("5", ldapProperties.getManagerDn());
        org.junit.Assert.assertEquals(managerPw, ldapProperties.getManagerPassword());
        org.junit.Assert.assertEquals("7", ldapProperties.getBaseDN());
        org.junit.Assert.assertEquals("8", ldapProperties.getUsernameAttribute());
        org.junit.Assert.assertEquals("9", ldapProperties.getUserBase());
        org.junit.Assert.assertEquals("10", ldapProperties.getUserObjectClass());
        org.junit.Assert.assertEquals("11", ldapProperties.getGroupBase());
        org.junit.Assert.assertEquals("12", ldapProperties.getGroupObjectClass());
        org.junit.Assert.assertEquals("13", ldapProperties.getGroupMembershipAttr());
        org.junit.Assert.assertEquals("14", ldapProperties.getGroupNamingAttr());
        org.junit.Assert.assertEquals("15", ldapProperties.getAdminGroupMappingRules());
        org.junit.Assert.assertEquals("16", ldapProperties.getGroupSearchFilter());
    }
}