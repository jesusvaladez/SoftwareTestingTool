package org.apache.ambari.server.serveraction;
import org.springframework.security.core.context.SecurityContextHolder;
@org.apache.ambari.server.StaticallyInject
public class ServerActionExecutor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.ServerActionExecutor.class);

    private static final java.lang.Long DEFAULT_EXECUTION_TIMEOUT_MS = (1000L * 60) * 5;

    private static final java.lang.Long POLLING_TIMEOUT_MS = 1000L * 5;

    @com.google.inject.Inject
    private static com.google.inject.Injector injector;

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration configuration;

    private final java.util.Map<java.lang.Long, java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object>> requestSharedDataMap = new java.util.concurrent.ConcurrentHashMap<>();

    private final org.apache.ambari.server.actionmanager.ActionDBAccessor db;

    private final java.lang.Object wakeupSyncObject = new java.lang.Object();

    private final long sleepTimeMS;

    private boolean activeAwakeRequest = false;

    private java.lang.Thread executorThread = null;

    private final java.util.Timer cacheTimer = new java.util.Timer("server-action-executor-cache-timer", true);

    public static void init(com.google.inject.Injector injector) {
        org.apache.ambari.server.serveraction.ServerActionExecutor.injector = injector;
    }

    public ServerActionExecutor(org.apache.ambari.server.actionmanager.ActionDBAccessor db, long sleepTimeMS) {
        this.db = db;
        this.sleepTimeMS = (sleepTimeMS < 1) ? org.apache.ambari.server.serveraction.ServerActionExecutor.POLLING_TIMEOUT_MS : sleepTimeMS;
        cacheTimer.schedule(new org.apache.ambari.server.serveraction.ServerActionExecutor.ServerActionSharedRequestEvictor(), java.util.concurrent.TimeUnit.HOURS.toMillis(1), java.util.concurrent.TimeUnit.HOURS.toMillis(1));
    }

    public void start() {
        org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info("Starting Server Action Executor thread...");
        executorThread = new java.lang.Thread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                while (!java.lang.Thread.interrupted()) {
                    try {
                        synchronized(wakeupSyncObject) {
                            if (!activeAwakeRequest) {
                                wakeupSyncObject.wait(sleepTimeMS);
                            }
                            activeAwakeRequest = false;
                        }
                        doWork();
                    } catch (java.lang.InterruptedException e) {
                        org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn("Server Action Executor thread interrupted, starting to shutdown...");
                        break;
                    }
                } 
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info("Server Action Executor thread shutting down...");
            }
        }, "Server Action Executor");
        executorThread.start();
        if (executorThread.isAlive()) {
            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info("Server Action Executor thread started.");
        }
    }

    public void stop() {
        org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info("Stopping Server Action Executor thread...");
        if (executorThread != null) {
            executorThread.interrupt();
            for (int i = 0; i < 120; i++) {
                try {
                    executorThread.join(500);
                } catch (java.lang.InterruptedException e) {
                }
                if (!executorThread.isAlive()) {
                    break;
                }
            }
            if (!executorThread.isAlive()) {
                executorThread = null;
            }
        }
        if (executorThread == null) {
            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info("Server Action Executor thread stopped.");
        } else {
            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn("Server Action Executor thread hasn't stopped, giving up waiting.");
        }
    }

    public void awake() {
        synchronized(wakeupSyncObject) {
            activeAwakeRequest = true;
            wakeupSyncObject.notify();
        }
    }

    private java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> getRequestSharedDataContext(long requestId) {
        synchronized(requestSharedDataMap) {
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> map = requestSharedDataMap.get(requestId);
            if (map == null) {
                map = new java.util.concurrent.ConcurrentHashMap<>();
                requestSharedDataMap.put(requestId, map);
            }
            return map;
        }
    }

    private org.apache.ambari.server.agent.CommandReport createInProgressReport() {
        org.apache.ambari.server.agent.CommandReport commandReport = new org.apache.ambari.server.agent.CommandReport();
        commandReport.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        commandReport.setStdErr("");
        commandReport.setStdOut("");
        return commandReport;
    }

    private org.apache.ambari.server.agent.CommandReport createTimedOutReport() {
        org.apache.ambari.server.agent.CommandReport commandReport = new org.apache.ambari.server.agent.CommandReport();
        commandReport.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT.toString());
        commandReport.setStdErr("");
        commandReport.setStdOut("");
        return commandReport;
    }

    private org.apache.ambari.server.agent.CommandReport createErrorReport(java.lang.String message) {
        org.apache.ambari.server.agent.CommandReport commandReport = new org.apache.ambari.server.agent.CommandReport();
        commandReport.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        commandReport.setExitCode(1);
        commandReport.setStdOut("Server action failed");
        commandReport.setStdErr(message == null ? "Server action failed" : message);
        return commandReport;
    }

    private void updateHostRoleState(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand, org.apache.ambari.server.agent.ExecutionCommand executionCommand, org.apache.ambari.server.agent.CommandReport commandReport) {
        if (commandReport == null) {
            commandReport = createErrorReport("Unknown error condition");
        }
        db.updateHostRoleState(null, hostRoleCommand.getRequestId(), hostRoleCommand.getStageId(), executionCommand.getRole(), commandReport);
    }

    private long determineTimeout(org.apache.ambari.server.agent.ExecutionCommand executionCommand) {
        java.util.Map<java.lang.String, java.lang.String> params = executionCommand.getCommandParams();
        java.lang.String paramsTimeout = (params == null) ? null : params.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT);
        java.lang.Long timeout;
        try {
            timeout = (paramsTimeout == null) ? null : java.lang.Long.parseLong(paramsTimeout) * 1000L;
        } catch (java.lang.NumberFormatException e) {
            timeout = null;
        }
        if (timeout == null) {
            java.lang.Integer defaultTimeoutSeconds = org.apache.ambari.server.serveraction.ServerActionExecutor.configuration.getDefaultServerTaskTimeout();
            if (defaultTimeoutSeconds != null) {
                timeout = defaultTimeoutSeconds * 1000L;
            }
        }
        return timeout == null ? org.apache.ambari.server.serveraction.ServerActionExecutor.DEFAULT_EXECUTION_TIMEOUT_MS : timeout < 0 ? 0 : timeout;
    }

    public void doWork() throws java.lang.InterruptedException {
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> tasks = db.getTasksByRoleAndStatus(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.name(), org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        if ((tasks != null) && (!tasks.isEmpty())) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand task : tasks) {
                java.lang.Long taskId = task.getTaskId();
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug("Processing task #{}", taskId);
                if (task.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) {
                    org.apache.ambari.server.actionmanager.ExecutionCommandWrapper executionWrapper = task.getExecutionCommandWrapper();
                    if (executionWrapper != null) {
                        org.apache.ambari.server.agent.ExecutionCommand executionCommand = executionWrapper.getExecutionCommand();
                        if (executionCommand != null) {
                            org.apache.ambari.server.serveraction.ServerActionExecutor.Worker worker = new org.apache.ambari.server.serveraction.ServerActionExecutor.Worker(task, executionCommand);
                            java.lang.Thread workerThread = new java.lang.Thread(worker, java.lang.String.format("Server Action Executor Worker %s", taskId));
                            java.lang.Long timeout = determineTimeout(executionCommand);
                            updateHostRoleState(task, executionCommand, createInProgressReport());
                            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug("Starting Server Action Executor Worker thread for task #{}.", taskId);
                            workerThread.start();
                            try {
                                workerThread.join(timeout);
                            } catch (java.lang.InterruptedException e) {
                                workerThread.interrupt();
                                throw e;
                            }
                            if (workerThread.isAlive()) {
                                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug("Server Action Executor Worker thread for task #{} timed out - it failed to complete within {} ms.", taskId, timeout);
                                workerThread.interrupt();
                                updateHostRoleState(task, executionCommand, createTimedOutReport());
                            } else {
                                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug("Server Action Executor Worker thread for task #{} exited on its own.", taskId);
                                updateHostRoleState(task, executionCommand, worker.getCommandReport());
                            }
                        } else {
                            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn("Task #{} failed to produce an ExecutionCommand, skipping.", taskId);
                        }
                    } else {
                        org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn("Task #{} failed to produce an ExecutionCommandWrapper, skipping.", taskId);
                    }
                } else {
                    org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn("Queued task #{} is expected to have a status of {} but has a status of {}, skipping.", taskId, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, task.getStatus());
                }
            }
        }
    }

    private class Worker implements java.lang.Runnable {
        private final java.lang.Long taskId;

        private final org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand;

        private final org.apache.ambari.server.agent.ExecutionCommand executionCommand;

        private org.apache.ambari.server.agent.CommandReport commandReport = null;

        @java.lang.Override
        public void run() {
            try {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug("Executing task #{}", taskId);
                org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken authentication = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken("server_action_executor");
                authentication.setAuthenticated(true);
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
                commandReport = execute(hostRoleCommand, executionCommand);
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug("Task #{} completed execution with status of {}", taskId, commandReport == null ? "UNKNOWN" : commandReport.getStatus());
            } catch (java.lang.Throwable t) {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn("Task #{} failed to complete execution due to thrown exception: {}:{}", taskId, t.getClass().getName(), t.getLocalizedMessage(), t);
                commandReport = createErrorReport(t.getLocalizedMessage());
            }
        }

        public org.apache.ambari.server.agent.CommandReport getCommandReport() {
            return commandReport;
        }

        private org.apache.ambari.server.agent.CommandReport execute(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand, org.apache.ambari.server.agent.ExecutionCommand executionCommand) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
            if (hostRoleCommand == null) {
                throw new org.apache.ambari.server.AmbariException("Missing HostRoleCommand data");
            } else if (executionCommand == null) {
                throw new org.apache.ambari.server.AmbariException("Missing ExecutionCommand data");
            } else {
                java.util.Map<java.lang.String, java.lang.String> roleParams = executionCommand.getRoleParams();
                if (roleParams == null) {
                    throw new org.apache.ambari.server.AmbariException("Missing RoleParams data");
                } else {
                    java.lang.String actionClassname = roleParams.get(org.apache.ambari.server.serveraction.ServerAction.ACTION_NAME);
                    if (actionClassname == null) {
                        throw new org.apache.ambari.server.AmbariException("Missing action classname for server action");
                    } else {
                        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceInfo>();
                        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary upgradeSummary = executionCommand.getUpgradeSummary();
                        if (upgradeSummary != null) {
                            java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeServiceSummary> upgradeServiceSummaries = upgradeSummary.services;
                            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug("UpgradeServiceSummary: " + upgradeServiceSummaries);
                            org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.serveraction.ServerActionExecutor.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
                            org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = ambariManagementController.getAmbariMetaInfo();
                            java.lang.String serviceName = executionCommand.getServiceName();
                            if ((serviceName != null) && (!serviceName.isEmpty())) {
                                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info(java.lang.String.format("Server action %s is associated with service %s", actionClassname, serviceName));
                                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeServiceSummary serviceSummary = upgradeServiceSummaries.get(serviceName);
                                addServiceInfo(services, ambariMetaInfo, serviceSummary.sourceStackId, serviceName);
                            } else {
                                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info(java.lang.String.format("Server action %s is not associated with a service", actionClassname));
                                for (java.lang.String key : upgradeServiceSummaries.keySet()) {
                                    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeServiceSummary serviceSummary = upgradeServiceSummaries.get(key);
                                    addServiceInfo(services, ambariMetaInfo, serviceSummary.sourceStackId, key);
                                }
                            }
                            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info(java.lang.String.format("Attempt to load server action classes from %s", services.keySet().toString()));
                        }
                        org.apache.ambari.server.serveraction.ServerAction action = createServerAction(actionClassname, services);
                        if (action == null) {
                            throw new org.apache.ambari.server.AmbariException("Failed to create server action: " + actionClassname);
                        } else {
                            action.setExecutionCommand(executionCommand);
                            action.setHostRoleCommand(hostRoleCommand);
                            return action.execute(getRequestSharedDataContext(hostRoleCommand.getRequestId()));
                        }
                    }
                }
            }
        }

        private void addServiceInfo(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo, java.lang.String stackId, java.lang.String serviceName) {
            java.util.List<java.lang.String> stackInfo = getStackInfo(stackId);
            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Stack info list: %s", stackInfo));
            if (stackInfo.size() > 1) {
                try {
                    org.apache.ambari.server.state.ServiceInfo service = ambariMetaInfo.getService(stackInfo.get(0), stackInfo.get(1), serviceName);
                    org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Adding %s to the list of services for loading external Jars...", service.getName()));
                    services.put(serviceName, service);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.error(java.lang.String.format("Failed to obtain service info for stack %s, service name %s", stackId, serviceName), e);
                }
            }
        }

        private java.util.List<java.lang.String> getStackInfo(java.lang.String stackId) {
            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Stack id: %s", stackId));
            java.util.StringTokenizer tokens = new java.util.StringTokenizer(stackId, "-");
            java.util.List<java.lang.String> info = new java.util.ArrayList<java.lang.String>();
            while (tokens.hasMoreElements()) {
                info.add(((java.lang.String) (tokens.nextElement())));
            } 
            return info;
        }

        private org.apache.ambari.server.serveraction.ServerAction createServerAction(java.lang.String classname, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services) throws org.apache.ambari.server.AmbariException {
            java.lang.Class<?> actionClass = null;
            actionClass = getServerActionClass(classname);
            if (actionClass == null) {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Did not find %s in Ambari, try to load it from external directories", classname));
                actionClass = getServiceLevelServerActionClass(classname, services);
            }
            if (actionClass == null) {
                throw new org.apache.ambari.server.AmbariException("Unable to load server action class: " + classname);
            } else {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Ready to init server action %s", classname));
                java.lang.Class<? extends org.apache.ambari.server.serveraction.ServerAction> serverActionClass = actionClass.asSubclass(org.apache.ambari.server.serveraction.ServerAction.class);
                if (serverActionClass == null) {
                    throw new org.apache.ambari.server.AmbariException("Unable to execute server action class, invalid type: " + classname);
                } else {
                    return org.apache.ambari.server.serveraction.ServerActionExecutor.injector.getInstance(serverActionClass);
                }
            }
        }

        private java.lang.Class<?> getServiceLevelServerActionClass(java.lang.String classname, java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services) {
            java.util.List<java.net.URL> urls = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.ServiceInfo service : services.values()) {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Checking service %s", service));
                java.io.File dir = service.getServerActionsFolder();
                if (dir != null) {
                    org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Service %s, external dir %s", service.getName(), dir.getAbsolutePath()));
                    java.io.File[] jars = dir.listFiles(new java.io.FilenameFilter() {
                        @java.lang.Override
                        public boolean accept(java.io.File dir, java.lang.String name) {
                            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Checking folder %s", name));
                            return name.endsWith(".jar");
                        }
                    });
                    for (java.io.File jar : jars) {
                        try {
                            java.net.URL url = jar.toURI().toURL();
                            urls.add(url);
                            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.info("Adding server action jar to classpath: {}", url);
                        } catch (java.lang.Exception e) {
                            org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.error("Failed to add server action jar to classpath: {}", jar.getAbsolutePath(), e);
                        }
                    }
                } else {
                    org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.error(java.lang.String.format("%s service server actions folder returned null", service));
                }
            }
            java.lang.ClassLoader classLoader = new java.net.URLClassLoader(urls.toArray(new java.net.URL[urls.size()]), org.springframework.util.ClassUtils.getDefaultClassLoader());
            java.lang.Class<?> actionClass = null;
            try {
                actionClass = org.springframework.util.ClassUtils.resolveClassName(classname, classLoader);
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.debug(java.lang.String.format("Found external server action %s", classname));
            } catch (java.lang.IllegalArgumentException illegalArgumentException) {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.error(java.lang.String.format("Unable to find server action %s in external server action directories", classname), illegalArgumentException);
            }
            return actionClass;
        }

        private java.lang.Class<?> getServerActionClass(java.lang.String classname) throws org.apache.ambari.server.AmbariException {
            java.lang.Class<?> actionClass = null;
            try {
                actionClass = java.lang.Class.forName(classname);
                if (actionClass == null) {
                    org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn(java.lang.String.format("Unable to load server action class: %s from Ambari", classname));
                }
            } catch (java.lang.ClassNotFoundException e) {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.error(java.lang.String.format("Unable to load server action class: %s", classname), e);
            }
            return actionClass;
        }

        private Worker(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand, org.apache.ambari.server.agent.ExecutionCommand executionCommand) {
            taskId = hostRoleCommand.getTaskId();
            this.hostRoleCommand = hostRoleCommand;
            this.executionCommand = executionCommand;
        }
    }

    private class ServerActionSharedRequestEvictor extends java.util.TimerTask {
        @java.lang.Override
        public void run() {
            if (requestSharedDataMap.isEmpty()) {
                return;
            }
            try {
                java.util.Set<java.lang.Long> requestsInProgress = new java.util.HashSet<>();
                java.util.List<org.apache.ambari.server.actionmanager.Stage> currentStageInProgressPerRequest = db.getFirstStageInProgressPerRequest();
                for (org.apache.ambari.server.actionmanager.Stage stage : currentStageInProgressPerRequest) {
                    requestsInProgress.add(stage.getRequestId());
                }
                synchronized(requestSharedDataMap) {
                    java.util.Set<java.lang.Long> cachedRequestIds = requestSharedDataMap.keySet();
                    for (long cachedRequestId : cachedRequestIds) {
                        if (!requestsInProgress.contains(cachedRequestId)) {
                            requestSharedDataMap.remove(cachedRequestId);
                        }
                    }
                }
            } catch (java.lang.Exception exception) {
                org.apache.ambari.server.serveraction.ServerActionExecutor.LOG.warn("Unable to clear the server-side action request cache", exception);
            }
        }
    }
}