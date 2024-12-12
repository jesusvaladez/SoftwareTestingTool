package org.apache.oozie.ambari.view.exception;
public enum ErrorCode {

    OOZIE_SUBMIT_ERROR("error.oozie.submit", "Submitting job to Oozie failed. Please check your definition/configuration.", true),
    FILE_ACCESS_ACL_ERROR("error.file.access.control", "Access Error to file due to access control", true),
    FILE_ACCESS_UNKNOWN_ERROR("error.file.access", "Error accessing file"),
    WORKFLOW_PATH_EXISTS("error.workflow.path.exists", "File exists", true),
    WORKFLOW_XML_DOES_NOT_EXIST("error.workflow.xml.not.exists", "File does not exist", true),
    INVALID_ASSET_INPUT("error.invalid.asset.input", "Invalid asset definition", true),
    INVALID_EMPTY_INPUT("error.invalid.empty.input", "Input path cannot be empty", true),
    ASSET_NOT_EXIST("error.asset.not.exist", "Asset doesn’t exist", true),
    PERMISSION_ERROR("error.permission", "Don’t have permission", true),
    ASSET_INVALID_FROM_OOZIE("error.oozie.asset.invalid", "Invalid Asset Definition", true);
    private java.lang.String errorCode;

    private java.lang.String description;

    private boolean isInputError = false;

    ErrorCode(java.lang.String errorCode, java.lang.String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    ErrorCode(java.lang.String errorCode, java.lang.String description, boolean isInputError) {
        this.errorCode = errorCode;
        this.description = description;
        this.isInputError = isInputError;
    }

    public java.lang.String getErrorCode() {
        return errorCode;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public boolean isInputError() {
        return isInputError;
    }
}