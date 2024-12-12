package org.apache.ambari.server.configuration;
public abstract class AmbariServerConfiguration {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.configuration.AmbariServerConfiguration.class);

    protected final java.util.Map<java.lang.String, java.lang.String> configurationMap = new java.util.HashMap<>();

    protected AmbariServerConfiguration(java.util.Map<java.lang.String, java.lang.String> configurationMap) {
        if (configurationMap != null) {
            this.configurationMap.putAll(configurationMap);
        }
    }

    protected java.lang.String getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey ambariServerConfigurationKey, java.util.Map<java.lang.String, java.lang.String> configurationMap) {
        return getValue(ambariServerConfigurationKey.key(), configurationMap, ambariServerConfigurationKey.getDefaultValue());
    }

    protected java.lang.String getValue(java.lang.String propertyName, java.util.Map<java.lang.String, java.lang.String> configurationMap, java.lang.String defaultValue) {
        if ((configurationMap != null) && configurationMap.containsKey(propertyName)) {
            return configurationMap.get(propertyName);
        } else {
            org.apache.ambari.server.configuration.AmbariServerConfiguration.LOGGER.debug("Ambari server configuration property [{}] hasn't been set; using default value", propertyName);
            return defaultValue;
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> toMap() {
        return new java.util.HashMap<>(configurationMap);
    }

    public void setValueFor(java.lang.String configName, java.lang.String value) {
        org.apache.ambari.server.configuration.AmbariServerConfigurationKey ambariServerConfigurationKey = org.apache.ambari.server.configuration.AmbariServerConfigurationKey.translate(getCategory(), configName);
        if (ambariServerConfigurationKey != null) {
            setValueFor(ambariServerConfigurationKey, value);
        }
    }

    public void setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey ambariServerConfigurationKey, java.lang.String value) {
        if (ambariServerConfigurationKey.getConfigurationCategory() != getCategory()) {
            throw new java.lang.IllegalArgumentException((ambariServerConfigurationKey.key() + " is not a valid ") + getCategory().getCategoryName());
        }
        configurationMap.put(ambariServerConfigurationKey.key(), value);
    }

    protected abstract org.apache.ambari.server.configuration.AmbariServerConfigurationCategory getCategory();
}