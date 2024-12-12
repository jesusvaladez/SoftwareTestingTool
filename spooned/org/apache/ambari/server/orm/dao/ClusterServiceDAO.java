package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ClusterServiceDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterServiceEntity findByPK(org.apache.ambari.server.orm.entities.ClusterServiceEntityPK clusterServiceEntityPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ClusterServiceEntity.class, clusterServiceEntityPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterServiceEntity findByClusterAndServiceNames(java.lang.String clusterName, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterServiceEntity> query = entityManagerProvider.get().createNamedQuery("clusterServiceByClusterAndServiceNames", org.apache.ambari.server.orm.entities.ClusterServiceEntity.class);
        query.setParameter("clusterName", clusterName);
        query.setParameter("serviceName", serviceName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterServiceEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.ClusterServiceEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity) {
        entityManagerProvider.get().refresh(clusterServiceEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity) {
        entityManagerProvider.get().persist(clusterServiceEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ClusterServiceEntity merge(org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity) {
        return entityManagerProvider.get().merge(clusterServiceEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity) {
        entityManagerProvider.get().remove(merge(clusterServiceEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(org.apache.ambari.server.orm.entities.ClusterServiceEntityPK clusterServiceEntityPK) {
        org.apache.ambari.server.orm.entities.ClusterServiceEntity entity = findByPK(clusterServiceEntityPK);
        entityManagerProvider.get().remove(entity);
    }
}