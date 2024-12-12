package org.apache.ambari.view.pig.resources.jobs.utils;
public class JobPolling implements java.lang.Runnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.class);

    private static final int WORKER_COUNT = 2;

    private static final int POLLING_DELAY = 60;

    private static final int LONG_POLLING_DELAY = 10 * 60;

    private static final int LONG_JOB_THRESHOLD = 10 * 60;

    private static final java.util.concurrent.ScheduledExecutorService pollWorkersPool = java.util.concurrent.Executors.newScheduledThreadPool(org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.WORKER_COUNT);

    private static final java.util.Map<java.lang.String, org.apache.ambari.view.pig.resources.jobs.utils.JobPolling> jobPollers = new java.util.HashMap<java.lang.String, org.apache.ambari.view.pig.resources.jobs.utils.JobPolling>();

    private org.apache.ambari.view.pig.resources.jobs.JobResourceManager resourceManager = null;

    private org.apache.ambari.view.pig.resources.jobs.models.PigJob job;

    private volatile java.util.concurrent.ScheduledFuture<?> thisFuture;

    private JobPolling(org.apache.ambari.view.pig.resources.jobs.JobResourceManager resourceManager, org.apache.ambari.view.pig.resources.jobs.models.PigJob job) {
        this.resourceManager = resourceManager;
        this.job = job;
    }

    public void run() {
        try {
            resourceManager.ignorePermissions(new java.util.concurrent.Callable<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void call() throws java.lang.Exception {
                    org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LOG.debug((("Polling job status " + job.getJobId()) + " #") + job.getId());
                    try {
                        job = resourceManager.read(job.getId());
                    } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                        org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LOG.error(("Job " + job.getId()) + " does not exist! Polling canceled");
                        thisFuture.cancel(false);
                        return null;
                    }
                    resourceManager.retrieveJobStatus(job);
                    java.lang.Long time = java.lang.System.currentTimeMillis() / 1000L;
                    if ((time - job.getDateStarted()) > org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LONG_JOB_THRESHOLD) {
                        org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LOG.debug("Job becomes long.. Rescheduling polling to longer period");
                        thisFuture.cancel(false);
                        scheduleJobPolling(true);
                    }
                    if (((job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMIT_FAILED) || job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_COMPLETED)) || job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_FAILED)) || job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_KILLED)) {
                        org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LOG.debug("Job finished. Polling canceled");
                        thisFuture.cancel(false);
                    } else {
                    }
                    return null;
                }
            });
        } catch (java.lang.Exception e) {
            org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LOG.error("Exception during handling job polling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void scheduleJobPolling(boolean longDelay) {
        if (!longDelay) {
            thisFuture = org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.pollWorkersPool.scheduleWithFixedDelay(this, org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.POLLING_DELAY, org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.POLLING_DELAY, java.util.concurrent.TimeUnit.SECONDS);
        } else {
            thisFuture = org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.pollWorkersPool.scheduleWithFixedDelay(this, org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LONG_POLLING_DELAY, org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LONG_POLLING_DELAY, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    private void scheduleJobPolling() {
        scheduleJobPolling(false);
    }

    public static boolean pollJob(org.apache.ambari.view.pig.resources.jobs.JobResourceManager resourceManager, org.apache.ambari.view.pig.resources.jobs.models.PigJob job) {
        if (org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.jobPollers.get(job.getJobId()) == null) {
            org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.LOG.debug("Setting up polling for " + job.getJobId());
            org.apache.ambari.view.pig.resources.jobs.utils.JobPolling polling = new org.apache.ambari.view.pig.resources.jobs.utils.JobPolling(resourceManager, job);
            polling.scheduleJobPolling();
            org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.jobPollers.put(job.getJobId(), polling);
            return true;
        }
        return false;
    }
}