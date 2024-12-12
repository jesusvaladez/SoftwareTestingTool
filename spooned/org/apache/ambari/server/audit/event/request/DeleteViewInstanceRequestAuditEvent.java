package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteViewInstanceRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteViewInstanceRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder> {
        private java.lang.String name;

        private java.lang.String type;

        private java.lang.String version;

        public DeleteViewInstanceRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder.class);
            super.withOperation("View deletion");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Type(").append(type).append("), Version(").append(version).append("), Name(").append(name).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder withType(java.lang.String type) {
            this.type = type;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder withVersion(java.lang.String version) {
            this.version = version;
            return this;
        }
    }

    protected DeleteViewInstanceRequestAuditEvent() {
    }

    protected DeleteViewInstanceRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent.DeleteViewInstanceRequestAuditEventBuilder();
    }
}