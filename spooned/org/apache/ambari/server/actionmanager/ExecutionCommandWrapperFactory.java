package org.apache.ambari.server.actionmanager;
public interface ExecutionCommandWrapperFactory {
    org.apache.ambari.server.actionmanager.ExecutionCommandWrapper createFromJson(java.lang.String jsonExecutionCommand);

    org.apache.ambari.server.actionmanager.ExecutionCommandWrapper createFromCommand(org.apache.ambari.server.agent.ExecutionCommand executionCommand);
}