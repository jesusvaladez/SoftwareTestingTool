package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddRequestRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.AddRequestAuditEventBuilder> {
        private java.lang.String command;

        private java.lang.String clusterName;

        public AddRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.AddRequestAuditEventBuilder.class);
            super.withOperation("Request from server");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Command(").append(command).append("), Cluster name(").append(clusterName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.AddRequestAuditEventBuilder withClusterName(java.lang.String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.AddRequestAuditEventBuilder withCommand(java.lang.String command) {
            this.command = command;
            return this;
        }
    }

    protected AddRequestRequestAuditEvent() {
    }

    protected AddRequestRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.AddRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.AddRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.AddRequestAuditEventBuilder();
    }
}