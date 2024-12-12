package org.apache.ambari.server.agent.stomp;
public class StompResponse {
    @com.fasterxml.jackson.annotation.JsonProperty("status")
    private org.apache.ambari.server.agent.stomp.StompResponse.StompResponseStatus status = org.apache.ambari.server.agent.stomp.StompResponse.StompResponseStatus.OK;

    public org.apache.ambari.server.agent.stomp.StompResponse.StompResponseStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.agent.stomp.StompResponse.StompResponseStatus status) {
        this.status = status;
    }

    public enum StompResponseStatus {

        OK,
        FAILED;}
}