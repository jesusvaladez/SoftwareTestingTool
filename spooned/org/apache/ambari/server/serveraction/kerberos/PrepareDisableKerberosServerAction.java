package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.collections.CollectionUtils;
public class PrepareDisableKerberosServerAction extends org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.PrepareDisableKerberosServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.state.Cluster cluster = getCluster();
        if (cluster == null) {
            throw new org.apache.ambari.server.AmbariException("Missing cluster object");
        }
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = getKerberosHelper();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = kerberosHelper.getKerberosDescriptor(cluster, false);
        java.util.Collection<java.lang.String> identityFilter = getIdentityFilter();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schToProcess = kerberosHelper.getServiceComponentHostsToProcess(cluster, kerberosDescriptor, getServiceComponentFilter(), null);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        java.lang.String dataDirectory = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY);
        int schCount = schToProcess.size();
        if (schCount == 0) {
            actionLog.writeStdOut("There are no components to process");
        } else if (schCount == 1) {
            actionLog.writeStdOut(java.lang.String.format("Processing %d component", schCount));
        } else {
            actionLog.writeStdOut(java.lang.String.format("Processing %d components", schCount));
        }
        java.util.Set<java.lang.String> services = cluster.getServices().keySet();
        boolean includeAmbariIdentity = "true".equalsIgnoreCase(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.INCLUDE_AMBARI_IDENTITY));
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = kerberosHelper.calculateConfigurations(cluster, null, kerberosDescriptor, false, false, null);
        processServiceComponentHosts(cluster, kerberosDescriptor, schToProcess, identityFilter, dataDirectory, configurations, kerberosConfigurations, includeAmbariIdentity, propertiesToIgnore);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> authToLocalProperties = kerberosHelper.translateConfigurationSpecifications(kerberosDescriptor.getAllAuthToLocalProperties());
        if (authToLocalProperties != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : authToLocalProperties.entrySet()) {
                java.lang.String configType = entry.getKey();
                java.util.Set<java.lang.String> propertyNames = entry.getValue();
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(propertyNames)) {
                    for (java.lang.String propertyName : propertyNames) {
                        java.util.Map<java.lang.String, java.lang.String> configuration = kerberosConfigurations.get(configType);
                        if (configuration != null) {
                            configuration.put(propertyName, "DEFAULT");
                        }
                    }
                }
            }
        }
        actionLog.writeStdOut("Determining configuration changes");
        java.util.Map<java.lang.String, java.lang.String> clusterEnvProperties = kerberosConfigurations.get(org.apache.ambari.server.controller.KerberosHelper.SECURITY_ENABLED_CONFIG_TYPE);
        if (clusterEnvProperties == null) {
            clusterEnvProperties = new java.util.HashMap<>();
            kerberosConfigurations.put(org.apache.ambari.server.controller.KerberosHelper.SECURITY_ENABLED_CONFIG_TYPE, clusterEnvProperties);
        }
        clusterEnvProperties.put(org.apache.ambari.server.controller.KerberosHelper.SECURITY_ENABLED_PROPERTY_NAME, "false");
        if (!kerberosConfigurations.isEmpty()) {
            if (dataDirectory == null) {
                java.lang.String message = "The data directory has not been set.  Generated data can not be stored.";
                org.apache.ambari.server.serveraction.kerberos.PrepareDisableKerberosServerAction.LOG.error(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> configurationsToRemove = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : kerberosConfigurations.entrySet()) {
                configurationsToRemove.put(entry.getKey(), new java.util.HashSet<>(entry.getValue().keySet()));
            }
            configurationsToRemove.remove("cluster-env");
            if (!schToProcess.isEmpty()) {
                java.util.Set<java.lang.String> visitedServices = new java.util.HashSet<>();
                for (org.apache.ambari.server.state.ServiceComponentHost sch : schToProcess) {
                    java.lang.String serviceName = sch.getServiceName();
                    if (!visitedServices.contains(serviceName)) {
                        org.apache.ambari.server.state.ServiceComponent serviceComponent = sch.getServiceComponent();
                        org.apache.ambari.server.state.StackId stackVersion = serviceComponent.getDesiredStackId();
                        visitedServices.add(serviceName);
                        if (stackVersion != null) {
                            java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = configHelper.getServiceProperties(stackVersion, serviceName, true);
                            if (serviceProperties != null) {
                                for (org.apache.ambari.server.state.PropertyInfo propertyInfo : serviceProperties) {
                                    java.lang.String filename = propertyInfo.getFilename();
                                    if (filename != null) {
                                        java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(filename);
                                        java.lang.String propertyName = propertyInfo.getName();
                                        java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration = kerberosConfigurations.get(type);
                                        if ((kerberosConfiguration != null) && kerberosConfiguration.containsKey(propertyName)) {
                                            kerberosConfiguration.put(propertyName, propertyInfo.getValue());
                                        }
                                        java.util.Collection<java.lang.String> propertiesToRemove = configurationsToRemove.get(type);
                                        if (propertiesToRemove != null) {
                                            propertiesToRemove.remove(propertyName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            kerberosHelper.applyStackAdvisorUpdates(cluster, services, configurations, kerberosConfigurations, propertiesToIgnore, configurationsToRemove, false);
            processConfigurationChanges(dataDirectory, kerberosConfigurations, configurationsToRemove, kerberosDescriptor, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getUpdateConfigurationPolicy(commandParameters));
        }
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
    }

    @java.lang.Override
    protected boolean pruneServiceFilter() {
        return false;
    }
}