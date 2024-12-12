package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang.StringUtils;
@javax.annotation.concurrent.Immutable
public class ClusterPrivilegeChangeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ClusterPrivilegeChangeRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent, org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder> {
        private java.util.Map<java.lang.String, java.util.List<java.lang.String>> users;

        private java.util.Map<java.lang.String, java.util.List<java.lang.String>> groups;

        private java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles;

        public ClusterPrivilegeChangeRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder.class);
            super.withOperation("Role change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            java.util.SortedSet<java.lang.String> roleSet = new java.util.TreeSet<>();
            roleSet.addAll(users.keySet());
            roleSet.addAll(groups.keySet());
            roleSet.addAll(roles.keySet());
            builder.append(", Roles(");
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

        public org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder withUsers(java.util.Map<java.lang.String, java.util.List<java.lang.String>> users) {
            this.users = users;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder withGroups(java.util.Map<java.lang.String, java.util.List<java.lang.String>> groups) {
            this.groups = groups;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder withRoles(java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles) {
            this.roles = roles;
            return this;
        }
    }

    protected ClusterPrivilegeChangeRequestAuditEvent() {
    }

    protected ClusterPrivilegeChangeRequestAuditEvent(org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.ClusterPrivilegeChangeRequestAuditEventBuilder();
    }
}