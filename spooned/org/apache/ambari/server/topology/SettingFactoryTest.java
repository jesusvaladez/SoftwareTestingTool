package org.apache.ambari.server.topology;
public class SettingFactoryTest {
    @org.junit.Test
    public void testGetSettingWithSetOfProperties() {
        org.apache.ambari.server.topology.SettingFactory settingFactory = new org.apache.ambari.server.topology.SettingFactory();
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties;
        org.apache.ambari.server.topology.Setting setting = settingFactory.getSetting(createSettingWithSetOfProperties());
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> propertyValues = setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_SETTINGS);
        org.junit.Assert.assertEquals(propertyValues.size(), 1);
        org.junit.Assert.assertEquals(propertyValues.iterator().next().get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED), "true");
    }

    @org.junit.Test
    public void testGetSettingWithoutSetOfProperties() {
        org.apache.ambari.server.topology.SettingFactory settingFactory = new org.apache.ambari.server.topology.SettingFactory();
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties;
        org.apache.ambari.server.topology.Setting setting = settingFactory.getSetting(createSettingWithoutSetOfProperties());
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> propertyValues = setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_SETTINGS);
        org.junit.Assert.assertEquals(propertyValues.size(), 1);
        org.junit.Assert.assertEquals(propertyValues.iterator().next().get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED), "true");
    }

    private java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> createSettingWithSetOfProperties() {
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> setting1 = new java.util.HashSet<>();
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> setting2 = new java.util.HashSet<>();
        java.util.HashMap<java.lang.String, java.lang.String> setting1Properties1 = new java.util.HashMap<>();
        setting1Properties1.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED, "true");
        setting1.add(setting1Properties1);
        java.util.HashMap<java.lang.String, java.lang.String> setting2Properties1 = new java.util.HashMap<>();
        setting2Properties1.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_NAME, "HDFS");
        setting2Properties1.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED, "false");
        java.util.HashMap<java.lang.String, java.lang.String> setting2Properties2 = new java.util.HashMap<>();
        setting2Properties2.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_NAME, "TEZ");
        setting2Properties2.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED, "false");
        setting2.add(setting2Properties1);
        setting2.add(setting2Properties2);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> setting = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.Object> properties;
        properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_SETTINGS, setting1);
        setting.add(properties);
        properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SERVICE_SETTINGS, setting2);
        setting.add(properties);
        return setting;
    }

    private java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> createSettingWithoutSetOfProperties() {
        java.util.HashMap<java.lang.String, java.lang.String> setting2Properties1 = new java.util.HashMap<>();
        setting2Properties1.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_NAME, "HDFS");
        setting2Properties1.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED, "false");
        java.util.HashMap<java.lang.String, java.lang.String> setting2Properties2 = new java.util.HashMap<>();
        setting2Properties2.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_NAME, "TEZ");
        setting2Properties2.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED, "false");
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> setting2 = new java.util.HashSet<>();
        setting2.add(setting2Properties1);
        setting2.add(setting2Properties2);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> setting = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.Object> properties;
        properties = new java.util.HashMap<>();
        properties.put((org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_SETTINGS + "/") + org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED, "true");
        setting.add(properties);
        properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SERVICE_SETTINGS, setting2);
        setting.add(properties);
        return setting;
    }
}