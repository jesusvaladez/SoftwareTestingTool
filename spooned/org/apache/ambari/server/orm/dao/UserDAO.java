package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class UserDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UserEntity findByPK(java.lang.Integer userPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.UserEntity.class, userPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.UserEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserEntity> query = entityManagerProvider.get().createQuery("SELECT user_entity FROM UserEntity user_entity", org.apache.ambari.server.orm.entities.UserEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UserEntity findUserByName(java.lang.String userName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserEntity> query = entityManagerProvider.get().createNamedQuery("userByName", org.apache.ambari.server.orm.entities.UserEntity.class);
        query.setParameter("username", userName.toLowerCase());
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.UserEntity> findUsersByPrincipal(java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalList) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserEntity> query = entityManagerProvider.get().createQuery("SELECT user_entity FROM UserEntity user_entity WHERE user_entity.principal IN :principalList", org.apache.ambari.server.orm.entities.UserEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(principalList, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            query.setParameter("principalList", chunk);
            result.addAll(daoUtils.selectList(query));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UserEntity findUserByPrincipal(org.apache.ambari.server.orm.entities.PrincipalEntity principal) {
        if (principal == null) {
            return null;
        }
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UserEntity> query = entityManagerProvider.get().createQuery("SELECT user_entity FROM UserEntity user_entity WHERE user_entity.principal.id=:principalId", org.apache.ambari.server.orm.entities.UserEntity.class);
        query.setParameter("principalId", principal.getId());
        return daoUtils.selectSingle(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.UserEntity user) {
        create(new java.util.HashSet<>(java.util.Collections.singleton(user)));
    }

    @com.google.inject.persist.Transactional
    public void create(java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> users) {
        for (org.apache.ambari.server.orm.entities.UserEntity user : users) {
            entityManagerProvider.get().persist(user);
        }
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.UserEntity merge(org.apache.ambari.server.orm.entities.UserEntity user) {
        return entityManagerProvider.get().merge(user);
    }

    @com.google.inject.persist.Transactional
    public void merge(java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> users) {
        for (org.apache.ambari.server.orm.entities.UserEntity user : users) {
            entityManagerProvider.get().merge(user);
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.UserEntity user) {
        entityManagerProvider.get().remove(merge(user));
        entityManagerProvider.get().getEntityManagerFactory().getCache().evictAll();
    }

    @com.google.inject.persist.Transactional
    public void remove(java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> users) {
        for (org.apache.ambari.server.orm.entities.UserEntity userEntity : users) {
            entityManagerProvider.get().remove(entityManagerProvider.get().merge(userEntity));
        }
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Integer userPK) {
        remove(findByPK(userPK));
    }
}