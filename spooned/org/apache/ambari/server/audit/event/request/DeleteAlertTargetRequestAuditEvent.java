package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteAlertTargetRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteAlertTargetRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent.DeleteAlertTargetRequestAuditEventBuilder> {
        private java.lang.String id;

        public DeleteAlertTargetRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent.DeleteAlertTargetRequestAuditEventBuilder.class);
            super.withOperation("Notification removal");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Notification ID(").append(id).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent.DeleteAlertTargetRequestAuditEventBuilder withId(java.lang.String id) {
            this.id = id;
            return this;
        }
    }

    protected DeleteAlertTargetRequestAuditEvent() {
    }

    protected DeleteAlertTargetRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent.DeleteAlertTargetRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent.DeleteAlertTargetRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent.DeleteAlertTargetRequestAuditEventBuilder();
    }
}