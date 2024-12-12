package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class NamedTaskUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    private java.lang.Long id;

    private java.lang.Long requestId;

    private java.lang.String hostName;

    private java.lang.Long endTime;

    private org.apache.ambari.server.actionmanager.HostRoleStatus status;

    private java.lang.String errorLog;

    private java.lang.String outLog;

    private java.lang.String stderr;

    private java.lang.String stdout;

    @com.fasterxml.jackson.annotation.JsonProperty("structured_out")
    private java.lang.String structuredOut;

    public NamedTaskUpdateEvent(java.lang.Long id, java.lang.Long requestId, java.lang.String hostName, java.lang.Long endTime, org.apache.ambari.server.actionmanager.HostRoleStatus status, java.lang.String errorLog, java.lang.String outLog, java.lang.String stderr, java.lang.String stdout, java.lang.String structuredOut) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.NAMEDTASK);
        this.id = id;
        this.requestId = requestId;
        this.hostName = hostName;
        this.endTime = endTime;
        this.status = status;
        this.errorLog = errorLog;
        this.outLog = outLog;
        this.stderr = stderr;
        this.stdout = stdout;
        this.structuredOut = structuredOut;
    }

    public NamedTaskUpdateEvent(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand) {
        this(hostRoleCommand.getTaskId(), hostRoleCommand.getRequestId(), hostRoleCommand.getHostName(), hostRoleCommand.getEndTime(), hostRoleCommand.getStatus(), hostRoleCommand.getErrorLog(), hostRoleCommand.getOutputLog(), hostRoleCommand.getStderr(), hostRoleCommand.getStdout(), hostRoleCommand.getStructuredOut());
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public java.lang.Long getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.Long endTime) {
        this.endTime = endTime;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    public java.lang.String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(java.lang.String errorLog) {
        this.errorLog = errorLog;
    }

    public java.lang.String getOutLog() {
        return outLog;
    }

    public void setOutLog(java.lang.String outLog) {
        this.outLog = outLog;
    }

    public java.lang.String getStderr() {
        return stderr;
    }

    public void setStderr(java.lang.String stderr) {
        this.stderr = stderr;
    }

    public java.lang.String getStdout() {
        return stdout;
    }

    public void setStdout(java.lang.String stdout) {
        this.stdout = stdout;
    }

    public java.lang.String getStructuredOut() {
        return structuredOut;
    }

    public void setStructuredOut(java.lang.String structuredOut) {
        this.structuredOut = structuredOut;
    }

    @java.lang.Override
    public java.lang.String completeDestination(java.lang.String destination) {
        return (destination + "/") + id;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.NamedTaskUpdateEvent that = ((org.apache.ambari.server.events.NamedTaskUpdateEvent) (o));
        return ((((((((java.util.Objects.equals(id, that.id) && java.util.Objects.equals(requestId, that.requestId)) && java.util.Objects.equals(hostName, that.hostName)) && java.util.Objects.equals(endTime, that.endTime)) && (status == that.status)) && java.util.Objects.equals(errorLog, that.errorLog)) && java.util.Objects.equals(outLog, that.outLog)) && java.util.Objects.equals(stderr, that.stderr)) && java.util.Objects.equals(stdout, that.stdout)) && java.util.Objects.equals(structuredOut, that.structuredOut);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(id, requestId, hostName, endTime, status, errorLog, outLog, stderr, stdout, structuredOut);
    }
}