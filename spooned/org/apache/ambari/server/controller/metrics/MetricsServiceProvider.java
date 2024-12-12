package org.apache.ambari.server.controller.metrics;
public interface MetricsServiceProvider {
    enum MetricsService {

        GANGLIA,
        TIMELINE_METRICS;}

    org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService getMetricsServiceType();
}