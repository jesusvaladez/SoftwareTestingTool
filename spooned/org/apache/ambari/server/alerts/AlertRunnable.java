package org.apache.ambari.server.alerts;
public abstract class AlertRunnable implements java.lang.Runnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.alerts.AlertRunnable.class);

    protected final java.lang.String m_definitionName;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clustersProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_dao;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_alertEventPublisher;

    @com.google.inject.Inject
    protected org.apache.ambari.server.state.alert.AlertHelper alertHelper;

    public AlertRunnable(java.lang.String definitionName) {
        m_definitionName = definitionName;
    }

    abstract java.util.List<org.apache.ambari.server.state.Alert> execute(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) throws org.apache.ambari.server.AmbariException;

    @java.lang.Override
    public final void run() {
        try {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = m_clustersProvider.get().getClusters();
            for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = m_dao.findByName(cluster.getClusterId(), m_definitionName);
                if ((null == definition) || (!definition.getEnabled())) {
                    continue;
                }
                java.util.List<org.apache.ambari.server.state.Alert> alerts = execute(cluster, definition);
                for (org.apache.ambari.server.state.Alert alert : alerts) {
                    org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(cluster.getClusterId(), alert);
                    m_alertEventPublisher.publish(event);
                }
            }
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.alerts.AlertRunnable.LOG.error("Unable to run the {} alert", m_definitionName, exception);
        }
    }

    protected org.apache.ambari.server.state.Alert buildAlert(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.AlertDefinitionEntity myDefinition, org.apache.ambari.server.state.AlertState alertState, java.lang.String message) {
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(myDefinition.getDefinitionName(), null, myDefinition.getServiceName(), myDefinition.getComponentName(), null, alertState);
        alert.setLabel(myDefinition.getLabel());
        alert.setText(message);
        alert.setTimestamp(java.lang.System.currentTimeMillis());
        alert.setClusterId(cluster.getClusterId());
        return alert;
    }
}