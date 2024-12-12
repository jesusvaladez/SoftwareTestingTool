package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class KeyValueDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.KeyValueEntity findByKey(java.lang.String key) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.KeyValueEntity.class, key);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Collection<org.apache.ambari.server.orm.entities.KeyValueEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KeyValueEntity> query = entityManagerProvider.get().createQuery("SELECT keyValue FROM KeyValueEntity keyValue", org.apache.ambari.server.orm.entities.KeyValueEntity.class);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.KeyValueEntity keyValueEntity) {
        entityManagerProvider.get().refresh(keyValueEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.KeyValueEntity keyValueEntity) {
        entityManagerProvider.get().persist(keyValueEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.KeyValueEntity merge(org.apache.ambari.server.orm.entities.KeyValueEntity keyValueEntity) {
        return entityManagerProvider.get().merge(keyValueEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.KeyValueEntity keyValueEntity) {
        entityManagerProvider.get().remove(merge(keyValueEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByHostName(java.lang.String key) {
        remove(findByKey(key));
    }
}