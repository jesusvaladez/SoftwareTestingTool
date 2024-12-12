package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteGroupRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteGroupRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent.DeleteGroupRequestAuditEventBuilder> {
        private java.lang.String groupName;

        public DeleteGroupRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent.DeleteGroupRequestAuditEventBuilder.class);
            super.withOperation("Group delete");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Group(").append(groupName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent.DeleteGroupRequestAuditEventBuilder withGroupName(java.lang.String groupName) {
            this.groupName = groupName;
            return this;
        }
    }

    protected DeleteGroupRequestAuditEvent() {
    }

    protected DeleteGroupRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent.DeleteGroupRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent.DeleteGroupRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteGroupRequestAuditEvent.DeleteGroupRequestAuditEventBuilder();
    }
}