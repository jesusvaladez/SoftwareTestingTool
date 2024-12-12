package org.apache.ambari.server.serveraction.upgrades;
import org.apache.commons.lang.StringUtils;
public class CreateAndConfigureAction extends org.apache.ambari.server.serveraction.upgrades.ConfigureAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.upgrades.CreateAndConfigureAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters m_clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController m_controller;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper m_configHelper;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.serveraction.upgrades.CreateAndConfigureAction.LOG.info("Create and Configure...");
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        if ((null == commandParameters) || commandParameters.isEmpty()) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "Unable to change configuration values without command parameters");
        }
        java.lang.String clusterName = commandParameters.get("clusterName");
        org.apache.ambari.server.state.Cluster cluster = m_clusters.getCluster(clusterName);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        org.apache.ambari.server.stack.upgrade.Direction direction = upgradeContext.getDirection();
        if (direction == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", "", "Skip changing configuration values for downgrade");
        }
        java.lang.String configType = commandParameters.get(org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask.PARAMETER_CONFIG_TYPE);
        java.lang.String serviceName = cluster.getServiceByConfigType(configType);
        if (org.apache.commons.lang.StringUtils.isBlank(serviceName)) {
            serviceName = commandParameters.get(org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask.PARAMETER_ASSOCIATED_SERVICE);
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepoVersion = upgradeContext.getSourceRepositoryVersion(serviceName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepoVersion = upgradeContext.getTargetRepositoryVersion(serviceName);
        org.apache.ambari.server.state.StackId sourceStackId = sourceRepoVersion.getStackId();
        org.apache.ambari.server.state.StackId targetStackId = targetRepoVersion.getStackId();
        if (!sourceStackId.equals(targetStackId)) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "Unable to change configuration values across stacks. Use regular config task type instead.");
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        if (desiredConfig == null) {
            org.apache.ambari.server.serveraction.upgrades.CreateAndConfigureAction.LOG.info(java.lang.String.format("Could not find desired config type with name %s. Create it with default values.", configType));
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> newServiceDefaultConfigsByType = m_configHelper.getDefaultProperties(targetStackId, serviceName);
            if (!newServiceDefaultConfigsByType.containsKey(configType)) {
                java.lang.String error = java.lang.String.format("%s in %s does not contain configuration type %s", serviceName, targetStackId.getStackId(), configType);
                org.apache.ambari.server.serveraction.upgrades.CreateAndConfigureAction.LOG.error(error);
                return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", error);
            }
            java.util.Map<java.lang.String, java.lang.String> defaultConfigsForType = newServiceDefaultConfigsByType.get(configType);
            java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.String>> iter = defaultConfigsForType.entrySet().iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<java.lang.String, java.lang.String> entry = iter.next();
                if (entry.getValue() == null) {
                    iter.remove();
                }
            } 
            java.lang.String serviceVersionNote = java.lang.String.format("%s %s %s", direction.getText(true), direction.getPreposition(), upgradeContext.getRepositoryVersion().getVersion());
            m_configHelper.createConfigType(cluster, targetStackId, m_controller, configType, defaultConfigsForType, m_controller.getAuthName(), serviceVersionNote);
        }
        return super.execute(requestSharedDataContext);
    }
}