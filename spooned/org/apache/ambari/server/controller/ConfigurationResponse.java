package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ConfigurationResponse {
    private final java.lang.String clusterName;

    private final org.apache.ambari.server.state.StackId stackId;

    private final java.lang.String type;

    private java.lang.String versionTag;

    private java.lang.Long version;

    private java.util.List<java.lang.Long> serviceConfigVersions;

    private java.util.Map<java.lang.String, java.lang.String> configs;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes;

    private java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes;

    public ConfigurationResponse(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, java.lang.String type, java.lang.String versionTag, java.lang.Long version, java.util.Map<java.lang.String, java.lang.String> configs, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes) {
        this.clusterName = clusterName;
        this.stackId = stackId;
        this.configs = configs;
        this.type = type;
        this.versionTag = versionTag;
        this.version = version;
        this.configAttributes = configAttributes;
        org.apache.ambari.server.utils.SecretReference.replacePasswordsWithReferencesForCustomProperties(configAttributes, configs, type, version);
    }

    public ConfigurationResponse(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, java.lang.String type, java.lang.String versionTag, java.lang.Long version, java.util.Map<java.lang.String, java.lang.String> configs, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes, java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes) {
        this.clusterName = clusterName;
        this.stackId = stackId;
        this.configs = configs;
        this.type = type;
        this.versionTag = versionTag;
        this.version = version;
        this.configAttributes = configAttributes;
        this.propertiesTypes = propertiesTypes;
        org.apache.ambari.server.utils.SecretReference.replacePasswordsWithReferences(propertiesTypes, configs, type, version);
        org.apache.ambari.server.utils.SecretReference.replacePasswordsWithReferencesForCustomProperties(configAttributes, configs, type, version);
    }

    public ConfigurationResponse(java.lang.String clusterName, org.apache.ambari.server.state.Config config) {
        this(clusterName, config.getStackId(), config.getType(), config.getTag(), config.getVersion(), config.getProperties(), config.getPropertiesAttributes(), config.getPropertiesTypes());
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG_PROPERTY_ID)
    public java.lang.String getVersionTag() {
        return versionTag;
    }

    public void setVersionTag(java.lang.String versionTag) {
        this.versionTag = versionTag;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.PROPERTIES_PROPERTY_ID)
    public java.util.Map<java.lang.String, java.lang.String> getConfigs() {
        return configs;
    }

    public void setConfigs(java.util.Map<java.lang.String, java.lang.String> configs) {
        this.configs = configs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID)
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getConfigAttributes() {
        return configAttributes;
    }

    public void setConfigAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes) {
        this.configAttributes = configAttributes;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE_PROPERTY_ID)
    public java.lang.String getType() {
        return type;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.VERSION_PROPERTY_ID)
    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.STACK_ID, dataType = "String")
    public org.apache.ambari.server.state.StackId getStackId() {
        return stackId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.controller.ConfigurationResponse that = ((org.apache.ambari.server.controller.ConfigurationResponse) (o));
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null) {
            return false;
        }
        if (stackId != null ? !stackId.equals(that.stackId) : that.stackId != null) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (version != null ? !version.equals(that.version) : that.version != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterName != null) ? clusterName.hashCode() : 0;
        result = (31 * result) + (stackId != null ? stackId.hashCode() : 0);
        result = (31 * result) + (type != null ? type.hashCode() : 0);
        result = (31 * result) + (version != null ? version.hashCode() : 0);
        return result;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.List<java.lang.Long> getServiceConfigVersions() {
        return serviceConfigVersions;
    }

    public void setServiceConfigVersions(java.util.List<java.lang.Long> serviceConfigVersions) {
        this.serviceConfigVersions = serviceConfigVersions;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getPropertiesTypes() {
        return propertiesTypes;
    }

    public void setPropertiesTypes(java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes) {
        this.propertiesTypes = propertiesTypes;
    }
}