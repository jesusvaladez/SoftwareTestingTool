package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteServiceRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteServiceRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent.DeleteServiceRequestAuditEventBuilder> {
        private java.lang.String serviceName;

        public DeleteServiceRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent.DeleteServiceRequestAuditEventBuilder.class);
            super.withOperation("Service deletion");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Service(").append(serviceName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent.DeleteServiceRequestAuditEventBuilder withService(java.lang.String service) {
            this.serviceName = service;
            return this;
        }
    }

    protected DeleteServiceRequestAuditEvent() {
    }

    protected DeleteServiceRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent.DeleteServiceRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent.DeleteServiceRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent.DeleteServiceRequestAuditEventBuilder();
    }
}