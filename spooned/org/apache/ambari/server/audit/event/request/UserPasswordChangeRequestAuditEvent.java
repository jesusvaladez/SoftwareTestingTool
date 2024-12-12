package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class UserPasswordChangeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class UserPasswordChangeRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent, org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent.UserPasswordChangeRequestAuditEventBuilder> {
        private java.lang.String username;

        public UserPasswordChangeRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent.UserPasswordChangeRequestAuditEventBuilder.class);
            super.withOperation("Password change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Affected username(").append(username).append(")");
        }

        public org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent.UserPasswordChangeRequestAuditEventBuilder withAffectedUsername(java.lang.String username) {
            this.username = username;
            return this;
        }
    }

    protected UserPasswordChangeRequestAuditEvent() {
    }

    protected UserPasswordChangeRequestAuditEvent(org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent.UserPasswordChangeRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent.UserPasswordChangeRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent.UserPasswordChangeRequestAuditEventBuilder();
    }
}