package org.apache.ambari.server.actionmanager;
public interface ActionDBAccessor {
    org.apache.ambari.server.actionmanager.Stage getStage(java.lang.String actionId);

    java.util.List<org.apache.ambari.server.actionmanager.Stage> getAllStages(long requestId);

    org.apache.ambari.server.orm.entities.RequestEntity getRequestEntity(long requestId);

    org.apache.ambari.server.actionmanager.Request getRequest(long requestId);

    java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortOperation(long requestId);

    void timeoutHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role);

    void timeoutHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role, boolean skipSupported, boolean hostUnknownState);

    java.util.List<org.apache.ambari.server.actionmanager.Stage> getFirstStageInProgressPerRequest();

    java.util.List<org.apache.ambari.server.actionmanager.Stage> getStagesInProgressForRequest(java.lang.Long requestId);

    int getCommandsInProgressCount();

    void persistActions(org.apache.ambari.server.actionmanager.Request request) throws org.apache.ambari.server.AmbariException;

    void startRequest(long requestId);

    void endRequest(long requestId);

    void setSourceScheduleForRequest(long requestId, long scheduleId);

    void updateHostRoleStates(java.util.Collection<org.apache.ambari.server.agent.CommandReport> reports);

    void updateHostRoleState(java.lang.String hostname, long requestId, long stageId, java.lang.String role, org.apache.ambari.server.agent.CommandReport report);

    void abortHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role);

    void abortHostRole(java.lang.String host, long requestId, long stageId, java.lang.String role, java.lang.String reason);

    long getLastPersistedRequestIdWhenInitialized();

    void bulkHostRoleScheduled(org.apache.ambari.server.actionmanager.Stage s, java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commands);

    void bulkAbortHostRole(org.apache.ambari.server.actionmanager.Stage s, java.util.Map<org.apache.ambari.server.agent.ExecutionCommand, java.lang.String> commands);

    void hostRoleScheduled(org.apache.ambari.server.actionmanager.Stage s, java.lang.String hostname, java.lang.String roleStr);

    java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getRequestTasks(long requestId);

    java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getAllTasksByRequestIds(java.util.Collection<java.lang.Long> requestIds);

    java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasks(java.util.Collection<java.lang.Long> taskIds);

    java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasksByHostRoleAndStatus(java.lang.String hostname, java.lang.String role, org.apache.ambari.server.actionmanager.HostRoleStatus status);

    java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getTasksByRoleAndStatus(java.lang.String role, org.apache.ambari.server.actionmanager.HostRoleStatus status);

    org.apache.ambari.server.actionmanager.HostRoleCommand getTask(long taskId);

    java.util.List<java.lang.Long> getRequestsByStatus(org.apache.ambari.server.actionmanager.RequestStatus status, int maxResults, boolean ascOrder);

    java.util.Map<java.lang.Long, java.lang.String> getRequestContext(java.util.List<java.lang.Long> requestIds);

    java.lang.String getRequestContext(long requestId);

    java.util.List<org.apache.ambari.server.actionmanager.Request> getRequests(java.util.Collection<java.lang.Long> requestIds);

    void resubmitTasks(java.util.List<java.lang.Long> taskIds);
}