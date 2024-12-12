package org.apache.ambari.server.configuration;
public class AmbariServerConfigurationKeyTest {
    @org.junit.Test
    public void testTranslateNullCategory() {
        org.junit.Assert.assertNull(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(null, "some.property"));
    }

    @org.junit.Test
    public void testTranslateNullPropertyName() {
        org.junit.Assert.assertNull(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION, null));
    }

    @org.junit.Test
    public void testTranslateInvalidPropertyName() {
        org.junit.Assert.assertNull(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "invalid_property_name"));
    }

    @org.junit.Test
    public void testTranslateExpected() {
        org.junit.Assert.assertSame(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.key()));
    }

    @org.junit.Test
    public void testTranslateRegex() {
        org.apache.ambari.server.configuration.AmbariServerConfigurationKey keyWithRegex = org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_HOSTS;
        org.junit.Assert.assertTrue(keyWithRegex.isRegex());
        org.junit.Assert.assertSame(keyWithRegex, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(keyWithRegex.getConfigurationCategory(), "ambari.tproxy.proxyuser.knox.hosts"));
        org.junit.Assert.assertSame(keyWithRegex, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(keyWithRegex.getConfigurationCategory(), "ambari.tproxy.proxyuser.not.knox.hosts"));
        org.apache.ambari.server.configuration.AmbariServerConfigurationKey translatedKey = org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(keyWithRegex.getConfigurationCategory(), "ambari.tproxy.proxyuser.not.knox.groups");
        org.junit.Assert.assertNotNull(translatedKey);
        org.junit.Assert.assertNotSame(keyWithRegex, translatedKey);
        org.junit.Assert.assertNull(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(keyWithRegex.getConfigurationCategory(), "ambari.tproxy.proxyuser.not.knox.invalid"));
    }

    @org.junit.Test
    public void testFindPasswordConfigurations() throws java.lang.Exception {
        final java.util.Set<java.lang.String> passwordConfigurations = org.apache.ambari.server.configuration.AmbariServerConfigurationKey.findPasswordConfigurations();
        org.junit.Assert.assertEquals(2, passwordConfigurations.size());
        org.junit.Assert.assertTrue(passwordConfigurations.contains(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD.key()));
        org.junit.Assert.assertTrue(passwordConfigurations.contains(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TRUST_STORE_PASSWORD.key()));
    }
}