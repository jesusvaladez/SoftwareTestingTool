package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddViewInstanceRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddViewInstanceRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder> {
        private java.lang.String description;

        private java.lang.String name;

        private java.lang.String type;

        private java.lang.String displayName;

        private java.lang.String version;

        public AddViewInstanceRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder.class);
            super.withOperation("View addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Type(").append(type).append("), Version(").append(version).append("), Name(").append(name).append("), Display name(").append(displayName).append("), Description(").append(description).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder withDescription(java.lang.String description) {
            this.description = description;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder withType(java.lang.String type) {
            this.type = type;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder withDisplayName(java.lang.String displayName) {
            this.displayName = displayName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder withVersion(java.lang.String version) {
            this.version = version;
            return this;
        }
    }

    protected AddViewInstanceRequestAuditEvent() {
    }

    protected AddViewInstanceRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent.AddViewInstanceRequestAuditEventBuilder();
    }
}