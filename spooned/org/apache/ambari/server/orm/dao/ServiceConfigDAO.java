package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
@com.google.inject.Singleton
public class ServiceConfigDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceConfigEntity find(java.lang.Long serviceConfigId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class, serviceConfigId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceConfigEntity findByServiceAndVersion(java.lang.String serviceName, java.lang.Long version) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createQuery("SELECT scv FROM ServiceConfigEntity scv " + "WHERE scv.serviceName=?1 AND scv.version=?2", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        return daoUtils.selectOne(query, serviceName, version);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> findByService(java.lang.Long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createQuery("SELECT scv FROM ServiceConfigEntity scv " + "WHERE scv.clusterId=?1 AND scv.serviceName=?2", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        return daoUtils.selectList(query, clusterId, serviceName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getLastServiceConfigVersionsForGroups(java.util.Collection<java.lang.Long> configGroupIds) {
        if ((configGroupIds == null) || configGroupIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        javax.persistence.criteria.CriteriaBuilder cb = entityManagerProvider.get().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<javax.persistence.Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root<org.apache.ambari.server.orm.entities.ServiceConfigEntity> groupVersion = cq.from(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        cq.multiselect(groupVersion.get("groupId").alias("groupId"), cb.max(groupVersion.<java.lang.Long>get("version")).alias("lastVersion"));
        cq.where(groupVersion.get("groupId").in(configGroupIds));
        cq.groupBy(groupVersion.get("groupId"));
        java.util.List<javax.persistence.Tuple> tuples = daoUtils.selectList(entityManagerProvider.get().createQuery(cq));
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> result = new java.util.ArrayList<>();
        for (javax.persistence.Tuple tuple : tuples) {
            javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> sce = cb.createQuery(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
            javax.persistence.criteria.Root<org.apache.ambari.server.orm.entities.ServiceConfigEntity> sceRoot = sce.from(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
            sce.where(cb.and(cb.equal(sceRoot.get("groupId"), tuple.get("groupId")), cb.equal(sceRoot.get("version"), tuple.get("lastVersion"))));
            sce.select(sceRoot);
            result.add(daoUtils.selectSingle(entityManagerProvider.get().createQuery(sce)));
        }
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceConfigEntity getLastServiceConfigVersionsForGroup(java.lang.Long configGroupId) {
        if (configGroupId == null) {
            return null;
        }
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> result = getLastServiceConfigVersionsForGroups(new java.util.ArrayList<>(java.util.Arrays.asList(configGroupId)));
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> getServiceConfigVersionsByConfig(java.lang.Long clusterId, java.lang.String configType, java.lang.Long configVersion) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT scv.version " + ("FROM ServiceConfigEntity scv JOIN scv.clusterConfigEntities cc " + "WHERE cc.clusterId=?1 AND cc.type = ?2 AND cc.version = ?3"), java.lang.Long.class);
        return daoUtils.selectList(query, clusterId, configType, configVersion);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getLastServiceConfigs(java.lang.Long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createNamedQuery("ServiceConfigEntity.findLatestServiceConfigsByCluster", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getLastServiceConfigsForService(java.lang.Long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createNamedQuery("ServiceConfigEntity.findLatestServiceConfigsByService", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceConfigEntity getLastServiceConfigForServiceDefaultGroup(java.lang.Long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createNamedQuery("ServiceConfigEntity.findLatestServiceConfigsByServiceDefaultGroup", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getServiceConfigsForServiceAndStack(java.lang.Long clusterId, org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName) {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createNamedQuery("ServiceConfigEntity.findAllServiceConfigsByStack", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("stack", stackEntity);
        query.setParameter("serviceName", serviceName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getLatestServiceConfigs(java.lang.Long clusterId, org.apache.ambari.server.state.StackId stackId) {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createNamedQuery("ServiceConfigEntity.findLatestServiceConfigsByStack", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("stack", stackEntity);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceConfigEntity getLastServiceConfig(java.lang.Long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createQuery("SELECT scv FROM ServiceConfigEntity scv " + ("WHERE scv.clusterId = ?1 AND scv.serviceName = ?2 " + "ORDER BY scv.createTimestamp DESC"), org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        return daoUtils.selectOne(query, clusterId, serviceName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceConfigEntity findMaxVersion(java.lang.Long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createQuery("SELECT scv FROM ServiceConfigEntity scv " + (("WHERE scv.clusterId=?1 AND scv.serviceName=?2 AND scv.version = (" + "SELECT max(scv2.version) FROM ServiceConfigEntity scv2 ") + "WHERE scv2.clusterId=?1 AND scv2.serviceName=?2)"), org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        return daoUtils.selectSingle(query, clusterId, serviceName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> getServiceConfigs(java.lang.Long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> query = entityManagerProvider.get().createNamedQuery("ServiceConfigEntity.findAll", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.lang.Long findNextServiceConfigVersion(long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<java.lang.Number> query = entityManagerProvider.get().createNamedQuery("ServiceConfigEntity.findNextServiceConfigVersion", java.lang.Number.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        return daoUtils.selectSingle(query).longValue();
    }

    @com.google.inject.persist.Transactional
    public void removeHostFromServiceConfigs(final java.lang.Long hostId) {
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> allServiceConfigs = findAll();
        for (org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity : allServiceConfigs) {
            java.util.List<java.lang.Long> hostIds = serviceConfigEntity.getHostIds();
            if ((hostIds != null) && hostIds.contains(hostId)) {
                org.apache.commons.collections.CollectionUtils.filter(hostIds, new org.apache.commons.collections.Predicate() {
                    @java.lang.Override
                    public boolean evaluate(java.lang.Object arg0) {
                        return !((java.lang.Long) (arg0)).equals(hostId);
                    }
                });
                serviceConfigEntity.setHostIds(hostIds);
            }
        }
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity) {
        entityManagerProvider.get().persist(serviceConfigEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ServiceConfigEntity merge(org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity) {
        return entityManagerProvider.get().merge(serviceConfigEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity) {
        entityManagerProvider.get().remove(merge(serviceConfigEntity));
    }
}