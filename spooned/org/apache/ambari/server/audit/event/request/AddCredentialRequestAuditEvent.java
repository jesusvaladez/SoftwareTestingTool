package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddCredentialRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddCredentialAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder> {
        private java.lang.String type;

        private java.lang.String clusterName;

        private java.lang.String principal;

        private java.lang.String alias;

        public AddCredentialAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder.class);
            super.withOperation("Credential addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Type(").append(type).append("), Principal(").append(principal).append("), Alias(").append(alias).append("), Cluster name(").append(clusterName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder withType(java.lang.String type) {
            this.type = type;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder withClusterName(java.lang.String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder withPrincipal(java.lang.String principal) {
            this.principal = principal;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder withAlias(java.lang.String alias) {
            this.alias = alias;
            return this;
        }
    }

    protected AddCredentialRequestAuditEvent() {
    }

    protected AddCredentialRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.AddCredentialAuditEventBuilder();
    }
}