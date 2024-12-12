package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class TopologyHostGroupDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyHostGroupEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.TopologyHostGroupEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyHostGroupEntity findByRequestIdAndName(long topologyRequestId, java.lang.String name) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> query = entityManagerProvider.get().createNamedQuery("TopologyHostGroupEntity.findByRequestIdAndName", org.apache.ambari.server.orm.entities.TopologyHostGroupEntity.class);
        query.setParameter("requestId", topologyRequestId);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.TopologyHostGroupEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity) {
        entityManagerProvider.get().persist(hostGroupEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.TopologyHostGroupEntity merge(org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity) {
        return entityManagerProvider.get().merge(hostGroupEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity) {
        entityManagerProvider.get().remove(hostGroupEntity);
    }
}