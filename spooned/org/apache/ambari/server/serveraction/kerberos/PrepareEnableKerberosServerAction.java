package org.apache.ambari.server.serveraction.kerberos;
public class PrepareEnableKerberosServerAction extends org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.PrepareEnableKerberosServerAction.class);

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.state.Cluster cluster = getCluster();
        if (cluster == null) {
            throw new org.apache.ambari.server.AmbariException("Missing cluster object");
        }
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType type = getCommandPreconfigureType();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = getKerberosDescriptor(cluster, type != org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.NONE);
        if (type == org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.ALL) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = kerberosDescriptor.getServices();
            if (serviceDescriptors != null) {
                for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
                    serviceDescriptor.setPreconfigure(true);
                }
            }
        }
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = getKerberosHelper();
        java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter = getServiceComponentFilter();
        java.util.Collection<java.lang.String> hostFilter = getHostFilter();
        java.util.Collection<java.lang.String> identityFilter = getIdentityFilter();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schToProcess = kerberosHelper.getServiceComponentHostsToProcess(cluster, kerberosDescriptor, serviceComponentFilter, hostFilter);
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
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToRemove = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = new java.util.HashMap<>();
        java.util.Set<java.lang.String> services = cluster.getServices().keySet();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = kerberosHelper.calculateConfigurations(cluster, null, kerberosDescriptor, false, false, null);
        processServiceComponentHosts(cluster, kerberosDescriptor, schToProcess, identityFilter, dataDirectory, configurations, kerberosConfigurations, true, propertiesToIgnore);
        kerberosConfigurations = kerberosHelper.processPreconfiguredServiceConfigurations(kerberosConfigurations, configurations, cluster, kerberosDescriptor);
        kerberosHelper.applyStackAdvisorUpdates(cluster, services, configurations, kerberosConfigurations, propertiesToIgnore, propertiesToRemove, true);
        processAuthToLocalRules(cluster, configurations, kerberosDescriptor, schToProcess, kerberosConfigurations, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDefaultRealm(commandParameters), true);
        java.util.Map<java.lang.String, java.lang.String> clusterEnvProperties = kerberosConfigurations.get(org.apache.ambari.server.controller.KerberosHelper.SECURITY_ENABLED_CONFIG_TYPE);
        if (clusterEnvProperties == null) {
            clusterEnvProperties = new java.util.HashMap<>();
            kerberosConfigurations.put(org.apache.ambari.server.controller.KerberosHelper.SECURITY_ENABLED_CONFIG_TYPE, clusterEnvProperties);
        }
        clusterEnvProperties.put(org.apache.ambari.server.controller.KerberosHelper.SECURITY_ENABLED_PROPERTY_NAME, "true");
        processConfigurationChanges(dataDirectory, kerberosConfigurations, propertiesToRemove, kerberosDescriptor, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getUpdateConfigurationPolicy(commandParameters));
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
    }

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        throw new java.lang.UnsupportedOperationException();
    }
}