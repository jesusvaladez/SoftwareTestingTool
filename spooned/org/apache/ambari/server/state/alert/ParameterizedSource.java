package org.apache.ambari.server.state.alert;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public abstract class ParameterizedSource extends org.apache.ambari.server.state.alert.Source {
    @com.google.gson.annotations.SerializedName("parameters")
    java.util.List<org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter> m_parameters;

    @com.fasterxml.jackson.annotation.JsonProperty("parameters")
    public java.util.List<org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter> getParameters() {
        if (null == m_parameters) {
            return java.util.Collections.emptyList();
        }
        return m_parameters;
    }

    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public static class AlertParameter {
        @com.google.gson.annotations.SerializedName("name")
        private java.lang.String m_name;

        @com.google.gson.annotations.SerializedName("display_name")
        private java.lang.String m_displayName;

        @com.google.gson.annotations.SerializedName("units")
        private java.lang.String m_units;

        @com.google.gson.annotations.SerializedName("value")
        private java.lang.Object m_value;

        @com.google.gson.annotations.SerializedName("description")
        private java.lang.String m_description;

        @com.google.gson.annotations.SerializedName("type")
        private org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameterType m_type;

        @com.google.gson.annotations.SerializedName("visibility")
        private org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameterVisibility m_visibility;

        @com.google.gson.annotations.SerializedName("threshold")
        private org.apache.ambari.server.state.AlertState m_threshold;

        @com.fasterxml.jackson.annotation.JsonProperty("name")
        public java.lang.String getName() {
            return m_name;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("display_name")
        public java.lang.String getDisplayName() {
            return m_displayName;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("units")
        public java.lang.String getUnits() {
            return m_units;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("value")
        public java.lang.Object getValue() {
            return m_value;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("description")
        public java.lang.String getDescription() {
            return m_description;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("type")
        public org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameterType getType() {
            return m_type;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("visibility")
        public org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameterVisibility getVisibility() {
            return m_visibility;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("threshold")
        public org.apache.ambari.server.state.AlertState getThreshold() {
            return m_threshold;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(m_description, m_displayName, m_name, m_threshold, m_type, m_units, m_value, m_visibility);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter other = ((org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter) (obj));
            return ((((((java.util.Objects.equals(m_description, other.m_description) && java.util.Objects.equals(m_displayName, other.m_displayName)) && java.util.Objects.equals(m_name, other.m_name)) && java.util.Objects.equals(m_threshold, other.m_threshold)) && java.util.Objects.equals(m_type, other.m_type)) && java.util.Objects.equals(m_units, other.m_units)) && java.util.Objects.equals(m_value, other.m_value)) && java.util.Objects.equals(m_visibility, other.m_visibility);
        }
    }

    public enum AlertParameterType {

        STRING,
        NUMERIC,
        PERCENT;}

    public enum AlertParameterVisibility {

        VISIBLE,
        HIDDEN,
        READ_ONLY;}

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.server.state.alert.ParameterizedSource))
            return false;

        org.apache.ambari.server.state.alert.ParameterizedSource that = ((org.apache.ambari.server.state.alert.ParameterizedSource) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().appendSuper(super.equals(o)).append(m_parameters, that.m_parameters).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(m_parameters).toHashCode();
    }
}