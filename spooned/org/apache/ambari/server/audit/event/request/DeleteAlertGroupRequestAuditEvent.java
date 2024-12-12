package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteAlertGroupRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteAlertGroupRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent.DeleteAlertGroupRequestAuditEventBuilder> {
        private java.lang.String id;

        public DeleteAlertGroupRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent.DeleteAlertGroupRequestAuditEventBuilder.class);
            super.withOperation("Alert group removal");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Alert group ID(").append(id).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent.DeleteAlertGroupRequestAuditEventBuilder withId(java.lang.String id) {
            this.id = id;
            return this;
        }
    }

    protected DeleteAlertGroupRequestAuditEvent() {
    }

    protected DeleteAlertGroupRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent.DeleteAlertGroupRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent.DeleteAlertGroupRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent.DeleteAlertGroupRequestAuditEventBuilder();
    }
}