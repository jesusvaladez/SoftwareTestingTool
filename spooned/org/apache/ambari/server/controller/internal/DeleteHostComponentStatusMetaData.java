package org.apache.ambari.server.controller.internal;
import javax.annotation.concurrent.NotThreadSafe;
@javax.annotation.concurrent.NotThreadSafe
public class DeleteHostComponentStatusMetaData extends org.apache.ambari.server.controller.internal.DeleteStatusMetaData {
    private java.util.Set<org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData.HostComponent> removedHostComponents;

    private org.apache.ambari.server.AmbariException ambariException;

    public DeleteHostComponentStatusMetaData() {
        removedHostComponents = new java.util.HashSet<>();
    }

    public void addDeletedHostComponent(java.lang.String componentName, java.lang.String serviceName, java.lang.String hostName, java.lang.Long hostId, java.lang.String clusterId, java.lang.String version, org.apache.ambari.server.state.State lastComponentState) {
        removedHostComponents.add(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData.HostComponent(componentName, serviceName, hostId, hostName, clusterId, version, lastComponentState));
        addDeletedKey((hostName + "/") + componentName);
    }

    public java.util.Set<org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData.HostComponent> getRemovedHostComponents() {
        return removedHostComponents;
    }

    public org.apache.ambari.server.AmbariException getAmbariException() {
        return ambariException;
    }

    public void setAmbariException(org.apache.ambari.server.AmbariException ambariException) {
        this.ambariException = ambariException;
    }

    public class HostComponent {
        private java.lang.String componentName;

        private java.lang.String serviceName;

        private java.lang.Long hostId;

        private java.lang.String hostName;

        private java.lang.String clusterId;

        private java.lang.String version;

        private org.apache.ambari.server.state.State lastComponentState;

        public HostComponent(java.lang.String componentName, java.lang.String serviceName, java.lang.Long hostId, java.lang.String hostName, java.lang.String clusterId, java.lang.String version, org.apache.ambari.server.state.State lastComponentState) {
            this.componentName = componentName;
            this.serviceName = serviceName;
            this.hostId = hostId;
            this.hostName = hostName;
            this.clusterId = clusterId;
            this.version = version;
            this.lastComponentState = lastComponentState;
        }

        public java.lang.String getComponentName() {
            return componentName;
        }

        public void setComponentName(java.lang.String componentName) {
            this.componentName = componentName;
        }

        public java.lang.String getClusterId() {
            return clusterId;
        }

        public void setClusterId(java.lang.String clusterId) {
            this.clusterId = clusterId;
        }

        public java.lang.String getVersion() {
            return version;
        }

        public void setVersion(java.lang.String version) {
            this.version = version;
        }

        public java.lang.String getServiceName() {
            return serviceName;
        }

        public void setServiceName(java.lang.String serviceName) {
            this.serviceName = serviceName;
        }

        public java.lang.String getHostName() {
            return hostName;
        }

        public void setHostName(java.lang.String hostName) {
            this.hostName = hostName;
        }

        public java.lang.Long getHostId() {
            return hostId;
        }

        public void setHostId(java.lang.Long hostId) {
            this.hostId = hostId;
        }

        public org.apache.ambari.server.state.State getLastComponentState() {
            return lastComponentState;
        }

        public void setLastComponentState(org.apache.ambari.server.state.State lastComponentState) {
            this.lastComponentState = lastComponentState;
        }
    }
}