package org.apache.ambari.server.audit.event.kerberos;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class CreatePrincipalKerberosAuditEvent extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent {
    public static class CreatePrincipalKerberosAuditEventBuilder extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent.AbstractKerberosAuditEventBuilder<org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent, org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.CreatePrincipalKerberosAuditEventBuilder> {
        private java.lang.String principal;

        private CreatePrincipalKerberosAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.CreatePrincipalKerberosAuditEventBuilder.class);
            this.withOperation("Principal creation");
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Principal(").append(principal).append(")");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent(this);
        }

        public org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.CreatePrincipalKerberosAuditEventBuilder withPrincipal(java.lang.String principal) {
            this.principal = principal;
            return this;
        }
    }

    private CreatePrincipalKerberosAuditEvent() {
    }

    private CreatePrincipalKerberosAuditEvent(org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.CreatePrincipalKerberosAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.CreatePrincipalKerberosAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.kerberos.CreatePrincipalKerberosAuditEvent.CreatePrincipalKerberosAuditEventBuilder();
    }
}