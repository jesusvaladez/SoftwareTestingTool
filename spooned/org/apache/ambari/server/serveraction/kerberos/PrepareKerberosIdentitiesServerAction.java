package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
public class PrepareKerberosIdentitiesServerAction extends org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction.class);

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.state.Cluster cluster = getCluster();
        if (cluster == null) {
            throw new org.apache.ambari.server.AmbariException("Missing cluster object");
        }
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = getKerberosHelper();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor(cluster, false);
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType operationType = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getOperationType(getCommandParameters());
        java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter = getServiceComponentFilter();
        java.util.Collection<java.lang.String> hostFilter = getHostFilter();
        java.util.Collection<java.lang.String> identityFilter = getIdentityFilter();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schToProcess = kerberosHelper.getServiceComponentHostsToProcess(cluster, kerberosDescriptor, operationType == org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.DEFAULT ? serviceComponentFilter : null, hostFilter);
        java.lang.String dataDirectory = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
        int schCount = schToProcess.size();
        if (schCount == 0) {
            actionLog.writeStdOut("There are no components to process");
        } else if (schCount == 1) {
            actionLog.writeStdOut(java.lang.String.format("Processing %d component", schCount));
        } else {
            actionLog.writeStdOut(java.lang.String.format("Processing %d components", schCount));
        }
        java.util.Set<java.lang.String> services = cluster.getServices().keySet();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToRemove = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = new java.util.HashMap<>();
        boolean includeAmbariIdentity = "true".equalsIgnoreCase(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.INCLUDE_AMBARI_IDENTITY));
        includeAmbariIdentity &= hostFilter == null;
        if (serviceComponentFilter != null) {
            includeAmbariIdentity &= (serviceComponentFilter.get(org.apache.ambari.server.controller.RootService.AMBARI.name()) != null) && serviceComponentFilter.get(org.apache.ambari.server.controller.RootService.AMBARI.name()).contains(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name());
            if (operationType != org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.DEFAULT) {
                identityFilter = updateIdentityFilter(kerberosDescriptor, identityFilter, serviceComponentFilter);
            }
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = kerberosHelper.calculateConfigurations(cluster, null, kerberosDescriptor, false, false, null);
        processServiceComponentHosts(cluster, kerberosDescriptor, schToProcess, identityFilter, dataDirectory, configurations, kerberosConfigurations, includeAmbariIdentity, propertiesToIgnore);
        org.apache.ambari.server.controller.UpdateConfigurationPolicy updateConfigurationPolicy = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getUpdateConfigurationPolicy(commandParameters);
        if (updateConfigurationPolicy != org.apache.ambari.server.controller.UpdateConfigurationPolicy.NONE) {
            if (updateConfigurationPolicy.invokeStackAdvisor()) {
                kerberosHelper.applyStackAdvisorUpdates(cluster, services, configurations, kerberosConfigurations, propertiesToIgnore, propertiesToRemove, true);
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculatedConfigurations = kerberosHelper.calculateConfigurations(cluster, null, kerberosDescriptor, false, false, null);
            if (updateConfigurationPolicy.applyIdentityChanges()) {
                processAuthToLocalRules(cluster, calculatedConfigurations, kerberosDescriptor, schToProcess, kerberosConfigurations, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDefaultRealm(commandParameters), false);
            }
            processConfigurationChanges(dataDirectory, kerberosConfigurations, propertiesToRemove, kerberosDescriptor, updateConfigurationPolicy);
        }
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
    }

    protected org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.state.Cluster cluster, boolean includePreconfigureData) throws org.apache.ambari.server.AmbariException {
        return getKerberosHelper().getKerberosDescriptor(cluster, includePreconfigureData);
    }

    void processAuthToLocalRules(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculatedConfiguration, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schToProcess, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.lang.String defaultRealm, boolean includePreconfiguredData) throws org.apache.ambari.server.AmbariException {
        if (!schToProcess.isEmpty()) {
            actionLog.writeStdOut("Creating auth-to-local rules");
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> services = new java.util.HashMap<>();
            for (org.apache.ambari.server.state.ServiceComponentHost sch : schToProcess) {
                java.util.Set<java.lang.String> components = services.get(sch.getServiceName());
                if (components == null) {
                    components = new java.util.HashSet<>();
                    services.put(sch.getServiceName(), components);
                }
                components.add(sch.getServiceComponentName());
            }
            org.apache.ambari.server.controller.KerberosHelper kerberosHelper = getKerberosHelper();
            kerberosHelper.setAuthToLocalRules(cluster, kerberosDescriptor, defaultRealm, services, calculatedConfiguration, kerberosConfigurations, includePreconfiguredData);
        }
    }

    private java.util.Collection<java.lang.String> updateIdentityFilter(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.Collection<java.lang.String> identityFilter, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) {
        java.util.Set<java.lang.String> updatedFilter = (identityFilter == null) ? new java.util.HashSet<>() : new java.util.HashSet<>(identityFilter);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = kerberosDescriptor.getServices();
        if (serviceDescriptors != null) {
            for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
                java.lang.String serviceName = serviceDescriptor.getName();
                if (serviceComponentFilter.containsKey("*") || serviceComponentFilter.containsKey(serviceName)) {
                    java.util.Collection<java.lang.String> componentFilter = serviceComponentFilter.get(serviceName);
                    boolean anyComponent = (componentFilter == null) || componentFilter.contains("*");
                    if (anyComponent) {
                        addIdentitiesToFilter(serviceDescriptor.getIdentities(), updatedFilter, true);
                    }
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
                    if (componentDescriptors != null) {
                        for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
                            java.lang.String componentName = componentDescriptor.getName();
                            if (anyComponent || componentFilter.contains(componentName)) {
                                addIdentitiesToFilter(componentDescriptor.getIdentities(), updatedFilter, true);
                            }
                        }
                    }
                }
            }
        }
        return updatedFilter;
    }

    private void addIdentitiesToFilter(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors, java.util.Collection<java.lang.String> identityFilter, boolean skipReferences) {
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(identityDescriptors)) {
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
                if ((!skipReferences) || (!identityDescriptor.isReference())) {
                    java.lang.String identityPath = identityDescriptor.getPath();
                    if (!org.apache.commons.lang.StringUtils.isEmpty(identityPath)) {
                        identityFilter.add(identityPath);
                        addIdentitiesToFilter(identityDescriptor.findReferences(), identityFilter, false);
                    }
                }
            }
        }
    }
}