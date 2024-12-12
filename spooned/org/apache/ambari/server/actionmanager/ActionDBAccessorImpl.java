package org.apache.ambari.server.actionmanager;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class ActionDBAccessorImpl implements org.apache.ambari.server.actionmanager.ActionDBAccessor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);

    private long requestId;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.StageDAO stageDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.RoleSuccessCriteriaDAO roleSuccessCriteriaDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.RequestScheduleDAO requestScheduleDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.TaskEventPublisher taskEventPublisher;

    @com.google.inject.Inject
    org.apache.ambari.server.audit.AuditLogger auditLogger;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    org.apache.ambari.server.topology.TopologyManager topologyManager;

    private com.google.common.cache.Cache<java.lang.Long, org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails> auditlogRequestCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterAccess(60, java.util.concurrent.TimeUnit.MINUTES).concurrencyLevel(4).build();

    private com.google.common.cache.Cache<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandCache;

    private long cacheLimit;

    private final java.util.concurrent.locks.ReadWriteLock hrcOperationsLock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    @com.google.inject.Inject
    public ActionDBAccessorImpl(@com.google.inject.name.Named("executionCommandCacheSize")
    long cacheLimit, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) {
        this.cacheLimit = cacheLimit;
        hostRoleCommandCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterAccess(5, java.util.concurrent.TimeUnit.MINUTES).build();
        eventPublisher.register(this);
    }

    @com.google.inject.Inject
    void init() {
        requestId = stageDAO.getLastRequestId();
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.Stage getStage(java.lang.String actionId) {
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = stageDAO.findByActionId(actionId);
        return stageEntity == null ? null : stageFactory.createExisting(stageEntity);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.Stage> getAllStages(long requestId) {
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = stageDAO.findByRequestId(requestId);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>(stageEntities.size());
        for (org.apache.ambari.server.orm.entities.StageEntity stageEntity : stageEntities) {
            stages.add(stageFactory.createExisting(stageEntity));
        }
        return stages;
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.RequestEntity getRequestEntity(long requestId) {
        return requestDAO.findByPK(requestId);
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.Request getRequest(long requestId) {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = getRequestEntity(requestId);
        if (requestEntity != null) {
            return requestFactory.createExisting(requestEntity);
        } else {
            return null;
        }
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortOperation(long requestId) {
        try {
            hrcOperationsLock.writeLock().lock();
            java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortedHostRoleCommands = new java.util.ArrayList<>();
            long now = java.lang.System.currentTimeMillis();
            java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commands = hostRoleCommandDAO.findByRequestIdAndStatuses(requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.SCHEDULED_STATES);
            for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : commands) {
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                command.setEndTime(now);
                abortedHostRoleCommands.add(hostRoleCommandDAO.merge(command));
                org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.info((((((((("Aborted command. Hostname " + command.getHostName()) + " role ") + command.getRole()) + " requestId ") + command.getRequestId()) + " taskId ") + command.getTaskId()) + " stageId ") + command.getStageId());
                auditLog(command, requestId);
                cacheHostRoleCommand(hostRoleCommandFactory.createExisting(command));
            }
            endRequest(requestId);
            return abortedHostRoleCommands;
        } finally {
            hrcOperationsLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public void timeoutHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role) {
        timeoutHostRole(host, requestId, stageId, role, false, false);
    }

    @java.lang.Override
    public void timeoutHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role, boolean skipSupported, boolean hostUnknownState) {
        long now = java.lang.System.currentTimeMillis();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commands = hostRoleCommandDAO.findByHostRole(host, requestId, stageId, role);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : commands) {
            if (skipSupported) {
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
            } else {
                command.setStatus(command.isRetryAllowed() ? org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT : hostUnknownState ? org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED : org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT);
            }
            command.setEndTime(now);
            auditLog(command, requestId);
        }
        if (!commands.isEmpty()) {
            hostRoleCommandDAO.mergeAll(commands);
        }
        endRequestIfCompleted(requestId);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.Stage> getStagesInProgressForRequest(java.lang.Long requestId) {
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = stageDAO.findByRequestIdAndCommandStatuses(requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES);
        return getStagesForEntities(stageEntities);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.Stage> getFirstStageInProgressPerRequest() {
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = stageDAO.findFirstStageByStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>(stageEntities.size());
        for (org.apache.ambari.server.orm.entities.StageEntity stageEntity : stageEntities) {
            stages.add(stageFactory.createExisting(stageEntity));
        }
        return stages;
    }

    private java.util.List<org.apache.ambari.server.actionmanager.Stage> getStagesForEntities(java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stageEntities) {
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>(stageEntities.size());
        for (org.apache.ambari.server.orm.entities.StageEntity stageEntity : stageEntities) {
            stages.add(stageFactory.createExisting(stageEntity));
        }
        return stages;
    }

    @java.lang.Override
    public int getCommandsInProgressCount() {
        java.lang.Number count = hostRoleCommandDAO.getCountByStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES);
        if (null == count) {
            return 0;
        }
        return count.intValue();
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
    public void persistActions(org.apache.ambari.server.actionmanager.Request request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = request.constructNewPersistenceEntity();
        java.lang.Long clusterId = -1L;
        java.lang.String clusterName = null;
        java.lang.Long requestId = requestEntity.getRequestId();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(request.getClusterId());
        if (clusterEntity != null) {
            clusterId = clusterEntity.getClusterId();
            clusterName = clusterEntity.getClusterName();
        }
        requestEntity.setClusterId(clusterId);
        requestDAO.create(requestEntity);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = new java.util.ArrayList<>(request.getStages().size());
        addRequestToAuditlogCache(request);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.Stage stage : request.getStages()) {
            org.apache.ambari.server.orm.entities.StageEntity stageEntity = stage.constructNewPersistenceEntity();
            java.lang.Long stageId = stageEntity.getStageId();
            stageEntities.add(stageEntity);
            stageEntity.setClusterId(clusterId);
            stageEntity.setRequest(requestEntity);
            stageDAO.create(stageEntity);
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> orderedHostRoleCommands = stage.getOrderedHostRoleCommands();
            java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommandEntities = new java.util.ArrayList<>();
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : orderedHostRoleCommands) {
                hostRoleCommand.setRequestId(requestId);
                hostRoleCommand.setStageId(stageId);
                org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = hostRoleCommand.constructNewPersistenceEntity();
                hostRoleCommandEntity.setStage(stageEntity);
                hostRoleCommandDAO.create(hostRoleCommandEntity);
                hostRoleCommandEntities.add(hostRoleCommandEntity);
                hostRoleCommand.setTaskId(hostRoleCommandEntity.getTaskId());
                java.lang.String prefix = "";
                java.lang.String output = ("output-" + hostRoleCommandEntity.getTaskId()) + ".txt";
                java.lang.String error = ("errors-" + hostRoleCommandEntity.getTaskId()) + ".txt";
                org.apache.ambari.server.orm.entities.HostEntity hostEntity = null;
                if (null != hostRoleCommandEntity.getHostId()) {
                    hostEntity = hostDAO.findById(hostRoleCommandEntity.getHostId());
                    if (hostEntity == null) {
                        java.lang.String msg = java.lang.String.format("Host %s doesn't exist in database", hostRoleCommandEntity.getHostName());
                        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.error(msg);
                        throw new org.apache.ambari.server.AmbariException(msg);
                    }
                    hostRoleCommandEntity.setHostEntity(hostEntity);
                    try {
                        org.apache.ambari.server.state.Host hostObject = clusters.getHost(hostEntity.getHostName());
                        if (!org.apache.commons.lang.StringUtils.isBlank(hostObject.getPrefix())) {
                            prefix = hostObject.getPrefix();
                            if (!prefix.endsWith("/")) {
                                prefix = prefix + "/";
                            }
                        }
                    } catch (org.apache.ambari.server.AmbariException e) {
                        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.warn("Exception in getting prefix for host and setting output and error log files.  Using no prefix");
                    }
                }
                hostRoleCommand.setOutputLog(prefix + output);
                hostRoleCommand.setErrorLog(prefix + error);
                hostRoleCommandEntity.setOutputLog(hostRoleCommand.getOutputLog());
                hostRoleCommandEntity.setErrorLog(hostRoleCommand.getErrorLog());
                org.apache.ambari.server.orm.entities.ExecutionCommandEntity executionCommandEntity = hostRoleCommand.constructExecutionCommandEntity();
                executionCommandEntity.setHostRoleCommand(hostRoleCommandEntity);
                executionCommandEntity.setTaskId(hostRoleCommandEntity.getTaskId());
                hostRoleCommandEntity.setExecutionCommand(executionCommandEntity);
                executionCommandDAO.create(hostRoleCommandEntity.getExecutionCommand());
                hostRoleCommandEntity = hostRoleCommandDAO.mergeWithoutPublishEvent(hostRoleCommandEntity);
                if (null != hostEntity) {
                    hostEntity = hostDAO.merge(hostEntity);
                }
                hostRoleCommands.add(hostRoleCommand);
            }
            for (org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity roleSuccessCriteriaEntity : stageEntity.getRoleSuccessCriterias()) {
                roleSuccessCriteriaDAO.create(roleSuccessCriteriaEntity);
            }
            stageEntity.setHostRoleCommands(hostRoleCommandEntities);
            stageEntity = stageDAO.merge(stageEntity);
        }
        requestEntity.setStages(stageEntities);
        requestDAO.merge(requestEntity);
        org.apache.ambari.server.events.TaskCreateEvent taskCreateEvent = new org.apache.ambari.server.events.TaskCreateEvent(hostRoleCommands);
        taskEventPublisher.publish(taskCreateEvent);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommandEntities = hostRoleCommandDAO.findByRequest(requestEntity.getRequestId());
        if (clusterName != null) {
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.RequestUpdateEvent(requestEntity, hostRoleCommandDAO, topologyManager, clusterName, hostRoleCommandEntities));
        } else {
            org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.debug("No STOMP request update event was fired for new request due no cluster related, " + "request id: {}, command name: {}", requestEntity.getRequestId(), requestEntity.getCommandName());
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void startRequest(long requestId) {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = getRequestEntity(requestId);
        if ((requestEntity != null) && (requestEntity.getStartTime() == (-1L))) {
            requestEntity.setStartTime(java.lang.System.currentTimeMillis());
            requestDAO.merge(requestEntity);
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void endRequest(long requestId) {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = getRequestEntity(requestId);
        if ((requestEntity != null) && (requestEntity.getEndTime() == (-1L))) {
            requestEntity.setEndTime(java.lang.System.currentTimeMillis());
            requestDAO.merge(requestEntity);
            ambariEventPublisher.publish(new org.apache.ambari.server.events.RequestFinishedEvent(requestEntity.getClusterId(), requestId));
        }
    }

    public void endRequestIfCompleted(long requestId) {
        if (requestDAO.isAllTasksCompleted(requestId)) {
            endRequest(requestId);
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setSourceScheduleForRequest(long requestId, long scheduleId) {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = requestDAO.findByPK(requestId);
        if (requestEntity != null) {
            org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(scheduleId);
            if (scheduleEntity != null) {
                requestEntity.setRequestScheduleEntity(scheduleEntity);
                scheduleEntity.getRequestEntities().add(requestEntity);
                requestDAO.merge(requestEntity);
                requestScheduleDAO.merge(scheduleEntity);
            } else {
                java.lang.String message = java.lang.String.format("Request Schedule with id=%s not found", scheduleId);
                org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.error(message);
                throw new java.lang.RuntimeException(message);
            }
        } else {
            java.lang.String message = java.lang.String.format("Request with id=%s not found", scheduleId);
            org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.error(message);
            throw new java.lang.RuntimeException(message);
        }
    }

    @java.lang.Override
    public void updateHostRoleStates(java.util.Collection<org.apache.ambari.server.agent.CommandReport> reports) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.agent.CommandReport> taskReports = new java.util.HashMap<>();
        for (org.apache.ambari.server.agent.CommandReport report : reports) {
            taskReports.put(report.getTaskId(), report);
        }
        long now = java.lang.System.currentTimeMillis();
        java.util.List<java.lang.Long> requestsToCheck = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities;
        try {
            hrcOperationsLock.readLock().lock();
            commandEntities = hostRoleCommandDAO.findByPKs(taskReports.keySet());
        } finally {
            hrcOperationsLock.readLock().unlock();
        }
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity : commandEntities) {
            org.apache.ambari.server.agent.CommandReport report = taskReports.get(commandEntity.getTaskId());
            org.apache.ambari.server.actionmanager.HostRoleStatus existingTaskStatus = commandEntity.getStatus();
            org.apache.ambari.server.actionmanager.HostRoleStatus reportedTaskStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus());
            if (!existingTaskStatus.isCompletedState()) {
            }
            if ((!existingTaskStatus.isCompletedState()) || (existingTaskStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED)) {
                if ((reportedTaskStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED) && commandEntity.isRetryAllowed()) {
                    reportedTaskStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED;
                    if (commandEntity.isFailureAutoSkipped()) {
                        reportedTaskStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED;
                    }
                }
                if ((reportedTaskStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT) && commandEntity.isRetryAllowed()) {
                    reportedTaskStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT;
                }
                if (!existingTaskStatus.isCompletedState()) {
                    org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.debug("Setting status from {} to {} for {}", existingTaskStatus, reportedTaskStatus, commandEntity.getTaskId());
                    commandEntity.setStatus(reportedTaskStatus);
                }
                commandEntity.setStdOut(report.getStdOut() == null ? null : report.getStdOut().getBytes());
                commandEntity.setStdError(report.getStdErr() == null ? null : report.getStdErr().getBytes());
                commandEntity.setStructuredOut(report.getStructuredOut() == null ? null : report.getStructuredOut().getBytes());
                commandEntity.setExitcode(report.getExitCode());
                if (commandEntity.getStatus().isCompletedState()) {
                    commandEntity.setEndTime(now);
                }
                try {
                    hrcOperationsLock.writeLock().lock();
                    hostRoleCommandDAO.merge(commandEntity);
                } finally {
                    hrcOperationsLock.writeLock().unlock();
                }
                if (commandEntity.getStatus().isCompletedState()) {
                    java.lang.String actionId = report.getActionId();
                    long[] requestStageIds = org.apache.ambari.server.utils.StageUtils.getRequestStage(actionId);
                    long requestId = requestStageIds[0];
                    long stageId = requestStageIds[1];
                    auditLog(commandEntity, requestId);
                    if (requestDAO.getLastStageId(requestId).equals(stageId)) {
                        requestsToCheck.add(requestId);
                    }
                }
            } else {
                org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.warn(java.lang.String.format("Request for invalid transition of host role command status received for task id %d from " + "agent: %s -> %s", commandEntity.getTaskId(), existingTaskStatus, reportedTaskStatus));
            }
        }
        for (java.lang.Long requestId : requestsToCheck) {
            endRequestIfCompleted(requestId);
        }
    }

    @java.lang.Override
    public void updateHostRoleState(java.lang.String hostname, long requestId, long stageId, java.lang.String role, org.apache.ambari.server.agent.CommandReport report) {
        boolean checkRequest = false;
        if (org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.debug("Update HostRoleState: HostName {} requestId {} stageId {} role {} report {}", hostname, requestId, stageId, role, report);
        }
        long now = java.lang.System.currentTimeMillis();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commands = hostRoleCommandDAO.findByHostRole(hostname, requestId, stageId, role);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : commands) {
            org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus());
            if ((status == org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED) && command.isRetryAllowed()) {
                status = org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED;
                if (command.isFailureAutoSkipped()) {
                    status = org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED;
                }
            }
            if ((status == org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT) && command.isRetryAllowed()) {
                status = org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT;
            }
            command.setStatus(status);
            command.setStdOut(report.getStdOut().getBytes());
            command.setStdError(report.getStdErr().getBytes());
            command.setStructuredOut(report.getStructuredOut() == null ? null : report.getStructuredOut().getBytes());
            if (org.apache.ambari.server.actionmanager.HostRoleStatus.getCompletedStates().contains(command.getStatus())) {
                command.setEndTime(now);
                if (requestDAO.getLastStageId(requestId).equals(stageId)) {
                    checkRequest = true;
                }
            }
            command.setExitcode(report.getExitCode());
            auditLog(command, requestId);
        }
        if (!commands.isEmpty()) {
            hostRoleCommandDAO.mergeAll(commands);
        }
        if (checkRequest) {
            endRequestIfCompleted(requestId);
        }
    }

    @java.lang.Override
    public void abortHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role) {
        java.lang.String reason = java.lang.String.format("On host %s role %s in invalid state.", host, role);
        abortHostRole(host, requestId, stageId, role, reason);
    }

    @java.lang.Override
    public void abortHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role, java.lang.String reason) {
        org.apache.ambari.server.agent.CommandReport report = new org.apache.ambari.server.agent.CommandReport();
        report.setExitCode(999);
        report.setStdErr(reason);
        report.setStdOut("");
        report.setStatus("ABORTED");
        updateHostRoleState(host, requestId, stageId, role, report);
    }

    @java.lang.Override
    public long getLastPersistedRequestIdWhenInitialized() {
        return requestId;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void bulkHostRoleScheduled(org.apache.ambari.server.actionmanager.Stage s, java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commands) {
        for (org.apache.ambari.server.agent.ExecutionCommand command : commands) {
            hostRoleScheduled(s, command.getHostname(), command.getRole());
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void bulkAbortHostRole(org.apache.ambari.server.actionmanager.Stage s, java.util.Map<org.apache.ambari.server.agent.ExecutionCommand, java.lang.String> commands) {
        for (org.apache.ambari.server.agent.ExecutionCommand command : commands.keySet()) {
            java.lang.String reason = java.lang.String.format("On host %s role %s in invalid state.\n%s", command.getHostname(), command.getRole(), commands.get(command));
            abortHostRole(command.getHostname(), s.getRequestId(), s.getStageId(), command.getRole(), reason);
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void hostRoleScheduled(org.apache.ambari.server.actionmanager.Stage s, java.lang.String hostname, java.lang.String roleStr) {
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = s.getHostRoleCommand(hostname, roleStr);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = hostRoleCommandDAO.findByPK(hostRoleCommand.getTaskId());
        if (entity != null) {
            entity.setStartTime(hostRoleCommand.getStartTime());
            if ((entity.getOriginalStartTime() == null) || (entity.getOriginalStartTime() == (-1))) {
                entity.setOriginalStartTime(java.lang.System.currentTimeMillis());
            }
            entity.setLastAttemptTime(hostRoleCommand.getLastAttemptTime());
            entity.setStatus(hostRoleCommand.getStatus());
            entity.setAttemptCount(hostRoleCommand.getAttemptCount());
            auditLog(entity, s.getRequestId());
            hostRoleCommandDAO.merge(entity);
        } else {
            throw new java.lang.RuntimeException("HostRoleCommand is not persisted, cannot update:\n" + hostRoleCommand);
        }
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getRequestTasks(long requestId) {
        return getTasks(hostRoleCommandDAO.findTaskIdsByRequest(requestId));
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getAllTasksByRequestIds(java.util.Collection<java.lang.Long> requestIds) {
        if (requestIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return getTasks(hostRoleCommandDAO.findTaskIdsByRequestIds(requestIds));
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasks(java.util.Collection<java.lang.Long> taskIds) {
        if (taskIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands;
        try {
            hrcOperationsLock.readLock().lock();
            java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> cached = hostRoleCommandCache.getAllPresent(taskIds);
            commands = new java.util.ArrayList<>(cached.values());
            java.util.List<java.lang.Long> absent = new java.util.ArrayList<>(taskIds);
            absent.removeAll(cached.keySet());
            if (!absent.isEmpty()) {
                for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity : hostRoleCommandDAO.findByPKs(absent)) {
                    org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.createExisting(commandEntity);
                    commands.add(hostRoleCommand);
                    cacheHostRoleCommand(hostRoleCommand);
                }
            }
            commands.sort((o1, o2) -> ((int) (o1.getTaskId() - o2.getTaskId())));
        } finally {
            hrcOperationsLock.readLock().unlock();
        }
        return commands;
    }

    private void cacheHostRoleCommand(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand) {
        if (hostRoleCommandCache.size() <= cacheLimit) {
            switch (hostRoleCommand.getStatus()) {
                case ABORTED :
                case COMPLETED :
                case TIMEDOUT :
                case FAILED :
                    hostRoleCommandCache.put(hostRoleCommand.getTaskId(), hostRoleCommand);
                    break;
                default :
                    break;
            }
        }
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasksByHostRoleAndStatus(java.lang.String hostname, java.lang.String role, org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        return getTasks(hostRoleCommandDAO.findTaskIdsByHostRoleAndStatus(hostname, role, status));
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasksByRoleAndStatus(java.lang.String role, org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        return getTasks(hostRoleCommandDAO.findTaskIdsByRoleAndStatus(role, status));
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.HostRoleCommand getTask(long taskId) {
        try {
            hrcOperationsLock.readLock().lock();
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity = hostRoleCommandDAO.findByPK(taskId);
            if (commandEntity == null) {
                return null;
            }
            return hostRoleCommandFactory.createExisting(commandEntity);
        } finally {
            hrcOperationsLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public java.util.List<java.lang.Long> getRequestsByStatus(org.apache.ambari.server.actionmanager.RequestStatus status, int maxResults, boolean ascOrder) {
        if (null == status) {
            return requestDAO.findAllRequestIds(maxResults, ascOrder);
        }
        java.util.Set<org.apache.ambari.server.actionmanager.HostRoleStatus> taskStatuses = null;
        switch (status) {
            case IN_PROGRESS :
                taskStatuses = org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES;
                break;
            case FAILED :
                taskStatuses = org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED_STATUSES;
                break;
            case COMPLETED :
                return hostRoleCommandDAO.getCompletedRequests(maxResults, ascOrder);
        }
        return hostRoleCommandDAO.getRequestsByTaskStatus(taskStatuses, maxResults, ascOrder);
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, java.lang.String> getRequestContext(java.util.List<java.lang.Long> requestIds) {
        return stageDAO.findRequestContext(requestIds);
    }

    @java.lang.Override
    public java.lang.String getRequestContext(long requestId) {
        return stageDAO.findRequestContext(requestId);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.Request> getRequests(java.util.Collection<java.lang.Long> requestIds) {
        java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> requestEntities = requestDAO.findByPks(requestIds);
        java.util.List<org.apache.ambari.server.actionmanager.Request> requests = new java.util.ArrayList<>(requestEntities.size());
        for (org.apache.ambari.server.orm.entities.RequestEntity requestEntity : requestEntities) {
            requests.add(requestFactory.createExisting(requestEntity));
        }
        return requests;
    }

    @java.lang.Override
    public void resubmitTasks(java.util.List<java.lang.Long> taskIds) {
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = hostRoleCommandDAO.findByPKs(taskIds);
        java.util.Set<org.apache.ambari.server.orm.entities.RequestEntity> requestEntities = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            org.apache.ambari.server.orm.entities.StageEntity stage = task.getStage();
            stage.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
            stageEntities.add(stage);
            org.apache.ambari.server.orm.entities.RequestEntity request = stage.getRequest();
            request.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
            requestEntities.add(request);
            task.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
            task.setStartTime(-1L);
            task.setEndTime(-1L);
            auditLog(task, task.getRequestId());
        }
        for (org.apache.ambari.server.orm.entities.StageEntity stageEntity : stageEntities) {
            stageDAO.merge(stageEntity);
        }
        for (org.apache.ambari.server.orm.entities.RequestEntity requestEntity : requestEntities) {
            requestDAO.merge(requestEntity);
        }
        if (!tasks.isEmpty()) {
            hostRoleCommandDAO.mergeAll(tasks);
        }
        hostRoleCommandCache.invalidateAll(taskIds);
    }

    @com.google.common.eventbus.Subscribe
    public void invalidateCommandCacheOnHostRemove(org.apache.ambari.server.events.HostsRemovedEvent event) {
        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.LOG.info("Invalidating HRC cache after receiveing {}", event);
        hostRoleCommandCache.invalidateAll();
    }

    private org.apache.ambari.server.actionmanager.HostRoleStatus updateAuditlogCache(org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity, java.lang.Long requestId) {
        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails details = auditlogRequestCache.getIfPresent(requestId);
        if (details == null) {
            return null;
        }
        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component component = new org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component(commandEntity.getRole(), commandEntity.getHostName());
        org.apache.ambari.server.actionmanager.HostRoleStatus lastTaskStatus = null;
        if (details.getTasks().containsKey(component)) {
            lastTaskStatus = details.getTasks().get(component);
        }
        details.getTasks().put(component, commandEntity.getStatus());
        return lastTaskStatus;
    }

    private void addRequestToAuditlogCache(org.apache.ambari.server.actionmanager.Request request) {
        if (!auditLogger.isEnabled()) {
            return;
        }
        if (auditlogRequestCache.getIfPresent(request.getRequestId()) == null) {
            int numberOfTasks = 0;
            for (org.apache.ambari.server.actionmanager.Stage stage : request.getStages()) {
                numberOfTasks += stage.getOrderedHostRoleCommands().size();
            }
            org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails requestDetails = new org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails();
            requestDetails.setNumberOfTasks(numberOfTasks);
            requestDetails.setUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
            requestDetails.setProxyUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName());
            auditlogRequestCache.put(request.getRequestId(), requestDetails);
        }
    }

    private void auditLog(org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity, java.lang.Long requestId) {
        if (!auditLogger.isEnabled()) {
            return;
        }
        if (requestId != null) {
            org.apache.ambari.server.actionmanager.HostRoleStatus lastTaskStatus = updateAuditlogCache(commandEntity, requestId);
            org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails details = auditlogRequestCache.getIfPresent(requestId);
            if (details != null) {
                org.apache.ambari.server.actionmanager.HostRoleStatus calculatedStatus = calculateStatus(requestId, details.getNumberOfTasks());
                if (details.getLastStatus() != calculatedStatus) {
                    org.apache.ambari.server.orm.entities.RequestEntity request = requestDAO.findByPK(requestId);
                    java.lang.String context = (request != null) ? request.getRequestContext() : null;
                    org.apache.ambari.server.audit.event.AuditEvent auditEvent = org.apache.ambari.server.audit.event.OperationStatusAuditEvent.builder().withRequestId(java.lang.String.valueOf(requestId)).withStatus(java.lang.String.valueOf(calculatedStatus)).withRequestContext(context).withUserName(details.getUserName()).withProxyUserName(details.getProxyUserName()).withTimestamp(java.lang.System.currentTimeMillis()).build();
                    auditLogger.log(auditEvent);
                    details.setLastStatus(calculatedStatus);
                }
            }
            logTask(commandEntity, requestId, lastTaskStatus);
        }
    }

    private org.apache.ambari.server.actionmanager.HostRoleStatus calculateStatus(java.lang.Long requestId, int numberOfTasks) {
        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails details = auditlogRequestCache.getIfPresent(requestId);
        if (details == null) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED;
        }
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> taskStatuses = details.getTaskStatuses();
        return org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(taskStatuses), numberOfTasks, false);
    }

    private void logTask(org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity, java.lang.Long requestId, org.apache.ambari.server.actionmanager.HostRoleStatus lastTaskStatus) {
        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component component = new org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component(commandEntity.getRole(), commandEntity.getHostName());
        org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails details = auditlogRequestCache.getIfPresent(requestId);
        if (details == null) {
            return;
        }
        org.apache.ambari.server.actionmanager.HostRoleStatus cachedStatus = details.getTasks().get(component);
        if ((lastTaskStatus == null) || (cachedStatus != lastTaskStatus)) {
            org.apache.ambari.server.audit.event.AuditEvent taskEvent = org.apache.ambari.server.audit.event.TaskStatusAuditEvent.builder().withTaskId(java.lang.String.valueOf(commandEntity.getTaskId())).withHostName(commandEntity.getHostName()).withUserName(details.getUserName()).withProxyUserName(details.getProxyUserName()).withOperation((commandEntity.getRoleCommand() + " ") + commandEntity.getRole()).withDetails(commandEntity.getCommandDetail()).withStatus(commandEntity.getStatus().toString()).withRequestId(java.lang.String.valueOf(requestId)).withTimestamp(java.lang.System.currentTimeMillis()).build();
            auditLogger.log(taskEvent);
        }
    }

    private static class RequestDetails {
        org.apache.ambari.server.actionmanager.HostRoleStatus lastStatus = null;

        int numberOfTasks = 0;

        java.lang.String userName;

        java.util.Map<org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component, org.apache.ambari.server.actionmanager.HostRoleStatus> tasks = new java.util.HashMap<>();

        private java.lang.String proxyUserName;

        public org.apache.ambari.server.actionmanager.HostRoleStatus getLastStatus() {
            return lastStatus;
        }

        public void setLastStatus(org.apache.ambari.server.actionmanager.HostRoleStatus lastStatus) {
            this.lastStatus = lastStatus;
        }

        public int getNumberOfTasks() {
            return numberOfTasks;
        }

        public void setNumberOfTasks(int numberOfTasks) {
            this.numberOfTasks = numberOfTasks;
        }

        public java.lang.String getUserName() {
            return userName;
        }

        public void setUserName(java.lang.String userName) {
            this.userName = userName;
        }

        public java.util.Map<org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component, org.apache.ambari.server.actionmanager.HostRoleStatus> getTasks() {
            return tasks;
        }

        public java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> getTaskStatuses() {
            return getTasks().values();
        }

        public java.lang.String getProxyUserName() {
            return proxyUserName;
        }

        public void setProxyUserName(java.lang.String proxyUserName) {
            this.proxyUserName = proxyUserName;
        }

        static class Component {
            private final org.apache.ambari.server.Role role;

            private final java.lang.String hostName;

            Component(org.apache.ambari.server.Role role, java.lang.String hostName) {
                this.role = role;
                this.hostName = hostName;
            }

            public org.apache.ambari.server.Role getRole() {
                return role;
            }

            public java.lang.String getHostName() {
                return hostName;
            }

            @java.lang.Override
            public final int hashCode() {
                int hash = 7;
                java.lang.String roleStr = (role == null) ? "null" : role.toString();
                java.lang.String hostNameStr = (hostName == null) ? "null" : hostName;
                java.lang.String str = roleStr.concat(hostNameStr);
                for (int i = 0; i < str.length(); i++) {
                    hash = (hash * 31) + str.charAt(i);
                }
                return hash;
            }

            @java.lang.Override
            public final boolean equals(final java.lang.Object other) {
                if (other instanceof org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component) {
                    org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component comp = ((org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.RequestDetails.Component) (other));
                    return java.util.Objects.equals(comp.role, role) && java.util.Objects.equals(comp.hostName, hostName);
                }
                return false;
            }
        }
    }
}