package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class HostComponentStateDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostComponentStateEntity findById(long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.HostComponentStateEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> findAll() {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentStateEntity.findAll", org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> findByHost(java.lang.String hostName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentStateEntity.findByHost", org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        query.setParameter("hostName", hostName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> findByService(java.lang.String serviceName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentStateEntity.findByService", org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        query.setParameter("serviceName", serviceName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> findByServiceAndComponent(java.lang.String serviceName, java.lang.String componentName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentStateEntity.findByServiceAndComponent", org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostComponentStateEntity findByServiceComponentAndHost(java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentStateEntity.findByServiceComponentAndHost", org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        query.setParameter("hostName", hostName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostComponentStateEntity findByIndex(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String componentName, java.lang.Long hostId) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentStateEntity.findByIndex", org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        query.setParameter("hostId", hostId);
        return daoUtils.selectSingle(query);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity) {
        entityManagerProvider.get().refresh(hostComponentStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity) {
        entityManagerProvider.get().persist(hostComponentStateEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.HostComponentStateEntity merge(org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        hostComponentStateEntity = entityManager.merge(hostComponentStateEntity);
        return hostComponentStateEntity;
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity) {
        entityManagerProvider.get().remove(hostComponentStateEntity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> findByServiceAndComponentAndNotVersion(java.lang.String serviceName, java.lang.String componentName, java.lang.String version) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostComponentStateEntity> query = entityManagerProvider.get().createNamedQuery("HostComponentStateEntity.findByServiceAndComponentAndNotVersion", org.apache.ambari.server.orm.entities.HostComponentStateEntity.class);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        query.setParameter("version", version);
        return daoUtils.selectList(query);
    }
}