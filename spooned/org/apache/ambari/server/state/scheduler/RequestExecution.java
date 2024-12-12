package org.apache.ambari.server.state.scheduler;
public interface RequestExecution {
    java.lang.Long getId();

    java.lang.String getClusterName();

    org.apache.ambari.server.state.scheduler.Batch getBatch();

    void setBatch(org.apache.ambari.server.state.scheduler.Batch batch);

    org.apache.ambari.server.state.scheduler.Schedule getSchedule();

    void setSchedule(org.apache.ambari.server.state.scheduler.Schedule schedule);

    org.apache.ambari.server.controller.RequestScheduleResponse convertToResponse();

    void persist();

    void refresh();

    void delete();

    java.lang.String getStatus();

    void setDescription(java.lang.String description);

    java.lang.String getDescription();

    void setStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status status);

    void setLastExecutionStatus(java.lang.String status);

    void setAuthenticatedUserId(java.lang.Integer username);

    void setCreateUser(java.lang.String username);

    void setUpdateUser(java.lang.String username);

    java.lang.String getCreateTime();

    java.lang.String getUpdateTime();

    java.lang.Integer getAuthenticatedUserId();

    java.lang.String getCreateUser();

    java.lang.String getUpdateUser();

    java.lang.String getLastExecutionStatus();

    org.apache.ambari.server.controller.RequestScheduleResponse convertToResponseWithBody();

    java.lang.String getRequestBody(java.lang.Long batchId);

    java.util.Collection<java.lang.Long> getBatchRequestRequestsIDs(long batchId);

    org.apache.ambari.server.state.scheduler.BatchRequest getBatchRequest(long batchId);

    void updateBatchRequest(long batchId, org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse, boolean statusOnly);

    void updateStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status status);

    enum Status {

        SCHEDULED,
        COMPLETED,
        DISABLED,
        ABORTED,
        PAUSED;}
}