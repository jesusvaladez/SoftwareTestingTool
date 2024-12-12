package org.apache.ambari.server.audit.event;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class LogoutAuditEvent extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent {
    public static class LogoutAuditEventBuilder extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<org.apache.ambari.server.audit.event.LogoutAuditEvent, org.apache.ambari.server.audit.event.LogoutAuditEvent.LogoutAuditEventBuilder> {
        private LogoutAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.LogoutAuditEvent.LogoutAuditEventBuilder.class);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Operation(Logout), Status(Success)");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.LogoutAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.LogoutAuditEvent(this);
        }
    }

    private LogoutAuditEvent() {
    }

    private LogoutAuditEvent(org.apache.ambari.server.audit.event.LogoutAuditEvent.LogoutAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.LogoutAuditEvent.LogoutAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.LogoutAuditEvent.LogoutAuditEventBuilder();
    }
}