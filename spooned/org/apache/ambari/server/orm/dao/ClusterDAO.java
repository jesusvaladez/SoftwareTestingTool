package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
@com.google.inject.Singleton
public class ClusterDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterEntity findById(long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ClusterEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterEntity findByName(java.lang.String clusterName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterEntity> query = entityManagerProvider.get().createNamedQuery("clusterByName", org.apache.ambari.server.orm.entities.ClusterEntity.class);
        query.setParameter("clusterName", clusterName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterEntity findByResourceId(long resourceId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterEntity> query = entityManagerProvider.get().createNamedQuery("clusterByResourceId", org.apache.ambari.server.orm.entities.ClusterEntity.class);
        query.setParameter("resourceId", resourceId);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterEntity> query = entityManagerProvider.get().createNamedQuery("allClusters", org.apache.ambari.server.orm.entities.ClusterEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterConfigEntity findConfig(java.lang.Long configEntityPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class, configEntityPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterConfigEntity findConfig(java.lang.Long clusterId, java.lang.String type, java.lang.String tag) {
        javax.persistence.criteria.CriteriaBuilder cb = entityManagerProvider.get().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> cq = cb.createQuery(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        javax.persistence.criteria.Root<org.apache.ambari.server.orm.entities.ClusterConfigEntity> config = cq.from(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        cq.where(cb.and(cb.equal(config.get("clusterId"), clusterId)), cb.equal(config.get("type"), type), cb.equal(config.get("tag"), tag));
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createQuery(cq);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getEnabledConfigsByTypes(java.lang.Long clusterId, java.util.Collection<java.lang.String> types) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findEnabledConfigsByTypes", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("types", types);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterConfigEntity findConfig(java.lang.Long clusterId, java.lang.String type, java.lang.Long version) {
        javax.persistence.criteria.CriteriaBuilder cb = entityManagerProvider.get().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> cq = cb.createQuery(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        javax.persistence.criteria.Root<org.apache.ambari.server.orm.entities.ClusterConfigEntity> config = cq.from(org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        cq.where(cb.and(cb.equal(config.get("clusterId"), clusterId)), cb.equal(config.get("type"), type), cb.equal(config.get("version"), version));
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createQuery(cq);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.lang.Long findNextConfigVersion(long clusterId, java.lang.String configType) {
        javax.persistence.TypedQuery<java.lang.Number> query = entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findNextConfigVersion", java.lang.Number.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("configType", configType);
        return daoUtils.selectSingle(query).longValue();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getAllConfigurations(java.lang.Long clusterId, org.apache.ambari.server.state.StackId stackId) {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findAllConfigsByStack", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("stack", stackEntity);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getLatestConfigurations(long clusterId, org.apache.ambari.server.state.StackId stackId) {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findLatestConfigsByStack", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("stack", stackEntity);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getLatestConfigurationsWithTypes(long clusterId, org.apache.ambari.server.state.StackId stackId, java.util.Collection<java.lang.String> configTypes) {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        if (configTypes.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return daoUtils.selectList(entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findLatestConfigsByStackWithTypes", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class).setParameter("clusterId", clusterId).setParameter("stack", stackEntity).setParameter("types", configTypes));
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getEnabledConfigsByStack(long clusterId, org.apache.ambari.server.state.StackId stackId) {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findEnabledConfigsByStack", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("stack", stackEntity);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getEnabledConfigs(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findEnabledConfigs", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ClusterConfigEntity findEnabledConfigByType(long clusterId, java.lang.String type) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManagerProvider.get().createNamedQuery("ClusterConfigEntity.findEnabledConfigByType", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("type", type);
        return daoUtils.selectOne(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        entityManagerProvider.get().persist(clusterEntity);
    }

    @com.google.inject.persist.Transactional
    public void createConfig(org.apache.ambari.server.orm.entities.ClusterConfigEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public void removeConfig(org.apache.ambari.server.orm.entities.ClusterConfigEntity entity) {
        entityManagerProvider.get().remove(entity);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        entityManagerProvider.get().refresh(clusterEntity);
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity merge(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        return merge(clusterEntity, false);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ClusterEntity merge(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, boolean flush) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        clusterEntity = entityManager.merge(clusterEntity);
        if (flush) {
            entityManager.flush();
        }
        return clusterEntity;
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ClusterConfigEntity merge(org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity) {
        return merge(clusterConfigEntity, false);
    }

    @com.google.inject.persist.Transactional
    public void merge(java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities) {
        merge(clusterConfigEntities, false);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ClusterConfigEntity merge(org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity, boolean flush) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntityRes = entityManager.merge(clusterConfigEntity);
        if (flush) {
            entityManager.flush();
        }
        return clusterConfigEntityRes;
    }

    @com.google.inject.persist.Transactional
    public void merge(java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities, boolean flush) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigEntities) {
            entityManager.merge(clusterConfigEntity);
        }
        if (flush) {
            entityManager.flush();
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        entityManagerProvider.get().remove(clusterEntity);
    }

    @com.google.inject.persist.Transactional
    public void removeByName(java.lang.String clusterName) {
        remove(findByName(clusterName));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(long id) {
        remove(findById(id));
    }

    @org.apache.ambari.server.orm.RequiresSession
    public boolean isManaged(org.apache.ambari.server.orm.entities.ClusterEntity entity) {
        return entityManagerProvider.get().contains(entity);
    }
}