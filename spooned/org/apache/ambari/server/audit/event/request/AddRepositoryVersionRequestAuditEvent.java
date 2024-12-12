package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AddRepositoryVersionRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class AddRepositoryVersionAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent, org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder> {
        private java.lang.String stackName;

        private java.lang.String displayName;

        private java.lang.String stackVersion;

        private java.lang.String repoVersion;

        private java.util.Map<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> repos;

        public AddRepositoryVersionAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder.class);
            super.withOperation("Repository version addition");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent(this);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Stack(").append(stackName).append("), Stack version(").append(stackVersion).append("), Display name(").append(displayName).append("), Repo version(").append(repoVersion).append("), Repositories(");
            for (java.util.Map.Entry<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> repo : repos.entrySet()) {
                builder.append("Operating system: ").append(repo.getKey());
                builder.append("(");
                for (java.util.Map<java.lang.String, java.lang.String> properties : repo.getValue()) {
                    builder.append("Repository ID(").append(properties.get("repo_id"));
                    builder.append("), Repository name(").append(properties.get("repo_name"));
                    builder.append("), Base url(").append(properties.get("base_url")).append(")");
                    builder.append(")");
                }
            }
            builder.append(")");
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder withStackName(java.lang.String stackName) {
            this.stackName = stackName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder withDisplayName(java.lang.String displayName) {
            this.displayName = displayName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder withStackVersion(java.lang.String stackVersion) {
            this.stackVersion = stackVersion;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder withRepoVersion(java.lang.String repoVersion) {
            this.repoVersion = repoVersion;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder withRepos(java.util.Map<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> repos) {
            this.repos = repos;
            return this;
        }
    }

    protected AddRepositoryVersionRequestAuditEvent() {
    }

    protected AddRepositoryVersionRequestAuditEvent(org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.AddRepositoryVersionAuditEventBuilder();
    }
}