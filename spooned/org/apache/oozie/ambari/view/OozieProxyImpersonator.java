package org.apache.oozie.ambari.view;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import static org.apache.oozie.ambari.view.Constants.MESSAGE_KEY;
import static org.apache.oozie.ambari.view.Constants.STATUS_KEY;
import static org.apache.oozie.ambari.view.Constants.STATUS_OK;
@com.google.inject.Singleton
public class OozieProxyImpersonator {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.OozieProxyImpersonator.class);

    private static final boolean PROJ_MANAGER_ENABLED = true;

    public static final java.lang.String RESPONSE_TYPE = "response-type";

    public static final java.lang.String OLDER_FORMAT_DRAFT_INGORED = "olderFormatDraftIngored";

    private final org.apache.ambari.view.ViewContext viewContext;

    private final org.apache.oozie.ambari.view.Utils utils = new org.apache.oozie.ambari.view.Utils();

    private final org.apache.oozie.ambari.view.HDFSFileUtils hdfsFileUtils;

    private final org.apache.oozie.ambari.view.WorkflowFilesService workflowFilesService;

    private org.apache.oozie.ambari.view.workflowmanager.WorkflowManagerService workflowManagerService;

    private final org.apache.oozie.ambari.view.OozieDelegate oozieDelegate;

    private final org.apache.oozie.ambari.view.OozieUtils oozieUtils = new org.apache.oozie.ambari.view.OozieUtils();

    private final org.apache.oozie.ambari.view.assets.AssetResource assetResource;

    private static enum WorkflowFormat {

        XML("xml"),
        DRAFT("draft");
        java.lang.String value;

        WorkflowFormat(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String getValue() {
            return value;
        }
    }

    @javax.inject.Inject
    public OozieProxyImpersonator(org.apache.ambari.view.ViewContext viewContext) {
        this.viewContext = viewContext;
        hdfsFileUtils = new org.apache.oozie.ambari.view.HDFSFileUtils(viewContext);
        workflowFilesService = new org.apache.oozie.ambari.view.WorkflowFilesService(hdfsFileUtils);
        this.oozieDelegate = new org.apache.oozie.ambari.view.OozieDelegate(viewContext);
        assetResource = new org.apache.oozie.ambari.view.assets.AssetResource(viewContext);
        if (org.apache.oozie.ambari.view.OozieProxyImpersonator.PROJ_MANAGER_ENABLED) {
            workflowManagerService = new org.apache.oozie.ambari.view.workflowmanager.WorkflowManagerService(viewContext);
        }
        org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.info(java.lang.String.format("OozieProxyImpersonator initialized for instance: %s", viewContext.getInstanceName()));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("hdfsCheck")
    public javax.ws.rs.core.Response hdfsCheck() {
        try {
            hdfsFileUtils.hdfsCheck();
            return javax.ws.rs.core.Response.ok().build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("homeDirCheck")
    public javax.ws.rs.core.Response homeDirCheck() {
        try {
            hdfsFileUtils.homeDirCheck();
            return javax.ws.rs.core.Response.ok().build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.Path("/fileServices")
    public org.apache.oozie.ambari.view.FileServices fileServices() {
        return new org.apache.oozie.ambari.view.FileServices(viewContext);
    }

    @javax.ws.rs.Path("/wfprojects")
    public org.apache.oozie.ambari.view.workflowmanager.WorkflowsManagerResource workflowsManagerResource() {
        return new org.apache.oozie.ambari.view.workflowmanager.WorkflowsManagerResource(viewContext);
    }

    @javax.ws.rs.Path("/assets")
    public org.apache.oozie.ambari.view.assets.AssetResource assetResource() {
        return this.assetResource;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/getCurrentUserName")
    public javax.ws.rs.core.Response getCurrentUserName() {
        org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
        obj.put("username", viewContext.getUsername());
        return javax.ws.rs.core.Response.ok(obj).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/getWorkflowManagerConfigs")
    public javax.ws.rs.core.Response getWorkflowConfigs() {
        try {
            java.util.HashMap<java.lang.String, java.lang.String> workflowConfigs = new java.util.HashMap<java.lang.String, java.lang.String>();
            workflowConfigs.put("nameNode", viewContext.getProperties().get("webhdfs.url"));
            workflowConfigs.put("resourceManager", viewContext.getProperties().get("yarn.resourcemanager.address"));
            workflowConfigs.put("userName", viewContext.getUsername());
            workflowConfigs.put("checkHomeDir", hdfsFileUtils.shouldCheckForHomeDir().toString());
            return javax.ws.rs.core.Response.ok(workflowConfigs).build();
        } catch (java.lang.Exception e) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(e.getMessage(), e);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(e);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/submitJob")
    @javax.ws.rs.Consumes({ (javax.ws.rs.core.MediaType.TEXT_PLAIN + ",") + javax.ws.rs.core.MediaType.TEXT_XML })
    public javax.ws.rs.core.Response submitJob(java.lang.String postBody, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.QueryParam("app.path")
    java.lang.String appPath, @javax.ws.rs.QueryParam("projectId")
    java.lang.String projectId, @javax.ws.rs.DefaultValue("false")
    @javax.ws.rs.QueryParam("overwrite")
    java.lang.Boolean overwrite, @javax.ws.rs.QueryParam("description")
    java.lang.String description, @javax.ws.rs.QueryParam("jobType")
    java.lang.String jobTypeString) {
        org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.info("submit workflow job called");
        org.apache.oozie.ambari.view.JobType jobType = org.apache.oozie.ambari.view.JobType.valueOf(jobTypeString);
        if (org.apache.commons.lang.StringUtils.isEmpty(appPath)) {
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.INVALID_EMPTY_INPUT);
        }
        appPath = workflowFilesService.getWorkflowFileName(appPath.trim(), jobType);
        try {
            if (!overwrite) {
                boolean fileExists = hdfsFileUtils.fileExists(appPath);
                if (fileExists) {
                    throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.WORKFLOW_PATH_EXISTS);
                }
            }
            postBody = utils.formatXml(postBody);
            java.lang.String filePath = workflowFilesService.createFile(appPath, postBody, overwrite);
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.info(java.lang.String.format("submit workflow job done. filePath=[%s]", filePath));
            if (org.apache.oozie.ambari.view.OozieProxyImpersonator.PROJ_MANAGER_ENABLED) {
                java.lang.String name = oozieUtils.deduceWorkflowNameFromXml(postBody);
                workflowManagerService.saveWorkflow(projectId, appPath, jobType, null, viewContext.getUsername(), name);
            }
            java.lang.String response = oozieDelegate.submitWorkflowJobToOozie(headers, appPath, ui.getQueryParameters(), jobType);
            return javax.ws.rs.core.Response.status(Status.OK).entity(response).build();
        } catch (org.apache.oozie.ambari.view.exception.WfmWebException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (org.apache.oozie.ambari.view.exception.WfmException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex, ex.getErrorCode());
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/saveWorkflow")
    @javax.ws.rs.Consumes({ (javax.ws.rs.core.MediaType.TEXT_PLAIN + ",") + javax.ws.rs.core.MediaType.TEXT_XML })
    public javax.ws.rs.core.Response saveWorkflow(java.lang.String postBody, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.QueryParam("app.path")
    java.lang.String appPath, @javax.ws.rs.QueryParam("jobType")
    java.lang.String jobTypeStr, @javax.ws.rs.DefaultValue("false")
    @javax.ws.rs.QueryParam("overwrite")
    java.lang.Boolean overwrite) {
        org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.info("save workflow  called");
        if (org.apache.commons.lang.StringUtils.isEmpty(appPath)) {
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.INVALID_EMPTY_INPUT);
        }
        org.apache.oozie.ambari.view.JobType jobType = (org.apache.commons.lang.StringUtils.isEmpty(jobTypeStr)) ? org.apache.oozie.ambari.view.JobType.WORKFLOW : org.apache.oozie.ambari.view.JobType.valueOf(jobTypeStr);
        java.lang.String workflowFilePath = workflowFilesService.getWorkflowFileName(appPath.trim(), jobType);
        try {
            if (!overwrite) {
                boolean fileExists = hdfsFileUtils.fileExists(workflowFilePath);
                if (fileExists) {
                    throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.WORKFLOW_PATH_EXISTS);
                }
            }
            if (utils.isXml(postBody)) {
                saveWorkflowXml(jobType, appPath, postBody, overwrite);
            } else {
                saveDraft(jobType, appPath, postBody, overwrite);
            }
            if (org.apache.oozie.ambari.view.OozieProxyImpersonator.PROJ_MANAGER_ENABLED) {
                workflowManagerService.saveWorkflow(null, workflowFilePath, jobType, null, viewContext.getUsername(), getWorkflowName(postBody));
            }
        } catch (org.apache.oozie.ambari.view.exception.WfmWebException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
        return javax.ws.rs.core.Response.ok().build();
    }

    private java.lang.String getWorkflowName(java.lang.String postBody) {
        if (utils.isXml(postBody)) {
            return oozieUtils.deduceWorkflowNameFromXml(postBody);
        } else {
            return oozieUtils.deduceWorkflowNameFromJson(postBody);
        }
    }

    private void saveWorkflowXml(org.apache.oozie.ambari.view.JobType jobType, java.lang.String appPath, java.lang.String postBody, java.lang.Boolean overwrite) throws java.io.IOException {
        appPath = workflowFilesService.getWorkflowFileName(appPath.trim(), jobType);
        postBody = utils.formatXml(postBody);
        workflowFilesService.createFile(appPath, postBody, overwrite);
        java.lang.String workflowDraftPath = workflowFilesService.getWorkflowDraftFileName(appPath.trim(), jobType);
        if (hdfsFileUtils.fileExists(workflowDraftPath)) {
            hdfsFileUtils.deleteFile(workflowDraftPath);
        }
    }

    private void saveDraft(org.apache.oozie.ambari.view.JobType jobType, java.lang.String appPath, java.lang.String postBody, java.lang.Boolean overwrite) throws java.io.IOException {
        java.lang.String workflowFilePath = workflowFilesService.getWorkflowFileName(appPath.trim(), jobType);
        if (!hdfsFileUtils.fileExists(workflowFilePath)) {
            java.lang.String noOpWorkflow = oozieUtils.getNoOpWorkflowXml(postBody, jobType);
            workflowFilesService.createFile(workflowFilePath, noOpWorkflow, overwrite);
        }
        java.lang.String workflowDraftPath = workflowFilesService.getWorkflowDraftFileName(appPath.trim(), jobType);
        workflowFilesService.createFile(workflowDraftPath, postBody, true);
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/publishAsset")
    @javax.ws.rs.Consumes({ (javax.ws.rs.core.MediaType.TEXT_PLAIN + ",") + javax.ws.rs.core.MediaType.TEXT_XML })
    public javax.ws.rs.core.Response publishAsset(java.lang.String postBody, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.QueryParam("uploadPath")
    java.lang.String uploadPath, @javax.ws.rs.DefaultValue("false")
    @javax.ws.rs.QueryParam("overwrite")
    java.lang.Boolean overwrite) {
        org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.info("publish asset called");
        if (org.apache.commons.lang.StringUtils.isEmpty(uploadPath)) {
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.INVALID_EMPTY_INPUT);
        }
        uploadPath = uploadPath.trim();
        try {
            java.util.Map<java.lang.String, java.lang.String> validateAsset = assetResource.validateAsset(headers, postBody, ui.getQueryParameters());
            if (!org.apache.oozie.ambari.view.Constants.STATUS_OK.equals(validateAsset.get(org.apache.oozie.ambari.view.Constants.STATUS_KEY))) {
                org.apache.oozie.ambari.view.exception.WfmWebException wfmEx = new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.INVALID_ASSET_INPUT);
                wfmEx.setAdditionalDetail(validateAsset.get(org.apache.oozie.ambari.view.Constants.MESSAGE_KEY));
                throw wfmEx;
            }
            return saveAsset(postBody, uploadPath, overwrite);
        } catch (org.apache.oozie.ambari.view.exception.WfmWebException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    private javax.ws.rs.core.Response saveAsset(java.lang.String postBody, java.lang.String uploadPath, java.lang.Boolean overwrite) throws java.io.IOException {
        uploadPath = workflowFilesService.getAssetFileName(uploadPath);
        if (!overwrite) {
            boolean fileExists = hdfsFileUtils.fileExists(uploadPath);
            if (fileExists) {
                throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.WORKFLOW_PATH_EXISTS);
            }
        }
        postBody = utils.formatXml(postBody);
        java.lang.String filePath = workflowFilesService.createAssetFile(uploadPath, postBody, overwrite);
        org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.info(java.lang.String.format("publish asset job done. filePath=[%s]", filePath));
        return javax.ws.rs.core.Response.ok().build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/readAsset")
    public javax.ws.rs.core.Response readAsset(@javax.ws.rs.QueryParam("assetPath")
    java.lang.String assetPath) {
        if (org.apache.commons.lang.StringUtils.isEmpty(assetPath)) {
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.INVALID_EMPTY_INPUT);
        }
        try {
            final java.io.InputStream is = workflowFilesService.readAssset(assetPath);
            javax.ws.rs.core.StreamingOutput streamer = utils.streamResponse(is);
            return javax.ws.rs.core.Response.ok(streamer).status(200).build();
        } catch (java.io.IOException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/readWorkflowDraft")
    public javax.ws.rs.core.Response readDraft(@javax.ws.rs.QueryParam("workflowXmlPath")
    java.lang.String workflowPath) {
        if (org.apache.commons.lang.StringUtils.isEmpty(workflowPath)) {
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.INVALID_EMPTY_INPUT);
        }
        try {
            final java.io.InputStream is = workflowFilesService.readDraft(workflowPath);
            javax.ws.rs.core.StreamingOutput streamer = utils.streamResponse(is);
            return javax.ws.rs.core.Response.ok(streamer).status(200).build();
        } catch (java.io.IOException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/discardWorkflowDraft")
    public javax.ws.rs.core.Response discardDraft(@javax.ws.rs.QueryParam("workflowXmlPath")
    java.lang.String workflowPath) {
        try {
            workflowFilesService.discardDraft(workflowPath);
            return javax.ws.rs.core.Response.ok().build();
        } catch (java.io.IOException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/readWorkflow")
    public javax.ws.rs.core.Response readWorkflow(@javax.ws.rs.QueryParam("workflowPath")
    java.lang.String workflowPath, @javax.ws.rs.QueryParam("jobType")
    java.lang.String jobTypeStr) {
        try {
            java.lang.String workflowFileName = workflowFilesService.getWorkflowFileName(workflowPath, org.apache.oozie.ambari.view.JobType.valueOf(jobTypeStr));
            if (!hdfsFileUtils.fileExists(workflowFileName)) {
                throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.WORKFLOW_XML_DOES_NOT_EXIST);
            }
            org.apache.oozie.ambari.view.WorkflowFileInfo workflowDetails = workflowFilesService.getWorkflowDetails(workflowPath, org.apache.oozie.ambari.view.JobType.valueOf(jobTypeStr));
            if (workflowPath.endsWith(org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION) || workflowDetails.getIsDraftCurrent()) {
                java.lang.String filePath = workflowFilesService.getWorkflowDraftFileName(workflowPath, org.apache.oozie.ambari.view.JobType.valueOf(jobTypeStr));
                java.io.InputStream inputStream = workflowFilesService.readWorkflowXml(filePath);
                java.lang.String stringResponse = org.apache.commons.io.IOUtils.toString(inputStream);
                if (!workflowFilesService.isDraftFormatCurrent(stringResponse)) {
                    filePath = workflowFilesService.getWorkflowFileName(workflowPath, org.apache.oozie.ambari.view.JobType.valueOf(jobTypeStr));
                    return getWorkflowResponse(filePath, org.apache.oozie.ambari.view.OozieProxyImpersonator.WorkflowFormat.XML.getValue(), true);
                } else {
                    return javax.ws.rs.core.Response.ok(stringResponse).header(org.apache.oozie.ambari.view.OozieProxyImpersonator.RESPONSE_TYPE, org.apache.oozie.ambari.view.OozieProxyImpersonator.WorkflowFormat.DRAFT.getValue()).build();
                }
            } else {
                java.lang.String filePath = workflowFilesService.getWorkflowFileName(workflowPath, org.apache.oozie.ambari.view.JobType.valueOf(jobTypeStr));
                return getWorkflowResponse(filePath, org.apache.oozie.ambari.view.OozieProxyImpersonator.WorkflowFormat.XML.getValue(), false);
            }
        } catch (org.apache.oozie.ambari.view.exception.WfmWebException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    private javax.ws.rs.core.Response getWorkflowResponse(java.lang.String filePath, java.lang.String responseType, boolean olderFormatDraftIngored) throws java.io.IOException {
        final java.io.InputStream is = workflowFilesService.readWorkflowXml(filePath);
        javax.ws.rs.core.StreamingOutput streamer = utils.streamResponse(is);
        javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.ok(streamer).header(org.apache.oozie.ambari.view.OozieProxyImpersonator.RESPONSE_TYPE, responseType);
        if (olderFormatDraftIngored) {
            responseBuilder.header(org.apache.oozie.ambari.view.OozieProxyImpersonator.OLDER_FORMAT_DRAFT_INGORED, java.lang.Boolean.TRUE.toString());
        }
        return responseBuilder.build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/readWorkflowXml")
    public javax.ws.rs.core.Response readWorkflowXml(@javax.ws.rs.QueryParam("workflowXmlPath")
    java.lang.String workflowPath, @javax.ws.rs.QueryParam("jobType")
    java.lang.String jobTypeStr) {
        if (org.apache.commons.lang.StringUtils.isEmpty(workflowPath)) {
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.INVALID_EMPTY_INPUT);
        }
        try {
            if (!hdfsFileUtils.fileExists(workflowPath)) {
                throw new org.apache.oozie.ambari.view.exception.WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode.WORKFLOW_XML_DOES_NOT_EXIST);
            }
            final java.io.InputStream is = workflowFilesService.readWorkflowXml(workflowPath);
            javax.ws.rs.core.StreamingOutput streamer = utils.streamResponse(is);
            return javax.ws.rs.core.Response.ok(streamer).status(200).build();
        } catch (org.apache.oozie.ambari.view.exception.WfmWebException ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/{path: .*}")
    public javax.ws.rs.core.Response handleGet(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        try {
            return oozieDelegate.consumeService(headers, ui.getAbsolutePath().getPath(), ui.getQueryParameters(), HttpMethod.GET, null);
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error("Error in GET proxy", ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/{path: .*}")
    public javax.ws.rs.core.Response handlePost(java.lang.String xml, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        try {
            return oozieDelegate.consumeService(headers, ui.getAbsolutePath().getPath(), ui.getQueryParameters(), HttpMethod.POST, xml);
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error("Error in POST proxy", ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/{path: .*}")
    public javax.ws.rs.core.Response handleDelete(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        try {
            return oozieDelegate.consumeService(headers, ui.getAbsolutePath().getPath(), ui.getQueryParameters(), HttpMethod.POST, null);
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error("Error in DELETE proxy", ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/{path: .*}")
    public javax.ws.rs.core.Response handlePut(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        try {
            return oozieDelegate.consumeService(headers, ui.getAbsolutePath().getPath(), ui.getQueryParameters(), HttpMethod.PUT, body);
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.OozieProxyImpersonator.LOGGER.error("Error in PUT proxy", ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }
}