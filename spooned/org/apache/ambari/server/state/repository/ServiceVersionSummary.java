package org.apache.ambari.server.state.repository;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class ServiceVersionSummary {
    @com.google.gson.annotations.SerializedName("version")
    @org.codehaus.jackson.annotate.JsonProperty("version")
    private java.lang.String m_version;

    @com.google.gson.annotations.SerializedName("release_version")
    @org.codehaus.jackson.annotate.JsonProperty("release_version")
    private java.lang.String m_releaseVersion;

    @com.google.gson.annotations.SerializedName("upgrade")
    @org.codehaus.jackson.annotate.JsonProperty("upgrade")
    private boolean m_upgrade = false;

    ServiceVersionSummary() {
    }

    void setVersions(java.lang.String binaryVersion, java.lang.String releaseVersion) {
        m_version = binaryVersion;
        m_releaseVersion = releaseVersion;
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    public boolean isUpgrade() {
        return m_upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        m_upgrade = upgrade;
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    public java.lang.String getReleaseVersion() {
        return m_releaseVersion;
    }
}