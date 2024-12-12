package org.apache.ambari.server.upgrade;
import org.apache.commons.lang.StringUtils;
public class UpgradeCatalog251 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    static final java.lang.String HOST_ROLE_COMMAND_TABLE = "host_role_command";

    static final java.lang.String HRC_IS_BACKGROUND_COLUMN = "is_background";

    protected static final java.lang.String KAFKA_BROKER_CONFIG = "kafka-broker";

    private static final java.lang.String STAGE_TABLE = "stage";

    private static final java.lang.String REQUEST_TABLE = "request";

    private static final java.lang.String CLUSTER_HOST_INFO_COLUMN = "cluster_host_info";

    private static final java.lang.String REQUEST_ID_COLUMN = "request_id";

    protected static final java.lang.String STORM_ENV_CONFIG = "storm-env";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog251.class);

    @com.google.inject.Inject
    public UpgradeCatalog251(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.5.0";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.5.1";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addBackgroundColumnToHostRoleCommand();
        moveClusterHostColumnFromStageToRequest();
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addNewConfigurationsFromXml();
        updateKAFKAConfigs();
        updateSTORMConfigs();
    }

    protected void updateKAFKAConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    java.util.Set<java.lang.String> installedServices = cluster.getServices().keySet();
                    if (installedServices.contains("KAFKA") && (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS)) {
                        org.apache.ambari.server.state.Config kafkaBroker = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.UpgradeCatalog251.KAFKA_BROKER_CONFIG);
                        if (kafkaBroker != null) {
                            java.lang.String listenersPropertyValue = kafkaBroker.getProperties().get("listeners");
                            if (org.apache.commons.lang.StringUtils.isNotEmpty(listenersPropertyValue)) {
                                java.lang.String newListenersPropertyValue = listenersPropertyValue.replaceAll("\\bPLAINTEXT\\b", "PLAINTEXTSASL");
                                if (!newListenersPropertyValue.equals(listenersPropertyValue)) {
                                    updateConfigurationProperties(org.apache.ambari.server.upgrade.UpgradeCatalog251.KAFKA_BROKER_CONFIG, java.util.Collections.singletonMap("listeners", newListenersPropertyValue), true, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addBackgroundColumnToHostRoleCommand() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog251.HOST_ROLE_COMMAND_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog251.HRC_IS_BACKGROUND_COLUMN, java.lang.Short.class, null, 0, false));
    }

    private void moveClusterHostColumnFromStageToRequest() throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog251.CLUSTER_HOST_INFO_COLUMN, byte[].class, null, null, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog251.CLUSTER_HOST_INFO_COLUMN, byte[].class, null, null, false);
        dbAccessor.moveColumnToAnotherTable(org.apache.ambari.server.upgrade.UpgradeCatalog251.STAGE_TABLE, sourceColumn, org.apache.ambari.server.upgrade.UpgradeCatalog251.REQUEST_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog251.REQUEST_TABLE, targetColumn, org.apache.ambari.server.upgrade.UpgradeCatalog251.REQUEST_ID_COLUMN, "{}".getBytes());
    }

    protected void updateSTORMConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    java.util.Set<java.lang.String> installedServices = cluster.getServices().keySet();
                    if (installedServices.contains("STORM")) {
                        org.apache.ambari.server.state.Config stormEnv = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.UpgradeCatalog251.STORM_ENV_CONFIG);
                        java.lang.String content = stormEnv.getProperties().get("content");
                        if ((content != null) && (!content.contains("STORM_AUTOCREDS_LIB_DIR"))) {
                            java.util.Map<java.lang.String, java.lang.String> newProperties = new java.util.HashMap<>();
                            java.lang.String stormEnvConfigs = "\n# set storm-auto creds\n" + ((((("# check if storm_jaas.conf in config, only enable storm_auto_creds in secure mode.\n" + "STORM_JAAS_CONF=$STORM_HOME/conf/storm_jaas.conf\n") + "STORM_AUTOCREDS_LIB_DIR=$STORM_HOME/external/storm-autocreds\n") + "if [ -f $STORM_JAAS_CONF ] && [ -d $STORM_AUTOCREDS_LIB_DIR ]; then\n") + "  export STORM_EXT_CLASSPATH=$STORM_AUTOCREDS_LIB_DIR\n") + "fi\n");
                            content += stormEnvConfigs;
                            newProperties.put("content", content);
                            updateConfigurationPropertiesForCluster(cluster, "storm-env", newProperties, true, false);
                        }
                    }
                }
            }
        }
    }
}