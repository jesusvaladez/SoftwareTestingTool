package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class AddAlertTargetRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddAlertTargetRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder> {
        private java.lang.String name;

        private java.lang.String description;

        private java.lang.String notificationType;

        private java.util.List<java.lang.String> groupIds;

        private java.lang.String emailFrom;

        private java.util.List<java.lang.String> emailRecipients;

        private java.util.List<java.lang.String> alertStates;

        public AddAlertTargetRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder.class);
            super.withOperation("Notification addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Notification name(").append(name).append("), Description(").append(description).append("), Notification type(").append(notificationType).append("), Group IDs(").append(org.apache.commons.lang.StringUtils.join(groupIds, ", "));
            if (emailFrom != null) {
                builder.append("), Email from(").append(emailFrom);
            }
            if ((emailRecipients != null) && (!emailRecipients.isEmpty())) {
                builder.append("), Email to(").append(org.apache.commons.lang.StringUtils.join(emailRecipients, ", "));
            }
            builder.append("), Alert states(").append(org.apache.commons.lang.StringUtils.join(alertStates, ", ")).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder withDescription(java.lang.String description) {
            this.description = description;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder withNotificationType(java.lang.String notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder withGroupIds(java.util.List<java.lang.String> groupIds) {
            this.groupIds = groupIds;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder withEmailFrom(java.lang.String emailFrom) {
            this.emailFrom = emailFrom;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder withEmailRecipients(java.util.List<java.lang.String> emailRecipients) {
            this.emailRecipients = emailRecipients;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder withAlertStates(java.util.List<java.lang.String> alertStates) {
            this.alertStates = alertStates;
            return this;
        }
    }

    protected AddAlertTargetRequestAuditEvent() {
    }

    protected AddAlertTargetRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.AddAlertTargetRequestAuditEventBuilder();
    }
}