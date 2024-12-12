package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class MemberDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.MemberEntity findByPK(java.lang.Integer memberPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.MemberEntity.class, memberPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.MemberEntity findByUserAndGroup(java.lang.String userName, java.lang.String groupName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.MemberEntity> query = entityManagerProvider.get().createNamedQuery("memberByUserAndGroup", org.apache.ambari.server.orm.entities.MemberEntity.class);
        query.setParameter("username", userName.toLowerCase());
        query.setParameter("groupname", groupName.toLowerCase());
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.MemberEntity> findAll() {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.MemberEntity> query = entityManagerProvider.get().createQuery("SELECT m FROM MemberEntity m", org.apache.ambari.server.orm.entities.MemberEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.MemberEntity> findAllMembersByUser(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.MemberEntity> query = entityManagerProvider.get().createQuery("SELECT m FROM MemberEntity m WHERE m.user = :userEntity", org.apache.ambari.server.orm.entities.MemberEntity.class);
        query.setParameter("userEntity", userEntity);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.MemberEntity member) {
        create(new java.util.HashSet<>(java.util.Arrays.asList(member)));
    }

    @com.google.inject.persist.Transactional
    public void create(java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> members) {
        for (org.apache.ambari.server.orm.entities.MemberEntity member : members) {
            entityManagerProvider.get().persist(member);
        }
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.MemberEntity merge(org.apache.ambari.server.orm.entities.MemberEntity member) {
        return entityManagerProvider.get().merge(member);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.MemberEntity member) {
        entityManagerProvider.get().remove(merge(member));
    }

    @com.google.inject.persist.Transactional
    public void remove(java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> members) {
        for (org.apache.ambari.server.orm.entities.MemberEntity member : members) {
            entityManagerProvider.get().remove(entityManagerProvider.get().merge(member));
        }
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Integer memberPK) {
        remove(findByPK(memberPK));
    }
}