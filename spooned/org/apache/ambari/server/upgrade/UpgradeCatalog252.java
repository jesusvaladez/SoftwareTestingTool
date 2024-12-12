package org.apache.ambari.server.upgrade;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
public class UpgradeCatalog252 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    static final java.lang.String CLUSTERCONFIG_TABLE = "clusterconfig";

    static final java.lang.String SERVICE_DELETED_COLUMN = "service_deleted";

    static final java.lang.String UNMAPPED_COLUMN = "unmapped";

    private static final java.lang.String UPGRADE_TABLE = "upgrade";

    private static final java.lang.String UPGRADE_TABLE_FROM_REPO_COLUMN = "from_repo_version_id";

    private static final java.lang.String UPGRADE_TABLE_TO_REPO_COLUMN = "to_repo_version_id";

    private static final java.lang.String CLUSTERS_TABLE = "clusters";

    private static final java.lang.String SERVICE_COMPONENT_HISTORY_TABLE = "servicecomponent_history";

    private static final java.lang.String UPGRADE_GROUP_TABLE = "upgrade_group";

    private static final java.lang.String UPGRADE_ITEM_TABLE = "upgrade_item";

    private static final java.lang.String UPGRADE_ID_COLUMN = "upgrade_id";

    private static final java.lang.String CLUSTER_ENV = "cluster-env";

    private static final java.lang.String HIVE_ENV = "hive-env";

    private static final java.lang.String MARIADB_REDHAT_SUPPORT = "mariadb_redhat_support";

    private static final java.util.List<java.lang.String> configTypesToEnsureSelected = java.util.Arrays.asList("spark2-javaopts-properties");

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog252.class);

    @com.google.inject.Inject
    public UpgradeCatalog252(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.5.1";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.5.2";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addServiceDeletedColumnToClusterConfigTable();
        addRepositoryColumnsToUpgradeTable();
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addNewConfigurationsFromXml();
        resetStackToolsAndFeatures();
        updateKerberosDescriptorArtifacts();
        fixLivySuperusers();
    }

    private void addServiceDeletedColumnToClusterConfigTable() throws java.sql.SQLException {
        if (!dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog252.CLUSTERCONFIG_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog252.UNMAPPED_COLUMN)) {
            dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog252.CLUSTERCONFIG_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog252.SERVICE_DELETED_COLUMN, java.lang.Short.class, null, 0, false));
        }
    }

    private void addRepositoryColumnsToUpgradeTable() throws java.sql.SQLException {
        dbAccessor.clearTableColumn(org.apache.ambari.server.upgrade.UpgradeCatalog252.CLUSTERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_ID_COLUMN, null);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog252.SERVICE_COMPONENT_HISTORY_TABLE);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog252.SERVICE_COMPONENT_HISTORY_TABLE);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_ITEM_TABLE);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_GROUP_TABLE);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE, "to_version");
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE, "from_version");
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE_FROM_REPO_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE, "FK_upgrade_from_repo_id", org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE_FROM_REPO_COLUMN, "repo_version", "repo_version_id", false);
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE_TO_REPO_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE, "FK_upgrade_to_repo_id", org.apache.ambari.server.upgrade.UpgradeCatalog252.UPGRADE_TABLE_FROM_REPO_COLUMN, "repo_version", "repo_version_id", false);
    }

    private void resetStackToolsAndFeatures() throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> propertiesToReset = com.google.common.collect.Sets.newHashSet("stack_tools", "stack_features", "stack_root");
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            org.apache.ambari.server.state.Config clusterEnv = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.UpgradeCatalog252.CLUSTER_ENV);
            if (null == clusterEnv) {
                continue;
            }
            java.util.Map<java.lang.String, java.lang.String> newStackProperties = new java.util.HashMap<>();
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = configHelper.getStackProperties(cluster);
            if (null == stackProperties) {
                continue;
            }
            for (org.apache.ambari.server.state.PropertyInfo propertyInfo : stackProperties) {
                java.lang.String fileName = propertyInfo.getFilename();
                if (org.apache.commons.lang.StringUtils.isEmpty(fileName)) {
                    continue;
                }
                if (org.apache.commons.lang.StringUtils.equals(org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(fileName), org.apache.ambari.server.upgrade.UpgradeCatalog252.CLUSTER_ENV)) {
                    java.lang.String stackPropertyName = propertyInfo.getName();
                    if (propertiesToReset.contains(stackPropertyName)) {
                        newStackProperties.put(stackPropertyName, propertyInfo.getValue());
                    }
                }
            }
            updateConfigurationPropertiesForCluster(cluster, org.apache.ambari.server.upgrade.UpgradeCatalog252.CLUSTER_ENV, newStackProperties, true, false);
        }
    }

    @java.lang.Override
    protected void updateKerberosDescriptorArtifact(org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO, org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity) throws org.apache.ambari.server.AmbariException {
        if (artifactEntity != null) {
            java.util.Map<java.lang.String, java.lang.Object> data = artifactEntity.getArtifactData();
            if (data != null) {
                final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(data);
                if (kerberosDescriptor != null) {
                    boolean updated = false;
                    if (removeConfigurationSpecifications(kerberosDescriptor.getService("SPARK"), java.util.Collections.<java.lang.String, java.util.Collection<java.lang.String>>singletonMap("livy-conf", java.util.Collections.singleton("livy.superusers")))) {
                        updated = true;
                    }
                    if (removeConfigurationSpecifications(kerberosDescriptor.getService("SPARK2"), java.util.Collections.<java.lang.String, java.util.Collection<java.lang.String>>singletonMap("livy2-conf", java.util.Collections.singleton("livy.superusers")))) {
                        updated = true;
                    }
                    org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor knoxKerberosDescriptor = kerberosDescriptor.getService("KNOX");
                    if (knoxKerberosDescriptor != null) {
                        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor knoxGatewayKerberosDescriptor = knoxKerberosDescriptor.getComponent("KNOX_GATEWAY");
                        if (knoxGatewayKerberosDescriptor != null) {
                            java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> configsToRemove = new java.util.HashMap<>();
                            configsToRemove.put("oozie-site", java.util.Arrays.asList("oozie.service.ProxyUserService.proxyuser.knox.groups", "oozie.service.ProxyUserService.proxyuser.knox.hosts"));
                            configsToRemove.put("webhcat-site", java.util.Arrays.asList("webhcat.proxyuser.knox.groups", "webhcat.proxyuser.knox.hosts"));
                            configsToRemove.put("core-site", java.util.Arrays.asList("hadoop.proxyuser.knox.groups", "hadoop.proxyuser.knox.hosts"));
                            configsToRemove.put("falcon-runtime.properties", java.util.Arrays.asList("*.falcon.service.ProxyUserService.proxyuser.knox.groups", "*.falcon.service.ProxyUserService.proxyuser.knox.hosts"));
                            if (removeConfigurationSpecifications(knoxGatewayKerberosDescriptor, configsToRemove)) {
                                updated = true;
                            }
                        }
                    }
                    if (updated) {
                        artifactEntity.setArtifactData(kerberosDescriptor.toMap());
                        artifactDAO.merge(artifactEntity);
                    }
                }
            }
        }
    }

    void fixLivySuperusers() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config zeppelinEnvProperties = cluster.getDesiredConfigByType("zeppelin-env");
                    if (zeppelinEnvProperties != null) {
                        java.util.Map<java.lang.String, java.lang.String> zeppelinProperties = zeppelinEnvProperties.getProperties();
                        if (zeppelinProperties != null) {
                            java.lang.String zeppelinPrincipal = zeppelinProperties.get("zeppelin.server.kerberos.principal");
                            if (!org.apache.commons.lang.StringUtils.isEmpty(zeppelinPrincipal)) {
                                org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal deconstructedPrincipal = org.apache.ambari.server.serveraction.kerberos.DeconstructedPrincipal.valueOf(zeppelinPrincipal, "EXAMPLE.COM");
                                java.lang.String newZeppelinPrincipalName = deconstructedPrincipal.getPrincipalName();
                                java.lang.String oldZeppelinPrincipalName = "zeppelin-" + cluster.getClusterName().toLowerCase();
                                updateListValues(cluster, "livy-conf", "livy.superusers", java.util.Collections.singleton(newZeppelinPrincipalName), java.util.Collections.singleton(oldZeppelinPrincipalName));
                                updateListValues(cluster, "livy2-conf", "livy.superusers", java.util.Collections.singleton(newZeppelinPrincipalName), java.util.Collections.singleton(oldZeppelinPrincipalName));
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateListValues(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String propertyName, java.util.Set<java.lang.String> valuesToAdd, java.util.Set<java.lang.String> valuesToRemove) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(configType);
        if (config != null) {
            java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
            if (properties != null) {
                java.lang.String existingValue = properties.get(propertyName);
                java.lang.String newValue = null;
                if (org.apache.commons.lang.StringUtils.isEmpty(existingValue)) {
                    if ((valuesToAdd != null) && (!valuesToAdd.isEmpty())) {
                        newValue = org.apache.commons.lang.StringUtils.join(valuesToAdd, ',');
                    }
                } else {
                    java.util.Set<java.lang.String> valueSet = new java.util.TreeSet<>(java.util.Arrays.asList(existingValue.split("\\s*,\\s*")));
                    boolean removedValues = false;
                    if (valuesToRemove != null) {
                        removedValues = valueSet.removeAll(valuesToRemove);
                    }
                    boolean addedValues = false;
                    if (valuesToAdd != null) {
                        addedValues = valueSet.addAll(valuesToAdd);
                    }
                    if (removedValues || addedValues) {
                        newValue = org.apache.commons.lang.StringUtils.join(valueSet, ',');
                    }
                }
                if (!org.apache.commons.lang.StringUtils.isEmpty(newValue)) {
                    updateConfigurationPropertiesForCluster(cluster, configType, java.util.Collections.singletonMap(propertyName, newValue), true, true);
                }
            }
        }
    }

    private boolean removeConfigurationSpecifications(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer kerberosDescriptorContainer, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> configurations) {
        boolean updated = false;
        if (kerberosDescriptorContainer != null) {
            if (!org.apache.commons.collections.MapUtils.isEmpty(configurations)) {
                for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> entry : configurations.entrySet()) {
                    java.lang.String configType = entry.getKey();
                    for (java.lang.String propertyName : entry.getValue()) {
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurationDescriptors = kerberosDescriptorContainer.getConfigurations(false);
                        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configurationDescriptor = (configurationDescriptors == null) ? null : configurationDescriptors.get(configType);
                        if (configurationDescriptor != null) {
                            java.util.Map<java.lang.String, java.lang.String> properties = configurationDescriptor.getProperties();
                            if ((properties != null) && properties.containsKey(propertyName)) {
                                properties.remove(propertyName);
                                org.apache.ambari.server.upgrade.UpgradeCatalog252.LOG.info("Removed {}/{} from the descriptor named {}", configType, propertyName, kerberosDescriptorContainer.getName());
                                updated = true;
                                if (properties.isEmpty()) {
                                    configurationDescriptors.remove(configType);
                                    kerberosDescriptorContainer.setConfigurations(configurationDescriptors);
                                }
                            }
                        }
                    }
                }
            }
        }
        return updated;
    }
}