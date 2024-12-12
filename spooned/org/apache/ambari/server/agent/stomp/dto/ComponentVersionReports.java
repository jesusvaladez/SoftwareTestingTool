package org.apache.ambari.server.agent.stomp.dto;
public class ComponentVersionReports {
    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentVersionReport>> componentVersionReports;

    public ComponentVersionReports() {
    }

    public ComponentVersionReports(java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentVersionReport>> componentVersionReports) {
        this.componentVersionReports = componentVersionReports;
    }

    public java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentVersionReport>> getComponentVersionReports() {
        return componentVersionReports;
    }

    public void setComponentVersionReports(java.util.TreeMap<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentVersionReport>> componentVersionReports) {
        this.componentVersionReports = componentVersionReports;
    }
}