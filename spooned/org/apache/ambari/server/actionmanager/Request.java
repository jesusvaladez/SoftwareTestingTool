package org.apache.ambari.server.actionmanager;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class Request {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.Request.class);

    private final long requestId;

    private long clusterId;

    private java.lang.String clusterName;

    private java.lang.Long requestScheduleId;

    private java.lang.String commandName;

    private java.lang.String requestContext;

    private long createTime;

    private long startTime;

    private long endTime;

    private java.lang.String clusterHostInfo;

    private java.lang.String userName;

    private boolean exclusive;

    private org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    private org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    private java.lang.String inputs;

    private java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters;

    private org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel;

    private org.apache.ambari.server.actionmanager.RequestType requestType;

    private java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.assistedinject.AssistedInject
    public Request(@com.google.inject.assistedinject.Assisted
    long requestId, @com.google.inject.assistedinject.Assisted("clusterId")
    java.lang.Long clusterId, org.apache.ambari.server.state.Clusters clusters) {
        this.requestId = requestId;
        this.clusterId = clusterId.longValue();
        this.createTime = java.lang.System.currentTimeMillis();
        this.startTime = -1;
        this.endTime = -1;
        this.exclusive = false;
        this.clusterHostInfo = "{}";
        if ((-1L) != this.clusterId) {
            try {
                this.clusterName = clusters.getClusterById(this.clusterId).getClusterName();
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.actionmanager.Request.LOG.debug("Could not load cluster with id {}, the cluster may have been removed for request {}", clusterId, java.lang.Long.valueOf(requestId));
            }
        }
    }

    @com.google.inject.assistedinject.AssistedInject
    public Request(@com.google.inject.assistedinject.Assisted
    java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages, @com.google.inject.assistedinject.Assisted
    java.lang.String clusterHostInfo, org.apache.ambari.server.state.Clusters clusters) {
        if ((stages != null) && (!stages.isEmpty())) {
            this.stages.addAll(stages);
            org.apache.ambari.server.actionmanager.Stage stage = stages.iterator().next();
            this.requestId = stage.getRequestId();
            this.clusterName = stage.getClusterName();
            try {
                this.clusterId = clusters.getCluster(clusterName).getClusterId();
            } catch (java.lang.Exception e) {
                if (null != clusterName) {
                    java.lang.String message = java.lang.String.format("Cluster %s not found", clusterName);
                    org.apache.ambari.server.actionmanager.Request.LOG.error(message);
                    throw new java.lang.RuntimeException(message);
                }
            }
            this.requestContext = stages.iterator().next().getRequestContext();
            this.createTime = java.lang.System.currentTimeMillis();
            this.startTime = -1;
            this.endTime = -1;
            this.clusterHostInfo = clusterHostInfo;
            this.requestType = org.apache.ambari.server.actionmanager.RequestType.INTERNAL_REQUEST;
            this.exclusive = false;
        } else {
            java.lang.String message = "Attempted to construct request from empty stage collection";
            org.apache.ambari.server.actionmanager.Request.LOG.error(message);
            throw new java.lang.RuntimeException(message);
        }
    }

    @com.google.inject.assistedinject.AssistedInject
    public Request(@com.google.inject.assistedinject.Assisted
    java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages, @com.google.inject.assistedinject.Assisted
    java.lang.String clusterHostInfo, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.ExecuteActionRequest actionRequest, org.apache.ambari.server.state.Clusters clusters, com.google.gson.Gson gson) throws org.apache.ambari.server.AmbariException {
        this(stages, clusterHostInfo, clusters);
        if (actionRequest != null) {
            this.resourceFilters = actionRequest.getResourceFilters();
            this.operationLevel = actionRequest.getOperationLevel();
            this.inputs = gson.toJson(actionRequest.getParameters());
            this.requestType = (actionRequest.isCommand()) ? org.apache.ambari.server.actionmanager.RequestType.COMMAND : org.apache.ambari.server.actionmanager.RequestType.ACTION;
            this.commandName = (actionRequest.isCommand()) ? actionRequest.getCommandName() : actionRequest.getActionName();
            this.exclusive = actionRequest.isExclusive();
        }
    }

    @com.google.inject.assistedinject.AssistedInject
    public Request(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.RequestEntity entity, final org.apache.ambari.server.actionmanager.StageFactory stageFactory, org.apache.ambari.server.state.Clusters clusters) {
        if (entity == null) {
            throw new java.lang.RuntimeException("Request entity cannot be null.");
        }
        this.requestId = entity.getRequestId();
        this.clusterId = entity.getClusterId();
        if ((-1L) != this.clusterId) {
            try {
                this.clusterName = clusters.getClusterById(this.clusterId).getClusterName();
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.actionmanager.Request.LOG.debug("Could not load cluster with id {}, the cluster may have been removed for request {}", java.lang.Long.valueOf(clusterId), java.lang.Long.valueOf(requestId));
            }
        }
        this.createTime = entity.getCreateTime();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.exclusive = entity.isExclusive();
        this.requestContext = entity.getRequestContext();
        this.inputs = entity.getInputs();
        this.clusterHostInfo = entity.getClusterHostInfo();
        this.requestType = entity.getRequestType();
        this.commandName = entity.getCommandName();
        this.status = entity.getStatus();
        this.displayStatus = entity.getDisplayStatus();
        if (entity.getRequestScheduleEntity() != null) {
            this.requestScheduleId = entity.getRequestScheduleEntity().getScheduleId();
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = entity.getStages();
        if ((stageEntities == null) || stageEntities.isEmpty()) {
            stages = java.util.Collections.emptyList();
        } else {
            stages = new java.util.ArrayList<>(stageEntities.size());
            for (org.apache.ambari.server.orm.entities.StageEntity stageEntity : stageEntities) {
                stages.add(stageFactory.createExisting(stageEntity));
            }
        }
        resourceFilters = org.apache.ambari.server.actionmanager.Request.filtersFromEntity(entity);
        operationLevel = org.apache.ambari.server.actionmanager.Request.operationLevelFromEntity(entity);
    }

    private static java.util.List<java.lang.String> getHostsList(java.lang.String hosts) {
        java.util.List<java.lang.String> hostList = new java.util.ArrayList<>();
        if ((hosts != null) && (!hosts.isEmpty())) {
            for (java.lang.String host : hosts.split(",")) {
                if (!host.trim().isEmpty()) {
                    hostList.add(host.trim());
                }
            }
        }
        return hostList;
    }

    public java.util.Collection<org.apache.ambari.server.actionmanager.Stage> getStages() {
        return stages;
    }

    public void setStages(java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages) {
        this.stages = stages;
    }

    public long getRequestId() {
        return requestId;
    }

    public synchronized org.apache.ambari.server.orm.entities.RequestEntity constructNewPersistenceEntity() {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(requestId);
        requestEntity.setClusterId(clusterId);
        requestEntity.setCreateTime(createTime);
        requestEntity.setStartTime(startTime);
        requestEntity.setEndTime(endTime);
        requestEntity.setExclusive(exclusive);
        requestEntity.setRequestContext(requestContext);
        requestEntity.setInputs(inputs);
        requestEntity.setRequestType(requestType);
        requestEntity.setRequestScheduleId(requestScheduleId);
        requestEntity.setStatus(status);
        requestEntity.setDisplayStatus(displayStatus);
        requestEntity.setClusterHostInfo(clusterHostInfo);
        requestEntity.setUserName(userName);
        if (resourceFilters != null) {
            java.util.List<org.apache.ambari.server.orm.entities.RequestResourceFilterEntity> filterEntities = new java.util.ArrayList<>();
            for (org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter : resourceFilters) {
                org.apache.ambari.server.orm.entities.RequestResourceFilterEntity filterEntity = new org.apache.ambari.server.orm.entities.RequestResourceFilterEntity();
                filterEntity.setServiceName(resourceFilter.getServiceName());
                filterEntity.setComponentName(resourceFilter.getComponentName());
                filterEntity.setHosts(resourceFilter.getHostNames() != null ? org.apache.commons.lang.StringUtils.join(resourceFilter.getHostNames(), ",") : null);
                filterEntity.setRequestEntity(requestEntity);
                filterEntity.setRequestId(requestId);
                filterEntities.add(filterEntity);
            }
            requestEntity.setResourceFilterEntities(filterEntities);
        }
        if (operationLevel != null) {
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = org.apache.ambari.server.actionmanager.Request.hostDAO.findByName(operationLevel.getHostName());
            java.lang.Long hostId = (hostEntity != null) ? hostEntity.getHostId() : null;
            org.apache.ambari.server.orm.entities.RequestOperationLevelEntity operationLevelEntity = new org.apache.ambari.server.orm.entities.RequestOperationLevelEntity();
            operationLevelEntity.setLevel(operationLevel.getLevel().toString());
            operationLevelEntity.setClusterName(operationLevel.getClusterName());
            operationLevelEntity.setServiceName(operationLevel.getServiceName());
            operationLevelEntity.setHostComponentName(operationLevel.getHostComponentName());
            operationLevelEntity.setHostId(hostId);
            operationLevelEntity.setRequestEntity(requestEntity);
            operationLevelEntity.setRequestId(requestId);
            requestEntity.setRequestOperationLevel(operationLevelEntity);
        }
        return requestEntity;
    }

    public java.lang.String getClusterHostInfo() {
        return clusterHostInfo;
    }

    public void setClusterHostInfo(java.lang.String clusterHostInfo) {
        this.clusterHostInfo = clusterHostInfo;
    }

    public java.lang.Long getClusterId() {
        return java.lang.Long.valueOf(clusterId);
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(java.lang.String requestContext) {
        this.requestContext = requestContext;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public java.lang.String getInputs() {
        return inputs;
    }

    public void setInputs(java.lang.String inputs) {
        this.inputs = inputs;
    }

    public java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> getResourceFilters() {
        return resourceFilters;
    }

    public void setResourceFilters(java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters) {
        this.resourceFilters = resourceFilters;
    }

    public org.apache.ambari.server.controller.internal.RequestOperationLevel getOperationLevel() {
        return operationLevel;
    }

    public void setOperationLevel(org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel) {
        this.operationLevel = operationLevel;
    }

    public org.apache.ambari.server.actionmanager.RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(org.apache.ambari.server.actionmanager.RequestType requestType) {
        this.requestType = requestType;
    }

    public java.lang.String getCommandName() {
        return commandName;
    }

    public void setCommandName(java.lang.String commandName) {
        this.commandName = commandName;
    }

    public java.lang.Long getRequestScheduleId() {
        return requestScheduleId;
    }

    public void setRequestScheduleId(java.lang.Long requestScheduleId) {
        this.requestScheduleId = requestScheduleId;
    }

    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getCommands() {
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            commands.addAll(stage.getOrderedHostRoleCommands());
        }
        return commands;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((((((((((((((((((((((("Request{" + "requestId=") + requestId) + ", clusterId=") + clusterId) + ", clusterName='") + clusterName) + '\'') + ", requestContext='") + requestContext) + '\'') + ", createTime=") + createTime) + ", startTime=") + startTime) + ", endTime=") + endTime) + ", inputs='") + inputs) + '\'') + ", status='") + status) + '\'') + ", displayStatus='") + displayStatus) + '\'') + ", resourceFilters='") + resourceFilters) + '\'') + ", operationLevel='") + operationLevel) + '\'') + ", requestType=") + requestType) + ", stages=") + stages) + '}';
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean isExclusive) {
        exclusive = isExclusive;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public static java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> filtersFromEntity(org.apache.ambari.server.orm.entities.RequestEntity entity) {
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = null;
        java.util.Collection<org.apache.ambari.server.orm.entities.RequestResourceFilterEntity> resourceFilterEntities = entity.getResourceFilterEntities();
        if (resourceFilterEntities != null) {
            resourceFilters = new java.util.ArrayList<>();
            for (org.apache.ambari.server.orm.entities.RequestResourceFilterEntity resourceFilterEntity : resourceFilterEntities) {
                org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(resourceFilterEntity.getServiceName(), resourceFilterEntity.getComponentName(), org.apache.ambari.server.actionmanager.Request.getHostsList(resourceFilterEntity.getHosts()));
                resourceFilters.add(resourceFilter);
            }
        }
        return resourceFilters;
    }

    public static org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevelFromEntity(org.apache.ambari.server.orm.entities.RequestEntity entity) {
        org.apache.ambari.server.controller.internal.RequestOperationLevel level = null;
        org.apache.ambari.server.orm.entities.RequestOperationLevelEntity operationLevelEntity = entity.getRequestOperationLevel();
        if (operationLevelEntity != null) {
            java.lang.String hostName = null;
            if (operationLevelEntity.getHostId() != null) {
                org.apache.ambari.server.orm.entities.HostEntity hostEntity = org.apache.ambari.server.actionmanager.Request.hostDAO.findById(operationLevelEntity.getHostId());
                hostName = hostEntity.getHostName();
            }
            level = new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.valueOf(operationLevelEntity.getLevel()), operationLevelEntity.getClusterName(), operationLevelEntity.getServiceName(), operationLevelEntity.getHostComponentName(), hostName);
        }
        return level;
    }
}