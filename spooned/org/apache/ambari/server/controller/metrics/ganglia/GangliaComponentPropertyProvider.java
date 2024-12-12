package org.apache.ambari.server.controller.metrics.ganglia;
public class GangliaComponentPropertyProvider extends org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider {
    public GangliaComponentPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String componentNamePropertyId) {
        super(componentMetrics, streamProvider, configuration, hostProvider, clusterNamePropertyId, null, componentNamePropertyId);
    }

    @java.lang.Override
    protected java.lang.String getHostName(org.apache.ambari.server.controller.spi.Resource resource) {
        return "__SummaryInfo__";
    }

    @java.lang.Override
    protected java.lang.String getComponentName(org.apache.ambari.server.controller.spi.Resource resource) {
        return ((java.lang.String) (resource.getPropertyValue(getComponentNamePropertyId())));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getGangliaClusterNames(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName) {
        java.lang.String component = getComponentName(resource);
        return new java.util.HashSet<>(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.GANGLIA_CLUSTER_NAME_MAP.containsKey(component) ? org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider.GANGLIA_CLUSTER_NAME_MAP.get(component) : java.util.Collections.emptyList());
    }
}