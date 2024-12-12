package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class PrivilegeDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PrivilegeEntity findById(java.lang.Integer id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.PrivilegeEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrivilegeEntity> query = entityManagerProvider.get().createQuery("SELECT privilege FROM PrivilegeEntity privilege", org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> findByResourceId(java.lang.Long id) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrivilegeEntity> query = entityManagerProvider.get().createQuery("SELECT privilege FROM PrivilegeEntity privilege WHERE privilege.resource.id = :resource_id", org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        query.setParameter("resource_id", id);
        return daoUtils.selectList(query);
    }

    public boolean exists(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) {
        return exists(entity.getPrincipal(), entity.getResource(), entity.getPermission());
    }

    @org.apache.ambari.server.orm.RequiresSession
    public boolean exists(org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity, org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrivilegeEntity> query = entityManagerProvider.get().createQuery("SELECT privilege FROM PrivilegeEntity privilege WHERE privilege.principal = :principal AND privilege.resource = :resource AND privilege.permission = :permission", org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        query.setParameter("principal", principalEntity);
        query.setParameter("resource", resourceEntity);
        query.setParameter("permission", permissionEntity);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = daoUtils.selectList(query);
        return !((privilegeEntities == null) || privilegeEntities.isEmpty());
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> findAllByPrincipal(java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalList) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrivilegeEntity> query = entityManagerProvider.get().createQuery("SELECT privilege FROM PrivilegeEntity privilege WHERE privilege.principal IN :principalList", org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(principalList, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            query.setParameter("principalList", chunk);
            result.addAll(daoUtils.selectList(query));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.PrivilegeEntity merge(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) {
        return entityManagerProvider.get().merge(entity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) {
        entityManagerProvider.get().remove(merge(entity));
    }

    @com.google.inject.persist.Transactional
    public void detach(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) {
        entityManagerProvider.get().detach(entity);
    }
}