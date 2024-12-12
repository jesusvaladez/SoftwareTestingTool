package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddRepositoryRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddRepositoryRequestAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder> {
        private java.lang.String repo;

        private java.lang.String stackName;

        private java.lang.String osType;

        private java.lang.String baseUrl;

        private java.lang.String stackVersion;

        public AddRepositoryRequestAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder.class);
            super.withOperation("Repository addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Stack(").append(stackName).append("), Stack version(").append(stackVersion).append("), OS(").append(osType).append("), Repo id(").append(repo).append("), Base URL(").append(baseUrl).append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder withRepo(java.lang.String repo) {
            this.repo = repo;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder withStackName(java.lang.String stackName) {
            this.stackName = stackName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder withOsType(java.lang.String osType) {
            this.osType = osType;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder withBaseUrl(java.lang.String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder withStackVersion(java.lang.String stackVersion) {
            this.stackVersion = stackVersion;
            return this;
        }
    }

    protected AddRepositoryRequestAuditEvent() {
    }

    protected AddRepositoryRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent.AddRepositoryRequestAuditEventBuilder();
    }
}