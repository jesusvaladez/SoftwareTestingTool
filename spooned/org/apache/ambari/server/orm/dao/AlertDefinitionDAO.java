package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class AlertDefinitionDAO {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);

    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO alertsDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO dispatchDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory alertDefinitionFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity findById(long definitionId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class, definitionId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity findByName(long clusterId, java.lang.String definitionName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findByName", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("definitionName", definitionName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findAll", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findAll(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findAllInCluster", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findAllEnabled(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findAllEnabledInCluster", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findByIds(java.util.List<java.lang.Long> definitionIds) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findByIds", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("definitionIds", definitionIds);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findByService(long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findByService", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findByServiceMaster(long clusterId, java.util.Set<java.lang.String> services) {
        if ((null == services) || (services.size() == 0)) {
            return java.util.Collections.emptyList();
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findByServiceMaster", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("services", services);
        query.setParameter("scope", org.apache.ambari.server.state.alert.Scope.SERVICE);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findByServiceComponent(long clusterId, java.lang.String serviceName, java.lang.String componentName) {
        if ((null == serviceName) || (null == componentName)) {
            return java.util.Collections.emptyList();
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findByServiceAndComponent", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        query.setParameter("componentName", componentName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findAgentScoped(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> query = entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findByServiceAndComponent", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", org.apache.ambari.server.controller.RootService.AMBARI.name());
        query.setParameter("componentName", org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name());
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> findBySourceType(java.lang.Long clusterId, org.apache.ambari.server.state.alert.SourceType sourceType) {
        return daoUtils.selectList(entityManagerProvider.get().createNamedQuery("AlertDefinitionEntity.findBySourceType", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class).setParameter("clusterId", clusterId).setParameter("sourceType", sourceType));
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition) throws org.apache.ambari.server.AmbariException {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(alertDefinition);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = dispatchDao.findDefaultServiceGroup(alertDefinition.getClusterId(), alertDefinition.getServiceName());
        if (null == group) {
            java.lang.String serviceName = alertDefinition.getServiceName();
            group = dispatchDao.createDefaultGroup(alertDefinition.getClusterId(), serviceName);
        }
        group.addAlertDefinition(alertDefinition);
        org.apache.ambari.server.events.AlertGroupsUpdateEvent alertGroupsUpdateEvent = new org.apache.ambari.server.events.AlertGroupsUpdateEvent(java.util.Collections.singletonList(new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(group)), org.apache.ambari.server.events.UpdateEventType.UPDATE);
        STOMPUpdatePublisher.publish(alertGroupsUpdateEvent);
        dispatchDao.merge(group);
        org.apache.ambari.server.state.alert.AlertDefinition coerced = alertDefinitionFactory.coerce(alertDefinition);
        if (null != coerced) {
            org.apache.ambari.server.events.AlertDefinitionRegistrationEvent event = new org.apache.ambari.server.events.AlertDefinitionRegistrationEvent(alertDefinition.getClusterId(), coerced);
            eventPublisher.publish(event);
        } else {
            org.apache.ambari.server.orm.dao.AlertDefinitionDAO.LOG.warn("Unable to broadcast alert registration event for {}", alertDefinition.getDefinitionName());
        }
        entityManager.refresh(alertDefinition);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition) {
        entityManagerProvider.get().refresh(alertDefinition);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity merge(org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition) {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = entityManagerProvider.get().merge(alertDefinition);
        org.apache.ambari.server.state.alert.AlertDefinition definition = alertDefinitionFactory.coerce(entity);
        org.apache.ambari.server.events.AlertDefinitionChangedEvent event = new org.apache.ambari.server.events.AlertDefinitionChangedEvent(alertDefinition.getClusterId(), definition);
        eventPublisher.publish(event);
        return entity;
    }

    public void createOrUpdate(org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition) throws org.apache.ambari.server.AmbariException {
        if (null == alertDefinition.getDefinitionId()) {
            create(alertDefinition);
        } else {
            merge(alertDefinition);
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition) {
        dispatchDao.removeNoticeByDefinitionId(alertDefinition.getDefinitionId());
        alertsDao.removeByDefinitionId(alertDefinition.getDefinitionId());
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        alertDefinition = findById(alertDefinition.getDefinitionId());
        if (null != alertDefinition) {
            entityManager.remove(alertDefinition);
            org.apache.ambari.server.state.alert.AlertDefinition coerced = alertDefinitionFactory.coerce(alertDefinition);
            if (null != coerced) {
                org.apache.ambari.server.events.AlertDefinitionDeleteEvent event = new org.apache.ambari.server.events.AlertDefinitionDeleteEvent(alertDefinition.getClusterId(), coerced);
                eventPublisher.publish(event);
            } else {
                org.apache.ambari.server.orm.dao.AlertDefinitionDAO.LOG.warn("Unable to broadcast alert removal event for {}", alertDefinition.getDefinitionName());
            }
        }
    }

    @com.google.inject.persist.Transactional
    public void removeAll(long clusterId) {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = findAll(clusterId);
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            remove(definition);
        }
    }
}