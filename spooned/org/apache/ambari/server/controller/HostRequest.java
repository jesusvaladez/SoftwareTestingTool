package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class HostRequest implements org.apache.ambari.server.controller.ApiModel {
    private java.lang.String hostname;

    private java.lang.String publicHostname;

    private java.lang.String clusterName;

    private java.lang.String rackInfo;

    private java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> desiredConfigs;

    private java.lang.String maintenanceState;

    private java.lang.String blueprint;

    private java.lang.String hostGroup;

    public HostRequest(java.lang.String hostname, java.lang.String clusterName) {
        this.hostname = hostname;
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_NAME_PROPERTY_ID)
    public java.lang.String getHostname() {
        return hostname;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.RACK_INFO_PROPERTY_ID)
    public java.lang.String getRackInfo() {
        return rackInfo;
    }

    public void setRackInfo(java.lang.String info) {
        rackInfo = info;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.PUBLIC_NAME_PROPERTY_ID)
    public java.lang.String getPublicHostName() {
        return publicHostname;
    }

    public void setPublicHostName(java.lang.String name) {
        publicHostname = name;
    }

    public void setDesiredConfigs(java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> request) {
        desiredConfigs = request;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.DESIRED_CONFIGS_PROPERTY_ID)
    public java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> getDesiredConfigs() {
        return desiredConfigs;
    }

    public void setMaintenanceState(java.lang.String state) {
        maintenanceState = state;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.MAINTENANCE_STATE_PROPERTY_ID)
    public java.lang.String getMaintenanceState() {
        return maintenanceState;
    }

    public void setBlueprintName(java.lang.String blueprintName) {
        blueprint = blueprintName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID)
    public java.lang.String getBlueprintName() {
        return blueprint;
    }

    public void setHostGroupName(java.lang.String hostGroupName) {
        hostGroup = hostGroupName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID)
    public java.lang.String getHostGroupName() {
        return hostGroup;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("{ hostname=" + hostname) + ", clusterName=") + clusterName) + " }";
    }
}