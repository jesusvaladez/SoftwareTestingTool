package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteUserRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteUserRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent.DeleteUserRequestAuditEventBuilder> {
        private java.lang.String username;

        public DeleteUserRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent.DeleteUserRequestAuditEventBuilder.class);
            super.withOperation("User delete");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Deleted Username(").append(username).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent.DeleteUserRequestAuditEventBuilder withDeletedUsername(java.lang.String username) {
            this.username = username;
            return this;
        }
    }

    protected DeleteUserRequestAuditEvent() {
    }

    protected DeleteUserRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent.DeleteUserRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent.DeleteUserRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent.DeleteUserRequestAuditEventBuilder();
    }
}