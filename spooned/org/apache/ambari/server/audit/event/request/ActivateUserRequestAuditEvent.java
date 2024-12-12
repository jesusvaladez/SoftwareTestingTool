package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class ActivateUserRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ActivateUserRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent, org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.ActivateUserRequestAuditEventBuilder> {
        private boolean active;

        private java.lang.String username;

        public ActivateUserRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.ActivateUserRequestAuditEventBuilder.class);
            super.withOperation("Set user active/inactive");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Affected username(").append(username).append("), ").append("Active(").append(active ? "yes" : "no").append(")");
        }

        public org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.ActivateUserRequestAuditEventBuilder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.ActivateUserRequestAuditEventBuilder withAffectedUsername(java.lang.String username) {
            this.username = username;
            return this;
        }
    }

    protected ActivateUserRequestAuditEvent() {
    }

    protected ActivateUserRequestAuditEvent(org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.ActivateUserRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.ActivateUserRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.ActivateUserRequestAuditEventBuilder();
    }
}