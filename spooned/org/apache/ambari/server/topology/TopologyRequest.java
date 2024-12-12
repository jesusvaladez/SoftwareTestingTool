package org.apache.ambari.server.topology;
public interface TopologyRequest {
    enum Type {

        PROVISION,
        SCALE,
        EXPORT;}

    java.lang.Long getClusterId();

    org.apache.ambari.server.topology.TopologyRequest.Type getType();

    org.apache.ambari.server.topology.Blueprint getBlueprint();

    org.apache.ambari.server.topology.Configuration getConfiguration();

    java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> getHostGroupInfo();

    java.lang.String getDescription();
}