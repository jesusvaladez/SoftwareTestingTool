package org.apache.ambari.server.serveraction;
public class MockServerAction extends org.apache.ambari.server.serveraction.AbstractServerAction {
    public static final java.lang.String PAYLOAD_FORCE_FAIL = "force_fail";

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> commandParameters = getCommandParameters();
        if (clusters == null) {
            throw new org.apache.ambari.server.AmbariException("Missing payload");
        } else if (commandParameters == null) {
            throw new org.apache.ambari.server.AmbariException("Missing payload");
        } else if ("exception".equalsIgnoreCase(commandParameters.get(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL))) {
            throw new org.apache.ambari.server.AmbariException("Failing execution by request");
        } else if ("report".equalsIgnoreCase(commandParameters.get(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL))) {
            return createCommandReport(1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, null, "Forced fail via command", "Failing execution by request");
        } else {
            if ("timeout".equalsIgnoreCase(commandParameters.get(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL))) {
                java.lang.Long timeout;
                try {
                    timeout = (commandParameters.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT)) ? java.lang.Long.parseLong(commandParameters.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT)) * 1000 : null;
                } catch (java.lang.NumberFormatException e) {
                    timeout = null;
                }
                if (timeout != null) {
                    java.lang.Thread.sleep(timeout * 10);
                }
            }
            if (requestSharedDataContext != null) {
                java.lang.Integer data = ((java.lang.Integer) (requestSharedDataContext.get("Data")));
                if (data == null) {
                    data = 0;
                }
                requestSharedDataContext.put("Data", ++data);
            }
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, null, "Success!", null);
        }
    }
}