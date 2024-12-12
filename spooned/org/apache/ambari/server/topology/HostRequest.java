package org.apache.ambari.server.topology;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START;
import static org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY;
import static org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY;
public class HostRequest implements java.lang.Comparable<org.apache.ambari.server.topology.HostRequest> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.HostRequest.class);

    private long requestId;

    private java.lang.String blueprint;

    private org.apache.ambari.server.topology.HostGroup hostGroup;

    private java.lang.String hostgroupName;

    private org.apache.ambari.server.controller.spi.Predicate predicate;

    private java.lang.String hostname = null;

    private long clusterId;

    private boolean containsMaster;

    private final long id;

    private boolean isOutstanding = true;

    private final boolean skipFailure;

    private org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    private java.lang.String statusMessage;

    private java.util.Map<org.apache.ambari.server.topology.tasks.TopologyTask, java.util.Map<java.lang.String, java.lang.Long>> logicalTaskMap = new java.util.HashMap<>();

    java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> logicalTasks = new java.util.HashMap<>();

    private java.util.Map<java.lang.Long, java.lang.Long> physicalTasks = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.List<org.apache.ambari.server.topology.tasks.TopologyHostTask> topologyTasks = new java.util.ArrayList<>();

    private org.apache.ambari.server.topology.ClusterTopology topology;

    private static org.apache.ambari.server.api.predicate.PredicateCompiler predicateCompiler = new org.apache.ambari.server.api.predicate.PredicateCompiler();

    public HostRequest(long requestId, long id, long clusterId, java.lang.String hostname, java.lang.String blueprintName, org.apache.ambari.server.topology.HostGroup hostGroup, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.topology.ClusterTopology topology, boolean skipFailure) {
        this.requestId = requestId;
        this.id = id;
        this.clusterId = clusterId;
        blueprint = blueprintName;
        this.hostGroup = hostGroup;
        hostgroupName = hostGroup.getName();
        this.predicate = predicate;
        containsMaster = hostGroup.containsMasterComponent();
        this.topology = topology;
        this.skipFailure = skipFailure;
        createTasks(this.skipFailure);
        org.apache.ambari.server.topology.HostRequest.LOG.info("HostRequest: Created request for host: " + (hostname == null ? "Host Assignment Pending" : hostname));
    }

    public HostRequest(long requestId, long id, java.lang.String predicate, org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.orm.entities.TopologyHostRequestEntity entity, boolean skipFailure) {
        this.requestId = requestId;
        this.id = id;
        clusterId = topology.getClusterId();
        blueprint = topology.getBlueprint().getName();
        hostgroupName = entity.getTopologyHostGroupEntity().getName();
        hostGroup = topology.getBlueprint().getHostGroup(hostgroupName);
        hostname = entity.getHostName();
        setStatus(entity.getStatus());
        statusMessage = entity.getStatusMessage();
        this.predicate = toPredicate(predicate);
        containsMaster = hostGroup.containsMasterComponent();
        this.topology = topology;
        this.skipFailure = skipFailure;
        createTasksForReplay(entity);
        isOutstanding = (hostname == null) || (!topology.getAmbariContext().isHostRegisteredWithCluster(clusterId, hostname));
        org.apache.ambari.server.topology.HostRequest.LOG.info("HostRequest: Successfully recovered host request for host: " + (hostname == null ? "Host Assignment Pending" : hostname));
    }

    void markHostRequestFailed(org.apache.ambari.server.actionmanager.HostRoleStatus status, java.lang.Throwable cause, org.apache.ambari.server.topology.PersistedState persistedState) {
        java.lang.String errorMessage = org.apache.commons.lang.StringUtils.substringBefore(com.google.common.base.Throwables.getRootCause(cause).getMessage(), "\n");
        org.apache.ambari.server.topology.HostRequest.LOG.info("HostRequest: marking host request {} for {} as {} due to {}", id, hostname, status, errorMessage);
        abortPendingTasks();
        setStatus(status);
        setStatusMessage(errorMessage);
        persistedState.setHostRequestStatus(id, status, errorMessage);
    }

    public synchronized org.apache.ambari.server.topology.HostOfferResponse offer(org.apache.ambari.server.state.Host host) {
        if (!isOutstanding) {
            return org.apache.ambari.server.topology.HostOfferResponse.DECLINED_DUE_TO_DONE;
        }
        if (matchesHost(host)) {
            isOutstanding = false;
            hostname = host.getHostName();
            setHostOnTasks(host);
            return org.apache.ambari.server.topology.HostOfferResponse.createAcceptedResponse(id, hostGroup.getName(), topologyTasks);
        } else {
            return org.apache.ambari.server.topology.HostOfferResponse.DECLINED_DUE_TO_PREDICATE;
        }
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void setStatusMessage(java.lang.String errorMessage) {
        this.statusMessage = errorMessage;
    }

    public com.google.common.base.Optional<java.lang.String> getStatusMessage() {
        return com.google.common.base.Optional.fromNullable(statusMessage);
    }

    public void setHostName(java.lang.String hostName) {
        hostname = hostName;
    }

    public long getRequestId() {
        return requestId;
    }

    public long getClusterId() {
        return clusterId;
    }

    public java.lang.String getBlueprint() {
        return blueprint;
    }

    public org.apache.ambari.server.topology.HostGroup getHostGroup() {
        return hostGroup;
    }

    public java.lang.String getHostgroupName() {
        return hostgroupName;
    }

    public org.apache.ambari.server.controller.spi.Predicate getPredicate() {
        return predicate;
    }

    public boolean isCompleted() {
        return !isOutstanding;
    }

    public boolean shouldSkipFailure() {
        return skipFailure;
    }

    private void createTasks(boolean skipFailure) {
        topologyTasks.add(new org.apache.ambari.server.topology.tasks.PersistHostResourcesTask(topology, this));
        topologyTasks.add(new org.apache.ambari.server.topology.tasks.RegisterWithConfigGroupTask(topology, this));
        org.apache.ambari.server.topology.tasks.InstallHostTask installTask = new org.apache.ambari.server.topology.tasks.InstallHostTask(topology, this, skipFailure);
        topologyTasks.add(installTask);
        logicalTaskMap.put(installTask, new java.util.HashMap<>());
        boolean skipStartTaskCreate = topology.getProvisionAction().equals(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY);
        boolean skipInstallTaskCreate = topology.getProvisionAction().equals(org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY);
        org.apache.ambari.server.topology.tasks.StartHostTask startTask = null;
        if (!skipStartTaskCreate) {
            startTask = new org.apache.ambari.server.topology.tasks.StartHostTask(topology, this, skipFailure);
            topologyTasks.add(startTask);
            logicalTaskMap.put(startTask, new java.util.HashMap<>());
        } else {
            org.apache.ambari.server.topology.HostRequest.LOG.info("Skipping Start task creation since provision action = " + topology.getProvisionAction());
        }
        org.apache.ambari.server.topology.HostGroup hostGroup = getHostGroup();
        java.util.Collection<java.lang.String> startOnlyComponents = hostGroup.getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY);
        java.util.Collection<java.lang.String> installOnlyComponents = hostGroup.getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY);
        java.util.Collection<java.lang.String> installAndStartComponents = hostGroup.getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START);
        for (java.lang.String component : hostGroup.getComponentNames()) {
            if ((component == null) || component.equals("AMBARI_SERVER")) {
                org.apache.ambari.server.topology.HostRequest.LOG.info("Skipping component {} when creating request\n", component);
                continue;
            }
            java.lang.String hostName = (getHostName() != null) ? getHostName() : "PENDING HOST ASSIGNMENT : HOSTGROUP=" + getHostgroupName();
            org.apache.ambari.server.topology.AmbariContext context = topology.getAmbariContext();
            org.apache.ambari.server.controller.internal.Stack stack = hostGroup.getStack();
            if (startOnlyComponents.contains(component) || ((((skipInstallTaskCreate && (!installOnlyComponents.contains(component))) && (!installAndStartComponents.contains(component))) && (stack != null)) && (!stack.getComponentInfo(component).isClient()))) {
                org.apache.ambari.server.topology.HostRequest.LOG.info("Skipping create of INSTALL task for {} on {}.", component, hostName);
            } else {
                org.apache.ambari.server.actionmanager.HostRoleCommand logicalInstallTask = context.createAmbariTask(getRequestId(), id, component, hostName, org.apache.ambari.server.topology.AmbariContext.TaskType.INSTALL, skipFailure);
                logicalTasks.put(logicalInstallTask.getTaskId(), logicalInstallTask);
                logicalTaskMap.get(installTask).put(component, logicalInstallTask.getTaskId());
            }
            if ((installOnlyComponents.contains(component) || skipStartTaskCreate) || ((stack != null) && stack.getComponentInfo(component).isClient())) {
                org.apache.ambari.server.topology.HostRequest.LOG.info("Skipping create of START task for {} on {}.", component, hostName);
            } else {
                org.apache.ambari.server.actionmanager.HostRoleCommand logicalStartTask = context.createAmbariTask(getRequestId(), id, component, hostName, org.apache.ambari.server.topology.AmbariContext.TaskType.START, skipFailure);
                logicalTasks.put(logicalStartTask.getTaskId(), logicalStartTask);
                logicalTaskMap.get(startTask).put(component, logicalStartTask.getTaskId());
            }
        }
    }

    private void createTasksForReplay(org.apache.ambari.server.orm.entities.TopologyHostRequestEntity entity) {
        topologyTasks.add(new org.apache.ambari.server.topology.tasks.PersistHostResourcesTask(topology, this));
        topologyTasks.add(new org.apache.ambari.server.topology.tasks.RegisterWithConfigGroupTask(topology, this));
        org.apache.ambari.server.topology.tasks.InstallHostTask installTask = new org.apache.ambari.server.topology.tasks.InstallHostTask(topology, this, skipFailure);
        topologyTasks.add(installTask);
        logicalTaskMap.put(installTask, new java.util.HashMap<>());
        boolean skipStartTaskCreate = topology.getProvisionAction().equals(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY);
        if (!skipStartTaskCreate) {
            org.apache.ambari.server.topology.tasks.StartHostTask startTask = new org.apache.ambari.server.topology.tasks.StartHostTask(topology, this, skipFailure);
            topologyTasks.add(startTask);
            logicalTaskMap.put(startTask, new java.util.HashMap<>());
        }
        org.apache.ambari.server.topology.AmbariContext ambariContext = topology.getAmbariContext();
        for (org.apache.ambari.server.orm.entities.TopologyHostTaskEntity topologyTaskEntity : entity.getTopologyHostTaskEntities()) {
            org.apache.ambari.server.topology.tasks.TopologyTask.Type taskType = org.apache.ambari.server.topology.tasks.TopologyTask.Type.valueOf(topologyTaskEntity.getType());
            for (org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity logicalTaskEntity : topologyTaskEntity.getTopologyLogicalTaskEntities()) {
                java.lang.Long logicalTaskId = logicalTaskEntity.getId();
                java.lang.String component = logicalTaskEntity.getComponentName();
                org.apache.ambari.server.topology.AmbariContext.TaskType logicalTaskType = org.apache.ambari.server.topology.HostRequest.getLogicalTaskType(taskType);
                org.apache.ambari.server.actionmanager.HostRoleCommand task = ambariContext.createAmbariTask(logicalTaskId, getRequestId(), id, component, entity.getHostName(), logicalTaskType, skipFailure);
                logicalTasks.put(logicalTaskId, task);
                java.lang.Long physicalTaskId = logicalTaskEntity.getPhysicalTaskId();
                if (physicalTaskId != null) {
                    registerPhysicalTaskId(logicalTaskId, physicalTaskId);
                }
                for (org.apache.ambari.server.topology.tasks.TopologyTask topologyTask : topologyTasks) {
                    if (taskType == topologyTask.getType()) {
                        logicalTaskMap.get(topologyTask).put(component, logicalTaskId);
                    }
                }
            }
        }
    }

    private static org.apache.ambari.server.topology.AmbariContext.TaskType getLogicalTaskType(org.apache.ambari.server.topology.tasks.TopologyTask.Type topologyTaskType) {
        return topologyTaskType == org.apache.ambari.server.topology.tasks.TopologyTask.Type.INSTALL ? org.apache.ambari.server.topology.AmbariContext.TaskType.INSTALL : org.apache.ambari.server.topology.AmbariContext.TaskType.START;
    }

    private void setHostOnTasks(org.apache.ambari.server.state.Host host) {
        for (org.apache.ambari.server.actionmanager.HostRoleCommand task : getLogicalTasks()) {
            task.setHost(host.getHostId(), host.getHostName());
        }
    }

    public java.util.List<org.apache.ambari.server.topology.tasks.TopologyHostTask> getTopologyTasks() {
        return topologyTasks;
    }

    public java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> getLogicalTasks() {
        for (org.apache.ambari.server.actionmanager.HostRoleCommand logicalTask : logicalTasks.values()) {
            java.lang.String commandDetail = logicalTask.getCommandDetail();
            if (((commandDetail != null) && commandDetail.contains("null")) && (hostname != null)) {
                logicalTask.setCommandDetail(commandDetail.replace("null", hostname));
            }
            java.lang.Long physicalTaskId = physicalTasks.get(logicalTask.getTaskId());
            if (physicalTaskId != null) {
                org.apache.ambari.server.actionmanager.HostRoleCommand physicalTask = topology.getAmbariContext().getPhysicalTask(physicalTaskId);
                if (physicalTask != null) {
                    logicalTask.setStatus(physicalTask.getStatus());
                    logicalTask.setCommandDetail(physicalTask.getCommandDetail());
                    logicalTask.setCustomCommandName(physicalTask.getCustomCommandName());
                    logicalTask.setStartTime(physicalTask.getStartTime());
                    logicalTask.setOriginalStartTime(physicalTask.getOriginalStartTime());
                    logicalTask.setEndTime(physicalTask.getEndTime());
                    logicalTask.setErrorLog(physicalTask.getErrorLog());
                    logicalTask.setExitCode(physicalTask.getExitCode());
                    logicalTask.setExecutionCommandWrapper(physicalTask.getExecutionCommandWrapper());
                    logicalTask.setLastAttemptTime(physicalTask.getLastAttemptTime());
                    logicalTask.setOutputLog(physicalTask.getOutputLog());
                    logicalTask.setStderr(physicalTask.getStderr());
                    logicalTask.setStdout(physicalTask.getStdout());
                    logicalTask.setStructuredOut(physicalTask.getStructuredOut());
                }
            }
            if ((logicalTask.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) && (status != org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                logicalTask.setStatus(status);
            }
        }
        return logicalTasks.values();
    }

    public java.util.Map<java.lang.String, java.lang.Long> getLogicalTasksForTopologyTask(org.apache.ambari.server.topology.tasks.TopologyTask topologyTask) {
        return new java.util.HashMap<>(logicalTaskMap.get(topologyTask));
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommand getLogicalTask(long logicalTaskId) {
        return logicalTasks.get(logicalTaskId);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> getTaskEntities() {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> taskEntities = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.HostRoleCommand task : logicalTasks.values()) {
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = task.constructNewPersistenceEntity();
            entity.setOutputLog(task.getOutputLog());
            entity.setErrorLog(task.errorLog);
            java.lang.Long physicalTaskId = physicalTasks.get(task.getTaskId());
            if (physicalTaskId != null) {
                org.apache.ambari.server.actionmanager.HostRoleCommand physicalTask = topology.getAmbariContext().getPhysicalTask(physicalTaskId);
                if (physicalTask != null) {
                    entity.setStatus(physicalTask.getStatus());
                    entity.setCommandDetail(physicalTask.getCommandDetail());
                    entity.setCustomCommandName(physicalTask.getCustomCommandName());
                    entity.setStartTime(physicalTask.getStartTime());
                    entity.setOriginalStartTime(physicalTask.getOriginalStartTime());
                    entity.setEndTime(physicalTask.getEndTime());
                    entity.setErrorLog(physicalTask.getErrorLog());
                    entity.setExitcode(physicalTask.getExitCode());
                    entity.setLastAttemptTime(physicalTask.getLastAttemptTime());
                    entity.setOutputLog(physicalTask.getOutputLog());
                    entity.setStdError(physicalTask.getStderr().getBytes());
                    entity.setStdOut(physicalTask.getStdout().getBytes());
                    entity.setStructuredOut(physicalTask.getStructuredOut().getBytes());
                }
            }
            taskEntities.add(entity);
        }
        return taskEntities;
    }

    public boolean containsMaster() {
        return containsMaster;
    }

    public boolean matchesHost(org.apache.ambari.server.state.Host host) {
        return hostname != null ? host.getHostName().equals(hostname) : (predicate == null) || predicate.evaluate(new org.apache.ambari.server.topology.HostRequest.HostResourceAdapter(host));
    }

    public java.lang.String getHostName() {
        return hostname;
    }

    public long getId() {
        return id;
    }

    public long getStageId() {
        return getId();
    }

    public java.lang.Long getPhysicalTaskId(long logicalTaskId) {
        return physicalTasks.get(logicalTaskId);
    }

    public java.util.Map<java.lang.Long, java.lang.Long> getPhysicalTaskMapping() {
        return new java.util.concurrent.ConcurrentHashMap<>(physicalTasks);
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.topology.HostRequest other) {
        if (containsMaster()) {
            return other.containsMaster() ? hashCode() - other.hashCode() : -1;
        } else if (other.containsMaster()) {
            return 1;
        } else {
            return hashCode() - other.hashCode();
        }
    }

    public void registerPhysicalTaskId(long logicalTaskId, long physicalTaskId) {
        physicalTasks.put(logicalTaskId, physicalTaskId);
        topology.getAmbariContext().getPersistedTopologyState().registerPhysicalTask(logicalTaskId, physicalTaskId);
        getLogicalTask(logicalTaskId).incrementAttemptCount();
    }

    public void abortPendingTasks() {
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : getLogicalTasks()) {
            if (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
            }
        }
    }

    private org.apache.ambari.server.controller.spi.Predicate toPredicate(java.lang.String predicate) {
        org.apache.ambari.server.controller.spi.Predicate compiledPredicate = null;
        try {
            if ((predicate != null) && (!predicate.isEmpty())) {
                compiledPredicate = org.apache.ambari.server.topology.HostRequest.predicateCompiler.compile(predicate);
            }
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
            org.apache.ambari.server.topology.HostRequest.LOG.error("Unable to compile predicate for host request: " + e, e);
        }
        return compiledPredicate;
    }

    private class HostResourceAdapter implements org.apache.ambari.server.controller.spi.Resource {
        org.apache.ambari.server.controller.spi.Resource hostResource;

        public HostResourceAdapter(org.apache.ambari.server.state.Host host) {
            buildPropertyMap(host);
        }

        @java.lang.Override
        public java.lang.Object getPropertyValue(java.lang.String id) {
            return hostResource.getPropertyValue(id);
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> getPropertiesMap() {
            return hostResource.getPropertiesMap();
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.Resource.Type getType() {
            return org.apache.ambari.server.controller.spi.Resource.Type.Host;
        }

        @java.lang.Override
        public void addCategory(java.lang.String id) {
        }

        @java.lang.Override
        public void setProperty(java.lang.String id, java.lang.Object value) {
        }

        private void buildPropertyMap(org.apache.ambari.server.state.Host host) {
            hostResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, host.getHostName());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PUBLIC_NAME_PROPERTY_ID, host.getPublicHostName());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_IP_PROPERTY_ID, host.getIPv4());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_TOTAL_MEM_PROPERTY_ID, host.getTotalMemBytes());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CPU_COUNT_PROPERTY_ID, ((long) (host.getCpuCount())));
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PHYSICAL_CPU_COUNT_PROPERTY_ID, ((long) (host.getPhCpuCount())));
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_ARCH_PROPERTY_ID, host.getOsArch());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_TYPE_PROPERTY_ID, host.getOsType());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_FAMILY_PROPERTY_ID, host.getOsFamily());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID, host.getRackInfo());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_HEARTBEAT_TIME_PROPERTY_ID, host.getLastHeartbeatTime());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_AGENT_ENV_PROPERTY_ID, host.getLastAgentEnv());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_REGISTRATION_TIME_PROPERTY_ID, host.getLastRegistrationTime());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID, host.getStatus());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_HEALTH_REPORT_PROPERTY_ID, host.getHealthStatus().getHealthReport());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_DISK_INFO_PROPERTY_ID, host.getDisksInfo());
            hostResource.setProperty(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_STATE_PROPERTY_ID, host.getState());
        }
    }
}