package org.apache.ambari.server.serveraction.kerberos;
import org.apache.commons.lang.StringUtils;
public abstract class KerberosServerAction extends org.apache.ambari.server.serveraction.AbstractServerAction {
    public static final java.lang.String AUTHENTICATED_USER_NAME = "authenticated_user_name";

    public static final java.lang.String DATA_DIRECTORY = "data_directory";

    public static final java.lang.String DEFAULT_REALM = "default_realm";

    public static final java.lang.String SERVICE_COMPONENT_FILTER = "service_component_filter";

    public static final java.lang.String HOST_FILTER = "host_filter";

    public static final java.lang.String IDENTITY_FILTER = "identity_filter";

    public static final java.lang.String KDC_TYPE = "kdc_type";

    public static final java.lang.String UPDATE_CONFIGURATION_POLICY = "update_configuration_policy";

    public static final java.lang.String UPDATE_CONFIGURATION_NOTE = "update_configuration_note";

    public static final java.lang.String DATA_DIRECTORY_PREFIX = ".ambari_";

    private static final java.lang.String PRINCIPAL_PASSWORD_MAP = "principal_password_map";

    private static final java.lang.String PRINCIPAL_KEY_NUMBER_MAP = "principal_key_number_map";

    public static final java.lang.String KEYTAB_CONTENT_BASE64 = "keytab_content_base64";

    public static final java.lang.String OPERATION_TYPE = "operation_type";

    public static final java.lang.String INCLUDE_AMBARI_IDENTITY = "include_ambari_identity";

    public static final java.lang.String PRECONFIGURE_SERVICES = "preconfigure_services";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters = null;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandlerFactory kerberosOperationHandlerFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabController;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private org.apache.ambari.server.utils.ThreadPools threadPools;

    protected static java.lang.String getCommandParameterValue(java.util.Map<java.lang.String, java.lang.String> commandParameters, java.lang.String propertyName) {
        return (commandParameters == null) || (propertyName == null) ? null : commandParameters.get(propertyName);
    }

