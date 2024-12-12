package org.apache.ambari.server.state;
public interface ServiceComponent {
    java.lang.String getName();

    boolean isRecoveryEnabled();

    void setRecoveryEnabled(boolean recoveryEnabled);

    java.lang.String getServiceName();

    java.lang.String getDisplayName();

    long getClusterId();

    java.lang.String getClusterName();

    org.apache.ambari.server.state.State getDesiredState();

    void setDesiredState(org.apache.ambari.server.state.State state);

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity getDesiredRepositoryVersion();

    org.apache.ambari.server.state.StackId getDesiredStackId();

    java.lang.String getDesiredVersion();

    void setDesiredRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity);

    void updateComponentInfo() throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> getServiceComponentHosts();

    java.util.Set<java.lang.String> getServiceComponentsHosts();

    org.apache.ambari.server.state.ServiceComponentHost getServiceComponentHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException;

    void addServiceComponentHosts(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents) throws org.apache.ambari.server.AmbariException;

    void addServiceComponentHost(org.apache.ambari.server.state.ServiceComponentHost hostComponent) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.ServiceComponentResponse convertToResponse();

    void debugDump(java.lang.StringBuilder sb);

    boolean isClientComponent();

    boolean isMasterComponent();

    boolean isVersionAdvertised();

    boolean canBeRemoved();

    void deleteAllServiceComponentHosts(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException;

    void deleteServiceComponentHosts(java.lang.String hostname, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.ServiceComponentHost addServiceComponentHost(java.lang.String hostName) throws org.apache.ambari.server.AmbariException;

    void delete(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData);

    void updateRepositoryState(java.lang.String reportedVersion) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.RepositoryVersionState getRepositoryState();
}