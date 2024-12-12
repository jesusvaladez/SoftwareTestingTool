package org.apache.ambari.server.orm.cache;
public class HostConfigMappingImpl implements org.apache.ambari.server.orm.cache.HostConfigMapping {
    private java.lang.Long clusterId;

    private java.lang.Long hostId;

    private java.lang.String type;

    private java.lang.Long createTimestamp;

    private java.lang.String version;

    private java.lang.String serviceName;

    private java.lang.String user;

    private java.lang.Integer selected;

    public HostConfigMappingImpl(org.apache.ambari.server.orm.cache.HostConfigMapping entry) {
        setClusterId(entry.getClusterId());
        setHostId(entry.getHostId());
        setType(entry.getType());
        setCreateTimestamp(entry.getCreateTimestamp());
        setVersion(entry.getVersion());
        setServiceName(entry.getServiceName());
        setUser(entry.getUser());
        setSelected(entry.getSelected());
    }

    public HostConfigMappingImpl() {
    }

    @java.lang.Override
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    @java.lang.Override
    public void setClusterId(java.lang.Long clusterId) {
        if (clusterId == null)
            throw new java.lang.RuntimeException("ClusterId couldn't be null");

        this.clusterId = clusterId;
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }

    @java.lang.Override
    public void setHostId(java.lang.Long hostId) {
        if (hostId == null)
            throw new java.lang.RuntimeException("HostId couldn't be null");

        this.hostId = hostId;
    }

    @java.lang.Override
    public java.lang.String getType() {
        return type;
    }

    @java.lang.Override
    public void setType(java.lang.String type) {
        if (type == null)
            throw new java.lang.RuntimeException("Type couldn't be null");

        this.type = type;
    }

    @java.lang.Override
    public java.lang.Long getCreateTimestamp() {
        return createTimestamp;
    }

    @java.lang.Override
    public void setCreateTimestamp(java.lang.Long createTimestamp) {
        if (createTimestamp == null)
            throw new java.lang.RuntimeException("CreateTimestamp couldn't be null");

        this.createTimestamp = createTimestamp;
    }

    @java.lang.Override
    public java.lang.String getVersion() {
        return version;
    }

    @java.lang.Override
    public void setVersion(java.lang.String version) {
        if (version == null)
            throw new java.lang.RuntimeException("Version couldn't be null");

        this.version = version;
    }

    @java.lang.Override
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @java.lang.Override
    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @java.lang.Override
    public java.lang.String getUser() {
        return user;
    }

    @java.lang.Override
    public void setUser(java.lang.String user) {
        if (user == null)
            throw new java.lang.RuntimeException("User couldn't be null");

        this.user = user;
    }

    @java.lang.Override
    public java.lang.Integer getSelected() {
        return selected;
    }

    @java.lang.Override
    public void setSelected(java.lang.Integer selected) {
        if (selected == null)
            throw new java.lang.RuntimeException("Selected couldn't be null");

        this.selected = selected;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (clusterId == null ? 0 : clusterId.hashCode());
        result = (prime * result) + (createTimestamp == null ? 0 : createTimestamp.hashCode());
        result = (prime * result) + (hostId == null ? 0 : hostId.hashCode());
        result = (prime * result) + (type == null ? 0 : type.hashCode());
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

        org.apache.ambari.server.orm.cache.HostConfigMappingImpl other = ((org.apache.ambari.server.orm.cache.HostConfigMappingImpl) (obj));
        if (clusterId != null ? !clusterId.equals(other.clusterId) : other.clusterId != null)
            return false;

        if (createTimestamp != null ? !createTimestamp.equals(other.createTimestamp) : other.createTimestamp != null)
            return false;

        if (hostId != null ? !hostId.equals(other.hostId) : other.hostId != null)
            return false;

        if (type != null ? !type.equals(other.type) : other.type != null)
            return false;

        return true;
    }
}