    protected static org.apache.ambari.server.controller.UpdateConfigurationPolicy getUpdateConfigurationPolicy(java.util.Map<java.lang.String, java.lang.String> commandParameters) {
        java.lang.String stringValue = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.UPDATE_CONFIGURATION_POLICY);
        org.apache.ambari.server.controller.UpdateConfigurationPolicy value = org.apache.ambari.server.controller.UpdateConfigurationPolicy.translate(stringValue);
        return value == null ? org.apache.ambari.server.controller.UpdateConfigurationPolicy.ALL : value;
    }

    protected static java.lang.String getDefaultRealm(java.util.Map<java.lang.String, java.lang.String> commandParameters) {
        return org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM);
    }

    protected static org.apache.ambari.server.serveraction.kerberos.KDCType getKDCType(java.util.Map<java.lang.String, java.lang.String> commandParameters) {
        java.lang.String kdcType = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KDC_TYPE);
        return (kdcType == null) || kdcType.isEmpty() ? org.apache.ambari.server.serveraction.kerberos.KDCType.NONE : org.apache.ambari.server.serveraction.kerberos.KDCType.translate(kdcType);
    }

    protected static java.lang.String getDataDirectoryPath(java.util.Map<java.lang.String, java.lang.String> commandParameters) {
        return org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY);
    }

    protected static org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType getOperationType(java.util.Map<java.lang.String, java.lang.String> commandParameters) {
        java.lang.String value = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(commandParameters, org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OPERATION_TYPE);
        if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
            return org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.DEFAULT;
        } else {
            return org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.OperationType.valueOf(value.toUpperCase());
        }
    }

    protected static void setPrincipalPasswordMap(java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext, java.util.Map<java.lang.String, java.lang.String> principalPasswordMap) {
        if (requestSharedDataContext != null) {
            requestSharedDataContext.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.PRINCIPAL_PASSWORD_MAP, principalPasswordMap);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    protected static java.util.Map<java.lang.String, java.lang.String> getPrincipalPasswordMap(java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) {
        if (requestSharedDataContext == null) {
            return null;
        }
        java.lang.Object map = requestSharedDataContext.computeIfAbsent(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.PRINCIPAL_PASSWORD_MAP, k -> new java.util.HashMap<java.lang.String, java.lang.String>());
        return ((java.util.Map<java.lang.String, java.lang.String>) (map));
    }

    @java.lang.SuppressWarnings("unchecked")
    protected static java.util.Map<java.lang.String, java.lang.Integer> getPrincipalKeyNumberMap(java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) {
        if (requestSharedDataContext == null) {
            return null;
        }
        java.lang.Object map = requestSharedDataContext.computeIfAbsent(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.PRINCIPAL_KEY_NUMBER_MAP, k -> new java.util.HashMap<java.lang.String, java.lang.String>());
        return ((java.util.Map<java.lang.String, java.lang.Integer>) (map));
    }

    protected java.lang.String getClusterName() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand();
        java.lang.String clusterName = (executionCommand == null) ? null : executionCommand.getClusterName();
        if ((clusterName == null) || clusterName.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("Failed to retrieve the cluster name from the execution command");
        }
        return clusterName;
    }

    protected org.apache.ambari.server.state.Cluster getCluster() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(getClusterName());
        if (cluster == null) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to retrieve cluster for %s", getClusterName()));
        }
        return cluster;
    }

    protected org.apache.ambari.server.state.Clusters getClusters() {
        return clusters;
    }

    protected java.lang.String getDataDirectoryPath() {
        return org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDataDirectoryPath(getCommandParameters());
    }

    protected org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType getCommandPreconfigureType() {
        java.lang.String preconfigureServices = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getCommandParameterValue(getCommandParameters(), org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.PRECONFIGURE_SERVICES);
        org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType type = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(preconfigureServices)) {
            try {
                type = org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.valueOf(preconfigureServices.toUpperCase());
            } catch (java.lang.Throwable t) {
                org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.LOG.warn("Invalid preconfigure_services value, assuming DEFAULT: {}", preconfigureServices);
                type = org.apache.ambari.server.serveraction.kerberos.PreconfigureServiceType.DEFAULT;
            }
        }
        return type;
    }

    protected org.apache.ambari.server.agent.CommandReport processIdentities(java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.agent.CommandReport[] commandReport = new org.apache.ambari.server.agent.CommandReport[]{ null };
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        actionLog.writeStdOut("Processing identities...");
        org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.LOG.info("Processing identities...");
        if (commandParameters != null) {
            org.apache.ambari.server.security.credential.PrincipalKeyCredential administratorCredential = kerberosHelper.getKDCAdministratorCredentials(getClusterName());
            org.apache.ambari.server.serveraction.kerberos.KDCType kdcType = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getKDCType(commandParameters);
            java.lang.String defaultRealm = org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.getDefaultRealm(commandParameters);
            org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler handler = kerberosOperationHandlerFactory.getKerberosOperationHandler(kdcType);
            java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration = getConfigurationProperties("kerberos-env");
            try {
                handler.open(administratorCredential, defaultRealm, kerberosConfiguration);
            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                java.lang.String message = java.lang.String.format("Failed to process the identities, could not properly open the KDC operation handler: %s", e.getMessage());
                actionLog.writeStdErr(message);
                org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.LOG.error(message);
                throw new org.apache.ambari.server.AmbariException(message, e);
            }
            try {
                org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand();
                int threadCount = configuration.getKerberosServerActionThreadPoolSize();
                java.lang.String factoryName = (executionCommand == null) ? "process-identity-%d" : ("process-identity-task-" + executionCommand.getTaskId()) + "-thread-%d";
                java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponentFilter = getServiceComponentFilter();
                if ((serviceComponentFilter != null) && pruneServiceFilter()) {
                    kerberosKeytabController.adjustServiceComponentFilter(clusters.getCluster(getClusterName()), true, serviceComponentFilter);
                }
                final java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities = (serviceComponentFilter == null) ? null : kerberosKeytabController.getServiceIdentities(getClusterName(), serviceComponentFilter.keySet(), null);
                threadPools.parallelOperation(factoryName, threadCount, "identities", kerberosKeytabController.getFilteredKeytabs(serviceIdentities, getHostFilter(), getIdentityFilter()).stream().flatMap(rkk -> rkk.getPrincipals().stream()).map(principal -> ((java.util.concurrent.Callable<org.apache.ambari.server.agent.CommandReport>) (() -> {
                    try {
                        return processIdentity(principal, handler, kerberosConfiguration, isRelevantIdentity(serviceIdentities, principal), requestSharedDataContext);
                    } catch (org.apache.ambari.server.AmbariException ambariException) {
                        throw new org.apache.ambari.server.AmbariRuntimeException(ambariException);
                    }
                }))).collect(java.util.stream.Collectors.toList()), cr -> {
                    boolean isFailed = java.util.Objects.isNull(cr);
                    if (!isFailed) {
                        commandReport[0] = cr;
                    }
                    return isFailed;
                });
            } catch (java.lang.Exception exception) {
                org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.LOG.error("Unable to process identities asynchronously", exception);
                return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
            } finally {
                try {
                    handler.close();
                } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException e) {
                }
            }
        }
        actionLog.writeStdOut("Processing identities completed.");
        org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.LOG.info("Processing identities completed.");
        return commandReport[0] == null ? createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr()) : commandReport[0];
    }

    protected boolean pruneServiceFilter() {
        return true;
    }

    private boolean isRelevantIdentity(java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal principal) {
        if (serviceIdentities != null) {
            boolean hasValidIdentity = false;
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor serviceIdentity : serviceIdentities) {
                if (principal.getPrincipal().equals(serviceIdentity.getPrincipalDescriptor().getName()) && org.apache.commons.lang.StringUtils.isBlank(serviceIdentity.getReference())) {
                    hasValidIdentity = true;
                    break;
                }
            }
            return hasValidIdentity;
        }
        return true;
    }

    protected abstract org.apache.ambari.server.agent.CommandReport processIdentity(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedPrincipal, org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler operationHandler, java.util.Map<java.lang.String, java.lang.String> kerberosConfiguration, boolean includedInFilter, java.util.Map<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException;

    protected void deleteDataDirectory(java.lang.String dataDirectoryPath) {
        if ((dataDirectoryPath != null) && dataDirectoryPath.contains("/" + org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY_PREFIX)) {
            java.io.File dataDirectory = new java.io.File(dataDirectoryPath);
            java.io.File dataDirectoryParent = dataDirectory.getParentFile();
            if ((((dataDirectoryParent != null) && dataDirectory.isDirectory()) && dataDirectoryParent.isDirectory()) && dataDirectoryParent.canWrite()) {
                try {
                    org.apache.commons.io.FileUtils.deleteDirectory(dataDirectory);
                } catch (java.io.IOException e) {
                    java.lang.String message = java.lang.String.format("The data directory (%s) was not deleted due to an error condition - {%s}", dataDirectory.getAbsolutePath(), e.getMessage());
                    org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.LOG.warn(message, e);
                }
            }
        }
    }

    protected java.util.Set<java.lang.String> getHostFilter() {
        java.lang.String serializedValue = getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.HOST_FILTER);
        if (serializedValue != null) {
            java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Set<java.lang.String>>() {}.getType();
            return org.apache.ambari.server.utils.StageUtils.getGson().fromJson(serializedValue, type);
        } else {
            return null;
        }
    }

    protected boolean hasHostFilters() {
        java.util.Set<java.lang.String> hostFilers = getHostFilter();
        return (hostFilers != null) && (hostFilers.size() > 0);
    }

    protected java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> getServiceComponentFilter() {
        java.lang.String serializedValue = getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.SERVICE_COMPONENT_FILTER);
        if (serializedValue != null) {
            java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>>>() {}.getType();
            return org.apache.ambari.server.utils.StageUtils.getGson().fromJson(serializedValue, type);
        } else {
            return null;
        }
    }

    protected java.util.Collection<java.lang.String> getIdentityFilter() {
        java.lang.String serializedValue = getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.IDENTITY_FILTER);
        if (serializedValue != null) {
            java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Collection<java.lang.String>>() {}.getType();
            return org.apache.ambari.server.utils.StageUtils.getGson().fromJson(serializedValue, type);
        } else {
            return null;
        }
    }

    protected java.lang.Long ambariServerHostID() {
        java.lang.String ambariServerHostName = org.apache.ambari.server.utils.StageUtils.getHostName();
        org.apache.ambari.server.orm.entities.HostEntity ambariServerHostEntity = hostDAO.findByName(ambariServerHostName);
        return ambariServerHostEntity == null ? null : ambariServerHostEntity.getHostId();
    }

    protected java.util.Map<java.lang.String, java.lang.String> getConfigurationProperties(java.lang.String configType) throws org.apache.ambari.server.AmbariException {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configType)) {
            org.apache.ambari.server.state.Cluster cluster = getCluster();
            org.apache.ambari.server.state.Config config = (cluster == null) ? null : cluster.getDesiredConfigByType(configType);
            java.util.Map<java.lang.String, java.lang.String> properties = (config == null) ? null : config.getProperties();
            if (properties == null) {
                org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.LOG.warn("The '{}' configuration data is not available:" + (("\n\tcluster: {}" + "\n\tconfig: {}") + "\n\tproperties: null"), configType, cluster == null ? "null" : "not null", config == null ? "null" : "not null");
            }
            return properties;
        } else {
            return null;
        }
    }

    public static class KerberosCommandParameters {
        private java.util.Map<java.lang.String, java.lang.String> params;

        public KerberosCommandParameters(org.apache.ambari.server.agent.ExecutionCommand ec) {
            params = ec.getCommandParams();
        }

        public KerberosCommandParameters(org.apache.ambari.server.serveraction.AbstractServerAction serverAction) {
            this(serverAction.getExecutionCommand());
        }

        public java.util.Set<java.lang.String> getHostFilter() {
            java.lang.String serializedValue = getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.HOST_FILTER);
            if (serializedValue != null) {
                java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Set<java.lang.String>>() {}.getType();
                return org.apache.ambari.server.utils.StageUtils.getGson().fromJson(serializedValue, type);
            } else {
                return null;
            }
        }

        public boolean hasHostFilters() {
            java.util.Set<java.lang.String> hostFilers = getHostFilter();
            return (hostFilers != null) && (hostFilers.size() > 0);
        }

        public java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> getServiceComponentFilter() {
            java.lang.String serializedValue = getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.SERVICE_COMPONENT_FILTER);
            if (serializedValue != null) {
                java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>>>() {}.getType();
                return org.apache.ambari.server.utils.StageUtils.getGson().fromJson(serializedValue, type);
            } else {
                return null;
            }
        }

        public java.util.Collection<java.lang.String> getIdentityFilter() {
            java.lang.String serializedValue = getCommandParameterValue(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.IDENTITY_FILTER);
            if (serializedValue != null) {
                java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Collection<java.lang.String>>() {}.getType();
                return org.apache.ambari.server.utils.StageUtils.getGson().fromJson(serializedValue, type);
            } else {
                return null;
            }
        }

        public java.lang.String getCommandParameterValue(java.lang.String propertyName) {
            java.util.Map<java.lang.String, java.lang.String> commandParameters = params;
            return commandParameters == null ? null : commandParameters.get(propertyName);
        }
    }

    public enum OperationType {

        RECREATE_ALL,
        CREATE_MISSING,
        DEFAULT;}
}