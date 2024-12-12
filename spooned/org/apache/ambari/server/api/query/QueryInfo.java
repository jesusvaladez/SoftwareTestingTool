package org.apache.ambari.server.api.query;
public class QueryInfo {
    private org.apache.ambari.server.api.resources.ResourceDefinition m_resource;

    private java.util.Set<java.lang.String> m_properties;

    public QueryInfo(org.apache.ambari.server.api.resources.ResourceDefinition resource, java.util.Set<java.lang.String> properties) {
        m_resource = resource;
        m_properties = new java.util.HashSet<>(properties);
    }

    public org.apache.ambari.server.api.resources.ResourceDefinition getResource() {
        return m_resource;
    }

    public java.util.Set<java.lang.String> getProperties() {
        return m_properties;
    }
}