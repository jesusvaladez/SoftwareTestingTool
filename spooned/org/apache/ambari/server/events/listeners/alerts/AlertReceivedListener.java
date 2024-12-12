package org.apache.ambari.server.events.listeners.alerts;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class AlertReceivedListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration m_configuration;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.controller.MaintenanceStateHelper> m_maintenanceStateHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertHelper alertHelper;

    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_alertEventPublisher;

    private com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> creationLocks = com.google.common.util.concurrent.Striped.lazyWeakLock(100);

    @com.google.inject.Inject
    public AlertReceivedListener(org.apache.ambari.server.events.publishers.AlertEventPublisher publisher) {
        m_alertEventPublisher = publisher;
        m_alertEventPublisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    @org.apache.ambari.server.orm.RequiresSession
    public void onAlertEvent(org.apache.ambari.server.events.AlertReceivedEvent event) throws org.apache.ambari.server.AmbariException {
        if (org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.isDebugEnabled()) {
            org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.debug(event.toString());
        }
        java.util.List<org.apache.ambari.server.state.Alert> alerts = event.getAlerts();
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> toMerge = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> toCreateHistoryAndMerge = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.events.AlertEvent> alertEvents = new java.util.ArrayList<>(20);
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> alertUpdates = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Alert alert : alerts) {
            java.lang.Long clusterId = alert.getClusterId();
            if (clusterId == null) {
                clusterId = event.getClusterId();
            }
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = m_definitionDao.findByName(clusterId, alert.getName());
            if (null == definition) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.warn("Received an alert for {} which is a definition that does not exist in cluster id={}", alert.getName(), clusterId);
                continue;
            }
            alert.setComponent(definition.getComponentName());
            alert.setLabel(definition.getComponentName());
            alert.setService(definition.getServiceName());
            if (!isValid(alert)) {
                continue;
            }
            if (!definition.getEnabled()) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.debug("Received an alert for {} which is disabled. No more alerts should be received for this definition.", alert.getName());
                continue;
            }
            updateAlertDetails(alert, definition);
            if (!isValid(alert)) {
                continue;
            }
            org.apache.ambari.server.orm.entities.AlertCurrentEntity current;
            org.apache.ambari.server.state.AlertState alertState = alert.getState();
            current = getCurrentEntity(clusterId, alert, definition);
            if (null == current) {
                if (alertState == org.apache.ambari.server.state.AlertState.SKIPPED) {
                    continue;
                }
                int key = java.util.Objects.hash(clusterId, alert.getName(), alert.getHostName());
                java.util.concurrent.locks.Lock lock = creationLocks.get(key);
                lock.lock();
                try {
                    current = getCurrentEntity(clusterId, alert, definition);
                    if (null != current) {
                        continue;
                    }
                    org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createHistory(clusterId, definition, alert);
                    org.apache.ambari.server.state.MaintenanceState maintenanceState = getMaintenanceState(alert, clusterId);
                    current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
                    current.setMaintenanceState(maintenanceState);
                    current.setAlertHistory(history);
                    current.setLatestTimestamp(alert.getTimestamp());
                    current.setOriginalTimestamp(alert.getTimestamp());
                    clearStaleAlerts(alert.getHostName(), definition.getDefinitionId());
                    current.setFirmness(org.apache.ambari.server.state.AlertFirmness.HARD);
                    m_alertsDao.create(current);
                    alertEvents.add(new org.apache.ambari.server.events.InitialAlertEvent(clusterId, alert, current));
                    if (!alertUpdates.containsKey(clusterId)) {
                        alertUpdates.put(clusterId, new java.util.HashMap<>());
                    }
                    java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> summaries = alertUpdates.get(clusterId);
                    org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.updateSummary(summaries, definition.getDefinitionId(), definition.getDefinitionName(), alertState, alert.getTimestamp(), maintenanceState, alert.getText());
                } finally {
                    lock.unlock();
                }
            } else if ((alertState == current.getAlertHistory().getAlertState()) || (alertState == org.apache.ambari.server.state.AlertState.SKIPPED)) {
                current.setLatestTimestamp(alert.getTimestamp());
                clearStaleAlerts(alert.getHostName(), definition.getDefinitionId());
                if (alertState != org.apache.ambari.server.state.AlertState.SKIPPED) {
                    current.setLatestText(alert.getText());
                    long occurrences = current.getOccurrences() + 1;
                    current.setOccurrences(occurrences);
                    org.apache.ambari.server.state.AlertFirmness firmness = current.getFirmness();
                    int repeatTolerance = getRepeatTolerance(definition, clusterId);
                    if ((firmness == org.apache.ambari.server.state.AlertFirmness.SOFT) && (occurrences >= repeatTolerance)) {
                        current.setFirmness(org.apache.ambari.server.state.AlertFirmness.HARD);
                        org.apache.ambari.server.events.AlertStateChangeEvent stateChangedEvent = new org.apache.ambari.server.events.AlertStateChangeEvent(clusterId, alert, current, alertState, firmness);
                        alertEvents.add(stateChangedEvent);
                    }
                }
                if (alertState == org.apache.ambari.server.state.AlertState.SKIPPED) {
                    java.lang.String alertText = alert.getText();
                    if (org.apache.commons.lang.StringUtils.isNotBlank(alertText)) {
                        current.setLatestText(alertText);
                    }
                }
                toMerge.add(current);
            } else {
                if (org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.debug("Alert State Changed: CurrentId {}, CurrentTimestamp {}, HistoryId {}, HistoryState {}", current.getAlertId(), current.getLatestTimestamp(), current.getAlertHistory().getAlertId(), current.getAlertHistory().getAlertState());
                }
                org.apache.ambari.server.orm.entities.AlertHistoryEntity oldHistory = current.getAlertHistory();
                org.apache.ambari.server.state.AlertState oldState = oldHistory.getAlertState();
                org.apache.ambari.server.state.AlertFirmness oldFirmness = current.getFirmness();
                org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createHistory(clusterId, oldHistory.getAlertDefinition(), alert);
                current.setLatestTimestamp(alert.getTimestamp());
                current.setOriginalTimestamp(alert.getTimestamp());
                current.setLatestText(alert.getText());
                clearStaleAlerts(alert.getHostName(), definition.getDefinitionId());
                current.setAlertHistory(history);
                switch (alertState) {
                    case OK :
                        current.setOccurrences(1);
                        break;
                    case CRITICAL :
                    case SKIPPED :
                    case UNKNOWN :
                    case WARNING :
                        if (oldState == org.apache.ambari.server.state.AlertState.OK) {
                            current.setOccurrences(1);
                        } else {
                            current.setOccurrences(current.getOccurrences() + 1);
                        }
                        break;
                    default :
                        break;
                }
                org.apache.ambari.server.state.AlertFirmness firmness = calculateFirmnessForStateChange(clusterId, definition, alertState, current.getOccurrences());
                current.setFirmness(firmness);
                toCreateHistoryAndMerge.add(current);
                alertEvents.add(new org.apache.ambari.server.events.AlertStateChangeEvent(clusterId, alert, current, oldState, oldFirmness));
                org.apache.ambari.server.state.MaintenanceState maintenanceState = getMaintenanceState(alert, clusterId);
                if (!alertUpdates.containsKey(clusterId)) {
                    alertUpdates.put(clusterId, new java.util.HashMap<>());
                }
                java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> summaries = alertUpdates.get(clusterId);
                org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.updateSummary(summaries, definition.getDefinitionId(), definition.getDefinitionName(), alertState, alert.getTimestamp(), maintenanceState, alert.getText());
            }
        }
        m_alertsDao.saveEntities(toMerge, toCreateHistoryAndMerge);
        for (org.apache.ambari.server.events.AlertEvent eventToFire : alertEvents) {
            m_alertEventPublisher.publish(eventToFire);
        }
        if (!alertUpdates.isEmpty()) {
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AlertUpdateEvent(alertUpdates));
        }
    }

    private void clearStaleAlerts(java.lang.String hostName, java.lang.Long definitionId) throws org.apache.ambari.server.AmbariException {
        if (org.eclipse.jetty.util.StringUtil.isNotBlank(hostName)) {
            org.apache.ambari.server.state.Host host = m_clusters.get().getHosts().stream().filter(h -> h.getHostName().equals(hostName)).findFirst().orElse(null);
            if (host != null) {
                alertHelper.clearStaleAlert(host.getHostId(), definitionId);
            }
        } else {
            alertHelper.clearStaleAlert(definitionId);
        }
    }

    private void updateAlertDetails(org.apache.ambari.server.state.Alert alert, org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) {
        if (alert.getService() == null) {
            alert.setService(definition.getServiceName());
        }
        if (alert.getComponent() == null) {
            alert.setComponent(definition.getComponentName());
        }
    }

    private org.apache.ambari.server.state.MaintenanceState getMaintenanceState(org.apache.ambari.server.state.Alert alert, java.lang.Long clusterId) {
        org.apache.ambari.server.state.MaintenanceState maintenanceState = org.apache.ambari.server.state.MaintenanceState.OFF;
        try {
            maintenanceState = m_maintenanceStateHelper.get().getEffectiveState(clusterId, alert);
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.error("Unable to determine the maintenance mode state for {}, defaulting to OFF", alert, exception);
        }
        return maintenanceState;
    }

    private org.apache.ambari.server.orm.entities.AlertCurrentEntity getCurrentEntity(long clusterId, org.apache.ambari.server.state.Alert alert, org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) {
        if (org.apache.commons.lang.StringUtils.isBlank(alert.getHostName()) || definition.isHostIgnored()) {
            return m_alertsDao.findCurrentByNameNoHost(clusterId, alert.getName());
        } else {
            return m_alertsDao.findCurrentByHostAndName(clusterId, alert.getHostName(), alert.getName());
        }
    }

    private boolean isValid(org.apache.ambari.server.state.Alert alert) {
        java.lang.Long clusterId = alert.getClusterId();
        java.lang.String serviceName = alert.getService();
        java.lang.String componentName = alert.getComponent();
        java.lang.String hostName = alert.getHostName();
        java.lang.String ambariServiceName = org.apache.ambari.server.controller.RootService.AMBARI.name();
        java.lang.String ambariServerComponentName = org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name();
        java.lang.String ambariAgentComponentName = org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name();
        if (ambariServiceName.equals(serviceName) && ambariServerComponentName.equals(componentName)) {
            return true;
        }
        org.apache.ambari.server.state.Clusters clusters = m_clusters.get();
        if (clusterId == null) {
            if (org.apache.commons.lang.StringUtils.isBlank(hostName)) {
                return true;
            }
            if (!clusters.hostExists(hostName)) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.error("Unable to process alert {} for an invalid host named {}", alert.getName(), hostName);
                return false;
            }
            return true;
        }
        final org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = clusters.getCluster(clusterId);
            if (null == cluster) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.error("Unable to process alert {} for cluster id={}", alert.getName(), clusterId);
                return false;
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            java.lang.String msg = java.lang.String.format("Unable to process alert %s for cluster id=%s", alert.getName(), clusterId);
            org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.error(msg, ambariException);
            return false;
        }
        if (ambariServiceName.equals(serviceName) && ambariAgentComponentName.equals(componentName)) {
            if ((org.apache.commons.lang.StringUtils.isBlank(hostName) || (!clusters.hostExists(hostName))) || (!clusters.isHostMappedToCluster(clusterId, hostName))) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.warn("Unable to process alert {} for cluster {} and host {} because the host is not a part of the cluster.", alert.getName(), hostName);
                return false;
            }
            return true;
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(hostName)) {
            if (!clusters.hostExists(hostName)) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.warn("Unable to process alert {} for an invalid host named {}", alert.getName(), hostName);
                return false;
            }
            if (!cluster.getServices().containsKey(serviceName)) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.warn("Unable to process alert {} for an invalid service named {}", alert.getName(), serviceName);
                return false;
            }
            if ((null != componentName) && (!cluster.getHosts(serviceName, componentName).contains(hostName))) {
                org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.warn("Unable to process alert {} for an invalid service {} and component {} on host {}", alert.getName(), serviceName, componentName, hostName);
                return false;
            }
        }
        return true;
    }

    private org.apache.ambari.server.orm.entities.AlertHistoryEntity createHistory(long clusterId, org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition, org.apache.ambari.server.state.Alert alert) {
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertDefinition(definition);
        history.setAlertDefinitionId(definition.getDefinitionId());
        history.setAlertLabel(definition.getLabel());
        history.setAlertInstance(alert.getInstance());
        history.setAlertState(alert.getState());
        history.setAlertText(alert.getText());
        history.setAlertTimestamp(alert.getTimestamp());
        history.setClusterId(clusterId);
        history.setComponentName(alert.getComponent());
        history.setServiceName(alert.getService());
        if (definition.isHostIgnored()) {
            history.setHostName(null);
        } else {
            history.setHostName(alert.getHostName());
        }
        return history;
    }

    private org.apache.ambari.server.state.AlertFirmness calculateFirmnessForStateChange(java.lang.Long clusterId, org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition, org.apache.ambari.server.state.AlertState state, long occurrences) {
        if (state == org.apache.ambari.server.state.AlertState.OK) {
            return org.apache.ambari.server.state.AlertFirmness.HARD;
        }
        if (definition.getSourceType() == org.apache.ambari.server.state.alert.SourceType.AGGREGATE) {
            return org.apache.ambari.server.state.AlertFirmness.HARD;
        }
        int tolerance = getRepeatTolerance(definition, clusterId);
        if (tolerance <= 1) {
            return org.apache.ambari.server.state.AlertFirmness.HARD;
        }
        if (tolerance <= occurrences) {
            return org.apache.ambari.server.state.AlertFirmness.HARD;
        }
        return org.apache.ambari.server.state.AlertFirmness.SOFT;
    }

    private int getRepeatTolerance(org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition, java.lang.Long clusterId) {
        if (definition.isRepeatToleranceEnabled()) {
            return definition.getRepeatTolerance();
        }
        int repeatTolerance = 1;
        try {
            org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getCluster(clusterId);
            java.lang.String value = cluster.getClusterProperty(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_ALERT_REPEAT_TOLERANCE, "1");
            repeatTolerance = org.apache.commons.lang.math.NumberUtils.toInt(value, 1);
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            java.lang.String msg = java.lang.String.format("Unable to read %s/%s from cluster %s, defaulting to 1", org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_ALERT_REPEAT_TOLERANCE, clusterId);
            org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.LOG.warn(msg, ambariException);
        }
        return repeatTolerance;
    }
}