package org.apache.ambari.server.audit.event.kerberos;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class CreateKeyTabKerberosAuditEvent extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent {
    public static class CreateKeyTabKerberosAuditEventBuilder extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent.AbstractKerberosAuditEventBuilder<org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent, org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder> {
        private java.lang.String keyTabFilePath;

        private java.lang.String hostName;

        private java.lang.String principal;

        private CreateKeyTabKerberosAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder.class);
            this.withOperation("Keytab file creation");
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Principal(").append(principal).append("), Hostname(").append(hostName).append("), Keytab file(").append(keyTabFilePath).append(")");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent(this);
        }

        public org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder withKeyTabFilePath(java.lang.String keyTabFilePath) {
            this.keyTabFilePath = keyTabFilePath;
            return this;
        }

        public org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder withHostName(java.lang.String hostName) {
            this.hostName = hostName;
            return this;
        }

        public org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder withPrincipal(java.lang.String principal) {
            this.principal = principal;
            return this;
        }

        public boolean hasPrincipal() {
            return principal != null;
        }
    }

    private CreateKeyTabKerberosAuditEvent() {
    }

    private CreateKeyTabKerberosAuditEvent(org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.kerberos.CreateKeyTabKerberosAuditEvent.CreateKeyTabKerberosAuditEventBuilder();
    }
}