package org.apache.ambari.server.topology;
@com.google.inject.Singleton
public class SettingFactory {
    public static org.apache.ambari.server.topology.Setting getSetting(java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> blueprintSetting) {
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Setting setting = new org.apache.ambari.server.topology.Setting(properties);
        if (blueprintSetting != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> settingMap : blueprintSetting) {
                for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : settingMap.entrySet()) {
                    final java.lang.String[] propertyNames = entry.getKey().split("/");
                    java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> settingValue;
                    if (entry.getValue() instanceof java.util.Set) {
                        settingValue = ((java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.String>>) (entry.getValue()));
                    } else if (propertyNames.length > 1) {
                        java.util.HashMap<java.lang.String, java.lang.String> property = new java.util.HashMap<>();
                        property.put(propertyNames[1], java.lang.String.valueOf(entry.getValue()));
                        settingValue = properties.get(propertyNames[0]);
                        if (settingValue == null) {
                            settingValue = new java.util.HashSet<>();
                        }
                        settingValue.add(property);
                    } else {
                        throw new java.lang.IllegalArgumentException("Invalid setting schema: " + java.lang.String.valueOf(entry.getValue()));
                    }
                    properties.put(propertyNames[0], settingValue);
                }
            }
        }
        return setting;
    }
}