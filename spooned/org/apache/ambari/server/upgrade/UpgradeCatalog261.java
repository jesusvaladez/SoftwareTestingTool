package org.apache.ambari.server.upgrade;
public class UpgradeCatalog261 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final java.lang.String CORE_SITE = "core-site";

    private static final java.lang.String COMPRESSION_CODECS_PROPERTY = "io.compression.codecs";

    private static final java.lang.String COMPRESSION_CODECS_LZO = "com.hadoop.compression.lzo";

    private static final java.lang.String LZO_ENABLED_JSON_KEY = "lzo_enabled";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog261.class);

    @com.google.inject.Inject
    public UpgradeCatalog261(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.6.0";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.6.1";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        this.getUpgradeJsonOutput().put(org.apache.ambari.server.upgrade.UpgradeCatalog261.LZO_ENABLED_JSON_KEY, isLzoEnabled().toString());
    }

    protected java.lang.Boolean isLzoEnabled() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config coreSite = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.UpgradeCatalog261.CORE_SITE);
                    if (coreSite != null) {
                        java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = coreSite.getProperties();
                        if (coreSiteProperties.containsKey(org.apache.ambari.server.upgrade.UpgradeCatalog261.COMPRESSION_CODECS_PROPERTY)) {
                            java.lang.String compressionCodecs = coreSiteProperties.get(org.apache.ambari.server.upgrade.UpgradeCatalog261.COMPRESSION_CODECS_PROPERTY);
                            if (compressionCodecs.contains(org.apache.ambari.server.upgrade.UpgradeCatalog261.COMPRESSION_CODECS_LZO)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}