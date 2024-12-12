package org.apache.ambari.server.api.services;
public class RequestBody {
    private java.lang.String m_query;

    private java.lang.String m_fields;

    private java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> m_propertySets = new java.util.HashSet<>();

    private java.lang.String m_body;

    private java.util.Map<java.lang.String, java.lang.String> m_requestInfoProps = new java.util.HashMap<>();

    public void setQueryString(java.lang.String query) {
        m_query = query;
    }

    public java.lang.String getQueryString() {
        return m_query;
    }

    public void setPartialResponseFields(java.lang.String fields) {
        m_fields = fields;
    }

    public java.lang.String getPartialResponseFields() {
        return m_fields;
    }

    public void addPropertySet(org.apache.ambari.server.api.services.NamedPropertySet propertySet) {
        m_propertySets.add(propertySet);
    }

    public java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> getNamedPropertySets() {
        return m_propertySets;
    }

    public java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getPropertySets() {
        java.util.Set<org.apache.ambari.server.api.services.NamedPropertySet> setNamedProps = getNamedPropertySets();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProps = new java.util.HashSet<>(setNamedProps.size());
        for (org.apache.ambari.server.api.services.NamedPropertySet namedProps : setNamedProps) {
            setProps.add(namedProps.getProperties());
        }
        return setProps;
    }

    public java.util.Map<java.lang.String, java.lang.String> getRequestInfoProperties() {
        return m_requestInfoProps;
    }

    public void addRequestInfoProperty(java.lang.String key, java.lang.String val) {
        m_requestInfoProps.put(key, val);
    }

    public void setBody(java.lang.String body) {
        if ((body != null) && (!body.isEmpty())) {
            m_body = body;
            m_requestInfoProps.put("RAW_REQUEST_BODY", body);
        }
    }

    public java.lang.String getBody() {
        return m_body;
    }
}