package org.apache.ambari.server.scheduler;
import org.quartz.Job;
public interface ExecutionJob extends org.quartz.Job {
    java.lang.String NEXT_EXECUTION_JOB_NAME_KEY = "ExecutionJob.Name";

    java.lang.String NEXT_EXECUTION_JOB_GROUP_KEY = "ExecutionJob.Group";

    java.lang.String NEXT_EXECUTION_SEPARATION_SECONDS = "ExecutionJob.SeparationMinutes";

    java.lang.String LINEAR_EXECUTION_JOB_GROUP = "LinearExecutionJobs";

    java.lang.String LINEAR_EXECUTION_TRIGGER_GROUP = "LinearExecutionTriggers";
}