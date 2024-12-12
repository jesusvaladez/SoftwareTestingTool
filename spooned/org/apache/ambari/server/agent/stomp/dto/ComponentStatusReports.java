package org.apache.ambari.server.agent.stomp.dto;
public class ComponentStatusReports {
    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport>> componentStatusReports;

    public ComponentStatusReports() {
    }

    public ComponentStatusReports(java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport>> componentStatusReports) {
        this.componentStatusReports = componentStatusReports;
    }

    public java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport>> getComponentStatusReports() {
        return componentStatusReports;
    }

    public void setComponentStatusReports(java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport>> componentStatusReports) {
        this.componentStatusReports = componentStatusReports;
    }
}