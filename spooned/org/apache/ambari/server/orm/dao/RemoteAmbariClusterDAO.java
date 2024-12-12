package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class RemoteAmbariClusterDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity> query = entityManagerProvider.get().createNamedQuery("allRemoteAmbariClusters", org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.class);
        return query.getResultList();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity findByName(java.lang.String clusterName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity> query = entityManagerProvider.get().createNamedQuery("remoteAmbariClusterByName", org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.class);
        query.setParameter("clusterName", clusterName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity findById(java.lang.Long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity> query = entityManagerProvider.get().createNamedQuery("remoteAmbariClusterById", org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectSingle(query);
    }

    @com.google.inject.persist.Transactional
    public void save(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public void update(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity) {
        entityManagerProvider.get().merge(entity);
    }

    @com.google.inject.persist.Transactional
    public void delete(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity clusterEntity) {
        entityManagerProvider.get().remove(clusterEntity);
    }
}