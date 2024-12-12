package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class CommandReport {
    private java.lang.String role;

    private java.lang.String actionId;

    private java.lang.String stdout;

    private java.lang.String stderr;

    private java.lang.String structuredOut;

    private java.lang.String status;

    int exitCode;

    private java.lang.String serviceName;

    private long taskId;

    private java.lang.String clusterId;

    private java.lang.String roleCommand;

    private java.lang.String customCommand;

    @org.codehaus.jackson.annotate.JsonProperty("customCommand")
    @com.fasterxml.jackson.annotation.JsonProperty("customCommand")
    public java.lang.String getCustomCommand() {
        return customCommand;
    }

    @org.codehaus.jackson.annotate.JsonProperty("customCommand")
    @com.fasterxml.jackson.annotation.JsonProperty("customCommand")
    public void setCustomCommand(java.lang.String customCommand) {
        this.customCommand = customCommand;
    }

    @org.codehaus.jackson.annotate.JsonProperty("taskId")
    @com.fasterxml.jackson.annotation.JsonProperty("taskId")
    public long getTaskId() {
        return taskId;
    }

    @org.codehaus.jackson.annotate.JsonProperty("taskId")
    @com.fasterxml.jackson.annotation.JsonProperty("taskId")
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @org.codehaus.jackson.annotate.JsonProperty("actionId")
    @com.fasterxml.jackson.annotation.JsonProperty("actionId")
    public java.lang.String getActionId() {
        return this.actionId;
    }

    @org.codehaus.jackson.annotate.JsonProperty("actionId")
    @com.fasterxml.jackson.annotation.JsonProperty("actionId")
    public void setActionId(java.lang.String actionId) {
        this.actionId = actionId;
    }

    @org.codehaus.jackson.annotate.JsonProperty("stderr")
    @com.fasterxml.jackson.annotation.JsonProperty("stderr")
    public java.lang.String getStdErr() {
        return this.stderr;
    }

    @org.codehaus.jackson.annotate.JsonProperty("stderr")
    @com.fasterxml.jackson.annotation.JsonProperty("stderr")
    public void setStdErr(java.lang.String stderr) {
        this.stderr = stderr;
    }

    @org.codehaus.jackson.annotate.JsonProperty("exitcode")
    @com.fasterxml.jackson.annotation.JsonProperty("exitcode")
    public int getExitCode() {
        return this.exitCode;
    }

    @org.codehaus.jackson.annotate.JsonProperty("exitcode")
    @com.fasterxml.jackson.annotation.JsonProperty("exitcode")
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    @org.codehaus.jackson.annotate.JsonProperty("stdout")
    @com.fasterxml.jackson.annotation.JsonProperty("stdout")
    public java.lang.String getStdOut() {
        return this.stdout;
    }

    @org.codehaus.jackson.annotate.JsonProperty("stdout")
    @com.fasterxml.jackson.annotation.JsonProperty("stdout")
    public void setStdOut(java.lang.String stdout) {
        this.stdout = stdout;
    }

    @org.codehaus.jackson.annotate.JsonProperty("structuredOut")
    @com.fasterxml.jackson.annotation.JsonProperty("structuredOut")
    public java.lang.String getStructuredOut() {
        return this.structuredOut;
    }

    @org.codehaus.jackson.annotate.JsonProperty("structuredOut")
    @com.fasterxml.jackson.annotation.JsonProperty("structuredOut")
    public void setStructuredOut(java.lang.String structuredOut) {
        this.structuredOut = structuredOut;
    }

    @org.codehaus.jackson.annotate.JsonProperty("roleCommand")
    @com.fasterxml.jackson.annotation.JsonProperty("roleCommand")
    public java.lang.String getRoleCommand() {
        return this.roleCommand;
    }

    @org.codehaus.jackson.annotate.JsonProperty("roleCommand")
    @com.fasterxml.jackson.annotation.JsonProperty("roleCommand")
    public void setRoleCommand(java.lang.String roleCommand) {
        this.roleCommand = roleCommand;
    }

    @org.codehaus.jackson.annotate.JsonProperty("role")
    @com.fasterxml.jackson.annotation.JsonProperty("role")
    public java.lang.String getRole() {
        return role;
    }

    @org.codehaus.jackson.annotate.JsonProperty("role")
    @com.fasterxml.jackson.annotation.JsonProperty("role")
    public void setRole(java.lang.String role) {
        this.role = role;
    }

    @org.codehaus.jackson.annotate.JsonProperty("status")
    @com.fasterxml.jackson.annotation.JsonProperty("status")
    public java.lang.String getStatus() {
        return status;
    }

    @org.codehaus.jackson.annotate.JsonProperty("status")
    @com.fasterxml.jackson.annotation.JsonProperty("status")
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    @org.codehaus.jackson.annotate.JsonProperty("serviceName")
    @com.fasterxml.jackson.annotation.JsonProperty("serviceName")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("serviceName")
    @com.fasterxml.jackson.annotation.JsonProperty("serviceName")
    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("clusterId")
    public java.lang.String getClusterId() {
        return clusterId;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("clusterId")
    public void setClusterId(java.lang.String clusterId) {
        this.clusterId = clusterId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((((((((((("CommandReport{" + "role='") + role) + '\'') + ", actionId='") + actionId) + '\'') + ", status='") + status) + '\'') + ", exitCode=") + exitCode) + ", clusterId='") + clusterId) + '\'') + ", serviceName='") + serviceName) + '\'') + ", taskId=") + taskId) + ", roleCommand=") + roleCommand) + ", customCommand=") + customCommand) + '}';
    }
}