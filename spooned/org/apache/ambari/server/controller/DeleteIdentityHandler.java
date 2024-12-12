package org.apache.ambari.server.controller;
import org.apache.commons.collections.CollectionUtils;
import static org.apache.ambari.server.controller.KerberosHelperImpl.BASE_LOG_DIR;
import static org.apache.ambari.server.controller.KerberosHelperImpl.REMOVE_KEYTAB;
class DeleteIdentityHandler {
    public static final java.lang.String COMPONENT_FILTER = "component_filter";

    private final org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper customCommandExecutionHelper;

    private final java.lang.Integer taskTimeout;

    private final org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    private final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    public DeleteIdentityHandler(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper customCommandExecutionHelper, java.lang.Integer taskTimeout, org.apache.ambari.server.actionmanager.StageFactory stageFactory, org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) {
        this.customCommandExecutionHelper = customCommandExecutionHelper;
        this.taskTimeout = taskTimeout;
        this.stageFactory = stageFactory;
        this.ambariManagementController = ambariManagementController;
    }

    public void addDeleteIdentityStages(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.OrderedRequestStageContainer stageContainer, org.apache.ambari.server.controller.DeleteIdentityHandler.CommandParams commandParameters, boolean manageIdentities) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), org.apache.ambari.server.utils.StageUtils.getHostName(), java.lang.System.currentTimeMillis());
        java.lang.String hostParamsJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(customCommandExecutionHelper.createDefaultHostParams(cluster, cluster.getDesiredStackVersion()));
        if (manageIdentities) {
            addPrepareDeleteIdentity(cluster, hostParamsJson, event, commandParameters, stageContainer);
            addDeleteKeytab(cluster, commandParameters.getAffectedHostNames(), hostParamsJson, commandParameters, stageContainer);
            addDestroyPrincipals(cluster, hostParamsJson, event, commandParameters, stageContainer);
        }
        addFinalize(cluster, hostParamsJson, event, stageContainer, commandParameters);
    }

    private void addPrepareDeleteIdentity(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.controller.DeleteIdentityHandler.CommandParams commandParameters, org.apache.ambari.server.controller.OrderedRequestStageContainer stageContainer) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(stageContainer.getLastStageId(), cluster, stageContainer.getId(), "Prepare delete identities", "{}", hostParamsJson, org.apache.ambari.server.controller.DeleteIdentityHandler.PrepareDeleteIdentityServerAction.class, event, commandParameters.asMap(), "Prepare delete identities", taskTimeout);
        stageContainer.addStage(stage);
    }

    private void addDestroyPrincipals(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.controller.DeleteIdentityHandler.CommandParams commandParameters, org.apache.ambari.server.controller.OrderedRequestStageContainer stageContainer) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(stageContainer.getLastStageId(), cluster, stageContainer.getId(), "Destroy Principals", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.class, event, commandParameters.asMap(), "Destroy Principals", java.lang.Math.max(org.apache.ambari.server.serveraction.ServerAction.DEFAULT_LONG_RUNNING_TASK_TIMEOUT_SECONDS, taskTimeout));
        stageContainer.addStage(stage);
    }

    private void addDeleteKeytab(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> hostFilter, java.lang.String hostParamsJson, org.apache.ambari.server.controller.DeleteIdentityHandler.CommandParams commandParameters, org.apache.ambari.server.controller.OrderedRequestStageContainer stageContainer) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> hostNames = (org.apache.commons.collections.CollectionUtils.isEmpty(hostFilter)) ? null : hostFilter.stream().filter(hostname -> ambariManagementController.getClusters().hostExists(hostname)).collect(java.util.stream.Collectors.toSet());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(hostNames)) {
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(stageContainer.getLastStageId(), cluster, stageContainer.getId(), "Delete Keytabs", commandParameters.asJson(), hostParamsJson);
            java.util.Map<java.lang.String, java.lang.String> requestParams = new java.util.HashMap<>();
            java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> requestResourceFilters = new java.util.ArrayList<>();
            org.apache.ambari.server.controller.internal.RequestResourceFilter reqResFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("KERBEROS", "KERBEROS_CLIENT", new java.util.ArrayList<>(hostNames));
            requestResourceFilters.add(reqResFilter);
            org.apache.ambari.server.controller.ActionExecutionContext actionExecContext = new org.apache.ambari.server.controller.ActionExecutionContext(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelperImpl.REMOVE_KEYTAB, requestResourceFilters, requestParams);
            customCommandExecutionHelper.addExecutionCommandsToStage(actionExecContext, stage, requestParams, null);
            stageContainer.addStage(stage);
        }
    }

    private void addFinalize(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.controller.OrderedRequestStageContainer requestStageContainer, org.apache.ambari.server.controller.DeleteIdentityHandler.CommandParams commandParameters) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Finalize Operations", "{}", hostParamsJson, org.apache.ambari.server.controller.DeleteIdentityHandler.DeleteDataDirAction.class, event, commandParameters.asMap(), "Finalize Operations", 300);
        requestStageContainer.addStage(stage);
    }

    public static class CommandParams {
        private final java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components;

        private final java.util.Set<java.lang.String> identities;

        private final java.lang.String authName;

        private final java.io.File dataDirectory;

        private final java.lang.String defaultRealm;

        private final org.apache.ambari.server.serveraction.kerberos.KDCType kdcType;

        public CommandParams(java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components, java.util.Set<java.lang.String> identities, java.lang.String authName, java.io.File dataDirectory, java.lang.String defaultRealm, org.apache.ambari.server.serveraction.kerberos.KDCType kdcType) {
            this.components = components;
            this.identities = identities;
            this.authName = authName;
            this.dataDirectory = dataDirectory;
            this.defaultRealm = defaultRealm;
            this.kdcType = kdcType;
        }

        public java.util.Map<java.lang.String, java.lang.String> asMap() {
            java.util.Map<java.lang.String, java.lang.String> commandParameters = new java.util.HashMap<>();
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, authName);
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, defaultRealm);
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, kdcType.name());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.IDENTITY_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(identities));
            commandParameters.put(org.apache.ambari.server.controller.DeleteIdentityHandler.COMPONENT_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(components));
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.SERVICE_COMPONENT_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(toServiceComponentFilter(components)));
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.HOST_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(toHostFilter(components)));
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
            return commandParameters;
        }

        public java.util.Set<java.lang.String> getAffectedHostNames() {
            return components.stream().map(org.apache.ambari.server.serveraction.kerberos.Component::getHostName).collect(java.util.stream.Collectors.toSet());
        }

        public java.lang.String asJson() {
            return org.apache.ambari.server.utils.StageUtils.getGson().toJson(asMap());
        }

        private java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> toServiceComponentFilter(java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components) {
            if (components == null) {
                return null;
            }
            return components.stream().collect(java.util.stream.Collectors.groupingBy(org.apache.ambari.server.serveraction.kerberos.Component::getServiceName, java.util.stream.Collectors.mapping(org.apache.ambari.server.serveraction.kerberos.Component::getServiceComponentName, java.util.stream.Collectors.toSet())));
        }

        private java.util.Set<java.lang.String> toHostFilter(java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components) {
            if (components == null) {
                return null;
            }
            return components.stream().map(org.apache.ambari.server.serveraction.kerberos.Component::getHostName).collect(java.util.stream.Collectors.toSet());
        }
    }

    public static class PrepareDeleteIdentityServerAction extends org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction {
        @java.lang.Override
        public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
            org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor();
            processServiceComponents(getCluster(), kerberosDescriptor, componentFilter(), getIdentityFilter(), dataDirectory(), calculateConfig(kerberosDescriptor, serviceNames()), new java.util.HashMap<>(), false, new java.util.HashMap<>());
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
        }

        private java.util.Set<java.lang.String> serviceNames() {
            return componentFilter().stream().map(org.apache.ambari.server.serveraction.kerberos.Component::getServiceName).collect(java.util.stream.Collectors.toSet());
        }

        private java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> componentFilter() {
            java.lang.reflect.Type jsonType = new com.google.gson.reflect.TypeToken<java.util.List<org.apache.ambari.server.serveraction.kerberos.Component>>() {}.getType();
            return org.apache.ambari.server.utils.StageUtils.getGson().fromJson(getCommandParameterValue(org.apache.ambari.server.controller.DeleteIdentityHandler.COMPONENT_FILTER), jsonType);
        }

        private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateConfig(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.Set<java.lang.String> serviceNames) throws org.apache.ambari.server.AmbariException {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actualConfig = getKerberosHelper().calculateConfigurations(getCluster(), null, kerberosDescriptor, false, false, null);
            extendWithDeletedConfigOfService(actualConfig, serviceNames);
            return actualConfig;
        }

        private void extendWithDeletedConfigOfService(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configToBeExtended, java.util.Set<java.lang.String> serviceNames) throws org.apache.ambari.server.AmbariException {
            java.util.Set<java.lang.String> deletedConfigTypes = serviceNames.stream().flatMap(serviceName -> configTypesOfService(serviceName).stream()).collect(java.util.stream.Collectors.toSet());
            for (org.apache.ambari.server.state.Config deletedConfig : getCluster().getLatestConfigsWithTypes(deletedConfigTypes)) {
                configToBeExtended.put(deletedConfig.getType(), deletedConfig.getProperties());
            }
        }

        private java.util.Set<java.lang.String> configTypesOfService(java.lang.String serviceName) {
            try {
                org.apache.ambari.server.state.StackId stackId = getCluster().getCurrentStackVersion();
                org.apache.ambari.server.controller.StackServiceRequest stackServiceRequest = new org.apache.ambari.server.controller.StackServiceRequest(stackId.getStackName(), stackId.getStackVersion(), serviceName);
                return org.apache.ambari.server.controller.AmbariServer.getController().getStackServices(java.util.Collections.singleton(stackServiceRequest)).stream().findFirst().orElseThrow(() -> new java.lang.IllegalArgumentException("Could not find stack service " + serviceName)).getConfigTypes().keySet();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        private java.lang.String dataDirectory() {
            return org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(getCommandParameters(), org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY);
        }

        private org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor() throws org.apache.ambari.server.AmbariException {
            return getKerberosHelper().getKerberosDescriptor(getCluster(), false);
        }

        @java.lang.Override
        protected boolean pruneServiceFilter() {
            return false;
        }
    }

    private org.apache.ambari.server.actionmanager.Stage createNewStage(long id, org.apache.ambari.server.state.Cluster cluster, long requestId, java.lang.String requestContext, java.lang.String commandParams, java.lang.String hostParams) {
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(requestId, (org.apache.ambari.server.controller.KerberosHelperImpl.BASE_LOG_DIR + java.io.File.pathSeparator) + requestId, cluster.getClusterName(), cluster.getClusterId(), requestContext, commandParams, hostParams);
        stage.setStageId(id);
        return stage;
    }

    private org.apache.ambari.server.actionmanager.Stage createServerActionStage(long id, org.apache.ambari.server.state.Cluster cluster, long requestId, java.lang.String requestContext, java.lang.String commandParams, java.lang.String hostParams, java.lang.Class<? extends org.apache.ambari.server.serveraction.ServerAction> actionClass, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, java.lang.String commandDetail, java.lang.Integer timeout) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage stage = createNewStage(id, cluster, requestId, requestContext, commandParams, hostParams);
        stage.addServerActionCommand(actionClass.getName(), null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, cluster.getClusterName(), event, commandParameters, commandDetail, ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null), timeout, false, false);
        return stage;
    }

    private static class DeleteDataDirAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
        @java.lang.Override
        public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
            deleteDataDirectory(getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY));
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
        }

        @java.lang.Override
        protected boolean pruneServiceFilter() {
            return false;
        }

        @java.lang.Override
        protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
            return null;
        }
    }
}