package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
public class ClusterRequest {
    private final java.lang.Long clusterId;

    private final java.lang.String clusterName;

    private final java.lang.String stackVersion;

    private final java.lang.String provisioningState;

    private org.apache.ambari.server.state.SecurityType securityType;

    private java.util.Set<java.lang.String> hostNames;

    private java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> configs;

    private org.apache.ambari.server.controller.ServiceConfigVersionRequest serviceConfigVersionRequest;

    private final java.util.Map<java.lang.String, java.lang.Object> sessionAttributes;

    public ClusterRequest(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String stackVersion, java.util.Set<java.lang.String> hostNames) {
        this(clusterId, clusterName, null, null, stackVersion, hostNames);
    }

    public ClusterRequest(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String provisioningState, org.apache.ambari.server.state.SecurityType securityType, java.lang.String stackVersion, java.util.Set<java.lang.String> hostNames) {
        this(clusterId, clusterName, provisioningState, securityType, stackVersion, hostNames, null);
    }

    public ClusterRequest(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String provisioningState, org.apache.ambari.server.state.SecurityType securityType, java.lang.String stackVersion, java.util.Set<java.lang.String> hostNames, java.util.Map<java.lang.String, java.lang.Object> sessionAttributes) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.provisioningState = provisioningState;
        this.securityType = securityType;
        this.stackVersion = stackVersion;
        this.hostNames = hostNames;
        this.sessionAttributes = sessionAttributes;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID)
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.PROVISIONING_STATE)
    public java.lang.String getProvisioningState() {
        return provisioningState;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY_TYPE)
    public org.apache.ambari.server.state.SecurityType getSecurityType() {
        return securityType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.VERSION)
    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Set<java.lang.String> getHostNames() {
        return hostNames;
    }

    public void setDesiredConfig(java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> configRequests) {
        configs = configRequests;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.DESIRED_CONFIGS)
    public java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> getDesiredConfig() {
        return configs;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("{" + " clusterName=").append(clusterName).append(", clusterId=").append(clusterId).append(", provisioningState=").append(provisioningState).append(", securityType=").append(securityType).append(", stackVersion=").append(stackVersion).append(", desired_scv=").append(serviceConfigVersionRequest).append(", hosts=[").append(hostNames != null ? java.lang.String.join(",", hostNames) : org.apache.commons.lang.StringUtils.EMPTY).append("] }");
        return sb.toString();
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.DESIRED_SERVICE_CONFIG_VERSIONS)
    public org.apache.ambari.server.controller.ServiceConfigVersionRequest getServiceConfigVersionRequest() {
        return serviceConfigVersionRequest;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Map<java.lang.String, java.lang.Object> getSessionAttributes() {
        return sessionAttributes;
    }

    public void setServiceConfigVersionRequest(org.apache.ambari.server.controller.ServiceConfigVersionRequest serviceConfigVersionRequest) {
        this.serviceConfigVersionRequest = serviceConfigVersionRequest;
    }
}