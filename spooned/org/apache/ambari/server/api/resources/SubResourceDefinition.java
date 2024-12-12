package org.apache.ambari.server.api.resources;
public class SubResourceDefinition {
    private org.apache.ambari.server.controller.spi.Resource.Type m_type;

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> m_setForeignKeys;

    private boolean m_isCollection = true;

    public SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type) {
        m_type = type;
    }

    public SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> setForeignKeys, boolean isCollection) {
        m_type = type;
        m_setForeignKeys = setForeignKeys;
        m_isCollection = isCollection;
    }

    public org.apache.ambari.server.controller.spi.Resource.Type getType() {
        return m_type;
    }

    public java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getAdditionalForeignKeys() {
        return m_setForeignKeys == null ? java.util.Collections.emptySet() : m_setForeignKeys;
    }

    public boolean isCollection() {
        return m_isCollection;
    }
}