package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class ScriptSource extends org.apache.ambari.server.state.alert.ParameterizedSource {
    @com.google.gson.annotations.SerializedName("path")
    private java.lang.String m_path = null;

    @com.fasterxml.jackson.annotation.JsonProperty("path")
    public java.lang.String getPath() {
        return m_path;
    }

    public void setPath(java.lang.String path) {
        m_path = path;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), m_path);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        org.apache.ambari.server.state.alert.ScriptSource other = ((org.apache.ambari.server.state.alert.ScriptSource) (obj));
        return java.util.Objects.equals(m_path, other.m_path);
    }
}