package org.apache.ambari.server.state.repository;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
public class AvailableVersion {
    @org.codehaus.jackson.annotate.JsonProperty("version")
    private java.lang.String version;

    @org.codehaus.jackson.annotate.JsonProperty("release_version")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
    private java.lang.String releaseVersion;

    @org.codehaus.jackson.annotate.JsonProperty("version_id")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
    private java.lang.String versionId;

    @org.codehaus.jackson.annotate.JsonProperty
    private java.util.Set<org.apache.ambari.server.state.repository.AvailableVersion.Component> components;

    AvailableVersion(java.lang.String version, java.lang.String versionId, java.lang.String releaseVersion, java.util.Set<org.apache.ambari.server.state.repository.AvailableVersion.Component> components) {
        this.version = version;
        this.versionId = versionId;
        this.releaseVersion = releaseVersion;
        this.components = components;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public java.lang.String getReleaseVersion() {
        return releaseVersion;
    }

    static class Component {
        @org.codehaus.jackson.annotate.JsonProperty("name")
        private java.lang.String name;

        @org.codehaus.jackson.annotate.JsonProperty("display_name")
        private java.lang.String display;

        Component(java.lang.String name, java.lang.String display) {
            this.name = name;
            this.display = display;
        }
    }
}