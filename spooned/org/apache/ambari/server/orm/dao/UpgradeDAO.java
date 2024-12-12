package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class UpgradeDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findAll", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> findUpgrades(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findAllForCluster", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        query.setParameter("clusterId", java.lang.Long.valueOf(clusterId));
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeEntity findUpgrade(long upgradeId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findUpgrade", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        query.setParameter("upgradeId", java.lang.Long.valueOf(upgradeId));
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeEntity findUpgradeByRequestId(java.lang.Long requestId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findUpgradeByRequestId", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        query.setParameter("requestId", requestId);
        return daoUtils.selectSingle(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.UpgradeEntity entity) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(entity);
    }

    @com.google.inject.persist.Transactional
    public void removeAll(long clusterId) {
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> entities = findUpgrades(clusterId);
        for (org.apache.ambari.server.orm.entities.UpgradeEntity entity : entities) {
            entityManagerProvider.get().remove(entity);
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeGroupEntity findUpgradeGroup(java.lang.Long groupId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> query = entityManagerProvider.get().createQuery("SELECT p FROM UpgradeGroupEntity p WHERE p.upgradeGroupId = :groupId", org.apache.ambari.server.orm.entities.UpgradeGroupEntity.class);
        query.setParameter("groupId", groupId);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeItemEntity findUpgradeItemByRequestAndStage(java.lang.Long requestId, java.lang.Long stageId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeItemEntity> query = entityManagerProvider.get().createQuery("SELECT p FROM UpgradeItemEntity p WHERE p.stageId = :stageId AND p.upgradeGroupEntity.upgradeEntity.requestId = :requestId", org.apache.ambari.server.orm.entities.UpgradeItemEntity.class);
        query.setParameter("requestId", requestId);
        query.setParameter("stageId", stageId);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeEntity findLastUpgradeForCluster(long clusterId, org.apache.ambari.server.stack.upgrade.Direction direction) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findLatestForClusterInDirection", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        query.setMaxResults(1);
        query.setParameter("clusterId", clusterId);
        query.setParameter("direction", direction);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeEntity findLastUpgradeOrDowngradeForCluster(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findLatestForCluster", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        query.setMaxResults(1);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeEntity findRevertable(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findRevertable", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        query.setMaxResults(1);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.UpgradeEntity findRevertableUsingJPQL(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.UpgradeEntity> query = entityManagerProvider.get().createNamedQuery("UpgradeEntity.findRevertableUsingJPQL", org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        query.setMaxResults(1);
        query.setParameter("clusterId", clusterId);
        query.setParameter("revertableTypes", org.apache.ambari.spi.RepositoryType.REVERTABLE);
        return daoUtils.selectSingle(query);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.UpgradeEntity merge(org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity) {
        return entityManagerProvider.get().merge(upgradeEntity);
    }
}