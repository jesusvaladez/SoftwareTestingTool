package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class UserAuthenticationDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UserAuthenticationEntity findByPK(java.lang.Long pk) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class, pk);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> query = entityManagerProvider.get().createNamedQuery("UserAuthenticationEntity.findAll", org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> findByType(org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> query = entityManagerProvider.get().createNamedQuery("UserAuthenticationEntity.findByType", org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
        query.setParameter("authenticationType", authenticationType.name());
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> findByTypeAndKey(org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String key) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> query = entityManagerProvider.get().createNamedQuery("UserAuthenticationEntity.findByTypeAndKey", org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
        query.setParameter("authenticationType", authenticationType.name());
        query.setParameter("authenticationKey", key);
        return daoUtils.selectList(query);
    }

    public java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> findByUser(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> query = entityManagerProvider.get().createNamedQuery("UserAuthenticationEntity.findByUser", org.apache.ambari.server.orm.entities.UserAuthenticationEntity.class);
        query.setParameter("userId", userEntity.getUserId());
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public void create(java.util.Set<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> entities) {
        for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : entities) {
            entityManagerProvider.get().persist(entity);
        }
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.UserAuthenticationEntity merge(org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity) {
        return entityManagerProvider.get().merge(entity);
    }

    @com.google.inject.persist.Transactional
    public void merge(java.util.Set<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> entities) {
        for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : entities) {
            entityManagerProvider.get().merge(entity);
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity) {
        entityManagerProvider.get().remove(entity);
    }

    @com.google.inject.persist.Transactional
    public void remove(java.util.Set<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> entities) {
        for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : entities) {
            entityManagerProvider.get().remove(entity);
        }
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Long pk) {
        remove(findByPK(pk));
    }
}