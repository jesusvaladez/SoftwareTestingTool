package org.apache.ambari.server.serveraction;
public abstract class AbstractServerAction implements org.apache.ambari.server.serveraction.ServerAction {
    private org.apache.ambari.server.agent.ExecutionCommand executionCommand = null;

    private org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = null;

    protected org.apache.ambari.server.serveraction.ActionLog actionLog = new org.apache.ambari.server.serveraction.ActionLog();

    @com.google.inject.Inject
    private org.apache.ambari.server.audit.AuditLogger auditLogger;

    @com.google.inject.Inject
    protected com.google.gson.Gson gson;

    @java.lang.Override
    public org.apache.ambari.server.agent.ExecutionCommand getExecutionCommand() {
        return executionCommand;
    }

    @java.lang.Override
    public void setExecutionCommand(org.apache.ambari.server.agent.ExecutionCommand executionCommand) {
        this.executionCommand = executionCommand;
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.HostRoleCommand getHostRoleCommand() {
        return hostRoleCommand;
    }

    @java.lang.Override
    public void setHostRoleCommand(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand) {
        this.hostRoleCommand = hostRoleCommand;
    }

    protected org.apache.ambari.server.agent.CommandReport createCompletedCommandReport() {
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", actionLog.getStdOut(), actionLog.getStdErr());
    }

    protected org.apache.ambari.server.agent.CommandReport createCommandReport(int exitCode, org.apache.ambari.server.actionmanager.HostRoleStatus status, java.lang.String structuredOut, java.lang.String stdout, java.lang.String stderr) {
        org.apache.ambari.server.agent.CommandReport report = null;
        if (hostRoleCommand != null) {
            if (executionCommand == null) {
                org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = hostRoleCommand.getExecutionCommandWrapper();
                if (wrapper != null) {
                    executionCommand = wrapper.getExecutionCommand();
                }
            }
            if (executionCommand != null) {
                org.apache.ambari.server.RoleCommand roleCommand = executionCommand.getRoleCommand();
                report = new org.apache.ambari.server.agent.CommandReport();
                report.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(hostRoleCommand.getRequestId(), hostRoleCommand.getStageId()));
                report.setClusterId(executionCommand.getClusterId());
                report.setRole(executionCommand.getRole());
                report.setRoleCommand(roleCommand == null ? null : roleCommand.toString());
                report.setServiceName(executionCommand.getServiceName());
                report.setTaskId(executionCommand.getTaskId());
                report.setStructuredOut(structuredOut);
                report.setStdErr(stderr == null ? "" : stderr);
                report.setStdOut(stdout == null ? "" : stdout);
                report.setStatus(status == null ? null : status.toString());
                report.setExitCode(exitCode);
            }
        }
        return report;
    }

    protected java.util.Map<java.lang.String, java.lang.String> getCommandParameters() {
        if (executionCommand == null) {
            return java.util.Collections.emptyMap();
        } else {
            return executionCommand.getCommandParams();
        }
    }

    protected java.lang.String getCommandParameterValue(java.lang.String propertyName) {
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        return commandParameters == null ? null : commandParameters.get(propertyName);
    }

    protected void auditLog(org.apache.ambari.server.audit.event.AuditEvent ae) {
        auditLogger.log(ae);
    }
}