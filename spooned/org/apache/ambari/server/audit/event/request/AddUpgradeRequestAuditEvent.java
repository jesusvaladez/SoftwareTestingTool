package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddUpgradeRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddUpgradeRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder> {
        private java.lang.String repositoryVersionId;

        private java.lang.String upgradeType;

        private java.lang.String clusterName;

        public AddUpgradeRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder.class);
            super.withOperation("Upgrade addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Repository version ID(").append(repositoryVersionId).append("), Upgrade type(").append(upgradeType).append("), Cluster name(").append(clusterName).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder withRepositoryVersionId(java.lang.String repositoryVersionId) {
            this.repositoryVersionId = repositoryVersionId;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder withUpgradeType(java.lang.String upgradeType) {
            this.upgradeType = upgradeType;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder withClusterName(java.lang.String clusterName) {
            this.clusterName = clusterName;
            return this;
        }
    }

    protected AddUpgradeRequestAuditEvent() {
    }

    protected AddUpgradeRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddUpgradeRequestAuditEvent.AddUpgradeRequestAuditEventBuilder();
    }
}