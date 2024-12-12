package org.apache.ambari.server.view.configuration;
public class PropertyConfigTest {
    @org.junit.Test
    public void testGetKey() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instanceConfigs = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        for (org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig : instanceConfigs) {
            java.util.List<org.apache.ambari.server.view.configuration.PropertyConfig> propertyConfigs = instanceConfig.getProperties();
            org.junit.Assert.assertTrue(propertyConfigs.size() <= 2);
            org.junit.Assert.assertEquals("p1", propertyConfigs.get(0).getKey());
            if (propertyConfigs.size() == 2) {
                org.junit.Assert.assertEquals("p2", propertyConfigs.get(1).getKey());
            }
        }
    }

    @org.junit.Test
    public void testGetValue() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instanceConfigs = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        for (org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig : instanceConfigs) {
            java.util.List<org.apache.ambari.server.view.configuration.PropertyConfig> propertyConfigs = instanceConfig.getProperties();
            org.junit.Assert.assertTrue(propertyConfigs.size() <= 2);
            org.junit.Assert.assertTrue(propertyConfigs.get(0).getValue().startsWith("v1-"));
            if (propertyConfigs.size() == 2) {
                org.junit.Assert.assertTrue(propertyConfigs.get(1).getValue().startsWith("v2-"));
            }
        }
    }
}