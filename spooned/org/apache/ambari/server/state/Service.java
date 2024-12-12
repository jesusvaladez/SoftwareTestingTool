package org.apache.ambari.server.state;
public interface Service {
    java.lang.String getName();

    java.lang.String getDisplayName();

    long getClusterId();

    org.apache.ambari.server.state.Cluster getCluster();

    org.apache.ambari.server.state.ServiceComponent getServiceComponent(java.lang.String componentName) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> getServiceComponents();

    java.util.Set<java.lang.String> getServiceHosts();

    void addServiceComponents(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components) throws org.apache.ambari.server.AmbariException;

    void addServiceComponent(org.apache.ambari.server.state.ServiceComponent component) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.State getDesiredState();

    void setDesiredState(org.apache.ambari.server.state.State state);

    org.apache.ambari.server.state.StackId getDesiredStackId();

    org.apache.ambari.server.controller.ServiceResponse convertToResponse();

    void debugDump(java.lang.StringBuilder sb);

    org.apache.ambari.server.state.ServiceComponent addServiceComponent(java.lang.String serviceComponentName) throws org.apache.ambari.server.AmbariException;

    boolean canBeRemoved();

    void deleteAllComponents(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData);

    void deleteServiceComponent(java.lang.String componentName, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException;

    boolean isClientOnlyService();

    void delete(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData);

    void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState state);

    org.apache.ambari.server.state.MaintenanceState getMaintenanceState();

    boolean isKerberosEnabled();

    boolean isKerberosEnabled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations);

    void updateServiceInfo() throws org.apache.ambari.server.AmbariException;

    boolean isCredentialStoreSupported();

    boolean isCredentialStoreRequired();

    boolean isCredentialStoreEnabled();

    void setCredentialStoreEnabled(boolean credentialStoreEnabled);

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity getDesiredRepositoryVersion();

    void setDesiredRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion);

    org.apache.ambari.server.state.RepositoryVersionState getRepositoryState();

    enum Type {

        HDFS,
        GLUSTERFS,
        MAPREDUCE,
        HBASE,
        HIVE,
        OOZIE,
        WEBHCAT,
        SQOOP,
        GANGLIA,
        ZOOKEEPER,
        PIG,
        FLUME,
        YARN,
        MAPREDUCE2,
        AMBARI_METRICS,
        KERBEROS;}
}