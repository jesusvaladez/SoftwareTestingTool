package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class DeleteRepositoryVersionRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class DeleteRepositoryVersionAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent, org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder> {
        private java.lang.String stackName;

        private java.lang.String stackVersion;

        private java.lang.String repoVersion;

        public DeleteRepositoryVersionAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder.class);
            super.withOperation("Repository version removal");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Stack(").append(stackName).append("), Stack version(").append(stackVersion).append("), Repo version ID(").append(repoVersion).append(")");
        }

        public org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder withStackName(java.lang.String stackName) {
            this.stackName = stackName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder withStackVersion(java.lang.String stackVersion) {
            this.stackVersion = stackVersion;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder withRepoVersion(java.lang.String repoVersion) {
            this.repoVersion = repoVersion;
            return this;
        }
    }

    protected DeleteRepositoryVersionRequestAuditEvent() {
    }

    protected DeleteRepositoryVersionRequestAuditEvent(org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.DeleteRepositoryVersionAuditEventBuilder();
    }
}