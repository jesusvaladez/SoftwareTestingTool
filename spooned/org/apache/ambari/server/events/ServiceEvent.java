package org.apache.ambari.server.events;
public abstract class ServiceEvent extends org.apache.ambari.server.events.ClusterEvent {
    protected final java.lang.String m_serviceName;

    protected final java.lang.String m_stackName;

    protected final java.lang.String m_stackVersion;

    public ServiceEvent(org.apache.ambari.server.events.AmbariEvent.AmbariEventType eventType, long clusterId, java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) {
        super(eventType, clusterId);
        m_stackName = stackName;
        m_stackVersion = stackVersion;
        m_serviceName = serviceName;
    }

    public java.lang.String getServiceName() {
        return m_serviceName;
    }

    public java.lang.String getStackName() {
        return m_stackName;
    }

    public java.lang.String getStackVersion() {
        return m_stackVersion;
    }
}