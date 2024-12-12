package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ServiceComponentHostResponse implements org.apache.ambari.server.controller.ApiModel {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String componentName;

    private java.lang.String displayName;

    private java.lang.String publicHostname;

    private java.lang.String hostname;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> actualConfigs;

    private java.lang.String liveState;

    private java.lang.String version;

    private java.lang.String desiredStackVersion;

    private java.lang.String desiredRepositoryVersion;

    private java.lang.String desiredState;

    private boolean staleConfig = false;

    private boolean reloadConfig = false;

    private java.lang.String adminState = null;

    private java.lang.String maintenanceState = null;

    private org.apache.ambari.server.state.UpgradeState upgradeState = org.apache.ambari.server.state.UpgradeState.NONE;

    public ServiceComponentHostResponse(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String displayName, java.lang.String hostname, java.lang.String publicHostname, java.lang.String liveState, java.lang.String version, java.lang.String desiredState, java.lang.String desiredStackVersion, java.lang.String desiredRepositoryVersion, org.apache.ambari.server.state.HostComponentAdminState adminState) {
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.displayName = displayName;
        this.hostname = hostname;
        this.publicHostname = publicHostname;
        this.liveState = liveState;
        this.version = version;
        this.desiredState = desiredState;
        this.desiredStackVersion = desiredStackVersion;
        this.desiredRepositoryVersion = desiredRepositoryVersion;
        if (adminState != null) {
            this.adminState = adminState.name();
        }
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID)
    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DISPLAY_NAME_PROPERTY_ID)
    public java.lang.String getDisplayName() {
        return displayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME_PROPERTY_ID)
    public java.lang.String getHostname() {
        return hostname;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.PUBLIC_HOST_NAME_PROPERTY_ID)
    public java.lang.String getPublicHostname() {
        return publicHostname;
    }

    public void setPublicHostname(java.lang.String publicHostname) {
        this.publicHostname = publicHostname;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE_PROPERTY_ID)
    public java.lang.String getLiveState() {
        return liveState;
    }

    public void setLiveState(java.lang.String liveState) {
        this.liveState = liveState;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION_PROPERTY_ID)
    public java.lang.String getVersion() {
        return version;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE_PROPERTY_ID)
    public java.lang.String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(java.lang.String desiredState) {
        this.desiredState = desiredState;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID_PROPERTY_ID)
    public java.lang.String getDesiredStackVersion() {
        return desiredStackVersion;
    }

    public void setDesiredStackVersion(java.lang.String desiredStackVersion) {
        this.desiredStackVersion = desiredStackVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION_PROPERTY_ID)
    public java.lang.String getDesiredRepositoryVersion() {
        return desiredRepositoryVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME_PROPERTY_ID)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE_PROPERTY_ID, hidden = true)
    public java.lang.String getAdminState() {
        return adminState;
    }

    public void setAdminState(java.lang.String adminState) {
        this.adminState = adminState;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.controller.ServiceComponentHostResponse that = ((org.apache.ambari.server.controller.ServiceComponentHostResponse) (o));
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null) {
            return false;
        }
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) {
            return false;
        }
        if (componentName != null ? !componentName.equals(that.componentName) : that.componentName != null) {
            return false;
        }
        if (hostname != null ? !hostname.equals(that.hostname) : that.hostname != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterName != null) ? clusterName.hashCode() : 0;
        result = (71 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (71 * result) + (componentName != null ? componentName.hashCode() : 0);
        result = (71 * result) + (hostname != null ? hostname.hashCode() : 0);
        return result;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ACTUAL_CONFIGS_PROPERTY_ID)
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> getActualConfigs() {
        return actualConfigs;
    }

    public void setActualConfigs(java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> configs) {
        actualConfigs = configs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS_PROPERTY_ID)
    public boolean isStaleConfig() {
        return staleConfig;
    }

    public void setStaleConfig(boolean stale) {
        staleConfig = stale;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.RELOAD_CONFIGS_PROPERTY_ID)
    public boolean isReloadConfig() {
        return reloadConfig;
    }

    public void setReloadConfig(boolean reloadConfig) {
        this.reloadConfig = reloadConfig;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE_PROPERTY_ID)
    public java.lang.String getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(java.lang.String state) {
        maintenanceState = state;
    }

    public void setUpgradeState(org.apache.ambari.server.state.UpgradeState state) {
        upgradeState = state;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE_PROPERTY_ID)
    public org.apache.ambari.server.state.UpgradeState getUpgradeState() {
        return upgradeState;
    }
}