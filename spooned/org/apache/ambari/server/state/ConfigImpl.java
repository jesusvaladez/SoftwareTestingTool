package org.apache.ambari.server.state;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
public class ConfigImpl implements org.apache.ambari.server.state.Config {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.ConfigImpl.class);

    private static final java.lang.String PROPERTY_LOCK_LABEL = "configurationPropertyLock";

    public static final java.lang.String GENERATED_TAG_PREFIX = "generatedTag_";

    private final long configId;

    private final org.apache.ambari.server.state.Cluster cluster;

    private final org.apache.ambari.server.state.StackId stackId;

    private final java.lang.String type;

    private final java.lang.String tag;

    private final java.lang.Long version;

    private java.util.Map<java.lang.String, java.lang.String> properties;

    private final java.util.concurrent.locks.ReadWriteLock propertyLock;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes;

    private java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes;

    private final org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private final com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @com.google.inject.assistedinject.AssistedInject
    ConfigImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, @com.google.inject.assistedinject.Assisted("tag")
    @javax.annotation.Nullable
    java.lang.String tag, @com.google.inject.assistedinject.Assisted
    java.util.Map<java.lang.String, java.lang.String> properties, @com.google.inject.assistedinject.Assisted
    @javax.annotation.Nullable
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.StackDAO stackDAO, com.google.gson.Gson gson, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher, org.apache.ambari.server.logging.LockFactory lockFactory, @com.google.inject.name.Named("ConfigPropertiesEncryptor")
    org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config> configPropertiesEncryptor) {
        this(cluster.getDesiredStackVersion(), type, cluster, tag, properties, propertiesAttributes, clusterDAO, stackDAO, gson, eventPublisher, lockFactory, true, configPropertiesEncryptor);
    }

    @com.google.inject.assistedinject.AssistedInject
    ConfigImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.StackId stackId, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, @com.google.inject.assistedinject.Assisted("tag")
    @javax.annotation.Nullable
    java.lang.String tag, @com.google.inject.assistedinject.Assisted
    java.util.Map<java.lang.String, java.lang.String> properties, @com.google.inject.assistedinject.Assisted
    @javax.annotation.Nullable
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.StackDAO stackDAO, com.google.gson.Gson gson, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher, org.apache.ambari.server.logging.LockFactory lockFactory, @com.google.inject.name.Named("ConfigPropertiesEncryptor")
    org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config> configPropertiesEncryptor) {
        this(stackId, type, cluster, tag, properties, propertiesAttributes, clusterDAO, stackDAO, gson, eventPublisher, lockFactory, true, configPropertiesEncryptor);
    }

    @com.google.inject.assistedinject.AssistedInject
    ConfigImpl(@com.google.inject.assistedinject.Assisted
    @javax.annotation.Nullable
    org.apache.ambari.server.state.StackId stackId, @com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("tag")
    @javax.annotation.Nullable
    java.lang.String tag, @com.google.inject.assistedinject.Assisted
    java.util.Map<java.lang.String, java.lang.String> properties, @com.google.inject.assistedinject.Assisted
    @javax.annotation.Nullable
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.StackDAO stackDAO, com.google.gson.Gson gson, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher, org.apache.ambari.server.logging.LockFactory lockFactory, @com.google.inject.assistedinject.Assisted
    boolean refreshCluster, @com.google.inject.name.Named("ConfigPropertiesEncryptor")
    org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config> configPropertiesEncryptor) {
        propertyLock = lockFactory.newReadWriteLock(org.apache.ambari.server.state.ConfigImpl.PROPERTY_LOCK_LABEL);
        this.cluster = cluster;
        this.type = type;
        this.properties = properties;
        configPropertiesEncryptor.encryptSensitiveData(this);
        this.propertiesAttributes = (null == propertiesAttributes) ? null : new java.util.HashMap<>(propertiesAttributes);
        this.clusterDAO = clusterDAO;
        this.gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
        this.eventPublisher = eventPublisher;
        version = cluster.getNextConfigVersion(type);
        tag = (org.apache.commons.lang.StringUtils.isBlank(tag)) ? java.util.UUID.randomUUID().toString() : tag;
        this.tag = tag;
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(cluster.getClusterId());
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.apache.ambari.server.orm.entities.ClusterConfigEntity entity = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        entity.setClusterEntity(clusterEntity);
        entity.setClusterId(cluster.getClusterId());
        entity.setType(type);
        entity.setVersion(version);
        entity.setTag(this.tag);
        entity.setTimestamp(java.lang.System.currentTimeMillis());
        entity.setStack(stackEntity);
        entity.setData(this.gson.toJson(this.properties));
        if (null != propertiesAttributes) {
            entity.setAttributes(this.gson.toJson(propertiesAttributes));
        }
        this.stackId = stackId;
        propertiesTypes = cluster.getConfigPropertiesTypes(type);
        persist(entity, refreshCluster);
        configId = entity.getConfigId();
        configPropertiesEncryptor.decryptSensitiveData(this);
    }

    @com.google.inject.assistedinject.AssistedInject
    ConfigImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.ClusterConfigEntity entity, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, com.google.gson.Gson gson, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher, org.apache.ambari.server.logging.LockFactory lockFactory, @com.google.inject.name.Named("ConfigPropertiesEncryptor")
    org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config> configPropertiesEncryptor) {
        propertyLock = lockFactory.newReadWriteLock(org.apache.ambari.server.state.ConfigImpl.PROPERTY_LOCK_LABEL);
        this.cluster = cluster;
        this.clusterDAO = clusterDAO;
        this.gson = gson;
        this.eventPublisher = eventPublisher;
        configId = entity.getConfigId();
        type = entity.getType();
        tag = entity.getTag();
        version = entity.getVersion();
        stackId = new org.apache.ambari.server.state.StackId(entity.getStack());
        propertiesTypes = cluster.getConfigPropertiesTypes(type);
        try {
            java.util.Map<java.lang.String, java.lang.String> deserializedProperties = gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(entity.getData(), java.util.Map.class);
            if (null == deserializedProperties) {
                deserializedProperties = new java.util.HashMap<>();
            }
            properties = deserializedProperties;
            configPropertiesEncryptor.decryptSensitiveData(this);
        } catch (com.google.gson.JsonSyntaxException e) {
            org.apache.ambari.server.state.ConfigImpl.LOG.error("Malformed configuration JSON stored in the database for {}/{}", entity.getType(), entity.getTag());
        }
        try {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> deserializedAttributes = gson.<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>fromJson(entity.getAttributes(), java.util.Map.class);
            if (null != deserializedAttributes) {
                propertiesAttributes = new java.util.HashMap<>(deserializedAttributes);
            }
        } catch (com.google.gson.JsonSyntaxException e) {
            org.apache.ambari.server.state.ConfigImpl.LOG.error("Malformed configuration attribute JSON stored in the database for {}/{}", entity.getType(), entity.getTag());
        }
    }

    @com.google.inject.assistedinject.AssistedInject
    ConfigImpl(@com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, @com.google.inject.assistedinject.Assisted("tag")
    @javax.annotation.Nullable
    java.lang.String tag, @com.google.inject.assistedinject.Assisted
    java.util.Map<java.lang.String, java.lang.String> properties, @com.google.inject.assistedinject.Assisted
    @javax.annotation.Nullable
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, com.google.gson.Gson gson, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher, org.apache.ambari.server.logging.LockFactory lockFactory) {
        propertyLock = lockFactory.newReadWriteLock(org.apache.ambari.server.state.ConfigImpl.PROPERTY_LOCK_LABEL);
        this.tag = tag;
        this.type = type;
        this.properties = new java.util.HashMap<>(properties);
        this.propertiesAttributes = (null == propertiesAttributes) ? null : new java.util.HashMap<>(propertiesAttributes);
        this.clusterDAO = clusterDAO;
        this.gson = gson;
        this.eventPublisher = eventPublisher;
        cluster = null;
        configId = 0;
        version = 0L;
        stackId = null;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.StackId getStackId() {
        return stackId;
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getPropertiesTypes() {
        return propertiesTypes;
    }

    @java.lang.Override
    public void setPropertiesTypes(java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes) {
        this.propertiesTypes = propertiesTypes;
    }

    @java.lang.Override
    public java.lang.String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String getTag() {
        return tag;
    }

    @java.lang.Override
    public java.lang.Long getVersion() {
        return version;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        propertyLock.readLock().lock();
        try {
            return properties == null ? new java.util.HashMap<>() : new java.util.HashMap<>(properties);
        } finally {
            propertyLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getPropertiesAttributes() {
        return null == propertiesAttributes ? null : new java.util.HashMap<>(propertiesAttributes);
    }

    @java.lang.Override
    public void setProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
        propertyLock.writeLock().lock();
        try {
            this.properties = properties;
        } finally {
            propertyLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public void setPropertiesAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes) {
        this.propertiesAttributes = propertiesAttributes;
    }

    @java.lang.Override
    public void updateProperties(java.util.Map<java.lang.String, java.lang.String> propertiesToUpdate) {
        propertyLock.writeLock().lock();
        try {
            properties.putAll(propertiesToUpdate);
        } finally {
            propertyLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Cluster getCluster() {
        return cluster;
    }

    @java.lang.Override
    public java.util.List<java.lang.Long> getServiceConfigVersions() {
        return serviceConfigDAO.getServiceConfigVersionsByConfig(cluster.getClusterId(), type, version);
    }

    @java.lang.Override
    public void deleteProperties(java.util.List<java.lang.String> propertyKeysToRemove) {
        propertyLock.writeLock().lock();
        try {
            java.util.Set<java.lang.String> keySet = properties.keySet();
            keySet.removeAll(propertyKeysToRemove);
        } finally {
            propertyLock.writeLock().unlock();
        }
    }

    private void persist(org.apache.ambari.server.orm.entities.ClusterConfigEntity entity, boolean refreshCluster) {
        persistEntitiesInTransaction(entity);
        cluster.addConfig(this);
        if (refreshCluster) {
            cluster.refresh();
        }
        org.apache.ambari.server.events.ClusterConfigChangedEvent event = new org.apache.ambari.server.events.ClusterConfigChangedEvent(cluster.getClusterName(), getType(), getTag(), getVersion());
        eventPublisher.publish(event);
    }

    @com.google.inject.persist.Transactional
    void persistEntitiesInTransaction(org.apache.ambari.server.orm.entities.ClusterConfigEntity entity) {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = entity.getClusterEntity();
        clusterDAO.createConfig(entity);
        clusterEntity.getClusterConfigEntities().add(entity);
        clusterEntity = clusterDAO.merge(clusterEntity, true);
        org.apache.ambari.server.state.ConfigImpl.LOG.info("Persisted config entity with id {} and cluster entity {}", entity.getConfigId(), clusterEntity.toString());
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void save() {
        org.apache.ambari.server.orm.entities.ClusterConfigEntity entity = clusterDAO.findConfig(configId);
        if (null != entity) {
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(entity.getClusterId());
            org.apache.ambari.server.state.ConfigImpl.LOG.debug("Updating {} version {} with new configurations; a new version will not be created", getType(), getVersion());
            entity.setData(gson.toJson(getProperties()));
            clusterDAO.merge(clusterEntity, true);
            cluster.refresh();
            org.apache.ambari.server.events.ClusterConfigChangedEvent event = new org.apache.ambari.server.events.ClusterConfigChangedEvent(cluster.getClusterName(), getType(), getTag(), getVersion());
            eventPublisher.publish(event);
        }
    }
}