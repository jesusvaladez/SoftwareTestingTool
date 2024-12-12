package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class StartOperationRequestAuditEvent extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent {
    public static class StartOperationAuditEventBuilder extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent, org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder> {
        private java.lang.String requestId;

        private java.lang.String reasonOfFailure;

        private java.lang.String operation;

        private java.lang.String hostname;

        private StartOperationAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder.class);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Operation(").append(operation);
            if (hostname != null) {
                builder.append("), Host name(").append(hostname);
            }
            builder.append("), RequestId(").append(requestId).append("), Status(").append(reasonOfFailure == null ? "Successfully queued" : "Failed to queue");
            if (reasonOfFailure != null) {
                builder.append("), Reason(").append(reasonOfFailure);
            }
            builder.append(")");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent(this);
        }

        public org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder withRequestId(java.lang.String requestId) {
            this.requestId = requestId;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder withReasonOfFailure(java.lang.String reasonOfFailure) {
            this.reasonOfFailure = reasonOfFailure;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder withOperation(java.lang.String operation) {
            this.operation = operation;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder withHostname(java.lang.String hostname) {
            this.hostname = hostname;
            return this;
        }
    }

    private StartOperationRequestAuditEvent() {
    }

    private StartOperationRequestAuditEvent(org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder();
    }
}