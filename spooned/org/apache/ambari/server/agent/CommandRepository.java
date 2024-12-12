package org.apache.ambari.server.agent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
public class CommandRepository {
    @com.google.gson.annotations.SerializedName("repositories")
    @com.fasterxml.jackson.annotation.JsonProperty("repositories")
    private java.util.List<org.apache.ambari.server.agent.CommandRepository.Repository> m_repositories = new java.util.ArrayList<>();

    @com.google.gson.annotations.SerializedName("repoVersion")
    @com.fasterxml.jackson.annotation.JsonProperty("repoVersion")
    private java.lang.String m_repoVersion;

    @com.google.gson.annotations.SerializedName("repoVersionId")
    @com.fasterxml.jackson.annotation.JsonProperty("repoVersionId")
    private long m_repoVersionId;

    @com.google.gson.annotations.SerializedName("stackName")
    @com.fasterxml.jackson.annotation.JsonProperty("stackName")
    private java.lang.String m_stackName;

    @com.google.gson.annotations.SerializedName("repoFileName")
    @com.fasterxml.jackson.annotation.JsonProperty("repoFileName")
    private java.lang.String m_repoFileName;

    @com.google.gson.annotations.SerializedName("feature")
    @com.fasterxml.jackson.annotation.JsonProperty("feature")
    private final org.apache.ambari.server.agent.CommandRepository.CommandRepositoryFeature feature = new org.apache.ambari.server.agent.CommandRepository.CommandRepositoryFeature();

    public org.apache.ambari.server.agent.CommandRepository.CommandRepositoryFeature getFeature() {
        return feature;
    }

    @com.google.gson.annotations.SerializedName("resolved")
    @com.fasterxml.jackson.annotation.JsonProperty("resolved")
    private boolean m_resolved;

    public void setRepoVersion(java.lang.String version) {
        m_repoVersion = version;
    }

    public java.lang.String getRepoVersion() {
        return m_repoVersion;
    }

    public void setRepositoryVersionId(long id) {
        m_repoVersionId = id;
    }

    public void setStackName(java.lang.String name) {
        m_stackName = name;
    }

