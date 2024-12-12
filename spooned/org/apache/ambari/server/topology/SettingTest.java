package org.apache.ambari.server.topology;
public class SettingTest {
    @org.junit.Test
    public void testGetProperties() {
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties = new java.util.HashMap<>();
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> setting1 = new java.util.HashSet<>();
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> setting2 = new java.util.HashSet<>();
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> setting3 = new java.util.HashSet<>();
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> setting4 = new java.util.HashSet<>();
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
        java.util.HashMap<java.lang.String, java.lang.String> setting3Properties1 = new java.util.HashMap<>();
        setting1Properties1.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE, "true");
        setting1.add(setting3Properties1);
        java.util.HashMap<java.lang.String, java.lang.String> setting4Properties1 = new java.util.HashMap<>();
        setting4Properties1.put(org.apache.ambari.server.topology.RepositorySetting.OVERRIDE_STRATEGY, org.apache.ambari.server.topology.RepositorySetting.OVERRIDE_STRATEGY_ALWAYS_APPLY);
        setting4Properties1.put(org.apache.ambari.server.topology.RepositorySetting.OPERATING_SYSTEM, "redhat7");
        setting4Properties1.put(org.apache.ambari.server.topology.RepositorySetting.REPO_ID, "HDP");
        setting4Properties1.put(org.apache.ambari.server.topology.RepositorySetting.BASE_URL, "http://localhost/repo");
        setting4.add(setting4Properties1);
        java.util.HashMap<java.lang.String, java.lang.String> setting4Properties2 = new java.util.HashMap<>();
        setting4Properties2.put(org.apache.ambari.server.topology.RepositorySetting.OVERRIDE_STRATEGY, org.apache.ambari.server.topology.RepositorySetting.OVERRIDE_STRATEGY_ALWAYS_APPLY);
        setting4Properties2.put(org.apache.ambari.server.topology.RepositorySetting.OPERATING_SYSTEM, "redhat7");
        setting4Properties2.put(org.apache.ambari.server.topology.RepositorySetting.REPO_ID, "HDP-UTIL");
        setting4Properties2.put(org.apache.ambari.server.topology.RepositorySetting.BASE_URL, "http://localhost/repo");
        setting4.add(setting4Properties2);
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_SETTINGS, setting1);
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SERVICE_SETTINGS, setting2);
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_DEPLOYMENT_SETTINGS, setting3);
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_REPOSITORY_SETTINGS, setting4);
        org.apache.ambari.server.topology.Setting setting = new org.apache.ambari.server.topology.Setting(properties);
        org.junit.Assert.assertEquals(properties, setting.getProperties());
    }

    @org.junit.Test
    public void testGetSettingProperties() {
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties = new java.util.HashMap<>();
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
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_SETTINGS, setting1);
        properties.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SERVICE_SETTINGS, setting2);
        org.apache.ambari.server.topology.Setting setting = new org.apache.ambari.server.topology.Setting(properties);
        org.junit.Assert.assertEquals(setting2, setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_SERVICE_SETTINGS));
    }
}