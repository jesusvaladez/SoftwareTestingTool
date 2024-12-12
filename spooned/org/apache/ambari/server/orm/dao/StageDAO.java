package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
@com.google.inject.Singleton
public class StageDAO {
    private static java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.util.EnumSet<org.apache.ambari.server.actionmanager.HostRoleStatus>> manualTransitionMap = new java.util.HashMap<>();

    static {
        manualTransitionMap.put(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, java.util.EnumSet.of(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED));
        manualTransitionMap.put(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, java.util.EnumSet.of(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED));
        manualTransitionMap.put(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT, java.util.EnumSet.of(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED));
    }

    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDao;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.StageEntity findByPK(org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.StageEntity.class, stageEntityPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.StageEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.StageEntity.class);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public long getLastRequestId() {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT max(stage.requestId) FROM StageEntity stage", java.lang.Long.class);
        java.lang.Long result = daoUtils.selectSingle(query);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.StageEntity findByActionId(java.lang.String actionId) {
        long[] ids = org.apache.ambari.server.utils.StageUtils.getRequestStage(actionId);
        org.apache.ambari.server.orm.entities.StageEntityPK pk = new org.apache.ambari.server.orm.entities.StageEntityPK();
        pk.setRequestId(ids[0]);
        pk.setStageId(ids[1]);
        return findByPK(pk);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.StageEntity> findByRequestId(long requestId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.StageEntity> query = entityManagerProvider.get().createQuery("SELECT stage " + (("FROM StageEntity stage " + "WHERE stage.requestId=?1 ") + "ORDER BY stage.stageId"), org.apache.ambari.server.orm.entities.StageEntity.class);
        return daoUtils.selectList(query, requestId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.StageEntity> findByRequestIdAndCommandStatuses(java.lang.Long requestId, java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.StageEntity> query = entityManagerProvider.get().createNamedQuery("StageEntity.findByRequestIdAndCommandStatuses", org.apache.ambari.server.orm.entities.StageEntity.class);
        query.setParameter("requestId", requestId);
        query.setParameter("statuses", statuses);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.StageEntity> findFirstStageByStatus(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses) {
        javax.persistence.TypedQuery<java.lang.Object[]> query = entityManagerProvider.get().createNamedQuery("StageEntity.findFirstStageByStatus", java.lang.Object[].class);
        query.setParameter("statuses", statuses);
        java.util.List<java.lang.Object[]> results = daoUtils.selectList(query);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stages = new java.util.ArrayList<>();
        for (java.lang.Object[] result : results) {
            org.apache.ambari.server.orm.entities.StageEntityPK stagePK = new org.apache.ambari.server.orm.entities.StageEntityPK();
            stagePK.setRequestId(((java.lang.Long) (result[0])));
            stagePK.setStageId(((java.lang.Long) (result[1])));
            org.apache.ambari.server.orm.entities.StageEntity stage = findByPK(stagePK);
            stages.add(stage);
        }
        return stages;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Map<java.lang.Long, java.lang.String> findRequestContext(java.util.List<java.lang.Long> requestIds) {
        java.util.Map<java.lang.Long, java.lang.String> resultMap = new java.util.HashMap<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(requestIds)) {
            javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.StageEntity> query = entityManagerProvider.get().createQuery("SELECT stage FROM StageEntity stage WHERE " + ("stage.requestId IN (SELECT DISTINCT s.requestId FROM StageEntity s " + "WHERE s.requestId IN ?1)"), org.apache.ambari.server.orm.entities.StageEntity.class);
            java.util.List<org.apache.ambari.server.orm.entities.StageEntity> result = new java.util.ArrayList<>();
            org.apache.ambari.server.orm.helpers.SQLOperations.batch(requestIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
                result.addAll(daoUtils.selectList(query, chunk));
                return 0;
            });
            for (org.apache.ambari.server.orm.entities.StageEntity entity : result) {
                resultMap.put(entity.getRequestId(), entity.getRequestContext());
            }
        }
        return resultMap;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.lang.String findRequestContext(long requestId) {
        javax.persistence.TypedQuery<java.lang.String> query = entityManagerProvider.get().createQuery("SELECT stage.requestContext " + ("FROM StageEntity stage " + "WHERE stage.requestId=?1"), java.lang.String.class);
        java.lang.String result = daoUtils.selectOne(query, requestId);
        if (result != null) {
            return result;
        } else {
            return "";
        }
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.StageEntity stageEntity) {
        entityManagerProvider.get().persist(stageEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.StageEntity merge(org.apache.ambari.server.orm.entities.StageEntity stageEntity) {
        return entityManagerProvider.get().merge(stageEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.StageEntity stageEntity) {
        entityManagerProvider.get().remove(merge(stageEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK) {
        remove(findByPK(stageEntityPK));
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.StageEntity> findAll(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        org.apache.ambari.server.orm.dao.StageDAO.StagePredicateVisitor visitor = new org.apache.ambari.server.orm.dao.StageDAO.StagePredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.StageEntity> query = visitor.getCriteriaQuery();
        javax.persistence.criteria.Predicate jpaPredicate = visitor.getJpaPredicate();
        if (jpaPredicate != null) {
            query.where(jpaPredicate);
        }
        org.apache.ambari.server.api.query.JpaSortBuilder<org.apache.ambari.server.orm.entities.StageEntity> sortBuilder = new org.apache.ambari.server.api.query.JpaSortBuilder<>();
        java.util.List<javax.persistence.criteria.Order> sortOrders = sortBuilder.buildSortOrders(request.getSortRequest(), visitor);
        query.orderBy(sortOrders);
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.StageEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return daoUtils.selectList(typedQuery);
    }

    @com.google.inject.persist.Transactional
    public void updateStageStatus(org.apache.ambari.server.orm.entities.StageEntity stage, org.apache.ambari.server.actionmanager.HostRoleStatus desiredStatus, org.apache.ambari.server.actionmanager.ActionManager actionManager) {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = stage.getHostRoleCommands();
        org.apache.ambari.server.actionmanager.HostRoleStatus currentStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromTaskEntities(tasks, stage.isSkippable()).getStatus();
        if (!org.apache.ambari.server.orm.dao.StageDAO.isValidManualTransition(currentStatus, desiredStatus)) {
            throw new java.lang.IllegalArgumentException((("Can not transition a stage from " + currentStatus) + " to ") + desiredStatus);
        }
        if (desiredStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) {
            actionManager.cancelRequest(stage.getRequestId(), "User aborted.");
        } else {
            java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hrcWithChangedStatus = new java.util.ArrayList<>();
            for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommand : tasks) {
                org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus = hostRoleCommand.getStatus();
                if (hostRoleStatus.equals(currentStatus)) {
                    hrcWithChangedStatus.add(hostRoleCommand);
                    hostRoleCommand.setStatus(desiredStatus);
                    if (desiredStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                        hostRoleCommand.setStartTime(-1L);
                    }
                    hostRoleCommandDao.merge(hostRoleCommand);
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    public void updateStatus(org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK, org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus) {
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = findByPK(stageEntityPK);
        stageEntity.setStatus(status);
        stageEntity.setDisplayStatus(displayStatus);
        merge(stageEntity);
    }

    private static boolean isValidManualTransition(org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.HostRoleStatus desiredStatus) {
        java.util.EnumSet<org.apache.ambari.server.actionmanager.HostRoleStatus> stageStatusSet = org.apache.ambari.server.orm.dao.StageDAO.manualTransitionMap.get(status);
        return (stageStatusSet != null) && stageStatusSet.contains(desiredStatus);
    }

    private final class StagePredicateVisitor extends org.apache.ambari.server.api.query.JpaPredicateVisitor<org.apache.ambari.server.orm.entities.StageEntity> {
        public StagePredicateVisitor() {
            super(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.StageEntity.class);
        }

        @java.lang.Override
        public java.lang.Class<org.apache.ambari.server.orm.entities.StageEntity> getEntityClass() {
            return org.apache.ambari.server.orm.entities.StageEntity.class;
        }

        @java.lang.Override
        public java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> getPredicateMapping(java.lang.String propertyId) {
            return org.apache.ambari.server.orm.entities.StageEntity_.getPredicateMapping().get(propertyId);
        }
    }
}