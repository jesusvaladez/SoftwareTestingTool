package org.apache.ambari.server.state;
import javax.annotation.Nullable;
public interface Cluster {
    long getClusterId();

    java.lang.String getClusterName();

    void setClusterName(java.lang.String clusterName);

    java.lang.Long getResourceId();

    void addService(org.apache.ambari.server.state.Service service);

    org.apache.ambari.server.state.Service getService(java.lang.String serviceName) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.Service getServiceByComponentName(java.lang.String componentName) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> getServices();

    java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts(java.lang.String hostname);

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getServiceComponentHostMap(java.util.Set<java.lang.String> hostNames, java.util.Set<java.lang.String> serviceNames);

    java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts(java.lang.String serviceName, java.lang.String componentName);

    java.util.List<org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts();

    java.util.Collection<org.apache.ambari.server.state.Host> getHosts();

    default java.util.Set<java.lang.String> getHostNames() {
        return getHosts().stream().map(org.apache.ambari.server.state.Host::getHostName).collect(java.util.stream.Collectors.toSet());
    }

    java.util.Set<java.lang.String> getHosts(java.lang.String serviceName, java.lang.String componentName);

    org.apache.ambari.server.state.Host getHost(java.lang.String hostName);

    org.apache.ambari.server.state.Host getHost(java.lang.Long hostId);

    void addServiceComponentHosts(java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts) throws org.apache.ambari.server.AmbariException;

    void removeServiceComponentHost(org.apache.ambari.server.state.ServiceComponentHost svcCompHost) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.StackId getDesiredStackVersion();

    void setDesiredStackVersion(org.apache.ambari.server.state.StackId stackVersion) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.StackId getCurrentStackVersion();

    void setCurrentStackVersion(org.apache.ambari.server.state.StackId stackVersion) throws org.apache.ambari.server.AmbariException;

    java.util.List<org.apache.ambari.server.state.Host> transitionHostsToInstalling(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity, org.apache.ambari.server.state.repository.VersionDefinitionXml versionDefinitionXml, boolean forceInstalled) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.State getProvisioningState();

    void setProvisioningState(org.apache.ambari.server.state.State provisioningState);

    org.apache.ambari.server.state.SecurityType getSecurityType();

    void setSecurityType(org.apache.ambari.server.state.SecurityType securityType);

    java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> getConfigsByType(java.lang.String configType);

    java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getConfigPropertiesTypes(java.lang.String configType);

    java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getConfigPropertiesTypes(java.lang.String configType, org.apache.ambari.server.state.StackId stackId);

    org.apache.ambari.server.state.Config getConfig(java.lang.String configType, java.lang.String versionTag);

    java.util.List<org.apache.ambari.server.state.Config> getLatestConfigsWithTypes(java.util.Collection<java.lang.String> types);

    org.apache.ambari.server.state.Config getConfigByVersion(java.lang.String configType, java.lang.Long configVersion);

    void addConfig(org.apache.ambari.server.state.Config config);

    java.util.Collection<org.apache.ambari.server.state.Config> getAllConfigs();

    org.apache.ambari.server.controller.ServiceConfigVersionResponse addDesiredConfig(java.lang.String user, java.util.Set<org.apache.ambari.server.state.Config> configs) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.ServiceConfigVersionResponse addDesiredConfig(java.lang.String user, java.util.Set<org.apache.ambari.server.state.Config> configs, java.lang.String serviceConfigVersionNote) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.ServiceConfigVersionResponse createServiceConfigVersion(java.lang.String serviceName, java.lang.String user, java.lang.String note, org.apache.ambari.server.state.configgroup.ConfigGroup configGroup) throws org.apache.ambari.server.AmbariException;

    java.lang.String getServiceForConfigTypes(java.util.Collection<java.lang.String> configTypes);

    org.apache.ambari.server.controller.ServiceConfigVersionResponse setServiceConfigVersion(java.lang.String serviceName, java.lang.Long version, java.lang.String user, java.lang.String note) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> getActiveServiceConfigVersions();

    java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getActiveServiceConfigVersionResponse(java.lang.String serviceName);

    java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> getServiceConfigVersions();

    org.apache.ambari.server.state.Config getDesiredConfigByType(java.lang.String configType);

    org.apache.ambari.server.state.Config getDesiredConfigByType(java.lang.String configType, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs);

    boolean isConfigTypeExists(java.lang.String configType);

    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> getDesiredConfigs();

    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> getDesiredConfigs(boolean cachedConfigEntities);

    org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity();

    java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.state.DesiredConfig>> getAllDesiredConfigVersions();

    org.apache.ambari.server.controller.ClusterResponse convertToResponse() throws org.apache.ambari.server.AmbariException;

    void refresh();

    void debugDump(java.lang.StringBuilder sb);

    void deleteAllServices() throws org.apache.ambari.server.AmbariException;

    void deleteAllClusterConfigs();

    void deleteService(java.lang.String serviceName, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException;

    boolean canBeRemoved();

    void delete() throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.Service addService(java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig>> getHostsDesiredConfigs(java.util.Collection<java.lang.Long> hostIds);

    java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig>> getAllHostsDesiredConfigs();

    void addConfigGroup(org.apache.ambari.server.state.configgroup.ConfigGroup configGroup) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> getConfigGroups();

    void deleteConfigGroup(java.lang.Long id) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> getConfigGroupsByHostname(java.lang.String hostname) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.configgroup.ConfigGroup getConfigGroupsById(java.lang.Long configId);

    java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> getConfigGroupsByServiceName(java.lang.String serviceName);

    void addRequestExecution(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> getAllRequestExecutions();

    void deleteRequestExecution(java.lang.Long id) throws org.apache.ambari.server.AmbariException;

    java.lang.Long getNextConfigVersion(java.lang.String type);

    java.util.Map<org.apache.ambari.server.state.ServiceComponentHostEvent, java.lang.String> processServiceComponentHostEvents(com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHostEvent> eventMap);

    boolean checkPermission(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, boolean readOnly);

    void addSessionAttributes(java.util.Map<java.lang.String, java.lang.Object> attributes);

    void setSessionAttribute(java.lang.String key, java.lang.Object value);

    void removeSessionAttribute(java.lang.String key);

    java.util.Map<java.lang.String, java.lang.Object> getSessionAttributes();

    void applyLatestConfigurations(org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName);

    void removeConfigurations(org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName);

    boolean isBluePrintDeployed();

    org.apache.ambari.server.orm.entities.UpgradeEntity getUpgradeInProgress();

    void setUpgradeEntity(org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity) throws org.apache.ambari.server.AmbariException;

    boolean isUpgradeSuspended();

    java.lang.String getServiceByConfigType(java.lang.String configType);

    java.lang.String getClusterProperty(java.lang.String propertyName, java.lang.String defaultValue);

    int getClusterSize();

    org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder();

    void addSuspendedUpgradeParameters(java.util.Map<java.lang.String, java.lang.String> commandParams, java.util.Map<java.lang.String, java.lang.String> roleParams);

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getComponentVersionMap();

    org.apache.ambari.spi.ClusterInformation buildClusterInformation();
}