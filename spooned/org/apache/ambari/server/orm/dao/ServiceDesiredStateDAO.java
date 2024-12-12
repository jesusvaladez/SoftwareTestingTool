package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ServiceDesiredStateDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity findByPK(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK primaryKey) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity.class, primaryKey);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity> query = entityManagerProvider.get().createQuery("SELECT sd from ServiceDesiredStateEntity sd", org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity) {
        entityManagerProvider.get().refresh(serviceDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity) {
        entityManagerProvider.get().persist(serviceDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity merge(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity) {
        return entityManagerProvider.get().merge(serviceDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity) {
        entityManagerProvider.get().remove(merge(serviceDesiredStateEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK primaryKey) {
        remove(findByPK(primaryKey));
    }
}