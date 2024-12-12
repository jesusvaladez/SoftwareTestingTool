package org.apache.ambari.server.state.scheduler;
public class BatchRequestResponse {
    private java.lang.Long requestId;

    private java.lang.String status;

    private int returnCode;

    private java.lang.String returnMessage;

    private int failedTaskCount;

    private int abortedTaskCount;

    private int timedOutTaskCount;

    private int totalTaskCount;

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public java.lang.String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(java.lang.String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public int getFailedTaskCount() {
        return failedTaskCount;
    }

    public void setFailedTaskCount(int failedTaskCount) {
        this.failedTaskCount = failedTaskCount;
    }

    public int getAbortedTaskCount() {
        return abortedTaskCount;
    }

    public void setAbortedTaskCount(int abortedTaskCount) {
        this.abortedTaskCount = abortedTaskCount;
    }

    public int getTimedOutTaskCount() {
        return timedOutTaskCount;
    }

    public void setTimedOutTaskCount(int timedOutTaskCount) {
        this.timedOutTaskCount = timedOutTaskCount;
    }

    public int getTotalTaskCount() {
        return totalTaskCount;
    }

    public void setTotalTaskCount(int totalTaskCount) {
        this.totalTaskCount = totalTaskCount;
    }
}