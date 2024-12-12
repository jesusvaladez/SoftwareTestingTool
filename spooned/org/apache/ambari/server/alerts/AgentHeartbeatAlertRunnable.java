package org.apache.ambari.server.alerts;
public class AgentHeartbeatAlertRunnable extends org.apache.ambari.server.alerts.AlertRunnable {
    private static final java.lang.String INIT_MSG = "{0} is initializing";

    private static final java.lang.String HEALTHY_MSG = "{0} is healthy";

    private static final java.lang.String STATUS_UPDATE_MSG = "{0} is waiting for status updates";

    private static final java.lang.String HEARTBEAT_LOST_MSG = "{0} is not sending heartbeats";

    private static final java.lang.String UNHEALTHY_MSG = "{0} is not healthy";

    private static final java.lang.String UNKNOWN_MSG = "{0} has an unknown state of {1}";

    public AgentHeartbeatAlertRunnable(java.lang.String definitionName) {
        super(definitionName);
    }

    @java.lang.Override
    java.util.List<org.apache.ambari.server.state.Alert> execute(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) throws org.apache.ambari.server.AmbariException {
        long alertTimestamp = java.lang.System.currentTimeMillis();
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        java.util.List<org.apache.ambari.server.state.Alert> alerts = new java.util.ArrayList<>(hosts.size());
        for (org.apache.ambari.server.state.Host host : hosts) {
            java.lang.String hostName = host.getHostName();
            java.lang.String alertText;
            org.apache.ambari.server.state.AlertState alertState = org.apache.ambari.server.state.AlertState.OK;
            org.apache.ambari.server.state.HostState hostState = host.getState();
            switch (hostState) {
                case INIT :
                    alertText = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable.INIT_MSG, hostName);
                    break;
                case HEALTHY :
                    alertText = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable.HEALTHY_MSG, hostName);
                    break;
                case WAITING_FOR_HOST_STATUS_UPDATES :
                    alertText = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable.STATUS_UPDATE_MSG, hostName);
                    break;
                case HEARTBEAT_LOST :
                    alertState = org.apache.ambari.server.state.AlertState.CRITICAL;
                    alertText = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable.HEARTBEAT_LOST_MSG, hostName);
                    break;
                case UNHEALTHY :
                    alertState = org.apache.ambari.server.state.AlertState.CRITICAL;
                    alertText = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable.UNHEALTHY_MSG, hostName);
                    break;
                default :
                    alertState = org.apache.ambari.server.state.AlertState.UNKNOWN;
                    alertText = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable.UNKNOWN_MSG, hostName, hostState);
                    break;
            }
            org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definition.getDefinitionName(), null, definition.getServiceName(), definition.getComponentName(), hostName, alertState);
            alert.setLabel(definition.getLabel());
            alert.setText(alertText);
            alert.setTimestamp(alertTimestamp);
            alert.setClusterId(cluster.getClusterId());
            alerts.add(alert);
        }
        return alerts;
    }
}