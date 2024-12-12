package org.apache.ambari.server.events;
public class ServiceComponentRecoveryChangedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final long m_clusterId;

    private final java.lang.String m_clusterName;

    private final java.lang.String m_serviceName;

    private final java.lang.String m_componentName;

    private final boolean m_recoveryEnabled;

    public ServiceComponentRecoveryChangedEvent(long clusterId, java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, boolean recoveryEnabled) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.SERVICE_COMPONENT_RECOVERY_CHANGED);
        m_clusterId = clusterId;
        m_clusterName = clusterName;
        m_serviceName = serviceName;
        m_componentName = componentName;
        m_recoveryEnabled = recoveryEnabled;
    }

    public long getClusterId() {
        return m_clusterId;
    }

    public java.lang.String getClusterName() {
        return m_clusterName;
    }

    public java.lang.String getServiceName() {
        return m_serviceName;
    }

    public java.lang.String getComponentName() {
        return m_componentName;
    }

    public boolean isRecoveryEnabled() {
        return m_recoveryEnabled;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("ServiceComponentRecoveryChangeEvent{");
        buffer.append("clusterId=").append(getClusterId());
        buffer.append(", clusterName=").append(getClusterName());
        buffer.append(", serviceName=").append(getServiceName());
        buffer.append(", componentName=").append(getComponentName());
        buffer.append(", recoveryEnabled=").append(isRecoveryEnabled());
        buffer.append("}");
        return buffer.toString();
    }
}