package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class CreateUserRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class CreateUserRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent, org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder> {
        private boolean admin;

        private boolean active;

        private java.lang.String username;

        public CreateUserRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder.class);
            super.withOperation("User creation");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Created Username(").append(username).append("), Active(").append(active ? "yes" : "no").append("), ").append("Administrator(").append(admin ? "yes" : "no").append(")");
        }

        public org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder withAdmin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder withCreatedUsername(java.lang.String username) {
            this.username = username;
            return this;
        }
    }

    protected CreateUserRequestAuditEvent() {
    }

    protected CreateUserRequestAuditEvent(org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.CreateUserRequestAuditEventBuilder();
    }
}