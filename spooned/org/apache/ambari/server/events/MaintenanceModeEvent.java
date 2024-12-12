package org.apache.ambari.server.events;
public class MaintenanceModeEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final org.apache.ambari.server.state.MaintenanceState m_state;

    private final org.apache.ambari.server.state.Service m_service;

    private final org.apache.ambari.server.state.Host m_host;

    private final org.apache.ambari.server.state.ServiceComponentHost m_serviceComponentHost;

    private final long m_clusterId;

    public MaintenanceModeEvent(org.apache.ambari.server.state.MaintenanceState state, org.apache.ambari.server.state.Service service) {
        this(state, service.getClusterId(), service, null, null);
    }

    public MaintenanceModeEvent(org.apache.ambari.server.state.MaintenanceState state, long clusterId, org.apache.ambari.server.state.Host host) {
        this(state, clusterId, null, host, null);
    }

    public MaintenanceModeEvent(org.apache.ambari.server.state.MaintenanceState state, org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost) {
        this(state, serviceComponentHost.getClusterId(), null, null, serviceComponentHost);
    }

    private MaintenanceModeEvent(org.apache.ambari.server.state.MaintenanceState state, long clusterId, org.apache.ambari.server.state.Service service, org.apache.ambari.server.state.Host host, org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.MAINTENANCE_MODE);
        m_state = state;
        m_clusterId = clusterId;
        m_service = service;
        m_host = host;
        m_serviceComponentHost = serviceComponentHost;
    }

    public org.apache.ambari.server.state.Service getService() {
        return m_service;
    }

    public org.apache.ambari.server.state.Host getHost() {
        return m_host;
    }

    public org.apache.ambari.server.state.ServiceComponentHost getServiceComponentHost() {
        return m_serviceComponentHost;
    }

    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return m_state;
    }

    public long getClusterId() {
        return m_clusterId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.Object object = null;
        if (null != m_service) {
            object = m_service;
        } else if (null != m_host) {
            object = m_host;
        } else {
            object = m_serviceComponentHost;
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("MaintenanceModeEvent{");
        buffer.append("state=").append(m_state);
        buffer.append(", object=").append(object);
        buffer.append("}");
        return buffer.toString();
    }
}