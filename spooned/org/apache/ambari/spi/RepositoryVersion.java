package org.apache.ambari.spi;
public class RepositoryVersion {
    private final long m_id;

    private final java.lang.String m_stackId;

    private final java.lang.String m_stackName;

    private final java.lang.String m_stackVersion;

    private final java.lang.String m_version;

    private final org.apache.ambari.spi.RepositoryType m_repositoryType;

    public RepositoryVersion(long id, java.lang.String stackName, java.lang.String stackVersion, java.lang.String stackId, java.lang.String version, org.apache.ambari.spi.RepositoryType repositoryType) {
        m_id = id;
        m_stackName = stackName;
        m_stackVersion = stackVersion;
        m_stackId = stackId;
        m_version = version;
        m_repositoryType = repositoryType;
    }

    public long getId() {
        return m_id;
    }

    public java.lang.String getStackId() {
        return m_stackId;
    }

    public java.lang.String getStackName() {
        return m_stackName;
    }

    public java.lang.String getStackVersion() {
        return m_stackVersion;
    }

    public java.lang.String getVersion() {
        return m_version;
    }

    public org.apache.ambari.spi.RepositoryType getRepositoryType() {
        return m_repositoryType;
    }
}