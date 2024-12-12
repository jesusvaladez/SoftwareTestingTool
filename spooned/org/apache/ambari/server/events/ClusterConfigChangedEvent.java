package org.apache.ambari.server.events;
public class ClusterConfigChangedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private java.lang.String m_clusterName;

    private java.lang.String m_configType;

    private java.lang.String m_versionTag;

    private java.lang.Long m_version;

    public ClusterConfigChangedEvent(java.lang.String clusterName, java.lang.String configType, java.lang.String versionTag, java.lang.Long version) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.CLUSTER_CONFIG_CHANGED);
        m_clusterName = clusterName;
        m_configType = configType;
        m_versionTag = versionTag;
        m_version = version;
    }

    public java.lang.String getClusterName() {
        return m_clusterName;
    }

    public java.lang.String getConfigType() {
        return m_configType;
    }

    public java.lang.String getVersionTag() {
        return m_versionTag;
    }

    public java.lang.Long getVersion() {
        return m_version;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("ClusterConfigChangedEvent{");
        buffer.append("clusterName=").append(getClusterName());
        buffer.append(", configType=").append(getConfigType());
        buffer.append(", versionTag=").append(getVersionTag());
        buffer.append(", version=").append(getVersion());
        buffer.append("}");
        return buffer.toString();
    }
}