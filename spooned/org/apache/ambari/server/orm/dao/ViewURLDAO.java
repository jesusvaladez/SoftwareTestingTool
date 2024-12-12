package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ViewURLDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ViewURLEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ViewURLEntity> query = entityManagerProvider.get().createNamedQuery("allViewUrls", org.apache.ambari.server.orm.entities.ViewURLEntity.class);
        return query.getResultList();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public com.google.common.base.Optional<org.apache.ambari.server.orm.entities.ViewURLEntity> findByName(java.lang.String urlName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ViewURLEntity> query = entityManagerProvider.get().createNamedQuery("viewUrlByName", org.apache.ambari.server.orm.entities.ViewURLEntity.class);
        query.setParameter("urlName", urlName);
        try {
            return com.google.common.base.Optional.of(query.getSingleResult());
        } catch (java.lang.Exception e) {
            return com.google.common.base.Optional.absent();
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public com.google.common.base.Optional<org.apache.ambari.server.orm.entities.ViewURLEntity> findBySuffix(java.lang.String urlSuffix) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ViewURLEntity> query = entityManagerProvider.get().createNamedQuery("viewUrlBySuffix", org.apache.ambari.server.orm.entities.ViewURLEntity.class);
        query.setParameter("urlSuffix", urlSuffix);
        try {
            return com.google.common.base.Optional.of(query.getSingleResult());
        } catch (javax.persistence.NoResultException e) {
            return com.google.common.base.Optional.absent();
        }
    }

    @com.google.inject.persist.Transactional
    public void save(org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity) {
        entityManagerProvider.get().persist(urlEntity);
        entityManagerProvider.get().flush();
    }

    @com.google.inject.persist.Transactional
    public void update(org.apache.ambari.server.orm.entities.ViewURLEntity entity) {
        entityManagerProvider.get().merge(entity);
        entityManagerProvider.get().flush();
    }

    @com.google.inject.persist.Transactional
    public void delete(org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity) {
        entityManagerProvider.get().remove(urlEntity);
        entityManagerProvider.get().flush();
    }
}