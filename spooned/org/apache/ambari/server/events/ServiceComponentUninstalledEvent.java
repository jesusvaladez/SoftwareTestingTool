package org.apache.ambari.server.events;
public class ServiceComponentUninstalledEvent extends org.apache.ambari.server.events.ServiceEvent {
    private final java.lang.String m_componentName;

    private final java.lang.String m_hostName;

    private final boolean m_recoveryEnabled;

    private final boolean masterComponent;

    private final java.lang.Long m_hostId;

    public ServiceComponentUninstalledEvent(long clusterId, java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName, boolean recoveryEnabled, boolean masterComponent, java.lang.Long hostId) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.SERVICE_COMPONENT_UNINSTALLED_SUCCESS, clusterId, stackName, stackVersion, serviceName);
        m_componentName = componentName;
        m_hostName = hostName;
        m_recoveryEnabled = recoveryEnabled;
        this.masterComponent = masterComponent;
        m_hostId = hostId;
    }

    public java.lang.String getComponentName() {
        return m_componentName;
    }

    public java.lang.String getHostName() {
        return m_hostName;
    }

    public boolean isRecoveryEnabled() {
        return m_recoveryEnabled;
    }

    public boolean isMasterComponent() {
        return masterComponent;
    }

    public java.lang.Long getHostId() {
        return m_hostId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("ServiceComponentUninstalledEvent{");
        buffer.append("clusterId=").append(m_clusterId);
        buffer.append(", stackName=").append(m_stackName);
        buffer.append(", stackVersion=").append(m_stackVersion);
        buffer.append(", serviceName=").append(m_serviceName);
        buffer.append(", componentName=").append(m_componentName);
        buffer.append(", hostName=").append(m_hostName);
        buffer.append(", recoveryEnabled=").append(m_recoveryEnabled);
        buffer.append(", hostId=").append(m_hostId);
        buffer.append("}");
        return buffer.toString();
    }

    public org.apache.ambari.server.serveraction.kerberos.Component getComponent() {
        return new org.apache.ambari.server.serveraction.kerberos.Component(getHostName(), getServiceName(), getComponentName(), getHostId());
    }
}