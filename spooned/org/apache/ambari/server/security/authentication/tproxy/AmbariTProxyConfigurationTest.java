package org.apache.ambari.server.security.authentication.tproxy;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_GROUPS;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_HOSTS;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_USERS;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED;
public class AmbariTProxyConfigurationTest {
    @org.junit.Test
    public void testEmptyConfiguration() {
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration configuration = new org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration(null);
        org.junit.Assert.assertFalse(configuration.isEnabled());
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_HOSTS.getDefaultValue(), configuration.getAllowedHosts("knox"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_USERS.getDefaultValue(), configuration.getAllowedUsers("knox"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_GROUPS.getDefaultValue(), configuration.getAllowedGroups("knox"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_HOSTS.getDefaultValue(), configuration.getAllowedHosts("otherproxyuser"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_USERS.getDefaultValue(), configuration.getAllowedUsers("otherproxyuser"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_GROUPS.getDefaultValue(), configuration.getAllowedGroups("otherproxyuser"));
    }

    @org.junit.Test
    public void testNonEmptyConfiguration() {
        java.util.Map<java.lang.String, java.lang.String> expectedProperties = new java.util.HashMap<>();
        expectedProperties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED.key(), "true");
        expectedProperties.put("ambari.tproxy.proxyuser.knox.hosts", "c7401.ambari.apache.org");
        expectedProperties.put("ambari.tproxy.proxyuser.knox.users", "*");
        expectedProperties.put("ambari.tproxy.proxyuser.knox.groups", "users");
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration configuration = new org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration(expectedProperties);
        org.junit.Assert.assertNotSame(expectedProperties, configuration.toMap());
        org.junit.Assert.assertEquals(expectedProperties, configuration.toMap());
        org.junit.Assert.assertTrue(configuration.isEnabled());
        org.junit.Assert.assertEquals(expectedProperties.get("ambari.tproxy.proxyuser.knox.hosts"), configuration.getAllowedHosts("knox"));
        org.junit.Assert.assertEquals(expectedProperties.get("ambari.tproxy.proxyuser.knox.users"), configuration.getAllowedUsers("knox"));
        org.junit.Assert.assertEquals(expectedProperties.get("ambari.tproxy.proxyuser.knox.groups"), configuration.getAllowedGroups("knox"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_HOSTS.getDefaultValue(), configuration.getAllowedHosts("otherproxyuser"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_USERS.getDefaultValue(), configuration.getAllowedUsers("otherproxyuser"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_GROUPS.getDefaultValue(), configuration.getAllowedGroups("otherproxyuser"));
    }
}