package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ServiceRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String desiredState;

    private java.lang.String maintenanceState;

    private java.lang.String credentialStoreEnabled;

    private java.lang.String credentialStoreSupported;

    private java.lang.Long desiredRepositoryVersionId;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity resolvedRepository;

    public ServiceRequest(java.lang.String clusterName, java.lang.String serviceName, java.lang.Long desiredRepositoryVersionId, java.lang.String desiredState) {
        this(clusterName, serviceName, desiredRepositoryVersionId, desiredState, null);
    }

    public ServiceRequest(java.lang.String clusterName, java.lang.String serviceName, java.lang.Long desiredRepositoryVersionId, java.lang.String desiredState, java.lang.String credentialStoreEnabled) {
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.desiredState = desiredState;
        this.desiredRepositoryVersionId = desiredRepositoryVersionId;
        this.credentialStoreEnabled = credentialStoreEnabled;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_name")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "state")
    public java.lang.String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(java.lang.String desiredState) {
        this.desiredState = desiredState;
    }

    public java.lang.Long getDesiredRepositoryVersionId() {
        return desiredRepositoryVersionId;
    }

    @io.swagger.annotations.ApiModelProperty(name = "cluster_name")
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public void setMaintenanceState(java.lang.String state) {
        maintenanceState = state;
    }

    @io.swagger.annotations.ApiModelProperty(name = "maintenance_state")
    public java.lang.String getMaintenanceState() {
        return maintenanceState;
    }

    @io.swagger.annotations.ApiModelProperty(name = "credential_store_enabled")
    public java.lang.String getCredentialStoreEnabled() {
        return credentialStoreEnabled;
    }

    public java.lang.String getCredentialStoreSupported() {
        return credentialStoreSupported;
    }

    public void setCredentialStoreEnabled(java.lang.String credentialStoreEnabled) {
        this.credentialStoreEnabled = credentialStoreEnabled;
    }

    @io.swagger.annotations.ApiModelProperty(name = "credential_store_supporteds")
    public void setCredentialStoreSupported(java.lang.String credentialStoreSupported) {
        this.credentialStoreSupported = credentialStoreSupported;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("clusterName=").append(clusterName).append(", serviceName=").append(serviceName).append(", desiredState=").append(desiredState).append(", credentialStoreEnabled=").append(credentialStoreEnabled).append(", credentialStoreSupported=").append(credentialStoreSupported);
        return sb.toString();
    }

    public void setResolvedRepository(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        resolvedRepository = repositoryVersion;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getResolvedRepository() {
        return resolvedRepository;
    }
}