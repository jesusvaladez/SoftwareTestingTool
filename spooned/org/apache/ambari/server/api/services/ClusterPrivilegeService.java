package org.apache.ambari.server.api.services;
public class ClusterPrivilegeService extends org.apache.ambari.server.api.services.PrivilegeService {
    private java.lang.String clusterName;

    public ClusterPrivilegeService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @java.lang.Override
    protected org.apache.ambari.server.api.resources.ResourceInstance createPrivilegeResource(java.lang.String privilegeId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege, privilegeId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege, mapIds);
    }
}