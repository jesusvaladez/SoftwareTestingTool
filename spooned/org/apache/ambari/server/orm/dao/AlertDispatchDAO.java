package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
@com.google.inject.Singleton
public class AlertDispatchDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> m_locksByService = com.google.common.util.concurrent.Striped.lazyWeakLock(20);

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertGroupEntity findGroupById(long groupId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.AlertGroupEntity.class, groupId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> findGroupsById(java.util.List<java.lang.Long> groupIds) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertGroupEntity> query = entityManagerProvider.get().createNamedQuery("AlertGroupEntity.findByIds", org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        query.setParameter("groupIds", groupIds);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertTargetEntity findTargetById(long targetId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.AlertTargetEntity.class, targetId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> findTargetsById(java.util.List<java.lang.Long> targetIds) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertTargetEntity> query = entityManagerProvider.get().createNamedQuery("AlertTargetEntity.findByIds", org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        query.setParameter("targetIds", targetIds);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertNoticeEntity findNoticeById(long noticeId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.AlertNoticeEntity.class, noticeId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertNoticeEntity findNoticeByUuid(java.lang.String uuid) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertNoticeEntity> query = entityManagerProvider.get().createNamedQuery("AlertNoticeEntity.findByUuid", org.apache.ambari.server.orm.entities.AlertNoticeEntity.class);
        query.setParameter("uuid", uuid);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> findPendingNotices() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertNoticeEntity> query = entityManagerProvider.get().createNamedQuery("AlertNoticeEntity.findByState", org.apache.ambari.server.orm.entities.AlertNoticeEntity.class);
        query.setParameter("notifyState", org.apache.ambari.server.state.NotificationState.PENDING);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertGroupEntity findGroupByName(long clusterId, java.lang.String groupName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertGroupEntity> query = entityManagerProvider.get().createNamedQuery("AlertGroupEntity.findByNameInCluster", org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("groupName", groupName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertTargetEntity findTargetByName(java.lang.String targetName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertTargetEntity> query = entityManagerProvider.get().createNamedQuery("AlertTargetEntity.findByName", org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        query.setParameter("targetName", targetName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> findAllGroups() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertGroupEntity> query = entityManagerProvider.get().createNamedQuery("AlertGroupEntity.findAll", org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> findAllGroups(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertGroupEntity> query = entityManagerProvider.get().createNamedQuery("AlertGroupEntity.findAllInCluster", org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> findAllTargets() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertTargetEntity> query = entityManagerProvider.get().createNamedQuery("AlertTargetEntity.findAll", org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> findAllGlobalTargets() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertTargetEntity> query = entityManagerProvider.get().createNamedQuery("AlertTargetEntity.findAllGlobal", org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> findGroupsByDefinition(org.apache.ambari.server.orm.entities.AlertDefinitionEntity definitionEntity) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertGroupEntity> query = entityManagerProvider.get().createNamedQuery("AlertGroupEntity.findByAssociatedDefinition", org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        query.setParameter("alertDefinition", definitionEntity);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.AlertGroupEntity findDefaultServiceGroup(long clusterId, java.lang.String serviceName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertGroupEntity> query = entityManagerProvider.get().createNamedQuery("AlertGroupEntity.findServiceDefaultGroup", org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("serviceName", serviceName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> findAllNotices() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertNoticeEntity> query = entityManagerProvider.get().createNamedQuery("AlertNoticeEntity.findAll", org.apache.ambari.server.orm.entities.AlertNoticeEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> findAllNotices(org.apache.ambari.server.controller.AlertNoticeRequest request) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        org.apache.ambari.server.orm.dao.AlertDispatchDAO.NoticePredicateVisitor visitor = new org.apache.ambari.server.orm.dao.AlertDispatchDAO.NoticePredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(request.Predicate, visitor);
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.AlertNoticeEntity> query = visitor.getCriteriaQuery();
        javax.persistence.criteria.Predicate jpaPredicate = visitor.getJpaPredicate();
        if (null != jpaPredicate) {
            query.where(jpaPredicate);
        }
        org.apache.ambari.server.api.query.JpaSortBuilder<org.apache.ambari.server.orm.entities.AlertNoticeEntity> sortBuilder = new org.apache.ambari.server.api.query.JpaSortBuilder<>();
        java.util.List<javax.persistence.criteria.Order> sortOrders = sortBuilder.buildSortOrders(request.Sort, visitor);
        query.orderBy(sortOrders);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertNoticeEntity> typedQuery = entityManager.createQuery(query);
        if (null != request.Pagination) {
            typedQuery.setFirstResult(request.Pagination.getOffset());
            typedQuery.setMaxResults(request.Pagination.getPageSize());
        }
        return daoUtils.selectList(typedQuery);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public int getNoticesCount(org.apache.ambari.server.controller.spi.Predicate predicate) {
        return 0;
    }

    @com.google.inject.persist.Transactional
    public void createGroups(java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> entities) {
        if (null == entities) {
            return;
        }
        java.util.List<org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate> alertGroupUpdates = new java.util.ArrayList<>(entities.size());
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity entity : entities) {
            create(entity, false);
            alertGroupUpdates.add(new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(entity));
        }
        org.apache.ambari.server.events.AlertGroupsUpdateEvent alertGroupsUpdateEvent = new org.apache.ambari.server.events.AlertGroupsUpdateEvent(alertGroupUpdates, org.apache.ambari.server.events.UpdateEventType.CREATE);
        STOMPUpdatePublisher.publish(alertGroupsUpdateEvent);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AlertGroupEntity group) {
        create(group, true);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AlertGroupEntity group, boolean fireEvent) {
        entityManagerProvider.get().persist(group);
        java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = findAllGlobalTargets();
        if (!targets.isEmpty()) {
            for (org.apache.ambari.server.orm.entities.AlertTargetEntity target : targets) {
                group.addAlertTarget(target);
            }
            entityManagerProvider.get().merge(group);
        }
        if (fireEvent) {
            org.apache.ambari.server.events.AlertGroupsUpdateEvent alertGroupsUpdateEvent = new org.apache.ambari.server.events.AlertGroupsUpdateEvent(java.util.Collections.singletonList(new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(group)), org.apache.ambari.server.events.UpdateEventType.CREATE);
            STOMPUpdatePublisher.publish(alertGroupsUpdateEvent);
        }
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.AlertGroupEntity createDefaultGroup(long clusterId, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        java.lang.String ambariServiceName = org.apache.ambari.server.controller.RootService.AMBARI.name();
        if (!ambariServiceName.equals(serviceName)) {
            org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getClusterById(clusterId);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
            if (!services.containsKey(serviceName)) {
                java.lang.String message = java.text.MessageFormat.format("Unable to create a default alert group for unknown service {0} in cluster {1}", serviceName, cluster.getClusterName());
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
        java.util.concurrent.locks.Lock lock = m_locksByService.get(serviceName);
        lock.lock();
        try {
            org.apache.ambari.server.orm.entities.AlertGroupEntity group = findDefaultServiceGroup(clusterId, serviceName);
            if (null != group) {
                return group;
            }
            group = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
            group.setClusterId(clusterId);
            group.setDefault(true);
            group.setGroupName(serviceName);
            group.setServiceName(serviceName);
            create(group);
            return group;
        } finally {
            lock.unlock();
        }
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup) {
        entityManagerProvider.get().refresh(alertGroup);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.AlertGroupEntity merge(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup) {
        return entityManagerProvider.get().merge(alertGroup);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup) {
        remove(alertGroup, true);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup, boolean fireEvent) {
        entityManagerProvider.get().remove(merge(alertGroup));
        if (fireEvent) {
            org.apache.ambari.server.events.AlertGroupsUpdateEvent alertGroupsUpdateEvent = org.apache.ambari.server.events.AlertGroupsUpdateEvent.deleteAlertGroupsUpdateEvent(java.util.Collections.singletonList(alertGroup.getGroupId()));
            STOMPUpdatePublisher.publish(alertGroupsUpdateEvent);
        }
    }

    @com.google.inject.persist.Transactional
    public void removeAllGroups(long clusterId) {
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = findAllGroups(clusterId);
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity group : groups) {
            remove(group, false);
        }
        org.apache.ambari.server.events.AlertGroupsUpdateEvent alertGroupsUpdateEvent = org.apache.ambari.server.events.AlertGroupsUpdateEvent.deleteAlertGroupsUpdateEvent(groups.stream().map(org.apache.ambari.server.orm.entities.AlertGroupEntity::getGroupId).collect(java.util.stream.Collectors.toList()));
        STOMPUpdatePublisher.publish(alertGroupsUpdateEvent);
    }

    @com.google.inject.persist.Transactional
    public void createTargets(java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> entities) {
        if (null == entities) {
            return;
        }
        for (org.apache.ambari.server.orm.entities.AlertTargetEntity entity : entities) {
            create(entity);
        }
    }

    @com.google.inject.persist.Transactional
    public java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> createNotices(java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> entities) {
        if ((null == entities) || entities.isEmpty()) {
            return entities;
        }
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> managedEntities = new java.util.ArrayList<>(entities.size());
        for (org.apache.ambari.server.orm.entities.AlertNoticeEntity entity : entities) {
            org.apache.ambari.server.orm.entities.AlertNoticeEntity managedEntity = merge(entity);
            managedEntities.add(managedEntity);
        }
        return managedEntities;
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget) {
        entityManagerProvider.get().persist(alertTarget);
        if (alertTarget.isGlobal()) {
            java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = findAllGroups();
            for (org.apache.ambari.server.orm.entities.AlertGroupEntity group : groups) {
                group.addAlertTarget(alertTarget);
                merge(group);
                org.apache.ambari.server.events.AlertGroupsUpdateEvent alertGroupsUpdateEvent = new org.apache.ambari.server.events.AlertGroupsUpdateEvent(java.util.Collections.singletonList(new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(group)), org.apache.ambari.server.events.UpdateEventType.UPDATE);
                STOMPUpdatePublisher.publish(alertGroupsUpdateEvent);
            }
        }
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget) {
        entityManagerProvider.get().refresh(alertTarget);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.AlertTargetEntity merge(org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget) {
        return entityManagerProvider.get().merge(alertTarget);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget) {
        java.util.List<org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate> alertGroupUpdates = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroupEntity : alertTarget.getAlertGroups()) {
            org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate alertGroupUpdate = new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(alertGroupEntity);
            alertGroupUpdate.getTargets().remove(alertTarget.getTargetId());
            alertGroupUpdates.add(alertGroupUpdate);
        }
        STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AlertGroupsUpdateEvent(alertGroupUpdates, org.apache.ambari.server.events.UpdateEventType.UPDATE));
        entityManagerProvider.get().remove(alertTarget);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.AlertNoticeEntity alertNotice) {
        entityManagerProvider.get().persist(alertNotice);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.AlertNoticeEntity alertNotice) {
        entityManagerProvider.get().refresh(alertNotice);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.AlertNoticeEntity merge(org.apache.ambari.server.orm.entities.AlertNoticeEntity alertNotice) {
        return entityManagerProvider.get().merge(alertNotice);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.AlertNoticeEntity alertNotice) {
        entityManagerProvider.get().remove(alertNotice);
    }

    @com.google.inject.persist.Transactional
    public void removeNoticeByDefinitionId(long definitionId) {
        org.apache.ambari.server.orm.dao.AlertDispatchDAO.LOG.info("Deleting AlertNotice entities by definition id.");
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.TypedQuery<java.lang.Integer> historyIdQuery = entityManager.createNamedQuery("AlertHistoryEntity.findHistoryIdsByDefinitionId", java.lang.Integer.class);
        historyIdQuery.setParameter("definitionId", definitionId);
        java.util.List<java.lang.Integer> ids = daoUtils.selectList(historyIdQuery);
        int BATCH_SIZE = 999;
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertNoticeEntity> noticeQuery = entityManager.createNamedQuery("AlertNoticeEntity.removeByHistoryIds", org.apache.ambari.server.orm.entities.AlertNoticeEntity.class);
        if ((ids != null) && (!ids.isEmpty())) {
            for (int i = 0; i < ids.size(); i += BATCH_SIZE) {
                int endIndex = ((i + BATCH_SIZE) > ids.size()) ? ids.size() : i + BATCH_SIZE;
                java.util.List<java.lang.Integer> idsSubList = ids.subList(i, endIndex);
                org.apache.ambari.server.orm.dao.AlertDispatchDAO.LOG.info((("Deleting AlertNotice entity batch with history ids: " + idsSubList.get(0)) + " - ") + idsSubList.get(idsSubList.size() - 1));
                noticeQuery.setParameter("historyIds", idsSubList);
                noticeQuery.executeUpdate();
            }
        }
        entityManager.clear();
    }

    private final class NoticePredicateVisitor extends org.apache.ambari.server.api.query.JpaPredicateVisitor<org.apache.ambari.server.orm.entities.AlertNoticeEntity> {
        public NoticePredicateVisitor() {
            super(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.AlertNoticeEntity.class);
        }

        @java.lang.Override
        public java.lang.Class<org.apache.ambari.server.orm.entities.AlertNoticeEntity> getEntityClass() {
            return org.apache.ambari.server.orm.entities.AlertNoticeEntity.class;
        }

        @java.lang.Override
        public java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> getPredicateMapping(java.lang.String propertyId) {
            return org.apache.ambari.server.orm.entities.AlertNoticeEntity_.getPredicateMapping().get(propertyId);
        }
    }
}