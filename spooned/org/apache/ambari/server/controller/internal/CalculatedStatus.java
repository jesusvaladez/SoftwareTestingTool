package org.apache.ambari.server.controller.internal;
public class CalculatedStatus {
    private final org.apache.ambari.server.actionmanager.HostRoleStatus status;

    private final org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus;

    private final double percent;

    public static final org.apache.ambari.server.controller.internal.CalculatedStatus COMPLETED = new org.apache.ambari.server.controller.internal.CalculatedStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, 100.0);

    public static final org.apache.ambari.server.controller.internal.CalculatedStatus PENDING = new org.apache.ambari.server.controller.internal.CalculatedStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, 0.0);

    public static final org.apache.ambari.server.controller.internal.CalculatedStatus ABORTED = new org.apache.ambari.server.controller.internal.CalculatedStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, -1);

    private CalculatedStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status, double percent) {
        this(status, null, percent);
    }

    private CalculatedStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus, double percent) {
        this.status = status;
        this.displayStatus = displayStatus;
        this.percent = percent;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getDisplayStatus() {
        return displayStatus;
    }

    public double getPercent() {
        return percent;
    }

    public static org.apache.ambari.server.controller.internal.CalculatedStatus statusFromTaskEntities(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks, boolean skippable) {
        int size = tasks.size();
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> taskStatusCounts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateTaskEntityStatusCounts(tasks);
        org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(taskStatusCounts, size, skippable);
        double progressPercent = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateProgressPercent(taskStatusCounts, size);
        return new org.apache.ambari.server.controller.internal.CalculatedStatus(status, progressPercent);
    }

    public static org.apache.ambari.server.controller.internal.CalculatedStatus statusFromStageEntities(java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stages) {
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> stageStatuses = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.StageEntity stage : stages) {
            java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> stageTasks = stage.getHostRoleCommands();
            org.apache.ambari.server.actionmanager.HostRoleStatus stageStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateTaskEntityStatusCounts(stageTasks), stageTasks.size(), stage.isSkippable());
            stageStatuses.add(stageStatus);
            tasks.addAll(stageTasks);
        }
        org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusOfUpgrade(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(stageStatuses), stageStatuses.size());
        double progressPercent = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateProgressPercent(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateTaskEntityStatusCounts(tasks), tasks.size());
        return new org.apache.ambari.server.controller.internal.CalculatedStatus(status, progressPercent);
    }

    public static org.apache.ambari.server.controller.internal.CalculatedStatus statusFromStages(java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages) {
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> stageStatuses = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> tasks = new java.util.HashSet<>();
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> stageTasks = stage.getOrderedHostRoleCommands();
            org.apache.ambari.server.actionmanager.HostRoleStatus stageStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateTaskStatusCounts(stageTasks), stageTasks.size(), stage.isSkippable());
            stageStatuses.add(stageStatus);
            tasks.addAll(stageTasks);
        }
        org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusOfUpgrade(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(stageStatuses), stageStatuses.size());
        double progressPercent = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateProgressPercent(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateTaskStatusCounts(tasks), tasks.size());
        return new org.apache.ambari.server.controller.internal.CalculatedStatus(status, progressPercent);
    }

    public static java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> calculateStatusCounts(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> hostRoleStatuses) {
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters = new java.util.HashMap<>();
        for (org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus : org.apache.ambari.server.actionmanager.HostRoleStatus.values()) {
            counters.put(hostRoleStatus, 0);
        }
        for (org.apache.ambari.server.actionmanager.HostRoleStatus status : hostRoleStatuses) {
            if (status.isCompletedState() && (status != org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) {
                counters.put(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED) + 1);
            }
            counters.put(status, counters.get(status) + 1);
        }
        counters.put(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, ((hostRoleStatuses.size() - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        return counters;
    }

    public static java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> calculateStatusCountsForTasks(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters = new java.util.HashMap<>();
        for (org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus : org.apache.ambari.server.actionmanager.HostRoleStatus.values()) {
            counters.put(hostRoleStatus, 0);
        }
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : hostRoleCommands) {
            if (hrc.getStatus().isCompletedState() && (hrc.getStatus() != org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) {
                counters.put(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED) + 1);
            }
            counters.put(hrc.getStatus(), counters.get(hrc.getStatus()) + 1);
        }
        counters.put(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, ((hostRoleCommands.size() - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        return counters;
    }

    public static java.util.Map<org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType, java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer>> calculateStatusCountsForStage(java.util.Collection<org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage> stages) {
        java.util.Map<org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType, java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer>> counters = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType statusType : org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType.values()) {
            java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> statusMap = new java.util.HashMap<>();
            counters.put(statusType, statusMap);
            for (org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus : org.apache.ambari.server.actionmanager.HostRoleStatus.values()) {
                statusMap.put(hostRoleStatus, 0);
            }
            for (org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.ActiveStage stage : stages) {
                org.apache.ambari.server.actionmanager.HostRoleStatus status;
                if (statusType == org.apache.ambari.server.controller.internal.CalculatedStatus.StatusType.DISPLAY_STATUS) {
                    status = stage.getDisplayStatus();
                } else {
                    status = stage.getStatus();
                }
                if (status.isCompletedState() && (status != org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) {
                    statusMap.put(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, statusMap.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED) + 1);
                }
                statusMap.put(status, statusMap.get(status) + 1);
            }
            statusMap.put(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, ((stages.size() - statusMap.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) - statusMap.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) - statusMap.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        }
        return counters;
    }

    public static java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> calculateStatusCountsForTasks(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands, org.apache.ambari.server.orm.entities.StageEntityPK stage) {
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandsOfStage = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus : org.apache.ambari.server.actionmanager.HostRoleStatus.values()) {
            counters.put(hostRoleStatus, 0);
        }
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : hostRoleCommands) {
            if ((stage.getStageId() == hrc.getStageId()) && (stage.getRequestId() == hrc.getRequestId())) {
                if (hrc.getStatus().isCompletedState() && (hrc.getStatus() != org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) {
                    counters.put(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED) + 1);
                }
                counters.put(hrc.getStatus(), counters.get(hrc.getStatus()) + 1);
                hostRoleCommandsOfStage.add(hrc);
            }
        }
        counters.put(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, ((hostRoleCommandsOfStage.size() - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) - counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        return counters;
    }

    public static java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> calculateTaskEntityStatusCounts(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks) {
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> hostRoleStatuses = new java.util.LinkedList<>();
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommand : tasks) {
            hostRoleStatuses.add(hostRoleCommand.getStatus());
        }
        return org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(hostRoleStatuses);
    }

    public static java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> calculateTaskStatusCounts(java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> stageDto, java.util.Set<java.lang.Long> stageIds) {
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> status = new java.util.ArrayList<>();
        for (java.lang.Long stageId : stageIds) {
            if (!stageDto.containsKey(stageId)) {
                continue;
            }
            org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO dto = stageDto.get(stageId);
            status.addAll(dto.getTaskStatuses());
        }
        return org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(status);
    }

    public static org.apache.ambari.server.controller.internal.CalculatedStatus statusFromRequest(org.apache.ambari.server.orm.dao.HostRoleCommandDAO s_hostRoleCommandDAO, org.apache.ambari.server.topology.TopologyManager topologyManager, java.lang.Long requestId) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = s_hostRoleCommandDAO.findAggregateCounts(requestId);
        summary.putAll(topologyManager.getStageSummaries(requestId));
        final org.apache.ambari.server.controller.internal.CalculatedStatus status;
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = topologyManager.getRequest(requestId);
        if (summary.isEmpty() && (null != logicalRequest)) {
            if (logicalRequest.getHostRequests().isEmpty()) {
                status = org.apache.ambari.server.controller.internal.CalculatedStatus.COMPLETED;
            } else {
                status = org.apache.ambari.server.controller.internal.CalculatedStatus.PENDING;
            }
        } else {
            status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(summary, summary.keySet());
        }
        return status;
    }

    public static org.apache.ambari.server.controller.internal.CalculatedStatus statusFromStageSummary(java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> stageDto, java.util.Set<java.lang.Long> stageIds) {
        if (stageDto.isEmpty() || stageIds.isEmpty()) {
            return org.apache.ambari.server.controller.internal.CalculatedStatus.COMPLETED;
        }
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> stageStatuses = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> stageDisplayStatuses = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> taskStatuses = new java.util.ArrayList<>();
        for (java.lang.Long stageId : stageIds) {
            if (!stageDto.containsKey(stageId)) {
                continue;
            }
            org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO summary = stageDto.get(stageId);
            int total = summary.getTaskTotal();
            boolean skip = summary.isStageSkippable();
            java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(summary.getTaskStatuses());
            org.apache.ambari.server.actionmanager.HostRoleStatus stageStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(counts, total, skip);
            org.apache.ambari.server.actionmanager.HostRoleStatus stageDisplayStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryDisplayStatus(counts, total, skip);
            stageStatuses.add(stageStatus);
            stageDisplayStatuses.add(stageDisplayStatus);
            taskStatuses.addAll(summary.getTaskStatuses());
        }
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(stageStatuses);
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> displayCounts = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(stageDisplayStatuses);
        org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatusOfUpgrade(counts, stageStatuses.size());
        org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryDisplayStatus(displayCounts, stageDisplayStatuses.size(), false);
        double progressPercent = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateProgressPercent(org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(taskStatuses), taskStatuses.size());
        return new org.apache.ambari.server.controller.internal.CalculatedStatus(status, displayStatus, progressPercent);
    }

    private static java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> calculateTaskStatusCounts(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> tasks) {
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> hostRoleStatuses = new java.util.LinkedList<>();
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : tasks) {
            hostRoleStatuses.add(hostRoleCommand.getStatus());
        }
        return org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(hostRoleStatuses);
    }

    private static double calculateProgressPercent(java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters, double total) {
        return total == 0 ? 0 : (((((((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) * 0.09) + (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS) * 0.35)) + (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING) * 0.35)) + (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED) * 0.35)) + (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT) * 0.35)) + counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) / total) * 100.0;
    }

    public static org.apache.ambari.server.actionmanager.HostRoleStatus calculateSummaryStatus(java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters, int total, boolean skippable) {
        if (total == 0) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
        }
        if (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) == total) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;
        }
        if (((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING) > 0) || (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED) > 0)) || (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT) > 0)) {
            return counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING : counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED : org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT;
        }
        if ((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED) > 0) && (!skippable)) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED;
        }
        if ((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT) > 0) && (!skippable)) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT;
        }
        int numActiveTasks = (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) + counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) + counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        if ((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) > 0) && (numActiveTasks == 0)) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED;
        }
        if (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED) == total) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
        }
        return org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS;
    }

    public static org.apache.ambari.server.actionmanager.HostRoleStatus calculateSummaryStatusFromPartialSet(java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters, boolean skippable) {
        org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;
        if (((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING) > 0) || (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED) > 0)) || (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT) > 0)) {
            status = (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING) > 0) ? org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING : counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED : org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT;
        }
        if ((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED) > 0) && (!skippable)) {
            status = org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED;
        }
        if ((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT) > 0) && (!skippable)) {
            status = org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT;
        }
        int inProgressTasks = counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) + counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        if (inProgressTasks > 0) {
            status = org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS;
        }
        return status;
    }

    public static org.apache.ambari.server.actionmanager.HostRoleStatus calculateStageStatus(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands, java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters, java.util.Map<org.apache.ambari.server.Role, java.lang.Float> successFactors, boolean skippable) {
        int total = hostRoleCommands.size();
        if (total == 0) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
        }
        if (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) == total) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;
        }
        if (((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING) > 0) || (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED) > 0)) || (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT) > 0)) {
            return counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING : counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED : org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT;
        }
        if ((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED) > 0) && (!skippable)) {
            java.util.Set<org.apache.ambari.server.Role> rolesWithFailedTasks = org.apache.ambari.server.controller.internal.CalculatedStatus.getRolesOfFailedTasks(hostRoleCommands);
            java.lang.Boolean didStageFailed = org.apache.ambari.server.controller.internal.CalculatedStatus.didStageFailed(hostRoleCommands, rolesWithFailedTasks, successFactors);
            if (didStageFailed)
                return org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED;

        }
        if ((counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT) > 0) && (!skippable)) {
            java.util.Set<org.apache.ambari.server.Role> rolesWithTimedOutTasks = org.apache.ambari.server.controller.internal.CalculatedStatus.getRolesOfTimedOutTasks(hostRoleCommands);
            java.lang.Boolean didStageFailed = org.apache.ambari.server.controller.internal.CalculatedStatus.didStageFailed(hostRoleCommands, rolesWithTimedOutTasks, successFactors);
            if (didStageFailed)
                return org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT;

        }
        int numActiveTasks = (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) + counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) + counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        if (numActiveTasks > 0) {
            return org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS;
        } else if (counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) > 0) {
            java.util.Set<org.apache.ambari.server.Role> rolesWithTimedOutTasks = org.apache.ambari.server.controller.internal.CalculatedStatus.getRolesOfAbortedTasks(hostRoleCommands);
            java.lang.Boolean didStageFailed = org.apache.ambari.server.controller.internal.CalculatedStatus.didStageFailed(hostRoleCommands, rolesWithTimedOutTasks, successFactors);
            if (didStageFailed)
                return org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED;

        }
        return org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
    }

    protected static java.util.Set<org.apache.ambari.server.Role> getRolesOfFailedTasks(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        return org.apache.ambari.server.controller.internal.CalculatedStatus.getRolesOfTasks(hostRoleCommands, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
    }

    protected static java.util.Set<org.apache.ambari.server.Role> getRolesOfTimedOutTasks(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        return org.apache.ambari.server.controller.internal.CalculatedStatus.getRolesOfTasks(hostRoleCommands, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT);
    }

    protected static java.util.Set<org.apache.ambari.server.Role> getRolesOfAbortedTasks(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        return org.apache.ambari.server.controller.internal.CalculatedStatus.getRolesOfTasks(hostRoleCommands, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
    }

    protected static java.util.Set<org.apache.ambari.server.Role> getRolesOfTasks(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands, final org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        com.google.common.base.Predicate<org.apache.ambari.server.actionmanager.HostRoleCommand> predicate = new com.google.common.base.Predicate<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public boolean apply(org.apache.ambari.server.actionmanager.HostRoleCommand hrc) {
                return hrc.getStatus() == status;
            }
        };
        com.google.common.base.Function<org.apache.ambari.server.actionmanager.HostRoleCommand, org.apache.ambari.server.Role> transform = new com.google.common.base.Function<org.apache.ambari.server.actionmanager.HostRoleCommand, org.apache.ambari.server.Role>() {
            @java.lang.Override
            public org.apache.ambari.server.Role apply(org.apache.ambari.server.actionmanager.HostRoleCommand hrc) {
                return hrc.getRole();
            }
        };
        return com.google.common.collect.FluentIterable.from(hostRoleCommands).filter(predicate).transform(transform).toSet();
    }

    protected static java.lang.Boolean didStageFailed(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands, java.util.Set<org.apache.ambari.server.Role> roles, java.util.Map<org.apache.ambari.server.Role, java.lang.Float> successFactors) {
        java.lang.Boolean isFailed = java.lang.Boolean.FALSE;
        for (org.apache.ambari.server.Role role : roles) {
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandsOfRole = org.apache.ambari.server.controller.internal.CalculatedStatus.getHostRoleCommandsOfRole(hostRoleCommands, role);
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> failedHostRoleCommands = org.apache.ambari.server.controller.internal.CalculatedStatus.getFailedHostRoleCommands(hostRoleCommandsOfRole);
            float successRatioForRole = (hostRoleCommandsOfRole.size() - failedHostRoleCommands.size()) / hostRoleCommandsOfRole.size();
            java.lang.Float successFactorForRole = (successFactors.get(role) == null) ? 1.0F : successFactors.get(role);
            if (successRatioForRole < successFactorForRole) {
                isFailed = java.lang.Boolean.TRUE;
                break;
            }
        }
        return isFailed;
    }

    protected static java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getHostRoleCommandsOfRole(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands, final org.apache.ambari.server.Role role) {
        com.google.common.base.Predicate<org.apache.ambari.server.actionmanager.HostRoleCommand> predicate = new com.google.common.base.Predicate<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public boolean apply(org.apache.ambari.server.actionmanager.HostRoleCommand hrc) {
                return hrc.getRole() == role;
            }
        };
        return com.google.common.collect.FluentIterable.from(hostRoleCommands).filter(predicate).toList();
    }

    protected static java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getFailedHostRoleCommands(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        com.google.common.base.Predicate<org.apache.ambari.server.actionmanager.HostRoleCommand> predicate = new com.google.common.base.Predicate<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public boolean apply(org.apache.ambari.server.actionmanager.HostRoleCommand hrc) {
                return hrc.getStatus().isFailedAndNotSkippableState();
            }
        };
        return com.google.common.collect.FluentIterable.from(hostRoleCommands).filter(predicate).toList();
    }

    public static org.apache.ambari.server.actionmanager.HostRoleStatus getOverallStatusForRequest(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> hostRoleStatuses) {
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> statusCount = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(hostRoleStatuses);
        return org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(statusCount, hostRoleStatuses.size(), false);
    }

    public static org.apache.ambari.server.actionmanager.HostRoleStatus getOverallDisplayStatusForRequest(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleStatus> hostRoleStatuses) {
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> statusCount = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCounts(hostRoleStatuses);
        return org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryDisplayStatus(statusCount, hostRoleStatuses.size(), false);
    }

    protected static org.apache.ambari.server.actionmanager.HostRoleStatus calculateSummaryStatusOfUpgrade(java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters, int total) {
        return org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(counters, total, false);
    }

    public static org.apache.ambari.server.actionmanager.HostRoleStatus calculateSummaryDisplayStatus(java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> counters, int total, boolean skippable) {
        return counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED : counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT : counters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED) > 0 ? org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED : org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryStatus(counters, total, skippable);
    }

    public enum StatusType {

        STATUS("status"),
        DISPLAY_STATUS("display_status");
        private java.lang.String value;

        StatusType(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String getValue() {
            return value;
        }
    }
}