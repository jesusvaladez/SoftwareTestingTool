package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class CreateGroupRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class CreateGroupRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent, org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent.CreateGroupRequestAuditEventBuilder> {
        private java.lang.String groupName;

        public CreateGroupRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent.CreateGroupRequestAuditEventBuilder.class);
            super.withOperation("Group creation");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Group(").append(groupName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent.CreateGroupRequestAuditEventBuilder withGroupName(java.lang.String groupName) {
            this.groupName = groupName;
            return this;
        }
    }

    protected CreateGroupRequestAuditEvent() {
    }

    protected CreateGroupRequestAuditEvent(org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent.CreateGroupRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent.CreateGroupRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.CreateGroupRequestAuditEvent.CreateGroupRequestAuditEventBuilder();
    }
}