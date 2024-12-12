package org.apache.ambari.server.topology;
public class Setting {
    private java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties;

    public static final java.lang.String SETTING_NAME_RECOVERY_SETTINGS = "recovery_settings";

    public static final java.lang.String SETTING_NAME_SERVICE_SETTINGS = "service_settings";

    public static final java.lang.String SETTING_NAME_COMPONENT_SETTINGS = "component_settings";

    public static final java.lang.String SETTING_NAME_DEPLOYMENT_SETTINGS = "deployment_settings";

    public static final java.lang.String SETTING_NAME_RECOVERY_ENABLED = "recovery_enabled";

    public static final java.lang.String SETTING_NAME_SKIP_FAILURE = "skip_failure";

    public static final java.lang.String SETTING_NAME_NAME = "name";

    public static final java.lang.String SETTING_NAME_REPOSITORY_SETTINGS = "repository_settings";

    public static final java.lang.String SETTING_NAME_CREDENTIAL_STORE_ENABLED = "credential_store_enabled";

    public Setting(java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties) {
        this.properties = properties;
    }

    public java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> getProperties() {
        return properties;
    }

    public java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> getSettingValue(java.lang.String settingName) {
        if (properties.containsKey(settingName)) {
            return properties.get(settingName);
        }
        return java.util.Collections.emptySet();
    }
}