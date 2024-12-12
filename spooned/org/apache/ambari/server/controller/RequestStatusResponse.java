package org.apache.ambari.server.controller;
public class RequestStatusResponse {
    private final java.lang.Long requestId;

    java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks;

    private java.lang.String logs;

    private java.lang.String message;

    private java.lang.String requestContext;

    public RequestStatusResponse(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public java.lang.String getLogs() {
        return logs;
    }

    public void setLogs(java.lang.String logs) {
        this.logs = logs;
    }

    public long getRequestId() {
        return requestId;
    }

    public java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> getTasks() {
        return tasks;
    }

    public void setTasks(java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks) {
        this.tasks = tasks;
    }

    public java.lang.String getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(java.lang.String requestContext) {
        this.requestContext = requestContext;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public void setMessage(java.lang.String message) {
        this.message = message;
    }
}