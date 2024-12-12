package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class TopologyLogicalRequestDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity requestEntity) {
        entityManagerProvider.get().persist(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity merge(org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity requestEntity) {
        return entityManagerProvider.get().merge(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity requestEntity) {
        entityManagerProvider.get().remove(requestEntity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<java.lang.Long> findRequestIdsByIds(java.util.Set<java.lang.Long> ids) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        final java.util.Set<java.lang.Long> result = new java.util.HashSet<>();
        final javax.persistence.TypedQuery<java.lang.Long> topologyLogicalRequestQuery = entityManager.createNamedQuery("TopologyLogicalRequestEntity.findRequestIds", java.lang.Long.class);
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(ids, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            topologyLogicalRequestQuery.setParameter("ids", chunk);
            result.addAll(daoUtils.selectList(topologyLogicalRequestQuery));
            return 0;
        });
        return com.google.common.collect.Sets.newHashSet(result);
    }
}