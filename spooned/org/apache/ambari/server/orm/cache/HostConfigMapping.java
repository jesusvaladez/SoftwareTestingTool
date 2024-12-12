package org.apache.ambari.server.orm.cache;
public interface HostConfigMapping {
    java.lang.Long getClusterId();

    void setClusterId(java.lang.Long clusterId);

    java.lang.Long getHostId();

    void setHostId(java.lang.Long hostId);

    java.lang.String getType();

    void setType(java.lang.String type);

    java.lang.Long getCreateTimestamp();

    void setCreateTimestamp(java.lang.Long createTimestamp);

    java.lang.String getVersion();

    void setVersion(java.lang.String version);

    java.lang.String getServiceName();

    void setServiceName(java.lang.String serviceName);

    java.lang.String getUser();

    void setUser(java.lang.String user);

    java.lang.Integer getSelected();

    void setSelected(java.lang.Integer selected);
}