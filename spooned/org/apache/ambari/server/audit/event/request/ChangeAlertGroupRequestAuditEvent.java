package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class ChangeAlertGroupRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ChangeAlertGroupRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent, org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder> {
        private java.lang.String name;

        private java.util.List<java.lang.String> definitionIds;

        private java.util.List<java.lang.String> notificationIds;

        public ChangeAlertGroupRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder.class);
            super.withOperation("Alert group change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Alert group name(").append(name).append("), Definition IDs(").append(org.apache.commons.lang.StringUtils.join(definitionIds, ", ")).append("), Notification IDs(").append(org.apache.commons.lang.StringUtils.join(notificationIds, ", ")).append(")");
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder withDefinitionIds(java.util.List<java.lang.String> ids) {
            this.definitionIds = ids;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder withNotificationIds(java.util.List<java.lang.String> ids) {
            this.notificationIds = ids;
            return this;
        }
    }

    protected ChangeAlertGroupRequestAuditEvent() {
    }

    protected ChangeAlertGroupRequestAuditEvent(org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.ChangeAlertGroupRequestAuditEventBuilder();
    }
}