package org.apache.ambari.server.controller.metrics.timeline;
public class AMSHostComponentPropertyProvider extends org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider {
    public AMSHostComponentPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        super(componentPropertyInfoMap, streamProvider, configuration, cacheProvider, hostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
    }

    @java.lang.Override
    protected java.lang.String getHostName(org.apache.ambari.server.controller.spi.Resource resource) {
        return ((java.lang.String) (resource.getPropertyValue(hostNamePropertyId)));
    }

    @java.lang.Override
    protected java.lang.String getComponentName(org.apache.ambari.server.controller.spi.Resource resource) {
        java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(componentNamePropertyId)));
        return componentName;
    }
}