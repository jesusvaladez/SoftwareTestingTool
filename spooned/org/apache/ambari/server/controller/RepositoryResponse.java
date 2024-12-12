package org.apache.ambari.server.controller;
public class RepositoryResponse {
    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String baseUrl;

    private java.lang.String osType;

    private java.lang.String repoId;

    private java.lang.String repoName;

    private java.lang.String distribution;

    private java.lang.String components;

    private java.lang.String mirrorsList;

    private java.lang.String defaultBaseUrl;

    private java.lang.Long repositoryVersionId;

    private java.lang.String versionDefinitionId;

    private java.lang.Long clusterVersionId;

    private boolean unique;

    private java.util.Set<org.apache.ambari.server.state.stack.RepoTag> tags;

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    private java.util.List<java.lang.String> applicableServices;

    public RepositoryResponse(java.lang.String baseUrl, java.lang.String osType, java.lang.String repoId, java.lang.String repoName, java.lang.String distribution, java.lang.String components, java.lang.String mirrorsList, java.lang.String defaultBaseUrl, java.util.Set<org.apache.ambari.server.state.stack.RepoTag> repoTags, java.util.List<java.lang.String> applicableServices) {
        setBaseUrl(baseUrl);
        setOsType(osType);
        setRepoId(repoId);
        setRepoName(repoName);
        setDistribution(distribution);
        setComponents(components);
        setMirrorsList(mirrorsList);
        setDefaultBaseUrl(defaultBaseUrl);
        setTags(repoTags);
        setApplicableServices(applicableServices);
    }

    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

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
        this.defaultBaseUrl = url;
    }

    public java.lang.Long getRepositoryVersionId() {
        return repositoryVersionId;
    }

    public void setRepositoryVersionId(java.lang.Long repositoryVersionId) {
        this.repositoryVersionId = repositoryVersionId;
    }

    public void setVersionDefinitionId(java.lang.String id) {
        versionDefinitionId = id;
    }

    public java.lang.String getVersionDefinitionId() {
        return versionDefinitionId;
    }

    public void setClusterVersionId(java.lang.Long id) {
        clusterVersionId = id;
    }

    public java.lang.Long getClusterVersionId() {
        return clusterVersionId;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public java.util.Set<org.apache.ambari.server.state.stack.RepoTag> getTags() {
        return tags;
    }

    public void setTags(java.util.Set<org.apache.ambari.server.state.stack.RepoTag> repoTags) {
        tags = repoTags;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public java.util.List<java.lang.String> getApplicableServices() {
        return applicableServices;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public void setApplicableServices(java.util.List<java.lang.String> applicableServices) {
        this.applicableServices = applicableServices;
    }
}