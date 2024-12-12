package org.apache.ambari.server.serveraction.upgrades;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM;
import static org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV;
import static org.apache.ambari.server.controller.KerberosHelper.PRECONFIGURE_SERVICES;
public class PreconfigureKerberosAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    static final java.lang.String UPGRADE_DIRECTION_KEY = "upgrade_direction";

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.kerberos.VariableReplacementHelper variableReplacementHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        if ((null == commandParameters) || commandParameters.isEmpty()) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "Unable to change configuration values without command parameters");
        }
        if (!isDowngrade()) {
            java.lang.String clusterName = commandParameters.get("clusterName");
            org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
            if (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) {
                org.apache.ambari.server.state.StackId stackId;
                try {
                    stackId = getTargetStackId(cluster);
                } catch (org.apache.ambari.server.AmbariException e) {
                    return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", e.getLocalizedMessage());
                }
                if (stackId == null) {
                    return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", "", "The target stack Id was not specified.");
                }
                org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = kerberosHelper.getKerberosDescriptor(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.COMPOSITE, cluster, stackId, true, null);
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = kerberosHelper.calculateConfigurations(cluster, null, kerberosDescriptor, true, false, null);
                org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType preconfigureServiceType = getPreconfigureServiceType(configurations);
                if (preconfigureServiceType != org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.NONE) {
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToRemove = new java.util.HashMap<>();
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = new java.util.HashMap<>();
                    if (preconfigureServiceType == org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.ALL) {
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = kerberosDescriptor.getServices();
                        if (serviceDescriptors != null) {
                            for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
                                serviceDescriptor.setPreconfigure(true);
                            }
                        }
                    }
                    processServiceComponentHosts(cluster, kerberosDescriptor, configurations, kerberosConfigurations, propertiesToIgnore, getDefaultRealm(configurations));
                    kerberosConfigurations = kerberosHelper.processPreconfiguredServiceConfigurations(kerberosConfigurations, configurations, cluster, kerberosDescriptor);
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices = calculateInstalledServices(cluster);
                    kerberosHelper.applyStackAdvisorUpdates(cluster, installedServices.keySet(), configurations, kerberosConfigurations, propertiesToIgnore, propertiesToRemove, true);
                    kerberosHelper.setAuthToLocalRules(cluster, kerberosDescriptor, getDefaultRealm(configurations), installedServices, configurations, kerberosConfigurations, true);
                    processConfigurationChanges(cluster, stackId, kerberosDescriptor, kerberosConfigurations, propertiesToRemove, configurations);
                } else {
                    actionLog.writeStdOut("Skipping: This facility is only available when kerberos-env/preconfigure_services is not \"NONE\"");
                }
            } else {
                actionLog.writeStdOut("Skipping: This facility is only available when Kerberos is enabled");
            }
        } else {
            actionLog.writeStdOut("Skipping: This facility is only available during an upgrade");
        }
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateInstalledServices(org.apache.ambari.server.state.Cluster cluster) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
        for (org.apache.ambari.server.state.Service service : services.values()) {
            installedServices.put(service.getName(), service.getServiceComponents().keySet());
        }
        return installedServices;
    }

    private java.lang.String getValueFromConfiguration(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String configType, java.lang.String propertyName) {
        java.lang.String value = null;
        if (configurations != null) {
            java.util.Map<java.lang.String, java.lang.String> kerberosEnv = configurations.get(configType);
            if (kerberosEnv != null) {
                value = kerberosEnv.get(propertyName);
            }
        }
        return value;
    }

    private java.lang.String getDefaultRealm(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) {
        return getValueFromConfiguration(configurations, org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV, org.apache.ambari.server.controller.KerberosHelper.DEFAULT_REALM);
    }

    private org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType getPreconfigureServiceType(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) {
        java.lang.String preconfigureServices = getValueFromConfiguration(configurations, org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV, org.apache.ambari.server.controller.KerberosHelper.PRECONFIGURE_SERVICES);
        org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType preconfigureServiceType = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(preconfigureServices)) {
            try {
                preconfigureServiceType = org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.valueOf(preconfigureServices.toUpperCase());
            } catch (java.lang.Throwable t) {
                preconfigureServiceType = org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.DEFAULT;
            }
        }
        return preconfigureServiceType == null ? org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.DEFAULT : preconfigureServiceType;
    }

    private boolean isDowngrade() {
        return org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name().equalsIgnoreCase(getCommandParameterValue(org.apache.ambari.server.serveraction.upgrades.PreconfigureKerberosAction.UPGRADE_DIRECTION_KEY));
    }

    private org.apache.ambari.server.state.StackId getTargetStackId(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        java.util.Set<org.apache.ambari.server.state.StackId> stackIds = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepoVersion = upgradeContext.getTargetRepositoryVersion(service.getName());
            org.apache.ambari.server.state.StackId targetStackId = targetRepoVersion.getStackId();
            stackIds.add(targetStackId);
        }
        if (1 != stackIds.size()) {
            throw new org.apache.ambari.server.AmbariException("Services are deployed from multiple stacks and cannot determine a unique one.");
        }
        return stackIds.iterator().next();
    }

    private void processServiceComponentHosts(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> currentConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToBeIgnored, java.lang.String realm) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        if (!hosts.isEmpty()) {
            java.util.Map<java.lang.String, java.lang.Object> filterContext = new java.util.HashMap<>();
            filterContext.put("configurations", currentConfigurations);
            filterContext.put("services", cluster.getServices().keySet());
            try {
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = null;
                java.util.HashMap<java.lang.String, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> resolvedKeytabs = new java.util.HashMap<>();
                for (org.apache.ambari.server.state.Host host : hosts) {
                    for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(host.getHostName())) {
                        java.lang.String hostName = sch.getHostName();
                        java.lang.String serviceName = sch.getServiceName();
                        java.lang.String componentName = sch.getServiceComponentName();
                        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosDescriptor.getService(serviceName);
                        if (!org.apache.commons.lang.StringUtils.isEmpty(hostName)) {
                            java.util.Map<java.lang.String, java.lang.String> generalProperties = currentConfigurations.get("");
                            if (generalProperties == null) {
                                generalProperties = new java.util.HashMap<>();
                                currentConfigurations.put("", generalProperties);
                            }
                            generalProperties.put("host", hostName);
                            generalProperties.put("hostname", hostName);
                        }
                        if (serviceDescriptor != null) {
                            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities = serviceDescriptor.getIdentities(true, filterContext);
                            kerberosHelper.addIdentities(null, serviceIdentities, null, hostName, host.getHostId(), serviceName, componentName, kerberosConfigurations, currentConfigurations, resolvedKeytabs, realm);
                            propertiesToIgnore = gatherPropertiesToIgnore(serviceIdentities, propertiesToIgnore);
                            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = serviceDescriptor.getComponent(componentName);
                            if (componentDescriptor != null) {
                                java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities = componentDescriptor.getIdentities(true, filterContext);
                                kerberosHelper.mergeConfigurations(kerberosConfigurations, componentDescriptor.getConfigurations(true), currentConfigurations, null);
                                kerberosHelper.addIdentities(null, componentIdentities, null, hostName, host.getHostId(), serviceName, componentName, kerberosConfigurations, currentConfigurations, resolvedKeytabs, realm);
                                propertiesToIgnore = gatherPropertiesToIgnore(componentIdentities, propertiesToIgnore);
                            }
                        }
                    }
                }
                if (kerberosHelper.createAmbariIdentities(currentConfigurations.get(org.apache.ambari.server.controller.KerberosHelper.KERBEROS_ENV))) {
                    java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> ambariIdentities = kerberosHelper.getAmbariServerIdentities(kerberosDescriptor);
                    for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : ambariIdentities) {
                        java.lang.String componentName = (org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_KERBEROS_IDENTITY_NAME.equals(identity.getName())) ? "AMBARI_SERVER_SELF" : org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name();
                        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities = java.util.Collections.singletonList(identity);
                        kerberosHelper.addIdentities(null, componentIdentities, null, org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_HOST_NAME, ambariServerHostID(), org.apache.ambari.server.controller.RootService.AMBARI.name(), componentName, kerberosConfigurations, currentConfigurations, resolvedKeytabs, realm);
                        propertiesToIgnore = gatherPropertiesToIgnore(componentIdentities, propertiesToIgnore);
                    }
                }
                if ((propertiesToBeIgnored != null) && (propertiesToIgnore != null)) {
                    propertiesToBeIgnored.putAll(propertiesToIgnore);
                }
                java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> keytabList = kerberosKeytabPrincipalDAO.findAll();
                resolvedKeytabs.values().forEach(keytab -> kerberosHelper.createResolvedKeytab(keytab, keytabList));
            } catch (java.io.IOException e) {
                throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
            }
        }
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> gatherPropertiesToIgnore(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> identityConfigurations = kerberosHelper.getIdentityConfigurations(identities);
        if (!org.apache.commons.collections.MapUtils.isEmpty(identityConfigurations)) {
            if (propertiesToIgnore == null) {
                propertiesToIgnore = new java.util.HashMap<>();
            }
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : identityConfigurations.entrySet()) {
                java.lang.String configType = entry.getKey();
                java.util.Map<java.lang.String, java.lang.String> properties = entry.getValue();
                if (org.apache.commons.collections.MapUtils.isEmpty(properties)) {
                    java.util.Set<java.lang.String> propertyNames = propertiesToIgnore.get(configType);
                    if (propertyNames == null) {
                        propertyNames = new java.util.HashSet<>();
                        propertiesToIgnore.put(configType, propertyNames);
                    }
                    propertyNames.addAll(properties.keySet());
                }
            }
        }
        return propertiesToIgnore;
    }

    private void processConfigurationChanges(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId targetStackId, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToBeRemoved, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> variableReplaments) throws org.apache.ambari.server.AmbariException {
        actionLog.writeStdOut("Determining configuration changes");
        if (!kerberosConfigurations.isEmpty()) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> installedServices = cluster.getServices();
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertyFilter = new java.util.HashMap<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = kerberosDescriptor.getServices();
            if (serviceDescriptors != null) {
                for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
                    if ((!installedServices.containsKey(serviceDescriptor.getName())) && serviceDescriptor.shouldPreconfigure()) {
                        buildFilter(java.util.Collections.singleton(serviceDescriptor), propertyFilter, variableReplaments);
                    }
                }
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> authToLocalProperties = kerberosHelper.translateConfigurationSpecifications(kerberosDescriptor.getAllAuthToLocalProperties());
            if (!org.apache.commons.collections.MapUtils.isEmpty(authToLocalProperties)) {
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : authToLocalProperties.entrySet()) {
                    java.util.Set<java.lang.String> properties = entry.getValue();
                    if (!org.apache.commons.collections.CollectionUtils.isEmpty(properties)) {
                        java.lang.String configurationType = entry.getKey();
                        java.util.Set<java.lang.String> propertyNames = propertyFilter.get(configurationType);
                        if (propertyNames == null) {
                            propertyNames = new java.util.HashSet<>();
                            propertyFilter.put(configurationType, propertyNames);
                        }
                        propertyNames.addAll(properties);
                    }
                }
            }
            java.util.Set<java.lang.String> visitedTypes = new java.util.HashSet<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : kerberosConfigurations.entrySet()) {
                java.lang.String configType = entry.getKey();
                java.lang.String service = cluster.getServiceByConfigType(configType);
                java.util.Set<java.lang.String> allowedProperties = propertyFilter.get(configType);
                if (installedServices.containsKey(service) && (!org.apache.commons.collections.CollectionUtils.isEmpty(allowedProperties))) {
                    java.util.Map<java.lang.String, java.lang.String> propertiesToUpdate = entry.getValue();
                    java.util.Set<java.lang.String> propertiesToRemove = (propertiesToBeRemoved == null) ? null : propertiesToBeRemoved.get(configType);
                    if (propertiesToUpdate != null) {
                        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.String>> mapIterator = propertiesToUpdate.entrySet().iterator();
                        while (mapIterator.hasNext()) {
                            java.util.Map.Entry<java.lang.String, java.lang.String> mapEntry = mapIterator.next();
                            if (!allowedProperties.contains(mapEntry.getKey())) {
                                mapIterator.remove();
                            }
                        } 
                    }
                    if (propertiesToRemove != null) {
                        java.util.Iterator<java.lang.String> setIterator = propertiesToRemove.iterator();
                        while (setIterator.hasNext()) {
                            java.lang.String setEntry = setIterator.next();
                            if (!allowedProperties.contains(setEntry)) {
                                setIterator.remove();
                            }
                        } 
                    }
                    visitedTypes.add(configType);
                    if ((!org.apache.commons.collections.MapUtils.isEmpty(propertiesToUpdate)) || (!org.apache.commons.collections.CollectionUtils.isEmpty(propertiesToRemove))) {
                        if (!org.apache.commons.collections.MapUtils.isEmpty(propertiesToUpdate)) {
                            for (java.util.Map.Entry<java.lang.String, java.lang.String> property : propertiesToUpdate.entrySet()) {
                                actionLog.writeStdOut(java.lang.String.format("Setting: %s/%s = %s", configType, property.getKey(), property.getValue()));
                            }
                        }
                        if (!org.apache.commons.collections.CollectionUtils.isEmpty(propertiesToRemove)) {
                            for (java.lang.String property : propertiesToRemove) {
                                actionLog.writeStdOut(java.lang.String.format("Removing: %s/%s", configType, property));
                            }
                        }
                        configHelper.updateConfigType(cluster, targetStackId, ambariManagementController, configType, propertiesToUpdate, propertiesToRemove, ambariManagementController.getAuthName(), "Preconfiguring for Kerberos during upgrade");
                    }
                }
            }
            if (!org.apache.commons.collections.MapUtils.isEmpty(propertiesToBeRemoved)) {
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : propertiesToBeRemoved.entrySet()) {
                    java.lang.String configType = entry.getKey();
                    if (!visitedTypes.contains(configType)) {
                        java.util.Set<java.lang.String> propertiesToRemove = entry.getValue();
                        if (!org.apache.commons.collections.CollectionUtils.isEmpty(propertiesToRemove)) {
                            for (java.lang.String property : propertiesToRemove) {
                                actionLog.writeStdOut(java.lang.String.format("Removing: %s/%s", configType, property));
                            }
                            configHelper.updateConfigType(cluster, targetStackId, ambariManagementController, configType, null, entry.getValue(), ambariManagementController.getAuthName(), "Preconfiguring for Kerberos during upgrade");
                        }
                    }
                }
            }
        }
    }

    private void buildFilter(java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> containers, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertyFilter, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements) throws org.apache.ambari.server.AmbariException {
        if (containers != null) {
            for (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer container : containers) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurationDescriptors = container.getConfigurations(false);
                if (!org.apache.commons.collections.MapUtils.isEmpty(configurationDescriptors)) {
                    for (org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configurationDescriptor : configurationDescriptors.values()) {
                        java.util.Map<java.lang.String, java.lang.String> properties = configurationDescriptor.getProperties();
                        if (!org.apache.commons.collections.MapUtils.isEmpty(properties)) {
                            java.lang.String configType = configurationDescriptor.getType();
                            java.util.Set<java.lang.String> propertyNames = propertyFilter.get(configType);
                            if (propertyNames == null) {
                                propertyNames = new java.util.HashSet<>();
                                propertyFilter.put(configType, propertyNames);
                            }
                            for (java.lang.String propertyName : properties.keySet()) {
                                propertyNames.add(variableReplacementHelper.replaceVariables(propertyName, replacements));
                            }
                        }
                    }
                }
                java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> childContainers = container.getChildContainers();
                if (childContainers != null) {
                    buildFilter(childContainers, propertyFilter, replacements);
                }
            }
        }
    }

    protected java.lang.Long ambariServerHostID() {
        java.lang.String ambariServerHostName = org.apache.ambari.server.utils.StageUtils.getHostName();
        org.apache.ambari.server.orm.entities.HostEntity ambariServerHostEntity = hostDAO.findByName(ambariServerHostName);
        return ambariServerHostEntity == null ? null : ambariServerHostEntity.getHostId();
    }
}