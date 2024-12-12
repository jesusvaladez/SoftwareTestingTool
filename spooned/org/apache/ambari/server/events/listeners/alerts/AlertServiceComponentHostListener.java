package org.apache.ambari.server.events.listeners.alerts;
@org.apache.ambari.server.EagerSingleton
public class AlertServiceComponentHostListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.alerts.AlertServiceComponentHostListener.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao = null;

    @com.google.inject.Inject
    public AlertServiceComponentHostListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onEvent(org.apache.ambari.server.events.ServiceComponentUninstalledEvent event) {
        org.apache.ambari.server.events.listeners.alerts.AlertServiceComponentHostListener.LOG.debug("Received event {}", event);
        long clusterId = event.getClusterId();
        java.lang.String serviceName = event.getServiceName();
        java.lang.String componentName = event.getComponentName();
        java.lang.String hostName = event.getHostName();
        m_alertsDao.removeCurrentByServiceComponentHost(clusterId, serviceName, componentName, hostName);
    }
}