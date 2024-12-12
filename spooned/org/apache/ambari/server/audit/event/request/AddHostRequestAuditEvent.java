package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddHostRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddHostRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent.AddHostRequestAuditEventBuilder> {
        private java.lang.String hostName;

        public AddHostRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent.AddHostRequestAuditEventBuilder.class);
            super.withOperation("Host addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Hostname(").append(hostName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent.AddHostRequestAuditEventBuilder withHostName(java.lang.String hostName) {
            this.hostName = hostName;
            return this;
        }
    }

    protected AddHostRequestAuditEvent() {
    }

    protected AddHostRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent.AddHostRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent.AddHostRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent.AddHostRequestAuditEventBuilder();
    }
}