package org.apache.ambari.view.pig.resources.jobs;
import javax.ws.rs.WebApplicationException;
import org.apache.hadoop.fs.FSDataOutputStream;
public class JobResourceManager extends org.apache.ambari.view.pig.resources.PersonalCRUDResourceManager<org.apache.ambari.view.pig.resources.jobs.models.PigJob> {
    protected org.apache.ambari.view.pig.templeton.client.TempletonApi api;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.jobs.JobResourceManager.class);

    public JobResourceManager(org.apache.ambari.view.ViewContext context) {
        super(org.apache.ambari.view.pig.resources.jobs.models.PigJob.class, context);
        setupPolling();
    }

    private void setupPolling() {
        java.util.List<org.apache.ambari.view.pig.resources.jobs.models.PigJob> notCompleted = this.readAll(new org.apache.ambari.view.pig.persistence.utils.FilteringStrategy() {
            @java.lang.Override
            public boolean isConform(org.apache.ambari.view.pig.persistence.utils.Indexed item) {
                org.apache.ambari.view.pig.resources.jobs.models.PigJob job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (item));
                return job.isInProgress();
            }
        });
        for (org.apache.ambari.view.pig.resources.jobs.models.PigJob job : notCompleted) {
            org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.pollJob(this, job);
        }
    }

    @java.lang.Override
    public org.apache.ambari.view.pig.resources.jobs.models.PigJob create(org.apache.ambari.view.pig.resources.jobs.models.PigJob object) {
        object.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTING);
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = super.create(object);
        org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug("Submitting job...");
        try {
            submitJob(object);
        } catch (java.lang.RuntimeException e) {
            object.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMIT_FAILED);
            save(object);
            org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug("Job submit FAILED");
            throw e;
        }
        org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug("Job submit OK");
        object.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTED);
        save(object);
        return job;
    }

    public void killJob(org.apache.ambari.view.pig.resources.jobs.models.PigJob object) throws java.io.IOException {
        org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug("Killing job...");
        if (object.getJobId() != null) {
            try {
                org.apache.ambari.view.pig.utils.UserLocalObjects.getTempletonApi(context).killJob(object.getJobId());
            } catch (java.io.IOException e) {
                org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug("Job kill FAILED");
                throw e;
            }
            org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug("Job kill OK");
        } else {
            org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug("Job was not submitted, ignoring kill request...");
        }
    }

    private void submitJob(org.apache.ambari.view.pig.resources.jobs.models.PigJob job) {
        java.lang.String date = new java.text.SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new java.util.Date());
        java.lang.String jobsBaseDir = context.getProperties().get("jobs.dir");
        java.lang.String storeBaseDir = context.getProperties().get("store.dir");
        if (((storeBaseDir == null) || (storeBaseDir.compareTo("null") == 0)) || (storeBaseDir.compareTo("") == 0))
            storeBaseDir = context.getProperties().get("jobs.dir");

        java.lang.String jobNameCleaned = job.getTitle().toLowerCase().replaceAll("[^a-zA-Z0-9 ]+", "").replace(" ", "_");
        java.lang.String storedir = java.lang.String.format(storeBaseDir + "/%s_%s", jobNameCleaned, date);
        java.lang.String statusdir = java.lang.String.format(jobsBaseDir + "/%s_%s", jobNameCleaned, date);
        java.lang.String newPigScriptPath = storedir + "/script.pig";
        java.lang.String newSourceFilePath = storedir + "/source.pig";
        java.lang.String newPythonScriptPath = storedir + "/udf.py";
        java.lang.String templetonParamsFilePath = storedir + "/params";
        try {
            if ((job.getSourceFileContent() != null) && (!job.getSourceFileContent().isEmpty())) {
                java.lang.String sourceFileContent = job.getSourceFileContent();
                job.setSourceFileContent(null);
                save(job);
                org.apache.hadoop.fs.FSDataOutputStream stream = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).create(newSourceFilePath, true);
                stream.writeBytes(sourceFileContent);
                stream.close();
            } else if ((job.getSourceFile() != null) && (!job.getSourceFile().isEmpty())) {
                org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).copy(job.getSourceFile(), newSourceFilePath);
            }
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create/copy source file: " + e.toString(), e);
        } catch (java.lang.InterruptedException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create/copy source file: " + e.toString(), e);
        } catch (org.apache.ambari.view.utils.hdfs.HdfsApiException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException((("Can't copy source file from " + job.getSourceFile()) + " to ") + newPigScriptPath, e);
        }
        try {
            if ((job.getForcedContent() != null) && (!job.getForcedContent().isEmpty())) {
                java.lang.String forcedContent = job.getForcedContent();
                java.lang.String defaultUrl = context.getProperties().get("webhdfs.url");
                java.net.URI uri = new java.net.URI(defaultUrl);
                if (uri.getScheme().equals("webhdfs")) {
                    defaultUrl = "hdfs://" + uri.getHost();
                }
                forcedContent = forcedContent.replace("${sourceFile}", defaultUrl + newSourceFilePath);
                job.setForcedContent(null);
                save(job);
                org.apache.hadoop.fs.FSDataOutputStream stream = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).create(newPigScriptPath, true);
                stream.writeBytes(forcedContent);
                stream.close();
            } else {
                org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).copy(job.getPigScript(), newPigScriptPath);
            }
        } catch (org.apache.ambari.view.utils.hdfs.HdfsApiException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException((("Can't copy pig script file from " + job.getPigScript()) + " to ") + newPigScriptPath, e);
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create/copy pig script file: " + e.getMessage(), e);
        } catch (java.lang.InterruptedException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create/copy pig script file: " + e.getMessage(), e);
        } catch (java.net.URISyntaxException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create/copy pig script file: " + e.getMessage(), e);
        }
        if ((job.getPythonScript() != null) && (!job.getPythonScript().isEmpty())) {
            try {
                org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).copy(job.getPythonScript(), newPythonScriptPath);
            } catch (org.apache.ambari.view.utils.hdfs.HdfsApiException e) {
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException((("Can't copy python udf script file from " + job.getPythonScript()) + " to ") + newPythonScriptPath);
            } catch (java.io.IOException e) {
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create/copy python udf file: " + e.toString(), e);
            } catch (java.lang.InterruptedException e) {
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create/copy python udf file: " + e.toString(), e);
            }
        }
        try {
            org.apache.hadoop.fs.FSDataOutputStream stream = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).create(templetonParamsFilePath, true);
            if (job.getTempletonArguments() != null) {
                stream.writeBytes(job.getTempletonArguments());
            }
            stream.close();
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create params file: " + e.toString(), e);
        } catch (java.lang.InterruptedException e) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Can't create params file: " + e.toString(), e);
        }
        job.setPigScript(newPigScriptPath);
        job.setStatusDir(statusdir);
        job.setDateStarted(java.lang.System.currentTimeMillis() / 1000L);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data;
        try {
            data = org.apache.ambari.view.pig.utils.UserLocalObjects.getTempletonApi(context).runPigQuery(new java.io.File(job.getPigScript()), statusdir, job.getTempletonArguments());
            if (data.id != null) {
                job.setJobId(data.id);
                org.apache.ambari.view.pig.resources.jobs.utils.JobPolling.pollJob(this, job);
            } else {
                throw new org.apache.ambari.view.utils.ambari.AmbariApiException("Cannot get id for the Job.");
            }
        } catch (java.io.IOException templetonBadResponse) {
            java.lang.String msg = java.lang.String.format("Templeton bad response: %s", templetonBadResponse.toString());
            org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug(msg);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(msg, templetonBadResponse);
        }
    }

    public void retrieveJobStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob job) {
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo info;
        try {
            info = org.apache.ambari.view.pig.utils.UserLocalObjects.getTempletonApi(context).checkJob(job.getJobId());
        } catch (java.io.IOException e) {
            org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.warn(java.lang.String.format("IO Exception: %s", e));
            return;
        }
        if ((info.status != null) && info.status.containsKey("runState")) {
            java.lang.Long time = java.lang.System.currentTimeMillis() / 1000L;
            java.lang.Long currentDuration = time - job.getDateStarted();
            int runState = ((java.lang.Double) (info.status.get("runState"))).intValue();
            boolean isStatusChanged = false;
            switch (runState) {
                case org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_KILLED :
                    org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug(java.lang.String.format("Job KILLED: %s", job.getJobId()));
                    isStatusChanged = !job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_KILLED);
                    job.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_KILLED);
                    break;
                case org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_FAILED :
                    org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug(java.lang.String.format("Job FAILED: %s", job.getJobId()));
                    isStatusChanged = !job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_FAILED);
                    job.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_FAILED);
                    break;
                case org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_PREP :
                case org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_RUNNING :
                    isStatusChanged = !job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_RUNNING);
                    job.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_RUNNING);
                    break;
                case org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_SUCCEEDED :
                    org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug(java.lang.String.format("Job COMPLETED: %s", job.getJobId()));
                    isStatusChanged = !job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_COMPLETED);
                    job.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_COMPLETED);
                    break;
                default :
                    org.apache.ambari.view.pig.resources.jobs.JobResourceManager.LOG.debug(java.lang.String.format("Job in unknown state: %s", job.getJobId()));
                    isStatusChanged = !job.getStatus().equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_UNKNOWN);
                    job.setStatus(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_UNKNOWN);
                    break;
            }
            if (isStatusChanged) {
                job.setDuration(currentDuration);
            }
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d+");
        java.util.regex.Matcher matcher = null;
        if (info.percentComplete != null) {
            matcher = pattern.matcher(info.percentComplete);
        }
        if ((matcher != null) && matcher.find()) {
            job.setPercentComplete(java.lang.Integer.valueOf(matcher.group()));
        } else {
            job.setPercentComplete(null);
        }
        save(job);
    }

    public static void webhcatSmokeTest(org.apache.ambari.view.ViewContext context) {
        try {
            org.apache.ambari.view.pig.templeton.client.TempletonApiFactory templetonApiFactory = new org.apache.ambari.view.pig.templeton.client.TempletonApiFactory(context);
            org.apache.ambari.view.pig.templeton.client.TempletonApi api = templetonApiFactory.connectToTempletonApi();
            api.status();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public static final int RUN_STATE_RUNNING = 1;

    public static final int RUN_STATE_SUCCEEDED = 2;

    public static final int RUN_STATE_FAILED = 3;

    public static final int RUN_STATE_PREP = 4;

    public static final int RUN_STATE_KILLED = 5;
}