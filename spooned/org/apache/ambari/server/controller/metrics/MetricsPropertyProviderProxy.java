package org.apache.ambari.server.controller.metrics;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS;
public class MetricsPropertyProviderProxy extends org.apache.ambari.server.controller.internal.AbstractPropertyProvider {
    private final org.apache.ambari.server.controller.metrics.MetricsServiceProvider metricsServiceProvider;

    private org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider amsPropertyProvider;

    private org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider gangliaPropertyProvider;

    private org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider;

    private java.lang.String clusterNamePropertyId;

    public MetricsPropertyProviderProxy(org.apache.ambari.server.controller.spi.Resource.InternalType type, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        super(componentPropertyInfoMap);
        this.metricsServiceProvider = serviceProvider;
        this.cacheProvider = cacheProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
        switch (type) {
            case Host :
                createHostPropertyProviders(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId);
                break;
            case HostComponent :
                createHostComponentPropertyProviders(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
                break;
            case Component :
                createComponentPropertyProviders(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, componentNamePropertyId);
                break;
            default :
                break;
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService metricsService = metricsServiceProvider.getMetricsServiceType();
        java.util.Set<java.lang.String> checkedPropertyIds = super.checkPropertyIds(propertyIds);
        if ((metricsService != null) && metricsService.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
            return amsPropertyProvider.checkPropertyIds(checkedPropertyIds);
        } else {
            return checkedPropertyIds;
        }
    }

    private void createHostPropertyProviders(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId) {
        this.amsPropertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, cacheProvider, hostProvider, clusterNamePropertyId, hostNamePropertyId);
        this.gangliaPropertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId);
    }

    private void createHostComponentPropertyProviders(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        this.amsPropertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostComponentPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, cacheProvider, hostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
        this.gangliaPropertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
    }

    private void createComponentPropertyProviders(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String componentNamePropertyId) {
        this.amsPropertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSComponentPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, cacheProvider, hostProvider, clusterNamePropertyId, componentNamePropertyId);
        this.gangliaPropertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaComponentPropertyProvider(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, componentNamePropertyId);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        if (!checkAuthorizationForMetrics(resources, clusterNamePropertyId)) {
            return resources;
        }
        org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService metricsService = metricsServiceProvider.getMetricsServiceType();
        if (metricsService != null) {
            if (metricsService.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)) {
                return gangliaPropertyProvider.populateResources(resources, request, predicate);
            } else if (metricsService.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
                return amsPropertyProvider.populateResources(resources, request, predicate);
            }
        }
        return resources;
    }
}