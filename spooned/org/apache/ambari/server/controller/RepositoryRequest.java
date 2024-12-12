package org.apache.ambari.server.controller;
public class RepositoryRequest extends org.apache.ambari.server.controller.OperatingSystemRequest {
    private java.lang.String repoId;

    private java.lang.String baseUrl;

    private java.lang.String mirrorsList;

    private boolean verify = true;

    private java.lang.Long clusterVersionId = null;

    private java.lang.String repoName = null;

    public RepositoryRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String osType, java.lang.String repoId, java.lang.String repoName) {
        super(stackName, stackVersion, osType);
        setRepoId(repoId);
        setRepoName(repoName);
    }

    public java.lang.String getRepoId() {
        return repoId;
    }

    public void setRepoId(java.lang.String repoId) {
        this.repoId = repoId;
    }

    public java.lang.String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(java.lang.String url) {
        baseUrl = url;
    }

    public boolean isVerifyBaseUrl() {
        return verify;
    }

    public void setVerifyBaseUrl(boolean verifyUrl) {
        verify = verifyUrl;
    }

    public void setClusterVersionId(java.lang.Long id) {
        clusterVersionId = id;
    }

    public java.lang.Long getClusterVersionId() {
        return clusterVersionId;
    }

    public java.lang.String getRepoName() {
        return repoName;
    }

    public void setRepoName(java.lang.String repoName) {
        this.repoName = repoName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((("RepositoryRequest [repoId=" + repoId) + ", baseUrl=") + org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(baseUrl)) + ", verify=") + verify) + ", getOsType()=") + getOsType()) + ", getRepositoryVersionId()=") + getRepositoryVersionId()) + ", getStackVersion()=") + getStackVersion()) + ", getStackName()=") + getStackName()) + ", getRepoName()=") + getRepoName()) + "]";
    }

    public java.lang.String getMirrorsList() {
        return mirrorsList;
    }

    public void setMirrorsList(java.lang.String mirrorsList) {
        this.mirrorsList = mirrorsList;
    }
}