package org.apache.ambari.server.serveraction.kerberos;
public class UpdateKerberosConfigsServerAction extends org.apache.ambari.server.serveraction.AbstractServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController controller;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReaderFactory kerberosConfigDataFileReaderFactory;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.agent.CommandReport commandReport = null;
        java.lang.String clusterName = getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Clusters clusters = controller.getClusters();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        java.lang.String authenticatedUserName = org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction.getCommandParameterValue(getCommandParameters(), org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME);
        java.lang.String dataDirectoryPath = org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction.getCommandParameterValue(getCommandParameters(), org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesToSet = new java.util.HashMap<>();
        java.util.HashMap<java.lang.String, java.util.Collection<java.lang.String>> propertiesToRemove = new java.util.HashMap<>();
        if (dataDirectoryPath != null) {
            java.io.File dataDirectory = new java.io.File(dataDirectoryPath);
            if (dataDirectory.exists()) {
                org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader configReader = null;
                java.util.Set<java.lang.String> configTypes = new java.util.HashSet<>();
                try {
                    java.io.File configFile = new java.io.File(dataDirectory, org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.DATA_FILE_NAME);
                    if (configFile.exists()) {
                        configReader = kerberosConfigDataFileReaderFactory.createKerberosConfigDataFileReader(configFile);
                        for (java.util.Map<java.lang.String, java.lang.String> record : configReader) {
                            java.lang.String configType = record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.CONFIGURATION_TYPE);
                            java.lang.String configKey = record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.KEY);
                            java.lang.String configOp = record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.OPERATION);
                            configTypes.add(configType);
                            if (org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.OPERATION_TYPE_REMOVE.equals(configOp)) {
                                removeConfigTypeProp(propertiesToRemove, configType, configKey);
                            } else {
                                java.lang.String configVal = record.get(org.apache.ambari.server.serveraction.kerberos.KerberosConfigDataFileReader.VALUE);
                                addConfigTypePropVal(propertiesToSet, configType, configKey, configVal);
                            }
                        }
                    }
                    final java.lang.String securityEnabled = (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) ? "true" : "false";
                    if (!configTypes.contains("cluster-env")) {
                        configTypes.add("cluster-env");
                    }
                    java.util.Map<java.lang.String, java.lang.String> clusterEnvProperties = propertiesToSet.get("cluster-env");
                    if (clusterEnvProperties == null) {
                        clusterEnvProperties = new java.util.HashMap<>();
                        propertiesToSet.put("cluster-env", clusterEnvProperties);
                    }
                    clusterEnvProperties.put("security_enabled", securityEnabled);
                    java.lang.String configNote = org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction.getCommandParameterValue(getCommandParameters(), org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_NOTE);
                    if ((configNote == null) || configNote.isEmpty()) {
                        configNote = (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) ? "Enabling Kerberos" : "Disabling Kerberos";
                    }
                    configHelper.updateBulkConfigType(cluster, cluster.getDesiredStackVersion(), controller, configTypes, propertiesToSet, propertiesToRemove, authenticatedUserName, configNote);
                } catch (java.io.IOException e) {
                    java.lang.String message = "Could not update services configs to enable kerberos";
                    actionLog.writeStdErr(message);
                    org.apache.ambari.server.serveraction.kerberos.UpdateKerberosConfigsServerAction.LOG.error(message, e);
                    commandReport = createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
                } finally {
                    if ((configReader != null) && (!configReader.isClosed())) {
                        try {
                            configReader.close();
                        } catch (java.lang.Throwable t) {
                        }
                    }
                }
            }
        }
        return commandReport == null ? createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr()) : commandReport;
    }

    private static java.lang.String getCommandParameterValue(java.util.Map<java.lang.String, java.lang.String> commandParameters, java.lang.String propertyName) {
        return (commandParameters == null) || (propertyName == null) ? null : commandParameters.get(propertyName);
    }

    private void addConfigTypePropVal(java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.lang.String configType, java.lang.String prop, java.lang.String val) {
        java.util.Map<java.lang.String, java.lang.String> configTypePropsVal = configurations.get(configType);
        if (configTypePropsVal == null) {
            configTypePropsVal = new java.util.HashMap<>();
            configurations.put(configType, configTypePropsVal);
        }
        configTypePropsVal.put(prop, val);
        actionLog.writeStdOut(java.lang.String.format("Setting property %s/%s: %s", configType, prop, val == null ? "<null>" : val));
    }

    private void removeConfigTypeProp(java.util.HashMap<java.lang.String, java.util.Collection<java.lang.String>> configurations, java.lang.String configType, java.lang.String prop) {
        java.util.Collection<java.lang.String> configTypeProps = configurations.get(configType);
        if (configTypeProps == null) {
            configTypeProps = new java.util.HashSet<>();
            configurations.put(configType, configTypeProps);
        }
        configTypeProps.add(prop);
        actionLog.writeStdOut(java.lang.String.format("Removing property %s/%s", configType, prop));
    }
}