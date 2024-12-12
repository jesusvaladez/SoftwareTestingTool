package org.apache.ambari.server.controller.metrics;
public class MetricsCollectorHAManager {
    @com.google.inject.Inject
    protected org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    private java.util.Map<java.lang.String, org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState> clusterCollectorHAState;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager.class);

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Boolean>> externalMetricCollectorsState = new java.util.HashMap<>();

    private org.apache.ambari.server.controller.metrics.CollectorHostDownRefreshCounter externalCollectorDownRefreshCounter = new org.apache.ambari.server.controller.metrics.CollectorHostDownRefreshCounter(5);

    public MetricsCollectorHAManager() {
        clusterCollectorHAState = new java.util.HashMap<>();
        if ((null == eventPublisher) && (null != org.apache.ambari.server.controller.AmbariServer.getController())) {
            eventPublisher = org.apache.ambari.server.controller.AmbariServer.getController().getAmbariEventPublisher();
            if (eventPublisher != null) {
                eventPublisher.register(this);
            } else {
                org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager.LOG.error("Unable to retrieve AmbariEventPublisher for Metric collector host event listening.");
            }
        }
    }

    public void addCollectorHost(java.lang.String clusterName, java.lang.String collectorHost) {
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager.LOG.info((("Adding collector host : " + collectorHost) + " to cluster : ") + clusterName);
        if (!clusterCollectorHAState.containsKey(clusterName)) {
            clusterCollectorHAState.put(clusterName, new org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState(clusterName));
        }
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState collectorHAClusterState = clusterCollectorHAState.get(clusterName);
        collectorHAClusterState.addMetricsCollectorHost(collectorHost);
    }

    public void addExternalMetricsCollectorHost(java.lang.String clusterName, java.lang.String collectorHost) {
        java.util.Map<java.lang.String, java.lang.Boolean> hostStateMap = new java.util.HashMap<>();
        hostStateMap.put(collectorHost, true);
        externalMetricCollectorsState.put(clusterName, hostStateMap);
    }

    public java.lang.String getCollectorHost(java.lang.String clusterName) {
        if (externalMetricCollectorsState.containsKey(clusterName)) {
            for (java.lang.String externalCollectorHost : externalMetricCollectorsState.get(clusterName).keySet()) {
                if (externalMetricCollectorsState.get(clusterName).get(externalCollectorHost)) {
                    return externalCollectorHost;
                }
            }
            return refreshAndReturnRandomExternalCollectorHost(clusterName);
        }
        if (!clusterCollectorHAState.containsKey(clusterName)) {
            clusterCollectorHAState.put(clusterName, new org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState(clusterName));
        }
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState collectorHAClusterState = clusterCollectorHAState.get(clusterName);
        return collectorHAClusterState.getCurrentCollectorHost();
    }

    private java.lang.String refreshAndReturnRandomExternalCollectorHost(java.lang.String clusterName) {
        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Boolean>> itr = externalMetricCollectorsState.get(clusterName).entrySet().iterator();
        while (itr.hasNext()) {
            java.util.Map.Entry<java.lang.String, java.lang.Boolean> entry = itr.next();
            entry.setValue(true);
        } 
        itr = externalMetricCollectorsState.get(clusterName).entrySet().iterator();
        return itr.next().getKey();
    }

    @com.google.common.eventbus.Subscribe
    public void onMetricsCollectorHostDownEvent(org.apache.ambari.server.events.MetricsCollectorHostDownEvent event) {
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager.LOG.debug("MetricsCollectorHostDownEvent caught, Down collector : {}", event.getCollectorHost());
        java.lang.String clusterName = event.getClusterName();
        if (externalMetricCollectorsState.containsKey(clusterName)) {
            if (externalCollectorDownRefreshCounter.testRefreshCounter()) {
                externalMetricCollectorsState.get(clusterName).put(event.getCollectorHost(), false);
            }
        } else {
            org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState collectorHAClusterState = clusterCollectorHAState.get(clusterName);
            collectorHAClusterState.onCollectorHostDown(event.getCollectorHost());
        }
    }

    public boolean isEmpty() {
        return this.clusterCollectorHAState.isEmpty() && externalMetricCollectorsState.isEmpty();
    }

    public boolean isExternalCollector() {
        return !externalMetricCollectorsState.isEmpty();
    }

    public boolean isCollectorHostLive(java.lang.String clusterName) {
        if (!externalMetricCollectorsState.isEmpty()) {
            return true;
        }
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState metricsCollectorHAClusterState = this.clusterCollectorHAState.get(clusterName);
        return (metricsCollectorHAClusterState != null) && metricsCollectorHAClusterState.isCollectorHostLive();
    }

    public boolean isCollectorComponentLive(java.lang.String clusterName) {
        if (!externalMetricCollectorsState.isEmpty()) {
            return true;
        }
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState metricsCollectorHAClusterState = this.clusterCollectorHAState.get(clusterName);
        return (metricsCollectorHAClusterState != null) && metricsCollectorHAClusterState.isCollectorComponentAlive();
    }
}