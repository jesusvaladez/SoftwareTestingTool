package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class RemoveUserFromGroupRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddUserToGroupRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent, org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder> {
        private java.lang.String groupName;

        private java.lang.String affectedUserName;

        public AddUserToGroupRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder.class);
            super.withOperation("User removal from group");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Group(");
            builder.append(groupName);
            builder.append("), Affected username(");
            builder.append(affectedUserName);
            builder.append(")");
        }

        public org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder withGroupName(java.lang.String groupName) {
            this.groupName = groupName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder withAffectedUserName(java.lang.String userName) {
            this.affectedUserName = userName;
            return this;
        }
    }

    protected RemoveUserFromGroupRequestAuditEvent() {
    }

    protected RemoveUserFromGroupRequestAuditEvent(org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder();
    }
}