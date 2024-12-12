package org.apache.ambari.server.security.authentication.tproxy;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED;
public class AmbariTProxyConfigurationProviderTest {
    @org.junit.Test
    public void testLoadInstance() {
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfigurationProvider provider = new org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfigurationProvider(null, null);
        java.util.Map<java.lang.String, java.lang.String> expectedProperties = new java.util.HashMap<>();
        expectedProperties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED.key(), "true");
        expectedProperties.put("ambari.tproxy.proxyuser.knox.hosts", "c7401.ambari.apache.org");
        expectedProperties.put("ambari.tproxy.proxyuser.knox.users", "*");
        expectedProperties.put("ambari.tproxy.proxyuser.knox.groups", "users");
        org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration instance = provider.loadInstance(createAmbariConfigurationEntities(expectedProperties));
        org.junit.Assert.assertNotNull(instance);
        org.junit.Assert.assertNotSame(expectedProperties, instance.toMap());
        org.junit.Assert.assertEquals(expectedProperties, instance.toMap());
        org.junit.Assert.assertTrue(instance.isEnabled());
        org.junit.Assert.assertEquals(expectedProperties.get("ambari.tproxy.proxyuser.knox.hosts"), instance.getAllowedHosts("knox"));
        org.junit.Assert.assertEquals(expectedProperties.get("ambari.tproxy.proxyuser.knox.users"), instance.getAllowedUsers("knox"));
        org.junit.Assert.assertEquals(expectedProperties.get("ambari.tproxy.proxyuser.knox.groups"), instance.getAllowedGroups("knox"));
    }

    private java.util.Collection<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> createAmbariConfigurationEntities(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : properties.entrySet()) {
            org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity = new org.apache.ambari.server.orm.entities.AmbariConfigurationEntity();
            entity.setCategoryName(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName());
            entity.setPropertyName(entry.getKey());
            entity.setPropertyValue(entry.getValue());
            entities.add(entity);
        }
        return entities;
    }
}