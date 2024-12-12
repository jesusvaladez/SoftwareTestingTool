package org.apache.ambari.server.topology;
import static org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID;
public class ConfigurationFactory {
    private static final java.lang.String SCHEMA_IS_NOT_SUPPORTED_MESSAGE = "Provided configuration format is not supported";

    public org.apache.ambari.server.topology.Configuration getConfiguration(java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> configProperties) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, attributes);
        if (configProperties != null) {
            for (java.util.Map<java.lang.String, java.lang.String> typeMap : configProperties) {
                org.apache.ambari.server.topology.ConfigurationFactory.ConfigurationStrategy strategy = decidePopulationStrategy(typeMap);
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : typeMap.entrySet()) {
                    java.lang.String[] propertyNameTokens = entry.getKey().split("/");
                    strategy.setConfiguration(configuration, propertyNameTokens, entry.getValue());
                }
            }
        }
        return configuration;
    }

    private org.apache.ambari.server.topology.ConfigurationFactory.ConfigurationStrategy decidePopulationStrategy(java.util.Map<java.lang.String, java.lang.String> configuration) {
        if ((configuration != null) && (!configuration.isEmpty())) {
            java.lang.String keyEntry = configuration.keySet().iterator().next();
            java.util.List<java.lang.String> keyNameTokens = org.apache.ambari.server.topology.ConfigurationFactory.splitConfigurationKey(keyEntry);
            if (org.apache.ambari.server.topology.ConfigurationFactory.isKeyInLegacyFormat(keyNameTokens)) {
                return new org.apache.ambari.server.topology.ConfigurationFactory.ConfigurationStrategyV1();
            } else if (org.apache.ambari.server.topology.ConfigurationFactory.isKeyInNewFormat(keyNameTokens)) {
                return new org.apache.ambari.server.topology.ConfigurationFactory.ConfigurationStrategyV2();
            } else {
                throw new java.lang.IllegalArgumentException(org.apache.ambari.server.topology.ConfigurationFactory.SCHEMA_IS_NOT_SUPPORTED_MESSAGE);
            }
        } else {
            return new org.apache.ambari.server.topology.ConfigurationFactory.ConfigurationStrategyV2();
        }
    }

    static java.util.List<java.lang.String> splitConfigurationKey(java.lang.String configurationKey) {
        return java.util.Arrays.asList(configurationKey.split("/"));
    }

    static boolean isKeyInLegacyFormat(java.util.List<java.lang.String> configurationKey) {
        return configurationKey.size() == 2;
    }

    static boolean isKeyInNewFormat(java.util.List<java.lang.String> configurationKey) {
        java.lang.String propertiesType = configurationKey.get(1);
        return ((configurationKey.size() == 3) && org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID.equals(propertiesType)) || ((configurationKey.size() == 4) && org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID.equals(propertiesType));
    }

    private static abstract class ConfigurationStrategy {
        protected abstract void setConfiguration(org.apache.ambari.server.topology.Configuration configuration, java.lang.String[] propertyNameTokens, java.lang.String propertyValue);
    }

    protected static class ConfigurationStrategyV1 extends org.apache.ambari.server.topology.ConfigurationFactory.ConfigurationStrategy {
        @java.lang.Override
        protected void setConfiguration(org.apache.ambari.server.topology.Configuration configuration, java.lang.String[] propertyNameTokens, java.lang.String propertyValue) {
            configuration.setProperty(propertyNameTokens[0], propertyNameTokens[1], propertyValue);
        }
    }

    protected static class ConfigurationStrategyV2 extends org.apache.ambari.server.topology.ConfigurationFactory.ConfigurationStrategy {
        @java.lang.Override
        protected void setConfiguration(org.apache.ambari.server.topology.Configuration configuration, java.lang.String[] propertyNameTokens, java.lang.String propertyValue) {
            java.lang.String type = propertyNameTokens[0];
            if (org.apache.ambari.server.topology.BlueprintFactory.PROPERTIES_PROPERTY_ID.equals(propertyNameTokens[1])) {
                configuration.setProperty(type, propertyNameTokens[2], propertyValue);
            } else if (org.apache.ambari.server.topology.BlueprintFactory.PROPERTIES_ATTRIBUTES_PROPERTY_ID.equals(propertyNameTokens[1])) {
                configuration.setAttribute(type, propertyNameTokens[3], propertyNameTokens[2], propertyValue);
            }
        }
    }
}