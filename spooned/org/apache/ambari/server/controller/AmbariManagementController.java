package org.apache.ambari.server.controller;
import javax.annotation.Nullable;
public interface AmbariManagementController {
    java.lang.String getAmbariServerURI(java.lang.String path);

    void createCluster(org.apache.ambari.server.controller.ClusterRequest request) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    void createHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    void createHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, boolean isBlueprintProvisioned) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    org.apache.ambari.server.controller.ConfigurationResponse createConfiguration(org.apache.ambari.server.controller.ConfigurationRequest request, boolean refreshCluster) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    org.apache.ambari.server.controller.ConfigurationResponse createConfiguration(org.apache.ambari.server.controller.ConfigurationRequest request) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    org.apache.ambari.server.state.Config createConfig(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, java.lang.String type, java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String versionTag, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes, boolean refreshCluster);

    org.apache.ambari.server.state.Config createConfig(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, java.lang.String type, java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String versionTag, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes);

    void createGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException;

    void createMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.MpackResponse registerMpack(org.apache.ambari.server.controller.MpackRequest request) throws java.io.IOException, org.apache.ambari.server.security.authorization.AuthorizationException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException;

    java.util.Set<org.apache.ambari.server.controller.ClusterResponse> getClusters(java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> getHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> getHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, boolean statusOnly) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> getConfigurations(java.util.Set<org.apache.ambari.server.controller.ConfigurationRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getServiceConfigVersions(java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.GroupResponse> getGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.MemberResponse> getMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.RequestStatusResponse updateClusters(java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    org.apache.ambari.server.controller.RequestStatusResponse updateClusters(java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean fireAgentUpdates, boolean refreshCluster) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    void updateGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException;

    void updateMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException;

    void deleteCluster(org.apache.ambari.server.controller.ClusterRequest request) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    void deleteGroups(java.util.Set<org.apache.ambari.server.controller.GroupRequest> requests) throws org.apache.ambari.server.AmbariException;

    void deleteMembers(java.util.Set<org.apache.ambari.server.controller.MemberRequest> requests) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.RequestStatusResponse createAction(org.apache.ambari.server.controller.ExecuteActionRequest actionRequest, java.util.Map<java.lang.String, java.lang.String> requestProperties) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.StackResponse> getStacks(java.util.Set<org.apache.ambari.server.controller.StackRequest> requests) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.RequestStatusResponse updateStacks() throws org.apache.ambari.server.AmbariException;

    void createExtensionLink(org.apache.ambari.server.controller.ExtensionLinkRequest request) throws org.apache.ambari.server.AmbariException;

    void updateExtensionLink(org.apache.ambari.server.controller.ExtensionLinkRequest request) throws org.apache.ambari.server.AmbariException;

    void updateExtensionLink(org.apache.ambari.server.orm.entities.ExtensionLinkEntity oldLinkEntity, org.apache.ambari.server.controller.ExtensionLinkRequest newLinkRequest) throws org.apache.ambari.server.AmbariException;

    void deleteExtensionLink(org.apache.ambari.server.controller.ExtensionLinkRequest request) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.ExtensionResponse> getExtensions(java.util.Set<org.apache.ambari.server.controller.ExtensionRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> getExtensionVersions(java.util.Set<org.apache.ambari.server.controller.ExtensionVersionRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> getStackVersions(java.util.Set<org.apache.ambari.server.controller.StackVersionRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> getRepositories(java.util.Set<org.apache.ambari.server.controller.RepositoryRequest> requests) throws org.apache.ambari.server.AmbariException;

    void verifyRepositories(java.util.Set<org.apache.ambari.server.controller.RepositoryRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> getStackServices(java.util.Set<org.apache.ambari.server.controller.StackServiceRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> getStackConfigurations(java.util.Set<org.apache.ambari.server.controller.StackConfigurationRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> getStackComponents(java.util.Set<org.apache.ambari.server.controller.StackServiceComponentRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> getOperatingSystems(java.util.Set<org.apache.ambari.server.controller.OperatingSystemRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> getRootServices(java.util.Set<org.apache.ambari.server.controller.RootServiceRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> getRootServiceComponents(java.util.Set<org.apache.ambari.server.controller.RootServiceComponentRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.lang.String findServiceName(org.apache.ambari.server.state.Cluster cluster, java.lang.String componentName) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.Clusters getClusters();

    org.apache.ambari.server.state.ConfigHelper getConfigHelper();

    org.apache.ambari.server.api.services.AmbariMetaInfo getAmbariMetaInfo();

    org.apache.ambari.server.state.ServiceComponentFactory getServiceComponentFactory();

    org.apache.ambari.server.controller.AbstractRootServiceResponseFactory getRootServiceResponseFactory();

    org.apache.ambari.server.state.configgroup.ConfigGroupFactory getConfigGroupFactory();

    org.apache.ambari.server.stageplanner.RoleGraphFactory getRoleGraphFactory();

    org.apache.ambari.server.actionmanager.ActionManager getActionManager();

    java.lang.String getAuthName();

    int getAuthId();

    org.apache.ambari.server.controller.RequestStatusResponse createAndPersistStages(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, java.util.Map<java.lang.String, java.lang.String> requestParameters, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComponents, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredHosts, boolean runSmokeTest, boolean reconfigureClients) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.internal.RequestStageContainer addStages(org.apache.ambari.server.controller.internal.RequestStageContainer requestStages, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, java.util.Map<java.lang.String, java.lang.String> requestParameters, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComponents, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredHosts, boolean runSmokeTest, boolean reconfigureClients, boolean useGeneratedConfigs) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.internal.RequestStageContainer addStages(org.apache.ambari.server.controller.internal.RequestStageContainer requestStages, org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, java.lang.String> requestProperties, java.util.Map<java.lang.String, java.lang.String> requestParameters, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComponents, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredHosts, boolean runSmokeTest, boolean reconfigureClients, boolean useGeneratedConfigs, boolean useClusterHostInfo) throws org.apache.ambari.server.AmbariException;

    java.lang.String getJdkResourceUrl();

    java.lang.String getJavaHome();

    java.lang.String getJDKName();

    java.lang.String getJCEName();

    java.lang.String getServerDB();

    java.lang.String getOjdbcUrl();

    java.lang.String getMysqljdbcUrl();

    java.util.List<java.lang.String> selectHealthyHosts(java.util.Set<java.lang.String> hostList) throws org.apache.ambari.server.AmbariException;

    java.lang.String getHealthyHost(java.util.Set<java.lang.String> hostList) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> findConfigurationTagsWithOverrides(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.lang.String> getRcaParameters();

    org.apache.ambari.server.state.scheduler.RequestExecutionFactory getRequestExecutionFactory();

    org.apache.ambari.server.scheduler.ExecutionScheduleManager getExecutionScheduleManager();

    org.apache.ambari.server.controller.ClusterResponse getClusterUpdateResults(org.apache.ambari.server.controller.ClusterRequest clusterRequest);

    @java.lang.Deprecated
    java.lang.String getJobTrackerHost(org.apache.ambari.server.state.Cluster cluster);

    org.apache.ambari.server.state.MaintenanceState getEffectiveMaintenanceState(org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder(org.apache.ambari.server.state.Cluster cluster);

    boolean checkLdapConfigured();

    org.apache.ambari.server.security.ldap.LdapSyncDto getLdapSyncInfo() throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeLdapUsersAndGroups(org.apache.ambari.server.controller.LdapSyncRequest userRequest, org.apache.ambari.server.controller.LdapSyncRequest groupRequest) throws org.apache.ambari.server.AmbariException;

    boolean isLdapSyncInProgress();

    java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> getStackLevelConfigurations(java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> getPackagesForServiceHost(org.apache.ambari.server.state.ServiceInfo serviceInfo, java.util.Map<java.lang.String, java.lang.String> hostParams, java.lang.String osFamily);

    void registerRackChange(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    void initializeWidgetsAndLayouts(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Service service) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.agent.ExecutionCommand getExecutionCommand(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost scHost, org.apache.ambari.server.RoleCommand roleCommand) throws org.apache.ambari.server.AmbariException;

    java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> getStackConfigurationDependencies(java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyRequest> requests) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider getTimelineMetricCacheProvider();

    org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory getMetricPropertyProviderFactory();

    org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider getLoggingSearchPropertyProvider();

    org.apache.ambari.server.api.services.LoggingService getLoggingService(java.lang.String clusterName);

    org.apache.ambari.server.controller.KerberosHelper getKerberosHelper();

    org.apache.ambari.server.security.encryption.CredentialStoreService getCredentialStoreService();

    org.apache.ambari.server.events.publishers.AmbariEventPublisher getAmbariEventPublisher();

    org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager getMetricsCollectorHAManager();

    org.apache.ambari.server.state.quicklinksprofile.QuickLinkVisibilityController getQuicklinkVisibilityController();

    org.apache.ambari.server.controller.ConfigGroupResponse getConfigGroupUpdateResults(org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest);

    void saveConfigGroupUpdate(org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest, org.apache.ambari.server.controller.ConfigGroupResponse configGroupResponse);

    org.apache.ambari.server.events.MetadataUpdateEvent getClusterMetadataOnConfigsUpdate(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.events.TopologyUpdateEvent getAddedComponentsTopologyEvent(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, org.apache.ambari.server.state.BlueprintProvisioningState> getBlueprintProvisioningStates(java.lang.Long clusterId, java.lang.Long hostId) throws org.apache.ambari.server.AmbariException;

    java.util.List<org.apache.ambari.server.state.Module> getModules(java.lang.Long mpackId);

    void removeMpack(org.apache.ambari.server.orm.entities.MpackEntity mpackEntity, org.apache.ambari.server.orm.entities.StackEntity stackEntity) throws java.io.IOException;

    java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> createServiceConfigVersion(java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException;

    java.util.Set<org.apache.ambari.server.controller.MpackResponse> getMpacks();

    org.apache.ambari.server.controller.MpackResponse getMpack(java.lang.Long mpackId);
}