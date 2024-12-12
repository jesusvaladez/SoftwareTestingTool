package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class HostDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostEntity findById(long hostId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.HostEntity.class, hostId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostEntity findByName(java.lang.String hostName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostEntity> query = entityManagerProvider.get().createNamedQuery("HostEntity.findByHostName", org.apache.ambari.server.orm.entities.HostEntity.class);
        query.setParameter("hostName", hostName);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostEntity> query = entityManagerProvider.get().createQuery("SELECT host FROM HostEntity host", org.apache.ambari.server.orm.entities.HostEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException e) {
            return java.util.Collections.emptyList();
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostEntity> findByStage(org.apache.ambari.server.orm.entities.StageEntity stageEntity) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostEntity> query = entityManagerProvider.get().createQuery("SELECT host FROM HostEntity host " + ((((("WHERE host.hostName IN (" + "SELECT DISTINCT host.hostName ") + "FROM HostEntity host ") + "JOIN host.hostRoleCommandEntities command ") + "JOIN command.stage stage ") + "WHERE stage=:stageEntity)"), org.apache.ambari.server.orm.entities.HostEntity.class);
        query.setParameter("stageEntity", stageEntity);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException e) {
            return java.util.Collections.emptyList();
        }
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        entityManagerProvider.get().refresh(hostEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        entityManagerProvider.get().persist(hostEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.HostEntity merge(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        return entityManagerProvider.get().merge(hostEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        entityManagerProvider.get().remove(hostEntity);
    }

    public java.util.List<java.lang.String> getHostNamesByHostIds(java.util.List<java.lang.Long> hostIds) {
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
        if (hostIds != null) {
            for (java.lang.Long hostId : hostIds) {
                org.apache.ambari.server.orm.entities.HostEntity hostEntity = findById(hostId);
                hostNames.add(hostEntity.getHostName());
            }
        }
        return hostNames;
    }
}