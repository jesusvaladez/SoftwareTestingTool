package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class AddAlertGroupRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddAlertGroupRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder> {
        private java.lang.String name;

        private java.util.List<java.lang.String> definitionIds;

        private java.util.List<java.lang.String> notificationIds;

        public AddAlertGroupRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder.class);
            super.withOperation("Alert group addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Alert group name(").append(name).append("), Definition IDs(").append(org.apache.commons.lang.StringUtils.join(definitionIds, ", ")).append("), Notification IDs(").append(org.apache.commons.lang.StringUtils.join(notificationIds, ", ")).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder withDefinitionIds(java.util.List<java.lang.String> ids) {
            this.definitionIds = ids;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder withNotificationIds(java.util.List<java.lang.String> ids) {
            this.notificationIds = ids;
            return this;
        }
    }

    protected AddAlertGroupRequestAuditEvent() {
    }

    protected AddAlertGroupRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.AddAlertGroupRequestAuditEventBuilder();
    }
}