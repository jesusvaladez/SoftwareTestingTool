package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class WebSource extends org.apache.ambari.server.state.alert.Source {
    @com.google.gson.annotations.SerializedName("uri")
    private org.apache.ambari.server.state.UriInfo uri = null;

    @com.fasterxml.jackson.annotation.JsonProperty("uri")
    public org.apache.ambari.server.state.UriInfo getUri() {
        return uri;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), uri);
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
        org.apache.ambari.server.state.alert.WebSource other = ((org.apache.ambari.server.state.alert.WebSource) (obj));
        return java.util.Objects.equals(uri, other.uri);
    }
}