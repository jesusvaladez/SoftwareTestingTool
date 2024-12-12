package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class PermissionDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity) {
        entityManagerProvider.get().persist(permissionEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.PermissionEntity merge(org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity) {
        return entityManagerProvider.get().merge(permissionEntity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PermissionEntity findById(java.lang.Integer id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.PermissionEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PermissionEntity findByName(java.lang.String name) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PermissionEntity> query = entityManagerProvider.get().createNamedQuery("PermissionEntity.findByName", org.apache.ambari.server.orm.entities.PermissionEntity.class);
        query.setParameter("permissionName", name);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PermissionEntity> findPermissionsByPrincipal(java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalList) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PermissionEntity> query = entityManagerProvider.get().createNamedQuery("PermissionEntity.findByPrincipals", org.apache.ambari.server.orm.entities.PermissionEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.PermissionEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(principalList, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            query.setParameter("principalList", chunk);
            result.addAll(daoUtils.selectList(query));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PermissionEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PermissionEntity> query = entityManagerProvider.get().createQuery("SELECT p FROM PermissionEntity p", org.apache.ambari.server.orm.entities.PermissionEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PermissionEntity findPermissionByNameAndType(java.lang.String name, org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType) {
        if (name.equals(org.apache.ambari.server.orm.entities.PermissionEntity.VIEW_USER_PERMISSION_NAME)) {
            return findViewUsePermission();
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PermissionEntity> query = entityManagerProvider.get().createQuery("SELECT p FROM PermissionEntity p WHERE p.permissionName=:permissionname AND p.resourceType=:resourcetype", org.apache.ambari.server.orm.entities.PermissionEntity.class);
        query.setParameter("permissionname", name);
        query.setParameter("resourcetype", resourceType);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PermissionEntity findAmbariAdminPermission() {
        return findById(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PermissionEntity findViewUsePermission() {
        return findById(org.apache.ambari.server.orm.entities.PermissionEntity.VIEW_USER_PERMISSION);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PermissionEntity findClusterOperatePermission() {
        return findById(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_ADMINISTRATOR_PERMISSION);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PermissionEntity findClusterReadPermission() {
        return findById(org.apache.ambari.server.orm.entities.PermissionEntity.CLUSTER_USER_PERMISSION);
    }
}