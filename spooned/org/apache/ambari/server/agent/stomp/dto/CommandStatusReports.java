package org.apache.ambari.server.agent.stomp.dto;
public class CommandStatusReports {
    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.CommandReport>> clustersComponentReports;

    public CommandStatusReports() {
    }

    public java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.CommandReport>> getClustersComponentReports() {
        return clustersComponentReports;
    }

    public void setClustersComponentReports(java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.CommandReport>> clustersComponentReports) {
        this.clustersComponentReports = clustersComponentReports;
    }
}