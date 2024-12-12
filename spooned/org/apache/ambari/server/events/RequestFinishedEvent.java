package org.apache.ambari.server.events;
public class RequestFinishedEvent extends org.apache.ambari.server.events.ClusterEvent {
    private long requestId;

    public RequestFinishedEvent(long clusterId, long requestId) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.REQUEST_FINISHED, clusterId);
        this.requestId = requestId;
    }

    public long getRequestId() {
        return requestId;
    }
}