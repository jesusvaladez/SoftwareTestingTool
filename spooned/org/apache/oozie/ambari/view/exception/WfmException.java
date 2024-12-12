package org.apache.oozie.ambari.view.exception;
public class WfmException extends java.lang.RuntimeException {
    private org.apache.oozie.ambari.view.exception.ErrorCode errorCode;

    public WfmException(org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public WfmException(java.lang.String message, org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public WfmException(java.lang.String message, java.lang.Throwable cause, org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public WfmException(java.lang.Throwable cause, org.apache.oozie.ambari.view.exception.ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public org.apache.oozie.ambari.view.exception.ErrorCode getErrorCode() {
        return errorCode;
    }
}