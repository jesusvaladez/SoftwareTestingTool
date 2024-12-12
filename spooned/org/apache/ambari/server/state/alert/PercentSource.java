package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class PercentSource extends org.apache.ambari.server.state.alert.Source {
    @com.google.gson.annotations.SerializedName("numerator")
    private org.apache.ambari.server.state.alert.PercentSource.MetricFractionPart m_numerator = null;

    @com.google.gson.annotations.SerializedName("denominator")
    private org.apache.ambari.server.state.alert.PercentSource.MetricFractionPart m_denominator = null;

    @com.fasterxml.jackson.annotation.JsonProperty("numerator")
    public org.apache.ambari.server.state.alert.PercentSource.MetricFractionPart getNumerator() {
        return m_numerator;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("denominator")
    public org.apache.ambari.server.state.alert.PercentSource.MetricFractionPart getDenominator() {
        return m_denominator;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), m_denominator, m_numerator);
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
        org.apache.ambari.server.state.alert.PercentSource other = ((org.apache.ambari.server.state.alert.PercentSource) (obj));
        return java.util.Objects.equals(m_denominator, other.m_denominator) && java.util.Objects.equals(m_numerator, other.m_numerator);
    }

    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public static final class MetricFractionPart {
        @com.google.gson.annotations.SerializedName("jmx")
        private java.lang.String m_jmxInfo = null;

        @com.google.gson.annotations.SerializedName("ganglia")
        private java.lang.String m_gangliaInfo = null;

        @com.fasterxml.jackson.annotation.JsonProperty("jmx")
        public java.lang.String getJmxInfo() {
            return m_jmxInfo;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("ganglia")
        public java.lang.String getGangliaInfo() {
            return m_gangliaInfo;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(m_gangliaInfo, m_jmxInfo);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            org.apache.ambari.server.state.alert.PercentSource.MetricFractionPart other = ((org.apache.ambari.server.state.alert.PercentSource.MetricFractionPart) (obj));
            return java.util.Objects.equals(m_gangliaInfo, other.m_gangliaInfo) && java.util.Objects.equals(m_jmxInfo, other.m_jmxInfo);
        }
    }
}