package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddBlueprintRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddBlueprintRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent.AddBlueprintRequestAuditEventBuilder> {
        private java.lang.String blueprintName;

        public AddBlueprintRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent.AddBlueprintRequestAuditEventBuilder.class);
            super.withOperation("Upload blueprint");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Blueprint name(").append(blueprintName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent.AddBlueprintRequestAuditEventBuilder withBlueprintName(java.lang.String blueprintName) {
            this.blueprintName = blueprintName;
            return this;
        }
    }

    protected AddBlueprintRequestAuditEvent() {
    }

    protected AddBlueprintRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent.AddBlueprintRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent.AddBlueprintRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddBlueprintRequestAuditEvent.AddBlueprintRequestAuditEventBuilder();
    }
}