package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
public abstract class AbstractPrepareKerberosServerAction extends org.apache.ambari.server.serveraction.kerberos.KerberosServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriterFactory kerberosIdentityDataFileWriterFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriterFactory kerberosConfigDataFileWriterFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @java.lang.Override
    protected org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        throw new java.lang.UnsupportedOperationException();
    }

    protected org.apache.ambari.server.controller.KerberosHelper getKerberosHelper() {
        return kerberosHelper;
    }

    public void processServiceComponentHosts(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schToProcess, java.util.Collection<java.lang.String> identityFilter, java.lang.String dataDirectory, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> currentConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, boolean includeAmbariIdentity, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToBeIgnored) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceComponentHost each : schToProcess) {
            components.add(org.apache.ambari.server.serveraction.kerberos.Component.fromServiceComponentHost(each));
        }
        processServiceComponents(cluster, kerberosDescriptor, components, identityFilter, dataDirectory, currentConfigurations, kerberosConfigurations, includeAmbariIdentity, propertiesToBeIgnored);
    }

    protected void processServiceComponents(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> schToProcess, java.util.Collection<java.lang.String> identityFilter, java.lang.String dataDirectory, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> currentConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, boolean includeAmbariIdentity, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToBeIgnored) throws org.apache.ambari.server.AmbariException {
        actionLog.writeStdOut("Processing Kerberos identities and configurations");
        if (!schToProcess.isEmpty()) {
            if (dataDirectory == null) {
                java.lang.String message = "The data directory has not been set.  Generated data can not be stored.";
                org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.error(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            java.io.File identityDataFile = new java.io.File(dataDirectory, org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.DATA_FILE_NAME);
            org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter kerberosIdentityDataFileWriter;
            java.util.Map<java.lang.String, java.lang.Object> filterContext = new java.util.HashMap<>();
            filterContext.put("configurations", currentConfigurations);
            filterContext.put("services", cluster.getServices().keySet());
            actionLog.writeStdOut(java.lang.String.format("Writing Kerberos identity data metadata file to %s", identityDataFile.getAbsolutePath()));
            try {
                kerberosIdentityDataFileWriter = kerberosIdentityDataFileWriterFactory.createKerberosIdentityDataFileWriter(identityDataFile);
            } catch (java.io.IOException e) {
                java.lang.String message = java.lang.String.format("Failed to write index file - %s", identityDataFile.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.error(message, e);
                actionLog.writeStdOut(message);
                actionLog.writeStdErr((message + "\n") + e.getLocalizedMessage());
                throw new org.apache.ambari.server.AmbariException(message, e);
            }
            java.util.HashMap<java.lang.String, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> resolvedKeytabs = new java.util.HashMap<>();
            java.lang.String realm = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDefaultRealm(getCommandParameters());
            try {
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = null;
                for (org.apache.ambari.server.serveraction.kerberos.Component sch : schToProcess) {
                    java.lang.String hostName = sch.getHostName();
                    java.lang.Long hostId = sch.getHostId();
                    java.lang.String serviceName = sch.getServiceName();
                    java.lang.String componentName = sch.getServiceComponentName();
                    org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosDescriptor.getService(serviceName);
                    if (serviceDescriptor != null) {
                        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities = serviceDescriptor.getIdentities(true, filterContext);
                        if (!org.apache.commons.lang.StringUtils.isEmpty(hostName)) {
                            java.util.Map<java.lang.String, java.lang.String> generalProperties = currentConfigurations.computeIfAbsent("", k -> new java.util.HashMap<>());
                            generalProperties.put("host", hostName);
                            generalProperties.put("hostname", hostName);
                        }
                        kerberosHelper.addIdentities(kerberosIdentityDataFileWriter, serviceIdentities, identityFilter, hostName, hostId, serviceName, componentName, kerberosConfigurations, currentConfigurations, resolvedKeytabs, realm);
                        propertiesToIgnore = gatherPropertiesToIgnore(serviceIdentities, propertiesToIgnore);
                        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = serviceDescriptor.getComponent(componentName);
                        if (componentDescriptor != null) {
                            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities = componentDescriptor.getIdentities(true, filterContext);
                            kerberosHelper.mergeConfigurations(kerberosConfigurations, componentDescriptor.getConfigurations(true), currentConfigurations, null);
                            kerberosHelper.addIdentities(kerberosIdentityDataFileWriter, componentIdentities, identityFilter, hostName, hostId, serviceName, componentName, kerberosConfigurations, currentConfigurations, resolvedKeytabs, realm);
                            propertiesToIgnore = gatherPropertiesToIgnore(componentIdentities, propertiesToIgnore);
                        }
                    }
                }
                if (includeAmbariIdentity && kerberosHelper.createAmbariIdentities(currentConfigurations.get("kerberos-env"))) {
                    java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> ambariIdentities = kerberosHelper.getAmbariServerIdentities(kerberosDescriptor);
                    if (!ambariIdentities.isEmpty()) {
                        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : ambariIdentities) {
                            java.lang.String componentName = (org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_KERBEROS_IDENTITY_NAME.equals(identity.getName())) ? "AMBARI_SERVER_SELF" : org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name();
                            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities = java.util.Collections.singletonList(identity);
                            kerberosHelper.addIdentities(kerberosIdentityDataFileWriter, componentIdentities, identityFilter, org.apache.ambari.server.utils.StageUtils.getHostName(), ambariServerHostID(), org.apache.ambari.server.controller.RootService.AMBARI.name(), componentName, kerberosConfigurations, currentConfigurations, resolvedKeytabs, realm);
                            propertiesToIgnore = gatherPropertiesToIgnore(componentIdentities, propertiesToIgnore);
                        }
                    }
                }
                if ((propertiesToBeIgnored != null) && (propertiesToIgnore != null)) {
                    propertiesToBeIgnored.putAll(propertiesToIgnore);
                }
                java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> keytabList = kerberosKeytabPrincipalDAO.findAll();
                resolvedKeytabs.values().forEach(keytab -> kerberosHelper.createResolvedKeytab(keytab, keytabList));
            } catch (java.io.IOException e) {
                java.lang.String message = java.lang.String.format("Failed to write index file - %s", identityDataFile.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.error(message, e);
                actionLog.writeStdOut(message);
                actionLog.writeStdErr((message + "\n") + e.getLocalizedMessage());
                throw new org.apache.ambari.server.AmbariException(message, e);
            } finally {
                if (kerberosIdentityDataFileWriter != null) {
                    try {
                        kerberosIdentityDataFileWriter.close();
                    } catch (java.io.IOException e) {
                        java.lang.String message = "Failed to close the index file writer";
                        org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.warn(message, e);
                        actionLog.writeStdOut(message);
                        actionLog.writeStdErr((message + "\n") + e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> gatherPropertiesToIgnore(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> identityConfigurations = kerberosHelper.getIdentityConfigurations(identities);
        if ((identityConfigurations != null) && (!identityConfigurations.isEmpty())) {
            if (propertiesToIgnore == null) {
                propertiesToIgnore = new java.util.HashMap<>();
            }
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : identityConfigurations.entrySet()) {
                java.lang.String configType = entry.getKey();
                java.util.Map<java.lang.String, java.lang.String> properties = entry.getValue();
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
        return propertiesToIgnore;
    }

    protected void processConfigurationChanges(java.lang.String dataDirectory, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToBeRemoved, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, org.apache.ambari.server.controller.UpdateConfigurationPolicy updateConfigurationPolicy) throws org.apache.ambari.server.AmbariException {
        actionLog.writeStdOut("Determining configuration changes");
        if (!kerberosConfigurations.isEmpty()) {
            if (dataDirectory == null) {
                java.lang.String message = "The data directory has not been set.  Generated data can not be stored.";
                org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.error(message);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> kerberosIdentityProperties = getIdentityProperties(kerberosDescriptor, null);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingProperties = configHelper.getEffectiveConfigProperties(getClusterName(), null);
            java.io.File configFile = new java.io.File(dataDirectory, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.DATA_FILE_NAME);
            org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter kerberosConfDataFileWriter = null;
            actionLog.writeStdOut(java.lang.String.format("Writing configuration changes metadata file to %s", configFile.getAbsolutePath()));
            try {
                kerberosConfDataFileWriter = kerberosConfigDataFileWriterFactory.createKerberosConfigDataFileWriter(configFile);
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : kerberosConfigurations.entrySet()) {
                    java.lang.String type = entry.getKey();
                    java.util.Map<java.lang.String, java.lang.String> properties = entry.getValue();
                    if (properties != null) {
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> configTypeEntry : properties.entrySet()) {
                            java.lang.String propertyName = configTypeEntry.getKey();
                            if (includeConfiguration(type, propertyName, updateConfigurationPolicy, existingProperties, kerberosIdentityProperties)) {
                                kerberosConfDataFileWriter.addRecord(type, propertyName, configTypeEntry.getValue(), org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.OPERATION_TYPE_SET);
                            }
                        }
                    }
                }
                if (propertiesToBeRemoved != null) {
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : propertiesToBeRemoved.entrySet()) {
                        java.lang.String type = entry.getKey();
                        java.util.Set<java.lang.String> properties = entry.getValue();
                        if (properties != null) {
                            for (java.lang.String property : properties) {
                                kerberosConfDataFileWriter.addRecord(type, property, "", org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileWriter.OPERATION_TYPE_REMOVE);
                            }
                        }
                    }
                }
            } catch (java.io.IOException e) {
                java.lang.String message = java.lang.String.format("Failed to write kerberos configurations file - %s", configFile.getAbsolutePath());
                org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.error(message, e);
                actionLog.writeStdOut(message);
                actionLog.writeStdErr((message + "\n") + e.getLocalizedMessage());
                throw new org.apache.ambari.server.AmbariException(message, e);
            } finally {
                if (kerberosConfDataFileWriter != null) {
                    try {
                        kerberosConfDataFileWriter.close();
                    } catch (java.io.IOException e) {
                        java.lang.String message = "Failed to close the kerberos configurations file writer";
                        org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.warn(message, e);
                        actionLog.writeStdOut(message);
                        actionLog.writeStdErr((message + "\n") + e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    private boolean includeConfiguration(java.lang.String configType, java.lang.String propertyName, org.apache.ambari.server.controller.UpdateConfigurationPolicy updateConfigurationPolicy, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingProperties, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> kerberosIdentityProperties) {
        boolean isIdentity;
        if (kerberosIdentityProperties == null) {
            isIdentity = false;
        } else {
            java.util.Set<java.lang.String> propertyNames = kerberosIdentityProperties.get(configType);
            isIdentity = (!org.apache.commons.collections.CollectionUtils.isEmpty(propertyNames)) && propertyNames.contains(propertyName);
        }
        if (isIdentity) {
            return updateConfigurationPolicy.applyIdentityChanges();
        }
        boolean isNew;
        if (existingProperties == null) {
            isNew = true;
        } else {
            java.util.Map<java.lang.String, java.lang.String> propertyNames = existingProperties.get(configType);
            isNew = (propertyNames == null) || (!propertyNames.containsKey(propertyName));
        }
        if (isNew) {
            return updateConfigurationPolicy.applyAdditions();
        }
        return updateConfigurationPolicy.applyOtherChanges();
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getIdentityProperties(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer container, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> identityProperties) {
        if (container != null) {
            if (identityProperties == null) {
                identityProperties = new java.util.HashMap<>();
            }
            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors;
            try {
                identityDescriptors = container.getIdentities(false, null);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.serveraction.kerberos.AbstractPrepareKerberosServerAction.LOG.error("An exception occurred getting the Kerberos identity descriptors.  No configurations will be identified.", e);
                identityDescriptors = null;
            }
            if (identityDescriptors != null) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> identityConfigurations = kerberosHelper.getIdentityConfigurations(identityDescriptors);
                if (identityConfigurations != null) {
                    for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : identityConfigurations.entrySet()) {
                        java.util.Map<java.lang.String, java.lang.String> properties = entry.getValue();
                        if (properties != null) {
                            java.util.Set<java.lang.String> configProperties = identityProperties.computeIfAbsent(entry.getKey(), k -> new java.util.HashSet<>());
                            configProperties.addAll(properties.keySet());
                        }
                    }
                }
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> authToLocalProperties = kerberosHelper.translateConfigurationSpecifications(container.getAuthToLocalProperties());
            if (authToLocalProperties != null) {
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : authToLocalProperties.entrySet()) {
                    java.lang.String configType = entry.getKey();
                    java.util.Set<java.lang.String> propertyNames = entry.getValue();
                    if (propertyNames != null) {
                        java.util.Set<java.lang.String> configProperties = identityProperties.computeIfAbsent(configType, k -> new java.util.HashSet<>());
                        configProperties.addAll(propertyNames);
                    }
                }
            }
            java.util.Collection<? extends org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer> childContainers = container.getChildContainers();
            if (childContainers != null) {
                for (org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer childContainer : childContainers) {
                    getIdentityProperties(childContainer, identityProperties);
                }
            }
        }
        return identityProperties;
    }
}