package org.apache.ambari.server.state;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
@com.google.inject.Singleton
public class ConfigHelper {
    private org.apache.ambari.server.state.Clusters clusters = null;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = null;

    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = null;

    private static final java.lang.String DELETED = "DELETED_";

    public static final java.lang.String CLUSTER_DEFAULT_TAG = "tag";

    private final boolean STALE_CONFIGS_CACHE_ENABLED;

    private final int STALE_CONFIGS_CACHE_EXPIRATION_TIME;

    private final com.google.common.cache.Cache<java.lang.Integer, java.lang.Boolean> staleConfigsCache;

    private final java.util.Map<java.lang.Long, java.util.Map<java.lang.Long, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Boolean>>>> stateCache = new java.util.HashMap<>();

    private final com.google.common.cache.Cache<java.lang.Integer, java.lang.String> refreshConfigCommandCache;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.ConfigHelper.class);

    public static final java.lang.String HBASE_SITE = "hbase-site";

    public static final java.lang.String HDFS_SITE = "hdfs-site";

    public static final java.lang.String HIVE_SITE = "hive-site";

    public static final java.lang.String YARN_SITE = "yarn-site";

    public static final java.lang.String CLUSTER_ENV = "cluster-env";

    public static final java.lang.String CLUSTER_ENV_ALERT_REPEAT_TOLERANCE = "alerts_repeat_tolerance";

    public static final java.lang.String CLUSTER_ENV_RETRY_ENABLED = "command_retry_enabled";

    public static final java.lang.String SERVICE_CHECK_TYPE = "service_check_type";

    public static final java.lang.String CLUSTER_ENV_RETRY_COMMANDS = "commands_to_retry";

    public static final java.lang.String CLUSTER_ENV_RETRY_MAX_TIME_IN_SEC = "command_retry_max_time_in_sec";

    public static final java.lang.String COMMAND_RETRY_MAX_TIME_IN_SEC_DEFAULT = "600";

    public static final java.lang.String CLUSTER_ENV_STACK_NAME_PROPERTY = "stack_name";

    public static final java.lang.String CLUSTER_ENV_STACK_FEATURES_PROPERTY = "stack_features";

    public static final java.lang.String CLUSTER_ENV_STACK_TOOLS_PROPERTY = "stack_tools";

    public static final java.lang.String CLUSTER_ENV_STACK_ROOT_PROPERTY = "stack_root";

    public static final java.lang.String CLUSTER_ENV_STACK_PACKAGES_PROPERTY = "stack_packages";

    public static final java.lang.String HTTP_ONLY = "HTTP_ONLY";

    public static final java.lang.String HTTPS_ONLY = "HTTPS_ONLY";

    public static final java.lang.String SERVICE_CHECK_MINIMAL = "minimal";

    public static final java.lang.String FIRST_VERSION_TAG = "version1";

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.MetadataHolder> m_metadataHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.AgentConfigsHolder> m_agentConfigsHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.controller.AmbariManagementControllerImpl> m_ambariManagementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    @com.google.inject.Inject
    public ConfigHelper(org.apache.ambari.server.state.Clusters c, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO) {
        clusters = c;
        ambariMetaInfo = metaInfo;
        this.clusterDAO = clusterDAO;
        STALE_CONFIGS_CACHE_ENABLED = configuration.isStaleConfigCacheEnabled();
        STALE_CONFIGS_CACHE_EXPIRATION_TIME = configuration.staleConfigCacheExpiration();
        staleConfigsCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(STALE_CONFIGS_CACHE_EXPIRATION_TIME, java.util.concurrent.TimeUnit.SECONDS).build();
        refreshConfigCommandCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(STALE_CONFIGS_CACHE_EXPIRATION_TIME, java.util.concurrent.TimeUnit.SECONDS).build();
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getEffectiveDesiredTags(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        return getEffectiveDesiredTags(cluster, hostName, null);
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getEffectiveDesiredTags(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host host = null;
        if (hostName != null) {
            try {
                host = clusters.getHost(hostName);
            } catch (org.apache.ambari.server.HostNotFoundException e) {
                org.apache.ambari.server.state.ConfigHelper.LOG.error("Cannot get desired config for unknown host {}", hostName, e);
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> desiredHostConfigs = (host == null) ? null : host.getDesiredHostConfigs(cluster, desiredConfigs);
        return getEffectiveDesiredTags(cluster, desiredConfigs, desiredHostConfigs);
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getEffectiveDesiredTags(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterDesired, java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> hostConfigOverrides) {
        if (null == cluster) {
            clusterDesired = new java.util.HashMap<>();
        }
        if (null == clusterDesired) {
            clusterDesired = cluster.getDesiredConfigs();
        }
        if (null == clusterDesired) {
            clusterDesired = new java.util.HashMap<>();
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> resolved = new java.util.TreeMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterEntry : clusterDesired.entrySet()) {
            java.lang.String type = clusterEntry.getKey();
            java.lang.String tag = clusterEntry.getValue().getTag();
            if (cluster != null) {
                org.apache.ambari.server.state.Config config = cluster.getConfig(type, tag);
                if (null == config) {
                    continue;
                }
                java.util.Map<java.lang.String, java.lang.String> tags = new java.util.LinkedHashMap<>();
                tags.put(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG, config.getTag());
                if (hostConfigOverrides != null) {
                    org.apache.ambari.server.state.HostConfig hostConfig = hostConfigOverrides.get(config.getType());
                    if (hostConfig != null) {
                        for (java.util.Map.Entry<java.lang.Long, java.lang.String> tagEntry : hostConfig.getConfigGroupOverrides().entrySet()) {
                            tags.put(tagEntry.getKey().toString(), tagEntry.getValue());
                        }
                    }
                }
                resolved.put(type, tags);
            }
        }
        return resolved;
    }

    public java.util.Set<java.lang.String> filterInvalidPropertyValues(java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> properties, java.lang.String filteredListName) {
        java.util.Set<java.lang.String> resultSet = new java.util.HashSet<>();
        for (java.util.Iterator<java.util.Map.Entry<org.apache.ambari.server.state.PropertyInfo, java.lang.String>> iterator = properties.entrySet().iterator(); iterator.hasNext();) {
            java.util.Map.Entry<org.apache.ambari.server.state.PropertyInfo, java.lang.String> property = iterator.next();
            org.apache.ambari.server.state.PropertyInfo propertyInfo = property.getKey();
            java.lang.String propertyValue = property.getValue();
            if (((propertyValue == null) || propertyValue.toLowerCase().equals("null")) || propertyValue.isEmpty()) {
                org.apache.ambari.server.state.ConfigHelper.LOG.error(java.lang.String.format("Excluding property %s from %s, because of invalid or empty value!", propertyInfo.getName(), filteredListName));
                iterator.remove();
            } else {
                resultSet.add(propertyValue);
            }
        }
        return resultSet;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getEffectiveConfigProperties(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> desiredTags) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        if (desiredTags != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : desiredTags.entrySet()) {
                java.lang.String type = entry.getKey();
                java.util.Map<java.lang.String, java.lang.String> propertyMap = properties.get(type);
                if (propertyMap == null) {
                    propertyMap = new java.util.HashMap<>();
                }
                java.util.Map<java.lang.String, java.lang.String> tags = new java.util.HashMap<>(entry.getValue());
                java.lang.String clusterTag = tags.get(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG);
                if (clusterTag != null) {
                    org.apache.ambari.server.state.Config config = cluster.getConfig(type, clusterTag);
                    if (config != null) {
                        propertyMap.putAll(config.getProperties());
                    }
                    tags.remove(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG);
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> overrideEntry : tags.entrySet()) {
                        org.apache.ambari.server.state.Config overrideConfig = cluster.getConfig(type, overrideEntry.getValue());
                        if (overrideConfig != null) {
                            propertyMap = getMergedConfig(propertyMap, overrideConfig.getProperties());
                        }
                    }
                }
                properties.put(type, propertyMap);
            }
        }
        return properties;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getEffectiveConfigProperties(java.lang.String clusterName, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        return getEffectiveConfigProperties(cluster, getEffectiveDesiredTags(cluster, hostName));
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getEffectiveConfigAttributes(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> desiredTags) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        if (desiredTags != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : desiredTags.entrySet()) {
                java.lang.String type = entry.getKey();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributesMap = null;
                java.util.Map<java.lang.String, java.lang.String> tags = new java.util.HashMap<>(entry.getValue());
                java.lang.String clusterTag = tags.get(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG);
                if (clusterTag != null) {
                    org.apache.ambari.server.state.Config config = cluster.getConfig(type, clusterTag);
                    if (config != null) {
                        attributesMap = new java.util.TreeMap<>();
                        cloneAttributesMap(config.getPropertiesAttributes(), attributesMap);
                    }
                    tags.remove(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG);
                }
                for (java.util.Map.Entry<java.lang.String, java.lang.String> overrideEntry : tags.entrySet()) {
                    org.apache.ambari.server.state.Config overrideConfig = cluster.getConfig(type, overrideEntry.getValue());
                    overrideAttributes(overrideConfig, attributesMap);
                }
                if (attributesMap != null) {
                    attributes.put(type, attributesMap);
                }
            }
        }
        return attributes;
    }

    public java.util.Map<java.lang.String, java.lang.String> getMergedConfig(java.util.Map<java.lang.String, java.lang.String> persistedClusterConfig, java.util.Map<java.lang.String, java.lang.String> override) {
        java.util.Map<java.lang.String, java.lang.String> finalConfig = new java.util.HashMap<>(persistedClusterConfig);
        if ((override != null) && (override.size() > 0)) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : override.entrySet()) {
                java.lang.Boolean deleted = 0 == entry.getKey().indexOf(org.apache.ambari.server.state.ConfigHelper.DELETED);
                java.lang.String nameToUse = (deleted) ? entry.getKey().substring(org.apache.ambari.server.state.ConfigHelper.DELETED.length()) : entry.getKey();
                if (finalConfig.containsKey(nameToUse)) {
                    finalConfig.remove(nameToUse);
                }
                if (!deleted) {
                    finalConfig.put(nameToUse, entry.getValue());
                }
            }
        }
        return finalConfig;
    }

    public void getAndMergeHostConfigs(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationTags, org.apache.ambari.server.state.Cluster cluster) {
        if ((null != configurationTags) && (!configurationTags.isEmpty())) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = getEffectiveConfigProperties(cluster, configurationTags);
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : configProperties.entrySet()) {
                java.lang.String type = entry.getKey();
                java.util.Map<java.lang.String, java.lang.String> allLevelMergedConfig = entry.getValue();
                if (configurations.containsKey(type)) {
                    java.util.Map<java.lang.String, java.lang.String> mergedConfig = getMergedConfig(allLevelMergedConfig, configurations.get(type));
                    configurations.get(type).clear();
                    configurations.get(type).putAll(mergedConfig);
                } else {
                    configurations.put(type, new java.util.HashMap<>());
                    configurations.get(type).putAll(allLevelMergedConfig);
                }
            }
        }
    }

    public void getAndMergeHostConfigAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationTags, org.apache.ambari.server.state.Cluster cluster) {
        if ((null != configurationTags) && (!configurationTags.isEmpty())) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configAttributes = getEffectiveConfigAttributes(cluster, configurationTags);
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributesOccurrence : configAttributes.entrySet()) {
                java.lang.String type = attributesOccurrence.getKey();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = attributesOccurrence.getValue();
                if (configurationAttributes != null) {
                    if (!configurationAttributes.containsKey(type)) {
                        configurationAttributes.put(type, new java.util.TreeMap<>());
                    }
                    cloneAttributesMap(attributes, configurationAttributes.get(type));
                }
            }
        }
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> overrideAttributes(org.apache.ambari.server.state.Config overrideConfig, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> persistedAttributes) {
        if ((overrideConfig != null) && (persistedAttributes != null)) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> overrideAttributes = overrideConfig.getPropertiesAttributes();
            if (overrideAttributes != null) {
                cloneAttributesMap(overrideAttributes, persistedAttributes);
                java.util.Map<java.lang.String, java.lang.String> overrideProperties = overrideConfig.getProperties();
                if (overrideProperties != null) {
                    java.util.Set<java.lang.String> overriddenProperties = overrideProperties.keySet();
                    for (java.lang.String overriddenProperty : overriddenProperties) {
                        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> persistedAttribute : persistedAttributes.entrySet()) {
                            java.lang.String attributeName = persistedAttribute.getKey();
                            java.util.Map<java.lang.String, java.lang.String> persistedAttributeValues = persistedAttribute.getValue();
                            java.util.Map<java.lang.String, java.lang.String> overrideAttributeValues = overrideAttributes.get(attributeName);
                            if ((overrideAttributeValues == null) || (!overrideAttributeValues.containsKey(overriddenProperty))) {
                                persistedAttributeValues.remove(overriddenProperty);
                            }
                        }
                    }
                }
            }
        }
        return persistedAttributes;
    }

    public void cloneAttributesMap(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> sourceAttributesMap, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> targetAttributesMap) {
        if ((sourceAttributesMap != null) && (targetAttributesMap != null)) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributesEntry : sourceAttributesMap.entrySet()) {
                java.lang.String attributeName = attributesEntry.getKey();
                if (!targetAttributesMap.containsKey(attributeName)) {
                    targetAttributesMap.put(attributeName, new java.util.TreeMap<>());
                }
                for (java.util.Map.Entry<java.lang.String, java.lang.String> attributesValue : attributesEntry.getValue().entrySet()) {
                    targetAttributesMap.get(attributeName).put(attributesValue.getKey(), attributesValue.getValue());
                }
            }
        }
    }

    public void applyCustomConfig(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String type, java.lang.String name, java.lang.String value, java.lang.Boolean deleted) {
        if (!configurations.containsKey(type)) {
            configurations.put(type, new java.util.HashMap<>());
        }
        java.lang.String nameToUse = (deleted) ? org.apache.ambari.server.state.ConfigHelper.DELETED + name : name;
        java.util.Map<java.lang.String, java.lang.String> properties = configurations.get(type);
        if (properties.containsKey(nameToUse)) {
            properties.remove(nameToUse);
        }
        properties.put(nameToUse, value);
    }

    public boolean isStaleConfigs(org.apache.ambari.server.state.ServiceComponentHost sch, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> requestDesiredConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity = sch.getDesiredStateEntity();
        return isStaleConfigs(sch, requestDesiredConfigs, hostComponentDesiredStateEntity);
    }

    public boolean isStaleConfigs(org.apache.ambari.server.state.ServiceComponentHost sch, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> requestDesiredConfigs, org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity) throws org.apache.ambari.server.AmbariException {
        boolean stale = calculateIsStaleConfigs(sch, requestDesiredConfigs, hostComponentDesiredStateEntity);
        if (org.apache.ambari.server.state.ConfigHelper.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.ConfigHelper.LOG.debug("Cache configuration staleness for host {} and component {} as {}", sch.getHostName(), sch.getServiceComponentName(), stale);
        }
        return stale;
    }

    @com.google.inject.persist.Transactional
    public void removeConfigsByType(org.apache.ambari.server.state.Cluster cluster, java.lang.String type) {
        java.util.Set<java.lang.String> globalVersions = cluster.getConfigsByType(type).keySet();
        for (java.lang.String version : globalVersions) {
            org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity = clusterDAO.findConfig(cluster.getClusterId(), type, version);
            clusterDAO.removeConfig(clusterConfigEntity);
        }
    }

    public java.util.Set<java.lang.String> findConfigTypesByPropertyName(org.apache.ambari.server.state.StackId stackId, java.lang.String propertyName, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
        java.util.Set<java.lang.String> result = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Service service : clusters.getCluster(clusterName).getServices().values()) {
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = ambariMetaInfo.getServiceProperties(stack.getName(), stack.getVersion(), service.getName());
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackLevelProperties = ambariMetaInfo.getStackProperties(stack.getName(), stack.getVersion());
            stackProperties.addAll(stackLevelProperties);
            for (org.apache.ambari.server.state.PropertyInfo stackProperty : stackProperties) {
                if (stackProperty.getName().equals(propertyName)) {
                    java.lang.String configType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(stackProperty.getFilename());
                    result.add(configType);
                }
            }
        }
        return result;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getCredentialStoreEnabledProperties(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.Service service) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.PropertyInfo.PropertyType propertyType = org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD;
        org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> result = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> passwordProperties;
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = ambariMetaInfo.getServiceProperties(stack.getName(), stack.getVersion(), service.getName());
        for (org.apache.ambari.server.state.PropertyInfo serviceProperty : serviceProperties) {
            if (serviceProperty.getPropertyTypes().contains(propertyType)) {
                if (!serviceProperty.getPropertyValueAttributes().isKeyStore()) {
                    continue;
                }
                java.lang.String stackPropertyConfigType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(serviceProperty.getFilename());
                passwordProperties = result.get(stackPropertyConfigType);
                if (passwordProperties == null) {
                    passwordProperties = new java.util.HashMap<>();
                    result.put(stackPropertyConfigType, passwordProperties);
                }
                if (serviceProperty.getUsedByProperties().size() > 0) {
                    for (org.apache.ambari.server.state.PropertyDependencyInfo usedByProperty : serviceProperty.getUsedByProperties()) {
                        java.lang.String propertyName = usedByProperty.getName();
                        if (!org.apache.commons.lang.StringUtils.isEmpty(usedByProperty.getType())) {
                            propertyName += ':' + usedByProperty.getType();
                        }
                        passwordProperties.put(propertyName, serviceProperty.getName());
                    }
                } else {
                    passwordProperties.put(serviceProperty.getName(), serviceProperty.getName());
                }
            }
        }
        return result;
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> createUserGroupsMap(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesMap = ambariMetaInfo.getServices(stack.getName(), stack.getVersion());
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = ambariMetaInfo.getStackProperties(stack.getName(), stack.getVersion());
        return createUserGroupsMap(cluster, desiredConfigs, servicesMap, stackProperties);
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> createUserGroupsMap(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesMap, java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap = new java.util.HashMap<>();
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> userProperties = getPropertiesWithPropertyType(org.apache.ambari.server.state.PropertyInfo.PropertyType.USER, cluster, desiredConfigs, servicesMap, stackProperties);
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> groupProperties = getPropertiesWithPropertyType(org.apache.ambari.server.state.PropertyInfo.PropertyType.GROUP, cluster, desiredConfigs, servicesMap, stackProperties);
        if ((userProperties != null) && (groupProperties != null)) {
            for (java.util.Map.Entry<org.apache.ambari.server.state.PropertyInfo, java.lang.String> userProperty : userProperties.entrySet()) {
                org.apache.ambari.server.state.PropertyInfo userPropertyInfo = userProperty.getKey();
                java.lang.String userPropertyValue = userProperty.getValue();
                if ((userPropertyInfo.getPropertyValueAttributes() != null) && (userPropertyInfo.getPropertyValueAttributes().getUserGroupEntries() != null)) {
                    java.util.Set<java.lang.String> groupPropertyValues = new java.util.HashSet<>();
                    java.util.Collection<org.apache.ambari.server.state.UserGroupInfo> userGroupEntries = userPropertyInfo.getPropertyValueAttributes().getUserGroupEntries();
                    for (org.apache.ambari.server.state.UserGroupInfo userGroupInfo : userGroupEntries) {
                        boolean found = false;
                        for (java.util.Map.Entry<org.apache.ambari.server.state.PropertyInfo, java.lang.String> groupProperty : groupProperties.entrySet()) {
                            org.apache.ambari.server.state.PropertyInfo groupPropertyInfo = groupProperty.getKey();
                            java.lang.String groupPropertyValue = groupProperty.getValue();
                            if (org.apache.commons.lang.StringUtils.equals(userGroupInfo.getType(), org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(groupPropertyInfo.getFilename())) && org.apache.commons.lang.StringUtils.equals(userGroupInfo.getName(), groupPropertyInfo.getName())) {
                                groupPropertyValues.add(groupPropertyValue);
                                found = true;
                            }
                        }
                        if (!found) {
                            org.apache.ambari.server.state.ConfigHelper.LOG.error(((((((((("User group mapping property {" + userGroupInfo.getType()) + "/") + userGroupInfo.getName()) + "} is missing for user property {") + org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(userPropertyInfo.getFilename())) + "/") + userPropertyInfo.getName()) + "} (username = ") + userPropertyInfo.getValue()) + ")");
                        }
                    }
                    userGroupsMap.put(userPropertyValue, groupPropertyValues);
                }
            }
        }
        return userGroupsMap;
    }

    public java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> getPropertiesWithPropertyType(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType propertyType, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesMap = ambariMetaInfo.getServices(stack.getName(), stack.getVersion());
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = ambariMetaInfo.getStackProperties(stack.getName(), stack.getVersion());
        return getPropertiesWithPropertyType(propertyType, cluster, desiredConfigs, servicesMap, stackProperties);
    }

    public java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> getPropertiesWithPropertyType(org.apache.ambari.server.state.PropertyInfo.PropertyType propertyType, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesMap, java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> actualConfigs = new java.util.HashMap<>();
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> result = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigEntry : desiredConfigs.entrySet()) {
            java.lang.String configType = desiredConfigEntry.getKey();
            org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigEntry.getValue();
            actualConfigs.put(configType, cluster.getConfig(configType, desiredConfig.getTag()));
        }
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.apache.ambari.server.state.ServiceInfo serviceInfo = servicesMap.get(service.getName());
            if (serviceInfo == null) {
                continue;
            }
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = new java.util.HashSet<>(serviceInfo.getProperties());
            for (org.apache.ambari.server.state.PropertyInfo serviceProperty : serviceProperties) {
                if (serviceProperty.getPropertyTypes().contains(propertyType)) {
                    java.lang.String stackPropertyConfigType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(serviceProperty.getFilename());
                    try {
                        java.lang.String property = actualConfigs.get(stackPropertyConfigType).getProperties().get(serviceProperty.getName());
                        if (null == property) {
                            org.apache.ambari.server.state.ConfigHelper.LOG.error(java.lang.String.format("Unable to obtain property values for %s with property attribute %s. " + "The property does not exist in version %s of %s configuration.", serviceProperty.getName(), propertyType, desiredConfigs.get(stackPropertyConfigType), stackPropertyConfigType));
                        } else {
                            result.put(serviceProperty, property);
                        }
                    } catch (java.lang.Exception ignored) {
                    }
                }
            }
        }
        for (org.apache.ambari.server.state.PropertyInfo stackProperty : stackProperties) {
            if (stackProperty.getPropertyTypes().contains(propertyType)) {
                java.lang.String stackPropertyConfigType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(stackProperty.getFilename());
                if (actualConfigs.containsKey(stackPropertyConfigType)) {
                    result.put(stackProperty, actualConfigs.get(stackPropertyConfigType).getProperties().get(stackProperty.getName()));
                }
            }
        }
        return result;
    }

    public java.util.Set<java.lang.String> getPropertyValuesWithPropertyType(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType propertyType, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesMap = ambariMetaInfo.getServices(stack.getName(), stack.getVersion());
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = ambariMetaInfo.getStackProperties(stack.getName(), stack.getVersion());
        return getPropertyValuesWithPropertyType(propertyType, cluster, desiredConfigs, servicesMap, stackProperties);
    }

    public java.util.Set<java.lang.String> getPropertyValuesWithPropertyType(org.apache.ambari.server.state.PropertyInfo.PropertyType propertyType, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesMap, java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> actualConfigs = new java.util.HashMap<>();
        java.util.Set<java.lang.String> result = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigEntry : desiredConfigs.entrySet()) {
            java.lang.String configType = desiredConfigEntry.getKey();
            org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigEntry.getValue();
            actualConfigs.put(configType, cluster.getConfig(configType, desiredConfig.getTag()));
        }
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            if (!servicesMap.containsKey(service.getName())) {
                continue;
            }
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = new java.util.HashSet<>(servicesMap.get(service.getName()).getProperties());
            for (org.apache.ambari.server.state.PropertyInfo serviceProperty : serviceProperties) {
                if (serviceProperty.getPropertyTypes().contains(propertyType)) {
                    java.lang.String stackPropertyConfigType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(serviceProperty.getFilename());
                    try {
                        java.lang.String property = actualConfigs.get(stackPropertyConfigType).getProperties().get(serviceProperty.getName());
                        if (null == property) {
                            org.apache.ambari.server.state.ConfigHelper.LOG.error(java.lang.String.format("Unable to obtain property values for %s with property attribute %s. " + "The property does not exist in version %s of %s configuration.", serviceProperty.getName(), propertyType, desiredConfigs.get(stackPropertyConfigType), stackPropertyConfigType));
                        } else {
                            result.add(property);
                        }
                    } catch (java.lang.Exception ignored) {
                    }
                }
            }
        }
        for (org.apache.ambari.server.state.PropertyInfo stackProperty : stackProperties) {
            if (stackProperty.getPropertyTypes().contains(propertyType)) {
                java.lang.String stackPropertyConfigType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(stackProperty.getFilename());
                if (actualConfigs.containsKey(stackPropertyConfigType)) {
                    result.add(actualConfigs.get(stackPropertyConfigType).getProperties().get(stackProperty.getName()));
                }
            }
        }
        return result;
    }

    public void checkAllStageConfigsPresentInDesiredConfigs(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = cluster.getDesiredStackVersion();
        java.util.Set<java.lang.String> stackConfigTypes = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion()).getConfigTypeAttributes().keySet();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> actualConfigs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigEntry : desiredConfigs.entrySet()) {
            java.lang.String configType = desiredConfigEntry.getKey();
            org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigEntry.getValue();
            actualConfigs.put(configType, cluster.getConfig(configType, desiredConfig.getTag()));
        }
        for (java.lang.String stackConfigType : stackConfigTypes) {
            if (!actualConfigs.containsKey(stackConfigType)) {
                org.apache.ambari.server.state.ConfigHelper.LOG.error(java.lang.String.format("Unable to find stack configuration %s in ambari configs!", stackConfigType));
            }
        }
    }

    public java.lang.String getPropertyValueFromStackDefinitions(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyName) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.StackId> stackIds = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            stackIds.add(service.getDesiredStackId());
        }
        for (org.apache.ambari.server.state.StackId stackId : stackIds) {
            org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : stack.getServices()) {
                java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = ambariMetaInfo.getServiceProperties(stack.getName(), stack.getVersion(), serviceInfo.getName());
                java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = ambariMetaInfo.getStackProperties(stack.getName(), stack.getVersion());
                serviceProperties.addAll(stackProperties);
                for (org.apache.ambari.server.state.PropertyInfo stackProperty : serviceProperties) {
                    java.lang.String stackPropertyConfigType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(stackProperty.getFilename());
                    if (stackProperty.getName().equals(propertyName) && stackPropertyConfigType.equals(configType)) {
                        return stackProperty.getValue();
                    }
                }
            }
        }
        return null;
    }

    public java.lang.String getPlaceholderValueFromDesiredConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String placeholder) {
        if (placeholder.startsWith("{{") && placeholder.endsWith("}}")) {
            placeholder = placeholder.substring(2, placeholder.length() - 2).trim();
        }
        int delimiterPosition = placeholder.indexOf("/");
        if (delimiterPosition < 0) {
            return placeholder;
        }
        java.lang.String configType = placeholder.substring(0, delimiterPosition);
        java.lang.String propertyName = placeholder.substring(delimiterPosition + 1, placeholder.length());
        java.lang.String value = getValueFromDesiredConfigurations(cluster, configType, propertyName);
        return value != null ? value : placeholder;
    }

    public java.lang.String getValueFromDesiredConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        if (desiredConfig != null) {
            org.apache.ambari.server.state.Config config = cluster.getConfig(configType, desiredConfig.getTag());
            java.util.Map<java.lang.String, java.lang.String> configurationProperties = config.getProperties();
            if (null != configurationProperties) {
                java.lang.String value = configurationProperties.get(propertyName);
                if (null != value) {
                    return value;
                }
            }
        }
        return null;
    }

    public org.apache.ambari.server.state.ServiceInfo getPropertyOwnerService(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyName) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
            org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : stack.getServices()) {
                java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = ambariMetaInfo.getServiceProperties(stack.getName(), stack.getVersion(), serviceInfo.getName());
                for (org.apache.ambari.server.state.PropertyInfo stackProperty : serviceProperties) {
                    java.lang.String stackPropertyConfigType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(stackProperty.getFilename());
                    if (stackProperty.getName().equals(propertyName) && stackPropertyConfigType.equals(configType)) {
                        return serviceInfo;
                    }
                }
            }
        }
        return null;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo> getServiceProperties(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        return getServiceProperties(service.getDesiredStackId(), serviceName, false);
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo> getServiceProperties(org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName, boolean removeExcluded) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo service = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = new java.util.HashSet<>(service.getProperties());
        if (removeExcluded) {
            java.util.Set<java.lang.String> excludedConfigTypes = service.getExcludedConfigTypes();
            if ((excludedConfigTypes != null) && (!excludedConfigTypes.isEmpty())) {
                java.util.Iterator<org.apache.ambari.server.state.PropertyInfo> iterator = properties.iterator();
                while (iterator.hasNext()) {
                    org.apache.ambari.server.state.PropertyInfo propertyInfo = iterator.next();
                    if (excludedConfigTypes.contains(org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(propertyInfo.getFilename()))) {
                        iterator.remove();
                    }
                } 
            }
        }
        return properties;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo> getStackProperties(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.StackId> stackIds = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            stackIds.add(service.getDesiredStackId());
        }
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> propertySets = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.StackId stackId : stackIds) {
            org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
            propertySets.addAll(ambariMetaInfo.getStackProperties(stack.getName(), stack.getVersion()));
        }
        return propertySets;
    }

    public void updateConfigType(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController controller, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> updates, java.util.Collection<java.lang.String> removals, java.lang.String authenticatedUserName, java.lang.String serviceVersionNote) throws org.apache.ambari.server.AmbariException {
        updateConfigType(cluster, stackId, controller, configType, updates, removals, authenticatedUserName, serviceVersionNote, null, true);
    }

    public void updateBulkConfigType(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController controller, java.lang.Iterable<java.lang.String> configTypes, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> updates, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> removals, java.lang.String authenticatedUserName, java.lang.String serviceVersionNote) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.AmbariRuntimeException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfig = cluster.getDesiredConfigs();
        java.lang.Boolean[] doUpdateAgentConfigs = new java.lang.Boolean[]{ false };
        org.apache.ambari.server.state.ConfigHelper.LOG.info("Bulk config update. Starting...");
        configTypes.forEach(configType -> {
            try {
                java.lang.Boolean updated = updateConfigType(cluster, stackId, controller, configType, updates.get(configType), removals.get(configType), authenticatedUserName, serviceVersionNote, desiredConfig, false);
                org.apache.ambari.server.state.ConfigHelper.LOG.info("Bulk config update. Working with {}...{}", configType, updated ? "updated" : "not updated");
                doUpdateAgentConfigs[0] = doUpdateAgentConfigs[0] || updated;
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.AmbariRuntimeException(e);
            }
        });
        org.apache.ambari.server.state.ConfigHelper.LOG.info("Bulk config update, agent update is {} required", doUpdateAgentConfigs[0] ? "" : "not");
        if (doUpdateAgentConfigs[0]) {
            updateAgentConfigs(java.util.Collections.singleton(cluster.getClusterName()));
        }
    }

    public java.lang.Boolean updateConfigType(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController controller, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> updates, java.util.Collection<java.lang.String> removals, java.lang.String authenticatedUserName, java.lang.String serviceVersionNote, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfig, java.lang.Boolean doUpdateAgentConfigs) throws org.apache.ambari.server.AmbariException {
        if ((configType == null) || (((updates == null) || updates.isEmpty()) && ((removals == null) || removals.isEmpty()))) {
            return false;
        }
        org.apache.ambari.server.state.Config oldConfig = ((desiredConfig != null) && desiredConfig.containsKey(configType)) ? cluster.getConfig(configType, desiredConfig.get(configType).getTag()) : cluster.getDesiredConfigByType(configType);
        java.util.Map<java.lang.String, java.lang.String> oldConfigProperties;
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        if (oldConfig == null) {
            oldConfigProperties = null;
        } else {
            oldConfigProperties = oldConfig.getProperties();
            if (oldConfigProperties != null) {
                properties.putAll(oldConfigProperties);
            }
            if (oldConfig.getPropertiesAttributes() != null) {
                propertiesAttributes.putAll(oldConfig.getPropertiesAttributes());
            }
        }
        if (updates != null) {
            properties.putAll(updates);
        }
        if (removals != null) {
            for (java.lang.String propertyName : removals) {
                properties.remove(propertyName);
                for (java.util.Map<java.lang.String, java.lang.String> attributesMap : propertiesAttributes.values()) {
                    attributesMap.remove(propertyName);
                }
            }
        }
        if (((oldConfigProperties == null) || (!com.google.common.collect.Maps.difference(oldConfigProperties, properties).areEqual())) && createConfigType(cluster, stackId, controller, configType, properties, propertiesAttributes, authenticatedUserName, serviceVersionNote)) {
            if (doUpdateAgentConfigs) {
                updateAgentConfigs(java.util.Collections.singleton(cluster.getClusterName()));
            }
            return true;
        }
        return false;
    }

    public void createConfigType(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController controller, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String authenticatedUserName, java.lang.String serviceVersionNote) throws org.apache.ambari.server.AmbariException {
        if (createConfigType(cluster, stackId, controller, configType, properties, new java.util.HashMap<>(), authenticatedUserName, serviceVersionNote)) {
            updateAgentConfigs(java.util.Collections.singleton(cluster.getClusterName()));
        }
    }

    public boolean createConfigType(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController controller, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyAttributes, java.lang.String authenticatedUserName, java.lang.String serviceVersionNote) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config baseConfig = createConfig(cluster, stackId, controller, configType, org.apache.ambari.server.state.ConfigHelper.FIRST_VERSION_TAG, properties, propertyAttributes);
        if (baseConfig != null) {
            cluster.addDesiredConfig(authenticatedUserName, java.util.Collections.singleton(baseConfig), serviceVersionNote);
            return true;
        }
        return false;
    }

    public boolean createConfigTypes(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> batchProperties, java.lang.String authenticatedUserName, java.lang.String serviceVersionNote) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.Config>> serviceMapped = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : batchProperties.entrySet()) {
            java.lang.String type = entry.getKey();
            java.util.Map<java.lang.String, java.lang.String> properties = entry.getValue();
            org.apache.ambari.server.state.Config baseConfig = createConfig(cluster, stackId, controller, type, org.apache.ambari.server.state.ConfigHelper.FIRST_VERSION_TAG, properties, java.util.Collections.emptyMap());
            if (null != baseConfig) {
                try {
                    java.lang.String service = cluster.getServiceByConfigType(type);
                    if (!serviceMapped.containsKey(service)) {
                        serviceMapped.put(service, new java.util.HashSet<>());
                    }
                    serviceMapped.get(service).add(baseConfig);
                } catch (java.lang.Exception e) {
                }
            }
        }
        boolean added = false;
        for (java.util.Set<org.apache.ambari.server.state.Config> configs : serviceMapped.values()) {
            if (!configs.isEmpty()) {
                cluster.addDesiredConfig(authenticatedUserName, configs, serviceVersionNote);
                added = true;
            }
        }
        return added;
    }

    org.apache.ambari.server.state.Config createConfig(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController controller, java.lang.String type, java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyAttributes) throws org.apache.ambari.server.AmbariException {
        if (cluster.getConfigsByType(type) != null) {
            tag = "version" + java.lang.System.currentTimeMillis();
        }
        java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes = cluster.getConfigPropertiesTypes(type);
        if (propertiesTypes.containsKey(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
            for (java.lang.String passwordProperty : propertiesTypes.get(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
                if (properties.containsKey(passwordProperty)) {
                    java.lang.String passwordPropertyValue = properties.get(passwordProperty);
                    if (!org.apache.ambari.server.utils.SecretReference.isSecret(passwordPropertyValue)) {
                        continue;
                    }
                    org.apache.ambari.server.utils.SecretReference ref = new org.apache.ambari.server.utils.SecretReference(passwordPropertyValue, cluster);
                    java.lang.String refValue = ref.getValue();
                    properties.put(passwordProperty, refValue);
                }
            }
        }
        return controller.createConfig(cluster, stackId, type, properties, tag, propertyAttributes);
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getDefaultStackProperties(org.apache.ambari.server.state.StackId stack) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> defaultPropertiesByType = new java.util.HashMap<>();
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackConfigurationProperties = ambariMetaInfo.getStackProperties(stack.getStackName(), stack.getStackVersion());
        for (org.apache.ambari.server.state.PropertyInfo stackDefaultProperty : stackConfigurationProperties) {
            java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(stackDefaultProperty.getFilename());
            if (!defaultPropertiesByType.containsKey(type)) {
                defaultPropertiesByType.put(type, new java.util.HashMap<>());
            }
            defaultPropertiesByType.get(type).put(stackDefaultProperty.getName(), stackDefaultProperty.getValue());
        }
        return defaultPropertiesByType;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getDefaultProperties(org.apache.ambari.server.state.StackId stack, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> defaultPropertiesByType = new java.util.HashMap<>();
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackConfigurationProperties = ambariMetaInfo.getStackProperties(stack.getStackName(), stack.getStackVersion());
        for (org.apache.ambari.server.state.PropertyInfo stackDefaultProperty : stackConfigurationProperties) {
            java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(stackDefaultProperty.getFilename());
            if (!defaultPropertiesByType.containsKey(type)) {
                defaultPropertiesByType.put(type, new java.util.HashMap<>());
            }
            defaultPropertiesByType.get(type).put(stackDefaultProperty.getName(), stackDefaultProperty.getValue());
        }
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceConfigurationProperties = ambariMetaInfo.getServiceProperties(stack.getStackName(), stack.getStackVersion(), serviceName);
        for (org.apache.ambari.server.state.PropertyInfo serviceDefaultProperty : serviceConfigurationProperties) {
            java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(serviceDefaultProperty.getFilename());
            if (!defaultPropertiesByType.containsKey(type)) {
                defaultPropertiesByType.put(type, new java.util.HashMap<>());
            }
            defaultPropertiesByType.get(type).put(serviceDefaultProperty.getName(), serviceDefaultProperty.getValue());
        }
        return defaultPropertiesByType;
    }

    private boolean calculateIsStaleConfigs(org.apache.ambari.server.state.ServiceComponentHost sch, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs, org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity) throws org.apache.ambari.server.AmbariException {
        if (sch.isRestartRequired(hostComponentDesiredStateEntity)) {
            return true;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> actual = sch.getActualConfigs();
        if ((null == actual) || actual.isEmpty()) {
            return false;
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(sch.getClusterId());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> desired = getEffectiveDesiredTags(cluster, sch.getHostName(), desiredConfigs);
        java.lang.Boolean stale = null;
        int staleHash = 0;
        if (STALE_CONFIGS_CACHE_ENABLED) {
            staleHash = com.google.common.base.Objects.hashCode(actual.hashCode(), desired.hashCode(), sch.getHostName(), sch.getServiceComponentName(), sch.getServiceName());
            stale = staleConfigsCache.getIfPresent(staleHash);
            if (stale != null) {
                return stale;
            }
        }
        stale = false;
        org.apache.ambari.server.state.StackId stackId = sch.getServiceComponent().getDesiredStackId();
        org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackId);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), sch.getServiceName());
        org.apache.ambari.server.state.ComponentInfo componentInfo = serviceInfo.getComponentByName(sch.getServiceComponentName());
        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> it = desired.entrySet().iterator();
        java.util.List<java.lang.String> changedProperties = new java.util.LinkedList<>();
        while (it.hasNext()) {
            boolean staleEntry = false;
            java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> desiredEntry = it.next();
            java.lang.String type = desiredEntry.getKey();
            java.util.Map<java.lang.String, java.lang.String> tags = desiredEntry.getValue();
            if (!actual.containsKey(type)) {
                staleEntry = serviceInfo.hasConfigDependency(type) || componentInfo.hasConfigType(type);
            } else {
                org.apache.ambari.server.state.HostConfig hc = actual.get(type);
                java.util.Map<java.lang.String, java.lang.String> actualTags = buildTags(hc);
                if (!isTagChanged(tags, actualTags, hasGroupSpecificConfigsForType(cluster, sch.getHostName(), type))) {
                    staleEntry = false;
                } else {
                    staleEntry = serviceInfo.hasConfigDependency(type) || componentInfo.hasConfigType(type);
                    if (staleEntry) {
                        java.util.Collection<java.lang.String> changedKeys = findChangedKeys(cluster, type, tags.values(), actualTags.values());
                        changedProperties.addAll(changedKeys);
                    }
                }
            }
            stale = stale | staleEntry;
        } 
        java.lang.String refreshCommand = calculateRefreshCommand(stackInfo.getRefreshCommandConfiguration(), sch, changedProperties);
        if (STALE_CONFIGS_CACHE_ENABLED) {
            staleConfigsCache.put(staleHash, stale);
            if (refreshCommand != null) {
                refreshConfigCommandCache.put(staleHash, refreshCommand);
            }
        }
        if (org.apache.ambari.server.state.ConfigHelper.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.ConfigHelper.LOG.debug("Changed properties {} ({}) {} :  COMMAND: {}", stale, sch.getServiceComponentName(), sch.getHostName(), refreshCommand);
            for (java.lang.String p : changedProperties) {
                org.apache.ambari.server.state.ConfigHelper.LOG.debug(p);
            }
        }
        return stale;
    }

    public void updateAgentConfigs(java.util.Set<java.lang.String> updatedClusters) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.Cluster> clustersInUse = new java.util.ArrayList<>();
        for (java.lang.String clusterName : updatedClusters) {
            org.apache.ambari.server.state.Cluster cluster;
            cluster = clusters.getCluster(clusterName);
            clustersInUse.add(cluster);
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.events.AgentConfigsUpdateEvent> currentConfigEvents = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.events.AgentConfigsUpdateEvent> previousConfigEvents = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Cluster cluster : clustersInUse) {
            for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
                java.lang.Long hostId = host.getHostId();
                if (!currentConfigEvents.containsKey(hostId)) {
                    currentConfigEvents.put(host.getHostId(), m_agentConfigsHolder.get().getCurrentData(hostId));
                }
                if (!previousConfigEvents.containsKey(host.getHostId())) {
                    previousConfigEvents.put(host.getHostId(), m_agentConfigsHolder.get().initializeDataIfNeeded(hostId, true));
                }
            }
        }
        for (org.apache.ambari.server.state.Cluster cluster : clustersInUse) {
            java.util.Map<java.lang.Long, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> changedConfigs = new java.util.HashMap<>();
            for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
                org.apache.ambari.server.events.AgentConfigsUpdateEvent currentConfigData = currentConfigEvents.get(host.getHostId());
                org.apache.ambari.server.events.AgentConfigsUpdateEvent previousConfigsData = previousConfigEvents.get(host.getHostId());
                java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> currentConfigs = currentConfigData.getClustersConfigs().get(java.lang.Long.toString(cluster.getClusterId())).getConfigurations();
                java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> previousConfigs = previousConfigsData.getClustersConfigs().get(java.lang.Long.toString(cluster.getClusterId())).getConfigurations();
                java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> changedConfigsHost = new java.util.HashMap<>();
                for (java.lang.String currentConfigType : currentConfigs.keySet()) {
                    if (previousConfigs.containsKey(currentConfigType)) {
                        java.util.Set<java.lang.String> changedKeys = new java.util.HashSet<>();
                        java.util.Map<java.lang.String, java.lang.String> currentTypedConfigs = currentConfigs.get(currentConfigType);
                        java.util.Map<java.lang.String, java.lang.String> previousTypedConfigs = previousConfigs.get(currentConfigType);
                        for (java.lang.String currentKey : currentTypedConfigs.keySet()) {
                            if ((!previousTypedConfigs.containsKey(currentKey)) || (!currentTypedConfigs.get(currentKey).equals(previousTypedConfigs.get(currentKey)))) {
                                changedKeys.add(currentKey);
                            }
                        }
                        for (java.lang.String previousKey : previousTypedConfigs.keySet()) {
                            if (!currentTypedConfigs.containsKey(previousKey)) {
                                changedKeys.add(previousKey);
                            }
                        }
                        if (!changedKeys.isEmpty()) {
                            changedConfigsHost.put(currentConfigType, changedKeys);
                        }
                    } else {
                        changedConfigsHost.put(currentConfigType, currentConfigs.get(currentConfigType).keySet());
                    }
                }
                for (java.lang.String previousConfigType : previousConfigs.keySet()) {
                    if (!currentConfigs.containsKey(previousConfigType)) {
                        changedConfigsHost.put(previousConfigType, previousConfigs.get(previousConfigType).keySet());
                    }
                }
                changedConfigs.put(host.getHostId(), changedConfigsHost);
            }
            for (java.lang.String serviceName : cluster.getServices().keySet()) {
                checkStaleConfigsStatusOnConfigsUpdate(cluster.getClusterId(), serviceName, changedConfigs);
            }
            m_metadataHolder.get().updateData(m_ambariManagementController.get().getClusterMetadataOnConfigsUpdate(cluster));
            m_agentConfigsHolder.get().updateData(cluster.getClusterId(), null);
        }
    }

    public void checkStaleConfigsStatusOnConfigsUpdate(java.lang.Long clusterId, java.lang.String serviceName, java.util.Map<java.lang.Long, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> changedConfigs) throws org.apache.ambari.server.AmbariException {
        if (org.apache.commons.collections.MapUtils.isEmpty(changedConfigs)) {
            return;
        }
        if (!clusters.getCluster(clusterId).getServices().keySet().contains(serviceName)) {
            return;
        }
        org.apache.ambari.server.state.Service service = clusters.getCluster(clusterId).getService(serviceName);
        for (org.apache.ambari.server.state.ServiceComponent serviceComponent : service.getServiceComponents().values()) {
            java.lang.String serviceComponentHostName = serviceComponent.getName();
            for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponent.getServiceComponentHosts().values()) {
                if (changedConfigs.keySet().contains(serviceComponentHost.getHost().getHostId())) {
                    boolean staleConfigs = checkStaleConfigsStatusForHostComponent(serviceComponentHost, changedConfigs.get(serviceComponentHost.getHost().getHostId()));
                    if (wasStaleConfigsStatusUpdated(clusterId, serviceComponentHost.getHost().getHostId(), serviceName, serviceComponentHostName, staleConfigs)) {
                        serviceComponentHost.setRestartRequiredWithoutEventPublishing(staleConfigs);
                        STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.HostComponentsUpdateEvent(java.util.Collections.singletonList(org.apache.ambari.server.events.HostComponentUpdate.createHostComponentStaleConfigsStatusUpdate(clusterId, serviceName, serviceComponentHost.getHostName(), serviceComponentHostName, staleConfigs))));
                    }
                }
            }
        }
    }

    public boolean wasStaleConfigsStatusUpdated(java.lang.Long clusterId, java.lang.Long hostId, java.lang.String serviceName, java.lang.String hostComponentName, java.lang.Boolean staleConfigs) {
        if (!stateCache.containsKey(clusterId)) {
            stateCache.put(clusterId, new java.util.HashMap<>());
        }
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Boolean>>> hosts = stateCache.get(clusterId);
        if (!hosts.containsKey(hostId)) {
            hosts.put(hostId, new java.util.HashMap<>());
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Boolean>> services = hosts.get(hostId);
        if (!services.containsKey(serviceName)) {
            services.put(serviceName, new java.util.HashMap<>());
        }
        java.util.Map<java.lang.String, java.lang.Boolean> hostComponents = services.get(serviceName);
        if (staleConfigs.equals(hostComponents.get(hostComponentName))) {
            return false;
        } else {
            hostComponents.put(hostComponentName, staleConfigs);
            return true;
        }
    }

    public boolean checkStaleConfigsStatusForHostComponent(org.apache.ambari.server.state.ServiceComponentHost sch, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> changedConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity = sch.getDesiredStateEntity();
        if (sch.isRestartRequired(hostComponentDesiredStateEntity)) {
            return true;
        }
        boolean stale = false;
        org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(sch.getClusterId());
        org.apache.ambari.server.state.StackId stackId = sch.getServiceComponent().getDesiredStackId();
        org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackId);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), sch.getServiceName());
        org.apache.ambari.server.state.ComponentInfo componentInfo = serviceInfo.getComponentByName(sch.getServiceComponentName());
        java.util.List<java.lang.String> changedProperties = new java.util.LinkedList<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> changedConfigType : changedConfigs.entrySet()) {
            java.lang.String type = changedConfigType.getKey();
            stale |= serviceInfo.hasConfigDependency(type) || componentInfo.hasConfigType(type);
            if (stale) {
                for (java.lang.String propertyName : changedConfigType.getValue()) {
                    changedProperties.add((type + "/") + propertyName);
                }
            }
        }
        java.lang.String refreshCommand = calculateRefreshCommand(stackInfo.getRefreshCommandConfiguration(), sch, changedProperties);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> actual = sch.getActualConfigs();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> desired = getEffectiveDesiredTags(cluster, sch.getHostName(), cluster.getDesiredConfigs());
        if (STALE_CONFIGS_CACHE_ENABLED) {
            if (refreshCommand != null) {
                int staleHash = com.google.common.base.Objects.hashCode(actual.hashCode(), desired.hashCode(), sch.getHostName(), sch.getServiceComponentName(), sch.getServiceName());
                refreshConfigCommandCache.put(staleHash, refreshCommand);
            }
        }
        return stale;
    }

    public java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> getChangedConfigTypes(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.ServiceConfigEntity currentServiceConfigEntity, java.lang.Long configGroupId, java.lang.Long clusterId, java.lang.String serviceName) {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity previousServiceConfigEntity;
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> previousConfigEntities = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> currentConfigEntities = new java.util.ArrayList<>();
        currentConfigEntities.addAll(currentServiceConfigEntity.getClusterConfigEntities());
        if (configGroupId != null) {
            previousServiceConfigEntity = serviceConfigDAO.getLastServiceConfigVersionsForGroup(configGroupId);
            if (previousServiceConfigEntity != null) {
                previousConfigEntities.addAll(previousServiceConfigEntity.getClusterConfigEntities());
            }
        }
        previousServiceConfigEntity = serviceConfigDAO.getLastServiceConfigForServiceDefaultGroup(clusterId, serviceName);
        if (previousServiceConfigEntity != null) {
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : previousServiceConfigEntity.getClusterConfigEntities()) {
                org.apache.ambari.server.orm.entities.ClusterConfigEntity exist = previousConfigEntities.stream().filter(c -> c.getType().equals(clusterConfigEntity.getType())).findAny().orElse(null);
                if (exist == null) {
                    previousConfigEntities.add(clusterConfigEntity);
                }
                if (configGroupId != null) {
                    exist = currentConfigEntities.stream().filter(c -> c.getType().equals(clusterConfigEntity.getType())).findAny().orElse(null);
                    if (exist == null) {
                        currentConfigEntities.add(clusterConfigEntity);
                    }
                }
            }
        }
        java.util.Map<java.lang.String, java.lang.String> previousConfigs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> currentConfigs = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : currentConfigEntities) {
            currentConfigs.put(clusterConfigEntity.getType(), clusterConfigEntity.getTag());
        }
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : previousConfigEntities) {
            previousConfigs.put(clusterConfigEntity.getType(), clusterConfigEntity.getTag());
        }
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> changedConfigs = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> currentConfig : currentConfigs.entrySet()) {
            java.lang.String type = currentConfig.getKey();
            java.lang.String tag = currentConfig.getValue();
            java.util.Collection<java.lang.String> changedKeys;
            if (previousConfigs.containsKey(type)) {
                changedKeys = findChangedKeys(cluster, type, java.util.Collections.singletonList(tag), java.util.Collections.singletonList(previousConfigs.get(type)));
            } else {
                changedKeys = cluster.getConfig(type, tag).getProperties().keySet();
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(changedKeys)) {
                changedConfigs.put(type, changedKeys);
            }
        }
        return changedConfigs;
    }

    public java.lang.String getRefreshConfigsCommand(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName, java.lang.String serviceName, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = cluster.getService(serviceName).getServiceComponent(componentName);
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponent.getServiceComponentHost(hostName);
        return getRefreshConfigsCommand(cluster, sch);
    }

    public java.lang.String getRefreshConfigsCommand(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
        java.lang.String refreshCommand = null;
        java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> actual = sch.getActualConfigs();
        if (STALE_CONFIGS_CACHE_ENABLED) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> desired = getEffectiveDesiredTags(cluster, sch.getHostName(), cluster.getDesiredConfigs());
            int staleHash = com.google.common.base.Objects.hashCode(actual.hashCode(), desired.hashCode(), sch.getHostName(), sch.getServiceComponentName(), sch.getServiceName());
            refreshCommand = refreshConfigCommandCache.getIfPresent(staleHash);
        }
        return refreshCommand;
    }

    private java.lang.String calculateRefreshCommand(org.apache.ambari.server.state.RefreshCommandConfiguration refreshCommandConfiguration, org.apache.ambari.server.state.ServiceComponentHost sch, java.util.List<java.lang.String> changedProperties) {
        java.lang.String finalRefreshCommand = null;
        for (java.lang.String propertyName : changedProperties) {
            java.lang.String refreshCommand = refreshCommandConfiguration.getRefreshCommandForComponent(sch, propertyName);
            if (refreshCommand == null) {
                return null;
            }
            if (finalRefreshCommand == null) {
                finalRefreshCommand = refreshCommand;
            }
            if (!finalRefreshCommand.equals(refreshCommand)) {
                if (finalRefreshCommand.equals(org.apache.ambari.server.state.RefreshCommandConfiguration.REFRESH_CONFIGS)) {
                    finalRefreshCommand = refreshCommand;
                } else if (!refreshCommand.equals(org.apache.ambari.server.state.RefreshCommandConfiguration.REFRESH_CONFIGS)) {
                    return null;
                }
            }
        }
        return finalRefreshCommand;
    }

    private boolean hasGroupSpecificConfigsForType(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, java.lang.String type) {
        try {
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = cluster.getConfigGroupsByHostname(hostname);
            if ((configGroups != null) && (!configGroups.isEmpty())) {
                for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroups.values()) {
                    org.apache.ambari.server.state.Config config = configGroup.getConfigurations().get(type);
                    if (config != null) {
                        return true;
                    }
                }
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.state.ConfigHelper.LOG.warn("Could not determine group configuration for host. Details: " + ambariException.getMessage());
        }
        return false;
    }

    private java.util.Collection<java.lang.String> findChangedKeys(org.apache.ambari.server.state.Cluster cluster, java.lang.String type, java.util.Collection<java.lang.String> desiredTags, java.util.Collection<java.lang.String> actualTags) {
        java.util.Map<java.lang.String, java.lang.String> desiredValues = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> actualValues = new java.util.HashMap<>();
        for (java.lang.String tag : desiredTags) {
            org.apache.ambari.server.state.Config config = cluster.getConfig(type, tag);
            if (null != config) {
                desiredValues.putAll(config.getProperties());
            }
        }
        for (java.lang.String tag : actualTags) {
            org.apache.ambari.server.state.Config config = cluster.getConfig(type, tag);
            if (null != config) {
                actualValues.putAll(config.getProperties());
            }
        }
        java.util.List<java.lang.String> keys = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : desiredValues.entrySet()) {
            java.lang.String key = entry.getKey();
            java.lang.String value = entry.getValue();
            if ((!actualValues.containsKey(key)) || (!org.apache.ambari.server.state.ConfigHelper.valuesAreEqual(actualValues.get(key), value))) {
                keys.add((type + "/") + key);
            }
        }
        for (java.lang.String key : actualValues.keySet()) {
            if (!desiredValues.containsKey(key)) {
                keys.add((type + "/") + key);
            }
        }
        return keys;
    }

    static boolean valuesAreEqual(java.lang.String value1, java.lang.String value2) {
        if (org.apache.commons.lang.math.NumberUtils.isNumber(value1) && org.apache.commons.lang.math.NumberUtils.isNumber(value2)) {
            try {
                java.lang.Number number1 = org.apache.commons.lang.math.NumberUtils.createNumber(value1);
                java.lang.Number number2 = org.apache.commons.lang.math.NumberUtils.createNumber(value2);
                return com.google.common.base.Objects.equal(number1, number2) || (number1.doubleValue() == number2.doubleValue());
            } catch (java.lang.NumberFormatException e) {
            }
        }
        return com.google.common.base.Objects.equal(value1, value2);
    }

    private java.util.Map<java.lang.String, java.lang.String> buildTags(org.apache.ambari.server.state.HostConfig hc) {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.LinkedHashMap<>();
        map.put(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG, hc.getDefaultVersionTag());
        if (hc.getConfigGroupOverrides() != null) {
            for (java.util.Map.Entry<java.lang.Long, java.lang.String> entry : hc.getConfigGroupOverrides().entrySet()) {
                map.put(entry.getKey().toString(), entry.getValue());
            }
        }
        return map;
    }

    private boolean isTagChanged(java.util.Map<java.lang.String, java.lang.String> desiredTags, java.util.Map<java.lang.String, java.lang.String> actualTags, boolean groupSpecificConfigs) {
        if (!actualTags.get(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG).equals(desiredTags.get(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG))) {
            return true;
        }
        if (groupSpecificConfigs) {
            actualTags.remove(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG);
            desiredTags.remove(org.apache.ambari.server.state.ConfigHelper.CLUSTER_DEFAULT_TAG);
        }
        java.util.Set<java.lang.String> desiredSet = new java.util.HashSet<>(desiredTags.values());
        java.util.Set<java.lang.String> actualSet = new java.util.HashSet<>(actualTags.values());
        return !desiredSet.equals(actualSet);
    }

    public static java.lang.String fileNameToConfigType(java.lang.String filename) {
        int extIndex = filename.indexOf(org.apache.ambari.server.stack.StackDirectory.SERVICE_CONFIG_FILE_NAME_POSTFIX);
        return filename.substring(0, extIndex);
    }

    public static void processHiddenAttribute(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes, java.lang.String componentName, boolean configDownload) {
        if (((configurations != null) && (attributes != null)) && (componentName != null)) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> confEntry : configurations.entrySet()) {
                java.lang.String configTag = confEntry.getKey();
                java.util.Map<java.lang.String, java.lang.String> confProperties = confEntry.getValue();
                if (attributes.containsKey(configTag)) {
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes = attributes.get(configTag);
                    if (configAttributes.containsKey("hidden")) {
                        java.util.Map<java.lang.String, java.lang.String> hiddenProperties = configAttributes.get("hidden");
                        if (hiddenProperties != null) {
                            for (java.util.Map.Entry<java.lang.String, java.lang.String> hiddenEntry : hiddenProperties.entrySet()) {
                                java.lang.String propertyName = hiddenEntry.getKey();
                                java.lang.String components = hiddenEntry.getValue();
                                if ((configDownload ? components.contains("CONFIG_DOWNLOAD") : components.contains(componentName)) && confProperties.containsKey(propertyName)) {
                                    confProperties.remove(propertyName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void mergeConfigAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> additionalAttributes) {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attrEntry : additionalAttributes.entrySet()) {
            java.lang.String attributeName = attrEntry.getKey();
            java.util.Map<java.lang.String, java.lang.String> attributeProperties = attrEntry.getValue();
            if (!attributes.containsKey(attributeName)) {
                attributes.put(attributeName, attributeProperties);
            } else {
                attributes.get(attributeName).putAll(attributeProperties);
            }
        }
    }

    public org.apache.ambari.server.events.AgentConfigsUpdateEvent getHostActualConfigs(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        return getHostActualConfigsExcludeCluster(hostId, null);
    }

    public org.apache.ambari.server.events.AgentConfigsUpdateEvent getHostActualConfigsExcludeCluster(java.lang.Long hostId, java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ClusterConfigs> clustersConfigs = new java.util.TreeMap<>();
        org.apache.ambari.server.state.Host host = clusters.getHostById(hostId);
        for (org.apache.ambari.server.state.Cluster cl : clusters.getClusters().values()) {
            if ((clusterId != null) && (cl.getClusterId() == clusterId)) {
                continue;
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes = new java.util.HashMap<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterDesiredConfigs = cl.getDesiredConfigs(false);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = getEffectiveDesiredTags(cl, host.getHostName(), clusterDesiredConfigs);
            if (org.apache.ambari.server.state.ConfigHelper.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.ConfigHelper.LOG.debug("For configs update on host {} will be used cluster entity {}", hostId, cl.getClusterEntity().toString());
                org.apache.ambari.server.state.ConfigHelper.LOG.debug("For configs update on host {} will be used following cluster desired configs {}", hostId, clusterDesiredConfigs.toString());
                org.apache.ambari.server.state.ConfigHelper.LOG.debug("For configs update on host {} will be used following effective desired tags {}", hostId, configTags.toString());
            }
            getAndMergeHostConfigs(configurations, configTags, cl);
            configurations = unescapeConfigNames(configurations);
            getAndMergeHostConfigAttributes(configurationAttributes, configTags, cl);
            configurationAttributes = unescapeConfigAttributeNames(configurationAttributes);
            java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> configurationsTreeMap = sortConfigutations(configurations);
            java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>>> configurationAttributesTreeMap = sortConfigurationAttributes(configurationAttributes);
            clustersConfigs.put(java.lang.Long.toString(cl.getClusterId()), new org.apache.ambari.server.agent.stomp.dto.ClusterConfigs(configurationsTreeMap, configurationAttributesTreeMap));
        }
        return new org.apache.ambari.server.events.AgentConfigsUpdateEvent(hostId, clustersConfigs);
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> unescapeConfigNames(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> unescapedConfigs = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTypeEntry : configurations.entrySet()) {
            java.util.Map<java.lang.String, java.lang.String> unescapedTypeConfigs = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> config : configTypeEntry.getValue().entrySet()) {
                unescapedTypeConfigs.put(org.apache.commons.lang3.StringEscapeUtils.unescapeJava(config.getKey()), config.getValue());
            }
            unescapedConfigs.put(configTypeEntry.getKey(), unescapedTypeConfigs);
        }
        return unescapedConfigs;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> unescapeConfigAttributeNames(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> unescapedConfigAttributes = new java.util.HashMap<>();
        configurationAttributes.forEach((key, value) -> unescapedConfigAttributes.put(key, unescapeConfigNames(value)));
        return unescapedConfigAttributes;
    }

    public java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> sortConfigutations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) {
        java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> configurationsTreeMap = new java.util.TreeMap<>();
        configurations.forEach((k, v) -> configurationsTreeMap.put(k, new java.util.TreeMap<>(v)));
        return configurationsTreeMap;
    }

    public java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>>> sortConfigurationAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes) {
        java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>>> configurationAttributesTreeMap = new java.util.TreeMap<>();
        configurationAttributes.forEach((k, v) -> {
            java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> c = new java.util.TreeMap<>();
            v.forEach((k1, v1) -> c.put(k1, new java.util.TreeMap<>(v1)));
            configurationAttributesTreeMap.put(k, c);
        });
        return configurationAttributesTreeMap;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateExistingConfigurations(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
            configurations.putAll(calculateExistingConfigurations(ambariManagementController, cluster, host.getHostName(), null));
        }
        return configurations;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateExistingConfigurations(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController, org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashMap<>();
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationTags = ambariManagementController.findConfigurationTagsWithOverrides(cluster, hostname, desiredConfigs);
        final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = getEffectiveConfigProperties(cluster, configurationTags);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : configProperties.entrySet()) {
            java.lang.String type = entry.getKey();
            java.util.Map<java.lang.String, java.lang.String> allLevelMergedConfig = entry.getValue();
            java.util.Map<java.lang.String, java.lang.String> configuration = configurations.get(type);
            if (configuration == null) {
                configuration = new java.util.HashMap<>(allLevelMergedConfig);
            } else {
                java.util.Map<java.lang.String, java.lang.String> mergedConfig = getMergedConfig(allLevelMergedConfig, configuration);
                configuration.clear();
                configuration.putAll(mergedConfig);
            }
            configurations.put(type, configuration);
        }
        return configurations;
    }

    public org.apache.commons.lang3.tuple.Pair<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>> calculateExistingConfigs(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> desiredConfigTags = getEffectiveDesiredTags(cluster, null);
        return org.apache.commons.lang3.tuple.Pair.of(getEffectiveConfigProperties(cluster, desiredConfigTags), getEffectiveConfigAttributes(cluster, desiredConfigTags));
    }
}