package org.apache.ambari.server.controller;
public class OperatingSystemRequest extends org.apache.ambari.server.controller.StackVersionRequest {
    private java.lang.String osType;

    private java.lang.Long repositoryVersionId;

    private java.lang.String versionDefinitionId;

    public OperatingSystemRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String osType) {
        super(stackName, stackVersion);
        setOsType(osType);
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
}