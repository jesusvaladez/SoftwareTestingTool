package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteHostRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteHostRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent.DeleteHostRequestAuditEventBuilder> {
        private java.lang.String hostName;

        public DeleteHostRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent.DeleteHostRequestAuditEventBuilder.class);
            super.withOperation("Host deletion");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Hostname(").append(hostName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent.DeleteHostRequestAuditEventBuilder withHostName(java.lang.String groupName) {
            this.hostName = groupName;
            return this;
        }
    }

    protected DeleteHostRequestAuditEvent() {
    }

    protected DeleteHostRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent.DeleteHostRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent.DeleteHostRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent.DeleteHostRequestAuditEventBuilder();
    }
}