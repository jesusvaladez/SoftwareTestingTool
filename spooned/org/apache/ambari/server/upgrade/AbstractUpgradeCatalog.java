package org.apache.ambari.server.upgrade;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
public abstract class AbstractUpgradeCatalog implements org.apache.ambari.server.upgrade.UpgradeCatalog {
    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.DBAccessor dbAccessor;

    @com.google.inject.Inject
    protected org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    protected org.apache.ambari.server.controller.AmbariManagementControllerImpl ambariManagementController;

    protected com.google.inject.Injector injector;

    protected java.lang.String ambariSequencesTable = "ambari_sequences";

    protected static final java.lang.String AUTHENTICATED_USER_NAME = "ambari-upgrade";

    private static final java.lang.String CONFIGURATION_TYPE_HDFS_SITE = "hdfs-site";

    public static final java.lang.String CONFIGURATION_TYPE_RANGER_HBASE_PLUGIN_PROPERTIES = "ranger-hbase-plugin-properties";

    public static final java.lang.String CONFIGURATION_TYPE_RANGER_KNOX_PLUGIN_PROPERTIES = "ranger-knox-plugin-properties";

    public static final java.lang.String CONFIGURATION_TYPE_RANGER_HIVE_PLUGIN_PROPERTIES = "ranger-hive-plugin-properties";

    private static final java.lang.String PROPERTY_DFS_NAMESERVICES = "dfs.nameservices";

    public static final java.lang.String PROPERTY_RANGER_HBASE_PLUGIN_ENABLED = "ranger-hbase-plugin-enabled";

    public static final java.lang.String PROPERTY_RANGER_KNOX_PLUGIN_ENABLED = "ranger-knox-plugin-enabled";

    public static final java.lang.String PROPERTY_RANGER_HIVE_PLUGIN_ENABLED = "ranger-hive-plugin-enabled";

    public static final java.lang.String YARN_SCHEDULER_CAPACITY_ROOT_QUEUE = "yarn.scheduler.capacity.root";

    public static final java.lang.String YARN_SCHEDULER_CAPACITY_ROOT_QUEUES = "yarn.scheduler.capacity.root.queues";

    public static final java.lang.String QUEUES = "queues";

