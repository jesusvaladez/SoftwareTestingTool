package org.apache.ambari.server.state.repository;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
public class AvailableService {
    @org.codehaus.jackson.annotate.JsonProperty("name")
    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("display_name")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL)
    private java.lang.String displayName;

    private java.util.List<org.apache.ambari.server.state.repository.AvailableVersion> versions = new java.util.ArrayList<>();

    AvailableService(java.lang.String service, java.lang.String serviceDisplay) {
        name = service;
        displayName = serviceDisplay;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.util.List<org.apache.ambari.server.state.repository.AvailableVersion> getVersions() {
        return versions;
    }
}