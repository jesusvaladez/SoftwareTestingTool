package org.apache.ambari.server.controller.metrics;
import static org.apache.ambari.server.controller.utilities.PropertyHelper.AGGREGATE_FUNCTION_IDENTIFIERS;
public class MetricsDataTransferMethodFactory {
    private static final java.util.Set<java.lang.String> PERCENTAGE_METRIC;

    static {
        java.util.Set<java.lang.String> percentMetrics = new java.util.HashSet<>();
        percentMetrics.add("cpu_wio");
        percentMetrics.add("cpu_idle");
        percentMetrics.add("cpu_nice");
        percentMetrics.add("cpu_aidle");
        percentMetrics.add("cpu_system");
        percentMetrics.add("cpu_user");
        java.util.Set<java.lang.String> metricsWithAggregateFunctionIds = new java.util.HashSet<>();
        for (java.lang.String metric : percentMetrics) {
            for (java.lang.String aggregateFunctionId : org.apache.ambari.server.controller.utilities.PropertyHelper.AGGREGATE_FUNCTION_IDENTIFIERS) {
                if (!"._sum".equals(aggregateFunctionId)) {
                    metricsWithAggregateFunctionIds.add(metric + aggregateFunctionId);
                }
            }
        }
        percentMetrics.addAll(metricsWithAggregateFunctionIds);
        PERCENTAGE_METRIC = java.util.Collections.unmodifiableSet(percentMetrics);
    }

    private static final org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod percentageAdjustment = new org.apache.ambari.server.controller.metrics.PercentageAdjustmentTransferMethod();

    private static final org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod passThrough = new org.apache.ambari.server.controller.metrics.PassThroughTransferMethod();

    public static org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod detectDataTransferMethod(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metricDecl) {
        if (org.apache.ambari.server.controller.metrics.MetricsDataTransferMethodFactory.PERCENTAGE_METRIC.contains(metricDecl.getMetricName())) {
            return org.apache.ambari.server.controller.metrics.MetricsDataTransferMethodFactory.percentageAdjustment;
        } else {
            return org.apache.ambari.server.controller.metrics.MetricsDataTransferMethodFactory.passThrough;
        }
    }
}