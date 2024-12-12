package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public abstract class Source {
    private org.apache.ambari.server.state.alert.SourceType type;

    @com.google.gson.annotations.SerializedName("reporting")
    private org.apache.ambari.server.state.alert.Reporting reporting;

    public org.apache.ambari.server.state.alert.SourceType getType() {
        return type;
    }

    public void setType(org.apache.ambari.server.state.alert.SourceType type) {
        this.type = type;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("reporting")
    public org.apache.ambari.server.state.alert.Reporting getReporting() {
        return reporting;
    }

    public void setReporting(org.apache.ambari.server.state.alert.Reporting reporting) {
        this.reporting = reporting;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(reporting, type);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        org.apache.ambari.server.state.alert.Source other = ((org.apache.ambari.server.state.alert.Source) (obj));
        return java.util.Objects.equals(reporting, other.reporting) && java.util.Objects.equals(type, other.type);
    }
}