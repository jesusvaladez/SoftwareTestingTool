package org.apache.ambari.server.events;
public class ServiceCredentialStoreUpdateEvent extends org.apache.ambari.server.events.ServiceEvent {
    public ServiceCredentialStoreUpdateEvent(long clusterId, java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.SERVICE_CREDENTIAL_STORE_UPDATE, clusterId, stackName, stackVersion, serviceName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("ServiceCredentialStoreUpdateEvent{");
        buffer.append("cluserId=").append(m_clusterId);
        buffer.append(", stackName=").append(m_stackName);
        buffer.append(", stackVersion=").append(m_stackVersion);
        buffer.append(", serviceName=").append(m_serviceName);
        buffer.append("}");
        return buffer.toString();
    }
}