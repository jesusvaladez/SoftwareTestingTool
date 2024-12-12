package org.apache.ambari.server.audit.event.kerberos;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class ChangeSecurityStateKerberosAuditEvent extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent {
    public static class ChangeSecurityStateKerberosAuditEventBuilder extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent.AbstractKerberosAuditEventBuilder<org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent, org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder> {
        private java.lang.String service;

        private java.lang.String component;

        private java.lang.String hostName;

        private java.lang.String state;

        private ChangeSecurityStateKerberosAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder.class);
            this.withOperation("Security state change");
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Hostname(").append(hostName).append("), Service(").append(service).append("), Component(").append(component).append("), State(").append(state).append(")");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent(this);
        }

        public org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder withService(java.lang.String service) {
            this.service = service;
            return this;
        }

        public org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder withComponent(java.lang.String component) {
            this.component = component;
            return this;
        }

        public org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder withHostName(java.lang.String hostName) {
            this.hostName = hostName;
            return this;
        }

        public org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder withState(java.lang.String state) {
            this.state = state;
            return this;
        }
    }

    private ChangeSecurityStateKerberosAuditEvent() {
    }

    private ChangeSecurityStateKerberosAuditEvent(org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.kerberos.ChangeSecurityStateKerberosAuditEvent.ChangeSecurityStateKerberosAuditEventBuilder();
    }
}