package org.apache.ambari.server.controller.metrics;
public abstract class MetricsReportPropertyProvider extends org.apache.ambari.server.controller.internal.AbstractPropertyProvider {
    protected final org.apache.ambari.server.controller.utilities.StreamProvider streamProvider;

    protected final org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider;

    protected final java.lang.String clusterNamePropertyId;

    protected final org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration;

    protected static final org.apache.ambari.server.controller.metrics.MetricsPaddingMethod DEFAULT_PADDING_METHOD = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS);

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.MetricsReportPropertyProvider.class);

    public MetricsReportPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId) {
        super(componentPropertyInfoMap);
        this.streamProvider = streamProvider;
        this.hostProvider = hostProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
        this.configuration = configuration;
    }

    public static org.apache.ambari.server.controller.metrics.MetricsReportPropertyProviderProxy createInstance(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId) {
        return new org.apache.ambari.server.controller.metrics.MetricsReportPropertyProviderProxy(componentPropertyInfoMap, streamProvider, configuration, cacheProvider, hostProvider, serviceProvider, clusterNamePropertyId);
    }
}