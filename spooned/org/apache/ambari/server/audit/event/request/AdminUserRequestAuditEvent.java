package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AdminUserRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AdminUserRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent, org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.AdminUserRequestAuditEventBuilder> {
        private boolean admin;

        private java.lang.String username;

        public AdminUserRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.AdminUserRequestAuditEventBuilder.class);
            super.withOperation("Set user admin");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Affeted username(").append(username).append("), ").append("Administrator(").append(admin ? "yes" : "no").append(")");
        }

        public org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.AdminUserRequestAuditEventBuilder withAdmin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.AdminUserRequestAuditEventBuilder withAffectedUsername(java.lang.String username) {
            this.username = username;
            return this;
        }
    }

    protected AdminUserRequestAuditEvent() {
    }

    protected AdminUserRequestAuditEvent(org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.AdminUserRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.AdminUserRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.AdminUserRequestAuditEventBuilder();
    }
}