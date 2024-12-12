package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class StackServiceResponse {
    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String serviceName;

    private java.lang.String serviceType;

    private java.lang.String serviceDisplayName;

    private java.lang.String userName;

    private java.lang.String comments;

    private java.lang.String serviceVersion;

    private org.apache.ambari.server.state.ServiceInfo.Selection selection;

    private java.lang.String maintainer;

    private boolean serviceCheckSupported;

    private java.util.List<java.lang.String> customCommands;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes;

    private java.util.Set<java.lang.String> excludedConfigTypes;

    private java.util.List<java.lang.String> requiredServices;

    private java.util.Map<java.lang.String, java.lang.String> serviceProperties;

    private java.io.File kerberosDescriptorFile;

    private boolean credentialStoreSupported;

    private boolean credentialStoreEnabled;

    private boolean credentialStoreRequired;

    private boolean rollingRestartSupported;

    private boolean isSupportDeleteViaUI;

    private final boolean ssoIntegrationSupported;

    private final boolean ssoIntegrationRequiresKerberos;

    private final boolean ldapIntegrationSupported;

    public StackServiceResponse(org.apache.ambari.server.state.ServiceInfo service) {
        serviceName = service.getName();
        serviceType = service.getServiceType();
        serviceDisplayName = service.getDisplayName();
        userName = null;
        comments = service.getComment();
        serviceVersion = service.getVersion();
        configTypes = service.getConfigTypeAttributes();
        excludedConfigTypes = service.getExcludedConfigTypes();
        requiredServices = service.getRequiredServices();
        serviceCheckSupported = null != service.getCommandScript();
        selection = service.getSelection();
        maintainer = service.getMaintainer();
        java.util.List<org.apache.ambari.server.state.CustomCommandDefinition> definitions = service.getCustomCommands();
        if ((null == definitions) || (definitions.size() == 0)) {
            customCommands = java.util.Collections.emptyList();
        } else {
            customCommands = new java.util.ArrayList<>(definitions.size());
            for (org.apache.ambari.server.state.CustomCommandDefinition command : definitions) {
                customCommands.add(command.getName());
            }
        }
        kerberosDescriptorFile = service.getKerberosDescriptorFile();
        serviceProperties = service.getServiceProperties();
        credentialStoreSupported = service.isCredentialStoreSupported();
        credentialStoreEnabled = service.isCredentialStoreEnabled();
        isSupportDeleteViaUI = service.isSupportDeleteViaUI();
        ssoIntegrationSupported = service.isSingleSignOnSupported();
        ssoIntegrationRequiresKerberos = service.isKerberosRequiredForSingleSignOnIntegration();
        ldapIntegrationSupported = service.isLdapSupported();
        rollingRestartSupported = service.isRollingRestartSupported();
    }

    @io.swagger.annotations.ApiModelProperty(name = "selection")
    public org.apache.ambari.server.state.ServiceInfo.Selection getSelection() {
        return selection;
    }

    public void setSelection(org.apache.ambari.server.state.ServiceInfo.Selection selection) {
        this.selection = selection;
    }

    @io.swagger.annotations.ApiModelProperty(name = "maintainer")
    public java.lang.String getMaintainer() {
        return maintainer;
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_name")
    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_version")
    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_name")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_type")
    public java.lang.String getServiceType() {
        return serviceType;
    }

    public void setServiceType(java.lang.String serviceType) {
        this.serviceType = serviceType;
    }

    @io.swagger.annotations.ApiModelProperty(name = "display_name")
    public java.lang.String getServiceDisplayName() {
        return serviceDisplayName;
    }

    public void setServiceDisplayName(java.lang.String serviceDisplayName) {
        this.serviceDisplayName = serviceDisplayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "user_name")
    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "comments")
    public java.lang.String getComments() {
        return comments;
    }

    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_version")
    public java.lang.String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(java.lang.String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = "config_types")
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getConfigTypes() {
        return configTypes;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Set<java.lang.String> getExcludedConfigTypes() {
        return excludedConfigTypes;
    }

    @io.swagger.annotations.ApiModelProperty(name = "required_services")
    public java.util.List<java.lang.String> getRequiredServices() {
        return requiredServices;
    }

    public void setRequiredServices(java.util.List<java.lang.String> requiredServices) {
        this.requiredServices = requiredServices;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.io.File getKerberosDescriptorFile() {
        return kerberosDescriptorFile;
    }

    public void setKerberosDescriptorFile(java.io.File kerberosDescriptorFile) {
        this.kerberosDescriptorFile = kerberosDescriptorFile;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_check_supported")
    public boolean isServiceCheckSupported() {
        return serviceCheckSupported;
    }

    @io.swagger.annotations.ApiModelProperty(name = "custom_commands")
    public java.util.List<java.lang.String> getCustomCommands() {
        return customCommands;
    }

    @io.swagger.annotations.ApiModelProperty(name = "properties")
    public java.util.Map<java.lang.String, java.lang.String> getServiceProperties() {
        return serviceProperties;
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

    @io.swagger.annotations.ApiModelProperty(name = "credential_store_required")
    public boolean isCredentialStoreRequired() {
        return credentialStoreRequired;
    }

    public void setCredentialStoreRequired(boolean credentialStoreRequired) {
        this.credentialStoreRequired = credentialStoreRequired;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public boolean isSupportDeleteViaUI() {
        return isSupportDeleteViaUI;
    }

    @io.swagger.annotations.ApiModelProperty(name = "sso_integration_supported")
    public boolean isSsoIntegrationSupported() {
        return ssoIntegrationSupported;
    }

    @io.swagger.annotations.ApiModelProperty(name = "sso_integration_requires_kerberos")
    public boolean isSsoIntegrationRequiresKerberos() {
        return ssoIntegrationRequiresKerberos;
    }

    @io.swagger.annotations.ApiModelProperty(name = "ldap_integration_supported")
    public boolean isLdapIntegrationSupported() {
        return ldapIntegrationSupported;
    }

    @io.swagger.annotations.ApiModelProperty(name = "rolling_restart_supported")
    public boolean isRollingRestartSupported() {
        return rollingRestartSupported;
    }

    public void setRollingRestartSupported(boolean rollingRestartSupported) {
        this.rollingRestartSupported = rollingRestartSupported;
    }

    public interface StackServiceResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = "StackServices")
        public org.apache.ambari.server.controller.StackServiceResponse getStackServiceResponse();
    }
}