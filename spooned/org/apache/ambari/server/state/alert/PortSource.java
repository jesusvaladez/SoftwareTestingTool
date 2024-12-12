package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class PortSource extends org.apache.ambari.server.state.alert.ParameterizedSource {
    @com.google.gson.annotations.SerializedName("uri")
    private java.lang.String m_uri = null;

    @com.google.gson.annotations.SerializedName("default_port")
    private int m_port = 0;

    @com.fasterxml.jackson.annotation.JsonProperty("uri")
    public java.lang.String getUri() {
        return m_uri;
    }

    public void setUri(java.lang.String uri) {
        m_uri = uri;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("default_port")
    public int getPort() {
        return m_port;
    }

    public void setPort(int port) {
        m_port = port;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), m_port, m_uri);
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
        org.apache.ambari.server.state.alert.PortSource other = ((org.apache.ambari.server.state.alert.PortSource) (obj));
        return java.util.Objects.equals(m_port, other.m_port) && java.util.Objects.equals(m_uri, other.m_uri);
    }
}