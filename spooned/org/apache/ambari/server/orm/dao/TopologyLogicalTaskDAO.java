package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class TopologyLogicalTaskDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<java.lang.Long> findHostTaskIdsByPhysicalTaskIds(java.util.Set<java.lang.Long> physicalTaskIds) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        final java.util.Set<java.lang.Long> result = new java.util.HashSet<>();
        final javax.persistence.TypedQuery<java.lang.Long> topologyHostTaskQuery = entityManager.createNamedQuery("TopologyLogicalTaskEntity.findHostTaskIdsByPhysicalTaskIds", java.lang.Long.class);
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(physicalTaskIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            topologyHostTaskQuery.setParameter("physicalTaskIds", chunk);
            result.addAll(daoUtils.selectList(topologyHostTaskQuery));
            return 0;
        });
        return com.google.common.collect.Sets.newHashSet(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity logicalTaskEntity) {
        entityManagerProvider.get().persist(logicalTaskEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity merge(org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity logicalTaskEntity) {
        return entityManagerProvider.get().merge(logicalTaskEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity logicalTaskEntity) {
        entityManagerProvider.get().remove(logicalTaskEntity);
    }
}