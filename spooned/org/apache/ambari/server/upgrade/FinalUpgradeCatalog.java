package org.apache.ambari.server.upgrade;
public class FinalUpgradeCatalog extends org.apache.ambari.server.upgrade.AbstractFinalUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.FinalUpgradeCatalog.class);

    @com.google.inject.Inject
    public FinalUpgradeCatalog(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        updateClusterEnv();
    }

    protected void updateClusterEnv() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.upgrade.FinalUpgradeCatalog.LOG.info("Updating stack_features and stack_tools config properties.");
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
        for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            java.util.Set<org.apache.ambari.server.state.StackId> stackIds = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
                stackIds.add(service.getDesiredStackId());
            }
            for (org.apache.ambari.server.state.StackId stackId : stackIds) {
                java.util.Map<java.lang.String, java.lang.String> propertyMap = new java.util.HashMap<>();
                org.apache.ambari.server.state.StackInfo stackInfo = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
                java.util.List<org.apache.ambari.server.state.PropertyInfo> properties = stackInfo.getProperties();
                for (org.apache.ambari.server.state.PropertyInfo property : properties) {
                    if ((property.getName().equals(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_FEATURES_PROPERTY) || property.getName().equals(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_TOOLS_PROPERTY)) || property.getName().equals(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_PACKAGES_PROPERTY)) {
                        propertyMap.put(property.getName(), property.getValue());
                    }
                }
                updateConfigurationPropertiesForCluster(cluster, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, propertyMap, true, true);
            }
        }
    }
}