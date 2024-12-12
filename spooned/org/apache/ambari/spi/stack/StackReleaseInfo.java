package org.apache.ambari.spi.stack;
public class StackReleaseInfo {
    private java.lang.String m_version;

    private java.lang.String m_hotfix;

    private java.lang.String m_build;

    public StackReleaseInfo(java.lang.String version, java.lang.String hotfix, java.lang.String build) {
        m_version = version;
        m_hotfix = hotfix;
        m_build = build;
    }

    public java.lang.String getVersion() {
        return m_version;
    }

    public java.lang.String getHotfix() {
        return m_hotfix;
    }

    public java.lang.String getBuild() {
        return m_build;
    }
}