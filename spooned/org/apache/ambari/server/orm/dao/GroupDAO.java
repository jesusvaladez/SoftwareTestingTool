package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class GroupDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.GroupEntity findByPK(java.lang.Integer groupPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.GroupEntity.class, groupPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.GroupEntity> findAll() {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.GroupEntity> query = entityManagerProvider.get().createQuery("SELECT group_entity FROM GroupEntity group_entity", org.apache.ambari.server.orm.entities.GroupEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.GroupEntity findGroupByName(java.lang.String groupName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.GroupEntity> query = entityManagerProvider.get().createNamedQuery("groupByName", org.apache.ambari.server.orm.entities.GroupEntity.class);
        query.setParameter("groupname", groupName.toLowerCase());
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.GroupEntity findGroupByNameAndType(java.lang.String groupName, org.apache.ambari.server.security.authorization.GroupType groupType) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.GroupEntity> query = entityManagerProvider.get().createQuery("SELECT group_entity FROM GroupEntity group_entity WHERE group_entity.groupType=:type AND lower(group_entity.groupName)=lower(:name)", org.apache.ambari.server.orm.entities.GroupEntity.class);
        query.setParameter("type", groupType);
        query.setParameter("name", groupName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.GroupEntity> findGroupsByPrincipal(java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalList) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.GroupEntity> query = entityManagerProvider.get().createQuery("SELECT grp FROM GroupEntity grp WHERE grp.principal IN :principalList", org.apache.ambari.server.orm.entities.GroupEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.GroupEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(principalList, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            query.setParameter("principalList", chunk);
            result.addAll(daoUtils.selectList(query));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.GroupEntity findGroupByPrincipal(org.apache.ambari.server.orm.entities.PrincipalEntity principal) {
        if (principal == null) {
            return null;
        }
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.GroupEntity> query = entityManagerProvider.get().createQuery("SELECT group_entity FROM GroupEntity group_entity WHERE group_entity.principal.id=:principalId", org.apache.ambari.server.orm.entities.GroupEntity.class);
        query.setParameter("principalId", principal.getId());
        return daoUtils.selectSingle(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.GroupEntity group) {
        create(new java.util.HashSet<>(java.util.Arrays.asList(group)));
    }

    @com.google.inject.persist.Transactional
    public void create(java.util.Set<org.apache.ambari.server.orm.entities.GroupEntity> groups) {
        for (org.apache.ambari.server.orm.entities.GroupEntity group : groups) {
            group.setGroupName(group.getGroupName().toLowerCase());
            entityManagerProvider.get().persist(group);
        }
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.GroupEntity merge(org.apache.ambari.server.orm.entities.GroupEntity group) {
        group.setGroupName(group.getGroupName().toLowerCase());
        return entityManagerProvider.get().merge(group);
    }

    @com.google.inject.persist.Transactional
    public void merge(java.util.Set<org.apache.ambari.server.orm.entities.GroupEntity> groups) {
        for (org.apache.ambari.server.orm.entities.GroupEntity group : groups) {
            group.setGroupName(group.getGroupName().toLowerCase());
            entityManagerProvider.get().merge(group);
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.GroupEntity group) {
        entityManagerProvider.get().remove(merge(group));
        entityManagerProvider.get().getEntityManagerFactory().getCache().evictAll();
    }

    @com.google.inject.persist.Transactional
    public void remove(java.util.Set<org.apache.ambari.server.orm.entities.GroupEntity> groups) {
        for (org.apache.ambari.server.orm.entities.GroupEntity groupEntity : groups) {
            entityManagerProvider.get().remove(entityManagerProvider.get().merge(groupEntity));
        }
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Integer groupPK) {
        remove(findByPK(groupPK));
    }
}