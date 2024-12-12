package org.apache.ambari.server.upgrade;
public class UpgradeCatalog276 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog276.class);

    @com.google.inject.Inject
    public UpgradeCatalog276(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.7.5";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.7.6";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.upgrade.UpgradeCatalog276.LOG.debug("UpgradeCatalog276 executing DML Updates.");
        fixNativeLibrariesPathsForMR2AndTez();
    }

    protected void fixNativeLibrariesPathsForMR2AndTez() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.lang.String oldNativePath = "/usr/hdp/${hdp.version}/hadoop/lib/native:" + "/usr/hdp/${hdp.version}/hadoop/lib/native/Linux-{{architecture}}-64";
            java.lang.String fixedNativePath = "/usr/hdp/current/hadoop-client/lib/native:" + "/usr/hdp/current/hadoop-client/lib/native/Linux-{{architecture}}-64";
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    java.util.Set<java.lang.String> installedServices = cluster.getServices().keySet();
                    if (installedServices.contains("TEZ")) {
                        updateConfigGroupNativePaths("tez-site", new java.util.HashSet<>(java.util.Arrays.asList("tez.am.launch.env", "tez.task.launch.env")), oldNativePath, fixedNativePath, cluster);
                    }
                    if (installedServices.contains("MAPREDUCE2")) {
                        updateConfigGroupNativePaths("mapred-site", new java.util.HashSet<>(java.util.Arrays.asList("mapreduce.admin.user.env")), oldNativePath, fixedNativePath, cluster);
                    }
                }
            }
        }
    }

    private void updateConfigGroupNativePaths(java.lang.String configGroupName, java.util.Set<java.lang.String> configsToChange, java.lang.String replaceFrom, java.lang.String replaceTo, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config targetConfig = cluster.getDesiredConfigByType(configGroupName);
        if (targetConfig != null) {
            java.util.Map<java.lang.String, java.lang.String> newProperty = new java.util.HashMap<>();
            for (java.lang.String configName : configsToChange) {
                java.lang.String configValue = targetConfig.getProperties().get(configName);
                if ((configValue != null) && configValue.contains(replaceFrom)) {
                    java.lang.String newConfigValue = configValue.replace(replaceFrom, replaceTo);
                    org.apache.ambari.server.upgrade.UpgradeCatalog276.LOG.info("Native path will be updated for '{}' property of '{}' config type, from '{}' to '{}'", configName, configGroupName, configValue, newConfigValue);
                    newProperty.put(configName, newConfigValue);
                }
            }
            if (!newProperty.isEmpty()) {
                updateConfigurationPropertiesForCluster(cluster, configGroupName, newProperty, true, false);
            }
        }
    }
}