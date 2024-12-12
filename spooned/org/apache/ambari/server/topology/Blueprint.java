package org.apache.ambari.server.topology;
public interface Blueprint {
    java.lang.String getName();

    java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> getHostGroups();

    org.apache.ambari.server.topology.HostGroup getHostGroup(java.lang.String name);

    org.apache.ambari.server.topology.Configuration getConfiguration();

    org.apache.ambari.server.topology.Setting getSetting();

    java.util.Collection<java.lang.String> getServices();

    java.util.Collection<org.apache.ambari.server.state.ServiceInfo> getServiceInfos();

    java.util.Collection<java.lang.String> getComponents(java.lang.String service);

    java.lang.String getRecoveryEnabled(java.lang.String serviceName, java.lang.String componentName);

    java.lang.String getCredentialStoreEnabled(java.lang.String serviceName);

    boolean shouldSkipFailure();

    org.apache.ambari.server.controller.internal.Stack getStack();

    java.util.Collection<org.apache.ambari.server.topology.HostGroup> getHostGroupsForService(java.lang.String service);

    java.util.Collection<org.apache.ambari.server.topology.HostGroup> getHostGroupsForComponent(java.lang.String component);

    org.apache.ambari.server.topology.SecurityConfiguration getSecurity();

    void validateTopology() throws org.apache.ambari.server.topology.InvalidTopologyException;

    void validateRequiredProperties() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException;

    boolean isValidConfigType(java.lang.String configType);

    org.apache.ambari.server.orm.entities.BlueprintEntity toEntity();

    java.util.List<org.apache.ambari.server.topology.RepositorySetting> getRepositorySettings();
}