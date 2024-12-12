package org.apache.ambari.server.orm.dao;
public class HostAlertSummaryDTO extends org.apache.ambari.server.orm.dao.AlertSummaryDTO {
    private java.lang.String hostName;

    public HostAlertSummaryDTO(java.lang.String hostName, java.lang.Number ok, java.lang.Number warning, java.lang.Number critical, java.lang.Number unknown, java.lang.Number maintenance) {
        super(ok, warning, critical, unknown, maintenance);
        this.setHostName(hostName);
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }
}