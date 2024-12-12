package org.apache.ambari.server.actionmanager;
@com.google.inject.Singleton
public class ActionManager {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.ActionManager.class);

    private final org.apache.ambari.server.actionmanager.ActionScheduler scheduler;

    private final org.apache.ambari.server.actionmanager.ActionDBAccessor db;

    private final java.util.concurrent.atomic.AtomicLong requestCounter;

    private final org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    private static org.apache.ambari.server.topology.TopologyManager topologyManager;

    @com.google.inject.Inject
    public ActionManager(org.apache.ambari.server.actionmanager.ActionDBAccessor db, org.apache.ambari.server.actionmanager.RequestFactory requestFactory, org.apache.ambari.server.actionmanager.ActionScheduler scheduler) {
        this.db = db;
        this.requestFactory = requestFactory;
        this.scheduler = scheduler;
        requestCounter = new java.util.concurrent.atomic.AtomicLong(db.getLastPersistedRequestIdWhenInitialized());
    }

    public void start() {
        org.apache.ambari.server.actionmanager.ActionManager.LOG.info("Starting scheduler thread");
        scheduler.start();
    }

    public void shutdown() {
        scheduler.stop();
    }

    public void sendActions(java.util.List<org.apache.ambari.server.actionmanager.Stage> stages, java.lang.String clusterHostInfo, org.apache.ambari.server.controller.ExecuteActionRequest actionRequest) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Request request = requestFactory.createNewFromStages(stages, clusterHostInfo, actionRequest);
        request.setUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
        sendActions(request, actionRequest);
    }

    public void sendActions(org.apache.ambari.server.actionmanager.Request request, org.apache.ambari.server.controller.ExecuteActionRequest executeActionRequest) throws org.apache.ambari.server.AmbariException {
        if (org.apache.ambari.server.actionmanager.ActionManager.LOG.isDebugEnabled()) {
            org.apache.ambari.server.actionmanager.ActionManager.LOG.debug("Persisting Request into DB: {}", request);
            if (executeActionRequest != null) {
                org.apache.ambari.server.actionmanager.ActionManager.LOG.debug("In response to request: {}", request);
            }
        }
        db.persistActions(request);
        scheduler.awake();
    }

    public java.util.List<org.apache.ambari.server.actionmanager.Request> getRequests(java.util.Collection<java.lang.Long> requestIds) {
        java.util.List<org.apache.ambari.server.actionmanager.Request> requests = db.getRequests(requestIds);
        requests.addAll(org.apache.ambari.server.actionmanager.ActionManager.topologyManager.getRequests(requestIds));
        return requests;
    }

    public java.util.List<org.apache.ambari.server.actionmanager.Stage> getRequestStatus(long requestId) {
        return db.getAllStages(requestId);
    }

    public org.apache.ambari.server.actionmanager.Stage getAction(long requestId, long stageId) {
        return db.getStage(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
    }

    public java.util.List<org.apache.ambari.server.actionmanager.Stage> getActions(long requestId) {
        return db.getAllStages(requestId);
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommand getTaskById(long taskId) {
        return db.getTask(taskId);
    }

    public void processTaskResponse(java.lang.String hostname, java.util.List<org.apache.ambari.server.agent.CommandReport> reports, java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> commands) {
        if (reports == null) {
            return;
        }
        java.util.Collections.sort(reports, new java.util.Comparator<org.apache.ambari.server.agent.CommandReport>() {
            @java.lang.Override
            public int compare(org.apache.ambari.server.agent.CommandReport o1, org.apache.ambari.server.agent.CommandReport o2) {
                return ((int) (o1.getTaskId() - o2.getTaskId()));
            }
        });
        java.util.List<org.apache.ambari.server.agent.CommandReport> reportsToProcess = new java.util.ArrayList<>();
        for (org.apache.ambari.server.agent.CommandReport report : reports) {
            org.apache.ambari.server.actionmanager.HostRoleCommand command = commands.get(report.getTaskId());
            if (org.apache.ambari.server.actionmanager.ActionManager.LOG.isDebugEnabled()) {
                org.apache.ambari.server.actionmanager.ActionManager.LOG.debug("Processing command report : {}", report);
            }
            if (command == null) {
                org.apache.ambari.server.actionmanager.ActionManager.LOG.warn(("The task " + report.getTaskId()) + " is invalid");
                continue;
            }
            if (((!command.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) && (!command.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED))) && (!command.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED))) {
                org.apache.ambari.server.actionmanager.ActionManager.LOG.warn(("The task " + command.getTaskId()) + " is not in progress, ignoring update");
                continue;
            }
            reportsToProcess.add(report);
        }
        db.updateHostRoleStates(reportsToProcess);
    }

    public boolean isInProgressCommand(org.apache.ambari.server.agent.CommandReport report) {
        org.apache.ambari.server.actionmanager.HostRoleCommand command = db.getTask(report.getTaskId());
        if (command == null) {
            org.apache.ambari.server.actionmanager.ActionManager.LOG.warn(("The task " + report.getTaskId()) + " is invalid");
            return false;
        }
        return command.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS) || command.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
    }

    public void handleLostHost(java.lang.String host) {
    }

    public long getNextRequestId() {
        return requestCounter.incrementAndGet();
    }

    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getRequestTasks(long requestId) {
        return db.getRequestTasks(requestId);
    }

    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getAllTasksByRequestIds(java.util.Collection<java.lang.Long> requestIds) {
        return db.getAllTasksByRequestIds(requestIds);
    }

    public java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasks(java.util.Collection<java.lang.Long> taskIds) {
        return db.getTasks(taskIds);
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> getTasksMap(java.util.Collection<java.lang.Long> taskIds) {
        return org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(getTasks(taskIds));
    }

    public java.util.List<java.lang.Long> getRequestsByStatus(org.apache.ambari.server.actionmanager.RequestStatus status, int maxResults, boolean ascOrder) {
        java.util.List<java.lang.Long> requests = db.getRequestsByStatus(status, maxResults, ascOrder);
        for (org.apache.ambari.server.actionmanager.Request logicalRequest : org.apache.ambari.server.actionmanager.ActionManager.topologyManager.getRequests(java.util.Collections.emptySet())) {
            org.apache.ambari.server.actionmanager.HostRoleStatus logicalRequestStatus = logicalRequest.getStatus();
            if ((status == null) || ((logicalRequestStatus != null) && logicalRequest.getStatus().name().equals(status.name()))) {
                requests.add(logicalRequest.getRequestId());
            }
        }
        return requests;
    }

    public java.util.Map<java.lang.Long, java.lang.String> getRequestContext(java.util.List<java.lang.Long> requestIds) {
        return db.getRequestContext(requestIds);
    }

    public java.lang.String getRequestContext(long requestId) {
        return db.getRequestContext(requestId);
    }

    public void cancelRequest(long requestId, java.lang.String reason) {
        scheduler.scheduleCancellingRequest(requestId, reason);
        scheduler.awake();
    }

    public static void setTopologyManager(org.apache.ambari.server.topology.TopologyManager topologyManager) {
        org.apache.ambari.server.actionmanager.ActionManager.topologyManager = topologyManager;
    }

    public void resubmitTasks(java.util.List<java.lang.Long> taskIds) {
        db.resubmitTasks(taskIds);
    }
}