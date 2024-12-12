package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlElement;
public class AutoDeployInfo {
    private boolean m_enabled = true;

    @javax.xml.bind.annotation.XmlElement(name = "co-locate")
    private java.lang.String m_coLocate;

    public void setEnabled(boolean enabled) {
        m_enabled = enabled;
    }

    public boolean isEnabled() {
        return m_enabled;
    }

    public void setCoLocate(java.lang.String coLocate) {
        m_coLocate = coLocate;
    }

    public java.lang.String getCoLocate() {
        return m_coLocate;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.AutoDeployInfo that = ((org.apache.ambari.server.state.AutoDeployInfo) (o));
        if (m_enabled != that.m_enabled)
            return false;

        if (m_coLocate != null ? !m_coLocate.equals(that.m_coLocate) : that.m_coLocate != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (m_enabled) ? 1 : 0;
        result = (31 * result) + (m_coLocate != null ? m_coLocate.hashCode() : 0);
        return result;
    }
}