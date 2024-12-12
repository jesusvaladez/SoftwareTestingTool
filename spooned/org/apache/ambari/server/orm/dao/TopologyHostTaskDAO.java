package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class TopologyHostTaskDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyHostTaskEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.TopologyHostTaskEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> findByHostRequest(java.lang.Long id) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> query = entityManagerProvider.get().createNamedQuery("TopologyHostTaskEntity.findByHostRequest", org.apache.ambari.server.orm.entities.TopologyHostTaskEntity.class);
        query.setParameter("hostRequestId", id);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<java.lang.Long> findHostRequestIdsByHostTaskIds(java.util.Set<java.lang.Long> hostTaskIds) {
        final java.util.Set<java.lang.Long> result = new java.util.HashSet<>();
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        final javax.persistence.TypedQuery<java.lang.Long> topologyHostTaskQuery = entityManager.createNamedQuery("TopologyLogicalTaskEntity.findHostRequestIdsByHostTaskIds", java.lang.Long.class);
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(hostTaskIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            topologyHostTaskQuery.setParameter("hostTaskIds", chunk);
            result.addAll(daoUtils.selectList(topologyHostTaskQuery));
            return 0;
        });
        return com.google.common.collect.Sets.newHashSet(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyHostTaskEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.TopologyHostTaskEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.TopologyHostTaskEntity requestEntity) {
        entityManagerProvider.get().persist(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.TopologyHostTaskEntity merge(org.apache.ambari.server.orm.entities.TopologyHostTaskEntity requestEntity) {
        return entityManagerProvider.get().merge(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.TopologyHostTaskEntity requestEntity) {
        entityManagerProvider.get().remove(requestEntity);
    }
}