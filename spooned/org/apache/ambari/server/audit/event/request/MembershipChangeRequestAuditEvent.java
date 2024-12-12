package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class MembershipChangeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddUserToGroupRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent, org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder> {
        private java.util.List<java.lang.String> userNameList;

        private java.lang.String groupName;

        public AddUserToGroupRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder.class);
            super.withOperation("Membership change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Group(").append(groupName).append("), Members(");
            if (userNameList.isEmpty()) {
                builder.append("<empty>");
            } else {
                builder.append(org.apache.commons.lang.StringUtils.join(userNameList, ", "));
            }
            builder.append(")");
        }

        public org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder withUserNameList(java.util.List<java.lang.String> users) {
            this.userNameList = users;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder withGroupName(java.lang.String groupName) {
            this.groupName = groupName;
            return this;
        }
    }

    protected MembershipChangeRequestAuditEvent() {
    }

    protected MembershipChangeRequestAuditEvent(org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.AddUserToGroupRequestAuditEventBuilder();
    }
}