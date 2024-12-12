package org.apache.ambari.server.events.listeners.services;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class ServiceUpdateListener {
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    public ServiceUpdateListener(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher, org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher) {
        STOMPUpdatePublisher.registerAPI(this);
        ambariEventPublisher.register(this);
        this.STOMPUpdatePublisher = STOMPUpdatePublisher;
    }

    @com.google.common.eventbus.Subscribe
    public void onHostComponentUpdate(org.apache.ambari.server.events.HostComponentsUpdateEvent event) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, java.util.Set<java.lang.String>> clustersServices = new java.util.HashMap<>();
        for (org.apache.ambari.server.events.HostComponentUpdate hostComponentUpdate : event.getHostComponentUpdates()) {
            java.lang.Long clusterId = hostComponentUpdate.getClusterId();
            java.lang.String serviceName = hostComponentUpdate.getServiceName();
            clustersServices.computeIfAbsent(clusterId, c -> new java.util.HashSet<>()).add(serviceName);
        }
        for (java.util.Map.Entry<java.lang.Long, java.util.Set<java.lang.String>> clusterServices : clustersServices.entrySet()) {
            java.lang.Long clusterId = clusterServices.getKey();
            java.lang.String clusterName = m_clusters.get().getClusterById(clusterId).getClusterName();
            for (java.lang.String serviceName : clusterServices.getValue()) {
                STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.ServiceUpdateEvent(clusterName, null, serviceName, null, true));
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onMaintenanceStateUpdate(org.apache.ambari.server.events.MaintenanceModeEvent event) throws org.apache.ambari.server.AmbariException {
        if (event.getService() == null) {
            return;
        }
        java.lang.Long clusterId = event.getClusterId();
        java.lang.String clusterName = m_clusters.get().getClusterById(clusterId).getClusterName();
        java.lang.String serviceName = event.getService().getName();
        org.apache.ambari.server.state.MaintenanceState maintenanceState = event.getMaintenanceState();
        STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.ServiceUpdateEvent(clusterName, maintenanceState, serviceName, null, false));
    }
}