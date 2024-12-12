package org.apache.ambari.server.audit.event.request;
import org.apache.commons.lang.StringUtils;
public class ChangeAlertTargetRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ChangeAlertTargetRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent, org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder> {
        private java.lang.String name;

        private java.lang.String description;

        private java.lang.String notificationType;

        private java.util.List<java.lang.String> groupIds;

        private java.lang.String emailFrom;

        private java.util.List<java.lang.String> emailRecipients;

        private java.util.List<java.lang.String> alertStates;

        public ChangeAlertTargetRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder.class);
            super.withOperation("Notification change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent(this);
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

        public org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder withDescription(java.lang.String description) {
            this.description = description;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder withNotificationType(java.lang.String notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder withGroupIds(java.util.List<java.lang.String> groupIds) {
            this.groupIds = groupIds;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder withEmailFrom(java.lang.String emailFrom) {
            this.emailFrom = emailFrom;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder withEmailRecipients(java.util.List<java.lang.String> emailRecipients) {
            this.emailRecipients = emailRecipients;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder withAlertStates(java.util.List<java.lang.String> alertStates) {
            this.alertStates = alertStates;
            return this;
        }
    }

    protected ChangeAlertTargetRequestAuditEvent() {
    }

    protected ChangeAlertTargetRequestAuditEvent(org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.ChangeAlertTargetRequestAuditEventBuilder();
    }
}