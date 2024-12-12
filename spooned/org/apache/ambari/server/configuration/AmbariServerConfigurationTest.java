package org.apache.ambari.server.configuration;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED;
public class AmbariServerConfigurationTest {
    @org.junit.Test
    public void testGetValue() {
        final java.util.Map<java.lang.String, java.lang.String> configurationMap = java.util.Collections.singletonMap(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED.key(), "true");
        final org.apache.ambari.server.configuration.AmbariServerConfiguration ambariServerConfiguration = new org.apache.ambari.server.configuration.AmbariServerConfiguration(configurationMap) {
            @java.lang.Override
            protected org.apache.ambari.server.configuration.AmbariServerConfigurationCategory getCategory() {
                return null;
            }
        };
        org.junit.Assert.assertEquals("true", ambariServerConfiguration.getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED, configurationMap));
        org.junit.Assert.assertEquals("false", org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.getDefaultValue());
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.getDefaultValue(), ambariServerConfiguration.getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED, configurationMap));
        org.junit.Assert.assertEquals("defaultValue", ambariServerConfiguration.getValue(null, configurationMap, "defaultValue"));
        org.junit.Assert.assertEquals("defaultValue", ambariServerConfiguration.getValue("property.name", null, "defaultValue"));
        org.junit.Assert.assertNull(ambariServerConfiguration.getValue("property.name", java.util.Collections.emptyMap(), null));
        org.junit.Assert.assertEquals(configurationMap, ambariServerConfiguration.toMap());
    }
}