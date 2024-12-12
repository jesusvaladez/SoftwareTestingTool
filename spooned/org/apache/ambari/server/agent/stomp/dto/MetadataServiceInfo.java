package org.apache.ambari.server.agent.stomp.dto;
public class MetadataServiceInfo {
    private java.lang.String version;

    private java.lang.Boolean credentialStoreEnabled;

    @com.fasterxml.jackson.annotation.JsonProperty("configuration_credentials")
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> credentialStoreEnabledProperties;

    @com.fasterxml.jackson.annotation.JsonProperty("status_commands_timeout")
    private java.lang.Long statusCommandsTimeout;

    @com.fasterxml.jackson.annotation.JsonProperty("service_package_folder")
    private java.lang.String servicePackageFolder;

    public MetadataServiceInfo(java.lang.String version, java.lang.Boolean credentialStoreEnabled, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> credentialStoreEnabledProperties, java.lang.Long statusCommandsTimeout, java.lang.String servicePackageFolder) {
        this.version = version;
        this.credentialStoreEnabled = credentialStoreEnabled;
        this.credentialStoreEnabledProperties = credentialStoreEnabledProperties;
        this.statusCommandsTimeout = statusCommandsTimeout;
        this.servicePackageFolder = servicePackageFolder;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public java.lang.Boolean getCredentialStoreEnabled() {
        return credentialStoreEnabled;
    }

    public void setCredentialStoreEnabled(java.lang.Boolean credentialStoreEnabled) {
        this.credentialStoreEnabled = credentialStoreEnabled;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getCredentialStoreEnabledProperties() {
        return credentialStoreEnabledProperties;
    }

    public void setCredentialStoreEnabledProperties(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> credentialStoreEnabledProperties) {
        this.credentialStoreEnabledProperties = credentialStoreEnabledProperties;
    }

    public java.lang.Long getStatusCommandsTimeout() {
        return statusCommandsTimeout;
    }

    public void setStatusCommandsTimeout(java.lang.Long statusCommandsTimeout) {
        this.statusCommandsTimeout = statusCommandsTimeout;
    }

    public java.lang.String getServicePackageFolder() {
        return servicePackageFolder;
    }

    public void setServicePackageFolder(java.lang.String servicePackageFolder) {
        this.servicePackageFolder = servicePackageFolder;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo that = ((org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo) (o));
        if (version != null ? !version.equals(that.version) : that.version != null)
            return false;

        if (credentialStoreEnabled != null ? !credentialStoreEnabled.equals(that.credentialStoreEnabled) : that.credentialStoreEnabled != null)
            return false;

        if (credentialStoreEnabledProperties != null ? !credentialStoreEnabledProperties.equals(that.credentialStoreEnabledProperties) : that.credentialStoreEnabledProperties != null)
            return false;

        if (statusCommandsTimeout != null ? !statusCommandsTimeout.equals(that.statusCommandsTimeout) : that.statusCommandsTimeout != null)
            return false;

        return servicePackageFolder != null ? servicePackageFolder.equals(that.servicePackageFolder) : that.servicePackageFolder == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (version != null) ? version.hashCode() : 0;
        result = (31 * result) + (credentialStoreEnabled != null ? credentialStoreEnabled.hashCode() : 0);
        result = (31 * result) + (credentialStoreEnabledProperties != null ? credentialStoreEnabledProperties.hashCode() : 0);
        result = (31 * result) + (statusCommandsTimeout != null ? statusCommandsTimeout.hashCode() : 0);
        result = (31 * result) + (servicePackageFolder != null ? servicePackageFolder.hashCode() : 0);
        return result;
    }
}