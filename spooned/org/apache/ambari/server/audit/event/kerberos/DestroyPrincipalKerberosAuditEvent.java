package org.apache.ambari.server.audit.event.kerberos;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DestroyPrincipalKerberosAuditEvent extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent {
    public static class DestroyPrincipalKerberosAuditEventBuilder extends org.apache.ambari.server.audit.event.kerberos.AbstractKerberosAuditEvent.AbstractKerberosAuditEventBuilder<org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent, org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.DestroyPrincipalKerberosAuditEventBuilder> {
        private java.lang.String principal;

        private DestroyPrincipalKerberosAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.DestroyPrincipalKerberosAuditEventBuilder.class);
            this.withOperation("Principal removal");
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Principal(").append(principal).append(")");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent(this);
        }

        public org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.DestroyPrincipalKerberosAuditEventBuilder withPrincipal(java.lang.String principal) {
            this.principal = principal;
            return this;
        }
    }

    private DestroyPrincipalKerberosAuditEvent() {
    }

    private DestroyPrincipalKerberosAuditEvent(org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.DestroyPrincipalKerberosAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.DestroyPrincipalKerberosAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.kerberos.DestroyPrincipalKerberosAuditEvent.DestroyPrincipalKerberosAuditEventBuilder();
    }
}