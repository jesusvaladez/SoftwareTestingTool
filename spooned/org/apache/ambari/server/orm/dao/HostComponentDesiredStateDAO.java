package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class HostComponentDesiredStateDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity findById(long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> findAll() {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentDesiredStateEntity.findAll", org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity findByServiceComponentAndHost(java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentDesiredStateEntity.findByServiceComponentAndHost", org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity.class);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        query.setParameter("hostName", hostName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity findByIndex(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String componentName, java.lang.Long hostId) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentDesiredStateEntity.findByIndexAndHost", org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        query.setParameter("hostId", hostId);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> findByIndex(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String componentName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentDesiredStateEntity.findByIndex", org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> findByHostsAndCluster(java.util.Collection<java.lang.Long> hostIds, java.lang.Long clusterId) {
        final javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> query = entityManager.createNamedQuery("HostComponentDesiredStateEntity.findByHostsAndCluster", org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity.class);
        final java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(hostIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            query.setParameter("hostIds", chunk);
            query.setParameter("clusterId", clusterId);
            result.addAll(daoUtils.selectList(query));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity) {
        entityManagerProvider.get().refresh(hostComponentDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity) {
        entityManagerProvider.get().persist(hostComponentDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity merge(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity) {
        return entityManagerProvider.get().merge(hostComponentDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostComponentDesiredStateEntity.getHostEntity();
        if (hostEntity == null) {
            throw new java.lang.IllegalStateException(java.lang.String.format("Missing hostEntity for host id %1d", hostComponentDesiredStateEntity.getHostId()));
        }
        hostEntity.removeHostComponentDesiredStateEntity(hostComponentDesiredStateEntity);
        entityManagerProvider.get().remove(hostComponentDesiredStateEntity);
        hostDAO.merge(hostEntity);
    }

    @com.google.inject.persist.Transactional
    public void removeId(long id) {
        remove(findById(id));
    }
}