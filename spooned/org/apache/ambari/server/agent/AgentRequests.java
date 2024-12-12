package org.apache.ambari.server.agent;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class AgentRequests {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.HeartbeatMonitor.class);

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Boolean>> requiresExecCmdDetails = new java.util.HashMap<>();

    private final java.lang.Object _lock = new java.lang.Object();

    public AgentRequests() {
    }

    public void setExecutionDetailsRequest(java.lang.String host, java.lang.String component, java.lang.String requestExecutionCmd) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(requestExecutionCmd)) {
            java.util.Map<java.lang.String, java.lang.Boolean> perHostRequiresExecCmdDetails = getPerHostRequiresExecCmdDetails(host);
            if (java.lang.Boolean.TRUE.toString().toUpperCase().equals(requestExecutionCmd.toUpperCase())) {
                org.apache.ambari.server.agent.AgentRequests.LOG.info((("Setting need for exec command to " + requestExecutionCmd) + " for ") + component);
                perHostRequiresExecCmdDetails.put(component, java.lang.Boolean.TRUE);
            } else {
                perHostRequiresExecCmdDetails.put(component, java.lang.Boolean.FALSE);
            }
        }
    }

    public java.lang.Boolean shouldSendExecutionDetails(java.lang.String host, java.lang.String component) {
        java.util.Map<java.lang.String, java.lang.Boolean> perHostRequiresExecCmdDetails = getPerHostRequiresExecCmdDetails(host);
        if ((perHostRequiresExecCmdDetails != null) && perHostRequiresExecCmdDetails.containsKey(component)) {
            org.apache.ambari.server.agent.AgentRequests.LOG.debug("Sending exec command details for {}", component);
            return perHostRequiresExecCmdDetails.get(component);
        }
        return java.lang.Boolean.FALSE;
    }

    private java.util.Map<java.lang.String, java.lang.Boolean> getPerHostRequiresExecCmdDetails(java.lang.String host) {
        if (!requiresExecCmdDetails.containsKey(host)) {
            synchronized(_lock) {
                if (!requiresExecCmdDetails.containsKey(host)) {
                    requiresExecCmdDetails.put(host, new java.util.HashMap<>());
                }
            }
        }
        return requiresExecCmdDetails.get(host);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder().append("requiresExecCmdDetails: ").append(requiresExecCmdDetails).toString();
    }
}