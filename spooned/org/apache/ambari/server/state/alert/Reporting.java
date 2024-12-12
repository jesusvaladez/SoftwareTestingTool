package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class Reporting {
    @com.google.gson.annotations.SerializedName("ok")
    private org.apache.ambari.server.state.alert.Reporting.ReportTemplate m_ok;

    @com.google.gson.annotations.SerializedName("warning")
    private org.apache.ambari.server.state.alert.Reporting.ReportTemplate m_warning;

    @com.google.gson.annotations.SerializedName("critical")
    private org.apache.ambari.server.state.alert.Reporting.ReportTemplate m_critical;

    @com.google.gson.annotations.SerializedName("units")
    private java.lang.String m_units;

    @com.google.gson.annotations.SerializedName("type")
    private org.apache.ambari.server.state.alert.Reporting.ReportingType m_type;

    @com.fasterxml.jackson.annotation.JsonProperty("warning")
    public org.apache.ambari.server.state.alert.Reporting.ReportTemplate getWarning() {
        return m_warning;
    }

    public void setWarning(org.apache.ambari.server.state.alert.Reporting.ReportTemplate warning) {
        m_warning = warning;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("critical")
    public org.apache.ambari.server.state.alert.Reporting.ReportTemplate getCritical() {
        return m_critical;
    }

    public void setCritical(org.apache.ambari.server.state.alert.Reporting.ReportTemplate critical) {
        m_critical = critical;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("ok")
    public org.apache.ambari.server.state.alert.Reporting.ReportTemplate getOk() {
        return m_ok;
    }

    public void setOk(org.apache.ambari.server.state.alert.Reporting.ReportTemplate ok) {
        m_ok = ok;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("units")
    public java.lang.String getUnits() {
        return m_units;
    }

    public void setUnits(java.lang.String units) {
        m_units = units;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("type")
    public org.apache.ambari.server.state.alert.Reporting.ReportingType getType() {
        return m_type;
    }

    public void setType(org.apache.ambari.server.state.alert.Reporting.ReportingType m_type) {
        this.m_type = m_type;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(m_critical, m_ok, m_warning, m_type);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        org.apache.ambari.server.state.alert.Reporting other = ((org.apache.ambari.server.state.alert.Reporting) (obj));
        return ((java.util.Objects.equals(m_critical, other.m_critical) && java.util.Objects.equals(m_ok, other.m_ok)) && java.util.Objects.equals(m_warning, other.m_warning)) && java.util.Objects.equals(m_type, other.m_type);
    }

    public org.apache.ambari.server.state.AlertState state(double value) {
        return getThreshold().state(value);
    }

    private org.apache.ambari.server.alerts.Threshold getThreshold() {
        return new org.apache.ambari.server.alerts.Threshold(getOk().getValue(), getWarning().getValue(), getCritical().getValue());
    }

    public java.lang.String formatMessage(double value, java.util.List<java.lang.Object> args) {
        java.util.List<java.lang.Object> copy = new java.util.ArrayList<>(args);
        copy.add(value);
        return java.text.MessageFormat.format(message(value), copy.toArray());
    }

    private java.lang.String message(double value) {
        switch (state(value)) {
            case OK :
                return getOk().getText();
            case WARNING :
                return getWarning().getText();
            case CRITICAL :
                return getCritical().getText();
            case UNKNOWN :
                return "Unknown";
            case SKIPPED :
                return "Skipped";
            default :
                throw new java.lang.IllegalStateException("Invalid alert state: " + state(value));
        }
    }

    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public static final class ReportTemplate {
        @com.google.gson.annotations.SerializedName("text")
        private java.lang.String m_text;

        @com.google.gson.annotations.SerializedName("value")
        private java.lang.Double m_value = null;

        @com.fasterxml.jackson.annotation.JsonProperty("text")
        public java.lang.String getText() {
            return m_text;
        }

        public void setText(java.lang.String text) {
            m_text = text;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public java.lang.Double getValue() {
            return m_value;
        }

        public void setValue(java.lang.Double value) {
            m_value = value;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(m_text, m_value);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            org.apache.ambari.server.state.alert.Reporting.ReportTemplate other = ((org.apache.ambari.server.state.alert.Reporting.ReportTemplate) (obj));
            return java.util.Objects.equals(m_text, other.m_text) && java.util.Objects.equals(m_value, other.m_value);
        }
    }

    public enum ReportingType {

        NUMERIC,
        PERCENT;}
}