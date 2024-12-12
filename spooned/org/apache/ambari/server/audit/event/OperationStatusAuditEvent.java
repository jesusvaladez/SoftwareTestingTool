package org.apache.ambari.server.audit.event;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class OperationStatusAuditEvent extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent {
    public static class OperationStatusAuditEventBuilder extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<org.apache.ambari.server.audit.event.OperationStatusAuditEvent, org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder> {
        private java.lang.String requestId;

        private java.lang.String status;

        private java.lang.String operation;

        private OperationStatusAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder.class);
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.OperationStatusAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.OperationStatusAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Operation(").append(this.operation).append("), Status(").append(this.status).append("), RequestId(").append(this.requestId).append(")");
        }

        public org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder withStatus(java.lang.String status) {
            this.status = status;
            return this;
        }

        public org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder withRequestId(java.lang.String requestId) {
            this.requestId = requestId;
            return this;
        }

        public org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder withRequestContext(java.lang.String operation) {
            this.operation = operation;
            return this;
        }
    }

    private OperationStatusAuditEvent() {
    }

    private OperationStatusAuditEvent(org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.OperationStatusAuditEvent.OperationStatusAuditEventBuilder();
    }
}