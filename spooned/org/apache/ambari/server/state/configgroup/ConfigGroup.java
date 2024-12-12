package org.apache.ambari.server.state.configgroup;
public interface ConfigGroup {
    java.lang.Long getId();

    java.lang.String getName();

    void setName(java.lang.String name);

    java.lang.String getClusterName();

    java.lang.String getTag();

    void setTag(java.lang.String tag);

    java.lang.String getDescription();

    void setDescription(java.lang.String description);

    java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> getHosts();

    java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> getConfigurations();

    void delete();

    void addHost(org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.controller.ConfigGroupResponse convertToResponse() throws org.apache.ambari.server.AmbariException;

    void setHosts(java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts);

    void setConfigurations(java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs) throws org.apache.ambari.server.AmbariException;

    void removeHost(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException;

    java.lang.String getServiceName();

    void setServiceName(java.lang.String serviceName);
}