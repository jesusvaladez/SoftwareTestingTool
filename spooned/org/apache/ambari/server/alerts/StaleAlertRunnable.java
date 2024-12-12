package org.apache.ambari.server.alerts;
import org.apache.commons.lang.StringUtils;
public class StaleAlertRunnable extends org.apache.ambari.server.alerts.AlertRunnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.alerts.StaleAlertRunnable.class);

    private static final java.lang.String ALL_ALERTS_CURRENT_MSG = "All alerts have run within their time intervals.";

    private static final java.lang.String STALE_ALERTS_MSG = "There are {0} stale alerts from {1} host(s):\n{2}";

    private static final java.lang.String TIMED_LABEL_MSG = "{0} ({1})";

    private static final java.lang.String HOST_LABEL_MSG = "{0}\n  [{1}]";

    private static final long MINUTE_TO_MS_CONVERSION = 60L * 1000L;

    private static final long MILLISECONDS_PER_MINUTE = 1000L * 60L;

    private static final int MINUTES_PER_DAY = 24 * 60;

    private static final int MINUTES_PER_HOUR = 60;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_definitionFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionsHolder;

    public StaleAlertRunnable(java.lang.String definitionName) {
        super(definitionName);
    }

    @java.lang.Override
    java.util.List<org.apache.ambari.server.state.Alert> execute(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.AlertDefinitionEntity myDefinition) {
        org.apache.ambari.server.state.alert.AlertDefinition alertDefinition = m_definitionFactory.coerce(myDefinition);
        int waitFactor = alertHelper.getWaitFactorMultiplier(alertDefinition);
        java.lang.management.RuntimeMXBean rb = java.lang.management.ManagementFactory.getRuntimeMXBean();
        long uptime = rb.getUptime();
        int totalStaleAlerts = 0;
        java.util.Set<java.lang.String> staleAlertGroupings = new java.util.TreeSet<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> staleAlertsByHost = new java.util.HashMap<>();
        java.util.Set<java.lang.String> hostsWithStaleAlerts = new java.util.TreeSet<>();
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_alertsDao.findCurrentByCluster(cluster.getClusterId());
        long now = java.lang.System.currentTimeMillis();
        java.util.Map<java.lang.Long, java.util.List<java.lang.Long>> alertDefinitionsToHosts = prepareHostDefinitions(cluster.getClusterId());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currentAlerts) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = current.getAlertHistory();
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity currentDefinition = history.getAlertDefinition();
            if (currentDefinition.getSourceType() == org.apache.ambari.server.state.alert.SourceType.AGGREGATE) {
                continue;
            }
            if (current.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.OFF) {
                continue;
            }
            if (current.getLatestTimestamp() == 0) {
                continue;
            }
            if (currentDefinition.getDefinitionName().equals(m_definitionName)) {
                continue;
            }
            long intervalInMillis = currentDefinition.getScheduleInterval() * org.apache.ambari.server.alerts.StaleAlertRunnable.MINUTE_TO_MS_CONVERSION;
            if (uptime <= (waitFactor * intervalInMillis)) {
                continue;
            }
            java.lang.Boolean timedout;
            java.lang.Long lastCheckTimestamp = 0L;
            java.lang.String currentHostName = history.getHostName();
            java.util.List<org.apache.ambari.server.state.Host> hosts = new java.util.ArrayList<>();
            if (currentHostName != null) {
                hosts.add(cluster.getHost(currentHostName));
            } else if (alertDefinitionsToHosts.containsKey(current.getDefinitionId())) {
                hosts = alertDefinitionsToHosts.get(current.getDefinitionId()).stream().map(i -> cluster.getHost(i)).collect(java.util.stream.Collectors.toList());
            }
            if (!hosts.isEmpty()) {
                timedout = true;
                for (org.apache.ambari.server.state.Host host : hosts) {
                    if (timedout) {
                        if (alertHelper.getStaleAlerts(host.getHostId()).containsKey(current.getDefinitionId())) {
                            lastCheckTimestamp = java.lang.Math.max(lastCheckTimestamp, alertHelper.getStaleAlerts(host.getHostId()).get(current.getDefinitionId()));
                        } else if (host.getState().equals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST)) {
                            lastCheckTimestamp = java.lang.Math.max(lastCheckTimestamp, host.getLastHeartbeatTime());
                        } else {
                            timedout = false;
                        }
                    }
                }
            } else {
                long timeDifference = now - current.getLatestTimestamp();
                timedout = timeDifference >= (waitFactor * intervalInMillis);
                lastCheckTimestamp = current.getOriginalTimestamp();
            }
            if (timedout) {
                totalStaleAlerts++;
                java.lang.String label = currentDefinition.getLabel();
                if (org.apache.commons.lang.StringUtils.isEmpty(label)) {
                    label = currentDefinition.getDefinitionName();
                }
                if (null != history.getHostName()) {
                    java.lang.String hostName = history.getHostName();
                    hostsWithStaleAlerts.add(hostName);
                    if (!staleAlertsByHost.containsKey(hostName)) {
                        staleAlertsByHost.put(hostName, new java.util.TreeSet<>());
                    }
                    long timeDifference = now - lastCheckTimestamp;
                    staleAlertsByHost.get(hostName).add(java.text.MessageFormat.format(org.apache.ambari.server.alerts.StaleAlertRunnable.TIMED_LABEL_MSG, label, org.apache.ambari.server.alerts.StaleAlertRunnable.millisToHumanReadableStr(timeDifference)));
                } else {
                    staleAlertGroupings.add(label);
                }
            }
        }
        for (java.lang.String host : staleAlertsByHost.keySet()) {
            staleAlertGroupings.add(java.text.MessageFormat.format(org.apache.ambari.server.alerts.StaleAlertRunnable.HOST_LABEL_MSG, host, org.apache.commons.lang.StringUtils.join(staleAlertsByHost.get(host), ",\n  ")));
        }
        org.apache.ambari.server.state.AlertState alertState = org.apache.ambari.server.state.AlertState.OK;
        java.lang.String alertText = org.apache.ambari.server.alerts.StaleAlertRunnable.ALL_ALERTS_CURRENT_MSG;
        if (!staleAlertGroupings.isEmpty()) {
            alertState = org.apache.ambari.server.state.AlertState.CRITICAL;
            alertText = java.text.MessageFormat.format(org.apache.ambari.server.alerts.StaleAlertRunnable.STALE_ALERTS_MSG, totalStaleAlerts, hostsWithStaleAlerts.size(), org.apache.commons.lang.StringUtils.join(staleAlertGroupings, ",\n"));
        }
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(myDefinition.getDefinitionName(), null, myDefinition.getServiceName(), myDefinition.getComponentName(), null, alertState);
        alert.setLabel(myDefinition.getLabel());
        alert.setText(alertText);
        alert.setTimestamp(now);
        alert.setClusterId(cluster.getClusterId());
        return java.util.Collections.singletonList(alert);
    }

    public java.util.Map<java.lang.Long, java.util.List<java.lang.Long>> prepareHostDefinitions(java.lang.Long clusterId) {
        java.util.Map<java.lang.Long, java.util.List<java.lang.Long>> alertDefinitionsToHosts = new java.util.HashMap<>();
        alertDefinitionsHolder.getData().entrySet().stream().filter(e -> e.getValue().getClusters() != null).filter(e -> e.getValue().getClusters().get(clusterId) != null).forEach(e -> e.getValue().getClusters().get(clusterId).getAlertDefinitions().stream().forEach(l -> {
            alertDefinitionsToHosts.putIfAbsent(l.getDefinitionId(), new java.util.ArrayList<>());
            alertDefinitionsToHosts.get(l.getDefinitionId()).add(e.getKey());
        }));
        return alertDefinitionsToHosts;
    }

    private static java.lang.String millisToHumanReadableStr(long milliseconds) {
        int min;
        int hour;
        int days;
        min = ((int) (milliseconds / org.apache.ambari.server.alerts.StaleAlertRunnable.MILLISECONDS_PER_MINUTE));
        days = min / org.apache.ambari.server.alerts.StaleAlertRunnable.MINUTES_PER_DAY;
        min = min % org.apache.ambari.server.alerts.StaleAlertRunnable.MINUTES_PER_DAY;
        hour = min / org.apache.ambari.server.alerts.StaleAlertRunnable.MINUTES_PER_HOUR;
        min = min % org.apache.ambari.server.alerts.StaleAlertRunnable.MINUTES_PER_HOUR;
        java.lang.String result = "";
        if (days > 0) {
            result += days + "d ";
        }
        if (hour > 0) {
            result += hour + "h ";
        }
        if (min > 0) {
            result += min + "m ";
        }
        return result.trim();
    }
}