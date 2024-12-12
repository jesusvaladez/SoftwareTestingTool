package org.apache.ambari.server.orm.cache;
public interface ConfigGroupHostMapping {
    java.lang.Long getConfigGroupId();

    java.lang.Long getHostId();

    org.apache.ambari.server.state.Host getHost();

    org.apache.ambari.server.state.configgroup.ConfigGroup getConfigGroup();

    void setConfigGroupId(java.lang.Long configGroupId);

    void setHostId(java.lang.Long setHostId);

    void setHost(org.apache.ambari.server.state.Host host);

    void setConfigGroup(org.apache.ambari.server.state.configgroup.ConfigGroup configGroup);
}