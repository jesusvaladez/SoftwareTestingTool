package org.apache.ambari.server.controller.metrics;
public interface MetricHostProvider {
    java.lang.String getCollectorHostName(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException;

    java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException;

    java.lang.String getCollectorPort(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException;

    boolean isCollectorHostLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException;

    boolean isCollectorComponentLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException;

    default java.util.Optional<java.lang.String> getExternalHostName(java.lang.String clusterName, java.lang.String componentName) {
        return java.util.Optional.empty();
    }

    public boolean isCollectorHostExternal(java.lang.String clusterName);
}