package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class UpdateRepositoryRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class UpdateRepositoryRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent, org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder> {
        private java.lang.String repo;

        private java.lang.String stackName;

        private java.lang.String osType;

        private java.lang.String baseUrl;

        private java.lang.String stackVersion;

        public UpdateRepositoryRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder.class);
            super.withOperation("Repository update");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Stack(").append(stackName).append("), Stack version(").append(stackVersion).append("), OS(").append(osType).append("), Repo id(").append(repo).append("), Base URL(").append(baseUrl).append(")");
        }

        public org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder withRepo(java.lang.String repo) {
            this.repo = repo;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder withStackName(java.lang.String stackName) {
            this.stackName = stackName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder withOsType(java.lang.String osType) {
            this.osType = osType;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder withBaseUrl(java.lang.String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder withStackVersion(java.lang.String stackVersion) {
            this.stackVersion = stackVersion;
            return this;
        }
    }

    protected UpdateRepositoryRequestAuditEvent() {
    }

    protected UpdateRepositoryRequestAuditEvent(org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent.UpdateRepositoryRequestAuditEventBuilder();
    }
}