package org.apache.ambari.server.controller;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_COUNT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_ON_UNAVAILABILITY;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLIENTS_TO_UPDATE_CONFIGS;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMPONENT_CATEGORY;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CUSTOM_COMMAND;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_DRIVER_FILENAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GPL_LICENSE_ACCEPTED;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GROUP_LIST;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOST_SYS_PREPPED;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MYSQL_JDBC_URL;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.ORACLE_JDBC_URL;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_LIST;
import static org.apache.ambari.server.controller.internal.RequestResourceProvider.HAS_RESOURCE_FILTERS;
@com.google.inject.Singleton
public class AmbariCustomCommandExecutionHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);

    static final java.util.Map<java.lang.String, java.lang.String> masterToSlaveMappingForDecom = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("NAMENODE", "DATANODE").put("RESOURCEMANAGER", "NODEMANAGER").put("HBASE_MASTER", "HBASE_REGIONSERVER").put("JOBTRACKER", "TASKTRACKER").build();

    public static final java.lang.String DECOM_INCLUDED_HOSTS = "included_hosts";

    public static final java.lang.String DECOM_EXCLUDED_HOSTS = "excluded_hosts";

    public static final java.lang.String ALL_DECOMMISSIONED_HOSTS = "all_decommissioned_hosts";

    public static final java.lang.String DECOM_SLAVE_COMPONENT = "slave_type";

    public static final java.lang.String HBASE_MARK_DRAINING_ONLY = "mark_draining_only";

    public static final java.lang.String UPDATE_FILES_ONLY = "update_files_only";

    public static final java.lang.String IS_ADD_OR_DELETE_SLAVE_REQUEST = "is_add_or_delete_slave_request";

    private static final java.lang.String ALIGN_MAINTENANCE_STATE = "align_maintenance_state";

    public static final int MIN_STRICT_SERVICE_CHECK_TIMEOUT = 120;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.ActionMetadata actionMetadata;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController managementController;

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configs;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configCredentialsForService = new java.util.HashMap<>();

    protected static final java.lang.String SERVICE_CHECK_COMMAND_NAME = "SERVICE_CHECK";

    protected static final java.lang.String START_COMMAND_NAME = "START";

    protected static final java.lang.String RESTART_COMMAND_NAME = "RESTART";

    protected static final java.lang.String INSTALL_COMMAND_NAME = "INSTALL";

    public static final java.lang.String DECOMMISSION_COMMAND_NAME = "DECOMMISSION";

    private java.lang.Boolean isServiceCheckCommand(java.lang.String command, java.lang.String service) {
        java.util.List<java.lang.String> actions = actionMetadata.getActions(service);
        return (!((actions == null) || (actions.size() == 0))) && actions.contains(command);
    }

    private java.lang.Boolean isValidCustomCommand(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String commandName) throws org.apache.ambari.server.AmbariException {
        if (componentName == null) {
            return false;
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(componentName);
        org.apache.ambari.server.state.StackId stackId = component.getDesiredStackId();
        org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
        return !((!componentInfo.isCustomCommand(commandName)) && (!actionMetadata.isDefaultHostComponentCommand(commandName)));
    }

    private java.lang.Boolean isValidCustomCommand(org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter) throws org.apache.ambari.server.AmbariException {
        if (actionExecutionContext.isFutureCommand()) {
            return true;
        }
        java.lang.String clusterName = actionExecutionContext.getClusterName();
        java.lang.String serviceName = resourceFilter.getServiceName();
        java.lang.String componentName = resourceFilter.getComponentName();
        java.lang.String commandName = actionExecutionContext.getActionName();
        if (componentName == null) {
            return false;
        }
        return isValidCustomCommand(clusterName, serviceName, componentName, commandName);
    }

    private java.lang.Boolean isValidCustomCommand(org.apache.ambari.server.controller.ExecuteActionRequest actionRequest, org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter) throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = actionRequest.getClusterName();
        java.lang.String serviceName = resourceFilter.getServiceName();
        java.lang.String componentName = resourceFilter.getComponentName();
        java.lang.String commandName = actionRequest.getCommandName();
        if (componentName == null) {
            return false;
        }
        return isValidCustomCommand(clusterName, serviceName, componentName, commandName);
    }

    private java.lang.String getReadableCustomCommandDetail(org.apache.ambari.server.controller.ActionExecutionContext actionRequest, org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(actionRequest.getActionName());
        if ((resourceFilter.getServiceName() != null) && (!resourceFilter.getServiceName().equals(""))) {
            sb.append(" ");
            sb.append(resourceFilter.getServiceName());
        }
        if ((resourceFilter.getComponentName() != null) && (!resourceFilter.getComponentName().equals(""))) {
            sb.append("/");
            sb.append(resourceFilter.getComponentName());
        }
        return sb.toString();
    }

    private void addCustomCommandAction(final org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, final org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter, org.apache.ambari.server.actionmanager.Stage stage, java.util.Map<java.lang.String, java.lang.String> additionalCommandParams, java.lang.String commandDetail, java.util.Map<java.lang.String, java.lang.String> requestParams) throws org.apache.ambari.server.AmbariException {
        final java.lang.String serviceName = resourceFilter.getServiceName();
        final java.lang.String componentName = resourceFilter.getComponentName();
        final java.lang.String commandName = actionExecutionContext.getActionName();
        boolean retryAllowed = actionExecutionContext.isRetryAllowed();
        boolean autoSkipFailure = actionExecutionContext.isFailureAutoSkipped();
        java.lang.String clusterName = stage.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        java.util.Set<java.lang.String> candidateHosts = new java.util.HashSet<>(resourceFilter.getHostNames());
        java.util.Set<java.lang.String> ignoredHosts = maintenanceStateHelper.filterHostsInMaintenanceState(candidateHosts, new org.apache.ambari.server.controller.MaintenanceStateHelper.HostPredicate() {
            @java.lang.Override
            public boolean shouldHostBeRemoved(final java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
                if (actionExecutionContext.isFutureCommand()) {
                    return false;
                }
                return !maintenanceStateHelper.isOperationAllowed(cluster, actionExecutionContext.getOperationLevel(), resourceFilter, serviceName, componentName, hostname);
            }
        });
        java.util.Set<java.lang.String> unhealthyHosts = getUnhealthyHosts(candidateHosts, actionExecutionContext, resourceFilter);
        if (!ignoredHosts.isEmpty()) {
            if (org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug("While building the {} custom command for {}/{}, the following hosts were excluded: unhealthy[{}], maintenance[{}]", commandName, serviceName, componentName, org.apache.commons.lang.StringUtils.join(unhealthyHosts, ','), org.apache.commons.lang.StringUtils.join(ignoredHosts, ','));
            }
        } else if (!unhealthyHosts.isEmpty()) {
            if (org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug("While building the {} custom command for {}/{}, the following hosts were excluded: unhealthy[{}], maintenance[{}]", commandName, serviceName, componentName, org.apache.commons.lang.StringUtils.join(unhealthyHosts, ','), org.apache.commons.lang.StringUtils.join(ignoredHosts, ','));
            }
        } else if (candidateHosts.isEmpty()) {
            java.lang.String message = java.text.MessageFormat.format("While building the {0} custom command for {1}/{2}, there were no healthy eligible hosts", commandName, serviceName, componentName);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
        if (null != actionExecutionContext.getStackId()) {
            stackId = actionExecutionContext.getStackId();
        }
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = managementController.getAmbariMetaInfo();
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(service);
        org.apache.ambari.server.state.CustomCommandDefinition customCommandDefinition = null;
        org.apache.ambari.server.state.ComponentInfo ci = serviceInfo.getComponentByName(componentName);
        if (ci != null) {
            customCommandDefinition = ci.getCustomCommandByName(commandName);
        }
        long nowTimestamp = java.lang.System.currentTimeMillis();
        for (java.lang.String hostName : candidateHosts) {
            stage.addHostRoleExecutionCommand(hostName, org.apache.ambari.server.Role.valueOf(componentName), org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(componentName, hostName, nowTimestamp), cluster.getClusterName(), serviceName, retryAllowed, autoSkipFailure);
            org.apache.ambari.server.agent.ExecutionCommand execCmd = stage.getExecutionCommandWrapper(hostName, componentName).getExecutionCommand();
            if ((actionExecutionContext.getParameters() != null) && actionExecutionContext.getParameters().containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS)) {
                execCmd.setOverrideConfigs(true);
            }
            org.apache.ambari.server.actionmanager.HostRoleCommand cmd = stage.getHostRoleCommand(hostName, componentName);
            if (cmd != null) {
                cmd.setCommandDetail(commandDetail);
                cmd.setCustomCommandName(commandName);
                if (customCommandDefinition != null) {
                    cmd.setOpsDisplayName(customCommandDefinition.getOpsDisplayName());
                }
            }
            if ((customCommandDefinition != null) && customCommandDefinition.isBackground()) {
                cmd.setBackgroundCommand(true);
                execCmd.setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.BACKGROUND_EXECUTION_COMMAND);
            }
            execCmd.setComponentVersions(cluster);
            execCmd.setConfigurations(new java.util.TreeMap<>());
            org.apache.ambari.server.state.Service clusterService = cluster.getService(serviceName);
            execCmd.setCredentialStoreEnabled(java.lang.String.valueOf(clusterService.isCredentialStoreEnabled()));
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configCredentials;
            configCredentials = configCredentialsForService.get(clusterService.getName());
            if (configCredentials == null) {
                configCredentials = configHelper.getCredentialStoreEnabledProperties(stackId, clusterService);
                configCredentialsForService.put(clusterService.getName(), configCredentials);
            }
            execCmd.setConfigurationCredentials(configCredentials);
            java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.TreeMap<>();
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME, stackId.getStackName());
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION, stackId.getStackVersion());
            java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
            java.util.Set<java.lang.String> userSet = configHelper.getPropertyValuesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.USER, cluster, desiredConfigs);
            java.lang.String userList = gson.toJson(userSet);
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_LIST, userList);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap = configHelper.createUserGroupsMap(stackId, cluster, desiredConfigs);
            java.lang.String userGroups = gson.toJson(userGroupsMap);
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS, userGroups);
            java.util.Set<java.lang.String> groupSet = configHelper.getPropertyValuesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.GROUP, cluster, desiredConfigs);
            java.lang.String groupList = gson.toJson(groupSet);
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GROUP_LIST, groupList);
            java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> notManagedHdfsPathMap = configHelper.getPropertiesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.NOT_MANAGED_HDFS_PATH, cluster, desiredConfigs);
            java.util.Set<java.lang.String> notManagedHdfsPathSet = configHelper.filterInvalidPropertyValues(notManagedHdfsPathMap, org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST);
            java.lang.String notManagedHdfsPathList = gson.toJson(notManagedHdfsPathSet);
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST, notManagedHdfsPathList);
            execCmd.setHostLevelParams(hostLevelParams);
            java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();
            if (additionalCommandParams != null) {
                for (java.lang.String key : additionalCommandParams.keySet()) {
                    commandParams.put(key, additionalCommandParams.get(key));
                }
            }
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CUSTOM_COMMAND, commandName);
            boolean isInstallCommand = commandName.equals(org.apache.ambari.server.RoleCommand.INSTALL.toString());
            int commandTimeout = java.lang.Integer.valueOf(configs.getDefaultAgentTaskTimeout(isInstallCommand));
            org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
            if (serviceInfo.getSchemaVersion().equals(org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2)) {
                org.apache.ambari.server.state.CommandScriptDefinition script = componentInfo.getCommandScript();
                if (script != null) {
                    commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT, script.getScript());
                    commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE, script.getScriptType().toString());
                    if (script.getTimeout() > 0) {
                        commandTimeout = script.getTimeout();
                    }
                } else {
                    java.lang.String message = java.lang.String.format("Component %s has not command script " + ("defined. It is not possible to send command for " + "this service"), componentName);
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            }
            if (null != actionExecutionContext.getTimeout()) {
                commandTimeout = actionExecutionContext.getTimeout().intValue();
                commandTimeout = java.lang.Math.max(60, commandTimeout);
            }
            if ((requestParams != null) && requestParams.containsKey(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT)) {
                java.lang.String requestContext = requestParams.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT);
                if (org.apache.commons.lang.StringUtils.isNotEmpty(requestContext) && requestContext.toLowerCase().contains("rolling-restart")) {
                    org.apache.ambari.server.state.Config clusterEnvConfig = cluster.getDesiredConfigByType("cluster-env");
                    if (clusterEnvConfig != null) {
                        java.lang.String componentRollingRestartTimeout = clusterEnvConfig.getProperties().get("namenode_rolling_restart_timeout");
                        if (org.apache.commons.lang.StringUtils.isNotEmpty(componentRollingRestartTimeout)) {
                            commandTimeout = java.lang.Integer.parseInt(componentRollingRestartTimeout);
                        }
                    }
                }
            }
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, "" + commandTimeout);
            java.util.Map<java.lang.String, java.lang.String> roleParams = execCmd.getRoleParams();
            if (roleParams == null) {
                roleParams = new java.util.TreeMap<>();
            }
            boolean isUpgradeSuspended = cluster.isUpgradeSuspended();
            if (isUpgradeSuspended) {
                cluster.addSuspendedUpgradeParameters(commandParams, roleParams);
            }
            org.apache.ambari.server.utils.StageUtils.useAmbariJdkInCommandParams(commandParams, configs);
            roleParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMPONENT_CATEGORY, componentInfo.getCategory());
            if (commandName.equals("RECONFIGURE")) {
                java.lang.String refreshConfigsCommand = configHelper.getRefreshConfigsCommand(cluster, hostName, serviceName, componentName);
                if ((refreshConfigsCommand != null) && (!refreshConfigsCommand.equals(org.apache.ambari.server.state.RefreshCommandConfiguration.REFRESH_CONFIGS))) {
                    org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.info("Refreshing configs for {}/{} with command: ", componentName, hostName, refreshConfigsCommand);
                    commandParams.put("reconfigureAction", refreshConfigsCommand);
                }
            }
            execCmd.setCommandParams(commandParams);
            execCmd.setRoleParams(roleParams);
            if (actionExecutionContext.isFutureCommand()) {
                continue;
            }
            applyCustomCommandBackendLogic(cluster, serviceName, componentName, commandName, hostName);
        }
    }

    private void applyCustomCommandBackendLogic(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, java.lang.String componentName, java.lang.String commandName, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        switch (commandName) {
            case "RESTART" :
                org.apache.ambari.server.state.ServiceComponent serviceComponent = cluster.getService(serviceName).getServiceComponent(componentName);
                org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = serviceComponent.getServiceComponentHost(hostname);
                org.apache.ambari.server.state.State currentDesiredState = serviceComponentHost.getDesiredState();
                if (!serviceComponent.isClientComponent()) {
                    if (currentDesiredState != org.apache.ambari.server.state.State.STARTED) {
                        org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.info("Updating desired state to {} on RESTART for {}/{} because it was {}", org.apache.ambari.server.state.State.STARTED, serviceName, componentName, currentDesiredState);
                        serviceComponentHost.setDesiredState(org.apache.ambari.server.state.State.STARTED);
                    }
                } else {
                    org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug("Desired state for client components should not be updated on RESTART. Service/Component {}/{}", serviceName, componentName);
                }
                break;
            default :
                org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug("No backend operations needed for the custom command: {}", commandName);
                break;
        }
    }

    private void findHostAndAddServiceCheckAction(final org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, final org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter, org.apache.ambari.server.actionmanager.Stage stage) throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = actionExecutionContext.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        final java.lang.String componentName = actionMetadata.getClient(resourceFilter.getServiceName());
        final java.lang.String serviceName = resourceFilter.getServiceName();
        java.lang.String smokeTestRole = actionMetadata.getServiceCheckAction(serviceName);
        if (null == smokeTestRole) {
            smokeTestRole = actionExecutionContext.getActionName();
        }
        java.util.Set<java.lang.String> candidateHosts;
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> serviceHostComponents;
        if (componentName != null) {
            serviceHostComponents = cluster.getService(serviceName).getServiceComponent(componentName).getServiceComponentHosts();
            if (serviceHostComponents.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("No hosts found for service: {0}, component: {1} in cluster: {2}", serviceName, componentName, clusterName));
            }
            java.util.List<java.lang.String> candidateHostsList = resourceFilter.getHostNames();
            if ((candidateHostsList != null) && (!candidateHostsList.isEmpty())) {
                candidateHosts = new java.util.HashSet<>(candidateHostsList);
                candidateHosts.retainAll(serviceHostComponents.keySet());
                if (candidateHosts.isEmpty()) {
                    throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("The resource filter for hosts does not contain components for " + "service: {0}, component: {1} in cluster: {2}", serviceName, componentName, clusterName));
                }
            } else {
                candidateHosts = serviceHostComponents.keySet();
            }
        } else {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = cluster.getService(serviceName).getServiceComponents();
            java.util.Iterator<java.lang.String> serviceComponentNameIterator = serviceComponents.keySet().iterator();
            while (serviceComponentNameIterator.hasNext()) {
                java.lang.String componentToCheck = serviceComponentNameIterator.next();
                if (serviceComponents.get(componentToCheck).getServiceComponentHosts().isEmpty()) {
                    serviceComponentNameIterator.remove();
                }
            } 
            if (serviceComponents.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("Did not find any hosts with components for service: {0} in cluster: {1}", serviceName, clusterName));
            }
            org.apache.ambari.server.state.ServiceComponent serviceComponent = serviceComponents.values().iterator().next();
            serviceHostComponents = serviceComponent.getServiceComponentHosts();
            candidateHosts = serviceHostComponents.keySet();
        }
        for (java.lang.String candidateHostName : candidateHosts) {
            org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = serviceHostComponents.get(candidateHostName);
            if (serviceComponentHost == null) {
                throw new org.apache.ambari.server.AmbariException(("Provided hostname = " + candidateHostName) + " is either not a valid cluster host or does not satisfy the filter condition.");
            }
        }
        java.util.Set<java.lang.String> hostsInMaintenanceMode = new java.util.HashSet<>();
        if (actionExecutionContext.isMaintenanceModeHostExcluded()) {
            java.util.Iterator<java.lang.String> iterator = candidateHosts.iterator();
            while (iterator.hasNext()) {
                java.lang.String candidateHostName = iterator.next();
                org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = serviceHostComponents.get(candidateHostName);
                org.apache.ambari.server.state.Host host = serviceComponentHost.getHost();
                if (host.getMaintenanceState(cluster.getClusterId()) == org.apache.ambari.server.state.MaintenanceState.ON) {
                    hostsInMaintenanceMode.add(candidateHostName);
                    iterator.remove();
                }
            } 
        }
        java.util.List<java.lang.String> healthyHostNames = managementController.selectHealthyHosts(candidateHosts);
        if (healthyHostNames.isEmpty()) {
            java.lang.String message = java.text.MessageFormat.format("While building a service check command for {0}, there were no healthy eligible hosts: unhealthy[{1}], maintenance[{2}]", serviceName, org.apache.commons.lang.StringUtils.join(candidateHosts, ','), org.apache.commons.lang.StringUtils.join(hostsInMaintenanceMode, ','));
            throw new org.apache.ambari.server.AmbariException(message);
        }
        java.lang.String preferredHostName = selectRandomHostNameWithPreferenceOnAvailability(healthyHostNames);
        long nowTimestamp = java.lang.System.currentTimeMillis();
        java.util.Map<java.lang.String, java.lang.String> actionParameters = actionExecutionContext.getParameters();
        addServiceCheckAction(stage, preferredHostName, smokeTestRole, nowTimestamp, serviceName, componentName, actionParameters, actionExecutionContext.isRetryAllowed(), actionExecutionContext.isFailureAutoSkipped(), false);
    }

    private java.lang.String selectRandomHostNameWithPreferenceOnAvailability(java.util.List<java.lang.String> candidateHostNames) throws org.apache.ambari.server.AmbariException {
        if ((null == candidateHostNames) || candidateHostNames.isEmpty()) {
            return null;
        }
        if (candidateHostNames.size() == 1) {
            return candidateHostNames.get(0);
        }
        java.util.List<java.lang.String> hostsWithZeroCommands = new java.util.ArrayList<>();
        java.util.List<java.lang.String> hostsWithInProgressCommands = new java.util.ArrayList<>();
        java.util.Map<java.lang.Long, java.lang.Integer> hostIdToCount = hostRoleCommandDAO.getHostIdToCountOfCommandsWithStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES);
        for (java.lang.String hostName : candidateHostNames) {
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            if (hostIdToCount.containsKey(host.getHostId()) && (hostIdToCount.get(host.getHostId()) > 0)) {
                hostsWithInProgressCommands.add(hostName);
            } else {
                hostsWithZeroCommands.add(hostName);
            }
        }
        java.util.List<java.lang.String> preferredList = (!hostsWithZeroCommands.isEmpty()) ? hostsWithZeroCommands : hostsWithInProgressCommands;
        if (!preferredList.isEmpty()) {
            int randomIndex = new java.util.Random().nextInt(preferredList.size());
            return preferredList.get(randomIndex);
        }
        return null;
    }

    public void addServiceCheckAction(org.apache.ambari.server.actionmanager.Stage stage, java.lang.String hostname, java.lang.String smokeTestRole, long nowTimestamp, java.lang.String serviceName, java.lang.String componentName, java.util.Map<java.lang.String, java.lang.String> actionParameters, boolean retryAllowed, boolean autoSkipFailure, boolean useLatestConfigs) throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = stage.getClusterName();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent component = null;
        if (null != componentName) {
            component = service.getServiceComponent(componentName);
        }
        org.apache.ambari.server.state.StackId stackId = (null != component) ? component.getDesiredStackId() : service.getDesiredStackId();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = managementController.getAmbariMetaInfo();
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
        stage.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.valueOf(smokeTestRole), org.apache.ambari.server.RoleCommand.SERVICE_CHECK, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(componentName, hostname, nowTimestamp), cluster.getClusterName(), serviceName, retryAllowed, autoSkipFailure);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = stage.getHostRoleCommand(hostname, smokeTestRole);
        if (hrc != null) {
            hrc.setCommandDetail(java.lang.String.format("%s %s", org.apache.ambari.server.RoleCommand.SERVICE_CHECK.toString(), serviceName));
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.TreeMap<>();
        org.apache.ambari.server.agent.ExecutionCommand execCmd = stage.getExecutionCommandWrapper(hostname, smokeTestRole).getExecutionCommand();
        if ((actionParameters != null) && actionParameters.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS)) {
            execCmd.setOverrideConfigs(true);
        }
        execCmd.setConfigurations(configurations);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(hostname)) {
            execCmd.getLocalComponents().add(sch.getServiceComponentName());
        }
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();
        java.lang.String commandTimeout = getStatusCommandTimeout(serviceInfo);
        if (serviceInfo.getSchemaVersion().equals(org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2)) {
            org.apache.ambari.server.state.CommandScriptDefinition script = serviceInfo.getCommandScript();
            if (script != null) {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT, script.getScript());
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE, script.getScriptType().toString());
            } else {
                java.lang.String message = java.lang.String.format("Service %s has no command script " + ("defined. It is not possible to run service check" + " for this service"), serviceName);
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
        commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, commandTimeout);
        java.lang.String checkType = configHelper.getValueFromDesiredConfigurations(cluster, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, org.apache.ambari.server.state.ConfigHelper.SERVICE_CHECK_TYPE);
        if (org.apache.ambari.server.state.ConfigHelper.SERVICE_CHECK_MINIMAL.equals(checkType)) {
            int actualTimeout = java.lang.Integer.parseInt(commandParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT)) / 2;
            actualTimeout = (actualTimeout < org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.MIN_STRICT_SERVICE_CHECK_TIMEOUT) ? org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.MIN_STRICT_SERVICE_CHECK_TIMEOUT : actualTimeout;
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, java.lang.Integer.toString(actualTimeout));
        }
        org.apache.ambari.server.utils.StageUtils.useAmbariJdkInCommandParams(commandParams, configs);
        execCmd.setCommandParams(commandParams);
        if (actionParameters != null) {
            execCmd.setRoleParams(actionParameters);
        }
        if (useLatestConfigs) {
            execCmd.setUseLatestConfigs(useLatestConfigs);
        }
    }

    private java.util.Set<java.lang.String> getHostList(java.util.Map<java.lang.String, java.lang.String> cmdParameters, java.lang.String key) {
        java.util.Set<java.lang.String> hosts = new java.util.HashSet<>();
        if (cmdParameters.containsKey(key)) {
            java.lang.String allHosts = cmdParameters.get(key);
            if (allHosts != null) {
                for (java.lang.String hostName : allHosts.trim().split(",")) {
                    hosts.add(hostName.trim());
                }
            }
        }
        return hosts;
    }

    private void addDecommissionAction(final org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, final org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter, org.apache.ambari.server.actionmanager.Stage stage, org.apache.ambari.server.controller.ExecuteCommandJson executeCommandJson) throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = actionExecutionContext.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        final java.lang.String serviceName = resourceFilter.getServiceName();
        java.lang.String masterCompType = resourceFilter.getComponentName();
        java.util.List<java.lang.String> hosts = resourceFilter.getHostNames();
        if ((hosts != null) && (!hosts.isEmpty())) {
            throw new org.apache.ambari.server.AmbariException("Decommission command cannot be issued with " + "target host(s) specified.");
        }
        java.util.Set<java.lang.String> excludedHosts = getHostList(actionExecutionContext.getParameters(), org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOM_EXCLUDED_HOSTS);
        java.util.Set<java.lang.String> includedHosts = getHostList(actionExecutionContext.getParameters(), org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOM_INCLUDED_HOSTS);
        if ((actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.IS_ADD_OR_DELETE_SLAVE_REQUEST) != null) && actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.IS_ADD_OR_DELETE_SLAVE_REQUEST).equalsIgnoreCase("true")) {
            includedHosts = getHostList(actionExecutionContext.getParameters(), (masterCompType + "_") + org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOM_INCLUDED_HOSTS);
        }
        java.util.Set<java.lang.String> cloneSet = new java.util.HashSet<>(excludedHosts);
        cloneSet.retainAll(includedHosts);
        if (cloneSet.size() > 0) {
            throw new org.apache.ambari.server.AmbariException(("Same host cannot be specified for inclusion " + "as well as exclusion. Hosts: ") + cloneSet);
        }
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        if (service == null) {
            throw new org.apache.ambari.server.AmbariException(("Specified service " + serviceName) + " is not a valid/deployed service.");
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> svcComponents = service.getServiceComponents();
        if (!svcComponents.containsKey(masterCompType)) {
            throw new org.apache.ambari.server.AmbariException(((("Specified component " + masterCompType) + " does not belong to service ") + serviceName) + ".");
        }
        org.apache.ambari.server.state.ServiceComponent masterComponent = svcComponents.get(masterCompType);
        if (!masterComponent.isMasterComponent()) {
            throw new org.apache.ambari.server.AmbariException(((("Specified component " + masterCompType) + " is not a MASTER for service ") + serviceName) + ".");
        }
        if (!org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom.containsKey(masterCompType)) {
            throw new org.apache.ambari.server.AmbariException("Decommissioning is not supported for " + masterCompType);
        }
        java.lang.String slaveCompStr = actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOM_SLAVE_COMPONENT);
        final java.lang.String slaveCompType;
        if ((slaveCompStr == null) || slaveCompStr.equals("")) {
            slaveCompType = org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom.get(masterCompType);
        } else {
            slaveCompType = slaveCompStr;
            if (!org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.masterToSlaveMappingForDecom.get(masterCompType).equals(slaveCompType)) {
                throw new org.apache.ambari.server.AmbariException(("Component " + slaveCompType) + " is not supported for decommissioning.");
            }
        }
        java.lang.String isDrainOnlyRequest = actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.HBASE_MARK_DRAINING_ONLY);
        if ((isDrainOnlyRequest != null) && (!slaveCompType.equals(org.apache.ambari.server.Role.HBASE_REGIONSERVER.name()))) {
            throw new org.apache.ambari.server.AmbariException((org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.HBASE_MARK_DRAINING_ONLY + " is not a valid parameter for ") + masterCompType);
        }
        org.apache.ambari.server.controller.MaintenanceStateHelper.HostPredicate hostPredicate = new org.apache.ambari.server.controller.MaintenanceStateHelper.HostPredicate() {
            @java.lang.Override
            public boolean shouldHostBeRemoved(final java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
                java.lang.String upd_excl_file_only_str = actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.UPDATE_FILES_ONLY);
                java.lang.String decom_incl_hosts_str = actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOM_INCLUDED_HOSTS);
                if ((upd_excl_file_only_str != null) && (!upd_excl_file_only_str.trim().equals(""))) {
                    upd_excl_file_only_str = upd_excl_file_only_str.trim();
                }
                boolean upd_excl_file_only = false;
                if (((upd_excl_file_only_str != null) && (!upd_excl_file_only_str.equals(""))) && ((upd_excl_file_only_str.equals("\"true\"") || upd_excl_file_only_str.equals("'true'")) || upd_excl_file_only_str.equals("true"))) {
                    upd_excl_file_only = true;
                }
                if ((upd_excl_file_only && (decom_incl_hosts_str != null)) && (!decom_incl_hosts_str.trim().equals(""))) {
                    return upd_excl_file_only;
                } else {
                    return !maintenanceStateHelper.isOperationAllowed(cluster, actionExecutionContext.getOperationLevel(), resourceFilter, serviceName, slaveCompType, hostname);
                }
            }
        };
        java.util.Set<java.lang.String> filteredExcludedHosts = new java.util.HashSet<>(excludedHosts);
        java.util.Set<java.lang.String> ignoredHosts = maintenanceStateHelper.filterHostsInMaintenanceState(filteredExcludedHosts, hostPredicate);
        if (!ignoredHosts.isEmpty()) {
            java.lang.String message = java.lang.String.format("Some hosts (%s) from host exclude list " + ("have been ignored " + "because components on them are in Maintenance state."), ignoredHosts);
            org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug(message);
        }
        java.util.Set<java.lang.String> filteredIncludedHosts = new java.util.HashSet<>(includedHosts);
        ignoredHosts = maintenanceStateHelper.filterHostsInMaintenanceState(filteredIncludedHosts, hostPredicate);
        if (!ignoredHosts.isEmpty()) {
            java.lang.String message = java.lang.String.format("Some hosts (%s) from host include list " + ("have been ignored " + "because components on them are in Maintenance state."), ignoredHosts);
            org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug(message);
        }
        for (org.apache.ambari.server.state.ServiceComponentHost sch : svcComponents.get(slaveCompType).getServiceComponentHosts().values()) {
            if ((filteredExcludedHosts.contains(sch.getHostName()) && (!"true".equals(isDrainOnlyRequest))) && (sch.getState() != org.apache.ambari.server.state.State.STARTED)) {
                throw new org.apache.ambari.server.AmbariException((((("Component " + slaveCompType) + " on host ") + sch.getHostName()) + " cannot be ") + "decommissioned as its not in STARTED state. Aborting the whole request.");
            }
        }
        java.lang.String alignMtnStateStr = actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.ALIGN_MAINTENANCE_STATE);
        boolean alignMtnState = "true".equals(alignMtnStateStr);
        java.util.List<java.lang.String> listOfExcludedHosts = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceComponentHost sch : svcComponents.get(slaveCompType).getServiceComponentHosts().values()) {
            if (filteredExcludedHosts.contains(sch.getHostName())) {
                sch.setComponentAdminState(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED);
                listOfExcludedHosts.add(sch.getHostName());
                if (alignMtnState) {
                    sch.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
                    org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.info("marking Maintenance=ON on " + sch.getHostName());
                }
                org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.info((("Decommissioning " + slaveCompType) + " on ") + sch.getHostName());
            }
            if (filteredIncludedHosts.contains(sch.getHostName())) {
                sch.setComponentAdminState(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE);
                if (alignMtnState) {
                    sch.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
                    org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.info("marking Maintenance=OFF on " + sch.getHostName());
                }
                org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.info((("Recommissioning " + slaveCompType) + " on ") + sch.getHostName());
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> masterSchs = masterComponent.getServiceComponentHosts();
        java.lang.String primaryCandidate = null;
        for (java.lang.String hostName : masterSchs.keySet()) {
            if (primaryCandidate == null) {
                primaryCandidate = hostName;
            } else {
                org.apache.ambari.server.state.ServiceComponentHost sch = masterSchs.get(hostName);
                if (sch.getState() == org.apache.ambari.server.state.State.STARTED) {
                    primaryCandidate = hostName;
                }
            }
        }
        java.lang.StringBuilder commandDetail = getReadableDecommissionCommandDetail(actionExecutionContext, filteredIncludedHosts, listOfExcludedHosts);
        for (java.lang.String hostName : masterSchs.keySet()) {
            org.apache.ambari.server.controller.internal.RequestResourceFilter commandFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(serviceName, masterComponent.getName(), java.util.Collections.singletonList(hostName));
            java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>();
            resourceFilters.add(commandFilter);
            org.apache.ambari.server.controller.ActionExecutionContext commandContext = new org.apache.ambari.server.controller.ActionExecutionContext(clusterName, actionExecutionContext.getActionName(), resourceFilters);
            java.lang.String clusterHostInfoJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster));
            if (executeCommandJson != null) {
                executeCommandJson.setClusterHostInfo(clusterHostInfoJson);
            }
            java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
            commandParams.put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.ALL_DECOMMISSIONED_HOSTS, org.apache.commons.lang.StringUtils.join(calculateDecommissionedNodes(service, slaveCompType), ','));
            if (serviceName.equals(org.apache.ambari.server.state.Service.Type.HBASE.name())) {
                commandParams.put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOM_EXCLUDED_HOSTS, org.apache.commons.lang.StringUtils.join(listOfExcludedHosts, ','));
                if ((isDrainOnlyRequest != null) && isDrainOnlyRequest.equals("true")) {
                    commandParams.put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.HBASE_MARK_DRAINING_ONLY, isDrainOnlyRequest);
                } else {
                    commandParams.put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.HBASE_MARK_DRAINING_ONLY, "false");
                }
            }
            if ((!serviceName.equals(org.apache.ambari.server.state.Service.Type.HBASE.name())) || hostName.equals(primaryCandidate)) {
                commandParams.put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.UPDATE_FILES_ONLY, "false");
                addCustomCommandAction(commandContext, commandFilter, stage, commandParams, commandDetail.toString(), null);
            }
        }
    }

    private java.util.Set<java.lang.String> calculateDecommissionedNodes(org.apache.ambari.server.state.Service service, java.lang.String slaveCompType) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> decommissionedHostsSet = new java.util.HashSet<>();
        org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(slaveCompType);
        for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponent.getServiceComponentHosts().values()) {
            if (serviceComponentHost.getComponentAdminState() == org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED) {
                decommissionedHostsSet.add(serviceComponentHost.getHostName());
            }
        }
        return decommissionedHostsSet;
    }

    private java.lang.StringBuilder getReadableDecommissionCommandDetail(org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, java.util.Set<java.lang.String> includedHosts, java.util.List<java.lang.String> listOfExcludedHosts) {
        java.lang.StringBuilder commandDetail = new java.lang.StringBuilder();
        commandDetail.append(actionExecutionContext.getActionName());
        if (actionExecutionContext.getParameters().containsKey(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.IS_ADD_OR_DELETE_SLAVE_REQUEST) && actionExecutionContext.getParameters().get(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.IS_ADD_OR_DELETE_SLAVE_REQUEST).equalsIgnoreCase("true")) {
            commandDetail.append(", Update Include/Exclude Files");
            return commandDetail;
        }
        if (listOfExcludedHosts.size() > 0) {
            commandDetail.append(", Excluded: ").append(org.apache.commons.lang.StringUtils.join(listOfExcludedHosts, ','));
        }
        if (includedHosts.size() > 0) {
            commandDetail.append(", Included: ").append(org.apache.commons.lang.StringUtils.join(includedHosts, ','));
        }
        return commandDetail;
    }

    public void validateAction(org.apache.ambari.server.controller.ExecuteActionRequest actionRequest) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = actionRequest.getResourceFilters();
        if ((((resourceFilters != null) && resourceFilters.isEmpty()) && actionRequest.getParameters().containsKey(org.apache.ambari.server.controller.internal.RequestResourceProvider.HAS_RESOURCE_FILTERS)) && actionRequest.getParameters().get(org.apache.ambari.server.controller.internal.RequestResourceProvider.HAS_RESOURCE_FILTERS).equalsIgnoreCase("true")) {
            org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.warn("Couldn't find any resource that satisfies given resource filters");
            return;
        }
        if ((resourceFilters == null) || resourceFilters.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("Command execution cannot proceed without a " + "resource filter.");
        }
        for (org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter : resourceFilters) {
            if ((((resourceFilter.getServiceName() == null) || resourceFilter.getServiceName().isEmpty()) || (actionRequest.getCommandName() == null)) || actionRequest.getCommandName().isEmpty()) {
                throw new org.apache.ambari.server.AmbariException(((((("Invalid resource filter : " + "cluster = ") + actionRequest.getClusterName()) + ", service = ") + resourceFilter.getServiceName()) + ", command = ") + actionRequest.getCommandName());
            }
            if ((!isServiceCheckCommand(actionRequest.getCommandName(), resourceFilter.getServiceName())) && (!isValidCustomCommand(actionRequest, resourceFilter))) {
                throw new org.apache.ambari.server.AmbariException((((("Unsupported action " + actionRequest.getCommandName()) + " for Service: ") + resourceFilter.getServiceName()) + " and Component: ") + resourceFilter.getComponentName());
            }
        }
    }

    public void addExecutionCommandsToStage(org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, org.apache.ambari.server.actionmanager.Stage stage, java.util.Map<java.lang.String, java.lang.String> requestParams, org.apache.ambari.server.controller.ExecuteCommandJson executeCommandJson) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = actionExecutionContext.getResourceFilters();
        for (org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter : resourceFilters) {
            org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug("Received a command execution request, clusterName={}, serviceName={}, request={}", actionExecutionContext.getClusterName(), resourceFilter.getServiceName(), actionExecutionContext);
            java.lang.String actionName = actionExecutionContext.getActionName();
            if (actionName.contains(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.SERVICE_CHECK_COMMAND_NAME)) {
                findHostAndAddServiceCheckAction(actionExecutionContext, resourceFilter, stage);
            } else if (actionName.equals(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.DECOMMISSION_COMMAND_NAME)) {
                addDecommissionAction(actionExecutionContext, resourceFilter, stage, executeCommandJson);
            } else if (isValidCustomCommand(actionExecutionContext, resourceFilter)) {
                java.lang.String commandDetail = getReadableCustomCommandDetail(actionExecutionContext, resourceFilter);
                java.util.Map<java.lang.String, java.lang.String> extraParams = new java.util.HashMap<>();
                java.lang.String componentName = (null == resourceFilter.getComponentName()) ? null : resourceFilter.getComponentName().toLowerCase();
                if ((null != componentName) && requestParams.containsKey(componentName)) {
                    extraParams.put(componentName, requestParams.get(componentName));
                }
                if (requestParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_RETRY_ENABLED)) {
                    extraParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_RETRY_ENABLED, requestParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_RETRY_ENABLED));
                    java.lang.String commandRetryDuration = org.apache.ambari.server.state.ConfigHelper.COMMAND_RETRY_MAX_TIME_IN_SEC_DEFAULT;
                    if (requestParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MAX_DURATION_OF_RETRIES)) {
                        java.lang.String commandRetryDurationStr = requestParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MAX_DURATION_OF_RETRIES);
                        java.lang.Integer commandRetryDurationInt = org.apache.commons.lang.math.NumberUtils.toInt(commandRetryDurationStr, 0);
                        if (commandRetryDurationInt > 0) {
                            commandRetryDuration = java.lang.Integer.toString(commandRetryDurationInt);
                        }
                    }
                    extraParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MAX_DURATION_OF_RETRIES, commandRetryDuration);
                }
                if (requestParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.LOG_OUTPUT)) {
                    extraParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.LOG_OUTPUT, requestParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.LOG_OUTPUT));
                }
                if (requestParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS)) {
                    actionExecutionContext.getParameters().put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS, requestParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS));
                }
                org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = actionExecutionContext.getOperationLevel();
                if (operationLevel != null) {
                    java.lang.String clusterName = operationLevel.getClusterName();
                    java.lang.String serviceName = operationLevel.getServiceName();
                    if (isTopologyRefreshRequired(actionName, clusterName, serviceName)) {
                        extraParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.REFRESH_TOPOLOGY, "True");
                    }
                }
                addCustomCommandAction(actionExecutionContext, resourceFilter, stage, extraParams, commandDetail, requestParams);
            } else {
                throw new org.apache.ambari.server.AmbariException("Unsupported action " + actionName);
            }
        }
    }

    public org.apache.ambari.server.controller.ExecuteCommandJson getCommandJson(org.apache.ambari.server.controller.ActionExecutionContext actionExecContext, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, java.lang.String requestContext) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> commandParamsStage = org.apache.ambari.server.utils.StageUtils.getCommandParamsStage(actionExecContext, requestContext);
        java.util.Map<java.lang.String, java.lang.String> hostParamsStage = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo;
        java.lang.String clusterHostInfoJson = "{}";
        if (null != cluster) {
            clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster);
            hostParamsStage = createDefaultHostParams(cluster, stackId);
            java.lang.String componentName = null;
            java.lang.String serviceName = null;
            if (actionExecContext.getOperationLevel() != null) {
                componentName = actionExecContext.getOperationLevel().getHostComponentName();
                serviceName = actionExecContext.getOperationLevel().getServiceName();
            }
            if ((serviceName != null) && (componentName != null)) {
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(componentName);
                stackId = component.getDesiredStackId();
                org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
                java.util.List<java.lang.String> clientsToUpdateConfigsList = componentInfo.getClientsToUpdateConfigs();
                if (clientsToUpdateConfigsList == null) {
                    clientsToUpdateConfigsList = new java.util.ArrayList<>();
                    clientsToUpdateConfigsList.add("*");
                }
                java.lang.String clientsToUpdateConfigs = gson.toJson(clientsToUpdateConfigsList);
                hostParamsStage.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLIENTS_TO_UPDATE_CONFIGS, clientsToUpdateConfigs);
            }
            clusterHostInfoJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(clusterHostInfo);
        }
        java.lang.String hostParamsStageJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostParamsStage);
        java.lang.String commandParamsStageJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(commandParamsStage);
        return new org.apache.ambari.server.controller.ExecuteCommandJson(clusterHostInfoJson, commandParamsStageJson, hostParamsStageJson);
    }

    java.util.Map<java.lang.String, java.lang.String> createDefaultHostParams(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        if (null == stackId) {
            stackId = cluster.getDesiredStackVersion();
        }
        java.util.TreeMap<java.lang.String, java.lang.String> hostLevelParams = new java.util.TreeMap<>();
        org.apache.ambari.server.utils.StageUtils.useStackJdkIfExists(hostLevelParams, configs);
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION, managementController.getJdkResourceUrl());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME, stackId.getStackName());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION, stackId.getStackVersion());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_NAME, managementController.getServerDB());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MYSQL_JDBC_URL, managementController.getMysqljdbcUrl());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.ORACLE_JDBC_URL, managementController.getOjdbcUrl());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_DRIVER_FILENAME, configs.getMySQLJarName());
        hostLevelParams.putAll(managementController.getRcaParameters());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOST_SYS_PREPPED, configs.areHostsSysPrepped());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_ON_UNAVAILABILITY, configs.isAgentStackRetryOnInstallEnabled());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_COUNT, configs.getAgentStackRetryOnInstallCount());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GPL_LICENSE_ACCEPTED, configs.getGplLicenseAccepted().toString());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> notManagedHdfsPathMap = configHelper.getPropertiesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.NOT_MANAGED_HDFS_PATH, cluster, desiredConfigs);
        java.util.Set<java.lang.String> notManagedHdfsPathSet = configHelper.filterInvalidPropertyValues(notManagedHdfsPathMap, org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST);
        java.lang.String notManagedHdfsPathList = gson.toJson(notManagedHdfsPathSet);
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.NOT_MANAGED_HDFS_PATH_LIST, notManagedHdfsPathList);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> dbConnectorName : configs.getDatabaseConnectorNames().entrySet()) {
            hostLevelParams.put(dbConnectorName.getKey(), dbConnectorName.getValue());
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.String> previousDBConnectorName : configs.getPreviousDatabaseConnectorNames().entrySet()) {
            hostLevelParams.put(previousDBConnectorName.getKey(), previousDBConnectorName.getValue());
        }
        return hostLevelParams;
    }

    public boolean isTopologyRefreshRequired(java.lang.String actionName, java.lang.String clusterName, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        if (actionName.equals(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.START_COMMAND_NAME) || actionName.equals(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.RESTART_COMMAND_NAME)) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            org.apache.ambari.server.state.StackId stackId = null;
            if (serviceName != null) {
                try {
                    org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                    stackId = service.getDesiredStackId();
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug("Could not load service {}, skipping topology check", serviceName);
                }
            }
            if (stackId == null) {
                stackId = cluster.getDesiredStackVersion();
            }
            org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = managementController.getAmbariMetaInfo();
            org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
            if (stack != null) {
                org.apache.ambari.server.state.ServiceInfo serviceInfo = stack.getService(serviceName);
                if (serviceInfo != null) {
                    java.lang.Boolean restartRequiredAfterRackChange = serviceInfo.isRestartRequiredAfterRackChange();
                    if ((restartRequiredAfterRackChange != null) && restartRequiredAfterRackChange) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private org.apache.ambari.server.state.ServiceComponent getServiceComponent(org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter) {
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(actionExecutionContext.getClusterName());
            org.apache.ambari.server.state.Service service = cluster.getService(resourceFilter.getServiceName());
            return service.getServiceComponent(resourceFilter.getComponentName());
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.LOG.debug("Unknown error appears during getting service component: {}", e.getMessage());
        }
        return null;
    }

    private boolean filterUnhealthHostItem(java.lang.String hostname, org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = actionExecutionContext.getOperationLevel();
        org.apache.ambari.server.state.ServiceComponent serviceComponent = getServiceComponent(actionExecutionContext, resourceFilter);
        if (((((serviceComponent != null) && (operationLevel != null)) && (operationLevel.getLevel() == org.apache.ambari.server.controller.spi.Resource.Type.Service)) && (actionExecutionContext.getResourceFilters().size() > 1)) && (!serviceComponent.isMasterComponent())) {
            return !(clusters.getHost(hostname).getState() == org.apache.ambari.server.state.HostState.HEALTHY);
        } else if ((((((serviceComponent != null) && (operationLevel != null)) && (operationLevel.getLevel() == org.apache.ambari.server.controller.spi.Resource.Type.Host)) && (actionExecutionContext.getResourceFilters().size() > 1)) && serviceComponent.getServiceComponentHosts().containsKey(hostname)) && (!serviceComponent.isMasterComponent())) {
            org.apache.ambari.server.state.State hostState = serviceComponent.getServiceComponentHosts().get(hostname).getState();
            return hostState == org.apache.ambari.server.state.State.UNKNOWN;
        }
        return false;
    }

    private java.util.Set<java.lang.String> getUnhealthyHosts(java.util.Set<java.lang.String> hosts, org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext, org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> removedHosts = new java.util.HashSet<>();
        for (java.lang.String hostname : hosts) {
            if (filterUnhealthHostItem(hostname, actionExecutionContext, resourceFilter)) {
                removedHosts.add(hostname);
            }
        }
        hosts.removeAll(removedHosts);
        return removedHosts;
    }

    public java.lang.String getStatusCommandTimeout(org.apache.ambari.server.state.ServiceInfo serviceInfo) throws org.apache.ambari.server.AmbariException {
        java.lang.String commandTimeout = configs.getDefaultAgentTaskTimeout(false);
        if (serviceInfo.getSchemaVersion().equals(org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2)) {
            org.apache.ambari.server.state.CommandScriptDefinition script = serviceInfo.getCommandScript();
            if (script != null) {
                if (script.getTimeout() > 0) {
                    commandTimeout = java.lang.String.valueOf(script.getTimeout());
                }
            } else {
                java.lang.String message = java.lang.String.format("Service %s has no command script " + ("defined. It is not possible to run service check" + " for this service"), serviceInfo.getName());
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
        java.lang.Long overriddenTimeout = configs.getAgentServiceCheckTaskTimeout();
        if (!overriddenTimeout.equals(org.apache.ambari.server.configuration.Configuration.AGENT_SERVICE_CHECK_TASK_TIMEOUT.getDefaultValue())) {
            commandTimeout = java.lang.String.valueOf(overriddenTimeout);
        }
        return commandTimeout;
    }
}