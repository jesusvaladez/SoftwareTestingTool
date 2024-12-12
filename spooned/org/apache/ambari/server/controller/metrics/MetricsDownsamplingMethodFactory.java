package org.apache.ambari.server.controller.metrics;
public class MetricsDownsamplingMethodFactory {
    private static final org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod perSecondDownsampling = new org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling();

    private static final org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod noDownsampling = new org.apache.ambari.server.controller.metrics.MetricNoDownsampling();

    public static org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod detectDownsamplingMethod(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metricDecl) {
        if (org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethodFactory.mustDownsample(metricDecl)) {
            return org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethodFactory.perSecondDownsampling;
        } else {
            return org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethodFactory.noDownsampling;
        }
    }

    private static boolean mustDownsample(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric) {
        for (java.lang.Long time : metric.getMetricValues().keySet()) {
            if (time > 9999999999L)
                return true;

        }
        return false;
    }
}