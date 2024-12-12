package org.apache.ambari.server.actionmanager;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
public class Stage {
    public static final java.lang.String INTERNAL_HOSTNAME = "_internal_ambari";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.Stage.class);

    private final long requestId;

    private java.lang.String clusterName;

    private long clusterId = -1L;

    private long stageId = -1;

    private final java.lang.String logDir;

    private final java.lang.String requestContext;

    private org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    private org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    private java.lang.String commandParamsStage;

    private java.lang.String hostParamsStage;

    private org.apache.ambari.server.actionmanager.CommandExecutionType commandExecutionType = org.apache.ambari.server.actionmanager.CommandExecutionType.STAGE;

    private boolean skippable;

    private boolean supportsAutoSkipOnFailure;

    private int stageTimeout = -1;

    private volatile boolean wrappersLoaded = false;

    private java.util.Map<org.apache.ambari.server.Role, java.lang.Float> successFactors = new java.util.HashMap<>();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommands = new java.util.TreeMap<>();

    private java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper>> commandsToSend = new java.util.TreeMap<>();

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory;

    @com.google.inject.assistedinject.AssistedInject
    public Stage(@com.google.inject.assistedinject.Assisted
    long requestId, @com.google.inject.assistedinject.Assisted("logDir")
    java.lang.String logDir, @com.google.inject.assistedinject.Assisted("clusterName")
    @javax.annotation.Nullable
    java.lang.String clusterName, @com.google.inject.assistedinject.Assisted("clusterId")
    long clusterId, @com.google.inject.assistedinject.Assisted("requestContext")
    @javax.annotation.Nullable
    java.lang.String requestContext, @com.google.inject.assistedinject.Assisted("commandParamsStage")
    java.lang.String commandParamsStage, @com.google.inject.assistedinject.Assisted("hostParamsStage")
    java.lang.String hostParamsStage, org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory) {
        wrappersLoaded = true;
        this.requestId = requestId;
        this.logDir = logDir;
        this.clusterName = clusterName;
        this.clusterId = clusterId;
        this.requestContext = (requestContext == null) ? "" : requestContext;
        this.commandParamsStage = commandParamsStage;
        this.hostParamsStage = hostParamsStage;
        skippable = false;
        supportsAutoSkipOnFailure = false;
        this.hostRoleCommandFactory = hostRoleCommandFactory;
        this.ecwFactory = ecwFactory;
    }

    @com.google.inject.assistedinject.AssistedInject
    public Stage(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.StageEntity stageEntity, org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO, org.apache.ambari.server.actionmanager.ActionDBAccessor dbAccessor, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory) {
        this.hostRoleCommandFactory = hostRoleCommandFactory;
        this.ecwFactory = ecwFactory;
        requestId = stageEntity.getRequestId();
        stageId = stageEntity.getStageId();
        skippable = stageEntity.isSkippable();
        supportsAutoSkipOnFailure = stageEntity.isAutoSkipOnFailureSupported();
        logDir = stageEntity.getLogInfo();
        clusterId = stageEntity.getClusterId().longValue();
        if ((-1L) != clusterId) {
            try {
                clusterName = clusters.getClusterById(clusterId).getClusterName();
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.actionmanager.Stage.LOG.debug("Could not load cluster with id {}, the cluster may have been removed for stage {}", java.lang.Long.valueOf(clusterId), java.lang.Long.valueOf(stageId));
            }
        }
        requestContext = stageEntity.getRequestContext();
        commandParamsStage = stageEntity.getCommandParamsStage();
        hostParamsStage = stageEntity.getHostParamsStage();
        commandExecutionType = stageEntity.getCommandExecutionType();
        status = stageEntity.getStatus();
        displayStatus = stageEntity.getDisplayStatus();
        java.util.List<java.lang.Long> taskIds = hostRoleCommandDAO.findTaskIdsByStage(requestId, stageId);
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = dbAccessor.getTasks(taskIds);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            java.lang.String hostname = org.apache.ambari.server.actionmanager.Stage.getSafeHost(command.getHostName());
            if (!hostRoleCommands.containsKey(hostname)) {
                hostRoleCommands.put(hostname, new java.util.LinkedHashMap<>());
            }
            hostRoleCommands.get(hostname).put(command.getRole().toString(), command);
        }
        for (org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity successCriteriaEntity : stageEntity.getRoleSuccessCriterias()) {
            successFactors.put(successCriteriaEntity.getRole(), successCriteriaEntity.getSuccessFactor().floatValue());
        }
    }

    public synchronized org.apache.ambari.server.orm.entities.StageEntity constructNewPersistenceEntity() {
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = new org.apache.ambari.server.orm.entities.StageEntity();
        stageEntity.setRequestId(requestId);
        stageEntity.setStageId(getStageId());
        stageEntity.setLogInfo(logDir);
        stageEntity.setSkippable(skippable);
        stageEntity.setAutoSkipFailureSupported(supportsAutoSkipOnFailure);
        stageEntity.setRequestContext(requestContext);
        stageEntity.setHostRoleCommands(new java.util.ArrayList<>());
        stageEntity.setRoleSuccessCriterias(new java.util.ArrayList<>());
        stageEntity.setCommandParamsStage(commandParamsStage);
        if (null != hostParamsStage) {
            stageEntity.setHostParamsStage(hostParamsStage);
        }
        stageEntity.setCommandExecutionType(commandExecutionType);
        stageEntity.setStatus(status);
        stageEntity.setDisplayStatus(displayStatus);
        for (org.apache.ambari.server.Role role : successFactors.keySet()) {
            org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity roleSuccessCriteriaEntity = new org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity();
            roleSuccessCriteriaEntity.setRole(role);
            roleSuccessCriteriaEntity.setStage(stageEntity);
            roleSuccessCriteriaEntity.setSuccessFactor(successFactors.get(role).doubleValue());
            stageEntity.getRoleSuccessCriterias().add(roleSuccessCriteriaEntity);
        }
        return stageEntity;
    }

    void checkWrappersLoaded() {
        if (!wrappersLoaded) {
            synchronized(this) {
                if (!wrappersLoaded) {
                    loadExecutionCommandWrappers();
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    void loadExecutionCommandWrappers() {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommandEntry : hostRoleCommands.entrySet()) {
            java.lang.String hostname = hostRoleCommandEntry.getKey();
            java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> wrappers = new java.util.ArrayList<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandMap = hostRoleCommandEntry.getValue();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandEntry : roleCommandMap.entrySet()) {
                wrappers.add(roleCommandEntry.getValue().getExecutionCommandWrapper());
            }
            commandsToSend.put(hostname, wrappers);
        }
    }

    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getOrderedHostRoleCommands() {
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommandEntry : hostRoleCommands.entrySet()) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandEntry : hostRoleCommandEntry.getValue().entrySet()) {
                commands.add(roleCommandEntry.getValue());
            }
        }
        return commands;
    }

    public java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> getHostRolesInProgress() {
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> commandsToScheduleSet = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommandEntry : hostRoleCommands.entrySet()) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandEntry : hostRoleCommandEntry.getValue().entrySet()) {
                if (org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES.contains(roleCommandEntry.getValue().getStatus())) {
                    commandsToScheduleSet.add(new org.apache.ambari.server.metadata.RoleCommandPair(roleCommandEntry.getValue().getRole(), roleCommandEntry.getValue().getRoleCommand()));
                }
            }
        }
        return commandsToScheduleSet;
    }

    public java.lang.String getCommandParamsStage() {
        return commandParamsStage;
    }

    public void setCommandParamsStage(java.lang.String commandParamsStage) {
        this.commandParamsStage = commandParamsStage;
    }

    public java.lang.String getHostParamsStage() {
        return hostParamsStage;
    }

    public void setHostParamsStage(java.lang.String hostParamsStage) {
        this.hostParamsStage = hostParamsStage;
    }

    public org.apache.ambari.server.actionmanager.CommandExecutionType getCommandExecutionType() {
        return commandExecutionType;
    }

    public void setCommandExecutionType(org.apache.ambari.server.actionmanager.CommandExecutionType commandExecutionType) {
        this.commandExecutionType = commandExecutionType;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    public synchronized void setStageId(long stageId) {
        if (this.stageId != (-1)) {
            throw new java.lang.RuntimeException("Attempt to set stageId again! Not allowed.");
        }
        this.stageId = stageId;
        for (java.lang.String host : commandsToSend.keySet()) {
            for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper : commandsToSend.get(host)) {
                org.apache.ambari.server.agent.ExecutionCommand cmd = wrapper.getExecutionCommand();
                cmd.setRequestAndStage(requestId, stageId);
            }
        }
    }

    public synchronized long getStageId() {
        return stageId;
    }

    public java.lang.String getActionId() {
        return org.apache.ambari.server.utils.StageUtils.getActionId(requestId, getStageId());
    }

    private synchronized org.apache.ambari.server.actionmanager.ExecutionCommandWrapper addGenericExecutionCommand(java.lang.String clusterName, java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand command, org.apache.ambari.server.state.ServiceComponentHostEvent event, boolean retryAllowed, boolean autoSkipFailure) {
        boolean isHostRoleCommandAutoSkippable = (autoSkipFailure && supportsAutoSkipOnFailure) && skippable;
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = hostRoleCommandFactory.create(hostName, role, event, command, retryAllowed, isHostRoleCommandAutoSkippable);
        return addGenericExecutionCommand(clusterName, hostName, role, command, event, hrc);
    }

    private org.apache.ambari.server.actionmanager.ExecutionCommandWrapper addGenericExecutionCommand(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Host host, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand command, org.apache.ambari.server.state.ServiceComponentHostEvent event, boolean retryAllowed, boolean autoSkipFailure) {
        boolean isHostRoleCommandAutoSkippable = (autoSkipFailure && supportsAutoSkipOnFailure) && skippable;
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = hostRoleCommandFactory.create(host, role, event, command, retryAllowed, isHostRoleCommandAutoSkippable);
        return addGenericExecutionCommand(cluster.getClusterName(), host.getHostName(), role, command, event, hrc);
    }

    private org.apache.ambari.server.actionmanager.ExecutionCommandWrapper addGenericExecutionCommand(java.lang.String clusterName, java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand command, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.actionmanager.HostRoleCommand hrc) {
        org.apache.ambari.server.agent.ExecutionCommand cmd = new org.apache.ambari.server.agent.ExecutionCommand();
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = ecwFactory.createFromCommand(cmd);
        hrc.setExecutionCommandWrapper(wrapper);
        cmd.setClusterId(java.lang.Long.toString(clusterId));
        cmd.setHostname(hostName);
        cmd.setClusterName(clusterName);
        cmd.setRequestAndStage(requestId, stageId);
        cmd.setRole(role.name());
        cmd.setRoleCommand(command);
        cmd.setServiceName("");
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> hrcMap = hostRoleCommands.get(hostName);
        if (hrcMap == null) {
            hrcMap = new java.util.LinkedHashMap<>();
            hostRoleCommands.put(hostName, hrcMap);
        }
        if (hrcMap.get(role.toString()) != null) {
            throw new java.lang.RuntimeException((((("Setting the host role command second time for same stage: stage=" + getActionId()) + ", host=") + hostName) + ", role=") + role);
        }
        hrcMap.put(role.toString(), hrc);
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> execCmdList = commandsToSend.get(hostName);
        if (execCmdList == null) {
            execCmdList = new java.util.ArrayList<>();
            commandsToSend.put(hostName, execCmdList);
        }
        if (execCmdList.contains(wrapper)) {
            throw new java.lang.RuntimeException((((((("Setting the execution command second time for same stage: stage=" + getActionId()) + ", host=") + hostName) + ", role=") + role) + ", event=") + event);
        }
        execCmdList.add(wrapper);
        return wrapper;
    }

    public synchronized void addHostRoleExecutionCommand(java.lang.String host, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand command, org.apache.ambari.server.state.ServiceComponentHostEvent event, java.lang.String clusterName, java.lang.String serviceName, boolean retryAllowed, boolean autoSkipFailure) {
        boolean isHostRoleCommandAutoSkippable = (autoSkipFailure && supportsAutoSkipOnFailure) && skippable;
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper commandWrapper = addGenericExecutionCommand(clusterName, host, role, command, event, retryAllowed, isHostRoleCommandAutoSkippable);
        commandWrapper.getExecutionCommand().setServiceName(serviceName);
    }

    public synchronized void addHostRoleExecutionCommand(org.apache.ambari.server.state.Host host, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand command, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, boolean retryAllowed, boolean autoSkipFailure) {
        boolean isHostRoleCommandAutoSkippable = (autoSkipFailure && supportsAutoSkipOnFailure) && skippable;
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper commandWrapper = addGenericExecutionCommand(cluster, host, role, command, event, retryAllowed, isHostRoleCommandAutoSkippable);
        commandWrapper.getExecutionCommand().setServiceName(serviceName);
    }

    public synchronized void addServerActionCommand(java.lang.String actionName, @javax.annotation.Nullable
    java.lang.String userName, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand command, java.lang.String clusterName, org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent event, @javax.annotation.Nullable
    java.util.Map<java.lang.String, java.lang.String> commandParams, @javax.annotation.Nullable
    java.lang.String commandDetail, @javax.annotation.Nullable
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags, @javax.annotation.Nullable
    java.lang.Integer timeout, boolean retryAllowed, boolean autoSkipFailure) {
        boolean isHostRoleCommandAutoSkippable = (autoSkipFailure && supportsAutoSkipOnFailure) && skippable;
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper commandWrapper = addGenericExecutionCommand(clusterName, org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME, role, command, event, retryAllowed, isHostRoleCommandAutoSkippable);
        org.apache.ambari.server.agent.ExecutionCommand cmd = commandWrapper.getExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> cmdParams = new java.util.HashMap<>();
        if (commandParams != null) {
            cmdParams.putAll(commandParams);
        }
        if (timeout != null) {
            cmdParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, java.lang.Long.toString(timeout));
        }
        cmd.setCommandParams(cmdParams);
        cmd.setConfigurations(new java.util.TreeMap<>());
        java.util.Map<java.lang.String, java.lang.String> roleParams = new java.util.HashMap<>();
        roleParams.put(org.apache.ambari.server.serveraction.ServerAction.ACTION_NAME, actionName);
        if (userName != null) {
            roleParams.put(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME, userName);
        }
        cmd.setRoleParams(roleParams);
        if (commandDetail != null) {
            org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = getHostRoleCommand(org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME, role.toString());
            if (hostRoleCommand != null) {
                hostRoleCommand.setCommandDetail(commandDetail);
                hostRoleCommand.setCustomCommandName(actionName);
            }
        }
    }

    public synchronized void addCancelRequestCommand(java.util.List<java.lang.Long> cancelTargets, java.lang.String clusterName, java.lang.String hostName) {
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper commandWrapper = addGenericExecutionCommand(clusterName, hostName, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.ABORT, null, false, false);
        org.apache.ambari.server.agent.ExecutionCommand cmd = commandWrapper.getExecutionCommand();
        cmd.setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.CANCEL_COMMAND);
        org.springframework.util.Assert.notEmpty(cancelTargets, "Provided targets task Id are empty.");
        java.util.Map<java.lang.String, java.lang.String> roleParams = new java.util.HashMap<>();
        roleParams.put("cancelTaskIdTargets", org.apache.commons.lang.StringUtils.join(cancelTargets, ','));
        cmd.setRoleParams(roleParams);
    }

    public synchronized java.util.List<java.lang.String> getHosts() {
        return new java.util.ArrayList<>(hostRoleCommands.keySet());
    }

    synchronized float getSuccessFactor(org.apache.ambari.server.Role r) {
        java.lang.Float f = successFactors.get(r);
        if (f == null) {
            if (((r.equals(org.apache.ambari.server.Role.DATANODE) || r.equals(org.apache.ambari.server.Role.TASKTRACKER)) || r.equals(org.apache.ambari.server.Role.GANGLIA_MONITOR)) || r.equals(org.apache.ambari.server.Role.HBASE_REGIONSERVER)) {
                return ((float) (0.5));
            } else {
                return 1;
            }
        } else {
            return f;
        }
    }

    public synchronized void setSuccessFactors(java.util.Map<org.apache.ambari.server.Role, java.lang.Float> suc) {
        successFactors = suc;
    }

    public synchronized java.util.Map<org.apache.ambari.server.Role, java.lang.Float> getSuccessFactors() {
        return successFactors;
    }

    public long getRequestId() {
        return requestId;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public long getClusterId() {
        return clusterId;
    }

    public java.lang.String getRequestContext() {
        return requestContext;
    }

    public long getLastAttemptTime(java.lang.String hostname, java.lang.String role) {
        return hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).getLastAttemptTime();
    }

    public short getAttemptCount(java.lang.String hostname, java.lang.String role) {
        return hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).getAttemptCount();
    }

    public void incrementAttemptCount(java.lang.String hostname, java.lang.String role) {
        hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).incrementAttemptCount();
    }

    public void setLastAttemptTime(java.lang.String hostname, java.lang.String role, long t) {
        hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).setLastAttemptTime(t);
    }

    public org.apache.ambari.server.actionmanager.ExecutionCommandWrapper getExecutionCommandWrapper(java.lang.String hostname, java.lang.String role) {
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role);
        if (hrc != null) {
            return hrc.getExecutionCommandWrapper();
        } else {
            return null;
        }
    }

    public java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> getExecutionCommands(java.lang.String hostname) {
        checkWrappersLoaded();
        return commandsToSend.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname));
    }

    public long getStartTime(java.lang.String hostname, java.lang.String role) {
        return hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).getStartTime();
    }

    public void setStartTime(java.lang.String hostname, java.lang.String role, long startTime) {
        hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).setStartTime(startTime);
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getHostRoleStatus(java.lang.String hostname, java.lang.String role) {
        return hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).getStatus();
    }

    public void setHostRoleStatus(java.lang.String hostname, java.lang.String role, org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).setStatus(status);
    }

    public org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper getFsmEvent(java.lang.String hostname, java.lang.String roleStr) {
        return hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(roleStr).getEvent();
    }

    public void setExitCode(java.lang.String hostname, java.lang.String role, int exitCode) {
        hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).setExitCode(exitCode);
    }

    public int getExitCode(java.lang.String hostname, java.lang.String role) {
        return hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).getExitCode();
    }

    public void setStderr(java.lang.String hostname, java.lang.String role, java.lang.String stdErr) {
        hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).setStderr(stdErr);
    }

    public void setStdout(java.lang.String hostname, java.lang.String role, java.lang.String stdOut) {
        hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role).setStdout(stdOut);
    }

    public synchronized boolean isStageInProgress() {
        for (java.lang.String host : hostRoleCommands.keySet()) {
            for (java.lang.String role : hostRoleCommands.get(host).keySet()) {
                org.apache.ambari.server.actionmanager.HostRoleCommand hrc = hostRoleCommands.get(host).get(role);
                if (hrc == null) {
                    return false;
                }
                if ((hrc.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) || hrc.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) || hrc.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized boolean doesStageHaveHostRoleStatus(java.util.Set<org.apache.ambari.server.actionmanager.HostRoleStatus> statuses) {
        for (java.lang.String host : hostRoleCommands.keySet()) {
            for (java.lang.String role : hostRoleCommands.get(host).keySet()) {
                org.apache.ambari.server.actionmanager.HostRoleCommand hrc = hostRoleCommands.get(host).get(role);
                if (hrc == null) {
                    return false;
                }
                for (org.apache.ambari.server.actionmanager.HostRoleStatus status : statuses) {
                    if (hrc.getStatus().equals(status)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper>> getExecutionCommands() {
        checkWrappersLoaded();
        return commandsToSend;
    }

    public java.lang.String getLogDir() {
        return logDir;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> getHostRoleCommands() {
        return hostRoleCommands;
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommand getHostRoleCommand(long taskId) {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostEntry : hostRoleCommands.entrySet()) {
            java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> hostCommands = hostEntry.getValue();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> hostCommand : hostCommands.entrySet()) {
                org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostCommand.getValue();
                if ((null != hostRoleCommand) && (hostRoleCommand.getTaskId() == taskId)) {
                    return hostRoleCommand;
                }
            }
        }
        return null;
    }

    public synchronized void addExecutionCommandWrapper(org.apache.ambari.server.actionmanager.Stage origStage, java.lang.String hostname, org.apache.ambari.server.Role r) {
        hostname = org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname);
        java.lang.String role = r.toString();
        if (commandsToSend.get(hostname) == null) {
            commandsToSend.put(hostname, new java.util.ArrayList<>());
        }
        commandsToSend.get(hostname).add(origStage.getExecutionCommandWrapper(hostname, role));
        if (hostRoleCommands.get(hostname) == null) {
            hostRoleCommands.put(hostname, new java.util.LinkedHashMap<>());
        }
        hostRoleCommands.get(hostname).put(role, origStage.getHostRoleCommand(hostname, role));
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommand getHostRoleCommand(java.lang.String hostname, java.lang.String role) {
        return hostRoleCommands.get(org.apache.ambari.server.actionmanager.Stage.getSafeHost(hostname)).get(role);
    }

    public synchronized int getStageTimeout() {
        checkWrappersLoaded();
        if (stageTimeout == (-1)) {
            for (java.lang.String host : commandsToSend.keySet()) {
                int summaryTaskTimeoutForHost = 0;
                for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper command : commandsToSend.get(host)) {
                    java.util.Map<java.lang.String, java.lang.String> commandParams = command.getExecutionCommand().getCommandParams();
                    java.lang.String timeoutKey = org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT;
                    if ((commandParams != null) && commandParams.containsKey(timeoutKey)) {
                        java.lang.String timeoutStr = commandParams.get(timeoutKey);
                        long commandTimeout = java.lang.Long.parseLong(timeoutStr) * 1000;
                        summaryTaskTimeoutForHost += commandTimeout;
                    } else {
                        org.apache.ambari.server.actionmanager.Stage.LOG.error("Execution command has no timeout parameter" + command);
                    }
                }
                if (summaryTaskTimeoutForHost > stageTimeout) {
                    stageTimeout = summaryTaskTimeoutForHost;
                }
            }
        }
        return stageTimeout;
    }

    public boolean isSkippable() {
        return skippable;
    }

    public void setSkippable(boolean skippable) {
        this.skippable = skippable;
    }

    public boolean isAutoSkipOnFailureSupported() {
        return supportsAutoSkipOnFailure;
    }

    public void setAutoSkipFailureSupported(boolean supportsAutoSkipOnFailure) {
        this.supportsAutoSkipOnFailure = supportsAutoSkipOnFailure;
    }

    @java.lang.Override
    public synchronized java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("STAGE DESCRIPTION BEGIN\n");
        builder.append("requestId=").append(requestId).append("\n");
        builder.append("stageId=").append(stageId).append("\n");
        builder.append("clusterName=").append(clusterName).append("\n");
        builder.append("logDir=").append(logDir).append("\n");
        builder.append("requestContext=").append(requestContext).append("\n");
        builder.append("commandParamsStage=").append(commandParamsStage).append("\n");
        builder.append("hostParamsStage=").append(hostParamsStage).append("\n");
        builder.append("status=").append(status).append("\n");
        builder.append("displayStatus=").append(displayStatus).append("\n");
        builder.append("Success Factors:\n");
        for (org.apache.ambari.server.Role r : successFactors.keySet()) {
            builder.append("  role: ").append(r).append(", factor: ").append(successFactors.get(r)).append("\n");
        }
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : getOrderedHostRoleCommands()) {
            builder.append("HOST: ").append(hostRoleCommand.getHostName()).append(" :\n");
            builder.append(hostRoleCommand.getExecutionCommandWrapper().getJson());
            builder.append("\n");
            builder.append(hostRoleCommand);
            builder.append("\n");
        }
        builder.append("STAGE DESCRIPTION END\n");
        return builder.toString();
    }

    private static java.lang.String getSafeHost(java.lang.String hostname) {
        return null == hostname ? org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME : hostname;
    }
}