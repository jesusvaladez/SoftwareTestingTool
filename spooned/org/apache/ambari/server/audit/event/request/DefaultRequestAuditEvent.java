package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DefaultRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DefaultRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent, org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent.DefaultRequestAuditEventBuilder> {
        private DefaultRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent.DefaultRequestAuditEventBuilder.class);
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent(this);
        }
    }

    protected DefaultRequestAuditEvent() {
    }

    private DefaultRequestAuditEvent(org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent.DefaultRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent.DefaultRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent.DefaultRequestAuditEventBuilder();
    }
}