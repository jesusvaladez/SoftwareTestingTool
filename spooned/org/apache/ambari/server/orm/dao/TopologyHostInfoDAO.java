package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class TopologyHostInfoDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.TopologyHostInfoEntity findById(long hostInfoId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.TopologyHostInfoEntity.class, hostInfoId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> findByHost(org.apache.ambari.server.orm.entities.HostEntity host) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> query = entityManagerProvider.get().createQuery("SELECT hostInfo FROM TopologyHostInfoEntity hostInfo where hostInfo.hostEntity=:host", org.apache.ambari.server.orm.entities.TopologyHostInfoEntity.class);
        query.setParameter("host", host);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> query = entityManagerProvider.get().createQuery("SELECT hostInfo FROM TopologyHostInfoEntity hostInfo", org.apache.ambari.server.orm.entities.TopologyHostInfoEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException e) {
            return java.util.Collections.emptyList();
        }
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.TopologyHostInfoEntity topologyHostInfoEntity) {
        entityManagerProvider.get().refresh(topologyHostInfoEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.TopologyHostInfoEntity topologyHostInfoEntity) {
        entityManagerProvider.get().persist(topologyHostInfoEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.TopologyHostInfoEntity merge(org.apache.ambari.server.orm.entities.TopologyHostInfoEntity topologyHostInfoEntity) {
        return entityManagerProvider.get().merge(topologyHostInfoEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.TopologyHostInfoEntity topologyHostInfoEntity) {
        entityManagerProvider.get().remove(merge(topologyHostInfoEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByHost(org.apache.ambari.server.orm.entities.HostEntity host) {
        for (org.apache.ambari.server.orm.entities.TopologyHostInfoEntity e : findByHost(host)) {
            entityManagerProvider.get().remove(merge(e));
        }
    }

    public org.apache.ambari.server.orm.entities.TopologyHostInfoEntity findByHostname(java.lang.String hostName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> query = entityManagerProvider.get().createQuery("SELECT hostInfo FROM TopologyHostInfoEntity hostInfo where hostInfo.fqdn=:hostName", org.apache.ambari.server.orm.entities.TopologyHostInfoEntity.class);
        query.setParameter("hostName", hostName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }
}