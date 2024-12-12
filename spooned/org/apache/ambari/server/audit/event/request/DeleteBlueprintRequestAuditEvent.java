package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteBlueprintRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteBlueprintRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent.DeleteBlueprintRequestAuditEventBuilder> {
        private java.lang.String blueprintName;

        public DeleteBlueprintRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent.DeleteBlueprintRequestAuditEventBuilder.class);
            super.withOperation("Delete blueprint");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Blueprint name(").append(blueprintName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent.DeleteBlueprintRequestAuditEventBuilder withBlueprintName(java.lang.String blueprintName) {
            this.blueprintName = blueprintName;
            return this;
        }
    }

    protected DeleteBlueprintRequestAuditEvent() {
    }

    protected DeleteBlueprintRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent.DeleteBlueprintRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent.DeleteBlueprintRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteBlueprintRequestAuditEvent.DeleteBlueprintRequestAuditEventBuilder();
    }
}