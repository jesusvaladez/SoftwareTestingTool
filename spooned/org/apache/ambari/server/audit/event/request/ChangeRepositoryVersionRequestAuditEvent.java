package org.apache.ambari.server.audit.event.request;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class ChangeRepositoryVersionRequestAuditEvent extends org.apache.ambari.server.audit.request.RequestAuditEvent {
    public static class ChangeRepositoryVersionAuditEventBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent, org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder> {
        private java.lang.String stackName;

        private java.lang.String displayName;

        private java.lang.String stackVersion;

        private java.lang.String repoVersion;

        private java.util.SortedMap<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> repos;

        public ChangeRepositoryVersionAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder.class);
            super.withOperation("Repository version change");
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent(this);
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

        public org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder withStackName(java.lang.String stackName) {
            this.stackName = stackName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder withDisplayName(java.lang.String displayName) {
            this.displayName = displayName;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder withStackVersion(java.lang.String stackVersion) {
            this.stackVersion = stackVersion;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder withRepoVersion(java.lang.String repoVersion) {
            this.repoVersion = repoVersion;
            return this;
        }

        public org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder withRepos(java.util.SortedMap<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> repos) {
            this.repos = repos;
            return this;
        }
    }

    protected ChangeRepositoryVersionRequestAuditEvent() {
    }

    protected ChangeRepositoryVersionRequestAuditEvent(org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.ChangeRepositoryVersionAuditEventBuilder();
    }
}