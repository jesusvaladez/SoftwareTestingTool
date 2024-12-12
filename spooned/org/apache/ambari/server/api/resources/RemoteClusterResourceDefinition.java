package org.apache.ambari.server.api.resources;
public class RemoteClusterResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RemoteClusterResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "remote_clusters";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "remote_cluster";
    }
}