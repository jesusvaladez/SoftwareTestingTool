package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class ConfigurationChangeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ConfigurationChangeRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent, org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.ConfigurationChangeRequestAuditEventBuilder> {
        private java.lang.String versionNumber;

        private java.lang.String versionNote;

        public ConfigurationChangeRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.ConfigurationChangeRequestAuditEventBuilder.class);
            super.withOperation("Configuration change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", VersionNumber(V").append(versionNumber).append("), ").append("VersionNote(").append(versionNote).append(")");
        }

        public org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.ConfigurationChangeRequestAuditEventBuilder withVersionNumber(java.lang.String versionNumber) {
            this.versionNumber = versionNumber;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.ConfigurationChangeRequestAuditEventBuilder withVersionNote(java.lang.String versionNote) {
            this.versionNote = versionNote;
            return this;
        }
    }

    protected ConfigurationChangeRequestAuditEvent() {
    }

    protected ConfigurationChangeRequestAuditEvent(org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.ConfigurationChangeRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.ConfigurationChangeRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.ConfigurationChangeRequestAuditEventBuilder();
    }
}