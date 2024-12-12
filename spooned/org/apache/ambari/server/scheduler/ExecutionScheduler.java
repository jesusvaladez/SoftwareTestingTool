package org.apache.ambari.server.scheduler;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
public interface ExecutionScheduler {
    void startScheduler(java.lang.Integer delay) throws org.apache.ambari.server.AmbariException;

    void stopScheduler() throws org.apache.ambari.server.AmbariException;

    void scheduleJob(org.quartz.Trigger trigger) throws org.quartz.SchedulerException;

    void addJob(org.quartz.JobDetail job) throws org.quartz.SchedulerException;

    void deleteJob(org.quartz.JobKey jobKey) throws org.quartz.SchedulerException;

    org.quartz.JobDetail getJobDetail(org.quartz.JobKey jobKey) throws org.quartz.SchedulerException;

    java.util.List<? extends org.quartz.Trigger> getTriggersForJob(org.quartz.JobKey jobKey) throws org.quartz.SchedulerException;

    boolean isSchedulerStarted() throws org.quartz.SchedulerException;
}