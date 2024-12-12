package org.apache.ambari.server.controller;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_COUNT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_ON_UNAVAILABILITY;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMPONENT_CATEGORY;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GPL_LICENSE_ACCEPTED;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME;
@com.google.inject.Singleton
public class AmbariActionExecutionHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.AmbariActionExecutionHelper.class);

    private static final java.lang.String TYPE_PYTHON = "PYTHON";

    private static final java.lang.String ACTION_FILE_EXTENSION = "py";

    private static final java.lang.String ACTION_UPDATE_REPO = "update_repo";

    private static final java.lang.String SUCCESS_FACTOR_PARAMETER = "success_factor";

    private static final float UPDATE_REPO_SUCCESS_FACTOR_DEFAULT = 0.0F;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController managementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configs;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repoVersionHelper;

    public void validateAction(org.apache.ambari.server.controller.ExecuteActionRequest actionRequest) throws org.apache.ambari.server.AmbariException {
        if ((actionRequest.getActionName() == null) || actionRequest.getActionName().isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("Action name must be specified");
        }
        org.apache.ambari.server.customactions.ActionDefinition actionDef = ambariMetaInfo.getActionDefinition(actionRequest.getActionName());
        if (actionDef == null) {
            throw new org.apache.ambari.server.AmbariException(("Action " + actionRequest.getActionName()) + " does not exist");
        }
        if (actionDef.getInputs() != null) {
            java.lang.String[] inputs = actionDef.getInputs().split(",");
            for (java.lang.String input : inputs) {
                java.lang.String inputName = input.trim();
                if (!inputName.isEmpty()) {
                    boolean mandatory = true;
                    if (inputName.startsWith("[") && inputName.endsWith("]")) {
                        mandatory = false;
                    }
                    if (mandatory && (!actionRequest.getParameters().containsKey(inputName))) {
                        throw new org.apache.ambari.server.AmbariException(((("Action " + actionRequest.getActionName()) + " requires input '") + input.trim()) + "' that is not provided.");
                    }
                }
            }
        }
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = actionRequest.getResourceFilters();
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = null;
        if ((resourceFilters != null) && (!resourceFilters.isEmpty())) {
            if (resourceFilters.size() > 1) {
                throw new org.apache.ambari.server.AmbariException("Custom action definition only allows one " + "resource filter to be specified.");
            } else {
                resourceFilter = resourceFilters.get(0);
            }
        }
        java.lang.String targetService = "";
        java.lang.String targetComponent = "";
        if (null != actionRequest.getClusterName()) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(actionRequest.getClusterName());
            if (cluster == null) {
                throw new org.apache.ambari.server.AmbariException("Unable to find cluster. clusterName = " + actionRequest.getClusterName());
            }
            java.lang.String expectedService = (actionDef.getTargetService() == null) ? "" : actionDef.getTargetService();
            java.lang.String actualService = ((resourceFilter == null) || (resourceFilter.getServiceName() == null)) ? "" : resourceFilter.getServiceName();
            if (((!expectedService.isEmpty()) && (!actualService.isEmpty())) && (!expectedService.equals(actualService))) {
                throw new org.apache.ambari.server.AmbariException((((("Action " + actionRequest.getActionName()) + " targets service ") + actualService) + " that does not match with expected ") + expectedService);
            }
            targetService = expectedService;
            if (org.apache.commons.lang.StringUtils.isBlank(targetService)) {
                targetService = actualService;
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(targetService)) {
                org.apache.ambari.server.state.Service service = cluster.getService(targetService);
                org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
                org.apache.ambari.server.state.ServiceInfo serviceInfo;
                try {
                    serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), targetService);
                } catch (org.apache.ambari.server.StackAccessException se) {
                    serviceInfo = null;
                }
                if (serviceInfo == null) {
                    throw new org.apache.ambari.server.AmbariException(((("Action " + actionRequest.getActionName()) + " targets service ") + targetService) + " that does not exist.");
                }
            }
            java.lang.String expectedComponent = (actionDef.getTargetComponent() == null) ? "" : actionDef.getTargetComponent();
            java.lang.String actualComponent = ((resourceFilter == null) || (resourceFilter.getComponentName() == null)) ? "" : resourceFilter.getComponentName();
            if (((!expectedComponent.isEmpty()) && (!actualComponent.isEmpty())) && (!expectedComponent.equals(actualComponent))) {
                throw new org.apache.ambari.server.AmbariException((((("Action " + actionRequest.getActionName()) + " targets component ") + actualComponent) + " that does not match with expected ") + expectedComponent);
            }
            targetComponent = expectedComponent;
            if (org.apache.commons.lang.StringUtils.isBlank(targetComponent)) {
                targetComponent = actualComponent;
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(targetComponent) && org.apache.commons.lang.StringUtils.isBlank(targetService)) {
                throw new org.apache.ambari.server.AmbariException(((("Action " + actionRequest.getActionName()) + " targets component ") + targetComponent) + " without specifying the target service.");
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(targetComponent)) {
                org.apache.ambari.server.state.Service service = cluster.getService(targetService);
                org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(targetComponent);
                org.apache.ambari.server.state.StackId stackId = component.getDesiredStackId();
                org.apache.ambari.server.state.ComponentInfo compInfo;
                try {
                    compInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), targetService, targetComponent);
                } catch (org.apache.ambari.server.StackAccessException se) {
                    compInfo = null;
                }
                if (compInfo == null) {
                    throw new org.apache.ambari.server.AmbariException(((("Action " + actionRequest.getActionName()) + " targets component ") + targetComponent) + " that does not exist.");
                }
            }
        }
        org.apache.ambari.server.actionmanager.TargetHostType targetHostType = actionDef.getTargetType();
        if (org.apache.ambari.server.actionmanager.TargetHostType.SPECIFIC.equals(targetHostType) || (targetService.isEmpty() && targetComponent.isEmpty())) {
            if (((resourceFilter == null) || (resourceFilter.getHostNames().size() == 0)) && (!isTargetHostTypeAllowsEmptyHosts(targetHostType))) {
                throw new org.apache.ambari.server.AmbariException((("Action " + actionRequest.getActionName()) + " requires explicit target host(s)") + " that is not provided.");
            }
        }
    }

    private boolean isTargetHostTypeAllowsEmptyHosts(org.apache.ambari.server.actionmanager.TargetHostType targetHostType) {
        return (targetHostType.equals(org.apache.ambari.server.actionmanager.TargetHostType.ALL) || targetHostType.equals(org.apache.ambari.server.actionmanager.TargetHostType.ANY)) || targetHostType.equals(org.apache.ambari.server.actionmanager.TargetHostType.MAJORITY);
    }

    public void addExecutionCommandsToStage(final org.apache.ambari.server.controller.ActionExecutionContext actionContext, org.apache.ambari.server.actionmanager.Stage stage, java.util.Map<java.lang.String, java.lang.String> requestParams) throws org.apache.ambari.server.AmbariException {
        addExecutionCommandsToStage(actionContext, stage, requestParams, true);
    }

    public void addExecutionCommandsToStage(final org.apache.ambari.server.controller.ActionExecutionContext actionContext, org.apache.ambari.server.actionmanager.Stage stage, java.util.Map<java.lang.String, java.lang.String> requestParams, boolean checkHostIsMemberOfCluster) throws org.apache.ambari.server.AmbariException {
        java.lang.String actionName = actionContext.getActionName();
        java.lang.String clusterName = actionContext.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster;
        if (null != clusterName) {
            cluster = clusters.getCluster(clusterName);
        } else {
            cluster = null;
        }
        org.apache.ambari.server.state.ComponentInfo componentInfo = null;
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = actionContext.getResourceFilters();
        final org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter;
        if ((resourceFilters != null) && (!resourceFilters.isEmpty())) {
            resourceFilter = resourceFilters.get(0);
        } else {
            resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter();
        }
        java.util.Set<java.lang.String> candidateHosts = new java.util.HashSet<>();
        final java.lang.String serviceName = actionContext.getExpectedServiceName();
        final java.lang.String componentName = actionContext.getExpectedComponentName();
        org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.debug("Called addExecutionCommandsToStage() for serviceName: {}, componentName: {}.", serviceName, componentName);
        if (resourceFilter.getHostNames().isEmpty()) {
            org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.debug("Resource filter has no hostnames.");
        } else {
            org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.debug("Resource filter has hosts: {}", org.apache.commons.lang.StringUtils.join(resourceFilter.getHostNames(), ", "));
        }
        if (null != cluster) {
            if ((serviceName != null) && (!serviceName.isEmpty())) {
                if ((componentName != null) && (!componentName.isEmpty())) {
                    org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                    org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(componentName);
                    org.apache.ambari.server.state.StackId stackId = component.getDesiredStackId();
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> componentHosts = component.getServiceComponentHosts();
                    candidateHosts.addAll(componentHosts.keySet());
                    try {
                        componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
                    } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                        org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.error("Did not find service {} and component {} in stack {}.", serviceName, componentName, stackId.getStackName());
                    }
                } else {
                    for (java.lang.String component : cluster.getService(serviceName).getServiceComponents().keySet()) {
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> componentHosts = cluster.getService(serviceName).getServiceComponent(component).getServiceComponentHosts();
                        candidateHosts.addAll(componentHosts.keySet());
                    }
                }
            } else {
                candidateHosts.addAll(clusters.getHostsForCluster(cluster.getClusterName()).keySet());
            }
            org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.debug("Request for service {} and component {} is set to run on candidate hosts: {}.", serviceName, componentName, org.apache.commons.lang.StringUtils.join(candidateHosts, ", "));
            java.util.Set<java.lang.String> ignoredHosts = maintenanceStateHelper.filterHostsInMaintenanceState(candidateHosts, new org.apache.ambari.server.controller.MaintenanceStateHelper.HostPredicate() {
                @java.lang.Override
                public boolean shouldHostBeRemoved(final java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
                    return !maintenanceStateHelper.isOperationAllowed(cluster, actionContext.getOperationLevel(), resourceFilter, serviceName, componentName, hostname);
                }
            });
            if (!ignoredHosts.isEmpty()) {
                org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.debug("Hosts to ignore: {}.", ignoredHosts);
                org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.debug("Ignoring action for hosts due to maintenance state.Ignored hosts ={}, component={}, service={}, cluster={}, actionName={}", ignoredHosts, componentName, serviceName, cluster.getClusterName(), actionContext.getActionName());
            }
        }
        if (resourceFilter.getHostNames().isEmpty() && candidateHosts.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException(((((("Suitable hosts not found, component=" + componentName) + ", service=") + serviceName) + (null == cluster ? "" : (", cluster=" + cluster.getClusterName()) + ", ")) + "actionName=") + actionContext.getActionName());
        }
        if (checkHostIsMemberOfCluster) {
            if ((!resourceFilter.getHostNames().isEmpty()) && (!candidateHosts.isEmpty())) {
                for (java.lang.String hostname : resourceFilter.getHostNames()) {
                    if (!candidateHosts.contains(hostname)) {
                        throw new org.apache.ambari.server.AmbariException(((((("Request specifies host " + hostname) + " but it is not a valid host based on the ") + "target service=") + serviceName) + " and component=") + componentName);
                    }
                }
            }
        }
        java.util.List<java.lang.String> targetHosts = resourceFilter.getHostNames();
        if (targetHosts.isEmpty()) {
            org.apache.ambari.server.actionmanager.TargetHostType hostType = actionContext.getTargetType();
            switch (hostType) {
                case ALL :
                    targetHosts.addAll(candidateHosts);
                    break;
                case ANY :
                    targetHosts.add(managementController.getHealthyHost(candidateHosts));
                    break;
                case MAJORITY :
                    for (int i = 0; i < ((candidateHosts.size() / 2) + 1); i++) {
                        java.lang.String hostname = managementController.getHealthyHost(candidateHosts);
                        targetHosts.add(hostname);
                        candidateHosts.remove(hostname);
                    }
                    break;
                default :
                    throw new org.apache.ambari.server.AmbariException("Unsupported target type = " + hostType);
            }
        }
        setAdditionalParametersForStageAccordingToAction(stage, actionContext);
        for (java.lang.String hostName : targetHosts) {
            java.util.Map<java.lang.String, java.lang.String> actionParameters = actionContext.getParameters();
            stage.addHostRoleExecutionCommand(hostName, org.apache.ambari.server.Role.valueOf(actionContext.getActionName()), org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(actionContext.getActionName(), hostName, java.lang.System.currentTimeMillis()), clusterName, serviceName, actionContext.isRetryAllowed(), actionContext.isFailureAutoSkipped());
            java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.TreeMap<>();
            int taskTimeout = java.lang.Integer.parseInt(configs.getDefaultAgentTaskTimeout(false));
            if ((null != actionContext.getTimeout()) && (actionContext.getTimeout() > taskTimeout)) {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, actionContext.getTimeout().toString());
            } else {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, java.lang.Integer.toString(taskTimeout));
            }
            if ((requestParams != null) && requestParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.LOG_OUTPUT)) {
                org.apache.ambari.server.controller.AmbariActionExecutionHelper.LOG.info("Should command log output?: " + requestParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.LOG_OUTPUT));
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.LOG_OUTPUT, requestParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.LOG_OUTPUT));
            }
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT, (actionName + ".") + org.apache.ambari.server.controller.AmbariActionExecutionHelper.ACTION_FILE_EXTENSION);
            commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE, org.apache.ambari.server.controller.AmbariActionExecutionHelper.TYPE_PYTHON);
            org.apache.ambari.server.utils.StageUtils.useAmbariJdkInCommandParams(commandParams, configs);
            org.apache.ambari.server.agent.ExecutionCommand execCmd = stage.getExecutionCommandWrapper(hostName, actionContext.getActionName()).getExecutionCommand();
            execCmd.setConfigurations(new java.util.TreeMap<>());
            if ((null != actionParameters) && (!actionParameters.isEmpty())) {
                if (actionParameters.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_CONFIGS)) {
                    execCmd.setOverrideConfigs(true);
                }
                if (actionParameters.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.OVERRIDE_STACK_NAME)) {
                    java.util.Map<java.lang.String, java.lang.String> clusterLevelParams = execCmd.getClusterLevelParams();
                    if (clusterLevelParams == null) {
                        clusterLevelParams = new java.util.HashMap<>();
                    }
                    clusterLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME, actionContext.getStackId().getStackName());
                    execCmd.setClusterLevelParams(clusterLevelParams);
                }
            }
            execCmd.setServiceName((serviceName == null) || serviceName.isEmpty() ? resourceFilter.getServiceName() : serviceName);
            execCmd.setComponentName((componentName == null) || componentName.isEmpty() ? resourceFilter.getComponentName() : componentName);
            java.util.Map<java.lang.String, java.lang.String> hostLevelParams = execCmd.getHostLevelParams();
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GPL_LICENSE_ACCEPTED, configs.getGplLicenseAccepted().toString());
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_ON_UNAVAILABILITY, configs.isAgentStackRetryOnInstallEnabled());
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.AGENT_STACK_RETRY_COUNT, configs.getAgentStackRetryOnInstallCount());
            for (java.util.Map.Entry<java.lang.String, java.lang.String> dbConnectorName : configs.getDatabaseConnectorNames().entrySet()) {
                hostLevelParams.put(dbConnectorName.getKey(), dbConnectorName.getValue());
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> previousDBConnectorName : configs.getPreviousDatabaseConnectorNames().entrySet()) {
                hostLevelParams.put(previousDBConnectorName.getKey(), previousDBConnectorName.getValue());
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(serviceName)) {
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                repoVersionHelper.addRepoInfoToHostLevelParams(cluster, actionContext, service.getDesiredRepositoryVersion(), hostLevelParams, hostName);
            } else {
                repoVersionHelper.addRepoInfoToHostLevelParams(cluster, actionContext, null, hostLevelParams, hostName);
            }
            java.util.Map<java.lang.String, java.lang.String> roleParams = execCmd.getRoleParams();
            if (roleParams == null) {
                roleParams = new java.util.TreeMap<>();
            }
            roleParams.putAll(actionParameters);
            org.apache.ambari.server.utils.SecretReference.replaceReferencesWithPasswords(roleParams, cluster);
            if (componentInfo != null) {
                roleParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMPONENT_CATEGORY, componentInfo.getCategory());
            }
            if ((null != cluster) && cluster.isUpgradeSuspended()) {
                cluster.addSuspendedUpgradeParameters(commandParams, roleParams);
            }
            execCmd.setCommandParams(commandParams);
            execCmd.setRoleParams(roleParams);
            if (null != cluster) {
                for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(hostName)) {
                    execCmd.getLocalComponents().add(sch.getServiceComponentName());
                }
            }
            actionContext.visitAll(execCmd);
        }
    }

    private void setAdditionalParametersForStageAccordingToAction(org.apache.ambari.server.actionmanager.Stage stage, org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext) throws org.apache.ambari.server.AmbariException {
        if (actionExecutionContext.getActionName().equals(org.apache.ambari.server.controller.AmbariActionExecutionHelper.ACTION_UPDATE_REPO)) {
            java.util.Map<java.lang.String, java.lang.String> params = actionExecutionContext.getParameters();
            float successFactor = org.apache.ambari.server.controller.AmbariActionExecutionHelper.UPDATE_REPO_SUCCESS_FACTOR_DEFAULT;
            if ((params != null) && params.containsKey(org.apache.ambari.server.controller.AmbariActionExecutionHelper.SUCCESS_FACTOR_PARAMETER)) {
                try {
                    successFactor = java.lang.Float.valueOf(params.get(org.apache.ambari.server.controller.AmbariActionExecutionHelper.SUCCESS_FACTOR_PARAMETER));
                } catch (java.lang.Exception ex) {
                    throw new org.apache.ambari.server.AmbariException("Failed to cast success_factor value to float!", ex.getCause());
                }
            }
            stage.getSuccessFactors().put(org.apache.ambari.server.Role.UPDATE_REPO, successFactor);
        }
    }
}