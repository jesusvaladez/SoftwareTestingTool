package org.apache.ambari.server.state;
public class RefreshCommandConfiguration {
    public static final java.lang.String RELOAD_CONFIGS = "reload_configs";

    public static final java.lang.String REFRESH_CONFIGS = "refresh_configs";

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyComponentCommandMap;

    public RefreshCommandConfiguration() {
    }

    private java.lang.String findKey(java.lang.String propertyName) {
        for (java.lang.String keyName : propertyComponentCommandMap.keySet()) {
            if (propertyName.startsWith(keyName)) {
                return keyName;
            }
        }
        return null;
    }

    public java.lang.String getRefreshCommandForComponent(org.apache.ambari.server.state.ServiceComponentHost sch, java.lang.String propertyName) {
        if (sch.isClientComponent()) {
            return org.apache.ambari.server.state.RefreshCommandConfiguration.REFRESH_CONFIGS;
        }
        java.lang.String keyName = findKey(propertyName);
        java.util.Map<java.lang.String, java.lang.String> componentCommandMap = propertyComponentCommandMap.get(keyName);
        if (componentCommandMap != null) {
            java.lang.String commandForComponent = componentCommandMap.get(sch.getServiceComponentName());
            if (commandForComponent != null) {
                return commandForComponent;
            } else if (componentCommandMap.size() == 1) {
                return org.apache.ambari.server.state.RefreshCommandConfiguration.REFRESH_CONFIGS;
            }
        }
        return null;
    }

    public void addRefreshCommands(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> refreshCommands) {
        if (propertyComponentCommandMap == null) {
            propertyComponentCommandMap = new java.util.HashMap();
        }
        propertyComponentCommandMap.putAll(refreshCommands);
    }
}