package org.apache.oozie.ambari.view.exception;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.security.AccessControlException;
import org.json.simple.JSONObject;
public class WfmWebException extends javax.ws.rs.WebApplicationException {
    private static final int STATUS = 500;

    private org.apache.oozie.ambari.view.exception.ErrorCode errorCode;

    private java.lang.String additionalDetail = null;

    private java.lang.String message;

    public WfmWebException(java.lang.String message) {
        super();
        setMessage(message);
    }

    private void setMessage(java.lang.String message) {
        this.message = message;
    }

    public WfmWebException(java.lang.Throwable cause) {
        super(cause);
    }

    public WfmWebException(org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        super();
        setMessage(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public WfmWebException(java.lang.String message, java.lang.Throwable cause) {
        super(cause);
        setMessage(message);
    }

    public WfmWebException(java.lang.String message, org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        super();
        setMessage(message);
        this.errorCode = errorCode;
    }

    public WfmWebException(java.lang.String message, java.lang.Throwable cause, org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        super(cause);
        setMessage(message);
        this.errorCode = errorCode;
    }

    public WfmWebException(java.lang.Throwable cause, org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        super(cause);
        setMessage(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public void setAdditionalDetail(java.lang.String additionalDetail) {
        this.additionalDetail = additionalDetail;
    }

    @java.lang.Override
    public javax.ws.rs.core.Response getResponse() {
        java.util.HashMap<java.lang.String, java.lang.Object> response = new java.util.HashMap<java.lang.String, java.lang.Object>();
        java.lang.String trace = null;
        java.lang.Throwable ex = this.getCause();
        if (ex != null) {
            if (ex.getStackTrace().length < 1) {
                trace = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(this);
            } else {
                trace = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(ex);
            }
            if (ex instanceof org.apache.hadoop.security.AccessControlException) {
                errorCode = org.apache.oozie.ambari.view.exception.ErrorCode.FILE_ACCESS_ACL_ERROR;
            } else if (ex instanceof java.io.IOException) {
                errorCode = org.apache.oozie.ambari.view.exception.ErrorCode.FILE_ACCESS_UNKNOWN_ERROR;
            }
        } else {
            trace = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(this);
        }
        response.put("stackTrace", trace);
        int status = ((errorCode != null) && errorCode.isInputError()) ? Response.Status.BAD_REQUEST.getStatusCode() : org.apache.oozie.ambari.view.exception.WfmWebException.STATUS;
        if (errorCode != null) {
            response.put("errorCode", errorCode.getErrorCode());
            response.put("message", errorCode.getDescription());
        } else if (this.getMessage() != null) {
            response.put("message", this.getMessage());
        } else if (this.getCause() != null) {
            response.put("message", this.getCause().getMessage());
        }
        if (this.additionalDetail != null) {
            response.put("additionalDetail", additionalDetail);
        }
        return javax.ws.rs.core.Response.status(status).entity(new org.json.simple.JSONObject(response)).type(MediaType.APPLICATION_JSON).build();
    }

    @java.lang.Override
    public java.lang.String getMessage() {
        return message;
    }
}