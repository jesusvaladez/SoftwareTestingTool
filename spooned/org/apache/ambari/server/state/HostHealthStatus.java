package org.apache.ambari.server.state;
public class HostHealthStatus {
    private org.apache.ambari.server.state.HostHealthStatus.HealthStatus healthStatus;

    private java.lang.String healthReport;

    public HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus healthStatus, java.lang.String healthReport) {
        super();
        this.healthStatus = healthStatus;
        this.healthReport = healthReport;
    }

    public synchronized org.apache.ambari.server.state.HostHealthStatus.HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public synchronized void setHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public synchronized void setHealthReport(java.lang.String healthReport) {
        this.healthReport = healthReport;
    }

    public synchronized java.lang.String getHealthReport() {
        return healthReport;
    }

    public enum HealthStatus {

        UNKNOWN,
        HEALTHY,
        UNHEALTHY,
        ALERT;}
}