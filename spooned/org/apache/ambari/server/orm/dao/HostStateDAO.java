package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
@com.google.inject.Singleton
public class HostStateDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostStateEntity findByHostId(java.lang.Long hostId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.HostStateEntity.class, hostId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostStateEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.HostStateEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        entityManagerProvider.get().refresh(hostStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        entityManagerProvider.get().persist(hostStateEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.HostStateEntity merge(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        return entityManagerProvider.get().merge(hostStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        entityManagerProvider.get().remove(merge(hostStateEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByHostId(java.lang.Long hostId) {
        remove(findByHostId(hostId));
    }
}