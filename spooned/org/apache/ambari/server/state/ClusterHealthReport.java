package org.apache.ambari.server.state;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonProperty;
public class ClusterHealthReport {
    private static final java.lang.String HOST_STALE_CONFIG = "Host/stale_config";

    private static final java.lang.String HOST_MAINTENANCE_STATE = "Host/maintenance_state";

    private static final java.lang.String HOST_HOST_STATE_HEALTHY = "Host/host_state/HEALTHY";

    private static final java.lang.String HOST_HOST_STATE_UNHEALTHY = "Host/host_state/UNHEALTHY";

    private static final java.lang.String HOST_HOST_STATE_INIT = "Host/host_state/INIT";

    private static final java.lang.String HOST_HOST_STATUS_HEALTHY = "Host/host_status/HEALTHY";

    private static final java.lang.String HOST_HOST_STATUS_UNHEALTHY = "Host/host_status/UNHEALTHY";

    private static final java.lang.String HOST_HOST_STATUS_UNKNOWN = "Host/host_status/UNKNOWN";

    private static final java.lang.String HOST_HOST_STATUS_ALERT = "Host/host_status/ALERT";

    private static final java.lang.String HOST_HOST_STATE_HEARTBEAT_LOST = "Host/host_state/HEARTBEAT_LOST";

    private int staleConfigsHosts;

    private int maintenanceStateHosts;

    private int healthyStateHosts;

    private int unhealthyStateHosts;

    private int heartbeatLostStateHosts;

    private int initStateHosts;

    private int healthyStatusHosts;

    private int unhealthyStatusHosts;

    private int unknownStatusHosts;

    private int alertStatusHosts;

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_STALE_CONFIG)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_STALE_CONFIG)
    public int getStaleConfigsHosts() {
        return staleConfigsHosts;
    }

    public void setStaleConfigsHosts(int staleConfigsHosts) {
        this.staleConfigsHosts = staleConfigsHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_MAINTENANCE_STATE)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_MAINTENANCE_STATE)
    public int getMaintenanceStateHosts() {
        return maintenanceStateHosts;
    }

    public void setMaintenanceStateHosts(int maintenanceStateHosts) {
        this.maintenanceStateHosts = maintenanceStateHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_HEALTHY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_HEALTHY)
    public int getHealthyStateHosts() {
        return healthyStateHosts;
    }

    public void setHealthyStateHosts(int healthyStateHosts) {
        this.healthyStateHosts = healthyStateHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_UNHEALTHY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_UNHEALTHY)
    public int getUnhealthyStateHosts() {
        return unhealthyStateHosts;
    }

    public void setUnhealthyStateHosts(int unhealthyStateHosts) {
        this.unhealthyStateHosts = unhealthyStateHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_INIT)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_INIT)
    public int getInitStateHosts() {
        return initStateHosts;
    }

    public void setInitStateHosts(int initStateHosts) {
        this.initStateHosts = initStateHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_HEALTHY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_HEALTHY)
    public int getHealthyStatusHosts() {
        return healthyStatusHosts;
    }

    public void setHealthyStatusHosts(int healthyStatusHosts) {
        this.healthyStatusHosts = healthyStatusHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_UNHEALTHY)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_UNHEALTHY)
    public int getUnhealthyStatusHosts() {
        return unhealthyStatusHosts;
    }

    public void setUnhealthyStatusHosts(int unhealthyStatusHosts) {
        this.unhealthyStatusHosts = unhealthyStatusHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_UNKNOWN)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_UNKNOWN)
    public int getUnknownStatusHosts() {
        return unknownStatusHosts;
    }

    public void setUnknownStatusHosts(int unknownStatusHosts) {
        this.unknownStatusHosts = unknownStatusHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_ALERT)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATUS_ALERT)
    public int getAlertStatusHosts() {
        return alertStatusHosts;
    }

    public void setAlertStatusHosts(int alertStatusHosts) {
        this.alertStatusHosts = alertStatusHosts;
    }

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_HEARTBEAT_LOST)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.ClusterHealthReport.HOST_HOST_STATE_HEARTBEAT_LOST)
    public int getHeartbeatLostStateHosts() {
        return heartbeatLostStateHosts;
    }

    public void setHeartbeatLostStateHosts(int heartbeatLostStateHosts) {
        this.heartbeatLostStateHosts = heartbeatLostStateHosts;
    }

    public ClusterHealthReport() {
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((("ClusterHealthReport{" + "staleConfigsHosts=") + staleConfigsHosts) + ", maintenanceStateHosts=") + maintenanceStateHosts) + ", healthyStateHosts=") + healthyStateHosts) + ", unhealthyStateHosts=") + unhealthyStateHosts) + ", heartbeatLostStateHosts=") + heartbeatLostStateHosts) + ", initStateHosts=") + initStateHosts) + ", healthyStatusHosts=") + healthyStatusHosts) + ", unhealthyStatusHosts=") + unhealthyStatusHosts) + ", unknownStatusHosts=") + unknownStatusHosts) + ", alertStatusHosts=") + alertStatusHosts) + '}';
    }
}