    public void setRepositories(java.util.Collection<org.apache.ambari.server.state.RepositoryInfo> repositories) {
        m_repositories = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.RepositoryInfo info : repositories) {
            m_repositories.add(new org.apache.ambari.server.agent.CommandRepository.Repository(info));
        }
    }

    public void setRepositories(java.lang.String osType, java.util.Collection<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> repositories) {
        m_repositories = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity entity : repositories) {
            m_repositories.add(new org.apache.ambari.server.agent.CommandRepository.Repository(osType, entity));
        }
    }

    public java.util.Collection<org.apache.ambari.server.agent.CommandRepository.Repository> getRepositories() {
        return m_repositories;
    }

    public void setUniqueSuffix(java.lang.String suffix) {
        for (org.apache.ambari.server.agent.CommandRepository.Repository repo : m_repositories) {
            repo.m_repoId = repo.m_repoId + suffix;
        }
    }

    public void setNonManaged() {
        for (org.apache.ambari.server.agent.CommandRepository.Repository repo : m_repositories) {
            repo.m_baseUrl = null;
            repo.m_mirrorsList = null;
            repo.m_ambariManaged = false;
        }
    }

    public long getRepoVersionId() {
        return m_repoVersionId;
    }

    public void setResolved(boolean resolved) {
        m_resolved = resolved;
    }

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public void setLegacyRepoId(java.lang.String repoVersion) {
        for (org.apache.ambari.server.agent.CommandRepository.Repository repo : m_repositories) {
            repo.m_repoId = java.lang.String.format("%s-%s", repo.getRepoName(), repoVersion);
        }
    }

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public void setLegacyRepoFileName(java.lang.String stackName, java.lang.String repoVersion) {
        this.m_repoFileName = java.lang.String.format("%s-%s", stackName, repoVersion);
    }

    public void setRepoFileName(java.lang.String stackName, java.lang.Long repoVersionId) {
        this.m_repoFileName = java.lang.String.format("ambari-%s-%s", stackName.toLowerCase(), repoVersionId.toString());
    }

    public static class CommandRepositoryFeature {
        @com.google.gson.annotations.SerializedName("preInstalled")
        @com.fasterxml.jackson.annotation.JsonProperty("preInstalled")
        private java.lang.Boolean m_isPreInstalled = false;

        @com.google.gson.annotations.SerializedName("scoped")
        @com.fasterxml.jackson.annotation.JsonProperty("scoped")
        private boolean m_isScoped = true;

        public void setIsScoped(boolean isScoped) {
            this.m_isScoped = isScoped;
        }

        public void setPreInstalled(java.lang.String isPreInstalled) {
            this.m_isPreInstalled = isPreInstalled.equalsIgnoreCase("true");
        }
    }

    public static class Repository {
        @com.google.gson.annotations.SerializedName("baseUrl")
        @com.fasterxml.jackson.annotation.JsonProperty("baseUrl")
        private java.lang.String m_baseUrl;

        @com.google.gson.annotations.SerializedName("repoId")
        @com.fasterxml.jackson.annotation.JsonProperty("repoId")
        private java.lang.String m_repoId;

        @com.google.gson.annotations.SerializedName("ambariManaged")
        @com.fasterxml.jackson.annotation.JsonProperty("ambariManaged")
        private boolean m_ambariManaged = true;

        @com.google.gson.annotations.SerializedName("repoName")
        @com.fasterxml.jackson.annotation.JsonProperty("repoName")
        private final java.lang.String m_repoName;

        @com.google.gson.annotations.SerializedName("distribution")
        @com.fasterxml.jackson.annotation.JsonProperty("distribution")
        private final java.lang.String m_distribution;

        @com.google.gson.annotations.SerializedName("components")
        @com.fasterxml.jackson.annotation.JsonProperty("components")
        private final java.lang.String m_components;

        @com.google.gson.annotations.SerializedName("mirrorsList")
        @com.fasterxml.jackson.annotation.JsonProperty("mirrorsList")
        private java.lang.String m_mirrorsList;

        @com.google.gson.annotations.SerializedName("applicableServices")
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
        private java.util.List<java.lang.String> m_applicableServices;

        @com.google.gson.annotations.SerializedName("tags")
        @com.fasterxml.jackson.annotation.JsonProperty("tags")
        private java.util.Set<org.apache.ambari.server.state.stack.RepoTag> m_tags;

        private transient java.lang.String m_osType;

        private Repository(org.apache.ambari.server.state.RepositoryInfo info) {
            m_baseUrl = info.getBaseUrl();
            m_osType = info.getOsType();
            m_repoId = info.getRepoId();
            m_repoName = info.getRepoName();
            m_distribution = info.getDistribution();
            m_components = info.getComponents();
            m_mirrorsList = info.getMirrorsList();
            m_applicableServices = info.getApplicableServices();
            m_tags = info.getTags();
        }

        private Repository(java.lang.String osType, org.apache.ambari.server.orm.entities.RepoDefinitionEntity entity) {
            m_baseUrl = entity.getBaseUrl();
            m_repoId = entity.getRepoID();
            m_repoName = entity.getRepoName();
            m_distribution = entity.getDistribution();
            m_components = entity.getComponents();
            m_mirrorsList = entity.getMirrors();
            m_applicableServices = entity.getApplicableServices();
            m_osType = osType;
            m_tags = entity.getTags();
        }

        public void setRepoId(java.lang.String repoId) {
            m_repoId = repoId;
        }

        public void setBaseUrl(java.lang.String url) {
            m_baseUrl = url;
        }

        public java.lang.String getRepoName() {
            return m_repoName;
        }

        public java.lang.String getRepoId() {
            return m_repoId;
        }

        public java.lang.String getBaseUrl() {
            return m_baseUrl;
        }

        public boolean isAmbariManaged() {
            return m_ambariManaged;
        }

        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
        public void setApplicableServices(java.util.List<java.lang.String> applicableServices) {
            m_applicableServices = applicableServices;
        }

        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
        public java.util.List<java.lang.String> getApplicableServices() {
            return m_applicableServices;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new org.apache.commons.lang.builder.ToStringBuilder(null).append("os", m_osType).append("name", m_repoName).append("distribution", m_distribution).append("components", m_components).append("id", m_repoId).append("baseUrl", org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(m_baseUrl)).append("applicableServices", m_applicableServices != null ? org.apache.commons.lang.StringUtils.join(m_applicableServices, ",") : "").toString();
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.CommandRepository that = ((org.apache.ambari.server.agent.CommandRepository) (o));
        return m_repoVersionId == that.m_repoVersionId;
    }

    @java.lang.Override
    public int hashCode() {
        return ((int) (m_repoVersionId ^ (m_repoVersionId >>> 32)));
    }
}