package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ConfigurationRequest {
    private java.lang.String clusterName;

    private java.lang.String type;

    private java.lang.String tag;

    private java.lang.Long version;

    private java.lang.String serviceConfigVersionNote;

    private java.util.Map<java.lang.String, java.lang.String> configs;

    private boolean selected = true;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configsAttributes;

    private boolean includeProperties;

    public ConfigurationRequest() {
        configs = new java.util.HashMap<>();
        configsAttributes = new java.util.HashMap<>();
    }

    public ConfigurationRequest(java.lang.String clusterName, java.lang.String type, java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> configs, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configsAttributes) {
        this.clusterName = clusterName;
        this.configs = configs;
        this.type = type;
        this.tag = tag;
        this.configsAttributes = configsAttributes;
        this.includeProperties = (type != null) && (tag != null);
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE_PROPERTY_ID)
    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG_PROPERTY_ID)
    public java.lang.String getVersionTag() {
        return tag;
    }

    public void setVersionTag(java.lang.String versionTag) {
        this.tag = versionTag;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.PROPERTIES_PROPERTY_ID)
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return configs;
    }

    public void setProperties(java.util.Map<java.lang.String, java.lang.String> configs) {
        this.configs = configs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public boolean isSelected() {
        return selected;
    }

    public void setIncludeProperties(boolean includeProperties) {
        this.includeProperties = includeProperties;
    }

    public boolean includeProperties() {
        return this.includeProperties;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID)
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getPropertiesAttributes() {
        return configsAttributes;
    }

    public void setPropertiesAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configsAttributes) {
        this.configsAttributes = configsAttributes;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.VERSION_PROPERTY_ID)
    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.lang.String getServiceConfigVersionNote() {
        return serviceConfigVersionNote;
    }

    public void setServiceConfigVersionNote(java.lang.String serviceConfigVersionNote) {
        this.serviceConfigVersionNote = serviceConfigVersionNote;
    }
}