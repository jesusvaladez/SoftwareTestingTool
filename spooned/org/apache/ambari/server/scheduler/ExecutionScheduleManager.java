package org.apache.ambari.server.scheduler;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.CsrfProtectionFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.quartz.CronExpression;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import static org.apache.ambari.server.state.scheduler.RequestExecution.Status.ABORTED;
import static org.apache.ambari.server.state.scheduler.RequestExecution.Status.PAUSED;
import static org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
@com.google.inject.Singleton
public class ExecutionScheduleManager {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class);

    private final org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage;

    private org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor;

    private final com.google.gson.Gson gson;

    private final org.apache.ambari.server.state.Clusters clusters;

    org.apache.ambari.server.scheduler.ExecutionScheduler executionScheduler;

    org.apache.ambari.server.configuration.Configuration configuration;

    private volatile boolean schedulerAvailable = false;

    protected static final java.lang.String BATCH_REQUEST_JOB_PREFIX = "BatchRequestJob";

    protected static final java.lang.String REQUEST_EXECUTION_TRIGGER_PREFIX = "RequestExecution";

    protected static final java.lang.String DEFAULT_API_PATH = "api/v1";

    public static final java.lang.String USER_ID_HEADER = "X-Authenticated-User-ID";

    protected com.sun.jersey.api.client.Client ambariClient;

    protected com.sun.jersey.api.client.WebResource ambariWebResource;

    protected static final java.lang.String REQUESTS_STATUS_KEY = "request_status";

    protected static final java.lang.String REQUESTS_ID_KEY = "id";

    protected static final java.lang.String REQUESTS_FAILED_TASKS_KEY = "failed_task_count";

    protected static final java.lang.String REQUESTS_ABORTED_TASKS_KEY = "aborted_task_count";

    protected static final java.lang.String REQUESTS_TIMEDOUT_TASKS_KEY = "timed_out_task_count";

    protected static final java.lang.String REQUESTS_TOTAL_TASKS_KEY = "task_count";

    protected static final java.util.regex.Pattern CONTAINS_API_VERSION_PATTERN = java.util.regex.Pattern.compile(("^/?" + org.apache.ambari.server.scheduler.ExecutionScheduleManager.DEFAULT_API_PATH) + ".*");

    @com.google.inject.Inject
    public ExecutionScheduleManager(org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.scheduler.ExecutionScheduler executionScheduler, org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor, com.google.gson.Gson gson) {
        this.configuration = configuration;
        this.executionScheduler = executionScheduler;
        this.tokenStorage = tokenStorage;
        this.clusters = clusters;
        this.actionDBAccessor = actionDBAccessor;
        this.gson = gson;
        try {
            buildApiClient();
        } catch (java.security.NoSuchAlgorithmException | java.security.KeyManagementException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    protected void buildApiClient() throws java.security.NoSuchAlgorithmException, java.security.KeyManagementException {
        com.sun.jersey.api.client.Client client;
        java.lang.String pattern;
        java.lang.String url;
        if (configuration.getApiSSLAuthentication()) {
            pattern = "https://localhost:%s/";
            url = java.lang.String.format(pattern, configuration.getClientSSLApiPort());
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{ new javax.net.ssl.X509TrustManager() {
                @java.lang.Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, java.lang.String s) throws java.security.cert.CertificateException {
                }

                @java.lang.Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, java.lang.String s) throws java.security.cert.CertificateException {
                }

                @java.lang.Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } };
            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new com.sun.jersey.client.urlconnection.HTTPSProperties(new javax.net.ssl.HostnameVerifier() {
                @java.lang.Override
                public boolean verify(java.lang.String s, javax.net.ssl.SSLSession sslSession) {
                    return true;
                }
            }, sc));
            client = com.sun.jersey.api.client.Client.create(config);
        } else {
            client = com.sun.jersey.api.client.Client.create();
            pattern = "http://localhost:%s/";
            url = java.lang.String.format(pattern, configuration.getClientApiPort());
        }
        this.ambariClient = client;
        this.ambariWebResource = client.resource(url);
        com.sun.jersey.api.client.filter.ClientFilter csrfFilter = new com.sun.jersey.api.client.filter.CsrfProtectionFilter("RequestSchedule");
        com.sun.jersey.api.client.filter.ClientFilter tokenFilter = new org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter(tokenStorage);
        ambariClient.addFilter(csrfFilter);
        ambariClient.addFilter(tokenFilter);
    }

    public void start() {
        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.info("Starting scheduler");
        try {
            executionScheduler.startScheduler(configuration.getExecutionSchedulerStartDelay());
            schedulerAvailable = true;
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.warn("Unable to start scheduler. No recurring tasks will be " + "scheduled.");
        }
    }

    public void stop() {
        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.info("Stopping scheduler");
        schedulerAvailable = false;
        try {
            executionScheduler.stopScheduler();
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.warn("Unable to stop scheduler. No new recurring tasks will be " + "scheduled.");
        }
    }

    public boolean isSchedulerAvailable() {
        return schedulerAvailable;
    }

    public void scheduleJob(org.quartz.Trigger trigger) {
        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Scheduling job: {}", trigger.getJobKey());
        if (isSchedulerAvailable()) {
            try {
                executionScheduler.scheduleJob(trigger);
            } catch (org.quartz.SchedulerException e) {
                org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.error("Unable to add trigger for execution job: " + trigger.getJobKey(), e);
            }
        } else {
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.error("Scheduler unavailable, cannot schedule jobs.");
        }
    }

    public boolean continueOnMisfire(org.quartz.JobExecutionContext jobExecutionContext) {
        if (jobExecutionContext != null) {
            java.util.Date scheduledTime = jobExecutionContext.getScheduledFireTime();
            java.lang.Long diff = org.apache.ambari.server.utils.DateUtils.getDateDifferenceInMinutes(scheduledTime);
            return diff < configuration.getExecutionSchedulerMisfireToleration();
        }
        return true;
    }

    private long getFirstJobOrderId(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution) throws org.apache.ambari.server.AmbariException {
        java.lang.Long firstBatchOrderId = null;
        org.apache.ambari.server.state.scheduler.Batch batch = requestExecution.getBatch();
        if (batch != null) {
            java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
            if (batchRequests != null) {
                java.util.Collections.sort(batchRequests);
                java.util.ListIterator<org.apache.ambari.server.state.scheduler.BatchRequest> iterator = batchRequests.listIterator();
                firstBatchOrderId = iterator.next().getOrderId();
            }
        }
        if (firstBatchOrderId == null) {
            throw new org.apache.ambari.server.AmbariException("Can't schedule RequestExecution with no batches");
        }
        return firstBatchOrderId;
    }

    public void scheduleAllBatches(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution) throws org.apache.ambari.server.AmbariException {
        java.lang.Long firstBatchOrderId = getFirstJobOrderId(requestExecution);
        scheduleBatch(requestExecution, firstBatchOrderId);
    }

    public void scheduleBatch(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution, long startingBatchOrderId) throws org.apache.ambari.server.AmbariException {
        if (!isSchedulerAvailable()) {
            throw new org.apache.ambari.server.AmbariException("Scheduler unavailable.");
        }
        try {
            if (!executionScheduler.isSchedulerStarted()) {
                executionScheduler.startScheduler(null);
            }
        } catch (org.quartz.SchedulerException e) {
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.error("Unable to determine scheduler state.", e);
            throw new org.apache.ambari.server.AmbariException("Scheduler unavailable.");
        }
        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Scheduling jobs starting from " + startingBatchOrderId);
        org.quartz.JobDetail firstJobDetail = persistBatch(requestExecution, startingBatchOrderId);
        if (firstJobDetail == null) {
            throw new org.apache.ambari.server.AmbariException("Unable to schedule jobs. firstJobDetail = " + firstJobDetail);
        }
        java.lang.Integer failedCount = countFailedTasksBeforeStartingBatch(requestExecution, startingBatchOrderId);
        org.apache.ambari.server.state.scheduler.Schedule schedule = requestExecution.getSchedule();
        if (schedule != null) {
            java.lang.String triggerExpression = schedule.getScheduleExpression();
            java.util.Date startDate = null;
            java.util.Date endDate = null;
            try {
                java.lang.String startTime = schedule.getStartTime();
                java.lang.String endTime = schedule.getEndTime();
                startDate = ((startTime != null) && (!startTime.isEmpty())) ? org.apache.ambari.server.utils.DateUtils.convertToDate(startTime) : new java.util.Date();
                endDate = ((endTime != null) && (!endTime.isEmpty())) ? org.apache.ambari.server.utils.DateUtils.convertToDate(endTime) : null;
            } catch (java.text.ParseException e) {
                org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.error("Unable to parse startTime / endTime.", e);
            }
            org.quartz.Trigger trigger = TriggerBuilder.newTrigger().withIdentity((org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUEST_EXECUTION_TRIGGER_PREFIX + "-") + requestExecution.getId(), org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_TRIGGER_GROUP).withSchedule(CronScheduleBuilder.cronSchedule(triggerExpression).withMisfireHandlingInstructionFireAndProceed()).forJob(firstJobDetail).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY, failedCount).startAt(startDate).endAt(endDate).build();
            try {
                executionScheduler.scheduleJob(trigger);
                org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Scheduled trigger next fire time: {}", trigger.getNextFireTime());
            } catch (org.quartz.SchedulerException e) {
                org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.error("Unable to schedule request execution.", e);
                throw new org.apache.ambari.server.AmbariException(e.getMessage());
            }
        } else {
            org.quartz.Trigger trigger = TriggerBuilder.newTrigger().forJob(firstJobDetail).withIdentity((org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUEST_EXECUTION_TRIGGER_PREFIX + "-") + requestExecution.getId(), org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_TRIGGER_GROUP).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY, failedCount).startNow().build();
            try {
                executionScheduler.scheduleJob(trigger);
                org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Scheduled trigger next fire time: {}", trigger.getNextFireTime());
            } catch (org.quartz.SchedulerException e) {
                org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.error("Unable to schedule request execution.", e);
                throw new org.apache.ambari.server.AmbariException(e.getMessage());
            }
        }
    }

    private java.lang.Integer countFailedTasksBeforeStartingBatch(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution, long startingBatchOrderId) throws org.apache.ambari.server.AmbariException {
        int result = 0;
        org.apache.ambari.server.state.scheduler.Batch batch = requestExecution.getBatch();
        if (batch != null) {
            java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
            if (batchRequests != null) {
                java.util.Collections.sort(batchRequests);
                for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batchRequests) {
                    if (batchRequest.getOrderId() >= startingBatchOrderId)
                        break;

                    if (batchRequest.getRequestId() != null) {
                        org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse = getBatchRequestResponse(batchRequest.getRequestId(), requestExecution.getClusterName());
                        if (batchRequestResponse != null) {
                            result += (batchRequestResponse.getFailedTaskCount() + batchRequestResponse.getAbortedTaskCount()) + batchRequestResponse.getTimedOutTaskCount();
                        }
                    }
                }
            }
        }
        return result;
    }

    private org.quartz.JobDetail persistBatch(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution, long startingBatchOrderId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.scheduler.Batch batch = requestExecution.getBatch();
        org.quartz.JobDetail jobDetail = null;
        if (batch != null) {
            java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
            if (batchRequests != null) {
                java.util.Collections.sort(batchRequests);
                java.util.ListIterator<org.apache.ambari.server.state.scheduler.BatchRequest> iterator = batchRequests.listIterator(batchRequests.size());
                java.lang.String nextJobName = null;
                long nextBatchOrderId = java.lang.Integer.MAX_VALUE / 2;
                while ((nextBatchOrderId != startingBatchOrderId) && iterator.hasPrevious()) {
                    org.apache.ambari.server.state.scheduler.BatchRequest batchRequest = iterator.previous();
                    java.lang.String jobName = getJobName(requestExecution.getId(), batchRequest.getOrderId());
                    java.lang.Integer separationSeconds = requestExecution.getBatch().getBatchSettings().getBatchSeparationInSeconds();
                    jobDetail = JobBuilder.newJob(org.apache.ambari.server.state.scheduler.BatchRequestJob.class).withIdentity(jobName, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP).usingJobData(org.apache.ambari.server.scheduler.ExecutionJob.NEXT_EXECUTION_JOB_NAME_KEY, nextJobName).usingJobData(org.apache.ambari.server.scheduler.ExecutionJob.NEXT_EXECUTION_JOB_GROUP_KEY, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_EXECUTION_ID_KEY, requestExecution.getId()).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_BATCH_ID_KEY, batchRequest.getOrderId()).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_CLUSTER_NAME_KEY, requestExecution.getClusterName()).usingJobData(org.apache.ambari.server.state.scheduler.BatchRequestJob.NEXT_EXECUTION_SEPARATION_SECONDS, separationSeconds != null ? separationSeconds : 0).storeDurably().build();
                    try {
                        executionScheduler.addJob(jobDetail);
                    } catch (org.quartz.SchedulerException e) {
                        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.error("Failed to add job detail. " + batchRequest, e);
                    }
                    nextJobName = jobName;
                    nextBatchOrderId = batchRequest.getOrderId();
                } 
            }
        }
        return jobDetail;
    }

    protected java.lang.String getJobName(java.lang.Long executionId, java.lang.Long orderId) {
        return (((org.apache.ambari.server.scheduler.ExecutionScheduleManager.BATCH_REQUEST_JOB_PREFIX + "-") + executionId) + "-") + orderId;
    }

    public void updateBatchSchedule(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.scheduler.BatchRequest activeBatch = calculateActiveBatch(requestExecution);
        if (activeBatch == null) {
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.warn("Ignoring RequestExecution status update since all batches has been executed");
            return;
        }
        if (requestExecution.getStatus().equals(org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED.name())) {
            scheduleBatch(requestExecution, activeBatch.getOrderId());
        } else if (requestExecution.getStatus().equals(org.apache.ambari.server.state.scheduler.RequestExecution.Status.PAUSED.name()) || requestExecution.getStatus().equals(org.apache.ambari.server.state.scheduler.RequestExecution.Status.ABORTED.name())) {
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.info(((("Request execution status changed to " + requestExecution.getStatus()) + " for request schedule ") + requestExecution.getId()) + ". Deleting related jobs.");
            deleteJobs(requestExecution, activeBatch.getOrderId());
            java.util.Collection<java.lang.Long> requestIDsToAbort = requestExecution.getBatchRequestRequestsIDs(activeBatch.getOrderId());
            for (java.lang.Long requestId : requestIDsToAbort) {
                if (requestId == null)
                    continue;

                abortRequestById(requestExecution, requestId);
            }
        }
    }

    private org.apache.ambari.server.state.scheduler.BatchRequest calculateActiveBatch(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution) {
        org.apache.ambari.server.state.scheduler.BatchRequest result = null;
        org.apache.ambari.server.state.scheduler.Batch batch = requestExecution.getBatch();
        if (batch != null) {
            java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
            if (batchRequests != null) {
                java.util.Collections.sort(batchRequests);
                java.util.ListIterator<org.apache.ambari.server.state.scheduler.BatchRequest> iterator = batchRequests.listIterator();
                do {
                    result = iterator.next();
                } while (((iterator.hasNext() && (result.getStatus() != null)) && org.apache.ambari.server.actionmanager.HostRoleStatus.getCompletedStates().contains(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(result.getStatus()))) && (!org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.name().equals(result.getStatus())) );
            }
        }
        if ((((result != null) && (result.getStatus() != null)) && org.apache.ambari.server.actionmanager.HostRoleStatus.getCompletedStates().contains(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(result.getStatus()))) && (!org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.name().equals(result.getStatus()))) {
            return null;
        }
        return result;
    }

    public void validateSchedule(org.apache.ambari.server.state.scheduler.Schedule schedule) throws org.apache.ambari.server.AmbariException {
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        if (!schedule.isEmpty()) {
            if ((schedule.getStartTime() != null) && (!schedule.getStartTime().isEmpty())) {
                try {
                    startDate = org.apache.ambari.server.utils.DateUtils.convertToDate(schedule.getStartTime());
                } catch (java.text.ParseException pe) {
                    throw new org.apache.ambari.server.AmbariException(((("Start time in invalid format. startTime " + "= ") + schedule.getStartTime()) + ", Allowed format = ") + org.apache.ambari.server.utils.DateUtils.ALLOWED_DATE_FORMAT);
                }
            }
            if ((schedule.getEndTime() != null) && (!schedule.getEndTime().isEmpty())) {
                try {
                    endDate = org.apache.ambari.server.utils.DateUtils.convertToDate(schedule.getEndTime());
                } catch (java.text.ParseException pe) {
                    throw new org.apache.ambari.server.AmbariException(((("End time in invalid format. endTime " + "= ") + schedule.getEndTime()) + ", Allowed format = ") + org.apache.ambari.server.utils.DateUtils.ALLOWED_DATE_FORMAT);
                }
            }
            if (endDate != null) {
                if (endDate.before(new java.util.Date())) {
                    throw new org.apache.ambari.server.AmbariException(("End date should be in the future. " + "endDate = ") + endDate);
                }
                if ((startDate != null) && endDate.before(startDate)) {
                    throw new org.apache.ambari.server.AmbariException(((("End date cannot be before start date. " + "startDate = ") + startDate) + ", endDate = ") + endDate);
                }
            }
            java.lang.String cronExpression = schedule.getScheduleExpression();
            if ((cronExpression != null) && (!cronExpression.trim().isEmpty())) {
                if (!org.quartz.CronExpression.isValidExpression(cronExpression)) {
                    throw new org.apache.ambari.server.AmbariException(("Invalid non-empty cron expression " + "provided. ") + cronExpression);
                }
            }
        }
    }

    public void deleteAllJobs(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution) throws org.apache.ambari.server.AmbariException {
        java.lang.Long firstBatchOrderId = getFirstJobOrderId(requestExecution);
        deleteJobs(requestExecution, firstBatchOrderId);
    }

    public void deleteJobs(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution, java.lang.Long startingBatchOrderId) throws org.apache.ambari.server.AmbariException {
        if (!isSchedulerAvailable()) {
            throw new org.apache.ambari.server.AmbariException("Scheduler unavailable.");
        }
        org.apache.ambari.server.state.scheduler.Batch batch = requestExecution.getBatch();
        if (batch != null) {
            java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
            if (batchRequests != null) {
                java.util.Collections.sort(batchRequests);
                for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batchRequests) {
                    if (batchRequest.getOrderId() < startingBatchOrderId)
                        continue;

                    java.lang.String jobName = getJobName(requestExecution.getId(), batchRequest.getOrderId());
                    org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Deleting Job, jobName = {}", jobName);
                    try {
                        executionScheduler.deleteJob(org.quartz.JobKey.jobKey(jobName, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP));
                    } catch (org.quartz.SchedulerException e) {
                        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.warn("Unable to delete job, " + jobName, e);
                        throw new org.apache.ambari.server.AmbariException(e.getMessage());
                    }
                }
            }
        }
    }

    public java.lang.Long executeBatchRequest(long executionId, long batchId, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        java.lang.String type = null;
        java.lang.String uri = null;
        java.lang.String body = null;
        try {
            org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = clusters.getCluster(clusterName).getAllRequestExecutions().get(executionId);
            org.apache.ambari.server.state.scheduler.BatchRequest batchRequest = requestExecution.getBatchRequest(batchId);
            type = batchRequest.getType();
            uri = batchRequest.getUri();
            body = requestExecution.getRequestBody(batchId);
            org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse = performApiRequest(uri, body, type, requestExecution.getAuthenticatedUserId());
            updateBatchRequest(executionId, batchId, clusterName, batchRequestResponse, false);
            if (batchRequestResponse.getRequestId() != null) {
                actionDBAccessor.setSourceScheduleForRequest(batchRequestResponse.getRequestId(), executionId);
            }
            batchRequest.setRequestId(batchRequestResponse.getRequestId());
            return batchRequestResponse.getRequestId();
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.AmbariException("Exception occurred while performing request", e);
        }
    }

    public org.apache.ambari.server.state.scheduler.BatchRequestResponse getBatchRequestResponse(java.lang.Long requestId, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.commons.lang.text.StrBuilder sb = new org.apache.commons.lang.text.StrBuilder();
        sb.append(org.apache.ambari.server.scheduler.ExecutionScheduleManager.DEFAULT_API_PATH).append("/clusters/").append(clusterName).append("/requests/").append(requestId);
        return performApiGetRequest(sb.toString(), true);
    }

    protected org.apache.ambari.server.controller.spi.RequestStatus abortRequestById(org.apache.ambari.server.state.scheduler.RequestExecution requestExecution, java.lang.Long requestId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Aborting request " + requestId);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Request);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, requestExecution.getClusterName());
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORT_REASON_PROPERTY_ID, "Request execution status changed to " + requestExecution.getStatus());
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID, java.lang.Long.toString(requestId));
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.name());
        org.apache.ambari.server.controller.spi.Request request = new org.apache.ambari.server.controller.internal.RequestImpl(java.util.Collections.emptySet(), java.util.Collections.singleton(properties), java.util.Collections.emptyMap(), null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID).equals(requestExecution.getClusterName()).and().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals(java.lang.Long.toString(requestId)).toPredicate();
        try {
            return provider.updateResources(request, predicate);
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.AmbariException("Error while aborting the request.", e);
        }
    }

    private org.apache.ambari.server.state.scheduler.BatchRequestResponse convertToBatchRequestResponse(com.sun.jersey.api.client.ClientResponse clientResponse) {
        org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse = new org.apache.ambari.server.state.scheduler.BatchRequestResponse();
        int retCode = clientResponse.getStatus();
        batchRequestResponse.setReturnCode(retCode);
        java.lang.String responseString = clientResponse.getEntity(java.lang.String.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Processing API response: status={}, body={}", retCode, responseString);
        java.util.Map<java.lang.String, java.lang.Object> httpResponseMap;
        try {
            httpResponseMap = gson.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(responseString, java.util.Map.class);
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Processing responce as JSON");
        } catch (com.google.gson.JsonSyntaxException e) {
            org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.debug("Response is not valid JSON object. Recording as is");
            httpResponseMap = new java.util.HashMap<>();
            httpResponseMap.put("message", responseString);
        }
        if (retCode < 300) {
            if (httpResponseMap == null) {
                batchRequestResponse.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
                return batchRequestResponse;
            }
            java.util.Map requestMap = null;
            java.lang.Object requestMapObject = httpResponseMap.get("Requests");
            if (requestMapObject instanceof java.util.Map) {
                requestMap = ((java.util.Map) (requestMapObject));
            }
            if (requestMap != null) {
                batchRequestResponse.setRequestId(((java.lang.Double) (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_ID_KEY))).longValue());
                java.lang.String status = null;
                if (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_STATUS_KEY) != null) {
                    status = requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_STATUS_KEY).toString();
                }
                if (requestMap.get("status") != null) {
                    status = requestMap.get("status").toString();
                }
                if (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_ABORTED_TASKS_KEY) != null) {
                    batchRequestResponse.setAbortedTaskCount(((java.lang.Double) (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_ABORTED_TASKS_KEY))).intValue());
                }
                if (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_FAILED_TASKS_KEY) != null) {
                    batchRequestResponse.setFailedTaskCount(((java.lang.Double) (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_FAILED_TASKS_KEY))).intValue());
                }
                if (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_TIMEDOUT_TASKS_KEY) != null) {
                    batchRequestResponse.setTimedOutTaskCount(((java.lang.Double) (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_TIMEDOUT_TASKS_KEY))).intValue());
                }
                if (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_TOTAL_TASKS_KEY) != null) {
                    batchRequestResponse.setTotalTaskCount(((java.lang.Double) (requestMap.get(org.apache.ambari.server.scheduler.ExecutionScheduleManager.REQUESTS_TOTAL_TASKS_KEY))).intValue());
                }
                batchRequestResponse.setStatus(status);
            }
        } else {
            batchRequestResponse.setReturnMessage(((java.lang.String) (httpResponseMap.get("message"))));
            batchRequestResponse.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        }
        return batchRequestResponse;
    }

    public java.lang.String getBatchRequestStatus(java.lang.Long executionId, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = cluster.getAllRequestExecutions().get(executionId);
        if (requestExecution == null) {
            throw new org.apache.ambari.server.AmbariException("Unable to find request schedule with id = " + executionId);
        }
        return requestExecution.getStatus();
    }

    public void updateBatchRequest(long executionId, long batchId, java.lang.String clusterName, org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse, boolean statusOnly) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = cluster.getAllRequestExecutions().get(executionId);
        if (requestExecution == null) {
            throw new org.apache.ambari.server.AmbariException("Unable to find request schedule with id = " + executionId);
        }
        requestExecution.updateBatchRequest(batchId, batchRequestResponse, statusOnly);
    }

    protected org.apache.ambari.server.state.scheduler.BatchRequestResponse performUriRequest(java.lang.String url, java.lang.String body, java.lang.String method) {
        com.sun.jersey.api.client.ClientResponse response;
        try {
            response = ambariClient.resource(url).entity(body).method(method, com.sun.jersey.api.client.ClientResponse.class);
        } catch (com.sun.jersey.api.client.UniformInterfaceException e) {
            response = e.getResponse();
        }
        return convertToBatchRequestResponse(response);
    }

    protected org.apache.ambari.server.state.scheduler.BatchRequestResponse performApiGetRequest(java.lang.String relativeUri, boolean queryAllFields) {
        com.sun.jersey.api.client.WebResource webResource = extendApiResource(ambariWebResource, relativeUri);
        if (queryAllFields) {
            webResource = webResource.queryParam("fields", "*");
        }
        com.sun.jersey.api.client.ClientResponse response;
        try {
            response = webResource.get(com.sun.jersey.api.client.ClientResponse.class);
        } catch (com.sun.jersey.api.client.UniformInterfaceException e) {
            response = e.getResponse();
        }
        return convertToBatchRequestResponse(response);
    }

    protected org.apache.ambari.server.state.scheduler.BatchRequestResponse performApiRequest(java.lang.String relativeUri, java.lang.String body, java.lang.String method, java.lang.Integer userId) {
        com.sun.jersey.api.client.ClientResponse response;
        try {
            response = extendApiResource(ambariWebResource, relativeUri).header(org.apache.ambari.server.scheduler.ExecutionScheduleManager.USER_ID_HEADER, userId).method(method, com.sun.jersey.api.client.ClientResponse.class, body);
        } catch (com.sun.jersey.api.client.UniformInterfaceException e) {
            response = e.getResponse();
        }
        return convertToBatchRequestResponse(response);
    }

    public boolean hasToleranceThresholdExceeded(java.lang.Long executionId, java.lang.String clusterName, java.util.Map<java.lang.String, java.lang.Integer> taskCounts) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = cluster.getAllRequestExecutions().get(executionId);
        if (requestExecution == null) {
            throw new org.apache.ambari.server.AmbariException("Unable to find request schedule with id = " + executionId);
        }
        org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = requestExecution.getBatch().getBatchSettings();
        boolean result = false;
        if (batchSettings != null) {
            if (batchSettings.getTaskFailureToleranceLimit() != null) {
                result = taskCounts.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY) > batchSettings.getTaskFailureToleranceLimit();
            }
            if (batchSettings.getTaskFailureToleranceLimitPerBatch() != null) {
                result = result || (taskCounts.get(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_IN_CURRENT_BATCH_KEY) > batchSettings.getTaskFailureToleranceLimitPerBatch());
            }
        }
        return result;
    }

    public void finalizeBatch(long executionId, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = cluster.getAllRequestExecutions().get(executionId);
        if (requestExecution == null) {
            throw new org.apache.ambari.server.AmbariException("Unable to find request schedule with id = " + executionId);
        }
        org.apache.ambari.server.state.scheduler.Batch batch = requestExecution.getBatch();
        org.apache.ambari.server.state.scheduler.BatchRequest firstBatchRequest = null;
        if (batch != null) {
            java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = batch.getBatchRequests();
            if ((batchRequests != null) && (batchRequests.size() > 0)) {
                java.util.Collections.sort(batchRequests);
                firstBatchRequest = batchRequests.get(0);
            }
        }
        boolean markCompleted = false;
        if (firstBatchRequest != null) {
            java.lang.String jobName = getJobName(executionId, firstBatchRequest.getOrderId());
            org.quartz.JobKey jobKey = org.quartz.JobKey.jobKey(jobName, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP);
            org.quartz.JobDetail jobDetail;
            try {
                jobDetail = executionScheduler.getJobDetail(jobKey);
            } catch (org.quartz.SchedulerException e) {
                org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.warn("Unable to retrieve job details from scheduler. job: " + jobKey);
                e.printStackTrace();
                return;
            }
            if (jobDetail != null) {
                try {
                    java.util.List<? extends org.quartz.Trigger> triggers = executionScheduler.getTriggersForJob(jobKey);
                    if ((triggers != null) && (triggers.size() > 0)) {
                        if (triggers.size() > 1) {
                            throw new org.apache.ambari.server.AmbariException(("Too many triggers defined for job. " + "job: ") + jobKey);
                        }
                        org.quartz.Trigger trigger = triggers.get(0);
                        if ((!trigger.mayFireAgain()) || ((trigger.getFinalFireTime() != null) && (!org.apache.ambari.server.utils.DateUtils.isFutureTime(trigger.getFinalFireTime())))) {
                            markCompleted = true;
                        }
                    } else {
                        markCompleted = true;
                    }
                } catch (org.quartz.SchedulerException e) {
                    org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.warn("Unable to retrieve triggers for job: " + jobKey);
                    e.printStackTrace();
                    return;
                }
            }
        }
        if (markCompleted) {
            requestExecution.updateStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.COMPLETED);
        }
    }

    protected com.sun.jersey.api.client.WebResource extendApiResource(com.sun.jersey.api.client.WebResource webResource, java.lang.String relativeUri) {
        com.sun.jersey.api.client.WebResource result = webResource;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(relativeUri) && (!org.apache.ambari.server.scheduler.ExecutionScheduleManager.CONTAINS_API_VERSION_PATTERN.matcher(relativeUri).matches())) {
            result = webResource.path(org.apache.ambari.server.scheduler.ExecutionScheduleManager.DEFAULT_API_PATH);
        }
        return result.path(relativeUri);
    }

    public void pauseAfterBatchIfNeeded(long executionId, long batchId, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = cluster.getAllRequestExecutions().get(executionId);
        if (requestExecution == null) {
            throw new org.apache.ambari.server.AmbariException("Unable to find request schedule with id = " + executionId);
        }
        org.apache.ambari.server.state.scheduler.Batch batch = requestExecution.getBatch();
        if (batch != null) {
            org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = batch.getBatchSettings();
            if (batchSettings != null) {
                if ((org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED.name().equals(requestExecution.getStatus()) && (getFirstJobOrderId(requestExecution) == batchId)) && batchSettings.isPauseAfterFirstBatch()) {
                    org.apache.ambari.server.scheduler.ExecutionScheduleManager.LOG.info("Auto pausing the scheduled request after first batch. Scheduled request ID : " + executionId);
                    requestExecution.updateStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.PAUSED);
                }
            }
        }
    }
}