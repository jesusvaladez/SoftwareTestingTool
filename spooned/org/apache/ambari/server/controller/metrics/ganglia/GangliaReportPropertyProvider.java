package org.apache.ambari.server.controller.metrics.ganglia;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
public class GangliaReportPropertyProvider extends org.apache.ambari.server.controller.metrics.MetricsReportPropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider.class);

    public GangliaReportPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId) {
        super(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId);
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

    private void populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> propertyIds = getPropertyIds();
        if (propertyIds.isEmpty()) {
            return;
        }
        java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
        if (hostProvider.getCollectorHostName(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA) == null) {
            if (org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider.LOG.isWarnEnabled()) {
                org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider.LOG.warn((("Attempting to get metrics but the Ganglia server is unknown. Resource=" + resource) + " : Cluster=") + clusterName);
            }
            return;
        }
        setProperties(resource, clusterName, request, getRequestPropertyIds(request, predicate));
        return;
    }

    private boolean setProperties(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName, org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> ids) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyIdMaps = getPropertyIdMaps(request, ids);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : propertyIdMaps.entrySet()) {
            java.util.Map<java.lang.String, java.lang.String> map = entry.getValue();
            java.lang.String report = entry.getKey();
            java.lang.String spec = getSpec(clusterName, report);
            try {
                java.util.List<org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric> gangliaMetrics = new org.codehaus.jackson.map.ObjectMapper().readValue(streamProvider.readFrom(spec), new org.codehaus.jackson.type.TypeReference<java.util.List<org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric>>() {});
                if (gangliaMetrics != null) {
                    for (org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric gangliaMetric : gangliaMetrics) {
                        java.lang.String propertyId = map.get(gangliaMetric.getMetric_name());
                        if (propertyId != null) {
                            resource.setProperty(propertyId, getValue(gangliaMetric));
                        }
                    }
                }
            } catch (java.io.IOException e) {
                if (org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider.LOG.isErrorEnabled()) {
                    org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider.LOG.error((("Caught exception getting Ganglia metrics : " + e) + " : spec=") + spec);
                }
                return false;
            }
        }
        return true;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getPropertyIdMaps(org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> ids) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyMap = new java.util.HashMap<>();
        for (java.lang.String id : ids) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = getPropertyInfoMap("*", id);
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : propertyInfoMap.entrySet()) {
                java.lang.String propertyId = entry.getKey();
                org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = entry.getValue();
                org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = request.getTemporalInfo(id);
                if ((temporalInfo != null) && propertyInfo.isTemporal()) {
                    java.lang.String propertyName = propertyInfo.getPropertyId();
                    java.lang.String report = null;
                    int dotIndex = propertyName.lastIndexOf('.');
                    if (dotIndex != (-1)) {
                        report = propertyName.substring(0, dotIndex);
                        propertyName = propertyName.substring(dotIndex + 1);
                    }
                    if (report != null) {
                        java.util.Map<java.lang.String, java.lang.String> map = propertyMap.get(report);
                        if (map == null) {
                            map = new java.util.HashMap<>();
                            propertyMap.put(report, map);
                        }
                        map.put(propertyName, propertyId);
                    }
                }
            }
        }
        return propertyMap;
    }

    private java.lang.Object getValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric metric) {
        return metric.getDatapoints();
    }

    protected java.lang.String getSpec(java.lang.String clusterName, java.lang.String report) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (configuration.isHttpsEnabled()) {
            sb.append("https://");
        } else {
            sb.append("http://");
        }
        sb.append(hostProvider.getCollectorHostName(clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)).append("/ganglia/graph.php?g=").append(report).append("&json=1");
        return sb.toString();
    }
}