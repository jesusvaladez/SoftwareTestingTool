package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
@org.apache.ambari.server.orm.RequiresSession
public class ConfigGroupDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ConfigGroupEntity findByName(java.lang.String groupName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ConfigGroupEntity> query = entityManagerProvider.get().createNamedQuery("configGroupByName", org.apache.ambari.server.orm.entities.ConfigGroupEntity.class);
        query.setParameter("groupName", groupName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ConfigGroupEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ConfigGroupEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ConfigGroupEntity> query = entityManagerProvider.get().createNamedQuery("allConfigGroups", org.apache.ambari.server.orm.entities.ConfigGroupEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupEntity> findAllByTag(java.lang.String tag) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ConfigGroupEntity> query = entityManagerProvider.get().createNamedQuery("configGroupsByTag", org.apache.ambari.server.orm.entities.ConfigGroupEntity.class);
        query.setParameter("tagName", tag);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) {
        entityManagerProvider.get().persist(configGroupEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ConfigGroupEntity merge(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) {
        return entityManagerProvider.get().merge(configGroupEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) {
        entityManagerProvider.get().remove(merge(configGroupEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Long id) {
        entityManagerProvider.get().remove(findById(id));
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) {
        entityManagerProvider.get().refresh(configGroupEntity);
    }
}