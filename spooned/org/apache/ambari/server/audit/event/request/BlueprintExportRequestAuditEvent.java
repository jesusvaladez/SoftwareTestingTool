package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class BlueprintExportRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class BlueprintExportRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent, org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent.BlueprintExportRequestAuditEventBuilder> {
        public BlueprintExportRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent.BlueprintExportRequestAuditEventBuilder.class);
            super.withOperation("Blueprint export");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
        }
    }

    protected BlueprintExportRequestAuditEvent() {
    }

    protected BlueprintExportRequestAuditEvent(org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent.BlueprintExportRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent.BlueprintExportRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent.BlueprintExportRequestAuditEventBuilder();
    }
}