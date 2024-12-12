package org.apache.ambari.server.metrics.system.impl;
import org.apache.commons.lang.StringUtils;
public class MetricsConfiguration {
    public static final java.lang.String CONFIG_FILE = "metrics.properties";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.class);

    private java.util.Properties properties;

    public static org.apache.ambari.server.metrics.system.impl.MetricsConfiguration getMetricsConfiguration() {
        java.util.Properties properties = org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.readConfigFile();
        if ((properties == null) || properties.isEmpty()) {
            return null;
        }
        return new org.apache.ambari.server.metrics.system.impl.MetricsConfiguration(properties);
    }

    public MetricsConfiguration(java.util.Properties properties) {
        this.properties = properties;
    }

    private static java.util.Properties readConfigFile() {
        java.util.Properties properties = new java.util.Properties();
        java.io.InputStream inputStream = org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.class.getClassLoader().getResourceAsStream(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.CONFIG_FILE);
        if (inputStream == null) {
            org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.LOG.info(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.CONFIG_FILE + " not found in classpath");
            return null;
        }
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (java.io.FileNotFoundException fnf) {
            org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.LOG.info(("No configuration file " + org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.CONFIG_FILE) + " found in classpath.");
            return null;
        } catch (java.io.IOException ie) {
            org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.LOG.error("Can't read configuration file " + org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.CONFIG_FILE, ie);
            return null;
        }
        return properties;
    }

    public java.lang.String getProperty(java.lang.String key) {
        return properties.getProperty(key);
    }

    public java.lang.String getProperty(java.lang.String key, java.lang.String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public java.util.Properties getProperties() {
        return properties;
    }

    public static org.apache.ambari.server.metrics.system.impl.MetricsConfiguration getSubsetConfiguration(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration metricsConfiguration, java.lang.String prefix) {
        if (null == metricsConfiguration) {
            return null;
        }
        java.util.Properties properties = metricsConfiguration.getProperties();
        if ((null == properties) || org.apache.commons.lang.StringUtils.isEmpty(prefix)) {
            return new org.apache.ambari.server.metrics.system.impl.MetricsConfiguration(properties);
        }
        java.util.Properties subsetProperties = new java.util.Properties();
        for (java.util.Map.Entry<java.lang.Object, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String key = entry.getKey().toString();
            java.lang.String val = entry.getValue().toString();
            if (key.startsWith(prefix)) {
                key = key.substring(prefix.length());
                subsetProperties.put(key, val);
            }
        }
        return new org.apache.ambari.server.metrics.system.impl.MetricsConfiguration(subsetProperties);
    }
}