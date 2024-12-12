package org.apache.ambari.server.topology.addservice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import static org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY;
import static org.apache.ambari.server.controller.internal.ClusterResourceProvider.CREDENTIALS;
import static org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY;
import static org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY;
import static org.apache.ambari.server.controller.internal.ServiceResourceProvider.OPERATION_TYPE;
import static org.apache.ambari.server.topology.Configurable.CONFIGURATIONS;
@io.swagger.annotations.ApiModel
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class AddServiceRequest {
    private static final java.lang.String STACK_NAME = "stack_name";

    private static final java.lang.String STACK_VERSION = "stack_version";

    private static final java.lang.String SERVICES = "services";

    private static final java.lang.String COMPONENTS = "components";

    private static final java.lang.String VALIDATION = "validation";

    public static final java.util.Set<java.lang.String> TOP_LEVEL_PROPERTIES = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.ServiceResourceProvider.OPERATION_TYPE, org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY, org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY, org.apache.ambari.server.topology.addservice.AddServiceRequest.VALIDATION, org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_NAME, org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_VERSION, org.apache.ambari.server.topology.addservice.AddServiceRequest.SERVICES, org.apache.ambari.server.topology.addservice.AddServiceRequest.COMPONENTS, org.apache.ambari.server.topology.Configurable.CONFIGURATIONS);

    private final org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType operationType;

    private final org.apache.ambari.server.topology.ConfigRecommendationStrategy recommendationStrategy;

    private final org.apache.ambari.server.controller.internal.ProvisionAction provisionAction;

    private final org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType validationType;

    private final java.lang.String stackName;

    private final java.lang.String stackVersion;

    private final java.util.Set<org.apache.ambari.server.topology.addservice.Service> services;

    private final java.util.Set<org.apache.ambari.server.topology.addservice.Component> components;

    private final org.apache.ambari.server.topology.SecurityConfiguration security;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.Credential> credentials;

    private final org.apache.ambari.server.topology.Configuration configuration;

    @com.fasterxml.jackson.annotation.JsonCreator
    public AddServiceRequest(@com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ServiceResourceProvider.OPERATION_TYPE)
    org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType operationType, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY)
    org.apache.ambari.server.topology.ConfigRecommendationStrategy recommendationStrategy, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)
    org.apache.ambari.server.controller.internal.ProvisionAction provisionAction, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.VALIDATION)
    org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType validationType, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_NAME)
    java.lang.String stackName, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_VERSION)
    java.lang.String stackVersion, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.SERVICES)
    java.util.Set<org.apache.ambari.server.topology.addservice.Service> services, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.COMPONENTS)
    java.util.Set<org.apache.ambari.server.topology.addservice.Component> components, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY)
    org.apache.ambari.server.topology.SecurityConfiguration security, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CREDENTIALS)
    java.util.Set<org.apache.ambari.server.topology.Credential> credentials, @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.Configurable.CONFIGURATIONS)
    java.util.Collection<? extends java.util.Map<java.lang.String, ?>> configs) {
        this.operationType = (null != operationType) ? operationType : org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE;
        this.recommendationStrategy = (null != recommendationStrategy) ? recommendationStrategy : org.apache.ambari.server.topology.ConfigRecommendationStrategy.defaultForAddService();
        this.provisionAction = (null != provisionAction) ? provisionAction : org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START;
        this.validationType = (validationType != null) ? validationType : org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.DEFAULT;
        this.stackName = stackName;
        this.stackVersion = stackVersion;
        this.services = (null != services) ? services : java.util.Collections.emptySet();
        this.components = (null != components) ? components : java.util.Collections.emptySet();
        this.security = security;
        this.configuration = (null != configs) ? org.apache.ambari.server.topology.ConfigurableHelper.parseConfigs(configs) : org.apache.ambari.server.topology.Configuration.newEmpty();
        this.credentials = (null != credentials) ? credentials.stream().collect(java.util.stream.Collectors.toMap(org.apache.ambari.server.topology.Credential::getAlias, java.util.function.Function.identity())) : com.google.common.collect.ImmutableMap.of();
    }

    public static org.apache.ambari.server.topology.addservice.AddServiceRequest of(java.lang.String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, org.apache.ambari.server.topology.addservice.AddServiceRequest.class);
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ServiceResourceProvider.OPERATION_TYPE)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceResourceProvider.OPERATION_TYPE)
    public org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType getOperationType() {
        return operationType;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY)
    public org.apache.ambari.server.topology.ConfigRecommendationStrategy getRecommendationStrategy() {
        return recommendationStrategy;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)
    public org.apache.ambari.server.controller.internal.ProvisionAction getProvisionAction() {
        return provisionAction;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.VALIDATION)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.AddServiceRequest.VALIDATION)
    public org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType getValidationType() {
        return validationType;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_NAME)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_NAME)
    public java.lang.String getStackName() {
        return stackName;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_VERSION)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_VERSION)
    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @org.apache.ambari.annotations.ApiIgnore
    public java.util.Optional<org.apache.ambari.server.state.StackId> getStackId() {
        return (null != stackName) && (null != stackVersion) ? java.util.Optional.of(new org.apache.ambari.server.state.StackId(stackName, stackVersion)) : java.util.Optional.empty();
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.SERVICES)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.AddServiceRequest.SERVICES)
    public java.util.Set<org.apache.ambari.server.topology.addservice.Service> getServices() {
        return services;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.addservice.AddServiceRequest.COMPONENTS)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.addservice.AddServiceRequest.COMPONENTS)
    public java.util.Set<org.apache.ambari.server.topology.addservice.Component> getComponents() {
        return components;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @org.apache.ambari.annotations.ApiIgnore
    public org.apache.ambari.server.topology.Configuration getConfiguration() {
        return configuration;
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.Configurable.CONFIGURATIONS)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.Configurable.CONFIGURATIONS)
    public java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>> getConfigurationContents() {
        return org.apache.ambari.server.topology.ConfigurableHelper.convertConfigToMap(configuration);
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @org.apache.ambari.annotations.ApiIgnore
    public java.util.Map<java.lang.String, org.apache.ambari.server.topology.Credential> getCredentials() {
        return credentials;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    @org.apache.ambari.annotations.ApiIgnore
    public java.util.Optional<org.apache.ambari.server.topology.SecurityConfiguration> getSecurity() {
        return java.util.Optional.ofNullable(security);
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY)
    public org.apache.ambari.server.topology.SecurityConfiguration _getSecurity() {
        return security;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        org.apache.ambari.server.topology.addservice.AddServiceRequest other = ((org.apache.ambari.server.topology.addservice.AddServiceRequest) (obj));
        return ((((((((java.util.Objects.equals(operationType, other.operationType) && java.util.Objects.equals(validationType, other.validationType)) && java.util.Objects.equals(recommendationStrategy, other.recommendationStrategy)) && java.util.Objects.equals(provisionAction, other.provisionAction)) && java.util.Objects.equals(stackName, other.stackName)) && java.util.Objects.equals(stackVersion, other.stackVersion)) && java.util.Objects.equals(services, other.services)) && java.util.Objects.equals(components, other.components)) && java.util.Objects.equals(security, other.security)) && java.util.Objects.equals(configuration, other.configuration);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(operationType, validationType, recommendationStrategy, provisionAction, stackName, stackVersion, services, components, configuration, security);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add(org.apache.ambari.server.controller.internal.ServiceResourceProvider.OPERATION_TYPE, operationType).add(org.apache.ambari.server.topology.addservice.AddServiceRequest.VALIDATION, validationType).add(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY, recommendationStrategy).add(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY, provisionAction).add(org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_NAME, stackName).add(org.apache.ambari.server.topology.addservice.AddServiceRequest.STACK_VERSION, stackVersion).add(org.apache.ambari.server.topology.addservice.AddServiceRequest.SERVICES, services).add(org.apache.ambari.server.topology.addservice.AddServiceRequest.COMPONENTS, components).add(org.apache.ambari.server.topology.Configurable.CONFIGURATIONS, configuration).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY, security).toString();
    }

    public enum OperationType {

        ADD_SERVICE,
        DELETE_SERVICE,
        MOVE_SERVICE;}

    public enum ValidationType {

        STRICT() {
            @java.lang.Override
            public boolean strictValidation() {
                return true;
            }
        },
        PERMISSIVE() {
            @java.lang.Override
            public boolean strictValidation() {
                return false;
            }
        };
        public static final org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType DEFAULT = org.apache.ambari.server.topology.addservice.AddServiceRequest.ValidationType.STRICT;

        public abstract boolean strictValidation();
    }
}