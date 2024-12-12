package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
@com.google.inject.Singleton
public class RequestDAO implements org.apache.ambari.server.orm.dao.Cleanable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.RequestDAO.class);

    private static final java.lang.String REQUEST_IDS_SORTED_SQL = "SELECT request.requestId FROM RequestEntity request ORDER BY request.requestId {0}";

    private static final java.lang.String REQUESTS_WITH_CLUSTER_SQL = "SELECT request.requestId FROM RequestEntity request WHERE request.clusterId = %s ORDER BY request.requestId %s";

    private static final java.lang.String REQUESTS_WITH_NO_CLUSTER_SQL = "SELECT request.requestId FROM RequestEntity request WHERE request.clusterId = -1 OR request.clusterId IS NULL ORDER BY request.requestId %s";

    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyLogicalTaskDAO topologyLogicalTaskDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyHostTaskDAO topologyHostTaskDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyLogicalRequestDAO topologyLogicalRequestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyRequestDAO topologyRequestDAO;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RequestEntity findByPK(java.lang.Long requestId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.RequestEntity.class, requestId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> findByPks(java.util.Collection<java.lang.Long> requestIds) {
        return findByPks(requestIds, false);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> findByPks(java.util.Collection<java.lang.Long> requestIds, boolean refreshHint) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RequestEntity> query = entityManagerProvider.get().createQuery("SELECT request FROM RequestEntity request " + "WHERE request.requestId IN ?1", org.apache.ambari.server.orm.entities.RequestEntity.class);
        if (refreshHint) {
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        }
        java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(requestIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            result.addAll(daoUtils.selectList(query, chunk));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.RequestEntity.class);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findAllRequestIds(int limit, boolean ascending) {
        java.lang.String sort = "ASC";
        if (!ascending) {
            sort = "DESC";
        }
        java.lang.String sql = java.text.MessageFormat.format(org.apache.ambari.server.orm.dao.RequestDAO.REQUEST_IDS_SORTED_SQL, sort);
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery(sql, java.lang.Long.class);
        query.setMaxResults(limit);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RequestResourceFilterEntity> findAllResourceFilters() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.RequestResourceFilterEntity.class);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public boolean isAllTasksCompleted(long requestId) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT task.taskId FROM HostRoleCommandEntity task WHERE task.requestId = ?1 AND " + ("task.stageId=(select max(stage.stageId) FROM StageEntity stage WHERE stage.requestId=?1) " + "AND task.status NOT IN ?2"), java.lang.Long.class);
        query.setMaxResults(1);
        return daoUtils.selectList(query, requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.getCompletedStates()).isEmpty();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.lang.Long getLastStageId(long requestId) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT max(stage.stageId) " + "FROM StageEntity stage WHERE stage.requestId=?1", java.lang.Long.class);
        return daoUtils.selectSingle(query, requestId);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RequestEntity updateStatus(long requestId, org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus) {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = findByPK(requestId);
        requestEntity.setStatus(status);
        requestEntity.setDisplayStatus(displayStatus);
        return merge(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.RequestEntity requestEntity) {
        entityManagerProvider.get().persist(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RequestEntity merge(org.apache.ambari.server.orm.entities.RequestEntity requestEntity) {
        return entityManagerProvider.get().merge(requestEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.RequestEntity requestEntity) {
        entityManagerProvider.get().remove(merge(requestEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Long requestId) {
        remove(findByPK(requestId));
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findAllRequestIds(int limit, boolean sortAscending, java.lang.Long clusterId) {
        final java.lang.String sql;
        if (null == clusterId) {
            sql = java.lang.String.format(org.apache.ambari.server.orm.dao.RequestDAO.REQUESTS_WITH_NO_CLUSTER_SQL, sortAscending ? "ASC" : "DESC");
        } else {
            sql = java.lang.String.format(org.apache.ambari.server.orm.dao.RequestDAO.REQUESTS_WITH_CLUSTER_SQL, clusterId, sortAscending ? "ASC" : "DESC");
        }
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery(sql, java.lang.Long.class);
        query.setMaxResults(limit);
        return daoUtils.selectList(query);
    }

    public static final class StageEntityPK {
        private java.lang.Long requestId;

        private java.lang.Long stageId;

        public StageEntityPK(java.lang.Long requestId, java.lang.Long stageId) {
            this.requestId = requestId;
            this.stageId = stageId;
        }

        public java.lang.Long getStageId() {
            return stageId;
        }

        public void setStageId(java.lang.Long stageId) {
            this.stageId = stageId;
        }

        public java.lang.Long getRequestId() {
            return requestId;
        }

        public void setRequestId(java.lang.Long requestId) {
            this.requestId = requestId;
        }
    }

    private java.util.Set<java.lang.Long> findAllRequestIdsFromUpgrade() {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.TypedQuery<java.lang.Long> upgradeQuery = entityManager.createNamedQuery("UpgradeEntity.findAllRequestIds", java.lang.Long.class);
        return com.google.common.collect.Sets.newHashSet(daoUtils.selectList(upgradeQuery));
    }

    public java.util.List<org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK> findRequestAndStageIdsInClusterBeforeDate(java.lang.Long clusterId, long beforeDateMillis) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK> requestQuery = entityManager.createNamedQuery("RequestEntity.findRequestStageIdsInClusterBeforeDate", org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK.class);
        requestQuery.setParameter("clusterId", clusterId);
        requestQuery.setParameter("beforeDate", beforeDateMillis);
        return daoUtils.selectList(requestQuery);
    }

    @com.google.inject.persist.Transactional
    protected <T> int cleanTableByIds(java.util.Set<java.lang.Long> ids, java.lang.String paramName, java.lang.String entityName, java.lang.Long beforeDateMillis, java.lang.String entityQuery, java.lang.Class<T> type) {
        org.apache.ambari.server.orm.dao.RequestDAO.LOG.info(java.lang.String.format("Purging %s entity records before date %s", entityName, new java.util.Date(beforeDateMillis)));
        if (org.apache.commons.collections.CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        final javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        final javax.persistence.TypedQuery<T> query = entityManager.createNamedQuery(entityQuery, type);
        return org.apache.ambari.server.orm.helpers.SQLOperations.batch(ids, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            org.apache.ambari.server.orm.dao.RequestDAO.LOG.info(java.lang.String.format("Purging %s entity, batch %s/%s.", entityName, currentBatch, totalBatches));
            query.setParameter(paramName, chunk);
            return query.executeUpdate();
        });
    }

    @com.google.inject.persist.Transactional
    protected <T> int cleanTableByStageEntityPK(java.util.List<org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK> ids, java.util.LinkedList<java.lang.String> paramNames, java.lang.String entityName, java.lang.Long beforeDateMillis, java.lang.String entityQuery, java.lang.Class<T> type) {
        org.apache.ambari.server.orm.dao.RequestDAO.LOG.info(java.lang.String.format("Purging %s entity records before date %s", entityName, new java.util.Date(beforeDateMillis)));
        if (org.apache.commons.collections.CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        final javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        final javax.persistence.TypedQuery<T> query = entityManager.createNamedQuery(entityQuery, type);
        return org.apache.ambari.server.orm.helpers.SQLOperations.batch(ids, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            int affectedRows = 0;
            for (org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK requestIds : chunk) {
                query.setParameter(paramNames.get(0), requestIds.getStageId());
                query.setParameter(paramNames.get(1), requestIds.getRequestId());
                affectedRows += query.executeUpdate();
            }
            return affectedRows;
        });
    }

    @com.google.inject.persist.Transactional
    @java.lang.Override
    public long cleanup(org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy policy) {
        long affectedRows = 0;
        try {
            java.lang.Long clusterId = m_clusters.get().getCluster(policy.getClusterName()).getClusterId();
            java.util.List<org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK> requestStageIds = findRequestAndStageIdsInClusterBeforeDate(clusterId, policy.getToDateInMillis());
            java.util.Set<java.lang.Long> requestIdsFromUpgrade = findAllRequestIdsFromUpgrade();
            java.util.Iterator<org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK> requestStageIdsIterator = requestStageIds.iterator();
            java.util.Set<java.lang.Long> requestIds = new java.util.HashSet<>();
            while (requestStageIdsIterator.hasNext()) {
                org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK nextRequestStageIds = requestStageIdsIterator.next();
                if (requestIdsFromUpgrade.contains(nextRequestStageIds.getRequestId())) {
                    requestStageIdsIterator.remove();
                    continue;
                }
                requestIds.add(nextRequestStageIds.getRequestId());
            } 
            java.util.Set<java.lang.Long> taskIds = hostRoleCommandDAO.findTaskIdsByRequestStageIds(requestStageIds);
            java.util.LinkedList<java.lang.String> params = new java.util.LinkedList<>();
            params.add("stageId");
            params.add("requestId");
            java.util.Set<java.lang.Long> hostTaskIds = topologyLogicalTaskDAO.findHostTaskIdsByPhysicalTaskIds(taskIds);
            java.util.Set<java.lang.Long> hostRequestIds = topologyHostTaskDAO.findHostRequestIdsByHostTaskIds(hostTaskIds);
            java.util.Set<java.lang.Long> topologyRequestIds = topologyLogicalRequestDAO.findRequestIdsByIds(hostRequestIds);
            affectedRows += cleanTableByIds(taskIds, "taskIds", "ExecutionCommand", policy.getToDateInMillis(), "ExecutionCommandEntity.removeByTaskIds", org.apache.ambari.server.orm.entities.ExecutionCommandEntity.class);
            affectedRows += cleanTableByIds(taskIds, "taskIds", "TopologyLogicalTask", policy.getToDateInMillis(), "TopologyLogicalTaskEntity.removeByPhysicalTaskIds", org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity.class);
            affectedRows += cleanTableByIds(hostTaskIds, "hostTaskIds", "TopologyHostTask", policy.getToDateInMillis(), "TopologyHostTaskEntity.removeByTaskIds", org.apache.ambari.server.orm.entities.TopologyHostTaskEntity.class);
            affectedRows += cleanTableByIds(hostRequestIds, "hostRequestIds", "TopologyHostRequest", policy.getToDateInMillis(), "TopologyHostRequestEntity.removeByIds", org.apache.ambari.server.orm.entities.TopologyHostRequestEntity.class);
            for (java.lang.Long topologyRequestId : topologyRequestIds) {
                topologyRequestDAO.removeByPK(topologyRequestId);
            }
            affectedRows += cleanTableByIds(taskIds, "taskIds", "HostRoleCommand", policy.getToDateInMillis(), "HostRoleCommandEntity.removeByTaskIds", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
            affectedRows += cleanTableByStageEntityPK(requestStageIds, params, "RoleSuccessCriteria", policy.getToDateInMillis(), "RoleSuccessCriteriaEntity.removeByRequestStageIds", org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity.class);
            affectedRows += cleanTableByStageEntityPK(requestStageIds, params, "Stage", policy.getToDateInMillis(), "StageEntity.removeByRequestStageIds", org.apache.ambari.server.orm.entities.StageEntity.class);
            affectedRows += cleanTableByIds(requestIds, "requestIds", "RequestResourceFilter", policy.getToDateInMillis(), "RequestResourceFilterEntity.removeByRequestIds", org.apache.ambari.server.orm.entities.RequestResourceFilterEntity.class);
            affectedRows += cleanTableByIds(requestIds, "requestIds", "RequestOperationLevel", policy.getToDateInMillis(), "RequestOperationLevelEntity.removeByRequestIds", org.apache.ambari.server.orm.entities.RequestOperationLevelEntity.class);
            affectedRows += cleanTableByIds(requestIds, "requestIds", "Request", policy.getToDateInMillis(), "RequestEntity.removeByRequestIds", org.apache.ambari.server.orm.entities.RequestEntity.class);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.orm.dao.RequestDAO.LOG.error("Error while looking up cluster with name: {}", policy.getClusterName(), e);
            throw new java.lang.IllegalStateException(e);
        }
        return affectedRows;
    }
}