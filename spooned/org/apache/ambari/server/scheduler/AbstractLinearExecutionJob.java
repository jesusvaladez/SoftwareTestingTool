package org.apache.ambari.server.scheduler;
import org.quartz.DateBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import static org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_BATCH_ID_KEY;
import static org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_CLUSTER_NAME_KEY;
import static org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY;
import static org.apache.ambari.server.state.scheduler.RequestExecution.Status.ABORTED;
import static org.apache.ambari.server.state.scheduler.RequestExecution.Status.PAUSED;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
public abstract class AbstractLinearExecutionJob implements org.apache.ambari.server.scheduler.ExecutionJob {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.class);

    protected org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager;

    public AbstractLinearExecutionJob(org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager) {
        this.executionScheduleManager = executionScheduleManager;
    }

    protected abstract void doWork(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException;

    protected abstract void finalizeExecution(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException;

    @java.lang.Override
    public void execute(org.quartz.JobExecutionContext context) throws org.quartz.JobExecutionException {
        org.quartz.JobKey jobKey = context.getJobDetail().getKey();
        org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.debug("Executing linear job: {}", jobKey);
        org.quartz.JobDataMap jobDataMap = context.getMergedJobDataMap();
        if (!executionScheduleManager.continueOnMisfire(context)) {
            throw new org.quartz.JobExecutionException(((("Canceled execution based on misfire" + " toleration threshold, job: ") + jobKey) + ", scheduleTime = ") + context.getScheduledFireTime());
        }
        java.util.Map<java.lang.String, java.lang.Object> properties = jobDataMap.getWrappedMap();
        boolean finalize = false;
        try {
            doWork(properties);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.error(("Exception caught on execution of job " + jobKey) + ". Exiting linear chain...", e);
            finalize = true;
            throw new org.quartz.JobExecutionException(e);
        } catch (java.lang.RuntimeException e) {
            org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.error((("Unexpected exception caught on execution of job " + jobKey) + ". ") + "Exiting linear chain...", e);
            finalize = true;
            throw e;
        } finally {
            if (finalize) {
                try {
                    finalizeExecution(properties);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.warn("Unable to finalize execution for job: " + jobKey);
                }
            }
        }
        org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.debug("Finished linear job: {}", jobKey);
        java.lang.String nextJobName = jobDataMap.getString(org.apache.ambari.server.scheduler.ExecutionJob.NEXT_EXECUTION_JOB_NAME_KEY);
        java.lang.String nextJobGroup = jobDataMap.getString(org.apache.ambari.server.scheduler.ExecutionJob.NEXT_EXECUTION_JOB_GROUP_KEY);
        if ((nextJobName == null) || nextJobName.isEmpty()) {
            org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.debug("End of linear job chain. Returning with success.");
            try {
                finalizeExecution(properties);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.warn("Unable to finalize execution for job: " + jobKey);
            }
            return;
        }
        try {
            executionScheduleManager.pauseAfterBatchIfNeeded(jobDataMap.getLong(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY), jobDataMap.getLong(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_BATCH_ID_KEY), jobDataMap.getString(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_CLUSTER_NAME_KEY));
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.warn("Received exception while trying to auto pause the scheduled request execution :", e);
        }
        java.lang.String status = null;
        try {
            status = executionScheduleManager.getBatchRequestStatus(jobDataMap.getLong(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY), jobDataMap.getString(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_CLUSTER_NAME_KEY));
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.warn("Unable to define the status of batch request : ", e);
        }
        if (org.apache.ambari.server.state.scheduler.RequestExecution.Status.ABORTED.name().equals(status) || org.apache.ambari.server.state.scheduler.RequestExecution.Status.PAUSED.name().equals(status)) {
            org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.LOG.info("The linear job chain was paused or aborted, not triggering the next one");
            return;
        }
        int separationSeconds = jobDataMap.getIntValue(org.apache.ambari.server.scheduler.ExecutionJob.NEXT_EXECUTION_SEPARATION_SECONDS);
        java.lang.Object failedCount = properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY);
        java.lang.Object totalCount = properties.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY);
        org.quartz.Trigger trigger = TriggerBuilder.newTrigger().forJob(nextJobName, nextJobGroup).withIdentity("TriggerForJob-" + nextJobName, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_TRIGGER_GROUP).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).startAt(DateBuilder.futureDate(separationSeconds, DateBuilder.IntervalUnit.SECOND)).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY, failedCount != null ? ((java.lang.Integer) (failedCount)) : 0).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY, totalCount != null ? ((java.lang.Integer) (totalCount)) : 0).build();
        executionScheduleManager.scheduleJob(trigger);
    }
}