package org.apache.ambari.server.state;
import org.apache.commons.lang.StringUtils;
public class RepositoryInfo {
    private java.lang.String baseUrl;

    private java.lang.String osType;

    private java.lang.String repoId;

    private java.lang.String repoName;

    private java.lang.String distribution;

    private java.lang.String components;

    private java.lang.String mirrorsList;

    private java.lang.String defaultBaseUrl;

    private boolean repoSaved = false;

    private boolean unique = false;

    private boolean ambariManagedRepositories = true;

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    private java.util.List<java.lang.String> applicableServices = new java.util.LinkedList<>();

    private java.util.Set<org.apache.ambari.server.state.stack.RepoTag> tags = new java.util.HashSet<>();

    public java.lang.String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(java.lang.String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public java.lang.String getOsType() {
        return osType;
    }

    public void setOsType(java.lang.String osType) {
        this.osType = osType;
    }

    public java.lang.String getRepoId() {
        return repoId;
    }

    public void setRepoId(java.lang.String repoId) {
        this.repoId = repoId;
    }

    public java.lang.String getRepoName() {
        return repoName;
    }

    public void setRepoName(java.lang.String repoName) {
        this.repoName = repoName;
    }

    public java.lang.String getDistribution() {
        return distribution;
    }

    public void setDistribution(java.lang.String distribution) {
        this.distribution = distribution;
    }

    public java.lang.String getComponents() {
        return components;
    }

    public void setComponents(java.lang.String components) {
        this.components = components;
    }

    public java.lang.String getMirrorsList() {
        return mirrorsList;
    }

    public void setMirrorsList(java.lang.String mirrorsList) {
        this.mirrorsList = mirrorsList;
    }

    public java.lang.String getDefaultBaseUrl() {
        return defaultBaseUrl;
    }

    public void setDefaultBaseUrl(java.lang.String url) {
        defaultBaseUrl = url;
    }

    public boolean isRepoSaved() {
        return repoSaved;
    }

    public void setRepoSaved(boolean saved) {
        repoSaved = saved;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public java.util.List<java.lang.String> getApplicableServices() {
        return applicableServices;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public void setApplicableServices(java.util.List<java.lang.String> applicableServices) {
        this.applicableServices = applicableServices;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((("[ repoInfo: " + ", osType=") + osType) + ", repoId=") + repoId) + ", baseUrl=") + org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(baseUrl)) + ", repoName=") + repoName) + ", distribution=") + distribution) + ", components=") + components) + ", mirrorsList=") + mirrorsList) + ", unique=") + unique) + ", ambariManagedRepositories=") + ambariManagedRepositories) + ", applicableServices=") + org.apache.commons.lang.StringUtils.join(applicableServices, ",")) + " ]";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.RepositoryInfo that = ((org.apache.ambari.server.state.RepositoryInfo) (o));
        return ((((((((((repoSaved == that.repoSaved) && (unique == that.unique)) && com.google.common.base.Objects.equal(baseUrl, that.baseUrl)) && com.google.common.base.Objects.equal(osType, that.osType)) && com.google.common.base.Objects.equal(repoId, that.repoId)) && com.google.common.base.Objects.equal(repoName, that.repoName)) && com.google.common.base.Objects.equal(distribution, that.distribution)) && com.google.common.base.Objects.equal(components, that.components)) && com.google.common.base.Objects.equal(mirrorsList, that.mirrorsList)) && com.google.common.base.Objects.equal(defaultBaseUrl, that.defaultBaseUrl)) && com.google.common.base.Objects.equal(ambariManagedRepositories, that.ambariManagedRepositories);
    }

    @java.lang.Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(baseUrl, osType, repoId, repoName, distribution, components, mirrorsList, defaultBaseUrl, ambariManagedRepositories);
    }

    public org.apache.ambari.server.controller.RepositoryResponse convertToResponse() {
        return new org.apache.ambari.server.controller.RepositoryResponse(getBaseUrl(), getOsType(), getRepoId(), getRepoName(), getDistribution(), getComponents(), getMirrorsList(), getDefaultBaseUrl(), getTags(), getApplicableServices());
    }

    public static final com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String> GET_REPO_NAME_FUNCTION = new com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String>() {
        @java.lang.Override
        public java.lang.String apply(org.apache.ambari.server.state.RepositoryInfo input) {
            return input.repoName;
        }
    };

    public static final com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String> GET_REPO_ID_FUNCTION = new com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String>() {
        @java.lang.Override
        public java.lang.String apply(org.apache.ambari.server.state.RepositoryInfo input) {
            return input.repoId;
        }
    };

    public static final com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String> SAFE_GET_BASE_URL_FUNCTION = new com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String>() {
        @java.lang.Override
        public java.lang.String apply(org.apache.ambari.server.state.RepositoryInfo input) {
            return com.google.common.base.Strings.nullToEmpty(input.baseUrl);
        }
    };

    public static final com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String> GET_OSTYPE_FUNCTION = new com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String>() {
        @java.lang.Override
        public java.lang.String apply(org.apache.ambari.server.state.RepositoryInfo input) {
            return input.osType;
        }
    };

    public boolean isAmbariManagedRepositories() {
        return ambariManagedRepositories;
    }

    public void setAmbariManagedRepositories(boolean ambariManagedRepositories) {
        this.ambariManagedRepositories = ambariManagedRepositories;
    }

    public java.util.Set<org.apache.ambari.server.state.stack.RepoTag> getTags() {
        return tags;
    }

    public void setTags(java.util.Set<org.apache.ambari.server.state.stack.RepoTag> repoTags) {
        tags = repoTags;
    }
}