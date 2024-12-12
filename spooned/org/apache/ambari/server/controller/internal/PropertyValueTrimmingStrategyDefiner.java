package org.apache.ambari.server.controller.internal;
public class PropertyValueTrimmingStrategyDefiner {
    private static final java.util.Set<java.lang.String> SET_OF_URL_PROPERTIES = com.google.common.collect.ImmutableSet.of("javax.jdo.option.ConnectionURL", "oozie.service.JPAService.jdbc.url");

    private static org.apache.ambari.server.controller.internal.TrimmingStrategy getTrimmingStrategyForConfigProperty(org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty) {
        if (configProperty != null) {
            org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfo = configProperty.getPropertyValueAttributes();
            if (valueAttributesInfo != null) {
                java.lang.String type = valueAttributesInfo.getType();
                if ("directory".equals(type) || "directories".equals(type)) {
                    return org.apache.ambari.server.controller.internal.TrimmingStrategy.DIRECTORIES;
                } else if ("host".equals(type)) {
                    return org.apache.ambari.server.controller.internal.TrimmingStrategy.DEFAULT;
                }
            }
            if ((configProperty.getPropertyTypes() != null) && configProperty.getPropertyTypes().contains(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
                return org.apache.ambari.server.controller.internal.TrimmingStrategy.PASSWORD;
            }
        }
        return null;
    }

    private static org.apache.ambari.server.controller.internal.TrimmingStrategy getTrimmingStrategyByPropertyName(java.lang.String propertyName) {
        if (org.apache.ambari.server.controller.internal.PropertyValueTrimmingStrategyDefiner.SET_OF_URL_PROPERTIES.contains(propertyName)) {
            return org.apache.ambari.server.controller.internal.TrimmingStrategy.DEFAULT;
        } else {
            return org.apache.ambari.server.controller.internal.TrimmingStrategy.DELETE_SPACES_AT_END;
        }
    }

    public static org.apache.ambari.server.controller.internal.TrimmingStrategy defineTrimmingStrategy(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String propertyName, java.lang.String configType) {
        org.apache.ambari.server.controller.internal.TrimmingStrategy result = null;
        java.lang.String service = stack.getServiceForConfigType(configType);
        if (service != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> map = stack.getConfigurationPropertiesWithMetadata(service, configType);
            if (map != null) {
                org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty = map.get(propertyName);
                if (configProperty != null) {
                    result = org.apache.ambari.server.controller.internal.PropertyValueTrimmingStrategyDefiner.getTrimmingStrategyForConfigProperty(configProperty);
                }
            }
        }
        if (result == null) {
            result = org.apache.ambari.server.controller.internal.PropertyValueTrimmingStrategyDefiner.getTrimmingStrategyByPropertyName(propertyName);
        }
        return result;
    }
}