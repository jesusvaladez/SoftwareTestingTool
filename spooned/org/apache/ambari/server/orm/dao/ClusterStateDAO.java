package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
@com.google.inject.Singleton
public class ClusterStateDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterStateEntity findByPK(long clusterId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ClusterStateEntity.class, clusterId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterStateEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.ClusterStateEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity) {
        entityManagerProvider.get().refresh(clusterStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity) {
        entityManagerProvider.get().persist(clusterStateEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ClusterStateEntity merge(org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity) {
        return entityManagerProvider.get().merge(clusterStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity) {
        entityManagerProvider.get().remove(merge(clusterStateEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(long clusterId) {
        remove(findByPK(clusterId));
    }
}