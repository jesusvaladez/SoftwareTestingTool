package org.apache.ambari.server.state.scheduler;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
public class RequestExecutionImpl implements org.apache.ambari.server.state.scheduler.RequestExecution {
    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.state.scheduler.Batch batch;

    private org.apache.ambari.server.state.scheduler.Schedule schedule;

    private org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity;

    private volatile boolean isPersisted = false;

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestScheduleDAO requestScheduleDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestScheduleBatchRequestDAO batchRequestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.scheduler.RequestExecutionImpl.class);

    private final java.util.concurrent.locks.ReadWriteLock readWriteLock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    @com.google.inject.assistedinject.AssistedInject
    public RequestExecutionImpl(@com.google.inject.assistedinject.Assisted("cluster")
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("batch")
    org.apache.ambari.server.state.scheduler.Batch batch, @com.google.inject.assistedinject.Assisted("schedule")
    @javax.annotation.Nullable
    org.apache.ambari.server.state.scheduler.Schedule schedule, com.google.inject.Injector injector) {
        this.cluster = cluster;
        this.batch = batch;
        this.schedule = schedule;
        injector.injectMembers(this);
        requestScheduleEntity = new org.apache.ambari.server.orm.entities.RequestScheduleEntity();
        requestScheduleEntity.setClusterId(cluster.getClusterId());
        updateBatchSettings();
        updateSchedule();
    }

    @com.google.inject.assistedinject.AssistedInject
    public RequestExecutionImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity, com.google.inject.Injector injector) {
        this.cluster = cluster;
        injector.injectMembers(this);
        this.requestScheduleEntity = requestScheduleEntity;
        batch = new org.apache.ambari.server.state.scheduler.Batch();
        schedule = new org.apache.ambari.server.state.scheduler.Schedule();
        org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = new org.apache.ambari.server.state.scheduler.BatchSettings();
        batchSettings.setBatchSeparationInSeconds(requestScheduleEntity.getBatchSeparationInSeconds());
        batchSettings.setTaskFailureToleranceLimit(requestScheduleEntity.getBatchTolerationLimit());
        batchSettings.setTaskFailureToleranceLimitPerBatch(requestScheduleEntity.getBatchTolerationLimitPerBatch());
        batchSettings.setPauseAfterFirstBatch(requestScheduleEntity.isPauseAfterFirstBatch());
        batch.setBatchSettings(batchSettings);
        java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> batchRequestEntities = requestScheduleEntity.getRequestScheduleBatchRequestEntities();
        if (batchRequestEntities != null) {
            for (org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity : batchRequestEntities) {
                org.apache.ambari.server.state.scheduler.BatchRequest batchRequest = new org.apache.ambari.server.state.scheduler.BatchRequest();
                batchRequest.setOrderId(batchRequestEntity.getBatchId());
                batchRequest.setRequestId(batchRequestEntity.getRequestId());
                if (batchRequestEntity.getRequestBody() != null) {
                    batchRequest.setBody(new java.lang.String(batchRequestEntity.getRequestBody()));
                }
                batchRequest.setType(org.apache.ambari.server.state.scheduler.BatchRequest.Type.valueOf(batchRequestEntity.getRequestType()));
                batchRequest.setUri(batchRequestEntity.getRequestUri());
                batchRequest.setStatus(batchRequestEntity.getRequestStatus());
                batchRequest.setReturnCode(batchRequestEntity.getReturnCode());
                batchRequest.setResponseMsg(batchRequestEntity.getReturnMessage());
                batch.getBatchRequests().add(batchRequest);
            }
        }
        schedule.setDayOfWeek(requestScheduleEntity.getDayOfWeek());
        schedule.setDaysOfMonth(requestScheduleEntity.getDaysOfMonth());
        schedule.setMinutes(requestScheduleEntity.getMinutes());
        schedule.setHours(requestScheduleEntity.getHours());
        schedule.setMonth(requestScheduleEntity.getMonth());
        schedule.setYear(requestScheduleEntity.getYear());
        schedule.setStartTime(requestScheduleEntity.getStartTime());
        schedule.setEndTime(requestScheduleEntity.getEndTime());
        if ((((((((schedule.getDayOfWeek() == null) && (schedule.getDaysOfMonth() == null)) && (schedule.getMinutes() == null)) && (schedule.getHours() == null)) && (schedule.getMonth() == null)) && (schedule.getYear() == null)) && (schedule.getStartTime() == null)) && (schedule.getEndTime() == null)) {
            schedule = null;
        }
        isPersisted = true;
    }

    @java.lang.Override
    public java.lang.Long getId() {
        return requestScheduleEntity.getScheduleId();
    }

    @java.lang.Override
    public java.lang.String getClusterName() {
        return cluster.getClusterName();
    }

    @java.lang.Override
    public org.apache.ambari.server.state.scheduler.Batch getBatch() {
        return batch;
    }

    @java.lang.Override
    public void setBatch(org.apache.ambari.server.state.scheduler.Batch batch) {
        this.batch = batch;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.scheduler.Schedule getSchedule() {
        return schedule;
    }

    @java.lang.Override
    public void setSchedule(org.apache.ambari.server.state.scheduler.Schedule schedule) {
        this.schedule = schedule;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.RequestScheduleResponse convertToResponse() {
        readWriteLock.readLock().lock();
        try {
            org.apache.ambari.server.controller.RequestScheduleResponse response = new org.apache.ambari.server.controller.RequestScheduleResponse(getId(), getClusterName(), getDescription(), getStatus(), getLastExecutionStatus(), getBatch(), getSchedule(), requestScheduleEntity.getCreateUser(), org.apache.ambari.server.utils.DateUtils.convertToReadableTime(requestScheduleEntity.getCreateTimestamp()), requestScheduleEntity.getUpdateUser(), org.apache.ambari.server.utils.DateUtils.convertToReadableTime(requestScheduleEntity.getUpdateTimestamp()), requestScheduleEntity.getAuthenticatedUserId());
            return response;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public void persist() {
        readWriteLock.writeLock().lock();
        try {
            if (!isPersisted) {
                persistEntities();
                refresh();
                cluster.refresh();
                isPersisted = true;
            } else {
                saveIfPersisted();
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public void refresh() {
        readWriteLock.writeLock().lock();
        try {
            if (isPersisted) {
                org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(requestScheduleEntity.getScheduleId());
                requestScheduleDAO.refresh(scheduleEntity);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public void delete() {
        readWriteLock.writeLock().lock();
        try {
            if (isPersisted) {
                batchRequestDAO.removeByScheduleId(requestScheduleEntity.getScheduleId());
                requestScheduleDAO.remove(requestScheduleEntity);
                cluster.refresh();
                isPersisted = false;
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @java.lang.Override
    public java.lang.String getStatus() {
        return requestScheduleEntity.getStatus();
    }

    @java.lang.Override
    public void setDescription(java.lang.String description) {
        requestScheduleEntity.setDescription(description);
    }

    @java.lang.Override
    public java.lang.String getDescription() {
        return requestScheduleEntity.getDescription();
    }

    @com.google.inject.persist.Transactional
    void persistEntities() {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(cluster.getClusterId());
        requestScheduleEntity.setClusterEntity(clusterEntity);
        requestScheduleEntity.setCreateTimestamp(java.lang.System.currentTimeMillis());
        requestScheduleEntity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
        requestScheduleDAO.create(requestScheduleEntity);
        persistRequestMapping();
    }

    @com.google.inject.persist.Transactional
    void persistRequestMapping() {
        if (isPersisted) {
            batchRequestDAO.removeByScheduleId(requestScheduleEntity.getScheduleId());
            requestScheduleEntity.getRequestScheduleBatchRequestEntities().clear();
        }
        if (batch != null) {
            java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
            if (batchRequests != null) {
                java.util.Collections.sort(batchRequests);
                for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batchRequests) {
                    org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity = new org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity();
                    batchRequestEntity.setBatchId(batchRequest.getOrderId());
                    batchRequestEntity.setRequestId(batchRequest.getRequestId());
                    batchRequestEntity.setScheduleId(requestScheduleEntity.getScheduleId());
                    batchRequestEntity.setRequestScheduleEntity(requestScheduleEntity);
                    batchRequestEntity.setRequestType(batchRequest.getType());
                    batchRequestEntity.setRequestUri(batchRequest.getUri());
                    batchRequestEntity.setRequestBody(batchRequest.getBody());
                    batchRequestEntity.setReturnCode(batchRequest.getReturnCode());
                    batchRequestEntity.setReturnMessage(batchRequest.getResponseMsg());
                    batchRequestEntity.setRequestStatus(batchRequest.getStatus());
                    batchRequestDAO.create(batchRequestEntity);
                    requestScheduleEntity.getRequestScheduleBatchRequestEntities().add(batchRequestEntity);
                    requestScheduleDAO.merge(requestScheduleEntity);
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    void saveIfPersisted() {
        if (isPersisted) {
            requestScheduleEntity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
            updateBatchSettings();
            updateSchedule();
            requestScheduleDAO.merge(requestScheduleEntity);
            persistRequestMapping();
        }
    }

    private void updateBatchSettings() {
        if (batch != null) {
            org.apache.ambari.server.state.scheduler.BatchSettings settings = batch.getBatchSettings();
            if (settings != null) {
                requestScheduleEntity.setBatchSeparationInSeconds(settings.getBatchSeparationInSeconds());
                requestScheduleEntity.setBatchTolerationLimit(settings.getTaskFailureToleranceLimit());
                requestScheduleEntity.setBatchTolerationLimitPerBatch(settings.getTaskFailureToleranceLimitPerBatch());
                requestScheduleEntity.setPauseAfterFirstBatch(settings.isPauseAfterFirstBatch());
            }
        }
    }

    private void updateSchedule() {
        if (schedule != null) {
            requestScheduleEntity.setMinutes(schedule.getMinutes());
            requestScheduleEntity.setHours(schedule.getHours());
            requestScheduleEntity.setDaysOfMonth(schedule.getDaysOfMonth());
            requestScheduleEntity.setDayOfWeek(schedule.getDayOfWeek());
            requestScheduleEntity.setMonth(schedule.getMonth());
            requestScheduleEntity.setYear(schedule.getYear());
            requestScheduleEntity.setStartTime(schedule.getStartTime());
            requestScheduleEntity.setEndTime(schedule.getEndTime());
        }
    }

    @java.lang.Override
    public void setStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status status) {
        requestScheduleEntity.setStatus(status.name());
    }

    @java.lang.Override
    public void setLastExecutionStatus(java.lang.String status) {
        requestScheduleEntity.setLastExecutionStatus(status);
    }

    @java.lang.Override
    public void setAuthenticatedUserId(java.lang.Integer username) {
        requestScheduleEntity.setAuthenticatedUserId(username);
    }

    @java.lang.Override
    public void setCreateUser(java.lang.String username) {
        requestScheduleEntity.setCreateUser(username);
    }

    @java.lang.Override
    public void setUpdateUser(java.lang.String username) {
        requestScheduleEntity.setUpdateUser(username);
    }

    @java.lang.Override
    public java.lang.String getCreateTime() {
        return org.apache.ambari.server.utils.DateUtils.convertToReadableTime(requestScheduleEntity.getCreateTimestamp());
    }

    @java.lang.Override
    public java.lang.String getUpdateTime() {
        return org.apache.ambari.server.utils.DateUtils.convertToReadableTime(requestScheduleEntity.getUpdateTimestamp());
    }

    @java.lang.Override
    public java.lang.Integer getAuthenticatedUserId() {
        return requestScheduleEntity.getAuthenticatedUserId();
    }

    @java.lang.Override
    public java.lang.String getCreateUser() {
        return requestScheduleEntity.getCreateUser();
    }

    @java.lang.Override
    public java.lang.String getUpdateUser() {
        return requestScheduleEntity.getUpdateUser();
    }

    @java.lang.Override
    public java.lang.String getLastExecutionStatus() {
        return requestScheduleEntity.getLastExecutionStatus();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.RequestScheduleResponse convertToResponseWithBody() {
        readWriteLock.readLock().lock();
        try {
            org.apache.ambari.server.controller.RequestScheduleResponse response = convertToResponse();
            org.apache.ambari.server.state.scheduler.Batch batch = response.getBatch();
            if (batch != null) {
                java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
                if (batchRequests != null) {
                    for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batchRequests) {
                        batchRequest.setBody(getRequestBody(batchRequest.getOrderId()));
                    }
                }
            }
            return response;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @java.lang.Override
    public java.lang.String getRequestBody(java.lang.Long batchId) {
        java.lang.String body = null;
        if (requestScheduleEntity != null) {
            java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> requestEntities = requestScheduleEntity.getRequestScheduleBatchRequestEntities();
            if (requestEntities != null) {
                for (org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity requestEntity : requestEntities) {
                    if (requestEntity.getBatchId().equals(batchId)) {
                        body = requestEntity.getRequestBodyAsString();
                    }
                }
            }
        }
        return body;
    }

    @java.lang.Override
    public java.util.Collection<java.lang.Long> getBatchRequestRequestsIDs(long batchId) {
        java.util.Collection<java.lang.Long> requestIDs = new java.util.ArrayList<>();
        if (requestScheduleEntity != null) {
            java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> requestEntities = requestScheduleEntity.getRequestScheduleBatchRequestEntities();
            if (requestEntities != null) {
                requestIDs.addAll(requestEntities.stream().filter(requestEntity -> requestEntity.getBatchId().equals(batchId)).map(org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity::getRequestId).collect(java.util.stream.Collectors.toList()));
            }
        }
        return requestIDs;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.scheduler.BatchRequest getBatchRequest(long batchId) {
        for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batch.getBatchRequests()) {
            if (batchId == batchRequest.getOrderId()) {
                return batchRequest;
            }
        }
        return null;
    }

    @java.lang.Override
    public void updateBatchRequest(long batchId, org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse, boolean statusOnly) {
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity batchRequestEntity = null;
        for (org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity entity : requestScheduleEntity.getRequestScheduleBatchRequestEntities()) {
            if ((entity.getBatchId() == batchId) && (entity.getScheduleId() == requestScheduleEntity.getScheduleId())) {
                batchRequestEntity = entity;
            }
        }
        if (org.apache.ambari.server.state.scheduler.RequestExecution.Status.PAUSED.name().equals(getStatus()) && org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name().equals(batchRequestResponse.getStatus())) {
            batchRequestResponse.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.name());
        }
        if (batchRequestEntity != null) {
            batchRequestEntity.setRequestStatus(batchRequestResponse.getStatus());
            if (!statusOnly) {
                batchRequestEntity.setReturnCode(batchRequestResponse.getReturnCode());
                batchRequestEntity.setRequestId(batchRequestResponse.getRequestId());
                batchRequestEntity.setReturnMessage(batchRequestResponse.getReturnMessage());
            }
            batchRequestDAO.merge(batchRequestEntity);
        }
        org.apache.ambari.server.state.scheduler.BatchRequest batchRequest = getBatchRequest(batchId);
        batchRequest.setStatus(batchRequestResponse.getStatus());
        if (!statusOnly) {
            batchRequest.setReturnCode(batchRequestResponse.getReturnCode());
            batchRequest.setResponseMsg(batchRequestResponse.getReturnMessage());
        }
        setLastExecutionStatus(batchRequestResponse.getStatus());
        requestScheduleDAO.merge(requestScheduleEntity);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void updateStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status status) {
        setStatus(status);
        if (isPersisted) {
            requestScheduleEntity.setUpdateTimestamp(java.lang.System.currentTimeMillis());
            requestScheduleDAO.merge(requestScheduleEntity);
        } else {
            org.apache.ambari.server.state.scheduler.RequestExecutionImpl.LOG.warn("Updated status in memory, since Request Schedule is not " + "persisted.");
        }
    }
}