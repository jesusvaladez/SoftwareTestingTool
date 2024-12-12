package org.apache.ambari.server.topology.addservice;
import com.google.inject.assistedinject.Assisted;
public class RequestValidator {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.addservice.RequestValidator.class);

    private static final org.apache.ambari.server.utils.LoggingPreconditions CHECK = new org.apache.ambari.server.utils.LoggingPreconditions(org.apache.ambari.server.topology.addservice.RequestValidator.LOG);

    private static final java.util.Set<java.lang.String> NOT_ALLOWED_CONFIG_TYPES = com.google.common.collect.ImmutableSet.of("kerberos-env", "krb5-conf");

    private final org.apache.ambari.server.topology.addservice.AddServiceRequest request;

    private final org.apache.ambari.server.state.Cluster cluster;

    private final org.apache.ambari.server.controller.AmbariManagementController controller;

    private final org.apache.ambari.server.state.ConfigHelper configHelper;

    private final org.apache.ambari.server.topology.StackFactory stackFactory;

    private final org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory;

    private final java.util.concurrent.atomic.AtomicBoolean serviceInfoCreated = new java.util.concurrent.atomic.AtomicBoolean();

    private final org.apache.ambari.server.topology.SecurityConfigurationFactory securityConfigurationFactory;

    private org.apache.ambari.server.topology.addservice.RequestValidator.State state;

    @javax.inject.Inject
    public RequestValidator(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.addservice.AddServiceRequest request, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.AmbariManagementController controller, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.topology.StackFactory stackFactory, org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory, org.apache.ambari.server.topology.SecurityConfigurationFactory securityConfigurationFactory) {
        this.state = org.apache.ambari.server.topology.addservice.RequestValidator.State.INITIAL;
        this.request = request;
        this.cluster = cluster;
        this.controller = controller;
        this.configHelper = configHelper;
        this.stackFactory = stackFactory;
        this.kerberosDescriptorFactory = kerberosDescriptorFactory;
        this.securityConfigurationFactory = securityConfigurationFactory;
    }

    void validate() {
        validateStack();
        validateServicesAndComponents();
        validateSecurity();
        validateHosts();
        validateConfiguration();
    }

    org.apache.ambari.server.topology.addservice.AddServiceInfo createValidServiceInfo(org.apache.ambari.server.actionmanager.ActionManager actionManager, org.apache.ambari.server.actionmanager.RequestFactory requestFactory) {
        final org.apache.ambari.server.topology.addservice.RequestValidator.State state = this.state;
        org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkState(state.isValid(), "The request needs to be validated first");
        org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkState(!serviceInfoCreated.getAndSet(true), "Can create only one instance for each validated add service request");
        org.apache.ambari.server.controller.internal.RequestStageContainer stages = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
        org.apache.ambari.server.topology.addservice.AddServiceInfo validatedRequest = new org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder().setRequest(request).setClusterName(cluster.getClusterName()).setStages(stages).setStack(state.getStack()).setConfig(state.getConfig()).setNewServices(state.getNewServices()).setKerberosDescriptor(state.getKerberosDescriptor()).build();
        stages.setRequestContext(validatedRequest.describe());
        return validatedRequest;
    }

    @com.google.common.annotations.VisibleForTesting
    org.apache.ambari.server.topology.addservice.RequestValidator.State getState() {
        return state;
    }

    @com.google.common.annotations.VisibleForTesting
    void setState(org.apache.ambari.server.topology.addservice.RequestValidator.State state) {
        this.state = state;
    }

    @com.google.common.annotations.VisibleForTesting
    void validateSecurity() {
        request.getSecurity().ifPresent(requestSecurity -> {
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument((!strictValidation()) || (requestSecurity.getType() == cluster.getSecurityType()), "Security type in the request (%s), if specified, should match cluster's security type (%s)", requestSecurity.getType(), cluster.getSecurityType());
            boolean hasDescriptor = requestSecurity.getDescriptor().isPresent();
            boolean hasDescriptorReference = requestSecurity.getDescriptorReference() != null;
            boolean secureCluster = cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS;
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(secureCluster || (!hasDescriptor), "Kerberos descriptor cannot be set for security type %s", cluster.getSecurityType());
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(secureCluster || (!hasDescriptorReference), "Kerberos descriptor reference cannot be set for security type %s", cluster.getSecurityType());
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument((!hasDescriptor) || (!hasDescriptorReference), "Kerberos descriptor and reference cannot be both set");
            java.util.Optional<java.util.Map<?, ?>> kerberosDescriptor = (hasDescriptor) ? requestSecurity.getDescriptor() : hasDescriptorReference ? loadKerberosDescriptor(requestSecurity.getDescriptorReference()) : java.util.Optional.empty();
            kerberosDescriptor.ifPresent(descriptorMap -> {
                org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkState(state.getNewServices() != null, "Services need to be validated before security settings");
                org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = kerberosDescriptorFactory.createInstance(descriptorMap);
                if (strictValidation()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> descriptorServices = descriptor.getServices();
                    java.util.Set<java.lang.String> servicesWithNewDescriptor = (descriptorServices != null) ? descriptorServices.keySet() : com.google.common.collect.ImmutableSet.of();
                    java.util.Set<java.lang.String> newServices = state.getNewServices().keySet();
                    java.util.Set<java.lang.String> nonNewServices = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Sets.difference(servicesWithNewDescriptor, newServices));
                    org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(nonNewServices.isEmpty(), "Kerberos descriptor should be provided only for new services, but found other services: %s", nonNewServices);
                }
                try {
                    descriptor.toMap();
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.wrapInUnchecked(e, java.lang.IllegalArgumentException::new, "Error validating Kerberos descriptor: %s", e);
                }
                state = state.with(descriptor);
            });
        });
    }

    @com.google.common.annotations.VisibleForTesting
    void validateStack() {
        java.util.Optional<org.apache.ambari.server.state.StackId> requestStackId = request.getStackId();
        org.apache.ambari.server.state.StackId stackId = requestStackId.orElseGet(cluster::getCurrentStackVersion);
        try {
            org.apache.ambari.server.controller.internal.Stack stack = stackFactory.createStack(stackId.getStackName(), stackId.getStackVersion(), controller);
            state = state.with(stack);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.wrapInUnchecked(e, requestStackId.isPresent() ? java.lang.IllegalArgumentException::new : java.lang.IllegalStateException::new, "Stack %s not found", stackId);
        }
    }

    @com.google.common.annotations.VisibleForTesting
    void validateServicesAndComponents() {
        org.apache.ambari.server.controller.internal.Stack stack = state.getStack();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = new java.util.LinkedHashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.google.common.collect.Multiset<java.lang.String>>> withAllHosts = new java.util.LinkedHashMap<>();
        java.util.Set<java.lang.String> existingServices = cluster.getServices().keySet();
        for (org.apache.ambari.server.topology.addservice.Service service : request.getServices()) {
            java.lang.String serviceName = service.getName();
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(stack.getServices().contains(serviceName), "Unknown service %s in %s", service, stack);
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(!existingServices.contains(serviceName), "Service %s already exists in cluster %s", serviceName, cluster.getClusterName());
            newServices.computeIfAbsent(serviceName, __ -> new java.util.HashMap<>());
        }
        for (org.apache.ambari.server.topology.addservice.Component requestedComponent : request.getComponents()) {
            java.lang.String componentName = requestedComponent.getName();
            java.lang.String serviceName = stack.getServiceForComponent(componentName);
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(serviceName != null, "No service found for component %s in %s", componentName, stack);
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(!existingServices.contains(serviceName), "Service %s (for component %s) already exists in cluster %s", serviceName, componentName, cluster.getClusterName());
            java.util.List<java.lang.String> hosts = requestedComponent.getHosts().stream().map(org.apache.ambari.server.topology.addservice.Host::getFqdn).collect(java.util.stream.Collectors.toList());
            newServices.computeIfAbsent(serviceName, __ -> new java.util.HashMap<>()).computeIfAbsent(componentName, __ -> new java.util.HashSet<>()).addAll(hosts);
            withAllHosts.computeIfAbsent(serviceName, __ -> new java.util.HashMap<>()).computeIfAbsent(componentName, __ -> com.google.common.collect.HashMultiset.create()).addAll(hosts);
        }
        org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(!newServices.isEmpty(), "Request should have at least one new service or component to be added");
        newServices.forEach((service, components) -> components.forEach((component, hosts) -> {
            com.google.common.collect.Multiset<java.lang.String> allHosts = withAllHosts.get(service).get(component);
            com.google.common.collect.Multisets.removeOccurrences(allHosts, hosts);
            org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(allHosts.isEmpty(), "Some hosts appear multiple times for the same component (%s) in the request: %s", component, allHosts);
        }));
        state = state.withNewServices(newServices);
    }

    @com.google.common.annotations.VisibleForTesting
    void validateConfiguration() {
        org.apache.ambari.server.topology.Configuration config = request.getConfiguration();
        if (strictValidation()) {
            for (java.lang.String type : org.apache.ambari.server.topology.addservice.RequestValidator.NOT_ALLOWED_CONFIG_TYPES) {
                org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(!config.getProperties().containsKey(type), "Cannot change '%s' configuration in Add Service request", type);
            }
        }
        org.apache.ambari.server.topology.Configuration clusterConfig = getClusterDesiredConfigs();
        clusterConfig.setParentConfiguration(state.getStack().getDefaultConfig());
        config.setParentConfiguration(clusterConfig);
        org.apache.ambari.server.controller.internal.UnitUpdater.removeUnits(config, state.getStack());
        state = state.with(config);
    }

    @com.google.common.annotations.VisibleForTesting
    void validateHosts() {
        java.util.Set<java.lang.String> clusterHosts = cluster.getHostNames();
        java.util.Set<java.lang.String> requestHosts = state.getNewServices().values().stream().flatMap(componentHosts -> componentHosts.values().stream()).flatMap(java.util.Collection::stream).collect(java.util.stream.Collectors.toSet());
        java.util.Set<java.lang.String> unknownHosts = new java.util.TreeSet<>(com.google.common.collect.Sets.difference(requestHosts, clusterHosts));
        org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.checkArgument(unknownHosts.isEmpty(), "Requested host not associated with cluster %s: %s", cluster.getClusterName(), unknownHosts);
    }

    private boolean strictValidation() {
        return request.getValidationType().strictValidation();
    }

    private org.apache.ambari.server.topology.Configuration getClusterDesiredConfigs() {
        try {
            return org.apache.ambari.server.topology.Configuration.of(configHelper.calculateExistingConfigs(cluster));
        } catch (org.apache.ambari.server.AmbariException e) {
            return org.apache.ambari.server.topology.addservice.RequestValidator.CHECK.wrapInUnchecked(e, java.lang.IllegalStateException::new, "Error getting effective configuration of cluster %s", cluster.getClusterName());
        }
    }

    private java.util.Optional<java.util.Map<?, ?>> loadKerberosDescriptor(java.lang.String descriptorReference) {
        return securityConfigurationFactory.loadSecurityConfigurationByReference(descriptorReference).getDescriptor();
    }

    @com.google.common.annotations.VisibleForTesting
    static class State {
        static final org.apache.ambari.server.topology.addservice.RequestValidator.State INITIAL = new org.apache.ambari.server.topology.addservice.RequestValidator.State(null, null, null, null);

        private final org.apache.ambari.server.controller.internal.Stack stack;

        private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices;

        private final org.apache.ambari.server.topology.Configuration config;

        private final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;

        State(org.apache.ambari.server.controller.internal.Stack stack, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices, org.apache.ambari.server.topology.Configuration config, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
            this.stack = stack;
            this.newServices = newServices;
            this.config = config;
            this.kerberosDescriptor = kerberosDescriptor;
        }

        boolean isValid() {
            return ((stack != null) && (newServices != null)) && (config != null);
        }

        org.apache.ambari.server.topology.addservice.RequestValidator.State with(org.apache.ambari.server.controller.internal.Stack stack) {
            return new org.apache.ambari.server.topology.addservice.RequestValidator.State(stack, newServices, config, kerberosDescriptor);
        }

        org.apache.ambari.server.topology.addservice.RequestValidator.State withNewServices(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices) {
            return new org.apache.ambari.server.topology.addservice.RequestValidator.State(stack, newServices, config, kerberosDescriptor);
        }

        org.apache.ambari.server.topology.addservice.RequestValidator.State with(org.apache.ambari.server.topology.Configuration config) {
            return new org.apache.ambari.server.topology.addservice.RequestValidator.State(stack, newServices, config, kerberosDescriptor);
        }

        org.apache.ambari.server.topology.addservice.RequestValidator.State with(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
            return new org.apache.ambari.server.topology.addservice.RequestValidator.State(stack, newServices, config, kerberosDescriptor);
        }

        org.apache.ambari.server.controller.internal.Stack getStack() {
            return stack;
        }

        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> getNewServices() {
            return newServices;
        }

        org.apache.ambari.server.topology.Configuration getConfig() {
            return config;
        }

        org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor() {
            return kerberosDescriptor;
        }
    }
}