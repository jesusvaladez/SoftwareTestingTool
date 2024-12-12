package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class PrivilegeChangeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class PrivilegeChangeRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent, org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder> {
        private java.lang.String user;

        private java.lang.String group;

        private java.lang.String role;

        public PrivilegeChangeRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder.class);
            super.withOperation("Role change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Role(").append(role).append(")");
            if (user != null) {
                builder.append(", User(").append(user).append(")");
            }
            if (group != null) {
                builder.append(", Group(").append(group).append(")");
            }
        }

        public org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder withUser(java.lang.String user) {
            this.user = user;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder withGroup(java.lang.String group) {
            this.group = group;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder withRole(java.lang.String role) {
            this.role = role;
            return this;
        }
    }

    protected PrivilegeChangeRequestAuditEvent() {
    }

    protected PrivilegeChangeRequestAuditEvent(org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.PrivilegeChangeRequestAuditEventBuilder();
    }
}