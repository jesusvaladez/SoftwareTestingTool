package org.apache.ambari.server.state;
public interface ServiceFactory {
    org.apache.ambari.server.state.Service createNew(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion);

    org.apache.ambari.server.state.Service createExisting(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.ClusterServiceEntity serviceEntity);
}