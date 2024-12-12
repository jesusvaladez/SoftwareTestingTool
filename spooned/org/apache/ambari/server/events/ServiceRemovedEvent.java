package org.apache.ambari.server.events;
public class ServiceRemovedEvent extends org.apache.ambari.server.events.ServiceEvent {
    private final java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components;

    public ServiceRemovedEvent(long clusterId, java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.SERVICE_REMOVED_SUCCESS, clusterId, stackName, stackVersion, serviceName);
        this.components = components;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("ServiceRemovedEvent{");
        buffer.append("cluserId=").append(m_clusterId);
        buffer.append(", stackName=").append(m_stackName);
        buffer.append(", stackVersion=").append(m_stackVersion);
        buffer.append(", serviceName=").append(m_serviceName);
        buffer.append("}");
        return buffer.toString();
    }

    public java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> getComponents() {
        return components;
    }

    public java.util.List<java.lang.String> getComponentNames() {
        return components.stream().map(org.apache.ambari.server.serveraction.kerberos.Component::getServiceComponentName).collect(java.util.stream.Collectors.toList());
    }
}