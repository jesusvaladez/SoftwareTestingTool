package org.apache.ambari.server.controller.internal;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;
public class BlueprintConfigurationProcessor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.class);

    private static final java.lang.String COMMAND_RETRY_ENABLED_PROPERTY_NAME = "command_retry_enabled";

    private static final java.lang.String COMMANDS_TO_RETRY_PROPERTY_NAME = "commands_to_retry";

    private static final java.lang.String COMMAND_RETRY_MAX_TIME_IN_SEC_PROPERTY_NAME = "command_retry_max_time_in_sec";

    private static final java.lang.String COMMAND_RETRY_ENABLED_DEFAULT = "true";

    private static final java.lang.String COMMANDS_TO_RETRY_DEFAULT = "INSTALL,START";

    private static final java.lang.String COMMAND_RETRY_MAX_TIME_IN_SEC_DEFAULT = "600";

    private static final java.lang.String CLUSTER_ENV_CONFIG_TYPE_NAME = "cluster-env";

    private static final java.lang.String HBASE_SITE_HBASE_COPROCESSOR_MASTER_CLASSES = "hbase.coprocessor.master.classes";

    private static final java.lang.String HBASE_SITE_HBASE_COPROCESSOR_REGION_CLASSES = "hbase.coprocessor.region.classes";

    private static final java.lang.String HAWQ_SITE_HAWQ_STANDBY_ADDRESS_HOST = "hawq_standby_address_host";

    private static final java.lang.String HAWQSTANDBY = "HAWQSTANDBY";

    private static final java.lang.String HDFS_HA_INITIAL_CONFIG_TYPE = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME;

    private static final java.lang.String HDFS_ACTIVE_NAMENODE_PROPERTY_NAME = "dfs_ha_initial_namenode_active";

    private static final java.lang.String HDFS_STANDBY_NAMENODE_PROPERTY_NAME = "dfs_ha_initial_namenode_standby";

    private static final java.lang.String HDFS_ACTIVE_NAMENODE_SET_PROPERTY_NAME = "dfs_ha_initial_namenode_active_set";

    private static final java.lang.String HDFS_STANDBY_NAMENODE_SET_PROPERTY_NAME = "dfs_ha_initial_namenode_standby_set";

    private static final java.lang.String HDFS_HA_INITIAL_CLUSTER_ID_PROPERTY_NAME = "dfs_ha_initial_cluster_id";

    private static final java.util.Set<java.lang.String> HDFS_HA_INITIAL_PROPERTIES = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_SET_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_SET_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CLUSTER_ID_PROPERTY_NAME);

    public static final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> TEMPORARY_PROPERTIES_FOR_CLUSTER_DEPLOYMENT = com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_PROPERTIES);

    private static final java.lang.String HADOOP_ENV_CONFIG_TYPE_NAME = "hadoop-env";

    private static final java.lang.String RANGER_TAGSYNC_SITE_CONFIG_TYPE_NAME = "ranger-tagsync-site";

    private static final java.lang.String LOCALHOST = "localhost";

    protected static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> singleHostTopologyUpdaters = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> multiHostTopologyUpdaters = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> dbHostTopologyUpdaters = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> mPropertyUpdaters = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> nonTopologyUpdaters = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> removePropertyUpdaters = new java.util.HashMap<>();

    private static final java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> allUpdaters = new java.util.ArrayList<>();

    private static final java.util.regex.Pattern HOSTGROUP_PORT_REGEX = java.util.regex.Pattern.compile("%HOSTGROUP::(\\S+?)%:?(\\d+)?");

    private static final java.util.regex.Pattern LOCALHOST_PORT_REGEX = java.util.regex.Pattern.compile("localhost:?(\\d+)?");

    private static final java.util.regex.Pattern PLACEHOLDER = java.util.regex.Pattern.compile("\\{\\{.*\\}\\}");

    private static final java.lang.String BIND_ALL_IP_ADDRESS = "0.0.0.0";

    private static final java.util.Set<java.lang.String> configPropertiesWithHASupport = new java.util.HashSet<>(java.util.Arrays.asList("fs.defaultFS", "hbase.rootdir", "instance.volumes", "policymgr_external_url", "xasecure.audit.destination.hdfs.dir"));

    private static final java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> PROPERTIES_FOR_HADOOP_PROXYUSER = com.google.common.collect.ImmutableSet.of(org.apache.commons.lang3.tuple.Pair.of("oozie-env", "oozie_user"), org.apache.commons.lang3.tuple.Pair.of("hive-env", "hive_user"), org.apache.commons.lang3.tuple.Pair.of("hive-env", "webhcat_user"), org.apache.commons.lang3.tuple.Pair.of("hbase-env", "hbase_user"), org.apache.commons.lang3.tuple.Pair.of("falcon-env", "falcon_user"));

    private static final java.lang.String HADOOP_PROXYUSER_HOSTS_FORMAT = "hadoop.proxyuser.%s.hosts";

    private static final java.lang.String HADOOP_PROXYUSER_GROUPS_FORMAT = "hadoop.proxyuser.%s.groups";

    private org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter[] getExportPropertyFilters(java.util.Map<java.lang.Long, java.util.Set<java.lang.String>> authToLocalPerClusterMap) {
        return new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter[]{ new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PasswordPropertyFilter(), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("tez.tez-ui.history-url.base", "tez-site"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("admin_server_host", "kerberos-env"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("kdc_hosts", "kerberos-env"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("master_kdc", "kerberos-env"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("realm", "kerberos-env"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("kdc_type", "kerberos-env"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("ldap-url", "kerberos-env"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("container_dn", "kerberos-env"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter("domains", "krb5-conf"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_ENV_CONFIG_TYPE_NAME), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_ENV_CONFIG_TYPE_NAME), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_SET_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_ENV_CONFIG_TYPE_NAME), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_SET_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_ENV_CONFIG_TYPE_NAME), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_SET_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SimplePropertyNameExportFilter(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_SET_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.StackPropertyTypeFilter(), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.KerberosAuthToLocalRulesFilter(authToLocalPerClusterMap) };
    }

    private static final org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter[] clusterUpdatePropertyFilters = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter[]{ new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.DependencyEqualsFilter("hbase.security.authorization", "hbase-site", "true"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.DependencyNotEqualsFilter("hive.server2.authentication", "hive-site", "NONE"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.ConditionalPropertyFilter("hbase-site", "hbase.rpc.controllerfactory.class", "org.apache.hadoop.hbase.ipc.controller.ServerRpcControllerFactory"), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFSNameNodeHAFilter(), new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HawqHAFilter() };

    private org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    public BlueprintConfigurationProcessor(org.apache.ambari.server.topology.ClusterTopology clusterTopology) {
        this.clusterTopology = clusterTopology;
        initRemovePropertyUpdaters();
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> getRemovePropertyUpdaters() {
        return removePropertyUpdaters;
    }

    public void initRemovePropertyUpdaters() {
        if (containsHostFromHostGroups("oozie-site", "oozie.service.JPAService.jdbc.url")) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieSiteUpdaters = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.get("oozie-site");
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieEnvUpdaters = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.get("oozie-env");
            if (oozieSiteUpdaters == null) {
                oozieSiteUpdaters = new java.util.HashMap<>();
            }
            if (oozieEnvUpdaters == null) {
                oozieEnvUpdaters = new java.util.HashMap<>();
            }
            oozieEnvUpdaters.put("oozie_existing_mysql_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("OOZIE_SERVER"));
            oozieEnvUpdaters.put("oozie_existing_oracle_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("OOZIE_SERVER"));
            oozieEnvUpdaters.put("oozie_existing_postgresql_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("OOZIE_SERVER"));
            oozieSiteUpdaters.put("oozie.service.JPAService.jdbc.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("OOZIE_SERVER"));
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.put("oozie-env", oozieEnvUpdaters);
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters.put("oozie-site", oozieSiteUpdaters);
        } else {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieEnvOriginalValueMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieSiteOriginalValueMap = new java.util.HashMap<>();
            oozieEnvOriginalValueMap.put("oozie_existing_mysql_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
            oozieEnvOriginalValueMap.put("oozie_existing_oracle_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
            oozieEnvOriginalValueMap.put("oozie_existing_postgresql_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
            oozieSiteOriginalValueMap.put("oozie.service.JPAService.jdbc.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
            removePropertyUpdaters.put("oozie-env", oozieEnvOriginalValueMap);
            removePropertyUpdaters.put("oozie-site", oozieSiteOriginalValueMap);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hiveEnvOriginalValueMap = new java.util.HashMap<>();
        hiveEnvOriginalValueMap.put("hive_existing_oracle_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
        hiveEnvOriginalValueMap.put("hive_existing_mssql_server_2_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
        hiveEnvOriginalValueMap.put("hive_existing_mssql_server_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
        hiveEnvOriginalValueMap.put("hive_existing_postgresql_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
        hiveEnvOriginalValueMap.put("hive_existing_mysql_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OriginalValuePropertyUpdater());
        removePropertyUpdaters.put("hive-env", hiveEnvOriginalValueMap);
    }

    private boolean containsHostFromHostGroups(java.lang.String configType, java.lang.String propertyName) {
        java.lang.String propertyValue = clusterTopology.getConfiguration().getPropertyValue(configType, propertyName);
        if (org.apache.commons.lang.StringUtils.isEmpty(propertyValue)) {
            return false;
        }
        java.util.regex.Matcher m = org.apache.ambari.server.topology.HostGroup.HOSTGROUP_REGEX.matcher(propertyValue);
        if (m.find()) {
            return true;
        }
        for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : clusterTopology.getHostGroupInfo().values()) {
            java.util.Collection<java.lang.String> hosts = groupInfo.getHostNames();
            for (java.lang.String host : hosts) {
                if (propertyValue.contains(host)) {
                    return true;
                }
            }
        }
        return false;
    }

    public java.util.Set<java.lang.String> getRequiredHostGroups() {
        java.util.Set<java.lang.String> requiredHostGroups = new java.util.HashSet<>();
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters = createCollectionOfUpdaters();
        for (java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> updaterMap : updaters) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> entry : updaterMap.entrySet()) {
                java.lang.String type = entry.getKey();
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> updaterEntry : entry.getValue().entrySet()) {
                    java.lang.String propertyName = updaterEntry.getKey();
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater updater = updaterEntry.getValue();
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterProps = clusterTopology.getConfiguration().getFullProperties();
                    java.util.Map<java.lang.String, java.lang.String> typeMap = clusterProps.get(type);
                    if (((typeMap != null) && typeMap.containsKey(propertyName)) && (typeMap.get(propertyName) != null)) {
                        requiredHostGroups.addAll(updater.getRequiredHostGroups(propertyName, typeMap.get(propertyName), clusterProps, clusterTopology));
                    }
                    for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : clusterTopology.getHostGroupInfo().values()) {
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hgConfigProps = groupInfo.getConfiguration().getProperties();
                        java.util.Map<java.lang.String, java.lang.String> hgTypeMap = hgConfigProps.get(type);
                        if ((hgTypeMap != null) && hgTypeMap.containsKey(propertyName)) {
                            requiredHostGroups.addAll(updater.getRequiredHostGroups(propertyName, hgTypeMap.get(propertyName), hgConfigProps, clusterTopology));
                        }
                    }
                }
            }
        }
        java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> propertiesWithUpdaters = getAllPropertiesWithUpdaters(updaters);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userDefinedClusterProperties = clusterTopology.getConfiguration().getFullProperties(1);
        addRequiredHostgroupsByDefaultUpdater(userDefinedClusterProperties, propertiesWithUpdaters, requiredHostGroups);
        clusterTopology.getHostGroupInfo().values().stream().forEach(hostGroup -> {
            org.apache.ambari.server.topology.Configuration hostGroupConfig = hostGroup.getConfiguration();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostGroupConfigProps = hostGroupConfig.getFullProperties(1);
            addRequiredHostgroupsByDefaultUpdater(hostGroupConfigProps, propertiesWithUpdaters, requiredHostGroups);
        });
        return requiredHostGroups;
    }

    private void addRequiredHostgroupsByDefaultUpdater(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> propertiesWithUpdaters, java.util.Set<java.lang.String> hostGroupAccumulator) {
        properties.entrySet().forEach(configTypeEntry -> {
            java.lang.String configType = configTypeEntry.getKey();
            configTypeEntry.getValue().entrySet().forEach(propertyEntry -> {
                java.lang.String propertyName = propertyEntry.getKey();
                java.lang.String oldValue = propertyEntry.getValue();
                if (!propertiesWithUpdaters.contains(org.apache.commons.lang3.tuple.Pair.of(configType, propertyName))) {
                    java.util.Collection<java.lang.String> requiredHostGroups = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater.defaultUpdater().getRequiredHostGroups(propertyName, oldValue, properties, clusterTopology);
                    if (!requiredHostGroups.isEmpty()) {
                        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("The following host groups are required by applying the default property updater on {}/{} property: {}", configType, propertyName, requiredHostGroups);
                    }
                    hostGroupAccumulator.addAll(requiredHostGroups);
                }
            });
        });
    }

    public java.util.Set<java.lang.String> doUpdateForClusterCreate() throws org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        java.util.Set<java.lang.String> configTypesUpdated = new java.util.HashSet<>();
        org.apache.ambari.server.topology.Configuration clusterConfig = clusterTopology.getConfiguration();
        doRecommendConfigurations(clusterConfig, configTypesUpdated);
        doFilterPriorToClusterUpdate(clusterConfig, configTypesUpdated);
        java.util.Set<java.lang.String> propertiesMoved = clusterConfig.moveProperties(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_ENV_CONFIG_TYPE_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_PROPERTIES);
        if (!propertiesMoved.isEmpty()) {
            configTypesUpdated.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_ENV_CONFIG_TYPE_NAME);
            configTypesUpdated.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE);
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterProps = clusterConfig.getFullProperties();
        doGeneralPropertyUpdatesForClusterCreate(clusterConfig, clusterProps, configTypesUpdated);
        if (clusterTopology.isNameNodeHAEnabled()) {
            doNameNodeHAUpdateOnClusterCreation(clusterConfig, clusterProps, configTypesUpdated);
        }
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.injectDefaults(clusterConfig, configTypesUpdated, clusterTopology.getBlueprint().getServices());
        setStackToolsAndFeatures(clusterConfig, configTypesUpdated);
        addExcludedConfigProperties(clusterConfig, configTypesUpdated, clusterTopology.getBlueprint().getStack());
        trimProperties(clusterConfig, clusterTopology);
        return configTypesUpdated;
    }

    private void doNameNodeHAUpdateOnClusterCreation(org.apache.ambari.server.topology.Configuration clusterConfig, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterProps, java.util.Set<java.lang.String> configTypesUpdated) throws org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        final java.util.Collection<java.lang.String> nnHosts = clusterTopology.getHostAssignmentsForComponent("NAMENODE");
        if (nnHosts.isEmpty()) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("NAMENODE HA is enabled but there are no NAMENODE components in the cluster. Assuming external name nodes.");
            try {
                new org.apache.ambari.server.topology.validators.NameNodeHaValidator().validateExternalNamenodeHa(clusterTopology);
            } catch (org.apache.ambari.server.topology.InvalidTopologyException ex) {
                throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException(ex.getMessage(), ex);
            }
            return;
        }
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteConfig = clusterConfig.getFullProperties().get("hdfs-site");
        java.lang.String nameservices = hdfsSiteConfig.get("dfs.nameservices");
        java.lang.String int_nameservices = hdfsSiteConfig.get("dfs.internal.nameservices");
        if ((int_nameservices == null) && (nameservices != null)) {
            clusterConfig.setProperty("hdfs-site", "dfs.internal.nameservices", nameservices);
        }
        java.lang.String[] parsedNameServices = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameServices(hdfsSiteConfig);
        if (parsedNameServices.length == 1) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Processing a single HDFS NameService, which indicates a default HDFS NameNode HA deployment");
            if ((!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isNameNodeHAInitialActiveNodeSet(clusterProps)) && (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isNameNodeHAInitialStandbyNodeSet(clusterProps))) {
                if (nnHosts.size() == 1) {
                    throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException(("NAMENODE HA requires at least two hosts running NAMENODE but there is " + "only one: ") + nnHosts.iterator().next());
                }
                java.util.Iterator<java.lang.String> nnHostIterator = nnHosts.iterator();
                clusterConfig.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_PROPERTY_NAME, nnHostIterator.next());
                clusterConfig.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_PROPERTY_NAME, nnHostIterator.next());
                configTypesUpdated.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE);
            }
        } else if ((!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isPropertySet(clusterProps, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_SET_PROPERTY_NAME)) && (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isPropertySet(clusterProps, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_SET_PROPERTY_NAME))) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Processing multiple HDFS NameService instances, which indicates a NameNode Federation deployment");
            if (parsedNameServices.length > 1) {
                java.util.Set<java.lang.String> activeNameNodeHostnames = new java.util.HashSet<>();
                java.util.Set<java.lang.String> standbyNameNodeHostnames = new java.util.HashSet<>();
                for (java.lang.String nameService : parsedNameServices) {
                    java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
                    java.lang.String[] nameNodes = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameNodes(nameService, hdfsSiteConfig);
                    for (java.lang.String nameNode : nameNodes) {
                        java.lang.String propertyName = (("dfs.namenode.rpc-address." + nameService) + ".") + nameNode;
                        java.lang.String propertyValue = hdfsSiteConfig.get(propertyName);
                        if (propertyValue == null) {
                            throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException((("NameNode HA property = " + propertyName) + " is not found in the cluster config.  This indicates an error in configuration for HA/Federated clusters.  ") + "Please recheck the HDFS configuration and try this deployment again");
                        }
                        java.lang.String hostName = propertyValue.split(":")[0];
                        hostNames.add(hostName);
                    }
                    if (hostNames.size() < 2) {
                        throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException((((("NAMENODE HA for nameservice = " + nameService) + " requires at least 2 hosts running NAMENODE but there are: ") + hostNames.size()) + " Hosts: ") + hostNames);
                    } else {
                        activeNameNodeHostnames.add(hostNames.get(0));
                        standbyNameNodeHostnames.add(hostNames.get(1));
                    }
                }
                if ((!activeNameNodeHostnames.isEmpty()) && (!standbyNameNodeHostnames.isEmpty())) {
                    clusterConfig.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_SET_PROPERTY_NAME, java.lang.String.join(",", activeNameNodeHostnames));
                    clusterConfig.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_SET_PROPERTY_NAME, java.lang.String.join(",", standbyNameNodeHostnames));
                    if (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isPropertySet(clusterProps, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CLUSTER_ID_PROPERTY_NAME)) {
                        clusterConfig.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CLUSTER_ID_PROPERTY_NAME, getClusterName());
                    }
                    configTypesUpdated.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE);
                } else {
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.warn("Error in processing the set of active/standby namenodes in this federated cluster, please check hdfs-site configuration");
                }
                doTagSyncSiteUpdateForNamenodeNFederationEnabledOnClusterCreation(clusterConfig, clusterProps, configTypesUpdated);
            }
        }
    }

    private void doTagSyncSiteUpdateForNamenodeNFederationEnabledOnClusterCreation(org.apache.ambari.server.topology.Configuration clusterConfig, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterProps, java.util.Set<java.lang.String> configTypesUpdated) throws org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteConfig = clusterConfig.getFullProperties().get("hdfs-site");
        java.lang.String[] parsedNameServices = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameServices(hdfsSiteConfig);
        java.lang.String clusterName = getClusterName();
        boolean isRangerHDFSPluginEnabled = false;
        java.lang.String rangerHDFSPluginServiceName = "";
        java.lang.String atlasServerComponentName = "ATLAS_SERVER";
        java.lang.String rangerAdminComponentName = "RANGER_ADMIN";
        java.lang.String rangerTagsyncComponentName = "RANGER_TAGSYNC";
        boolean isRangerAdminToBeInstalled = clusterTopology.getHostGroupsForComponent(rangerAdminComponentName).size() >= 1;
        boolean isRangerTagsyncToBeInstalled = clusterTopology.getHostGroupsForComponent(rangerTagsyncComponentName).size() >= 1;
        boolean isAtlasServerToBeInstalled = clusterTopology.getHostGroupsForComponent(atlasServerComponentName).size() >= 1;
        if (isRangerAdminToBeInstalled) {
            java.util.Map<java.lang.String, java.lang.String> rangerHDFSPluginProperties = clusterProps.get("ranger-hdfs-plugin-properties");
            java.lang.String rangerHDFSPluginEnabledValue = rangerHDFSPluginProperties.getOrDefault("ranger-hdfs-plugin-enabled", "No");
            isRangerHDFSPluginEnabled = "yes".equalsIgnoreCase(rangerHDFSPluginEnabledValue);
            java.util.Map<java.lang.String, java.lang.String> rangerHDFSSecurityConfig = clusterProps.get("ranger-hdfs-security");
            rangerHDFSPluginServiceName = rangerHDFSSecurityConfig.get("ranger.plugin.hdfs.service.name");
        }
        boolean isTagsyncPropertyConfigurationRequired = ((isRangerAdminToBeInstalled && isRangerTagsyncToBeInstalled) && isAtlasServerToBeInstalled) && isRangerHDFSPluginEnabled;
        java.util.Map<java.lang.String, java.lang.String> coreSiteConfig = clusterProps.get("core-site");
        java.lang.String fsDefaultFSValue = coreSiteConfig.get("fs.defaultFS");
        java.lang.String nameServiceInFsDefaultFSConfig = "";
        if (isTagsyncPropertyConfigurationRequired && "{{repo_name}}".equalsIgnoreCase(rangerHDFSPluginServiceName)) {
            rangerHDFSPluginServiceName = clusterName + "_hadoop";
        }
        if ((parsedNameServices.length > 1) && isTagsyncPropertyConfigurationRequired) {
            for (java.lang.String nameService : parsedNameServices) {
                java.lang.String tagsyncNameserviceMappingProperty = ((("ranger.tagsync.atlas.hdfs.instance." + clusterName) + ".nameservice.") + nameService) + ".ranger.service";
                java.lang.String updatedRangerHDFSPluginServiceName = (rangerHDFSPluginServiceName + "_") + nameService;
                clusterConfig.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.RANGER_TAGSYNC_SITE_CONFIG_TYPE_NAME, tagsyncNameserviceMappingProperty, updatedRangerHDFSPluginServiceName);
                try {
                    java.net.URI fsDefaultFSURI = new java.net.URI(fsDefaultFSValue);
                    java.lang.String fsDefaultFSNameService = fsDefaultFSURI.getHost();
                    if (fsDefaultFSNameService.contains(nameService)) {
                        nameServiceInFsDefaultFSConfig = nameService;
                    }
                } catch (java.net.URISyntaxException e) {
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.error("Error occurred while parsing the defaultFS URI.", e);
                }
            }
            java.lang.String rangerTagsyncAtlasNNServiceMappingProperty = ("ranger.tagsync.atlas.hdfs.instance." + clusterName) + ".ranger.service";
            java.lang.String rangerTagsyncAtlasNNServiceName = (rangerHDFSPluginServiceName + "_") + nameServiceInFsDefaultFSConfig;
            clusterConfig.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.RANGER_TAGSYNC_SITE_CONFIG_TYPE_NAME, rangerTagsyncAtlasNNServiceMappingProperty, rangerTagsyncAtlasNNServiceName);
            configTypesUpdated.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.RANGER_TAGSYNC_SITE_CONFIG_TYPE_NAME);
        }
    }

    private void doGeneralPropertyUpdatesForClusterCreate(org.apache.ambari.server.topology.Configuration clusterConfig, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterProps, java.util.Set<java.lang.String> configTypesUpdated) {
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters = createCollectionOfUpdaters();
        for (java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> updaterMap : updaters) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> entry : updaterMap.entrySet()) {
                final java.lang.String configType = entry.getKey();
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> updaterEntry : entry.getValue().entrySet()) {
                    java.lang.String propertyName = updaterEntry.getKey();
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater updater = updaterEntry.getValue();
                    java.util.Map<java.lang.String, java.lang.String> typeMap = clusterProps.get(configType);
                    if (((typeMap != null) && typeMap.containsKey(propertyName)) && (typeMap.get(propertyName) != null)) {
                        final java.lang.String originalValue = typeMap.get(propertyName);
                        final java.lang.String updatedValue = updateValue(configType, propertyName, originalValue, updater, clusterProps, clusterConfig, configTypesUpdated, true);
                        if (null == updatedValue) {
                            continue;
                        }
                    }
                    for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : clusterTopology.getHostGroupInfo().values()) {
                        org.apache.ambari.server.topology.Configuration hgConfig = groupInfo.getConfiguration();
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hgConfigProps = hgConfig.getFullProperties(1);
                        java.util.Map<java.lang.String, java.lang.String> hgTypeMap = hgConfigProps.get(configType);
                        if ((hgTypeMap != null) && hgTypeMap.containsKey(propertyName)) {
                            final java.lang.String originalValue = hgTypeMap.get(propertyName);
                            updateValue(configType, propertyName, originalValue, updater, hgConfigProps, hgConfig, configTypesUpdated, true);
                        }
                    }
                }
            }
        }
        java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> propertiesWithUpdaters = getAllPropertiesWithUpdaters(updaters);
        applyDefaultUpdater(clusterConfig, clusterConfig.getFullProperties(1), configTypesUpdated, propertiesWithUpdaters);
        clusterTopology.getHostGroupInfo().values().stream().forEach(hostGroup -> {
            org.apache.ambari.server.topology.Configuration hostGroupConfig = hostGroup.getConfiguration();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostGroupConfigProps = hostGroupConfig.getFullProperties(1);
            applyDefaultUpdater(hostGroupConfig, hostGroupConfigProps, configTypesUpdated, propertiesWithUpdaters);
        });
    }

    private java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> getAllPropertiesWithUpdaters(java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters) {
        return updaters.stream().flatMap(map -> map.entrySet().stream()).flatMap(entry -> {
            java.lang.String configType = entry.getKey();
            return entry.getValue().keySet().stream().map(propertyName -> org.apache.commons.lang3.tuple.Pair.of(configType, propertyName));
        }).collect(java.util.stream.Collectors.toSet());
    }

    private void applyDefaultUpdater(org.apache.ambari.server.topology.Configuration configuration, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, java.util.Set<java.lang.String> configTypesUpdated, java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> propertiesWithUpdaters) {
        properties.entrySet().forEach(configTypeEntry -> {
            java.lang.String configType = configTypeEntry.getKey();
            configTypeEntry.getValue().entrySet().forEach(propertyEntry -> {
                java.lang.String propertyName = propertyEntry.getKey();
                if (!propertiesWithUpdaters.contains(org.apache.commons.lang3.tuple.Pair.of(configType, propertyName))) {
                    java.lang.String oldValue = propertyEntry.getValue();
                    java.lang.String newValue = updateValue(configType, propertyName, oldValue, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater.defaultUpdater(), properties, configuration, configTypesUpdated, false);
                    if (!java.util.Objects.equals(oldValue, newValue)) {
                        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Property {}/{} was updated by the default updater from [{}] to [{}]", configType, propertyName, oldValue, newValue);
                    }
                }
            });
        });
    }

    private java.lang.String updateValue(java.lang.String configType, java.lang.String propertyName, java.lang.String oldValue, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater updater, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allProps, org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated, boolean alwaysUpdateConfig) {
        java.lang.String newValue = updater.updateForClusterCreate(propertyName, oldValue, allProps, clusterTopology);
        if (null != newValue) {
            if (!newValue.equals(oldValue)) {
                configTypesUpdated.add(configType);
            }
            if ((!newValue.equals(oldValue)) || alwaysUpdateConfig) {
                configuration.setProperty(configType, propertyName, newValue);
            }
        }
        return newValue;
    }

    private java.lang.String getClusterName() throws org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        java.lang.String clusterNameToReturn = null;
        try {
            clusterNameToReturn = clusterTopology.getAmbariContext().getClusterName(clusterTopology.getClusterId());
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException("Cluster name could not obtained, this may indicate a deployment or configuration error.", e);
        }
        if (clusterNameToReturn == null) {
            throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException("Cluster name could not obtained, this may indicate a deployment or configuration error.");
        }
        return clusterNameToReturn;
    }

    private void trimProperties(org.apache.ambari.server.topology.Configuration clusterConfig, org.apache.ambari.server.topology.ClusterTopology clusterTopology) {
        org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
        org.apache.ambari.server.controller.internal.Stack stack = blueprint.getStack();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTypes = clusterConfig.getFullProperties();
        for (java.lang.String configType : configTypes.keySet()) {
            java.util.Map<java.lang.String, java.lang.String> properties = configTypes.get(configType);
            for (java.lang.String propertyName : properties.keySet()) {
                trimPropertyValue(clusterConfig, stack, configType, properties, propertyName);
            }
        }
    }

    private void trimPropertyValue(org.apache.ambari.server.topology.Configuration clusterConfig, org.apache.ambari.server.controller.internal.Stack stack, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String propertyName) {
        if ((propertyName != null) && (properties.get(propertyName) != null)) {
            org.apache.ambari.server.controller.internal.TrimmingStrategy trimmingStrategy = org.apache.ambari.server.controller.internal.PropertyValueTrimmingStrategyDefiner.defineTrimmingStrategy(stack, propertyName, configType);
            java.lang.String oldValue = properties.get(propertyName);
            java.lang.String newValue = trimmingStrategy.trim(oldValue);
            if (!newValue.equals(oldValue)) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.debug("Changing value for config {} property {} from [{}] to [{}]", configType, propertyName, oldValue, newValue);
                clusterConfig.setProperty(configType, propertyName, newValue);
            }
        }
    }

    private static boolean shouldPropertyBeStoredWithDefault(java.lang.String propertyName) {
        if ((!org.apache.commons.lang.StringUtils.isBlank(propertyName)) && (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HBASE_SITE_HBASE_COPROCESSOR_MASTER_CLASSES.equals(propertyName) || org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HBASE_SITE_HBASE_COPROCESSOR_REGION_CLASSES.equals(propertyName))) {
            return true;
        }
        return false;
    }

    public void doUpdateForBlueprintExport(org.apache.ambari.server.controller.internal.BlueprintExportType exportType) {
        if (clusterTopology.isNameNodeHAEnabled()) {
            doNameNodeHAUpdate();
        }
        if (clusterTopology.isYarnResourceManagerHAEnabled()) {
            doYarnResourceManagerHAUpdate();
        }
        if (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isOozieServerHAEnabled(clusterTopology.getConfiguration().getFullProperties())) {
            doOozieServerHAUpdate();
        }
        java.util.Collection<org.apache.ambari.server.topology.Configuration> allConfigs = new java.util.ArrayList<>();
        org.apache.ambari.server.topology.Configuration clusterConfig = clusterTopology.getConfiguration();
        allConfigs.add(clusterConfig);
        for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : clusterTopology.getHostGroupInfo().values()) {
            org.apache.ambari.server.topology.Configuration hgConfiguration = groupInfo.getConfiguration();
            if (!hgConfiguration.getFullProperties(1).isEmpty()) {
                allConfigs.add(new org.apache.ambari.server.topology.Configuration(hgConfiguration.getProperties(), null, new org.apache.ambari.server.topology.Configuration(hgConfiguration.getParentConfiguration().getProperties(), null)));
            }
        }
        for (org.apache.ambari.server.topology.Configuration configuration : allConfigs) {
            doSingleHostExportUpdate(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.singleHostTopologyUpdaters, configuration);
            doSingleHostExportUpdate(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.dbHostTopologyUpdaters, configuration);
            doMultiHostExportUpdate(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.multiHostTopologyUpdaters, configuration);
            doNonTopologyUpdate(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.nonTopologyUpdaters, configuration);
            doRemovePropertyExport(removePropertyUpdaters, configuration);
            doFilterPriorToExport(configuration);
        }
        org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
        applyTypeSpecificFilter(exportType, clusterConfig, blueprint.getStack().getConfiguration(), blueprint.getServices());
    }

    @com.google.common.annotations.VisibleForTesting
    void applyTypeSpecificFilter(org.apache.ambari.server.controller.internal.BlueprintExportType exportType, org.apache.ambari.server.topology.Configuration clusterConfig, org.apache.ambari.server.topology.Configuration stackConfig, java.util.Collection<java.lang.String> services) {
        if (exportType == org.apache.ambari.server.controller.internal.BlueprintExportType.MINIMAL) {
            doNonTopologyUpdate(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.mPropertyUpdaters, clusterConfig);
        }
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.injectDefaults(stackConfig, new java.util.HashSet<>(), services);
        exportType.filter(clusterConfig, stackConfig);
    }

    private void doFilterPriorToExport(org.apache.ambari.server.topology.Configuration configuration) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getFullProperties();
        java.util.Map<java.lang.Long, java.util.Set<java.lang.String>> authToLocalPerClusterMap = null;
        try {
            java.lang.String clusterName = clusterTopology.getAmbariContext().getClusterName(clusterTopology.getClusterId());
            org.apache.ambari.server.state.Cluster cluster = clusterTopology.getAmbariContext().getController().getClusters().getCluster(clusterName);
            authToLocalPerClusterMap = new java.util.HashMap<>();
            authToLocalPerClusterMap.put(java.lang.Long.valueOf(clusterTopology.getClusterId()), clusterTopology.getAmbariContext().getController().getKerberosHelper().getKerberosDescriptor(cluster, false).getAllAuthToLocalProperties());
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.error("Error while getting authToLocal properties. ", e);
        }
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter[] exportPropertyFilters = getExportPropertyFilters(authToLocalPerClusterMap);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configEntry : properties.entrySet()) {
            java.lang.String type = configEntry.getKey();
            try {
                clusterTopology.getBlueprint().getStack().getServiceForConfigType(type);
            } catch (java.lang.IllegalArgumentException illegalArgumentException) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.error(new java.lang.StringBuilder(java.lang.String.format("Error encountered while trying to obtain the service name for config type [%s]. ", type)).append("Further processing on this config type will be skipped. ").append("This usually means that a service's definitions have been manually removed from the Ambari stack definitions. ").append("If the stack definitions have not been changed manually, this may indicate a stack definition error in Ambari. ").toString(), illegalArgumentException);
                continue;
            }
            java.util.Map<java.lang.String, java.lang.String> typeProperties = configEntry.getValue();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyEntry : typeProperties.entrySet()) {
                java.lang.String propertyName = propertyEntry.getKey();
                java.lang.String propertyValue = propertyEntry.getValue();
                if (shouldPropertyBeExcludedForBlueprintExport(propertyName, propertyValue, type, clusterTopology, exportPropertyFilters)) {
                    configuration.removeProperty(type, propertyName);
                }
            }
        }
    }

    private void doFilterPriorToClusterUpdate(org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getFullProperties();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configEntry : properties.entrySet()) {
            java.lang.String configType = configEntry.getKey();
            java.util.Map<java.lang.String, java.lang.String> configPropertiesPerType = configEntry.getValue();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyEntry : configPropertiesPerType.entrySet()) {
                java.lang.String propName = propertyEntry.getKey();
                if (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.shouldPropertyBeExcludedForClusterUpdate(propName, propertyEntry.getValue(), configType, clusterTopology)) {
                    configuration.removeProperty(configType, propName);
                    configTypesUpdated.add(configType);
                }
            }
        }
    }

    private void doRecommendConfigurations(org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated) {
        org.apache.ambari.server.topology.ConfigRecommendationStrategy configRecommendationStrategy = clusterTopology.getConfigRecommendationStrategy();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = clusterTopology.getAdvisedConfigurations();
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Config recommendation strategy being used is {})", configRecommendationStrategy);
        if (org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY.equals(configRecommendationStrategy)) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Filter out recommended configurations. Keep only the stack defaults.");
            doFilterStackDefaults(advisedConfigurations);
        }
        if (!org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY.equals(configRecommendationStrategy)) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advConfEntry : advisedConfigurations.entrySet()) {
                java.lang.String configType = advConfEntry.getKey();
                org.apache.ambari.server.topology.AdvisedConfiguration advisedConfig = advConfEntry.getValue();
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Update '{}' configurations with recommended configurations provided by the stack advisor.", configType);
                if (advisedConfig.getProperties() != null) {
                    doReplaceProperties(configuration, configType, advisedConfig, configTypesUpdated);
                }
                if (advisedConfig.getPropertyValueAttributes() != null) {
                    doRemovePropertiesIfNeeded(configuration, configType, advisedConfig, configTypesUpdated);
                }
            }
        } else {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("No recommended configurations are applied. (strategy: {})", org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY);
        }
    }

    private void doFilterStackDefaults(java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations) {
        org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
        org.apache.ambari.server.topology.Configuration stackDefaults = blueprint.getStack().getConfiguration(blueprint.getServices());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackDefaultProps = stackDefaults.getProperties();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> adConfEntry : advisedConfigurations.entrySet()) {
            org.apache.ambari.server.topology.AdvisedConfiguration advisedConfiguration = adConfEntry.getValue();
            if (stackDefaultProps.containsKey(adConfEntry.getKey())) {
                java.util.Map<java.lang.String, java.lang.String> defaultProps = stackDefaultProps.get(adConfEntry.getKey());
                if (advisedConfiguration.getProperties() != null) {
                    java.util.Map<java.lang.String, java.lang.String> outFilteredProps = com.google.common.collect.Maps.filterKeys(advisedConfiguration.getProperties(), com.google.common.base.Predicates.not(com.google.common.base.Predicates.in(defaultProps.keySet())));
                    advisedConfiguration.getProperties().keySet().removeAll(com.google.common.collect.Sets.newCopyOnWriteArraySet(outFilteredProps.keySet()));
                }
                if (advisedConfiguration.getPropertyValueAttributes() != null) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> outFilteredValueAttrs = com.google.common.collect.Maps.filterKeys(advisedConfiguration.getPropertyValueAttributes(), com.google.common.base.Predicates.not(com.google.common.base.Predicates.in(defaultProps.keySet())));
                    advisedConfiguration.getPropertyValueAttributes().keySet().removeAll(com.google.common.collect.Sets.newCopyOnWriteArraySet(outFilteredValueAttrs.keySet()));
                }
            } else {
                advisedConfiguration.getProperties().clear();
            }
        }
    }

    private void doReplaceProperties(org.apache.ambari.server.topology.Configuration configuration, java.lang.String configType, org.apache.ambari.server.topology.AdvisedConfiguration advisedConfig, java.util.Set<java.lang.String> configTypesUpdated) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> propEntry : advisedConfig.getProperties().entrySet()) {
            java.lang.String originalValue = configuration.getPropertyValue(configType, propEntry.getKey());
            configuration.setProperty(configType, propEntry.getKey(), propEntry.getValue());
            if (!propEntry.getValue().equals(originalValue)) {
                configTypesUpdated.add(configType);
            }
        }
    }

    private void doRemovePropertiesIfNeeded(org.apache.ambari.server.topology.Configuration configuration, java.lang.String configType, org.apache.ambari.server.topology.AdvisedConfiguration advisedConfig, java.util.Set<java.lang.String> configTypesUpdated) {
        if (advisedConfig.getPropertyValueAttributes() != null) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> valueAttrEntry : advisedConfig.getPropertyValueAttributes().entrySet()) {
                if ("true".equalsIgnoreCase(valueAttrEntry.getValue().getDelete())) {
                    if (null != configuration.removeProperty(configType, valueAttrEntry.getKey())) {
                        configTypesUpdated.add(configType);
                    }
                }
            }
        }
    }

    java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> createCollectionOfUpdaters() {
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.allUpdaters;
        if (clusterTopology.isNameNodeHAEnabled()) {
            updaters = addNameNodeHAUpdaters(updaters);
        }
        if (clusterTopology.isYarnResourceManagerHAEnabled()) {
            updaters = addYarnResourceManagerHAUpdaters(updaters);
        }
        if (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isOozieServerHAEnabled(clusterTopology.getConfiguration().getFullProperties())) {
            updaters = addOozieServerHAUpdaters(updaters);
        }
        return updaters;
    }

    private java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> addNameNodeHAUpdaters(java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters) {
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> highAvailabilityUpdaters = new java.util.LinkedList<>();
        highAvailabilityUpdaters.addAll(updaters);
        highAvailabilityUpdaters.add(createMapOfNameNodeHAUpdaters());
        return highAvailabilityUpdaters;
    }

    private java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> addYarnResourceManagerHAUpdaters(java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters) {
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> highAvailabilityUpdaters = new java.util.LinkedList<>();
        highAvailabilityUpdaters.addAll(updaters);
        highAvailabilityUpdaters.add(createMapOfYarnResourceManagerHAUpdaters());
        return highAvailabilityUpdaters;
    }

    private java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> addOozieServerHAUpdaters(java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> updaters) {
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>>> highAvailabilityUpdaters = new java.util.LinkedList<>();
        highAvailabilityUpdaters.addAll(updaters);
        highAvailabilityUpdaters.add(createMapOfOozieServerHAUpdaters());
        return highAvailabilityUpdaters;
    }

    private void doRemovePropertyExport(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> updaters, org.apache.ambari.server.topology.Configuration configuration) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getProperties();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> entry : updaters.entrySet()) {
            java.lang.String type = entry.getKey();
            for (java.lang.String propertyName : entry.getValue().keySet()) {
                java.util.Map<java.lang.String, java.lang.String> typeProperties = properties.get(type);
                if ((typeProperties != null) && typeProperties.containsKey(propertyName)) {
                    configuration.removeProperty(type, propertyName);
                }
            }
        }
    }

    public void doNameNodeHAUpdate() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> highAvailabilityUpdaters = createMapOfNameNodeHAUpdaters();
        if (highAvailabilityUpdaters.get("hdfs-site").size() > 0) {
            doSingleHostExportUpdate(highAvailabilityUpdaters, clusterTopology.getConfiguration());
        }
    }

    public void doYarnResourceManagerHAUpdate() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> highAvailabilityUpdaters = createMapOfYarnResourceManagerHAUpdaters();
        if (highAvailabilityUpdaters.get("yarn-site").size() > 0) {
            doSingleHostExportUpdate(highAvailabilityUpdaters, clusterTopology.getConfiguration());
        }
    }

    public void doOozieServerHAUpdate() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> highAvailabilityUpdaters = createMapOfOozieServerHAUpdaters();
        if (highAvailabilityUpdaters.get("oozie-site").size() > 0) {
            doMultiHostExportUpdate(highAvailabilityUpdaters, clusterTopology.getConfiguration());
        }
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> createMapOfNameNodeHAUpdaters() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> highAvailabilityUpdaters = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hdfsSiteUpdatersForAvailability = new java.util.HashMap<>();
        highAvailabilityUpdaters.put("hdfs-site", hdfsSiteUpdatersForAvailability);
        java.util.Map<java.lang.String, java.lang.String> hdfsSiteConfig = clusterTopology.getConfiguration().getFullProperties().get("hdfs-site");
        for (java.lang.String nameService : org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameServices(hdfsSiteConfig)) {
            final java.lang.String journalEditsDirPropertyName = "dfs.namenode.shared.edits.dir." + nameService;
            hdfsSiteUpdatersForAvailability.put(journalEditsDirPropertyName, new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("JOURNALNODE", ';', false, false, true));
            for (java.lang.String nameNode : org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseNameNodes(nameService, hdfsSiteConfig)) {
                final java.lang.String httpsPropertyName = (("dfs.namenode.https-address." + nameService) + ".") + nameNode;
                hdfsSiteUpdatersForAvailability.put(httpsPropertyName, new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
                final java.lang.String httpPropertyName = (("dfs.namenode.http-address." + nameService) + ".") + nameNode;
                hdfsSiteUpdatersForAvailability.put(httpPropertyName, new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
                final java.lang.String rpcPropertyName = (("dfs.namenode.rpc-address." + nameService) + ".") + nameNode;
                hdfsSiteUpdatersForAvailability.put(rpcPropertyName, new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
                final java.lang.String serviceRpcPropertyName = (("dfs.namenode.servicerpc-address." + nameService) + ".") + nameNode;
                hdfsSiteUpdatersForAvailability.put(serviceRpcPropertyName, new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
            }
        }
        return highAvailabilityUpdaters;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> createMapOfYarnResourceManagerHAUpdaters() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> highAvailabilityUpdaters = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> yarnSiteUpdatersForAvailability = new java.util.HashMap<>();
        highAvailabilityUpdaters.put("yarn-site", yarnSiteUpdatersForAvailability);
        java.util.Map<java.lang.String, java.lang.String> yarnSiteConfig = clusterTopology.getConfiguration().getFullProperties().get("yarn-site");
        for (java.lang.String resourceManager : org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.parseResourceManagers(yarnSiteConfig)) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater updater = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("RESOURCEMANAGER");
            yarnSiteUpdatersForAvailability.put("yarn.resourcemanager.hostname." + resourceManager, updater);
            yarnSiteUpdatersForAvailability.put("yarn.resourcemanager.address." + resourceManager, updater);
            yarnSiteUpdatersForAvailability.put("yarn.resourcemanager.admin.address." + resourceManager, updater);
            yarnSiteUpdatersForAvailability.put("yarn.resourcemanager.resource-tracker.address." + resourceManager, updater);
            yarnSiteUpdatersForAvailability.put("yarn.resourcemanager.scheduler.address." + resourceManager, updater);
            yarnSiteUpdatersForAvailability.put("yarn.resourcemanager.webapp.address." + resourceManager, updater);
            yarnSiteUpdatersForAvailability.put("yarn.resourcemanager.webapp.https.address." + resourceManager, updater);
        }
        return highAvailabilityUpdaters;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> createMapOfOozieServerHAUpdaters() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> highAvailabilityUpdaters = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieSiteUpdatersForAvailability = new java.util.HashMap<>();
        highAvailabilityUpdaters.put("oozie-site", oozieSiteUpdatersForAvailability);
        oozieSiteUpdatersForAvailability.put("oozie.zookeeper.connection.string", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        return highAvailabilityUpdaters;
    }

    static boolean isOozieServerHAEnabled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties) {
        return (configProperties.containsKey("oozie-site") && configProperties.get("oozie-site").containsKey("oozie.services.ext")) && configProperties.get("oozie-site").get("oozie.services.ext").contains("org.apache.oozie.service.ZKLocksService");
    }

    static boolean isHiveServerHAEnabled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties) {
        return (configProperties.containsKey("hive-site") && configProperties.get("hive-site").containsKey("hive.server2.support.dynamic.service.discovery")) && configProperties.get("hive-site").get("hive.server2.support.dynamic.service.discovery").equals("true");
    }

    static boolean isNameNodeHAInitialActiveNodeSet(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties) {
        return configProperties.containsKey(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE) && configProperties.get(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE).containsKey(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_ACTIVE_NAMENODE_PROPERTY_NAME);
    }

    static boolean isNameNodeHAInitialStandbyNodeSet(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties) {
        return configProperties.containsKey(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE) && configProperties.get(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_HA_INITIAL_CONFIG_TYPE).containsKey(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HDFS_STANDBY_NAMENODE_PROPERTY_NAME);
    }

    static boolean isPropertySet(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties, java.lang.String configType, java.lang.String propertyName) {
        return configProperties.containsKey(configType) && configProperties.get(configType).containsKey(propertyName);
    }

    static java.lang.String[] parseNameServices(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.lang.String nameServices = properties.get("dfs.internal.nameservices");
        if (nameServices == null) {
            nameServices = properties.get("dfs.nameservices");
        }
        return org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.splitAndTrimStrings(nameServices);
    }

    static java.lang.String[] parseResourceManagers(java.util.Map<java.lang.String, java.lang.String> properties) {
        final java.lang.String resourceManagerNames = properties.get("yarn.resourcemanager.ha.rm-ids");
        return org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.splitAndTrimStrings(resourceManagerNames);
    }

    static java.lang.String[] parseNameNodes(java.lang.String nameService, java.util.Map<java.lang.String, java.lang.String> properties) {
        final java.lang.String nameNodes = properties.get("dfs.ha.namenodes." + nameService);
        return org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.splitAndTrimStrings(nameNodes);
    }

    private boolean shouldPropertyBeExcludedForBlueprintExport(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyType, org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter[] exportPropertyFilters) {
        for (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter filter : exportPropertyFilters) {
            if (!filter.isPropertyIncluded(propertyName, propertyValue, propertyType, topology)) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldPropertyBeExcludedForClusterUpdate(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyType, org.apache.ambari.server.topology.ClusterTopology topology) {
        for (org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter filter : org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.clusterUpdatePropertyFilters) {
            try {
                if (!filter.isPropertyIncluded(propertyName, propertyValue, propertyType, topology)) {
                    if (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.shouldPropertyBeStoredWithDefault(propertyName)) {
                        return true;
                    }
                }
            } catch (java.lang.Throwable throwable) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.warn(("Error occurred while attempting to process the property '" + propertyName) + "' with a filter.  This may indicate a config error.", throwable);
            }
        }
        return false;
    }

    private void doSingleHostExportUpdate(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> updaters, org.apache.ambari.server.topology.Configuration configuration) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getFullProperties();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> entry : updaters.entrySet()) {
            java.lang.String type = entry.getKey();
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> propertyNameUpdaters = entry.getValue();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> propertyNameUpdater : propertyNameUpdaters.entrySet()) {
                java.lang.String propertyName = propertyNameUpdater.getKey();
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater propertyUpdater = propertyNameUpdater.getValue();
                boolean matchedHost = false;
                java.util.Map<java.lang.String, java.lang.String> typeProperties = properties.get(type);
                if ((typeProperties != null) && typeProperties.containsKey(propertyName)) {
                    java.lang.String propValue = typeProperties.get(propertyName);
                    java.lang.String replacedValue = propertyUpdater.updateForBlueprintExport(propertyName, propValue, properties, clusterTopology);
                    if (!replacedValue.equals(propValue)) {
                        matchedHost = true;
                        configuration.setProperty(type, propertyName, replacedValue);
                    }
                    if ((((((!matchedHost) && (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isNameServiceProperty(propertyName))) && (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isDatabaseConnectionURL(propertyName))) && (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isSpecialNetworkAddress(propValue))) && (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isUndefinedAddress(propValue))) && (!org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.isPlaceholder(propValue))) {
                        configuration.removeProperty(type, propertyName);
                    }
                }
            }
        }
    }

    private static boolean isPlaceholder(java.lang.String propertyValue) {
        return org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PLACEHOLDER.matcher(propertyValue).find();
    }

    private static boolean isNameServiceProperty(java.lang.String propertyName) {
        return org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.configPropertiesWithHASupport.contains(propertyName);
    }

    private static boolean isDatabaseConnectionURL(java.lang.String propertyName) {
        return propertyName.equals("javax.jdo.option.ConnectionURL");
    }

    private static boolean isSpecialNetworkAddress(java.lang.String propertyValue) {
        return propertyValue.contains(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.BIND_ALL_IP_ADDRESS);
    }

    private static boolean isUndefinedAddress(java.lang.String propertyValue) {
        return propertyValue.contains("undefined");
    }

    private void doMultiHostExportUpdate(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> updaters, org.apache.ambari.server.topology.Configuration configuration) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getFullProperties();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> entry : updaters.entrySet()) {
            java.lang.String type = entry.getKey();
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> propertyNameUpdaters = entry.getValue();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> propertyNameUpdater : propertyNameUpdaters.entrySet()) {
                java.lang.String propertyName = propertyNameUpdater.getKey();
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater propertyUpdater = propertyNameUpdater.getValue();
                java.util.Map<java.lang.String, java.lang.String> typeProperties = properties.get(type);
                if ((typeProperties != null) && typeProperties.containsKey(propertyName)) {
                    java.lang.String propValue = typeProperties.get(propertyName);
                    propValue = propertyUpdater.updateForBlueprintExport(propertyName, propValue, properties, clusterTopology);
                    java.util.Collection<java.lang.String> addedGroups = new java.util.HashSet<>();
                    java.lang.String[] toks = propValue.split(",");
                    boolean inBrackets = propValue.startsWith("[");
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    if (inBrackets) {
                        sb.append('[');
                    }
                    boolean firstTok = true;
                    for (java.lang.String tok : toks) {
                        tok = tok.replaceAll("[\\[\\]]", "");
                        if (addedGroups.add(tok)) {
                            if (!firstTok) {
                                sb.append(',');
                            }
                            sb.append(tok);
                        }
                        firstTok = false;
                    }
                    if (inBrackets) {
                        sb.append(']');
                    }
                    configuration.setProperty(type, propertyName, sb.toString());
                }
            }
        }
    }

    private static java.util.Collection<java.lang.String> getHostStrings(java.lang.String val, org.apache.ambari.server.topology.ClusterTopology topology) {
        java.util.Collection<java.lang.String> hosts = new java.util.LinkedHashSet<>();
        java.util.regex.Matcher m = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HOSTGROUP_PORT_REGEX.matcher(val);
        while (m.find()) {
            java.lang.String groupName = m.group(1);
            java.lang.String port = m.group(2);
            org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo = topology.getHostGroupInfo().get(groupName);
            if (hostGroupInfo == null) {
                throw new java.lang.IllegalArgumentException("Unable to match blueprint host group token to a host group: " + groupName);
            }
            for (java.lang.String host : hostGroupInfo.getHostNames()) {
                if (port != null) {
                    host += ":" + port;
                }
                hosts.add(host);
            }
        } 
        return hosts;
    }

    private static java.lang.String[] splitAndTrimStrings(java.lang.String propertyName) {
        if (propertyName != null) {
            java.util.List<java.lang.String> namesWithoutWhitespace = new java.util.LinkedList<>();
            for (java.lang.String service : propertyName.split(",")) {
                namesWithoutWhitespace.add(service.trim());
            }
            return namesWithoutWhitespace.toArray(new java.lang.String[namesWithoutWhitespace.size()]);
        } else {
            return new java.lang.String[0];
        }
    }

    private void doNonTopologyUpdate(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> updaters, org.apache.ambari.server.topology.Configuration configuration) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getFullProperties();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> entry : updaters.entrySet()) {
            java.lang.String type = entry.getKey();
            for (java.lang.String propertyName : entry.getValue().keySet()) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater pu = entry.getValue().get(propertyName);
                java.util.Map<java.lang.String, java.lang.String> typeProperties = properties.get(type);
                if ((typeProperties != null) && typeProperties.containsKey(propertyName)) {
                    java.lang.String newValue = pu.updateForBlueprintExport(propertyName, typeProperties.get(propertyName), properties, clusterTopology);
                    configuration.setProperty(type, propertyName, newValue);
                }
            }
        }
    }

    public interface PropertyUpdater {
        java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology);

        default java.lang.String updateForBlueprintExport(java.lang.String propertyName, java.lang.String value, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            return value;
        }

        java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology);

        static org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater defaultUpdater() {
            return org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.INSTANCE;
        }
    }

    static class HostGroupUpdater implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater {
        static final org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater INSTANCE = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater();

        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if (origValue == null) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Property {} is null, skipping search for host group placeholder", propertyName);
                return null;
            }
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.HostGroups hostGroups = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.HostGroups(topology, propertyName);
            java.util.LinkedList<org.apache.commons.lang3.tuple.Pair<org.apache.commons.lang3.tuple.Pair<java.lang.Integer, java.lang.Integer>, java.lang.String>> replacements = new java.util.LinkedList<>();
            for (java.util.regex.Matcher m = org.apache.ambari.server.topology.HostGroup.HOSTGROUP_REGEX.matcher(origValue); m.find();) {
                java.lang.String replacement = hostGroups.getHost(m.group(1));
                int from = m.start();
                int to = m.end();
                replacements.add(org.apache.commons.lang3.tuple.Pair.of(org.apache.commons.lang3.tuple.Pair.of(from, to), replacement));
            }
            java.lang.StringBuilder newValue = new java.lang.StringBuilder(origValue);
            for (java.util.Iterator<org.apache.commons.lang3.tuple.Pair<org.apache.commons.lang3.tuple.Pair<java.lang.Integer, java.lang.Integer>, java.lang.String>> it = replacements.descendingIterator(); it.hasNext();) {
                org.apache.commons.lang3.tuple.Pair<org.apache.commons.lang3.tuple.Pair<java.lang.Integer, java.lang.Integer>, java.lang.String> replacement = it.next();
                int from = replacement.getLeft().getLeft();
                int to = replacement.getLeft().getRight();
                java.lang.String replacementValue = replacement.getRight();
                newValue.replace(from, to, replacementValue);
            }
            return newValue.toString();
        }

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if (origValue == null) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.info("Property {} is null, skipping search for host group placeholder", propertyName);
                return java.util.Collections.emptyList();
            }
            java.util.regex.Matcher m = org.apache.ambari.server.topology.HostGroup.HOSTGROUP_REGEX.matcher(origValue);
            java.util.Set<java.lang.String> hostGroups = new java.util.HashSet<>();
            while (m.find()) {
                hostGroups.add(m.group(1));
            } 
            return hostGroups;
        }

        static class HostGroups {
            private org.apache.ambari.server.topology.ClusterTopology topology;

            private java.lang.String propertyName;

            private java.util.Set<java.lang.String> hostGroupsUsed = new java.util.HashSet<>();

            HostGroups(org.apache.ambari.server.topology.ClusterTopology topology, java.lang.String propertyName) {
                this.topology = topology;
                this.propertyName = propertyName;
            }

            java.lang.String getHost(java.lang.String hostGroup) {
                com.google.common.base.Preconditions.checkState(!hostGroupsUsed.contains(hostGroup), "Multiple occurrence of host group [%s] in property value of: [%s].", hostGroup, propertyName);
                org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo = topology.getHostGroupInfo().get(hostGroup);
                com.google.common.base.Preconditions.checkArgument(null != hostGroupInfo, "Encountered a host group token in configuration which couldn't be matched to a host group: %s", hostGroup);
                if (hostGroupInfo.getHostNames().size() > 1) {
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.warn("Host group {} contains multiple hosts. Using {} with such host groups may result in unintended configuration.", hostGroup, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.class.getSimpleName());
                }
                hostGroupsUsed.add(hostGroup);
                return hostGroupInfo.getHostNames().iterator().next();
            }
        }
    }

    private static class SingleHostTopologyUpdater extends org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater {
        private final java.lang.String component;

        public SingleHostTopologyUpdater(java.lang.String component) {
            this.component = component;
        }

        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            java.lang.String replacedValue = super.updateForClusterCreate(propertyName, origValue, properties, topology);
            if (!java.util.Objects.equals(origValue, replacedValue)) {
                return replacedValue;
            } else if ((null != origValue) && (!origValue.contains(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST))) {
                return origValue;
            } else {
                int matchingGroupCount = topology.getHostGroupsForComponent(component).size();
                if (matchingGroupCount == 1) {
                    return origValue.replace(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST, topology.getHostAssignmentsForComponent(component).iterator().next());
                } else {
                    org.apache.ambari.server.topology.Cardinality cardinality = topology.getBlueprint().getStack().getCardinality(component);
                    if ((matchingGroupCount == 0) && cardinality.isValidCount(0)) {
                        return origValue;
                    } else {
                        if ((topology.isNameNodeHAEnabled() && isComponentNameNode()) && (matchingGroupCount >= 2)) {
                            if (properties.containsKey("core-site") && properties.get("core-site").get("fs.defaultFS").equals(origValue)) {
                                return origValue;
                            }
                            if (properties.containsKey("hbase-site") && properties.get("hbase-site").get("hbase.rootdir").equals(origValue)) {
                                return origValue;
                            }
                            if (properties.containsKey("accumulo-site") && properties.get("accumulo-site").get("instance.volumes").equals(origValue)) {
                                return origValue;
                            }
                        }
                        if ((topology.isNameNodeHAEnabled() && isComponentSecondaryNameNode()) && (matchingGroupCount == 0)) {
                            return origValue;
                        }
                        if (isComponentNameNode() && topology.hasHadoopCompatibleService()) {
                            return origValue;
                        }
                        throw new java.lang.IllegalArgumentException(java.lang.String.format("Unable to update configuration property '%s' with topology information. " + "Component '%s' is mapped to an invalid number of host groups '%s'.", propertyName, component, matchingGroupCount));
                    }
                }
            }
        }

        @java.lang.Override
        public java.lang.String updateForBlueprintExport(java.lang.String propertyName, java.lang.String value, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : topology.getHostGroupInfo().values()) {
                java.util.Collection<java.lang.String> hosts = groupInfo.getHostNames();
                for (java.lang.String host : hosts) {
                    if (value.contains(host)) {
                        return value.replace(host, ("%HOSTGROUP::" + groupInfo.getHostGroupName()) + "%");
                    }
                }
            }
            return value;
        }

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            java.util.Collection<java.lang.String> result = super.getRequiredHostGroups(propertyName, origValue, properties, topology);
            if (!result.isEmpty()) {
                return result;
            } else {
                java.util.Collection<java.lang.String> matchingGroups = topology.getHostGroupsForComponent(component);
                int matchingGroupCount = matchingGroups.size();
                if (matchingGroupCount != 0) {
                    return new java.util.HashSet<>(matchingGroups);
                } else {
                    org.apache.ambari.server.topology.Cardinality cardinality = topology.getBlueprint().getStack().getCardinality(component);
                    if (!cardinality.isValidCount(0)) {
                        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.warn("The property '{}' is associated with the component '{}' which isn't mapped to any host group. " + "This may affect configuration topology resolution.", propertyName, component);
                    }
                    return java.util.Collections.emptySet();
                }
            }
        }

        private boolean isComponentNameNode() {
            return component.equals("NAMENODE");
        }

        private boolean isComponentSecondaryNameNode() {
            return component.equals("SECONDARY_NAMENODE");
        }

        private boolean isComponentResourceManager() {
            return component.equals("RESOURCEMANAGER");
        }

        private boolean isComponentOozieServer() {
            return component.equals("OOZIE_SERVER");
        }

        private boolean isComponentHiveServer() {
            return component.equals("HIVE_SERVER");
        }

        private boolean isComponentHiveMetaStoreServer() {
            return component.equals("HIVE_METASTORE");
        }

        private boolean isRangerAdmin() {
            return component.equals("RANGER_ADMIN");
        }

        private boolean isComponentHistoryServer() {
            return component.equals("HISTORYSERVER");
        }

        private boolean isComponentAppTimelineServer() {
            return component.equals("APP_TIMELINE_SERVER");
        }

        public java.lang.String getComponentName() {
            return component;
        }
    }

    @java.lang.Deprecated
    private static class OptionalSingleHostTopologyUpdater extends org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater {
        public OptionalSingleHostTopologyUpdater(java.lang.String component) {
            super(component);
        }

        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            try {
                return super.updateForClusterCreate(propertyName, origValue, properties, topology);
            } catch (java.lang.IllegalArgumentException illegalArgumentException) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.warn("Error while updating property [{}] with original value [{}]. Exception message: {}", propertyName, origValue, illegalArgumentException.getMessage());
                return origValue;
            }
        }

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            try {
                return super.getRequiredHostGroups(propertyName, origValue, properties, topology);
            } catch (java.lang.IllegalArgumentException e) {
                return java.util.Collections.emptySet();
            }
        }
    }

    private static class DBTopologyUpdater extends org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater {
        private final java.lang.String configPropertyType;

        private final java.lang.String conditionalPropertyName;

        private DBTopologyUpdater(java.lang.String component, java.lang.String conditionalPropertyType, java.lang.String conditionalPropertyName) {
            super(component);
            configPropertyType = conditionalPropertyType;
            this.conditionalPropertyName = conditionalPropertyName;
        }

        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if (isDatabaseManaged(properties)) {
                return super.updateForClusterCreate(propertyName, origValue, properties, topology);
            } else {
                return origValue;
            }
        }

        @java.lang.Override
        public java.lang.String updateForBlueprintExport(java.lang.String propertyName, java.lang.String value, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if (isDatabaseManaged(properties)) {
                return super.updateForBlueprintExport(propertyName, value, properties, topology);
            } else {
                return value;
            }
        }

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if (isDatabaseManaged(properties)) {
                return super.getRequiredHostGroups(propertyName, origValue, properties, topology);
            } else {
                return java.util.Collections.emptySet();
            }
        }

        private boolean isDatabaseManaged(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties) {
            return properties.get(configPropertyType).get(conditionalPropertyName).startsWith("New");
        }
    }

    protected static class MultipleHostTopologyUpdater implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater {
        private static final java.lang.Character DEFAULT_SEPARATOR = ',';

        private final java.lang.String component;

        private final java.lang.Character separator;

        private final boolean usePrefixForEachHost;

        private final boolean useSuffixForEachHost;

        private final boolean usePortForEachHost;

        public MultipleHostTopologyUpdater(java.lang.String component) {
            this(component, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater.DEFAULT_SEPARATOR, false, false, true);
        }

        public MultipleHostTopologyUpdater(java.lang.String component, java.lang.Character separator, boolean usePrefixForEachHost, boolean useSuffixForEachHost, boolean usePortForEachHost) {
            this.component = component;
            this.separator = separator;
            this.usePrefixForEachHost = usePrefixForEachHost;
            this.useSuffixForEachHost = useSuffixForEachHost;
            this.usePortForEachHost = usePortForEachHost;
        }

        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if ((!origValue.contains("%HOSTGROUP")) && (!origValue.contains(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST))) {
                return origValue;
            }
            java.util.Collection<java.lang.String> hostStrings = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.getHostStrings(origValue, topology);
            hostStrings.addAll(getHostStringsFromLocalhost(origValue, topology));
            return resolveHostGroupPlaceholder(origValue, hostStrings);
        }

        @java.lang.Override
        public java.lang.String updateForBlueprintExport(java.lang.String propertyName, java.lang.String value, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : topology.getHostGroupInfo().values()) {
                java.util.Collection<java.lang.String> hosts = groupInfo.getHostNames();
                for (java.lang.String host : hosts) {
                    value = value.replaceAll(host + "\\b", ("%HOSTGROUP::" + groupInfo.getHostGroupName()) + "%");
                }
            }
            return value;
        }

        private java.lang.String getPrefix(java.lang.String value) {
            java.util.regex.Matcher localhostMatcher = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST_PORT_REGEX.matcher(value);
            java.util.regex.Matcher hostGroupMatcher = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HOSTGROUP_PORT_REGEX.matcher(value);
            java.lang.String prefixCandidate = null;
            if (localhostMatcher.find()) {
                prefixCandidate = value.substring(0, localhostMatcher.start());
            } else if (hostGroupMatcher.find()) {
                prefixCandidate = value.substring(0, hostGroupMatcher.start());
            } else {
                return prefixCandidate;
            }
            if (prefixCandidate.startsWith("[")) {
                prefixCandidate = prefixCandidate.substring(1);
            }
            if (prefixCandidate.startsWith("'")) {
                prefixCandidate = prefixCandidate.substring(1);
            }
            return prefixCandidate;
        }

        private java.lang.String getSuffix(java.lang.String value) {
            java.util.regex.Matcher localhostMatcher = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST_PORT_REGEX.matcher(value);
            java.util.regex.Matcher hostGroupMatcher = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HOSTGROUP_PORT_REGEX.matcher(value);
            java.util.regex.Matcher activeMatcher = null;
            if (localhostMatcher.find()) {
                activeMatcher = localhostMatcher;
            } else if (hostGroupMatcher.find()) {
                activeMatcher = hostGroupMatcher;
            } else {
                return null;
            }
            java.lang.String suffixCandidate = null;
            int indexOfEnd;
            do {
                indexOfEnd = activeMatcher.end();
            } while (activeMatcher.find() );
            suffixCandidate = value.substring(indexOfEnd);
            if (suffixCandidate.endsWith("]")) {
                suffixCandidate = suffixCandidate.substring(0, suffixCandidate.length() - 1);
            }
            if (suffixCandidate.endsWith("'")) {
                suffixCandidate = suffixCandidate.substring(0, suffixCandidate.length() - 1);
            }
            return suffixCandidate;
        }

        private java.util.Collection<java.lang.String> getHostStringsFromLocalhost(java.lang.String origValue, org.apache.ambari.server.topology.ClusterTopology topology) {
            java.util.Set<java.lang.String> hostStrings = new java.util.HashSet<>();
            if (origValue.contains(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST)) {
                java.util.regex.Matcher localhostMatcher = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST_PORT_REGEX.matcher(origValue);
                java.lang.String port = null;
                if (localhostMatcher.find()) {
                    port = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater.calculatePort(localhostMatcher.group());
                }
                for (java.lang.String host : topology.getHostAssignmentsForComponent(component)) {
                    if (port != null) {
                        host += ":" + port;
                    }
                    hostStrings.add(host);
                }
            }
            return hostStrings;
        }

        protected java.lang.String resolveHostGroupPlaceholder(java.lang.String originalValue, java.util.Collection<java.lang.String> hostStrings) {
            java.lang.String prefix = getPrefix(originalValue);
            java.lang.String suffix = getSuffix(originalValue);
            java.lang.String port = removePorts(hostStrings);
            java.lang.String sep = ((useSuffixForEachHost ? suffix : "") + separator) + (usePrefixForEachHost ? prefix : "");
            java.lang.String combinedHosts = (usePrefixForEachHost ? prefix : "") + org.apache.commons.lang.StringUtils.join(hostStrings, sep);
            return (((usePrefixForEachHost ? "" : prefix) + combinedHosts) + (usePortForEachHost || (port == null) ? "" : ":" + port)) + suffix;
        }

        private java.lang.String removePorts(java.util.Collection<java.lang.String> hostStrings) {
            java.lang.String port = null;
            if ((!usePortForEachHost) && (!hostStrings.isEmpty())) {
                java.util.Set<java.lang.String> temp = new java.util.HashSet<>();
                java.util.Iterator<java.lang.String> i = hostStrings.iterator();
                do {
                    port = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater.calculatePort(i.next());
                } while (i.hasNext() && (port == null) );
                if (port != null) {
                    for (java.lang.String host : hostStrings) {
                        temp.add(host.replace(":" + port, ""));
                    }
                }
                hostStrings.clear();
                hostStrings.addAll(temp);
            }
            return port;
        }

        private static java.lang.String calculatePort(java.lang.String origValue) {
            if (origValue.contains(":")) {
                return origValue.substring(origValue.indexOf(":") + 1);
            }
            return null;
        }

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            java.util.Collection<java.lang.String> requiredHostGroups = new java.util.HashSet<>();
            java.util.regex.Matcher m = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HOSTGROUP_PORT_REGEX.matcher(origValue);
            while (m.find()) {
                java.lang.String groupName = m.group(1);
                if (!topology.getBlueprint().getHostGroups().containsKey(groupName)) {
                    throw new java.lang.IllegalArgumentException("Unable to match blueprint host group token to a host group: " + groupName);
                }
                requiredHostGroups.add(groupName);
            } 
            if (requiredHostGroups.isEmpty()) {
                requiredHostGroups.addAll(topology.getHostGroupsForComponent(component));
            }
            return requiredHostGroups;
        }
    }

    private static abstract class AbstractPropertyValueDecorator implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater propertyUpdater;

        public AbstractPropertyValueDecorator(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater propertyUpdater) {
            this.propertyUpdater = propertyUpdater;
        }

        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if (isFQDNValue(origValue)) {
                return origValue;
            }
            return doFormat(propertyUpdater.updateForClusterCreate(propertyName, origValue, properties, topology));
        }

        public abstract java.lang.String doFormat(java.lang.String originalValue);

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            return propertyUpdater.getRequiredHostGroups(propertyName, origValue, properties, topology);
        }

        public boolean isFQDNValue(java.lang.String value) {
            return (!value.contains("%HOSTGROUP")) && (!value.contains(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST));
        }
    }

    static class YamlMultiValuePropertyDecorator extends org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.AbstractPropertyValueDecorator {
        enum FlowStyle {

            SINGLE_QUOTED,
            PLAIN;}

        private static java.util.regex.Pattern REGEX_IN_BRACKETS = java.util.regex.Pattern.compile("\\s*\\[(?<INNER>.*)\\]\\s*");

        private static java.util.regex.Pattern REGEX_IN_QUOTES = java.util.regex.Pattern.compile("\\s*\'(?<INNER>.*)\'\\s*");

        private final org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle flowStyle;

        public YamlMultiValuePropertyDecorator(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater propertyUpdater) {
            this(propertyUpdater, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.SINGLE_QUOTED);
        }

        protected YamlMultiValuePropertyDecorator(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater propertyUpdater, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle flowStyle) {
            super(propertyUpdater);
            this.flowStyle = flowStyle;
        }

        @java.lang.Override
        public java.lang.String doFormat(java.lang.String origValue) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.util.regex.Matcher m = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.REGEX_IN_BRACKETS.matcher(origValue);
            if (m.matches()) {
                origValue = m.group("INNER");
            }
            if (origValue != null) {
                sb.append("[");
                boolean isFirst = true;
                for (java.lang.String value : origValue.split(",")) {
                    m = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.REGEX_IN_QUOTES.matcher(value);
                    if (m.matches()) {
                        value = m.group("INNER");
                    }
                    if (!isFirst) {
                        sb.append(",");
                    } else {
                        isFirst = false;
                    }
                    if (flowStyle == org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.SINGLE_QUOTED) {
                        sb.append("'");
                    }
                    sb.append(value);
                    if (flowStyle == org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.SINGLE_QUOTED) {
                        sb.append("'");
                    }
                }
                sb.append("]");
            }
            return sb.toString();
        }
    }

    private static class OriginalValuePropertyUpdater implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater {
        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            return origValue;
        }

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            return java.util.Collections.emptySet();
        }
    }

    private static class TempletonHivePropertyUpdater implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater {
        private java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> mapOfKeysToUpdaters = new java.util.HashMap<>();

        TempletonHivePropertyUpdater() {
            mapOfKeysToUpdaters.put("hive.metastore.uris", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("HIVE_METASTORE", ',', true, false, true));
        }

        @java.lang.Override
        public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if ((!origValue.contains("%HOSTGROUP")) && (!origValue.contains(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST))) {
                return origValue;
            }
            java.lang.StringBuilder updatedResult = new java.lang.StringBuilder();
            java.lang.String[] keyValuePairs = origValue.split(",");
            boolean firstValue = true;
            for (java.lang.String keyValuePair : keyValuePairs) {
                keyValuePair = keyValuePair.trim();
                if (!firstValue) {
                    updatedResult.append(",");
                } else {
                    firstValue = false;
                }
                java.lang.String key = keyValuePair.split("=")[0].trim();
                if (mapOfKeysToUpdaters.containsKey(key)) {
                    java.lang.String result = mapOfKeysToUpdaters.get(key).updateForClusterCreate(key, keyValuePair.split("=")[1].trim(), properties, topology);
                    updatedResult.append(key);
                    updatedResult.append("=");
                    updatedResult.append(result.replaceAll(",", java.util.regex.Matcher.quoteReplacement("\\,")));
                } else {
                    updatedResult.append(keyValuePair);
                }
            }
            return updatedResult.toString();
        }

        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            if ((!origValue.contains("%HOSTGROUP")) && (!origValue.contains(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOCALHOST))) {
                return java.util.Collections.emptySet();
            }
            java.util.Collection<java.lang.String> requiredGroups = new java.util.HashSet<>();
            java.lang.String[] keyValuePairs = origValue.split(",");
            for (java.lang.String keyValuePair : keyValuePairs) {
                java.lang.String key = keyValuePair.split("=")[0];
                if (mapOfKeysToUpdaters.containsKey(key)) {
                    requiredGroups.addAll(mapOfKeysToUpdaters.get(key).getRequiredHostGroups(propertyName, keyValuePair.split("=")[1], properties, topology));
                }
            }
            return requiredGroups;
        }
    }

    private static abstract class NonTopologyUpdater implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater {
        @java.lang.Override
        public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
            return java.util.Collections.emptyList();
        }
    }

    static {
        allUpdaters.add(singleHostTopologyUpdaters);
        allUpdaters.add(multiHostTopologyUpdaters);
        allUpdaters.add(dbHostTopologyUpdaters);
        allUpdaters.add(mPropertyUpdaters);
        allUpdaters.add(nonTopologyUpdaters);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> amsSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> druidCommon = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hdfsSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> mapredSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> coreSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hbaseSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> yarnSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hiveSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hiveSiteNonTopologyMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hiveEnvOriginalValueMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieSiteOriginalValueMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> stormSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> stormSiteNonTopologyMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> accumuloSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> falconStartupPropertiesMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> kafkaBrokerMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> kafkaBrokerNonTopologyMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> atlasPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> mapredEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> mHadoopEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> shHadoopEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> clusterEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hbaseEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hiveEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hiveInteractiveEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hiveInteractiveSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieEnvMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieEnvHeapSizeMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiWebhcatSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiHbaseSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> livy2Conf = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiStormSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiCoreSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiHdfsSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiHiveSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiKafkaBrokerMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiYarnSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiOozieSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiAccumuloSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> multiRangerKmsSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> dbHiveSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerAdminPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerEnvPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerYarnAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerHdfsAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerHbaseAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerHiveAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerKnoxAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerKafkaAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerStormAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerAtlasAuditPropsMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> hawqSiteMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> zookeeperEnvMap = new java.util.HashMap<>();
        singleHostTopologyUpdaters.put("ams-site", amsSiteMap);
        singleHostTopologyUpdaters.put("druid-common", druidCommon);
        singleHostTopologyUpdaters.put("hdfs-site", hdfsSiteMap);
        singleHostTopologyUpdaters.put("mapred-site", mapredSiteMap);
        singleHostTopologyUpdaters.put("core-site", coreSiteMap);
        singleHostTopologyUpdaters.put("hbase-site", hbaseSiteMap);
        singleHostTopologyUpdaters.put("yarn-site", yarnSiteMap);
        singleHostTopologyUpdaters.put("hive-site", hiveSiteMap);
        singleHostTopologyUpdaters.put("hive-interactive-env", hiveInteractiveEnvMap);
        singleHostTopologyUpdaters.put("storm-site", stormSiteMap);
        singleHostTopologyUpdaters.put("accumulo-site", accumuloSiteMap);
        singleHostTopologyUpdaters.put("falcon-startup.properties", falconStartupPropertiesMap);
        singleHostTopologyUpdaters.put("hive-env", hiveEnvMap);
        singleHostTopologyUpdaters.put("oozie-env", oozieEnvMap);
        singleHostTopologyUpdaters.put("kafka-broker", kafkaBrokerMap);
        singleHostTopologyUpdaters.put("admin-properties", rangerAdminPropsMap);
        singleHostTopologyUpdaters.put("ranger-env", rangerEnvPropsMap);
        singleHostTopologyUpdaters.put("ranger-yarn-audit", rangerYarnAuditPropsMap);
        singleHostTopologyUpdaters.put("ranger-hdfs-audit", rangerHdfsAuditPropsMap);
        singleHostTopologyUpdaters.put("ranger-hbase-audit", rangerHbaseAuditPropsMap);
        singleHostTopologyUpdaters.put("ranger-hive-audit", rangerHiveAuditPropsMap);
        singleHostTopologyUpdaters.put("ranger-knox-audit", rangerKnoxAuditPropsMap);
        singleHostTopologyUpdaters.put("ranger-kafka-audit", rangerKafkaAuditPropsMap);
        singleHostTopologyUpdaters.put("ranger-storm-audit", rangerStormAuditPropsMap);
        singleHostTopologyUpdaters.put("ranger-atlas-audit", rangerAtlasAuditPropsMap);
        singleHostTopologyUpdaters.put(HADOOP_ENV_CONFIG_TYPE_NAME, shHadoopEnvMap);
        singleHostTopologyUpdaters.put(CLUSTER_ENV_CONFIG_TYPE_NAME, clusterEnvMap);
        singleHostTopologyUpdaters.put("hawq-site", hawqSiteMap);
        singleHostTopologyUpdaters.put("zookeeper-env", zookeeperEnvMap);
        mPropertyUpdaters.put(HADOOP_ENV_CONFIG_TYPE_NAME, mHadoopEnvMap);
        mPropertyUpdaters.put("hbase-env", hbaseEnvMap);
        mPropertyUpdaters.put("mapred-env", mapredEnvMap);
        mPropertyUpdaters.put("oozie-env", oozieEnvHeapSizeMap);
        multiHostTopologyUpdaters.put("webhcat-site", multiWebhcatSiteMap);
        multiHostTopologyUpdaters.put("hbase-site", multiHbaseSiteMap);
        multiHostTopologyUpdaters.put("storm-site", multiStormSiteMap);
        multiHostTopologyUpdaters.put("core-site", multiCoreSiteMap);
        multiHostTopologyUpdaters.put("hdfs-site", multiHdfsSiteMap);
        multiHostTopologyUpdaters.put("hive-site", multiHiveSiteMap);
        multiHostTopologyUpdaters.put("hive-interactive-site", hiveInteractiveSiteMap);
        multiHostTopologyUpdaters.put("kafka-broker", multiKafkaBrokerMap);
        multiHostTopologyUpdaters.put("yarn-site", multiYarnSiteMap);
        multiHostTopologyUpdaters.put("oozie-site", multiOozieSiteMap);
        multiHostTopologyUpdaters.put("accumulo-site", multiAccumuloSiteMap);
        multiHostTopologyUpdaters.put("kms-site", multiRangerKmsSiteMap);
        multiHostTopologyUpdaters.put("application-properties", atlasPropsMap);
        multiHostTopologyUpdaters.put("livy2-conf", livy2Conf);
        dbHostTopologyUpdaters.put("hive-site", dbHiveSiteMap);
        nonTopologyUpdaters.put("hive-site", hiveSiteNonTopologyMap);
        nonTopologyUpdaters.put("kafka-broker", kafkaBrokerNonTopologyMap);
        nonTopologyUpdaters.put("storm-site", stormSiteNonTopologyMap);
        hdfsSiteMap.put("dfs.http.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        hdfsSiteMap.put("dfs.https.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        coreSiteMap.put("fs.default.name", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        hdfsSiteMap.put("dfs.namenode.http-address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        hdfsSiteMap.put("dfs.namenode.https-address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        hdfsSiteMap.put("dfs.namenode.rpc-address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        coreSiteMap.put("fs.defaultFS", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        hbaseSiteMap.put("hbase.rootdir", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        accumuloSiteMap.put("instance.volumes", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
        multiHdfsSiteMap.put("dfs.namenode.shared.edits.dir", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("JOURNALNODE", ';', false, false, true));
        multiHdfsSiteMap.put("dfs.encryption.key.provider.uri", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("RANGER_KMS_SERVER", ';', false, false, false));
        clusterEnvMap.put(HDFS_ACTIVE_NAMENODE_PROPERTY_NAME, new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
        clusterEnvMap.put(HDFS_STANDBY_NAMENODE_PROPERTY_NAME, new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
        hdfsSiteMap.put("dfs.secondary.http.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("SECONDARY_NAMENODE"));
        hdfsSiteMap.put("dfs.namenode.secondary.http-address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("SECONDARY_NAMENODE"));
        mapredSiteMap.put("mapred.job.tracker", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("JOBTRACKER"));
        mapredSiteMap.put("mapred.job.tracker.http.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("JOBTRACKER"));
        mapredSiteMap.put("mapreduce.history.server.http.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("JOBTRACKER"));
        mapredSiteMap.put("mapreduce.job.hdfs-servers", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
        yarnSiteMap.put("yarn.log.server.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("HISTORYSERVER"));
        mapredSiteMap.put("mapreduce.jobhistory.webapp.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("HISTORYSERVER"));
        mapredSiteMap.put("mapreduce.jobhistory.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("HISTORYSERVER"));
        yarnSiteMap.put("yarn.resourcemanager.hostname", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("RESOURCEMANAGER"));
        yarnSiteMap.put("yarn.resourcemanager.resource-tracker.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("RESOURCEMANAGER"));
        yarnSiteMap.put("yarn.resourcemanager.webapp.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("RESOURCEMANAGER"));
        yarnSiteMap.put("yarn.resourcemanager.scheduler.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("RESOURCEMANAGER"));
        yarnSiteMap.put("yarn.resourcemanager.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("RESOURCEMANAGER"));
        yarnSiteMap.put("yarn.resourcemanager.admin.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("RESOURCEMANAGER"));
        yarnSiteMap.put("yarn.resourcemanager.webapp.https.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("RESOURCEMANAGER"));
        yarnSiteMap.put("yarn.timeline-service.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("APP_TIMELINE_SERVER"));
        yarnSiteMap.put("yarn.timeline-service.webapp.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("APP_TIMELINE_SERVER"));
        yarnSiteMap.put("yarn.timeline-service.webapp.https.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("APP_TIMELINE_SERVER"));
        yarnSiteMap.put("yarn.log.server.web-service.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("APP_TIMELINE_SERVER"));
        yarnSiteMap.put("yarn.timeline-service.reader.webapp.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("TIMELINE_READER"));
        yarnSiteMap.put("yarn.timeline-service.reader.webapp.https.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("TIMELINE_READER"));
        hiveSiteMap.put("hive.server2.authentication.ldap.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("HIVE_SERVER2"));
        multiHiveSiteMap.put("hive.metastore.uris", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("HIVE_METASTORE", ',', true, true, true));
        dbHiveSiteMap.put("javax.jdo.option.ConnectionURL", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.DBTopologyUpdater("MYSQL_SERVER", "hive-env", "hive_database"));
        multiCoreSiteMap.put("hadoop.proxyuser.hive.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("HIVE_SERVER"));
        multiCoreSiteMap.put("hadoop.proxyuser.HTTP.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("WEBHCAT_SERVER"));
        multiCoreSiteMap.put("hadoop.proxyuser.hcat.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("WEBHCAT_SERVER"));
        multiCoreSiteMap.put("hadoop.proxyuser.yarn.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("RESOURCEMANAGER"));
        multiCoreSiteMap.put("hadoop.security.key.provider.path", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("RANGER_KMS_SERVER", ';', false, false, true));
        multiWebhcatSiteMap.put("templeton.hive.properties", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.TempletonHivePropertyUpdater());
        multiHiveSiteMap.put("hive.zookeeper.quorum", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        multiHiveSiteMap.put("hive.cluster.delegation.token.store.zookeeper.connectString", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        hiveInteractiveEnvMap.put("hive_server_interactive_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("HIVE_SERVER_INTERACTIVE"));
        hiveInteractiveSiteMap.put("hive.llap.zk.sm.connectionString", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        hiveSiteNonTopologyMap.put("hive.exec.post.hooks", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.NonTopologyUpdater() {
            @java.lang.Override
            public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
                java.lang.String atlasHookClass = "org.apache.atlas.hive.hook.HiveHook";
                java.lang.String[] hiveHooks = origValue.split(",");
                java.util.List<java.lang.String> hiveHooksClean = new java.util.ArrayList<>();
                for (java.lang.String hiveHook : hiveHooks) {
                    if (!org.apache.commons.lang.StringUtils.isBlank(hiveHook.trim())) {
                        hiveHooksClean.add(hiveHook.trim());
                    }
                }
                boolean isAtlasInCluster = topology.getBlueprint().getServices().contains("ATLAS");
                boolean isAtlasHiveHookEnabled = java.lang.Boolean.parseBoolean(properties.get("hive-env").get("hive.atlas.hook"));
                if (isAtlasInCluster || isAtlasHiveHookEnabled) {
                    if (!hiveHooksClean.contains(atlasHookClass)) {
                        hiveHooksClean.add(atlasHookClass);
                    }
                } else {
                    while (hiveHooksClean.contains(atlasHookClass)) {
                        hiveHooksClean.remove(atlasHookClass);
                    } 
                }
                if (!hiveHooksClean.isEmpty()) {
                    return org.apache.commons.lang.StringUtils.join(hiveHooksClean, ",");
                } else {
                    return " ";
                }
            }
        });
        hiveSiteNonTopologyMap.put("atlas.cluster.name", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.NonTopologyUpdater() {
            @java.lang.Override
            public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
                if (topology.getBlueprint().getServices().contains("ATLAS")) {
                    if (((origValue == null) || origValue.trim().isEmpty()) || origValue.equals("primary")) {
                        return java.lang.String.valueOf(topology.getClusterId());
                    } else {
                        return origValue;
                    }
                } else {
                    return origValue;
                }
            }

            @java.lang.Override
            public java.lang.String updateForBlueprintExport(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
                if (origValue.equals(java.lang.String.valueOf(topology.getClusterId()))) {
                    return "primary";
                }
                return origValue;
            }
        });
        hiveSiteMap.put("atlas.rest.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("ATLAS_SERVER") {
            @java.lang.Override
            public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
                if (topology.getBlueprint().getServices().contains("ATLAS")) {
                    java.lang.String host = topology.getHostAssignmentsForComponent("ATLAS_SERVER").iterator().next();
                    boolean tlsEnabled = java.lang.Boolean.parseBoolean(properties.get("application-properties").get("atlas.enableTLS"));
                    java.lang.String scheme;
                    java.lang.String port;
                    if (tlsEnabled) {
                        scheme = "https";
                        port = properties.get("application-properties").get("atlas.server.https.port");
                    } else {
                        scheme = "http";
                        port = properties.get("application-properties").get("atlas.server.http.port");
                    }
                    return java.lang.String.format("%s://%s:%s", scheme, host, port);
                }
                return origValue;
            }
        });
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> oozieStringPropertyUpdaterMap = singleHostTopologyUpdaters.get("oozie-site");
        if (oozieStringPropertyUpdaterMap == null) {
            oozieStringPropertyUpdaterMap = new java.util.HashMap<>();
        }
        oozieStringPropertyUpdaterMap.put("oozie.base.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("OOZIE_SERVER"));
        singleHostTopologyUpdaters.put("oozie-site", oozieStringPropertyUpdaterMap);
        multiCoreSiteMap.put("hadoop.proxyuser.oozie.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("OOZIE_SERVER"));
        multiHbaseSiteMap.put("hbase.zookeeper.quorum", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        multiWebhcatSiteMap.put("templeton.zookeeper.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        multiCoreSiteMap.put("ha.zookeeper.quorum", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        multiYarnSiteMap.put("hadoop.registry.zk.quorum", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        multiYarnSiteMap.put("yarn.resourcemanager.zk-address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        multiKafkaBrokerMap.put("zookeeper.connect", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        multiAccumuloSiteMap.put("instance.zookeeper.host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        stormSiteMap.put("nimbus.host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NIMBUS"));
        stormSiteMap.put("nimbus_hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NIMBUS"));
        stormSiteMap.put("drpc_server_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("DRPC_SERVER"));
        stormSiteMap.put("drpc.servers", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("DRPC_SERVER"));
        stormSiteMap.put("storm_ui_server_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("STORM_UI_SERVER"));
        stormSiteMap.put("worker.childopts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("GANGLIA_SERVER"));
        stormSiteMap.put("supervisor.childopts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("GANGLIA_SERVER"));
        stormSiteMap.put("nimbus.childopts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("GANGLIA_SERVER"));
        stormSiteNonTopologyMap.put("metrics.reporter.register", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.NonTopologyUpdater() {
            @java.lang.Override
            public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
                if (topology.getBlueprint().getServices().contains("AMBARI_METRICS")) {
                    final java.lang.String amsReporterClass = "org.apache.hadoop.metrics2.sink.storm.StormTimelineMetricsReporter";
                    if ((origValue == null) || origValue.isEmpty()) {
                        return amsReporterClass;
                    }
                }
                return origValue;
            }
        });
        multiStormSiteMap.put("supervisor_hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("SUPERVISOR")));
        multiStormSiteMap.put("storm.zookeeper.servers", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER")));
        multiStormSiteMap.put("nimbus.seeds", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator(new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("NIMBUS"), org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.YamlMultiValuePropertyDecorator.FlowStyle.PLAIN));
        falconStartupPropertiesMap.put("*.broker.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("FALCON_SERVER"));
        kafkaBrokerMap.put("kafka.ganglia.metrics.host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("GANGLIA_SERVER"));
        kafkaBrokerNonTopologyMap.put("kafka.metrics.reporters", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.NonTopologyUpdater() {
            @java.lang.Override
            public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
                if (topology.getBlueprint().getServices().contains("AMBARI_METRICS")) {
                    final java.lang.String amsReportesClass = "org.apache.hadoop.metrics2.sink.kafka.KafkaTimelineMetricsReporter";
                    if ((origValue == null) || origValue.isEmpty()) {
                        return amsReportesClass;
                    } else if (!origValue.contains(amsReportesClass)) {
                        return java.lang.String.format("%s,%s", origValue, amsReportesClass);
                    }
                }
                return origValue;
            }
        });
        multiCoreSiteMap.put("hadoop.proxyuser.knox.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("KNOX_GATEWAY"));
        multiWebhcatSiteMap.put("webhcat.proxyuser.knox.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("KNOX_GATEWAY"));
        multiOozieSiteMap.put("hadoop.proxyuser.knox.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("KNOX_GATEWAY"));
        multiOozieSiteMap.put("oozie.service.ProxyUserService.proxyuser.knox.hosts", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("KNOX_GATEWAY"));
        atlasPropsMap.put("atlas.server.bind.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ATLAS_SERVER"));
        atlasPropsMap.put("atlas.rest.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ATLAS_SERVER", ',', true, true, true));
        atlasPropsMap.put("atlas.kafka.bootstrap.servers", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("KAFKA_BROKER"));
        atlasPropsMap.put("atlas.kafka.zookeeper.connect", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        atlasPropsMap.put("atlas.graph.index.search.solr.zookeeper-url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER", ',', false, true, true));
        atlasPropsMap.put("atlas.graph.storage.hostname", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        atlasPropsMap.put("atlas.audit.hbase.zookeeper.quorum", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        rangerAdminPropsMap.put("policymgr_external_url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("RANGER_ADMIN"));
        java.util.List<java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater>> configsWithRangerHdfsAuditDirProperty = com.google.common.collect.ImmutableList.of(rangerEnvPropsMap, rangerYarnAuditPropsMap, rangerHdfsAuditPropsMap, rangerHbaseAuditPropsMap, rangerHiveAuditPropsMap, rangerKnoxAuditPropsMap, rangerKafkaAuditPropsMap, rangerStormAuditPropsMap, rangerAtlasAuditPropsMap);
        for (java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> rangerAuditPropsMap : configsWithRangerHdfsAuditDirProperty) {
            rangerAuditPropsMap.put("xasecure.audit.destination.hdfs.dir", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.OptionalSingleHostTopologyUpdater("NAMENODE"));
        }
        multiRangerKmsSiteMap.put("hadoop.kms.authentication.signer.secret.provider.zookeeper.connection.string", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        addUnitPropertyUpdaters();
        hawqSiteMap.put("hawq_master_address_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("HAWQMASTER"));
        hawqSiteMap.put("hawq_standby_address_host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("HAWQSTANDBY"));
        hawqSiteMap.put("hawq_dfs_url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("NAMENODE"));
        amsSiteMap.put("timeline.metrics.service.webapp.address", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.SingleHostTopologyUpdater("METRICS_COLLECTOR") {
            @java.lang.Override
            public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
                if (!origValue.startsWith(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.BIND_ALL_IP_ADDRESS)) {
                    return origValue.replace(origValue.split(":")[0], org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.BIND_ALL_IP_ADDRESS);
                } else {
                    return origValue;
                }
            }
        });
        druidCommon.put("metastore_hostname", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.INSTANCE);
        druidCommon.put("druid.metadata.storage.connector.connectURI", org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HostGroupUpdater.INSTANCE);
        druidCommon.put("druid.zk.service.host", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
        livy2Conf.put("livy.server.recovery.state-store.url", new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.MultipleHostTopologyUpdater("ZOOKEEPER_SERVER"));
    }

    private static void addUnitPropertyUpdaters() {
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.topology.validators.UnitValidatedProperty>> propsPerConfigType = org.apache.ambari.server.topology.validators.UnitValidatedProperty.ALL.stream().collect(java.util.stream.Collectors.groupingBy(org.apache.ambari.server.topology.validators.UnitValidatedProperty::getConfigType));
        for (java.lang.String configType : propsPerConfigType.keySet()) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater> unitUpdaters = new java.util.HashMap<>();
            for (org.apache.ambari.server.topology.validators.UnitValidatedProperty each : propsPerConfigType.get(configType)) {
                unitUpdaters.put(each.getPropertyName(), new org.apache.ambari.server.controller.internal.UnitUpdater(each.getServiceName(), each.getConfigType()));
            }
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.mPropertyUpdaters.put(configType, unitUpdaters);
        }
    }

    private static java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> generateHadoopProxyUserPropertyNames(org.apache.ambari.server.topology.Configuration configuration) {
        java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> proxyUsers = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingProperties = configuration.getFullProperties();
        for (org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String> userProp : org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PROPERTIES_FOR_HADOOP_PROXYUSER) {
            java.lang.String configType = userProp.getLeft();
            java.lang.String property = userProp.getRight();
            java.util.Map<java.lang.String, java.lang.String> configs = existingProperties.get(configType);
            if (configs != null) {
                java.lang.String user = configs.get(property);
                if (!com.google.common.base.Strings.isNullOrEmpty(user)) {
                    proxyUsers.add(org.apache.commons.lang3.tuple.Pair.of(configType, java.lang.String.format(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_PROXYUSER_HOSTS_FORMAT, user)));
                    proxyUsers.add(org.apache.commons.lang3.tuple.Pair.of(configType, java.lang.String.format(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HADOOP_PROXYUSER_GROUPS_FORMAT, user)));
                }
            }
        }
        return proxyUsers;
    }

    private static void setupHDFSProxyUsers(org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated, java.util.Collection<java.lang.String> services) {
        if (services.contains("HDFS")) {
            java.util.Set<org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String>> configTypePropertyPairs = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.generateHadoopProxyUserPropertyNames(configuration);
            java.util.Set<java.lang.String> acceptableConfigTypes = org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.getEligibleConfigTypesForHadoopProxyUsers(services);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingProperties = configuration.getFullProperties();
            for (org.apache.commons.lang3.tuple.Pair<java.lang.String, java.lang.String> pair : configTypePropertyPairs) {
                java.lang.String configType = pair.getLeft();
                if (acceptableConfigTypes.contains(configType)) {
                    java.util.Map<java.lang.String, java.lang.String> configs = existingProperties.get(configType);
                    if (configs != null) {
                        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.ensureProperty(configuration, "core-site", pair.getRight(), "*", configTypesUpdated);
                    }
                }
            }
        }
    }

    private static java.util.Set<java.lang.String> getEligibleConfigTypesForHadoopProxyUsers(java.util.Collection<java.lang.String> services) {
        java.util.Set<java.lang.String> acceptableConfigTypes = new java.util.HashSet<>();
        if (services.contains("OOZIE")) {
            acceptableConfigTypes.add("oozie-env");
        }
        if (services.contains("HIVE")) {
            acceptableConfigTypes.add("hive-env");
        }
        if (services.contains("HBASE")) {
            acceptableConfigTypes.add("hbase-env");
        }
        if (services.contains("FALCON")) {
            acceptableConfigTypes.add("falcon-env");
        }
        return acceptableConfigTypes;
    }

    private void addExcludedConfigProperties(org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated, org.apache.ambari.server.controller.internal.Stack stack) {
        java.util.Collection<java.lang.String> blueprintServices = clusterTopology.getBlueprint().getServices();
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.debug("Handling excluded properties for blueprint services: {}", blueprintServices);
        for (java.lang.String blueprintService : blueprintServices) {
            org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.debug("Handling excluded properties for blueprint service: {}", blueprintService);
            java.util.Set<java.lang.String> excludedConfigTypes = stack.getExcludedConfigurationTypes(blueprintService);
            if (excludedConfigTypes.isEmpty()) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.debug("There are no excluded config types for blueprint service: {}", blueprintService);
                continue;
            }
            for (java.lang.String configType : excludedConfigTypes) {
                org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.debug("Handling excluded config type [{}] for blueprint service: [{}]", configType, blueprintService);
                java.lang.String blueprintServiceForExcludedConfig;
                try {
                    blueprintServiceForExcludedConfig = stack.getServiceForConfigType(configType);
                } catch (java.lang.IllegalArgumentException illegalArgumentException) {
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.warn(((("Error encountered while trying to obtain the service name for config type [" + configType) + "].  Further processing on this excluded config type will be skipped.  ") + "This usually means that a service's definitions have been manually removed from the Ambari stack definitions.  ") + "If the stack definitions have not been changed manually, this may indicate a stack definition error in Ambari.  ", illegalArgumentException);
                    continue;
                }
                if (!blueprintServices.contains(blueprintServiceForExcludedConfig)) {
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.debug("Service [{}] for excluded config type [{}] is not present in the blueprint. " + "Ignoring excluded config entries.", blueprintServiceForExcludedConfig, configType);
                    continue;
                }
                java.util.Map<java.lang.String, java.lang.String> configProperties = stack.getConfigurationProperties(blueprintService, configType);
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : configProperties.entrySet()) {
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.LOG.debug("ADD property {} {} {}", configType, entry.getKey(), entry.getValue());
                    org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.ensureProperty(configuration, configType, entry.getKey(), entry.getValue(), configTypesUpdated);
                }
            }
        }
    }

    private static void setRetryConfiguration(org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated) {
        boolean wasUpdated = false;
        if (configuration.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMAND_RETRY_ENABLED_PROPERTY_NAME) == null) {
            configuration.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMAND_RETRY_ENABLED_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMAND_RETRY_ENABLED_DEFAULT);
            wasUpdated = true;
        }
        if (configuration.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMANDS_TO_RETRY_PROPERTY_NAME) == null) {
            configuration.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMANDS_TO_RETRY_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMANDS_TO_RETRY_DEFAULT);
            wasUpdated = true;
        }
        if (configuration.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMAND_RETRY_MAX_TIME_IN_SEC_PROPERTY_NAME) == null) {
            configuration.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMAND_RETRY_MAX_TIME_IN_SEC_PROPERTY_NAME, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.COMMAND_RETRY_MAX_TIME_IN_SEC_DEFAULT);
            wasUpdated = true;
        }
        if (wasUpdated) {
            configTypesUpdated.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME);
        }
    }

    protected void setStackToolsAndFeatures(org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated) throws org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        org.apache.ambari.server.state.ConfigHelper configHelper = clusterTopology.getAmbariContext().getConfigHelper();
        org.apache.ambari.server.controller.internal.Stack stack = clusterTopology.getBlueprint().getStack();
        java.lang.String stackName = stack.getName();
        java.lang.String stackVersion = stack.getVersion();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackName, stackVersion);
        java.util.Set<java.lang.String> properties = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_NAME_PROPERTY, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_ROOT_PROPERTY, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_TOOLS_PROPERTY, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_FEATURES_PROPERTY, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_PACKAGES_PROPERTY);
        try {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> defaultStackProperties = configHelper.getDefaultStackProperties(stackId);
            java.util.Map<java.lang.String, java.lang.String> clusterEnvDefaultProperties = defaultStackProperties.get(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME);
            for (java.lang.String property : properties) {
                if (clusterEnvDefaultProperties.containsKey(property)) {
                    java.lang.String newValue = clusterEnvDefaultProperties.get(property);
                    java.lang.String previous = configuration.setProperty(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, property, newValue);
                    if (!java.util.Objects.equals(trimValue(previous, stack, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, property), trimValue(newValue, stack, org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME, property))) {
                        configTypesUpdated.add(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.CLUSTER_ENV_CONFIG_TYPE_NAME);
                    }
                }
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException("Unable to retrieve the stack tools and features", ambariException);
        }
    }

    private static void injectDefaults(org.apache.ambari.server.topology.Configuration configuration, java.util.Set<java.lang.String> configTypesUpdated, java.util.Collection<java.lang.String> services) {
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.setRetryConfiguration(configuration, configTypesUpdated);
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.setupHDFSProxyUsers(configuration, configTypesUpdated, services);
    }

    @javax.annotation.Nullable
    private java.lang.String trimValue(@javax.annotation.Nullable
    java.lang.String value, @javax.validation.constraints.NotNull
    org.apache.ambari.server.controller.internal.Stack stack, @javax.validation.constraints.NotNull
    java.lang.String configType, @javax.validation.constraints.NotNull
    java.lang.String propertyName) {
        if (null == value) {
            return null;
        } else {
            org.apache.ambari.server.controller.internal.TrimmingStrategy trimmingStrategy = org.apache.ambari.server.controller.internal.PropertyValueTrimmingStrategyDefiner.defineTrimmingStrategy(stack, propertyName, configType);
            return trimmingStrategy.trim(value);
        }
    }

    private static void ensureProperty(org.apache.ambari.server.topology.Configuration configuration, java.lang.String type, java.lang.String property, java.lang.String defaultValue, java.util.Set<java.lang.String> configTypesUpdated) {
        if (configuration.getPropertyValue(type, property) == null) {
            configuration.setProperty(type, property, defaultValue);
            configTypesUpdated.add(type);
        }
    }

    private interface PropertyFilter {
        boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology);
    }

    private static class PasswordPropertyFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        private static final java.util.regex.Pattern PASSWORD_NAME_REGEX = java.util.regex.Pattern.compile("\\S+(PASSWORD|SECRET)", java.util.regex.Pattern.CASE_INSENSITIVE);

        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            return !org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PasswordPropertyFilter.PASSWORD_NAME_REGEX.matcher(propertyName).matches();
        }
    }

    private static class StackPropertyTypeFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
            final java.lang.String serviceName = stack.getServiceForConfigType(configType);
            return !(stack.isPasswordProperty(serviceName, configType, propertyName) || stack.isKerberosPrincipalNameProperty(serviceName, configType, propertyName));
        }
    }

    private static class KerberosAuthToLocalRulesFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        java.util.Map<java.lang.Long, java.util.Set<java.lang.String>> authToLocalPerClusterMap = null;

        KerberosAuthToLocalRulesFilter(java.util.Map<java.lang.Long, java.util.Set<java.lang.String>> authToLocalPerClusterMap) {
            this.authToLocalPerClusterMap = authToLocalPerClusterMap;
        }

        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            return ((authToLocalPerClusterMap == null) || (authToLocalPerClusterMap.get(topology.getClusterId()) == null)) || (!authToLocalPerClusterMap.get(topology.getClusterId()).contains(java.lang.String.format("%s/%s", configType, propertyName)));
        }
    }

    private static class SimplePropertyNameExportFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        private final java.lang.String propertyName;

        private final java.lang.String propertyConfigType;

        SimplePropertyNameExportFilter(java.lang.String propertyName, java.lang.String propertyConfigType) {
            this.propertyName = propertyName;
            this.propertyConfigType = propertyConfigType;
        }

        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            return !(propertyConfigType.equals(configType) && this.propertyName.equals(propertyName));
        }
    }

    private static abstract class DependencyFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        private final java.lang.String dependsOnPropertyName;

        private final java.lang.String dependsOnConfigType;

        DependencyFilter(java.lang.String dependsOnPropertyName, java.lang.String dependsOnConfigType) {
            this.dependsOnPropertyName = dependsOnPropertyName;
            this.dependsOnConfigType = dependsOnConfigType;
        }

        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
            org.apache.ambari.server.topology.Configuration configuration = topology.getConfiguration();
            final java.lang.String serviceName = stack.getServiceForConfigType(configType);
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> typeProperties = stack.getConfigurationPropertiesWithMetadata(serviceName, configType);
            org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty = typeProperties.get(propertyName);
            if (configProperty != null) {
                java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependencyInfos = configProperty.getDependsOnProperties();
                if (dependencyInfos != null) {
                    for (org.apache.ambari.server.state.PropertyDependencyInfo propertyDependencyInfo : dependencyInfos) {
                        if (propertyDependencyInfo.getName().equals(dependsOnPropertyName) && propertyDependencyInfo.getType().equals(dependsOnConfigType)) {
                            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfig = configuration.getFullProperties();
                            java.util.Map<java.lang.String, java.lang.String> configByType = clusterConfig.get(dependsOnConfigType);
                            return isConditionSatisfied(dependsOnPropertyName, configByType.get(dependsOnPropertyName), dependsOnConfigType);
                        }
                    }
                }
            }
            return true;
        }

        public abstract boolean isConditionSatisfied(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyType);
    }

    private static class DependencyEqualsFilter extends org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.DependencyFilter {
        private final java.lang.String value;

        DependencyEqualsFilter(java.lang.String dependsOnPropertyName, java.lang.String dependsOnConfigType, java.lang.String value) {
            super(dependsOnPropertyName, dependsOnConfigType);
            this.value = value;
        }

        @java.lang.Override
        public boolean isConditionSatisfied(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyType) {
            return value.equals(propertyValue);
        }
    }

    private static class DependencyNotEqualsFilter extends org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.DependencyFilter {
        private final java.lang.String value;

        DependencyNotEqualsFilter(java.lang.String dependsOnPropertyName, java.lang.String dependsOnConfigType, java.lang.String value) {
            super(dependsOnPropertyName, dependsOnConfigType);
            this.value = value;
        }

        @java.lang.Override
        public boolean isConditionSatisfied(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyType) {
            return !value.equals(propertyValue);
        }
    }

    private static class HDFSNameNodeHAFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        private final java.util.Set<java.lang.String> setOfHDFSPropertyNamesNonHA = java.util.Collections.unmodifiableSet(new java.util.HashSet<>(java.util.Arrays.asList("dfs.namenode.http-address", "dfs.namenode.https-address", "dfs.namenode.rpc-address")));

        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            if (topology.isNameNodeHAEnabled()) {
                if (setOfHDFSPropertyNamesNonHA.contains(propertyName)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class ConditionalPropertyFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        private final java.lang.String propertyName;

        private final java.lang.String propertyValue;

        private final java.lang.String configType;

        public ConditionalPropertyFilter(java.lang.String configType, java.lang.String propertyName, java.lang.String propertyValue) {
            this.propertyName = propertyName;
            this.propertyValue = propertyValue;
            this.configType = configType;
        }

        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            if ((configType.equals(this.configType) && propertyName.equals(this.propertyName)) && propertyValue.equals(this.propertyValue)) {
                return false;
            }
            return true;
        }
    }

    private static class HawqHAFilter implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyFilter {
        private final java.util.Set<java.lang.String> setOfHawqPropertyNamesNonHA = java.util.Collections.unmodifiableSet(new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HAWQ_SITE_HAWQ_STANDBY_ADDRESS_HOST)));

        @java.lang.Override
        public boolean isPropertyIncluded(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String configType, org.apache.ambari.server.topology.ClusterTopology topology) {
            int matchingGroupCount = topology.getHostGroupsForComponent(org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.HAWQSTANDBY).size();
            if (matchingGroupCount == 0) {
                if (setOfHawqPropertyNamesNonHA.contains(propertyName)) {
                    return false;
                }
            }
            return true;
        }
    }
}