package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class RequestScheduleDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RequestScheduleEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.RequestScheduleEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestScheduleEntity> findByStatus(java.lang.String status) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RequestScheduleEntity> query = entityManagerProvider.get().createNamedQuery("reqScheduleByStatus", org.apache.ambari.server.orm.entities.RequestScheduleEntity.class);
        query.setParameter("status", status);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestScheduleEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RequestScheduleEntity> query = entityManagerProvider.get().createNamedQuery("allReqSchedules", org.apache.ambari.server.orm.entities.RequestScheduleEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity) {
        entityManagerProvider.get().persist(requestScheduleEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RequestScheduleEntity merge(org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity) {
        return entityManagerProvider.get().merge(requestScheduleEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity) {
        entityManagerProvider.get().remove(merge(requestScheduleEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Long id) {
        entityManagerProvider.get().remove(findById(id));
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity) {
        entityManagerProvider.get().refresh(requestScheduleEntity);
    }
}