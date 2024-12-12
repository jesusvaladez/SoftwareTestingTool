package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class RequestOperationLevelDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestOperationLevelEntity> findByHostId(java.lang.Long hostId) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RequestOperationLevelEntity> query = entityManagerProvider.get().createNamedQuery("requestOperationLevelByHostId", org.apache.ambari.server.orm.entities.RequestOperationLevelEntity.class);
        query.setParameter("hostId", hostId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestOperationLevelEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.RequestOperationLevelEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.RequestOperationLevelEntity requestOperationLevelEntity) {
        entityManagerProvider.get().refresh(requestOperationLevelEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.RequestOperationLevelEntity requestOperationLevelEntity) {
        entityManagerProvider.get().persist(requestOperationLevelEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RequestOperationLevelEntity merge(org.apache.ambari.server.orm.entities.RequestOperationLevelEntity requestOperationLevelEntity) {
        return entityManagerProvider.get().merge(requestOperationLevelEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.RequestOperationLevelEntity requestOperationLevelEntity) {
        entityManagerProvider.get().remove(merge(requestOperationLevelEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByHostId(java.lang.Long hostId) {
        java.util.Collection<org.apache.ambari.server.orm.entities.RequestOperationLevelEntity> hostRequestOpLevels = this.findByHostId(hostId);
        for (org.apache.ambari.server.orm.entities.RequestOperationLevelEntity hostRequestOpLevel : hostRequestOpLevels) {
            this.remove(hostRequestOpLevel);
        }
    }
}