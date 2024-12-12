package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ResourceTypeDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ResourceTypeEntity findById(java.lang.Integer id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ResourceTypeEntity findByName(java.lang.String name) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ResourceTypeEntity> query = entityManagerProvider.get().createQuery("SELECT resourceType FROM ResourceTypeEntity resourceType WHERE resourceType.name = ?1", org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        return daoUtils.selectSingle(query, name);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ResourceTypeEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ResourceTypeEntity> query = entityManagerProvider.get().createQuery("SELECT resourceType FROM ResourceTypeEntity resourceType", org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ResourceTypeEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ResourceTypeEntity merge(org.apache.ambari.server.orm.entities.ResourceTypeEntity entity) {
        return entityManagerProvider.get().merge(entity);
    }
}