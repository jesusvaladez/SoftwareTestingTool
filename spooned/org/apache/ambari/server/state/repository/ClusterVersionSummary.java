package org.apache.ambari.server.state.repository;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class ClusterVersionSummary {
    @com.google.gson.annotations.SerializedName("services")
    @org.codehaus.jackson.annotate.JsonProperty("services")
    private java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ServiceVersionSummary> m_services;

    private transient java.util.Set<java.lang.String> m_available = new java.util.HashSet<>();

    ClusterVersionSummary(java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ServiceVersionSummary> services) {
        m_services = services;
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.repository.ServiceVersionSummary> entry : services.entrySet()) {
            if (entry.getValue().isUpgrade()) {
                m_available.add(entry.getKey());
            }
        }
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    public java.util.Set<java.lang.String> getAvailableServiceNames() {
        return m_available;
    }
}