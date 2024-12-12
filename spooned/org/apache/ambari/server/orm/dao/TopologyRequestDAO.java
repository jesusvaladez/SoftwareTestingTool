package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class TopologyRequestDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyRequestEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.TopologyRequestEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> findByClusterId(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.TopologyRequestEntity> query = entityManagerProvider.get().createNamedQuery("TopologyRequestEntity.findByClusterId", org.apache.ambari.server.orm.entities.TopologyRequestEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.TopologyRequestEntity.class);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> findAllProvisionRequests() {
        return daoUtils.selectList(entityManagerProvider.get().createNamedQuery("TopologyRequestEntity.findProvisionRequests", org.apache.ambari.server.orm.entities.TopologyRequestEntity.class));
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.TopologyRequestEntity requestEntity) {
        entityManagerProvider.get().persist(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.TopologyRequestEntity merge(org.apache.ambari.server.orm.entities.TopologyRequestEntity requestEntity) {
        return entityManagerProvider.get().merge(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.TopologyRequestEntity requestEntity) {
        entityManagerProvider.get().remove(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Long requestId) {
        remove(findById(requestId));
    }

    @com.google.inject.persist.Transactional
    public void removeAll(java.lang.Long clusterId) {
        java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> clusterTopologyRequests = findByClusterId(clusterId);
        for (org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity : clusterTopologyRequests)
            remove(topologyRequestEntity);

    }
}