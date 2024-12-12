package org.apache.ambari.server.audit.request;
public abstract class AbstractBaseCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    public java.lang.String getPrefix() {
        return this.getClass().getName();
    }

    @java.lang.Override
    public org.apache.ambari.server.audit.event.AuditEvent createAuditEvent(final org.apache.ambari.server.api.services.Request request, final org.apache.ambari.server.api.services.Result result) {
        return new org.apache.ambari.server.audit.event.AuditEvent() {
            @java.lang.Override
            public java.lang.Long getTimestamp() {
                return java.lang.System.currentTimeMillis();
            }

            @java.lang.Override
            public java.lang.String getAuditMessage() {
                return (getPrefix() + " ") + java.lang.String.format("%s %s %s %s %s", request.getRequestType(), request.getURI(), result.getStatus().getStatusCode(), result.getStatus().getStatus(), result.getStatus().getMessage());
            }
        };
    }
}