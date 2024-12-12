package org.apache.ambari.server.events.publishers;
@org.apache.ambari.server.EagerSingleton
public class RequestUpdateEventPublisher extends org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher<org.apache.ambari.server.events.RequestUpdateEvent> {
    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.TopologyManager topologyManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    public RequestUpdateEventPublisher(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher) {
        super(stompUpdatePublisher);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.STOMPEvent.Type getType() {
        return org.apache.ambari.server.events.STOMPEvent.Type.REQUEST;
    }

    @java.lang.Override
    public void mergeBufferAndPost(java.util.List<org.apache.ambari.server.events.RequestUpdateEvent> events, com.google.common.eventbus.EventBus m_eventBus) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.events.RequestUpdateEvent> filteredRequests = new java.util.HashMap<>();
        for (org.apache.ambari.server.events.RequestUpdateEvent event : events) {
            java.lang.Long requestId = event.getRequestId();
            if (filteredRequests.containsKey(requestId)) {
                org.apache.ambari.server.events.RequestUpdateEvent filteredRequest = filteredRequests.get(requestId);
                filteredRequest.setEndTime(event.getEndTime());
                filteredRequest.setRequestStatus(event.getRequestStatus());
                filteredRequest.setRequestContext(event.getRequestContext());
                filteredRequest.getHostRoleCommands().removeAll(event.getHostRoleCommands());
                filteredRequest.getHostRoleCommands().addAll(event.getHostRoleCommands());
            } else {
                filteredRequests.put(requestId, event);
            }
        }
        for (org.apache.ambari.server.events.RequestUpdateEvent requestUpdateEvent : filteredRequests.values()) {
            org.apache.ambari.server.events.RequestUpdateEvent filled = fillRequest(requestUpdateEvent);
            m_eventBus.post(filled);
        }
    }

    private org.apache.ambari.server.events.RequestUpdateEvent fillRequest(org.apache.ambari.server.events.RequestUpdateEvent event) {
        event.setProgressPercent(org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromRequest(hostRoleCommandDAO, topologyManager, event.getRequestId()).getPercent());
        if ((((event.getEndTime() == null) || (event.getStartTime() == null)) || (event.getClusterName() == null)) || (event.getRequestContext() == null)) {
            org.apache.ambari.server.orm.entities.RequestEntity requestEntity = requestDAO.findByPK(event.getRequestId());
            event.setStartTime(requestEntity.getStartTime());
            event.setUserName(requestEntity.getUserName());
            event.setEndTime(requestEntity.getEndTime());
            if (requestEntity.getClusterId() != (-1)) {
                event.setClusterName(clusterDAO.findById(requestEntity.getClusterId()).getClusterName());
            }
            event.setRequestContext(requestEntity.getRequestContext());
            event.setRequestStatus(requestEntity.getStatus());
        }
        return event;
    }
}