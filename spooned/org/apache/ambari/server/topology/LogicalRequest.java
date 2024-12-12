package org.apache.ambari.server.topology;
import org.apache.commons.lang.StringUtils;
public class LogicalRequest extends org.apache.ambari.server.actionmanager.Request {
    private final java.util.Collection<org.apache.ambari.server.topology.HostRequest> allHostRequests = new java.util.ArrayList<>();

    private final java.util.Collection<org.apache.ambari.server.topology.HostRequest> outstandingHostRequests = new java.util.TreeSet<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostRequest> requestsWithReservedHosts = new java.util.HashMap<>();

    private final org.apache.ambari.server.topology.ClusterTopology topology;

    private static org.apache.ambari.server.controller.AmbariManagementController controller;

    private static final java.util.concurrent.atomic.AtomicLong hostIdCounter = new java.util.concurrent.atomic.AtomicLong(1);

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.LogicalRequest.class);

    public LogicalRequest(java.lang.Long id, org.apache.ambari.server.topology.TopologyRequest request, org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.AmbariException {
        super(id, topology.getClusterId(), org.apache.ambari.server.topology.LogicalRequest.getController().getClusters());
        setRequestContext(java.lang.String.format("Logical Request: %s", request.getDescription()));
        this.topology = topology;
        createHostRequests(request, topology);
    }

    public LogicalRequest(java.lang.Long id, org.apache.ambari.server.topology.TopologyRequest request, org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity requestEntity) throws org.apache.ambari.server.AmbariException {
        super(id, topology.getClusterId(), org.apache.ambari.server.topology.LogicalRequest.getController().getClusters());
        setRequestContext(java.lang.String.format("Logical Request: %s", request.getDescription()));
        this.topology = topology;
        createHostRequests(topology, requestEntity);
    }

    public org.apache.ambari.server.topology.HostOfferResponse offer(org.apache.ambari.server.state.Host host) {
        synchronized(requestsWithReservedHosts) {
            org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer: attempting to match a request to a request for a reserved host to hostname = {}", host.getHostName());
            org.apache.ambari.server.topology.HostRequest hostRequest = requestsWithReservedHosts.remove(host.getHostName());
            if (hostRequest != null) {
                org.apache.ambari.server.topology.HostOfferResponse response = hostRequest.offer(host);
                if (response.getAnswer() != org.apache.ambari.server.topology.HostOfferResponse.Answer.ACCEPTED) {
                    throw new java.lang.RuntimeException("LogicalRequest declined host offer of explicitly requested host: " + host.getHostName());
                } else {
                    org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer: request mapping ACCEPTED for host = {}", host.getHostName());
                }
                org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer returning response, reservedHost list size = {}", requestsWithReservedHosts.size());
                return response;
            }
        }
        boolean predicateRejected = false;
        synchronized(outstandingHostRequests) {
            java.util.Iterator<org.apache.ambari.server.topology.HostRequest> hostRequestIterator = outstandingHostRequests.iterator();
            while (hostRequestIterator.hasNext()) {
                org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer: attempting to match a request to a request for a non-reserved host to hostname = {}", host.getHostName());
                org.apache.ambari.server.topology.HostOfferResponse response = hostRequestIterator.next().offer(host);
                switch (response.getAnswer()) {
                    case ACCEPTED :
                        hostRequestIterator.remove();
                        org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer: host request matched to non-reserved host, hostname = {}, host request has been removed from list", host.getHostName());
                        return response;
                    case DECLINED_DONE :
                        hostRequestIterator.remove();
                        org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer: host request returned DECLINED_DONE for hostname = {}, host request has been removed from list", host.getHostName());
                        break;
                    case DECLINED_PREDICATE :
                        org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer: host request returned DECLINED_PREDICATE for hostname = {}", host.getHostName());
                        predicateRejected = true;
                        break;
                }
            } 
            org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.offer: outstandingHost request list size = " + outstandingHostRequests.size());
        }
        return predicateRejected || (!requestsWithReservedHosts.isEmpty()) ? org.apache.ambari.server.topology.HostOfferResponse.DECLINED_DUE_TO_PREDICATE : org.apache.ambari.server.topology.HostOfferResponse.DECLINED_DUE_TO_DONE;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getCommands() {
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.HostRequest hostRequest : allHostRequests) {
            commands.addAll(new java.util.ArrayList<>(hostRequest.getLogicalTasks()));
        }
        return commands;
    }

    public java.util.Collection<java.lang.String> getReservedHosts() {
        return requestsWithReservedHosts.keySet();
    }

    public boolean hasPendingHostRequests() {
        return !(requestsWithReservedHosts.isEmpty() && outstandingHostRequests.isEmpty());
    }

    public java.util.Collection<org.apache.ambari.server.topology.HostRequest> getCompletedHostRequests() {
        java.util.Collection<org.apache.ambari.server.topology.HostRequest> completedHostRequests = new java.util.ArrayList<>(allHostRequests);
        completedHostRequests.removeAll(outstandingHostRequests);
        completedHostRequests.removeAll(requestsWithReservedHosts.values());
        return completedHostRequests;
    }

    public int getPendingHostRequestCount() {
        return outstandingHostRequests.size() + requestsWithReservedHosts.size();
    }

    public java.util.Collection<org.apache.ambari.server.topology.HostRequest> getHostRequests() {
        return new java.util.ArrayList<>(allHostRequests);
    }

    public java.util.Collection<org.apache.ambari.server.topology.HostRequest> removePendingHostRequests(java.lang.String hostGroupName) {
        java.util.Collection<org.apache.ambari.server.topology.HostRequest> pendingHostRequests = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.HostRequest hostRequest : outstandingHostRequests) {
            if ((hostGroupName == null) || hostRequest.getHostgroupName().equals(hostGroupName)) {
                pendingHostRequests.add(hostRequest);
            }
        }
        if (hostGroupName == null) {
            outstandingHostRequests.clear();
        } else {
            outstandingHostRequests.removeAll(pendingHostRequests);
        }
        java.util.Collection<java.lang.String> pendingReservedHostNames = new java.util.ArrayList<>();
        for (java.lang.String reservedHostName : requestsWithReservedHosts.keySet()) {
            org.apache.ambari.server.topology.HostRequest hostRequest = requestsWithReservedHosts.get(reservedHostName);
            if ((hostGroupName == null) || hostRequest.getHostgroupName().equals(hostGroupName)) {
                pendingHostRequests.add(hostRequest);
                pendingReservedHostNames.add(reservedHostName);
            }
        }
        requestsWithReservedHosts.keySet().removeAll(pendingReservedHostNames);
        allHostRequests.removeAll(pendingHostRequests);
        return pendingHostRequests;
    }

    public java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> getProjectedTopology() {
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> hostComponentMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.topology.HostRequest hostRequest : allHostRequests) {
            org.apache.ambari.server.topology.HostGroup hostGroup = hostRequest.getHostGroup();
            for (java.lang.String host : topology.getHostGroupInfo().get(hostGroup.getName()).getHostNames()) {
                java.util.Collection<java.lang.String> hostComponents = hostComponentMap.get(host);
                if (hostComponents == null) {
                    hostComponents = new java.util.HashSet<>();
                    hostComponentMap.put(host, hostComponents);
                }
                hostComponents.addAll(hostGroup.getComponentNames());
            }
        }
        return hostComponentMap;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> getStageEntities() {
        java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stages = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.HostRequest hostRequest : allHostRequests) {
            org.apache.ambari.server.orm.entities.StageEntity stage = new org.apache.ambari.server.orm.entities.StageEntity();
            stage.setStageId(hostRequest.getStageId());
            stage.setRequestContext(getRequestContext());
            stage.setRequestId(getRequestId());
            stage.setClusterId(getClusterId());
            boolean skipFailure = hostRequest.shouldSkipFailure();
            stage.setSkippable(skipFailure);
            stage.setAutoSkipFailureSupported(skipFailure);
            stage.setHostRoleCommands(hostRequest.getTaskEntities());
            stages.add(stage);
        }
        return stages;
    }

    public org.apache.ambari.server.controller.RequestStatusResponse getRequestStatus() {
        org.apache.ambari.server.controller.RequestStatusResponse requestStatus = new org.apache.ambari.server.controller.RequestStatusResponse(getRequestId());
        requestStatus.setRequestContext(getRequestContext());
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> shortTasks = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.HostRoleCommand task : getCommands()) {
            shortTasks.add(new org.apache.ambari.server.controller.ShortTaskStatus(task));
        }
        requestStatus.setTasks(shortTasks);
        return requestStatus;
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> getStageSummaries() {
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summaryMap = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand>> stageTasksMap = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, java.lang.Long> taskToStageMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.topology.HostRequest hostRequest : getHostRequests()) {
            java.util.Map<java.lang.Long, java.lang.Long> physicalTaskMapping = hostRequest.getPhysicalTaskMapping();
            java.util.Collection<java.lang.Long> stageTasks = physicalTaskMapping.values();
            for (java.lang.Long stageTask : stageTasks) {
                taskToStageMap.put(stageTask, hostRequest.getStageId());
            }
        }
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> physicalTasks = topology.getAmbariContext().getPhysicalTasks(taskToStageMap.keySet());
        for (org.apache.ambari.server.actionmanager.HostRoleCommand physicalTask : physicalTasks) {
            java.lang.Long stageId = taskToStageMap.get(physicalTask.getTaskId());
            java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> stageTasks = stageTasksMap.get(stageId);
            if (stageTasks == null) {
                stageTasks = new java.util.ArrayList<>();
                stageTasksMap.put(stageId, stageTasks);
            }
            stageTasks.add(physicalTask);
        }
        for (java.lang.Long stageId : stageTasksMap.keySet()) {
            int aborted = 0;
            int completed = 0;
            int failed = 0;
            int holding = 0;
            int holdingFailed = 0;
            int holdingTimedout = 0;
            int inProgress = 0;
            int pending = 0;
            int queued = 0;
            int timedout = 0;
            int skippedFailed = 0;
            for (org.apache.ambari.server.actionmanager.HostRoleCommand task : stageTasksMap.get(stageId)) {
                org.apache.ambari.server.actionmanager.HostRoleStatus taskStatus = task.getStatus();
                switch (taskStatus) {
                    case ABORTED :
                        aborted += 1;
                        break;
                    case COMPLETED :
                        completed += 1;
                        break;
                    case FAILED :
                        failed += 1;
                        break;
                    case HOLDING :
                        holding += 1;
                        break;
                    case HOLDING_FAILED :
                        holdingFailed += 1;
                        break;
                    case HOLDING_TIMEDOUT :
                        holdingTimedout += 1;
                        break;
                    case IN_PROGRESS :
                        inProgress += 1;
                        break;
                    case PENDING :
                        pending += 1;
                        break;
                    case QUEUED :
                        queued += 1;
                        break;
                    case TIMEDOUT :
                        timedout += 1;
                        break;
                    case SKIPPED_FAILED :
                        skippedFailed += 1;
                        break;
                    default :
                        java.lang.System.out.println("Unexpected status when creating stage summaries: " + taskStatus);
                }
            }
            org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO stageSummary = new org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO(0, 0, 0, stageId, aborted, completed, failed, holding, holdingFailed, holdingTimedout, inProgress, pending, queued, timedout, skippedFailed);
            summaryMap.put(stageId, stageSummary);
        }
        return summaryMap;
    }

    public java.util.Set<org.apache.ambari.server.topology.HostRequest> removeHostRequestByHostName(java.lang.String hostName) {
        java.util.Set<org.apache.ambari.server.topology.HostRequest> removed = new java.util.HashSet<>();
        synchronized(requestsWithReservedHosts) {
            synchronized(outstandingHostRequests) {
                requestsWithReservedHosts.remove(hostName);
                java.util.Iterator<org.apache.ambari.server.topology.HostRequest> hostRequestIterator = outstandingHostRequests.iterator();
                while (hostRequestIterator.hasNext()) {
                    org.apache.ambari.server.topology.HostRequest hostRequest = hostRequestIterator.next();
                    if (java.util.Objects.equals(hostRequest.getHostName(), hostName)) {
                        hostRequestIterator.remove();
                        removed.add(hostRequest);
                        break;
                    }
                } 
                java.util.Iterator<org.apache.ambari.server.topology.HostRequest> allHostRequestIterator = allHostRequests.iterator();
                while (allHostRequestIterator.hasNext()) {
                    org.apache.ambari.server.topology.HostRequest hostRequest = allHostRequestIterator.next();
                    if (java.util.Objects.equals(hostRequest.getHostName(), hostName)) {
                        allHostRequestIterator.remove();
                        removed.add(hostRequest);
                        break;
                    }
                } 
            }
        }
        return removed;
    }

    public boolean isFinished() {
        for (org.apache.ambari.server.controller.ShortTaskStatus ts : getRequestStatus().getTasks()) {
            if (!org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(ts.getStatus()).isCompletedState()) {
                return false;
            }
        }
        return true;
    }

    public boolean isSuccessful() {
        for (org.apache.ambari.server.controller.ShortTaskStatus ts : getRequestStatus().getTasks()) {
            if (org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(ts.getStatus()) != org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    public com.google.common.base.Optional<java.lang.String> getFailureReason() {
        for (org.apache.ambari.server.topology.HostRequest request : getHostRequests()) {
            com.google.common.base.Optional<java.lang.String> failureReason = request.getStatusMessage();
            if (failureReason.isPresent()) {
                return failureReason;
            }
        }
        return com.google.common.base.Optional.absent();
    }

    private void createHostRequests(org.apache.ambari.server.topology.TopologyRequest request, org.apache.ambari.server.topology.ClusterTopology topology) {
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = request.getHostGroupInfo();
        org.apache.ambari.server.topology.Blueprint blueprint = topology.getBlueprint();
        boolean skipFailure = topology.getBlueprint().shouldSkipFailure();
        for (org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo : hostGroupInfoMap.values()) {
            java.lang.String groupName = hostGroupInfo.getHostGroupName();
            int hostCardinality = hostGroupInfo.getRequestedHostCount();
            java.util.List<java.lang.String> hostnames = new java.util.ArrayList<>(hostGroupInfo.getHostNames());
            for (int i = 0; i < hostCardinality; ++i) {
                if (!hostnames.isEmpty()) {
                    java.lang.String hostname = hostnames.get(i);
                    org.apache.ambari.server.topology.HostRequest hostRequest = new org.apache.ambari.server.topology.HostRequest(getRequestId(), org.apache.ambari.server.topology.LogicalRequest.hostIdCounter.getAndIncrement(), getClusterId(), hostname, blueprint.getName(), blueprint.getHostGroup(groupName), null, topology, skipFailure);
                    synchronized(requestsWithReservedHosts) {
                        requestsWithReservedHosts.put(hostname, hostRequest);
                    }
                } else {
                    org.apache.ambari.server.topology.HostRequest hostRequest = new org.apache.ambari.server.topology.HostRequest(getRequestId(), org.apache.ambari.server.topology.LogicalRequest.hostIdCounter.getAndIncrement(), getClusterId(), null, blueprint.getName(), blueprint.getHostGroup(groupName), hostGroupInfo.getPredicate(), topology, skipFailure);
                    outstandingHostRequests.add(hostRequest);
                }
            }
        }
        allHostRequests.addAll(outstandingHostRequests);
        allHostRequests.addAll(requestsWithReservedHosts.values());
        org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.createHostRequests: all host requests size {} , outstanding requests size = {}", allHostRequests.size(), outstandingHostRequests.size());
    }

    private void createHostRequests(org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity requestEntity) {
        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> hostGroupEntities = requestEntity.getTopologyRequestEntity().getTopologyHostGroupEntities();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> allReservedHostNamesByHostGroups = getReservedHostNamesByHostGroupName(hostGroupEntities);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> pendingReservedHostNamesByHostGroups = new java.util.HashMap<>(allReservedHostNamesByHostGroups);
        for (org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntity : requestEntity.getTopologyHostRequestEntities()) {
            org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity = hostRequestEntity.getTopologyHostGroupEntity();
            java.lang.String hostGroupName = hostGroupEntity.getName();
            java.lang.String hostName = hostRequestEntity.getHostName();
            if ((hostName != null) && pendingReservedHostNamesByHostGroups.containsKey(hostGroupName)) {
                java.util.Set<java.lang.String> pendingReservedHostNamesInHostGroup = pendingReservedHostNamesByHostGroups.get(hostGroupName);
                if (pendingReservedHostNamesInHostGroup != null) {
                    pendingReservedHostNamesInHostGroup.remove(hostName);
                }
            }
        }
        boolean skipFailure = topology.getBlueprint().shouldSkipFailure();
        for (org.apache.ambari.server.orm.entities.TopologyHostRequestEntity hostRequestEntity : requestEntity.getTopologyHostRequestEntities()) {
            java.lang.Long hostRequestId = hostRequestEntity.getId();
            synchronized(org.apache.ambari.server.topology.LogicalRequest.hostIdCounter) {
                if (org.apache.ambari.server.topology.LogicalRequest.hostIdCounter.get() <= hostRequestId) {
                    org.apache.ambari.server.topology.LogicalRequest.hostIdCounter.set(hostRequestId + 1);
                }
            }
            org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity = hostRequestEntity.getTopologyHostGroupEntity();
            java.util.Set<java.lang.String> pendingReservedHostsInGroup = pendingReservedHostNamesByHostGroups.get(hostGroupEntity.getName());
            java.lang.String reservedHostName = com.google.common.collect.Iterables.getFirst(pendingReservedHostsInGroup, null);
            org.apache.ambari.server.topology.HostRequest hostRequest = new org.apache.ambari.server.topology.HostRequest(getRequestId(), hostRequestId, reservedHostName, topology, hostRequestEntity, skipFailure);
            allHostRequests.add(hostRequest);
            if (!hostRequest.isCompleted()) {
                if (reservedHostName != null) {
                    requestsWithReservedHosts.put(reservedHostName, hostRequest);
                    pendingReservedHostsInGroup.remove(reservedHostName);
                    org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.createHostRequests: created new request for a reserved request ID = {} for host name = {}", hostRequest.getId(), reservedHostName);
                } else {
                    outstandingHostRequests.add(hostRequest);
                    org.apache.ambari.server.topology.LogicalRequest.LOG.info("LogicalRequest.createHostRequests: created new outstanding host request ID = {}", hostRequest.getId());
                }
            }
        }
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getReservedHostNamesByHostGroupName(java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> hostGroups) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> reservedHostNamesByHostGroups = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.TopologyHostGroupEntity hostGroupEntity : hostGroups) {
            java.lang.String hostGroupName = hostGroupEntity.getName();
            if (!reservedHostNamesByHostGroups.containsKey(hostGroupName))
                reservedHostNamesByHostGroups.put(hostGroupName, new java.util.HashSet<>());

            for (org.apache.ambari.server.orm.entities.TopologyHostInfoEntity hostInfoEntity : hostGroupEntity.getTopologyHostInfoEntities()) {
                if (org.apache.commons.lang.StringUtils.isNotEmpty(hostInfoEntity.getFqdn())) {
                    reservedHostNamesByHostGroups.get(hostGroupName).add(hostInfoEntity.getFqdn());
                }
            }
        }
        return reservedHostNamesByHostGroups;
    }

    private static synchronized org.apache.ambari.server.controller.AmbariManagementController getController() {
        if (org.apache.ambari.server.topology.LogicalRequest.controller == null) {
            org.apache.ambari.server.topology.LogicalRequest.controller = org.apache.ambari.server.controller.AmbariServer.getController();
        }
        return org.apache.ambari.server.topology.LogicalRequest.controller;
    }

    public org.apache.ambari.server.controller.internal.CalculatedStatus calculateStatus() {
        return !isFinished() ? org.apache.ambari.server.controller.internal.CalculatedStatus.PENDING : isSuccessful() ? org.apache.ambari.server.controller.internal.CalculatedStatus.COMPLETED : org.apache.ambari.server.controller.internal.CalculatedStatus.ABORTED;
    }
}