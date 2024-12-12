package org.apache.ambari.server.state.repository;
import org.codehaus.jackson.annotate.JsonProperty;
public class ManifestServiceInfo {
    @org.codehaus.jackson.annotate.JsonProperty("name")
    java.lang.String m_name;

    @org.codehaus.jackson.annotate.JsonProperty("display_name")
    java.lang.String m_display;

    @org.codehaus.jackson.annotate.JsonProperty("comment")
    java.lang.String m_comment;

    @org.codehaus.jackson.annotate.JsonProperty("versions")
    java.util.Set<java.lang.String> m_versions;

    public ManifestServiceInfo(java.lang.String name, java.lang.String display, java.lang.String comment, java.util.Set<java.lang.String> versions) {
        m_name = name;
        m_display = display;
        m_comment = comment;
        m_versions = versions;
    }
}