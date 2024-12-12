package org.apache.ambari.server.serveraction.upgrades;
public class PluginUpgradeServerAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.class);

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String clusterName = getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = m_clusters.getCluster(clusterName);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = upgradeContext.getUpgradePack();
        org.apache.ambari.server.state.StackId stackId = upgradePack.getOwnerStackId();
        org.apache.ambari.server.state.StackInfo stackInfo = m_metainfoProvider.get().getStack(stackId);
        java.lang.ClassLoader pluginClassLoader = stackInfo.getLibraryClassLoader();
        if (null == pluginClassLoader) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "There is no plugin classloader defined for stack " + stackId);
        }
        final org.apache.ambari.spi.upgrade.UpgradeAction upgradeAction;
        final java.lang.String pluginClassName = getActionClassName();
        try {
            upgradeAction = stackInfo.getLibraryInstance(pluginClassName);
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.LOG.error("Unable to load the upgrade action {}", pluginClassName, exception);
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "Unable to load the upgrade class  " + pluginClassName);
        }
        java.lang.String standardOutput;
        try {
            org.apache.ambari.spi.ClusterInformation clusterInformation = cluster.buildClusterInformation();
            org.apache.ambari.spi.upgrade.UpgradeActionOperations upgradeActionOperations = upgradeAction.getOperations(clusterInformation, upgradeContext.buildUpgradeInformation());
            changeConfigurations(cluster, upgradeActionOperations.getConfigurationChanges(), upgradeContext);
            removeConfigurationTypes(cluster, upgradeActionOperations.getConfigurationTypeRemovals());
            standardOutput = "Successfully executed " + pluginClassName;
            if (null != upgradeActionOperations.getStandardOutput()) {
                standardOutput = upgradeActionOperations.getStandardOutput();
            }
        } catch (org.apache.ambari.spi.exceptions.UpgradeActionException exception) {
            org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.LOG.error("Unable to run the upgrade action {}", pluginClassName, exception);
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", exception.getMessage());
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.LOG.error("Unable to run the upgrade action {}", pluginClassName, exception);
            java.lang.String standardError = "Unable to run " + pluginClassName;
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", standardError);
        }
        agentConfigsHolder.updateData(cluster.getClusterId(), cluster.getHosts().stream().map(org.apache.ambari.server.state.Host::getHostId).collect(java.util.stream.Collectors.toList()));
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", standardOutput, "");
    }

    private void changeConfigurations(org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges> configurationChanges, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        if (null == configurationChanges) {
            return;
        }
        for (org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges configTypeChanges : configurationChanges) {
            java.lang.String configType = configTypeChanges.getConfigType();
            org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(configType);
            if (null == config) {
                if (configTypeChanges.isOnlyRemovals()) {
                    continue;
                }
                org.apache.ambari.server.stack.upgrade.Direction direction = upgradeContext.getDirection();
                java.lang.String serviceVersionNote = java.lang.String.format("%s %s %s", direction.getText(true), direction.getPreposition(), upgradeContext.getRepositoryVersion().getVersion());
                m_configHelper.createConfigType(cluster, upgradeContext.getRepositoryVersion().getStackId(), m_amc, configType, new java.util.HashMap<>(), m_amc.getAuthName(), serviceVersionNote);
                config = cluster.getDesiredConfigByType(configType);
                if (null == config) {
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to create the % configuration type", configType));
                }
            }
            java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange> propertyChanges = configTypeChanges.getPropertyChanges();
            for (org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange propertyChange : propertyChanges) {
                org.apache.ambari.spi.upgrade.UpgradeActionOperations.ChangeType changeType = propertyChange.getChangeType();
                switch (changeType) {
                    case REMOVE :
                        config.deleteProperties(java.util.Collections.singletonList(propertyChange.getPropertyName()));
                        break;
                    case SET :
                        java.util.Map<java.lang.String, java.lang.String> propertyMap = new java.util.HashMap<>();
                        propertyMap.put(propertyChange.getPropertyName(), propertyChange.getPropertyValue());
                        config.updateProperties(propertyMap);
                        break;
                    default :
                        org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.LOG.error("Unknown configuration action type {}", changeType);
                        throw new org.apache.ambari.server.AmbariException(("Unable to update configurations because " + changeType) + " is an unknown type");
                }
            }
            config.save();
        }
    }

    private void removeConfigurationTypes(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> configurationTypeRemovals) throws org.apache.ambari.server.AmbariException {
        if (null == configurationTypeRemovals) {
            return;
        }
        for (java.lang.String configType : configurationTypeRemovals) {
            m_configHelper.removeConfigsByType(cluster, configType);
        }
    }

    private java.lang.String getActionClassName() throws org.apache.ambari.server.AmbariException {
        java.lang.String wrappedClassName = getCommandParameterValue(org.apache.ambari.server.serveraction.ServerAction.WRAPPED_CLASS_NAME);
        if (null == wrappedClassName) {
            throw new org.apache.ambari.server.AmbariException("The name of the upgrade action class to execute was not found.");
        }
        return wrappedClassName;
    }
}