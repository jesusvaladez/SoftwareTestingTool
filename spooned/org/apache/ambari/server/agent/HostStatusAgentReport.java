package org.apache.ambari.server.agent;
public class HostStatusAgentReport extends org.apache.ambari.server.agent.AgentReport<org.apache.ambari.server.agent.stomp.dto.HostStatusReport> {
    private final org.apache.ambari.server.agent.HeartBeatHandler hh;

    public HostStatusAgentReport(org.apache.ambari.server.agent.HeartBeatHandler hh, java.lang.String hostName, org.apache.ambari.server.agent.stomp.dto.HostStatusReport hostStatusReport) {
        super(hostName, hostStatusReport);
        this.hh = hh;
    }

    @java.lang.Override
    protected void process(org.apache.ambari.server.agent.stomp.dto.HostStatusReport report, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        hh.handleHostReportStatus(report, hostName);
    }
}