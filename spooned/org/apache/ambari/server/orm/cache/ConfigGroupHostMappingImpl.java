package org.apache.ambari.server.orm.cache;
public class ConfigGroupHostMappingImpl implements org.apache.ambari.server.orm.cache.ConfigGroupHostMapping {
    private java.lang.Long configGroupId;

    private java.lang.Long hostId;

    private org.apache.ambari.server.state.Host host;

    private org.apache.ambari.server.state.configgroup.ConfigGroup configGroup;

    @java.lang.Override
    public java.lang.Long getConfigGroupId() {
        return configGroupId;
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Host getHost() {
        return host;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.configgroup.ConfigGroup getConfigGroup() {
        return configGroup;
    }

    @java.lang.Override
    public void setConfigGroupId(java.lang.Long configGroupId) {
        this.configGroupId = configGroupId;
    }

    @java.lang.Override
    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    @java.lang.Override
    public void setHost(org.apache.ambari.server.state.Host host) {
        this.host = host;
    }

    @java.lang.Override
    public void setConfigGroup(org.apache.ambari.server.state.configgroup.ConfigGroup configGroup) {
        this.configGroup = configGroup;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (configGroup == null ? 0 : configGroup.hashCode());
        result = (prime * result) + (configGroupId == null ? 0 : configGroupId.hashCode());
        result = (prime * result) + (host == null ? 0 : host.hashCode());
        result = (prime * result) + (hostId == null ? 0 : hostId.hashCode());
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        org.apache.ambari.server.orm.cache.ConfigGroupHostMappingImpl other = ((org.apache.ambari.server.orm.cache.ConfigGroupHostMappingImpl) (obj));
        if (configGroup != null ? !configGroup.equals(other.configGroup) : other.configGroup != null)
            return false;

        if (configGroupId != null ? !configGroupId.equals(other.configGroupId) : other.configGroupId != null)
            return false;

        if (host != null ? !host.equals(other.host) : other.host != null)
            return false;

        if (hostId != null ? !hostId.equals(other.hostId) : other.hostId != null)
            return false;

        return true;
    }
}