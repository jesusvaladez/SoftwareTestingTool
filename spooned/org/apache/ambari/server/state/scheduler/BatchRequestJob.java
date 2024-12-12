package org.apache.ambari.server.state.scheduler;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
@org.quartz.PersistJobDataAfterExecution
@org.quartz.DisallowConcurrentExecution
public class BatchRequestJob extends org.apache.ambari.server.scheduler.AbstractLinearExecutionJob {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.scheduler.BatchRequestJob.class);

    public static final java.lang.String BATCH_REQUEST_EXECUTION_ID_KEY = "BatchRequestJob.ExecutionId";

    public static final java.lang.String BATCH_REQUEST_BATCH_ID_KEY = "BatchRequestJob.BatchId";

    public static final java.lang.String BATCH_REQUEST_CLUSTER_NAME_KEY = "BatchRequestJob.ClusterName";

    public static final java.lang.String BATCH_REQUEST_FAILED_TASKS_KEY = "BatchRequestJob.FailedTaskCount";

    public static final java.lang.String BATCH_REQUEST_FAILED_TASKS_IN_CURRENT_BATCH_KEY = "BatchRequestJob.FailedTaskInCurrentBatchCount";

    public static final java.lang.String BATCH_REQUEST_TOTAL_TASKS_KEY = "BatchRequestJob.TotalTaskCount";

    private final long statusCheckInterval;

    @com.google.inject.Inject
    public BatchRequestJob(org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager, @com.google.inject.name.Named("statusCheckInterval")
    long statusCheckInterval) {
        super(executionScheduleManager);
        this.statusCheckInterval = statusCheckInterval;
    }

    @java.lang.Override
    protected void doWork(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        java.lang.Long executionId = (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY) != null) ? ((java.lang.Long) (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY))) : null;
        java.lang.Long batchId = (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_BATCH_ID_KEY) != null) ? ((java.lang.Long) (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_BATCH_ID_KEY))) : null;
        java.lang.String clusterName = ((java.lang.String) (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_CLUSTER_NAME_KEY)));
        if ((executionId == null) || (batchId == null)) {
            throw new org.apache.ambari.server.AmbariException(((("Unable to retrieve persisted batch request" + ", execution_id = ") + executionId) + ", batch_id = ") + batchId);
        }
        java.util.Map<java.lang.String, java.lang.Integer> taskCounts = getTaskCountProperties(properties);
        java.lang.Long requestId = executionScheduleManager.executeBatchRequest(executionId, batchId, clusterName);
        if (requestId != null) {
            org.apache.ambari.server.actionmanager.HostRoleStatus status;
            org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse;
            do {
                batchRequestResponse = executionScheduleManager.getBatchRequestResponse(requestId, clusterName);
                status = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(batchRequestResponse.getStatus());
                executionScheduleManager.updateBatchRequest(executionId, batchId, clusterName, batchRequestResponse, true);
                try {
                    java.lang.Thread.sleep(statusCheckInterval);
                } catch (java.lang.InterruptedException e) {
                    java.lang.String message = "Job Thread interrupted";
                    org.apache.ambari.server.state.scheduler.BatchRequestJob.LOG.error(message, e);
                    throw new org.apache.ambari.server.AmbariException(message, e);
                }
            } while (!status.isCompletedState() );
            java.util.Map<java.lang.String, java.lang.Integer> aggregateCounts = addTaskCountToProperties(properties, taskCounts, batchRequestResponse);
            if (executionScheduleManager.hasToleranceThresholdExceeded(executionId, clusterName, aggregateCounts)) {
                throw new org.apache.ambari.server.AmbariException(((((((((("Task failure tolerance limit exceeded" + ", execution_id = ") + executionId) + ", processed batch_id = ") + batchId) + ", failed tasks in current batch = ") + aggregateCounts.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_IN_CURRENT_BATCH_KEY)) + ", failed tasks total = ") + aggregateCounts.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY)) + ", total tasks completed = ") + aggregateCounts.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY));
            }
        }
    }

    @java.lang.Override
    protected void finalizeExecution(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        java.lang.Long executionId = (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY) != null) ? ((java.lang.Long) (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY))) : null;
        java.lang.Long batchId = (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_BATCH_ID_KEY) != null) ? ((java.lang.Long) (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_BATCH_ID_KEY))) : null;
        java.lang.String clusterName = ((java.lang.String) (properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_CLUSTER_NAME_KEY)));
        if ((executionId == null) || (batchId == null)) {
            throw new org.apache.ambari.server.AmbariException(((("Unable to retrieve persisted batch request" + ", execution_id = ") + executionId) + ", batch_id = ") + batchId);
        }
        executionScheduleManager.finalizeBatch(executionId, clusterName);
    }

    private java.util.Map<java.lang.String, java.lang.Integer> addTaskCountToProperties(java.util.Map<java.lang.String, java.lang.Object> properties, java.util.Map<java.lang.String, java.lang.Integer> oldCounts, org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse) {
        java.util.Map<java.lang.String, java.lang.Integer> taskCounts = new java.util.HashMap<>();
        if (batchRequestResponse != null) {
            java.lang.Integer failedTasks = (batchRequestResponse.getFailedTaskCount() + batchRequestResponse.getAbortedTaskCount()) + batchRequestResponse.getTimedOutTaskCount();
            java.lang.Integer failedCount = oldCounts.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY) + failedTasks;
            java.lang.Integer totalCount = oldCounts.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY) + batchRequestResponse.getTotalTaskCount();
            taskCounts.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY, failedCount);
            taskCounts.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_IN_CURRENT_BATCH_KEY, batchRequestResponse.getFailedTaskCount());
            taskCounts.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY, totalCount);
            properties.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY, failedCount);
            properties.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_IN_CURRENT_BATCH_KEY, batchRequestResponse.getFailedTaskCount());
            properties.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY, totalCount);
        }
        return taskCounts;
    }

    private java.util.Map<java.lang.String, java.lang.Integer> getTaskCountProperties(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.util.Map<java.lang.String, java.lang.Integer> taskCounts = new java.util.HashMap<>();
        if (properties != null) {
            java.lang.Object countObj = properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY);
            taskCounts.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY, countObj != null ? java.lang.Integer.parseInt(countObj.toString()) : 0);
            countObj = properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY);
            taskCounts.put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY, countObj != null ? java.lang.Integer.parseInt(countObj.toString()) : 0);
        }
        return taskCounts;
    }
}