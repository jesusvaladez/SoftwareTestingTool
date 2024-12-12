package org.apache.oozie.ambari.view;
import org.apache.hadoop.fs.FileStatus;
public class WorkflowFilesService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.WorkflowFilesService.class);

    private org.apache.oozie.ambari.view.HDFSFileUtils hdfsFileUtils;

    private java.lang.String currentDraftVersion = "v1";

    public WorkflowFilesService(org.apache.oozie.ambari.view.HDFSFileUtils hdfsFileUtils) {
        super();
        this.hdfsFileUtils = hdfsFileUtils;
    }

    public java.lang.String createFile(java.lang.String appPath, java.lang.String content, boolean overwrite) throws java.io.IOException {
        return hdfsFileUtils.writeToFile(appPath, content, overwrite);
    }

    public java.lang.String createAssetFile(java.lang.String appPath, java.lang.String content, boolean overwrite) throws java.io.IOException {
        return hdfsFileUtils.writeToFile(appPath, content, overwrite);
    }

    public java.io.InputStream readDraft(java.lang.String appPath) throws java.io.IOException {
        return hdfsFileUtils.read(getWorkflowDraftFileName(appPath, null));
    }

    public java.io.InputStream readWorkflowXml(java.lang.String appPath) throws java.io.IOException {
        return hdfsFileUtils.read(appPath);
    }

    public java.io.InputStream readAssset(java.lang.String assetPath) throws java.io.IOException {
        return hdfsFileUtils.read(getAssetFileName(assetPath));
    }

    public java.lang.String getWorkflowDraftFileName(java.lang.String appPath, org.apache.oozie.ambari.view.JobType jobType) {
        if (appPath.endsWith(org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION)) {
            return appPath;
        } else if (appPath.endsWith(org.apache.oozie.ambari.view.Constants.WF_EXTENSION)) {
            java.lang.String folderPath = appPath.substring(0, appPath.lastIndexOf(org.apache.oozie.ambari.view.Constants.WF_EXTENSION));
            return folderPath + org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION;
        }
        if (jobType == null) {
            throw new java.lang.RuntimeException("Could not determine jobType(Workflow/Coordniator/Bundle");
        }
        if (appPath.endsWith("/")) {
            return appPath + getDefaultDraftFileName(jobType);
        } else {
            return (appPath + "/") + getDefaultDraftFileName(jobType);
        }
    }

    public java.lang.String getWorkflowFileName(java.lang.String appPath, org.apache.oozie.ambari.view.JobType jobType) {
        if (appPath.endsWith(org.apache.oozie.ambari.view.Constants.WF_EXTENSION)) {
            return appPath;
        } else if (appPath.endsWith(org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION)) {
            java.lang.String folderPath = appPath.substring(0, appPath.lastIndexOf(org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION));
            return folderPath + org.apache.oozie.ambari.view.Constants.WF_EXTENSION;
        } else if (appPath.endsWith("/")) {
            return appPath + getDefaultFileName(jobType);
        } else {
            return (appPath + "/") + getDefaultFileName(jobType);
        }
    }

    private java.lang.String getDefaultFileName(org.apache.oozie.ambari.view.JobType jobType) {
        switch (jobType) {
            case BUNDLE :
                return org.apache.oozie.ambari.view.Constants.DEFAULT_BUNDLE_FILENAME + org.apache.oozie.ambari.view.Constants.WF_EXTENSION;
            case COORDINATOR :
                return org.apache.oozie.ambari.view.Constants.DEFAULT_COORDINATOR_FILENAME + org.apache.oozie.ambari.view.Constants.WF_EXTENSION;
            case WORKFLOW :
                return org.apache.oozie.ambari.view.Constants.DEFAULT_WORKFLOW_FILENAME + org.apache.oozie.ambari.view.Constants.WF_EXTENSION;
            default :
                return null;
        }
    }

    private java.lang.String getDefaultDraftFileName(org.apache.oozie.ambari.view.JobType jobType) {
        switch (jobType) {
            case BUNDLE :
                return org.apache.oozie.ambari.view.Constants.DEFAULT_BUNDLE_FILENAME + org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION;
            case COORDINATOR :
                return org.apache.oozie.ambari.view.Constants.DEFAULT_COORDINATOR_FILENAME + org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION;
            case WORKFLOW :
                return org.apache.oozie.ambari.view.Constants.DEFAULT_WORKFLOW_FILENAME + org.apache.oozie.ambari.view.Constants.WF_DRAFT_EXTENSION;
            default :
                return null;
        }
    }

    public java.lang.String getAssetFileName(java.lang.String appPath) {
        java.lang.String assetFile = null;
        if (appPath.endsWith(org.apache.oozie.ambari.view.Constants.WF_ASSET_EXTENSION)) {
            assetFile = appPath;
        } else {
            java.lang.String[] paths = appPath.split("/");
            if (paths[paths.length - 1].contains(".")) {
                return appPath;
            } else {
                assetFile = (appPath + (appPath.endsWith("/") ? "" : "/")) + org.apache.oozie.ambari.view.Constants.DEFAULT_WORKFLOW_ASSET_FILENAME;
            }
        }
        return assetFile;
    }

    public void discardDraft(java.lang.String workflowPath) throws java.io.IOException {
        hdfsFileUtils.deleteFile(getWorkflowDraftFileName(workflowPath, null));
    }

    public org.apache.oozie.ambari.view.WorkflowFileInfo getWorkflowDetails(java.lang.String appPath, org.apache.oozie.ambari.view.JobType jobType) {
        appPath = appPath.trim();
        org.apache.oozie.ambari.view.WorkflowFileInfo workflowInfo = new org.apache.oozie.ambari.view.WorkflowFileInfo();
        workflowInfo.setWorkflowPath(appPath);
        boolean draftExists = hdfsFileUtils.fileExists(getWorkflowDraftFileName(appPath, jobType));
        workflowInfo.setDraftExists(draftExists);
        boolean workflowExists = hdfsFileUtils.fileExists(appPath);
        workflowInfo.setWorkflowDefinitionExists(workflowExists);
        org.apache.hadoop.fs.FileStatus workflowFileStatus = null;
        if (workflowExists) {
            workflowFileStatus = hdfsFileUtils.getFileStatus(appPath);
            workflowInfo.setWorkflowModificationTime(workflowFileStatus.getModificationTime());
        }
        if (draftExists) {
            org.apache.hadoop.fs.FileStatus draftFileStatus = hdfsFileUtils.getFileStatus(getWorkflowDraftFileName(appPath, jobType));
            workflowInfo.setDraftModificationTime(draftFileStatus.getModificationTime());
            if (!workflowExists) {
                workflowInfo.setIsDraftCurrent(true);
            } else {
                workflowInfo.setIsDraftCurrent((draftFileStatus.getModificationTime() - workflowFileStatus.getModificationTime()) > 0);
            }
        }
        return workflowInfo;
    }

    public void deleteWorkflowFile(java.lang.String fullWorkflowFilePath) {
        try {
            hdfsFileUtils.deleteFile(fullWorkflowFilePath);
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public boolean isDraftFormatCurrent(java.lang.String json) {
        com.google.gson.JsonElement jsonElement = new com.google.gson.JsonParser().parse(json);
        com.google.gson.JsonElement draftVersion = jsonElement.getAsJsonObject().get("draftVersion");
        if ((draftVersion != null) && currentDraftVersion.equals(draftVersion.getAsString().trim())) {
            return true;
        } else {
            return false;
        }
    }
}