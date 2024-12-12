package org.apache.ambari.server.api.services.stackadvisor;
import org.apache.commons.lang.StringUtils;
public class StackAdvisorRequest {
    private java.lang.Long clusterId;

    private java.lang.String serviceName;

    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType;

    private java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();

    private java.util.Collection<java.lang.String> services = new java.util.ArrayList<>();

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostComponents = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroupBindings = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurations = new java.util.HashMap<>();

    private java.util.List<org.apache.ambari.server.state.ChangedConfigInfo> changedConfigurations = new java.util.LinkedList<>();

    private java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups;

    private java.util.Map<java.lang.String, java.lang.String> userContext = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.Object> ldapConfig = new java.util.HashMap<>();

    private java.lang.Boolean gplLicenseAccepted;

    private java.lang.Boolean configsResponse = false;

    public java.lang.String getStackName() {
        return stackName;
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType getRequestType() {
        return requestType;
    }

    public java.util.List<java.lang.String> getHosts() {
        return hosts;
    }

    public java.util.Collection<java.lang.String> getServices() {
        return services;
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getComponentHostsMap() {
        return componentHostsMap;
    }

    public java.lang.String getHostsCommaSeparated() {
        return org.apache.commons.lang.StringUtils.join(hosts, ",");
    }

    public java.lang.String getServicesCommaSeparated() {
        return org.apache.commons.lang.StringUtils.join(services, ",");
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getHostComponents() {
        return hostComponents;
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getHostGroupBindings() {
        return hostGroupBindings;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getConfigurations() {
        return configurations;
    }

    public java.util.Map<java.lang.String, java.lang.Object> getLdapConfig() {
        return ldapConfig;
    }

    public java.util.List<org.apache.ambari.server.state.ChangedConfigInfo> getChangedConfigurations() {
        return changedConfigurations;
    }

    public void setChangedConfigurations(java.util.List<org.apache.ambari.server.state.ChangedConfigInfo> changedConfigurations) {
        this.changedConfigurations = changedConfigurations;
    }

    public java.util.Map<java.lang.String, java.lang.String> getUserContext() {
        return this.userContext;
    }

    public void setUserContext(java.util.Map<java.lang.String, java.lang.String> userContext) {
        this.userContext = userContext;
    }

    public java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> getConfigGroups() {
        return configGroups;
    }

    public void setConfigGroups(java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups) {
        this.configGroups = configGroups;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public java.lang.Boolean getGplLicenseAccepted() {
        return gplLicenseAccepted;
    }

    public java.lang.Boolean getConfigsResponse() {
        return configsResponse;
    }

    private StackAdvisorRequest(java.lang.String stackName, java.lang.String stackVersion) {
        this.stackName = stackName;
        this.stackVersion = stackVersion;
    }

    public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder builder() {
        return org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(stackName, stackVersion).ofType(requestType).forHosts(hosts).forServices(services).forHostComponents(hostComponents).forHostsGroupBindings(hostGroupBindings).withComponentHostsMap(componentHostsMap).withConfigurations(configurations).withChangedConfigurations(changedConfigurations).withConfigGroups(configGroups).withUserContext(userContext).withGPLLicenseAccepted(gplLicenseAccepted).withLdapConfig(ldapConfig);
    }

    public static class StackAdvisorRequestBuilder {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest instance;

        private StackAdvisorRequestBuilder(java.lang.String stackName, java.lang.String stackVersion) {
            this.instance = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest(stackName, stackVersion);
        }

        public static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder forStack(org.apache.ambari.server.state.StackId stackId) {
            return org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(stackId.getStackName(), stackId.getStackVersion());
        }

        public static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder forStack(java.lang.String stackName, java.lang.String stackVersion) {
            return new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder(stackName, stackVersion);
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType) {
            this.instance.requestType = requestType;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder forHosts(java.util.List<java.lang.String> hosts) {
            this.instance.hosts = hosts;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder forServices(java.util.Collection<java.lang.String> services) {
            this.instance.services = services;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withComponentHostsMap(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap) {
            this.instance.componentHostsMap = componentHostsMap;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder forHostComponents(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostComponents) {
            this.instance.hostComponents = hostComponents;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder forHostsGroupBindings(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroupBindings) {
            this.instance.hostGroupBindings = hostGroupBindings;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurations) {
            this.instance.configurations = configurations;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withConfigurations(org.apache.ambari.server.topology.Configuration configuration) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getFullProperties();
            this.instance.configurations = properties.entrySet().stream().map(e -> org.apache.commons.lang3.tuple.Pair.of(e.getKey(), com.google.common.collect.ImmutableMap.of("properties", e.getValue()))).collect(java.util.stream.Collectors.toMap(org.apache.commons.lang3.tuple.Pair::getKey, org.apache.commons.lang3.tuple.Pair::getValue));
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withChangedConfigurations(java.util.List<org.apache.ambari.server.state.ChangedConfigInfo> changedConfigurations) {
            this.instance.changedConfigurations = changedConfigurations;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withUserContext(java.util.Map<java.lang.String, java.lang.String> userContext) {
            this.instance.userContext = userContext;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withConfigGroups(java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups) {
            this.instance.configGroups = configGroups;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withGPLLicenseAccepted(java.lang.Boolean gplLicenseAccepted) {
            this.instance.gplLicenseAccepted = gplLicenseAccepted;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withLdapConfig(java.util.Map<java.lang.String, java.lang.Object> ldapConfig) {
            com.google.common.base.Preconditions.checkNotNull(ldapConfig);
            this.instance.ldapConfig = ldapConfig;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withClusterId(java.lang.Long clusterId) {
            this.instance.clusterId = clusterId;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withServiceName(java.lang.String serviceName) {
            this.instance.serviceName = serviceName;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder withConfigsResponse(java.lang.Boolean configsResponse) {
            this.instance.configsResponse = configsResponse;
            return this;
        }

        public org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest build() {
            return this.instance;
        }
    }

    public enum StackAdvisorRequestType {

        HOST_GROUPS("host_groups"),
        CONFIGURATIONS("configurations"),
        LDAP_CONFIGURATIONS("ldap-configurations"),
        SSO_CONFIGURATIONS("sso-configurations"),
        KERBEROS_CONFIGURATIONS("kerberos-configurations"),
        CONFIGURATION_DEPENDENCIES("configuration-dependencies");
        private java.lang.String type;

        StackAdvisorRequestType(java.lang.String type) {
            this.type = type;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return type;
        }

        public static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType fromString(java.lang.String text) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
            if (text != null) {
                for (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType next : org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.values()) {
                    if (text.equalsIgnoreCase(next.type)) {
                        return next;
                    }
                }
            }
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(java.lang.String.format("Unknown request type: %s, possible values: %s", text, java.util.Arrays.toString(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.values())));
        }
    }
}