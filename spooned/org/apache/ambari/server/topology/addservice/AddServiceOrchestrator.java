package org.apache.ambari.server.topology.addservice;
@javax.inject.Singleton
public class AddServiceOrchestrator {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.class);

    private static final org.apache.ambari.server.utils.LoggingPreconditions CHECK = new org.apache.ambari.server.utils.LoggingPreconditions(org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG);

    @javax.inject.Inject
    private org.apache.ambari.server.topology.addservice.ResourceProviderAdapter resourceProviders;

    @javax.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController controller;

    @javax.inject.Inject
    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @javax.inject.Inject
    private org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.topology.addservice.RequestValidatorFactory requestValidatorFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.topology.addservice.StackAdvisorAdapter stackAdvisorAdapter;

    public org.apache.ambari.server.controller.RequestStatusResponse processAddServiceRequest(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.topology.addservice.AddServiceRequest request) {
        org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Received {} request for {}: {}", request.getOperationType(), cluster.getClusterName(), request);
        org.apache.ambari.server.topology.addservice.AddServiceInfo validatedRequest = validate(cluster, request);
        ensureCredentials(cluster, validatedRequest);
        org.apache.ambari.server.topology.addservice.AddServiceInfo requestWithLayout = recommendLayout(validatedRequest);
        org.apache.ambari.server.topology.addservice.AddServiceInfo requestWithConfig = recommendConfiguration(requestWithLayout);
        createResources(cluster, requestWithConfig);
        createHostTasks(requestWithConfig);
        return requestWithConfig.getStages().getRequestStatusResponse();
    }

    private org.apache.ambari.server.topology.addservice.AddServiceInfo validate(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.topology.addservice.AddServiceRequest request) {
        org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Validating {}", request);
        org.apache.ambari.server.topology.addservice.RequestValidator validator = requestValidatorFactory.create(request, cluster);
        validator.validate();
        return validator.createValidServiceInfo(actionManager, requestFactory);
    }

    private void ensureCredentials(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.topology.addservice.AddServiceInfo validatedRequest) {
        resourceProviders.createCredentials(validatedRequest);
        if (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) {
            try {
                controller.getKerberosHelper().validateKDCCredentials(cluster);
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException | org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException | org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException e) {
                org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.CHECK.wrapInUnchecked(e, java.lang.IllegalArgumentException::new, "KDC credentials validation failed: %s", e);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.CHECK.wrapInUnchecked(e, java.lang.IllegalStateException::new, "Error occurred while validating KDC credentials: %s", e);
            }
        }
    }

    private org.apache.ambari.server.topology.addservice.AddServiceInfo recommendLayout(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        if (!request.requiresLayoutRecommendation()) {
            org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Using layout specified in request for {}", request);
            return request;
        }
        org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Recommending layout for {}", request);
        return stackAdvisorAdapter.recommendLayout(request);
    }

    private org.apache.ambari.server.topology.addservice.AddServiceInfo recommendConfiguration(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Recommending configuration for {}", request);
        return stackAdvisorAdapter.recommendConfigurations(request);
    }

    private void createResources(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Creating resources for {}", request);
        java.util.Set<java.lang.String> existingServices = cluster.getServices().keySet();
        updateKerberosDescriptor(request);
        resourceProviders.createServices(request);
        resourceProviders.createComponents(request);
        resourceProviders.updateServiceDesiredState(request, org.apache.ambari.server.state.State.INSTALLED);
        resourceProviders.updateServiceDesiredState(request, org.apache.ambari.server.state.State.STARTED);
        resourceProviders.createHostComponents(request);
        configureKerberos(request, cluster, existingServices);
        resourceProviders.updateExistingConfigs(request, existingServices);
        resourceProviders.createConfigs(request);
    }

    private void configureKerberos(org.apache.ambari.server.topology.addservice.AddServiceInfo request, org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> existingServices) {
        if (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) {
            org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Configuring Kerberos for {}", request);
            org.apache.ambari.server.topology.Configuration stackDefaultConfig = request.getStack().getValidDefaultConfig();
            java.util.Set<java.lang.String> newServices = request.newServices().keySet();
            java.util.Set<java.lang.String> services = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Sets.union(newServices, existingServices));
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = request.getConfig().getFullProperties();
            existingConfigurations.put(org.apache.ambari.server.controller.KerberosHelper.CLUSTER_HOST_INFO, org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.createComponentHostMap(cluster));
            try {
                org.apache.ambari.server.controller.KerberosHelper kerberosHelper = controller.getKerberosHelper();
                kerberosHelper.ensureHeadlessIdentities(cluster, existingConfigurations, services);
                request.getConfig().applyUpdatesToStackDefaultProperties(stackDefaultConfig, existingConfigurations, kerberosHelper.getServiceConfigurationUpdates(cluster, existingConfigurations, org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.createServiceComponentMap(cluster), null, existingServices, true, true));
            } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException e) {
                org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.CHECK.wrapInUnchecked(e, java.lang.RuntimeException::new, "Error configuring Kerberos for %s: %s", request, e);
            }
        }
    }

    private void createHostTasks(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.LOG.info("Creating host tasks for {}", request);
        org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder predicates = new org.apache.ambari.server.topology.addservice.ProvisionActionPredicateBuilder(request);
        for (org.apache.ambari.server.topology.ProvisionStep step : org.apache.ambari.server.topology.ProvisionStep.values()) {
            predicates.getPredicate(step).ifPresent(predicate -> resourceProviders.updateHostComponentDesiredState(request, predicate, step));
        }
        try {
            request.getStages().persist();
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.CHECK.wrapInUnchecked(e, java.lang.IllegalStateException::new, "Error creating host tasks for %s", request);
        }
    }

    private void updateKerberosDescriptor(org.apache.ambari.server.topology.addservice.AddServiceInfo request) {
        request.getKerberosDescriptor().ifPresent(descriptorInRequest -> {
            java.util.Optional<org.apache.ambari.server.state.kerberos.KerberosDescriptor> existingDescriptor = resourceProviders.getKerberosDescriptor(request);
            if (existingDescriptor.isPresent()) {
                org.apache.ambari.server.state.kerberos.KerberosDescriptor newDescriptor = existingDescriptor.get().update(descriptorInRequest);
                resourceProviders.updateKerberosDescriptor(request, newDescriptor);
            } else {
                resourceProviders.createKerberosDescriptor(request, descriptorInRequest);
            }
        });
    }

    private static java.util.Map<java.lang.String, java.lang.String> createComponentHostMap(org.apache.ambari.server.state.Cluster cluster) {
        return org.apache.ambari.server.utils.StageUtils.createComponentHostMap(cluster.getServices().keySet(), service -> org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.getComponentsForService(cluster, service), (service, component) -> org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.getHostsForServiceComponent(cluster, service, component));
    }

    private static java.util.Set<java.lang.String> getHostsForServiceComponent(org.apache.ambari.server.state.Cluster cluster, java.lang.String service, java.lang.String component) {
        try {
            return cluster.getService(service).getServiceComponent(component).getServiceComponentsHosts();
        } catch (org.apache.ambari.server.AmbariException e) {
            return org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.CHECK.wrapInUnchecked(e, java.lang.IllegalStateException::new, "Error getting hosts for service %s component %: %s", service, component, e, e);
        }
    }

    private static java.util.Set<java.lang.String> getComponentsForService(org.apache.ambari.server.state.Cluster cluster, java.lang.String service) {
        try {
            return cluster.getService(service).getServiceComponents().keySet();
        } catch (org.apache.ambari.server.AmbariException e) {
            return org.apache.ambari.server.topology.addservice.AddServiceOrchestrator.CHECK.wrapInUnchecked(e, java.lang.IllegalStateException::new, "Error getting components of service %s: %s", service, e, e);
        }
    }

    private static java.util.Map<java.lang.String, java.util.Set<java.lang.String>> createServiceComponentMap(org.apache.ambari.server.state.Cluster cluster) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceComponentMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Service> e : cluster.getServices().entrySet()) {
            serviceComponentMap.put(e.getKey(), com.google.common.collect.ImmutableSet.copyOf(e.getValue().getServiceComponents().keySet()));
        }
        return serviceComponentMap;
    }
}