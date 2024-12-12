package org.apache.ambari.server.controller.internal;
@com.google.inject.Singleton
public class AmbariServerConfigurationHandler extends org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler.class);

    private final org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO;

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher;

    @com.google.inject.Inject
    AmbariServerConfigurationHandler(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO, org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        this.ambariConfigurationDAO = ambariConfigurationDAO;
        this.publisher = publisher;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> getComponentConfigurations(java.lang.String categoryName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> configurations = null;
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = (categoryName == null) ? ambariConfigurationDAO.findAll() : ambariConfigurationDAO.findByCategory(categoryName);
        if (entities != null) {
            configurations = new java.util.HashMap<>();
            for (org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity : entities) {
                java.lang.String category = entity.getCategoryName();
                org.apache.ambari.server.api.services.RootServiceComponentConfiguration configuration = configurations.get(category);
                if (configuration == null) {
                    configuration = new org.apache.ambari.server.api.services.RootServiceComponentConfiguration();
                    configurations.put(category, configuration);
                }
                configuration.addProperty(entity.getPropertyName(), entity.getPropertyValue());
                if (categoryName != null) {
                    configuration.addPropertyType(entity.getPropertyName(), org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationPropertyTypeName(categoryName, entity.getPropertyName()));
                }
            }
        }
        return configurations;
    }

    @java.lang.Override
    public void removeComponentConfiguration(java.lang.String categoryName) {
        if (null == categoryName) {
            org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler.LOGGER.debug("No resource id provided in the request");
        } else {
            org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler.LOGGER.debug("Deleting Ambari configuration with id: {}", categoryName);
            if (ambariConfigurationDAO.removeByCategory(categoryName) > 0) {
                publisher.publish(new org.apache.ambari.server.events.AmbariConfigurationChangedEvent(categoryName));
            }
        }
    }

    @java.lang.Override
    public void updateComponentCategory(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean removePropertiesIfNotSpecified) throws org.apache.ambari.server.AmbariException {
        validateProperties(categoryName, properties);
        final boolean toBePublished = (properties.isEmpty()) ? false : ambariConfigurationDAO.reconcileCategory(categoryName, properties, removePropertiesIfNotSpecified);
        if (toBePublished) {
            publisher.publish(new org.apache.ambari.server.events.AmbariConfigurationChangedEvent(categoryName));
        }
    }

    private void validateProperties(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties) {
        for (java.lang.String key : properties.keySet()) {
            if (org.apache.ambari.server.controller.internal.AmbariServerConfigurationUtils.getConfigurationKey(categoryName, key) == null) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid Ambari server configuration key: %s:%s", categoryName, key));
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler.OperationResult performOperation(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean mergeExistingProperties, java.lang.String operation, java.util.Map<java.lang.String, java.lang.Object> operationParameters) throws org.apache.ambari.server.controller.spi.SystemException {
        throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("The requested operation is not supported for this category: %s", categoryName));
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getConfigurations() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = ambariConfigurationDAO.findAll();
        if (entities != null) {
            for (org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity : entities) {
                java.lang.String category = entity.getCategoryName();
                java.util.Map<java.lang.String, java.lang.String> configuration = configurations.computeIfAbsent(category, k -> new java.util.HashMap<>());
                configuration.put(entity.getPropertyName(), entity.getPropertyValue());
            }
        }
        return configurations;
    }

    public java.util.Map<java.lang.String, java.lang.String> getConfigurationProperties(java.lang.String categoryName) {
        java.util.Map<java.lang.String, java.lang.String> properties = null;
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> entities = ambariConfigurationDAO.findByCategory(categoryName);
        if (entities != null) {
            properties = new java.util.HashMap<>();
            for (org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity : entities) {
                properties.put(entity.getPropertyName(), entity.getPropertyValue());
            }
        }
        return properties;
    }

    protected java.util.Set<java.lang.String> getEnabledServices(java.lang.String categoryName, java.lang.String manageServicesConfigurationPropertyName, java.lang.String enabledServicesPropertyName) {
        final java.util.Map<java.lang.String, java.lang.String> configurationProperties = getConfigurationProperties(categoryName);
        final boolean manageConfigurations = org.apache.commons.lang3.StringUtils.isNotBlank(manageServicesConfigurationPropertyName) && "true".equalsIgnoreCase(configurationProperties.get(manageServicesConfigurationPropertyName));
        final java.lang.String enabledServices = (manageConfigurations) ? configurationProperties.get(enabledServicesPropertyName) : null;
        if (org.apache.commons.lang3.StringUtils.isEmpty(enabledServices)) {
            return java.util.Collections.emptySet();
        } else {
            return java.util.Arrays.stream(enabledServices.split(",")).map(java.lang.String::trim).map(java.lang.String::toUpperCase).collect(java.util.stream.Collectors.toSet());
        }
    }
}