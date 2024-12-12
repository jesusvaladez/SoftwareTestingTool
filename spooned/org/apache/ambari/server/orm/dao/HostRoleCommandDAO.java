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
public class HostRoleCommandDAO {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);

    private static final java.lang.String SUMMARY_DTO = java.lang.String.format("SELECT NEW %s(" + (((((((((((((((("MAX(hrc.stage.skippable), " + "MIN(hrc.startTime), ") + "MAX(hrc.endTime), ") + "hrc.stageId, ") + "SUM(CASE WHEN hrc.status = :aborted THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :completed THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :failed THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :holding THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :holding_failed THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :holding_timedout THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :in_progress THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :pending THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :queued THEN 1 ELSE 0 END), ") + "SUM(CASE WHEN hrc.status = :timedout THEN 1 ELSE 0 END),") + "SUM(CASE WHEN hrc.status = :skipped_failed THEN 1 ELSE 0 END)") + ") FROM HostRoleCommandEntity hrc ") + " GROUP BY hrc.requestId, hrc.stageId HAVING hrc.requestId = :requestId"), org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.class.getName());

    private static final java.lang.String REQUESTS_BY_TASK_STATUS_SQL = "SELECT DISTINCT task.requestId FROM HostRoleCommandEntity task WHERE task.status IN :taskStatuses ORDER BY task.requestId {0}";

    private static final java.lang.String COMPLETED_REQUESTS_SQL = "SELECT DISTINCT task.requestId FROM HostRoleCommandEntity task WHERE task.requestId NOT IN (SELECT task.requestId FROM HostRoleCommandEntity task WHERE task.status IN :notCompletedStatuses) ORDER BY task.requestId {0}";

    private final com.google.common.cache.Cache<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>> hrcStatusSummaryCache;

    private final boolean hostRoleCommandStatusSummaryCacheEnabled;

    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.TaskEventPublisher taskEventPublisher;

    @com.google.inject.Inject
    private final org.apache.ambari.server.orm.TransactionalLocks transactionLocks = null;

    public static final java.lang.String HRC_STATUS_SUMMARY_CACHE_SIZE = "hostRoleCommandStatusSummaryCacheSize";

    public static final java.lang.String HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION_MINUTES = "hostRoleCommandStatusCacheExpiryDurationMins";

    public static final java.lang.String HRC_STATUS_SUMMARY_CACHE_ENABLED = "hostRoleCommandStatusSummaryCacheEnabled";

    protected void invalidateHostRoleCommandStatusSummaryCache(java.lang.Long requestId) {
        if (!hostRoleCommandStatusSummaryCacheEnabled) {
            return;
        }
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LOG.debug("Invalidating host role command status summary cache for request {} !", requestId);
        hrcStatusSummaryCache.invalidate(requestId);
    }

    protected void invalidateHostRoleCommandStatusSummaryCache(java.util.Set<java.lang.Long> requestIds) {
        for (java.lang.Long requestId : requestIds) {
            if (null != requestId) {
                invalidateHostRoleCommandStatusSummaryCache(requestId);
            }
        }
    }

    protected void invalidateHostRoleCommandStatusSummaryCache(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity) {
        if (!hostRoleCommandStatusSummaryCacheEnabled) {
            return;
        }
        if (hostRoleCommandEntity != null) {
            java.lang.Long requestId = hostRoleCommandEntity.getRequestId();
            if (requestId == null) {
                org.apache.ambari.server.orm.entities.StageEntity stageEntity = hostRoleCommandEntity.getStage();
                if (stageEntity != null) {
                    requestId = stageEntity.getRequestId();
                }
            }
            if (requestId != null) {
                invalidateHostRoleCommandStatusSummaryCache(requestId.longValue());
            }
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    private java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> loadAggregateCounts(java.lang.Long requestId) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> map = new java.util.HashMap<>();
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> query = entityManager.createQuery(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.SUMMARY_DTO, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.class);
        query.setParameter("requestId", requestId);
        query.setParameter("aborted", org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        query.setParameter("completed", org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        query.setParameter("failed", org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        query.setParameter("holding", org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING);
        query.setParameter("holding_failed", org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED);
        query.setParameter("holding_timedout", org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT);
        query.setParameter("in_progress", org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        query.setParameter("pending", org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        query.setParameter("queued", org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        query.setParameter("timedout", org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT);
        query.setParameter("skipped_failed", org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
        for (org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO dto : daoUtils.selectList(query)) {
            map.put(dto.getStageId(), dto);
        }
        return map;
    }

    @com.google.inject.Inject
    public HostRoleCommandDAO(@com.google.inject.name.Named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_ENABLED)
    boolean hostRoleCommandStatusSummaryCacheEnabled, @com.google.inject.name.Named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_SIZE)
    long hostRoleCommandStatusSummaryCacheLimit, @com.google.inject.name.Named(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION_MINUTES)
    long hostRoleCommandStatusSummaryCacheExpiryDurationMins) {
        this.hostRoleCommandStatusSummaryCacheEnabled = hostRoleCommandStatusSummaryCacheEnabled;
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LOG.info("Host role command status summary cache {} !", hostRoleCommandStatusSummaryCacheEnabled ? "enabled" : "disabled");
        hrcStatusSummaryCache = com.google.common.cache.CacheBuilder.newBuilder().maximumSize(hostRoleCommandStatusSummaryCacheLimit).expireAfterWrite(hostRoleCommandStatusSummaryCacheExpiryDurationMins, java.util.concurrent.TimeUnit.MINUTES).build();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity findByPK(long taskId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class, taskId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByPKs(java.util.Collection<java.lang.Long> taskIds) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createQuery("SELECT task FROM HostRoleCommandEntity task WHERE task.taskId IN ?1 " + "ORDER BY task.taskId", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(taskIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            result.addAll(daoUtils.selectList(query, chunk));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findStatusRolesByPKs(java.util.Collection<java.lang.Long> taskIds) {
        javax.persistence.TypedQuery<java.lang.Object[]> query = entityManagerProvider.get().createQuery("SELECT task.taskId, task.status, task.role FROM HostRoleCommandEntity task WHERE task.taskId IN ?1 " + "ORDER BY task.taskId", java.lang.Object[].class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(taskIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            java.util.List<java.lang.Object[]> queryResult = daoUtils.selectList(query, chunk);
            result.addAll(queryResult.stream().map(o -> {
                org.apache.ambari.server.orm.entities.HostRoleCommandEntity e = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
                e.setTaskId(((java.lang.Long) (o[0])));
                e.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(o[1].toString()));
                e.setRole(org.apache.ambari.server.Role.valueOf(o[2].toString()));
                return e;
            }).collect(java.util.stream.Collectors.toList()));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByHostId(java.lang.Long hostId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findByHostId", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        query.setParameter("hostId", hostId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByRequestIds(java.util.Collection<java.lang.Long> requestIds) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createQuery("SELECT task FROM HostRoleCommandEntity task " + ("WHERE task.requestId IN ?1 " + "ORDER BY task.taskId"), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(requestIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            result.addAll(daoUtils.selectList(query, chunk));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByRequestIdAndStatuses(java.lang.Long requestId, java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findByRequestIdAndStatuses", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        query.setParameter("requestId", requestId);
        query.setParameter("statuses", statuses);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> results = query.getResultList();
        return results;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findTaskIdsByRequestIds(java.util.Collection<java.lang.Long> requestIds) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT task.taskId FROM HostRoleCommandEntity task " + ("WHERE task.requestId IN ?1 " + "ORDER BY task.taskId"), java.lang.Long.class);
        java.util.List<java.lang.Long> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(requestIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
            result.addAll(daoUtils.selectList(query, chunk));
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByRequestAndTaskIds(java.util.Collection<java.lang.Long> requestIds, java.util.Collection<java.lang.Long> taskIds) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(requestIds) || org.apache.commons.collections.CollectionUtils.isEmpty(taskIds)) {
            return java.util.Collections.<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>emptyList();
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createQuery("SELECT DISTINCT task FROM HostRoleCommandEntity task " + ("WHERE task.requestId IN ?1 AND task.taskId IN ?2 " + "ORDER BY task.taskId"), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        return runQueryForVastRequestsAndTaskIds(query, requestIds, taskIds);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findTaskIdsByRequestAndTaskIds(java.util.Collection<java.lang.Long> requestIds, java.util.Collection<java.lang.Long> taskIds) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(requestIds) || org.apache.commons.collections.CollectionUtils.isEmpty(taskIds)) {
            return java.util.Collections.<java.lang.Long>emptyList();
        }
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT DISTINCT task.taskId FROM HostRoleCommandEntity task " + ("WHERE task.requestId IN ?1 AND task.taskId IN ?2 " + "ORDER BY task.taskId"), java.lang.Long.class);
        return runQueryForVastRequestsAndTaskIds(query, requestIds, taskIds);
    }

    private <T> java.util.List<T> runQueryForVastRequestsAndTaskIds(javax.persistence.TypedQuery<T> query, java.util.Collection<java.lang.Long> requestIds, java.util.Collection<java.lang.Long> taskIds) {
        final int batchSize = org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE;
        final java.util.List<T> result = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.helpers.SQLOperations.batch(taskIds, batchSize, (taskChunk, currentTaskBatch, totalTaskBatches, totalTaskSize) -> {
            org.apache.ambari.server.orm.helpers.SQLOperations.batch(requestIds, batchSize, (requestChunk, currentRequestBatch, totalRequestBatches, totalRequestSize) -> {
                result.addAll(daoUtils.selectList(query, requestChunk, taskChunk));
                return 0;
            });
            return 0;
        });
        return com.google.common.collect.Lists.newArrayList(result);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findTaskIdsByHostRoleAndStatus(java.lang.String hostname, java.lang.String role, org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT DISTINCT task.taskId FROM HostRoleCommandEntity task " + ("WHERE task.hostEntity.hostName=?1 AND task.role=?2 AND task.status=?3 " + "ORDER BY task.taskId"), java.lang.Long.class);
        return daoUtils.selectList(query, hostname, role, status);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findTaskIdsByRoleAndStatus(java.lang.String role, org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT DISTINCT task.taskId FROM HostRoleCommandEntity task " + ("WHERE task.role=?1 AND task.status=?2 " + "ORDER BY task.taskId"), java.lang.Long.class);
        return daoUtils.selectList(query, role, status);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findSortedCommandsByRequestIdAndCustomCommandName(java.lang.Long requestId, java.lang.String customCommandName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createQuery("SELECT hostRoleCommand " + (("FROM HostRoleCommandEntity hostRoleCommand " + "WHERE hostRoleCommand.requestId=?1 AND hostRoleCommand.customCommandName=?2 ") + "ORDER BY hostRoleCommand.taskId"), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        return daoUtils.selectList(query, requestId, customCommandName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findSortedCommandsByStageAndHost(org.apache.ambari.server.orm.entities.StageEntity stageEntity, org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createQuery("SELECT hostRoleCommand " + (("FROM HostRoleCommandEntity hostRoleCommand " + "WHERE hostRoleCommand.stage=?1 AND hostRoleCommand.hostEntity.hostName=?2 ") + "ORDER BY hostRoleCommand.taskId"), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        return daoUtils.selectList(query, stageEntity, hostEntity.getHostName());
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>> findSortedCommandsByStage(org.apache.ambari.server.orm.entities.StageEntity stageEntity) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createQuery("SELECT hostRoleCommand " + (("FROM HostRoleCommandEntity hostRoleCommand " + "WHERE hostRoleCommand.stage=?1 ") + "ORDER BY hostRoleCommand.hostEntity.hostName, hostRoleCommand.taskId"), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities = daoUtils.selectList(query, stageEntity);
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>> hostCommands = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity : commandEntities) {
            if (!hostCommands.containsKey(commandEntity.getHostName())) {
                hostCommands.put(commandEntity.getHostName(), new java.util.ArrayList<>());
            }
            hostCommands.get(commandEntity.getHostName()).add(commandEntity);
        }
        return hostCommands;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findTaskIdsByStage(long requestId, long stageId) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT hostRoleCommand.taskId " + ((("FROM HostRoleCommandEntity hostRoleCommand " + "WHERE hostRoleCommand.stage.requestId=?1 ") + "AND hostRoleCommand.stage.stageId=?2 ") + "ORDER BY hostRoleCommand.taskId"), java.lang.Long.class);
        return daoUtils.selectList(query, requestId, stageId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Map<java.lang.Long, java.lang.Integer> getHostIdToCountOfCommandsWithStatus(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses) {
        java.util.Map<java.lang.Long, java.lang.Integer> hostIdToCount = new java.util.HashMap<>();
        java.lang.String queryName = "SELECT command.hostId FROM HostRoleCommandEntity command WHERE command.status IN :statuses";
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery(queryName, java.lang.Long.class);
        query.setParameter("statuses", statuses);
        java.util.List<java.lang.Long> results = query.getResultList();
        for (java.lang.Long hostId : results) {
            if (hostIdToCount.containsKey(hostId)) {
                hostIdToCount.put(hostId, hostIdToCount.get(hostId) + 1);
            } else {
                hostIdToCount.put(hostId, 1);
            }
        }
        return hostIdToCount;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByHostRole(java.lang.String hostName, long requestId, long stageId, java.lang.String role) {
        java.lang.String queryName = (null == hostName) ? "HostRoleCommandEntity.findByHostRoleNullHost" : "HostRoleCommandEntity.findByHostRole";
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createNamedQuery(queryName, org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        if (null != hostName) {
            query.setParameter("hostName", hostName);
        }
        query.setParameter("requestId", requestId);
        query.setParameter("stageId", stageId);
        query.setParameter("role", role);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByRequest(long requestId) {
        return findByRequest(requestId, false);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByRequest(long requestId, boolean refreshHint) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findByRequestId", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        if (refreshHint) {
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        }
        query.setParameter("requestId", requestId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> findTaskIdsByRequest(long requestId) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("SELECT command.taskId " + ("FROM HostRoleCommandEntity command " + "WHERE command.requestId=?1 ORDER BY command.taskId"), java.lang.Long.class);
        return daoUtils.selectList(query, requestId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByStatus(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findByCommandStatuses", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        query.setParameter("statuses", statuses);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.lang.Number getCountByStatus(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses) {
        javax.persistence.TypedQuery<java.lang.Number> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findCountByCommandStatuses", java.lang.Number.class);
        query.setParameter("statuses", statuses);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findByStatusBetweenStages(long requestId, org.apache.ambari.server.actionmanager.HostRoleStatus status, long minStageId, long maxStageId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findByStatusBetweenStages", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        query.setParameter("requestId", requestId);
        query.setParameter("status", status);
        query.setParameter("minStageId", minStageId);
        query.setParameter("maxStageId", maxStageId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> getRequestsByTaskStatus(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses, int maxResults, boolean ascOrder) {
        java.lang.String sortOrder = "ASC";
        if (!ascOrder) {
            sortOrder = "DESC";
        }
        java.lang.String sql = java.text.MessageFormat.format(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.REQUESTS_BY_TASK_STATUS_SQL, sortOrder);
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery(sql, java.lang.Long.class);
        query.setParameter("taskStatuses", statuses);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.Long> getCompletedRequests(int maxResults, boolean ascOrder) {
        java.lang.String sortOrder = "ASC";
        if (!ascOrder) {
            sortOrder = "DESC";
        }
        java.lang.String sql = java.text.MessageFormat.format(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.COMPLETED_REQUESTS_SQL, sortOrder);
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery(sql, java.lang.Long.class);
        query.setParameter("notCompletedStatuses", org.apache.ambari.server.actionmanager.HostRoleStatus.NOT_COMPLETED_STATUSES);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public int updateStatusByRequestId(long requestId, org.apache.ambari.server.actionmanager.HostRoleStatus target, java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> sources) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> selectQuery = entityManagerProvider.get().createQuery("SELECT command " + ("FROM HostRoleCommandEntity command " + "WHERE command.requestId=?1 AND command.status IN ?2"), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities = daoUtils.selectList(selectQuery, requestId, sources);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity : commandEntities) {
            entity.setStatus(target);
            merge(entity);
        }
        return commandEntities.size();
    }

    @com.google.inject.persist.Transactional
    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
    public void create(org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(entity);
        invalidateHostRoleCommandStatusSummaryCache(entity);
    }

    @com.google.inject.persist.Transactional
    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity merge(org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity) {
        entity = mergeWithoutPublishEvent(entity);
        publishTaskUpdateEvent(java.util.Collections.singletonList(hostRoleCommandFactory.createExisting(entity)));
        return entity;
    }

    @com.google.inject.persist.Transactional
    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity mergeWithoutPublishEvent(org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entity = entityManager.merge(entity);
        invalidateHostRoleCommandStatusSummaryCache(entity);
        return entity;
    }

    @com.google.inject.persist.Transactional
    public void removeByHostId(java.lang.Long hostId) {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commands = findByHostId(hostId);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity cmd : commands) {
            remove(cmd);
        }
    }

    @com.google.inject.persist.Transactional
    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> mergeAll(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities) {
        java.util.Set<java.lang.Long> requestsToInvalidate = new java.util.LinkedHashSet<>();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> managedList = new java.util.ArrayList<>(entities.size());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity : entities) {
            javax.persistence.EntityManager entityManager = entityManagerProvider.get();
            entity = entityManager.merge(entity);
            managedList.add(entity);
            java.lang.Long requestId = entity.getRequestId();
            if (requestId == null) {
                org.apache.ambari.server.orm.entities.StageEntity stageEntity = entity.getStage();
                if (stageEntity != null) {
                    requestId = stageEntity.getRequestId();
                }
            }
            requestsToInvalidate.add(requestId);
        }
        invalidateHostRoleCommandStatusSummaryCache(requestsToInvalidate);
        publishTaskUpdateEvent(getHostRoleCommands(entities));
        return managedList;
    }

    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getHostRoleCommands(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities) {
        com.google.common.base.Function<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, org.apache.ambari.server.actionmanager.HostRoleCommand> transform = new com.google.common.base.Function<org.apache.ambari.server.orm.entities.HostRoleCommandEntity, org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public org.apache.ambari.server.actionmanager.HostRoleCommand apply(org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity) {
                return hostRoleCommandFactory.createExisting(entity);
            }
        };
        return com.google.common.collect.FluentIterable.from(entities).transform(transform).toList();
    }

    public void publishTaskUpdateEvent(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        if (!hostRoleCommands.isEmpty()) {
            org.apache.ambari.server.events.TaskUpdateEvent taskUpdateEvent = new org.apache.ambari.server.events.TaskUpdateEvent(hostRoleCommands);
            taskEventPublisher.publish(taskUpdateEvent);
        }
    }

    public void publishTaskCreateEvent(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        if (!hostRoleCommands.isEmpty()) {
            org.apache.ambari.server.events.TaskCreateEvent taskCreateEvent = new org.apache.ambari.server.events.TaskCreateEvent(hostRoleCommands);
            taskEventPublisher.publish(taskCreateEvent);
        }
    }

    @com.google.inject.persist.Transactional
    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
    public void remove(org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.remove(entity);
        invalidateHostRoleCommandStatusSummaryCache(entity);
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(int taskId) {
        remove(findByPK(taskId));
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> findAggregateCounts(java.lang.Long requestId) {
        if (!hostRoleCommandStatusSummaryCacheEnabled) {
            return loadAggregateCounts(requestId);
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> map = hrcStatusSummaryCache.getIfPresent(requestId);
        if (null != map) {
            return map;
        }
        java.util.concurrent.locks.ReadWriteLock lock = transactionLocks.getLock(org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE);
        lock.readLock().lock();
        try {
            map = loadAggregateCounts(requestId);
            hrcStatusSummaryCache.put(requestId, map);
            return map;
        } finally {
            lock.readLock().unlock();
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity findMostRecentFailure(java.lang.Long requestId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findTasksByStatusesOrderByIdDesc", org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        query.setParameter("requestId", requestId);
        query.setParameter("statuses", org.apache.ambari.server.actionmanager.HostRoleStatus.STACK_UPGRADE_FAILED_STATUSES);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> results = query.getResultList();
        if (!results.isEmpty()) {
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity candidate = results.get(0);
            javax.persistence.TypedQuery<java.lang.Number> numberAlreadyRanTasksInFutureStage = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findNumTasksAlreadyRanInStage", java.lang.Number.class);
            numberAlreadyRanTasksInFutureStage.setParameter("requestId", requestId);
            numberAlreadyRanTasksInFutureStage.setParameter("taskId", candidate.getTaskId());
            numberAlreadyRanTasksInFutureStage.setParameter("stageId", candidate.getStageId());
            numberAlreadyRanTasksInFutureStage.setParameter("statuses", org.apache.ambari.server.actionmanager.HostRoleStatus.SCHEDULED_STATES);
            java.lang.Number result = daoUtils.selectSingle(numberAlreadyRanTasksInFutureStage);
            if (result.longValue() == 0L) {
                return candidate;
            }
        }
        return null;
    }

    @com.google.inject.persist.Transactional
    public void updateAutomaticSkipOnFailure(long requestId, boolean skipOnFailure, boolean skipOnServiceCheckFailure) {
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = findByRequest(requestId);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            org.apache.ambari.server.orm.entities.StageEntity stage = task.getStage();
            boolean isStageSkippable = stage.isSkippable();
            boolean isAutoSkipSupportedOnStage = stage.isAutoSkipOnFailureSupported();
            if ((!isStageSkippable) || (!isAutoSkipSupportedOnStage)) {
                task.setAutoSkipOnFailure(false);
            } else if (task.getRoleCommand() == org.apache.ambari.server.RoleCommand.SERVICE_CHECK) {
                task.setAutoSkipOnFailure(skipOnServiceCheckFailure);
            } else {
                task.setAutoSkipOnFailure(skipOnFailure);
            }
            merge(task);
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> findAll(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HostRoleCommandPredicateVisitor visitor = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.HostRoleCommandPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> query = visitor.getCriteriaQuery();
        javax.persistence.criteria.Predicate jpaPredicate = visitor.getJpaPredicate();
        if (null != jpaPredicate) {
            query.where(jpaPredicate);
        }
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = request.getSortRequest();
        if (null != sortRequest) {
            org.apache.ambari.server.api.query.JpaSortBuilder<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> sortBuilder = new org.apache.ambari.server.api.query.JpaSortBuilder<>();
            java.util.List<javax.persistence.criteria.Order> sortOrders = sortBuilder.buildSortOrders(sortRequest, visitor);
            query.orderBy(sortOrders);
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> typedQuery = entityManager.createQuery(query);
        org.apache.ambari.server.controller.spi.PageRequest pagination = request.getPageRequest();
        if (null != pagination) {
            typedQuery.setFirstResult(pagination.getOffset());
            typedQuery.setMaxResults(pagination.getPageSize());
        }
        return daoUtils.selectList(typedQuery);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.String> getHostsWithPendingTasks(long iLowestRequestIdInProgress, long iHighestRequestIdInProgress) {
        javax.persistence.TypedQuery<java.lang.String> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findHostsByCommandStatus", java.lang.String.class);
        query.setParameter("iLowestRequestIdInProgress", iLowestRequestIdInProgress);
        query.setParameter("iHighestRequestIdInProgress", iHighestRequestIdInProgress);
        query.setParameter("statuses", org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<java.lang.String> getBlockingHostsForRequest(long lowerRequestIdInclusive, long requestId) {
        javax.persistence.TypedQuery<java.lang.String> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.getBlockingHostsForRequest", java.lang.String.class);
        query.setParameter("lowerRequestIdInclusive", lowerRequestIdInclusive);
        query.setParameter("upperRequestIdExclusive", requestId);
        query.setParameter("statuses", org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO> getLatestServiceChecksByRole(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO> query = entityManagerProvider.get().createNamedQuery("HostRoleCommandEntity.findLatestServiceChecksByRole", org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("roleCommand", org.apache.ambari.server.RoleCommand.SERVICE_CHECK);
        return daoUtils.selectList(query);
    }

    private final class HostRoleCommandPredicateVisitor extends org.apache.ambari.server.api.query.JpaPredicateVisitor<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> {
        public HostRoleCommandPredicateVisitor() {
            super(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        }

        @java.lang.Override
        public java.lang.Class<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> getEntityClass() {
            return org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class;
        }

        @java.lang.Override
        public java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> getPredicateMapping(java.lang.String propertyId) {
            return org.apache.ambari.server.orm.entities.HostRoleCommandEntity_.getPredicateMapping().get(propertyId);
        }
    }

    public java.util.Set<java.lang.Long> findTaskIdsByRequestStageIds(java.util.List<org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK> requestStageIds) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        java.util.List<java.lang.Long> taskIds = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK requestIds : requestStageIds) {
            javax.persistence.TypedQuery<java.lang.Long> hostRoleCommandQuery = entityManager.createNamedQuery("HostRoleCommandEntity.findTaskIdsByRequestStageIds", java.lang.Long.class);
            hostRoleCommandQuery.setParameter("requestId", requestIds.getRequestId());
            hostRoleCommandQuery.setParameter("stageId", requestIds.getStageId());
            taskIds.addAll(daoUtils.selectList(hostRoleCommandQuery));
        }
        return com.google.common.collect.Sets.newHashSet(taskIds);
    }

    public static class LastServiceCheckDTO {
        public final java.lang.String role;

        public final long endTime;

        public LastServiceCheckDTO(java.lang.String role, long endTime) {
            this.role = role;
            this.endTime = endTime;
        }
    }
}