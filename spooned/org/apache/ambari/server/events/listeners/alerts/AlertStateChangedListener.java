package org.apache.ambari.server.events.listeners.alerts;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertStateChangedListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.class);

    private static final org.slf4j.Logger ALERT_LOG = org.slf4j.LoggerFactory.getLogger("alerts");

    private static final java.lang.String ALERT_LOG_MESSAGE = "[{}] [{}] [{}] [{}] ({}) {}";

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_alertsDispatchDao;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    public AlertStateChangedListener(org.apache.ambari.server.events.publishers.AlertEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAlertEvent(org.apache.ambari.server.events.AlertStateChangeEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.LOG.debug("Received event {}", event);
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = event.getCurrentAlert();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = event.getNewHistoricalEntry();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = history.getAlertDefinition();
        org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.ALERT_LOG.info(org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.ALERT_LOG_MESSAGE, alert.getState(), current.getFirmness(), definition.getServiceName(), definition.getDefinitionName(), definition.getLabel(), alert.getText());
        if (current.getFirmness() == org.apache.ambari.server.state.AlertFirmness.SOFT) {
            return;
        }
        if ((history.getAlertState() == org.apache.ambari.server.state.AlertState.OK) && (event.getFromFirmness() == org.apache.ambari.server.state.AlertFirmness.SOFT)) {
            return;
        }
        org.apache.ambari.server.orm.entities.AlertCurrentEntity currentAlert = event.getCurrentAlert();
        if ((null != currentAlert) && (currentAlert.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.OFF)) {
            return;
        }
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = m_alertsDispatchDao.findGroupsByDefinition(definition);
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = new java.util.LinkedList<>();
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity group : groups) {
            java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = group.getAlertTargets();
            if ((null == targets) || (targets.size() == 0)) {
                continue;
            }
            for (org.apache.ambari.server.orm.entities.AlertTargetEntity target : targets) {
                if (!canDispatch(target, history, definition)) {
                    continue;
                }
                org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
                notice.setUuid(java.util.UUID.randomUUID().toString());
                notice.setAlertTarget(target);
                notice.setAlertHistory(event.getNewHistoricalEntry());
                notice.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
                notices.add(notice);
            }
        }
        if (!notices.isEmpty()) {
            m_alertsDispatchDao.createNotices(notices);
        }
    }

    private boolean canDispatch(org.apache.ambari.server.orm.entities.AlertTargetEntity target, org.apache.ambari.server.orm.entities.AlertHistoryEntity history, org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) {
        if (!target.isEnabled()) {
            return false;
        }
        java.util.Set<org.apache.ambari.server.state.AlertState> alertStates = target.getAlertStates();
        if ((null != alertStates) && (alertStates.size() > 0)) {
            if (!alertStates.contains(history.getAlertState())) {
                return false;
            }
        }
        java.lang.Long clusterId = history.getClusterId();
        try {
            org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getClusterById(clusterId);
            if (null != cluster.getUpgradeInProgress()) {
                java.lang.String serviceName = definition.getServiceName();
                if (!org.apache.commons.lang.StringUtils.equals(serviceName, org.apache.ambari.server.controller.RootService.AMBARI.name())) {
                    org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.LOG.debug("Skipping alert notifications for {} because the cluster is upgrading", definition.getDefinitionName(), target);
                    return false;
                }
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.LOG.warn("Unable to process an alert state change for cluster with ID {} because it does not exist", clusterId);
            return false;
        }
        return true;
    }
}