package org.apache.ambari.server.upgrade;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.MapUtils;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.YARN_SERVICE;
public class UpgradeCatalog271 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog271.class);

    private static final java.lang.String SOLR_NEW_LOG4J2_XML = "<Configuration>\n" + (((((((((((((((((((((((((((((((((((((((((((((((((((((((("  <Appenders>\n" + "\n") + "    <Console name=\"STDOUT\" target=\"SYSTEM_OUT\">\n") + "      <PatternLayout>\n") + "        <Pattern>\n") + "          %d{ISO8601} [%t] %-5p [%X{collection} %X{shard} %X{replica} %X{core}] %C (%F:%L) - %m%n\n") + "        </Pattern>\n") + "      </PatternLayout>\n") + "    </Console>\n") + "\n") + "    <RollingFile\n") + "        name=\"RollingFile\"\n") + "        fileName=\"{{infra_solr_log_dir}}/solr.log\"\n") + "        filePattern=\"{{infra_solr_log_dir}}/solr.log.%i\" >\n") + "      <PatternLayout>\n") + "        <Pattern>\n") + "          %d{ISO8601} [%t] %-5p [%X{collection} %X{shard} %X{replica} %X{core}] %C (%F:%L) - %m%n\n") + "        </Pattern>\n") + "      </PatternLayout>\n") + "      <Policies>\n") + "        <OnStartupTriggeringPolicy />\n") + "        <SizeBasedTriggeringPolicy size=\"{{infra_log_maxfilesize}} MB\"/>\n") + "      </Policies>\n") + "      <DefaultRolloverStrategy max=\"{{infra_log_maxbackupindex}}\"/>\n") + "    </RollingFile>\n") + "\n") + "    <RollingFile\n") + "        name=\"SlowFile\"\n") + "        fileName=\"{{infra_solr_log_dir}}/solr_slow_requests.log\"\n") + "        filePattern=\"{{infra_solr_log_dir}}/solr_slow_requests.log.%i\" >\n") + "      <PatternLayout>\n") + "        <Pattern>\n") + "          %d{ISO8601} [%t] %-5p [%X{collection} %X{shard} %X{replica} %X{core}] %C (%F:%L) - %m%n\n") + "        </Pattern>\n") + "      </PatternLayout>\n") + "      <Policies>\n") + "        <OnStartupTriggeringPolicy />\n") + "        <SizeBasedTriggeringPolicy size=\"{{infra_log_maxfilesize}} MB\"/>\n") + "      </Policies>\n") + "      <DefaultRolloverStrategy max=\"{{infra_log_maxbackupindex}}\"/>\n") + "    </RollingFile>\n") + "\n") + "  </Appenders>\n") + "  <Loggers>\n") + "    <Logger name=\"org.apache.hadoop\" level=\"warn\"/>\n") + "    <Logger name=\"org.apache.solr.update.LoggingInfoStream\" level=\"off\"/>\n") + "    <Logger name=\"org.apache.zookeeper\" level=\"warn\"/>\n") + "    <Logger name=\"org.apache.solr.core.SolrCore.SlowRequest\" level=\"warn\" additivity=\"false\">\n") + "      <AppenderRef ref=\"SlowFile\"/>\n") + "    </Logger>\n") + "\n") + "    <Root level=\"warn\">\n") + "      <AppenderRef ref=\"RollingFile\"/>\n") + "      <!-- <AppenderRef ref=\"STDOUT\"/> -->\n") + "    </Root>\n") + "  </Loggers>\n") + "</Configuration>");

    private static final java.lang.String SERVICE_CONFIG_MAPPING_TABLE = "serviceconfigmapping";

    private static final java.lang.String CLUSTER_CONFIG_TABLE = "clusterconfig";

    protected static final java.lang.String CLUSTERS_TABLE = "clusters";

    protected static final java.lang.String CLUSTERS_BLUEPRINT_PROVISIONING_STATE_COLUMN = "blueprint_provisioning_state";

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    public UpgradeCatalog271(com.google.inject.Injector injector) {
        super(injector);
        daoUtils = injector.getInstance(org.apache.ambari.server.orm.dao.DaoUtils.class);
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.7.1";
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.7.0";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addBlueprintProvisioningState();
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addNewConfigurationsFromXml();
        updateRangerLogDirConfigs();
        updateRangerKmsDbUrl();
        renameAmbariInfraService();
        removeLogSearchPatternConfigs();
        updateSolrConfigurations();
        updateTimelineReaderAddress();
    }

    protected void updateRangerLogDirConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    java.util.Set<java.lang.String> installedServices = cluster.getServices().keySet();
                    if (installedServices.contains("RANGER")) {
                        org.apache.ambari.server.state.Config rangerEnvConfig = cluster.getDesiredConfigByType("ranger-env");
                        org.apache.ambari.server.state.Config rangerAdminSiteConfig = cluster.getDesiredConfigByType("ranger-admin-site");
                        org.apache.ambari.server.state.Config rangerUgsyncSiteConfig = cluster.getDesiredConfigByType("ranger-ugsync-site");
                        if (rangerEnvConfig != null) {
                            java.lang.String rangerAdminLogDir = rangerEnvConfig.getProperties().get("ranger_admin_log_dir");
                            java.lang.String rangerUsersyncLogDir = rangerEnvConfig.getProperties().get("ranger_usersync_log_dir");
                            if ((rangerAdminLogDir != null) && (rangerAdminSiteConfig != null)) {
                                java.util.Map<java.lang.String, java.lang.String> newProperty = new java.util.HashMap<java.lang.String, java.lang.String>();
                                newProperty.put("ranger.logs.base.dir", rangerAdminLogDir);
                                updateConfigurationPropertiesForCluster(cluster, "ranger-admin-site", newProperty, true, false);
                            }
                            if (((rangerUsersyncLogDir != null) && (rangerUgsyncSiteConfig != null)) && rangerUgsyncSiteConfig.getProperties().containsKey("ranger.usersync.logdir")) {
                                java.util.Map<java.lang.String, java.lang.String> updateProperty = new java.util.HashMap<java.lang.String, java.lang.String>();
                                updateProperty.put("ranger.usersync.logdir", rangerUsersyncLogDir);
                                updateConfigurationPropertiesForCluster(cluster, "ranger-ugsync-site", updateProperty, true, false);
                            }
                            java.util.Set<java.lang.String> removeProperties = com.google.common.collect.Sets.newHashSet("ranger_admin_log_dir", "ranger_usersync_log_dir");
                            removeConfigurationPropertiesFromCluster(cluster, "ranger-env", removeProperties);
                        }
                    }
                }
            }
        }
    }

    protected void updateRangerKmsDbUrl() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    java.util.Set<java.lang.String> installedServices = cluster.getServices().keySet();
                    if (installedServices.contains("RANGER_KMS")) {
                        org.apache.ambari.server.state.Config rangerKmsPropertiesConfig = cluster.getDesiredConfigByType("kms-properties");
                        org.apache.ambari.server.state.Config rangerKmsEnvConfig = cluster.getDesiredConfigByType("kms-env");
                        org.apache.ambari.server.state.Config rangerKmsDbksConfig = cluster.getDesiredConfigByType("dbks-site");
                        if (rangerKmsPropertiesConfig != null) {
                            java.lang.String dbFlavor = rangerKmsPropertiesConfig.getProperties().get("DB_FLAVOR");
                            java.lang.String dbHost = rangerKmsPropertiesConfig.getProperties().get("db_host");
                            java.lang.String rangerKmsRootDbUrl = "";
                            if ((dbFlavor != null) && (dbHost != null)) {
                                java.lang.String port = "";
                                if (rangerKmsDbksConfig != null) {
                                    java.lang.String rangerKmsDbUrl = rangerKmsDbksConfig.getProperties().get("ranger.ks.jpa.jdbc.url");
                                    if (rangerKmsDbUrl != null) {
                                        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(:[0-9]+)");
                                        java.util.regex.Matcher matcher = pattern.matcher(rangerKmsDbUrl);
                                        if (matcher.find()) {
                                            port = matcher.group();
                                        }
                                    }
                                }
                                if ("MYSQL".equalsIgnoreCase(dbFlavor)) {
                                    rangerKmsRootDbUrl = ("jdbc:mysql://" + dbHost) + (!port.equalsIgnoreCase("") ? port : ":3306");
                                } else if ("ORACLE".equalsIgnoreCase(dbFlavor)) {
                                    rangerKmsRootDbUrl = ("jdbc:oracle:thin:@//" + dbHost) + (!port.equalsIgnoreCase("") ? port : ":1521");
                                } else if ("POSTGRES".equalsIgnoreCase(dbFlavor)) {
                                    rangerKmsRootDbUrl = (("jdbc:postgresql://" + dbHost) + (!port.equalsIgnoreCase("") ? port : ":5432")) + "/postgres";
                                } else if ("MSSQL".equalsIgnoreCase(dbFlavor)) {
                                    rangerKmsRootDbUrl = ("jdbc:sqlserver://" + dbHost) + (!port.equalsIgnoreCase("") ? port : ":1433");
                                } else if ("SQLA".equalsIgnoreCase(dbFlavor)) {
                                    rangerKmsRootDbUrl = (("jdbc:sqlanywhere:host=" + dbHost) + (!port.equalsIgnoreCase("") ? port : ":2638")) + ";";
                                }
                                java.util.Map<java.lang.String, java.lang.String> newProperty = new java.util.HashMap<java.lang.String, java.lang.String>();
                                newProperty.put("ranger_kms_privelege_user_jdbc_url", rangerKmsRootDbUrl);
                                if (rangerKmsEnvConfig != null) {
                                    updateConfigurationPropertiesForCluster(cluster, "kms-env", newProperty, true, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void updateTimelineReaderAddress() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class).getClusters();
        if ((clusters == null) || (clusters.getClusters() == null)) {
            return;
        }
        for (org.apache.ambari.server.state.Cluster cluster : clusters.getClusters().values()) {
            java.util.Set<java.lang.String> installedServices = cluster.getServices().keySet();
            if (installedServices.contains(org.apache.ambari.server.upgrade.UpgradeCatalog270.YARN_SERVICE) && cluster.getService(org.apache.ambari.server.upgrade.UpgradeCatalog270.YARN_SERVICE).getServiceComponents().keySet().contains("TIMELINE_READER")) {
                java.lang.String timelineReaderHost = hostNameOf(cluster, org.apache.ambari.server.upgrade.UpgradeCatalog270.YARN_SERVICE, "TIMELINE_READER").orElse("localhost");
                updateProperty(cluster, "yarn-site", "yarn.timeline-service.reader.webapp.address", timelineReaderHost + ":8198");
                updateProperty(cluster, "yarn-site", "yarn.timeline-service.reader.webapp.https.address", timelineReaderHost + ":8199");
            }
        }
    }

    private void updateProperty(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyName, java.lang.String newValue) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(configType);
        if (config == null) {
            return;
        }
        java.lang.String oldValue = config.getProperties().get(propertyName);
        if (oldValue != null) {
            java.util.Map<java.lang.String, java.lang.String> newProperty = new java.util.HashMap<>();
            newProperty.put(propertyName, newValue);
            updateConfigurationPropertiesForCluster(cluster, configType, newProperty, true, false);
        }
    }

    private java.util.Optional<java.lang.String> hostNameOf(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        try {
            org.apache.ambari.server.state.ServiceComponent component = cluster.getService(serviceName).getServiceComponent(componentName);
            java.util.Set<java.lang.String> hosts = component.getServiceComponentHosts().keySet();
            return hosts.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(hosts.iterator().next());
        } catch (org.apache.ambari.server.ServiceComponentNotFoundException | org.apache.ambari.server.ServiceNotFoundException e) {
            return java.util.Optional.empty();
        }
    }

    protected void renameAmbariInfraService() {
        org.apache.ambari.server.upgrade.UpgradeCatalog271.LOG.info("Renaming service AMBARI_INFRA to AMBARI_INFRA_SOLR in config group records");
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters == null)
            return;

        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if (org.apache.commons.collections.MapUtils.isEmpty(clusterMap))
            return;

        javax.persistence.EntityManager entityManager = getEntityManagerProvider().get();
        executeInTransaction(() -> {
            javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ConfigGroupEntity> serviceConfigUpdate = entityManager.createQuery("UPDATE ConfigGroupEntity SET serviceName = :newServiceName WHERE serviceName = :oldServiceName", org.apache.ambari.server.orm.entities.ConfigGroupEntity.class);
            serviceConfigUpdate.setParameter("newServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            serviceConfigUpdate.setParameter("oldServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            serviceConfigUpdate.executeUpdate();
        });
        executeInTransaction(() -> {
            javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ConfigGroupEntity> serviceConfigUpdate = entityManager.createQuery("UPDATE ConfigGroupEntity SET tag = :newServiceName WHERE tag = :oldServiceName", org.apache.ambari.server.orm.entities.ConfigGroupEntity.class);
            serviceConfigUpdate.setParameter("newServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            serviceConfigUpdate.setParameter("oldServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            serviceConfigUpdate.executeUpdate();
        });
        executeInTransaction(() -> {
            javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> upgradeHistoryUpdate = entityManager.createQuery("UPDATE UpgradeHistoryEntity SET serviceName = :newServiceName WHERE serviceName = :oldServiceName", org.apache.ambari.server.orm.entities.UpgradeHistoryEntity.class);
            upgradeHistoryUpdate.setParameter("newServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            upgradeHistoryUpdate.setParameter("oldServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            upgradeHistoryUpdate.executeUpdate();
        });
        entityManager.getEntityManagerFactory().getCache().evictAll();
        clusters.invalidateAllClusters();
    }

    protected void removeLogSearchPatternConfigs() throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessor dba = (dbAccessor != null) ? dbAccessor : injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class);
        java.lang.String configSuffix = "-logsearch-conf";
        java.lang.String serviceConfigMappingRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE config_id IN (SELECT config_id from %s where type_name like '%%%s')", org.apache.ambari.server.upgrade.UpgradeCatalog271.SERVICE_CONFIG_MAPPING_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog271.CLUSTER_CONFIG_TABLE, configSuffix);
        java.lang.String clusterConfigRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE type_name like '%%%s'", org.apache.ambari.server.upgrade.UpgradeCatalog271.CLUSTER_CONFIG_TABLE, configSuffix);
        dba.executeQuery(serviceConfigMappingRemoveSQL);
        dba.executeQuery(clusterConfigRemoveSQL);
    }

    protected void addBlueprintProvisioningState() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog271.CLUSTERS_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog271.CLUSTERS_BLUEPRINT_PROVISIONING_STATE_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.state.BlueprintProvisioningState.NONE, true));
    }

    protected void updateSolrConfigurations() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters == null)
            return;

        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if ((clusterMap == null) || clusterMap.isEmpty())
            return;

        for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            updateConfig(cluster, "logsearch-service_logs-solrconfig", content -> updateLuceneMatchVersion(content, "7.4.0"));
            updateConfig(cluster, "logsearch-audit_logs-solrconfig", content -> updateLuceneMatchVersion(content, "7.4.0"));
            updateConfig(cluster, "infra-solr-log4j", content -> org.apache.ambari.server.upgrade.UpgradeCatalog271.SOLR_NEW_LOG4J2_XML);
        }
    }

    private void updateConfig(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.function.Function<java.lang.String, java.lang.String> contentUpdater) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(configType);
        if (config == null)
            return;

        if ((config.getProperties() == null) || (!config.getProperties().containsKey("content")))
            return;

        java.lang.String content = config.getProperties().get("content");
        content = contentUpdater.apply(content);
        updateConfigurationPropertiesForCluster(cluster, configType, java.util.Collections.singletonMap("content", content), true, true);
    }

    private java.lang.String updateLuceneMatchVersion(java.lang.String content, java.lang.String newLuceneMatchVersion) {
        return content.replaceAll("<luceneMatchVersion>.*</luceneMatchVersion>", ("<luceneMatchVersion>" + newLuceneMatchVersion) + "</luceneMatchVersion>");
    }
}