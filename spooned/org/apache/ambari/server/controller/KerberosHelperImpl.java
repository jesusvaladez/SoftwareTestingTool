package org.apache.ambari.server.controller;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.server.kerberos.shared.keytab.Keytab;
@com.google.inject.Singleton
public class KerberosHelperImpl implements org.apache.ambari.server.controller.KerberosHelper {
    public static final java.lang.String BASE_LOG_DIR = "/tmp/ambari";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.KerberosHelperImpl.class);

    private static final java.util.Set<org.apache.ambari.server.state.State> PREVIOUSLY_INSTALLED_STATES = java.util.EnumSet.of(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.DISABLED);

    public static final java.lang.String CHECK_KEYTABS = "CHECK_KEYTABS";

    public static final java.lang.String SET_KEYTAB = "SET_KEYTAB";

    public static final java.lang.String REMOVE_KEYTAB = "REMOVE_KEYTAB";

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper customCommandExecutionHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.stageplanner.RoleGraphFactory roleGraphFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.kerberos.VariableReplacementHelper variableReplacementHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory kerberosOperationHandlerFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosPrincipalDAO kerberosPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabDAO kerberosKeytabDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper;

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer toggleKerberos(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.SecurityType securityType, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.controller.KerberosDetails kerberosDetails = getKerberosDetails(cluster, manageIdentities);
        kerberosDetails.setSecurityType(securityType);
        if (securityType == org.apache.ambari.server.state.SecurityType.KERBEROS) {
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.info("Configuring Kerberos for realm {} on cluster, {}", kerberosDetails.getDefaultRealm(), cluster.getClusterName());
            requestStageContainer = handle(cluster, kerberosDetails, null, null, null, null, requestStageContainer, new org.apache.ambari.server.controller.KerberosHelperImpl.EnableKerberosHandler());
        } else if (securityType == org.apache.ambari.server.state.SecurityType.NONE) {
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.info("Disabling Kerberos from cluster, {}", cluster.getClusterName());
            requestStageContainer = handle(cluster, kerberosDetails, null, null, null, null, requestStageContainer, new org.apache.ambari.server.controller.KerberosHelperImpl.DisableKerberosHandler());
        } else {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unexpected security type value: %s", securityType.name()));
        }
        return requestStageContainer;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer executeCustomOperations(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (requestProperties != null) {
            for (org.apache.ambari.server.controller.KerberosHelperImpl.SupportedCustomOperation operation : org.apache.ambari.server.controller.KerberosHelperImpl.SupportedCustomOperation.values()) {
                if (requestProperties.containsKey(operation.name().toLowerCase())) {
                    java.lang.String value = requestProperties.get(operation.name().toLowerCase());
                    switch (operation) {
                        case REGENERATE_KEYTABS :
                            if (cluster.getSecurityType() != org.apache.ambari.server.state.SecurityType.KERBEROS) {
                                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Custom operation %s can only be requested with the security type cluster property: %s", operation.name(), org.apache.ambari.server.state.SecurityType.KERBEROS.name()));
                            }
                            org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType operationType;
                            if ("true".equalsIgnoreCase(value) || "all".equalsIgnoreCase(value)) {
                                operationType = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.RECREATE_ALL;
                            } else if ("missing".equalsIgnoreCase(value)) {
                                operationType = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.CREATE_MISSING;
                            } else {
                                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unexpected directive value: %s", value));
                            }
                            boolean retryAllowed = false;
                            if (requestProperties.containsKey(org.apache.ambari.server.controller.KerberosHelper.ALLOW_RETRY)) {
                                java.lang.String allowRetryString = requestProperties.get(org.apache.ambari.server.controller.KerberosHelper.ALLOW_RETRY);
                                retryAllowed = java.lang.Boolean.parseBoolean(allowRetryString);
                            }
                            java.util.Set<java.lang.String> hostFilter = org.apache.ambari.server.controller.KerberosHelperImpl.parseHostFilter(requestProperties);
                            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceComponentFilter = org.apache.ambari.server.controller.KerberosHelperImpl.parseComponentFilter(requestProperties);
                            org.apache.ambari.server.controller.UpdateConfigurationPolicy updateConfigurationsPolicy = org.apache.ambari.server.controller.UpdateConfigurationPolicy.ALL;
                            if (requestProperties.containsKey(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_CONFIG_UPDATE_POLICY)) {
                                java.lang.String policyValue = requestProperties.get(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_CONFIG_UPDATE_POLICY);
                                updateConfigurationsPolicy = org.apache.ambari.server.controller.UpdateConfigurationPolicy.translate(policyValue);
                                if (updateConfigurationsPolicy == null) {
                                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unexpected comfiguration policy value: %s", policyValue));
                                }
                            } else if (requestProperties.containsKey(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_IGNORE_CONFIGS)) {
                                if ("true".equalsIgnoreCase(requestProperties.get(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_IGNORE_CONFIGS))) {
                                    updateConfigurationsPolicy = org.apache.ambari.server.controller.UpdateConfigurationPolicy.NEW_AND_IDENTITIES;
                                }
                            }
                            boolean forceAllHosts = (hostFilter == null) || hostFilter.contains("*");
                            org.apache.ambari.server.controller.KerberosHelperImpl.CreatePrincipalsAndKeytabsHandler handler = new org.apache.ambari.server.controller.KerberosHelperImpl.CreatePrincipalsAndKeytabsHandler(operationType, updateConfigurationsPolicy, forceAllHosts, true);
                            handler.setRetryAllowed(retryAllowed);
                            requestStageContainer = handle(cluster, getKerberosDetails(cluster, manageIdentities), serviceComponentFilter, hostFilter, null, null, requestStageContainer, handler);
                            break;
                        default :
                            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Custom operation not supported: %s", operation.name()));
                    }
                }
            }
        }
        return requestStageContainer;
    }

    public static java.util.Set<java.lang.String> parseHostFilter(final java.util.Map<java.lang.String, java.lang.String> requestProperties) {
        if (requestProperties.containsKey(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_HOSTS)) {
            return com.google.common.collect.ImmutableSet.copyOf(requestProperties.get(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_HOSTS).split(","));
        }
        return null;
    }

    public static java.util.Map<java.lang.String, java.util.Set<java.lang.String>> parseComponentFilter(final java.util.Map<java.lang.String, java.lang.String> requestProperties) {
        if (requestProperties.containsKey(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_COMPONENTS)) {
            com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.util.Set<java.lang.String>> serviceComponentFilter = com.google.common.collect.ImmutableMap.builder();
            for (java.lang.String serviceString : requestProperties.get(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_COMPONENTS).split(",")) {
                java.lang.String[] serviceComponentsArray = serviceString.split(":");
                java.lang.String serviceName = serviceComponentsArray[0];
                if (serviceComponentsArray.length == 2) {
                    serviceComponentFilter.put(serviceName, com.google.common.collect.ImmutableSet.copyOf(serviceComponentsArray[1].split(";")));
                } else {
                    serviceComponentFilter.put(serviceName, com.google.common.collect.ImmutableSet.of("*"));
                }
            }
            return serviceComponentFilter.build();
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer ensureIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsToForceKerberosOperations, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        return handle(cluster, getKerberosDetails(cluster, manageIdentities), serviceComponentFilter, hostFilter, identityFilter, hostsToForceKerberosOperations, requestStageContainer, new org.apache.ambari.server.controller.KerberosHelperImpl.CreatePrincipalsAndKeytabsHandler(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.DEFAULT, org.apache.ambari.server.controller.UpdateConfigurationPolicy.NONE, false, false));
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer deleteIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        return handle(cluster, getKerberosDetails(cluster, manageIdentities), serviceComponentFilter, hostFilter, identityFilter, null, requestStageContainer, new org.apache.ambari.server.controller.KerberosHelperImpl.DeletePrincipalsAndKeytabsHandler());
    }

    @java.lang.Override
    public void deleteIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components, java.util.Set<java.lang.String> identities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.info("Deleting identities: ", identities);
        org.apache.ambari.server.controller.KerberosDetails kerberosDetails = getKerberosDetails(cluster, null);
        validateKDCCredentials(kerberosDetails, cluster);
        java.io.File dataDirectory = createTemporaryDirectory();
        org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder = ambariManagementController.getRoleCommandOrder(cluster);
        org.apache.ambari.server.controller.DeleteIdentityHandler handler = new org.apache.ambari.server.controller.DeleteIdentityHandler(customCommandExecutionHelper, configuration.getDefaultServerTaskTimeout(), stageFactory, ambariManagementController);
        org.apache.ambari.server.controller.DeleteIdentityHandler.CommandParams commandParameters = new org.apache.ambari.server.controller.DeleteIdentityHandler.CommandParams(components, identities, ambariManagementController.getAuthName(), dataDirectory, kerberosDetails.getDefaultRealm(), kerberosDetails.getKdcType());
        org.apache.ambari.server.controller.OrderedRequestStageContainer stageContainer = new org.apache.ambari.server.controller.OrderedRequestStageContainer(roleGraphFactory, roleCommandOrder, new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager));
        handler.addDeleteIdentityStages(cluster, stageContainer, commandParameters, kerberosDetails.manageIdentities());
        stageContainer.getRequestStageContainer().persist();
    }

    @java.lang.Override
    public void configureServices(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceFilter) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException {
        final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices = new java.util.HashMap<>();
        final java.util.Set<java.lang.String> previouslyExistingServices = new java.util.HashSet<>();
        getServiceComponentHosts(cluster, sch -> {
            if (sch != null) {
                java.lang.String serviceName = sch.getServiceName();
                java.util.Set<java.lang.String> installedComponents = installedServices.computeIfAbsent(serviceName, k -> new java.util.HashSet<>());
                installedComponents.add(sch.getServiceComponentName());
                if ((!previouslyExistingServices.contains(serviceName)) && org.apache.ambari.server.controller.KerberosHelperImpl.PREVIOUSLY_INSTALLED_STATES.contains(sch.getState())) {
                    previouslyExistingServices.add(serviceName);
                }
                return true;
            }
            return false;
        });
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = configHelper.calculateExistingConfigurations(ambariManagementController, cluster, null, null);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> updates = getServiceConfigurationUpdates(cluster, existingConfigurations, installedServices, serviceFilter, previouslyExistingServices, true, true);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : updates.entrySet()) {
            configHelper.updateConfigType(cluster, cluster.getDesiredStackVersion(), ambariManagementController, entry.getKey(), entry.getValue(), null, ambariManagementController.getAuthName(), "Enabling Kerberos for added components");
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getServiceConfigurationUpdates(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceFilter, java.util.Set<java.lang.String> previouslyExistingServices, boolean kerberosEnabled, boolean applyStackAdvisorUpdates) throws org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
        org.apache.ambari.server.controller.KerberosDetails kerberosDetails = getKerberosDetails(cluster, null);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor(cluster, false);
        java.util.Map<java.lang.String, java.lang.String> kerberosDescriptorProperties = kerberosDescriptor.getProperties();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = addAdditionalConfigurations(cluster, deepCopy(existingConfigurations), null, kerberosDescriptorProperties, null);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = new java.util.HashMap<>();
        if (createAmbariIdentities(existingConfigurations.get(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV))) {
            installedServices = new java.util.HashMap<>(installedServices);
            installedServices.put(org.apache.ambari.server.controller.RootService.AMBARI.name(), java.util.Collections.singleton(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name()));
        }
        java.util.Map<java.lang.String, java.lang.Object> filterContext = new java.util.HashMap<>();
        filterContext.put("configurations", configurations);
        filterContext.put("services", installedServices.keySet());
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> installedServiceEntry : installedServices.entrySet()) {
            java.lang.String installedService = installedServiceEntry.getKey();
            if ((serviceFilter == null) || serviceFilter.containsKey(installedService)) {
                java.util.Collection<java.lang.String> componentFilter = (serviceFilter == null) ? null : serviceFilter.get(installedService);
                java.util.Set<java.lang.String> installedComponents = installedServiceEntry.getValue();
                org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosDescriptor.getService(installedService);
                if (serviceDescriptor != null) {
                    if (installedComponents != null) {
                        boolean servicePreviouslyExisted = (previouslyExistingServices != null) && previouslyExistingServices.contains(installedService);
                        for (java.lang.String installedComponent : installedComponents) {
                            if ((componentFilter == null) || componentFilter.contains(installedComponent)) {
                                org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = serviceDescriptor.getComponent(installedComponent);
                                if (componentDescriptor != null) {
                                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> identityConfigurations;
                                    identityConfigurations = getIdentityConfigurations(serviceDescriptor.getIdentities(true, filterContext));
                                    processIdentityConfigurations(identityConfigurations, kerberosConfigurations, configurations, propertiesToIgnore);
                                    identityConfigurations = getIdentityConfigurations(componentDescriptor.getIdentities(true, filterContext));
                                    processIdentityConfigurations(identityConfigurations, kerberosConfigurations, configurations, propertiesToIgnore);
                                    mergeConfigurations(kerberosConfigurations, componentDescriptor.getConfigurations(!servicePreviouslyExisted), configurations, null);
                                }
                            }
                        }
                    }
                }
            }
        }
        setAuthToLocalRules(cluster, kerberosDescriptor, kerberosDetails.getDefaultRealm(), installedServices, configurations, kerberosConfigurations, false);
        return applyStackAdvisorUpdates ? applyStackAdvisorUpdates(cluster, installedServices.keySet(), configurations, kerberosConfigurations, propertiesToIgnore, new java.util.HashMap<>(), kerberosEnabled) : kerberosConfigurations;
    }

    private void applyStackAdvisorHostRecommendations(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> services, java.util.Set<java.lang.String> componentFilter, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackVersion = cluster.getCurrentStackVersion();
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        if (hosts != null) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                hostNames.add(host.getHostName());
            }
        }
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(stackVersion.getStackName(), stackVersion.getStackVersion()).forServices(services).forHosts(hostNames).withComponentHostsMap(cluster.getServiceComponentHostMap(null, services)).ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.HOST_GROUPS).build();
        try {
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = stackAdvisorHelper.recommend(request);
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendation = (response == null) ? null : response.getRecommendations();
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = (recommendation == null) ? null : recommendation.getBlueprint();
            java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroups = (blueprint == null) ? null : blueprint.getHostGroups();
            if (hostGroups != null) {
                org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding blueprintBinding = recommendation.getBlueprintClusterBinding();
                java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> bindingMap = new java.util.HashMap<>();
                if (blueprintBinding != null) {
                    java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> bindingHostGroups = blueprintBinding.getHostGroups();
                    if (bindingHostGroups != null) {
                        for (org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup bindingHostGroup : bindingHostGroups) {
                            bindingMap.put(bindingHostGroup.getName(), bindingHostGroup);
                        }
                    }
                }
                java.util.Map<java.lang.String, java.lang.String> clusterHostInfoMap = configurations.computeIfAbsent(org.apache.ambari.server.controller.KerberosHelper.CLUSTER_HOST_INFO, __ -> new java.util.HashMap<>());
                for (org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup : hostGroups) {
                    java.util.Set<java.util.Map<java.lang.String, java.lang.String>> components = hostGroup.getComponents();
                    if (components != null) {
                        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup binding = bindingMap.get(hostGroup.getName());
                        if (binding != null) {
                            java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hostGroupHosts = binding.getHosts();
                            if (hostGroupHosts != null) {
                                for (java.util.Map<java.lang.String, java.lang.String> component : components) {
                                    java.lang.String componentName = component.get("name");
                                    if ((componentFilter == null) || componentFilter.contains(componentName)) {
                                        java.lang.String key = org.apache.ambari.server.utils.StageUtils.getClusterHostInfoKey(componentName);
                                        java.util.Set<java.lang.String> fqdns = new java.util.TreeSet<>();
                                        if (!org.apache.commons.lang.StringUtils.isEmpty(clusterHostInfoMap.get(key))) {
                                            fqdns.addAll(java.util.Arrays.asList(clusterHostInfoMap.get(key).split(",")));
                                        }
                                        for (java.util.Map<java.lang.String, java.lang.String> hostGroupHost : hostGroupHosts) {
                                            java.lang.String fqdn = hostGroupHost.get("fqdn");
                                            if (!org.apache.commons.lang.StringUtils.isEmpty(fqdn)) {
                                                fqdns.add(fqdn);
                                            }
                                        }
                                        clusterHostInfoMap.put(key, org.apache.commons.lang.StringUtils.join(fqdns, ','));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException e) {
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("Failed to obtain the recommended host groups for the preconfigured components.", e);
            throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> applyStackAdvisorUpdates(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> services, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToRemove, boolean kerberosEnabled) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        if (hosts != null) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                hostNames.add(host.getHostName());
            }
        }
        if (!hostNames.isEmpty()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> requestConfigurations = new java.util.HashMap<>();
            if (existingConfigurations != null) {
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configuration : existingConfigurations.entrySet()) {
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
                    java.lang.String configType = configuration.getKey();
                    java.util.Map<java.lang.String, java.lang.String> configurationProperties = configuration.getValue();
                    if (configurationProperties == null) {
                        configurationProperties = java.util.Collections.emptyMap();
                    }
                    if ("cluster-env".equals(configType)) {
                        configurationProperties = new java.util.HashMap<>(configurationProperties);
                        configurationProperties.put("security_enabled", kerberosEnabled ? "true" : "false");
                    }
                    properties.put("properties", configurationProperties);
                    requestConfigurations.put(configType, properties);
                }
            }
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configuration : kerberosConfigurations.entrySet()) {
                java.lang.String configType = configuration.getKey();
                java.util.Map<java.lang.String, java.lang.String> configurationProperties = configuration.getValue();
                if ((configurationProperties != null) && (!configurationProperties.isEmpty())) {
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> requestConfiguration = requestConfigurations.get(configType);
                    if (requestConfiguration == null) {
                        requestConfiguration = new java.util.HashMap<>();
                        requestConfigurations.put(configType, requestConfiguration);
                    }
                    java.util.Map<java.lang.String, java.lang.String> requestConfigurationProperties = requestConfiguration.get("properties");
                    if (requestConfigurationProperties == null) {
                        requestConfigurationProperties = new java.util.HashMap<>();
                    } else {
                        requestConfigurationProperties = new java.util.HashMap<>(requestConfigurationProperties);
                    }
                    requestConfigurationProperties.putAll(configurationProperties);
                    requestConfiguration.put("properties", requestConfigurationProperties);
                }
            }
            java.util.Set<org.apache.ambari.server.state.StackId> visitedStacks = new java.util.HashSet<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> installedServices = cluster.getServices();
            for (java.lang.String serviceName : services) {
                org.apache.ambari.server.state.Service service = installedServices.get(serviceName);
                if (service == null) {
                    continue;
                }
                org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
                if (visitedStacks.contains(stackId)) {
                    continue;
                }
                org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(stackId.getStackName(), stackId.getStackVersion()).forServices(services).forHosts(hostNames).withComponentHostsMap(cluster.getServiceComponentHostMap(null, services)).withConfigurations(requestConfigurations).ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.KERBEROS_CONFIGURATIONS).build();
                try {
                    org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = stackAdvisorHelper.recommend(request);
                    org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendation = (response == null) ? null : response.getRecommendations();
                    org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = (recommendation == null) ? null : recommendation.getBlueprint();
                    java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configurations = (blueprint == null) ? null : blueprint.getConfigurations();
                    if (configurations != null) {
                        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configuration : configurations.entrySet()) {
                            java.lang.String configType = configuration.getKey();
                            java.util.Map<java.lang.String, java.lang.String> recommendedConfigProperties = configuration.getValue().getProperties();
                            java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> recommendedConfigPropertyAttributes = configuration.getValue().getPropertyAttributes();
                            java.util.Map<java.lang.String, java.lang.String> existingConfigProperties = (existingConfigurations == null) ? null : existingConfigurations.get(configType);
                            java.util.Map<java.lang.String, java.lang.String> kerberosConfigProperties = kerberosConfigurations.get(configType);
                            java.util.Set<java.lang.String> ignoreProperties = (propertiesToIgnore == null) ? null : propertiesToIgnore.get(configType);
                            addRecommendedPropertiesForConfigType(kerberosConfigurations, configType, recommendedConfigProperties, existingConfigProperties, kerberosConfigProperties, ignoreProperties);
                            if (recommendedConfigPropertyAttributes != null) {
                                removeRecommendedPropertiesForConfigType(configType, recommendedConfigPropertyAttributes, existingConfigProperties, kerberosConfigurations, ignoreProperties, propertiesToRemove);
                            }
                        }
                    }
                } catch (java.lang.Exception e) {
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
                visitedStacks.add(stackId);
            }
        }
        return kerberosConfigurations;
    }

    private void addRecommendedPropertiesForConfigType(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> recommendedConfigProperties, java.util.Map<java.lang.String, java.lang.String> existingConfigProperties, java.util.Map<java.lang.String, java.lang.String> kerberosConfigProperties, java.util.Set<java.lang.String> ignoreProperties) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : recommendedConfigProperties.entrySet()) {
            java.lang.String propertyName = property.getKey();
            if ((ignoreProperties == null) || (!ignoreProperties.contains(propertyName))) {
                java.lang.String recommendedValue = property.getValue();
                if ((kerberosConfigProperties == null) || (!kerberosConfigProperties.containsKey(propertyName))) {
                    if ((existingConfigProperties == null) || (!existingConfigProperties.containsKey(propertyName))) {
                        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.debug("Adding Kerberos configuration based on StackAdvisor recommendation:" + "\n\tConfigType: {}\n\tProperty: {}\n\tValue: {}", configType, propertyName, recommendedValue);
                        if (kerberosConfigProperties == null) {
                            kerberosConfigProperties = new java.util.HashMap<>();
                            kerberosConfigurations.put(configType, kerberosConfigProperties);
                        }
                        kerberosConfigProperties.put(propertyName, recommendedValue);
                    }
                } else {
                    java.lang.String value = kerberosConfigProperties.get(propertyName);
                    if (value == null ? recommendedValue != null : !value.equals(recommendedValue)) {
                        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.debug("Updating Kerberos configuration based on StackAdvisor recommendation:" + "\n\tConfigType: {}\n\tProperty: {}\n\tOld Value: {}\n\tNew Value: {}", configType, propertyName, value == null ? "" : value, recommendedValue == null ? "" : recommendedValue);
                        kerberosConfigProperties.put(propertyName, recommendedValue);
                    }
                }
            }
        }
    }

    private void removeRecommendedPropertiesForConfigType(java.lang.String configType, java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> recommendedConfigPropertyAttributes, java.util.Map<java.lang.String, java.lang.String> existingConfigProperties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Set<java.lang.String> ignoreProperties, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToRemove) {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> property : recommendedConfigPropertyAttributes.entrySet()) {
            java.lang.String propertyName = property.getKey();
            if ("true".equalsIgnoreCase(property.getValue().getDelete())) {
                java.util.Map<java.lang.String, java.lang.String> kerberosConfigProperties = kerberosConfigurations.get(configType);
                if ((((ignoreProperties == null) || (!ignoreProperties.contains(propertyName))) && ((kerberosConfigProperties == null) || (kerberosConfigProperties.get(propertyName) == null))) && ((existingConfigProperties != null) && existingConfigProperties.containsKey(propertyName))) {
                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.debug("Property to remove from configuration based on StackAdvisor recommendation:" + "\n\tConfigType: {}\n\tProperty: {}", configType, propertyName);
                    if (propertiesToRemove != null) {
                        java.util.Set<java.lang.String> properties = propertiesToRemove.get(configType);
                        if (properties == null) {
                            properties = new java.util.HashSet<>();
                            propertiesToRemove.put(configType, properties);
                        }
                        properties.add(propertyName);
                    } else {
                        if (kerberosConfigProperties == null) {
                            kerberosConfigProperties = new java.util.HashMap<>();
                            kerberosConfigurations.put(configType, kerberosConfigProperties);
                        }
                        kerberosConfigProperties.put(propertyName, "");
                    }
                }
            }
        }
    }

    @java.lang.Override
    public boolean ensureHeadlessIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Set<java.lang.String> services) throws org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.KerberosDetails kerberosDetails = getKerberosDetails(cluster, null);
        if (kerberosDetails.manageIdentities()) {
            org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor(cluster, false);
            java.util.Map<java.lang.String, java.lang.String> kerberosDescriptorProperties = kerberosDescriptor.getProperties();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = addAdditionalConfigurations(cluster, deepCopy(existingConfigurations), null, kerberosDescriptorProperties, null);
            java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration = kerberosDetails.getKerberosEnvProperties();
            org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler kerberosOperationHandler = kerberosOperationHandlerFactory.getKerberosOperationHandler(kerberosDetails.getKdcType());
            org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential = getKDCAdministratorCredentials(cluster.getClusterName());
            try {
                kerberosOperationHandler.open(administratorCredential, kerberosDetails.getDefaultRealm(), kerberosConfiguration);
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                java.lang.String message = java.lang.String.format("Failed to process the identities, could not properly open the KDC operation handler: %s", e.getMessage());
                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
                throw new org.apache.ambari.server.AmbariException(message, e);
            }
            java.util.Map<java.lang.String, java.lang.Object> filterContext = new java.util.HashMap<>();
            filterContext.put("configurations", configurations);
            filterContext.put("services", services);
            for (java.lang.String serviceName : services) {
                org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosDescriptor.getService(serviceName);
                if (serviceDescriptor != null) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
                    if (null != componentDescriptors) {
                        for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
                            if (componentDescriptor != null) {
                                java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors;
                                identityDescriptors = serviceDescriptor.getIdentities(true, filterContext);
                                if (identityDescriptors != null) {
                                    for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
                                        createIdentity(identityDescriptor, org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, kerberosConfiguration, kerberosOperationHandler, configurations, null);
                                    }
                                }
                                identityDescriptors = componentDescriptor.getIdentities(true, filterContext);
                                if (identityDescriptors != null) {
                                    for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
                                        createIdentity(identityDescriptor, org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, kerberosConfiguration, kerberosOperationHandler, configurations, null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (kerberosDetails.createAmbariPrincipal()) {
                installAmbariIdentities(kerberosDescriptor, kerberosOperationHandler, kerberosConfiguration, configurations, kerberosDetails);
            }
            try {
                kerberosOperationHandler.close();
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
            }
        }
        return true;
    }

    private void installAmbariIdentities(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler kerberosOperationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, org.apache.ambari.server.controller.KerberosDetails kerberosDetails) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> ambariIdentities = getAmbariServerIdentities(kerberosDescriptor);
        if (!ambariIdentities.isEmpty()) {
            java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : ambariIdentities) {
                if (identity != null) {
                    org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principal = identity.getPrincipalDescriptor();
                    if (principal != null) {
                        boolean updateJAASFile = org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_KERBEROS_IDENTITY_NAME.equals(identity.getName());
                        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = createIdentity(identity, principal.getType(), kerberosEnvProperties, kerberosOperationHandler, configurations, ambariServerHostname);
                        installAmbariIdentity(identity, keytab, configurations, ambariServerHostname, kerberosDetails, updateJAASFile);
                        if (updateJAASFile) {
                            try {
                                org.apache.ambari.server.controller.utilities.KerberosChecker.checkJaasConfiguration();
                            } catch (org.apache.ambari.server.AmbariException e) {
                                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("Error in Ambari JAAS configuration: " + e.getLocalizedMessage(), e);
                            }
                        }
                    }
                }
            }
        }
    }

    private void installAmbariIdentity(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor ambariServerIdentity, org.apache.directory.server.kerberos.shared.keytab.Keytab keytab, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String hostname, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, boolean updateJAASFile) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = ambariServerIdentity.getPrincipalDescriptor();
        if (principalDescriptor != null) {
            java.lang.String principal = variableReplacementHelper.replaceVariables(principalDescriptor.getValue(), configurations);
            if (!org.apache.commons.lang.StringUtils.isEmpty(hostname)) {
                principal = principal.replace("_HOST", hostname);
            }
            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = ambariServerIdentity.getKeytabDescriptor();
            if (keytabDescriptor != null) {
                java.lang.String destKeytabFilePath = variableReplacementHelper.replaceVariables(keytabDescriptor.getFile(), configurations);
                java.io.File destKeytabFile = new java.io.File(destKeytabFilePath);
                org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction configureAmbariIdentitiesServerAction = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class);
                if (keytab != null) {
                    try {
                        org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler = kerberosOperationHandlerFactory.getKerberosOperationHandler(kerberosDetails.getKdcType());
                        java.io.File tmpKeytabFile = createTemporaryFile();
                        try {
                            if ((operationHandler != null) && operationHandler.createKeytabFile(keytab, tmpKeytabFile)) {
                                java.lang.String ownerName = variableReplacementHelper.replaceVariables(keytabDescriptor.getOwnerName(), configurations);
                                java.lang.String ownerAccess = keytabDescriptor.getOwnerAccess();
                                java.lang.String groupName = variableReplacementHelper.replaceVariables(keytabDescriptor.getGroupName(), configurations);
                                java.lang.String groupAccess = keytabDescriptor.getGroupAccess();
                                java.lang.String componentName = (principal.contains(org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_KERBEROS_IDENTITY_NAME)) ? "AMBARI_SERVER_SELF" : org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name();
                                org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedKerberosPrincipal = new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal(null, hostname, principal, false, null, org.apache.ambari.server.controller.RootService.AMBARI.name(), componentName, destKeytabFilePath);
                                configureAmbariIdentitiesServerAction.installAmbariServerIdentity(resolvedKerberosPrincipal, tmpKeytabFile.getAbsolutePath(), destKeytabFilePath, ownerName, ownerAccess, groupName, groupAccess, null);
                                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.debug("Successfully created keytab file for {} at {}", principal, destKeytabFile.getAbsolutePath());
                            } else {
                                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("Failed to create keytab file for {} at {}", principal, destKeytabFile.getAbsolutePath());
                            }
                        } finally {
                            tmpKeytabFile.delete();
                        }
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to create keytab file for %s at %s: %s:", principal, destKeytabFile.getAbsolutePath(), e.getLocalizedMessage()), e);
                    }
                } else {
                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("No keytab data is available to create the keytab file for {} at {}", principal, destKeytabFile.getAbsolutePath());
                }
                if (updateJAASFile) {
                    configureAmbariIdentitiesServerAction.configureJAAS(principal, destKeytabFile.getAbsolutePath(), null);
                }
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer createTestIdentity(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> commandParamsStage, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException, org.apache.ambari.server.AmbariException {
        return handleTestIdentity(cluster, getKerberosDetails(cluster, null), commandParamsStage, requestStageContainer, new org.apache.ambari.server.controller.KerberosHelperImpl.CreatePrincipalsAndKeytabsHandler(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.DEFAULT, org.apache.ambari.server.controller.UpdateConfigurationPolicy.NONE, false, false));
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.RequestStageContainer deleteTestIdentity(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> commandParamsStage, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException, org.apache.ambari.server.AmbariException {
        requestStageContainer = handleTestIdentity(cluster, getKerberosDetails(cluster, null), commandParamsStage, requestStageContainer, new org.apache.ambari.server.controller.KerberosHelperImpl.DeletePrincipalsAndKeytabsHandler());
        return requestStageContainer;
    }

    @java.lang.Override
    public void validateKDCCredentials(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException, org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException, org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException {
        validateKDCCredentials(null, cluster);
    }

    @java.lang.Override
    public void setAuthToLocalRules(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.lang.String realm, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, boolean includePreconfigureData) throws org.apache.ambari.server.AmbariException {
        boolean processAuthToLocalRules = true;
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = existingConfigurations.get(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV);
        if (kerberosEnvProperties.containsKey(org.apache.ambari.server.controller.KerberosHelper.MANAGE_AUTH_TO_LOCAL_RULES)) {
            processAuthToLocalRules = java.lang.Boolean.valueOf(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.MANAGE_AUTH_TO_LOCAL_RULES));
        }
        if ((kerberosDescriptor != null) && processAuthToLocalRules) {
            java.util.Set<java.lang.String> authToLocalProperties;
            java.util.Set<java.lang.String> authToLocalPropertiesToSet = new java.util.HashSet<>();
            boolean caseInsensitiveUser = java.lang.Boolean.valueOf(existingConfigurations.get(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV).get(org.apache.ambari.server.controller.KerberosHelper.CASE_INSENSITIVE_USERNAME_RULES));
            java.lang.String additionalRealms = kerberosDescriptor.getProperty("additional_realms");
            java.util.Map<java.lang.String, java.lang.Object> filterContext = new java.util.HashMap<>();
            filterContext.put("configurations", existingConfigurations);
            filterContext.put("services", installedServices.keySet());
            org.apache.ambari.server.controller.AuthToLocalBuilder authToLocalBuilder = new org.apache.ambari.server.controller.AuthToLocalBuilder(realm, additionalRealms, caseInsensitiveUser);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements = (includePreconfigureData) ? addConfigurationsForPreProcessedServices(deepCopy(existingConfigurations), cluster, kerberosDescriptor, false) : existingConfigurations;
            addIdentities(authToLocalBuilder, kerberosDescriptor.getIdentities(true, filterContext), null, replacements);
            authToLocalProperties = kerberosDescriptor.getAuthToLocalProperties();
            if (authToLocalProperties != null) {
                authToLocalPropertiesToSet.addAll(authToLocalProperties);
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = kerberosDescriptor.getServices();
            if (serviceDescriptors != null) {
                final boolean includeAllComponents = java.lang.Boolean.valueOf(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.INCLUDE_ALL_COMPONENTS_IN_AUTH_TO_LOCAL_RULES));
                for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
                    java.lang.String serviceName = serviceDescriptor.getName();
                    boolean preconfigure = includePreconfigureData && serviceDescriptor.shouldPreconfigure();
                    boolean explicitlyAdded = installedServices.containsKey(serviceName);
                    if (preconfigure || explicitlyAdded) {
                        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.info("Adding identities for service {} to auth to local mapping [{}]", serviceName, explicitlyAdded ? "explicit" : "preconfigured");
                        addIdentities(authToLocalBuilder, serviceDescriptor.getIdentities(true, filterContext), null, replacements);
                        authToLocalProperties = serviceDescriptor.getAuthToLocalProperties();
                        if (authToLocalProperties != null) {
                            authToLocalPropertiesToSet.addAll(authToLocalProperties);
                        }
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
                        if (componentDescriptors != null) {
                            java.util.Set<java.lang.String> installedServiceComponents = installedServices.get(serviceName);
                            if (installedServiceComponents == null) {
                                installedServiceComponents = java.util.Collections.emptySet();
                            }
                            for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
                                java.lang.String componentName = componentDescriptor.getName();
                                if ((preconfigure || includeAllComponents) || installedServiceComponents.contains(componentName)) {
                                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.info("Adding identities for component {} to auth to local mapping", componentName);
                                    addIdentities(authToLocalBuilder, componentDescriptor.getIdentities(true, filterContext), null, replacements);
                                    authToLocalProperties = componentDescriptor.getAuthToLocalProperties();
                                    if (authToLocalProperties != null) {
                                        authToLocalPropertiesToSet.addAll(authToLocalProperties);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!authToLocalPropertiesToSet.isEmpty()) {
                for (java.lang.String authToLocalProperty : authToLocalPropertiesToSet) {
                    java.util.regex.Matcher m = org.apache.ambari.server.state.kerberos.KerberosDescriptor.AUTH_TO_LOCAL_PROPERTY_SPECIFICATION_PATTERN.matcher(authToLocalProperty);
                    if (m.matches()) {
                        org.apache.ambari.server.controller.AuthToLocalBuilder builder;
                        try {
                            builder = ((org.apache.ambari.server.controller.AuthToLocalBuilder) (authToLocalBuilder.clone()));
                        } catch (java.lang.CloneNotSupportedException e) {
                            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("Failed to clone the AuthToLocalBuilder: " + e.getLocalizedMessage(), e);
                            throw new org.apache.ambari.server.AmbariException("Failed to clone the AuthToLocalBuilder: " + e.getLocalizedMessage(), e);
                        }
                        java.lang.String configType = m.group(1);
                        java.lang.String propertyName = m.group(2);
                        if (configType == null) {
                            configType = "";
                        }
                        java.util.Map<java.lang.String, java.lang.String> existingConfiguration = existingConfigurations.get(configType);
                        if (existingConfiguration != null) {
                            builder.addRules(existingConfiguration.get(propertyName));
                        }
                        java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration = kerberosConfigurations.get(configType);
                        if (kerberosConfiguration != null) {
                            builder.addRules(kerberosConfiguration.get(propertyName));
                        } else {
                            kerberosConfiguration = new java.util.HashMap<>();
                            kerberosConfigurations.put(configType, kerberosConfiguration);
                        }
                        kerberosConfiguration.put(propertyName, builder.generate(org.apache.ambari.server.controller.AuthToLocalBuilder.ConcatenationType.translate(m.group(3))));
                    }
                }
            }
        }
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHostsToProcess(final org.apache.ambari.server.state.Cluster cluster, final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, final java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, final java.util.Collection<java.lang.String> hostFilter) throws org.apache.ambari.server.AmbariException {
        return getServiceComponentHosts(cluster, new org.apache.ambari.server.controller.KerberosHelper.Command<java.lang.Boolean, org.apache.ambari.server.state.ServiceComponentHost>() {
            @java.lang.Override
            public java.lang.Boolean invoke(org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
                if (sch != null) {
                    if (((hostFilter == null) || hostFilter.contains("*")) || hostFilter.contains(sch.getHostName())) {
                        java.lang.String serviceName = sch.getServiceName();
                        if (((serviceComponentFilter == null) || serviceComponentFilter.containsKey("*")) || serviceComponentFilter.containsKey(serviceName)) {
                            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosDescriptor.getService(serviceName);
                            if (serviceDescriptor != null) {
                                java.util.Collection<java.lang.String> componentFilter = ((serviceComponentFilter == null) || serviceComponentFilter.containsKey("*")) ? null : serviceComponentFilter.get(serviceName);
                                return ((componentFilter == null) || componentFilter.contains("*")) || componentFilter.contains(sch.getServiceComponentName());
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    private java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.KerberosHelper.Command<java.lang.Boolean, org.apache.ambari.server.state.ServiceComponentHost> shouldIncludeCommand) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHostsToProcess = new java.util.ArrayList<>();
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        if ((hosts != null) && (!hosts.isEmpty())) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                java.lang.String hostname = host.getHostName();
                java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = cluster.getServiceComponentHosts(hostname);
                if ((serviceComponentHosts != null) && (!serviceComponentHosts.isEmpty())) {
                    for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHosts) {
                        if ((shouldIncludeCommand == null) || shouldIncludeCommand.invoke(sch)) {
                            serviceComponentHostsToProcess.add(sch);
                        }
                    }
                }
            }
        }
        return serviceComponentHostsToProcess;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getHostsWithValidKerberosClient(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> hostsWithValidKerberosClient = new java.util.HashSet<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schKerberosClients = cluster.getServiceComponentHosts(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), org.apache.ambari.server.Role.KERBEROS_CLIENT.name());
        if (schKerberosClients != null) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : schKerberosClients) {
                if (sch.getState() == org.apache.ambari.server.state.State.INSTALLED) {
                    hostsWithValidKerberosClient.add(sch.getHostName());
                }
            }
        }
        return hostsWithValidKerberosClient;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.state.Cluster cluster, boolean includePreconfigureData, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        return getKerberosDescriptor(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.COMPOSITE, cluster, false, null, includePreconfigureData, userDescriptor, desiredConfigs);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.state.Cluster cluster, boolean includePreconfigureData) throws org.apache.ambari.server.AmbariException {
        return getKerberosDescriptor(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.COMPOSITE, cluster, false, null, includePreconfigureData, null, null);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType kerberosDescriptorType, org.apache.ambari.server.state.Cluster cluster, boolean evaluateWhenClauses, java.util.Collection<java.lang.String> additionalServices, boolean includePreconfigureData, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = cluster.getDesiredStackVersion();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor(kerberosDescriptorType, cluster, stackId, includePreconfigureData, userDescriptor);
        if (evaluateWhenClauses) {
            java.util.Set<java.lang.String> services = new java.util.HashSet<>(cluster.getServices().keySet());
            if (additionalServices != null) {
                services.addAll(additionalServices);
            }
            java.util.Map<java.lang.String, java.lang.Object> context = new java.util.HashMap<>();
            context.put("configurations", calculateConfigurations(cluster, null, kerberosDescriptor, false, false, desiredConfigs));
            context.put("services", services);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> identitiesToRemove = processWhenClauses("", kerberosDescriptor, context, new java.util.HashMap<>());
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> identity : identitiesToRemove.entrySet()) {
                java.lang.String[] path = identity.getKey().split("/");
                org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer container = null;
                for (java.lang.String name : path) {
                    if (container == null) {
                        container = kerberosDescriptor;
                    } else {
                        container = container.getChildContainer(name);
                        if (container == null) {
                            break;
                        }
                    }
                }
                if (container != null) {
                    for (java.lang.String identityName : identity.getValue()) {
                        container.removeIdentity(identityName);
                    }
                }
            }
        }
        return kerberosDescriptor;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType kerberosDescriptorType, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, boolean includePreconfigureData, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor stackDescriptor = ((kerberosDescriptorType == org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.STACK) || (kerberosDescriptorType == org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.COMPOSITE)) ? getKerberosDescriptorFromStack(stackId, includePreconfigureData) : null;
        org.apache.ambari.server.state.kerberos.KerberosDescriptor finalUserDescriptor = ((kerberosDescriptorType == org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.USER) || (kerberosDescriptorType == org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.COMPOSITE)) ? userDescriptor == null ? getKerberosDescriptorUpdates(cluster) : userDescriptor : null;
        return combineKerberosDescriptors(stackDescriptor, finalUserDescriptor);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mergeConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> updates, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements, java.util.Set<java.lang.String> configurationTypeFilter) throws org.apache.ambari.server.AmbariException {
        if ((updates != null) && (!updates.isEmpty())) {
            if (configurations == null) {
                configurations = new java.util.HashMap<>();
            }
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> entry : updates.entrySet()) {
                java.lang.String type = entry.getKey();
                if ((configurationTypeFilter == null) || configurationTypeFilter.contains(type)) {
                    org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configurationDescriptor = entry.getValue();
                    if (configurationDescriptor != null) {
                        java.util.Map<java.lang.String, java.lang.String> updatedProperties = configurationDescriptor.getProperties();
                        mergeConfigurations(configurations, type, updatedProperties, replacements);
                    }
                }
            }
        }
        return configurations;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> processPreconfiguredServiceConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) throws org.apache.ambari.server.AmbariException {
        if (kerberosDescriptor == null) {
            kerberosDescriptor = getKerberosDescriptor(cluster, true);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = kerberosDescriptor.getServices();
        if (serviceDescriptors != null) {
            if (configurations == null) {
                configurations = new java.util.HashMap<>();
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacementsWithDefaults = addConfigurationsForPreProcessedServices(deepCopy(replacements), cluster, kerberosDescriptor, true);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> existingServices = cluster.getServices();
            for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
                java.lang.String serviceName = serviceDescriptor.getName();
                boolean shouldPreconfigure = serviceDescriptor.shouldPreconfigure();
                if ((!existingServices.containsKey(serviceName)) && shouldPreconfigure) {
                    configurations = mergeConfigurations(configurations, serviceDescriptor.getConfigurations(), replacementsWithDefaults, replacements.keySet());
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
                    if (componentDescriptors != null) {
                        for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
                            configurations = mergeConfigurations(configurations, componentDescriptor.getConfigurations(), replacementsWithDefaults, replacements.keySet());
                        }
                    }
                }
            }
        }
        return configurations;
    }

    @java.lang.Override
    public int addIdentities(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter kerberosIdentityDataFileWriter, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.Collection<java.lang.String> identityFilter, java.lang.String hostname, java.lang.Long hostId, java.lang.String serviceName, java.lang.String componentName, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> resolvedKeytabs, java.lang.String realm) throws java.io.IOException {
        int identitiesAdded = 0;
        if (identities != null) {
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                if ((identityFilter == null) || identityFilter.contains(identity.getPath())) {
                    org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = identity.getPrincipalDescriptor();
                    java.lang.String principal = null;
                    java.lang.String principalType = null;
                    java.lang.String principalConfiguration = null;
                    if (principalDescriptor != null) {
                        principal = variableReplacementHelper.replaceVariables(principalDescriptor.getValue(), configurations);
                        principalType = org.apache.ambari.server.state.kerberos.KerberosPrincipalType.translate(principalDescriptor.getType());
                        principalConfiguration = variableReplacementHelper.replaceVariables(principalDescriptor.getConfiguration(), configurations);
                    }
                    if (principal != null) {
                        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = identity.getKeytabDescriptor();
                        java.lang.String keytabFilePath = null;
                        java.lang.String keytabFileOwnerName = null;
                        java.lang.String keytabFileOwnerAccess = null;
                        java.lang.String keytabFileGroupName = null;
                        java.lang.String keytabFileGroupAccess = null;
                        java.lang.String keytabFileConfiguration = null;
                        if (keytabDescriptor != null) {
                            keytabFilePath = variableReplacementHelper.replaceVariables(keytabDescriptor.getFile(), configurations);
                            keytabFileOwnerName = variableReplacementHelper.replaceVariables(keytabDescriptor.getOwnerName(), configurations);
                            keytabFileOwnerAccess = variableReplacementHelper.replaceVariables(keytabDescriptor.getOwnerAccess(), configurations);
                            keytabFileGroupName = variableReplacementHelper.replaceVariables(keytabDescriptor.getGroupName(), configurations);
                            keytabFileGroupAccess = variableReplacementHelper.replaceVariables(keytabDescriptor.getGroupAccess(), configurations);
                            keytabFileConfiguration = variableReplacementHelper.replaceVariables(keytabDescriptor.getConfiguration(), configurations);
                            if ((keytabFileOwnerName == null) || (keytabFileGroupName == null)) {
                                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn("Missing owner ({}) or group name ({}) of kerberos descriptor {}", keytabFileOwnerName, keytabFileGroupName, keytabDescriptor.getName());
                            }
                        } else {
                            throw new org.apache.ambari.server.AmbariException("Missing keytab descriptor for " + identity.getName());
                        }
                        java.lang.String evaluatedPrincipal = principal.replace("_HOST", hostname).replace("_REALM", realm);
                        org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKeytab = new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab(keytabFilePath, keytabFileOwnerName, keytabFileOwnerAccess, keytabFileGroupName, keytabFileGroupAccess, com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal(hostId, hostname, evaluatedPrincipal, "service".equalsIgnoreCase(principalType), null, serviceName, componentName, keytabFilePath)), serviceName.equalsIgnoreCase(org.apache.ambari.server.controller.RootService.AMBARI.name()), componentName.equalsIgnoreCase("AMBARI_SERVER_SELF"));
                        if (resolvedKeytabs.containsKey(keytabFilePath)) {
                            org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab sameKeytab = resolvedKeytabs.get(keytabFilePath);
                            boolean differentOwners = false;
                            java.lang.String warnTemplate = "Keytab '{}' on host '{}' has different {}, originally set to '{}' and '{}:{}' has '{}', using '{}'";
                            if (!org.apache.commons.lang.StringUtils.equals(resolvedKeytab.getOwnerName(), sameKeytab.getOwnerName())) {
                                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn(warnTemplate, keytabFilePath, hostname, "owners", sameKeytab.getOwnerName(), serviceName, componentName, resolvedKeytab.getOwnerName(), sameKeytab.getOwnerName());
                                differentOwners = true;
                            }
                            if (!org.apache.commons.lang.StringUtils.equals(resolvedKeytab.getOwnerAccess(), sameKeytab.getOwnerAccess())) {
                                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn(warnTemplate, keytabFilePath, hostname, "owner access", sameKeytab.getOwnerAccess(), serviceName, componentName, resolvedKeytab.getOwnerAccess(), sameKeytab.getOwnerAccess());
                            }
                            if (!org.apache.commons.lang.StringUtils.equals(resolvedKeytab.getGroupName(), sameKeytab.getGroupName())) {
                                if (differentOwners) {
                                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(warnTemplate, keytabFilePath, hostname, "groups", sameKeytab.getGroupName(), serviceName, componentName, resolvedKeytab.getGroupName(), sameKeytab.getGroupName());
                                } else {
                                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn(warnTemplate, keytabFilePath, hostname, "groups", sameKeytab.getGroupName(), serviceName, componentName, resolvedKeytab.getGroupName(), sameKeytab.getGroupName());
                                }
                            }
                            if (!org.apache.commons.lang.StringUtils.equals(resolvedKeytab.getGroupAccess(), sameKeytab.getGroupAccess())) {
                                if (differentOwners) {
                                    if (!sameKeytab.getGroupAccess().contains("r")) {
                                        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("Keytab '{}' on host '{}' referenced by multiple identities which have different owners," + ("but 'r' attribute missing for group. Make sure all users (that need this keytab) are in '{}' +" + "group and keytab can be read by this group"), keytabFilePath, hostname, sameKeytab.getGroupName());
                                    }
                                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(warnTemplate, keytabFilePath, hostname, "group access", sameKeytab.getGroupAccess(), serviceName, componentName, resolvedKeytab.getGroupAccess(), sameKeytab.getGroupAccess());
                                } else {
                                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn(warnTemplate, keytabFilePath, hostname, "group access", sameKeytab.getGroupAccess(), serviceName, componentName, resolvedKeytab.getGroupAccess(), sameKeytab.getGroupAccess());
                                }
                            }
                            sameKeytab.mergePrincipals(resolvedKeytab);
                            if (sameKeytab.isMustWriteAmbariJaasFile() || resolvedKeytab.isMustWriteAmbariJaasFile()) {
                                sameKeytab.setMustWriteAmbariJaasFile(true);
                            }
                            if (sameKeytab.isAmbariServerKeytab() || resolvedKeytab.isAmbariServerKeytab()) {
                                sameKeytab.setAmbariServerKeytab(true);
                            }
                        } else {
                            resolvedKeytabs.put(keytabFilePath, resolvedKeytab);
                            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.info("Keytab {} owner:'{}:{}', group:'{}:{}' is defined", keytabFilePath, keytabFileOwnerName, keytabFileOwnerAccess, keytabFileGroupName, keytabFileGroupAccess);
                        }
                        if (kerberosIdentityDataFileWriter != null) {
                            kerberosIdentityDataFileWriter.writeRecord(hostname, serviceName, componentName, evaluatedPrincipal, principalType, keytabFilePath, keytabFileOwnerName, keytabFileOwnerAccess, keytabFileGroupName, keytabFileGroupAccess, "true");
                        }
                        mergeConfiguration(kerberosConfigurations, principalConfiguration, principal, null);
                        mergeConfiguration(kerberosConfigurations, keytabFileConfiguration, keytabFilePath, null);
                        identitiesAdded++;
                    }
                }
            }
        }
        return identitiesAdded;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, boolean includePreconfigureData, boolean calculateClusterHostInfo, java.util.Map<java.lang.String, java.lang.String> componentHosts, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        return calculateConfigurations(cluster, hostname, kerberosDescriptor, null, includePreconfigureData, calculateClusterHostInfo, componentHosts, desiredConfigs);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, boolean includePreconfigureData, boolean calculateClusterHostInfo, java.util.Map<java.lang.String, java.lang.String> componentHosts, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculatedConfigurations = addAdditionalConfigurations(cluster, configHelper.calculateExistingConfigurations(ambariManagementController, cluster, hostname, desiredConfigs), hostname, kerberosDescriptor == null ? null : kerberosDescriptor.getProperties(), userDescriptor, componentHosts, desiredConfigs);
        if (includePreconfigureData) {
            calculatedConfigurations = addConfigurationsForPreProcessedServices(calculatedConfigurations, cluster, kerberosDescriptor, calculateClusterHostInfo);
        }
        return calculatedConfigurations;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, boolean includePreconfigureData, boolean calculateClusterHostInfo, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        return calculateConfigurations(cluster, hostname, kerberosDescriptor, includePreconfigureData, calculateClusterHostInfo, null, desiredConfigs);
    }

    private java.util.Map<java.lang.String, java.lang.String> principalNames(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configuration, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> result = new java.util.HashMap<>();
        getKerberosDescriptor(cluster, false, userDescriptor, desiredConfigs).principals().forEach((key, value) -> {
            result.put(key, variableReplacementHelper.replaceVariables(value, configuration));
        });
        return result;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> getActiveIdentities(java.lang.String clusterName, java.lang.String hostName, java.lang.String serviceName, java.lang.String componentName, boolean replaceHostNames, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> hostConfigurations, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        if ((clusterName == null) || clusterName.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Invalid argument, cluster name is required");
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        if (cluster == null) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The cluster object for the cluster name %s is not available", clusterName));
        }
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> activeIdentities = new java.util.HashMap<>();
        org.apache.ambari.server.state.Config kerberosEnvConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV, desiredConfigs);
        if (kerberosEnvConfig == null) {
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.debug("Calculating the active identities for {} is being skipped since the kerberos-env configuration is not available", clusterName, cluster.getSecurityType().name(), org.apache.ambari.server.state.SecurityType.KERBEROS.name());
            return activeIdentities;
        }
        java.util.Collection<java.lang.String> hosts;
        java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
        boolean ambariServerHostnameIsForced = false;
        if (hostName == null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostMap = clusters.getHostsForCluster(clusterName);
            hosts = (hostMap == null) ? java.util.Collections.emptySet() : hostMap.keySet();
            if (!hosts.contains(ambariServerHostname)) {
                java.util.Collection<java.lang.String> extendedHosts = new java.util.ArrayList<>(hosts.size() + 1);
                extendedHosts.addAll(hosts);
                extendedHosts.add(ambariServerHostname);
                hosts = extendedHosts;
                ambariServerHostnameIsForced = true;
            }
        } else {
            hosts = java.util.Collections.singleton(hostName);
        }
        if (null == hostConfigurations) {
            hostConfigurations = new java.util.HashMap<>();
        }
        if (hosts.isEmpty()) {
            return activeIdentities;
        }
        if (null == kerberosDescriptor) {
            kerberosDescriptor = getKerberosDescriptor(cluster, false);
        }
        if (kerberosDescriptor == null) {
            return activeIdentities;
        }
        java.util.Set<java.lang.String> existingServices = cluster.getServices().keySet();
        for (java.lang.String host : hosts) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = hostConfigurations.get(host);
            if (configurations == null) {
                configurations = calculateConfigurations(cluster, ambariServerHostnameIsForced && ambariServerHostname.equals(host) ? null : host, kerberosDescriptor, false, false, desiredConfigs);
                hostConfigurations.put(host, configurations);
            }
            java.util.Map<java.lang.String, java.lang.Object> filterContext = new java.util.HashMap<>();
            filterContext.put("configurations", configurations);
            filterContext.put("services", existingServices);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> hostActiveIdentities = new java.util.HashMap<>();
            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = getActiveIdentities(cluster, host, serviceName, componentName, kerberosDescriptor, filterContext);
            if (host.equals(ambariServerHostname)) {
                if (createAmbariIdentities(kerberosEnvConfig.getProperties())) {
                    java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> ambariIdentities = getAmbariServerIdentities(kerberosDescriptor);
                    if (ambariIdentities != null) {
                        identities.addAll(ambariIdentities);
                    }
                }
            }
            if (!identities.isEmpty()) {
                for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                    org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = identity.getPrincipalDescriptor();
                    java.lang.String principal = null;
                    if (principalDescriptor != null) {
                        principal = variableReplacementHelper.replaceVariables(principalDescriptor.getValue(), configurations);
                    }
                    if (principal != null) {
                        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = identity.getKeytabDescriptor();
                        java.lang.String keytabFile = null;
                        if (keytabDescriptor != null) {
                            keytabFile = variableReplacementHelper.replaceVariables(keytabDescriptor.getFile(), configurations);
                        }
                        if (replaceHostNames) {
                            principal = principal.replace("_HOST", host);
                        }
                        java.lang.String uniqueKey = java.lang.String.format("%s|%s", principal, keytabFile == null ? "" : keytabFile);
                        if ((!hostActiveIdentities.containsKey(uniqueKey)) || (org.apache.commons.lang.StringUtils.isNotBlank(hostActiveIdentities.get(uniqueKey).getReference()) && org.apache.commons.lang.StringUtils.isBlank(identity.getReference()))) {
                            org.apache.ambari.server.state.kerberos.KerberosPrincipalType principalType = principalDescriptor.getType();
                            if (principalType == null) {
                                principalType = org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE;
                            }
                            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor resolvedPrincipalDescriptor = new org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor(principal, principalType, variableReplacementHelper.replaceVariables(principalDescriptor.getConfiguration(), configurations), variableReplacementHelper.replaceVariables(principalDescriptor.getLocalUsername(), configurations));
                            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor resolvedKeytabDescriptor;
                            if (keytabFile == null) {
                                resolvedKeytabDescriptor = null;
                            } else {
                                resolvedKeytabDescriptor = new org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor(keytabFile, variableReplacementHelper.replaceVariables(keytabDescriptor.getOwnerName(), configurations), variableReplacementHelper.replaceVariables(keytabDescriptor.getOwnerAccess(), configurations), variableReplacementHelper.replaceVariables(keytabDescriptor.getGroupName(), configurations), variableReplacementHelper.replaceVariables(keytabDescriptor.getGroupAccess(), configurations), variableReplacementHelper.replaceVariables(keytabDescriptor.getConfiguration(), configurations), keytabDescriptor.isCachable());
                            }
                            hostActiveIdentities.put(uniqueKey, new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(identity.getName(), identity.getReference(), resolvedPrincipalDescriptor, resolvedKeytabDescriptor, identity.getWhen()));
                        }
                    }
                }
            }
            activeIdentities.put(host, hostActiveIdentities.values());
        }
        return activeIdentities;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getAmbariServerIdentities(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> ambariIdentities = new java.util.ArrayList<>();
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor ambariKerberosDescriptor = kerberosDescriptor.getService(org.apache.ambari.server.controller.RootService.AMBARI.name());
        if (ambariKerberosDescriptor != null) {
            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities = ambariKerberosDescriptor.getIdentities(true, null);
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor ambariServerKerberosComponentDescriptor = ambariKerberosDescriptor.getComponent(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name());
            if (serviceIdentities != null) {
                ambariIdentities.addAll(serviceIdentities);
            }
            if (ambariServerKerberosComponentDescriptor != null) {
                java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities = ambariServerKerberosComponentDescriptor.getIdentities(true, null);
                if (componentIdentities != null) {
                    ambariIdentities.addAll(componentIdentities);
                }
            }
        }
        return ambariIdentities;
    }

    @java.lang.Override
    public boolean createAmbariIdentities(java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties) {
        return (kerberosEnvProperties == null) || (!"false".equalsIgnoreCase(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL)));
    }

    @java.lang.Override
    public org.apache.ambari.server.security.credential.PrincipalKeyCredential getKDCAdministratorCredentials(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.credential.Credential credentials = credentialStoreService.getCredential(clusterName, org.apache.ambari.server.controller.KerberosHelper.KDC_ADMINISTRATOR_CREDENTIAL_ALIAS);
        if (credentials instanceof org.apache.ambari.server.security.credential.PrincipalKeyCredential) {
            return ((org.apache.ambari.server.security.credential.PrincipalKeyCredential) (credentials));
        } else {
            return null;
        }
    }

    @com.google.inject.persist.Transactional
    public void createResolvedKeytab(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab, @javax.annotation.Nullable
    java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> keytabList) {
        com.google.common.base.Stopwatch stopwatch = com.google.common.base.Stopwatch.createStarted();
        org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke = kerberosKeytabDAO.findOrCreate(resolvedKerberosKeytab);
        resolvedKerberosKeytab.getPrincipals().forEach(principal -> {
            org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kpe = kerberosPrincipalDAO.find(principal.getPrincipal());
            if (kpe == null) {
                kpe = kerberosPrincipalDAO.create(principal.getPrincipal(), principal.isService());
            }
            final java.lang.Boolean[] mergeBidirectionalAssociatedEntities = new java.lang.Boolean[]{ false };
            org.apache.ambari.server.orm.entities.KerberosPrincipalEntity finalKpe = kpe;
            principal.getServiceMapping().forEach((serviceName, value) -> {
                org.apache.ambari.server.orm.entities.HostEntity hostEntity = (principal.getHostId() != null) ? hostDAO.findById(principal.getHostId()) : null;
                org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult result = kerberosKeytabPrincipalDAO.findOrCreate(kke, hostEntity, finalKpe, keytabList);
                org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkp = result.kkp;
                if ((keytabList != null) && result.created) {
                    keytabList.add(result.kkp);
                }
                mergeBidirectionalAssociatedEntities[0] = mergeBidirectionalAssociatedEntities[0] || result.created;
                if (kkp.putServiceMapping(serviceName, value)) {
                    kerberosKeytabPrincipalDAO.merge(kkp);
                }
            });
            if (mergeBidirectionalAssociatedEntities[0]) {
                kerberosKeytabDAO.merge(kke);
                kerberosPrincipalDAO.merge(kpe);
            }
        });
        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.info("Resolving this keytab and all associated principals took {} ms ", stopwatch.elapsed(java.util.concurrent.TimeUnit.MILLISECONDS));
    }

    @java.lang.Override
    public void removeStaleKeytabs(java.util.Collection<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> expectedKeytabs) {
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> translateConfigurationSpecifications(java.util.Collection<java.lang.String> configurationSpecifications) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> translation = null;
        if (configurationSpecifications != null) {
            translation = new java.util.HashMap<>();
            for (java.lang.String configurationSpecification : configurationSpecifications) {
                java.util.regex.Matcher m = org.apache.ambari.server.state.kerberos.KerberosDescriptor.AUTH_TO_LOCAL_PROPERTY_SPECIFICATION_PATTERN.matcher(configurationSpecification);
                if (m.matches()) {
                    java.lang.String configType = m.group(1);
                    java.lang.String propertyName = m.group(2);
                    if (configType == null) {
                        configType = "";
                    }
                    java.util.Set<java.lang.String> propertyNames = translation.get(configType);
                    if (propertyNames == null) {
                        propertyNames = new java.util.HashSet<>();
                        translation.put(configType, propertyNames);
                    }
                    propertyNames.add(propertyName);
                }
            }
        }
        return translation;
    }

    private org.apache.directory.server.kerberos.shared.keytab.Keytab createIdentity(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor, org.apache.ambari.server.state.kerberos.KerberosPrincipalType expectedType, java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler kerberosOperationHandler, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        org.apache.directory.server.kerberos.shared.keytab.Keytab keytab = null;
        if (identityDescriptor != null) {
            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = identityDescriptor.getPrincipalDescriptor();
            if (principalDescriptor != null) {
                if (expectedType == principalDescriptor.getType()) {
                    java.lang.String principal = variableReplacementHelper.replaceVariables(principalDescriptor.getValue(), configurations);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(hostname)) {
                        principal = principal.replace("_HOST", hostname);
                    }
                    if (!kerberosPrincipalDAO.exists(principal)) {
                        org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.CreatePrincipalResult result;
                        result = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.class).createPrincipal(principal, org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE.equals(expectedType), kerberosEnvProperties, kerberosOperationHandler, false, null);
                        if (result == null) {
                            throw new org.apache.ambari.server.AmbariException("Failed to create the account for " + principal);
                        } else {
                            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = identityDescriptor.getKeytabDescriptor();
                            if (keytabDescriptor != null) {
                                org.apache.ambari.server.orm.entities.KerberosPrincipalEntity principalEntity = kerberosPrincipalDAO.find(principal);
                                keytab = injector.getInstance(org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.class).createKeytab(principal, principalEntity, result.getPassword(), result.getKeyNumber(), kerberosOperationHandler, true, true, null);
                                if (keytab == null) {
                                    throw new org.apache.ambari.server.AmbariException("Failed to create the keytab for " + principal);
                                }
                            }
                        }
                    }
                }
            }
        }
        return keytab;
    }

    private void validateKDCCredentials(org.apache.ambari.server.controller.KerberosDetails kerberosDetails, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException, org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException, org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException {
        if (kerberosDetails == null) {
            kerberosDetails = getKerberosDetails(cluster, null);
        }
        if (kerberosDetails.manageIdentities()) {
            org.apache.ambari.server.security.credential.PrincipalKeyCredential credentials = getKDCAdministratorCredentials(cluster.getClusterName());
            if (credentials == null) {
                throw new org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException();
            } else {
                org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler = kerberosOperationHandlerFactory.getKerberosOperationHandler(kerberosDetails.getKdcType());
                if (operationHandler == null) {
                    throw new org.apache.ambari.server.AmbariException("Failed to get an appropriate Kerberos operation handler.");
                } else {
                    boolean missingCredentials = false;
                    try {
                        operationHandler.open(credentials, kerberosDetails.getDefaultRealm(), kerberosDetails.getKerberosEnvProperties());
                        missingCredentials = !operationHandler.testAdministratorCredentials();
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException e) {
                        throw new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException("Invalid KDC administrator credentials.\n" + (((((("The KDC administrator credentials must be set as a persisted or temporary credential resource." + "This may be done by issuing a POST (or PUT for updating) to the /api/v1/clusters/:clusterName/credentials/kdc.admin.credential API entry point with the following payload:\n") + "{\n") + "  \"Credential\" : {\n") + "    \"principal\" : \"(PRINCIPAL)\", \"key\" : \"(PASSWORD)\", \"type\" : \"(persisted|temporary)\"}\n") + "  }\n") + "}"), e);
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosKDCConnectionException e) {
                        throw new org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException((("Failed to connect to KDC - " + e.getMessage()) + "\n") + "Update the KDC settings in krb5-conf and kerberos-env configurations to correct this issue.", e);
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosKDCSSLConnectionException e) {
                        throw new org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException((("Failed to connect to KDC - " + e.getMessage()) + "\n") + "Make sure the server's SSL certificate or CA certificates have been imported into Ambari's truststore.", e);
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosRealmException e) {
                        throw new org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException((("Failed to find a KDC for the specified realm - " + e.getMessage()) + "\n") + "Update the KDC settings in krb5-conf and kerberos-env configurations to correct this issue.", e);
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosLDAPContainerException e) {
                        throw new org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException("The principal container was not specified\n" + "Set the 'container_dn' value in the kerberos-env configuration to correct this issue.", e);
                    } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                        throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                    } finally {
                        try {
                            operationHandler.close();
                        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                        }
                    }
                    if (missingCredentials) {
                        throw new org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException();
                    }
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.controller.internal.RequestStageContainer handle(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsToForceKerberosOperations, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, final org.apache.ambari.server.controller.KerberosHelperImpl.Handler handler) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor(cluster, false);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schToProcess = getServiceComponentHostsToProcess(cluster, kerberosDescriptor, serviceComponentFilter, hostFilter);
        java.util.Set<java.lang.String> hostsWithValidKerberosClient = null;
        java.io.File dataDirectory = null;
        if (!schToProcess.isEmpty()) {
            validateKDCCredentials(kerberosDetails, cluster);
            dataDirectory = createTemporaryDirectory();
            hostsWithValidKerberosClient = getHostsWithValidKerberosClient(cluster);
            if (hostsToForceKerberosOperations != null) {
                hostsWithValidKerberosClient.addAll(hostsToForceKerberosOperations);
            }
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster);
        java.lang.String clusterHostInfoJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(clusterHostInfo);
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.MULTI_SERVICE, comment = "The cluster stack id is deprecated")
        java.util.Map<java.lang.String, java.lang.String> hostParams = customCommandExecutionHelper.createDefaultHostParams(cluster, cluster.getDesiredStackVersion());
        java.lang.String hostParamsJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostParams);
        java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), ambariServerHostname, java.lang.System.currentTimeMillis());
        org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder = ambariManagementController.getRoleCommandOrder(cluster);
        if (requestStageContainer == null) {
            requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
        }
        handler.createStages(cluster, clusterHostInfoJson, hostParamsJson, event, roleCommandOrder, kerberosDetails, dataDirectory, requestStageContainer, schToProcess, serviceComponentFilter, hostFilter, identityFilter, hostsWithValidKerberosClient);
        handler.addFinalizeOperationStage(cluster, clusterHostInfoJson, hostParamsJson, event, dataDirectory, roleCommandOrder, requestStageContainer, kerberosDetails);
        return requestStageContainer;
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer handleTestIdentity(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, org.apache.ambari.server.controller.KerberosHelperImpl.Handler handler) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        if (kerberosDetails.manageIdentities()) {
            if (commandParameters == null) {
                throw new org.apache.ambari.server.AmbariException("The properties map must not be null.  It is needed to store data related to the service check identity");
            }
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHostsToProcess = new java.util.ArrayList<>();
            org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor(cluster, false);
            java.util.Set<java.lang.String> hostsWithValidKerberosClient = getHostsWithValidKerberosClient(cluster);
            java.io.File dataDirectory = createTemporaryDirectory();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = calculateConfigurations(cluster, null, kerberosDescriptor, false, false, null);
            java.lang.String principal = variableReplacementHelper.replaceVariables("${kerberos-env/service_check_principal_name}@${realm}", configurations);
            java.lang.String keytabFilePath = variableReplacementHelper.replaceVariables("${keytab_dir}/kerberos.service_check.${short_date}.keytab", configurations);
            java.lang.String keytabFileOwnerName = variableReplacementHelper.replaceVariables("${cluster-env/smokeuser}", configurations);
            java.lang.String keytabFileOwnerAccess = "rw";
            java.lang.String keytabFileGroupName = variableReplacementHelper.replaceVariables("${cluster-env/user_group}", configurations);
            java.lang.String keytabFileGroupAccess = "r";
            commandParameters.put("principal_name", principal);
            commandParameters.put("keytab_file", keytabFilePath);
            try {
                java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = cluster.getServiceComponentHosts(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), org.apache.ambari.server.Role.KERBEROS_CLIENT.name());
                if ((serviceComponentHosts != null) && (!serviceComponentHosts.isEmpty())) {
                    for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHosts) {
                        if (sch.getState() == org.apache.ambari.server.state.State.INSTALLED) {
                            java.lang.String hostname = sch.getHostName();
                            org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke = kerberosKeytabDAO.find(keytabFilePath);
                            if (kke == null) {
                                kke = new org.apache.ambari.server.orm.entities.KerberosKeytabEntity();
                                kke.setKeytabPath(keytabFilePath);
                                kke.setOwnerName(keytabFileOwnerName);
                                kke.setOwnerAccess(keytabFileOwnerAccess);
                                kke.setGroupName(keytabFileGroupName);
                                kke.setGroupAccess(keytabFileGroupAccess);
                                kerberosKeytabDAO.create(kke);
                            }
                            org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kpe = kerberosPrincipalDAO.find(principal);
                            if (kpe == null) {
                                kpe = new org.apache.ambari.server.orm.entities.KerberosPrincipalEntity(principal, false, null);
                                kerberosPrincipalDAO.create(kpe);
                            }
                            org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult result = kerberosKeytabPrincipalDAO.findOrCreate(kke, hostDAO.findById(sch.getHost().getHostId()), kpe, null);
                            org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkp = result.kkp;
                            if (kkp.putServiceMapping(sch.getServiceName(), sch.getServiceComponentName())) {
                                kerberosKeytabPrincipalDAO.merge(kkp);
                            }
                            kerberosKeytabDAO.merge(kke);
                            kerberosPrincipalDAO.merge(kpe);
                            hostsWithValidKerberosClient.add(hostname);
                            serviceComponentHostsToProcess.add(sch);
                        }
                    }
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("Failed " + e);
                throw e;
            }
            if (!serviceComponentHostsToProcess.isEmpty()) {
                try {
                    validateKDCCredentials(kerberosDetails, cluster);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error("Cannot validate credentials: " + e);
                    try {
                        org.apache.commons.io.FileUtils.deleteDirectory(dataDirectory);
                    } catch (java.lang.Throwable t) {
                        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn(java.lang.String.format("The data directory (%s) was not deleted due to an error condition - {%s}", dataDirectory.getAbsolutePath(), t.getMessage()), t);
                    }
                    throw e;
                }
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster);
            java.lang.String clusterHostInfoJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(clusterHostInfo);
            @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.MULTI_SERVICE, comment = "The cluster stack id is deprecated")
            java.util.Map<java.lang.String, java.lang.String> hostParams = customCommandExecutionHelper.createDefaultHostParams(cluster, cluster.getDesiredStackVersion());
            java.lang.String hostParamsJson = org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostParams);
            java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name(), ambariServerHostname, java.lang.System.currentTimeMillis());
            org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder = ambariManagementController.getRoleCommandOrder(cluster);
            if (requestStageContainer == null) {
                requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
            }
            handler.createStages(cluster, clusterHostInfoJson, hostParamsJson, event, roleCommandOrder, kerberosDetails, dataDirectory, requestStageContainer, serviceComponentHostsToProcess, null, null, com.google.common.collect.Sets.newHashSet(principal), hostsWithValidKerberosClient);
            handler.addFinalizeOperationStage(cluster, clusterHostInfoJson, hostParamsJson, event, dataDirectory, roleCommandOrder, requestStageContainer, kerberosDetails);
        }
        return requestStageContainer;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.KerberosDetails getKerberosDetails(org.apache.ambari.server.state.Cluster cluster, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.KerberosDetails kerberosDetails = new org.apache.ambari.server.controller.KerberosDetails();
        if (cluster == null) {
            java.lang.String message = "The cluster object is not available";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        org.apache.ambari.server.state.Config configKrb5Conf = cluster.getDesiredConfigByType("krb5-conf");
        if (configKrb5Conf == null) {
            java.lang.String message = "The 'krb5-conf' configuration is not available";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        java.util.Map<java.lang.String, java.lang.String> krb5ConfProperties = configKrb5Conf.getProperties();
        if (krb5ConfProperties == null) {
            java.lang.String message = "The 'krb5-conf' configuration properties are not available";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        org.apache.ambari.server.state.Config configKerberosEnv = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV);
        if (configKerberosEnv == null) {
            java.lang.String message = "The 'kerberos-env' configuration is not available";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties = configKerberosEnv.getProperties();
        if (kerberosEnvProperties == null) {
            java.lang.String message = "The 'kerberos-env' configuration properties are not available";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        kerberosDetails.setSecurityType(cluster.getSecurityType());
        kerberosDetails.setDefaultRealm(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM));
        kerberosDetails.setKerberosEnvProperties(kerberosEnvProperties);
        kerberosDetails.setManageIdentities(manageIdentities);
        java.lang.String kdcTypeProperty = kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.KDC_TYPE);
        if ((kdcTypeProperty == null) && kerberosDetails.manageIdentities()) {
            java.lang.String message = "The 'kerberos-env/kdc_type' value must be set to a valid KDC type";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
            throw new org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException(message);
        }
        org.apache.ambari.server.serveraction.kerberos.KDCType kdcType;
        try {
            kdcType = org.apache.ambari.server.serveraction.kerberos.KDCType.translate(kdcTypeProperty);
        } catch (java.lang.IllegalArgumentException e) {
            java.lang.String message = java.lang.String.format("Invalid 'kdc_type' value: %s", kdcTypeProperty);
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message);
            throw new org.apache.ambari.server.AmbariException(message);
        }
        kerberosDetails.setKdcType(kdcType == null ? org.apache.ambari.server.serveraction.kerberos.KDCType.MIT_KDC : kdcType);
        return kerberosDetails;
    }

    @java.lang.Override
    public java.io.File createTemporaryDirectory() throws org.apache.ambari.server.AmbariException {
        try {
            java.io.File temporaryDirectory = getConfiguredTemporaryDirectory();
            java.io.File directory;
            int tries = 0;
            long now = java.lang.System.currentTimeMillis();
            do {
                directory = new java.io.File(temporaryDirectory, java.lang.String.format("%s%d-%d.d", org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY_PREFIX, now, tries));
                if (directory.exists() || (!directory.mkdirs())) {
                    directory = null;
                } else {
                    org.apache.ambari.server.controller.KerberosHelperImpl.LOG.debug("Created temporary directory: {}", directory.getAbsolutePath());
                }
            } while ((directory == null) && ((++tries) < 100) );
            if (directory == null) {
                throw new java.io.IOException(java.lang.String.format("Failed to create a temporary directory in %s", temporaryDirectory));
            }
            return directory;
        } catch (java.io.IOException e) {
            java.lang.String message = "Failed to create the temporary data directory.";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message, e);
            throw new org.apache.ambari.server.AmbariException(message, e);
        }
    }

    private void mergeConfiguration(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String configurationSpecification, java.lang.String value, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements) throws org.apache.ambari.server.AmbariException {
        if (configurationSpecification != null) {
            java.lang.String[] parts = configurationSpecification.split("/");
            if (parts.length == 2) {
                java.lang.String type = parts[0];
                java.lang.String property = parts[1];
                mergeConfigurations(configurations, type, java.util.Collections.singletonMap(property, value), replacements);
            }
        }
    }

    private void mergeConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String type, java.util.Map<java.lang.String, java.lang.String> updates, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements) throws org.apache.ambari.server.AmbariException {
        if (updates != null) {
            java.util.Map<java.lang.String, java.lang.String> existingProperties = configurations.get(type);
            if (existingProperties == null) {
                existingProperties = new java.util.HashMap<>();
                configurations.put(type, existingProperties);
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> property : updates.entrySet()) {
                existingProperties.put(variableReplacementHelper.replaceVariables(property.getKey(), replacements), variableReplacementHelper.replaceVariables(property.getValue(), replacements));
            }
        }
    }

    private void addIdentities(org.apache.ambari.server.controller.AuthToLocalBuilder authToLocalBuilder, java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.Collection<java.lang.String> identityFilter, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) throws org.apache.ambari.server.AmbariException {
        if (identities != null) {
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                if ((identityFilter == null) || identityFilter.contains(identity.getName())) {
                    org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = identity.getPrincipalDescriptor();
                    if (principalDescriptor != null) {
                        authToLocalBuilder.addRule(variableReplacementHelper.replaceVariables(principalDescriptor.getValue(), configurations), variableReplacementHelper.replaceVariables(principalDescriptor.getLocalUsername(), configurations));
                    }
                }
            }
        }
    }

    protected java.io.File createTemporaryFile() throws org.apache.ambari.server.AmbariException {
        try {
            return java.io.File.createTempFile("tmp", ".tmp", getConfiguredTemporaryDirectory());
        } catch (java.io.IOException e) {
            java.lang.String message = "Failed to create a temporary file.";
            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.error(message, e);
            throw new org.apache.ambari.server.AmbariException(message, e);
        }
    }

    protected java.io.File getConfiguredTemporaryDirectory() throws java.io.IOException {
        java.lang.String tempDirectoryPath = configuration.getServerTempDir();
        if (org.apache.commons.lang.StringUtils.isEmpty(tempDirectoryPath)) {
            tempDirectoryPath = java.lang.System.getProperty("java.io.tmpdir");
        }
        if (tempDirectoryPath == null) {
            throw new java.io.IOException("The System property 'java.io.tmpdir' does not specify a temporary directory");
        }
        return new java.io.File(tempDirectoryPath);
    }

    private org.apache.ambari.server.actionmanager.Stage createNewStage(long id, org.apache.ambari.server.state.Cluster cluster, long requestId, java.lang.String requestContext, java.lang.String commandParams, java.lang.String hostParams) {
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(requestId, (org.apache.ambari.server.controller.KerberosHelperImpl.BASE_LOG_DIR + java.io.File.pathSeparator) + requestId, cluster.getClusterName(), cluster.getClusterId(), requestContext, commandParams, hostParams);
        stage.setStageId(id);
        return stage;
    }

    private java.util.List<java.lang.String> createUniqueHostList(java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Set<org.apache.ambari.server.state.HostState> allowedStates) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        java.util.Set<java.lang.String> visitedHostNames = new java.util.HashSet<>();
        if (serviceComponentHosts != null) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHosts) {
                java.lang.String hostname = sch.getHostName();
                if (!visitedHostNames.contains(hostname)) {
                    if (allowedStates == null) {
                        hostNames.add(hostname);
                    } else {
                        org.apache.ambari.server.state.Host host = clusters.getHost(hostname);
                        if (allowedStates.contains(host.getState())) {
                            hostNames.add(hostname);
                        } else {
                            org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn("Host {} was excluded due {} state is not allowed. Allowed states: {}", hostname, host.getState(), allowedStates);
                        }
                    }
                    visitedHostNames.add(hostname);
                }
            }
        }
        return new java.util.ArrayList<>(hostNames);
    }

    @java.lang.Override
    public boolean isClusterKerberosEnabled(org.apache.ambari.server.state.Cluster cluster) {
        return cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS;
    }

    @java.lang.Override
    public boolean shouldExecuteCustomOperations(org.apache.ambari.server.state.SecurityType requestSecurityType, java.util.Map<java.lang.String, java.lang.String> requestProperties) {
        if ((((requestSecurityType == org.apache.ambari.server.state.SecurityType.KERBEROS) || (requestSecurityType == org.apache.ambari.server.state.SecurityType.NONE)) && (requestProperties != null)) && (!requestProperties.isEmpty())) {
            for (org.apache.ambari.server.controller.KerberosHelperImpl.SupportedCustomOperation type : org.apache.ambari.server.controller.KerberosHelperImpl.SupportedCustomOperation.values()) {
                if (requestProperties.containsKey(type.name().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public java.lang.Boolean getManageIdentitiesDirective(java.util.Map<java.lang.String, java.lang.String> requestProperties) {
        java.lang.String value = (requestProperties == null) ? null : requestProperties.get(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES);
        return value == null ? null : !"false".equalsIgnoreCase(value);
    }

    @java.lang.Override
    public boolean getForceToggleKerberosDirective(java.util.Map<java.lang.String, java.lang.String> requestProperties) {
        return (requestProperties != null) && "true".equalsIgnoreCase(requestProperties.get(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS));
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getIdentityConfigurations(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> map = new java.util.HashMap<>();
        if (identityDescriptors != null) {
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
                org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = identityDescriptor.getPrincipalDescriptor();
                if (principalDescriptor != null) {
                    putConfiguration(map, principalDescriptor.getConfiguration(), principalDescriptor.getValue());
                }
                org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = identityDescriptor.getKeytabDescriptor();
                if (keytabDescriptor != null) {
                    putConfiguration(map, keytabDescriptor.getConfiguration(), keytabDescriptor.getFile());
                }
            }
        }
        return map;
    }

    private void putConfiguration(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> map, java.lang.String configuration, java.lang.String value) {
        if (configuration != null) {
            java.lang.String[] principalTokens = configuration.split("/");
            if (principalTokens.length == 2) {
                java.lang.String type = principalTokens[0];
                java.lang.String propertyName = principalTokens[1];
                java.util.Map<java.lang.String, java.lang.String> properties = map.get(type);
                if (properties == null) {
                    properties = new java.util.HashMap<>();
                    map.put(type, properties);
                }
                properties.put(propertyName, value);
            }
        }
    }

    private java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getActiveIdentities(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.Map<java.lang.String, java.lang.Object> filterContext) {
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = cluster.getServiceComponentHosts(hostname);
        if (serviceComponentHosts == null) {
            return identities;
        }
        serviceComponentHosts.forEach(serviceComponentHost -> {
            java.lang.String schServiceName = serviceComponentHost.getServiceName();
            java.lang.String schComponentName = serviceComponentHost.getServiceComponentName();
            if (((serviceName == null) || serviceName.equals(schServiceName)) && ((componentName == null) || componentName.equals(schComponentName))) {
                org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosDescriptor.getService(schServiceName);
                if (serviceDescriptor != null) {
                    java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities;
                    try {
                        serviceIdentities = serviceDescriptor.getIdentities(true, filterContext);
                    } catch (org.apache.ambari.server.AmbariException e) {
                        serviceIdentities = null;
                    }
                    if (serviceIdentities != null) {
                        identities.addAll(serviceIdentities);
                    }
                    org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = serviceDescriptor.getComponent(schComponentName);
                    if (componentDescriptor != null) {
                        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities;
                        try {
                            componentIdentities = componentDescriptor.getIdentities(true, filterContext);
                        } catch (org.apache.ambari.server.AmbariException e) {
                            componentIdentities = null;
                        }
                        if (componentIdentities != null) {
                            identities.addAll(componentIdentities);
                        }
                    }
                }
            }
        });
        return identities;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> addAdditionalConfigurations(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String hostname, java.util.Map<java.lang.String, java.lang.String> kerberosDescriptorProperties, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, java.util.Map<java.lang.String, java.lang.String> componentHosts, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> generalProperties = configurations.computeIfAbsent("", k -> new java.util.HashMap<>());
        if (kerberosDescriptorProperties != null) {
            generalProperties.putAll(kerberosDescriptorProperties);
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(hostname)) {
            generalProperties.put("host", hostname);
            generalProperties.put("hostname", hostname);
        }
        generalProperties.put("cluster_name", cluster.getClusterName());
        generalProperties.put("short_date", new java.text.SimpleDateFormat("MMddyy").format(new java.util.Date()));
        if (configurations.get(org.apache.ambari.server.controller.KerberosHelper.CLUSTER_HOST_INFO) == null) {
            if (componentHosts == null) {
                componentHosts = new java.util.HashMap<>();
            }
            if (componentHosts.isEmpty()) {
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster);
                if (clusterHostInfo != null) {
                    clusterHostInfo = org.apache.ambari.server.utils.StageUtils.substituteHostIndexes(clusterHostInfo);
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : clusterHostInfo.entrySet()) {
                        componentHosts.put(entry.getKey(), org.apache.commons.lang.StringUtils.join(entry.getValue(), ","));
                    }
                }
            }
            if (!componentHosts.isEmpty()) {
                configurations.put(org.apache.ambari.server.controller.KerberosHelper.CLUSTER_HOST_INFO, componentHosts);
            }
        }
        configurations.put("principals", principalNames(cluster, configurations, userDescriptor, desiredConfigs));
        return configurations;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> addAdditionalConfigurations(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String hostname, java.util.Map<java.lang.String, java.lang.String> kerberosDescriptorProperties, org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor) throws org.apache.ambari.server.AmbariException {
        return addAdditionalConfigurations(cluster, configurations, hostname, kerberosDescriptorProperties, userDescriptor, null, null);
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> deepCopy(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> map) {
        if (map == null) {
            return null;
        } else {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> copy = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : map.entrySet()) {
                java.util.Map<java.lang.String, java.lang.String> innerMap = entry.getValue();
                copy.put(entry.getKey(), innerMap == null ? null : new java.util.HashMap<>(innerMap));
            }
            return copy;
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptorUpdates(org.apache.ambari.server.state.Cluster cluster) {
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        foreignKeys.put("cluster", java.lang.String.valueOf(cluster.getClusterId()));
        org.apache.ambari.server.orm.entities.ArtifactEntity entity = artifactDAO.findByNameAndForeignKeys("kerberos_descriptor", foreignKeys);
        return entity == null ? null : kerberosDescriptorFactory.createInstance(entity.getArtifactData());
    }

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptorFromStack(org.apache.ambari.server.state.StackId stackId, boolean includePreconfigureData) throws org.apache.ambari.server.AmbariException {
        return ambariMetaInfo.getKerberosDescriptor(stackId.getStackName(), stackId.getStackVersion(), includePreconfigureData);
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> processWhenClauses(java.lang.String currentPath, org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer container, java.util.Map<java.lang.String, java.lang.Object> context, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> identitiesToRemove) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = container.getIdentities(true, null);
        if ((identities != null) && (!identities.isEmpty())) {
            java.util.Set<java.lang.String> set = null;
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                if (!identity.shouldInclude(context)) {
                    if (set == null) {
                        set = new java.util.HashSet<>();
                        identitiesToRemove.put(currentPath, set);
                    }
                    set.add(identity.getName());
                }
            }
        }
        java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> children = container.getChildContainers();
        if (children != null) {
            for (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer child : children) {
                identitiesToRemove = processWhenClauses((currentPath + "/") + child.getName(), child, context, identitiesToRemove);
            }
        }
        return identitiesToRemove;
    }

    private void processIdentityConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> identityConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore) throws org.apache.ambari.server.AmbariException {
        if (identityConfigurations != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> identitiyEntry : identityConfigurations.entrySet()) {
                java.lang.String configType = identitiyEntry.getKey();
                java.util.Map<java.lang.String, java.lang.String> properties = identitiyEntry.getValue();
                mergeConfigurations(kerberosConfigurations, configType, identitiyEntry.getValue(), configurations);
                if ((properties != null) && (!properties.isEmpty())) {
                    java.util.Set<java.lang.String> propertyNames = propertiesToIgnore.get(configType);
                    if (propertyNames == null) {
                        propertyNames = new java.util.HashSet<>();
                        propertiesToIgnore.put(configType, propertyNames);
                    }
                    propertyNames.addAll(properties.keySet());
                }
            }
        }
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> addConfigurationsForPreProcessedServices(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, org.apache.ambari.server.state.Cluster cluster, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, boolean calculateClusterHostInfo) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptorMap = kerberosDescriptor.getServices();
        if (serviceDescriptorMap != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> existingServices = cluster.getServices();
            java.util.Set<java.lang.String> allServices = new java.util.HashSet<>(existingServices.keySet());
            java.util.Set<java.lang.String> componentFilter = new java.util.HashSet<>();
            org.apache.ambari.server.state.StackId stackVersion = cluster.getCurrentStackVersion();
            for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptorMap.values()) {
                java.lang.String serviceName = serviceDescriptor.getName();
                boolean shouldPreconfigure = serviceDescriptor.shouldPreconfigure();
                if (shouldPreconfigure && (!existingServices.containsKey(serviceName))) {
                    if (ambariMetaInfo.isValidService(stackVersion.getStackName(), stackVersion.getStackVersion(), serviceName)) {
                        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackVersion.getStackName(), stackVersion.getStackVersion(), serviceName);
                        java.util.Collection<org.apache.ambari.server.state.PropertyInfo> servicePropertiesInfos = serviceInfo.getProperties();
                        if (servicePropertiesInfos != null) {
                            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesToAdd = new java.util.HashMap<>();
                            for (org.apache.ambari.server.state.PropertyInfo propertyInfo : servicePropertiesInfos) {
                                java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(propertyInfo.getFilename());
                                java.util.Map<java.lang.String, java.lang.String> map = propertiesToAdd.get(type);
                                if (map == null) {
                                    map = new java.util.HashMap<>();
                                    propertiesToAdd.put(type, map);
                                }
                                map.put(propertyInfo.getName(), propertyInfo.getValue());
                            }
                            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : propertiesToAdd.entrySet()) {
                                if (!configurations.containsKey(entry.getKey())) {
                                    configurations.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                        if (calculateClusterHostInfo) {
                            allServices.add(serviceName);
                            java.util.List<org.apache.ambari.server.state.ComponentInfo> componentInfos = serviceInfo.getComponents();
                            if (componentInfos != null) {
                                for (org.apache.ambari.server.state.ComponentInfo componentInfo : componentInfos) {
                                    componentFilter.add(componentInfo.getName());
                                }
                            }
                        }
                    }
                }
            }
            if (calculateClusterHostInfo && (allServices.size() > existingServices.size())) {
                applyStackAdvisorHostRecommendations(cluster, allServices, componentFilter, configurations);
            }
        }
        return configurations;
    }

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor combineKerberosDescriptors(org.apache.ambari.server.state.kerberos.KerberosDescriptor stackDescriptor, org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor) {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;
        if (stackDescriptor == null) {
            if (userDescriptor == null) {
                return new org.apache.ambari.server.state.kerberos.KerberosDescriptor();
            } else {
                kerberosDescriptor = userDescriptor;
            }
        } else {
            if (userDescriptor != null) {
                stackDescriptor.update(userDescriptor);
            }
            kerberosDescriptor = stackDescriptor;
        }
        return kerberosDescriptor;
    }

    public enum SupportedCustomOperation {

        REGENERATE_KEYTABS;}

    private abstract class Handler {
        protected boolean retryAllowed = false;

        void setRetryAllowed(boolean retryAllowed) {
            this.retryAllowed = retryAllowed;
        }

        abstract long createStages(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfo, java.lang.String hostParams, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, java.io.File dataDirectory, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsWithValidKerberosClient) throws org.apache.ambari.server.AmbariException;

        public void addPrepareEnableKerberosOperationsStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Preparing Operations", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.PrepareEnableKerberosServerAction.class, event, commandParameters, "Preparing Operations", configuration.getDefaultServerTaskTimeout());
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addPrepareKerberosIdentitiesStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Preparing Operations", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction.class, event, commandParameters, "Preparing Operations", configuration.getDefaultServerTaskTimeout());
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addPrepareDisableKerberosOperationsStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Preparing Operations", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.PrepareDisableKerberosServerAction.class, event, commandParameters, "Preparing Operations", configuration.getDefaultServerTaskTimeout());
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addCreatePrincipalsStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Create Principals", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.CreatePrincipalsServerAction.class, event, commandParameters, "Create Principals", java.lang.Math.max(org.apache.ambari.server.serveraction.ServerAction.DEFAULT_LONG_RUNNING_TASK_TIMEOUT_SECONDS, configuration.getDefaultServerTaskTimeout()));
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addDestroyPrincipalsStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Destroy Principals", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.DestroyPrincipalsServerAction.class, event, commandParameters, "Destroy Principals", java.lang.Math.max(org.apache.ambari.server.serveraction.ServerAction.DEFAULT_LONG_RUNNING_TASK_TIMEOUT_SECONDS, configuration.getDefaultServerTaskTimeout()));
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addConfigureAmbariIdentityStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Configure Ambari Identity", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.ConfigureAmbariIdentitiesServerAction.class, event, commandParameters, "Configure Ambari Identity", configuration.getDefaultServerTaskTimeout());
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addCreateKeytabFilesStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Create Keytabs", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.CreateKeytabFilesServerAction.class, event, commandParameters, "Create Keytabs", java.lang.Math.max(org.apache.ambari.server.serveraction.ServerAction.DEFAULT_LONG_RUNNING_TASK_TIMEOUT_SECONDS, configuration.getDefaultServerTaskTimeout()));
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        void addDistributeKeytabFilesStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.List<java.lang.String> hosts) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Distribute Keytabs", org.apache.ambari.server.utils.StageUtils.getGson().toJson(commandParameters), hostParamsJson);
            if (!hosts.isEmpty()) {
                java.util.Map<java.lang.String, java.lang.String> requestParams = new java.util.HashMap<>();
                org.apache.ambari.server.controller.ActionExecutionContext actionExecContext = createActionExecutionContext(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB, createRequestResourceFilters(hosts), requestParams, retryAllowed);
                customCommandExecutionHelper.addExecutionCommandsToStage(actionExecContext, stage, requestParams, null);
            } else {
                org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn("Skipping {} command. No suitable hosts found", org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB);
            }
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        void addCheckMissingKeytabsStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.List<java.lang.String> hostsToInclude) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Checking keytabs", org.apache.ambari.server.utils.StageUtils.getGson().toJson(commandParameters), hostParamsJson);
            if (!hostsToInclude.isEmpty()) {
                java.util.Map<java.lang.String, java.lang.String> requestParams = new java.util.HashMap<>();
                org.apache.ambari.server.controller.ActionExecutionContext actionExecContext = createActionExecutionContext(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelperImpl.CHECK_KEYTABS, createRequestResourceFilters(hostsToInclude), requestParams, retryAllowed);
                customCommandExecutionHelper.addExecutionCommandsToStage(actionExecContext, stage, requestParams, null);
            }
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        void addDisableSecurityHookStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Disable security", org.apache.ambari.server.utils.StageUtils.getGson().toJson(commandParameters), hostParamsJson);
            addDisableSecurityCommandToAllServices(cluster, stage);
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        private void addDisableSecurityCommandToAllServices(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.actionmanager.Stage stage) throws org.apache.ambari.server.AmbariException {
            for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
                for (org.apache.ambari.server.state.ServiceComponent component : service.getServiceComponents().values()) {
                    if (!component.getServiceComponentHosts().isEmpty()) {
                        java.lang.String firstHost = component.getServiceComponentHosts().keySet().iterator().next();
                        org.apache.ambari.server.controller.ActionExecutionContext exec = new org.apache.ambari.server.controller.ActionExecutionContext(cluster.getClusterName(), "DISABLE_SECURITY", java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.RequestResourceFilter(service.getName(), component.getName(), java.util.Collections.singletonList(firstHost))), java.util.Collections.emptyMap());
                        customCommandExecutionHelper.addExecutionCommandsToStage(exec, stage, java.util.Collections.emptyMap(), null);
                    }
                }
            }
        }

        void addStopZookeeperStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.state.Service zookeeper;
            try {
                zookeeper = cluster.getService("ZOOKEEPER");
            } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                return;
            }
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Stopping ZooKeeper", org.apache.ambari.server.utils.StageUtils.getGson().toJson(commandParameters), hostParamsJson);
            for (org.apache.ambari.server.state.ServiceComponent component : zookeeper.getServiceComponents().values()) {
                java.util.Set<java.lang.String> hosts = component.getServiceComponentHosts().keySet();
                org.apache.ambari.server.controller.ActionExecutionContext exec = new org.apache.ambari.server.controller.ActionExecutionContext(cluster.getClusterName(), "STOP", java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.RequestResourceFilter(zookeeper.getName(), component.getName(), new java.util.ArrayList<>(hosts))), java.util.Collections.emptyMap());
                customCommandExecutionHelper.addExecutionCommandsToStage(exec, stage, java.util.Collections.emptyMap(), null);
            }
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addDeleteKeytabFilesStage(org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.Set<java.lang.String> hostsWithValidKerberosClient) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Delete Keytabs", org.apache.ambari.server.utils.StageUtils.getGson().toJson(commandParameters), hostParamsJson);
            java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> filteredComponents = filterServiceComponentHostsForHosts(new java.util.ArrayList<>(serviceComponentHosts), hostsWithValidKerberosClient);
            if (!filteredComponents.isEmpty()) {
                java.util.List<java.lang.String> hostsToUpdate = createUniqueHostList(filteredComponents, java.util.Collections.singleton(org.apache.ambari.server.state.HostState.HEALTHY));
                if (!hostsToUpdate.isEmpty()) {
                    java.util.Map<java.lang.String, java.lang.String> requestParams = new java.util.HashMap<>();
                    java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> requestResourceFilters = new java.util.ArrayList<>();
                    org.apache.ambari.server.controller.internal.RequestResourceFilter reqResFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("KERBEROS", "KERBEROS_CLIENT", hostsToUpdate);
                    requestResourceFilters.add(reqResFilter);
                    org.apache.ambari.server.controller.ActionExecutionContext actionExecContext = new org.apache.ambari.server.controller.ActionExecutionContext(cluster.getClusterName(), org.apache.ambari.server.controller.KerberosHelperImpl.REMOVE_KEYTAB, requestResourceFilters, requestParams);
                    customCommandExecutionHelper.addExecutionCommandsToStage(actionExecContext, stage, requestParams, null);
                }
            }
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addUpdateConfigurationsStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Update Configurations", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction.class, event, commandParameters, "Update Service Configurations", configuration.getDefaultServerTaskTimeout());
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addFinalizeOperationStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.io.File dataDirectory, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, org.apache.ambari.server.controller.KerberosDetails kerberosDetails) throws org.apache.ambari.server.AmbariException {
            java.util.Map<java.lang.String, java.lang.String> commandParameters = new java.util.HashMap<>();
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, kerberosDetails.getDefaultRealm());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, kerberosDetails.getKdcType().name());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, ambariManagementController.getAuthName());
            if (dataDirectory != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
            }
            int timeout = configuration.getKerberosServerActionFinalizeTimeout();
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Finalize Operations", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.FinalizeKerberosServerAction.class, event, commandParameters, "Finalize Operations", timeout);
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        public void addCleanupStage(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createServerActionStage(requestStageContainer.getLastStageId(), cluster, requestStageContainer.getId(), "Kerberization Clean Up", "{}", hostParamsJson, org.apache.ambari.server.serveraction.kerberos.CleanupServerAction.class, event, commandParameters, "Kerberization Clean Up", configuration.getDefaultServerTaskTimeout());
            org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
            roleGraph.build(stage);
            requestStageContainer.addStages(roleGraph.getStages());
        }

        private java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> createRequestResourceFilters(java.util.List<java.lang.String> hostsToInclude) {
            java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> requestResourceFilters = new java.util.ArrayList<>();
            org.apache.ambari.server.controller.internal.RequestResourceFilter reqResFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(org.apache.ambari.server.state.Service.Type.KERBEROS.name(), org.apache.ambari.server.Role.KERBEROS_CLIENT.name(), hostsToInclude);
            requestResourceFilters.add(reqResFilter);
            return requestResourceFilters;
        }

        private org.apache.ambari.server.actionmanager.Stage createServerActionStage(long id, org.apache.ambari.server.state.Cluster cluster, long requestId, java.lang.String requestContext, java.lang.String commandParams, java.lang.String hostParams, java.lang.Class<? extends org.apache.ambari.server.serveraction.ServerAction> actionClass, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, java.util.Map<java.lang.String, java.lang.String> commandParameters, java.lang.String commandDetail, java.lang.Integer timeout) throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.actionmanager.Stage stage = createNewStage(id, cluster, requestId, requestContext, commandParams, hostParams);
            stage.addServerActionCommand(actionClass.getName(), null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, cluster.getClusterName(), event, commandParameters, commandDetail, ambariManagementController.findConfigurationTagsWithOverrides(cluster, null, null), timeout, retryAllowed, false);
            return stage;
        }

        private org.apache.ambari.server.controller.ActionExecutionContext createActionExecutionContext(java.lang.String clusterName, java.lang.String commandName, java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters, java.util.Map<java.lang.String, java.lang.String> parameters, boolean retryAllowed) {
            org.apache.ambari.server.controller.ActionExecutionContext actionExecContext = new org.apache.ambari.server.controller.ActionExecutionContext(clusterName, commandName, resourceFilters, parameters);
            actionExecContext.setRetryAllowed(retryAllowed);
            return actionExecContext;
        }
    }

    private class EnableKerberosHandler extends org.apache.ambari.server.controller.KerberosHelperImpl.Handler {
        @java.lang.Override
        public long createStages(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, java.io.File dataDirectory, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsWithValidKerberosClient) throws org.apache.ambari.server.AmbariException {
            if (requestStageContainer == null) {
                requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
            }
            java.util.Map<java.lang.String, java.lang.String> commandParameters = new java.util.HashMap<>();
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, ambariManagementController.getAuthName());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_NOTE, "Enabling Kerberos");
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_POLICY, org.apache.ambari.server.controller.UpdateConfigurationPolicy.ALL.name());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, kerberosDetails.getDefaultRealm());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.INCLUDE_AMBARI_IDENTITY, kerberosDetails.createAmbariPrincipal() ? "true" : "false");
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.PRECONFIGURE_SERVICES, kerberosDetails.getPreconfigureServices());
            if (dataDirectory != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
            }
            if (serviceComponentFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.SERVICE_COMPONENT_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(serviceComponentFilter));
            }
            if (hostFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.HOST_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostFilter));
            }
            if (identityFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.IDENTITY_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(identityFilter));
            }
            addPrepareEnableKerberosOperationsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            if (kerberosDetails.manageIdentities()) {
                java.util.List<java.lang.String> hostsToInclude = calculateHosts(cluster, serviceComponentHosts, hostsWithValidKerberosClient, false);
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, kerberosDetails.getKdcType().name());
                addCreatePrincipalsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                addCreateKeytabFilesStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                if (kerberosDetails.createAmbariPrincipal()) {
                    addConfigureAmbariIdentityStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                }
                addDistributeKeytabFilesStage(cluster, clusterHostInfoJson, hostParamsJson, commandParameters, roleCommandOrder, requestStageContainer, hostsToInclude);
            }
            addUpdateConfigurationsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            return requestStageContainer.getLastStageId();
        }
    }

    private class DisableKerberosHandler extends org.apache.ambari.server.controller.KerberosHelperImpl.Handler {
        @java.lang.Override
        public long createStages(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, java.io.File dataDirectory, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsWithValidKerberosClient) throws org.apache.ambari.server.AmbariException {
            if (requestStageContainer == null) {
                requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
            }
            java.util.Map<java.lang.String, java.lang.String> commandParameters = new java.util.HashMap<>();
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, ambariManagementController.getAuthName());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_NOTE, "Disabling Kerberos");
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_POLICY, org.apache.ambari.server.controller.UpdateConfigurationPolicy.ALL.name());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, kerberosDetails.getDefaultRealm());
            if (dataDirectory != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
            }
            if (serviceComponentFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.SERVICE_COMPONENT_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(serviceComponentFilter));
            }
            if (hostFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.HOST_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostFilter));
            }
            if (identityFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.IDENTITY_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(identityFilter));
            }
            addDisableSecurityHookStage(cluster, clusterHostInfoJson, hostParamsJson, commandParameters, roleCommandOrder, requestStageContainer);
            addStopZookeeperStage(cluster, clusterHostInfoJson, hostParamsJson, commandParameters, roleCommandOrder, requestStageContainer);
            addPrepareDisableKerberosOperationsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            addUpdateConfigurationsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            if (kerberosDetails.manageIdentities()) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, kerberosDetails.getKdcType().name());
                addDeleteKeytabFilesStage(cluster, serviceComponentHosts, clusterHostInfoJson, hostParamsJson, commandParameters, roleCommandOrder, requestStageContainer, hostsWithValidKerberosClient);
                addDestroyPrincipalsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            }
            addCleanupStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            return requestStageContainer.getLastStageId();
        }
    }

    private class CreatePrincipalsAndKeytabsHandler extends org.apache.ambari.server.controller.KerberosHelperImpl.Handler {
        private org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType operationType;

        private org.apache.ambari.server.controller.UpdateConfigurationPolicy updateConfigurationPolicy;

        private boolean forceAllHosts;

        private boolean includeAmbariIdentity;

        CreatePrincipalsAndKeytabsHandler(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType operationType, org.apache.ambari.server.controller.UpdateConfigurationPolicy updateConfigurationPolicy, boolean forceAllHosts, boolean includeAmbariIdentity) {
            this.operationType = operationType;
            this.updateConfigurationPolicy = updateConfigurationPolicy;
            this.forceAllHosts = forceAllHosts;
            this.includeAmbariIdentity = includeAmbariIdentity;
        }

        @java.lang.Override
        public long createStages(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, java.io.File dataDirectory, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsWithValidKerberosClient) throws org.apache.ambari.server.AmbariException {
            if (requestStageContainer == null) {
                requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
            }
            boolean processAmbariIdentity = includeAmbariIdentity;
            java.util.Map<java.lang.String, java.lang.String> commandParameters = new java.util.HashMap<>();
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, ambariManagementController.getAuthName());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, kerberosDetails.getDefaultRealm());
            if (dataDirectory != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
            }
            if (serviceComponentFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.SERVICE_COMPONENT_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(serviceComponentFilter));
                processAmbariIdentity = serviceComponentFilter.containsKey(org.apache.ambari.server.controller.RootService.AMBARI.name()) && (((serviceComponentFilter.get(org.apache.ambari.server.controller.RootService.AMBARI.name()) == null) || serviceComponentFilter.get(org.apache.ambari.server.controller.RootService.AMBARI.name()).contains("*")) || serviceComponentFilter.get("AMBARI").contains(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name()));
            }
            if (hostFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.HOST_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostFilter));
                processAmbariIdentity = hostFilter.contains("*") || hostFilter.contains(org.apache.ambari.server.utils.StageUtils.getHostName());
            }
            if (identityFilter != null) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.IDENTITY_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(identityFilter));
            }
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OPERATION_TYPE, operationType == null ? org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.DEFAULT.name() : operationType.name());
            commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.INCLUDE_AMBARI_IDENTITY, processAmbariIdentity ? "true" : "false");
            if (updateConfigurationPolicy != org.apache.ambari.server.controller.UpdateConfigurationPolicy.NONE) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_NOTE, "Updated Kerberos-related configurations");
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_POLICY, updateConfigurationPolicy.name());
            }
            java.util.List<java.lang.String> hostsToInclude = calculateHosts(cluster, serviceComponentHosts, hostsWithValidKerberosClient, forceAllHosts);
            addPrepareKerberosIdentitiesStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            if (kerberosDetails.manageIdentities()) {
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, kerberosDetails.getKdcType().name());
                if (operationType != org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.RECREATE_ALL) {
                    addCheckMissingKeytabsStage(cluster, clusterHostInfoJson, hostParamsJson, commandParameters, roleCommandOrder, requestStageContainer, hostsToInclude);
                }
                addCreatePrincipalsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                addCreateKeytabFilesStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                if (processAmbariIdentity && kerberosDetails.createAmbariPrincipal()) {
                    addConfigureAmbariIdentityStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                }
                addDistributeKeytabFilesStage(cluster, clusterHostInfoJson, hostParamsJson, commandParameters, roleCommandOrder, requestStageContainer, hostsToInclude);
            }
            if (updateConfigurationPolicy != org.apache.ambari.server.controller.UpdateConfigurationPolicy.NONE) {
                addUpdateConfigurationsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            }
            return requestStageContainer.getLastStageId();
        }
    }

    private java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> filterServiceComponentHostsForHosts(java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Set<java.lang.String> hosts) {
        if ((serviceComponentHosts != null) && (hosts != null)) {
            java.util.Iterator<org.apache.ambari.server.state.ServiceComponentHost> iterator = serviceComponentHosts.iterator();
            while (iterator.hasNext()) {
                org.apache.ambari.server.state.ServiceComponentHost sch = iterator.next();
                if (!hosts.contains(sch.getHostName())) {
                    iterator.remove();
                }
            } 
        }
        return serviceComponentHosts;
    }

    private java.util.List<java.lang.String> calculateHosts(org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Set<java.lang.String> hostsWithValidKerberosClient, boolean forceAllHosts) throws org.apache.ambari.server.AmbariException {
        if (forceAllHosts) {
            java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
            java.util.Collection<org.apache.ambari.server.state.Host> clusterHosts = cluster.getHosts();
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(clusterHosts)) {
                for (org.apache.ambari.server.state.Host host : clusterHosts) {
                    if (host.getState() == org.apache.ambari.server.state.HostState.HEALTHY) {
                        hosts.add(host.getHostName());
                    } else {
                        org.apache.ambari.server.controller.KerberosHelperImpl.LOG.warn("Host {} was excluded due {} state", host.getHostName(), host.getState());
                    }
                }
            }
            return hosts;
        } else {
            java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> filteredComponents = filterServiceComponentHostsForHosts(new java.util.ArrayList<>(serviceComponentHosts), hostsWithValidKerberosClient);
            if (filteredComponents.isEmpty()) {
                return java.util.Collections.emptyList();
            } else {
                return createUniqueHostList(filteredComponents, java.util.Collections.singleton(org.apache.ambari.server.state.HostState.HEALTHY));
            }
        }
    }

    private class DeletePrincipalsAndKeytabsHandler extends org.apache.ambari.server.controller.KerberosHelperImpl.Handler {
        @java.lang.Override
        public long createStages(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterHostInfoJson, java.lang.String hostParamsJson, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.KerberosDetails kerberosDetails, java.io.File dataDirectory, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsWithValidKerberosClient) throws org.apache.ambari.server.AmbariException {
            if (requestStageContainer == null) {
                requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
            }
            if (kerberosDetails.manageIdentities()) {
                java.util.Map<java.lang.String, java.lang.String> commandParameters = new java.util.HashMap<>();
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, ambariManagementController.getAuthName());
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, kerberosDetails.getDefaultRealm());
                if (dataDirectory != null) {
                    commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, dataDirectory.getAbsolutePath());
                }
                if (serviceComponentFilter != null) {
                    commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.SERVICE_COMPONENT_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(serviceComponentFilter));
                }
                if (hostFilter != null) {
                    commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.HOST_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(hostFilter));
                }
                if (identityFilter != null) {
                    commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.IDENTITY_FILTER, org.apache.ambari.server.utils.StageUtils.getGson().toJson(identityFilter));
                }
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE, kerberosDetails.getKdcType().name());
                commandParameters.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_POLICY, org.apache.ambari.server.controller.UpdateConfigurationPolicy.ALL.name());
                addPrepareKerberosIdentitiesStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                addDeleteKeytabFilesStage(cluster, serviceComponentHosts, clusterHostInfoJson, hostParamsJson, commandParameters, roleCommandOrder, requestStageContainer, hostsWithValidKerberosClient);
                addDestroyPrincipalsStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
                addCleanupStage(cluster, clusterHostInfoJson, hostParamsJson, event, commandParameters, roleCommandOrder, requestStageContainer);
            }
            return requestStageContainer.getLastStageId();
        }
    }
}