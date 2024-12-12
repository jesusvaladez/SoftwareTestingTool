package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class HostResponse {
    private java.lang.String hostname;

    private java.lang.String clusterName;

    private java.lang.String ipv4;

    private long cpuCount;

    private long phCpuCount;

    private java.lang.String osArch;

    private java.lang.String osFamily;

    private java.lang.String osType;

    private long totalMemBytes;

    private java.util.List<org.apache.ambari.server.agent.DiskInfo> disksInfo;

    private long lastHeartbeatTime;

    private org.apache.ambari.server.agent.AgentEnv lastAgentEnv;

    private long lastRegistrationTime;

    private java.lang.String rackInfo;

    private java.util.Map<java.lang.String, java.lang.String> hostAttributes;

    private org.apache.ambari.server.state.AgentVersion agentVersion;

    private org.apache.ambari.server.state.HostHealthStatus healthStatus;

    private org.apache.ambari.server.agent.RecoveryReport recoveryReport;

    private java.lang.String recoverySummary = "DISABLED";

    private java.lang.String publicHostname;

    private org.apache.ambari.server.state.HostState hostState;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> desiredHostConfigs;

    private java.lang.String status;

    private org.apache.ambari.server.state.MaintenanceState maintenanceState;

    public HostResponse(java.lang.String hostname, java.lang.String clusterName, java.lang.String ipv4, int cpuCount, int phCpuCount, java.lang.String osArch, java.lang.String osType, long totalMemBytes, java.util.List<org.apache.ambari.server.agent.DiskInfo> disksInfo, long lastHeartbeatTime, long lastRegistrationTime, java.lang.String rackInfo, java.util.Map<java.lang.String, java.lang.String> hostAttributes, org.apache.ambari.server.state.AgentVersion agentVersion, org.apache.ambari.server.state.HostHealthStatus healthStatus, org.apache.ambari.server.state.HostState hostState, java.lang.String status) {
        this.hostname = hostname;
        this.clusterName = clusterName;
        this.ipv4 = ipv4;
        this.cpuCount = cpuCount;
        this.phCpuCount = phCpuCount;
        this.osArch = osArch;
        this.osType = osType;
        this.totalMemBytes = totalMemBytes;
        this.disksInfo = disksInfo;
        this.lastHeartbeatTime = lastHeartbeatTime;
        this.lastRegistrationTime = lastRegistrationTime;
        this.rackInfo = rackInfo;
        this.hostAttributes = hostAttributes;
        this.agentVersion = agentVersion;
        this.healthStatus = healthStatus;
        this.hostState = hostState;
        this.status = status;
    }

    public HostResponse(java.lang.String hostname) {
        this(hostname, "", "", 0, 0, "", "", 0, new java.util.ArrayList<>(), 0, 0, "", new java.util.HashMap<>(), null, null, null, null);
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_NAME_PROPERTY_ID)
    public java.lang.String getHostname() {
        return hostname;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.CLUSTER_NAME_PROPERTY_ID)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.IP_PROPERTY_ID)
    public java.lang.String getIpv4() {
        return ipv4;
    }

    public void setIpv4(java.lang.String ipv4) {
        this.ipv4 = ipv4;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.CPU_COUNT_PROPERTY_ID)
    public long getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(long cpuCount) {
        this.cpuCount = cpuCount;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.PHYSICAL_CPU_COUNT_PROPERTY_ID)
    public long getPhCpuCount() {
        return phCpuCount;
    }

    public void setPhCpuCount(long phCpuCount) {
        this.phCpuCount = phCpuCount;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.OS_ARCH_PROPERTY_ID)
    public java.lang.String getOsArch() {
        return osArch;
    }

    public void setOsArch(java.lang.String osArch) {
        this.osArch = osArch;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.OS_FAMILY_PROPERTY_ID)
    public java.lang.String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(java.lang.String osFamily) {
        this.osFamily = osFamily;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.OS_TYPE_PROPERTY_ID)
    public java.lang.String getOsType() {
        return osType;
    }

    public void setOsType(java.lang.String osType) {
        this.osType = osType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.TOTAL_MEM_PROPERTY_ID)
    public long getTotalMemBytes() {
        return totalMemBytes;
    }

    public void setTotalMemBytes(long totalMemBytes) {
        this.totalMemBytes = totalMemBytes;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.DISK_INFO_PROPERTY_ID)
    public java.util.List<org.apache.ambari.server.agent.DiskInfo> getDisksInfo() {
        return disksInfo;
    }

    public void setDisksInfo(java.util.List<org.apache.ambari.server.agent.DiskInfo> disksInfo) {
        this.disksInfo = disksInfo;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.LAST_HEARTBEAT_TIME_PROPERTY_ID)
    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.LAST_REGISTRATION_TIME_PROPERTY_ID)
    public long getLastRegistrationTime() {
        return lastRegistrationTime;
    }

    public void setLastRegistrationTime(long lastRegistrationTime) {
        this.lastRegistrationTime = lastRegistrationTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.RACK_INFO_PROPERTY_ID)
    public java.lang.String getRackInfo() {
        return rackInfo;
    }

    public void setRackInfo(java.lang.String rackInfo) {
        this.rackInfo = rackInfo;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Map<java.lang.String, java.lang.String> getHostAttributes() {
        return hostAttributes;
    }

    public void setHostAttributes(java.util.Map<java.lang.String, java.lang.String> hostAttributes) {
        this.hostAttributes = hostAttributes;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public org.apache.ambari.server.state.AgentVersion getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(org.apache.ambari.server.state.AgentVersion agentVersion) {
        this.agentVersion = agentVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HEALTH_REPORT_PROPERTY_ID)
    public java.lang.String getHealthReport() {
        return healthStatus.getHealthReport();
    }

    public void setHealthStatus(org.apache.ambari.server.state.HostHealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.HostResponse other = ((org.apache.ambari.server.controller.HostResponse) (o));
        return java.util.Objects.equals(hostname, other.hostname);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hashCode(hostname);
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.PUBLIC_NAME_PROPERTY_ID)
    public java.lang.String getPublicHostName() {
        return publicHostname;
    }

    public void setPublicHostName(java.lang.String name) {
        publicHostname = name;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.STATE_PROPERTY_ID)
    public org.apache.ambari.server.state.HostState getHostState() {
        return hostState;
    }

    public void setHostState(org.apache.ambari.server.state.HostState hostState) {
        this.hostState = hostState;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.LAST_AGENT_ENV_PROPERTY_ID)
    public org.apache.ambari.server.agent.AgentEnv getLastAgentEnv() {
        return lastAgentEnv;
    }

    public void setLastAgentEnv(org.apache.ambari.server.agent.AgentEnv agentEnv) {
        lastAgentEnv = agentEnv;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.DESIRED_CONFIGS_PROPERTY_ID)
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> getDesiredHostConfigs() {
        return desiredHostConfigs;
    }

    public void setDesiredHostConfigs(java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> desiredHostConfigs) {
        this.desiredHostConfigs = desiredHostConfigs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_STATUS_PROPERTY_ID)
    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState state) {
        maintenanceState = state;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.MAINTENANCE_STATE_PROPERTY_ID)
    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return maintenanceState;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.RECOVERY_SUMMARY_PROPERTY_ID)
    public java.lang.String getRecoverySummary() {
        return recoverySummary;
    }

    public void setRecoverySummary(java.lang.String recoverySummary) {
        this.recoverySummary = recoverySummary;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.RECOVERY_REPORT_PROPERTY_ID)
    public org.apache.ambari.server.agent.RecoveryReport getRecoveryReport() {
        return recoveryReport;
    }

    public void setRecoveryReport(org.apache.ambari.server.agent.RecoveryReport recoveryReport) {
        this.recoveryReport = recoveryReport;
    }

    public interface HostResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.HostResponse getHostResponse();
    }
}