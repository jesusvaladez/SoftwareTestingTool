package org.apache.ambari.server.controller.metrics.ganglia;
public class GangliaHostComponentPropertyProvider extends org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider {
    public GangliaHostComponentPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        super(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
    }

    @java.lang.Override
    protected java.lang.String getHostName(org.apache.ambari.server.controller.spi.Resource resource) {
        return ((java.lang.String) (resource.getPropertyValue(getHostNamePropertyId())));
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