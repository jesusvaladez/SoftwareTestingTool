package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ConfigGroupResponse {
    private java.lang.Long id;

    private java.lang.String clusterName;

    private java.lang.String groupName;

    private java.lang.String tag;

    private java.lang.String description;

    private java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hosts;

    private java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configVersions;

    private java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> versionTags = new java.util.HashSet<>();

    public ConfigGroupResponse(java.lang.Long id, java.lang.String clusterName, java.lang.String groupName, java.lang.String tag, java.lang.String description, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hosts, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configVersions) {
        this.id = id;
        this.clusterName = clusterName;
        this.groupName = groupName;
        this.tag = tag;
        this.description = description;
        this.hosts = hosts;
        this.configVersions = configVersions;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID_PROPERTY_ID)
    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
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

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESCRIPTION_PROPERTY_ID)
    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_PROPERTY_ID)
    public java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getHosts() {
        return hosts;
    }

    public void setHosts(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hosts) {
        this.hosts = hosts;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS_PROPERTY_ID)
    public java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getConfigurations() {
        return configVersions;
    }

    public void setConfigurations(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configurations) {
        this.configVersions = configurations;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.VERSION_TAGS_PROPERTY_ID)
    public java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getVersionTags() {
        return versionTags;
    }

    public void setVersionTags(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> versionTags) {
        this.versionTags = versionTags;
    }

    public interface ConfigGroupWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP)
        org.apache.ambari.server.controller.ConfigGroupResponse getConfigGroup();
    }
}