package org.apache.ambari.server.agent;
public class ComponentVersionAgentReport extends org.apache.ambari.server.agent.AgentReport<org.apache.ambari.server.agent.stomp.dto.ComponentVersionReports> {
    private final org.apache.ambari.server.agent.HeartBeatHandler hh;

    public ComponentVersionAgentReport(org.apache.ambari.server.agent.HeartBeatHandler hh, java.lang.String hostName, org.apache.ambari.server.agent.stomp.dto.ComponentVersionReports componentVersionReports) {
        super(hostName, componentVersionReports);
        this.hh = hh;
    }

    @java.lang.Override
    protected void process(org.apache.ambari.server.agent.stomp.dto.ComponentVersionReports report, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        hh.handleComponentVersionReports(report, hostName);
    }
}