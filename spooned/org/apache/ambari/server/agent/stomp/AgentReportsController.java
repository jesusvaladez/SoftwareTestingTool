package org.apache.ambari.server.agent.stomp;
import javax.ws.rs.WebApplicationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
@org.springframework.stereotype.Controller
@org.springframework.messaging.simp.annotation.SendToUser("/")
@org.springframework.messaging.handler.annotation.MessageMapping("/reports")
public class AgentReportsController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.AgentReportsController.class);

    @org.springframework.beans.factory.annotation.Autowired
    private javax.inject.Provider<org.apache.ambari.server.events.DefaultMessageEmitter> defaultMessageEmitterProvider;

    private final org.apache.ambari.server.agent.HeartBeatHandler hh;

    private final org.apache.ambari.server.agent.AgentSessionManager agentSessionManager;

    private final org.apache.ambari.server.agent.AgentReportsProcessor agentReportsProcessor;

    public AgentReportsController(com.google.inject.Injector injector) {
        hh = injector.getInstance(org.apache.ambari.server.agent.HeartBeatHandler.class);
        agentSessionManager = injector.getInstance(org.apache.ambari.server.agent.AgentSessionManager.class);
        agentReportsProcessor = injector.getInstance(org.apache.ambari.server.agent.AgentReportsProcessor.class);
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/component_version")
    public org.apache.ambari.server.agent.stomp.ReportsResponse handleComponentVersionReport(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.ComponentVersionReports message) throws javax.ws.rs.WebApplicationException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.apache.ambari.server.AmbariException {
        agentReportsProcessor.addAgentReport(new org.apache.ambari.server.agent.ComponentVersionAgentReport(hh, agentSessionManager.getHost(simpSessionId).getHostName(), message));
        return new org.apache.ambari.server.agent.stomp.ReportsResponse();
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/component_status")
    public org.apache.ambari.server.agent.stomp.ReportsResponse handleComponentReportStatus(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.ComponentStatusReports message) throws javax.ws.rs.WebApplicationException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.agent.ComponentStatus> statuses = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport>> clusterReport : message.getComponentStatusReports().entrySet()) {
            for (org.apache.ambari.server.agent.stomp.dto.ComponentStatusReport report : clusterReport.getValue()) {
                org.apache.ambari.server.agent.ComponentStatus componentStatus = new org.apache.ambari.server.agent.ComponentStatus();
                componentStatus.setClusterId(report.getClusterId());
                componentStatus.setComponentName(report.getComponentName());
                componentStatus.setServiceName(report.getServiceName());
                componentStatus.setStatus(report.getStatus());
                statuses.add(componentStatus);
            }
        }
        agentReportsProcessor.addAgentReport(new org.apache.ambari.server.agent.ComponentStatusAgentReport(hh, agentSessionManager.getHost(simpSessionId).getHostName(), statuses));
        return new org.apache.ambari.server.agent.stomp.ReportsResponse();
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/commands_status")
    public org.apache.ambari.server.agent.stomp.ReportsResponse handleCommandReportStatus(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.CommandStatusReports message) throws javax.ws.rs.WebApplicationException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.agent.CommandReport> statuses = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.server.agent.CommandReport>> clusterReport : message.getClustersComponentReports().entrySet()) {
            statuses.addAll(clusterReport.getValue());
        }
        agentReportsProcessor.addAgentReport(new org.apache.ambari.server.agent.CommandStatusAgentReport(hh, agentSessionManager.getHost(simpSessionId).getHostName(), statuses));
        return new org.apache.ambari.server.agent.stomp.ReportsResponse();
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/host_status")
    public org.apache.ambari.server.agent.stomp.ReportsResponse handleHostReportStatus(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.HostStatusReport message) throws org.apache.ambari.server.AmbariException {
        agentReportsProcessor.addAgentReport(new org.apache.ambari.server.agent.HostStatusAgentReport(hh, agentSessionManager.getHost(simpSessionId).getHostName(), message));
        return new org.apache.ambari.server.agent.stomp.ReportsResponse();
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/alerts_status")
    public org.apache.ambari.server.agent.stomp.ReportsResponse handleAlertsStatus(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.state.Alert[] message) throws org.apache.ambari.server.AmbariException {
        java.lang.String hostName = agentSessionManager.getHost(simpSessionId).getHostName();
        java.util.List<org.apache.ambari.server.state.Alert> alerts = java.util.Arrays.asList(message);
        org.apache.ambari.server.agent.stomp.AgentReportsController.LOG.debug("Handling {} alerts status for host {}", alerts.size(), hostName);
        hh.getHeartbeatProcessor().processAlerts(hostName, alerts);
        return new org.apache.ambari.server.agent.stomp.ReportsResponse();
    }

    @org.springframework.messaging.handler.annotation.MessageMapping("/responses")
    public org.apache.ambari.server.agent.stomp.ReportsResponse handleReceiveReport(@org.springframework.messaging.handler.annotation.Header
    java.lang.String simpSessionId, org.apache.ambari.server.agent.stomp.dto.AckReport ackReport) throws org.apache.ambari.server.HostNotRegisteredException {
        java.lang.Long hostId = agentSessionManager.getHost(simpSessionId).getHostId();
        org.apache.ambari.server.agent.stomp.AgentReportsController.LOG.debug("Handling agent receive report for execution message with messageId {}, status {}, reason {}", ackReport.getMessageId(), ackReport.getStatus(), ackReport.getReason());
        defaultMessageEmitterProvider.get().processReceiveReport(hostId, ackReport);
        return new org.apache.ambari.server.agent.stomp.ReportsResponse();
    }
}