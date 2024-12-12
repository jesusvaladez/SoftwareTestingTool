package org.apache.ambari.server.audit.event;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class TaskStatusAuditEvent extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent {
    public static class TaskStatusAuditEventBuilder extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<org.apache.ambari.server.audit.event.TaskStatusAuditEvent, org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder> {
        private java.lang.String requestId;

        private java.lang.String taskId;

        private java.lang.String hostName;

        private java.lang.String status;

        private java.lang.String operation;

        private java.lang.String details;

        private TaskStatusAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder.class);
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.TaskStatusAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.TaskStatusAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Operation(").append(this.operation);
            if (details != null) {
                builder.append("), Details(").append(this.details);
            }
            builder.append("), Status(").append(this.status).append("), RequestId(").append(this.requestId).append("), TaskId(").append(this.taskId).append("), Hostname(").append(this.hostName).append(")");
        }

        public org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder withStatus(java.lang.String status) {
            this.status = status;
            return this;
        }

        public org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder withRequestId(java.lang.String requestId) {
            this.requestId = requestId;
            return this;
        }

        public org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder withTaskId(java.lang.String taskId) {
            this.taskId = taskId;
            return this;
        }

        public org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder withHostName(java.lang.String hostName) {
            this.hostName = hostName;
            return this;
        }

        public org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder withOperation(java.lang.String operation) {
            this.operation = operation;
            return this;
        }

        public org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder withDetails(java.lang.String details) {
            this.details = details;
            return this;
        }
    }

    private TaskStatusAuditEvent() {
    }

    private TaskStatusAuditEvent(org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.TaskStatusAuditEvent.TaskStatusAuditEventBuilder();
    }
}