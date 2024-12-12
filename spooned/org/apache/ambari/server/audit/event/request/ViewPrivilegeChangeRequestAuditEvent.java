package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class ViewPrivilegeChangeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ViewPrivilegeChangeRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent, org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder> {
        private java.util.Map<java.lang.String, java.util.List<java.lang.String>> users;

        private java.util.Map<java.lang.String, java.util.List<java.lang.String>> groups;

        private java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles;

        private java.lang.String name;

        private java.lang.String type;

        private java.lang.String version;

        public ViewPrivilegeChangeRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder.class);
            super.withOperation("View permission change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Type(").append(type).append("), Version(").append(version).append("), Name(").append(name).append(")");
            java.util.SortedSet<java.lang.String> roleSet = new java.util.TreeSet<>();
            roleSet.addAll(users.keySet());
            roleSet.addAll(groups.keySet());
            roleSet.addAll(roles.keySet());
            builder.append(", Permissions(");
            java.util.List<java.lang.String> lines = new java.util.LinkedList<>();
            for (java.lang.String role : roleSet) {
                java.util.List<java.lang.String> tmpLines = new java.util.LinkedList<>();
                lines.add(role + ": [");
                if ((users.get(role) != null) && (!users.get(role).isEmpty())) {
                    tmpLines.add("Users: " + org.apache.commons.lang.StringUtils.join(users.get(role), ", "));
                }
                if ((groups.get(role) != null) && (!groups.get(role).isEmpty())) {
                    tmpLines.add("Groups: " + org.apache.commons.lang.StringUtils.join(groups.get(role), ", "));
                }
                if ((roles.get(role) != null) && (!roles.get(role).isEmpty())) {
                    tmpLines.add("Roles: " + org.apache.commons.lang.StringUtils.join(roles.get(role), ", "));
                }
                lines.add(org.apache.commons.lang.StringUtils.join(tmpLines, ";"));
                lines.add("] ");
            }
            builder.append(org.apache.commons.lang.StringUtils.join(lines, ""));
            builder.append(")");
        }

        public org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder withName(java.lang.String name) {
            this.name = name;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder withType(java.lang.String type) {
            this.type = type;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder withVersion(java.lang.String version) {
            this.version = version;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder withUsers(java.util.Map<java.lang.String, java.util.List<java.lang.String>> users) {
            this.users = users;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder withGroups(java.util.Map<java.lang.String, java.util.List<java.lang.String>> groups) {
            this.groups = groups;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder withRoles(java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles) {
            this.roles = roles;
            return this;
        }
    }

    protected ViewPrivilegeChangeRequestAuditEvent() {
    }

    protected ViewPrivilegeChangeRequestAuditEvent(org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.ViewPrivilegeChangeRequestAuditEventBuilder();
    }
}