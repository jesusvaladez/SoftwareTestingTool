package org.apache.ambari.server.controller;
public class ServiceComponentResponse {
    private java.lang.Long clusterId;

    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String componentName;

    private java.lang.String displayName;

    private java.lang.String desiredStackId;

    private java.lang.String desiredState;

    private java.lang.String category;

    private java.util.Map<java.lang.String, java.lang.Integer> serviceComponentStateCount;

    private boolean recoveryEnabled;

    private java.lang.String desiredVersion;

    private org.apache.ambari.server.state.RepositoryVersionState repoState;

    public ServiceComponentResponse(java.lang.Long clusterId, java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.StackId desiredStackId, java.lang.String desiredState, java.util.Map<java.lang.String, java.lang.Integer> serviceComponentStateCount, boolean recoveryEnabled, java.lang.String displayName, java.lang.String desiredVersion, org.apache.ambari.server.state.RepositoryVersionState repoState) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.displayName = displayName;
        this.desiredStackId = desiredStackId.getStackId();
        this.desiredState = desiredState;
        this.serviceComponentStateCount = serviceComponentStateCount;
        this.recoveryEnabled = recoveryEnabled;
        this.desiredVersion = desiredVersion;
        this.repoState = repoState;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(java.lang.String desiredState) {
        this.desiredState = desiredState;
    }

    public java.lang.String getDesiredStackId() {
        return desiredStackId;
    }

    public java.lang.String getCategory() {
        return category;
    }

    public void setCategory(java.lang.String category) {
        this.category = category;
    }

    public java.util.Map<java.lang.String, java.lang.Integer> getServiceComponentStateCount() {
        return serviceComponentStateCount;
    }

    public boolean isRecoveryEnabled() {
        return recoveryEnabled;
    }

    public void setRecoveryEnabled(boolean recoveryEnabled) {
        this.recoveryEnabled = recoveryEnabled;
    }

    public java.lang.String getDesiredVersion() {
        return desiredVersion;
    }

    public org.apache.ambari.server.state.RepositoryVersionState getRepositoryState() {
        return repoState;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.controller.ServiceComponentResponse that = ((org.apache.ambari.server.controller.ServiceComponentResponse) (o));
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null) {
            return false;
        }
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) {
            return false;
        }
        if (componentName != null ? !componentName.equals(that.componentName) : that.componentName != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.intValue() : 0;
        result = (71 * result) + (clusterName != null ? clusterName.hashCode() : 0);
        result = (71 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (71 * result) + (componentName != null ? componentName.hashCode() : 0);
        return result;
    }
}