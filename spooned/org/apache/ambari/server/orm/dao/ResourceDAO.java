package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ResourceDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ResourceEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ResourceEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ResourceEntity findByResourceTypeId(java.lang.Integer id) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ResourceEntity> query = entityManagerProvider.get().createQuery("SELECT resource FROM ResourceEntity resource WHERE resource.resourceType.id =:resourceTypeId", org.apache.ambari.server.orm.entities.ResourceEntity.class);
        query.setParameter("resourceTypeId", id);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ResourceEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ResourceEntity> query = entityManagerProvider.get().createQuery("SELECT resource FROM ResourceEntity resource", org.apache.ambari.server.orm.entities.ResourceEntity.class);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ResourceEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ResourceEntity merge(org.apache.ambari.server.orm.entities.ResourceEntity entity) {
        return entityManagerProvider.get().merge(entity);
    }

    public org.apache.ambari.server.orm.entities.ResourceEntity findAmbariResource() {
        return findById(org.apache.ambari.server.orm.entities.ResourceEntity.AMBARI_RESOURCE_ID);
    }
}