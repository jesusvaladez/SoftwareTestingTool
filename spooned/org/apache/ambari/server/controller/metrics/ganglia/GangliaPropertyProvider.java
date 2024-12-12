package org.apache.ambari.server.controller.metrics.ganglia;
import org.apache.http.client.utils.URIBuilder;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
public abstract class GangliaPropertyProvider extends org.apache.ambari.server.controller.metrics.MetricsPropertyProvider {
    static final java.util.Map<java.lang.String, java.util.List<java.lang.String>> GANGLIA_CLUSTER_NAME_MAP = new java.util.HashMap<>();

    static {
        GANGLIA_CLUSTER_NAME_MAP.put("NAMENODE", java.util.Collections.singletonList("HDPNameNode"));
        GANGLIA_CLUSTER_NAME_MAP.put("DATANODE", java.util.Arrays.asList("HDPDataNode", "HDPSlaves"));
        GANGLIA_CLUSTER_NAME_MAP.put("JOBTRACKER", java.util.Collections.singletonList("HDPJobTracker"));
        GANGLIA_CLUSTER_NAME_MAP.put("TASKTRACKER", java.util.Arrays.asList("HDPTaskTracker", "HDPSlaves"));
        GANGLIA_CLUSTER_NAME_MAP.put("RESOURCEMANAGER", java.util.Collections.singletonList("HDPResourceManager"));
        GANGLIA_CLUSTER_NAME_MAP.put("NODEMANAGER", java.util.Arrays.asList("HDPNodeManager", "HDPSlaves"));
        GANGLIA_CLUSTER_NAME_MAP.put("HISTORYSERVER", java.util.Collections.singletonList("HDPHistoryServer"));
        GANGLIA_CLUSTER_NAME_MAP.put("HBASE_MASTER", java.util.Collections.singletonList("HDPHBaseMaster"));
        GANGLIA_CLUSTER_NAME_MAP.put("HBASE_REGIONSERVER", java.util.Arrays.asList("HDPHBaseRegionServer", "HDPSlaves"));
        GANGLIA_CLUSTER_NAME_MAP.put("FLUME_HANDLER", java.util.Arrays.asList("HDPFlumeServer", "HDPSlaves"));
        GANGLIA_CLUSTER_NAME_MAP.put("JOURNALNODE", java.util.Arrays.asList("HDPJournalNode", "HDPSlaves"));
        GANGLIA_CLUSTER_NAME_MAP.put("NIMBUS", java.util.Collections.singletonList("HDPNimbus"));
        GANGLIA_CLUSTER_NAME_MAP.put("SUPERVISOR", java.util.Collections.singletonList("HDPSupervisor"));
    }

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.class);

    public GangliaPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        super(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResourcesWithProperties(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> propertyIds) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest>> requestMap = getRRDRequests(resources, request, propertyIds);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest>> clusterEntry : requestMap.entrySet()) {
            for (org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest rrdRequest : clusterEntry.getValue().values()) {
                rrdRequest.populateResources();
            }
        }
        return resources;
    }

    protected abstract java.util.Set<java.lang.String> getGangliaClusterNames(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName);

    protected java.lang.String getComponentNamePropertyId() {
        return componentNamePropertyId;
    }

    protected java.lang.String getHostNamePropertyId() {
        return hostNamePropertyId;
    }

    public org.apache.ambari.server.controller.utilities.StreamProvider getStreamProvider() {
        return streamProvider;
    }

    private java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest>> getRRDRequests(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> ids) {
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest>> requestMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
            java.util.Map<org.apache.ambari.server.controller.spi.TemporalInfo, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest> requests = requestMap.get(clusterName);
            if (requests == null) {
                requests = new java.util.HashMap<>();
                requestMap.put(clusterName, requests);
            }
            java.util.Set<java.lang.String> gangliaClusterNames = getGangliaClusterNames(resource, clusterName);
            for (java.lang.String gangliaClusterName : gangliaClusterNames) {
                org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey key = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey(getHostName(resource), gangliaClusterName);
                for (java.lang.String id : ids) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = new java.util.HashMap<>();
                    java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> componentMetricMap = getComponentMetrics().get(getComponentName(resource));
                    if ((componentMetricMap != null) && (!componentMetricMap.containsKey(id))) {
                        updateComponentMetricMap(componentMetricMap, id);
                    }
                    updatePropertyInfoMap(getComponentName(resource), id, propertyInfoMap);
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : propertyInfoMap.entrySet()) {
                        java.lang.String propertyId = entry.getKey();
                        org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = entry.getValue();
                        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = request.getTemporalInfo(id);
                        if (((temporalInfo == null) && propertyInfo.isPointInTime()) || ((temporalInfo != null) && propertyInfo.isTemporal())) {
                            org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest rrdRequest = requests.get(temporalInfo);
                            if (rrdRequest == null) {
                                rrdRequest = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest(clusterName, temporalInfo);
                                requests.put(temporalInfo, rrdRequest);
                            }
                            rrdRequest.putResource(key, resource);
                            rrdRequest.putPropertyId(propertyInfo.getPropertyId(), propertyId);
                        }
                    }
                }
            }
        }
        return requestMap;
    }

    private java.lang.String getSpec(java.lang.String clusterName, java.util.Set<java.lang.String> clusterSet, java.util.Set<java.lang.String> hostSet, java.util.Set<java.lang.String> metricSet, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String clusters = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.getSetString(clusterSet, -1);
        java.lang.String hosts = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.getSetString(hostSet, -1);
        java.lang.String metrics = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.getSetString(metricSet, -1);
        org.apache.http.client.utils.URIBuilder uriBuilder = new org.apache.http.client.utils.URIBuilder();
        if (configuration.isHttpsEnabled()) {
            uriBuilder.setScheme("https");
        } else {
            uriBuilder.setScheme("http");
        }
        uriBuilder.setHost(hostProvider.getCollectorHostName(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA));
        uriBuilder.setPath("/cgi-bin/rrd.py");
        uriBuilder.setParameter("c", clusters);
        if (hosts.length() > 0) {
            uriBuilder.setParameter("h", hosts);
        }
        if (metrics.length() > 0) {
            uriBuilder.setParameter("m", metrics);
        } else {
            uriBuilder.setParameter("m", ".*");
        }
        if (temporalInfo != null) {
            long startTime = temporalInfo.getStartTime();
            if (startTime != (-1)) {
                uriBuilder.setParameter("s", java.lang.String.valueOf(startTime));
            }
            long endTime = temporalInfo.getEndTime();
            if (endTime != (-1)) {
                uriBuilder.setParameter("e", java.lang.String.valueOf(endTime));
            }
            long step = temporalInfo.getStep();
            if (step != (-1)) {
                uriBuilder.setParameter("r", java.lang.String.valueOf(step));
            }
        } else {
            uriBuilder.setParameter("e", "now");
            uriBuilder.setParameter("pt", "true");
        }
        return uriBuilder.toString();
    }

    private static java.lang.Object getValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric metric, boolean isTemporal) {
        java.lang.Number[][] dataPoints = metric.getDatapoints();
        int length = dataPoints.length;
        if (isTemporal) {
            return length > 0 ? dataPoints : null;
        } else {
            return length > 0 ? dataPoints[length - 1][0] : 0;
        }
    }

    private class RRDRequest {
        private static final int POPULATION_TIME_UPPER_LIMIT = 5;

        private final java.lang.String clusterName;

        private final org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo;

        private final java.util.Map<org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey, java.util.Set<org.apache.ambari.server.controller.spi.Resource>> resources = new java.util.HashMap<>();

        private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> metrics = new java.util.HashMap<>();

        private final java.util.Set<java.lang.String> clusterSet = new java.util.HashSet<>();

        private final java.util.Set<java.lang.String> hostSet = new java.util.HashSet<>();

        private RRDRequest(java.lang.String clusterName, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
            this.clusterName = clusterName;
            this.temporalInfo = temporalInfo;
        }

        public void putResource(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey key, org.apache.ambari.server.controller.spi.Resource resource) {
            clusterSet.add(key.getClusterName());
            hostSet.add(key.getHostName());
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = resources.get(key);
            if (resourceSet == null) {
                resourceSet = new java.util.HashSet<>();
                resources.put(key, resourceSet);
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

        public java.util.Collection<org.apache.ambari.server.controller.spi.Resource> populateResources() throws org.apache.ambari.server.controller.spi.SystemException {
            java.lang.String specWithParams = getSpec(clusterName, clusterSet, hostSet, metrics.keySet(), temporalInfo);
            java.lang.String spec = null;
            java.lang.String params = null;
            java.lang.String[] tokens = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.questionMarkPattern.split(specWithParams, 2);
            try {
                spec = tokens[0];
                params = tokens[1];
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info(e.toString());
            }
            java.io.BufferedReader reader = null;
            try {
                if (!hostProvider.isCollectorHostLive(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info("Ganglia host is not live");
                    return java.util.Collections.emptySet();
                }
                if (!hostProvider.isCollectorComponentLive(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info("Ganglia server component is not live");
                    return java.util.Collections.emptySet();
                }
                reader = new java.io.BufferedReader(new java.io.InputStreamReader(getStreamProvider().readFrom(spec, "POST", params)));
                java.lang.String feedStart = reader.readLine();
                if ((feedStart == null) || feedStart.isEmpty()) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info("Empty feed while getting ganglia metrics for spec => " + spec);
                    return java.util.Collections.emptySet();
                }
                int startTime = convertToNumber(feedStart).intValue();
                java.lang.String dsName = reader.readLine();
                if ((dsName == null) || dsName.isEmpty()) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info(("Feed without body while reading ganglia metrics for spec " + "=> ") + spec);
                    return java.util.Collections.emptySet();
                }
                while (!"[~EOF]".equals(dsName)) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric metric = new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric();
                    java.util.List<org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric> listTemporalMetrics = new java.util.ArrayList<>();
                    metric.setDs_name(dsName);
                    metric.setCluster_name(reader.readLine());
                    metric.setHost_name(reader.readLine());
                    metric.setMetric_name(reader.readLine());
                    java.lang.String timeStr = reader.readLine();
                    java.lang.String stepStr = reader.readLine();
                    if ((((timeStr == null) || timeStr.isEmpty()) || (stepStr == null)) || stepStr.isEmpty()) {
                        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info(("Unexpected end of stream reached while getting ganglia " + "metrics for spec => ") + spec);
                        return java.util.Collections.emptySet();
                    }
                    int time = convertToNumber(timeStr).intValue();
                    int step = convertToNumber(stepStr).intValue();
                    java.lang.String val = reader.readLine();
                    java.lang.String lastVal = null;
                    while ((val != null) && (!"[~EOM]".equals(val))) {
                        if (val.startsWith("[~r]")) {
                            int repeat = java.lang.Integer.parseInt(val.substring(4)) - 1;
                            for (int i = 0; i < repeat; ++i) {
                                if (!"[~n]".equals(lastVal)) {
                                    org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric tm = new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric(lastVal, time);
                                    if (tm.isValid())
                                        listTemporalMetrics.add(tm);

                                }
                                time += step;
                            }
                        } else {
                            if (!"[~n]".equals(val)) {
                                org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric tm = new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric(val, time);
                                if (tm.isValid())
                                    listTemporalMetrics.add(tm);

                            }
                            time += step;
                        }
                        lastVal = val;
                        val = reader.readLine();
                    } 
                    metric.setDatapointsFromList(listTemporalMetrics);
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey key = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey(metric.getHost_name(), metric.getCluster_name());
                    java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = resources.get(key);
                    if (resourceSet != null) {
                        for (org.apache.ambari.server.controller.spi.Resource resource : resourceSet) {
                            populateResource(resource, metric);
                        }
                    }
                    dsName = reader.readLine();
                    if ((dsName == null) || dsName.isEmpty()) {
                        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info(("Unexpected end of stream reached while getting ganglia " + "metrics for spec => ") + spec);
                        return java.util.Collections.emptySet();
                    }
                } 
                java.lang.String feedEnd = reader.readLine();
                if ((feedEnd == null) || feedEnd.isEmpty()) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info(("Error reading end of feed while getting ganglia metrics " + "for spec => ") + spec);
                } else {
                    int endTime = convertToNumber(feedEnd).intValue();
                    int totalTime = endTime - startTime;
                    if (org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.isInfoEnabled() && (totalTime > org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.RRDRequest.POPULATION_TIME_UPPER_LIMIT)) {
                        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.info("Ganglia resource population time: " + totalTime);
                    }
                }
            } catch (java.io.IOException e) {
                if (org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.isErrorEnabled()) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.error("Caught exception getting Ganglia metrics : spec=" + spec);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (java.io.IOException e) {
                        if (org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.isWarnEnabled()) {
                            org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.LOG.warn("Unable to close http input steam : spec=" + spec, e);
                        }
                    }
                }
            }
            return java.util.Collections.emptySet();
        }

        private void populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric gangliaMetric) {
            java.lang.String metric_name = gangliaMetric.getMetric_name();
            java.util.Set<java.lang.String> propertyIdSet = null;
            java.util.List<java.lang.String> parameterList = null;
            if (metric_name != null) {
                propertyIdSet = metrics.get(metric_name);
                parameterList = new java.util.LinkedList<>();
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
            }
            if (propertyIdSet != null) {
                java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metricsMap = getComponentMetrics().get(getComponentName(resource));
                if (metricsMap != null) {
                    for (java.lang.String propertyId : propertyIdSet) {
                        if (propertyId != null) {
                            if (metricsMap.containsKey(propertyId)) {
                                if (containsArguments(propertyId)) {
                                    int i = 1;
                                    for (java.lang.String param : parameterList) {
                                        propertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument(propertyId, "$" + i, param);
                                        ++i;
                                    }
                                }
                                java.lang.Object value = org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.getValue(gangliaMetric, temporalInfo != null);
                                if (value != null) {
                                    resource.setProperty(propertyId, value);
                                }
                            }
                        }
                    }
                }
            }
        }

        private java.lang.Number convertToNumber(java.lang.String s) {
            return s.contains(".") ? java.lang.Double.parseDouble(s) : java.lang.Long.parseLong(s);
        }
    }

    private static class ResourceKey {
        private final java.lang.String hostName;

        private final java.lang.String gangliaClusterName;

        private ResourceKey(java.lang.String hostName, java.lang.String gangliaClusterName) {
            this.hostName = hostName;
            this.gangliaClusterName = gangliaClusterName;
        }

        public java.lang.String getHostName() {
            return hostName;
        }

        public java.lang.String getClusterName() {
            return gangliaClusterName;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey that = ((org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.ResourceKey) (o));
            return (!(gangliaClusterName != null ? !gangliaClusterName.equals(that.gangliaClusterName) : that.gangliaClusterName != null)) && (!(hostName != null ? !hostName.equals(that.hostName) : that.hostName != null));
        }

        @java.lang.Override
        public int hashCode() {
            int result = (hostName != null) ? hostName.hashCode() : 0;
            result = (31 * result) + (gangliaClusterName != null ? gangliaClusterName.hashCode() : 0);
            return result;
        }
    }
}