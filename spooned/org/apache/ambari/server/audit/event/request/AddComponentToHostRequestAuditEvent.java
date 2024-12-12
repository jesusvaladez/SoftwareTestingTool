package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class AddComponentToHostRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddComponentToHostRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.AddComponentToHostRequestAuditEventBuilder> {
        private java.lang.String hostName;

        private java.util.Set<java.lang.String> components;

        public AddComponentToHostRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.AddComponentToHostRequestAuditEventBuilder.class);
            super.withOperation("Component addition to host");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Hostname(").append(hostName).append("), Component(").append(components == null ? "" : org.apache.commons.lang.StringUtils.join(components, ", ")).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.AddComponentToHostRequestAuditEventBuilder withHostName(java.lang.String hostName) {
            this.hostName = hostName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.AddComponentToHostRequestAuditEventBuilder withComponents(java.util.Set<java.lang.String> component) {
            this.components = component;
            return this;
        }
    }

    protected AddComponentToHostRequestAuditEvent() {
    }

    protected AddComponentToHostRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.AddComponentToHostRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.AddComponentToHostRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.AddComponentToHostRequestAuditEventBuilder();
    }
}