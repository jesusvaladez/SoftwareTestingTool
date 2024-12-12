package org.apache.ambari.view.pig.resources.jobs;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONObject;
public class JobService extends org.apache.ambari.view.pig.services.BaseService {
    @com.google.inject.Inject
    org.apache.ambari.view.ViewResourceHandler handler;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.jobs.JobService.class);

    protected org.apache.ambari.view.pig.resources.jobs.JobResourceManager resourceManager = null;

    public synchronized org.apache.ambari.view.pig.resources.jobs.JobResourceManager getResourceManager() {
        if (resourceManager == null) {
            resourceManager = new org.apache.ambari.view.pig.resources.jobs.JobResourceManager(context);
        }
        return resourceManager;
    }

    public synchronized void setResourceManager(org.apache.ambari.view.pig.resources.jobs.JobResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{jobId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getJob(@javax.ws.rs.PathParam("jobId")
    java.lang.String jobId) {
        org.apache.ambari.view.pig.resources.jobs.JobService.LOG.info("Fetching job with id : {}", jobId);
        try {
            org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
            try {
                job = getResourceManager().read(jobId);
            } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", itemNotFound);
                throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
            }
            getResourceManager().retrieveJobStatus(job);
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("job", job);
            return javax.ws.rs.core.Response.ok(object).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{jobId}")
    public javax.ws.rs.core.Response killJob(@javax.ws.rs.PathParam("jobId")
    java.lang.String jobId, @javax.ws.rs.QueryParam("remove")
    final java.lang.String remove) throws java.io.IOException {
        org.apache.ambari.view.pig.resources.jobs.JobService.LOG.info("killing job : {}, remove : {}", jobId, remove);
        try {
            org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
            try {
                job = getResourceManager().read(jobId);
            } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", itemNotFound);
                throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(itemNotFound.getMessage(), itemNotFound);
            }
            getResourceManager().killJob(job);
            if ((remove != null) && (remove.compareTo("true") == 0)) {
                getResourceManager().delete(jobId);
            }
            return javax.ws.rs.core.Response.status(204).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{jobId}/notify")
    public javax.ws.rs.core.Response jobCompletionNotification(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("jobId")
    final java.lang.String jobId) {
        try {
            org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
            job = getResourceManager().ignorePermissions(new java.util.concurrent.Callable<org.apache.ambari.view.pig.resources.jobs.models.PigJob>() {
                public org.apache.ambari.view.pig.resources.jobs.models.PigJob call() throws java.lang.Exception {
                    org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
                    try {
                        job = getResourceManager().read(jobId);
                    } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                        org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", itemNotFound);
                        return null;
                    }
                    return job;
                }
            });
            if (job == null)
                throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(("Job with id '" + jobId) + "' not found", null);

            getResourceManager().retrieveJobStatus(job);
            return javax.ws.rs.core.Response.ok().build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{jobId}/results/{fileName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response jobExitCode(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("jobId")
    java.lang.String jobId, @javax.ws.rs.PathParam("fileName")
    java.lang.String fileName, @javax.ws.rs.QueryParam("page")
    java.lang.Long page) {
        org.apache.ambari.view.pig.resources.jobs.JobService.LOG.info("fetching results in fileName {} ", fileName);
        try {
            org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
            try {
                job = getResourceManager().read(jobId);
            } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", itemNotFound);
                throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(("Job with id '" + jobId) + "' not found", null);
            }
            java.lang.String filePath = (job.getStatusDir() + "/") + fileName;
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.debug("Reading file {}", filePath);
            org.apache.ambari.view.pig.utils.FilePaginator paginator = new org.apache.ambari.view.pig.utils.FilePaginator(filePath, context);
            if (page == null) {
                page = 0L;
            }
            org.apache.ambari.view.pig.resources.files.FileResource file = new org.apache.ambari.view.pig.resources.files.FileResource();
            file.setFilePath(filePath);
            file.setFileContent(paginator.readPage(page));
            file.setHasNext(paginator.pageCount() > (page + 1));
            file.setPage(page);
            file.setPageCount(paginator.pageCount());
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("file", file);
            return javax.ws.rs.core.Response.ok(object).status(200).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.io.IOException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(ex.getMessage(), ex);
        } catch (java.lang.InterruptedException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(ex.getMessage(), ex);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getJobList(@javax.ws.rs.QueryParam("scriptId")
    final java.lang.String scriptId) {
        org.apache.ambari.view.pig.resources.jobs.JobService.LOG.info("Fechting scriptId : {} ", scriptId);
        try {
            java.util.List allJobs = getResourceManager().readAll(new org.apache.ambari.view.pig.persistence.utils.OnlyOwnersFilteringStrategy(this.context.getUsername()) {
                @java.lang.Override
                public boolean isConform(org.apache.ambari.view.pig.persistence.utils.Indexed item) {
                    if (scriptId == null)
                        return super.isConform(item);
                    else {
                        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (item));
                        return ((job.getScriptId() != null) && (scriptId.compareTo(job.getScriptId()) == 0)) && super.isConform(item);
                    }
                }
            });
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("jobs", allJobs);
            return javax.ws.rs.core.Response.ok(object).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response runJob(org.apache.ambari.view.pig.resources.jobs.JobService.PigJobRequest request, @javax.ws.rs.core.Context
    javax.servlet.http.HttpServletResponse response, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        org.apache.ambari.view.pig.resources.jobs.JobService.LOG.info("Creating new job : {} ", request);
        try {
            request.validatePOST();
            getResourceManager().create(request.job);
            org.apache.ambari.view.pig.resources.jobs.models.PigJob job = null;
            try {
                job = getResourceManager().read(request.job.getId());
            } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException("Job not found", null);
            }
            response.setHeader("Location", java.lang.String.format("%s/%s", ui.getAbsolutePath().toString(), request.job.getId()));
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("job", job);
            return javax.ws.rs.core.Response.ok(object).status(201).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw ex;
        } catch (java.lang.IllegalArgumentException ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.BadRequestFormattedException(ex.getMessage(), ex);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.jobs.JobService.LOG.error("Exception occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public static class PigJobRequest {
        public org.apache.ambari.view.pig.resources.jobs.models.PigJob job;

        public java.lang.String explainPOST() {
            java.lang.StringBuilder result = new java.lang.StringBuilder();
            if (((job.getPigScript() == null) || job.getPigScript().isEmpty()) && ((job.getForcedContent() == null) || job.getForcedContent().isEmpty()))
                result.append("No pigScript file or forcedContent specifed;");

            if ((job.getTitle() == null) || job.getTitle().isEmpty())
                result.append("No title specifed;");

            if ((job.getId() != null) && (!job.getTitle().isEmpty()))
                result.append("ID should not exists in creation request;");

            return result.toString();
        }

        public void validatePOST() {
            if (!explainPOST().isEmpty()) {
                throw new org.apache.ambari.view.pig.utils.BadRequestFormattedException(explainPOST(), null);
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new java.lang.StringBuilder("PigJobRequest{").append("job=").append(job).append('}').toString();
        }
    }
}