package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class UpdateUpgradeItemRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class UpdateUpgradeItemRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent, org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder> {
        private java.lang.String stageId;

        private java.lang.String status;

        private java.lang.String requestId;

        public UpdateUpgradeItemRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder.class);
            super.withOperation("Action confirmation by the user");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Stage id(").append(stageId).append("), Status(").append(status).append("), Request id(").append(requestId).append(")");
        }

        public org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder withStageId(java.lang.String stageId) {
            this.stageId = stageId;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder withStatus(java.lang.String status) {
            this.status = status;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder withRequestId(java.lang.String requestId) {
            this.requestId = requestId;
            return this;
        }
    }

    protected UpdateUpgradeItemRequestAuditEvent() {
    }

    protected UpdateUpgradeItemRequestAuditEvent(org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent.UpdateUpgradeItemRequestAuditEventBuilder();
    }
}