package org.apache.ambari.server.events.listeners.tasks;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class TaskStatusListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.class);

    private java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> activeTasksMap = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.Map<java.lang.Long, org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveRequest> activeRequestMap = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.Map<org.apache.ambari.server.orm.entities.StageEntityPK, org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage> activeStageMap = new java.util.concurrent.ConcurrentHashMap<>();

    private org.apache.ambari.server.orm.dao.StageDAO stageDAO;

    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    private org.apache.ambari.server.api.stomp.NamedTasksSubscriptions namedTasksSubscriptions;

    @com.google.inject.Inject
    public TaskStatusListener(org.apache.ambari.server.events.publishers.TaskEventPublisher taskEventPublisher, org.apache.ambari.server.orm.dao.StageDAO stageDAO, org.apache.ambari.server.orm.dao.RequestDAO requestDAO, org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher, org.apache.ambari.server.api.stomp.NamedTasksSubscriptions namedTasksSubscriptions) {
        this.stageDAO = stageDAO;
        this.requestDAO = requestDAO;
        this.STOMPUpdatePublisher = STOMPUpdatePublisher;
        this.namedTasksSubscriptions = namedTasksSubscriptions;
        taskEventPublisher.register(this);
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> getActiveTasksMap() {
        return activeTasksMap;
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveRequest> getActiveRequestMap() {
        return activeRequestMap;
    }

    public java.util.Map<org.apache.ambari.server.orm.entities.StageEntityPK, org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage> getActiveStageMap() {
        return activeStageMap;
    }

    @com.google.common.eventbus.Subscribe
    public void onTaskUpdateEvent(org.apache.ambari.server.events.TaskUpdateEvent event) {
        org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.debug("Received task update event {}", event);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandListAll = event.getHostRoleCommands();
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandWithReceivedStatus = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> stagesWithReceivedTaskStatus = new java.util.HashSet<>();
        java.util.Set<java.lang.Long> requestIdsWithReceivedTaskStatus = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.events.RequestUpdateEvent> requestsToPublish = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.events.NamedTaskUpdateEvent> namedTasksToPublish = new java.util.HashSet<>();
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : hostRoleCommandListAll) {
            java.lang.Long reportedTaskId = hostRoleCommand.getTaskId();
            org.apache.ambari.server.actionmanager.HostRoleCommand activeTask = activeTasksMap.get(reportedTaskId);
            if (activeTask == null) {
                org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.error(java.lang.String.format("Received update for a task %d which is not being tracked as running task", reportedTaskId));
            } else {
                hostRoleCommandWithReceivedStatus.add(hostRoleCommand);
                org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK = new org.apache.ambari.server.orm.entities.StageEntityPK();
                stageEntityPK.setRequestId(hostRoleCommand.getRequestId());
                stageEntityPK.setStageId(hostRoleCommand.getStageId());
                stagesWithReceivedTaskStatus.add(stageEntityPK);
                requestIdsWithReceivedTaskStatus.add(hostRoleCommand.getRequestId());
                org.apache.ambari.server.events.NamedTaskUpdateEvent namedTaskUpdateEvent = new org.apache.ambari.server.events.NamedTaskUpdateEvent(hostRoleCommand);
                if (namedTasksSubscriptions.checkTaskId(reportedTaskId) && (!namedTaskUpdateEvent.equals(new org.apache.ambari.server.events.NamedTaskUpdateEvent(activeTasksMap.get(reportedTaskId))))) {
                    namedTasksToPublish.add(namedTaskUpdateEvent);
                }
                if (hostRoleCommand.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) {
                    namedTasksSubscriptions.removeTaskId(reportedTaskId);
                }
                if (!activeTasksMap.get(reportedTaskId).getStatus().equals(hostRoleCommand.getStatus())) {
                    java.lang.Long clusterId = activeRequestMap.get(hostRoleCommand.getRequestId()).getClusterId();
                    if ((clusterId != null) && (clusterId != (-1))) {
                        java.util.Set<org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand> hostRoleCommands = new java.util.HashSet<>();
                        hostRoleCommands.add(new org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand(hostRoleCommand.getTaskId(), hostRoleCommand.getRequestId(), hostRoleCommand.getStatus(), hostRoleCommand.getHostName()));
                        requestsToPublish.add(new org.apache.ambari.server.events.RequestUpdateEvent(hostRoleCommand.getRequestId(), activeRequestMap.get(hostRoleCommand.getRequestId()).getStatus(), hostRoleCommands));
                    } else {
                        org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.debug("No STOMP request update event was fired for host component status change due no cluster related, " + "request id: {}, role: {}, role command: {}, host: {}, task id: {}, old state: {}, new state: {}", hostRoleCommand.getRequestId(), hostRoleCommand.getRole(), hostRoleCommand.getRoleCommand(), hostRoleCommand.getHostName(), hostRoleCommand.getTaskId(), activeTasksMap.get(reportedTaskId).getStatus(), hostRoleCommand.getStatus());
                    }
                }
            }
        }
        updateActiveTasksMap(hostRoleCommandWithReceivedStatus);
        java.lang.Boolean didAnyStageStatusUpdated = updateActiveStagesStatus(stagesWithReceivedTaskStatus, hostRoleCommandListAll);
        if (didAnyStageStatusUpdated) {
            updateActiveRequestsStatus(requestIdsWithReceivedTaskStatus, stagesWithReceivedTaskStatus);
        }
        for (org.apache.ambari.server.events.RequestUpdateEvent requestToPublish : requestsToPublish) {
            STOMPUpdatePublisher.publish(requestToPublish);
        }
        for (org.apache.ambari.server.events.NamedTaskUpdateEvent namedTaskUpdateEvent : namedTasksToPublish) {
            org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.info(java.lang.String.format("NamedTaskUpdateEvent with id %s will be send", namedTaskUpdateEvent.getId()));
            STOMPUpdatePublisher.publish(namedTaskUpdateEvent);
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onTaskCreateEvent(org.apache.ambari.server.events.TaskCreateEvent event) {
        org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.debug("Received task create event {}", event);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandListAll = event.getHostRoleCommands();
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : hostRoleCommandListAll) {
            activeTasksMap.put(hostRoleCommand.getTaskId(), hostRoleCommand);
            addStagePK(hostRoleCommand);
            addRequestId(hostRoleCommand);
        }
    }

    private void updateActiveTasksMap(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandWithReceivedStatus) {
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : hostRoleCommandWithReceivedStatus) {
            java.lang.Long taskId = hostRoleCommand.getTaskId();
            activeTasksMap.put(taskId, hostRoleCommand);
        }
    }

    private void addStagePK(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand) {
        org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK = new org.apache.ambari.server.orm.entities.StageEntityPK();
        stageEntityPK.setRequestId(hostRoleCommand.getRequestId());
        stageEntityPK.setStageId(hostRoleCommand.getStageId());
        if (activeStageMap.containsKey(stageEntityPK)) {
            activeStageMap.get(stageEntityPK).addTaskId(hostRoleCommand.getTaskId());
        } else {
            org.apache.ambari.server.orm.entities.StageEntity stageEntity = stageDAO.findByPK(stageEntityPK);
            assert stageEntity != null;
            java.util.Map<org.apache.ambari.server.Role, java.lang.Float> successFactors = new java.util.HashMap<>();
            java.util.Collection<org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity> roleSuccessCriteriaEntities = stageEntity.getRoleSuccessCriterias();
            for (org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity successCriteriaEntity : roleSuccessCriteriaEntities) {
                successFactors.put(successCriteriaEntity.getRole(), successCriteriaEntity.getSuccessFactor().floatValue());
            }
            java.util.Set<java.lang.Long> taskIdSet = com.google.common.collect.Sets.newHashSet(hostRoleCommand.getTaskId());
            org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage reportedStage = new org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage(stageEntity.getStatus(), stageEntity.getDisplayStatus(), successFactors, stageEntity.isSkippable(), taskIdSet);
            activeStageMap.put(stageEntityPK, reportedStage);
        }
    }

    private java.lang.Boolean updateActiveStagesStatus(final java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> stagesWithReceivedTaskStatus, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandListAll) {
        java.lang.Boolean didAnyStageStatusUpdated = java.lang.Boolean.FALSE;
        for (org.apache.ambari.server.orm.entities.StageEntityPK reportedStagePK : stagesWithReceivedTaskStatus) {
            if (activeStageMap.containsKey(reportedStagePK)) {
                java.lang.Boolean didStatusChange = updateStageStatus(reportedStagePK, hostRoleCommandListAll);
                if (didStatusChange) {
                    org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage reportedStage = activeStageMap.get(reportedStagePK);
                    stageDAO.updateStatus(reportedStagePK, reportedStage.getStatus(), reportedStage.getDisplayStatus());
                    didAnyStageStatusUpdated = java.lang.Boolean.TRUE;
                }
            } else {
                org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.error(java.lang.String.format("Received update for a task whose stage is not being tracked as running stage: %s", reportedStagePK.toString()));
            }
        }
        return didAnyStageStatusUpdated;
    }

    private void addRequestId(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand) {
        java.lang.Long requestId = hostRoleCommand.getRequestId();
        org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK = new org.apache.ambari.server.orm.entities.StageEntityPK();
        stageEntityPK.setRequestId(hostRoleCommand.getRequestId());
        stageEntityPK.setStageId(hostRoleCommand.getStageId());
        if (activeRequestMap.containsKey(requestId)) {
            activeRequestMap.get(requestId).addStageEntityPK(stageEntityPK);
        } else {
            org.apache.ambari.server.orm.entities.RequestEntity requestEntity = requestDAO.findByPK(requestId);
            assert requestEntity != null;
            java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> stageEntityPKs = com.google.common.collect.Sets.newHashSet(stageEntityPK);
            org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveRequest request = new org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveRequest(requestEntity.getStatus(), requestEntity.getDisplayStatus(), stageEntityPKs, requestEntity.getClusterId());
            activeRequestMap.put(requestId, request);
        }
    }

    private void updateActiveRequestsStatus(final java.util.Set<java.lang.Long> requestIdsWithReceivedTaskStatus, java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> stagesWithChangedTaskStatus) {
        for (java.lang.Long reportedRequestId : requestIdsWithReceivedTaskStatus) {
            if (activeRequestMap.containsKey(reportedRequestId)) {
                org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveRequest request = activeRequestMap.get(reportedRequestId);
                java.lang.Boolean didStatusChange = updateRequestStatus(reportedRequestId, stagesWithChangedTaskStatus);
                if (didStatusChange) {
                    requestDAO.updateStatus(reportedRequestId, request.getStatus(), request.getDisplayStatus());
                }
                if (request.isCompleted() && isAllTasksCompleted(reportedRequestId)) {
                    removeRequestStageAndTasks(reportedRequestId);
                }
            } else {
                org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.error(java.lang.String.format("Received update for a task whose request %d is not being tracked as running request", reportedRequestId));
            }
        }
    }

    private java.lang.Boolean isAllTasksCompleted(java.lang.Long requestId) {
        java.lang.Boolean result = java.lang.Boolean.TRUE;
        for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> entry : activeTasksMap.entrySet()) {
            if ((entry.getValue().getRequestId() == requestId) && (!entry.getValue().getStatus().isCompletedState())) {
                result = java.lang.Boolean.FALSE;
            }
        }
        return result;
    }

    private void removeRequestStageAndTasks(java.lang.Long requestId) {
        removeTasks(requestId);
        removeStages(requestId);
        removeRequest(requestId);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.StageEntityPK> getAllStageEntityPKForRequest(final java.lang.Long requestID) {
        com.google.common.base.Predicate<org.apache.ambari.server.orm.entities.StageEntityPK> predicate = new com.google.common.base.Predicate<org.apache.ambari.server.orm.entities.StageEntityPK>() {
            @java.lang.Override
            public boolean apply(org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK) {
                return stageEntityPK.getRequestId().equals(requestID);
            }
        };
        return com.google.common.collect.FluentIterable.from(activeStageMap.keySet()).filter(predicate).toList();
    }

    private java.lang.Boolean updateStageStatus(final org.apache.ambari.server.orm.entities.StageEntityPK stagePK, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandListAll) {
        java.lang.Boolean didAnyStatusChanged = java.lang.Boolean.FALSE;
        org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage reportedStage = activeStageMap.get(stagePK);
        org.apache.ambari.server.actionmanager.HostRoleStatus stageCurrentStatus = reportedStage.getStatus();
        org.apache.ambari.server.actionmanager.HostRoleStatus stageCurrentDisplayStatus = reportedStage.getDisplayStatus();
        if ((!stageCurrentDisplayStatus.isCompletedState()) || (!stageCurrentStatus.isCompletedState())) {
            java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> receivedTaskStatusCount = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCountsForTasks(hostRoleCommandListAll, stagePK);
            org.apache.ambari.server.actionmanager.HostRoleStatus statusFromPartialSet = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusFromPartialSet(receivedTaskStatusCount, reportedStage.getSkippable());
            org.apache.ambari.server.actionmanager.HostRoleStatus displayStatusFromPartialSet = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusFromPartialSet(receivedTaskStatusCount, java.lang.Boolean.FALSE);
            if ((statusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) || (displayStatusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                com.google.common.base.Function<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> transform = new com.google.common.base.Function<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand>() {
                    @java.lang.Override
                    public org.apache.ambari.server.actionmanager.HostRoleCommand apply(java.lang.Long taskId) {
                        return activeTasksMap.get(taskId);
                    }
                };
                java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> activeHostRoleCommandsOfStage = com.google.common.collect.FluentIterable.from(reportedStage.getTaskIds()).transform(transform).toList();
                java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> statusCount = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCountsForTasks(activeHostRoleCommandsOfStage);
                if (displayStatusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                    org.apache.ambari.server.actionmanager.HostRoleStatus display_status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryDisplayStatus(statusCount, activeHostRoleCommandsOfStage.size(), reportedStage.getSkippable());
                    if (display_status != stageCurrentDisplayStatus) {
                        reportedStage.setDisplayStatus(display_status);
                        didAnyStatusChanged = java.lang.Boolean.TRUE;
                    }
                } else {
                    reportedStage.setDisplayStatus(displayStatusFromPartialSet);
                    didAnyStatusChanged = java.lang.Boolean.TRUE;
                }
                if (statusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                    org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStageStatus(activeHostRoleCommandsOfStage, statusCount, reportedStage.getSuccessFactors(), reportedStage.getSkippable());
                    if (status != stageCurrentStatus) {
                        reportedStage.setStatus(status);
                        didAnyStatusChanged = java.lang.Boolean.TRUE;
                    }
                } else {
                    reportedStage.setDisplayStatus(displayStatusFromPartialSet);
                    didAnyStatusChanged = java.lang.Boolean.TRUE;
                }
            } else {
                reportedStage.setStatus(statusFromPartialSet);
                reportedStage.setDisplayStatus(displayStatusFromPartialSet);
                didAnyStatusChanged = java.lang.Boolean.TRUE;
            }
        }
        return didAnyStatusChanged;
    }

    private java.lang.Boolean updateRequestStatus(final java.lang.Long requestId, java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> stagesWithChangedTaskStatus) {
        java.lang.Boolean didStatusChanged = java.lang.Boolean.FALSE;
        org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveRequest request = activeRequestMap.get(requestId);
        org.apache.ambari.server.actionmanager.HostRoleStatus requestCurrentStatus = request.getStatus();
        org.apache.ambari.server.actionmanager.HostRoleStatus requestCurrentDisplayStatus = request.getDisplayStatus();
        if ((!requestCurrentDisplayStatus.isCompletedState()) || (!requestCurrentStatus.isCompletedState())) {
            java.util.List<org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage> activeStagesWithChangesTaskStatus = new java.util.ArrayList<>();
            for (org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK : stagesWithChangedTaskStatus) {
                if (requestId.equals(stageEntityPK.getRequestId())) {
                    org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage activeStage = activeStageMap.get(stageEntityPK);
                    activeStagesWithChangesTaskStatus.add(activeStage);
                }
            }
            java.util.Map<org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType, java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer>> stageStatusCountFromPartialSet = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCountsForStage(activeStagesWithChangesTaskStatus);
            org.apache.ambari.server.actionmanager.HostRoleStatus statusFromPartialSet = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusFromPartialSet(stageStatusCountFromPartialSet.get(org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType.STATUS), java.lang.Boolean.FALSE);
            org.apache.ambari.server.actionmanager.HostRoleStatus displayStatusFromPartialSet = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusFromPartialSet(stageStatusCountFromPartialSet.get(org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType.DISPLAY_STATUS), java.lang.Boolean.FALSE);
            if ((statusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) || (displayStatusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                java.util.List<org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage> allActiveStages = new java.util.ArrayList<>();
                for (org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK : request.getStageEntityPks()) {
                    org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage activeStage = activeStageMap.get(stageEntityPK);
                    allActiveStages.add(activeStage);
                }
                java.util.Map<org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType, java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer>> stageStatusCount = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCountsForStage(allActiveStages);
                if (displayStatusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                    org.apache.ambari.server.actionmanager.HostRoleStatus display_status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryDisplayStatus(stageStatusCount.get(org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType.DISPLAY_STATUS), allActiveStages.size(), false);
                    if (display_status != requestCurrentDisplayStatus) {
                        request.setDisplayStatus(display_status);
                        didStatusChanged = java.lang.Boolean.TRUE;
                    }
                } else {
                    request.setDisplayStatus(displayStatusFromPartialSet);
                    didStatusChanged = java.lang.Boolean.TRUE;
                }
                if (statusFromPartialSet == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                    org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(stageStatusCount.get(org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType.STATUS), allActiveStages.size(), false);
                    if (status != requestCurrentStatus) {
                        request.setStatus(status);
                        didStatusChanged = java.lang.Boolean.TRUE;
                    }
                } else {
                    request.setDisplayStatus(displayStatusFromPartialSet);
                    didStatusChanged = java.lang.Boolean.TRUE;
                }
            } else {
                request.setStatus(statusFromPartialSet);
                request.setDisplayStatus(displayStatusFromPartialSet);
                didStatusChanged = java.lang.Boolean.TRUE;
            }
        }
        return didStatusChanged;
    }

    private void removeTasks(java.lang.Long requestId) {
        java.util.Iterator<java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand>> iter = activeTasksMap.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> entry = iter.next();
            org.apache.ambari.server.actionmanager.HostRoleCommand hrc = entry.getValue();
            if (hrc.getRequestId() == requestId) {
                if (!hrc.getStatus().isCompletedState()) {
                    org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.LOG.error(java.lang.String.format("Task %d should have been completed before being removed from running task cache(activeTasksMap)", hrc.getTaskId()));
                }
                iter.remove();
            }
        } 
    }

    private void removeStages(java.lang.Long requestId) {
        java.util.List<org.apache.ambari.server.orm.entities.StageEntityPK> stageEntityPKs = getAllStageEntityPKForRequest(requestId);
        for (org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK : stageEntityPKs) {
            activeStageMap.remove(stageEntityPK);
        }
    }

    private void removeRequest(java.lang.Long requestId) {
        activeRequestMap.remove(requestId);
    }

    protected class ActiveRequest {
        private org.apache.ambari.server.actionmanager.HostRoleStatus status;

        private org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus;

        private java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> stageEntityPks;

        private java.lang.Long clusterId;

        public ActiveRequest(org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus, java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> stageEntityPks, java.lang.Long clusterId) {
            this.status = status;
            this.displayStatus = displayStatus;
            this.stageEntityPks = stageEntityPks;
            this.clusterId = clusterId;
        }

        public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
            return status;
        }

        public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
            this.status = status;
        }

        public org.apache.ambari.server.actionmanager.HostRoleStatus getDisplayStatus() {
            return displayStatus;
        }

        public void setDisplayStatus(org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus) {
            this.displayStatus = displayStatus;
        }

        public java.lang.Boolean isCompleted() {
            return status.isCompletedState() && displayStatus.isCompletedState();
        }

        public java.util.Set<org.apache.ambari.server.orm.entities.StageEntityPK> getStageEntityPks() {
            return stageEntityPks;
        }

        public void addStageEntityPK(org.apache.ambari.server.orm.entities.StageEntityPK stageEntityPK) {
            stageEntityPks.add(stageEntityPK);
        }

        public java.lang.Long getClusterId() {
            return clusterId;
        }

        public void setClusterId(java.lang.Long clusterId) {
            this.clusterId = clusterId;
        }
    }

    public class ActiveStage {
        private org.apache.ambari.server.actionmanager.HostRoleStatus status;

        private org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus;

        private java.lang.Boolean skippable;

        private java.util.Set<java.lang.Long> taskIds;

        private java.util.Map<org.apache.ambari.server.Role, java.lang.Float> successFactors = new java.util.HashMap<>();

        public ActiveStage(org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus, java.util.Map<org.apache.ambari.server.Role, java.lang.Float> successFactors, java.lang.Boolean skippable, java.util.Set<java.lang.Long> taskIds) {
            this.status = status;
            this.displayStatus = displayStatus;
            this.successFactors = successFactors;
            this.skippable = skippable;
            this.taskIds = taskIds;
        }

        public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
            return status;
        }

        public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
            this.status = status;
        }

        public org.apache.ambari.server.actionmanager.HostRoleStatus getDisplayStatus() {
            return displayStatus;
        }

        public void setDisplayStatus(org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus) {
            this.displayStatus = displayStatus;
        }

        public java.lang.Boolean getSkippable() {
            return skippable;
        }

        public void setSkippable(java.lang.Boolean skippable) {
            this.skippable = skippable;
        }

        public java.util.Map<org.apache.ambari.server.Role, java.lang.Float> getSuccessFactors() {
            return successFactors;
        }

        public void setSuccessFactors(java.util.Map<org.apache.ambari.server.Role, java.lang.Float> successFactors) {
            this.successFactors = successFactors;
        }

        public java.util.Set<java.lang.Long> getTaskIds() {
            return taskIds;
        }

        public void addTaskId(java.lang.Long taskId) {
            taskIds.add(taskId);
        }
    }
}