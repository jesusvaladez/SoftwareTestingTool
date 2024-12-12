package org.apache.ambari.server.audit.event.request;
public class ClusterNameChangeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ClusterNameChangeRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent, org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.ClusterNameChangeRequestAuditEventBuilder> {
        private java.lang.String oldName;

        private java.lang.String newName;

        public ClusterNameChangeRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.ClusterNameChangeRequestAuditEventBuilder.class);
            super.withOperation("Cluster name change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Old name(").append(oldName).append("), New name(").append(newName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.ClusterNameChangeRequestAuditEventBuilder withOldName(java.lang.String oldName) {
            this.oldName = oldName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.ClusterNameChangeRequestAuditEventBuilder withNewName(java.lang.String newName) {
            this.newName = newName;
            return this;
        }
    }

    protected ClusterNameChangeRequestAuditEvent() {
    }

    protected ClusterNameChangeRequestAuditEvent(org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.ClusterNameChangeRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.ClusterNameChangeRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.ClusterNameChangeRequestAuditEventBuilder();
    }
}