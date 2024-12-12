package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class RequestScheduleBatchRequestDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity findByPk(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntityPK batchRequestEntity) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity.class, batchRequestEntity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> findByScheduleId(java.lang.Long scheduleId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> query = entityManagerProvider.get().createNamedQuery("findByScheduleId", org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity.class);
        query.setParameter("id", scheduleId);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity) {
        entityManagerProvider.get().persist(batchRequestEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity merge(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity) {
        return entityManagerProvider.get().merge(batchRequestEntity);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity) {
        entityManagerProvider.get().refresh(batchRequestEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity) {
        entityManagerProvider.get().remove(merge(batchRequestEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPk(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntityPK batchRequestEntityPK) {
        entityManagerProvider.get().remove(findByPk(batchRequestEntityPK));
    }

    @com.google.inject.persist.Transactional
    public void removeByScheduleId(java.lang.Long scheduleId) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("DELETE FROM RequestScheduleBatchRequestEntity batchreq WHERE " + "batchreq.scheduleId = ?1", java.lang.Long.class);
        daoUtils.executeUpdate(query, scheduleId);
        entityManagerProvider.get().flush();
    }
}