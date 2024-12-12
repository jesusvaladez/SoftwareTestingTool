package org.apache.ambari.server.events.listeners.hosts;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class HostUpdateListener {
    private java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.events.HostUpdateEvent>> hosts = new java.util.HashMap<>();

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper> stackAdvisorHelperProvider;

    @com.google.inject.Inject
    public HostUpdateListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, org.apache.ambari.server.events.publishers.AlertEventPublisher m_alertEventPublisher) {
        ambariEventPublisher.register(this);
        m_alertEventPublisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    public void onHostStatusUpdate(org.apache.ambari.server.events.HostStatusUpdateEvent event) throws org.apache.ambari.server.AmbariException {
        java.lang.String hostName = event.getHostName();
        java.lang.Long lastHeartbeatTime = m_clusters.get().getHost(hostName).getLastHeartbeatTime();
        for (org.apache.ambari.server.state.Cluster cluster : m_clusters.get().getClustersForHost(hostName)) {
            java.lang.Long clusterId = cluster.getClusterId();
            org.apache.ambari.server.events.HostUpdateEvent hostUpdateEvent = retrieveHostUpdateFromCache(clusterId, hostName);
            if (hostUpdateEvent.getHostStatus().equals(event.getHostStatus())) {
                continue;
            } else {
                hostUpdateEvent.setHostStatus(event.getHostStatus());
            }
            hostUpdateEvent.setLastHeartbeatTime(lastHeartbeatTime);
            STOMPUpdatePublisher.publish(org.apache.ambari.server.events.HostUpdateEvent.createHostStatusUpdate(hostUpdateEvent.getClusterName(), hostUpdateEvent.getHostName(), hostUpdateEvent.getHostStatus(), hostUpdateEvent.getLastHeartbeatTime()));
            stackAdvisorHelperProvider.get().clearCaches(hostName);
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onHostStateUpdate(org.apache.ambari.server.events.HostStateUpdateEvent event) throws org.apache.ambari.server.AmbariException {
        java.lang.String hostName = event.getHostName();
        java.lang.Long lastHeartbeatTime = m_clusters.get().getHost(hostName).getLastHeartbeatTime();
        for (org.apache.ambari.server.state.Cluster cluster : m_clusters.get().getClustersForHost(hostName)) {
            java.lang.Long clusterId = cluster.getClusterId();
            org.apache.ambari.server.events.HostUpdateEvent hostUpdateEvent = retrieveHostUpdateFromCache(clusterId, hostName);
            if (hostUpdateEvent.getHostState().equals(event.getHostState())) {
                continue;
            } else {
                hostUpdateEvent.setHostState(event.getHostState());
            }
            hostUpdateEvent.setLastHeartbeatTime(lastHeartbeatTime);
            STOMPUpdatePublisher.publish(org.apache.ambari.server.events.HostUpdateEvent.createHostStateUpdate(hostUpdateEvent.getClusterName(), hostUpdateEvent.getHostName(), hostUpdateEvent.getHostState(), hostUpdateEvent.getLastHeartbeatTime()));
        }
        stackAdvisorHelperProvider.get().clearCaches(hostName);
    }

    @com.google.common.eventbus.Subscribe
    public void onAlertsHostUpdate(org.apache.ambari.server.events.AlertEvent event) throws org.apache.ambari.server.AmbariException {
        java.lang.String hostName;
        if ((!(event instanceof org.apache.ambari.server.events.AlertStateChangeEvent)) && (!(event instanceof org.apache.ambari.server.events.InitialAlertEvent))) {
            return;
        } else if (event instanceof org.apache.ambari.server.events.AlertStateChangeEvent) {
            hostName = ((org.apache.ambari.server.events.AlertStateChangeEvent) (event)).getNewHistoricalEntry().getHostName();
        } else {
            hostName = ((org.apache.ambari.server.events.InitialAlertEvent) (event)).getNewHistoricalEntry().getHostName();
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(hostName)) {
            return;
        }
        java.lang.Long clusterId = event.getClusterId();
        org.apache.ambari.server.events.HostUpdateEvent hostUpdateEvent = retrieveHostUpdateFromCache(clusterId, hostName);
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = alertsDAO.findCurrentCounts(clusterId, null, hostName);
        if (hostUpdateEvent.getAlertsSummary().equals(summary)) {
            return;
        }
        hostUpdateEvent.setAlertsSummary(summary);
        STOMPUpdatePublisher.publish(org.apache.ambari.server.events.HostUpdateEvent.createHostAlertsUpdate(hostUpdateEvent.getClusterName(), hostName, summary));
    }

    @com.google.common.eventbus.Subscribe
    public void onMaintenanceStateUpdate(org.apache.ambari.server.events.MaintenanceModeEvent event) throws org.apache.ambari.server.AmbariException {
        java.lang.Long clusterId = event.getClusterId();
        if ((event.getHost() != null) || (event.getServiceComponentHost() != null)) {
            java.lang.String hostName = (event.getHost() == null) ? event.getServiceComponentHost().getHostName() : event.getHost().getHostName();
            org.apache.ambari.server.events.HostUpdateEvent hostUpdateEvent = retrieveHostUpdateFromCache(clusterId, hostName);
            org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = alertsDAO.findCurrentCounts(clusterId, null, hostName);
            if (hostUpdateEvent.getAlertsSummary().equals(summary)) {
                return;
            }
            hostUpdateEvent.setAlertsSummary(summary);
            if (event.getHost() != null) {
                org.apache.ambari.server.state.MaintenanceState maintenanceState = event.getMaintenanceState();
                hostUpdateEvent.setMaintenanceState(maintenanceState);
                STOMPUpdatePublisher.publish(org.apache.ambari.server.events.HostUpdateEvent.createHostMaintenanceStatusUpdate(hostUpdateEvent.getClusterName(), hostName, maintenanceState, summary));
            } else {
                STOMPUpdatePublisher.publish(org.apache.ambari.server.events.HostUpdateEvent.createHostAlertsUpdate(hostUpdateEvent.getClusterName(), hostName, summary));
            }
            stackAdvisorHelperProvider.get().clearCaches(hostName);
        } else if (event.getService() != null) {
            java.lang.String serviceName = event.getService().getName();
            for (java.lang.String hostName : m_clusters.get().getCluster(clusterId).getService(serviceName).getServiceHosts()) {
                org.apache.ambari.server.events.HostUpdateEvent hostUpdateEvent = retrieveHostUpdateFromCache(clusterId, hostName);
                org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = alertsDAO.findCurrentCounts(clusterId, null, hostName);
                if (hostUpdateEvent.getAlertsSummary().equals(summary)) {
                    continue;
                }
                hostUpdateEvent.setAlertsSummary(summary);
                STOMPUpdatePublisher.publish(org.apache.ambari.server.events.HostUpdateEvent.createHostAlertsUpdate(hostUpdateEvent.getClusterName(), hostName, summary));
                stackAdvisorHelperProvider.get().clearCaches(hostName);
            }
        }
    }

    private org.apache.ambari.server.events.HostUpdateEvent retrieveHostUpdateFromCache(java.lang.Long clusterId, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.HostUpdateEvent hostUpdateEvent;
        if (hosts.containsKey(clusterId) && hosts.get(clusterId).containsKey(hostName)) {
            hostUpdateEvent = hosts.get(clusterId).get(hostName);
        } else {
            hostUpdateEvent = createHostUpdateEvent(clusterId, hostName);
            if (!hosts.containsKey(clusterId)) {
                hosts.put(clusterId, new java.util.HashMap<>());
            }
            hosts.get(clusterId).put(hostName, hostUpdateEvent);
        }
        return hostUpdateEvent;
    }

    private org.apache.ambari.server.events.HostUpdateEvent createHostUpdateEvent(java.lang.Long clusterId, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = m_clusters.get().getClusterById(clusterId).getClusterName();
        org.apache.ambari.server.state.Host host = m_clusters.get().getHost(hostName);
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = alertsDAO.findCurrentCounts(clusterId, null, hostName);
        return new org.apache.ambari.server.events.HostUpdateEvent(clusterName, hostName, host.getStatus(), host.getState(), host.getLastHeartbeatTime(), host.getMaintenanceState(clusterId), summary);
    }
}