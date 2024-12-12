package org.apache.ambari.server.events;
public final class ActionFinalReportReceivedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private java.lang.Long clusterId;

    private java.lang.String hostname;

    private org.apache.ambari.server.agent.CommandReport commandReport;

    private java.lang.String role;

    private java.lang.Boolean emulated;

    public ActionFinalReportReceivedEvent(java.lang.Long clusterId, java.lang.String hostname, org.apache.ambari.server.agent.CommandReport report, java.lang.Boolean emulated) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.ACTION_EXECUTION_FINISHED);
        this.clusterId = clusterId;
        this.hostname = hostname;
        this.commandReport = report;
        if (report.getRole() != null) {
            this.role = report.getRole();
        } else {
            this.role = null;
        }
        this.emulated = emulated;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public java.lang.String getHostname() {
        return hostname;
    }

    public org.apache.ambari.server.agent.CommandReport getCommandReport() {
        return commandReport;
    }

    public java.lang.String getRole() {
        return role;
    }

    public java.lang.Boolean isEmulated() {
        return emulated;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("ActionFinalReportReceivedEvent{" + "clusterId=") + clusterId) + ", hostname='") + hostname) + '\'') + ", commandReportStatus=") + commandReport.getStatus()) + ", commandReportRole=") + role) + '}';
    }
}