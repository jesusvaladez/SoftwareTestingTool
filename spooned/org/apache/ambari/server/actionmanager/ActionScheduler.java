package org.apache.ambari.server.actionmanager;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
class ActionScheduler implements java.lang.Runnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.ActionScheduler.class);

    public static final java.lang.String FAILED_TASK_ABORT_REASONING = "Server considered task failed and automatically aborted it";

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.RoleCommandOrderProvider roleCommandOrderProvider;

    @com.google.inject.Inject
    private com.google.inject.persist.UnitOfWork unitOfWork;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.HostsMap hostsMap;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher;

    volatile javax.persistence.EntityManager threadEntityManager;

    private final long actionTimeout;

    private final long sleepTime;

    private volatile boolean shouldRun = true;

    private java.lang.Thread schedulerThread = null;

    private final org.apache.ambari.server.actionmanager.ActionDBAccessor db;

    private short maxAttempts = 2;

    private final org.apache.ambari.server.events.publishers.JPAEventPublisher jpaPublisher;

    private boolean taskTimeoutAdjustment = true;

    private final java.lang.Object wakeupSyncObject = new java.lang.Object();

    private final org.apache.ambari.server.serveraction.ServerActionExecutor serverActionExecutor;

    private final java.util.Set<java.lang.Long> requestsInProgress = new java.util.HashSet<>();

    private final java.util.Set<java.lang.Long> requestsToBeCancelled = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<java.lang.Long, java.lang.Boolean>());

    private final java.util.Map<java.lang.Long, java.lang.String> requestCancelReasons = new java.util.HashMap<>();

    private boolean activeAwakeRequest = false;

    private java.util.concurrent.atomic.AtomicBoolean taskStatusLoaded = new java.util.concurrent.atomic.AtomicBoolean();

    private com.google.common.cache.Cache<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> clusterHostInfoCache;

    private com.google.common.cache.Cache<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> commandParamsStageCache;

    private com.google.common.cache.Cache<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostParamsStageCache;

    @com.google.inject.Inject
    public ActionScheduler(@com.google.inject.name.Named("schedulerSleeptime")
    long sleepTime, @com.google.inject.name.Named("actionTimeout")
    long actionTimeout, org.apache.ambari.server.actionmanager.ActionDBAccessor db, org.apache.ambari.server.events.publishers.JPAEventPublisher jpaPublisher) {
        this.sleepTime = sleepTime;
        this.actionTimeout = actionTimeout;
        this.db = db;
        this.jpaPublisher = jpaPublisher;
        this.jpaPublisher.register(this);
        serverActionExecutor = new org.apache.ambari.server.serveraction.ServerActionExecutor(db, sleepTime);
        initializeCaches();
    }

    protected ActionScheduler(long sleepTimeMilliSec, long actionTimeoutMilliSec, org.apache.ambari.server.actionmanager.ActionDBAccessor db, org.apache.ambari.server.state.Clusters fsmObject, int maxAttempts, org.apache.ambari.server.controller.HostsMap hostsMap, com.google.inject.persist.UnitOfWork unitOfWork, org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, org.apache.ambari.server.configuration.Configuration configuration, com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider, org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO, org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory, org.apache.ambari.server.metadata.RoleCommandOrderProvider roleCommandOrderProvider, org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher) {
        sleepTime = sleepTimeMilliSec;
        actionTimeout = actionTimeoutMilliSec;
        this.db = db;
        clusters = fsmObject;
        this.maxAttempts = ((short) (maxAttempts));
        this.hostsMap = hostsMap;
        this.unitOfWork = unitOfWork;
        this.ambariEventPublisher = ambariEventPublisher;
        this.configuration = configuration;
        this.entityManagerProvider = entityManagerProvider;
        this.hostRoleCommandDAO = hostRoleCommandDAO;
        this.hostRoleCommandFactory = hostRoleCommandFactory;
        jpaPublisher = null;
        this.roleCommandOrderProvider = roleCommandOrderProvider;
        this.agentCommandsPublisher = agentCommandsPublisher;
        serverActionExecutor = new org.apache.ambari.server.serveraction.ServerActionExecutor(db, sleepTime);
        initializeCaches();
    }

    protected ActionScheduler(long sleepTimeMilliSec, long actionTimeoutMilliSec, org.apache.ambari.server.actionmanager.ActionDBAccessor db, org.apache.ambari.server.state.Clusters fsmObject, int maxAttempts, org.apache.ambari.server.controller.HostsMap hostsMap, com.google.inject.persist.UnitOfWork unitOfWork, org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, org.apache.ambari.server.configuration.Configuration configuration, com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider, org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO, org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory, org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher) {
        this(sleepTimeMilliSec, actionTimeoutMilliSec, db, fsmObject, maxAttempts, hostsMap, unitOfWork, ambariEventPublisher, configuration, entityManagerProvider, hostRoleCommandDAO, hostRoleCommandFactory, null, agentCommandsPublisher);
    }

    private void initializeCaches() {
        clusterHostInfoCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterAccess(5, java.util.concurrent.TimeUnit.MINUTES).build();
        commandParamsStageCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterAccess(5, java.util.concurrent.TimeUnit.MINUTES).build();
        hostParamsStageCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterAccess(5, java.util.concurrent.TimeUnit.MINUTES).build();
    }

    public void start() {
        schedulerThread = new java.lang.Thread(this, "ambari-action-scheduler");
        schedulerThread.start();
        serverActionExecutor.start();
    }

    public void stop() {
        shouldRun = false;
        schedulerThread.interrupt();
        serverActionExecutor.stop();
    }

    public void awake() {
        synchronized(wakeupSyncObject) {
            activeAwakeRequest = true;
            wakeupSyncObject.notify();
        }
    }

    @java.lang.Override
    public void run() {
        while (shouldRun) {
            try {
                synchronized(wakeupSyncObject) {
                    if (!activeAwakeRequest) {
                        wakeupSyncObject.wait(sleepTime);
                    }
                    activeAwakeRequest = false;
                }
                doWork();
            } catch (java.lang.InterruptedException ex) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("Scheduler thread is interrupted going to stop", ex);
                shouldRun = false;
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("Exception received", ex);
                requestsInProgress.clear();
            } catch (java.lang.Throwable t) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("ERROR", t);
                requestsInProgress.clear();
            }
        } 
    }

    public void doWork() throws org.apache.ambari.server.AmbariException {
        try {
            unitOfWork.begin();
            threadEntityManager = entityManagerProvider.get();
            processCancelledRequestsList();
            if (db.getCommandsInProgressCount() == 0) {
                if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("There are no stages currently in progress.");
                }
                return;
            }
            java.util.Set<java.lang.Long> runningRequestIds = new java.util.HashSet<>();
            java.util.List<org.apache.ambari.server.actionmanager.Stage> firstStageInProgressPerRequest = db.getFirstStageInProgressPerRequest();
            if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isDebugEnabled()) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Scheduler wakes up");
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Processing {} in progress stages", firstStageInProgressPerRequest.size());
            }
            publishInProgressTasks(firstStageInProgressPerRequest);
            if (firstStageInProgressPerRequest.isEmpty()) {
                if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("There are no stages currently in progress.");
                }
                return;
            }
            int i_stage = 0;
            java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = filterParallelPerHostStages(firstStageInProgressPerRequest);
            boolean exclusiveRequestIsGoing = false;
            for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                i_stage++;
                long requestId = stage.getRequestId();
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> STAGE_i = {}(requestId={},StageId={})", i_stage, requestId, stage.getStageId());
                org.apache.ambari.server.orm.entities.RequestEntity request = db.getRequestEntity(requestId);
                if (request.isExclusive()) {
                    if (runningRequestIds.size() > 0) {
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Stage requires exclusive execution, but other requests are already executing. Stopping for now");
                        break;
                    }
                    exclusiveRequestIsGoing = true;
                }
                if (runningRequestIds.contains(requestId)) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> We don't want to process different stages from the same request in parallel");
                    continue;
                } else {
                    runningRequestIds.add(requestId);
                    if (!requestsInProgress.contains(requestId)) {
                        requestsInProgress.add(requestId);
                        db.startRequest(requestId);
                    }
                }
                java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commandsToSchedule = new java.util.ArrayList<>();
                com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> commandsToEnqueue = com.google.common.collect.ArrayListMultimap.create();
                java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats> roleStats = processInProgressStage(stage, commandsToSchedule, commandsToEnqueue);
                boolean failed = false;
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats> entry : roleStats.entrySet()) {
                    java.lang.String role = entry.getKey();
                    org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats stats = entry.getValue();
                    if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Stats for role: {}, stats={}", role, stats);
                    }
                    if (stats.isRoleFailed() && (!stage.isSkippable())) {
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("{} failed, request {} will be aborted", role, request.getRequestId());
                        failed = true;
                        break;
                    }
                }
                if (!failed) {
                    failed = hasPreviousStageFailed(stage);
                }
                if (failed) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.error("Operation completely failed, aborting request id: {}", stage.getRequestId());
                    cancelHostRoleCommands(stage.getOrderedHostRoleCommands(), org.apache.ambari.server.actionmanager.ActionScheduler.FAILED_TASK_ABORT_REASONING);
                    abortOperationsForStage(stage);
                    return;
                }
                java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commandsToStart = new java.util.ArrayList<>();
                java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commandsToUpdate = new java.util.ArrayList<>();
                for (org.apache.ambari.server.agent.ExecutionCommand cmd : commandsToSchedule) {
                    processHostRole(request, stage, cmd, commandsToStart, commandsToUpdate);
                }
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> Commands to start: {}", commandsToStart.size());
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> Commands to update: {}", commandsToUpdate.size());
                com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHostEvent> eventMap = formEventMap(stage, commandsToStart);
                java.util.Map<org.apache.ambari.server.agent.ExecutionCommand, java.lang.String> commandsToAbort = new java.util.HashMap<>();
                if (!eventMap.isEmpty()) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> processing {} serviceComponentHostEvents...", eventMap.size());
                    org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(stage.getClusterName());
                    if (cluster != null) {
                        java.util.Map<org.apache.ambari.server.state.ServiceComponentHostEvent, java.lang.String> failedEvents = cluster.processServiceComponentHostEvents(eventMap);
                        if (failedEvents.size() > 0) {
                            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.error("==> {} events failed.", failedEvents.size());
                        }
                        for (java.util.Iterator<org.apache.ambari.server.agent.ExecutionCommand> iterator = commandsToUpdate.iterator(); iterator.hasNext();) {
                            org.apache.ambari.server.agent.ExecutionCommand cmd = iterator.next();
                            for (org.apache.ambari.server.state.ServiceComponentHostEvent event : failedEvents.keySet()) {
                                if (org.apache.commons.lang.StringUtils.equals(event.getHostName(), cmd.getHostname()) && org.apache.commons.lang.StringUtils.equals(event.getServiceComponentName(), cmd.getRole())) {
                                    iterator.remove();
                                    commandsToAbort.put(cmd, failedEvents.get(event));
                                    break;
                                }
                            }
                        }
                    } else {
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("There was events to process but cluster {} not found", stage.getClusterName());
                    }
                }
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> Scheduling {} tasks...", commandsToUpdate.size());
                db.bulkHostRoleScheduled(stage, commandsToUpdate);
                if (commandsToAbort.size() > 0) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> Aborting {} tasks...", commandsToAbort.size());
                    java.util.List<java.lang.Long> taskIds = new java.util.ArrayList<>();
                    for (org.apache.ambari.server.agent.ExecutionCommand command : commandsToAbort.keySet()) {
                        taskIds.add(command.getTaskId());
                    }
                    java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = db.getTasks(taskIds);
                    cancelHostRoleCommands(hostRoleCommands, org.apache.ambari.server.actionmanager.ActionScheduler.FAILED_TASK_ABORT_REASONING);
                    db.bulkAbortHostRole(stage, commandsToAbort);
                }
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> Adding {} tasks to queue...", commandsToUpdate.size());
                for (org.apache.ambari.server.agent.ExecutionCommand cmd : commandsToUpdate) {
                    if (org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.name().equals(cmd.getRole())) {
                        serverActionExecutor.awake();
                    } else {
                        commandsToEnqueue.put(clusters.getHost(cmd.getHostname()).getHostId(), cmd);
                    }
                }
                if (!commandsToEnqueue.isEmpty()) {
                    agentCommandsPublisher.sendAgentCommand(commandsToEnqueue);
                }
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> Finished.");
                if (!configuration.getParallelStageExecution()) {
                    return;
                }
                if (exclusiveRequestIsGoing) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Stage requires exclusive execution, skipping all executing any further stages");
                    break;
                }
            }
            requestsInProgress.retainAll(runningRequestIds);
        } finally {
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Scheduler finished work.");
            unitOfWork.end();
        }
    }

    private void publishInProgressTasks(java.util.List<org.apache.ambari.server.actionmanager.Stage> stages) {
        if (taskStatusLoaded.compareAndSet(false, true)) {
            if (!stages.isEmpty()) {
                com.google.common.base.Function<org.apache.ambari.server.actionmanager.Stage, java.lang.Long> transform = new com.google.common.base.Function<org.apache.ambari.server.actionmanager.Stage, java.lang.Long>() {
                    @java.lang.Override
                    public java.lang.Long apply(org.apache.ambari.server.actionmanager.Stage stage) {
                        return stage.getRequestId();
                    }
                };
                java.util.Set<java.lang.Long> runningRequestID = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Lists.transform(stages, transform));
                java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = db.getAllTasksByRequestIds(runningRequestID);
                hostRoleCommandDAO.publishTaskCreateEvent(hostRoleCommands);
            }
        }
    }

    private java.util.List<org.apache.ambari.server.actionmanager.Stage> filterParallelPerHostStages(java.util.List<org.apache.ambari.server.actionmanager.Stage> firstStageInProgressPerRequest) {
        if (firstStageInProgressPerRequest.size() == 1) {
            return firstStageInProgressPerRequest;
        }
        java.util.List<org.apache.ambari.server.actionmanager.Stage> retVal = new java.util.ArrayList<>();
        long lowerRequestIdInclusive = firstStageInProgressPerRequest.get(0).getRequestId();
        for (org.apache.ambari.server.actionmanager.Stage stage : firstStageInProgressPerRequest) {
            long requestId = stage.getRequestId();
            if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isTraceEnabled()) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("==> Processing stage: {}/{} ({}) for {}", requestId, stage.getStageId(), stage.getRequestContext());
            }
            boolean addStage = true;
            java.util.HashSet<java.lang.String> hostsInProgressForEarlierRequests = new java.util.HashSet<>(hostRoleCommandDAO.getBlockingHostsForRequest(lowerRequestIdInclusive, requestId));
            for (java.lang.String host : stage.getHosts()) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("===> Processing Host {}", host);
                if (hostsInProgressForEarlierRequests.contains(host)) {
                    if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isTraceEnabled()) {
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("===>  Skipping stage since it utilizes at least one host that a previous stage requires: {}/{} ({})", stage.getRequestId(), stage.getStageId(), stage.getRequestContext());
                    }
                    addStage = false;
                    break;
                }
            }
            if (addStage) {
                if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isTraceEnabled()) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("===>  Adding stage to return value: {}/{} ({})", stage.getRequestId(), stage.getStageId(), stage.getRequestContext());
                }
                retVal.add(stage);
            }
        }
        return retVal;
    }

    private boolean hasPreviousStageFailed(org.apache.ambari.server.actionmanager.Stage stage) {
        boolean failed = false;
        long prevStageId = stage.getStageId() - 1;
        if (prevStageId >= 0) {
            java.lang.String actionId = org.apache.ambari.server.utils.StageUtils.getActionId(stage.getRequestId(), prevStageId);
            org.apache.ambari.server.actionmanager.Stage prevStage = db.getStage(actionId);
            if ((prevStage == null) || prevStage.isSkippable()) {
                return false;
            }
            java.util.Map<org.apache.ambari.server.Role, java.lang.Integer> hostCountsForRoles = new java.util.HashMap<>();
            java.util.Map<org.apache.ambari.server.Role, java.lang.Integer> failedHostCountsForRoles = new java.util.HashMap<>();
            for (java.lang.String host : prevStage.getHostRoleCommands().keySet()) {
                java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandMap = prevStage.getHostRoleCommands().get(host);
                for (java.lang.String role : roleCommandMap.keySet()) {
                    org.apache.ambari.server.actionmanager.HostRoleCommand c = roleCommandMap.get(role);
                    if (hostCountsForRoles.get(c.getRole()) == null) {
                        hostCountsForRoles.put(c.getRole(), 0);
                        failedHostCountsForRoles.put(c.getRole(), 0);
                    }
                    int hostCount = hostCountsForRoles.get(c.getRole());
                    hostCountsForRoles.put(c.getRole(), hostCount + 1);
                    if (c.getStatus().isFailedAndNotSkippableState()) {
                        int failedHostCount = failedHostCountsForRoles.get(c.getRole());
                        failedHostCountsForRoles.put(c.getRole(), failedHostCount + 1);
                    }
                }
            }
            for (org.apache.ambari.server.Role role : hostCountsForRoles.keySet()) {
                float failedHosts = failedHostCountsForRoles.get(role);
                float totalHosts = hostCountsForRoles.get(role);
                if (((totalHosts - failedHosts) / totalHosts) < prevStage.getSuccessFactor(role)) {
                    failed = true;
                }
            }
        }
        return failed;
    }

    protected java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats> processInProgressStage(org.apache.ambari.server.actionmanager.Stage s, java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commandsToSchedule, com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> commandsToEnqueue) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("==> Collecting commands to schedule...");
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats> roleStats = initRoleStats(s);
        long now = java.lang.System.currentTimeMillis();
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> rolesCommandsInProgress = s.getHostRolesInProgress();
        org.apache.ambari.server.state.Cluster cluster = null;
        if (null != s.getClusterName()) {
            cluster = clusters.getCluster(s.getClusterName());
        }
        for (java.lang.String host : s.getHosts()) {
            java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commandWrappers = s.getExecutionCommands(host);
            org.apache.ambari.server.state.Host hostObj = null;
            try {
                hostObj = clusters.getHost(host);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Host {} not found, stage is likely a server side action", host);
            }
            int i_my = 0;
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("===>host={}", host);
            for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper : commandWrappers) {
                org.apache.ambari.server.agent.ExecutionCommand c = wrapper.getExecutionCommand();
                java.lang.String roleStr = c.getRole();
                org.apache.ambari.server.actionmanager.HostRoleStatus status = s.getHostRoleStatus(host, roleStr);
                i_my++;
                if (org.apache.ambari.server.actionmanager.ActionScheduler.LOG.isTraceEnabled()) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("Host task {}) id = {} status = {} (role={}), roleCommand = {}", i_my, c.getTaskId(), status, roleStr, c.getRoleCommand());
                }
                boolean hostDeleted = false;
                if (null != cluster) {
                    org.apache.ambari.server.state.Service svc = null;
                    if ((c.getServiceName() != null) && (!c.getServiceName().isEmpty())) {
                        svc = cluster.getService(c.getServiceName());
                    }
                    org.apache.ambari.server.state.ServiceComponent svcComp = null;
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> scHosts = null;
                    try {
                        if (svc != null) {
                            svcComp = svc.getServiceComponent(roleStr);
                            scHosts = svcComp.getServiceComponentHosts();
                        }
                    } catch (org.apache.ambari.server.ServiceComponentNotFoundException scnex) {
                        java.lang.String msg = java.lang.String.format("%s is not not a service component, assuming its an action", roleStr);
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug(msg);
                    }
                    hostDeleted = (scHosts != null) && (!scHosts.containsKey(host));
                    if (hostDeleted) {
                        java.lang.String message = java.lang.String.format("Host component information has not been found.  Details:" + "cluster=%s; host=%s; service=%s; component=%s; ", c.getClusterName(), host, svcComp == null ? "null" : svcComp.getServiceName(), svcComp == null ? "null" : svcComp.getName());
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn(message);
                    }
                }
                long commandTimeout = actionTimeout;
                if (taskTimeoutAdjustment) {
                    java.util.Map<java.lang.String, java.lang.String> commandParams = c.getCommandParams();
                    java.lang.String timeoutKey = org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT;
                    if ((commandParams != null) && commandParams.containsKey(timeoutKey)) {
                        java.lang.String timeoutStr = commandParams.get(timeoutKey);
                        commandTimeout += java.lang.Long.parseLong(timeoutStr) * 1000;
                    } else {
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.error("Execution command has no timeout parameter" + c);
                    }
                }
                boolean isHostStateUnknown = false;
                if (hostDeleted) {
                    java.lang.String message = java.lang.String.format("Host not found when trying to schedule an execution command. " + ((("The most probable reason for that is that host or host component " + "has been deleted recently. The command has been aborted and dequeued.") + "Execution command details: ") + "cmdId: %s; taskId: %s; roleCommand: %s"), c.getCommandId(), c.getTaskId(), c.getRoleCommand());
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("Host {} has been detected as non-available. {}", host, message);
                    db.abortHostRole(host, s.getRequestId(), s.getStageId(), c.getRole(), message);
                    if (c.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE)) {
                        processActionDeath(cluster.getClusterName(), c.getHostname(), roleStr);
                    }
                    status = org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED;
                } else if (timeOutActionNeeded(status, s, hostObj, roleStr, now, commandTimeout) || (isHostStateUnknown = isHostStateUnknown(s, hostObj, roleStr))) {
                    if ((s.getAttemptCount(host, roleStr) >= maxAttempts) || isHostStateUnknown) {
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("Host: {}, role: {}, actionId: {} expired and will be failed", host, roleStr, s.getActionId());
                        boolean isSkipSupported = s.isAutoSkipOnFailureSupported();
                        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = s.getHostRoleCommand(c.getTaskId());
                        if (isSkipSupported && (null != hostRoleCommand)) {
                            isSkipSupported = hostRoleCommand.isFailureAutoSkipped();
                        }
                        db.timeoutHostRole(host, s.getRequestId(), s.getStageId(), c.getRole(), isSkipSupported, isHostStateUnknown);
                        status = s.getHostRoleStatus(host, roleStr);
                        if (null != cluster) {
                            if (((!org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.equals(c.getRoleCommand())) && (!org.apache.ambari.server.RoleCommand.SERVICE_CHECK.equals(c.getRoleCommand()))) && (!org.apache.ambari.server.RoleCommand.ACTIONEXECUTE.equals(c.getRoleCommand()))) {
                                transitionToFailedState(cluster.getClusterName(), c.getServiceName(), roleStr, host, now, false);
                            }
                            if (c.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE)) {
                                processActionDeath(cluster.getClusterName(), c.getHostname(), roleStr);
                            }
                        }
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.info("Removing command from queue, host={}, commandId={} ", host, c.getCommandId());
                    } else {
                        cancelCommandOnTimeout(java.util.Collections.singletonList(s.getHostRoleCommand(host, roleStr)), commandsToEnqueue);
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.info("Host: {}, role: {}, actionId: {} timed out and will be rescheduled", host, roleStr, s.getActionId());
                        commandsToSchedule.add(c);
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("===> commandsToSchedule(reschedule)={}", commandsToSchedule.size());
                    }
                } else if (status.equals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                    if ((org.apache.ambari.server.actionmanager.CommandExecutionType.STAGE == s.getCommandExecutionType()) || (((org.apache.ambari.server.actionmanager.CommandExecutionType.DEPENDENCY_ORDERED == s.getCommandExecutionType()) && (org.apache.ambari.server.actionmanager.CommandExecutionType.DEPENDENCY_ORDERED == configuration.getStageExecutionType())) && areCommandDependenciesFinished(c, s, rolesCommandsInProgress))) {
                        commandsToSchedule.add(c);
                        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.trace("===>commandsToSchedule(first_time)={}", commandsToSchedule.size());
                    }
                }
                updateRoleStats(status, roleStats.get(roleStr));
                if (status == org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED) {
                    org.apache.ambari.server.actionmanager.ActionScheduler.LOG.info("Role {} on host {} was failed", roleStr, host);
                }
            }
        }
        org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Collected {} commands to schedule in this wakeup.", commandsToSchedule.size());
        return roleStats;
    }

    private boolean areCommandDependenciesFinished(org.apache.ambari.server.agent.ExecutionCommand command, org.apache.ambari.server.actionmanager.Stage stage, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> rolesCommandsInProgress) {
        boolean areCommandDependenciesFinished = true;
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(stage.getClusterId());
        if (rco != null) {
            org.apache.ambari.server.metadata.RoleCommandPair roleCommand = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.valueOf(command.getRole()), command.getRoleCommand());
            java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> roleCommandDependencies = rco.getDependencies().get(roleCommand);
            if (roleCommandDependencies != null) {
                roleCommandDependencies.remove(roleCommand);
                if (org.apache.commons.collections.CollectionUtils.containsAny(rolesCommandsInProgress, roleCommandDependencies)) {
                    areCommandDependenciesFinished = false;
                }
            }
        }
        return areCommandDependenciesFinished;
    }

    private void abortOperationsForStage(org.apache.ambari.server.actionmanager.Stage stage) {
        long now = java.lang.System.currentTimeMillis();
        for (java.lang.String hostName : stage.getHosts()) {
            java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commandWrappers = stage.getExecutionCommands(hostName);
            for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper : commandWrappers) {
                org.apache.ambari.server.agent.ExecutionCommand c = wrapper.getExecutionCommand();
                transitionToFailedState(stage.getClusterName(), c.getServiceName(), c.getRole(), hostName, now, true);
            }
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortedOperations = db.abortOperation(stage.getRequestId());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : abortedOperations) {
            if (command.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE)) {
                java.lang.String clusterName = stage.getClusterName();
                processActionDeath(clusterName, command.getHostName(), command.getRole().name());
            }
        }
    }

    private void transitionToFailedState(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, long timestamp, boolean ignoreTransitionException) {
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent failedEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent(componentName, hostname, timestamp);
            if ((((serviceName != null) && (!serviceName.isEmpty())) && (componentName != null)) && (!componentName.isEmpty())) {
                org.apache.ambari.server.state.Service svc = cluster.getService(serviceName);
                org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(componentName);
                org.apache.ambari.server.state.ServiceComponentHost svcCompHost = svcComp.getServiceComponentHost(hostname);
                svcCompHost.handleEvent(failedEvent);
            } else {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.info((((("Service name is " + serviceName) + ", component name is ") + componentName) + "skipping sending ServiceComponentHostOpFailedEvent for ") + componentName);
            }
        } catch (org.apache.ambari.server.ServiceComponentNotFoundException scnex) {
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("{} associated with service {} is not a service component, assuming it's an action.", componentName, serviceName);
        } catch (org.apache.ambari.server.ServiceComponentHostNotFoundException e) {
            java.lang.String msg = java.lang.String.format("Service component host %s not found, " + "unable to transition to failed state.", componentName);
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn(msg, e);
        } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
            if (ignoreTransitionException) {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Unable to transition to failed state.", e);
            } else {
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("Unable to transition to failed state.", e);
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.warn("Unable to transition to failed state.", e);
        }
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats> initRoleStats(org.apache.ambari.server.actionmanager.Stage s) {
        java.util.Map<org.apache.ambari.server.Role, java.lang.Integer> hostCountsForRoles = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats> roleStats = new java.util.TreeMap<>();
        for (java.lang.String host : s.getHostRoleCommands().keySet()) {
            java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandMap = s.getHostRoleCommands().get(host);
            for (java.lang.String role : roleCommandMap.keySet()) {
                org.apache.ambari.server.actionmanager.HostRoleCommand c = roleCommandMap.get(role);
                if (hostCountsForRoles.get(c.getRole()) == null) {
                    hostCountsForRoles.put(c.getRole(), 0);
                }
                int val = hostCountsForRoles.get(c.getRole());
                hostCountsForRoles.put(c.getRole(), val + 1);
            }
        }
        for (org.apache.ambari.server.Role r : hostCountsForRoles.keySet()) {
            org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats stats = new org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats(hostCountsForRoles.get(r), s.getSuccessFactor(r));
            roleStats.put(r.name(), stats);
        }
        return roleStats;
    }

    protected boolean wasAgentRestartedDuringOperation(org.apache.ambari.server.state.Host host, org.apache.ambari.server.actionmanager.Stage stage, java.lang.String role) {
        java.lang.String hostName = host.getHostName();
        long taskStartTime = stage.getHostRoleCommand(hostName, role).getStartTime();
        long lastAgentStartTime = host.getLastAgentStartTime();
        return ((taskStartTime > 0) && (lastAgentStartTime > 0)) && (taskStartTime <= lastAgentStartTime);
    }

    protected boolean timeOutActionNeeded(org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.Stage stage, org.apache.ambari.server.state.Host host, java.lang.String role, long currentTime, long taskTimeout) throws org.apache.ambari.server.AmbariException {
        if ((!status.equals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) && (!status.equals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS))) {
            return false;
        }
        java.lang.String hostName = (null == host) ? null : host.getHostName();
        if (hasCommandInProgress(stage, hostName) && (!status.equals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS))) {
            return false;
        }
        if (currentTime >= (stage.getLastAttemptTime(hostName, role) + taskTimeout)) {
            return true;
        }
        return false;
    }

    private boolean isHostStateUnknown(org.apache.ambari.server.actionmanager.Stage stage, org.apache.ambari.server.state.Host host, java.lang.String role) {
        if ((null != host) && (host.getState().equals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST) || wasAgentRestartedDuringOperation(host, stage, role))) {
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.debug("Abort action since agent is not heartbeating or agent was restarted.");
            return true;
        }
        return false;
    }

    private boolean hasCommandInProgress(org.apache.ambari.server.actionmanager.Stage stage, java.lang.String host) {
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commandWrappers = stage.getExecutionCommands(host);
        for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper : commandWrappers) {
            org.apache.ambari.server.agent.ExecutionCommand c = wrapper.getExecutionCommand();
            java.lang.String roleStr = c.getRole();
            org.apache.ambari.server.actionmanager.HostRoleStatus status = stage.getHostRoleStatus(host, roleStr);
            if (status == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS) {
                return true;
            }
        }
        return false;
    }

    private com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHostEvent> formEventMap(org.apache.ambari.server.actionmanager.Stage s, java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commands) {
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHostEvent> serviceEventMap = com.google.common.collect.ArrayListMultimap.create();
        for (org.apache.ambari.server.agent.ExecutionCommand cmd : commands) {
            java.lang.String hostname = cmd.getHostname();
            java.lang.String roleStr = cmd.getRole();
            if (org.apache.ambari.server.RoleCommand.ACTIONEXECUTE != cmd.getRoleCommand()) {
                serviceEventMap.put(cmd.getServiceName(), s.getFsmEvent(hostname, roleStr).getEvent());
            }
        }
        return serviceEventMap;
    }

    private void processHostRole(org.apache.ambari.server.orm.entities.RequestEntity r, org.apache.ambari.server.actionmanager.Stage s, org.apache.ambari.server.agent.ExecutionCommand cmd, java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commandsToStart, java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commandsToUpdate) throws org.apache.ambari.server.AmbariException {
        long now = java.lang.System.currentTimeMillis();
        java.lang.String roleStr = cmd.getRole();
        java.lang.String hostname = cmd.getHostname();
        if (s.getStartTime(hostname, roleStr) < 0) {
            commandsToStart.add(cmd);
            s.setStartTime(hostname, roleStr, now);
            s.setHostRoleStatus(hostname, roleStr, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        }
        s.setLastAttemptTime(hostname, roleStr, now);
        s.incrementAttemptCount(hostname, roleStr);
        java.lang.String requestPK = r.getRequestId().toString();
        java.lang.String stagePk = (s.getStageId() + "-") + s.getRequestId();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = clusterHostInfoCache.getIfPresent(requestPK);
        if (clusterHostInfo == null) {
            java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>() {}.getType();
            clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(r.getClusterHostInfo(), type);
            clusterHostInfoCache.put(requestPK, clusterHostInfo);
        }
        cmd.setClusterHostInfo(clusterHostInfo);
        java.util.Map<java.lang.String, java.lang.String> commandParams = commandParamsStageCache.getIfPresent(stagePk);
        if (commandParams == null) {
            java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
            commandParams = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(s.getCommandParamsStage(), type);
            commandParamsStageCache.put(stagePk, commandParams);
        }
        java.util.Map<java.lang.String, java.lang.String> commandParamsCmd = cmd.getCommandParams();
        commandParamsCmd.putAll(commandParams);
        cmd.setCommandParams(commandParamsCmd);
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(s.getClusterName());
            if (null != cluster) {
                for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(hostname)) {
                    cmd.getLocalComponents().add(sch.getServiceComponentName());
                }
            }
        } catch (org.apache.ambari.server.ClusterNotFoundException cnfe) {
        }
        java.util.Map<java.lang.String, java.lang.String> hostParams = hostParamsStageCache.getIfPresent(stagePk);
        if (hostParams == null) {
            java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
            hostParams = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(s.getHostParamsStage(), type);
            hostParamsStageCache.put(stagePk, hostParams);
        }
        java.util.Map<java.lang.String, java.lang.String> hostParamsCmd = cmd.getHostLevelParams();
        hostParamsCmd.putAll(hostParams);
        cmd.setHostLevelParams(hostParamsCmd);
        cmd.setHostname(hostsMap.getHostMap(hostname));
        commandsToUpdate.add(cmd);
    }

    public void scheduleCancellingRequest(long requestId, java.lang.String reason) {
        synchronized(requestsToBeCancelled) {
            requestsToBeCancelled.add(requestId);
            requestCancelReasons.put(requestId, reason);
        }
    }

    private void processCancelledRequestsList() throws org.apache.ambari.server.AmbariException {
        synchronized(requestsToBeCancelled) {
            for (java.lang.Long requestId : requestsToBeCancelled) {
                java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entitiesToDequeue = hostRoleCommandDAO.findByRequestIdAndStatuses(requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.NOT_COMPLETED_STATUSES);
                if (!entitiesToDequeue.isEmpty()) {
                    java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> tasksToDequeue = new java.util.ArrayList<>(entitiesToDequeue.size());
                    for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrcEntity : entitiesToDequeue) {
                        org.apache.ambari.server.actionmanager.HostRoleCommand task = hostRoleCommandFactory.createExisting(hrcEntity);
                        tasksToDequeue.add(task);
                    }
                    java.lang.String reason = requestCancelReasons.get(requestId);
                    cancelHostRoleCommands(tasksToDequeue, reason);
                }
                java.util.List<org.apache.ambari.server.actionmanager.Stage> stagesInProgress = db.getStagesInProgressForRequest(requestId);
                for (org.apache.ambari.server.actionmanager.Stage stageInProgress : stagesInProgress) {
                    abortOperationsForStage(stageInProgress);
                }
            }
            requestsToBeCancelled.clear();
            requestCancelReasons.clear();
        }
    }

    void cancelHostRoleCommands(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands, java.lang.String reason) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : hostRoleCommands) {
            if (!org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.equals(hostRoleCommand.getRole())) {
                if ((hostRoleCommand.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (hostRoleCommand.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) {
                    org.apache.ambari.server.agent.CancelCommand cancelCommand = new org.apache.ambari.server.agent.CancelCommand();
                    cancelCommand.setTargetTaskId(hostRoleCommand.getTaskId());
                    cancelCommand.setReason(reason);
                    agentCommandsPublisher.sendAgentCommand(hostRoleCommand.getHostId(), cancelCommand);
                }
            }
            if (hostRoleCommand.getStatus().isHoldingState()) {
                db.abortHostRole(hostRoleCommand.getHostName(), hostRoleCommand.getRequestId(), hostRoleCommand.getStageId(), hostRoleCommand.getRole().name());
            }
            if (hostRoleCommand.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE)) {
                java.lang.String clusterName = hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getClusterName();
                processActionDeath(clusterName, hostRoleCommand.getHostName(), hostRoleCommand.getRole().name());
            }
        }
    }

    void cancelCommandOnTimeout(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands, com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> commandsToEnqueue) {
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : hostRoleCommands) {
            if (!org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.equals(hostRoleCommand.getRole())) {
                if ((hostRoleCommand.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (hostRoleCommand.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) {
                    org.apache.ambari.server.agent.CancelCommand cancelCommand = new org.apache.ambari.server.agent.CancelCommand();
                    cancelCommand.setTargetTaskId(hostRoleCommand.getTaskId());
                    cancelCommand.setReason("Stage timeout");
                    commandsToEnqueue.put(hostRoleCommand.getHostId(), cancelCommand);
                }
            }
        }
    }

    private void processActionDeath(java.lang.String clusterName, java.lang.String hostname, java.lang.String role) {
        try {
            java.lang.Long clusterId = (clusterName != null) ? clusters.getCluster(clusterName).getClusterId() : null;
            org.apache.ambari.server.agent.CommandReport report = new org.apache.ambari.server.agent.CommandReport();
            report.setRole(role);
            report.setStdOut("Action is dead");
            report.setStdErr("Action is dead");
            report.setStructuredOut("{}");
            report.setExitCode(1);
            report.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.toString());
            org.apache.ambari.server.events.ActionFinalReportReceivedEvent event = new org.apache.ambari.server.events.ActionFinalReportReceivedEvent(clusterId, hostname, report, true);
            ambariEventPublisher.publish(event);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.error(java.lang.String.format("Can not get cluster %s", clusterName), e);
        }
    }

    private void updateRoleStats(org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats rs) {
        switch (status) {
            case COMPLETED :
                rs.numSucceeded++;
                break;
            case FAILED :
                rs.numFailed++;
                break;
            case QUEUED :
                rs.numQueued++;
                break;
            case PENDING :
                rs.numPending++;
                break;
            case TIMEDOUT :
                rs.numTimedOut++;
                break;
            case ABORTED :
                rs.numAborted++;
                break;
            case IN_PROGRESS :
                rs.numInProgress++;
                break;
            case HOLDING :
            case HOLDING_FAILED :
            case HOLDING_TIMEDOUT :
                rs.numHolding++;
                break;
            case SKIPPED_FAILED :
                rs.numSkipped++;
                break;
            default :
                org.apache.ambari.server.actionmanager.ActionScheduler.LOG.error("Unknown status " + status.name());
        }
    }

    public void setTaskTimeoutAdjustment(boolean val) {
        taskTimeoutAdjustment = val;
    }

    org.apache.ambari.server.serveraction.ServerActionExecutor getServerActionExecutor() {
        return serverActionExecutor;
    }

    @com.google.common.eventbus.Subscribe
    public void onEvent(org.apache.ambari.server.events.jpa.EntityManagerCacheInvalidationEvent event) {
        try {
            if ((null != threadEntityManager) && threadEntityManager.isOpen()) {
                threadEntityManager.clear();
            }
        } catch (java.lang.Throwable throwable) {
            org.apache.ambari.server.actionmanager.ActionScheduler.LOG.error("Unable to clear the EntityManager for the scheduler thread", throwable);
        }
    }

    static class RoleStats {
        int numInProgress;

        int numQueued = 0;

        int numSucceeded = 0;

        int numFailed = 0;

        int numTimedOut = 0;

        int numPending = 0;

        int numAborted = 0;

        int numHolding = 0;

        int numSkipped = 0;

        final int totalHosts;

        final float successFactor;

        RoleStats(int total, float successFactor) {
            totalHosts = total;
            this.successFactor = successFactor;
        }

        boolean isSuccessFactorMet() {
            int minSuccessNeeded = ((int) (java.lang.Math.ceil(successFactor * totalHosts)));
            return minSuccessNeeded <= numSucceeded;
        }

        private boolean isRoleInProgress() {
            return (((numPending + numQueued) + numInProgress) + numHolding) > 0;
        }

        boolean isRoleFailed() {
            return !(isRoleInProgress() || isSuccessFactorMet());
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("numQueued=").append(numQueued);
            builder.append(", numInProgress=").append(numInProgress);
            builder.append(", numSucceeded=").append(numSucceeded);
            builder.append(", numFailed=").append(numFailed);
            builder.append(", numTimedOut=").append(numTimedOut);
            builder.append(", numPending=").append(numPending);
            builder.append(", numAborted=").append(numAborted);
            builder.append(", numSkipped=").append(numSkipped);
            builder.append(", totalHosts=").append(totalHosts);
            builder.append(", successFactor=").append(successFactor);
            return builder.toString();
        }
    }
}