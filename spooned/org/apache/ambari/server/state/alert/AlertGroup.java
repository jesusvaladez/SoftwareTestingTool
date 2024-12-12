package org.apache.ambari.server.state.alert;
import org.codehaus.jackson.annotate.JsonProperty;
public class AlertGroup {
    private long m_id;

    private java.lang.String m_name;

    private long m_clusterId;

    private boolean m_isDefault;

    @org.codehaus.jackson.annotate.JsonProperty("id")
    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    @org.codehaus.jackson.annotate.JsonProperty("name")
    public java.lang.String getName() {
        return m_name;
    }

    public void setName(java.lang.String name) {
        m_name = name;
    }

    @org.codehaus.jackson.annotate.JsonProperty("cluster_id")
    public long getClusterId() {
        return m_clusterId;
    }

    public void setClusterName(long clusterId) {
        m_clusterId = clusterId;
    }

    @org.codehaus.jackson.annotate.JsonProperty("default")
    public boolean isDefault() {
        return m_isDefault;
    }

    public void setDefault(boolean isDefault) {
        m_isDefault = isDefault;
    }
}