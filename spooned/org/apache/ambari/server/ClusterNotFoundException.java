package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class ClusterNotFoundException extends org.apache.ambari.server.ObjectNotFoundException {
    public ClusterNotFoundException(java.lang.String clusterName) {
        super("Cluster not found, clusterName=" + clusterName);
    }

    public ClusterNotFoundException(java.lang.Long clusterId) {
        super("Cluster not found, clusterId=" + clusterId);
    }
}