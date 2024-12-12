package org.apache.ambari.server.agent;
public class ComponentStatusAgentReport extends org.apache.ambari.server.agent.AgentReport<java.util.List<org.apache.ambari.server.agent.ComponentStatus>> {
    private final org.apache.ambari.server.agent.HeartBeatHandler hh;

    public ComponentStatusAgentReport(org.apache.ambari.server.agent.HeartBeatHandler hh, java.lang.String hostName, java.util.List<org.apache.ambari.server.agent.ComponentStatus> componentStatuses) {
        super(hostName, componentStatuses);
        this.hh = hh;
    }

    @java.lang.Override
    protected void process(java.util.List<org.apache.ambari.server.agent.ComponentStatus> report, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        hh.handleComponentReportStatus(report, hostName);
    }
}