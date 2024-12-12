package org.apache.ambari.server.configuration;
public abstract class AmbariServerConfigurationProvider<T extends org.apache.ambari.server.configuration.AmbariServerConfiguration> implements com.google.inject.Provider<T> {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.class);

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.orm.dao.AmbariConfigurationDAO> ambariConfigurationDAOProvider;

    @com.google.inject.Inject
    @com.google.inject.name.Named("AmbariServerConfigurationEncryptor")
    private org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration> encryptor;

    private final java.util.concurrent.atomic.AtomicBoolean jpaStarted = new java.util.concurrent.atomic.AtomicBoolean(false);

    private final org.apache.ambari.server.configuration.AmbariServerConfigurationCategory configurationCategory;

    private T instance = null;

    protected AmbariServerConfigurationProvider(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory configurationCategory, org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher, com.google.inject.persist.jpa.AmbariJpaPersistService persistService) {
        this.configurationCategory = configurationCategory;
        if (publisher != null) {
            publisher.register(this);
            org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.LOGGER.info("Registered {} in event publisher", this.getClass().getName());
        }
        if (persistService != null) {
            jpaStarted.set(persistService.isStarted());
        }
    }

    @com.google.common.eventbus.Subscribe
    public void ambariConfigurationChanged(org.apache.ambari.server.events.AmbariConfigurationChangedEvent event) {
        if (configurationCategory.getCategoryName().equalsIgnoreCase(event.getCategoryName())) {
            org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.LOGGER.info("Ambari configuration changed event received: {}", event);
            instance = loadInstance();
        }
    }

    @com.google.common.eventbus.Subscribe
    public void jpaInitialized(org.apache.ambari.server.events.JpaInitializedEvent event) {
        org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.LOGGER.info("JPA initialized event received: {}", event);
        jpaStarted.set(true);
        instance = loadInstance();
    }

    @java.lang.Override
    public T get() {
        org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.LOGGER.debug("Getting {} configuration...", configurationCategory.getCategoryName());
        if (instance == null) {
            instance = loadInstance();
        }
        return instance;
    }

    private T loadInstance() {
        if (jpaStarted.get()) {
            org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.LOGGER.info("Loading {} configuration data", configurationCategory.getCategoryName());
            T instance = loadInstance(ambariConfigurationDAOProvider.get().findByCategory(configurationCategory.getCategoryName()));
            encryptor.decryptSensitiveData(instance);
            return instance;
        } else {
            org.apache.ambari.server.configuration.AmbariServerConfigurationProvider.LOGGER.info("Cannot load {} configuration data since JPA is not initialized", configurationCategory.getCategoryName());
            if (instance == null) {
                return loadInstance(java.util.Collections.emptyList());
            } else {
                return instance;
            }
        }
    }

    protected java.util.Map<java.lang.String, java.lang.String> toProperties(java.util.Collection<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> configurationEntities) {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        if (configurationEntities != null) {
            for (org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity : configurationEntities) {
                map.put(entity.getPropertyName(), entity.getPropertyValue());
            }
        }
        return map;
    }

    protected abstract T loadInstance(java.util.Collection<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> configurationEntities);
}