    public static final java.lang.String ALERT_URL_PROPERTY_CONNECTION_TIMEOUT = "connection_timeout";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.class);

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogMap = new java.util.HashMap<>();

    protected java.lang.String ambariUpgradeConfigUpdatesFileName;

    private java.util.Map<java.lang.String, java.lang.String> upgradeJsonOutput = new java.util.HashMap<>();

    @com.google.inject.Inject
    public AbstractUpgradeCatalog(com.google.inject.Injector injector) {
        this.injector = injector;
        injector.injectMembers(this);
        registerCatalog(this);
    }

    protected void registerCatalog(org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog) {
        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.upgradeCatalogMap.put(upgradeCatalog.getTargetVersion(), upgradeCatalog);
    }

    protected final void addSequence(java.lang.String seqName, java.lang.Long seqDefaultValue, boolean ignoreFailure) throws java.sql.SQLException {
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        try {
            statement = dbAccessor.getConnection().createStatement();
            if (statement != null) {
                rs = statement.executeQuery(java.lang.String.format("SELECT COUNT(*) from %s where sequence_name='%s'", ambariSequencesTable, seqName));
                if (rs != null) {
                    if (rs.next() && (rs.getInt(1) == 0)) {
                        dbAccessor.executeQuery(java.lang.String.format("INSERT INTO %s(sequence_name, sequence_value) VALUES('%s', %d)", ambariSequencesTable, seqName, seqDefaultValue), ignoreFailure);
                    } else {
                        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.warn("Sequence {} already exists, skipping", seqName);
                    }
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    protected final void addSequences(java.util.List<java.lang.String> seqNames, java.lang.Long seqDefaultValue, boolean ignoreFailure) throws java.sql.SQLException {
        for (java.lang.String seqName : seqNames) {
            addSequence(seqName, seqDefaultValue, ignoreFailure);
        }
    }

    protected final long fetchMaxId(java.lang.String tableName, java.lang.String idColumnName) throws java.sql.SQLException {
        try (java.sql.Statement stmt = dbAccessor.getConnection().createStatement();java.sql.ResultSet rs = stmt.executeQuery(java.lang.String.format("SELECT MAX(%s) FROM %s", idColumnName, tableName))) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        }
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return null;
    }

    protected static org.apache.ambari.server.upgrade.UpgradeCatalog getUpgradeCatalog(java.lang.String version) {
        return org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.upgradeCatalogMap.get(version);
    }

    protected static org.w3c.dom.Document convertStringToDocument(java.lang.String xmlStr) {
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder builder;
        org.w3c.dom.Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xmlStr)));
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.error(("Error during convertation from String \"" + xmlStr) + "\" to Xml!", e);
        }
        return doc;
    }

    protected static boolean isConfigEnabled(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyName) {
        boolean isRangerPluginEnabled = false;
        if (cluster != null) {
            org.apache.ambari.server.state.Config rangerPluginProperties = cluster.getDesiredConfigByType(configType);
            if (rangerPluginProperties != null) {
                java.lang.String rangerPluginEnabled = rangerPluginProperties.getProperties().get(propertyName);
                if (org.apache.commons.lang.StringUtils.isNotEmpty(rangerPluginEnabled)) {
                    isRangerPluginEnabled = "yes".equalsIgnoreCase(rangerPluginEnabled);
                }
            }
        }
        return isRangerPluginEnabled;
    }

    protected static class VersionComparator implements java.util.Comparator<org.apache.ambari.server.upgrade.UpgradeCatalog> {
        @java.lang.Override
        public int compare(org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog1, org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog2) {
            if (upgradeCatalog1.isFinal() ^ upgradeCatalog2.isFinal()) {
                return java.lang.Boolean.compare(upgradeCatalog1.isFinal(), upgradeCatalog2.isFinal());
            }
            return org.apache.ambari.server.utils.VersionUtils.compareVersions(upgradeCatalog1.getTargetVersion(), upgradeCatalog2.getTargetVersion(), 4);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getUpgradeJsonOutput() {
        return upgradeJsonOutput;
    }

    @com.google.inject.persist.Transactional
    public int updateMetaInfoVersion(java.lang.String version) {
        int rows = 0;
        if (version != null) {
            org.apache.ambari.server.orm.dao.MetainfoDAO metainfoDAO = injector.getInstance(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
            org.apache.ambari.server.orm.entities.MetainfoEntity versionEntity = metainfoDAO.findByKey("version");
            if (versionEntity != null) {
                versionEntity.setMetainfoValue(version);
                metainfoDAO.merge(versionEntity);
            } else {
                versionEntity = new org.apache.ambari.server.orm.entities.MetainfoEntity();
                versionEntity.setMetainfoName("version");
                versionEntity.setMetainfoValue(version);
                metainfoDAO.create(versionEntity);
            }
        }
        return rows;
    }

    public void addConnectionTimeoutParamForWebAndMetricAlerts() {
        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info("Updating alert definitions.");
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
        for (final org.apache.ambari.server.state.Cluster cluster : getCheckedClusterMap(clusters).values()) {
            long clusterID = cluster.getClusterId();
            java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitionList = alertDefinitionDAO.findAll(clusterID);
            for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinitionEntity : alertDefinitionList) {
                org.apache.ambari.server.state.alert.SourceType sourceType = alertDefinitionEntity.getSourceType();
                if ((sourceType == org.apache.ambari.server.state.alert.SourceType.METRIC) || (sourceType == org.apache.ambari.server.state.alert.SourceType.WEB)) {
                    java.lang.String source = alertDefinitionEntity.getSource();
                    com.google.gson.JsonObject rootJson = jsonParser.parse(source).getAsJsonObject();
                    com.google.gson.JsonObject uriJson = rootJson.get("uri").getAsJsonObject();
                    if (!uriJson.has(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ALERT_URL_PROPERTY_CONNECTION_TIMEOUT)) {
                        uriJson.addProperty(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ALERT_URL_PROPERTY_CONNECTION_TIMEOUT, 5.0);
                        alertDefinitionEntity.setSource(rootJson.toString());
                        alertDefinitionDAO.merge(alertDefinitionEntity);
                    }
                }
            }
        }
    }

    protected com.google.inject.Provider<javax.persistence.EntityManager> getEntityManagerProvider() {
        return injector.getProvider(javax.persistence.EntityManager.class);
    }

    protected void executeInTransaction(java.lang.Runnable func) {
        javax.persistence.EntityManager entityManager = getEntityManagerProvider().get();
        if (entityManager.getTransaction().isActive()) {
            func.run();
        } else {
            entityManager.getTransaction().begin();
            try {
                func.run();
                entityManager.getTransaction().commit();
                entityManager.getEntityManagerFactory().getCache().evictAll();
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.error("Error in transaction ", e);
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new java.lang.RuntimeException(e);
            }
        }
    }

    protected void changePostgresSearchPath() throws java.sql.SQLException {
        java.lang.String dbUser = configuration.getDatabaseUser();
        java.lang.String schemaName = configuration.getServerJDBCPostgresSchemaName();
        if ((((null != dbUser) && (!dbUser.equals(""))) && (null != schemaName)) && (!schemaName.equals(""))) {
            if (!dbUser.contains("\"")) {
                dbUser = java.lang.String.format("\"%s\"", dbUser);
            }
            dbAccessor.executeQuery(java.lang.String.format("ALTER SCHEMA %s OWNER TO %s;", schemaName, dbUser));
            dbAccessor.executeQuery(java.lang.String.format("ALTER ROLE %s SET search_path to '%s';", dbUser, schemaName));
        }
    }

    public void addNewConfigurationsFromXml() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        if (clusters == null) {
            return;
        }
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if ((clusterMap != null) && (!clusterMap.isEmpty())) {
            for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> toAddProperties = new java.util.HashMap<>();
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> toUpdateProperties = new java.util.HashMap<>();
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> toRemoveProperties = new java.util.HashMap<>();
                java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = configHelper.getStackProperties(cluster);
                for (java.lang.String serviceName : cluster.getServices().keySet()) {
                    java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = configHelper.getServiceProperties(cluster, serviceName);
                    if (properties == null) {
                        continue;
                    }
                    properties.addAll(stackProperties);
                    for (org.apache.ambari.server.state.PropertyInfo property : properties) {
                        java.lang.String configType = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(property.getFilename());
                        org.apache.ambari.server.state.PropertyUpgradeBehavior upgradeBehavior = property.getPropertyAmbariUpgradeBehavior();
                        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.debug("Property: {}, Config Type: {}", property, configType);
                        if (property.isDeleted()) {
                        } else if (upgradeBehavior.isDelete()) {
                            if (!toRemoveProperties.containsKey(configType)) {
                                toRemoveProperties.put(configType, new java.util.HashSet<>());
                            }
                            toRemoveProperties.get(configType).add(property.getName());
                        } else if (upgradeBehavior.isUpdate()) {
                            if (!toUpdateProperties.containsKey(configType)) {
                                toUpdateProperties.put(configType, new java.util.HashSet<>());
                            }
                            toUpdateProperties.get(configType).add(property.getName());
                        } else if (upgradeBehavior.isAdd()) {
                            if (!toAddProperties.containsKey(configType)) {
                                toAddProperties.put(configType, new java.util.HashSet<>());
                            }
                            toAddProperties.get(configType).add(property.getName());
                        }
                    }
                }
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> newProperty : toAddProperties.entrySet()) {
                    java.lang.String newPropertyKey = newProperty.getKey();
                    updateConfigurationPropertiesWithValuesFromXml(newPropertyKey, newProperty.getValue(), false, true);
                }
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> newProperty : toUpdateProperties.entrySet()) {
                    java.lang.String newPropertyKey = newProperty.getKey();
                    updateConfigurationPropertiesWithValuesFromXml(newPropertyKey, newProperty.getValue(), true, false);
                }
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> toRemove : toRemoveProperties.entrySet()) {
                    java.lang.String newPropertyKey = toRemove.getKey();
                    updateConfigurationPropertiesWithValuesFromXml(newPropertyKey, java.util.Collections.emptySet(), toRemove.getValue(), false, true);
                }
            }
        }
    }

    protected boolean isNNHAEnabled(org.apache.ambari.server.state.Cluster cluster) {
        org.apache.ambari.server.state.Config hdfsSiteConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.CONFIGURATION_TYPE_HDFS_SITE);
        if (hdfsSiteConfig != null) {
            java.util.Map<java.lang.String, java.lang.String> properties = hdfsSiteConfig.getProperties();
            if (properties.containsKey("dfs.internal.nameservices")) {
                return true;
            }
            java.lang.String nameServices = properties.get(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.PROPERTY_DFS_NAMESERVICES);
            if (!org.apache.commons.lang.StringUtils.isEmpty(nameServices)) {
                for (java.lang.String nameService : nameServices.split(",")) {
                    java.lang.String namenodes = properties.get(java.lang.String.format("dfs.ha.namenodes.%s", nameService));
                    if (!org.apache.commons.lang.StringUtils.isEmpty(namenodes)) {
                        return namenodes.split(",").length > 1;
                    }
                }
            }
        }
        return false;
    }

    protected java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> getCheckedClusterMap(org.apache.ambari.server.state.Clusters clusters) {
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if (clusterMap != null) {
                return clusterMap;
            }
        }
        return new java.util.HashMap<>();
    }

    protected void updateConfigurationPropertiesWithValuesFromXml(java.lang.String configType, java.util.Set<java.lang.String> propertyNames, boolean updateIfExists, boolean createNewConfigType) throws org.apache.ambari.server.AmbariException {
        updateConfigurationPropertiesWithValuesFromXml(configType, propertyNames, null, updateIfExists, createNewConfigType);
    }

    protected void updateConfigurationPropertiesWithValuesFromXml(java.lang.String configType, java.util.Set<java.lang.String> propertyNames, java.util.Set<java.lang.String> toRemove, boolean updateIfExists, boolean createNewConfigType) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        if (clusters == null) {
            return;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if ((clusterMap != null) && (!clusterMap.isEmpty())) {
            for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
                for (java.lang.String propertyName : propertyNames) {
                    java.lang.String propertyValue = configHelper.getPropertyValueFromStackDefinitions(cluster, configType, propertyName);
                    if (propertyValue == null) {
                        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info((((("Config " + propertyName) + " from ") + configType) + " is not found in xml definitions.") + "Skipping configuration property update");
                        continue;
                    }
                    org.apache.ambari.server.state.ServiceInfo propertyService = configHelper.getPropertyOwnerService(cluster, configType, propertyName);
                    if ((propertyService != null) && (!cluster.getServices().containsKey(propertyService.getName()))) {
                        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info((((((((("Config " + propertyName) + " from ") + configType) + " with value = ") + propertyValue) + " ") + "Is not added due to service ") + propertyService.getName()) + " is not in the cluster.");
                        continue;
                    }
                    properties.put(propertyName, propertyValue);
                }
                updateConfigurationPropertiesForCluster(cluster, configType, properties, toRemove, updateIfExists, createNewConfigType);
            }
        }
    }

    protected void updateConfigurationPropertiesForCluster(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Set<java.lang.String> removePropertiesList, boolean updateIfExists, boolean createNewConfigType) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController controller = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.lang.String newTag = "version" + java.lang.System.currentTimeMillis();
        if (properties != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> all = cluster.getConfigsByType(configType);
            if (((all == null) || (!all.containsKey(newTag))) || (properties.size() > 0)) {
                java.util.Map<java.lang.String, java.lang.String> oldConfigProperties;
                org.apache.ambari.server.state.Config oldConfig = cluster.getDesiredConfigByType(configType);
                if ((oldConfig == null) && (!createNewConfigType)) {
                    org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info((("Config " + configType) + " not found. Assuming service not installed. ") + "Skipping configuration properties update");
                    return;
                } else if (oldConfig == null) {
                    oldConfigProperties = new java.util.HashMap<>();
                } else {
                    oldConfigProperties = oldConfig.getProperties();
                }
                com.google.common.collect.Multimap<org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType, java.util.Map.Entry<java.lang.String, java.lang.String>> propertiesToLog = com.google.common.collect.ArrayListMultimap.create();
                java.lang.String serviceName = cluster.getServiceByConfigType(configType);
                java.util.Map<java.lang.String, java.lang.String> mergedProperties = mergeProperties(oldConfigProperties, properties, updateIfExists, propertiesToLog);
                if (removePropertiesList != null) {
                    mergedProperties = removeProperties(mergedProperties, removePropertiesList, propertiesToLog);
                }
                if (propertiesToLog.size() > 0) {
                    try {
                        configuration.writeToAmbariUpgradeConfigUpdatesFile(propertiesToLog, configType, serviceName, ambariUpgradeConfigUpdatesFileName);
                    } catch (java.lang.Exception e) {
                        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.error("Write to config updates file failed:", e);
                    }
                }
                if (!com.google.common.collect.Maps.difference(oldConfigProperties, mergedProperties).areEqual()) {
                    org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info("Applying configuration with tag '{}' and configType '{}' to " + "cluster '{}'", newTag, configType, cluster.getClusterName());
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = null;
                    if (oldConfig != null) {
                        propertiesAttributes = oldConfig.getPropertiesAttributes();
                    }
                    if (null == propertiesAttributes) {
                        propertiesAttributes = java.util.Collections.emptyMap();
                    }
                    controller.createConfig(cluster, cluster.getDesiredStackVersion(), configType, mergedProperties, newTag, propertiesAttributes);
                    org.apache.ambari.server.state.Config baseConfig = cluster.getConfig(configType, newTag);
                    if (baseConfig != null) {
                        java.lang.String authName = org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.AUTHENTICATED_USER_NAME;
                        java.lang.String configVersionNote = java.lang.String.format("Updated %s during Ambari Upgrade from %s to %s.", configType, getSourceVersion(), getTargetVersion());
                        if (cluster.addDesiredConfig(authName, java.util.Collections.singleton(baseConfig), configVersionNote) != null) {
                            java.lang.String oldConfigString = (oldConfig != null) ? (" from='" + oldConfig.getTag()) + "'" : "";
                            org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info(((((((((((("cluster '" + cluster.getClusterName()) + "' ") + "changed by: '") + authName) + "'; ") + "type='") + baseConfig.getType()) + "' ") + "tag='") + baseConfig.getTag()) + "'") + oldConfigString);
                        }
                        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
                        configHelper.updateAgentConfigs(java.util.Collections.singleton(cluster.getClusterName()));
                    }
                } else {
                    org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info(("No changes detected to config " + configType) + ". Skipping configuration properties update");
                }
            }
        }
    }

    protected void updateConfigurationPropertiesForCluster(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> properties, boolean updateIfExists, boolean createNewConfigType) throws org.apache.ambari.server.AmbariException {
        updateConfigurationPropertiesForCluster(cluster, configType, properties, null, updateIfExists, createNewConfigType);
    }

    protected void removeConfigurationPropertiesFromCluster(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.Set<java.lang.String> removePropertiesList) throws org.apache.ambari.server.AmbariException {
        updateConfigurationPropertiesForCluster(cluster, configType, new java.util.HashMap<>(), removePropertiesList, false, true);
    }

    protected void updateConfigurationProperties(java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> properties, boolean updateIfExists, boolean createNewConfigType) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController controller = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = controller.getClusters();
        if (clusters == null) {
            return;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if ((clusterMap != null) && (!clusterMap.isEmpty())) {
            for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                updateConfigurationPropertiesForCluster(cluster, configType, properties, updateIfExists, createNewConfigType);
            }
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> mergeProperties(java.util.Map<java.lang.String, java.lang.String> originalProperties, java.util.Map<java.lang.String, java.lang.String> newProperties, boolean updateIfExists, com.google.common.collect.Multimap<org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType, java.util.Map.Entry<java.lang.String, java.lang.String>> propertiesToLog) {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>(originalProperties);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : newProperties.entrySet()) {
            if (!properties.containsKey(entry.getKey())) {
                properties.put(entry.getKey(), entry.getValue());
                propertiesToLog.put(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType.ADDED, entry);
            }
            if (updateIfExists) {
                properties.put(entry.getKey(), entry.getValue());
                propertiesToLog.put(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType.UPDATED, entry);
            }
        }
        return properties;
    }

    private java.util.Map<java.lang.String, java.lang.String> removeProperties(java.util.Map<java.lang.String, java.lang.String> originalProperties, java.util.Set<java.lang.String> removeList, com.google.common.collect.Multimap<org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType, java.util.Map.Entry<java.lang.String, java.lang.String>> propertiesToLog) {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.putAll(originalProperties);
        for (java.lang.String removeProperty : removeList) {
            if (originalProperties.containsKey(removeProperty)) {
                properties.remove(removeProperty);
                propertiesToLog.put(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.ConfigUpdateType.REMOVED, new java.util.AbstractMap.SimpleEntry<>(removeProperty, ""));
            }
        }
        return properties;
    }

    public enum ConfigUpdateType {

        ADDED("Added"),
        UPDATED("Updated"),
        REMOVED("Removed");
        private final java.lang.String description;

        ConfigUpdateType(java.lang.String description) {
            this.description = description;
        }

        public java.lang.String getDescription() {
            return description;
        }
    }

    protected void updateKerberosDescriptorIdentityReferences(java.util.Map<java.lang.String, ? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> descriptorMap, java.lang.String referenceName, java.lang.String newReferenceName) {
        if (descriptorMap != null) {
            for (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer kerberosServiceDescriptor : descriptorMap.values()) {
                updateKerberosDescriptorIdentityReferences(kerberosServiceDescriptor, referenceName, newReferenceName);
                if (kerberosServiceDescriptor instanceof org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) {
                    updateKerberosDescriptorIdentityReferences(((org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) (kerberosServiceDescriptor)).getComponents(), referenceName, newReferenceName);
                }
            }
        }
    }

    protected void updateKerberosDescriptorIdentityReferences(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer descriptorContainer, java.lang.String referenceName, java.lang.String newReferenceName) {
        if (descriptorContainer != null) {
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity = descriptorContainer.getIdentity(referenceName);
            if (identity != null) {
                identity.setName(newReferenceName);
            }
        }
    }

    protected void updateKerberosDescriptorArtifacts() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ArtifactDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ArtifactEntity> artifactEntities = artifactDAO.findByName("kerberos_descriptor");
        if (artifactEntities != null) {
            for (org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity : artifactEntities) {
                updateKerberosDescriptorArtifact(artifactDAO, artifactEntity);
            }
        }
    }

    protected org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "can only take the first stack we find until we can support multiple with Kerberos")
        org.apache.ambari.server.state.StackId stackId = getStackId(cluster);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor defaultDescriptor = ambariMetaInfo.getKerberosDescriptor(stackId.getStackName(), stackId.getStackVersion(), false);
        org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ArtifactDAO.class);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor artifactDescriptor = null;
        org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity = artifactDAO.findByNameAndForeignKeys("kerberos_descriptor", new java.util.TreeMap<>(java.util.Collections.singletonMap("cluster", java.lang.String.valueOf(cluster.getClusterId()))));
        if (artifactEntity != null) {
            java.util.Map<java.lang.String, java.lang.Object> data = artifactEntity.getArtifactData();
            if (data != null) {
                artifactDescriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(data);
            }
        }
        if (defaultDescriptor == null) {
            return artifactDescriptor;
        } else if (artifactDescriptor == null) {
            return defaultDescriptor;
        } else {
            defaultDescriptor.update(artifactDescriptor);
            return defaultDescriptor;
        }
    }

    protected void addRoleAuthorization(java.lang.String roleAuthorizationID, java.lang.String roleAuthorizationName, java.util.Collection<java.lang.String> applicableRoles) throws java.sql.SQLException {
        if (!org.apache.commons.lang.StringUtils.isEmpty(roleAuthorizationID)) {
            org.apache.ambari.server.orm.dao.RoleAuthorizationDAO roleAuthorizationDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RoleAuthorizationDAO.class);
            org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorization = roleAuthorizationDAO.findById(roleAuthorizationID);
            if (roleAuthorization == null) {
                roleAuthorization = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
                roleAuthorization.setAuthorizationId(roleAuthorizationID);
                roleAuthorization.setAuthorizationName(roleAuthorizationName);
                roleAuthorizationDAO.create(roleAuthorization);
            }
            if ((applicableRoles != null) && (!applicableRoles.isEmpty())) {
                for (java.lang.String role : applicableRoles) {
                    java.lang.String[] parts = role.split("\\:");
                    addAuthorizationToRole(parts[0], parts[1], roleAuthorization);
                }
            }
        }
    }

    protected void addAuthorizationToRole(java.lang.String roleName, java.lang.String resourceType, java.lang.String roleAuthorizationID) {
        if (!org.apache.commons.lang.StringUtils.isEmpty(roleAuthorizationID)) {
            org.apache.ambari.server.orm.dao.RoleAuthorizationDAO roleAuthorizationDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RoleAuthorizationDAO.class);
            org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorization = roleAuthorizationDAO.findById(roleAuthorizationID);
            if (roleAuthorization != null) {
                addAuthorizationToRole(roleName, resourceType, roleAuthorization);
            }
        }
    }

    protected void addAuthorizationToRole(java.lang.String roleName, java.lang.String resourceType, org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorization) {
        if (((roleAuthorization != null) && (!org.apache.commons.lang.StringUtils.isEmpty(roleName))) && (!org.apache.commons.lang.StringUtils.isEmpty(resourceType))) {
            org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
            org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
            org.apache.ambari.server.orm.entities.PermissionEntity role = permissionDAO.findPermissionByNameAndType(roleName, resourceTypeDAO.findByName(resourceType));
            if (role != null) {
                role.addAuthorization(roleAuthorization);
                permissionDAO.merge(role);
            }
        }
    }

    protected void addAuthorizationToRole(org.apache.ambari.server.orm.entities.PermissionEntity role, java.lang.String roleAuthorizationID) {
        if ((role != null) && (!org.apache.commons.lang.StringUtils.isEmpty(roleAuthorizationID))) {
            org.apache.ambari.server.orm.dao.RoleAuthorizationDAO roleAuthorizationDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RoleAuthorizationDAO.class);
            org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorization = roleAuthorizationDAO.findById(roleAuthorizationID);
            if (roleAuthorization != null) {
                org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
                role.getAuthorizations().add(roleAuthorization);
                permissionDAO.merge(role);
            }
        }
    }

    protected void updateKerberosDescriptorArtifact(org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO, org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity) throws org.apache.ambari.server.AmbariException {
    }

    @java.lang.Override
    public void upgradeSchema() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES) {
            changePostgresSearchPath();
        }
        executeDDLUpdates();
    }

    @java.lang.Override
    public void preUpgradeData() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        executePreDMLUpdates();
    }

    @java.lang.Override
    public void setConfigUpdatesFileName(java.lang.String ambariUpgradeConfigUpdatesFileName) {
        this.ambariUpgradeConfigUpdatesFileName = ambariUpgradeConfigUpdatesFileName;
    }

    @java.lang.Override
    public void upgradeData() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        executeDMLUpdates();
    }

    @java.lang.Override
    public final void updateDatabaseSchemaVersion() {
        updateMetaInfoVersion(getTargetVersion());
    }

    @java.lang.Override
    public boolean isFinal() {
        return false;
    }

    protected abstract void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException;

    protected abstract void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException;

    protected abstract void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException;

    @java.lang.Override
    public java.lang.String toString() {
        return (((("{ upgradeCatalog: sourceVersion = " + getSourceVersion()) + ", ") + "targetVersion = ") + getTargetVersion()) + " }";
    }

    @java.lang.Override
    public void onPostUpgrade() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    protected boolean isQueueNameValid(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> validLeafQueues, java.lang.String queueNameProperty, java.lang.String configType) {
        org.apache.ambari.server.state.Config site = cluster.getDesiredConfigByType(configType);
        java.util.Map<java.lang.String, java.lang.String> properties = site.getProperties();
        boolean result = properties.containsKey(queueNameProperty) && validLeafQueues.contains(properties.get(queueNameProperty));
        if (!result) {
            org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info(((("Queue name " + queueNameProperty) + " in ") + configType) + " not defined or not corresponds to valid capacity-scheduler queue");
        }
        return result;
    }

    protected void updateQueueName(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> validLeafQueues, java.lang.String queueNameProperty, java.lang.String configType) throws org.apache.ambari.server.AmbariException {
        java.lang.String recommendQueue = validLeafQueues.iterator().next();
        org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info((((("Update " + queueNameProperty) + " in ") + configType) + " set to ") + recommendQueue);
        java.util.Map<java.lang.String, java.lang.String> updates = java.util.Collections.singletonMap(queueNameProperty, recommendQueue);
        updateConfigurationPropertiesForCluster(cluster, configType, updates, true, true);
    }

    protected java.util.Set<java.lang.String> getCapacitySchedulerLeafQueues(java.util.Map<java.lang.String, java.lang.String> capacitySchedulerMap) {
        java.util.Set<java.lang.String> leafQueues = new java.util.HashSet<>();
        java.util.Stack<java.lang.String> toProcessQueues = new java.util.Stack<>();
        if (capacitySchedulerMap.containsKey(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.YARN_SCHEDULER_CAPACITY_ROOT_QUEUES)) {
            java.util.StringTokenizer queueTokenizer = new java.util.StringTokenizer(capacitySchedulerMap.get(org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.YARN_SCHEDULER_CAPACITY_ROOT_QUEUES), ",");
            while (queueTokenizer.hasMoreTokens()) {
                toProcessQueues.push(queueTokenizer.nextToken());
            } 
        }
        while (!toProcessQueues.empty()) {
            java.lang.String queue = toProcessQueues.pop();
            java.lang.String queueKey = (((org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.YARN_SCHEDULER_CAPACITY_ROOT_QUEUE + ".") + queue) + ".") + org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.QUEUES;
            if (capacitySchedulerMap.containsKey(queueKey)) {
                java.util.StringTokenizer queueTokenizer = new java.util.StringTokenizer(capacitySchedulerMap.get(queueKey), ",");
                while (queueTokenizer.hasMoreTokens()) {
                    toProcessQueues.push((queue + ".") + queueTokenizer.nextToken());
                } 
            } else if (!queue.endsWith(".")) {
                java.lang.String queueName = queue.substring(queue.lastIndexOf('.') + 1);
                leafQueues.add(queueName);
            } else {
                org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.warn(("Queue " + queue) + " is not valid");
            }
        } 
        return leafQueues;
    }

    protected void updateWidgetDefinitionsForService(java.lang.String serviceName, java.util.Map<java.lang.String, java.util.List<java.lang.String>> widgetMap, java.util.Map<java.lang.String, java.lang.String> sectionLayoutMap) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.lang.reflect.Type widgetLayoutType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.WidgetLayout>>>() {}.getType();
        com.google.gson.Gson gson = injector.getInstance(com.google.gson.Gson.class);
        org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO = injector.getInstance(org.apache.ambari.server.orm.dao.WidgetDAO.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
        for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            long clusterID = cluster.getClusterId();
            org.apache.ambari.server.state.Service service = cluster.getServices().get(serviceName);
            if (null == service) {
                continue;
            }
            org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
            java.util.Map<java.lang.String, java.lang.Object> widgetDescriptor = null;
            org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
            org.apache.ambari.server.state.ServiceInfo serviceInfo = stackInfo.getService(serviceName);
            if (serviceInfo == null) {
                org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info(((("Skipping updating widget definition, because " + serviceName) + " service is not present in cluster ") + "cluster_name= ") + cluster.getClusterName());
                continue;
            }
            for (java.lang.String section : widgetMap.keySet()) {
                java.util.List<java.lang.String> widgets = widgetMap.get(section);
                for (java.lang.String widgetName : widgets) {
                    java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> widgetEntities = widgetDAO.findByName(clusterID, widgetName, "ambari", section);
                    if ((widgetEntities != null) && (widgetEntities.size() > 0)) {
                        org.apache.ambari.server.orm.entities.WidgetEntity entityToUpdate = null;
                        if (widgetEntities.size() > 1) {
                            org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info(((("Found more that 1 entity with name = " + widgetName) + " for cluster = ") + cluster.getClusterName()) + ", skipping update.");
                        } else {
                            entityToUpdate = widgetEntities.iterator().next();
                        }
                        if (entityToUpdate != null) {
                            org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.info("Updating widget: " + entityToUpdate.getWidgetName());
                            org.apache.ambari.server.state.stack.WidgetLayoutInfo targetWidgetLayoutInfo = null;
                            java.io.File widgetDescriptorFile = serviceInfo.getWidgetsDescriptorFile();
                            if ((widgetDescriptorFile != null) && widgetDescriptorFile.exists()) {
                                try {
                                    widgetDescriptor = gson.fromJson(new java.io.FileReader(widgetDescriptorFile), widgetLayoutType);
                                } catch (java.lang.Exception ex) {
                                    java.lang.String msg = "Error loading widgets from file: " + widgetDescriptorFile;
                                    org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.error(msg, ex);
                                    widgetDescriptor = null;
                                }
                            }
                            if (widgetDescriptor != null) {
                                org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.debug("Loaded widget descriptor: {}", widgetDescriptor);
                                for (java.lang.Object artifact : widgetDescriptor.values()) {
                                    java.util.List<org.apache.ambari.server.state.stack.WidgetLayout> widgetLayouts = ((java.util.List<org.apache.ambari.server.state.stack.WidgetLayout>) (artifact));
                                    for (org.apache.ambari.server.state.stack.WidgetLayout widgetLayout : widgetLayouts) {
                                        if (widgetLayout.getLayoutName().equals(sectionLayoutMap.get(section))) {
                                            for (org.apache.ambari.server.state.stack.WidgetLayoutInfo layoutInfo : widgetLayout.getWidgetLayoutInfoList()) {
                                                if (layoutInfo.getWidgetName().equals(widgetName)) {
                                                    targetWidgetLayoutInfo = layoutInfo;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (targetWidgetLayoutInfo != null) {
                                entityToUpdate.setMetrics(gson.toJson(targetWidgetLayoutInfo.getMetricsInfo()));
                                entityToUpdate.setWidgetValues(gson.toJson(targetWidgetLayoutInfo.getValues()));
                                entityToUpdate.setDescription(targetWidgetLayoutInfo.getDescription());
                                widgetDAO.merge(entityToUpdate);
                            } else {
                                org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.LOG.warn((("Unable to find widget layout info for " + widgetName) + " in the stack: ") + stackId);
                            }
                        }
                    }
                }
            }
        }
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "can only take the first stack we find until we can support multiple with Kerberos")
    private org.apache.ambari.server.state.StackId getStackId(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        return cluster.getServices().values().iterator().next().getDesiredStackId();
    }
}