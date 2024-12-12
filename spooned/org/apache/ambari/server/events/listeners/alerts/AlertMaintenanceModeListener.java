package org.apache.ambari.server.events.listeners.alerts;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertMaintenanceModeListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertMaintenanceModeListener.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_alertEventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao = null;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AggregateDefinitionMapping m_aggregateMapping;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    private long clusterId = -1;

    @com.google.inject.Inject
    public AlertMaintenanceModeListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onEvent(org.apache.ambari.server.events.MaintenanceModeEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertMaintenanceModeListener.LOG.debug("Received event {}", event);
        boolean recalculateAggregateAlert = false;
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_alertsDao.findCurrent();
        org.apache.ambari.server.state.MaintenanceState newMaintenanceState = org.apache.ambari.server.state.MaintenanceState.OFF;
        if (event.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.OFF) {
            newMaintenanceState = org.apache.ambari.server.state.MaintenanceState.ON;
        }
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> alertUpdates = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity currentAlert : currentAlerts) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = currentAlert.getAlertHistory();
            java.lang.String alertHostName = history.getHostName();
            java.lang.String alertServiceName = history.getServiceName();
            java.lang.String alertComponentName = history.getComponentName();
            try {
                org.apache.ambari.server.state.Host host = event.getHost();
                org.apache.ambari.server.state.Service service = event.getService();
                org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = event.getServiceComponentHost();
                if (null != host) {
                    java.lang.String hostName = host.getHostName();
                    if (hostName.equals(alertHostName)) {
                        if (updateMaintenanceStateAndRecalculateAggregateAlert(history, currentAlert, newMaintenanceState, alertUpdates))
                            recalculateAggregateAlert = true;

                        continue;
                    }
                } else if (null != service) {
                    java.lang.String serviceName = service.getName();
                    if (serviceName.equals(alertServiceName)) {
                        if (updateMaintenanceStateAndRecalculateAggregateAlert(history, currentAlert, newMaintenanceState, alertUpdates))
                            recalculateAggregateAlert = true;

                        continue;
                    }
                } else if (null != serviceComponentHost) {
                    java.lang.String hostName = serviceComponentHost.getHostName();
                    java.lang.String serviceName = serviceComponentHost.getServiceName();
                    java.lang.String componentName = serviceComponentHost.getServiceComponentName();
                    if ((hostName.equals(alertHostName) && serviceName.equals(alertServiceName)) && componentName.equals(alertComponentName)) {
                        if (updateMaintenanceStateAndRecalculateAggregateAlert(history, currentAlert, newMaintenanceState, alertUpdates))
                            recalculateAggregateAlert = true;

                        continue;
                    }
                }
            } catch (java.lang.Exception exception) {
                org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = history.getAlertDefinition();
                org.apache.ambari.server.events.listeners.alerts.AlertMaintenanceModeListener.LOG.error("Unable to put alert '{}' for host {} into maintenance mode", definition.getDefinitionName(), alertHostName, exception);
            }
        }
        if (!alertUpdates.isEmpty()) {
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AlertUpdateEvent(alertUpdates));
        }
        if (recalculateAggregateAlert) {
            m_alertEventPublisher.publish(new org.apache.ambari.server.events.AggregateAlertRecalculateEvent(clusterId));
        }
    }

    private boolean updateMaintenanceStateAndRecalculateAggregateAlert(org.apache.ambari.server.orm.entities.AlertHistoryEntity historyAlert, org.apache.ambari.server.orm.entities.AlertCurrentEntity currentAlert, org.apache.ambari.server.state.MaintenanceState maintenanceState, java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> alertUpdates) {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definitionEntity = currentAlert.getAlertHistory().getAlertDefinition();
        if ((maintenanceState != org.apache.ambari.server.state.MaintenanceState.OFF) && (maintenanceState != org.apache.ambari.server.state.MaintenanceState.ON)) {
            org.apache.ambari.server.events.listeners.alerts.AlertMaintenanceModeListener.LOG.warn("Unable to set invalid maintenance state of {} on the alert {}", maintenanceState, definitionEntity.getDefinitionName());
            return false;
        }
        org.apache.ambari.server.state.MaintenanceState currentState = currentAlert.getMaintenanceState();
        if (currentState == maintenanceState) {
            return false;
        }
        currentAlert.setMaintenanceState(maintenanceState);
        m_alertsDao.merge(currentAlert);
        org.apache.ambari.server.state.AlertState alertState = historyAlert.getAlertState();
        if (!alertUpdates.containsKey(historyAlert.getClusterId())) {
            alertUpdates.put(historyAlert.getClusterId(), new java.util.HashMap<>());
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> summaries = alertUpdates.get(historyAlert.getClusterId());
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.updateSummary(summaries, definitionEntity.getDefinitionId(), definitionEntity.getDefinitionName(), alertState, -1L, maintenanceState, "");
        if (org.apache.ambari.server.state.AlertState.RECALCULATE_AGGREGATE_ALERT_STATES.contains(alertState)) {
            clusterId = historyAlert.getClusterId();
            java.lang.String alertName = historyAlert.getAlertDefinition().getDefinitionName();
            if (m_aggregateMapping.getAggregateDefinition(clusterId, alertName) != null) {
                return true;
            }
        }
        return false;
    }
}