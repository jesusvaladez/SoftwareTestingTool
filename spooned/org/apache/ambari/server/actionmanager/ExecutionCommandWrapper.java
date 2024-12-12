package org.apache.ambari.server.actionmanager;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION;
public class ExecutionCommandWrapper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.ExecutionCommandWrapper.class);

    java.lang.String jsonExecutionCommand = null;

    org.apache.ambari.server.agent.ExecutionCommand executionCommand = null;

    @com.google.inject.Inject
    org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory upgradeContextFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repoVersionHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.assistedinject.AssistedInject
    public ExecutionCommandWrapper(@com.google.inject.assistedinject.Assisted
    java.lang.String jsonExecutionCommand) {
        this.jsonExecutionCommand = jsonExecutionCommand;
    }

    @com.google.inject.assistedinject.AssistedInject
    public ExecutionCommandWrapper(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.agent.ExecutionCommand executionCommand) {
        this.executionCommand = executionCommand;
    }

    public org.apache.ambari.server.agent.ExecutionCommand getExecutionCommand() {
        if (executionCommand != null) {
            return executionCommand;
        }
        if (null == jsonExecutionCommand) {
            throw new java.lang.RuntimeException("Invalid ExecutionCommandWrapper, both object and string representations are null");
        }
        try {
            executionCommand = gson.fromJson(jsonExecutionCommand, org.apache.ambari.server.agent.ExecutionCommand.class);
            if (null == executionCommand.getConfigurations()) {
                executionCommand.setConfigurations(new java.util.TreeMap<>());
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = executionCommand.getConfigurations();
            java.lang.Long clusterId = hostRoleCommandDAO.findByPK(executionCommand.getTaskId()).getStage().getClusterId();
            org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(clusterId);
            boolean overrideConfigs = executionCommand.isOverrideConfigs();
            if (overrideConfigs) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationTags = configHelper.getEffectiveDesiredTags(cluster, executionCommand.getHostname(), desiredConfigs);
                org.apache.ambari.server.actionmanager.ExecutionCommandWrapper.LOG.debug("While scheduling task {} on cluster {}, configurations are being refreshed using desired configurations of {}", executionCommand.getTaskId(), cluster.getClusterName(), desiredConfigs);
                configurations = configHelper.getEffectiveConfigProperties(cluster, configurationTags);
                executionCommand.setConfigurations(configurations);
            }
            org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = cluster.getUpgradeInProgress();
            if (null != upgrade) {
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = upgradeContextFactory.create(cluster, upgrade);
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary upgradeSummary = upgradeContext.getUpgradeSummary();
                executionCommand.setUpgradeSummary(upgradeSummary);
            }
            final org.apache.ambari.server.state.Host host = cluster.getHost(executionCommand.getHostname());
            final java.lang.String serviceName = executionCommand.getServiceName();
            org.apache.ambari.server.agent.CommandRepository commandRepository = executionCommand.getRepositoryFile();
            if (((null == commandRepository) && (null != host)) && (null != serviceName)) {
                commandRepository = repoVersionHelper.getCommandRepository(cluster, cluster.getService(serviceName), host, executionCommand.getComponentName());
            }
            setVersions(cluster, commandRepository);
            executionCommand.setRepositoryFile(commandRepository);
        } catch (org.apache.ambari.server.ClusterNotFoundException cnfe) {
            org.apache.ambari.server.actionmanager.ExecutionCommandWrapper.LOG.warn("Unable to lookup the cluster by ID; assuming that there is no cluster and therefore no configs for this execution command: {}", cnfe.getMessage());
            return executionCommand;
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(e);
        }
        return executionCommand;
    }

    public void setVersions(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.agent.CommandRepository commandRepository) {
        java.lang.String serviceName = executionCommand.getServiceName();
        try {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = null;
            if (!org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                if (null != service) {
                    repositoryVersion = service.getDesiredRepositoryVersion();
                    java.lang.String componentName = executionCommand.getComponentName();
                    if (!org.apache.commons.lang.StringUtils.isEmpty(componentName)) {
                        org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(componentName);
                        if (null != serviceComponent) {
                            repositoryVersion = serviceComponent.getDesiredRepositoryVersion();
                        }
                    }
                }
            }
            java.util.Map<java.lang.String, java.lang.String> commandParams = executionCommand.getCommandParams();
            if (null != repositoryVersion) {
                if (((!commandParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION)) && repositoryVersion.isResolved()) && (executionCommand.getRoleCommand() != org.apache.ambari.server.RoleCommand.INSTALL)) {
                    commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION, repositoryVersion.getVersion());
                }
                if (((null != commandRepository) && repositoryVersion.isResolved()) && (!repositoryVersion.getVersion().equals(commandRepository.getRepoVersion()))) {
                    commandRepository.setRepoVersion(repositoryVersion.getVersion());
                    commandRepository.setResolved(true);
                }
                if (!commandParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER)) {
                    commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER, configuration.getProperty(org.apache.ambari.server.configuration.Configuration.HOOKS_FOLDER));
                }
            }
            executionCommand.setComponentVersions(cluster);
        } catch (org.apache.ambari.server.ServiceNotFoundException serviceNotFoundException) {
            org.apache.ambari.server.actionmanager.ExecutionCommandWrapper.LOG.warn("The service {} is not installed in the cluster. No repository version will be sent for this command.", serviceName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public org.apache.ambari.server.agent.AgentCommand.AgentCommandType getCommandType() {
        if (executionCommand != null) {
            return executionCommand.getCommandType();
        }
        if (null == jsonExecutionCommand) {
            throw new java.lang.RuntimeException("Invalid ExecutionCommandWrapper, both object and string" + " representations are null");
        }
        return gson.fromJson(jsonExecutionCommand, org.apache.ambari.server.agent.ExecutionCommand.class).getCommandType();
    }

    public java.lang.String getJson() {
        if (jsonExecutionCommand != null) {
            return jsonExecutionCommand;
        } else if (executionCommand != null) {
            jsonExecutionCommand = gson.toJson(executionCommand);
            return jsonExecutionCommand;
        } else {
            throw new java.lang.RuntimeException("Invalid ExecutionCommandWrapper, both object and string" + " representations are null");
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = ((org.apache.ambari.server.actionmanager.ExecutionCommandWrapper) (o));
        if ((executionCommand != null) && (wrapper.executionCommand != null)) {
            return executionCommand.equals(wrapper.executionCommand);
        } else {
            return getJson().equals(wrapper.getJson());
        }
    }

    @java.lang.Override
    public int hashCode() {
        if (executionCommand != null) {
            return executionCommand.hashCode();
        } else if (jsonExecutionCommand != null) {
            return jsonExecutionCommand.hashCode();
        }
        throw new java.lang.RuntimeException("Invalid Wrapper object");
    }

    void invalidateJson() {
        if (executionCommand == null) {
            throw new java.lang.RuntimeException("Invalid Wrapper object");
        }
        jsonExecutionCommand = null;
    }
}