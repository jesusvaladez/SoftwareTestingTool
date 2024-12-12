package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class ClientConfigDownloadRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ClientConfigDownloadRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent, org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent.ClientConfigDownloadRequestAuditEventBuilder> {
        private java.lang.String service;

        private java.lang.String component;

        public ClientConfigDownloadRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent.ClientConfigDownloadRequestAuditEventBuilder.class);
            super.withOperation("Client config download");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Service(").append(service).append("), Component(").append(component).append(")");
        }

        public org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent.ClientConfigDownloadRequestAuditEventBuilder withService(java.lang.String service) {
            this.service = service;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent.ClientConfigDownloadRequestAuditEventBuilder withComponent(java.lang.String component) {
            this.component = component;
            return this;
        }
    }

    protected ClientConfigDownloadRequestAuditEvent() {
    }

    protected ClientConfigDownloadRequestAuditEvent(org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent.ClientConfigDownloadRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent.ClientConfigDownloadRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ClientConfigDownloadRequestAuditEvent.ClientConfigDownloadRequestAuditEventBuilder();
    }
}