package org.apache.ambari.server.controller;
import javax.annotation.Nullable;
public interface KerberosHelper {
    java.lang.String DIRECTIVE_MANAGE_KERBEROS_IDENTITIES = "manage_kerberos_identities";

    java.lang.String DIRECTIVE_REGENERATE_KEYTABS = "regenerate_keytabs";

    java.lang.String DIRECTIVE_HOSTS = "regenerate_hosts";

    java.lang.String DIRECTIVE_COMPONENTS = "regenerate_components";

    java.lang.String DIRECTIVE_IGNORE_CONFIGS = "ignore_config_updates";

    java.lang.String DIRECTIVE_CONFIG_UPDATE_POLICY = "config_update_policy";

    java.lang.String DIRECTIVE_FORCE_TOGGLE_KERBEROS = "force_toggle_kerberos";

    java.lang.String SECURITY_ENABLED_CONFIG_TYPE = "cluster-env";

    java.lang.String SECURITY_ENABLED_PROPERTY_NAME = "security_enabled";

    java.lang.String KDC_ADMINISTRATOR_CREDENTIAL_ALIAS = "kdc.admin.credential";

    java.lang.String AMBARI_SERVER_HOST_NAME = "ambari_server";

    java.lang.String KERBEROS_ENV = "kerberos-env";

    java.lang.String AMBARI_SERVER_KERBEROS_IDENTITY_NAME = "ambari-server";

    java.lang.String CREATE_AMBARI_PRINCIPAL = "create_ambari_principal";

    java.lang.String MANAGE_IDENTITIES = "manage_identities";

    java.lang.String DEFAULT_REALM = "realm";

    java.lang.String KDC_TYPE = "kdc_type";

    java.lang.String MANAGE_AUTH_TO_LOCAL_RULES = "manage_auth_to_local";

    java.lang.String INCLUDE_ALL_COMPONENTS_IN_AUTH_TO_LOCAL_RULES = "include_all_components_in_auth_to_local_rules";

    java.lang.String CASE_INSENSITIVE_USERNAME_RULES = "case_insensitive_username_rules";

    java.lang.String PRECONFIGURE_SERVICES = "preconfigure_services";

    java.lang.String ALLOW_RETRY = "allow_retry_on_failure";

    java.lang.String CLUSTER_HOST_INFO = "clusterHostInfo";

    org.apache.ambari.server.controller.internal.RequestStageContainer toggleKerberos(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.SecurityType securityType, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    org.apache.ambari.server.controller.internal.RequestStageContainer executeCustomOperations(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    org.apache.ambari.server.controller.internal.RequestStageContainer ensureIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, java.util.Set<java.lang.String> hostsToForceKerberosOperations, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    org.apache.ambari.server.controller.internal.RequestStageContainer deleteIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    void deleteIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components, java.util.Set<java.lang.String> identities) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException;

    void configureServices(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceFilter) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getServiceConfigurationUpdates(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceFilter, java.util.Set<java.lang.String> previouslyExistingServices, boolean kerberosEnabled, boolean applyStackAdvisorUpdates) throws org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> applyStackAdvisorUpdates(org.apache.ambari.server.state.Cluster cluster, java.util.Set<java.lang.String> services, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToRemove, boolean kerberosEnabled) throws org.apache.ambari.server.AmbariException;

    boolean ensureHeadlessIdentities(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Set<java.lang.String> services) throws org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.internal.RequestStageContainer createTestIdentity(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> commandParamsStage, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException, org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.internal.RequestStageContainer deleteTestIdentity(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> commandParamsStage, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) throws org.apache.ambari.server.serveraction.kerberos.KerberosOperationException, org.apache.ambari.server.AmbariException;

    void validateKDCCredentials(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException, org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException, org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException;

    void setAuthToLocalRules(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.lang.String realm, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> installedServices, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, boolean includePreconfigureData) throws org.apache.ambari.server.AmbariException;

    java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHostsToProcess(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Collection<java.lang.String> hostFilter) throws org.apache.ambari.server.AmbariException;

    java.util.Set<java.lang.String> getHostsWithValidKerberosClient(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.state.Cluster cluster, boolean includePreconfigureData) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.state.Cluster cluster, boolean includePreconfigureData, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType kerberosDescriptorType, org.apache.ambari.server.state.Cluster cluster, boolean evaluateWhenClauses, java.util.Collection<java.lang.String> additionalServices, boolean includePreconfigureData, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType kerberosDescriptorType, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, boolean includePreconfigureData, @javax.annotation.Nullable
    org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mergeConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> updates, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements, java.util.Set<java.lang.String> configurationTypeFilter) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> processPreconfiguredServiceConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> replacements, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) throws org.apache.ambari.server.AmbariException;

    int addIdentities(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter kerberosIdentityDataFileWriter, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities, java.util.Collection<java.lang.String> identityFilter, java.lang.String hostname, java.lang.Long hostId, java.lang.String serviceName, java.lang.String componentName, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations, java.util.Map<java.lang.String, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> resolvedKeytabs, java.lang.String realm) throws java.io.IOException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, boolean includePreconfigureData, boolean calculateClusterHostInfo, java.util.Map<java.lang.String, java.lang.String> componentHosts, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor, boolean includePreconfigureData, boolean calculateClusterHostInfo, java.util.Map<java.lang.String, java.lang.String> componentHosts, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> calculateConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, boolean includePreconfigureData, boolean calculateClusterHostInfo, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException;

    boolean isClusterKerberosEnabled(org.apache.ambari.server.state.Cluster cluster);

    boolean shouldExecuteCustomOperations(org.apache.ambari.server.state.SecurityType requestSecurityType, java.util.Map<java.lang.String, java.lang.String> requestProperties);

    java.lang.Boolean getManageIdentitiesDirective(java.util.Map<java.lang.String, java.lang.String> requestProperties);

    boolean getForceToggleKerberosDirective(java.util.Map<java.lang.String, java.lang.String> requestProperties);

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getIdentityConfigurations(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors);

    java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> getActiveIdentities(java.lang.String clusterName, java.lang.String hostName, java.lang.String serviceName, java.lang.String componentName, boolean replaceHostNames, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> hostConfigurations, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException;

    java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getAmbariServerIdentities(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) throws org.apache.ambari.server.AmbariException;

    boolean createAmbariIdentities(java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties);

    org.apache.ambari.server.security.credential.PrincipalKeyCredential getKDCAdministratorCredentials(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    void createResolvedKeytab(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab, java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> keytabList);

    void removeStaleKeytabs(java.util.Collection<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> expectedKeytabs);

    java.io.File createTemporaryDirectory() throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> translateConfigurationSpecifications(java.util.Collection<java.lang.String> configurationSpecifications);

    org.apache.ambari.server.controller.KerberosDetails getKerberosDetails(org.apache.ambari.server.state.Cluster cluster, java.lang.Boolean manageIdentities) throws org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptorUpdates(org.apache.ambari.server.state.Cluster cluster);

    enum KerberosDescriptorType {

        STACK,
        USER,
        COMPOSITE;}

    interface Command<T, A> {
        T invoke(A arg) throws org.apache.ambari.server.AmbariException;
    }
}