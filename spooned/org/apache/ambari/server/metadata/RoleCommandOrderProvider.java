package org.apache.ambari.server.metadata;
public interface RoleCommandOrderProvider {
    org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder(org.apache.ambari.server.state.Cluster cluster);

    org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder(java.lang.Long clusterId);
}