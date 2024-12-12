package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ServiceResponse {
    private java.lang.Long clusterId;

    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private org.apache.ambari.server.state.StackId desiredStackId;

    private java.lang.String desiredRepositoryVersion;

    private java.lang.Long desiredRepositoryVersionId;

    private org.apache.ambari.server.state.RepositoryVersionState repositoryVersionState;

    private java.lang.String desiredState;

    private java.lang.String maintenanceState;

    private boolean credentialStoreSupported;

    private boolean credentialStoreEnabled;

    private final boolean ssoIntegrationSupported;

    private final boolean ssoIntegrationDesired;

    private final boolean ssoIntegrationEnabled;

    private final boolean ssoIntegrationRequiresKerberos;

    private final boolean kerberosEnabled;

    private final boolean ldapIntegrationSupported;

    private final boolean ldapIntegrationEnabled;

    private final boolean ldapIntegrationDesired;

    public ServiceResponse(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.state.StackId desiredStackId, java.lang.String desiredRepositoryVersion, org.apache.ambari.server.state.RepositoryVersionState repositoryVersionState, java.lang.String desiredState, boolean credentialStoreSupported, boolean credentialStoreEnabled, boolean ssoIntegrationSupported, boolean ssoIntegrationDesired, boolean ssoIntegrationEnabled, boolean ssoIntegrationRequiresKerberos, boolean kerberosEnabled, boolean ldapIntegrationSupported, boolean ldapIntegrationEnabled, boolean ldapIntegrationDesired) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.desiredStackId = desiredStackId;
        this.repositoryVersionState = repositoryVersionState;
        this.ssoIntegrationSupported = ssoIntegrationSupported;
        this.ssoIntegrationDesired = ssoIntegrationDesired;
        this.ssoIntegrationEnabled = ssoIntegrationEnabled;
        setDesiredState(desiredState);
        this.desiredRepositoryVersion = desiredRepositoryVersion;
        this.credentialStoreSupported = credentialStoreSupported;
        this.credentialStoreEnabled = credentialStoreEnabled;
        this.ssoIntegrationRequiresKerberos = ssoIntegrationRequiresKerberos;
        this.kerberosEnabled = kerberosEnabled;
        this.ldapIntegrationSupported = ldapIntegrationSupported;
        this.ldapIntegrationEnabled = ldapIntegrationEnabled;
        this.ldapIntegrationDesired = ldapIntegrationDesired;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_name")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    @io.swagger.annotations.ApiModelProperty(name = "cluster_name")
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "state")
    public java.lang.String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(java.lang.String desiredState) {
        this.desiredState = desiredState;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.lang.String getDesiredStackId() {
        return desiredStackId.getStackId();
    }

    public java.lang.String getDesiredRepositoryVersion() {
        return desiredRepositoryVersion;
    }

    public org.apache.ambari.server.state.RepositoryVersionState getRepositoryVersionState() {
        return repositoryVersionState;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.controller.ServiceResponse that = ((org.apache.ambari.server.controller.ServiceResponse) (o));
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null) {
            return false;
        }
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null) {
            return false;
        }
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) {
            return false;
        }
        return true;
    }

    public void setMaintenanceState(java.lang.String state) {
        maintenanceState = state;
    }

    @io.swagger.annotations.ApiModelProperty(name = "maintenance_state")
    public java.lang.String getMaintenanceState() {
        return maintenanceState;
    }

    @io.swagger.annotations.ApiModelProperty(name = "credential_store_supported")
    public boolean isCredentialStoreSupported() {
        return credentialStoreSupported;
    }

    public void setCredentialStoreSupported(boolean credentialStoreSupported) {
        this.credentialStoreSupported = credentialStoreSupported;
    }

    @io.swagger.annotations.ApiModelProperty(name = "credential_store_enabled")
    public boolean isCredentialStoreEnabled() {
        return credentialStoreEnabled;
    }

    public void setCredentialStoreEnabled(boolean credentialStoreEnabled) {
        this.credentialStoreEnabled = credentialStoreEnabled;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.intValue() : 0;
        result = (71 * result) + (clusterName != null ? clusterName.hashCode() : 0);
        result = (71 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        return result;
    }

    @io.swagger.annotations.ApiModelProperty(name = "sso_integration_supported")
    public boolean isSsoIntegrationSupported() {
        return ssoIntegrationSupported;
    }

    @io.swagger.annotations.ApiModelProperty(name = "sso_integration_desired")
    public boolean isSsoIntegrationDesired() {
        return ssoIntegrationDesired;
    }

    @io.swagger.annotations.ApiModelProperty(name = "sso_integration_enabled")
    public boolean isSsoIntegrationEnabled() {
        return ssoIntegrationEnabled;
    }

    @io.swagger.annotations.ApiModelProperty(name = "sso_integration_requires_kerberos")
    public boolean isSsoIntegrationRequiresKerberos() {
        return ssoIntegrationRequiresKerberos;
    }

    @io.swagger.annotations.ApiModelProperty(name = "kerberos_enabled")
    public boolean isKerberosEnabled() {
        return kerberosEnabled;
    }

    @io.swagger.annotations.ApiModelProperty(name = "ldap_integration_supported")
    public boolean isLdapIntegrationSupported() {
        return ldapIntegrationSupported;
    }

    @io.swagger.annotations.ApiModelProperty(name = "ldap_integration_enabled")
    public boolean isLdapIntegrationEnabled() {
        return ldapIntegrationEnabled;
    }

    @io.swagger.annotations.ApiModelProperty(name = "ldap_integration_desired")
    public boolean isLdapIntegrationDesired() {
        return ldapIntegrationDesired;
    }

    public interface ServiceResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = "ServiceInfo")
        org.apache.ambari.server.controller.ServiceResponse getServiceResponse();
    }

    public void setDesiredRepositoryVersionId(java.lang.Long id) {
        desiredRepositoryVersionId = id;
    }

    public java.lang.Long getDesiredRepositoryVersionId() {
        return desiredRepositoryVersionId;
    }
}