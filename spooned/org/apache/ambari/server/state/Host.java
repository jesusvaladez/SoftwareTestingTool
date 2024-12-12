package org.apache.ambari.server.state;
import javax.annotation.Nullable;
public interface Host extends java.lang.Comparable {
    org.apache.ambari.server.orm.entities.HostEntity getHostEntity();

    java.lang.String getHostName();

    java.lang.Long getHostId();

    java.lang.Integer getCurrentPingPort();

    void setCurrentPingPort(java.lang.Integer currentPingPort);

    void setPublicHostName(java.lang.String hostName);

    java.lang.String getPublicHostName();

    java.lang.String getIPv4();

    void setIPv4(java.lang.String ip);

    java.lang.String getIPv6();

    void setIPv6(java.lang.String ip);

    int getCpuCount();

    void setCpuCount(int cpuCount);

    int getPhCpuCount();

    void setPhCpuCount(int phCpuCount);

    long getTotalMemBytes();

    void setTotalMemBytes(long totalMemBytes);

    long getAvailableMemBytes();

    void setAvailableMemBytes(long availableMemBytes);

    java.lang.String getOsArch();

    void setOsArch(java.lang.String osArch);

    java.lang.String getOsInfo();

    void setOsInfo(java.lang.String osInfo);

    java.lang.String getOsType();

    java.lang.String getOsFamily();

    java.lang.String getOsFamily(java.util.Map<java.lang.String, java.lang.String> hostAttributes);

    java.lang.String getOSFamilyFromHostAttributes(java.util.Map<java.lang.String, java.lang.String> hostAttributes);

    void setOsType(java.lang.String osType);

    java.util.List<org.apache.ambari.server.agent.DiskInfo> getDisksInfo();

    void setDisksInfo(java.util.List<org.apache.ambari.server.agent.DiskInfo> disksInfo);

    org.apache.ambari.server.state.HostHealthStatus getHealthStatus();

    org.apache.ambari.server.state.HostHealthStatus getHealthStatus(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity);

    org.apache.ambari.server.agent.RecoveryReport getRecoveryReport();

    void setRecoveryReport(org.apache.ambari.server.agent.RecoveryReport recoveryReport);

    void setHealthStatus(org.apache.ambari.server.state.HostHealthStatus healthStatus);

    java.util.Map<java.lang.String, java.lang.String> getHostAttributes();

    java.util.Map<java.lang.String, java.lang.String> getHostAttributes(org.apache.ambari.server.orm.entities.HostEntity hostEntity);

    void setHostAttributes(java.util.Map<java.lang.String, java.lang.String> hostAttributes);

    java.lang.String getRackInfo();

    void setRackInfo(java.lang.String rackInfo);

    long getLastRegistrationTime();

    void setLastRegistrationTime(long lastRegistrationTime);

    long getLastAgentStartTime();

    void setLastAgentStartTime(long lastAgentStartTime);

    long getLastHeartbeatTime();

    void setLastHeartbeatTime(long lastHeartbeatTime);

    void setLastAgentEnv(org.apache.ambari.server.agent.AgentEnv env);

    org.apache.ambari.server.agent.AgentEnv getLastAgentEnv();

    org.apache.ambari.server.state.AgentVersion getAgentVersion();

    org.apache.ambari.server.state.AgentVersion getAgentVersion(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity);

    void setAgentVersion(org.apache.ambari.server.state.AgentVersion agentVersion);

    org.apache.ambari.server.state.HostState getState();

    void setState(org.apache.ambari.server.state.HostState state);

    void setStateMachineState(org.apache.ambari.server.state.HostState state);

    java.lang.String getPrefix();

    void setPrefix(java.lang.String prefix);

    void handleEvent(org.apache.ambari.server.state.HostEvent event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException;

    long getTimeInState();

    void setTimeInState(long timeInState);

    java.lang.String getStatus();

    void setStatus(java.lang.String status);

    org.apache.ambari.server.controller.HostResponse convertToResponse();

    void importHostInfo(org.apache.ambari.server.agent.HostInfo hostInfo);

    boolean addDesiredConfig(long clusterId, boolean selected, java.lang.String user, org.apache.ambari.server.state.Config config);

    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> getDesiredConfigs(long clusterId);

    java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> getDesiredHostConfigs(org.apache.ambari.server.state.Cluster cluster, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterDesiredConfigs) throws org.apache.ambari.server.AmbariException;

    void setMaintenanceState(long clusterId, org.apache.ambari.server.state.MaintenanceState state);

    org.apache.ambari.server.state.MaintenanceState getMaintenanceState(long clusterId);

    java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> getAllHostVersions();

    boolean hasComponentsAdvertisingVersions(org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException;

    void calculateHostStatus(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException;

    boolean isRepositoryVersionCorrect(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException;
}