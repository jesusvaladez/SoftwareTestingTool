package org.apache.ambari.server.controller;
public class OperatingSystemResponse {
    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String osType;

    private java.lang.Long repositoryVersionId;

    private java.lang.String versionDefinitionId;

    private boolean ambariManagedRepos = true;

    public OperatingSystemResponse(java.lang.String osType) {
        setOsType(osType);
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

    public java.lang.String getOsType() {
        return osType;
    }

    public void setOsType(java.lang.String osType) {
        this.osType = osType;
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

    public void setAmbariManagedRepos(boolean managed) {
        ambariManagedRepos = managed;
    }

    public boolean isAmbariManagedRepos() {
        return ambariManagedRepos;
    }
}