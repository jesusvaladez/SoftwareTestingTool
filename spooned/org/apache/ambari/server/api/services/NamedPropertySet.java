package org.apache.ambari.server.api.services;
public class NamedPropertySet {
    private java.lang.String m_name;

    private java.util.Map<java.lang.String, java.lang.Object> m_mapProperties;

    public NamedPropertySet(java.lang.String name, java.util.Map<java.lang.String, java.lang.Object> mapProperties) {
        m_name = name;
        m_mapProperties = mapProperties;
    }

    public java.lang.String getName() {
        return m_name;
    }

    public java.util.Map<java.lang.String, java.lang.Object> getProperties() {
        return m_mapProperties;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.api.services.NamedPropertySet that = ((org.apache.ambari.server.api.services.NamedPropertySet) (o));
        return (m_mapProperties == null ? that.m_mapProperties == null : m_mapProperties.equals(that.m_mapProperties)) && (m_name == null ? that.m_name == null : m_name.equals(that.m_name));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (m_name != null) ? m_name.hashCode() : 0;
        result = (31 * result) + (m_mapProperties != null ? m_mapProperties.hashCode() : 0);
        return result;
    }
}