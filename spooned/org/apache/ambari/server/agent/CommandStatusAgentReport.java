package org.apache.ambari.server.agent;
public class CommandStatusAgentReport extends org.apache.ambari.server.agent.AgentReport<java.util.List<org.apache.ambari.server.agent.CommandReport>> {
    private final org.apache.ambari.server.agent.HeartBeatHandler hh;

    public CommandStatusAgentReport(org.apache.ambari.server.agent.HeartBeatHandler hh, java.lang.String hostName, java.util.List<org.apache.ambari.server.agent.CommandReport> commandReports) {
        super(hostName, commandReports);
        this.hh = hh;
    }

    @java.lang.Override
    protected void process(java.util.List<org.apache.ambari.server.agent.CommandReport> report, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        hh.handleCommandReportStatus(report, hostName);
    }
}