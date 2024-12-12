package org.apache.ambari.server.audit.event;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class LoginAuditEvent extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent {
    public static class LoginAuditEventBuilder extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<org.apache.ambari.server.audit.event.LoginAuditEvent, org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder> {
        private LoginAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder.class);
        }

        private java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles;

        private java.lang.String reasonOfFailure;

        private java.lang.Integer consecutiveFailures;

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Operation(User login), Roles(");
            if ((roles != null) && (!roles.isEmpty())) {
                java.util.List<java.lang.String> lines = new java.util.LinkedList<>();
                for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : roles.entrySet()) {
                    lines.add((entry.getKey() + ": ") + org.apache.commons.lang.StringUtils.join(entry.getValue(), ", "));
                }
                builder.append(org.apache.commons.lang.StringUtils.join(lines, ","));
            }
            builder.append("), Status(").append(reasonOfFailure == null ? "Success" : "Failed");
            if (reasonOfFailure != null) {
                builder.append("), Reason(").append(reasonOfFailure);
                builder.append("), Consecutive failures(").append(consecutiveFailures == null ? "UNKNOWN USER" : java.lang.String.valueOf(consecutiveFailures));
            }
            builder.append(")");
        }

        public org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder withRoles(java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles) {
            this.roles = roles;
            return this;
        }

        public org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder withReasonOfFailure(java.lang.String reasonOfFailure) {
            this.reasonOfFailure = reasonOfFailure;
            return this;
        }

        public org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder withConsecutiveFailures(java.lang.Integer consecutiveFailures) {
            this.consecutiveFailures = consecutiveFailures;
            return this;
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.LoginAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.LoginAuditEvent(this);
        }
    }

    private LoginAuditEvent() {
    }

    private LoginAuditEvent(org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.LoginAuditEvent.LoginAuditEventBuilder();
    }
}