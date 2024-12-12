package org.apache.ambari.server.controller.metrics;
public class MetricsCollectorHAClusterState {
    private java.lang.String clusterName;

    private java.util.Set<java.lang.String> liveCollectorHosts;

    private java.util.Set<java.lang.String> deadCollectorHosts;

    private org.apache.ambari.server.controller.metrics.CollectorHostDownRefreshCounter collectorDownRefreshCounter = new org.apache.ambari.server.controller.metrics.CollectorHostDownRefreshCounter(5);

    private java.lang.String currentCollectorHost = null;

    @com.google.inject.Inject
    org.apache.ambari.server.controller.AmbariManagementController managementController;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState.class);

    public MetricsCollectorHAClusterState(java.lang.String clusterName) {
        if (managementController == null) {
            managementController = org.apache.ambari.server.controller.AmbariServer.getController();
        }
        this.clusterName = clusterName;
        this.liveCollectorHosts = new java.util.concurrent.CopyOnWriteArraySet<>();
        this.deadCollectorHosts = new java.util.concurrent.CopyOnWriteArraySet<>();
    }

    public void addMetricsCollectorHost(java.lang.String collectorHost) {
        if (org.apache.ambari.server.controller.internal.HostStatusHelper.isHostComponentLive(managementController, clusterName, collectorHost, "AMBARI_METRICS", org.apache.ambari.server.Role.METRICS_COLLECTOR.name())) {
            liveCollectorHosts.add(collectorHost);
            deadCollectorHosts.remove(collectorHost);
        } else {
            deadCollectorHosts.add(collectorHost);
            liveCollectorHosts.remove(collectorHost);
        }
        if ((currentCollectorHost == null) || (!org.apache.ambari.server.controller.internal.HostStatusHelper.isHostComponentLive(managementController, clusterName, currentCollectorHost, "AMBARI_METRICS", org.apache.ambari.server.Role.METRICS_COLLECTOR.name()))) {
            refreshCollectorHost(currentCollectorHost);
        }
    }

    private void refreshCollectorHost(java.lang.String currentHost) {
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState.LOG.info("Refreshing collector host, current collector host : " + currentHost);
        testAndAddDeadCollectorsToLiveList();
        if (currentHost != null) {
            if (liveCollectorHosts.contains(currentHost)) {
                liveCollectorHosts.remove(currentHost);
            }
            if (!deadCollectorHosts.contains(currentHost)) {
                deadCollectorHosts.add(currentHost);
            }
        }
        if (!liveCollectorHosts.isEmpty()) {
            currentCollectorHost = getRandom(liveCollectorHosts);
        }
        if ((currentCollectorHost == null) && (!deadCollectorHosts.isEmpty())) {
            currentCollectorHost = getRandom(deadCollectorHosts);
        }
        org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState.LOG.info("After refresh, new collector host : " + currentCollectorHost);
    }

    public java.lang.String getCurrentCollectorHost() {
        return currentCollectorHost;
    }

    public void onCollectorHostDown(java.lang.String deadCollectorHost) {
        if (deadCollectorHost == null) {
            refreshCollectorHost(null);
        } else if (deadCollectorHost.equals(currentCollectorHost) && (numCollectors() > 1)) {
            if (collectorDownRefreshCounter.testRefreshCounter()) {
                refreshCollectorHost(deadCollectorHost);
            }
        }
    }

    private void testAndAddDeadCollectorsToLiveList() {
        java.util.Set<java.lang.String> liveHosts = new java.util.HashSet<>();
        for (java.lang.String deadHost : deadCollectorHosts) {
            if (isValidAliveCollectorHost(clusterName, deadHost)) {
                liveHosts.add(deadHost);
            }
        }
        for (java.lang.String liveHost : liveHosts) {
            org.apache.ambari.server.controller.metrics.MetricsCollectorHAClusterState.LOG.info(("Removing collector " + liveHost) + " from dead list to live list");
            deadCollectorHosts.remove(liveHost);
            liveCollectorHosts.add(liveHost);
        }
    }

    private boolean isValidAliveCollectorHost(java.lang.String clusterName, java.lang.String collectorHost) {
        return ((collectorHost != null) && org.apache.ambari.server.controller.internal.HostStatusHelper.isHostLive(managementController, clusterName, collectorHost)) && org.apache.ambari.server.controller.internal.HostStatusHelper.isHostComponentLive(managementController, clusterName, collectorHost, "AMBARI_METRICS", org.apache.ambari.server.Role.METRICS_COLLECTOR.name());
    }

    public boolean isCollectorHostLive() {
        for (java.lang.String host : liveCollectorHosts) {
            if (org.apache.ambari.server.controller.internal.HostStatusHelper.isHostLive(managementController, clusterName, host)) {
                return true;
            }
        }
        testAndAddDeadCollectorsToLiveList();
        for (java.lang.String host : liveCollectorHosts) {
            if (org.apache.ambari.server.controller.internal.HostStatusHelper.isHostLive(managementController, clusterName, host)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollectorComponentAlive() {
        for (java.lang.String host : liveCollectorHosts) {
            if (org.apache.ambari.server.controller.internal.HostStatusHelper.isHostComponentLive(managementController, clusterName, host, "AMBARI_METRICS", org.apache.ambari.server.Role.METRICS_COLLECTOR.name())) {
                return true;
            }
        }
        for (java.lang.String host : deadCollectorHosts) {
            if (org.apache.ambari.server.controller.internal.HostStatusHelper.isHostComponentLive(managementController, clusterName, host, "AMBARI_METRICS", org.apache.ambari.server.Role.METRICS_COLLECTOR.name())) {
                return true;
            }
        }
        return false;
    }

    private int numCollectors() {
        return this.liveCollectorHosts.size() + deadCollectorHosts.size();
    }

    private java.lang.String getRandom(java.util.Set<java.lang.String> collectorSet) {
        int randIndex = new java.util.Random().nextInt(collectorSet.size());
        int i = 0;
        for (java.lang.String host : collectorSet) {
            if (i == randIndex) {
                return host;
            }
            i = i + 1;
        }
        return null;
    }
}