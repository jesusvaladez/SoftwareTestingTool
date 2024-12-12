package org.apache.ambari.server.controller.metrics.timeline;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
import org.apache.http.client.utils.URIBuilder;
import static org.apache.ambari.server.Role.HBASE_MASTER;
import static org.apache.ambari.server.Role.HBASE_REGIONSERVER;
import static org.apache.ambari.server.Role.METRICS_COLLECTOR;
import static org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS;
public abstract class AMSPropertyProvider extends org.apache.ambari.server.controller.metrics.MetricsPropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.class);

    private static final java.lang.String METRIC_REGEXP_PATTERN = "\\([^)]*\\)";

    private static final int COLLECTOR_DEFAULT_PORT = 6188;

    private final org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache metricCache;

    private static final java.lang.Integer HOST_NAMES_BATCH_REQUEST_SIZE = 100;

    private static java.util.concurrent.atomic.AtomicInteger printSkipPopulateMsgHostCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    private static java.util.concurrent.atomic.AtomicInteger printSkipPopulateMsgHostCompCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    private static final java.util.Map<java.lang.String, java.lang.String> timelineAppIdCache = new java.util.concurrent.ConcurrentHashMap<>(10);

    private static final java.util.Map<java.lang.String, java.lang.String> JVM_PROCESS_NAMES = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("HBASE_MASTER", "Master.").put("HBASE_REGIONSERVER", "RegionServer.").build();

    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    public AMSPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        super(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
        this.metricCache = cacheProvider.getTimelineMetricsCache();
        if (org.apache.ambari.server.controller.AmbariServer.getController() != null) {
            this.ambariEventPublisher = org.apache.ambari.server.controller.AmbariServer.getController().getAmbariEventPublisher();
        }
    }

    protected java.lang.String getOverridenComponentName(org.apache.ambari.server.controller.spi.Resource resource) {
        java.lang.String componentName = getComponentName(resource);
        if (componentName.equals("HOST")) {
            return "*";
        }
        return componentName;
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

    class MetricsRequest {
        private final org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo;

        private final java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.controller.spi.Resource>> resources = new java.util.HashMap<>();

        private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> metrics = new java.util.HashMap<>();

        private final org.apache.http.client.utils.URIBuilder uriBuilder;

        java.util.Set<java.lang.String> resolvedMetricsParams;

        org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper requestHelper = new org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper(streamProvider);

        private final java.util.Set<java.lang.String> hostComponentHostMetrics = new java.util.HashSet<>();

        private java.lang.String clusterName;

        private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentMetricMap = new java.util.HashMap<>();

        private MetricsRequest(org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo, org.apache.http.client.utils.URIBuilder uriBuilder, java.lang.String clusterName) {
            this.temporalInfo = temporalInfo;
            this.uriBuilder = uriBuilder;
            this.clusterName = clusterName;
        }

        public java.lang.String getClusterName() {
            return clusterName;
        }

        public void putResource(java.lang.String componentName, org.apache.ambari.server.controller.spi.Resource resource) {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = resources.get(componentName);
            if (resourceSet == null) {
                resourceSet = new java.util.HashSet<>();
                resources.put(componentName, resourceSet);
            }
            resourceSet.add(resource);
        }

        public void putPropertyId(java.lang.String metric, java.lang.String id) {
            java.util.Set<java.lang.String> propertyIds = metrics.get(metric);
            if (propertyIds == null) {
                propertyIds = new java.util.HashSet<>();
                metrics.put(metric, propertyIds);
            }
            propertyIds.add(id);
        }

        public void putHosComponentHostMetric(java.lang.String metric) {
            if (metric != null) {
                hostComponentHostMetrics.add(metric);
            }
        }

        private org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics getTimelineMetricsFromCache(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey metricCacheKey, java.lang.String componentName) throws java.io.IOException {
            if ((((metricCache != null) && (!org.apache.commons.lang.StringUtils.isEmpty(componentName))) && (!componentName.equalsIgnoreCase("HOST"))) && (metricCacheKey.getTemporalInfo() != null)) {
                return metricCache.getAppTimelineMetricsFromCache(metricCacheKey);
            }
            java.lang.Long startTime = (metricCacheKey.getTemporalInfo() != null) ? metricCacheKey.getTemporalInfo().getStartTimeMillis() : null;
            java.lang.Long endTime = (metricCacheKey.getTemporalInfo() != null) ? metricCacheKey.getTemporalInfo().getEndTimeMillis() : null;
            return requestHelper.fetchTimelineMetrics(uriBuilder, startTime, endTime);
        }

        @java.lang.SuppressWarnings("unchecked")
        public java.util.Collection<org.apache.ambari.server.controller.spi.Resource> populateResources() throws org.apache.ambari.server.controller.spi.SystemException, java.io.IOException {
            if ((temporalInfo != null) && ((temporalInfo.getStartTime() == null) || (temporalInfo.getEndTime() == null))) {
                return java.util.Collections.emptySet();
            }
            for (java.util.Map.Entry<java.lang.String, java.util.Set<org.apache.ambari.server.controller.spi.Resource>> resourceEntry : resources.entrySet()) {
                java.lang.String componentName = resourceEntry.getKey();
                java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = resourceEntry.getValue();
                org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
                java.util.Set<java.lang.String> nonHostComponentMetrics = componentMetricMap.get(componentName);
                if (nonHostComponentMetrics == null) {
                    nonHostComponentMetrics = new java.util.HashSet<>();
                }
                nonHostComponentMetrics.removeAll(hostComponentHostMetrics);
                java.util.Set<java.lang.String> hostNamesBatches = splitHostNamesInBatches(getHostnames(resources.get(componentName)), org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.HOST_NAMES_BATCH_REQUEST_SIZE);
                java.util.Map<java.lang.String, java.util.Set<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric>> metricsMap = new java.util.HashMap<>();
                for (java.lang.String hostNamesBatch : hostNamesBatches) {
                    try {
                        if (!hostComponentHostMetrics.isEmpty()) {
                            java.lang.String hostComponentHostMetricParams = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.getSetString(processRegexps(hostComponentHostMetrics), -1);
                            setQueryParams(hostComponentHostMetricParams, hostNamesBatch, true, componentName);
                            org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics metricsResponse = getTimelineMetricsFromCache(getTimelineAppMetricCacheKey(hostComponentHostMetrics, componentName, hostNamesBatch, uriBuilder.toString()), componentName);
                            if (metricsResponse != null) {
                                timelineMetrics.getMetrics().addAll(metricsResponse.getMetrics());
                            }
                        }
                        if (!nonHostComponentMetrics.isEmpty()) {
                            java.lang.String nonHostComponentHostMetricParams = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.getSetString(processRegexps(nonHostComponentMetrics), -1);
                            setQueryParams(nonHostComponentHostMetricParams, hostNamesBatch, false, componentName);
                            org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics metricsResponse = getTimelineMetricsFromCache(getTimelineAppMetricCacheKey(nonHostComponentMetrics, componentName, hostNamesBatch, uriBuilder.toString()), componentName);
                            if (metricsResponse != null) {
                                timelineMetrics.getMetrics().addAll(metricsResponse.getMetrics());
                            }
                        }
                    } catch (java.io.IOException io) {
                        if ((io instanceof java.net.SocketTimeoutException) || (io instanceof java.net.ConnectException)) {
                            if (ambariEventPublisher != null) {
                                ambariEventPublisher.publish(new org.apache.ambari.server.events.MetricsCollectorHostDownEvent(clusterName, uriBuilder.getHost()));
                            }
                            throw io;
                        }
                    }
                    java.util.Set<java.lang.String> patterns = createPatterns(metrics.keySet());
                    if (!timelineMetrics.getMetrics().isEmpty()) {
                        for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric : timelineMetrics.getMetrics()) {
                            if (((metric.getMetricName() != null) && (metric.getMetricValues() != null)) && checkMetricName(patterns, metric.getMetricName())) {
                                java.lang.String hostnameTmp = metric.getHostName();
                                if (!metricsMap.containsKey(hostnameTmp)) {
                                    metricsMap.put(hostnameTmp, new java.util.HashSet<>());
                                }
                                metricsMap.get(hostnameTmp).add(metric);
                            }
                        }
                        for (org.apache.ambari.server.controller.spi.Resource resource : resourceSet) {
                            java.lang.String hostnameTmp = getHostName(resource);
                            if (metricsMap.containsKey(hostnameTmp)) {
                                for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric : metricsMap.get(hostnameTmp)) {
                                    org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetricClone = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric(metric);
                                    metricsPaddingMethod.applyPaddingStrategy(timelineMetricClone, temporalInfo);
                                    populateResource(resource, timelineMetricClone, temporalInfo);
                                }
                            }
                        }
                    }
                }
            }
            return java.util.Collections.emptySet();
        }

        private java.lang.String getTimelineAppId(java.lang.String componentName) {
            if (org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.timelineAppIdCache.containsKey(componentName)) {
                return org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.timelineAppIdCache.get(componentName);
            } else {
                org.apache.ambari.server.state.StackId stackId;
                try {
                    org.apache.ambari.server.controller.AmbariManagementController managementController = org.apache.ambari.server.controller.AmbariServer.getController();
                    org.apache.ambari.server.state.Cluster cluster = managementController.getClusters().getCluster(clusterName);
                    org.apache.ambari.server.state.Service service = cluster.getServiceByComponentName(componentName);
                    stackId = service.getDesiredStackId();
                    if (stackId != null) {
                        java.lang.String stackName = stackId.getStackName();
                        java.lang.String version = stackId.getStackVersion();
                        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = managementController.getAmbariMetaInfo();
                        java.lang.String serviceName = service.getName();
                        java.lang.String timeLineAppId = ambariMetaInfo.getComponent(stackName, version, serviceName, componentName).getTimelineAppid();
                        if (timeLineAppId != null) {
                            org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.timelineAppIdCache.put(componentName, timeLineAppId);
                            return timeLineAppId;
                        }
                    }
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                }
            }
            return componentName;
        }

        private void setQueryParams(java.lang.String metricsParam, java.lang.String hostname, boolean isHostMetric, java.lang.String componentName) {
            uriBuilder.removeQuery();
            if (metricsParam.length() > 0) {
                uriBuilder.setParameter("metricNames", metricsParam);
                resolvedMetricsParams = com.google.common.collect.Sets.newHashSet(metricsParam.split(","));
            }
            if ((hostname != null) && (!hostname.isEmpty())) {
                uriBuilder.setParameter("hostname", hostname);
            }
            if (isHostMetric) {
                uriBuilder.setParameter("appId", "HOST");
            } else {
                if (((componentName != null) && (!componentName.isEmpty())) && (!componentName.equalsIgnoreCase("HOST"))) {
                    componentName = getTimelineAppId(componentName);
                }
                uriBuilder.setParameter("appId", componentName);
            }
            if ((clusterName != null) && hostProvider.isCollectorHostExternal(clusterName)) {
                uriBuilder.setParameter("instanceId", clusterName);
            }
            if (temporalInfo != null) {
                long startTime = temporalInfo.getStartTime();
                if (startTime != (-1)) {
                    uriBuilder.setParameter("startTime", java.lang.String.valueOf(startTime));
                }
                long endTime = temporalInfo.getEndTime();
                if (endTime != (-1)) {
                    uriBuilder.setParameter("endTime", java.lang.String.valueOf(endTime));
                }
            }
        }

        private java.util.Set<java.lang.String> createPatterns(java.util.Set<java.lang.String> rawNames) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.METRIC_REGEXP_PATTERN);
            java.util.Set<java.lang.String> result = new java.util.HashSet<>();
            for (java.lang.String rawName : rawNames) {
                java.util.regex.Matcher matcher = pattern.matcher(rawName);
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                int lastPos = 0;
                while (matcher.find()) {
                    sb.append(java.util.regex.Pattern.quote(rawName.substring(lastPos, matcher.start())));
                    sb.append(matcher.group());
                    lastPos = matcher.end();
                } 
                sb.append(java.util.regex.Pattern.quote(rawName.substring(lastPos)));
                result.add(sb.toString());
            }
            return result;
        }

        private boolean checkMetricName(java.util.Set<java.lang.String> patterns, java.lang.String name) {
            for (java.lang.String pattern : patterns) {
                if (java.util.regex.Pattern.matches(pattern, name)) {
                    return true;
                }
            }
            return false;
        }

        private java.util.Set<java.lang.String> processRegexps(java.util.Set<java.lang.String> metricNames) {
            java.util.Set<java.lang.String> result = new java.util.HashSet<>();
            for (java.lang.String name : metricNames) {
                result.add(name.replaceAll(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.METRIC_REGEXP_PATTERN, java.util.regex.Matcher.quoteReplacement("%")));
            }
            return result;
        }

        private void populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
            java.lang.String metric_name = metric.getMetricName();
            java.util.Set<java.lang.String> propertyIdSet = metrics.get(metric_name);
            java.util.List<java.lang.String> parameterList = new java.util.LinkedList<>();
            if (propertyIdSet == null) {
                for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : metrics.entrySet()) {
                    java.lang.String key = entry.getKey();
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(key);
                    java.util.regex.Matcher matcher = pattern.matcher(metric_name);
                    if (matcher.matches()) {
                        propertyIdSet = entry.getValue();
                        for (int i = 0; i < matcher.groupCount(); ++i) {
                            parameterList.add(matcher.group(i + 1));
                        }
                        break;
                    }
                }
            }
            if (propertyIdSet != null) {
                java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metricsMap = getComponentMetrics().get(getOverridenComponentName(resource));
                if (metricsMap != null) {
                    for (java.lang.String propertyId : propertyIdSet) {
                        if (propertyId != null) {
                            if (metricsMap.containsKey(propertyId)) {
                                if (containsArguments(propertyId)) {
                                    int i = 1;
                                    if (!parameterList.isEmpty()) {
                                        for (java.lang.String param : parameterList) {
                                            propertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument(propertyId, "$" + i, param);
                                            ++i;
                                        }
                                    } else {
                                        propertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument(propertyId, "$1", metric.getInstanceId());
                                    }
                                } else if (metric.getInstanceId() != null) {
                                    java.lang.String instanceId = metric.getInstanceId();
                                    instanceId = (instanceId.matches("^\\w+\\..+$")) ? instanceId.split("\\.")[1] : "";
                                    if (!propertyId.contains(instanceId))
                                        continue;

                                }
                                java.lang.Object value = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.getValue(metric, temporalInfo);
                                if ((value != null) && (!containsArguments(propertyId))) {
                                    resource.setProperty(propertyId, value);
                                }
                            }
                        }
                    }
                }
            }
        }

        private org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey getTimelineAppMetricCacheKey(java.util.Set<java.lang.String> metrics, java.lang.String hostnames, java.lang.String componentName, java.lang.String spec) {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey metricCacheKey = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(metrics, componentName, hostnames, temporalInfo);
            metricCacheKey.setSpec(spec);
            return metricCacheKey;
        }

        public void linkResourceToMetric(java.lang.String componentName, java.lang.String metric) {
            if (componentMetricMap.get(componentName) == null) {
                componentMetricMap.put(componentName, new java.util.HashSet<>(java.util.Arrays.asList(metric)));
            } else {
                componentMetricMap.get(componentName).add(metric);
            }
        }
    }

    private java.util.List<java.lang.String> getHostnames(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources) {
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String hostname = getHostName(resource);
            if (hostname != null) {
                hostNames.add(hostname);
            }
        }
        return hostNames;
    }

    private java.util.Set<java.lang.String> splitHostNamesInBatches(java.util.List<java.lang.String> hostNames, int batch_size) {
        java.util.Set<java.lang.String> hostNamesBatches = new java.util.HashSet<>();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < hostNames.size(); i++) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(hostNames.get(i));
            if (((i + 1) % batch_size) == 0) {
                hostNamesBatches.add(sb.toString());
                sb = new java.lang.StringBuilder();
            }
        }
        if ((hostNamesBatches.size() == 0) || (!"".equals(sb.toString()))) {
            hostNamesBatches.add(sb.toString());
        }
        return hostNamesBatches;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResourcesWithProperties(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> propertyIds) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest>> requestMap = getMetricsRequests(resources, request, propertyIds);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest>> clusterEntry : requestMap.entrySet()) {
            for (org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest metricsRequest : clusterEntry.getValue().values()) {
                try {
                    metricsRequest.populateResources();
                } catch (java.io.IOException io) {
                    if (io instanceof java.net.SocketTimeoutException) {
                        if (org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.LOG.debug("Skip populating resources on socket timeout.");
                        }
                        break;
                    }
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> getComponentMetrics() {
        if (super.getComponentMetrics().containsKey(org.apache.ambari.server.Role.METRICS_COLLECTOR.name())) {
            return super.getComponentMetrics();
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> metricPropertyIds;
        if (this.hostNamePropertyId != null) {
            metricPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        } else {
            metricPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> amsMetrics = new java.util.HashMap<>();
        if (metricPropertyIds.containsKey(org.apache.ambari.server.Role.HBASE_MASTER.name())) {
            amsMetrics.putAll(metricPropertyIds.get(org.apache.ambari.server.Role.HBASE_MASTER.name()));
        }
        if (metricPropertyIds.containsKey(org.apache.ambari.server.Role.HBASE_REGIONSERVER.name())) {
            amsMetrics.putAll(metricPropertyIds.get(org.apache.ambari.server.Role.HBASE_REGIONSERVER.name()));
        }
        if (!amsMetrics.isEmpty()) {
            super.getComponentMetrics().putAll(java.util.Collections.singletonMap(org.apache.ambari.server.Role.METRICS_COLLECTOR.name(), amsMetrics));
        }
        return super.getComponentMetrics();
    }

    private java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest>> getMetricsRequests(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> ids) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest>> requestMap = new java.util.HashMap<>();
        java.lang.String collectorPort = null;
        java.util.Map<java.lang.String, java.lang.Boolean> clusterCollectorComponentLiveMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Boolean> clusterCollectorHostLiveMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
            if (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
                continue;
            }
            boolean clusterCollectorHostLive;
            if (clusterCollectorHostLiveMap.containsKey(clusterName)) {
                clusterCollectorHostLive = clusterCollectorHostLiveMap.get(clusterName);
            } else {
                clusterCollectorHostLive = hostProvider.isCollectorComponentLive(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS);
                clusterCollectorHostLiveMap.put(clusterName, clusterCollectorHostLive);
            }
            if (!clusterCollectorHostLive) {
                if (org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.printSkipPopulateMsgHostCounter.getAndIncrement() == 0) {
                    org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.LOG.info("METRICS_COLLECTOR host is not live. Skip populating " + ("resources with metrics, next message will be logged after 1000 " + "attempts."));
                } else {
                    org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.printSkipPopulateMsgHostCounter.compareAndSet(1000, 0);
                }
                continue;
            }
            org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.printSkipPopulateMsgHostCounter.set(0);
            boolean clusterCollectorComponentLive;
            if (clusterCollectorComponentLiveMap.containsKey(clusterName)) {
                clusterCollectorComponentLive = clusterCollectorComponentLiveMap.get(clusterName);
            } else {
                clusterCollectorComponentLive = hostProvider.isCollectorComponentLive(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS);
                clusterCollectorComponentLiveMap.put(clusterName, clusterCollectorComponentLive);
            }
            if (!clusterCollectorComponentLive) {
                if (org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.printSkipPopulateMsgHostCompCounter.getAndIncrement() == 0) {
                    org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.LOG.info("METRICS_COLLECTOR is not live. Skip populating resources " + ("with metrics., next message will be logged after 1000 " + "attempts."));
                } else {
                    org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.printSkipPopulateMsgHostCompCounter.compareAndSet(1000, 0);
                }
                continue;
            }
            org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.printSkipPopulateMsgHostCompCounter.set(0);
            java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest> requests = requestMap.get(clusterName);
            if (requests == null) {
                requests = new java.util.HashMap<>();
                requestMap.put(clusterName, requests);
            }
            java.lang.String collectorHost = hostProvider.getCollectorHostName(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS);
            if (collectorPort == null) {
                collectorPort = hostProvider.getCollectorPort(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS);
            }
            for (java.lang.String id : ids) {
                java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = new java.util.HashMap<>();
                java.lang.String componentName = getOverridenComponentName(resource);
                java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> componentMetricMap = getComponentMetrics().get(componentName);
                if ((componentMetricMap != null) && (!componentMetricMap.containsKey(id))) {
                    updateComponentMetricMap(componentMetricMap, id);
                }
                updatePropertyInfoMap(componentName, id, propertyInfoMap);
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : propertyInfoMap.entrySet()) {
                    java.lang.String propertyId = entry.getKey();
                    org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = entry.getValue();
                    org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = request.getTemporalInfo(id);
                    if (((temporalInfo == null) && propertyInfo.isPointInTime()) || ((temporalInfo != null) && propertyInfo.isTemporal())) {
                        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest metricsRequest = requests.get(temporalInfo);
                        if (metricsRequest == null) {
                            metricsRequest = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.MetricsRequest(temporalInfo, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder(collectorHost, collectorPort != null ? java.lang.Integer.parseInt(collectorPort) : org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.COLLECTOR_DEFAULT_PORT, configuration.isHttpsEnabled()), ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId))));
                            requests.put(temporalInfo, metricsRequest);
                        }
                        metricsRequest.putResource(getComponentName(resource), resource);
                        metricsRequest.putPropertyId(preprocessPropertyId(propertyInfo.getPropertyId(), getComponentName(resource)), propertyId);
                        metricsRequest.linkResourceToMetric(getComponentName(resource), preprocessPropertyId(propertyInfo.getPropertyId(), getComponentName(resource)));
                        if (propertyInfo.isAmsHostMetric()) {
                            metricsRequest.putHosComponentHostMetric(propertyInfo.getPropertyId());
                        }
                    }
                }
            }
        }
        return requestMap;
    }

    private java.lang.String preprocessPropertyId(java.lang.String propertyId, java.lang.String componentName) {
        if (propertyId.startsWith("jvm") && org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.JVM_PROCESS_NAMES.keySet().contains(componentName)) {
            java.lang.String newPropertyId = propertyId.replace("jvm.", "jvm." + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.JVM_PROCESS_NAMES.get(componentName));
            org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.LOG.debug("Pre-process: {}, to: {}", propertyId, newPropertyId);
            return newPropertyId;
        }
        return propertyId;
    }

    static org.apache.http.client.utils.URIBuilder getAMSUriBuilder(java.lang.String hostname, int port, boolean httpsEnabled) {
        org.apache.http.client.utils.URIBuilder uriBuilder = new org.apache.http.client.utils.URIBuilder();
        uriBuilder.setScheme(httpsEnabled ? "https" : "http");
        uriBuilder.setHost(hostname);
        uriBuilder.setPort(port);
        uriBuilder.setPath("/ws/v1/timeline/metrics");
        return uriBuilder;
    }
}