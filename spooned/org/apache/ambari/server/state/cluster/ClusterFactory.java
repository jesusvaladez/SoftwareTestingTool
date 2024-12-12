package org.apache.ambari.server.state.cluster;
public interface ClusterFactory {
    org.apache.ambari.server.state.Cluster create(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity);
}