package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class ChangeViewInstanceRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ChangeViewInstanceRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent, org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder> {
        private java.lang.String description;

        private java.lang.String name;

        private java.lang.String type;

        private java.lang.String displayName;

        private java.lang.String version;

        public ChangeViewInstanceRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder.class);
            super.withOperation("View change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Type(").append(type).append("), Version(").append(version).append("), Name(").append(name).append("), Display name(").append(displayName).append("), Description(").append(description).append(")");
        }

        public org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder withDescription(java.lang.String description) {
            this.description = description;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder withType(java.lang.String type) {
            this.type = type;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder withDisplayName(java.lang.String displayName) {
            this.displayName = displayName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder withVersion(java.lang.String version) {
            this.version = version;
            return this;
        }
    }

    protected ChangeViewInstanceRequestAuditEvent() {
    }

    protected ChangeViewInstanceRequestAuditEvent(org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent.ChangeViewInstanceRequestAuditEventBuilder();
    }
}