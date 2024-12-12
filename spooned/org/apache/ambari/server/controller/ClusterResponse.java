package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ClusterResponse {
    private final long clusterId;

    private final java.lang.String clusterName;

    private final java.util.Set<java.lang.String> hostNames;

    private final java.lang.String desiredStackVersion;

    private final org.apache.ambari.server.state.State provisioningState;

    private final org.apache.ambari.server.state.SecurityType securityType;

    private final int totalHosts;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs;

    private java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> desiredServiceConfigVersions;

    private org.apache.ambari.server.state.ClusterHealthReport clusterHealthReport;

    private java.util.Map<java.lang.String, java.lang.String> credentialStoreServiceProperties;

    public ClusterResponse(long clusterId, java.lang.String clusterName, org.apache.ambari.server.state.State provisioningState, org.apache.ambari.server.state.SecurityType securityType, java.util.Set<java.lang.String> hostNames, int totalHosts, java.lang.String desiredStackVersion, org.apache.ambari.server.state.ClusterHealthReport clusterHealthReport) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.hostNames = hostNames;
        this.totalHosts = totalHosts;
        this.desiredStackVersion = desiredStackVersion;
        this.clusterHealthReport = clusterHealthReport;
        if (null != provisioningState) {
            this.provisioningState = provisioningState;
        } else {
            this.provisioningState = org.apache.ambari.server.state.State.UNKNOWN;
        }
        if (null == securityType) {
            this.securityType = org.apache.ambari.server.state.SecurityType.NONE;
        } else {
            this.securityType = securityType;
        }
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID)
    public long getClusterId() {
        return clusterId;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.PROVISIONING_STATE)
    public org.apache.ambari.server.state.State getProvisioningState() {
        return provisioningState;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY_TYPE)
    public org.apache.ambari.server.state.SecurityType getSecurityType() {
        return securityType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("{ clusterName=").append(clusterName).append(", clusterId=").append(clusterId).append(", provisioningState=").append(provisioningState).append(", desiredStackVersion=").append(desiredStackVersion).append(", totalHosts=").append(totalHosts).append(", hosts=[");
        if (hostNames != null) {
            int i = 0;
            for (java.lang.String hostName : hostNames) {
                if (i != 0) {
                    sb.append(",");
                }
                ++i;
                sb.append(hostName);
            }
        }
        sb.append("], clusterHealthReport= ").append(clusterHealthReport).append("}");
        return sb.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.controller.ClusterResponse other = ((org.apache.ambari.server.controller.ClusterResponse) (o));
        return java.util.Objects.equals(clusterId, other.clusterId) && java.util.Objects.equals(clusterName, other.clusterName);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(clusterId, clusterName);
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.VERSION)
    public java.lang.String getDesiredStackVersion() {
        return desiredStackVersion;
    }

    public void setDesiredConfigs(java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> configs) {
        desiredConfigs = configs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.DESIRED_CONFIGS)
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> getDesiredConfigs() {
        return desiredConfigs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.TOTAL_HOSTS)
    public int getTotalHosts() {
        return totalHosts;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.HEALTH_REPORT)
    public org.apache.ambari.server.state.ClusterHealthReport getClusterHealthReport() {
        return clusterHealthReport;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.DESIRED_SERVICE_CONFIG_VERSIONS)
    public java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> getDesiredServiceConfigVersions() {
        return desiredServiceConfigVersions;
    }

    public void setDesiredServiceConfigVersions(java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> desiredServiceConfigVersions) {
        this.desiredServiceConfigVersions = desiredServiceConfigVersions;
    }

    public void setCredentialStoreServiceProperties(java.util.Map<java.lang.String, java.lang.String> credentialServiceProperties) {
        this.credentialStoreServiceProperties = credentialServiceProperties;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CREDENTIAL_STORE_PROPERTIES)
    public java.util.Map<java.lang.String, java.lang.String> getCredentialStoreServiceProperties() {
        return credentialStoreServiceProperties;
    }

    public interface ClusterResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.ClusterResponse getClusterResponse();
    }
}