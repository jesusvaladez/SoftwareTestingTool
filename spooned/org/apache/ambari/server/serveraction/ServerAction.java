package org.apache.ambari.server.serveraction;
public interface ServerAction {
    java.lang.String ACTION_NAME = "ACTION_NAME";

    java.lang.String ACTION_USER_NAME = "ACTION_USER_NAME";

    java.lang.String WRAPPED_CLASS_NAME = "WRAPPED_CLASS_NAME";

    int DEFAULT_LONG_RUNNING_TASK_TIMEOUT_SECONDS = 36000;

    org.apache.ambari.server.agent.ExecutionCommand getExecutionCommand();

    void setExecutionCommand(org.apache.ambari.server.agent.ExecutionCommand command);

    org.apache.ambari.server.actionmanager.HostRoleCommand getHostRoleCommand();

    void setHostRoleCommand(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand);

    org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException;
}