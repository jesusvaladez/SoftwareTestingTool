package org.apache.ambari.server.events.listeners.hostcomponents;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class HostComponentsUpdateListener {
    private final org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> m_configHelper;

    @com.google.inject.Inject
    public HostComponentsUpdateListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher) {
        ambariEventPublisher.register(this);
        this.STOMPUpdatePublisher = STOMPUpdatePublisher;
    }

    @com.google.common.eventbus.Subscribe
    public void onMaintenanceStateUpdate(org.apache.ambari.server.events.MaintenanceModeEvent event) {
        if (event.getServiceComponentHost() != null) {
            java.lang.Long clusterId = event.getClusterId();
            org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = event.getServiceComponentHost();
            java.lang.String serviceName = serviceComponentHost.getServiceName();
            java.lang.String componentName = serviceComponentHost.getServiceComponentName();
            java.lang.String hostName = serviceComponentHost.getHostName();
            org.apache.ambari.server.state.MaintenanceState maintenanceState = serviceComponentHost.getMaintenanceState();
            org.apache.ambari.server.events.HostComponentUpdate hostComponentUpdate = org.apache.ambari.server.events.HostComponentUpdate.createHostComponentMaintenanceStatusUpdate(clusterId, serviceName, hostName, componentName, maintenanceState);
            org.apache.ambari.server.events.HostComponentsUpdateEvent hostComponentsUpdateEvent = new org.apache.ambari.server.events.HostComponentsUpdateEvent(java.util.Collections.singletonList(hostComponentUpdate));
            STOMPUpdatePublisher.publish(hostComponentsUpdateEvent);
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onStaleConfigsStateUpdate(org.apache.ambari.server.events.StaleConfigsUpdateEvent staleConfigsUpdateEvent) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = staleConfigsUpdateEvent.getServiceComponentHost();
        if (staleConfigsUpdateEvent.getStaleConfigs() != null) {
            boolean staleConfigs = staleConfigsUpdateEvent.getStaleConfigs();
            if (m_configHelper.get().wasStaleConfigsStatusUpdated(serviceComponentHost.getClusterId(), serviceComponentHost.getHost().getHostId(), serviceComponentHost.getServiceName(), serviceComponentHost.getServiceComponentName(), staleConfigs)) {
                STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.HostComponentsUpdateEvent(java.util.Collections.singletonList(org.apache.ambari.server.events.HostComponentUpdate.createHostComponentStaleConfigsStatusUpdate(serviceComponentHost.getClusterId(), serviceComponentHost.getServiceName(), serviceComponentHost.getHostName(), serviceComponentHost.getServiceComponentName(), staleConfigs))));
            }
        }
    }
}