package org.apache.ambari.server.controller.metrics;
import static org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM;
public abstract class MetricsPropertyProvider extends org.apache.ambari.server.controller.internal.AbstractPropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.class);

    protected static final java.util.regex.Pattern questionMarkPattern = java.util.regex.Pattern.compile("\\?");

    protected final org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider;

    protected final org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider;

    protected final java.lang.String clusterNamePropertyId;

    protected final java.lang.String hostNamePropertyId;

    protected final java.lang.String componentNamePropertyId;

    protected final org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration;

    protected org.apache.ambari.server.controller.metrics.MetricsPaddingMethod metricsPaddingMethod;

    private static final org.apache.ambari.server.controller.metrics.MetricsPaddingMethod DEFAULT_PADDING_METHOD = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS);

    protected MetricsPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        super(componentPropertyInfoMap);
        this.streamProvider = streamProvider;
        this.configuration = configuration;
        this.hostProvider = hostProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
        this.hostNamePropertyId = hostNamePropertyId;
        this.componentNamePropertyId = componentNamePropertyId;
    }

    public static org.apache.ambari.server.controller.metrics.MetricsPropertyProviderProxy createInstance(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        if (type.isInternalType()) {
            return new org.apache.ambari.server.controller.metrics.MetricsPropertyProviderProxy(type.getInternalType(), componentPropertyInfoMap, streamProvider, configuration, cacheProvider, hostProvider, serviceProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
        }
        return null;
    }

    protected abstract java.lang.String getHostName(org.apache.ambari.server.controller.spi.Resource resource);

    protected abstract java.lang.String getComponentName(org.apache.ambari.server.controller.spi.Resource resource);

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> ids = getRequestPropertyIds(request, predicate);
        if (ids.isEmpty()) {
            return resources;
        }
        if (!checkAuthorizationForMetrics(resources, clusterNamePropertyId)) {
            return resources;
        }
        metricsPaddingMethod = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.DEFAULT_PADDING_METHOD;
        java.util.Set<java.lang.String> requestPropertyIds = request.getPropertyIds();
        if ((requestPropertyIds != null) && (!requestPropertyIds.isEmpty())) {
            for (java.lang.String propertyId : requestPropertyIds) {
                if (propertyId.startsWith(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM)) {
                    java.lang.String paddingStrategyStr = propertyId.substring(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.ZERO_PADDING_PARAM.length() + 1);
                    metricsPaddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.valueOf(paddingStrategyStr));
                }
            }
        }
        return populateResourcesWithProperties(resources, request, ids);
    }

    protected abstract java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResourcesWithProperties(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, java.util.Set<java.lang.String> propertyIds) throws org.apache.ambari.server.controller.spi.SystemException;

    public static java.lang.String getSetString(java.util.Set<java.lang.String> set, int limit) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if ((limit == (-1)) || (set.size() <= limit)) {
            for (java.lang.String cluster : set) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(cluster);
            }
        }
        return sb.toString();
    }
}