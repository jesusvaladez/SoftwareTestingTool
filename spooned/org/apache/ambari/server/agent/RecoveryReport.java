package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class RecoveryReport {
    private java.lang.String summary = "DISABLED";

    private java.util.List<org.apache.ambari.server.agent.ComponentRecoveryReport> componentReports = new java.util.ArrayList<>();

    @org.codehaus.jackson.annotate.JsonProperty("summary")
    @com.fasterxml.jackson.annotation.JsonProperty("summary")
    public java.lang.String getSummary() {
        return summary;
    }

    @org.codehaus.jackson.annotate.JsonProperty("summary")
    @com.fasterxml.jackson.annotation.JsonProperty("summary")
    public void setSummary(java.lang.String summary) {
        this.summary = summary;
    }

    @org.codehaus.jackson.annotate.JsonProperty("component_reports")
    @com.fasterxml.jackson.annotation.JsonProperty("component_reports")
    public java.util.List<org.apache.ambari.server.agent.ComponentRecoveryReport> getComponentReports() {
        return componentReports;
    }

    @org.codehaus.jackson.annotate.JsonProperty("component_reports")
    @com.fasterxml.jackson.annotation.JsonProperty("component_reports")
    public void setComponentReports(java.util.List<org.apache.ambari.server.agent.ComponentRecoveryReport> componentReports) {
        this.componentReports = componentReports;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String componentReportsStr = "[]";
        if (componentReports != null) {
            componentReportsStr = java.util.Arrays.toString(componentReports.toArray());
        }
        return ((((("RecoveryReport{" + "summary='") + summary) + '\'') + ", component_reports='") + componentReportsStr) + "'}";
    }
}