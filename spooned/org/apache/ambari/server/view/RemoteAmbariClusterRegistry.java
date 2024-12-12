package org.apache.ambari.server.view;
@javax.inject.Singleton
public class RemoteAmbariClusterRegistry {
    private java.util.concurrent.ConcurrentHashMap<java.lang.Long, org.apache.ambari.server.view.RemoteAmbariCluster> clusterMap = new java.util.concurrent.ConcurrentHashMap<>();

    @javax.inject.Inject
    private org.apache.ambari.server.orm.dao.RemoteAmbariClusterDAO remoteAmbariClusterDAO;

    @javax.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    public org.apache.ambari.server.view.RemoteAmbariCluster get(java.lang.Long clusterId) throws java.net.MalformedURLException, org.apache.ambari.server.ClusterNotFoundException {
        org.apache.ambari.server.view.RemoteAmbariCluster remoteAmbariCluster = clusterMap.get(clusterId);
        if (remoteAmbariCluster == null) {
            org.apache.ambari.server.view.RemoteAmbariCluster cluster = getCluster(clusterId);
            org.apache.ambari.server.view.RemoteAmbariCluster oldCluster = clusterMap.putIfAbsent(clusterId, cluster);
            if (oldCluster == null)
                remoteAmbariCluster = cluster;
            else
                remoteAmbariCluster = oldCluster;

        }
        return remoteAmbariCluster;
    }

    private org.apache.ambari.server.view.RemoteAmbariCluster getCluster(java.lang.Long clusterId) throws java.net.MalformedURLException, org.apache.ambari.server.ClusterNotFoundException {
        org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity remoteAmbariClusterEntity = remoteAmbariClusterDAO.findById(clusterId);
        if (remoteAmbariClusterEntity == null) {
            throw new org.apache.ambari.server.ClusterNotFoundException(clusterId);
        }
        org.apache.ambari.server.view.RemoteAmbariCluster remoteAmbariCluster = new org.apache.ambari.server.view.RemoteAmbariCluster(remoteAmbariClusterEntity, configuration);
        return remoteAmbariCluster;
    }

    public void update(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity) {
        remoteAmbariClusterDAO.update(entity);
        clusterMap.remove(entity.getId());
    }

    public void delete(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity) {
        remoteAmbariClusterDAO.delete(entity);
        clusterMap.remove(entity.getId());
    }

    public void saveOrUpdate(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity, boolean update) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        org.apache.ambari.server.view.RemoteAmbariCluster cluster = new org.apache.ambari.server.view.RemoteAmbariCluster(entity, configuration);
        java.util.Set<java.lang.String> services = cluster.getServices();
        if (!cluster.isAmbariOrClusterAdmin()) {
            throw new org.apache.ambari.server.AmbariException("User must be Ambari or Cluster Adminstrator.");
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.RemoteAmbariClusterServiceEntity> serviceEntities = new java.util.ArrayList<>();
        for (java.lang.String service : services) {
            org.apache.ambari.server.orm.entities.RemoteAmbariClusterServiceEntity serviceEntity = new org.apache.ambari.server.orm.entities.RemoteAmbariClusterServiceEntity();
            serviceEntity.setServiceName(service);
            serviceEntity.setCluster(entity);
            serviceEntities.add(serviceEntity);
        }
        entity.setServices(serviceEntities);
        if (update) {
            update(entity);
        } else {
            remoteAmbariClusterDAO.save(entity);
        }
    }
}