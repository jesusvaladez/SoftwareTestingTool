package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class AggregateSource extends org.apache.ambari.server.state.alert.Source {
    @com.google.gson.annotations.SerializedName("alert_name")
    private java.lang.String m_alertName = null;

    @com.fasterxml.jackson.annotation.JsonProperty("alert_name")
    public java.lang.String getAlertName() {
        return m_alertName;
    }

    public void setAlertName(java.lang.String alertName) {
        m_alertName = alertName;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), m_alertName);
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
        org.apache.ambari.server.state.alert.AggregateSource other = ((org.apache.ambari.server.state.alert.AggregateSource) (obj));
        return java.util.Objects.equals(m_alertName, other.m_alertName);
    }
}