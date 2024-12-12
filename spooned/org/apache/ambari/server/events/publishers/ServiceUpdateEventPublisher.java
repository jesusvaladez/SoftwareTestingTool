package org.apache.ambari.server.events.publishers;
@org.apache.ambari.server.EagerSingleton
public class ServiceUpdateEventPublisher extends org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher<org.apache.ambari.server.events.ServiceUpdateEvent> {
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.State>> states = new java.util.HashMap<>();

    @com.google.inject.Inject
    public ServiceUpdateEventPublisher(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher) {
        super(stompUpdatePublisher);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.STOMPEvent.Type getType() {
        return org.apache.ambari.server.events.STOMPEvent.Type.SERVICE;
    }

    @java.lang.Override
    public void mergeBufferAndPost(java.util.List<org.apache.ambari.server.events.ServiceUpdateEvent> events, com.google.common.eventbus.EventBus eventBus) {
        java.util.List<org.apache.ambari.server.events.ServiceUpdateEvent> filtered = new java.util.ArrayList<>();
        for (org.apache.ambari.server.events.ServiceUpdateEvent event : events) {
            int pos = filtered.indexOf(event);
            if (pos != (-1)) {
                if (event.isStateChanged()) {
                    filtered.get(pos).setStateChanged(true);
                }
                if (event.getMaintenanceState() != null) {
                    filtered.get(pos).setMaintenanceState(event.getMaintenanceState());
                }
            } else {
                filtered.add(event);
            }
        }
        for (org.apache.ambari.server.events.ServiceUpdateEvent serviceUpdateEvent : filtered) {
            if (serviceUpdateEvent.isStateChanged()) {
                org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState serviceCalculatedState = org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.getServiceStateProvider(serviceUpdateEvent.getServiceName());
                org.apache.ambari.server.state.State serviceState = serviceCalculatedState.getState(serviceUpdateEvent.getClusterName(), serviceUpdateEvent.getServiceName());
                java.lang.String serviceName = serviceUpdateEvent.getServiceName();
                java.lang.String clusterName = serviceUpdateEvent.getClusterName();
                if (((states.containsKey(clusterName) && states.get(clusterName).containsKey(serviceName)) && states.get(clusterName).get(serviceName).equals(serviceState)) && (serviceUpdateEvent.getMaintenanceState() == null)) {
                    continue;
                }
                states.computeIfAbsent(clusterName, c -> new java.util.HashMap<>()).put(serviceName, serviceState);
                serviceUpdateEvent.setState(serviceState);
            }
            eventBus.post(serviceUpdateEvent);
        }
    }
}