package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class ServerSource extends org.apache.ambari.server.state.alert.ParameterizedSource {
    @com.google.gson.annotations.SerializedName("class")
    private java.lang.String m_class;

    @com.google.gson.annotations.SerializedName("uri")
    private org.apache.ambari.server.state.UriInfo uri = null;

    @com.google.gson.annotations.SerializedName("jmx")
    private org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo = null;

    @com.fasterxml.jackson.annotation.JsonProperty("class")
    public java.lang.String getSourceClass() {
        return m_class;
    }

    public org.apache.ambari.server.state.alert.MetricSource.JmxInfo getJmxInfo() {
        return jmxInfo;
    }

    public org.apache.ambari.server.state.UriInfo getUri() {
        return uri;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), m_class);
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
        org.apache.ambari.server.state.alert.ServerSource other = ((org.apache.ambari.server.state.alert.ServerSource) (obj));
        return java.util.Objects.equals(m_class, other.m_class);
    }
}