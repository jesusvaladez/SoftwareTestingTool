package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ConfigGroupRequest {
    private java.lang.Long id;

    private java.lang.String clusterName;

    private java.lang.String groupName;

    private java.lang.String tag;

    private java.lang.String serviceName;

    private java.lang.String description;

    private java.lang.String serviceConfigVersionNote;

    private java.util.Set<java.lang.String> hosts;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs;

    public ConfigGroupRequest(java.lang.Long id, java.lang.String clusterName, java.lang.String groupName, java.lang.String tag, java.lang.String serviceName, java.lang.String description, java.util.Set<java.lang.String> hosts, java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs) {
        this.id = id;
        this.clusterName = clusterName;
        this.groupName = groupName;
        this.tag = tag;
        this.serviceName = serviceName;
        this.description = description;
        this.hosts = hosts;
        this.configs = configs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME_PROPERTY_ID)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME_PROPERTY_ID)
    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG_PROPERTY_ID)
    public java.lang.String getTag() {
        return tag;
    }

    public void setTag(java.lang.String tag) {
        this.tag = tag;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_NAME_PROPERTY_ID)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESCRIPTION_PROPERTY_ID)
    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_PROPERTY_ID)
    public java.util.Set<java.lang.String> getHosts() {
        return hosts;
    }

    public void setHosts(java.util.Set<java.lang.String> hosts) {
        this.hosts = hosts;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS_PROPERTY_ID)
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> getConfigs() {
        return configs;
    }

    public void setConfigs(java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs) {
        this.configs = configs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID_PROPERTY_ID)
    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID)
    public java.lang.String getServiceConfigVersionNote() {
        return serviceConfigVersionNote;
    }

    public void setServiceConfigVersionNote(java.lang.String serviceConfigVersionNote) {
        this.serviceConfigVersionNote = serviceConfigVersionNote;
    }
}