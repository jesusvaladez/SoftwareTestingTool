package org.apache.ambari.server.state;
public interface ServiceComponentHost {
    long getClusterId();

    java.lang.String getClusterName();

    java.lang.String getServiceName();

    boolean isClientComponent();

    java.lang.String getServiceComponentName();

    java.lang.String getHostName();

    java.lang.String getPublicHostName();

    org.apache.ambari.server.state.Host getHost();

    boolean isRecoveryEnabled();

    void handleEvent(org.apache.ambari.server.state.ServiceComponentHostEvent event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException;

    org.apache.ambari.server.state.State getDesiredState();

    void setDesiredState(org.apache.ambari.server.state.State state);

    org.apache.ambari.server.state.State getState();

    void setState(org.apache.ambari.server.state.State state);

    java.lang.String getVersion();

    void setVersion(java.lang.String version) throws org.apache.ambari.server.AmbariException;

    void setUpgradeState(org.apache.ambari.server.state.UpgradeState upgradeState);

    org.apache.ambari.server.state.UpgradeState getUpgradeState();

    org.apache.ambari.server.state.HostComponentAdminState getComponentAdminState();

    void setComponentAdminState(org.apache.ambari.server.state.HostComponentAdminState attribute);

    org.apache.ambari.server.controller.ServiceComponentHostResponse convertToResponse(java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs);

    org.apache.ambari.server.controller.ServiceComponentHostResponse convertToResponseStatusOnly(java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs, boolean collectStaleConfigsStatus);

    void debugDump(java.lang.StringBuilder sb);

    boolean canBeRemoved();

    void delete(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData);

    java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> getActualConfigs();

    org.apache.ambari.server.state.HostState getHostState();

    void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState state);

    org.apache.ambari.server.state.MaintenanceState getMaintenanceState();

    void setProcesses(java.util.List<java.util.Map<java.lang.String, java.lang.String>> procs);

    java.util.List<java.util.Map<java.lang.String, java.lang.String>> getProcesses();

    boolean isRestartRequired();

    boolean isRestartRequired(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity);

    void setRestartRequired(boolean restartRequired);

    boolean setRestartRequiredWithoutEventPublishing(boolean restartRequired);

    org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity getDesiredStateEntity();

    org.apache.ambari.server.state.ServiceComponent getServiceComponent();

    org.apache.ambari.server.orm.entities.HostVersionEntity recalculateHostVersionState() throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.StackId getDesiredStackId();
}