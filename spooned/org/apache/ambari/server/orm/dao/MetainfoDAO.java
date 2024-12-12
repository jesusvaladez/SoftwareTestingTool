package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class MetainfoDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.MetainfoEntity findByKey(java.lang.String key) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.MetainfoEntity.class, key);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Collection<org.apache.ambari.server.orm.entities.MetainfoEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.MetainfoEntity> query = entityManagerProvider.get().createQuery("SELECT metainfo FROM MetainfoEntity metainfo", org.apache.ambari.server.orm.entities.MetainfoEntity.class);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.MetainfoEntity metainfoEntity) {
        entityManagerProvider.get().refresh(metainfoEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.MetainfoEntity metainfoEntity) {
        entityManagerProvider.get().persist(metainfoEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.MetainfoEntity merge(org.apache.ambari.server.orm.entities.MetainfoEntity metainfoEntity) {
        return entityManagerProvider.get().merge(metainfoEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.MetainfoEntity metainfoEntity) {
        entityManagerProvider.get().remove(merge(metainfoEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByHostName(java.lang.String key) {
        remove(findByKey(key));
    }
}