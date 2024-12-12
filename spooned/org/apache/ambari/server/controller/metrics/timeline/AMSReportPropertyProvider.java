package org.apache.ambari.server.controller.metrics.timeline;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
import org.apache.http.client.utils.URIBuilder;
import static org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS;
public class AMSReportPropertyProvider extends org.apache.ambari.server.controller.metrics.MetricsReportPropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.class);

    private org.apache.ambari.server.controller.metrics.MetricsPaddingMethod metricsPaddingMethod;

    private final org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache metricCache;

    org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper requestHelper;

    private static java.util.concurrent.atomic.AtomicInteger printSkipPopulateMsgHostCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    private static java.util.concurrent.atomic.AtomicInteger printSkipPopulateMsgHostCompCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    public AMSReportPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId) {
        super(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId);
        this.metricCache = cacheProvider.getTimelineMetricsCache();
        this.requestHelper = new org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper(streamProvider);
        if (org.apache.ambari.server.controller.AmbariServer.getController() != null) {
            this.ambariEventPublisher = org.apache.ambari.server.controller.AmbariServer.getController().getAmbariEventPublisher();
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> supportedIds = new java.util.HashSet<>();
        for (java.lang.String propertyId : propertyIds) {
            if (propertyId.startsWith(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM) || org.apache.ambari.server.controller.utilities.PropertyHelper.hasAggregateFunctionSuffix(propertyId)) {
                supportedIds.add(propertyId);
            }
        }
        propertyIds.removeAll(supportedIds);
        return propertyIds;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> keepers = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            populateResource(resource, request, predicate);
            keepers.add(resource);
        }
        return keepers;
    }

    private boolean populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> propertyIds = getPropertyIds();
        if (propertyIds.isEmpty()) {
            return true;
        }
        metricsPaddingMethod = org.apache.ambari.server.controller.metrics.MetricsReportPropertyProvider.DEFAULT_PADDING_METHOD;
        java.util.Set<java.lang.String> requestPropertyIds = request.getPropertyIds();
        if ((requestPropertyIds != null) && (!requestPropertyIds.isEmpty())) {
            for (java.lang.String propertyId : requestPropertyIds) {
                if (propertyId.startsWith(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM)) {
                    java.lang.String paddingStrategyStr = propertyId.substring(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM.length() + 1);
                    metricsPaddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.valueOf(paddingStrategyStr));
                }
            }
        }
        java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
        if (!hostProvider.isCollectorHostLive(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
            if (org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.printSkipPopulateMsgHostCounter.getAndIncrement() == 0) {
                org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.LOG.info("METRICS_COLLECTOR host is not live. Skip populating " + ("resources with metrics, next message will be logged after 1000 " + "attempts."));
            } else {
                org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.printSkipPopulateMsgHostCounter.compareAndSet(1000, 0);
            }
            return true;
        }
        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.printSkipPopulateMsgHostCompCounter.set(0);
        if (!hostProvider.isCollectorComponentLive(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
            if (org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.printSkipPopulateMsgHostCompCounter.getAndIncrement() == 0) {
                org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.LOG.info("METRICS_COLLECTOR is not live. Skip populating resources" + (" with metrics, next message will be logged after 1000 " + "attempts."));
            } else {
                org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.printSkipPopulateMsgHostCompCounter.compareAndSet(1000, 0);
            }
            return true;
        }
        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.printSkipPopulateMsgHostCompCounter.set(0);
        setProperties(resource, clusterName, request, getRequestPropertyIds(request, predicate));
        return true;
    }

    private void setProperties(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName, org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> ids) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.MetricReportRequest> reportRequestMap = getPropertyIdMaps(request, ids);
        java.lang.String host = hostProvider.getCollectorHostName(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS);
        java.lang.String port = hostProvider.getCollectorPort(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder(host, port != null ? java.lang.Integer.parseInt(port) : 6188, configuration.isHttpsEnabled());
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.MetricReportRequest> entry : reportRequestMap.entrySet()) {
            org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.MetricReportRequest reportRequest = entry.getValue();
            org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = reportRequest.getTemporalInfo();
            java.util.Map<java.lang.String, java.lang.String> propertyIdMap = reportRequest.getPropertyIdMap();
            uriBuilder.removeQuery();
            uriBuilder.addParameter("metricNames", org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.getSetString(propertyIdMap.keySet(), -1));
            uriBuilder.setParameter("appId", "HOST");
            if ((clusterName != null) && hostProvider.isCollectorHostExternal(clusterName)) {
                uriBuilder.setParameter("instanceId", clusterName);
            }
            long startTime = temporalInfo.getStartTime();
            if (startTime != (-1)) {
                uriBuilder.setParameter("startTime", java.lang.String.valueOf(startTime));
            }
            long endTime = temporalInfo.getEndTime();
            if (endTime != (-1)) {
                uriBuilder.setParameter("endTime", java.lang.String.valueOf(endTime));
            }
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey metricCacheKey = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(propertyIdMap.keySet(), "HOST", temporalInfo);
            metricCacheKey.setSpec(uriBuilder.toString());
            org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics;
            try {
                if ((metricCache != null) && (metricCacheKey.getTemporalInfo() != null)) {
                    timelineMetrics = metricCache.getAppTimelineMetricsFromCache(metricCacheKey);
                } else {
                    timelineMetrics = requestHelper.fetchTimelineMetrics(uriBuilder, temporalInfo.getStartTimeMillis(), temporalInfo.getEndTimeMillis());
                }
            } catch (java.io.IOException io) {
                timelineMetrics = null;
                if ((io instanceof java.net.SocketTimeoutException) || (io instanceof java.net.ConnectException)) {
                    if (org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.LOG.debug("Skip populating metrics on socket timeout exception.");
                    }
                    if (ambariEventPublisher != null) {
                        ambariEventPublisher.publish(new org.apache.ambari.server.events.MetricsCollectorHostDownEvent(clusterName, host));
                    }
                    break;
                }
            }
            if (timelineMetrics != null) {
                for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric : timelineMetrics.getMetrics()) {
                    if ((metric.getMetricName() != null) && (metric.getMetricValues() != null)) {
                        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetricClone = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric(metric);
                        metricsPaddingMethod.applyPaddingStrategy(timelineMetricClone, temporalInfo);
                        java.lang.String propertyId = propertyIdMap.get(metric.getMetricName());
                        if (propertyId != null) {
                            resource.setProperty(propertyId, org.apache.ambari.server.controller.internal.AbstractPropertyProvider.getValue(timelineMetricClone, temporalInfo));
                        }
                    }
                }
            }
        }
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.MetricReportRequest> getPropertyIdMaps(org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> ids) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.MetricReportRequest> propertyMap = new java.util.HashMap<>();
        for (java.lang.String id : ids) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = getPropertyInfoMap("*", id);
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : propertyInfoMap.entrySet()) {
                org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = entry.getValue();
                java.lang.String propertyId = entry.getKey();
                java.lang.String amsId = propertyInfo.getAmsId();
                org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = request.getTemporalInfo(id);
                if ((temporalInfo != null) && propertyInfo.isTemporal()) {
                    java.lang.String propertyName = propertyInfo.getPropertyId();
                    java.lang.String report = null;
                    int dotIndex = propertyName.lastIndexOf('.');
                    if (dotIndex != (-1)) {
                        report = propertyName.substring(0, dotIndex);
                    }
                    if (report != null) {
                        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.MetricReportRequest reportRequest = propertyMap.get(report);
                        if (reportRequest == null) {
                            reportRequest = new org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider.MetricReportRequest();
                            propertyMap.put(report, reportRequest);
                            reportRequest.setTemporalInfo(temporalInfo);
                        }
                        reportRequest.addPropertyId(amsId, propertyId);
                    }
                }
            }
        }
        return propertyMap;
    }

    class MetricReportRequest {
        private org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo;

        private java.util.Map<java.lang.String, java.lang.String> propertyIdMap = new java.util.HashMap<>();

        public org.apache.ambari.server.controller.spi.TemporalInfo getTemporalInfo() {
            return temporalInfo;
        }

        public void setTemporalInfo(org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
            this.temporalInfo = temporalInfo;
        }

        public java.util.Map<java.lang.String, java.lang.String> getPropertyIdMap() {
            return propertyIdMap;
        }

        public void addPropertyId(java.lang.String propertyName, java.lang.String propertyId) {
            propertyIdMap.put(propertyName, propertyId);
        }
    }
}