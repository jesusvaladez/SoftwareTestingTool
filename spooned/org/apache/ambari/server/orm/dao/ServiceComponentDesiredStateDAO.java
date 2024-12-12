package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections.MapUtils;
@com.google.inject.Singleton
public class ServiceComponentDesiredStateDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity findById(long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> query = entityManagerProvider.get().createQuery("SELECT scd from ServiceComponentDesiredStateEntity scd", org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity.class);
        try {
            return query.getResultList();
        } catch (javax.persistence.NoResultException ignored) {
        }
        return null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity findByName(long clusterId, java.lang.String serviceName, java.lang.String componentName) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> query = entityManager.createNamedQuery("ServiceComponentDesiredStateEntity.findByName", org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity entity = null;
        java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> entities = daoUtils.selectList(query);
        if ((null != entities) && (!entities.isEmpty())) {
            entity = entities.get(0);
        }
        return entity;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> findByNames(java.util.Map<java.lang.Long, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> serviceComponentDesiredStates) {
        if (org.apache.commons.collections.MapUtils.isEmpty(serviceComponentDesiredStates)) {
            return java.util.Collections.emptyList();
        }
        javax.persistence.criteria.CriteriaBuilder cb = entityManagerProvider.get().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> cq = cb.createQuery(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity.class);
        javax.persistence.criteria.Root<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> desiredStates = cq.from(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity.class);
        java.util.List<javax.persistence.criteria.Predicate> clusters = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.Long, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> cluster : serviceComponentDesiredStates.entrySet()) {
            java.util.List<javax.persistence.criteria.Predicate> services = new java.util.ArrayList<>();
            for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> service : cluster.getValue().entrySet()) {
                services.add(cb.and(cb.equal(desiredStates.get("serviceName"), service.getKey()), desiredStates.get("componentName").in(service.getValue())));
            }
            clusters.add(cb.and(cb.equal(desiredStates.get("clusterId"), cluster.getKey()), cb.or(services.toArray(new javax.persistence.criteria.Predicate[services.size()]))));
        }
        cq.where(cb.or(clusters.toArray(new javax.persistence.criteria.Predicate[clusters.size()])));
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> query = entityManagerProvider.get().createQuery(cq);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        entityManagerProvider.get().refresh(serviceComponentDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        entityManagerProvider.get().persist(serviceComponentDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity merge(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        return entityManagerProvider.get().merge(serviceComponentDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        entityManagerProvider.get().remove(serviceComponentDesiredStateEntity);
    }

    @com.google.inject.persist.Transactional
    public void removeByName(long clusterId, java.lang.String serviceName, java.lang.String componentName) {
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity entity = findByName(clusterId, serviceName, componentName);
        if (null != entity) {
            entityManagerProvider.get().remove(entity);
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> findVersions(long clusterId, java.lang.String serviceName, java.lang.String componentName) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> query = entityManager.createNamedQuery("ServiceComponentVersionEntity.findByComponent", org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity findVersion(long clusterId, java.lang.String serviceName, java.lang.String componentName, java.lang.String version) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> query = entityManager.createNamedQuery("ServiceComponentVersionEntity.findByComponentAndVersion", org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        query.setParameter("repoVersion", version);
        return daoUtils.selectSingle(query);
    }
}