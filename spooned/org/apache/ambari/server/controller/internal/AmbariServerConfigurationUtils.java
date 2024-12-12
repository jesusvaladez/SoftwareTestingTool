package org.apache.ambari.server.controller.internal;
public class AmbariServerConfigurationUtils {
    public static org.apache.ambari.server.configuration.AmbariServerConfigurationKey getConfigurationKey(java.lang.String category, java.lang.String propertyName) {
        return org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.translate(category), propertyName);
    }

    public static org.apache.ambari.server.configuration.AmbariServerConfigurationKey getConfigurationKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory category, java.lang.String propertyName) {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(category, propertyName);
    }

    public static org.apache.ambari.server.configuration.ConfigurationPropertyType getConfigurationPropertyType(java.lang.String category, java.lang.String propertyName) {
        return org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationPropertyType(org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationKey(category, propertyName));
    }

    public static org.apache.ambari.server.configuration.ConfigurationPropertyType getConfigurationPropertyType(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory category, java.lang.String propertyName) {
        return org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationPropertyType(org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationKey(category, propertyName));
    }

    private static org.apache.ambari.server.configuration.ConfigurationPropertyType getConfigurationPropertyType(org.apache.ambari.server.configuration.AmbariServerConfigurationKey configurationKey) {
        return configurationKey == null ? org.apache.ambari.server.configuration.ConfigurationPropertyType.UNKNOWN : configurationKey.getConfigurationPropertyType();
    }

    public static java.lang.String getConfigurationPropertyTypeName(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory category, java.lang.String propertyName) {
        final org.apache.ambari.server.configuration.ConfigurationPropertyType configurationPropertyType = org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationPropertyType(category, propertyName);
        return configurationPropertyType == null ? null : configurationPropertyType.name();
    }

    public static java.lang.String getConfigurationPropertyTypeName(java.lang.String category, java.lang.String propertyName) {
        final org.apache.ambari.server.configuration.ConfigurationPropertyType configurationPropertyType = org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationPropertyType(category, propertyName);
        return configurationPropertyType == null ? null : configurationPropertyType.name();
    }

    public static boolean isPassword(java.lang.String category, java.lang.String propertyName) {
        return org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.isPassword(org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationKey(category, propertyName));
    }

    public static boolean isPassword(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory category, java.lang.String propertyName) {
        return org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.isPassword(org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationKey(category, propertyName));
    }

    public static boolean isPassword(org.apache.ambari.server.configuration.AmbariServerConfigurationKey configurationKey) {
        return org.apache.ambari.server.configuration.ConfigurationPropertyType.PASSWORD.equals(org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationPropertyType(configurationKey));
    }
}