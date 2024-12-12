package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ViewInstanceDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ViewInstanceEntity findByName(java.lang.String viewName, java.lang.String instanceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ViewInstanceEntity> query = entityManagerProvider.get().createQuery("SELECT instance FROM ViewInstanceEntity instance WHERE instance.viewName = ?1 AND instance.name = ?2", org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        return daoUtils.selectSingle(query, viewName, instanceName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ViewInstanceEntity findByResourceId(long resourceId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ViewInstanceEntity> query = entityManagerProvider.get().createNamedQuery("viewInstanceByResourceId", org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        query.setParameter("resourceId", resourceId);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ViewInstanceEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ViewInstanceEntity> query = entityManagerProvider.get().createNamedQuery("allViewInstances", org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        return query.getResultList();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ResourceEntity findResourceForViewInstance(java.lang.String viewName, java.lang.String instanceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ResourceEntity> query = entityManagerProvider.get().createNamedQuery("getResourceIdByViewInstance", org.apache.ambari.server.orm.entities.ResourceEntity.class);
        query.setParameter("viewName", viewName);
        query.setParameter("instanceName", instanceName);
        return daoUtils.selectOne(query);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity) {
        entityManagerProvider.get().refresh(viewInstanceEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity) {
        entityManagerProvider.get().persist(viewInstanceEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ViewInstanceEntity merge(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity) {
        return entityManagerProvider.get().merge(viewInstanceEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity) {
        entityManagerProvider.get().remove(merge(viewInstanceEntity));
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ViewInstanceDataEntity mergeData(org.apache.ambari.server.orm.entities.ViewInstanceDataEntity viewInstanceDataEntity) {
        return entityManagerProvider.get().merge(viewInstanceDataEntity);
    }

    @com.google.inject.persist.Transactional
    public void removeData(org.apache.ambari.server.orm.entities.ViewInstanceDataEntity viewInstanceDataEntity) {
        entityManagerProvider.get().remove(mergeData(viewInstanceDataEntity));
    }
}