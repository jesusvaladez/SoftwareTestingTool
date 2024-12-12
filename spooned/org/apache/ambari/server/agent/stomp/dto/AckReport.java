package org.apache.ambari.server.agent.stomp.dto;
public class AckReport {
    @com.fasterxml.jackson.annotation.JsonProperty("status")
    private org.apache.ambari.server.agent.stomp.dto.AckReport.AckStatus status;

    @com.fasterxml.jackson.annotation.JsonProperty("reason")
    private java.lang.String reason;

    @com.fasterxml.jackson.annotation.JsonProperty("messageId")
    private java.lang.Long messageId;

    public AckReport() {
    }

    public org.apache.ambari.server.agent.stomp.dto.AckReport.AckStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.agent.stomp.dto.AckReport.AckStatus status) {
        this.status = status;
    }

    public java.lang.String getReason() {
        return reason;
    }

    public void setReason(java.lang.String reason) {
        this.reason = reason;
    }

    public java.lang.Long getMessageId() {
        return messageId;
    }

    public void setMessageId(java.lang.Long messageId) {
        this.messageId = messageId;
    }

    public enum AckStatus {

        OK,
        ERROR;}
}