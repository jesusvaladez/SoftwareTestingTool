package org.apache.ambari.server.controller.metrics;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS;
public class MetricsReportPropertyProviderProxy extends org.apache.ambari.server.controller.internal.AbstractPropertyProvider {
    private org.apache.ambari.server.controller.metrics.MetricsReportPropertyProvider amsMetricsReportProvider;

    private org.apache.ambari.server.controller.metrics.MetricsReportPropertyProvider gangliaMetricsReportProvider;

    private final org.apache.ambari.server.controller.metrics.MetricsServiceProvider metricsServiceProvider;

    private org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider;

    private java.lang.String clusterNamePropertyId;

    public MetricsReportPropertyProviderProxy(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId) {
        super(componentPropertyInfoMap);
        this.metricsServiceProvider = serviceProvider;
        this.cacheProvider = cacheProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
        createReportPropertyProviders(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId);
    }

    private void createReportPropertyProviders(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId) {
        this.amsMetricsReportProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, cacheProvider, hostProvider, clusterNamePropertyId);
        this.gangliaMetricsReportProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService metricsService = metricsServiceProvider.getMetricsServiceType();
        java.util.Set<java.lang.String> checkedPropertyIds = super.checkPropertyIds(propertyIds);
        if ((metricsService != null) && metricsService.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
            return amsMetricsReportProvider.checkPropertyIds(checkedPropertyIds);
        } else {
            return checkedPropertyIds;
        }
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        if (!checkAuthorizationForMetrics(resources, clusterNamePropertyId)) {
            return resources;
        }
        org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService metricsService = metricsServiceProvider.getMetricsServiceType();
        if (metricsService != null) {
            if (metricsService.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)) {
                return gangliaMetricsReportProvider.populateResources(resources, request, predicate);
            } else if (metricsService.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
                return amsMetricsReportProvider.populateResources(resources, request, predicate);
            }
        }
        return resources;
    }
}