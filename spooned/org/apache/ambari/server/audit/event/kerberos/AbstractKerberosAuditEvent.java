package org.apache.ambari.server.audit.event.kerberos;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AbstractKerberosAuditEvent extends org.apache.ambari.server.audit.event.AbstractAuditEvent {
    static abstract class AbstractKerberosAuditEventBuilder<T extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent, TBuilder extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent.AbstractKerberosAuditEventBuilder<T, TBuilder>> extends org.apache.ambari.server.audit.event.AbstractAuditEvent.AbstractAuditEventBuilder<T, TBuilder> {
        protected java.lang.String operation;

        protected java.lang.String reasonOfFailure;

        protected java.lang.Long requestId;

        protected java.lang.Long taskId;

        protected AbstractKerberosAuditEventBuilder(java.lang.Class<? extends TBuilder> builderClass) {
            super(builderClass);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            builder.append("Operation(").append(operation);
            builder.append("), Status(").append(reasonOfFailure == null ? "Success" : "Failed");
            if (reasonOfFailure != null) {
                builder.append("), Reason of failure(").append(reasonOfFailure);
            }
            builder.append("), RequestId(").append(requestId).append("), TaskId(").append(taskId).append(")");
        }

        public TBuilder withOperation(java.lang.String operation) {
            this.operation = operation;
            return self();
        }

        public TBuilder withReasonOfFailure(java.lang.String reasonOfFailure) {
            this.reasonOfFailure = reasonOfFailure;
            return self();
        }

        public TBuilder withRequestId(java.lang.Long requestId) {
            this.requestId = requestId;
            return self();
        }

        public TBuilder withTaskId(java.lang.Long taskId) {
            this.taskId = taskId;
            return self();
        }
    }

    protected AbstractKerberosAuditEvent() {
    }

    protected AbstractKerberosAuditEvent(org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent.AbstractKerberosAuditEventBuilder<?, ?> builder) {
        super(builder);
    }
}