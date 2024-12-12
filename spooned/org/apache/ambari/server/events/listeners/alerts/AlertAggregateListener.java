package org.apache.ambari.server.events.listeners.alerts;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertAggregateListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao = null;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    private final org.apache.ambari.server.events.publishers.AlertEventPublisher m_publisher;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.Alert> m_alertCache = new java.util.concurrent.ConcurrentHashMap<>();

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AggregateDefinitionMapping m_aggregateMapping;

    @com.google.inject.Inject
    public AlertAggregateListener(org.apache.ambari.server.events.publishers.AlertEventPublisher publisher) {
        m_publisher = publisher;
        m_publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    public void onInitialAlertEvent(org.apache.ambari.server.events.InitialAlertEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.LOG.debug("Received event {}", event);
        onAlertEvent(event.getClusterId(), event.getAlert().getName());
    }

    @com.google.common.eventbus.Subscribe
    public void onAlertStateChangeEvent(org.apache.ambari.server.events.AlertStateChangeEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.LOG.debug("Received event {}", event);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity currentEntity = event.getCurrentAlert();
        if (currentEntity.getFirmness() == org.apache.ambari.server.state.AlertFirmness.SOFT) {
            return;
        }
        onAlertEvent(event.getClusterId(), event.getAlert().getName());
    }

    @com.google.common.eventbus.Subscribe
    public void onAlertStateChangeEvent(org.apache.ambari.server.events.AggregateAlertRecalculateEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.LOG.debug("Received event {}", event);
        java.util.List<java.lang.String> alertNames = m_aggregateMapping.getAlertsWithAggregates(event.getClusterId());
        for (java.lang.String alertName : alertNames) {
            onAlertEvent(event.getClusterId(), alertName);
        }
    }

    private void onAlertEvent(long clusterId, java.lang.String alertName) {
        org.apache.ambari.server.state.alert.AlertDefinition aggregateDefinition = m_aggregateMapping.getAggregateDefinition(clusterId, alertName);
        if ((null == aggregateDefinition) || (null == m_alertsDao)) {
            return;
        }
        org.apache.ambari.server.state.alert.AggregateSource aggregateSource = ((org.apache.ambari.server.state.alert.AggregateSource) (aggregateDefinition.getSource()));
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = m_alertsDao.findAggregateCounts(clusterId, aggregateSource.getAlertName());
        int okCount = summary.getOkCount() + summary.getMaintenanceCount();
        int warningCount = summary.getWarningCount();
        int criticalCount = summary.getCriticalCount();
        int unknownCount = summary.getUnknownCount();
        int totalCount = ((okCount + warningCount) + criticalCount) + unknownCount;
        org.apache.ambari.server.state.Alert aggregateAlert = new org.apache.ambari.server.state.Alert(aggregateDefinition.getName(), null, aggregateDefinition.getServiceName(), null, null, org.apache.ambari.server.state.AlertState.UNKNOWN);
        aggregateAlert.setLabel(aggregateDefinition.getLabel());
        aggregateAlert.setTimestamp(java.lang.System.currentTimeMillis());
        aggregateAlert.setClusterId(clusterId);
        if (0 == totalCount) {
            aggregateAlert.setText("There are no instances of the aggregated alert.");
        } else if (summary.getUnknownCount() > 0) {
            aggregateAlert.setText("There are alerts with a state of UNKNOWN.");
        } else {
            org.apache.ambari.server.state.alert.Reporting reporting = aggregateSource.getReporting();
            int numerator = summary.getCriticalCount() + summary.getWarningCount();
            int denominator = totalCount;
            double value = ((double) (numerator)) / denominator;
            if (org.apache.ambari.server.state.alert.Reporting.ReportingType.PERCENT.equals(reporting.getType())) {
                value *= 100;
            }
            if (value >= reporting.getCritical().getValue()) {
                aggregateAlert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
                aggregateAlert.setText(java.text.MessageFormat.format(reporting.getCritical().getText(), denominator, numerator));
            } else if (value >= reporting.getWarning().getValue()) {
                aggregateAlert.setState(org.apache.ambari.server.state.AlertState.WARNING);
                aggregateAlert.setText(java.text.MessageFormat.format(reporting.getWarning().getText(), denominator, numerator));
            } else {
                aggregateAlert.setState(org.apache.ambari.server.state.AlertState.OK);
                aggregateAlert.setText(java.text.MessageFormat.format(reporting.getOk().getText(), denominator, numerator));
            }
        }
        boolean sendAlertEvent = true;
        org.apache.ambari.server.state.Alert cachedAlert = m_alertCache.get(aggregateAlert.getName());
        if (null != cachedAlert) {
            org.apache.ambari.server.state.AlertState cachedState = cachedAlert.getState();
            org.apache.ambari.server.state.AlertState alertState = aggregateAlert.getState();
            java.lang.String cachedText = cachedAlert.getText();
            java.lang.String alertText = aggregateAlert.getText();
            if ((cachedState == alertState) && org.apache.commons.lang.StringUtils.equals(cachedText, alertText)) {
                sendAlertEvent = false;
            }
        }
        m_alertCache.put(aggregateAlert.getName(), aggregateAlert);
        if (sendAlertEvent) {
            org.apache.ambari.server.events.AlertReceivedEvent aggEvent = new org.apache.ambari.server.events.AlertReceivedEvent(clusterId, aggregateAlert);
            m_publisher.publish(aggEvent);
        }
    }
}