package org.apache.ambari.server.metrics.system.impl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.metrics2.sink.timeline.AbstractTimelineMetricsSink;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
import org.apache.hadoop.metrics2.sink.timeline.cache.TimelineMetricsCache;
import org.springframework.security.core.context.SecurityContextHolder;
public class AmbariMetricSinkImpl extends org.apache.hadoop.metrics2.sink.timeline.AbstractTimelineMetricsSink implements org.apache.ambari.server.metrics.system.MetricsSink {
    private static final java.lang.String AMBARI_SERVER_APP_ID = "ambari_server";

    private java.util.Collection<java.lang.String> collectorHosts;

    private java.lang.String collectorUri;

    private java.lang.String port;

    private java.lang.String protocol;

    private java.lang.String hostName;

    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    private org.apache.hadoop.metrics2.sink.timeline.cache.TimelineMetricsCache timelineMetricsCache;

    private boolean isInitialized = false;

    private boolean setInstanceId = false;

    private java.lang.String instanceId;

    public AmbariMetricSinkImpl(org.apache.ambari.server.controller.AmbariManagementController amc) {
        this.ambariManagementController = amc;
    }

    @java.lang.Override
    public void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration) {
        if (ambariManagementController == null) {
            return;
        }
        org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken authenticationToken = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken("admin");
        authenticationToken.setAuthenticated(true);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if ((clusters == null) || clusters.getClusters().isEmpty()) {
            org.apache.ambari.server.metrics.system.impl.LOG.info("No clusters configured.");
            return;
        }
        java.lang.String ambariMetricsServiceName = "AMBARI_METRICS";
        collectorHosts = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Cluster> kv : clusters.getClusters().entrySet()) {
            java.lang.String clusterName = kv.getKey();
            instanceId = clusterName;
            org.apache.ambari.server.state.Cluster c = kv.getValue();
            org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion;
            boolean externalHostConfigPresent = false;
            boolean externalPortConfigPresent = false;
            org.apache.ambari.server.state.Config clusterEnv = c.getDesiredConfigByType(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV);
            if (clusterEnv != null) {
                java.util.Map<java.lang.String, java.lang.String> configs = clusterEnv.getProperties();
                java.lang.String metricsCollectorExternalHosts = configs.get("metrics_collector_external_hosts");
                if (org.apache.commons.lang.StringUtils.isNotEmpty(metricsCollectorExternalHosts)) {
                    org.apache.ambari.server.metrics.system.impl.LOG.info("Setting Metrics Collector External Host : " + metricsCollectorExternalHosts);
                    collectorHosts.addAll(java.util.Arrays.asList(metricsCollectorExternalHosts.split(",")));
                    externalHostConfigPresent = true;
                    setInstanceId = true;
                }
                java.lang.String metricsCollectorExternalPort = configs.get("metrics_collector_external_port");
                if (org.apache.commons.lang.StringUtils.isNotEmpty(metricsCollectorExternalPort)) {
                    org.apache.ambari.server.metrics.system.impl.LOG.info("Setting Metrics Collector External Port : " + metricsCollectorExternalPort);
                    port = metricsCollectorExternalPort;
                    externalPortConfigPresent = true;
                }
            }
            java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
            propertyIds.add(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CONFIGURATIONS_PROPERTY_ID);
            org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CLUSTER_NAME_PROPERTY_ID).equals(clusterName).and().property(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_NAME_PROPERTY_ID).equals(ambariMetricsServiceName).and().property(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.IS_CURRENT_PROPERTY_ID).equals("true").toPredicate();
            org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
            org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, ambariManagementController);
            try {
                if (!externalHostConfigPresent) {
                    org.apache.ambari.server.state.Service service = c.getService(ambariMetricsServiceName);
                    if (service != null) {
                        for (java.lang.String component : service.getServiceComponents().keySet()) {
                            org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponents().get(component);
                            for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : sc.getServiceComponentHosts().values()) {
                                if (serviceComponentHost.getServiceComponentName().equals("METRICS_COLLECTOR")) {
                                    collectorHosts.add(serviceComponentHost.getHostName());
                                }
                            }
                        }
                    }
                }
                java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
                for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                    if (resource != null) {
                        java.util.ArrayList<java.util.LinkedHashMap<java.lang.Object, java.lang.Object>> configs = ((java.util.ArrayList<java.util.LinkedHashMap<java.lang.Object, java.lang.Object>>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CONFIGURATIONS_PROPERTY_ID)));
                        for (java.util.LinkedHashMap<java.lang.Object, java.lang.Object> config : configs) {
                            if ((config != null) && config.get("type").equals("ams-site")) {
                                java.util.TreeMap<java.lang.Object, java.lang.Object> properties = ((java.util.TreeMap<java.lang.Object, java.lang.Object>) (config.get("properties")));
                                java.lang.String timelineWebappAddress = ((java.lang.String) (properties.get("timeline.metrics.service.webapp.address")));
                                if (((!externalPortConfigPresent) && org.apache.commons.lang.StringUtils.isNotEmpty(timelineWebappAddress)) && timelineWebappAddress.contains(":")) {
                                    port = timelineWebappAddress.split(":")[1];
                                }
                                java.lang.String httpPolicy = ((java.lang.String) (properties.get("timeline.metrics.service.http.policy")));
                                protocol = (httpPolicy.equals("HTTP_ONLY")) ? "http" : "https";
                                break;
                            }
                        }
                    }
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.metrics.system.impl.LOG.info("Exception caught when retrieving Collector URI", e);
            }
        }
        hostName = configuration.getProperty("ambariserver.hostname.override", getDefaultLocalHostName());
        org.apache.ambari.server.metrics.system.impl.LOG.info("Hostname used for ambari server metrics : " + hostName);
        if (protocol.contains("https")) {
            org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
            java.lang.String trustStorePath = sslConfiguration.getTruststorePath();
            java.lang.String trustStoreType = sslConfiguration.getTruststoreType();
            java.lang.String trustStorePwd = sslConfiguration.getTruststorePassword();
            loadTruststore(trustStorePath, trustStoreType, trustStorePwd);
        }
        collectorUri = getCollectorUri(findPreferredCollectHost());
        int maxRowCacheSize = java.lang.Integer.parseInt(configuration.getProperty(org.apache.ambari.server.metrics.system.impl.MAX_METRIC_ROW_CACHE_SIZE, java.lang.String.valueOf(TimelineMetricsCache.MAX_RECS_PER_NAME_DEFAULT)));
        int metricsSendInterval = java.lang.Integer.parseInt(configuration.getProperty(org.apache.ambari.server.metrics.system.impl.METRICS_SEND_INTERVAL, java.lang.String.valueOf(TimelineMetricsCache.MAX_EVICTION_TIME_MILLIS)));
        timelineMetricsCache = new org.apache.hadoop.metrics2.sink.timeline.cache.TimelineMetricsCache(maxRowCacheSize, metricsSendInterval);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(collectorHosts)) {
            org.apache.ambari.server.metrics.system.impl.LOG.info("Metric Sink initialized with collectorHosts : " + collectorHosts.toString());
            isInitialized = true;
        }
    }

    private java.lang.String getDefaultLocalHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getCanonicalHostName();
        } catch (java.net.UnknownHostException e) {
            org.apache.ambari.server.metrics.system.impl.LOG.info("Error getting host address");
        }
        return null;
    }

    @java.lang.Override
    public void publish(java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> metrics) {
        if (isInitialized) {
            java.util.List<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> metricList = getFilteredMetricList(metrics);
            if (!metricList.isEmpty()) {
                org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
                timelineMetrics.setMetrics(metricList);
                emitMetrics(timelineMetrics);
            }
        } else {
            org.apache.ambari.server.metrics.system.impl.LOG.debug("Metric Sink not yet initialized. Discarding metrics.");
        }
    }

    @java.lang.Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @java.lang.Override
    protected java.lang.String getCollectorUri(java.lang.String host) {
        return constructTimelineMetricUri(protocol, host, port);
    }

    @java.lang.Override
    protected java.lang.String getCollectorProtocol() {
        return protocol;
    }

    @java.lang.Override
    protected java.lang.String getCollectorPort() {
        return port;
    }

    @java.lang.Override
    protected int getTimeoutSeconds() {
        return 10;
    }

    @java.lang.Override
    protected java.lang.String getZookeeperQuorum() {
        return null;
    }

    @java.lang.Override
    protected java.util.Collection<java.lang.String> getConfiguredCollectorHosts() {
        return collectorHosts;
    }

    @java.lang.Override
    protected java.lang.String getHostname() {
        return hostName;
    }

    @java.lang.Override
    protected boolean isHostInMemoryAggregationEnabled() {
        return false;
    }

    @java.lang.Override
    protected int getHostInMemoryAggregationPort() {
        return 0;
    }

    @java.lang.Override
    protected java.lang.String getHostInMemoryAggregationProtocol() {
        return "http";
    }

    private java.util.List<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> getFilteredMetricList(java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> metrics) {
        final java.util.List<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> metricList = new java.util.ArrayList<>();
        for (org.apache.ambari.server.metrics.system.SingleMetric metric : metrics) {
            java.lang.String metricName = metric.getMetricName();
            java.lang.Double value = metric.getValue();
            org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = createTimelineMetric(metric.getTimestamp(), org.apache.ambari.server.metrics.system.impl.AmbariMetricSinkImpl.AMBARI_SERVER_APP_ID, metricName, value);
            timelineMetricsCache.putTimelineMetric(timelineMetric, false);
            org.apache.hadoop.metrics2.sink.timeline.TimelineMetric cachedMetric = timelineMetricsCache.getTimelineMetric(metricName);
            if (cachedMetric != null) {
                metricList.add(cachedMetric);
            }
        }
        return metricList;
    }

    private org.apache.hadoop.metrics2.sink.timeline.TimelineMetric createTimelineMetric(long currentTimeMillis, java.lang.String component, java.lang.String attributeName, java.lang.Number attributeValue) {
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName(attributeName);
        timelineMetric.setHostName(hostName);
        if (setInstanceId) {
            timelineMetric.setInstanceId(instanceId);
        }
        timelineMetric.setAppId(component);
        timelineMetric.setStartTime(currentTimeMillis);
        timelineMetric.getMetricValues().put(currentTimeMillis, attributeValue.doubleValue());
        return timelineMetric;
    }
}